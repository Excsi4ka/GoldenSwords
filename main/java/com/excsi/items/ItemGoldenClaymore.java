package com.excsi.items;

import com.excsi.util.MaterialUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemGoldenClaymore extends ItemSword {
    public ItemGoldenClaymore(){
    super(MaterialUtils.SilverGold);
    this.setCreativeTab(CreativeTabs.tabCombat);
    this.setUnlocalizedName("goldenclaymore");
    this.setFull3D();
    }
    @Override
    public void addInformation(ItemStack item,EntityPlayer player, List list, boolean bool){
        list.add(StatCollector.translateToLocal("goldenswords.sword.description1"));
        list.add(StatCollector.translateToLocal("goldenswords.sword.description2"));
    }
    @Override
    public void registerIcons(IIconRegister reg) {
        this.itemIcon = reg.registerIcon("goldenswords:sword");
    }
}