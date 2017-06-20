/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.block.Biome;
/*     */ 
/*     */ public class AttributeBiomeRadiusPerLevel extends AttributeBiomeBase
/*     */ {
/*  12 */   private HashMap<String, Double> biomeInfo = new HashMap();
/*     */   
/*     */ 
/*     */   private String attribute;
/*     */   
/*     */   private double baseValue;
/*     */   
/*     */   private ChunkCoord centerCoord;
/*     */   
/*     */ 
/*     */   public void createComponent(Buildable buildable, boolean async)
/*     */   {
/*  24 */     super.createComponent(buildable, async);
/*     */     
/*  26 */     String[] biomes = getString("biomes").split(",");
/*  27 */     String[] arrayOfString1; int j = (arrayOfString1 = biomes).length; for (int i = 0; i < j; i++) { String biomeInfoStr = arrayOfString1[i];
/*  28 */       String[] split = biomeInfoStr.split(":");
/*  29 */       String biome = split[0];
/*  30 */       Double val = Double.valueOf(split[1]);
/*     */       
/*  32 */       this.biomeInfo.put(biome.trim().toUpperCase(), val);
/*     */     }
/*     */     
/*  35 */     setAttribute(getString("attribute"));
/*  36 */     setBaseValue(getDouble("base_value"));
/*     */     
/*  38 */     this.centerCoord = new ChunkCoord(buildable.getCorner());
/*     */   }
/*     */   
/*     */   public String getAttribute()
/*     */   {
/*  43 */     return this.attribute;
/*     */   }
/*     */   
/*     */   public void setAttribute(String attribute) {
/*  47 */     this.attribute = attribute;
/*     */   }
/*     */   
/*     */   public double getBaseValue() {
/*  51 */     return this.baseValue;
/*     */   }
/*     */   
/*     */   public void setBaseValue(double baseValue) {
/*  55 */     this.baseValue = baseValue;
/*     */   }
/*     */   
/*     */   public boolean isInRange(ChunkCoord coord) {
/*  59 */     int diffX = coord.getX() - this.centerCoord.getX();
/*  60 */     int diffZ = coord.getZ() - this.centerCoord.getZ();
/*     */     
/*  62 */     if ((diffX > 1) || (diffX < -1)) {
/*  63 */       return false;
/*     */     }
/*     */     
/*  66 */     if ((diffZ > 1) || (diffZ < -1)) {
/*  67 */       return false;
/*     */     }
/*     */     
/*  70 */     return true;
/*     */   }
/*     */   
/*     */   public double getGenerated(CultureChunk cc) {
/*  74 */     if (!getBuildable().isActive()) {
/*  75 */       return 0.0D;
/*     */     }
/*     */     
/*  78 */     if (!isInRange(cc.getChunkCoord())) {
/*  79 */       return 0.0D;
/*     */     }
/*     */     
/*  82 */     int mineLevel = -1;
/*  83 */     for (Component comp : getBuildable().attachedComponents) {
/*  84 */       if ((comp instanceof ConsumeLevelComponent)) {
/*  85 */         ConsumeLevelComponent consumeComp = (ConsumeLevelComponent)comp;
/*  86 */         mineLevel = consumeComp.getLevel();
/*     */       }
/*     */     }
/*     */     
/*  90 */     if (mineLevel == -1) {
/*  91 */       CivLog.warning(
/*  92 */         "Couldn't find consume component for buildable " + getBuildable().getDisplayName() + " but it has an AttributeBiomeRadiusPerLevel component attached.");
/*  93 */       return 0.0D;
/*     */     }
/*     */     
/*  96 */     double generated = getBaseValue() * mineLevel;
/*  97 */     Double extra = (Double)this.biomeInfo.get(cc.getBiome().name());
/*     */     
/*  99 */     if (extra != null) {
/* 100 */       generated += extra.doubleValue() * mineLevel;
/*     */     }
/*     */     
/* 103 */     return generated;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\AttributeBiomeRadiusPerLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */