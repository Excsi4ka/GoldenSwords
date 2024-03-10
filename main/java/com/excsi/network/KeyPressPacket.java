package com.excsi.network;

import com.excsi.entities.ExtendedPlayer;
import com.excsi.util.ModEntityUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class KeyPressPacket implements IMessage {
    public int key;
    public KeyPressPacket(){}
    public KeyPressPacket(int b){
    key=b;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
    key=buf.readInt();
    }
    @Override
    public void toBytes(ByteBuf buf) {
    buf.writeInt(key);
    }
    public static class Handler implements IMessageHandler<KeyPressPacket,IMessage> {
        @Override
        public IMessage onMessage(KeyPressPacket message, MessageContext ctx) {
            if(ctx.side== Side.SERVER){
                EntityPlayer player = ctx.getServerHandler().playerEntity;
                if(message.key==1){
                    ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
                    if (playerExtended.getGoldenSwordsLvl() >= 8) {
                        if(player.worldObj.getTotalWorldTime()-playerExtended.getReflectCooldown()>=2400L) {
                            playerExtended.setReflectTime(player.worldObj.getTotalWorldTime() + 440L);
                            playerExtended.setReflectCooldown(player.worldObj.getTotalWorldTime());
                            List<EntityPlayer> nearplayers = player.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(player.posX - 10, player.posY - 10, player.posZ - 10, player.posX + 10, player.posY + 10, player.posZ + 10));
                            for (EntityPlayer nplayer : nearplayers) {
                                nplayer.addChatComponentMessage(new ChatComponentTranslation("goldenswords.reflect.active",new Object[]{(player.getDisplayName())}).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD).setBold(true)));
                            }
                        }else{
                            player.addChatComponentMessage(new ChatComponentTranslation("goldenswords.oncooldown").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                        }
                    }
                } else if (message.key==2) {
                    ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
                    if(playerExtended.getGoldenSwordsLvl()>=7){
                        if(player.worldObj.getTotalWorldTime()-playerExtended.getAllySpawnCooldown()>=1800L) {
                            playerExtended.setAllySpawnCooldown(player.worldObj.getTotalWorldTime());
                            World w = player.getEntityWorld();
                            ModEntityUtils.summonInFormation(w,player);
                        }else{
                            player.addChatComponentMessage(new ChatComponentTranslation("goldenswords.oncooldown").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                        }
                    }
                }
            }
            return null;
        }
    }
}