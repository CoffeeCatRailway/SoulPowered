package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 26/11/2020
 */
public class SoulFeatures
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Features");

    public static final Supplier<ConfiguredFeature<?, ?>> COPPER_ORE = () -> registerConfiguredFeature("copper_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD,
            SoulBlocks.COPPER_ORE.get().getDefaultState(), 9)).range(64).square().func_242731_b(20));

    public static final Supplier<StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>>> SOUL_CASTLE = registerConfiguredStructure("soul_castle", SoulWorldGen.SOUL_CASTLE.get().withConfiguration(NoFeatureConfig.field_236559_b_),
            SoulWorldGen.SOUL_CASTLE.get());

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> registerConfiguredFeature(String id, ConfiguredFeature<FC, ?> configuredFeature)
    {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, SoulPoweredMod.getLocation(id), configuredFeature);
    }

    private static <FC extends IFeatureConfig, F extends StructureFeature<FC, ? extends Structure<FC>>, UC extends Structure<FC>> Supplier<F> registerConfiguredStructure(String id, F structure, UC unconfiguredStructure)
    {
        F ret = Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, SoulPoweredMod.getLocation(id), structure);
        FlatGenerationSettings.STRUCTURES.put(unconfiguredStructure, ret);
        return () -> ret;
    }
}
