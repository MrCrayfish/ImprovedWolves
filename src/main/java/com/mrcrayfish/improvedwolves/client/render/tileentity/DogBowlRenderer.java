package com.mrcrayfish.improvedwolves.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.improvedwolves.block.DogBowlBlock;
import com.mrcrayfish.improvedwolves.tileentity.DogBowlTileEntity;
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
public class DogBowlRenderer extends TileEntityRenderer<DogBowlTileEntity>
{
    public DogBowlRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DogBowlTileEntity tileEntityIn, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn)
    {
        ItemStack itemStack = tileEntityIn.getStackInSlot(0);
        if(itemStack.isEmpty())
            return;

        BlockState state = tileEntityIn.getBlockState();
        if(!state.has(DogBowlBlock.HORIZONTAL_FACING))
            return;

        int count = (int) Math.ceil(itemStack.getCount() / 16.0);
        for(int i = 0; i < count; i++)
        {
            stack.push();
            stack.translate(0.5, 0.1, 0.5);
            stack.translate(0, i * 0.03125, 0);
            Direction direction = state.get(DogBowlBlock.HORIZONTAL_FACING);
            stack.rotate(Vector3f.YP.rotationDegrees(-direction.getHorizontalAngle()));
            stack.rotate(Vector3f.YP.rotationDegrees(i * 90F));
            stack.rotate(Vector3f.XP.rotationDegrees(90.0F));
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.scale(1.0F, 1.0F, 1.5F);
            Minecraft.getInstance().getItemRenderer().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, stack, buffer);
            stack.pop();
        }
    }
}
