package com.excsi.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GoldenSwordKnightRenderer extends RenderBiped {
    public ResourceLocation knightTexture1 = new ResourceLocation("goldenswords","textures/entity/knight1.png");

    public GoldenSwordKnightRenderer(ModelBiped model, float shadow){
    super(model,shadow);
    }
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return knightTexture1;
    }
}