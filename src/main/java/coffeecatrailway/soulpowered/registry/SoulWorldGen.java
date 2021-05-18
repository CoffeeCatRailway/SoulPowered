package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.world.gen.structure.SoulCastlePieces;
import coffeecatrailway.soulpowered.common.world.gen.structure.SoulCastleStructure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

/**
 * @author CoffeeCatRailway
 * Created: 26/11/2020
 */
public class SoulWorldGen
{
    private static final Logger LOGGER = SoulMod.getLogger("World-Generation-Registry");
    private static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SoulMod.MOD_ID);

    public static final RegistryObject<SoulCastleStructure<NoFeatureConfig>> SOUL_CASTLE = registerStructure("soul_castle", new SoulCastleStructure<>(NoFeatureConfig.CODEC),
            new StructureSeparationSettings(SoulMod.COMMON_CONFIG.soulCastleChunksMax.get(), SoulMod.COMMON_CONFIG.soulCastleChunksMin.get(), 12230000), false, GenerationStage.Decoration.UNDERGROUND_STRUCTURES);

    private static <T extends Structure<NoFeatureConfig>> RegistryObject<T> registerStructure(String id, T structure, StructureSeparationSettings separationSettings, boolean transformSurroundingLand, GenerationStage.Decoration decoration)
    {
        Structure.STRUCTURES_REGISTRY.put(SoulMod.getLocation(id).toString(), structure);
        Structure.STEP.put(structure, decoration);
        DimensionStructuresSettings.DEFAULTS = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.DEFAULTS)
                .put(structure, separationSettings).build();
        if (transformSurroundingLand)
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder().addAll(Structure.NOISE_AFFECTING_FEATURES).add(structure).build();
        return STRUCTURES.register(id, () -> structure);
    }

    public static class StructurePieces
    {
        public static final IStructurePieceType SOUL_CASTLE = SoulCastlePieces.Piece::new;

        public static void loadStructureTypes()
        {
            Registry.register(Registry.STRUCTURE_PIECE, SoulMod.getLocation("soul_castle"), SOUL_CASTLE);
            WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, SoulMod.getLocation("soul_castle"), SoulWorldGen.SOUL_CASTLE.get().configured(NoFeatureConfig.INSTANCE));
            LOGGER.debug("Loaded structure types");
        }
    }

    public static void load(IEventBus bus)
    {
        STRUCTURES.register(bus);
        LOGGER.debug("Loaded");
    }
}
