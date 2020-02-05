package com.mrcrayfish.improvedwolves.entity.ai.goal;

import com.mrcrayfish.improvedwolves.init.ModBlocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

/**
 * Author: MrCrayfish
 */
public class TestGoal extends MoveToBlockGoal
{
    public TestGoal(CreatureEntity creature)
    {
        super(creature, creature.getAIMoveSpeed(), 10);
    }

    @Override
    protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock() == ModBlocks.DOG_BOWL;
    }
}
