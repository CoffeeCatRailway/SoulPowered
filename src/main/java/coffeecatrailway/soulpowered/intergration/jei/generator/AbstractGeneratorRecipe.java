package coffeecatrailway.soulpowered.intergration.jei.generator;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.api.Tier;
import com.google.common.base.Preconditions;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 17/01/2021
 */
public class AbstractGeneratorRecipe
{
    private final List<ItemStack> inputs;
    private final int rfPerTick;
    private final ITextComponent burnTime;
    private final IDrawableAnimated flame;

    public AbstractGeneratorRecipe(IGuiHelper guiHelper, Collection<ItemStack> inputs, int rfPerTick, int burnTime)
    {
        Preconditions.checkArgument(burnTime > 0, "Burn time must be greater than 0");
        Preconditions.checkArgument(rfPerTick > 0, "RF per tick must be greater than 0");

        this.inputs = new ArrayList<>(inputs);
        this.rfPerTick = rfPerTick * burnTime;
        this.burnTime = SoulData.Lang.itemBurnTime(burnTime);
        this.flame = guiHelper.drawableBuilder(AbstractGeneratorRecipeCategory.TEXTURE, 92, 0, 14, 14).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
    }

    public List<ItemStack> getInputs()
    {
        return this.inputs;
    }

    public int getRfPerTick(Tier tier)
    {
        return tier.calculatePowerGenerated(this.rfPerTick);
    }

    public ITextComponent getRfPerTickLang(Tier tier)
    {
        return SoulData.Lang.energyPerTick(this.getRfPerTick(tier));
    }

    public ITextComponent getBurnTime()
    {
        return this.burnTime;
    }

    public IDrawableAnimated getFlame()
    {
        return this.flame;
    }
}
