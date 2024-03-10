package com.excsi.util;

import com.excsi.entities.ExtendedPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class GoldenSwordsAdminCommands extends CommandBase {
    @Override
    public String getCommandName() {
        return "goldenswords";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/goldenswords <level|shield|cooldown|kazarma>,<get|set|reset|>,<player>,<amount>";
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length==0){
            sender.addChatMessage(new ChatComponentText("/goldenswords <level|shield|cooldown>,<get|set|reset>,<player>,<amount>"));
        }else {
            EntityPlayerMP entityplayer = null;
            if (args[2] != null) {
                entityplayer = getPlayer(sender, args[2]);
            }
            ExtendedPlayer playerExtended = ExtendedPlayer.get(entityplayer);
            if (args[0].equalsIgnoreCase("level")) {
                if (args[1].equalsIgnoreCase("set")) {
                    int lvl = Integer.parseInt(args[3]);
                    if (lvl > 10 ) {
                        sender.addChatMessage(new ChatComponentText("Max level allowed is 10"));
                    } else {
                        playerExtended.setGoldenSwordsLvl(lvl);
                        entityplayer.addChatMessage(new ChatComponentText("Golden Sword level set to: " + lvl));
                    }
                }else if (args[1].equalsIgnoreCase("get")){
                    int curlvl = playerExtended.getGoldenSwordsLvl();
                    sender.addChatMessage(new ChatComponentText(args[2]+"'s Golden Sword level is " + curlvl));
                }
            } else if (args[0].equalsIgnoreCase("shield")) {
                if (args[1].equalsIgnoreCase("set")) {
                    float shield = (float) Integer.parseInt(args[3]);
                    if (shield < 0f || shield > 1000f) {
                        sender.addChatMessage(new ChatComponentText("Invalid amount"));
                    } else {
                        playerExtended.setGoldShieldAmount(shield);
                        entityplayer.addChatMessage(new ChatComponentText("Golden shield durability set to: " + shield));
                    }
                } else if (args[1].equalsIgnoreCase("get")) {
                    float currentshield = playerExtended.getGoldShieldAmount();
                    sender.addChatMessage(new ChatComponentText(args[2] + "'s Current golden shield durability is: " + currentshield));
                }
            } else if(args[0].equalsIgnoreCase("cooldown")) {
                if(args[1].equalsIgnoreCase("reset")) {
                    playerExtended.resetCooldowns();
                    sender.addChatMessage(new ChatComponentText("Cooldowns reset"));
                }
            }
        }
    }
}