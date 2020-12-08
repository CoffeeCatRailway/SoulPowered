package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.client.entity.SoulShieldRenderer;
import coffeecatrailway.soulpowered.common.entity.SoulShieldEntity;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import org.apache.logging.log4j.Logger;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 5/12/2020
 */
public class SoulEntities
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Entities");

    public static final RegistryEntry<EntityType<SoulShieldEntity>> SOUL_SHIELD = REGISTRATE.<SoulShieldEntity>entity("soul_shield", SoulShieldEntity::new, EntityClassification.MISC)
            .properties(prop -> prop.size(1f, 1f).setShouldReceiveVelocityUpdates(false)).defaultLang().loot(NonNullBiConsumer.noop())
            .renderer(() -> manager -> new SoulShieldRenderer(manager, true)).register();

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}
