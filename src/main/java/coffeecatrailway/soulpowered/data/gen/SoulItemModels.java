package coffeecatrailway.soulpowered.data.gen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.registry.SoulItems;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 14/05/2021
 */
public class SoulItemModels implements IDataProvider
{
    private static final Logger LOGGER = SoulMod.getLogger("Item-Models");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final DataGenerator dataGenerator;
    private BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer;

    public SoulItemModels(DataGenerator dataGenerator)
    {
        this.dataGenerator = dataGenerator;
    }

    @Override
    public void run(DirectoryCache cache)
    {
        Path path = this.dataGenerator.getOutputFolder();
        Map<ResourceLocation, Supplier<JsonElement>> map1 = Maps.newHashMap();
        this.consumer = (location, json) ->
        {
            Supplier<JsonElement> supplier = map1.put(location, json);
            if (supplier != null)
            {
                throw new IllegalStateException("Duplicate model definition for " + location);
            }
        };
        this.register();
        this.saveCollection(cache, path, map1, SoulItemModels::createModelPath);
    }

    private void register()
    {
        // Tech
        this.generateFlatItem(SoulItems.SIMPLE_BATTERY.get(), StockModelShapes.FLAT_ITEM);
        this.generateFlatItem(SoulItems.NORMAL_BATTERY.get(), StockModelShapes.FLAT_ITEM);
        this.generateFlatItem(SoulItems.SOULIUM_BATTERY.get(), StockModelShapes.FLAT_ITEM);

        // Ingots
        this.generateFlatItem(SoulItems.SOULIUM_INGOT.get(), StockModelShapes.FLAT_ITEM);
        this.generateFlatItem(SoulItems.COPPER_INGOT.get(), StockModelShapes.FLAT_ITEM);

        // Tools
        this.generateFlatItem(SoulItems.SOULIUM_SWORD.get(), StockModelShapes.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(SoulItems.SOULIUM_AXE.get(), StockModelShapes.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(SoulItems.SOULIUM_PICKAXE.get(), StockModelShapes.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(SoulItems.SOULIUM_SHOVEL.get(), StockModelShapes.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(SoulItems.SOULIUM_HOE.get(), StockModelShapes.FLAT_HANDHELD_ITEM);

        // Misc
        this.generateFlatItem(SoulItems.SOUL_BOTTLE.get(), StockModelShapes.FLAT_ITEM);
    }

    private void generateFlatItem(Item item, ModelsUtil modelsUtil)
    {
        modelsUtil.create(ModelsResourceUtil.getModelLocation(item), ModelTextures.layer0(item), this.consumer);
    }

    private <T> void saveCollection(DirectoryCache cache, Path dataFolder, Map<T, ? extends Supplier<JsonElement>> models, BiFunction<Path, T, Path> resolver)
    {
        models.forEach((p_240088_3_, p_240088_4_) ->
        {
            Path path = resolver.apply(dataFolder, p_240088_3_);

            try
            {
                IDataProvider.save(GSON, cache, p_240088_4_.get(), path);
            } catch (Exception exception)
            {
                LOGGER.error("Couldn't save {}", path, exception);
            }
        });
    }

    private static Path createModelPath(Path dataFolder, ResourceLocation name)
    {
        return dataFolder.resolve("assets/" + name.getNamespace() + "/models/" + name.getPath() + ".json");
    }

    @Override
    public String getName()
    {
        return SoulMod.MOD_ID + " Block State Definitions";
    }
}
