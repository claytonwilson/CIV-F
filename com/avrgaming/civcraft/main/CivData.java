/*     */ package com.avrgaming.civcraft.main;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.InvalidBlockLocation;
/*     */ import com.avrgaming.civcraft.util.BlockSnapshot;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.io.PrintStream;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CivData
/*     */ {
/*     */   public static final int WALL_SIGN = 68;
/*     */   public static final int SIGN = 63;
/*     */   public static final int CHEST = 54;
/*     */   public static final int WOOD = 17;
/*     */   public static final int LEAF = 18;
/*     */   public static final byte DATA_OAK = 0;
/*     */   public static final byte DATA_PINE = 1;
/*     */   public static final byte DATA_BIRCH = 2;
/*     */   public static final byte DATA_JUNGLE = 3;
/*     */   public static final int GOLD_ORE = 14;
/*     */   public static final int IRON_ORE = 15;
/*     */   public static final int IRON_INGOT = 265;
/*     */   public static final int GOLD_INGOT = 266;
/*     */   public static final int WATER = 9;
/*     */   public static final int WATER_RUNNING = 8;
/*     */   public static final int FENCE = 85;
/*     */   public static final int BEDROCK = 7;
/*     */   public static final int RAILROAD = 66;
/*     */   public static final int LAVA = 11;
/*     */   public static final int LAVA_RUNNING = 10;
/*     */   public static final int COBBLESTONE = 4;
/*     */   public static final int EMERALD = 388;
/*     */   public static final int DIAMOND = 264;
/*     */   public static final int GRAVEL = 13;
/*     */   public static final int AIR = 0;
/*     */   public static final int DISPENSER = 23;
/*     */   public static final int REDSTONE_DUST = 331;
/*     */   public static final int WHEAT = 59;
/*     */   public static final int SUGARCANE = 83;
/*     */   public static final int PUMPKIN_STEM = 104;
/*     */   public static final int MELON_STEM = 105;
/*     */   public static final int CARROTS = 141;
/*     */   public static final int POTATOES = 142;
/*     */   public static final int NETHERWART = 115;
/*     */   public static final int COCOAPOD = 127;
/*     */   public static final int REDMUSHROOM = 39;
/*     */   public static final int BROWNMUSHROOM = 40;
/*     */   public static final int FARMLAND = 60;
/*     */   public static final int MELON = 103;
/*     */   public static final int PUMPKIN = 86;
/*     */   public static final int PUBLISHED_BOOK = 387;
/*     */   public static final int ROTTEN_FLESH = 367;
/*     */   public static final int TORCH = 50;
/*     */   public static final int WATER_BUCKET = 326;
/*     */   public static final int EMPTY_BUCKET = 325;
/*     */   public static final int ENDER_PEARL = 368;
/*     */   public static final String BOOK_UNDERLINE = "§n";
/*     */   public static final String BOOK_BOLD = "§l";
/*     */   public static final String BOOK_ITALIC = "§o";
/*     */   public static final String BOOK_NORMAL = "§r";
/*     */   public static final byte DATA_SIGN_EAST = 5;
/*     */   public static final int DATA_SIGN_WEST = 4;
/*     */   public static final int DATA_SIGN_NORTH = 2;
/*     */   public static final int DATA_SIGN_SOUTH = 3;
/*     */   public static final int ITEMFRAME = 389;
/*     */   public static final int EMERALD_BLOCK = 133;
/*     */   public static final int WOOL = 35;
/*     */   public static final byte DATA_WOOL_BLACK = 15;
/*     */   public static final int COOKED_FISH = 350;
/*     */   public static final int OBSIDIAN = 49;
/*     */   public static final int FIRE = 51;
/*     */   public static final int FISH_RAW = 349;
/*     */   public static final int BREAD = 297;
/*     */   public static final int GLOWSTONE = 89;
/*     */   public static final int DYE = 351;
/*     */   public static final int REDSTONE_TORCH_OFF = 75;
/*     */   public static final int STONE_BRICK = 98;
/*     */   public static final byte CHEST_NORTH = 2;
/*     */   public static final byte CHEST_SOUTH = 3;
/*     */   public static final byte CHEST_WEST = 4;
/*     */   public static final byte CHEST_EAST = 5;
/*     */   public static final byte SIGNPOST_NORTH = 8;
/*     */   public static final byte SIGNPOST_SOUTH = 0;
/*     */   public static final byte SIGNPOST_WEST = 4;
/*     */   public static final byte SIGNPOST_EAST = 12;
/*     */   public static final int BREAD_SEED = 295;
/*     */   public static final int CARROT_ITEM = 391;
/*     */   public static final int POTATO_ITEM = 392;
/*     */   public static final int LEATHER_HELMET = 298;
/*     */   public static final int LEATHER_CHESTPLATE = 299;
/*     */   public static final int LEATHER_LEGGINGS = 300;
/*     */   public static final int LEATHER_BOOTS = 301;
/*     */   public static final int IRON_HELMET = 306;
/*     */   public static final int IRON_CHESTPLATE = 307;
/*     */   public static final int IRON_LEGGINGS = 308;
/*     */   public static final int IRON_BOOTS = 309;
/*     */   public static final int DIAMOND_HELMET = 310;
/*     */   public static final int DIAMOND_CHESTPLATE = 311;
/*     */   public static final int DIAMOND_LEGGINGS = 312;
/*     */   public static final int DIAMOND_BOOTS = 313;
/*     */   public static final int GOLD_HELMET = 314;
/*     */   public static final int GOLD_CHESTPLATE = 315;
/*     */   public static final int GOLD_LEGGINGS = 316;
/*     */   public static final int GOLD_BOOTS = 317;
/*     */   public static final int CHAIN_HELMET = 302;
/*     */   public static final int CHAIN_CHESTPLATE = 303;
/*     */   public static final int CHAIN_LEGGINGS = 304;
/*     */   public static final int CHAIN_BOOTS = 305;
/*     */   public static final int WOOD_SWORD = 268;
/*     */   public static final int STONE_SWORD = 272;
/*     */   public static final int IRON_SWORD = 267;
/*     */   public static final int DIAMOND_SWORD = 276;
/*     */   public static final int GOLD_SWORD = 283;
/*     */   public static final int WOOD_PICKAXE = 270;
/*     */   public static final int STONE_PICKAXE = 274;
/*     */   public static final int IRON_PICKAXE = 257;
/*     */   public static final int DIAMOND_PICKAXE = 278;
/*     */   public static final int GOLD_PICKAXE = 285;
/*     */   public static final byte DATA_WOOL_GREEN = 5;
/* 155 */   public static final Integer LADDER = Integer.valueOf(65);
/* 156 */   public static final int COAL = ItemManager.getId(Material.COAL);
/*     */   public static final int WOOD_DOOR = 64;
/*     */   public static final int IRON_DOOR = 71;
/*     */   public static final int NETHERRACK = 87;
/*     */   public static final int BOW = 261;
/*     */   public static final int ANVIL = 145;
/*     */   public static final int IRON_BLOCK = 42;
/*     */   public static final int COBWEB = 30;
/*     */   public static final int STONE = 1;
/*     */   public static final short MUNDANE_POTION_DATA = 8192;
/*     */   public static final short MUNDANE_POTION_EXT_DATA = 64;
/*     */   public static final short THICK_POTION_DATA = 32;
/*     */   public static final short DATA_WOOL_RED = 14;
/*     */   public static final int DATA_WOOL_WHITE = 0;
/*     */   public static final int CLOWNFISH = 2;
/*     */   public static final int PUFFERFISH = 3;
/*     */   public static final int GOLDEN_APPLE = 322;
/*     */   public static final int TNT = 46;
/*     */   
/*     */   public static String getDisplayName(int id)
/*     */   {
/* 177 */     if (id == 14)
/* 178 */       return "Gold Ore";
/* 179 */     if (id == 15)
/* 180 */       return "Iron Ore";
/* 181 */     if (id == 265)
/* 182 */       return "Iron";
/* 183 */     if (id == 266) {
/* 184 */       return "Gold";
/*     */     }
/* 186 */     return "Unknown_Id";
/*     */   }
/*     */   
/*     */   public static boolean canGrowFromStem(BlockSnapshot bs)
/*     */   {
/* 191 */     int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/* 192 */     boolean hasAir = false;
/* 193 */     for (int i = 0; i < 4; i++)
/*     */     {
/*     */       try {
/* 196 */         nextBs = bs.getRelative(offset[i][0], 0, offset[i][1]);
/*     */       }
/*     */       catch (InvalidBlockLocation e)
/*     */       {
/*     */         BlockSnapshot nextBs;
/*     */         
/* 202 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */       BlockSnapshot nextBs;
/*     */       
/* 208 */       if (nextBs.getTypeId() == 0) {
/* 209 */         hasAir = true;
/*     */       }
/*     */       
/* 212 */       if (((nextBs.getTypeId() == 103) && 
/* 213 */         (bs.getTypeId() == 105)) || (
/* 214 */         (nextBs.getTypeId() == 86) && 
/* 215 */         (bs.getTypeId() == 104))) {
/* 216 */         return false;
/*     */       }
/*     */     }
/* 219 */     return hasAir;
/*     */   }
/*     */   
/*     */   public static boolean canGrowMushroom(BlockState blockState) {
/* 223 */     int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/* 224 */     boolean hasAir = false;
/* 225 */     for (int i = 0; i < 4; i++) {
/* 226 */       Block nextBlock = blockState.getBlock().getRelative(offset[i][0], 0, offset[i][1]);
/* 227 */       if (ItemManager.getId(nextBlock) == 0) {
/* 228 */         hasAir = true;
/*     */       }
/*     */     }
/* 231 */     return hasAir;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean canCocoaGrow(BlockSnapshot bs)
/*     */   {
/* 268 */     byte bits = (byte)(bs.getData() & 0xC);
/* 269 */     if (bits == 8)
/* 270 */       return false;
/* 271 */     return true;
/*     */   }
/*     */   
/*     */   public static byte getNextCocoaValue(BlockSnapshot bs) {
/* 275 */     byte bits = (byte)(bs.getData() & 0xC);
/* 276 */     if (bits == 0)
/* 277 */       return 4;
/* 278 */     if (bits == 4) {
/* 279 */       return 8;
/*     */     }
/* 281 */     return 8;
/*     */   }
/*     */   
/*     */   public static boolean canGrow(BlockSnapshot bs) {
/* 285 */     switch (bs.getTypeId()) {
/*     */     case 59: 
/*     */     case 141: 
/*     */     case 142: 
/* 289 */       if (bs.getData() == 7) {
/* 290 */         return false;
/*     */       }
/* 292 */       return true;
/*     */     
/*     */     case 115: 
/* 295 */       if (bs.getData() == 3) {
/* 296 */         return false;
/*     */       }
/* 298 */       return true;
/*     */     
/*     */     case 127: 
/* 301 */       return canCocoaGrow(bs);
/*     */     
/*     */     case 104: 
/*     */     case 105: 
/* 305 */       return canGrowFromStem(bs);
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 315 */     return false;
/*     */   }
/*     */   
/*     */   public static byte convertSignDataToDoorDirectionData(byte data) {
/* 319 */     switch (data) {
/*     */     case 8: 
/* 321 */       return 1;
/*     */     case 0: 
/* 323 */       return 3;
/*     */     case 12: 
/* 325 */       return 2;
/*     */     case 4: 
/* 327 */       return 0;
/*     */     }
/*     */     
/* 330 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte convertSignDataToChestData(byte data)
/*     */   {
/* 348 */     switch (data) {
/*     */     case 8: 
/* 350 */       return 2;
/*     */     case 0: 
/* 352 */       return 3;
/*     */     case 12: 
/* 354 */       return 5;
/*     */     case 4: 
/* 356 */       return 4;
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 372 */     System.out.println("Warning, unknown sign post direction:" + data);
/* 373 */     return 3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\main\CivData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */