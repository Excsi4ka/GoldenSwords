package com.excsi.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GoldenClaymoreRenderer implements IItemRenderer {
    public final float scale;
    public GoldenClaymoreRenderer(float f){
    scale=f;
    }
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON: {
                this.renderEquippedItem(item, (EntityLivingBase)data[1], true);
                break;
            }
            case EQUIPPED: {
                this.renderEquippedItem(item, (EntityLivingBase)data[1], false);
            }
            case ENTITY: {
            }
            case FIRST_PERSON_MAP: {
            }
        }
    }
    private void renderEquippedItem(final ItemStack a1, final EntityLivingBase a2, final boolean a3) {
        GL11.glPushMatrix();
        float v1 = this.scale;
        if (a3) {
            v1 *= 1.75f;
            GL11.glTranslatef(-0.35f * this.scale, -0.125f * this.scale, 0.0f);
        }
        else {
            v1 *= ((a2 instanceof EntityPlayer) ? 2.0f : 1.75f);
            GL11.glTranslatef(1.0f - v1, -0.125f * this.scale, 0.05f * this.scale);
        }
        GL11.glScalef(v1, v1, v1);
        final IIcon v2 = a1.getItem().getIcon(a1, 0);
        final Tessellator v3 = Tessellator.instance;
        ItemRenderer.renderItemIn2D(v3, v2.getMaxU(), v2.getMinV(), v2.getMinU(), v2.getMaxV(), 255, 255, 0.0625f);
        GL11.glPopMatrix();
    }
}
