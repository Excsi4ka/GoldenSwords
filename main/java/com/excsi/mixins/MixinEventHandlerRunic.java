package com.excsi.mixins;

import  com.excsi.entities.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketRunicCharge;
import java.util.HashMap;

@Mixin(EventHandlerRunic.class)
public class MixinEventHandlerRunic {
    @Shadow(remap = false)
    public HashMap<Integer, Integer[]> runicInfo;

    @Shadow(remap = false)
    public HashMap<Integer, Integer> runicCharge;

    @ModifyVariable(method = "livingTick", name = "max",at=@At(value = "INVOKE_ASSIGN",target = "Ljava/util/HashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0),remap = false)
    private int injected(int max) {
        return (max + 10) * 3;
    }

    @Inject(method = "livingTick",at = @At(value = "INVOKE_ASSIGN",target = "Ljava/util/HashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0),remap = false,locals = LocalCapture.CAPTURE_FAILSOFT)
    public void postAssign(LivingEvent.LivingUpdateEvent event, CallbackInfo ci, EntityPlayer player, int max, int charged, int kinetic, int healing, int emergency) {
        ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
        if (playerExtended.getGoldenSwordsLvl() >= 4) {
            if (runicInfo.containsKey(player.getEntityId())) {
                int kcharge = this.runicInfo.get(player.getEntityId())[2];
                this.runicInfo.put(player.getEntityId(), new Integer[]{max, charged, kcharge, healing, emergency});
            }
        }
    }

    @Inject(method = "livingTick",at = @At(value = "INVOKE",target = "Ljava/util/HashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",ordinal = 0),remap = false,locals = LocalCapture.CAPTURE_FAILSOFT)
    public void preAssign(LivingEvent.LivingUpdateEvent event, CallbackInfo ci, EntityPlayer player, int max, int charged, int kinetic, int healing, int emergency) {
        ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
        if (playerExtended.getGoldenSwordsLvl() < 4) {
            if (runicInfo.containsKey(player.getEntityId())) {
                if(runicCharge.get(player.getEntityId())>max){
                    runicCharge.put(player.getEntityId(),max);
                    PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player,(short)max,max),(EntityPlayerMP)player);
                }
            }
        }
    }
}