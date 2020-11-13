package coffeecatrailway.soulpowered.common.inventory.container;

import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import coffeecatrailway.soulpowered.utils.RedstoneMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.silentchaos512.utils.EnumUtils;
import net.silentchaos512.utils.MathUtils;

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
        int energyClamped = MathUtils.clamp(this.getEnergyStored(), 0, max);
        return max > 0 ? 50 * energyClamped / max : 0;
    }

    public RedstoneMode getRedstoneMode()
    {
        return EnumUtils.byOrdinal(this.fields.get(4), RedstoneMode.IGNORED);
    }

    public void setRedstoneMode(RedstoneMode mode)
    {
        this.fields.set(4, mode.ordinal());
    }
}
