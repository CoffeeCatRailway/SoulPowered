package coffeecatrailway.soulpowered.common.item.crafting;

import coffeecatrailway.soulpowered.common.tileentity.AbstractProcessMachineTileEntity;
import coffeecatrailway.soulpowered.registry.SoulRecipes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author CoffeeCatRailway
 * Created: 17/12/2020
 */
public class AlloySmelterRecipe implements IRecipe<AbstractProcessMachineTileEntity<?>>
{
    private final ResourceLocation recipeId;
    private int processTime;
    private final Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
    private ItemStack result;

    public AlloySmelterRecipe(ResourceLocation recipeId)
    {
        this.recipeId = recipeId;
    }

    public int getProcessTime()
    {
        return this.processTime;
    }

    public void consumeIngredients(IInventory inventory)
    {
        this.ingredients.forEach((ingredient, count) -> consumeIngredients(inventory, ingredient, count));
    }

    private void consumeIngredients(IInventory inventory, Ingredient ingredient, int count)
    {
        int amountLeft = count;
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack))
            {
                int remove = Math.min(amountLeft, stack.getCount());
                ItemStack copy = stack.copy();
                stack.shrink(remove);
                if (stack.isEmpty())
                {
                    ItemStack replace = ItemStack.EMPTY;
                    if (copy.hasContainerItem())
                        replace = copy.getContainerItem();
                    inventory.setInventorySlotContents(i, replace);
                }

                amountLeft -= remove;
                if (amountLeft <= 0)
                    return;
            }
        }
    }

    public Map<Ingredient, Integer> getIngredientsMap()
    {
        return this.ingredients;
    }

    @Override
    public boolean matches(AbstractProcessMachineTileEntity<?> inventory, World world)
    {
        for (Ingredient ingredient : this.ingredients.keySet())
        {
            int required = this.ingredients.get(ingredient);
            int found = this.foundItems(inventory, ingredient);
            if (found < required)
                return false;
        }

        for (int i = 0; i < inventory.getInputSlotCount(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                boolean foundMatch = this.ingredients.keySet().stream().anyMatch(ingredient -> ingredient.test(stack));
                if (!foundMatch)
                    return false;
            }
        }
        return true;
    }

    protected int foundItems(IInventory inventory, Ingredient ingredient)
    {
        int found = 0;
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            found += (!stack.isEmpty() && ingredient.test(stack)) ? stack.getCount() : 0;
        }
        return found;
    }

    @Override
    public ItemStack getCraftingResult(AbstractProcessMachineTileEntity<?> inventory)
    {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return this.result;
    }

    @Override
    public ResourceLocation getId()
    {
        return this.recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return SoulRecipes.ALLOY_SMELTING.get();
    }

    @Override
    public IRecipeType<?> getType()
    {
        return SoulRecipes.ALLOY_SMELTING_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlloySmelterRecipe>
    {
        @Override
        public AlloySmelterRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            AlloySmelterRecipe recipe = new AlloySmelterRecipe(recipeId);
            recipe.processTime = JSONUtils.getInt(json, "processTime");
            recipe.result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));

            JSONUtils.getJsonArray(json, "ingredients").forEach(element -> {
                Ingredient ingredient = deserializeIngredient(element);
                int count = JSONUtils.getInt(element.getAsJsonObject(), "count", 1);
                recipe.ingredients.put(ingredient, count);
            });

            return recipe;
        }

        private static Ingredient deserializeIngredient(JsonElement element)
        {
            if (element.isJsonObject())
                return Ingredient.deserialize(element.getAsJsonObject().get("value"));
            return Ingredient.deserialize(element);
        }

        @Nullable
        @Override
        public AlloySmelterRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            AlloySmelterRecipe recipe = new AlloySmelterRecipe(recipeId);
            recipe.processTime = buffer.readVarInt();
            recipe.result = buffer.readItemStack();

            int ingredientCount = buffer.readByte();
            for (int i = 0; i < ingredientCount; i++)
            {
                Ingredient ingredient = Ingredient.read(buffer);
                int count = buffer.readByte();
                recipe.ingredients.put(ingredient, count);
            }

            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, AlloySmelterRecipe recipe)
        {
            buffer.writeVarInt(recipe.processTime);
            buffer.writeItemStack(recipe.result);

            buffer.writeByte(recipe.ingredients.size());
            recipe.ingredients.forEach((ingredient, count) -> {
                ingredient.write(buffer);
                buffer.writeByte(count);
            });
        }
    }
}
