package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.client.gui.screen.AlloySmelterScreen;
import coffeecatrailway.soulpowered.client.gui.screen.CoalGeneratorScreen;
import coffeecatrailway.soulpowered.client.gui.screen.SoulBoxScreen;
import coffeecatrailway.soulpowered.client.gui.screen.SoulGeneratorScreen;
import coffeecatrailway.soulpowered.common.inventory.container.AlloySmelterContainer;
import coffeecatrailway.soulpowered.common.inventory.container.CoalGeneratorContainer;
import coffeecatrailway.soulpowered.common.inventory.container.SoulBoxContainer;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import com.tterrag.registrate.builders.ContainerBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 */
public class SoulContainers
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Containers");

    public static final RegistryEntry<ContainerType<SoulGeneratorContainer>> SOUL_GENERATOR = REGISTRATE.container("soul_generator", (ContainerBuilder.ContainerFactory<SoulGeneratorContainer>) SoulGeneratorContainer::new, () -> SoulGeneratorScreen::new).register();

    public static final Map<Tier, RegistryEntry<ContainerType<CoalGeneratorContainer>>> COAL_GENERATOR = registerMachineContainer("coal_generator", CoalGeneratorContainer::new, () -> CoalGeneratorScreen::new, new Tier[]{Tier.SIMPLE, Tier.NORMAL});

    public static final Map<Tier, RegistryEntry<ContainerType<SoulBoxContainer>>> SOUL_BOX = registerMachineContainer("soul_box", SoulBoxContainer::new, () -> SoulBoxScreen::new);

    public static final Map<Tier, RegistryEntry<ContainerType<AlloySmelterContainer>>> ALLOY_SMELTER = registerMachineContainer("alloy_smelter", AlloySmelterContainer::new, () -> AlloySmelterScreen::new);

    private static <T extends Container, SC extends Screen & IHasContainer<T>> Map<Tier, RegistryEntry<ContainerType<T>>> registerMachineContainer(String name, TieredContainerFactory<T> factory, NonNullSupplier<ContainerBuilder.ScreenFactory<T, SC>> screenFactory)
    {
        return registerMachineContainer(name, factory, screenFactory, Tier.values());
    }

    private static <T extends Container, SC extends Screen & IHasContainer<T>> Map<Tier, RegistryEntry<ContainerType<T>>> registerMachineContainer(String name, TieredContainerFactory<T> factory, NonNullSupplier<ContainerBuilder.ScreenFactory<T, SC>> screenFactory, Tier[] tiers)
    {
        Map<Tier, RegistryEntry<ContainerType<T>>> containers = new HashMap<>();
        Arrays.stream(tiers).forEach(tier -> {
            RegistryEntry<ContainerType<T>> container = REGISTRATE.container(name + "_" + tier.getId(), (type, id, inventory) -> factory.create(type, id, inventory, tier), screenFactory).register();
            containers.put(tier, container);
        });
        return containers;
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }

    interface TieredContainerFactory<T extends Container>
    {
        T create(ContainerType<T> type, int id, PlayerInventory inventory, Tier tier);
    }
}
