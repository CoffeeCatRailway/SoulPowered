package coffeecatrailway.soulpowered.common.block;

import io.github.ocelot.sonar.common.util.VoxelShapeHelper;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.NonNullFunction;

/**
 * @author CoffeeCatRailway
 * Created: 15/11/2020
 */
public class MachineFrameBlock extends RotatedPillarBlock
{
    private static final VoxelShape SHAPE = generateShape();

    public MachineFrameBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    private static VoxelShape generateShape()
    {
        NonNullFunction<Direction, VoxelShape> frameBottomPart = dir -> VoxelShapeHelper.makeCuboidShape(0d, 0d, 0d, 16d, 4d, 4d, dir);
        NonNullFunction<Direction, VoxelShape> frameSidePart = dir -> VoxelShapeHelper.makeCuboidShape(0d, 4d, 0d, 4d, 12d, 4d, dir);
        NonNullFunction<Direction, VoxelShape> frameTopPart = dir -> VoxelShapeHelper.makeCuboidShape(0d, 12d, 0d, 16d, 16d, 4d, dir);
        VoxelShapeHelper.Builder builder = new VoxelShapeHelper.Builder();

        Direction.Plane.HORIZONTAL.forEach(dir -> builder.append(frameBottomPart.apply(dir)).append(frameSidePart.apply(dir)).append(frameTopPart.apply(dir)));

        return builder.build();
    }
}
