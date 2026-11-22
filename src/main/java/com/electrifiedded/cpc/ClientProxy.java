package com.electrifiedded.cpc;

import com.electrifiedded.cpc.block.ContainerLaptop;
import com.electrifiedded.cpc.block.TileEntityLaptop;
import com.electrifiedded.cpc.client.model.*;
import com.electrifiedded.cpc.entity.*;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;
import java.util.function.Supplier;

@SideOnly(Side.CLIENT)
public final class ClientProxy {
    private ClientProxy() {}

    private static ResourceLocation tex(String p) {
        return new ResourceLocation(Reference.MOD_ID, "textures/entity/" + p + ".png");
    }

    static final class Simple<T extends EntityLiving> extends RenderLiving<T> {
        private final Function<T, ResourceLocation> texture;

        Simple(net.minecraft.client.renderer.entity.RenderManager m, ModelBase model, Function<T, ResourceLocation> texture) {
            super(m, model, 0.5F);
            this.texture = texture;
        }

        @Override
        protected ResourceLocation getEntityTexture(T e) {
            return texture.apply(e);
        }
    }

    private static <T extends EntityLiving> void render(Class<T> c, Supplier<ModelBase> model, String texture) {
        ResourceLocation rl = tex(texture);
        RenderingRegistry.registerEntityRenderingHandler(c, m -> new Simple<T>(m, model.get(), e -> rl));
    }

    public static void preInit() {
        render(EntityJeff.class, ModelJeff::new, "jeff");
        render(EntityJane.class, ModelJane::new, "jane");
        render(EntityPewds.class, ModelBiped::new, "pewds");
        render(EntityCry.class, ModelBiped::new, "cry");
        render(EntityJack.class, ModelBiped::new, "jack");
        render(EntityRake.class, ModelRake::new, "rake");
        render(EntitySeed.class, ModelSeedEater::new, "seed");
        render(EntityMothman.class, ModelMothman::new, "mothman");
        render(EntityStrider.class, ModelStrider::new, "strider");
        render(EntitySquidward.class, ModelSquidward::new, "squidward/squidwardhappy");
        render(EntitySquidwardSuicide.class, ModelSquidward::new, "squidward/squidwardsuicide");
        render(EntitySquidwardMad.class, ModelSquidward::new, "squidward/squidwardmad");
        ResourceLocation idle = tex("smiledog/idle"), angry = tex("smiledog/angry");
        RenderingRegistry.registerEntityRenderingHandler(EntitySmile.class,
                m -> new Simple<EntitySmile>(m, new ModelSmileDog(), e -> e.isAngry() ? angry : idle));
        RenderingRegistry.registerEntityRenderingHandler(EntityStephano.class,
                m -> new RenderSnowball<>(m, CPC.stephano, Minecraft.getMinecraft().getRenderItem()));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaptop.class, new LaptopRenderer());
        MinecraftForge.EVENT_BUS.register(ClientProxy.class);
    }

    @SubscribeEvent
    public static void models(ModelRegistryEvent e) {
        for (Item i : new Item[]{CPC.horror, CPC.jeffKnife, CPC.hiltBlack, CPC.diaper, CPC.stephano, CPC.pasta, CPC.recordLavender,
                CPC.smileJpg, Item.getItemFromBlock(CPC.laptop)})
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
        ModelResourceLocation arch = new ModelResourceLocation(CPC.archive.getRegistryName(), "inventory");
        for (int m = 0; m < 16; m++) ModelLoader.setCustomModelResourceLocation(CPC.archive, m, arch);
    }

    public static Object laptopGui(EntityPlayer p, TileEntityLaptop te) {
        return new GuiLaptop(new ContainerLaptop(p.inventory, te), te);
    }

    static final class GuiLaptop extends GuiContainer {
        private static final ResourceLocation BG = new ResourceLocation(Reference.MOD_ID, "textures/gui/laptop.png");
        private final TileEntityLaptop te;

        GuiLaptop(ContainerLaptop c, TileEntityLaptop te) {
            super(c);
            this.te = te;
            ySize = 166;
        }

        @Override
        public void drawScreen(int mx, int my, float pt) {
            drawDefaultBackground();
            super.drawScreen(mx, my, pt);
            renderHoveredToolTip(mx, my);
        }

        @Override
        protected void drawGuiContainerBackgroundLayer(float pt, int mx, int my) {
            GlStateManager.color(1, 1, 1, 1);
            mc.getTextureManager().bindTexture(BG);
            int x = (width - xSize) / 2, y = (height - ySize) / 2;
            drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
            drawTexturedModalRect(x + 79, y + 34, 176, 14, te.getPrintProgressScaled(24) + 1, 16);
        }
    }

    static final class LaptopRenderer extends TileEntitySpecialRenderer<TileEntityLaptop> {
        private static final ResourceLocation TEX = tex("laptop");
        private final ModelLaptop model = new ModelLaptop();

        @Override
        public void render(TileEntityLaptop te, double x, double y, double z, float pt, int destroy, float alpha) {
            EnumFacing f = EnumFacing.SOUTH;
            if (te.hasWorld()) {
                net.minecraft.block.state.IBlockState s = te.getWorld().getBlockState(te.getPos());
                if (s.getBlock() == CPC.laptop) f = s.getValue(BlockHorizontal.FACING);
            }
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            bindTexture(TEX);
            float rot = f == EnumFacing.NORTH ? 180 : f == EnumFacing.WEST ? 90 : f == EnumFacing.EAST ? -90 : 0;
            GlStateManager.rotate(rot, 0, 1, 0);
            float lid = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * pt;
            lid = 1.0F - lid;
            lid = 1.0F - lid * lid * lid;
            model.base.rotateAngleX = -(lid * (float) Math.PI / 2.0F);
            model.renderAll();
            GlStateManager.popMatrix();
        }
    }
}
