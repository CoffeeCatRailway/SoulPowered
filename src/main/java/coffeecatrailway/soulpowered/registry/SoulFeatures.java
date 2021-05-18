package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 26/11/2020
 */
public class SoulFeatures
{
    public static final Supplier<StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>>> SOUL_CASTLE = registerConfiguredStructure("soul_castle", () -> SoulWorldGen.SOUL_CASTLE.get().configured(NoFeatureConfig.INSTANCE),
            SoulWorldGen.SOUL_CASTLE);

    private static <FC extends IFeatureConfig, F extends StructureFeature<FC, ? extends Structure<FC>>, UC extends Structure<FC>> Supplier<F> registerConfiguredStructure(String id, Supplier<F> structure, Supplier<UC> unconfiguredStructure)
    {
        F ret = Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, SoulMod.getLocation(id), structure.get());
        FlatGenerationSettings.STRUCTURE_FEATURES.put(unconfiguredStructure.get(), ret);
        return () -> ret;
    }
}
