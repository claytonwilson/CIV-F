/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigGovernment;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bukkit.Location;
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
/*     */ public class Trommel
/*     */   extends Structure
/*     */ {
/*  35 */   private static final double IRON_CHANCE = CivSettings.getDoubleStructure("trommel.iron_chance").doubleValue();
/*  36 */   private static final double GOLD_CHANCE = CivSettings.getDoubleStructure("trommel.gold_chance").doubleValue();
/*  37 */   private static final double DIAMOND_CHANCE = CivSettings.getDoubleStructure("trommel.diamond_chance").doubleValue();
/*  38 */   private static final double EMERALD_CHANCE = CivSettings.getDoubleStructure("trommel.emerald_chance").doubleValue();
/*  39 */   private static final double CHROMIUM_CHANCE = CivSettings.getDoubleStructure("trommel.chromium_chance").doubleValue();
/*     */   
/*  41 */   public int skippedCounter = 0;
/*  42 */   public ReentrantLock lock = new ReentrantLock();
/*     */   
/*     */   public static enum Mineral {
/*  45 */     EMERALD, 
/*  46 */     DIAMOND, 
/*  47 */     GOLD, 
/*  48 */     IRON, 
/*  49 */     CHROMIUM;
/*     */   }
/*     */   
/*     */   protected Trommel(Location center, String id, Town town) throws CivException {
/*  53 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public Trommel(ResultSet rs) throws SQLException, CivException {
/*  57 */     super(rs);
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/*  62 */     return null;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/*  67 */     return "minecart";
/*     */   }
/*     */   
/*     */   public double getMineralChance(Mineral mineral) {
/*  71 */     double chance = 0.0D;
/*  72 */     switch (mineral) {
/*     */     case CHROMIUM: 
/*  74 */       chance = EMERALD_CHANCE;
/*  75 */       break;
/*     */     case DIAMOND: 
/*  77 */       chance = DIAMOND_CHANCE;
/*  78 */       break;
/*     */     case EMERALD: 
/*  80 */       chance = GOLD_CHANCE;
/*  81 */       break;
/*     */     case GOLD: 
/*  83 */       chance = IRON_CHANCE;
/*  84 */       break;
/*     */     case IRON: 
/*  86 */       chance = CHROMIUM_CHANCE;
/*     */     }
/*     */     
/*  89 */     double increase = chance * getTown().getBuffManager().getEffectiveDouble("buff_extraction");
/*  90 */     chance += increase;
/*     */     try
/*     */     {
/*  93 */       if (getTown().getGovernment().id.equals("gov_despotism")) {
/*  94 */         chance *= CivSettings.getDouble(CivSettings.structureConfig, "trommel.despotism_rate");
/*     */       } else {
/*  96 */         chance *= CivSettings.getDouble(CivSettings.structureConfig, "trommel.penalty_rate");
/*     */       }
/*     */     } catch (InvalidConfiguration e) {
/*  99 */       e.printStackTrace();
/*     */     }
/*     */     
/* 102 */     return chance;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Trommel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */