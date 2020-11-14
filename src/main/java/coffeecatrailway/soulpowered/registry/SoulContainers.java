package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.client.gui.screen.SoulBoxScreen;
import coffeecatrailway.soulpowered.client.gui.screen.SoulGeneratorScreen;
import coffeecatrailway.soulpowered.common.inventory.container.SoulBoxContainer;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.inventory.container.ContainerType;
import org.apache.logging.log4j.Logger;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 */
public class SoulContainers
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Containers");

    public static final RegistryEntry<ContainerType<SoulGeneratorContainer>> SOUL_GENERATOR = REGISTRATE.container("soul_generator", SoulGeneratorContainer::new, () -> SoulGeneratorScreen::new)
            .register();

    public static final RegistryEntry<ContainerType<SoulBoxContainer>> SOUL_BOX = REGISTRATE.container("soul_box", SoulBoxContainer::new, () -> SoulBoxScreen::new)
            .register();

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}
