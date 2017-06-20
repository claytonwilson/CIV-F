/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.Attribute.TypeKeys;
/*     */ import com.avrgaming.civcraft.components.AttributeBiomeBase;
/*     */ import com.avrgaming.civcraft.components.AttributeBiomeRadiusPerLevel;
/*     */ import com.avrgaming.civcraft.components.Component;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigCultureBiomeInfo;
/*     */ import com.avrgaming.civcraft.config.ConfigCultureLevel;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.util.BiomeCache;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Biome;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
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
/*     */ public class CultureChunk
/*     */ {
/*     */   private Town town;
/*     */   private ChunkCoord chunkCoord;
/*  45 */   private int distance = 0;
/*  46 */   private Biome biome = null;
/*     */   
/*     */   public CultureChunk(Town town, ChunkCoord coord) {
/*  49 */     this.town = town;
/*  50 */     this.chunkCoord = coord;
/*  51 */     this.biome = BiomeCache.getBiome(this);
/*     */   }
/*     */   
/*  54 */   public Civilization getCiv() { return this.town.getCiv(); }
/*     */   
/*     */   public Town getTown()
/*     */   {
/*  58 */     return this.town;
/*     */   }
/*     */   
/*  61 */   public void setTown(Town town) { this.town = town; }
/*     */   
/*     */   public ChunkCoord getChunkCoord()
/*     */   {
/*  65 */     return this.chunkCoord;
/*     */   }
/*     */   
/*  68 */   public void setChunkCoord(ChunkCoord chunkCoord) { this.chunkCoord = chunkCoord; }
/*     */   
/*     */   public int getDistanceToNearestEdge(ArrayList<TownChunk> edges) {
/*  71 */     int distance = Integer.MAX_VALUE;
/*     */     
/*  73 */     for (TownChunk tc : edges) {
/*  74 */       int tmp = tc.getChunkCoord().manhattanDistance(this.chunkCoord);
/*  75 */       if (tmp < distance) {
/*  76 */         distance = tmp;
/*     */       }
/*     */     }
/*     */     
/*  80 */     return distance;
/*     */   }
/*     */   
/*  83 */   public void setDistance(int distance) { this.distance = distance; }
/*     */   
/*     */   public String getOnLeaveString()
/*     */   {
/*  87 */     return "§dLeaving " + this.town.getCiv().getName() + " Borders";
/*     */   }
/*     */   
/*     */   public String getOnEnterString() {
/*  91 */     return "§dEntering " + this.town.getCiv().getName() + " Borders";
/*     */   }
/*     */   
/*     */ 
/*     */   public double getPower()
/*     */   {
/*  97 */     if (this.distance == 0) {
/*  98 */       return Double.MAX_VALUE;
/*     */     }
/*     */     
/* 101 */     ConfigCultureLevel clc = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(getTown().getCultureLevel()));
/* 102 */     double power = clc.amount / Math.pow(this.distance, 2.0D);
/*     */     
/* 104 */     return power;
/*     */   }
/*     */   
/*     */   public Biome getBiome() {
/* 108 */     return this.biome;
/*     */   }
/*     */   
/* 111 */   public void setBiome(Biome biome) { this.biome = biome; }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 116 */     return this.chunkCoord.toString();
/*     */   }
/*     */   
/*     */   public ConfigCultureBiomeInfo getCultureBiomeInfo() {
/* 120 */     if (this.biome != null) {
/* 121 */       ConfigCultureBiomeInfo info = CivSettings.getCultureBiome(this.biome.name());
/* 122 */       return info;
/*     */     }
/*     */     
/* 125 */     return CivSettings.getCultureBiome("UNKNOWN");
/*     */   }
/*     */   
/*     */   public double getCoins()
/*     */   {
/* 130 */     return getCultureBiomeInfo().coins + getAdditionalAttributes(Attribute.TypeKeys.COINS.name());
/*     */   }
/*     */   
/*     */   public double getHappiness() {
/* 134 */     return getCultureBiomeInfo().happiness + getAdditionalAttributes(Attribute.TypeKeys.HAPPINESS.name());
/*     */   }
/*     */   
/*     */   public double getHammers()
/*     */   {
/* 139 */     return getCultureBiomeInfo().hammers + getAdditionalAttributes(Attribute.TypeKeys.HAMMERS.name());
/*     */   }
/*     */   
/*     */   public double getGrowth() {
/* 143 */     return getCultureBiomeInfo().growth + getAdditionalAttributes(Attribute.TypeKeys.GROWTH.name());
/*     */   }
/*     */   
/*     */   public double getBeakers() {
/* 147 */     return getCultureBiomeInfo().beakers + getAdditionalAttributes(Attribute.TypeKeys.BEAKERS.name());
/*     */   }
/*     */   
/*     */   private double getAdditionalAttributes(String attrType) {
/* 151 */     if (getBiome() == null) {
/* 152 */       return 0.0D;
/*     */     }
/*     */     
/* 155 */     Component.componentsLock.lock();
/*     */     try {
/* 157 */       ArrayList<Component> attrs = (ArrayList)Component.componentsByType.get("AttributeBiomeBase");
/* 158 */       double total = 0.0D;
/*     */       double d1;
/* 160 */       if (attrs == null) {
/* 161 */         return total;
/*     */       }
/*     */       
/* 164 */       for (Component comp : attrs) {
/* 165 */         (comp instanceof AttributeBiomeRadiusPerLevel);
/*     */         
/*     */ 
/* 168 */         if ((comp instanceof AttributeBiomeBase)) {
/* 169 */           AttributeBiomeBase attrComp = (AttributeBiomeBase)comp;
/* 170 */           if (attrComp.getAttribute().equals(attrType)) {
/* 171 */             total += attrComp.getGenerated(this);
/*     */           }
/*     */         }
/*     */       }
/* 175 */       return total;
/*     */     } finally {
/* 177 */       Component.componentsLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void showInfo(Player player)
/*     */   {
/* 183 */     Biome biome = getBiomeFromLocation(player.getLocation());
/*     */     
/* 185 */     CultureChunk cc = CivGlobal.getCultureChunk(new ChunkCoord(player.getLocation()));
/* 186 */     ConfigCultureBiomeInfo info = CivSettings.getCultureBiome(biome.name());
/*     */     
/*     */ 
/* 189 */     if (cc == null) {
/* 190 */       CivMessage.send(player, "§d" + biome.name() + 
/* 191 */         "§2" + " Coins: " + "§a" + info.coins + 
/* 192 */         "§2" + " Happiness:" + "§a" + info.happiness + 
/* 193 */         "§2" + " Hammers:" + "§a" + info.hammers + 
/* 194 */         "§2" + " Growth:" + "§a" + info.growth + 
/* 195 */         "§2" + " Beakers:" + "§a" + info.beakers);
/*     */     } else {
/* 197 */       CivMessage.send(player, "§d" + biome.name() + 
/* 198 */         "§2" + " Coins: " + "§a" + cc.getCoins() + 
/* 199 */         "§2" + " Happiness:" + "§a" + cc.getHappiness() + 
/* 200 */         "§2" + " Hammers:" + "§a" + cc.getHammers() + 
/* 201 */         "§2" + " Growth:" + "§a" + cc.getGrowth() + 
/* 202 */         "§2" + " Beakers:" + "§a" + cc.getBeakers());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Biome getBiomeFromLocation(Location loc)
/*     */   {
/* 208 */     Block block = loc.getChunk().getBlock(0, 0, 0);
/* 209 */     return block.getBiome();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\CultureChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */