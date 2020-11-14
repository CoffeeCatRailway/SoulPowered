package coffeecatrailway.soulpowered.common.block;

import coffeecatrailway.soulpowered.common.tileentity.SoulBoxTileEntity;
import io.github.ocelot.sonar.common.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 14/11/2020
 */
public class SoulBoxBlock extends Block
{
    public static final BooleanProperty ON = BooleanProperty.create("on");

    private static final VoxelShape SHAPE = new VoxelShapeHelper.Builder().append(VoxelShapes.fullCube(),
            VoxelShapeHelper.makeCuboidShape(1d, 16d, 1d, 3d, 17d, 3d, Direction.EAST),
            VoxelShapeHelper.makeCuboidShape(1d, 16d, 13d, 3d, 17d, 15d, Direction.EAST),
            VoxelShapeHelper.makeCuboidShape(13d, 16d, 13d, 15d, 17d, 15d, Direction.EAST),
            VoxelShapeHelper.makeCuboidShape(13d, 16d, 1d, 15d, 17d, 3d, Direction.EAST)).build();

    public SoulBoxBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(ON, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new SoulBoxTileEntity();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(ON);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof SoulBoxTileEntity)
                player.openContainer((SoulBoxTileEntity) tile);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof SoulBoxTileEntity)
                ((SoulBoxTileEntity) tile).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof SoulBoxTileEntity)
            {
                InventoryHelper.dropInventoryItems(world, pos, (SoulBoxTileEntity) tile);
                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }
}