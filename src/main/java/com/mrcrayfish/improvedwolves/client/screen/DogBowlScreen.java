package com.mrcrayfish.improvedwolves.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.improvedwolves.inventory.container.DogBowlContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * Author: MrCrayfish
 */
public class DogBowlScreen extends ContainerScreen<DogBowlContainer>
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("improvedwolves:textures/gui/container/dog_bowl.png");

    public DogBowlScreen(DogBowlContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.xSize = 176;
        this.ySize = 133;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String title = this.title.getFormattedText();
        this.font.drawString(title, (float)(this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }
}
