/*     */ package com.avrgaming.civcraft.util;
/*     */ 
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
/*     */ public class BlockCoord
/*     */ {
/*     */   private String worldname;
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*  32 */   private Location location = null;
/*  33 */   private boolean dirty = false;
/*     */   
/*     */   public BlockCoord(String worldname, int x, int y, int z) {
/*  36 */     setWorldname(worldname);
/*  37 */     setX(x);
/*  38 */     setY(y);
/*  39 */     setZ(z);
/*     */   }
/*     */   
/*     */   public BlockCoord(Location location) {
/*  43 */     setFromLocation(location);
/*     */   }
/*     */   
/*     */   public BlockCoord(String string) {
/*  47 */     String[] split = string.split(",");
/*  48 */     setWorldname(split[0]);
/*  49 */     setX(Integer.valueOf(split[1]).intValue());
/*  50 */     setY(Integer.valueOf(split[2]).intValue());
/*  51 */     setZ(Integer.valueOf(split[3]).intValue());
/*     */   }
/*     */   
/*     */   public BlockCoord(BlockCoord obj) {
/*  55 */     setX(obj.getX());
/*  56 */     setY(obj.getY());
/*  57 */     setZ(obj.getZ());
/*  58 */     setWorldname(obj.getWorldname());
/*     */   }
/*     */   
/*     */   public BlockCoord(Block block) {
/*  62 */     setX(block.getX());
/*  63 */     setY(block.getY());
/*  64 */     setZ(block.getZ());
/*  65 */     setWorldname(block.getWorld().getName());
/*     */   }
/*     */   
/*     */   public BlockCoord(SimpleBlock next) {
/*  69 */     setWorldname(next.worldname);
/*  70 */     setX(next.x);
/*  71 */     setY(next.y);
/*  72 */     setZ(next.z);
/*     */   }
/*     */   
/*     */   public BlockCoord() {}
/*     */   
/*     */   public BlockCoord(ChunkCoord nextChunk)
/*     */   {
/*  79 */     setWorldname(nextChunk.getWorldname());
/*  80 */     setX(nextChunk.getX() * 16 + 8);
/*  81 */     setY(64);
/*  82 */     setZ(nextChunk.getZ() * 16 + 8);
/*     */   }
/*     */   
/*     */   public void setFromLocation(Location location) {
/*  86 */     this.dirty = true;
/*  87 */     setWorldname(location.getWorld().getName());
/*  88 */     setX(location.getBlockX());
/*  89 */     setY(location.getBlockY());
/*  90 */     setZ(location.getBlockZ());
/*     */   }
/*     */   
/*     */   public String getWorldname() {
/*  94 */     return this.worldname;
/*     */   }
/*     */   
/*     */   public void setWorldname(String worldname) {
/*  98 */     this.dirty = true;
/*  99 */     this.worldname = worldname;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 103 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 107 */     this.dirty = true;
/* 108 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 112 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 116 */     this.dirty = true;
/* 117 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getZ() {
/* 121 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setZ(int z) {
/* 125 */     this.dirty = true;
/* 126 */     this.z = z;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 131 */     return this.worldname + "," + this.x + "," + this.y + "," + this.z;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 136 */     return toString().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 141 */     if ((other instanceof BlockCoord)) {
/* 142 */       BlockCoord otherCoord = (BlockCoord)other;
/* 143 */       if ((otherCoord.worldname.equals(this.worldname)) && 
/* 144 */         (otherCoord.getX() == this.x) && (otherCoord.getY() == this.y) && 
/* 145 */         (otherCoord.getZ() == this.z)) {
/* 146 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 154 */     if ((this.location == null) || (this.dirty)) {
/* 155 */       this.location = new Location(Bukkit.getWorld(this.worldname), this.x, this.y, this.z);
/* 156 */       this.dirty = false;
/*     */     }
/* 158 */     return this.location;
/*     */   }
/*     */   
/*     */   public Block getBlock() {
/* 162 */     return Bukkit.getWorld(this.worldname).getBlockAt(this.x, this.y, this.z);
/*     */   }
/*     */   
/*     */   public double distance(BlockCoord corner) {
/* 166 */     return Math.sqrt(distanceSquared(corner));
/*     */   }
/*     */   
/*     */   public double distanceXZ(BlockCoord corner) {
/* 170 */     return Math.sqrt(distanceXZSquared(corner));
/*     */   }
/*     */   
/*     */   public double distanceXZSquared(BlockCoord corner) {
/* 174 */     double distance = Double.MAX_VALUE;
/*     */     
/* 176 */     if (!corner.getWorldname().equals(this.worldname)) {
/* 177 */       return distance;
/*     */     }
/*     */     
/* 180 */     distance = Math.pow(corner.getX() - getX(), 2.0D) + 
/* 181 */       Math.pow(corner.getZ() - getZ(), 2.0D);
/* 182 */     return distance;
/*     */   }
/*     */   
/*     */   public double distanceSquared(BlockCoord corner) {
/* 186 */     double distance = Double.MAX_VALUE;
/*     */     
/* 188 */     if (!corner.getWorldname().equals(this.worldname)) {
/* 189 */       return distance;
/*     */     }
/*     */     
/* 192 */     distance = Math.pow(corner.getX() - getX(), 2.0D) + 
/* 193 */       Math.pow(corner.getY() - getY(), 2.0D) + 
/* 194 */       Math.pow(corner.getZ() - getZ(), 2.0D);
/*     */     
/* 196 */     return distance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Location getCenteredLocation()
/*     */   {
/* 205 */     Location loc = new Location(Bukkit.getWorld(this.worldname), this.x + 0.5D, this.y + 0.5D, this.z + 0.5D);
/* 206 */     return loc;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\BlockCoord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */