package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.client.gui.SoulHUDOverlayHandler;
import coffeecatrailway.soulpowered.client.particle.SoulParticle;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.common.command.SoulsCommand;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.registry.OtherRegistries;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SoulPoweredMod.MOD_ID)
public class SoulPoweredMod
{
    public static final String MOD_ID = "soulpowered";
    private static final Logger LOGGER = getLogger("");

    public static SoulPoweredConfig.Client CLIENT_CONFIG;
    public static SoulPoweredConfig.Server SERVER_CONFIG;

    public static Registrate REGISTRATE;

    public static final ItemGroup GROUP_ALL = new ItemGroup(SoulPoweredMod.MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Items.CHAIN);
        }
    };

    public SoulPoweredMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setupClient);
        modEventBus.addListener(this::setupCommon);

//        final Pair<SoulPoweredConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(SoulPoweredConfig.Client::new);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client.getRight());
//        CLIENT_CONFIG = client.getLeft();

        final Pair<SoulPoweredConfig.Server, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(SoulPoweredConfig.Server::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.getRight());
        SERVER_CONFIG = server.getLeft();
        LOGGER.info("Register configs");

        MinecraftForge.EVENT_BUS.register(this);

        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> GROUP_ALL, "Soul Powered")
//                .addDataGenerator(ProviderType.BLOCK_TAGS, new ExampleTags.Blocks())
//                .addDataGenerator(ProviderType.ITEM_TAGS, new ExampleTags.Items())
                .addDataGenerator(ProviderType.LANG, new SoulData.SoulLang());
//                .addDataGenerator(ProviderType.BLOCKSTATE, new ExampleBlockstates());

        OtherRegistries.load(modEventBus);
    }

    private void setupClient(FMLClientSetupEvent event)
    {
        SoulHUDOverlayHandler.init();

        Minecraft.getInstance().particles.registerFactory(OtherRegistries.SOUL_PARTICLE.get(), SoulParticle.Factory::new);
    }

    private void setupCommon(FMLCommonSetupEvent event)
    {
        SoulsCapability.register();
        SoulMessageHandler.init();
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event)
    {
        SoulsCommand.register(event.getDispatcher());
        LOGGER.debug("Registered command(s)");
    }

    public static ResourceLocation getLocation(String path)
    {
        return new ResourceLocation(SoulPoweredMod.MOD_ID, path);
    }

    public static Logger getLogger(String name)
    {
        return LogManager.getLogger(SoulPoweredMod.MOD_ID + (!StringUtils.isNullOrEmpty(name) ? "-" + name : ""));
    }
}
