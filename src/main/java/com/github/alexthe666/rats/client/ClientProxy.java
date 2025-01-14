package com.github.alexthe666.rats.client;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.gui.GuiRat;
import com.github.alexthe666.rats.client.model.ModelChefToque;
import com.github.alexthe666.rats.client.render.entity.RenderRat;
import com.github.alexthe666.rats.client.render.tile.RatsTEISR;
import com.github.alexthe666.rats.client.render.tile.RenderRatHole;
import com.github.alexthe666.rats.client.render.tile.RenderRatTrap;
import com.github.alexthe666.rats.server.CommonProxy;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.compat.TinkersCompatBridge;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {
    @SideOnly(Side.CLIENT)
    private static final RatsTEISR TEISR = new RatsTEISR();
    @SideOnly(Side.CLIENT)
    private static final ModelChefToque MODEL_CHEF_TOQUE = new ModelChefToque(1.0F);


    public void preInit() {
        TinkersCompatBridge.loadTinkersClientCompat();
    }

    @SideOnly(Side.CLIENT)
    public void init(){
        RenderingRegistry.registerEntityRenderingHandler(EntityRat.class, new RenderRat());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatHole.class, new RenderRatHole());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRatTrap.class, new RenderRatTrap());
        Item.getItemFromBlock(RatsBlockRegistry.RAT_HOLE).setTileEntityItemStackRenderer(TEISR);
        Item.getItemFromBlock(RatsBlockRegistry.RAT_TRAP).setTileEntityItemStackRenderer(TEISR);
    }

    public void postInit() {

    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_TRAP, (new StateMap.Builder()).ignore(BlockRatTrap.FACING).build());
        ModelLoader.setCustomStateMapper(RatsBlockRegistry.RAT_HOLE, (new StateMap.Builder()).ignore(BlockRatHole.NORTH, BlockRatHole.EAST, BlockRatHole.SOUTH, BlockRatHole.WEST).build());
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block && !(obj instanceof ICustomRendered)) {
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock((Block)obj), 0, new ModelResourceLocation("rats:" + ((Block)obj).getRegistryName().getPath(), "inventory"));
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        if(!(block instanceof ICustomRendered)){
                            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation("rats:" + block.getRegistryName().getPath(), "inventory"));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            for (Field f : RatsItemRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item && !(obj instanceof ICustomRendered)) {
                    ModelLoader.setCustomModelResourceLocation((Item)obj, 0, new ModelResourceLocation("rats:" + ((Item)obj).getRegistryName().getPath(), "inventory"));
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        if(!(item instanceof ICustomRendered)){
                            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("rats:" + item.getRegistryName().getPath(), "inventory"));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ResourceLocation SYNESTHESIA = new ResourceLocation("rats:shaders/post/synesthesia.json");
    private float synesthesiaProgress = 0;
    private float prevSynesthesiaProgress = 0;
    private float maxSynesthesiaProgress = 40;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() == Minecraft.getMinecraft().player) {
            EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
            PotionEffect active = event.getEntityLiving().getActivePotionEffect(RatsMod.CONFIT_BYALDI_POTION);
            boolean synesthesia = active != null;
            if (synesthesia && !renderer.isShaderActive()) {
                renderer.loadShader(SYNESTHESIA);
            }
            if (!synesthesia && renderer.isShaderActive()) {
                renderer.stopUseShader();
            }
            if(prevSynesthesiaProgress == 2 && synesthesia){
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RatsSoundRegistry.POTION_EFFECT_BEGIN, 1.0F));
            }
            if(prevSynesthesiaProgress == 38 && !synesthesia){
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RatsSoundRegistry.POTION_EFFECT_END, 1.0F));
            }
            prevSynesthesiaProgress = synesthesiaProgress;
            if (synesthesia && synesthesiaProgress < maxSynesthesiaProgress) {
                synesthesiaProgress += 2F;
            } else if (!synesthesia && synesthesiaProgress > 0.0F) {
                synesthesiaProgress -= 2F;
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGetFOVModifier(EntityViewRenderEvent.FOVModifier event) {
        if(event.getEntity() == Minecraft.getMinecraft().player && prevSynesthesiaProgress > 0){
            float prog = (prevSynesthesiaProgress + (synesthesiaProgress - prevSynesthesiaProgress) * LLibrary.PROXY.getPartialTicks());
            float renderProg;
            if(prevSynesthesiaProgress > synesthesiaProgress){
                renderProg = (float)Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            }else{
                renderProg = -(float)Math.sin(prog / maxSynesthesiaProgress * Math.PI) * 40F;
            }
            event.setFOV(event.getFOV() + renderProg);
        }
    }

    public boolean shouldRenderNameplates() {
        return Minecraft.getMinecraft().currentScreen == null || !(Minecraft.getMinecraft().currentScreen instanceof GuiRat);
    }

    @SideOnly(Side.CLIENT)
    public Object getToqueModel() {
        return new ModelChefToque(1.0F);
    }

}
