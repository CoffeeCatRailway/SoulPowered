package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.api.RedstoneMode;
import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 * Based on: https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/block/AbstractEnergyStorageContainer.java
 */
public class AbstractEnergyStorageContainer<T extends AbstractMachineTileEntity> extends Container
{
    protected final T tileEntity;
    protected final IIntArray fields;

    public AbstractEnergyStorageContainer(ContainerType<?> type, int id, T tileEntity, IIntArray fields)
    {
        super(type, id);
        this.tileEntity = tileEntity;
        this.fields = fields;

        this.trackIntArray(this.fields);
    }

    protected Collection<Slot> addPlayerSlots(PlayerInventory playerInventory, int startX, int startY)
    {
        Collection<Slot> list = new ArrayList<>();
        for (int y = 0; y < 3; ++y)
            for (int x = 0; x < 9; ++x)
                list.add(new Slot(playerInventory, x + y * 9 + 9, startX + x * 18, startY + y * 18));
        for (int x = 0; x < 9; ++x)
            list.add(new Slot(playerInventory, x, 8 + x * 18, startY + 58));
        return list;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return true;
    }

    public T getTileEntity()
    {
        return this.tileEntity;
    }

    public IIntArray getFields()
    {
        return this.fields;
    }

    public int getEnergyStored()
    {
        int upper = this.fields.get(1) & 0xFFFF;
        int lower = this.fields.get(0) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getMaxEnergyStored()
    {
        int upper = this.fields.get(3) & 0xFFFF;
        int lower = this.fields.get(2) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getEnergyBarHeight()
    {
        int max = this.getMaxEnergyStored();
        int energyClamped = MathHelper.clamp(this.getEnergyStored(), 0, max);
        return max > 0 ? 52 * energyClamped / max : 0;
    }

    public RedstoneMode getRedstoneMode()
    {
        return RedstoneMode.byOrdinal(this.fields.get(4), RedstoneMode.IGNORED);
    }

    public void setRedstoneMode(RedstoneMode mode)
    {
        this.fields.set(4, mode.ordinal());
    }
}
