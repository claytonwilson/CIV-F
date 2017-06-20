/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
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
/*     */ public class SimpleBlock
/*     */ {
/*     */   public static enum Type
/*     */   {
/*  37 */     NORMAL, 
/*  38 */     COMMAND, 
/*  39 */     LITERAL;
/*     */   }
/*     */   
/*  42 */   private int type = 0;
/*  43 */   private byte data = 0;
/*     */   
/*     */   public int x;
/*     */   
/*     */   public int y;
/*     */   
/*     */   public int z;
/*     */   public Type specialType;
/*     */   public String command;
/*  52 */   public String[] message = new String[4];
/*     */   public String worldname;
/*     */   public Buildable buildable;
/*  55 */   public Map<String, String> keyvalues = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleBlock(Block block)
/*     */   {
/*  63 */     this.x = block.getX();
/*  64 */     this.y = block.getY();
/*  65 */     this.z = block.getZ();
/*  66 */     this.worldname = block.getWorld().getName();
/*  67 */     this.type = ItemManager.getId(block);
/*  68 */     this.data = ItemManager.getData(block);
/*  69 */     this.specialType = Type.NORMAL;
/*     */   }
/*     */   
/*     */   public SimpleBlock(String hash, int type, byte data) {
/*  73 */     String[] split = hash.split(",");
/*  74 */     this.worldname = split[0];
/*  75 */     this.x = Integer.valueOf(split[1]).intValue();
/*  76 */     this.y = Integer.valueOf(split[2]).intValue();
/*  77 */     this.z = Integer.valueOf(split[3]).intValue();
/*  78 */     this.type = type;
/*  79 */     this.data = data;
/*  80 */     this.specialType = Type.NORMAL;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  84 */     return this.worldname + "," + this.x + "," + this.y + "," + this.z;
/*     */   }
/*     */   
/*     */   public static String getKeyFromBlockCoord(BlockCoord coord) {
/*  88 */     return coord.getWorldname() + "," + coord.getX() + "," + coord.getY() + "," + coord.getZ();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleBlock(int type, int data)
/*     */   {
/*  98 */     this.type = ((short)type);
/*  99 */     this.data = ((byte)data);
/* 100 */     this.specialType = Type.NORMAL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getType()
/*     */   {
/* 108 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setType(int type)
/*     */   {
/* 115 */     this.type = ((short)type);
/*     */   }
/*     */   
/*     */   public void setTypeAndData(int type, int data) {
/* 119 */     this.type = ((short)type);
/* 120 */     this.data = ((byte)data);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getData()
/*     */   {
/* 126 */     return this.data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setData(int data)
/*     */   {
/* 133 */     this.data = ((byte)data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAir()
/*     */   {
/* 142 */     return this.type == 0;
/*     */   }
/*     */   
/*     */   public String getKeyValueString() {
/* 146 */     String out = "";
/*     */     
/* 148 */     for (String key : this.keyvalues.keySet()) {
/* 149 */       String value = (String)this.keyvalues.get(key);
/* 150 */       out = out + key + ":" + value + ",";
/*     */     }
/*     */     
/* 153 */     return out;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 157 */     return new Location(Bukkit.getWorld(this.worldname), this.x, this.y, this.z);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\SimpleBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */