package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.tileentity.TileEntityType;
import org.apache.logging.log4j.Logger;

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

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}