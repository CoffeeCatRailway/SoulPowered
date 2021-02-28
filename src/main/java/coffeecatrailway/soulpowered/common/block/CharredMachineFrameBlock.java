package coffeecatrailway.soulpowered.common.block;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

/**
 * @author CoffeeCatRailway
 * Created: 19/01/2021
 */
public class CharredMachineFrameBlock extends MachineFrameBlock
{
    private static final Set<Function<BlockPos, BlockPos>> CHARRABLE_POSITIONS = Sets.newHashSet(BlockPos::up, BlockPos::down, BlockPos::north, BlockPos::east, BlockPos::south, BlockPos::west);
    private static final int MAX_CHARRED_LEVEL = 4;

    public static final IntegerProperty CHARRED_LEVEL = IntegerProperty.create("charred_level", 0, MAX_CHARRED_LEVEL);

    public CharredMachineFrameBlock(Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(CHARRED_LEVEL, 0));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(CHARRED_LEVEL);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return super.getStateForPlacement(context).with(CHARRED_LEVEL, 0);
    }

    @Override
    public boolean ticksRandomly(BlockState state)
    {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        int updateFlag = world.getBlockState(pos.down()).isIn(SoulData.TagBlocks.HEAT_BLOCKS_BOTTOM) ? 1 : 0;

        for (Function<BlockPos, BlockPos> posFunc : CHARRABLE_POSITIONS)
            if (world.getBlockState(posFunc.apply(pos)).isIn(SoulData.TagBlocks.HEAT_BLOCKS))
                updateFlag += 1;

        if (updateFlag != 0)
            this.updateCharredLevel(state, world, pos, updateFlag);
    }

    private void updateCharredLevel(BlockState state, ServerWorld world, BlockPos pos, int updateFlag)
    {
        int charredLevel = state.get(CHARRED_LEVEL);
        BlockState newState = null;

        if (charredLevel == MAX_CHARRED_LEVEL)
            newState = SoulBlocks.CHARRED_SIMPLE_MACHINE_FRAME.get().getDefaultState().with(AXIS, state.get(AXIS));
        else
            world.setBlockState(pos, state.with(CHARRED_LEVEL, Math.min(charredLevel + updateFlag, MAX_CHARRED_LEVEL)), Constants.BlockFlags.DEFAULT);

        if (newState != null)
            world.setBlockState(pos, newState, Constants.BlockFlags.DEFAULT);
    }
}
