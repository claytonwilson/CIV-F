/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
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
/*     */ public class ConfigGovernment
/*     */ {
/*     */   public String id;
/*     */   public String displayName;
/*     */   public String require_tech;
/*     */   public double trade_rate;
/*     */   public double upkeep_rate;
/*     */   public double cottage_rate;
/*     */   public double growth_rate;
/*     */   public double culture_rate;
/*     */   public double hammer_rate;
/*     */   public double beaker_rate;
/*     */   public double maximum_tax_rate;
/*     */   
/*     */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigGovernment> government_map)
/*     */   {
/*  46 */     government_map.clear();
/*  47 */     List<Map<?, ?>> techs = cfg.getMapList("governments");
/*  48 */     for (Map<?, ?> level : techs) {
/*  49 */       ConfigGovernment gov = new ConfigGovernment();
/*     */       
/*  51 */       gov.id = ((String)level.get("id"));
/*  52 */       gov.displayName = ((String)level.get("displayName"));
/*  53 */       gov.require_tech = ((String)level.get("require_tech"));
/*     */       
/*  55 */       gov.trade_rate = ((Double)level.get("trade_rate")).doubleValue();
/*  56 */       gov.upkeep_rate = ((Double)level.get("upkeep_rate")).doubleValue();
/*  57 */       gov.cottage_rate = ((Double)level.get("cottage_rate")).doubleValue();
/*  58 */       gov.growth_rate = ((Double)level.get("growth_rate")).doubleValue();
/*  59 */       gov.culture_rate = ((Double)level.get("culture_rate")).doubleValue();
/*  60 */       gov.hammer_rate = ((Double)level.get("hammer_rate")).doubleValue();
/*  61 */       gov.beaker_rate = ((Double)level.get("beaker_rate")).doubleValue();
/*  62 */       gov.maximum_tax_rate = ((Double)level.get("maximum_tax_rate")).doubleValue();
/*     */       
/*  64 */       government_map.put(gov.id, gov);
/*     */     }
/*  66 */     CivLog.info("Loaded " + government_map.size() + " governments.");
/*     */   }
/*     */   
/*     */   public static ArrayList<ConfigGovernment> getAvailableGovernments(Civilization civ) {
/*  70 */     ArrayList<ConfigGovernment> govs = new ArrayList();
/*     */     
/*  72 */     for (ConfigGovernment gov : CivSettings.governments.values()) {
/*  73 */       if (!gov.id.equalsIgnoreCase("gov_anarchy"))
/*     */       {
/*     */ 
/*  76 */         if (gov.isAvailable(civ)) {
/*  77 */           govs.add(gov);
/*     */         }
/*     */       }
/*     */     }
/*  81 */     return govs;
/*     */   }
/*     */   
/*     */   public static ConfigGovernment getGovernmentFromName(String string)
/*     */   {
/*  86 */     for (ConfigGovernment gov : CivSettings.governments.values()) {
/*  87 */       if (!gov.id.equalsIgnoreCase("gov_anarchy"))
/*     */       {
/*     */ 
/*  90 */         if (gov.displayName.equalsIgnoreCase(string)) {
/*  91 */           return gov;
/*     */         }
/*     */       }
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isAvailable(Civilization civ) {
/*  99 */     if (civ.hasTechnology(this.require_tech)) {
/* 100 */       return true;
/*     */     }
/* 102 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigGovernment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */