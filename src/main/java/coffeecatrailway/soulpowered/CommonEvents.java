package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.api.item.IEnergyItem;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.client.particle.SoulParticle;
import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageImplBase;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.common.command.SoulsCommand;
import coffeecatrailway.soulpowered.common.item.PoweredSouliumSwordItem;
import coffeecatrailway.soulpowered.common.item.SoulAmuletItem;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import coffeecatrailway.soulpowered.registry.SoulFeatures;
import coffeecatrailway.soulpowered.registry.SoulItems;
import coffeecatrailway.soulpowered.registry.SoulWorldGen;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
@Mod.EventBusSubscriber(modid = SoulMod.MOD_ID)
public class CommonEvents
{
    private static final Logger LOGGER = SoulMod.getLogger("Common Events");

    public static void init(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(SoulWorldGen.StructurePieces::loadStructureTypes);
        SoulsCapability.register();
        SoulMessageHandler.init();
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        SoulsCommand.register(event.getDispatcher());
        LOGGER.debug("Registered command(s)");
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof LivingEntity)
            event.addCapability(SoulsCapability.ID, new SoulsCapability.Provider((LivingEntity) event.getObject()));
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity)
        {
            SoulsCapability.ifPresent(player, handler ->
                    SoulMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncSoulsTotalMessage(player.getId(), handler.getSouls())));
            LOGGER.debug("Capability - Player logged in, syncing souls");
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        if (!event.isWasDeath())
            return;
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        SoulsCapability.ifPresent(original, originalHandler -> SoulsCapability.ifPresent(player, handler -> handler.setSouls(originalHandler.getSouls())));
        LOGGER.debug("Capability - Updated souls on death");
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
        Entity target = event.getTarget();
        PlayerEntity player = event.getPlayer();

        if (player instanceof ServerPlayerEntity)
            SoulsCapability.ifPresent(target, handler -> SoulMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncSoulsTotalMessage(target.getId(), handler.getSouls())));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        PlayerEntity player = event.player;
        SoulsCapability.ifPresent(player, handler -> {
            if (handler.getSouls() == 20 && !player.level.isClientSide())
                SoulParticle.spawnParticles(player.level, player, player.position().add(0d, 2d, 0d), 1, true);
        });
    }

    @SubscribeEvent
    public static void onEntityDie(LivingDeathEvent event)
    {
        if (event.getSource().getEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
            World world = player.level;

            if (!CuriosIntegration.hasCurio(player))
                return;

            for (int slot = 0; slot < CuriosApi.getSlotHelper().getSlotType("necklace").get().getSize(); slot++)
            {
                ItemStack charm = CuriosIntegration.getCurioStack(player, "necklace", slot);
                if (charm.getItem() instanceof SoulAmuletItem)
                {
                    boolean isPowered = EnergyUtils.isPresent(charm);
                    IEnergyStorage energy = EnergyUtils.getIfPresent(charm).orElse(EnergyUtils.EMPTY);
                    if (isPowered && energy.canExtract() && energy.getEnergyStored() < SoulMod.SERVER_CONFIG.soulAmuletPoweredExtract.get())
                        return;

                    LivingEntity entityKilled = event.getEntityLiving();
                    CompoundNBT nbt = charm.getOrCreateTag();

                    if (player.position().closerThan(entityKilled.position(), nbt.getFloat("Range") + .5f) && world.random.nextFloat() < nbt.getFloat("SoulGatheringChance"))
                    {
                        PoweredSouliumSwordItem.gainSouls(player, entityKilled, world, () -> {
                            if (isPowered)
                                energy.extractEnergy(SoulMod.SERVER_CONFIG.soulAmuletPoweredExtract.get(), false);
                            else if (player instanceof ServerPlayerEntity)
                                charm.hurt(1, world.random, (ServerPlayerEntity) player);
                        });
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        ItemStack stack = event.getItemStack();
        PlayerEntity player = event.getPlayer();

        if (!(stack.getItem() instanceof GlassBottleItem))
            return;

        if (!CuriosIntegration.hasCurio(player))
            return;

        SoulsCapability.ifPresent(player, handler -> {
            if (handler.getSouls() > 1)
            {
                if (handler.removeSouls(2, false) && !player.isCreative())
                {
                    player.addItem(new ItemStack(SoulItems.SOUL_BOTTLE.get()));
                    stack.shrink(1);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onAttachItemCaps(AttachCapabilitiesEvent<Item> event)
    {
        if (event.getObject() instanceof IEnergyItem)
        {
            IEnergyItem item = (IEnergyItem) event.getObject();
            event.addCapability(SoulMod.getLocation("energy"), new SoulEnergyStorageImplBase(item.getMaxEnergy(), item.getMaxReceive(), item.getMaxExtract()));
            LOGGER.debug("Capability - Energy item capability updated");
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBiomeLoading(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (SoulMod.COMMON_CONFIG.soulCastleGeneration.get())
        {
            if (event.getCategory() == Biome.Category.NETHER)
                generation.getStructures().add(SoulFeatures.SOUL_CASTLE::get);
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(final WorldEvent.Load event)
    {
        IWorld world = event.getWorld();
        if (world.isClientSide())
            return;

        ServerWorld serverWorld = (ServerWorld) world;
        if ((serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD)) || !serverWorld.dimension().equals(World.NETHER))
            return;

        if (!serverWorld.getChunkSource().getGenerator().getSettings().structureConfig.containsKey(SoulWorldGen.SOUL_CASTLE.get()))
            serverWorld.getChunkSource().getGenerator().getSettings().structureConfig = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(serverWorld.getChunkSource().getGenerator().getSettings().structureConfig)
                    .put(SoulWorldGen.SOUL_CASTLE.get(), DimensionStructuresSettings.DEFAULTS.get(SoulWorldGen.SOUL_CASTLE.get())).build();
    }
}
