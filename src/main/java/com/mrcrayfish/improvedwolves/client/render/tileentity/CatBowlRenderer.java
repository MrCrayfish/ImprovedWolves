package com.mrcrayfish.improvedwolves.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.improvedwolves.block.CatBowlBlock;
import com.mrcrayfish.improvedwolves.tileentity.CatBowlTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

/**
 * Author: MrCrayfish
 */
public class CatBowlRenderer extends TileEntityRenderer<CatBowlTileEntity>
{
    public CatBowlRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(CatBowlTileEntity tileEntityIn, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
    {
        BlockState state = tileEntityIn.getBlockState();
        if(!state.has(CatBowlBlock.HORIZONTAL_FACING))
            return;

        ItemStack itemStack = tileEntityIn.getStackInSlot(0);
        if(!itemStack.isEmpty())
        {
            int count = (int) Math.ceil(itemStack.getCount() / 16.0);
            for(int i = 0; i < count; i++)
            {
                stack.push();
                stack.translate(0.5, 0.1, 0.5);
                stack.translate(0, i * 0.03125, 0);
                Direction direction = state.get(CatBowlBlock.HORIZONTAL_FACING);
                stack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
                stack.rotate(Vector3f.YP.rotationDegrees(i * 90F));
                stack.rotate(Vector3f.XP.rotationDegrees(90.0F));
                stack.scale(0.5F, 0.5F, 0.5F);
                stack.scale(1.0F, 1.0F, 1.5F);
                Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, stack, buffer);
                stack.pop();
            }
        }

        if(tileEntityIn.hasCustomName())
        {
            stack.push();
            stack.translate(0.5, 0, 0.5);
            Direction direction = state.get(CatBowlBlock.HORIZONTAL_FACING);
            stack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            stack.translate(0, 0.145, -0.3125 - 0.001);
            stack.scale(-1, -1, 0);
            String text = tileEntityIn.getDisplayName().getFormattedText();
            int width = this.renderDispatcher.fontRenderer.getStringWidth(text);
            float scale = 0.4375F / width;
            stack.scale(scale, scale, scale);
            stack.translate(-width / 2.0, -4.5, 0);
            this.renderDispatcher.fontRenderer.renderString(text, 0, 0, 0xFFFFFF, false, stack.getLast().getPositionMatrix(), buffer, false, 0, combinedLightIn);
            stack.pop();
        }
    }
}
