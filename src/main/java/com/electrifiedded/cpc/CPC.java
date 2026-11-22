package com.electrifiedded.cpc;

import com.electrifiedded.archaiclib.legacy.sound.LegacyModSounds;
import com.electrifiedded.archaiclib.original.OriginalModAssets;
import com.electrifiedded.cpc.block.BlockLaptop;
import com.electrifiedded.cpc.block.TileEntityLaptop;
import com.electrifiedded.cpc.entity.*;
import com.electrifiedded.cpc.item.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:archaiclib")
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class CPC {
    public static final String ORIGINAL_NAME = "creepypastacraft_revived_mod_1.7.10.jar - CreepyPastaCraft Revived 1.7.x-0.0.0.8";
    public static final String ORIGINAL_URL = "https://www.curseforge.com/minecraft/mc-mods (search: CreepyPastaCraft Revived)";

    @Mod.Instance(Reference.MOD_ID)
    public static CPC instance;

    public static Item horror, jeffKnife, hiltBlack, diaper, stephano, pasta, recordLavender, archive, smileJpg;
    public static Block laptop;
    public static Map<String, SoundEvent> sounds;

    /** Archive metadata -> pasta (same ids as the original PastaRegistry). */
    public static final List<String> PASTA_NAMES = new ArrayList<>();
    public static final Map<String, Function<World, ? extends Entity>> PASTA = new LinkedHashMap<>();

    public static final CreativeTabs TAB = new CreativeTabs("tabCPC") {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(horror);
        }
    };

    public CPC() {
        // the original keeps camelCase file names; 1.12 resource locations are lower-case
        String[][] a = {
                {"textures/items/jeffknife.png", "textures/items/jeffKnife.png"},
                {"textures/items/hiltblack.png", "textures/items/hiltBlack.png"},
                {"textures/items/tabcpc.png", "textures/items/tabCPC.png"},
                {"textures/entity/squidward/squidwardhappy.png", "textures/entity/squidward/SquidwardHappy.png"},
                {"textures/entity/squidward/squidwardmad.png", "textures/entity/squidward/SquidwardMad.png"},
                {"textures/entity/squidward/squidwardsuicide.png", "textures/entity/squidward/SquidwardSuicide.png"},
                {"textures/blocks/laptop.png", "textures/blocks/laptop.png"},
        };
        for (String[] p : a) OriginalModAssets.alias(Reference.MOD_ID, p[0], p[1]);
        // our own 1.12 sounds.json replaces the original one (which also lacks mob.jeff.kill)
        OriginalModAssets.hide(Reference.MOD_ID, "sounds.json");
        OriginalModAssets.hide(Reference.MOD_ID, "lang/en_us.lang");
        sounds = LegacyModSounds.autoRegister(Reference.MOD_ID);
    }

    public static SoundEvent sound(String key) {
        SoundEvent e = sounds.get(key);
        return e != null ? e : SoundEvents.ENTITY_GENERIC_HURT;
    }

    private static <T extends Item> T item(T item, String name) {
        item.setRegistryName(Reference.MOD_ID, name.toLowerCase());
        item.setTranslationKey(Reference.MOD_ID + "." + name);
        item.setCreativeTab(TAB);
        return item;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e) {
        laptop = new BlockLaptop().setRegistryName(Reference.MOD_ID, "laptop").setTranslationKey(Reference.MOD_ID + ".laptop").setCreativeTab(TAB);
        e.getRegistry().register(laptop);
        GameRegistry.registerTileEntity(TileEntityLaptop.class, new ResourceLocation(Reference.MOD_ID, "laptop"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {
        Item.ToolMaterial jeff = EnumHelper.addToolMaterial("CPC_JEFF", 0, -1, 0.0F, 11.0F, 0);
        Item.ToolMaterial hilt = EnumHelper.addToolMaterial("CPC_HILT", 0, -1, 0.0F, 11.0F, 0);
        ItemArmor.ArmorMaterial baby = EnumHelper.addArmorMaterial("CPC_BABY", "cpc:baby", 5, new int[]{1, 2, 3, 1}, 0,
                SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
        horror = item(new Item(), "horror").setMaxStackSize(1).setCreativeTab(null);
        jeffKnife = item(new ItemCPCKnife(jeff), "jeffKnife");
        hiltBlack = item(new ItemCPCKnife(hilt), "hiltBlack");
        diaper = item(new ItemCPCDiaper(baby), "diaper");
        stephano = item(new ItemCPCStephano(), "stephano");
        pasta = item(new ItemCPCPasta(), "pasta");
        recordLavender = item(new ItemCPCRecord("lavender", sound("records.lavender")), "record_lavender");
        archive = item(new ItemCPCArchive(), "archive");
        smileJpg = item(new Item(), "smile_jpg");
        e.getRegistry().registerAll(horror, jeffKnife, hiltBlack, diaper, stephano, pasta, recordLavender, archive, smileJpg,
                new ItemBlock(laptop).setRegistryName(laptop.getRegistryName()));
    }

    private static void pasta(String name, Function<World, ? extends Entity> f) {
        PASTA_NAMES.add(name);
        PASTA.put(name, f);
    }

    private static int nextId = 0;

    private static void entity(Class<? extends Entity> c, String name, int egg1, int egg2) {
        ResourceLocation rl = new ResourceLocation(Reference.MOD_ID, name);
        if (egg1 < 0) EntityRegistry.registerModEntity(rl, c, Reference.MOD_ID + "." + name, nextId++, instance, 64, 3, true);
        else EntityRegistry.registerModEntity(rl, c, Reference.MOD_ID + "." + name, nextId++, instance, 80, 3, true, egg1, egg2);
    }

    @SuppressWarnings("unchecked")
    private static void spawn(Class<? extends EntityLiving> c, int w, EnumCreatureType t, Biome... b) {
        EntityRegistry.addSpawn(c, w, 1, 1, t, b);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        OriginalModAssets.require(Reference.MOD_ID, Reference.MOD_ID, ORIGINAL_NAME, ORIGINAL_URL, Reference.MOD_ID);
        entity(EntityJeff.class, "jeff", 0xF0F0F0, 0x202020);
        entity(EntityJane.class, "jane", 0x101010, 0xE0E0E0);
        entity(EntityPewds.class, "pewds", 3515848, 14185267);
        entity(EntityStephano.class, "stephano", -1, 0);
        entity(EntityCry.class, "cry", 16777215, 0);
        entity(EntityJack.class, "jack", 0x1A2F5A, 0x000000);
        entity(EntityRake.class, "rake", 0xB0B0A0, 0x303030);
        entity(EntitySmile.class, "smile", 0x8B5A2B, 0xC00000);
        entity(EntitySeed.class, "seed", 0x506030, 0x202010);
        entity(EntityMothman.class, "moth", 0x302020, 0xFF0000);
        entity(EntitySquidward.class, "squidward", 0x9FC8B0, 0x7A4A2A);
        entity(EntityStrider.class, "strider", 0x404040, 0x909090);
        entity(EntitySquidwardSuicide.class, "squidward_suicide", -1, 0);
        entity(EntitySquidwardMad.class, "squidward_mad", -1, 0);

        spawn(EntityJeff.class, 5, EnumCreatureType.MONSTER, Biomes.FOREST, Biomes.FOREST_HILLS);
        spawn(EntityJane.class, 5, EnumCreatureType.MONSTER, Biomes.FOREST, Biomes.FOREST_HILLS);
        spawn(EntityPewds.class, 2, EnumCreatureType.CREATURE, Biomes.DESERT, Biomes.PLAINS);
        spawn(EntityCry.class, 2, EnumCreatureType.CREATURE, Biomes.DESERT, Biomes.PLAINS);
        spawn(EntityJack.class, 4, EnumCreatureType.MONSTER, Biomes.FOREST);
        spawn(EntityRake.class, 1, EnumCreatureType.MONSTER, Biomes.FOREST_HILLS);
        spawn(EntitySmile.class, 4, EnumCreatureType.MONSTER, Biomes.FOREST, Biomes.FOREST_HILLS);
        spawn(EntitySeed.class, 3, EnumCreatureType.MONSTER, Biomes.FOREST);
        // Mothman: registered with an empty biome list in the original -> never spawns naturally

        pasta("jeff", EntityJeff::new);
        pasta("jane", EntityJane::new);
        pasta("jack", EntityJack::new);
        pasta("rake", EntityRake::new);
        pasta("smile", EntitySmile::new);
        pasta("seed", EntitySeed::new);
        pasta("moth", EntityMothman::new);
        pasta("squidward", EntitySquidward::new);
        pasta("strider", EntityStrider::new);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CPCGuiHandler());
        MinecraftForge.EVENT_BUS.register(new CPCEvents());
        if (FMLCommonHandler.instance().getSide().isClient()) ClientProxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        ResourceLocation g = new ResourceLocation(Reference.MOD_ID, "recipes");
        GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "diaper"), g, new ItemStack(diaper), "p p", "ppp", 'p', Items.PAPER);
        GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "archive"), g, new ItemStack(archive, 1, 4), "a", 'a', smileJpg);
        GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "pasta"), g, new ItemStack(pasta), "w", "b", 'w', Items.WHEAT, 'b', Items.BOWL);
        GameRegistry.addShapedRecipe(new ResourceLocation(Reference.MOD_ID, "laptop"), g, new ItemStack(laptop), "xxx", "cdc", "xxx",
                'x', Items.IRON_INGOT, 'c', Items.REDSTONE, 'd', Items.DIAMOND);
    }

    @SubscribeEvent
    public static void loot(LootTableLoadEvent e) {
        if (e.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH)) {
            e.getTable().addPool(new LootPool(new net.minecraft.world.storage.loot.LootEntry[]{
                    new LootEntryItem(hiltBlack, 2, 0, new LootFunction[0], new LootCondition[0], "cpc:hiltblack"),
                    new net.minecraft.world.storage.loot.LootEntryEmpty(18, 0, new LootCondition[0], "cpc:none")},
                    new LootCondition[0], new net.minecraft.world.storage.loot.RandomValueRange(1), new net.minecraft.world.storage.loot.RandomValueRange(0), "cpc_hilt"));
        }
    }

    static Ingredient unused() { return Ingredient.EMPTY; }
}
