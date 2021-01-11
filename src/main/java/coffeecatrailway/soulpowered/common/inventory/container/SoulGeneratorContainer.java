package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.common.tileentity.AbstractGeneratorTileEntity;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
import coffeecatrailway.soulpowered.registry.SoulContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public class SoulGeneratorContainer extends AbstractGeneratorContainer<SoulGeneratorTileEntity>
{
    public SoulGeneratorContainer(ContainerType<? extends SoulGeneratorContainer> type, int id, PlayerInventory inventory)
    {
        this(type, id, inventory, new SoulGeneratorTileEntity(), new IntArray(AbstractGeneratorTileEntity.FIELDS_COUNT));
    }

    public SoulGeneratorContainer(int id, PlayerInventory inventory, SoulGeneratorTileEntity tile, IIntArray fields)
    {
        this(SoulContainers.SOUL_GENERATOR.get(), id, inventory, tile, fields);
    }

    public SoulGeneratorContainer(ContainerType<? extends SoulGeneratorContainer> type, int id, PlayerInventory inventory, SoulGeneratorTileEntity tile, IIntArray fields)
    {
        super(type, id, tile, fields);
        this.addSlot(new Slot(this.tileEntity, 0, 80, 33));
        this.addPlayerSlots(inventory, 8, 84).forEach(this::addSlot);
    }

    @Override
    protected boolean isFuel(ItemStack stack)
    {
        return SoulGeneratorTileEntity.isFuel(stack);
    }
}
