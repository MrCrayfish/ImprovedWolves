package com.mrcrayfish.improvedwolves.entity.ai.goal;

import com.mrcrayfish.improvedwolves.init.ModBlocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class MoveToDogBowlGoal extends Goal
{
    private int eatingTicks;
    private BlockPos bowlPos;
    private TameableEntity entity;

    public MoveToDogBowlGoal(TameableEntity entity)
    {
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean shouldExecute()
    {
        if(!this.entity.isTamed() || this.entity.isSitting() || this.entity.getHealth() >= this.entity.getMaxHealth())
        {
            return false;
        }
        this.findNearestDogBowl();
        return this.bowlPos != null;
    }

    @Override
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.bowlPos.getX() + 0.5, this.bowlPos.getY(), this.bowlPos.getZ() + 0.5, 1);
    }

    @Override
    public void tick()
    {
        if(!this.bowlPos.withinDistance(this.entity.getPositionVec(), 1.5))
        {
            this.entity.getNavigator().tryMoveToXYZ(this.bowlPos.getX() + 0.5, this.bowlPos.getY(), this.bowlPos.getZ() + 0.5, 1);
        }
        else
        {
            this.entity.getNavigator().clearPath();
            this.entity.getLookController().setLookPosition(this.bowlPos.getX() + 0.5, this.bowlPos.getY(), this.bowlPos.getZ() + 0.5);
            this.eatingTicks++;
            if(this.eatingTicks == 20)
            {
                //TODO make this depend on the food
                this.entity.setHealth(this.entity.getMaxHealth());
                this.eatingTicks = 0;
            }
            else
            {
                World world = this.entity.getEntityWorld();
                if(world instanceof ServerWorld)
                {
                    ServerWorld serverWorld = (ServerWorld) world;
                    ItemStack food = new ItemStack(Items.COOKED_BEEF);
                    serverWorld.spawnParticle(new ItemParticleData(ParticleTypes.ITEM, food), this.bowlPos.getX() + 0.5, this.bowlPos.getY() + 0.0625 * 4, this.bowlPos.getZ() + 0.5, 2, 0.05, 0, 0.05, 0.05);
                    if(this.eatingTicks % 4 == 0)
                    {
                        serverWorld.playSound(null, this.bowlPos, food.func_226630_G_(), SoundCategory.NEUTRAL, 0.5F + 0.5F * serverWorld.rand.nextInt(2), (serverWorld.rand.nextFloat() - serverWorld.rand.nextFloat()) * 0.2F + 1.0F);
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return this.entity.getHealth() < this.entity.getMaxHealth();
    }

    @Override
    public void resetTask()
    {
        this.eatingTicks = 0;
        this.bowlPos = null;
    }

    private void findNearestDogBowl()
    {
        BlockPos center = new BlockPos(this.entity);
        List<BlockPos> dogBowls = new ArrayList<>();
        BlockPos.getAllInBox(center.add(-10, -10, -10), center.add(10, 10, 10)).forEach(pos -> {
            if(this.entity.getEntityWorld().getBlockState(pos).getBlock() == ModBlocks.DOG_BOWL)
            {
                dogBowls.add(pos.toImmutable());
            }
        });
        this.bowlPos = dogBowls.stream().min(Comparator.comparing(pos -> pos.manhattanDistance(center))).orElse(null);
    }
}
