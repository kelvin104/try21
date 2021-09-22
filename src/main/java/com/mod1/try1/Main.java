package com.mod1.try1;

import com.mod1.try1.block.ModBlocks;
import com.mod1.try1.client.entity.model.new_mob_1_model;
import com.mod1.try1.client.entity.new_mob_1_renderer;
import com.mod1.try1.effect.ModEffects;
import com.mod1.try1.entity.ModEntities;
import com.mod1.try1.entity.custom.new_mob_1_class;
import com.mod1.try1.item.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fmllegacy.network.FMLMCRegisterPacketHandler;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("try1")
public class Main
{
    public static final String MOD_ID = "try1";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Main() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEffects.MOB_EFFECTS.register(eventBus);
        ModEffects.POTIONS.register(eventBus);
        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::onAttributeCreate);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.NEW_MOB_1.get(), new_mob_1_class.setAttributes().build());
    }



    private void setup(final FMLCommonSetupEvent event)
    {

        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        ModEffects.addPotionRecipes();

        //
        //EntityRenderers.register();
        //
       // Map<EntityType<? extends LivingEntity>, AttributeSupplier> MOD1_ATTRIBUTES = new HashMap<>();
      //  new EntityAttributeCreationEvent(MOD1_ATTRIBUTES);
       //  ParallelDispatchEvent.enqueueWork(() ->{
      //      EntityAttributeCreationEvent.put(ModEntities.NEW_MOB_1.get(), new_mob_1_class.setAttributes().build());
      //  });
        //DeferredWorkQueue.runLater(
        //() -> {

        //
        // }
        // )
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("try1", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(new_mob_1_model.LAYER, new_mob_1_model::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NEW_MOB_1.get(), new_mob_1_renderer::new);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
   // @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
   // public static class RegistryEvents {
  //      @SubscribeEvent
   //     public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
   //         LOGGER.info("HELLO from Register Block");
   //     }
   // }
}
