package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCheeseStick extends Item {

    public ItemCheeseStick() {
        super();
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.cheese_stick");
        this.setRegistryName(RatsMod.MODID, "cheese_stick");
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Entity rat = null;
        ItemStack stack = player.getHeldItem(hand);
        if(stack.getTagCompound().getUniqueId("RatUUID") != null){
            if (worldIn.getMinecraftServer() != null) {
                rat = worldIn.getMinecraftServer().getEntityFromUuid(stack.getTagCompound().getUniqueId("RatUUID"));
            }
        }
        if(!worldIn.isRemote){
            if(rat == null || !(rat instanceof EntityRat)){
                player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.no_rat"), true);
                return EnumActionResult.PASS;
            }else{
                EntityRat boundRat = (EntityRat)rat;
                player.swingArm(hand);
                if(worldIn.getTileEntity(pos) == null || worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing) == null){
                    player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.cannot_use_block"), true);
                    return EnumActionResult.PASS;
                }else{
                    IItemHandler itemHandler = worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                    String blockName = worldIn.getBlockState(pos).getBlock().getLocalizedName();
                    if(player.isSneaking()){
                        if(boundRat.pickupPos != null){
                            boundRat.pickupPos = null;
                            player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.not_take_items", rat.getName(), blockName), true);
                            return EnumActionResult.SUCCESS;
                        }else{
                            boundRat.pickupPos = pos;
                            player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.take_items", rat.getName(), blockName), true);
                            return EnumActionResult.SUCCESS;
                        }
                    }else{
                        if(boundRat.depositPos != null){
                            boundRat.depositPos = null;
                            boundRat.depositFacing = EnumFacing.UP;
                            player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.not_deposit_items", rat.getName(), blockName), true);
                            return EnumActionResult.SUCCESS;
                        }else{
                            boundRat.depositPos = pos;
                            boundRat.depositFacing = facing;

                            ITextComponent directionName = new TextComponentTranslation("rats.direction." + facing.getName()).appendText(" ").appendSibling(new TextComponentTranslation("rats.direction.slot"));
                            player.sendStatusMessage(new TextComponentTranslation("entity.rat.staff.deposit_items", rat.getName(), blockName).appendText(" ").appendSibling(directionName), true);
                            return EnumActionResult.SUCCESS;
                        }
                    }

                }
            }
        }
        return EnumActionResult.PASS;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.rats.cheese_stick.desc0"));
        tooltip.add(I18n.format("item.rats.cheese_stick.desc1"));
        tooltip.add(I18n.format("item.rats.cheese_stick.desc2"));
    }
}
