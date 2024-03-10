package com.excsi.events;

import com.excsi.network.KeyPressPacket;
import com.excsi.network.NetworkHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyPressEventHandler {
    public static KeyBinding keypress1 = new KeyBinding("goldenswords.key1", Keyboard.KEY_NONE,"goldenswordskeybinds");
    public static KeyBinding keypress2 = new KeyBinding("goldenswords.key2", Keyboard.KEY_NONE,"goldenswordskeybinds");

    public static void register(){
        ClientRegistry.registerKeyBinding(keypress1);
        ClientRegistry.registerKeyBinding(keypress2);
    }
    @SubscribeEvent
    public void keyPress(InputEvent.KeyInputEvent event){
        if(keypress1.isPressed()){
            NetworkHandler.sendToServer(new KeyPressPacket(1));
        }
        if(keypress2.isPressed()){
            NetworkHandler.sendToServer(new KeyPressPacket(2));
        }
    }
}