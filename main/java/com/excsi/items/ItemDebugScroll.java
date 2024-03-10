package com.excsi.items;

import com.excsi.entities.ExtendedPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class ItemDebugScroll extends Item {
    public ItemDebugScroll(){
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setTextureName("goldenswords:scroll");
        this.setUnlocalizedName("debugscroll");
        this.setCreativeTab(CreativeTabs.tabCombat);
    }
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if(Thaumcraft.instance.runicEventHandler.runicCharge.containsKey(player.getEntityId())) {
                ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
                int charge = Thaumcraft.instance.runicEventHandler.runicCharge.get(player.getEntityId());
                int max = Thaumcraft.instance.runicEventHandler.runicInfo.get(player.getEntityId())[0];
                int lvl = extendedPlayer.getGoldenSwordsLvl();
                float shield = extendedPlayer.getGoldShieldAmount();
                player.addChatComponentMessage(new ChatComponentText("Runic charge: " +charge+" Runic max: "+max));
                player.addChatComponentMessage(new ChatComponentText("Golden Sword Level: "+lvl+" Shield Amount: "+shield));
            }
        }
        return stack;
    }
}