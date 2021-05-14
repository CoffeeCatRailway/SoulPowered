package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.client.particle.SoulParticle;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.common.command.SoulsCommand;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
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

    public static final ItemGroup GROUP = new ItemGroup(SoulPoweredMod.MOD_ID)
    {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon()
        {
            return new ItemStack(SoulItems.SOULIUM_SOUL_AMULET.get());
        }
    };
    public static final String KEY_CATEGORY = "key." + MOD_ID + ".category";
    public static final KeyBinding ACTIVATE_CURIO = new KeyBinding("key." + MOD_ID + ".activate_curio", GLFW.GLFW_KEY_H, KEY_CATEGORY);

    public SoulPoweredMod()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientEvents::init);
        bus.addListener(this::onParticleFactoryRegister);
        bus.addListener(CommonEvents::init);
        bus.addListener(CuriosIntegration::onInterModComms);
        bus.addListener(this::onGatherData);

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

        SoulBlocks.load();
        SoulItems.load();
        SoulTileEntities.load();
        SoulContainers.load();
        OtherRegistries.load(bus);
        SoulWorldGen.load(bus);
        SoulEntities.load();
        SoulRecipes.load(bus);
    }

    private void onParticleFactoryRegister(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particleEngine.register(OtherRegistries.SOUL_PARTICLE.get(), SoulParticle.Factory::new);
    }

    private void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        HNCBlockTags blockTags = new HNCBlockTags(generator, existingFileHelper);

        generator.addProvider(new HNCLanguage(generator));
        generator.addProvider(new HNCItemTags(generator, blockTags, existingFileHelper));
        generator.addProvider(blockTags);
        generator.addProvider(new HNCLootTables(generator));
        generator.addProvider(new HNCRecipeGen(generator));
        generator.addProvider(new HNCItemModels(generator));
        generator.addProvider(new HNCBlockStates(generator, existingFileHelper));
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
