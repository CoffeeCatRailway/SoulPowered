package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.tileentity.AlloySmelterTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.CoalGeneratorTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.SoulBoxTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 */
public class SoulTileEntities
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("TileEntities");

    public static final RegistryEntry<TileEntityType<SoulGeneratorTileEntity>> SOUL_GENERATOR = REGISTRATE.tileEntity("soul_generator", (NonNullFunction<TileEntityType<SoulGeneratorTileEntity>, SoulGeneratorTileEntity>) SoulGeneratorTileEntity::new)
            .validBlock(SoulBlocks.SOUL_GENERATOR).register();

    public static final Map<Tier, RegistryEntry<TileEntityType<CoalGeneratorTileEntity>>> COAL_GENERATOR = registerMachineTileEntity("coal_generator", CoalGeneratorTileEntity::new, new Tier[]{Tier.SIMPLE, Tier.NORMAL}, SoulBlocks.CHARRED_SIMPLE_COAL_GENERATOR, SoulBlocks.SIMPLE_COAL_GENERATOR, SoulBlocks.NORMAL_COAL_GENERATOR);

    public static final Map<Tier, RegistryEntry<TileEntityType<SoulBoxTileEntity>>> SOUL_BOX = registerMachineTileEntity("soul_box", SoulBoxTileEntity::new, SoulBlocks.CHARRED_SIMPLE_SOUL_BOX, SoulBlocks.SIMPLE_SOUL_BOX, SoulBlocks.NORMAL_SOUL_BOX, SoulBlocks.SOULIUM_SOUL_BOX);

    public static final Map<Tier, RegistryEntry<TileEntityType<AlloySmelterTileEntity>>> ALLOY_SMELTER = registerMachineTileEntity("alloy_smelter", AlloySmelterTileEntity::new, SoulBlocks.CHARRED_SIMPLE_ALLOY_SMELTER, SoulBlocks.SIMPLE_ALLOY_SMELTER, SoulBlocks.NORMAL_ALLOY_SMELTER, SoulBlocks.SOULIUM_ALLOY_SMELTER);

    private static <T extends TileEntity> Map<Tier, RegistryEntry<TileEntityType<T>>> registerMachineTileEntity(String id, NonNullBiFunction<TileEntityType<T>, Tier, T> factory, NonNullSupplier<? extends Block>... blocks)
    {
        return registerMachineTileEntity(id, factory, Tier.values(), blocks);
    }

    private static <T extends TileEntity> Map<Tier, RegistryEntry<TileEntityType<T>>> registerMachineTileEntity(String id, NonNullBiFunction<TileEntityType<T>, Tier, T> factory, Tier[] tiers, NonNullSupplier<? extends Block>... blocks)
    {
        Map<Tier, RegistryEntry<TileEntityType<T>>> tiles = new HashMap<>();
        Arrays.stream(tiers).forEach(tier -> {
            RegistryEntry<TileEntityType<T>> tile = REGISTRATE.<T>tileEntity(id + "_" + tier.getId(), type -> factory.apply(type, tier)).validBlocks(blocks).register();
            tiles.put(tier, tile);
        });
        return tiles;
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}
