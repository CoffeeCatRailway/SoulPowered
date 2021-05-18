package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.client.gui.screen.AlloySmelterScreen;
import coffeecatrailway.soulpowered.client.gui.screen.CoalGeneratorScreen;
import coffeecatrailway.soulpowered.client.gui.screen.SoulBoxScreen;
import coffeecatrailway.soulpowered.common.inventory.container.AlloySmelterContainer;
import coffeecatrailway.soulpowered.common.inventory.container.CoalGeneratorContainer;
import coffeecatrailway.soulpowered.common.inventory.container.SoulBoxContainer;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 12/11/2020
 */
public class SoulContainers
{
    private static final Logger LOGGER = SoulMod.getLogger("Containers");
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SoulMod.MOD_ID);
    public static final Map<Supplier<ContainerType>, ScreenManager.IScreenFactory<?, ?>> SCREENS = new HashMap<>();

    public static final RegistryObject<ContainerType<SoulGeneratorContainer>> SOUL_GENERATOR = CONTAINERS.register("soul_generator", () -> new ContainerType<>(SoulGeneratorContainer::new));

    public static final Map<Tier, RegistryObject<ContainerType<CoalGeneratorContainer>>> COAL_GENERATOR = registerMachineContainer("coal_generator", CoalGeneratorContainer::new, () -> CoalGeneratorScreen::new, new Tier[]{Tier.SIMPLE, Tier.NORMAL});

    public static final Map<Tier, RegistryObject<ContainerType<SoulBoxContainer>>> SOUL_BOX = registerMachineContainer("soul_box", SoulBoxContainer::new, () -> SoulBoxScreen::new);

    public static final Map<Tier, RegistryObject<ContainerType<AlloySmelterContainer>>> ALLOY_SMELTER = registerMachineContainer("alloy_smelter", AlloySmelterContainer::new, () -> AlloySmelterScreen::new);

    private static <T extends Container, SC extends Screen & IHasContainer<T>> Map<Tier, RegistryObject<ContainerType<T>>> registerMachineContainer(String name, TieredContainerFactory<T> factory, Supplier<ScreenManager.IScreenFactory<T, SC>> screenFactory)
    {
        return registerMachineContainer(name, factory, screenFactory, Tier.values());
    }

    private static <T extends Container, SC extends Screen & IHasContainer<T>> Map<Tier, RegistryObject<ContainerType<T>>> registerMachineContainer(String name, TieredContainerFactory<T> factory, Supplier<ScreenManager.IScreenFactory<T, SC>> screenFactory, Tier[] tiers)
    {
        Map<Tier, RegistryObject<ContainerType<T>>> containers = new HashMap<>();
        Arrays.stream(tiers).forEach(tier -> {
            RegistryObject<ContainerType<T>> container = CONTAINERS.register(name + "_" + tier.getId(), () -> new ContainerType<>((id, inventory) -> factory.create(id, inventory, tier)));
            containers.put(tier, container);
            SCREENS.put(container::get, screenFactory.get());
        });
        return containers;
    }

    public static void load(IEventBus bus)
    {
        CONTAINERS.register(bus);
        LOGGER.debug("Loaded");
    }

    interface TieredContainerFactory<T extends Container>
    {
        T create(int id, PlayerInventory inventory, Tier tier);
    }
}
