package coffeecatrailway.soulpowered.client.gui.screen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.inventory.container.SoulGeneratorContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public class SoulGeneratorScreen extends AbstractGeneratorScreen<SoulGeneratorContainer>
{
    public static final ResourceLocation TEXTURE = SoulMod.getLocation("textures/gui/container/soul_generator.png");

    public SoulGeneratorScreen(SoulGeneratorContainer container, PlayerInventory inv, ITextComponent title)
    {
        super(container, inv, title);
    }

    @Override
    public ResourceLocation getGuiTexture()
    {
        return TEXTURE;
    }
}
