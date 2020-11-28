package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
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
    private static final Logger LOGGER = SoulPoweredMod.getLogger("World-Generation-Registry");
    private static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, SoulPoweredMod.MOD_ID);

    public static final RegistryObject<SoulCastleStructure<NoFeatureConfig>> SOUL_CASTLE = registerStructure("soul_castle", new SoulCastleStructure<>(NoFeatureConfig.field_236558_a_),
            new StructureSeparationSettings(SoulPoweredMod.COMMON_CONFIG.soulCastleChunksMax.get(), SoulPoweredMod.COMMON_CONFIG.soulCastleChunksMin.get(), 12230000), false, GenerationStage.Decoration.UNDERGROUND_STRUCTURES);

    private static <T extends Structure<NoFeatureConfig>> RegistryObject<T> registerStructure(String id, T structure, StructureSeparationSettings separationSettings, boolean transformSurroundingLand, GenerationStage.Decoration decoration)
    {
        Structure.NAME_STRUCTURE_BIMAP.put(SoulPoweredMod.getLocation(id).toString(), structure);
        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(structure, decoration);
        DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.field_236191_b_)
                .put(structure, separationSettings).build();
        if (transformSurroundingLand)
            Structure.field_236384_t_ = ImmutableList.<Structure<?>>builder().addAll(Structure.field_236384_t_).add(structure).build();
        return STRUCTURES.register(id, () -> structure);
    }

    public static class StructurePieces
    {
        public static final IStructurePieceType SOUL_CASTLE = SoulCastlePieces.Piece::new;

        public static void loadStructureTypes()
        {
            Registry.register(Registry.STRUCTURE_PIECE, SoulPoweredMod.getLocation("soul_castle"), SOUL_CASTLE);
            WorldGenRegistries.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, SoulPoweredMod.getLocation("soul_castle"), SoulWorldGen.SOUL_CASTLE.get().withConfiguration(NoFeatureConfig.field_236559_b_));
            LOGGER.debug("Loaded structure types");
        }
    }

    public static void load(IEventBus bus)
    {
        STRUCTURES.register(bus);
        LOGGER.debug("Loaded");
    }
}
