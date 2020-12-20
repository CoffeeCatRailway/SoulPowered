package coffeecatrailway.soulpowered.common.tileentity;

import coffeecatrailway.soulpowered.common.inventory.container.AlloySmelterContainer;
import coffeecatrailway.soulpowered.common.item.crafting.AlloySmelterRecipe;
import coffeecatrailway.soulpowered.registry.SoulRecipes;
import coffeecatrailway.soulpowered.registry.SoulTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * @author CoffeeCatRailway
 * Created: 13/12/2020
 */
public class AlloySmelterTileEntity extends AbstractProcessMachineTileEntity<AlloySmelterRecipe>
{
    public static final int MAX_ENERGY = 20_000;
    public static final int MAX_RECEIVE = 1000;
    public static final int ENERGY_CONSUMPTION = 20;

    public static final int INVENTORY_SIZE = 4;
    private static final int[] SLOTS_INPUT = IntStream.range(0, INVENTORY_SIZE - 1).toArray();
    private static final int[] SLOTS_OUTPUT = {INVENTORY_SIZE - 1};
    private static final int[] SLOTS_ALL = IntStream.range(0, INVENTORY_SIZE).toArray();

    public AlloySmelterTileEntity()
    {
        this(SoulTileEntities.ALLOY_SMELTER.get());
    }

    public AlloySmelterTileEntity(TileEntityType<?> type)
    {
        super(type, INVENTORY_SIZE, MAX_ENERGY, MAX_RECEIVE, 0);
    }

    @Override
    protected int getEnergyConsumedPerTick()
    {
        return ENERGY_CONSUMPTION;
    }

    @Override
    protected int[] outputSlots()
    {
        return SLOTS_OUTPUT;
    }

    @Override
    protected void consumeIngredients(AlloySmelterRecipe recipe)
    {
        recipe.consumeIngredients(this);
    }

    @Nullable
    @Override
    protected AlloySmelterRecipe getRecipe()
    {
        if (this.world == null) return null;
        return this.world.getRecipeManager().getRecipe(SoulRecipes.ALLOY_SMELTING_TYPE, this, this.world).orElse(null);
    }

    @Override
    protected int getProcessTime(AlloySmelterRecipe recipe)
    {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<ItemStack> getProcessedResults(AlloySmelterRecipe recipe)
    {
        return Collections.singleton(recipe.getCraftingResult(this));
    }

    @Override
    public int getInputSlotCount()
    {
        return SLOTS_INPUT.length;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return SLOTS_ALL;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction)
    {
        return index < INVENTORY_SIZE - 1;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return index == INVENTORY_SIZE - 1;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return new AlloySmelterContainer(id, player, this, this.getFields());
    }
}
