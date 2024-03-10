package com.excsi.events;

import com.excsi.GoldenSwords;
import com.excsi.entities.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.EntityEvent;
import java.util.UUID;

public class SyncEventHandler {
    @SubscribeEvent
    public void onEntityConstruct(final EntityEvent.EntityConstructing event){
        if (event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer)event.entity) == null) {
            ExtendedPlayer.register((EntityPlayer) event.entity);
        }
    }
    @SubscribeEvent
    public void onClonePlayer(final net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        EntityPlayer player = event.entityPlayer;
        if (!event.wasDeath) {
            return;
        }
        NBTTagCompound compound = new NBTTagCompound();
        ExtendedPlayer original = ExtendedPlayer.get(event.original);
        original.saveNBTData(compound);
        ExtendedPlayer clone = ExtendedPlayer.get(event.entityPlayer);
        clone.loadNBTData(compound);
        ExtendedPlayer playerExtended = ExtendedPlayer.get(event.entityPlayer);
        AttributeModifier health = new AttributeModifier(UUID.fromString("19519bac-8b89-4292-a56f-95be05597d4e"), "goldenswordhealth", (double) playerExtended.GoldenSwordsLvl * 4, 0);
        event.entityPlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(health);
        AttributeModifier speed = new AttributeModifier(UUID.fromString("8361fa0d-f1ef-46d4-b8c3-fa633c0e5007"), "goldenswordspeed", (double) playerExtended.GoldenSwordsLvl * 0.05, 1);
        event.entityPlayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(speed);
    }
    @SubscribeEvent
    public void onDimensionChange(final PlayerEvent.PlayerChangedDimensionEvent event){
        if (!event.player.worldObj.isRemote){
            ExtendedPlayer playerExtended = ExtendedPlayer.get(event.player);
            playerExtended.sync();
        }
    }
   @SubscribeEvent
   public void onLogin(final PlayerEvent.PlayerLoggedInEvent event){
        ExtendedPlayer playerExtended = ExtendedPlayer.get(event.player);
        playerExtended.sync();
    }
    @SubscribeEvent
    public void onRespawn(final PlayerEvent.PlayerRespawnEvent event){
    ExtendedPlayer playerExtended = ExtendedPlayer.get(event.player);
    playerExtended.setIncurablePotionData(0,0,0);
    playerExtended.sync();
    }
    @SubscribeEvent
    public void playerTickEvent(final TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER && !event.player.worldObj.isRemote) {
            ExtendedPlayer playerExtended = ExtendedPlayer.get(event.player);
            if (event.phase == TickEvent.Phase.START) {
                if(event.player.isPotionActive(GoldenSwords.KnightsBurden)){
                    PotionEffect knightsburden = event.player.getActivePotionEffect(GoldenSwords.KnightsBurden);
                    playerExtended.setIncurablePotionData(knightsburden.getPotionID(),knightsburden.getDuration(),knightsburden.getAmplifier());
                    if(knightsburden.getDuration()<20){
                        playerExtended.setIncurablePotionData(0,0,0);
                    }
                }
            }else if (event.phase == TickEvent.Phase.END) {
                if(!event.player.isPotionActive(GoldenSwords.KnightsBurden)) {
                    int[] potiondata = playerExtended.getIncurablePotionData();
                    if(potiondata[0] != 0) {
                        PotionEffect restoreeffect = new PotionEffect(potiondata[0], potiondata[1], potiondata[2]);
                        event.player.addPotionEffect(restoreeffect);
                    }
                }
            }
        }
    }
}