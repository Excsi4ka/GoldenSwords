package com.excsi.events;

import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.excsi.GoldenSwords;
import com.excsi.entities.EntityGoldenSwordKnight;
import com.excsi.entities.ExtendedPlayer;
import com.excsi.util.GoldenSwordsConfig;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketRunicCharge;

import java.util.ArrayList;
import java.util.Collection;

public class LevelEventsHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onReflectDamage(final LivingHurtEvent event) {
        if (event.entity instanceof EntityPlayer && !event.isCanceled() && event.source.getEntity() != null) {
            EntityPlayer player = (EntityPlayer) event.entity;
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            long time = playerExtended.getReflectTime();
            if (time > 0L && time - player.worldObj.getTotalWorldTime() <= 400L) {
                if (!event.source.isUnblockable() && !event.source.isDamageAbsolute() && !event.source.isMagicDamage()) {
                    float reflectamount = event.ammount * 1.3f;
                    event.source.getEntity().attackEntityFrom(event.source, reflectamount);
                    event.ammount *= 0.01f;
                } else if (!event.source.isUnblockable() && !event.source.isDamageAbsolute() && event.source.isMagicDamage()) {
                    float reflectamount = event.ammount * 0.1f;
                    event.source.getEntity().attackEntityFrom(event.source, reflectamount);
                    event.ammount *= 0.6f;
                } else if (!event.source.isUnblockable() && event.source.isDamageAbsolute() && !event.source.isMagicDamage()) {
                    float reflectamount = event.ammount;
                    event.source.getEntity().attackEntityFrom(event.source, reflectamount);
                    event.ammount *= 0.05f;
                } else {
                    float reflectamount = event.ammount * 0.25f;
                    event.source.getEntity().attackEntityFrom(event.source, reflectamount);
                    event.ammount *= 0.5f;
                }
            }
        }
    }
    @SubscribeEvent
    public void onDamageEvent(final LivingHurtEvent event) {
        if (event.source.getEntity() instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            EntityLivingBase entity = (EntityLivingBase) event.entity;
            if (playerExtended.getGoldenSwordsLvl() > 0) {
                float boostamount = 1 + playerExtended.getGoldenSwordsLvl() * 0.08f;
                event.ammount *= boostamount;
            }
            if(CreatureUtil.isUndead(entity) && !(entity instanceof IBossDisplayData) && !(entity instanceof EntityPlayer) ){
                if(playerExtended.getGoldenSwordsLvl()>=2 && entity.getMaxHealth()<=250) {
                    EntityUtil.instantDeath(entity,player);
                }
            }
        }
        if (event.entity instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer) event.entity;
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            if (playerExtended.getGoldenSwordsLvl() > 0) {
                float newamount = 1 - playerExtended.getGoldenSwordsLvl() * 0.05f;
                PotionEffect checkeffect = player.getActivePotionEffect(GoldenSwords.KnightsBurden);
                if (checkeffect != null) {
                    newamount += 0.95 * (checkeffect.getAmplifier() + 1);
                }
                float testlimit = event.ammount *= newamount;
                event.ammount *= newamount;
                //prevents player breaking
                if (testlimit > Float.MAX_VALUE) {
                    event.ammount = Float.MAX_VALUE;
                }
            }
        }
        //peaceful mob damage reduction
        if (event.entity.isCreatureType(EnumCreatureType.creature, false) && !event.isCanceled()) {
            if (event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer) {
                ExtendedPlayer playerExtended = ExtendedPlayer.get((EntityPlayer) event.source.getEntity());
                if (playerExtended.getGoldenSwordsLvl() > 0) {
                    event.ammount *= 0.1f;
                }
            }
        }
        //peaceful player damage reduction
        if (event.entity instanceof EntityPlayer && event.source.getEntity() instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            if (playerExtended.getGoldenSwordsLvl() > 0 && event.entity!= event.source.getEntity()) {
                if (!CreatureUtil.isVampire(event.entity) && !CreatureUtil.isWerewolf(event.entity,true)) {
                    event.ammount *= 0.25f;
                }
            }
        }
        //runic shield regen
        if(event.entity instanceof EntityPlayer && !event.isCanceled() && !event.entity.worldObj.isRemote){
            EntityPlayer player = (EntityPlayer)event.entity;
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            if(playerExtended.getGoldenSwordsLvl()>=4 && player.ticksExisted > 40) {
                if(Thaumcraft.instance.runicEventHandler.runicInfo.containsKey(player.getEntityId()) && Math.random()<=0.1) {
                    int max = Thaumcraft.instance.runicEventHandler.runicInfo.get(player.getEntityId())[0];
                    if(max>0) {
                        int currentcharge = Thaumcraft.instance.runicEventHandler.runicCharge.get(player.getEntityId());
                        int runicregen = Math.min((int) (max * 0.45) + currentcharge, max);
                        Thaumcraft.instance.runicEventHandler.runicCharge.put(player.getEntityId(), runicregen);
                        PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short) runicregen, max), (EntityPlayerMP) player);
                        player.worldObj.playSoundAtEntity(player, "thaumcraft:runicShieldCharge", 1f, 1f);
                    }
                }
            }
        }
        //Unblockable "damage" against goldenswords
        if(event.entity instanceof EntityPlayer && event.source.getEntity() instanceof EntityPlayer && !event.isCanceled()){
            EntityPlayer player = (EntityPlayer) event.entity;
            EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
            ExtendedPlayer extendedPlayer = ExtendedPlayer.get((EntityPlayer) event.entity);
            if(extendedPlayer.getGoldenSwordsLvl()>0){
                if(CreatureUtil.isVampire(attacker) || CreatureUtil.isWerewolf(attacker,false)) {
                    double health = player.getMaxHealth();
                    double healthdamage = Math.min(health * 0.45, event.ammount * 0.25);
                    if(health-healthdamage>0) {
                        player.setHealth((float) (player.getHealth() - healthdamage));
                    }else{
                        EntityUtil.instantDeath(player,attacker);
                    }
                }
            }
        }
        //Entity Knight's protection
        if(event.entity instanceof EntityGoldenSwordKnight && !event.isCanceled()){
            event.ammount*=0.55f;
        }
    }
    @SubscribeEvent
    public void onEatingGoldenApple(final PlayerUseItemEvent.Finish event) {
        ExtendedPlayer playerExtended = ExtendedPlayer.get(event.entityPlayer);
        Item item = event.item.getItem();
        if (item instanceof net.minecraft.item.ItemAppleGold && playerExtended.getGoldenSwordsLvl() >= 5) {
            int meta = event.item.getItemDamage();
            if (!event.entityPlayer.worldObj.isRemote) {
                if (meta == 1) {
                    playerExtended.addGoldShieldAmount(100f);
                    ChatUtil.sendTranslated(EnumChatFormatting.GOLD,event.entityPlayer,"goldenswords.shield.set",new Object[]{Float.valueOf(playerExtended.getGoldShieldAmount()).toString()});
                }
                if (meta == 0) {
                    playerExtended.addGoldShieldAmount(50f);
                    ChatUtil.sendTranslated(EnumChatFormatting.GOLD,event.entityPlayer,"goldenswords.shield.set",new Object[]{Float.valueOf(playerExtended.getGoldShieldAmount()).toString()});
                }
            }
        }
    }
    @SubscribeEvent
    public void onBlockBreak(final PlayerEvent.BreakSpeed event) {
        ExtendedPlayer playerExtended = ExtendedPlayer.get(event.entityPlayer);
        if (playerExtended.getGoldenSwordsLvl() > 0) {
            event.newSpeed = event.originalSpeed * (1 + playerExtended.getGoldenSwordsLvl() * 0.05f);
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void shieldAbsorptionEvent(final LivingHurtEvent event) {
        if (event.entity instanceof EntityPlayer && !event.isCanceled()) {
            ExtendedPlayer playerExtended = ExtendedPlayer.get((EntityPlayer) event.entity);
            float shield = playerExtended.getGoldShieldAmount();
            float individualshield = 0;
            float remainingshield = 0;
            if (shield > 0) {
                if (shield % 100 == 0.0) {
                    individualshield = (shield / 100 - (shield / 100 - 1)) * 100f;
                    remainingshield = (shield / 100 - 1) * 100f;
                } else {
                    individualshield = shield % 100;
                    remainingshield = shield - individualshield;
                }
                if (event.ammount >= individualshield) {
                    event.setCanceled(true);
                    individualshield = 0f;
                    playerExtended.setGoldShieldAmount(remainingshield + individualshield);
                }
                if (event.ammount < individualshield) {
                    event.setCanceled(true);
                    individualshield -= event.ammount;
                    playerExtended.setGoldShieldAmount(remainingshield + individualshield);
                }
            }
        }
    }
    @SubscribeEvent
    public void livingUpdateEvent(final LivingEvent.LivingUpdateEvent event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            if (playerExtended.getGoldenSwordsLvl() > 0) {
                PotionEffect potion = player.getActivePotionEffect(Potion.resistance);
                if (potion != null) {
                    player.removePotionEffect(potion.getPotionID());
                }
            }
            if (!player.worldObj.isRemote) {
                long reflectcd = playerExtended.getReflectTime();
                if (player.worldObj.getTotalWorldTime() >= reflectcd && reflectcd != 0L) {
                    playerExtended.setReflectTime(0L);
                }
                long respawninvulcd = playerExtended.getRespawnInvulnerability();
                if (player.worldObj.getTotalWorldTime() >= respawninvulcd && respawninvulcd != 0L) {
                    playerExtended.setRespawnInvulnerability(0L);
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDie(final LivingDeathEvent event) {
    //golden sword respawn
        if (event.entity instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (!player.worldObj.isRemote) {
                ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
                if (playerExtended.getGoldenSwordsLvl() == 10 && player.worldObj.getTotalWorldTime() - playerExtended.getRespawnCooldown() >= 36000L) {
                    event.setCanceled(true);
                    player.setHealth(player.getMaxHealth());
                    player.worldObj.playSoundAtEntity(player, "goldenswords:goldensword.respawn", 1f, 1f);
                    playerExtended.setRespawnCooldown(player.worldObj.getTotalWorldTime());
                    playerExtended.setRespawnInvulnerability(player.worldObj.getTotalWorldTime() + 160L);
                    Collection<PotionEffect> collection = player.getActivePotionEffects();
                    for (PotionEffect p : collection) {
                        int id = p.getPotionID();
                        //boolean isBad = ReflectionHelper.getPrivateValue(Potion.class, Potion.potionTypes[id], new String[]{"isBadEffect","field_76418_K"});
                        boolean isBad = Potion.potionTypes[id].isBadEffect();
                        if (!isBad) {
                            int oldamplifier = p.getAmplifier();
                            int newamplifier = (oldamplifier < 7) ? oldamplifier + 1 : oldamplifier;
                            PotionEffect updatedeffect = new PotionEffect(id, p.getDuration(), newamplifier);
                            player.addPotionEffect(updatedeffect);
                        }
                    }
                }
            }
        }
        //peaceful player kill effects
        if (event.entity instanceof EntityPlayer && event.source.getEntity() instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            if (playerExtended.getGoldenSwordsLvl() > 0 && event.entity!=event.source.getEntity() ) {
                if (!CreatureUtil.isVampire(event.entity) && !CreatureUtil.isWerewolf(event.entity,true)) {
                    Collection<PotionEffect> effects = player.getActivePotionEffects();
                    ArrayList<PotionEffect> badPotions = new ArrayList<PotionEffect>();
                    for (PotionEffect potionEffect : effects) {
                        int id = potionEffect.getPotionID();
                        boolean isBad = Potion.potionTypes[id].isBadEffect();
                        if (!isBad) {
                            badPotions.add(potionEffect);
                        }
                    }
                    PotionEffect knightsburden = new PotionEffect(GoldenSwordsConfig.potionId, 18000, 1);
                    player.addPotionEffect(knightsburden);
                    for(PotionEffect effect : badPotions){
                        int amp = effect.getAmplifier();
                        int newamplifier = amp - 1;
                        int id = effect.getPotionID();
                        if(newamplifier<0){
                            player.removePotionEffect(id);
                        }else {
                            PotionEffect updatedeffect = new PotionEffect(id, effect.getDuration(), newamplifier);
                            player.removePotionEffect(effect.getPotionID());
                            player.addPotionEffect(updatedeffect);
                        }
                    }
                }
            }
        }
        //peaceful mob kill debuff
        if (event.entity.isCreatureType(EnumCreatureType.creature, false) && !event.isCanceled() && event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
            if (attacker != null) {
                ExtendedPlayer playerExtended = ExtendedPlayer.get(attacker);
                if (playerExtended.getGoldenSwordsLvl() > 0) {
                    PotionEffect knightsburden = new PotionEffect(GoldenSwordsConfig.potionId, 1200, 0);
                    attacker.addPotionEffect(knightsburden);
                }
            }
        }
    }
    //fix instant health potion killing player while debuff is active
    @SubscribeEvent
    public void onHealEvent(final LivingHealEvent event){
        if(event.entity instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer) event.entity;
            PotionEffect effect = player.getActivePotionEffect(GoldenSwords.KnightsBurden);
            if(effect!=null){
                double health = player.getMaxHealth();
                float newamount = (health<event.amount)?(float)health:event.amount;
                event.amount=newamount;
            }
        }
    }
    //respawn invulnerability
    @SubscribeEvent
    public void onAttackEvent(final LivingAttackEvent event) {
        if (event.entity instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer)event.entity;
            ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
            if(playerExtended.getRespawnInvulnerability()>0L){
                event.setCanceled(true);
            }
        }
    }
}