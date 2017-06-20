/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
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
/*     */ public class ChunkCoord
/*     */ {
/*     */   private String worldname;
/*     */   private int x;
/*     */   private int z;
/*  37 */   private static ConcurrentHashMap<String, World> worlds = new ConcurrentHashMap();
/*     */   
/*     */   public static void addWorld(World world) {
/*  40 */     worlds.put(world.getName(), world);
/*     */   }
/*     */   
/*     */   public static void buildWorldList() {
/*  44 */     for (World world : ) {
/*  45 */       worlds.put(world.getName(), world);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChunkCoord(String worldname, int x, int z) {
/*  50 */     setWorldname(worldname);
/*  51 */     setX(x);
/*  52 */     setZ(z);
/*     */   }
/*     */   
/*     */   public ChunkCoord(Location location) {
/*  56 */     setFromLocation(location);
/*     */   }
/*     */   
/*     */   public ChunkCoord(Chunk c) {
/*  60 */     setWorldname(c.getWorld().getName());
/*  61 */     setX(c.getX());
/*  62 */     setZ(c.getZ());
/*     */   }
/*     */   
/*     */   public ChunkCoord(BlockCoord corner) {
/*  66 */     setFromLocation(corner.getLocation());
/*     */   }
/*     */   
/*     */   public ChunkCoord() {}
/*     */   
/*     */   public String getWorldname()
/*     */   {
/*  73 */     return this.worldname;
/*     */   }
/*     */   
/*     */   public void setWorldname(String worldname) {
/*  77 */     this.worldname = worldname;
/*     */   }
/*     */   
/*     */   public int getX() {
/*  81 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/*  85 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getZ() {
/*  89 */     return this.z;
/*     */   }
/*     */   
/*     */   public void setZ(int z) {
/*  93 */     this.z = z;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  98 */     return this.worldname + "," + this.x + "," + this.z;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 103 */     if ((other instanceof ChunkCoord)) {
/* 104 */       ChunkCoord otherCoord = (ChunkCoord)other;
/* 105 */       if ((otherCoord.worldname.equals(this.worldname)) && 
/* 106 */         (otherCoord.getX() == this.x) && (otherCoord.getZ() == this.z)) {
/* 107 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 116 */     return toString().hashCode();
/*     */   }
/*     */   
/*     */   public static int castToChunkX(int blockx) {
/* 120 */     return castToChunk(blockx);
/*     */   }
/*     */   
/*     */   public static int castToChunkZ(int blockz) {
/* 124 */     return castToChunk(blockz);
/*     */   }
/*     */   
/*     */   public static int castToChunk(int i) {
/* 128 */     return (int)Math.floor(i / 16.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFromLocation(Location location)
/*     */   {
/* 138 */     for (String name : worlds.keySet()) {
/* 139 */       World world = (World)worlds.get(name);
/* 140 */       if (world != null)
/*     */       {
/*     */ 
/*     */ 
/* 144 */         if (world.equals(location.getWorld())) {
/* 145 */           this.worldname = name;
/* 146 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 151 */     this.x = castToChunkX(location.getBlockX());
/* 152 */     this.z = castToChunkZ(location.getBlockZ());
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
/*     */   public int manhattanDistance(ChunkCoord chunkCoord)
/*     */   {
/* 172 */     return Math.abs(chunkCoord.x - this.x) + Math.abs(chunkCoord.z - this.z);
/*     */   }
/*     */   
/*     */   public double distance(ChunkCoord chunkCoord) {
/* 176 */     if (!chunkCoord.getWorldname().equals(getWorldname())) {
/* 177 */       return Double.MAX_VALUE;
/*     */     }
/*     */     
/* 180 */     double dist = Math.pow(getX() - chunkCoord.getX(), 2.0D) + Math.pow(getZ() - chunkCoord.getZ(), 2.0D);
/* 181 */     return Math.sqrt(dist);
/*     */   }
/*     */   
/*     */   public Chunk getChunk() {
/* 185 */     return Bukkit.getWorld(this.worldname).getChunkAt(this.x, this.z);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\ChunkCoord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */