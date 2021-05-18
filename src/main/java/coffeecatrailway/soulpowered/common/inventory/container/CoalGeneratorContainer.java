package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.tileentity.AbstractGeneratorTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.CoalGeneratorTileEntity;
import coffeecatrailway.soulpowered.registry.SoulContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

/**
 * @author CoffeeCatRailway
 * Created: 8/01/2021
 */
public class CoalGeneratorContainer extends AbstractGeneratorContainer<CoalGeneratorTileEntity>
{
    public CoalGeneratorContainer(int id, PlayerInventory inventory, Tier tier)
    {
        this(SoulContainers.COAL_GENERATOR.get(tier).get(), id, inventory, new CoalGeneratorTileEntity(tier), new IntArray(AbstractGeneratorTileEntity.FIELDS_COUNT));
    }

    public CoalGeneratorContainer(int id, PlayerInventory inventory, CoalGeneratorTileEntity tile, IIntArray fields, Tier tier)
    {
        this(SoulContainers.COAL_GENERATOR.get(tier).get(), id, inventory, tile, fields);
    }

    public CoalGeneratorContainer(ContainerType<? extends CoalGeneratorContainer> type, int id, PlayerInventory inventory, CoalGeneratorTileEntity tile, IIntArray fields)
    {
        super(type, id, tile, fields);
        this.addSlot(new Slot(this.tileEntity, 0, 80, 33));
        this.addPlayerSlots(inventory, 8, 84).forEach(this::addSlot);
    }

    @Override
    protected boolean isFuel(ItemStack stack)
    {
        return CoalGeneratorTileEntity.isFuel(stack);
    }
}
