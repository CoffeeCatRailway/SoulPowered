package coffeecatrailway.soulpowered.data.gen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.api.RedstoneMode;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CoffeeCatRailway
 * Created: 14/05/2021
 */
@SuppressWarnings("unchecked")
public class SoulLanguage extends LanguageProvider
{
    public static final Map<String, String> EXTRA_LANGS = new HashMap<>();
    public static final Map<RegistryObject<? extends Item>, String> ITEMS = new HashMap<>();
    public static final Map<RegistryObject<? extends Block>, String> BLOCKS = new HashMap<>();
    public static final Map<RegistryObject<? extends EntityType<?>>, String> ENTITIES = new HashMap<>();

    private static final String ENERGY = "misc." + SoulMod.MOD_ID + ".energy";
    private static final String ENERGY_PER_TICK = "misc." + SoulMod.MOD_ID + ".energy_per_tick";
    private static final String ENERGY_WITH_MAX = "misc." + SoulMod.MOD_ID + ".energy_with_max";
    private static final String REDSTONE_MODE = "misc." + SoulMod.MOD_ID + ".redstone_mode";

    public static final String JEI_CATEGORY_COAL_GENERATOR = "misc." + SoulMod.MOD_ID + ".jei.category.coal_generator";
    public static final String JEI_CATEGORY_SOUL_GENERATOR = "misc." + SoulMod.MOD_ID + ".jei.category.soul_generator";
    private static final String JEI_ITEM_BURN_TIME = "misc." + SoulMod.MOD_ID + ".jei.item_burn_time";

    public SoulLanguage(DataGenerator generator)
    {
        super(generator, SoulMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        this.add("itemGroup." + SoulMod.MOD_ID, "Ham N' Cheese");

        this.add("commands." + SoulMod.MOD_ID + ".souls.get", "%1$s has %2$s soul(s)");
        this.add("commands." + SoulMod.MOD_ID + ".souls.modify.add", "Gave %2$s soul(s) to %1$s");
        this.add("commands." + SoulMod.MOD_ID + ".souls.modify.remove", "Removed %2$s soul(s) to %1$s");
        this.add("commands." + SoulMod.MOD_ID + ".souls.modify.set", "%1$s now has %2$s soul(s)");

        this.add("item." + SoulMod.MOD_ID + ".curio.range", TextFormatting.GOLD + "Range: " + TextFormatting.YELLOW + "%s Blocks");

        this.add("item." + SoulMod.MOD_ID + ".soul_amulet.soul_gathering_chance", TextFormatting.GOLD + "Soul Gathering Chance: " + TextFormatting.YELLOW + "%s");
        this.add("item." + SoulMod.MOD_ID + ".soul_shield.duration", TextFormatting.GOLD + "Duration: " + TextFormatting.YELLOW + "%s Seconds");
        this.add("item." + SoulMod.MOD_ID + ".soul_shield.cooldown", TextFormatting.GOLD + "Cooldown: " + TextFormatting.YELLOW + "%s Seconds");

        this.add(ENERGY, "%s SE");
        this.add(ENERGY_PER_TICK, "%s SE/t");
        this.add(ENERGY_WITH_MAX, "%s / %s SE");
        this.add(REDSTONE_MODE, "Redstone Mode: %s");

        this.add(JEI_CATEGORY_COAL_GENERATOR, "Coal Generator");
        this.add(JEI_CATEGORY_SOUL_GENERATOR, "Soul Generator");
        this.add(JEI_ITEM_BURN_TIME, "%s BT");

        this.add(SoulMod.KEY_CATEGORY, "Soul Powered");
        this.add(SoulMod.ACTIVATE_CURIO.getName(), "Activate soul curio");

        EXTRA_LANGS.forEach(this::add);
        ITEMS.forEach((item, name) -> this.add(item.get(), name));
        BLOCKS.forEach((block, name) -> this.add(block.get(), name));
        ENTITIES.forEach((entity, name) -> this.add(entity.get(), name));
    }

    public static IFormattableTextComponent energy(int amount)
    {
        return new TranslationTextComponent(ENERGY, amount);
    }

    public static IFormattableTextComponent energyPerTick(int amount)
    {
        return new TranslationTextComponent(ENERGY_PER_TICK, amount);
    }

    public static IFormattableTextComponent energyWithMax(int amount, int max)
    {
        return new TranslationTextComponent(ENERGY_WITH_MAX, amount, max);
    }

    public static IFormattableTextComponent redstoneMode(RedstoneMode mode)
    {
        return new TranslationTextComponent(REDSTONE_MODE, mode.name());
    }

    public static ITextComponent itemBurnTime(int burnTime)
    {
        return new TranslationTextComponent(JEI_ITEM_BURN_TIME, burnTime);
    }

    public static String capitalize(String id)
    {
        String[] names = id.split("_");
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String name : names)
        {
            builder.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
            i++;
            if (i != names.length)
                builder.append(" ");
        }
        return builder.toString();
    }
}
