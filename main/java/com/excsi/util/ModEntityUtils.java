package com.excsi.util;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import com.excsi.GoldenSwords;
import com.excsi.entities.EntityGoldenSwordKnight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.IRunicArmor;
import thaumcraft.common.lib.events.EventHandlerRunic;

public class ModEntityUtils {
public static boolean isBehindPlayer(EntityPlayer player, Entity entity) {
        Vec3 targetVec = entity.getLookVec();
        Vec3 attackVec = player.getLookVec();
        return Math.signum(targetVec.xCoord) == Math.signum(attackVec.xCoord) && Math.signum(targetVec.zCoord) == Math.signum(attackVec.zCoord);
    }
    public static int getTotalRunicCharge(EntityPlayer player){
        int max = 0;
        for (int a = 0; a < 4; a++) {
            if (player.inventory.armorItemInSlot(a) != null) {
                max += EventHandlerRunic.getFinalCharge(player.inventory.armorItemInSlot(a));
            }
        }
        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(player);
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            if (baubles.getStackInSlot(i) != null && baubles.getStackInSlot(i).getItem() instanceof IRunicArmor) {
                max += EventHandlerRunic.getFinalCharge(baubles.getStackInSlot(i));
            }
        }
        return max;
    }
    public static void summonInFormation(World w,EntityPlayer player){
        for(int i=0;i<4;i++) {
            EntityGoldenSwordKnight knight = new EntityGoldenSwordKnight(w);
            double x = Math.random() * 4 - 2 + player.posX;
            double z = Math.random() * 4 - 2 + player.posZ;
            knight.setPosition(x, player.posY + 0.5, z);
            knight.setOwner(player);
            knight.setCurrentItemOrArmor(0, new ItemStack(GoldenSwords.ItemGoldenClaymore));
            w.spawnEntityInWorld(knight);
        }
    }
}