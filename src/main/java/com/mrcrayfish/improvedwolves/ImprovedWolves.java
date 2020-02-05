package com.mrcrayfish.improvedwolves;

import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import com.mrcrayfish.improvedwolves.client.screen.DogBowlScreen;
import com.mrcrayfish.improvedwolves.entity.ai.goal.MoveToDogBowlGoal;
import com.mrcrayfish.improvedwolves.init.ModBlocks;
import com.mrcrayfish.improvedwolves.init.ModContainers;
import com.mrcrayfish.improvedwolves.tileentity.DogBowlTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
            return new ItemStack(ModBlocks.RED_DOG_BOWL);
        }
    };

    public ImprovedWolves()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        Minecraft.getInstance().getItemColors().register((stack, index) -> {
            Block block = Block.getBlockFromItem(stack.getItem());
            return block instanceof DogBowlBlock ? ((DogBowlBlock) block).getColor().getColorValue() : 0xFFFFFF;
        }, ModBlocks.WHITE_DOG_BOWL, ModBlocks.ORANGE_DOG_BOWL, ModBlocks.MAGENTA_DOG_BOWL, ModBlocks.LIGHT_BLUE_DOG_BOWL, ModBlocks.YELLOW_DOG_BOWL, ModBlocks.LIME_DOG_BOWL, ModBlocks.PINK_DOG_BOWL, ModBlocks.GRAY_DOG_BOWL, ModBlocks.LIGHT_GRAY_DOG_BOWL, ModBlocks.CYAN_DOG_BOWL, ModBlocks.PURPLE_DOG_BOWL, ModBlocks.BLUE_DOG_BOWL, ModBlocks.BROWN_DOG_BOWL, ModBlocks.GREEN_DOG_BOWL, ModBlocks.RED_DOG_BOWL, ModBlocks.BLACK_DOG_BOWL);

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, index) -> {
            Block block = state.getBlock();
            return block instanceof DogBowlBlock ? ((DogBowlBlock) block).getColor().getColorValue() : 0xFFFFFF;
        }, ModBlocks.WHITE_DOG_BOWL, ModBlocks.ORANGE_DOG_BOWL, ModBlocks.MAGENTA_DOG_BOWL, ModBlocks.LIGHT_BLUE_DOG_BOWL, ModBlocks.YELLOW_DOG_BOWL, ModBlocks.LIME_DOG_BOWL, ModBlocks.PINK_DOG_BOWL, ModBlocks.GRAY_DOG_BOWL, ModBlocks.LIGHT_GRAY_DOG_BOWL, ModBlocks.CYAN_DOG_BOWL, ModBlocks.PURPLE_DOG_BOWL, ModBlocks.BLUE_DOG_BOWL, ModBlocks.BROWN_DOG_BOWL, ModBlocks.GREEN_DOG_BOWL, ModBlocks.RED_DOG_BOWL, ModBlocks.BLACK_DOG_BOWL);

        ScreenManager.registerFactory(ModContainers.DOG_BOWL, DogBowlScreen::new);
    }

    public static void registerGoals(MobEntity entity)
    {
        if(entity instanceof WolfEntity)
        {
            WolfEntity wolfEntity = (WolfEntity) entity;
            wolfEntity.goalSelector.addGoal(6, new MoveToDogBowlGoal(wolfEntity));
        }
    }
}
