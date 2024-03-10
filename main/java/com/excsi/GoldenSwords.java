package com.excsi;

import com.excsi.entities.EntityGoldenSwordKnight;
import com.excsi.entities.GoldenSwordKnightRenderer;
import com.excsi.events.SyncEventHandler;
import com.excsi.events.LevelEventsHandler;
import com.excsi.items.GoldenClaymoreRenderer;
import com.excsi.items.ItemDebugScroll;
import com.excsi.items.ItemGoldenClaymore;
import com.excsi.events.KeyPressEventHandler;
import com.excsi.network.NetworkHandler;
import com.excsi.events.OverlayRenderHandler;
import com.excsi.util.GoldenSwordsAdminCommands;
import com.excsi.util.GoldenSwordsConfig;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;


@Mod(modid = GoldenSwords.MODID, version = "1.0", name = "Golden Swords",dependencies = "required-after:witchery;required-after:Thaumcraft;required-after:Baubles;required-after:thebetweenlands")
public class GoldenSwords
{
    public static final String MODID = "goldenswords";
    public static Item ItemGoldenClaymore;
    public static Potion KnightsBurden;
    public static Item ItemDebugStick;

    @Mod.Instance("goldenswords")
    public static GoldenSwords Instance;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        NetworkHandler.preInit();
        KeyPressEventHandler.register();
        GoldenSwordsConfig.init(event.getSuggestedConfigurationFile());
        ItemGoldenClaymore = new ItemGoldenClaymore();
        GameRegistry.registerItem(ItemGoldenClaymore, "ItemGoldenClaymore");
        ItemDebugStick = new ItemDebugScroll();
        GameRegistry.registerItem(ItemDebugStick,"ItemDebugScroll");
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkHandler.init();
        GoldenSwords.KnightsBurden = new GoldenSwordsPotionBase(GoldenSwordsConfig.potionId,true,16646020).setIconIndex(0,0).setPotionName("goldenswords.potion").func_111184_a(SharedMonsterAttributes.maxHealth,"f6d10ff5-e641-4f93-93a8-fc409d486037",-0.45D,1).func_111184_a(SharedMonsterAttributes.attackDamage,"4471c1f8-f43c-4e47-9eaf-ea27ef83dce4",-0.45D,1);
        MinecraftForge.EVENT_BUS.register(new SyncEventHandler());
        FMLCommonHandler.instance().bus().register(new SyncEventHandler());
        MinecraftForge.EVENT_BUS.register(new LevelEventsHandler());
        FMLCommonHandler.instance().bus().register(new KeyPressEventHandler());
        int entityid = EntityRegistry.findGlobalUniqueEntityId()+1;
        EntityRegistry.registerModEntity(EntityGoldenSwordKnight.class,"goldenswordknight",entityid,GoldenSwords.Instance,32,1,true);
        RenderingRegistry.registerEntityRenderingHandler(EntityGoldenSwordKnight.class,new GoldenSwordKnightRenderer(new ModelBiped(),1f));
        MinecraftForgeClient.registerItemRenderer(GoldenSwords.ItemGoldenClaymore,new GoldenClaymoreRenderer(0.75f));
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new OverlayRenderHandler());
    }
    @EventHandler
    public void serverStart(FMLServerStartingEvent event){
        event.registerServerCommand(new GoldenSwordsAdminCommands());
    }
}