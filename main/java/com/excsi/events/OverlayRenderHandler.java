package com.excsi.events;

import com.excsi.entities.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
@SideOnly(Side.CLIENT)
public class OverlayRenderHandler extends Gui {
private final Minecraft mc;
private static final ResourceLocation icons = new ResourceLocation("goldenswords","textures/gui/goldenswordicons.png");
public OverlayRenderHandler() {
mc = Minecraft.getMinecraft();
}
@SubscribeEvent
public void onRenderGui(RenderGameOverlayEvent.Post event) {
    if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.HOTBAR) {
        return;
    }
    EntityPlayer player = this.mc.thePlayer;
    ExtendedPlayer playerExtended = ExtendedPlayer.get(player);
    if (playerExtended == null || playerExtended.getGoldenSwordsLvl()==0) {
        return;
    }
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int xPos = (scaledresolution.getScaledWidth() / 2) - 91;
        int yPos = scaledresolution.getScaledHeight();
        this.mc.getTextureManager().bindTexture(icons);
        int lvl = playerExtended.getGoldenSwordsLvl();
        long reflectcooldown = playerExtended.getReflectCooldown();
        long respawncooldown = playerExtended.getRespawnCooldown();
        long allyspawncooldown = playerExtended.getAllySpawnCooldown();
        float shield = playerExtended.getGoldShieldAmount();
        if(lvl>=7 && player.worldObj.getTotalWorldTime() - allyspawncooldown >= 1800L) {
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPos - 17, yPos - 18, 64, 9, 16, 16);
            GL11.glPopMatrix();
        }else if(lvl>=7 && player.worldObj.getTotalWorldTime() - allyspawncooldown < 1800L) {
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPos - 17, yPos - 18, 80, 9, 16, 16);
            GL11.glPopMatrix();
        }
        if (player.worldObj.getTotalWorldTime() - reflectcooldown >= 2400L && lvl>=8) {
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPos - 37, yPos - 18, 0, 9, 16, 16);
            GL11.glPopMatrix();
        } else if(player.worldObj.getTotalWorldTime() - reflectcooldown < 2400L && lvl>=8) {
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPos - 37, yPos - 18, 16, 9, 16, 16);
            GL11.glPopMatrix();
        }
        if(lvl==10 && player.worldObj.getTotalWorldTime() - respawncooldown >= 36000L){
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPos - 53, yPos - 18, 32, 9, 16, 16);
            GL11.glPopMatrix();
        } else if(lvl==10 && player.worldObj.getTotalWorldTime() - respawncooldown < 36000L){
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPos - 53, yPos - 18, 48, 9, 16, 16);
            GL11.glPopMatrix();
        }
        if (shield > 0 && !player.capabilities.isCreativeMode) {
            int fullShields = 0;
            int halfShields = 0;
            int healthoffset;
            if (shield % 100 != 0.0) {
                float individualshield = shield % 100;
                float remainingshield = (shield - individualshield) / 100;
                fullShields = (int) remainingshield;
                halfShields = 1;
            }
            if (shield % 100 == 0.0) {
                fullShields = (int) shield / 100;
                halfShields = 0;
            }
            if ((player.getMaxHealth() + player.getAbsorptionAmount()) % 20 != 0.0) {
                healthoffset = (int) (player.getMaxHealth() / 20) + 1;
            } else {
                healthoffset = (int) (player.getMaxHealth() + player.getAbsorptionAmount()) / 20;
            }
            yPos -= (healthoffset * 10);
            yPos += (healthoffset > 2) ? (healthoffset - 3) * 4 + 2 : 0;
            yPos += (healthoffset > 4) ? (healthoffset - 5) * 4 + 2 : 0;
            yPos += (healthoffset > 6) ? (healthoffset - 7) * 4 + 2 : 0;
            yPos += (healthoffset > 8) ? (healthoffset - 9) * 4 + 2 : 0;
            int location = 0;
            for (int i = 1; i <= fullShields; i++) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                drawTexturedModalRect(xPos + location, yPos - 39, 0, 0, 9, 9);
                GL11.glPopMatrix();
                location += 8;
            }
            if (halfShields > 0) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                drawTexturedModalRect(xPos + location, yPos - 39, 9, 0, 9, 9);
                GL11.glPopMatrix();
            }
        }
    }
}