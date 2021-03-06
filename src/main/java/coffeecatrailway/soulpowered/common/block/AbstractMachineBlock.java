package coffeecatrailway.soulpowered.common.block;

import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author CoffeeCatRailway
 * Created: 10/11/2020
 * Based On: https://github.com/SilentChaos512/Silents-Mechanisms/blob/1.16.x/src/main/java/net/silentchaos512/mechanisms/block/AbstractMachineBlock.java
 */
public abstract class AbstractMachineBlock extends AbstractFurnaceBlock
{
    public AbstractMachineBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void openContainer(World world, BlockPos pos, PlayerEntity player)
    {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof INamedContainerProvider)
            player.openMenu((INamedContainerProvider) tile);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof IInventory)
            {
                InventoryHelper.dropContents(world, pos, (IInventory) tile);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntity tile = world.getBlockEntity(pos);
        if (stack.hasCustomHoverName())
            if (tile instanceof AbstractMachineTileEntity)
                ((AbstractMachineTileEntity) tile).setCustomName(stack.getDisplayName());
    }
}
