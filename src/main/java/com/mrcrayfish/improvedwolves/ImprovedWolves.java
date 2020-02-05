package com.mrcrayfish.improvedwolves;

import com.mrcrayfish.improvedwolves.entity.ai.goal.MoveToDogBowlGoal;
import com.mrcrayfish.improvedwolves.entity.ai.goal.TestGoal;
import com.mrcrayfish.improvedwolves.init.ModBlocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MrCrayfish
 */
@Mod(Reference.MOD_ID)
public class ImprovedWolves
{
    public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModBlocks.DOG_BOWL);
        }
    };

    public static void registerGoals(MobEntity entity)
    {
        if(entity instanceof WolfEntity)
        {
            WolfEntity wolfEntity = (WolfEntity) entity;
            wolfEntity.goalSelector.addGoal(1, new MoveToDogBowlGoal(wolfEntity));
            //wolfEntity.goalSelector.addGoal(1, new TestGoal(wolfEntity));
        }
    }
}
