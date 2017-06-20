/*     */ package com.avrgaming.civcraft.populators;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigHemisphere;
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.TreeSet;
/*     */ import org.bukkit.Bukkit;
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
/*     */ public class TradeGoodPreGenerate
/*     */ {
/*     */   private int chunks_min;
/*     */   private int chunks_max;
/*     */   private int chunks_x;
/*     */   private int chunks_z;
/*     */   private int seed;
/*     */   private String worldName;
/*  47 */   public Map<ChunkCoord, TradeGoodPick> goodPicks = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean validHemisphere(ConfigHemisphere hemi, int x, int z)
/*     */   {
/*  55 */     if ((hemi.x_max != 0) && (x > hemi.x_max)) {
/*  56 */       return false;
/*     */     }
/*  58 */     if ((hemi.x_min != 0) && (x < hemi.x_min)) {
/*  59 */       return false;
/*     */     }
/*  61 */     if ((hemi.z_max != 0) && (z > hemi.z_max)) {
/*  62 */       return false;
/*     */     }
/*  64 */     if ((hemi.z_min != 0) && (z < hemi.z_min)) {
/*  65 */       return false;
/*     */     }
/*  67 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private TreeSet<ConfigTradeGood> getValidTradeGoods(int x, int z, Map<String, ConfigTradeGood> goods)
/*     */   {
/*  74 */     TreeSet<ConfigTradeGood> validGoods = new TreeSet();
/*  75 */     for (ConfigTradeGood good : goods.values()) {
/*  76 */       String hemiString = good.hemiString;
/*  77 */       if (hemiString == null)
/*     */       {
/*  79 */         validGoods.add(good);
/*     */       }
/*     */       else
/*     */       {
/*  83 */         String[] hemiStrs = hemiString.split(",");
/*  84 */         String[] arrayOfString1; int j = (arrayOfString1 = hemiStrs).length; for (int i = 0; i < j; i++) { String str = arrayOfString1[i];
/*  85 */           ConfigHemisphere hemi = (ConfigHemisphere)CivSettings.hemispheres.get(str);
/*  86 */           if (hemi == null) {
/*  87 */             CivLog.warning("Invalid hemisphere:" + str + " detected for trade good generation.");
/*     */ 
/*     */ 
/*     */           }
/*  91 */           else if (validHemisphere(hemi, x, z)) {
/*  92 */             validGoods.add(good);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  97 */     return validGoods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void preGenerate()
/*     */   {
/*     */     try
/*     */     {
/* 107 */       this.chunks_min = CivSettings.getInteger(CivSettings.goodsConfig, "generation.chunks_min").intValue();
/* 108 */       this.chunks_max = CivSettings.getInteger(CivSettings.goodsConfig, "generation.chunks_max").intValue();
/* 109 */       this.chunks_x = CivSettings.getInteger(CivSettings.goodsConfig, "generation.chunks_x").intValue();
/* 110 */       this.chunks_z = CivSettings.getInteger(CivSettings.goodsConfig, "generation.chunks_z").intValue();
/* 111 */       this.seed = CivSettings.getInteger(CivSettings.goodsConfig, "generation.seed").intValue();
/* 112 */       this.worldName = ((World)Bukkit.getWorlds().get(0)).getName();
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 115 */       e.printStackTrace();
/*     */     }
/*     */     
/* 118 */     Random rand = new Random();
/* 119 */     rand.setSeed(this.seed);
/* 120 */     CivLog.info("Generating Trade Goodie Locations.");
/* 121 */     for (int x = -this.chunks_x; x < this.chunks_x; x += this.chunks_min) {
/* 122 */       for (int z = -this.chunks_z; z < this.chunks_z; z += this.chunks_min) {
/* 123 */         int diff = this.chunks_max - this.chunks_min;
/* 124 */         int randX = x;
/* 125 */         int randZ = z;
/*     */         
/* 127 */         if (diff > 0) {
/* 128 */           if (rand.nextBoolean()) {
/* 129 */             randX += rand.nextInt(diff);
/*     */           } else {
/* 131 */             randX -= rand.nextInt(diff);
/*     */           }
/*     */           
/* 134 */           if (rand.nextBoolean()) {
/* 135 */             randZ += rand.nextInt(diff);
/*     */           } else {
/* 137 */             randZ -= rand.nextInt(diff);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 142 */         ChunkCoord cCoord = new ChunkCoord(this.worldName, randX, randZ);
/* 143 */         pickFromCoord(cCoord);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 148 */     CivLog.info("Done.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ConfigTradeGood pickFromSet(TreeSet<ConfigTradeGood> set, int rand)
/*     */   {
/* 155 */     double lowest_rarity = Double.MAX_VALUE;
/* 156 */     for (ConfigTradeGood good : set) {
/* 157 */       if ((rand < good.rarity.doubleValue() * 100.0D) && 
/* 158 */         (good.rarity.doubleValue() < lowest_rarity)) {
/* 159 */         lowest_rarity = good.rarity.doubleValue();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 165 */     ArrayList<ConfigTradeGood> pickList = new ArrayList();
/* 166 */     for (ConfigTradeGood good : set) {
/* 167 */       if (good.rarity.doubleValue() == lowest_rarity) {
/* 168 */         pickList.add(good);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 173 */     Random random = new Random();
/*     */     
/* 175 */     return (ConfigTradeGood)pickList.get(random.nextInt(pickList.size()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void pickFromCoord(ChunkCoord cCoord)
/*     */   {
/* 182 */     TradeGoodPick pick = new TradeGoodPick();
/*     */     
/* 184 */     TreeSet<ConfigTradeGood> validLandGoods = getValidTradeGoods(cCoord.getX(), cCoord.getZ(), CivSettings.landGoods);
/* 185 */     TreeSet<ConfigTradeGood> validWaterGoods = getValidTradeGoods(cCoord.getX(), cCoord.getZ(), CivSettings.waterGoods);
/*     */     
/* 187 */     pick.chunkCoord = cCoord;
/*     */     
/* 189 */     Random random = new Random();
/* 190 */     int rand = random.nextInt(100);
/*     */     
/* 192 */     pick.landPick = pickFromSet(validLandGoods, rand);
/* 193 */     pick.waterPick = pickFromSet(validWaterGoods, rand);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 199 */     for (int x = -4; x < 4; x++) {
/* 200 */       for (int z = -4; z < 4; z++) {
/* 201 */         ChunkCoord n = new ChunkCoord(cCoord.getWorldname(), cCoord.getX(), cCoord.getZ());
/* 202 */         n.setX(n.getX() + x);
/* 203 */         n.setZ(n.getZ() + z);
/*     */         
/* 205 */         TradeGoodPick nearby = (TradeGoodPick)this.goodPicks.get(n);
/* 206 */         if (nearby != null)
/*     */         {
/*     */ 
/*     */ 
/* 210 */           if (nearby.landPick == pick.landPick) {
/* 211 */             if (validLandGoods.size() <= 1)
/*     */             {
/* 213 */               return;
/*     */             }
/* 215 */             while (nearby.landPick == pick.landPick) {
/* 216 */               rand = random.nextInt(100);
/* 217 */               pick.landPick = pickFromSet(validLandGoods, rand);
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 222 */           if (nearby.waterPick == pick.waterPick) {
/* 223 */             if (validLandGoods.size() <= 1)
/*     */             {
/* 225 */               return;
/*     */             }
/* 227 */             while (nearby.landPick == pick.landPick) {
/* 228 */               rand = random.nextInt(100);
/* 229 */               pick.landPick = pickFromSet(validLandGoods, rand);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 237 */     this.goodPicks.put(cCoord, pick);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\populators\TradeGoodPreGenerate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */