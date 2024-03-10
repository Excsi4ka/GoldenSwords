package com.excsi.network;

import com.excsi.entities.ExtendedPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ExtendedPlayerSyncPacket implements IMessage{

    private NBTTagCompound data;
    public ExtendedPlayerSyncPacket(){}
    public ExtendedPlayerSyncPacket(EntityPlayer player){
        data = new NBTTagCompound();
        ExtendedPlayer.get(player).saveNBTData(data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,data);
    }

    public static class Handler implements IMessageHandler<ExtendedPlayerSyncPacket,IMessage>{
        public IMessage onMessage(ExtendedPlayerSyncPacket message, MessageContext ctx) {
            EntityPlayer player;
            if (ctx.side == Side.CLIENT) {
                player = Minecraft.getMinecraft().thePlayer;
                ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
                playerExtended.loadNBTData(message.data);
            }
            return null;
        }
    }
}