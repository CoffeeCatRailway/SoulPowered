package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.ClientEvents;
import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.item.*;
import coffeecatrailway.soulpowered.data.gen.SoulLanguage;
import coffeecatrailway.soulpowered.intergration.jei.JeiSoulPlugin;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author CoffeeCatRailway
 * Created: 7/11/2020
 */
public class SoulItems
{
    private static final Logger LOGGER = SoulMod.getLogger("Items");
    protected static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SoulMod.MOD_ID);

    // Tech
    public static final RegistryObject<BatteryItem> SIMPLE_BATTERY = powered(registerIdAsName("simple_battery", prop -> new BatteryItem(prop, Tier.SIMPLE)), false);
    public static final RegistryObject<BatteryItem> NORMAL_BATTERY = powered(registerIdAsName("normal_battery", prop -> new BatteryItem(prop, Tier.NORMAL)), false);
    public static final RegistryObject<BatteryItem> SOULIUM_BATTERY = powered(registerIdAsName("soulium_battery", prop -> new BatteryItem(prop, Tier.SOULIUM)), false);

    // Ingots
    public static final RegistryObject<Item> SOULIUM_INGOT = registerIdAsName("soulium_ingot", Item::new);
    public static final RegistryObject<Item> COPPER_INGOT = registerIdAsName("copper_ingot", Item::new);

    // Tools
    public static final RegistryObject<SwordItem> SOULIUM_SWORD = registerIdAsName("soulium_sword", prop -> new SwordItem(SoulItemTier.SOULIUM, 3, -2.4f, prop));
    public static final RegistryObject<AxeItem> SOULIUM_AXE = registerIdAsName("soulium_axe", prop -> new AxeItem(SoulItemTier.SOULIUM, 5f, -3f, prop));
    public static final RegistryObject<PickaxeItem> SOULIUM_PICKAXE = registerIdAsName("soulium_pickaxe", prop -> new PickaxeItem(SoulItemTier.SOULIUM, 1, -2.8f, prop));
    public static final RegistryObject<ShovelItem> SOULIUM_SHOVEL = registerIdAsName("soulium_shovel", prop -> new ShovelItem(SoulItemTier.SOULIUM, 1.5f, -3f, prop));
    public static final RegistryObject<HoeItem> SOULIUM_HOE = registerIdAsName("soulium_hoe", prop -> new HoeItem(SoulItemTier.SOULIUM, -3, 0f, prop));

    public static final RegistryObject<PoweredSouliumSwordItem> POWERED_SOULIUM_SWORD = powered(registerIdAsName("powered_soulium_sword", PoweredSouliumSwordItem::new), true);
    public static final RegistryObject<PoweredSouliumAxeItem> POWERED_SOULIUM_AXE = powered(registerIdAsName("powered_soulium_axe", PoweredSouliumAxeItem::new), true);
    public static final RegistryObject<PoweredSouliumPickaxeItem> POWERED_SOULIUM_PICKAXE = powered(registerIdAsName("powered_soulium_pickaxe", PoweredSouliumPickaxeItem::new), true);
    public static final RegistryObject<PoweredSouliumShovelItem> POWERED_SOULIUM_SHOVEL = powered(registerIdAsName("powered_soulium_shovel", PoweredSouliumShovelItem::new), true);
    public static final RegistryObject<PoweredSouliumHoeItem> POWERED_SOULIUM_HOE = powered(registerIdAsName("powered_soulium_hoe", PoweredSouliumHoeItem::new), true);

    // Misc
    public static final RegistryObject<SoulBottleItem> SOUL_BOTTLE = registerIdAsName("soul_bottle", SoulBottleItem::new);

    public static final RegistryObject<SoulAmuletItem> GOLD_SOUL_AMULET = registerCurio(registerWithName("gold_soul_amulet", "Golden Soul Amulet",
            prop -> new SoulAmuletItem(prop, ItemTier.GOLD, 1.5f, .1f, SoulMod.getLocation("textures/models/gold_soul_amulet.png"))),
            "Allows you to gather souls from your kills");
    public static final RegistryObject<SoulAmuletItem> COPPER_SOUL_AMULET = registerCurio(registerIdAsName("copper_soul_amulet",
            prop -> new SoulAmuletItem(prop, 100, 1.5f, .15f, SoulMod.getLocation("textures/models/copper_soul_amulet.png"))),
            "Allows you to gather souls from your kills");
    public static final RegistryObject<SoulAmuletItem> IRON_SOUL_AMULET = registerCurio(registerIdAsName("iron_soul_amulet",
            prop -> new SoulAmuletItem(prop, ItemTier.IRON, 1.75f, .25f, SoulMod.getLocation("textures/models/iron_soul_amulet.png"))),
            "Allows you to gather souls from your kills");
    public static final RegistryObject<SoulAmuletItem> DIAMOND_SOUL_AMULET = registerCurio(registerIdAsName("diamond_soul_amulet",
            prop -> new SoulAmuletItem(prop, ItemTier.DIAMOND, 2f, .5f, SoulMod.getLocation("textures/models/diamond_soul_amulet.png"))),
            "Allows you to gather souls from your kills");
    public static final RegistryObject<SoulAmuletItem> SOULIUM_SOUL_AMULET = registerCurio(registerIdAsName("soulium_soul_amulet",
            prop -> new SoulAmuletItem(prop, 1750, 2.5f, .75f, SoulMod.getLocation("textures/models/soulium_soul_amulet.png"))),
            "Allows you to gather souls from your kills");
    public static final RegistryObject<SoulAmuletPoweredItem> POWERED_SOULIUM_SOUL_AMULET = powered(registerCurio(registerIdAsName("powered_soulium_soul_amulet",
            prop -> new SoulAmuletPoweredItem(prop, 3f, 1f)), "Allows you to gather souls from your kills"), true);
    public static final RegistryObject<SoulAmuletItem> NETHERITE_SOUL_AMULET = registerCurio(registerIdAsName("netherite_soul_amulet",
            prop -> new SoulAmuletItem(prop, ItemTier.NETHERITE, 3, 1f, SoulMod.getLocation("textures/models/netherite_soul_amulet.png"))),
            "Allows you to gather souls from your kills");

    public static final RegistryObject<SoulShieldItem> SOUL_SHIELD = registerCurio(registerIdAsName("soul_shield", prop -> new SoulShieldItem(prop, 4f, 10f, 25f)),
            "Uses souls to shield you");

    private static <T extends Item> RegistryObject<T> registerCurio(RegistryObject<T> object, String description)
    {
        SoulLanguage.EXTRA_LANGS.put("item." + SoulMod.MOD_ID + "." + object.getId().getPath() + ".description", description);
        return object;
    }

    private static <T extends Item> RegistryObject<T> powered(RegistryObject<T> item, boolean hasModelProperty)
    {
        if (hasModelProperty)
            ClientEvents.POWERED_ITEM_PROPERTY_SET.add(item::get);
        JeiSoulPlugin.POWERED_ITEM_SET.add(item::get);
        return item;
    }

    protected static <T extends Item> RegistryObject<T> registerIdAsName(String id, Function<Item.Properties, T> factory)
    {
        return registerWithName(id, null, factory);
    }

    private static <T extends Item> RegistryObject<T> registerWithName(String id, @Nullable String name, Function<Item.Properties, T> factory)
    {
        return register(id, name, true, factory);
    }

    private static <T extends Item> RegistryObject<T> register(String id, @Nullable String name, boolean addLang, Function<Item.Properties, T> factory)
    {
        RegistryObject<T> object = ITEMS.register(id, () -> factory.apply(new Item.Properties().tab(SoulMod.GROUP)));
        if (addLang)
            SoulLanguage.ITEMS.put(object, name == null ? SoulLanguage.capitalize(id) : name);
        return object;
    }

    public static void load(IEventBus bus)
    {
        ITEMS.register(bus);
        LOGGER.debug("Loaded");
    }
}
