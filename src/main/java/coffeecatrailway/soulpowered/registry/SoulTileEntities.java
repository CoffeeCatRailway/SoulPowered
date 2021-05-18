package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.tileentity.AlloySmelterTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.CoalGeneratorTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.SoulBoxTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 */
public class SoulTileEntities
{
    private static final Logger LOGGER = SoulMod.getLogger("Tile-Entities");
    protected static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SoulMod.MOD_ID);

    public static final RegistryObject<TileEntityType<SoulGeneratorTileEntity>> SOUL_GENERATOR = TILE_ENTITIES.register("soul_generator", () -> TileEntityType.Builder.of(SoulGeneratorTileEntity::new, SoulBlocks.SOUL_GENERATOR.get()).build(null));

    public static final Map<Tier, RegistryObject<TileEntityType<CoalGeneratorTileEntity>>> COAL_GENERATOR = registerMachineTileEntity("coal_generator", CoalGeneratorTileEntity::new, new Tier[]{Tier.SIMPLE, Tier.NORMAL}, () -> new Block[] {SoulBlocks.SIMPLE_COAL_GENERATOR.get(), SoulBlocks.NORMAL_COAL_GENERATOR.get()});

    public static final Map<Tier, RegistryObject<TileEntityType<SoulBoxTileEntity>>> SOUL_BOX = registerMachineTileEntity("soul_box", SoulBoxTileEntity::new, () -> new Block[] {SoulBlocks.SIMPLE_SOUL_BOX.get(), SoulBlocks.NORMAL_SOUL_BOX.get(), SoulBlocks.SOULIUM_SOUL_BOX.get()});

    public static final Map<Tier, RegistryObject<TileEntityType<AlloySmelterTileEntity>>> ALLOY_SMELTER = registerMachineTileEntity("alloy_smelter", AlloySmelterTileEntity::new, () -> new Block[] {SoulBlocks.SIMPLE_ALLOY_SMELTER.get(), SoulBlocks.NORMAL_ALLOY_SMELTER.get(), SoulBlocks.SOULIUM_ALLOY_SMELTER.get()});

    private static <T extends TileEntity> Map<Tier, RegistryObject<TileEntityType<T>>> registerMachineTileEntity(String id, Function<Tier, T> factory, Supplier<Block[]> blocks)
    {
        return registerMachineTileEntity(id, factory, Tier.values(), blocks);
    }

    private static <T extends TileEntity> Map<Tier, RegistryObject<TileEntityType<T>>> registerMachineTileEntity(String id, Function<Tier, T> factory, Tier[] tiers, Supplier<Block[]> blocks)
    {
        Map<Tier, RegistryObject<TileEntityType<T>>> tiles = new HashMap<>();
        Arrays.stream(tiers).forEach(tier -> {
            RegistryObject<TileEntityType<T>> tile = TILE_ENTITIES.register(id + "_" + tier.getId(), () -> TileEntityType.Builder.of(() -> factory.apply(tier), blocks.get()).build(null));
            tiles.put(tier, tile);
        });
        return tiles;
    }

    public static void load(IEventBus bus)
    {
        TILE_ENTITIES.register(bus);
        LOGGER.debug("Loaded");
    }
}
