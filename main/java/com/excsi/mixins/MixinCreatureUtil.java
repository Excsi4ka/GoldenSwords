package com.excsi.mixins;

import com.emoniph.witchery.util.CreatureUtil;
import com.excsi.entities.ExtendedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreatureUtil.class)
public class MixinCreatureUtil {
    @Inject(method = "checkForVampireDeath",at=@At("HEAD"),cancellable = true,remap = false)
    private static void checkForVampireDeath(EntityLivingBase creature, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if(source.getEntity() instanceof EntityPlayer){
            ExtendedPlayer playerExtended = ExtendedPlayer.get((EntityPlayer) source.getEntity());
            if(playerExtended.getGoldenSwordsLvl()>=9){
                cir.setReturnValue(true);
            }
        }
    }
}