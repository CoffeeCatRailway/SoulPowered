package coffeecatrailway.soulpowered.common.block;

import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.tileentity.SoulBoxTileEntity;
import io.github.ocelot.sonar.common.util.VoxelShapeHelper;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 14/11/2020
 */
public class SoulBoxBlock extends AbstractMachineBlock
{
    private static final VoxelShape SHAPE = new VoxelShapeHelper.Builder().append(VoxelShapes.block(),
            VoxelShapeHelper.makeCuboidShape(1d, 16d, 1d, 3d, 17d, 3d, Direction.EAST),
            VoxelShapeHelper.makeCuboidShape(1d, 16d, 13d, 3d, 17d, 15d, Direction.EAST),
            VoxelShapeHelper.makeCuboidShape(13d, 16d, 13d, 15d, 17d, 15d, Direction.EAST),
            VoxelShapeHelper.makeCuboidShape(13d, 16d, 1d, 15d, 17d, 3d, Direction.EAST)).build();

    private final Tier tier;

    public SoulBoxBlock(Properties properties, Tier tier)
    {
        super(properties);
        this.tier = tier;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader world)
    {
        return new SoulBoxTileEntity(this.tier);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }
}
