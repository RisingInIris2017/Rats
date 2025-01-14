package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class LayerRatHeldItem implements LayerRenderer<EntityRat> {
    private static ItemStack PLATTER_STACK = new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
    private static ItemStack AXE_STACK = new ItemStack(Items.STONE_AXE);
    private static ItemStack PICKAXE_STACK = new ItemStack(Items.STONE_PICKAXE);
    RenderRat renderer;

    public LayerRatHeldItem(RenderRat renderer) {
        this.renderer = renderer;
    }

    public void doRenderLayer(EntityRat entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!(this.renderer.getMainModel() instanceof  ModelRat)){
            return;
        }
        ItemStack itemstack = entity.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack upgradeStack = entity.getHeldItem(EnumHand.OFF_HAND);
        if (!itemstack.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();

            if (this.renderer.getMainModel().isChild) {
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getMinecraft();
            if (entity.holdInMouth && entity.getAnimation() != EntityRat.ANIMATION_EAT && entity.cookingProgress <= 0 && upgradeStack.getItem() != RatsItemRegistry.RAT_UPGRADE_PLATTER) {
                translateToHead();
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(0F, 0.25, 0.05F);
            } else {
                translateToHand(true);
                GlStateManager.rotate(190.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.translate(-0.155F, -0.025, 0.125F);
                if(upgradeStack.getItem() == RatsItemRegistry.RAT_UPGRADE_PLATTER){
                    GlStateManager.translate(0F, 0.25F, 0F);
                    if(itemstack.getItem() instanceof ItemBlock){
                        GlStateManager.rotate(-90, 1.0F, 0.0F, 0.0F);
                    }else{
                        GlStateManager.translate(0F, -0.1F, -0.075F);

                    }

                }
            }
            minecraft.getItemRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
        if(!upgradeStack.isEmpty() && upgradeStack.getItem() == RatsItemRegistry.RAT_UPGRADE_PLATTER){
            if (this.renderer.getMainModel().isChild) {
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                float f = 0.5F;
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }
            Minecraft minecraft = Minecraft.getMinecraft();
            translateToHand(true);
            GlStateManager.rotate(190.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-70.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(-0.155F, -0.225, 0.2F);
            GlStateManager.scale(2F, 2F, 2F);
            minecraft.getItemRenderer().renderItem(entity, PLATTER_STACK, ItemCameraTransforms.TransformType.GROUND);
        }
        if(!upgradeStack.isEmpty() && upgradeStack.getItem() == RatsItemRegistry.RAT_UPGRADE_CRAFTING){
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            translateToHand(true);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, AXE_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            translateToHand(false);
            GlStateManager.rotate(-90F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            minecraft.getItemRenderer().renderItem(entity, PICKAXE_STACK, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }
    }

    protected void translateToHead() {
        ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).body2.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).neck.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).head.postRender(0.0625F);
    }

    protected void translateToHand(boolean left) {
        ((ModelRat) this.renderer.getMainModel()).body1.postRender(0.0625F);
        ((ModelRat) this.renderer.getMainModel()).body2.postRender(0.0625F);
        if(left){
            ((ModelRat) this.renderer.getMainModel()).leftArm.postRender(0.0625F);
            ((ModelRat) this.renderer.getMainModel()).leftHand.postRender(0.0625F);
        }else{
            ((ModelRat) this.renderer.getMainModel()).rightArm.postRender(0.0625F);
            ((ModelRat) this.renderer.getMainModel()).rightHand.postRender(0.0625F);
        }
    }


    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
