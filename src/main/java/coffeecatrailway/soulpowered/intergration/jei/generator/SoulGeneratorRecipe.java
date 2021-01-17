package coffeecatrailway.soulpowered.intergration.jei.generator;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;

import java.util.Collection;

/**
 * @author CoffeeCatRailway
 * Created: 17/01/2021
 */
public class SoulGeneratorRecipe extends AbstractGeneratorRecipe
{
    public SoulGeneratorRecipe(IGuiHelper guiHelper, Collection<ItemStack> inputs, int rfPerTick, int burnTime)
    {
        super(guiHelper, inputs, rfPerTick, burnTime);
    }
}
