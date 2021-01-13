package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.client.gui.SoulHUDOverlayHandler;
import coffeecatrailway.soulpowered.client.particle.SoulParticle;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.common.command.SoulsCommand;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.registry.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Mod(SoulPoweredMod.MOD_ID)
public class SoulPoweredMod
{
    public static final String MOD_ID = "soulpowered";
    private static final Logger LOGGER = getLogger("");

    public static SoulConfig.Client CLIENT_CONFIG;
    public static SoulConfig.Common COMMON_CONFIG;
    public static SoulConfig.Server SERVER_CONFIG;

    public static Registrate REGISTRATE;

    public static final ItemGroup GROUP_ALL = new ItemGroup(SoulPoweredMod.MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(SoulItems.SOULIUM_SOUL_AMULET.get());
        }
    };
    public static final String KEY_CATEGORY = "key." + MOD_ID + ".category";
    public static final KeyBinding ACTIVATE_CURIO = new KeyBinding("key." + MOD_ID + ".activate_curio", GLFW.GLFW_KEY_H, KEY_CATEGORY);

    public static final ResourceLocation POWERED_ITEM_PROPERTY = getLocation("powered");
    public static final Set<Supplier<Item>> POWERED_ITEM_PROPERTY_SET = new HashSet<>();

    public SoulPoweredMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onParticleFactoryRegister);
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(CuriosIntegration::onInterModComms);

        final Pair<SoulConfig.Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(SoulConfig.Client::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, client.getRight());
        CLIENT_CONFIG = client.getLeft();

        final Pair<SoulConfig.Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(SoulConfig.Common::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, common.getRight());
        COMMON_CONFIG = common.getLeft();

        final Pair<SoulConfig.Server, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(SoulConfig.Server::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, server.getRight());
        SERVER_CONFIG = server.getLeft();
        LOGGER.info("Register configs");

        MinecraftForge.EVENT_BUS.register(this);

        REGISTRATE = Registrate.create(MOD_ID).itemGroup(() -> GROUP_ALL, "Soul Powered")
                .addDataGenerator(ProviderType.ITEM_TAGS, new SoulData.TagItems())
                .addDataGenerator(ProviderType.BLOCK_TAGS, new SoulData.TagBlocks())
                .addDataGenerator(ProviderType.LANG, new SoulData.Lang())
                .addDataGenerator(ProviderType.LOOT, new SoulData.LootTables());

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        SoulBlocks.load();
        SoulItems.load();
        SoulTileEntities.load();
        SoulContainers.load();
        OtherRegistries.load(bus);
        SoulWorldGen.load(bus);
        SoulEntities.load();
        SoulRecipes.load(bus);
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        POWERED_ITEM_PROPERTY_SET.stream().map(Supplier::get).forEach(item -> ItemModelsProperties.registerProperty(item, POWERED_ITEM_PROPERTY, (stack, world, entity) ->
                EnergyUtils.getIfPresent(stack).orElse(EnergyUtils.EMPTY).getEnergyStored() > 0 ? 1f : 0f));
        SoulHUDOverlayHandler.init();
        ClientRegistry.registerKeyBinding(ACTIVATE_CURIO);
    }

    private void onParticleFactoryRegister(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particles.registerFactory(OtherRegistries.SOUL_PARTICLE.get(), SoulParticle.Factory::new);
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(SoulWorldGen.StructurePieces::loadStructureTypes);
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
