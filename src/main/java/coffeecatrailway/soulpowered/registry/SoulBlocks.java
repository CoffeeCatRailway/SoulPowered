package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.block.*;
import coffeecatrailway.soulpowered.data.gen.SoulLanguage;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 9/11/2020
 */
public class SoulBlocks
{
    private static final Logger LOGGER = SoulMod.getLogger("Blocks");
    protected static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SoulMod.MOD_ID);

    // Building Blocks
    public static final RegistryObject<Block> SOULIUM_BLOCK = register("soulium_block", () -> new Block(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)), prop -> prop);

    // Machine Frames
    public static final RegistryObject<MachineFrameBlock> SIMPLE_MACHINE_FRAME = registerMachineFrame("simple_machine_frame", Tier.SIMPLE, SoundType.WOOD);
    public static final RegistryObject<MachineFrameBlock> NORMAL_MACHINE_FRAME = registerMachineFrame("normal_machine_frame", Tier.NORMAL, SoundType.STONE);
    public static final RegistryObject<MachineFrameBlock> SOULIUM_MACHINE_FRAME = registerMachineFrame("soulium_machine_frame", Tier.SOULIUM, SoundType.METAL);

    // Generators
    public static final RegistryObject<CoalGeneratorBlock> SIMPLE_COAL_GENERATOR = registerCoalGenerator("simple_coal_generator", Tier.SIMPLE, SoundType.WOOD);
    public static final RegistryObject<CoalGeneratorBlock> NORMAL_COAL_GENERATOR = registerCoalGenerator("normal_coal_generator", Tier.NORMAL, SoundType.STONE);
    public static final RegistryObject<SoulGeneratorBlock> SOUL_GENERATOR = registerMachine(register("soul_generator", () -> new SoulGeneratorBlock(Tier.SOULIUM.getProperties().apply(AbstractBlock.Properties.of(Tier.SOULIUM.getMaterial(), Tier.SOULIUM.getMaterialColor())).sound(SoundType.METAL)), prop -> prop));

    // Soul Boxes
    public static final RegistryObject<SoulBoxBlock> SIMPLE_SOUL_BOX = registerSoulBox("simple_soul_box", Tier.SIMPLE, SoundType.WOOD);
    public static final RegistryObject<SoulBoxBlock> NORMAL_SOUL_BOX = registerSoulBox("normal_soul_box", Tier.NORMAL, SoundType.STONE);
    public static final RegistryObject<SoulBoxBlock> SOULIUM_SOUL_BOX = registerSoulBox("soulium_soul_box", Tier.SOULIUM, SoundType.METAL);

    // Alloy Smelters
    public static final RegistryObject<AlloySmelterBlock> SIMPLE_ALLOY_SMELTER = registerAlloySmelter("simple_alloy_smelter", Tier.SIMPLE, SoundType.WOOD);
    public static final RegistryObject<AlloySmelterBlock> NORMAL_ALLOY_SMELTER = registerAlloySmelter("normal_alloy_smelter", Tier.NORMAL, SoundType.STONE);
    public static final RegistryObject<AlloySmelterBlock> SOULIUM_ALLOY_SMELTER = registerAlloySmelter("soulium_alloy_smelter", Tier.SOULIUM, SoundType.METAL);

    // Helper Methods
    private static RegistryObject<MachineFrameBlock> registerMachineFrame(String id, Tier tier, SoundType soundType)
    {
        return register(id, () -> new MachineFrameBlock(tier.getProperties().apply(AbstractBlock.Properties.of(tier.getMaterial(), tier.getMaterialColor())).sound(soundType)), prop -> prop);
    }

    private static RegistryObject<CoalGeneratorBlock> registerCoalGenerator(String id, Tier tier, SoundType soundType)
    {
        return registerMachine(register(id, () -> new CoalGeneratorBlock(tier.getProperties().apply(AbstractBlock.Properties.of(tier.getMaterial(), tier.getMaterialColor())).sound(soundType), tier), prop -> prop));
    }

    private static RegistryObject<SoulBoxBlock> registerSoulBox(String id, Tier tier, SoundType soundType)
    {
        return registerMachine(register(id, () -> new SoulBoxBlock(tier.getProperties().apply(AbstractBlock.Properties.of(tier.getMaterial(), tier.getMaterialColor())).sound(soundType), tier), prop -> prop));
    }

    private static RegistryObject<AlloySmelterBlock> registerAlloySmelter(String id, Tier tier, SoundType soundType)
    {
        return registerMachine(register(id, () -> new AlloySmelterBlock(tier.getProperties().apply(AbstractBlock.Properties.of(tier.getMaterial(), tier.getMaterialColor())).sound(soundType), tier), prop -> prop));
    }

    private static <T extends Block> RegistryObject<T> registerMachine(RegistryObject<T> object)
    {
        SoulLanguage.EXTRA_LANGS.put("container." + SoulMod.MOD_ID + "." + object.getId().getPath(), SoulLanguage.capitalize(object.getId().getPath()));
        return object;
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> block, Function<Item.Properties, Item.Properties> properties)
    {
        return registerWithItem(id, block, (object, prop) -> new BlockItem(object.get(), properties.apply(prop)));
    }

    private static <T extends Block> RegistryObject<T> registerWithItem(String id, Supplier<T> block, @Nullable BiFunction<RegistryObject<T>, Item.Properties, Item> item)
    {
        RegistryObject<T> object = BLOCKS.register(id, block);
        if (item != null)
            SoulItems.ITEMS.register(id, () -> item.apply(object, new Item.Properties().tab(SoulMod.GROUP)));
        SoulLanguage.BLOCKS.put(object, SoulLanguage.capitalize(id));
        return object;
    }

    public static void load(IEventBus bus)
    {
        BLOCKS.register(bus);
        LOGGER.debug("Loaded");
    }
}
