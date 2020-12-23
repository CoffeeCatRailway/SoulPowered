package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.api.item.IEnergyItem;
import coffeecatrailway.soulpowered.client.particle.SoulParticle;
import coffeecatrailway.soulpowered.common.capability.SoulEnergyStorageImplBase;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.common.item.SoulAmuletItem;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import coffeecatrailway.soulpowered.registry.SoulFeatures;
import coffeecatrailway.soulpowered.registry.SoulItems;
import coffeecatrailway.soulpowered.registry.SoulWorldGen;
import coffeecatrailway.soulpowered.utils.EnergyUtils;
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
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
@Mod.EventBusSubscriber(modid = SoulPoweredMod.MOD_ID)
public class CommonEvents
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Common Events");

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
                    SoulMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncSoulsTotalMessage(player.getEntityId(), handler.getSouls())));
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
            SoulsCapability.ifPresent(target, handler -> SoulMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncSoulsTotalMessage(target.getEntityId(), handler.getSouls())));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        PlayerEntity player = event.player;
        SoulsCapability.ifPresent(player, handler -> {
            if (handler.getSouls() == 20 && !player.world.isRemote())
                SoulParticle.spawnParticles(player.world, player, player.getPositionVec().add(0d, 2d, 0d), 1, true);
        });
    }

    @SubscribeEvent
    public static void onEntityDie(LivingDeathEvent event)
    {
        if (event.getSource().getTrueSource() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            World world = player.world;

            if (!CuriosIntegration.hasCurio(player))
                return;

            for (int slot = 0; slot < CuriosApi.getSlotHelper().getSlotType("necklace").get().getSize(); slot++)
            {
                ItemStack charm = CuriosIntegration.getCurioStack(player, "necklace", slot);
                if (charm.getItem() instanceof SoulAmuletItem)
                {
                    boolean isPowered = EnergyUtils.isPresent(charm);
                    IEnergyStorage energy = EnergyUtils.getIfPresent(charm).orElse(EnergyUtils.EMPTY);
                    if (isPowered && energy.canExtract() && energy.getEnergyStored() < SoulPoweredMod.SERVER_CONFIG.soulAmuletPoweredExtract.get())
                        return;

                    LivingEntity entityKilled = event.getEntityLiving();
                    CompoundNBT nbt = charm.getOrCreateTag();

                    if (player.getPosition().withinDistance(entityKilled.getPosition(), nbt.getFloat("Range") + .5f) && world.rand.nextFloat() < nbt.getFloat("SoulGatheringChance"))
                    {
                        SoulsCapability.ifPresent(player, playerHandler -> {
                            int soulCount = 1;
                            if (entityKilled instanceof PlayerEntity && SoulsCapability.isPresent(entityKilled))
                                soulCount = player.world.rand.nextInt(Math.max(1, SoulsCapability.get(entityKilled).orElse(SoulsCapability.EMPTY).getSouls()) / 2) + 1;

                            playerHandler.addSouls(1, false);
                            if (!world.isRemote)
                            {
                                SoulParticle.spawnParticles(world, player, entityKilled.getPositionVec().add(0d, 1d, 0d), soulCount + player.world.getRandom().nextInt(3) + 1, false);
                                if (isPowered)
                                    energy.extractEnergy(SoulPoweredMod.SERVER_CONFIG.soulAmuletPoweredExtract.get(), false);
                                else if (player instanceof ServerPlayerEntity)
                                    charm.attemptDamageItem(1, world.rand, (ServerPlayerEntity) player);
                                LOGGER.debug("Player killed mob/player, souls given");
                            }
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
                    player.addItemStackToInventory(new ItemStack(SoulItems.SOUL_BOTTLE.get()));
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
            event.addCapability(SoulPoweredMod.getLocation("energy"), new SoulEnergyStorageImplBase(item.getMaxEnergy(), item.getMaxReceive(), item.getMaxExtract()));
            LOGGER.debug("Capability - Energy item capability updated");
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBiomeLoading(BiomeLoadingEvent event)
    {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        if (SoulPoweredMod.COMMON_CONFIG.oreGeneration.get())
        {
            generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, SoulFeatures.COPPER_ORE.get());
        }
        if (SoulPoweredMod.COMMON_CONFIG.soulCastleGeneration.get())
        {
            if (event.getCategory() == Biome.Category.NETHER)
                generation.getStructures().add(SoulFeatures.SOUL_CASTLE::get);
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(final WorldEvent.Load event)
    {
        IWorld world = event.getWorld();
        if (world.isRemote())
            return;

        ServerWorld serverWorld = (ServerWorld) world;
        if ((serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) || !serverWorld.getDimensionKey().equals(World.THE_NETHER))
            return;

        if (!serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_.containsKey(SoulWorldGen.SOUL_CASTLE.get()))
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_)
                    .put(SoulWorldGen.SOUL_CASTLE.get(), DimensionStructuresSettings.field_236191_b_.get(SoulWorldGen.SOUL_CASTLE.get())).build();
    }
}
