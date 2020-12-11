package coffeecatrailway.soulpowered.intergration.tinkers;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.TinkerModule;
import slimeknights.tconstruct.fluids.FluidIcons;

/**
 * @author CoffeeCatRailway
 * Created: 29/11/2020
 */
public class SoulTinkerFluids extends TinkerModule
{
    public static final FluidObject<ForgeFlowingFluid> moltenSoulium;

    public SoulTinkerFluids()
    {
    }

    private static FluidAttributes.Builder moltenBuilder() {
        return FluidAttributes.builder(FluidIcons.MOLTEN_STILL, FluidIcons.MOLTEN_FLOWING).density(2000).viscosity(10000).temperature(1000);
    }

    static {
        moltenSoulium = FLUIDS.register("molten_soulium", moltenBuilder().color(0xff7b5f4f).temperature(520), Material.LAVA, 9);
    }
}
