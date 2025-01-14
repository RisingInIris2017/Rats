package com.github.alexthe666.rats.server.potion;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionConfitByaldi extends Potion {

    public static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/potion_effect.png");

    public PotionConfitByaldi() {
        super(false, 0XFFDD59);
        this.setRegistryName(RatsMod.MODID, "synesthesia");
        this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 10.0D, 0).setBeneficial();
    }

    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn.getHealth() < entityLivingBaseIn.getMaxHealth()) {
            entityLivingBaseIn.heal(1.0F);
        }
        if(entityLivingBaseIn instanceof EntityPlayer){
            ((EntityPlayer) entityLivingBaseIn).getFoodStats().addStats(100, 1.0F);
        }
    }

    public boolean isReady(int duration, int amplifier) {
        return duration > 0;
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() - (float)(20 * (amplifier + 1)));
        if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth()) {
            entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
        }
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier){
        entityLivingBaseIn.setAbsorptionAmount(entityLivingBaseIn.getAbsorptionAmount() + (float)(20 * (amplifier + 1)));
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }

    public String getName() {
        return "rats.synesthesia";
    }


    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(x + 6, y + 7, 0, 0, 18, 18);
    }


    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(x + 3, y + 3, 0, 0, 18, 18);

    }

    @SideOnly(Side.CLIENT)
    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (x + 0), (double) (y + height), (double) 200).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), (double) 200).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + 0), (double) 200).tex((double) ((float) (textureX + width) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double) (x + 0), (double) (y + 0), (double) 200).tex((double) ((float) (textureX + 0) * 0.00390625F), (double) ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }


}
