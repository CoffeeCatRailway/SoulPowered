package coffeecatrailway.soulpowered.client.gui.screen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.inventory.container.CoalGeneratorContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author CoffeeCatRailway
 * Created: 8/01/2021
 */
public class CoalGeneratorScreen extends AbstractGeneratorScreen<CoalGeneratorContainer>
{
    public static final ResourceLocation TEXTURE = SoulMod.getLocation("textures/gui/container/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorContainer container, PlayerInventory inv, ITextComponent title)
    {
        super(container, inv, title);
    }

    @Override
    public ResourceLocation getGuiTexture()
    {
        return TEXTURE;
    }
}
