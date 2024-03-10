package com.excsi;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class GoldenSwordsPotionBase extends Potion {
    public static ResourceLocation potionstexture = new ResourceLocation("goldenswords", "textures/gui/potions.png");
    public GoldenSwordsPotionBase(int id, boolean b, int c){
    super(id,b,c);
    }
    public Potion setIconIndex(final int par1, final int par2) {
        super.setIconIndex(par1, par2);
        return this;
    }
    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex(){
        Minecraft.getMinecraft().renderEngine.bindTexture(GoldenSwordsPotionBase.potionstexture);
        return super.getStatusIconIndex();
    }
}