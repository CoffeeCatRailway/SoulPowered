package coffeecatrailway.soulpowered.intergration.jei.generator;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;

import java.util.Collection;

/**
 * @author CoffeeCatRailway
 * Created: 14/01/2021
 */
public class CoalGeneratorRecipe extends AbstractGeneratorRecipe
{
    public CoalGeneratorRecipe(IGuiHelper guiHelper, Collection<ItemStack> inputs, int rfPerTick, int burnTime)
    {
        super(guiHelper, inputs, rfPerTick, burnTime);
    }
}
