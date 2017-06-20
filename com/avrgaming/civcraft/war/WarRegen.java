/*     */ package com.avrgaming.civcraft.war;
/*     */ 
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BukkitObjects;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import gpl.InventorySerializer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.Dispenser;
/*     */ import org.bukkit.block.Dropper;
/*     */ import org.bukkit.block.Furnace;
/*     */ import org.bukkit.block.Hopper;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.inventory.FurnaceInventory;
/*     */ import org.bukkit.inventory.Inventory;
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
/*     */ public class WarRegen
/*     */ {
/*  51 */   private static Map<Block, Boolean> blockCache = new HashMap();
/*     */   
/*     */ 
/*     */   private static String blockAsAir(Block blk)
/*     */   {
/*  56 */     return "0:0:" + blk.getX() + ":" + blk.getY() + ":" + blk.getZ() + ":" + blk.getWorld().getName();
/*     */   }
/*     */   
/*     */   private static String blockBasicString(Block blk) {
/*  60 */     return ItemManager.getId(blk) + ":" + ItemManager.getData(blk) + ":" + blk.getX() + ":" + blk.getY() + ":" + blk.getZ() + ":" + blk.getWorld().getName();
/*     */   }
/*     */   
/*     */   public static String blockInventoryString(Inventory inv) {
/*  64 */     String out = ":";
/*     */     
/*  66 */     out = out + InventorySerializer.InventoryToString(inv);
/*     */     
/*  68 */     return out;
/*     */   }
/*     */   
/*     */   public static String blockSignString(Sign sign) {
/*  72 */     String out = ":";
/*     */     String[] arrayOfString;
/*  74 */     int j = (arrayOfString = sign.getLines()).length; for (int i = 0; i < j; i++) { String str = arrayOfString[i];
/*  75 */       out = out + str + ",";
/*     */     }
/*     */     
/*  78 */     return out;
/*     */   }
/*     */   
/*     */   private static String blockToString(Block blk, boolean save_as_air) {
/*  82 */     if (save_as_air) {
/*  83 */       return blockAsAir(blk);
/*     */     }
/*     */     
/*  86 */     String str = blockBasicString(blk);
/*     */     
/*  88 */     Inventory inv = null;
/*  89 */     switch (blk.getType()) {
/*     */     case COMMAND_MINECART: 
/*     */     case HOPPER_MINECART: 
/*  92 */       inv = ((Chest)blk.getState()).getBlockInventory();
/*  93 */       str = str + blockInventoryString(inv);
/*  94 */       break;
/*     */     case BREWING_STAND_ITEM: 
/*  96 */       inv = ((Dispenser)blk.getState()).getInventory();
/*  97 */       str = str + blockInventoryString(inv);
/*  98 */       break;
/*     */     case DARK_OAK_STAIRS: 
/*     */     case DAYLIGHT_DETECTOR: 
/* 101 */       inv = ((Furnace)blk.getState()).getInventory();
/* 102 */       str = str + blockInventoryString(inv);
/* 103 */       break;
/*     */     case IRON_FENCE: 
/* 105 */       inv = ((Dropper)blk.getState()).getInventory();
/* 106 */       str = str + blockInventoryString(inv);
/* 107 */       break;
/*     */     case IRON_BOOTS: 
/* 109 */       inv = ((Hopper)blk.getState()).getInventory();
/* 110 */       str = str + blockInventoryString(inv);
/* 111 */       break;
/*     */     case DEAD_BUSH: 
/*     */     case DIAMOND_BLOCK: 
/*     */     case RAILS: 
/* 115 */       Sign sign = (Sign)blk.getState();
/* 116 */       str = str + blockSignString(sign);
/* 117 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 122 */     return str;
/*     */   }
/*     */   
/*     */   private static void restoreBlockFromString(String line)
/*     */   {
/* 127 */     String[] split = line.split(":");
/*     */     
/* 129 */     int type = Integer.valueOf(split[0]).intValue();
/* 130 */     byte data = Byte.valueOf(split[1]).byteValue();
/* 131 */     int x = Integer.valueOf(split[2]).intValue();
/* 132 */     int y = Integer.valueOf(split[3]).intValue();
/* 133 */     int z = Integer.valueOf(split[4]).intValue();
/* 134 */     String world = split[5];
/*     */     
/* 136 */     Block block = BukkitObjects.getWorld(world).getBlockAt(x, y, z);
/*     */     
/* 138 */     ItemManager.setTypeId(block, type);
/* 139 */     ItemManager.setData(block, data, false);
/*     */     
/*     */ 
/* 142 */     Inventory inv = null;
/* 143 */     switch (block.getType()) {
/*     */     case HOPPER_MINECART: 
/* 145 */       inv = ((Chest)block.getState()).getBlockInventory();
/* 146 */       InventorySerializer.StringToInventory(inv, split[6]);
/* 147 */       break;
/*     */     case COMMAND_MINECART: 
/* 149 */       inv = ((Chest)block.getState()).getBlockInventory();
/* 150 */       InventorySerializer.StringToInventory(inv, split[6]);
/* 151 */       break;
/*     */     case BREWING_STAND_ITEM: 
/* 153 */       inv = ((Dispenser)block.getState()).getInventory();
/* 154 */       InventorySerializer.StringToInventory(inv, split[6]);
/* 155 */       break;
/*     */     case DARK_OAK_STAIRS: 
/*     */     case DAYLIGHT_DETECTOR: 
/* 158 */       inv = ((Furnace)block.getState()).getInventory();
/* 159 */       InventorySerializer.StringToInventory(inv, split[6]);
/* 160 */       break;
/*     */     case IRON_FENCE: 
/* 162 */       inv = ((Dropper)block.getState()).getInventory();
/* 163 */       InventorySerializer.StringToInventory(inv, split[6]);
/* 164 */       break;
/*     */     case IRON_BOOTS: 
/* 166 */       inv = ((Hopper)block.getState()).getInventory();
/* 167 */       InventorySerializer.StringToInventory(inv, split[6]);
/* 168 */       break;
/*     */     case DEAD_BUSH: 
/*     */     case DIAMOND_BLOCK: 
/*     */     case RAILS: 
/* 172 */       Sign sign = (Sign)block.getState();
/* 173 */       String[] messages = split[6].split(",");
/* 174 */       for (int i = 0; i < 4; i++) {
/* 175 */         if (messages[i] != null) {
/* 176 */           sign.setLine(i, messages[i]);
/*     */         }
/*     */       }
/* 179 */       sign.update();
/* 180 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void destroyThisBlock(Block blk, Town town)
/*     */   {
/* 191 */     saveBlock(blk, town.getName(), false);
/*     */     
/* 193 */     switch (blk.getType()) {
/*     */     case HOPPER_MINECART: 
/* 195 */       ((Chest)blk.getState()).getBlockInventory().clear();
/* 196 */       break;
/*     */     case COMMAND_MINECART: 
/* 198 */       ((Chest)blk.getState()).getBlockInventory().clear();
/* 199 */       break;
/*     */     case BREWING_STAND_ITEM: 
/* 201 */       ((Dispenser)blk.getState()).getInventory().clear();
/* 202 */       break;
/*     */     case DARK_OAK_STAIRS: 
/*     */     case DAYLIGHT_DETECTOR: 
/* 205 */       ((Furnace)blk.getState()).getInventory().clear();
/* 206 */       break;
/*     */     case IRON_FENCE: 
/* 208 */       ((Dropper)blk.getState()).getInventory().clear();
/* 209 */       break;
/*     */     case IRON_BOOTS: 
/* 211 */       ((Hopper)blk.getState()).getInventory().clear();
/* 212 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 217 */     ItemManager.setTypeId(blk, 0);
/* 218 */     ItemManager.setData(blk, 0, true);
/*     */   }
/*     */   
/*     */   public static boolean canPlaceThisBlock(Block blk)
/*     */   {
/* 223 */     switch (blk.getType()) {
/*     */     case BED: 
/*     */     case BED_BLOCK: 
/* 226 */       return false;
/*     */     }
/*     */     
/*     */     
/* 230 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void saveBlock(Block blk, String name, boolean save_as_air)
/*     */   {
/* 236 */     Boolean saved = (Boolean)blockCache.get(blk);
/* 237 */     if (saved == Boolean.TRUE)
/*     */     {
/*     */ 
/*     */ 
/* 241 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 245 */       String filepath = "templates/war/" + name;
/* 246 */       FileWriter fstream = new FileWriter(filepath, true);
/* 247 */       BufferedWriter out = new BufferedWriter(fstream);
/* 248 */       out.append(blockToString(blk, save_as_air) + "\n");
/* 249 */       blockCache.put(blk, Boolean.TRUE);
/* 250 */       out.close();
/* 251 */       fstream.close();
/*     */     } catch (IOException e) {
/* 253 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void restoreBlocksFor(String name)
/*     */   {
/*     */     try
/*     */     {
/* 263 */       int count = 0;
/* 264 */       String filepath = "templates/war/" + name;
/* 265 */       File warLog = new File(filepath);
/*     */       
/* 267 */       if (!warLog.exists()) {
/* 268 */         return;
/*     */       }
/* 270 */       BufferedReader reader = new BufferedReader(new FileReader(warLog));
/*     */       
/* 272 */       String line = null;
/* 273 */       while ((line = reader.readLine()) != null) {
/*     */         try {
/* 275 */           restoreBlockFromString(line);
/* 276 */           count++;
/*     */         } catch (Exception e) {
/* 278 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 281 */       reader.close();
/* 282 */       warLog.delete();
/* 283 */       System.out.println("[CivCraft] Restored " + count + " blocks for town " + name);
/*     */     }
/*     */     catch (IOException e) {
/* 286 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\war\WarRegen.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */