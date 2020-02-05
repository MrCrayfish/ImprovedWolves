package com.mrcrayfish.improvedwolves.client;

import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import com.mrcrayfish.improvedwolves.client.render.tileentity.DogBowlRenderer;
import com.mrcrayfish.improvedwolves.client.screen.DogBowlScreen;
import com.mrcrayfish.improvedwolves.init.ModBlocks;
import com.mrcrayfish.improvedwolves.init.ModContainers;
import com.mrcrayfish.improvedwolves.init.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Author: MrCrayfish
 */
public class ClientHandler
{
    public static void setup()
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
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.DOG_BOWL, DogBowlRenderer::new);
    }
}
