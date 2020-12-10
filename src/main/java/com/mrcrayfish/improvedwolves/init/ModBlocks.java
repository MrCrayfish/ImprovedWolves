package com.mrcrayfish.improvedwolves.init;

import com.mrcrayfish.improvedwolves.ImprovedWolves;
import com.mrcrayfish.improvedwolves.Reference;
import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import com.mrcrayfish.improvedwolves.block.CatBowlBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks
{
    private static final List<Block> BLOCKS = new ArrayList<>();
    private static final List<Item> ITEMS = new ArrayList<>();

    public static final Block WHITE_DOG_BOWL = register(new DogBowlBlock(DyeColor.WHITE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "white_dog_bowl"));
    public static final Block ORANGE_DOG_BOWL = register(new DogBowlBlock(DyeColor.ORANGE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "orange_dog_bowl"));
    public static final Block MAGENTA_DOG_BOWL = register(new DogBowlBlock(DyeColor.MAGENTA, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "magenta_dog_bowl"));
    public static final Block LIGHT_BLUE_DOG_BOWL = register(new DogBowlBlock(DyeColor.LIGHT_BLUE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "light_blue_dog_bowl"));
    public static final Block YELLOW_DOG_BOWL = register(new DogBowlBlock(DyeColor.YELLOW, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "yellow_dog_bowl"));
    public static final Block LIME_DOG_BOWL = register(new DogBowlBlock(DyeColor.LIME, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "lime_dog_bowl"));
    public static final Block PINK_DOG_BOWL = register(new DogBowlBlock(DyeColor.PINK, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "pink_dog_bowl"));
    public static final Block GRAY_DOG_BOWL = register(new DogBowlBlock(DyeColor.GRAY, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "gray_dog_bowl"));
    public static final Block LIGHT_GRAY_DOG_BOWL = register(new DogBowlBlock(DyeColor.LIGHT_GRAY, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "light_gray_dog_bowl"));
    public static final Block CYAN_DOG_BOWL = register(new DogBowlBlock(DyeColor.CYAN, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "cyan_dog_bowl"));
    public static final Block PURPLE_DOG_BOWL = register(new DogBowlBlock(DyeColor.PURPLE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "purple_dog_bowl"));
    public static final Block BLUE_DOG_BOWL = register(new DogBowlBlock(DyeColor.BLUE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "blue_dog_bowl"));
    public static final Block BROWN_DOG_BOWL = register(new DogBowlBlock(DyeColor.BROWN, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "brown_dog_bowl"));
    public static final Block GREEN_DOG_BOWL = register(new DogBowlBlock(DyeColor.GREEN, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "green_dog_bowl"));
    public static final Block RED_DOG_BOWL = register(new DogBowlBlock(DyeColor.RED, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "red_dog_bowl"));
    public static final Block BLACK_DOG_BOWL = register(new DogBowlBlock(DyeColor.BLACK, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "black_dog_bowl"));
        //Cats
    public static final Block WHITE_Cat_BOWL = register(new CatBowlBlock(DyeColor.WHITE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "white_cat_bowl"));
    public static final Block ORANGE_Cat_BOWL = register(new CatBowlBlock(DyeColor.ORANGE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "orange_cat_bowl"));
    public static final Block MAGENTA_Cat_BOWL = register(new CatBowlBlock(DyeColor.MAGENTA, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "magenta_cat_bowl"));
    public static final Block LIGHT_BLUE_Cat_BOWL = register(new CatBowlBlock(DyeColor.LIGHT_BLUE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "light_blue_cat_bowl"));
    public static final Block YELLOW_Cat_BOWL = register(new CatBowlBlock(DyeColor.YELLOW, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "yellow_cat_bowl"));
    public static final Block LIME_Cat_BOWL = register(new CatBowlBlock(DyeColor.LIME, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "lime_cat_bowl"));
    public static final Block PINK_Cat_BOWL = register(new CatBowlBlock(DyeColor.PINK, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "pink_cat_bowl"));
    public static final Block GRAY_Cat_BOWL = register(new CatBowlBlock(DyeColor.GRAY, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "gray_cat_bowl"));
    public static final Block LIGHT_GRAY_Cat_BOWL = register(new CatBowlBlock(DyeColor.LIGHT_GRAY, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "light_gray_cat_bowl"));
    public static final Block CYAN_Cat_BOWL = register(new CatBowlBlock(DyeColor.CYAN, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "cyan_cat_bowl"));
    public static final Block PURPLE_Cat_BOWL = register(new CatBowlBlock(DyeColor.PURPLE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "purple_cat_bowl"));
    public static final Block BLUE_Cat_BOWL = register(new CatBowlBlock(DyeColor.BLUE, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "blue_cat_bowl"));
    public static final Block BROWN_Cat_BOWL = register(new CatBowlBlock(DyeColor.BROWN, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "brown_cat_bowl"));
    public static final Block GREEN_Cat_BOWL = register(new CatBowlBlock(DyeColor.GREEN, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "green_cat_bowl"));
    public static final Block RED_Cat_BOWL = register(new CatBowlBlock(DyeColor.RED, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "red_cat_bowl"));
    public static final Block BLACK_Cat_BOWL = register(new CatBowlBlock(DyeColor.BLACK, Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).notSolid()).setRegistryName(Reference.MOD_ID, "black_cat_bowl"));

    private static Block register(Block block)
    {
        return register(block, block1 -> new BlockItem(block1, new Item.Properties().group(ImprovedWolves.GROUP)));
    }

    private static Block register(Block block, @Nullable Function<Block, BlockItem> supplier)
    {
        if(block.getRegistryName() == null)
        {
            throw new IllegalArgumentException("A block being registered does not have a registry name and could be registered.");
        }

        BLOCKS.add(block);
        if(supplier != null)
        {
            BlockItem item = supplier.apply(block);
            item.setRegistryName(block.getRegistryName());
            ITEMS.add(item);
        }
        return block;
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event)
    {
        BLOCKS.forEach(block -> event.getRegistry().register(block));
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event)
    {
        ITEMS.forEach(block -> event.getRegistry().register(block));
    }
}
