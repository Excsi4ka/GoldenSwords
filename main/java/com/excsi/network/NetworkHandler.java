package com.excsi.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class NetworkHandler {

    public static SimpleNetworkWrapper CHANNEL;

    public static void preInit(){
    CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("goldenswords");
    }
    public static void init(){
        NetworkHandler.CHANNEL.registerMessage((Class)ExtendedPlayerSyncPacket.Handler.class,(Class)ExtendedPlayerSyncPacket.class,0, Side.CLIENT);
        NetworkHandler.CHANNEL.registerMessage((Class)KeyPressPacket.Handler.class,(Class) KeyPressPacket.class,1,Side.SERVER);
    }
    public static void sendToPlayer(IMessage message, EntityPlayer player){
        NetworkHandler.CHANNEL.sendTo(message,(EntityPlayerMP) player);
    }
    public static void sendToServer(IMessage message){
        NetworkHandler.CHANNEL.sendToServer(message);
    }
}