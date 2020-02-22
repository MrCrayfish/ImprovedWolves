package com.mrcrayfish.improvedwolves.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.improvedwolves.Reference;
import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import com.mrcrayfish.improvedwolves.block.CatBowlBlock;
import com.mrcrayfish.improvedwolves.client.render.tileentity.DogBowlRenderer;
import com.mrcrayfish.improvedwolves.client.render.tileentity.CatBowlRenderer;
import com.mrcrayfish.improvedwolves.client.screen.DogBowlScreen;
import com.mrcrayfish.improvedwolves.client.screen.CatBowlScreen;
import com.mrcrayfish.improvedwolves.common.CustomDataParameters;
import com.mrcrayfish.improvedwolves.init.ModBlocks;
import com.mrcrayfish.improvedwolves.init.ModContainers;
import com.mrcrayfish.improvedwolves.init.ModTileEntities;
import com.mrcrayfish.improvedwolves.network.PacketHandler;
import com.mrcrayfish.improvedwolves.network.message.MessageWolfDepositItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ClientHandler
{
    public static final KeyBinding KEY_COMMAND_DEPOSIT = new KeyBinding("key.improvedwolves.deposit", GLFW.GLFW_KEY_U, "key.categories.improvedwolves");

    public static void setup()
    {
        Minecraft.getInstance().getItemColors().register((stack, index) -> {
            Block block = Block.getBlockFromItem(stack.getItem());
            return block instanceof DogBowlBlock ? ((DogBowlBlock) block).getColor().getColorValue() : 0xFFFFFF;
        }, ModBlocks.WHITE_DOG_BOWL, ModBlocks.ORANGE_DOG_BOWL, ModBlocks.MAGENTA_DOG_BOWL, ModBlocks.LIGHT_BLUE_DOG_BOWL, ModBlocks.YELLOW_DOG_BOWL, ModBlocks.LIME_DOG_BOWL, ModBlocks.PINK_DOG_BOWL, ModBlocks.GRAY_DOG_BOWL, ModBlocks.LIGHT_GRAY_DOG_BOWL, ModBlocks.CYAN_DOG_BOWL, ModBlocks.PURPLE_DOG_BOWL, ModBlocks.BLUE_DOG_BOWL, ModBlocks.BROWN_DOG_BOWL, ModBlocks.GREEN_DOG_BOWL, ModBlocks.RED_DOG_BOWL, ModBlocks.BLACK_DOG_BOWL);
        Minecraft.getInstance().getItemColors().register((stack, index) -> {
            Block block = Block.getBlockFromItem(stack.getItem());
            return block instanceof CatBowlBlock ? ((CatBowlBlock) block).getColor().getColorValue() : 0xFFFFFF;
        }, ModBlocks.WHITE_Cat_BOWL, ModBlocks.ORANGE_Cat_BOWL, ModBlocks.MAGENTA_Cat_BOWL, ModBlocks.LIGHT_BLUE_Cat_BOWL, ModBlocks.YELLOW_Cat_BOWL, ModBlocks.LIME_Cat_BOWL, ModBlocks.PINK_Cat_BOWL, ModBlocks.GRAY_Cat_BOWL, ModBlocks.LIGHT_GRAY_Cat_BOWL, ModBlocks.CYAN_Cat_BOWL, ModBlocks.PURPLE_Cat_BOWL, ModBlocks.BLUE_Cat_BOWL, ModBlocks.BROWN_Cat_BOWL, ModBlocks.GREEN_Cat_BOWL, ModBlocks.RED_Cat_BOWL, ModBlocks.BLACK_Cat_BOWL);

        Minecraft.getInstance().getBlockColors().register((state, reader, pos, index) -> {
            Block block = state.getBlock();
            return block instanceof DogBowlBlock ? ((DogBowlBlock) block).getColor().getColorValue() : 0xFFFFFF;
        }, ModBlocks.WHITE_DOG_BOWL, ModBlocks.ORANGE_DOG_BOWL, ModBlocks.MAGENTA_DOG_BOWL, ModBlocks.LIGHT_BLUE_DOG_BOWL, ModBlocks.YELLOW_DOG_BOWL, ModBlocks.LIME_DOG_BOWL, ModBlocks.PINK_DOG_BOWL, ModBlocks.GRAY_DOG_BOWL, ModBlocks.LIGHT_GRAY_DOG_BOWL, ModBlocks.CYAN_DOG_BOWL, ModBlocks.PURPLE_DOG_BOWL, ModBlocks.BLUE_DOG_BOWL, ModBlocks.BROWN_DOG_BOWL, ModBlocks.GREEN_DOG_BOWL, ModBlocks.RED_DOG_BOWL, ModBlocks.BLACK_DOG_BOWL);
        
        Minecraft.getInstance().getBlockColors().register((state, reader, pos, index) -> {
            Block block = state.getBlock();
            return block instanceof CatBowlBlock ? ((CatBowlBlock) block).getColor().getColorValue() : 0xFFFFFF;
        }, ModBlocks.WHITE_Cat_BOWL, ModBlocks.ORANGE_Cat_BOWL, ModBlocks.MAGENTA_Cat_BOWL, ModBlocks.LIGHT_BLUE_Cat_BOWL, ModBlocks.YELLOW_Cat_BOWL, ModBlocks.LIME_Cat_BOWL, ModBlocks.PINK_Cat_BOWL, ModBlocks.GRAY_Cat_BOWL, ModBlocks.LIGHT_GRAY_Cat_BOWL, ModBlocks.CYAN_Cat_BOWL, ModBlocks.PURPLE_Cat_BOWL, ModBlocks.BLUE_Cat_BOWL, ModBlocks.BROWN_Cat_BOWL, ModBlocks.GREEN_Cat_BOWL, ModBlocks.RED_Cat_BOWL, ModBlocks.BLACK_Cat_BOWL);

        ScreenManager.registerFactory(ModContainers.DOG_BOWL, DogBowlScreen::new);
        ScreenManager.registerFactory(ModContainers.Cat_BOWL, CatBowlScreen::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.DOG_BOWL, DogBowlRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.Cat_BOWL, CatBowlRenderer::new);
        ClientRegistry.registerKeyBinding(KEY_COMMAND_DEPOSIT);
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event)
    {
        if(event.getAction() == GLFW.GLFW_PRESS && event.getKey() == KEY_COMMAND_DEPOSIT.getKey().getKeyCode())
        {
            if(Minecraft.getInstance().player != null)
            {
                RayTraceResult result = Minecraft.getInstance().player.pick(20.0D, Minecraft.getInstance().getRenderPartialTicks(), false);
                if(result.getType() == RayTraceResult.Type.BLOCK || result.getType() == RayTraceResult.Type.MISS)
                {
                    BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
                    BlockPos pos = blockRayTraceResult.getPos();
                    if(result.getType() == RayTraceResult.Type.MISS || pos.withinDistance(Minecraft.getInstance().player.getEyePosition(0F), 20.0D))
                    {
                        PacketHandler.instance.sendToServer(new MessageWolfDepositItem(pos, result.getType() == RayTraceResult.Type.MISS));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderWolf(RenderLivingEvent.Post event)
    {
        if(event.getEntity() instanceof WolfEntity)
        {
            WolfEntity wolf = (WolfEntity) event.getEntity();
            ItemStack heldItem = wolf.getDataManager().get(CustomDataParameters.WOLF_HELD_ITEM);
            if(!heldItem.isEmpty())
            {
                MatrixStack matrix = event.getMatrixStack();
                matrix.push();
                matrix.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(event.getPartialRenderTick(), -wolf.prevRenderYawOffset, -wolf.renderYawOffset)));
                matrix.translate(-1 * 0.0625, 0, 7 * 0.0625);
                matrix.rotate(Vector3f.YN.rotationDegrees(MathHelper.lerp(event.getPartialRenderTick(), -wolf.prevRenderYawOffset, -wolf.renderYawOffset)));
                matrix.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(event.getPartialRenderTick(), -wolf.prevRotationYawHead, -wolf.rotationYawHead)));
                matrix.rotate(Vector3f.XP.rotationDegrees(MathHelper.lerp(event.getPartialRenderTick(), wolf.prevRotationPitch, wolf.rotationPitch)));
                matrix.translate(1 * 0.0625, 8.5 * 0.0625, 5 * 0.0625);
                if(heldItem.getItem() instanceof BlockItem)
                {
                    matrix.translate(0, -1.0 * 0.0625, 0);
                    matrix.rotate(Vector3f.XP.rotationDegrees(75F));
                    matrix.scale(0.2F, 0.2F, 0.2F);
                }
                else
                {
                    matrix.scale(0.4F, 0.4F, 0.4F);
                }
                matrix.rotate(Vector3f.YP.rotationDegrees(90F));
                matrix.rotate(Vector3f.XP.rotationDegrees(90F));
                Minecraft.getInstance().getItemRenderer().renderItem(heldItem, ItemCameraTransforms.TransformType.NONE, event.getLight(), OverlayTexture.DEFAULT_LIGHT, matrix, event.getBuffers());
                matrix.pop();
            }
        }
    }

}
