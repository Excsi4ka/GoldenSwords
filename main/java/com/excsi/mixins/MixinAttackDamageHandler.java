package com.excsi.mixins;

import com.excsi.entities.ExtendedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import thebetweenlands.entities.mobs.IEntityBL;
import thebetweenlands.event.entity.AttackDamageHandler;

@Mixin(AttackDamageHandler.class)
public class MixinAttackDamageHandler {
    @Inject(method = "onEntityAttacked",at = @At(value = "TAIL"),remap = false,locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onEntityAttacked(LivingHurtEvent event, CallbackInfo ci, EntityLivingBase attackedEntity){
        if(attackedEntity instanceof IEntityBL && event.source.getEntity() instanceof EntityPlayer){
            ExtendedPlayer extendedPlayer = ExtendedPlayer.get((EntityPlayer) event.source.getEntity());
            if(extendedPlayer.getGoldenSwordsLvl()>=6){
                event.ammount*=1.15f;
            }
        }
    }
}
