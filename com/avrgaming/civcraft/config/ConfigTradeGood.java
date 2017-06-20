/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import java.util.HashMap;
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
/*     */ public class ConfigTradeGood
/*     */   implements Comparable<ConfigTradeGood>
/*     */ {
/*     */   public String id;
/*     */   public String name;
/*     */   public double value;
/*     */   public boolean water;
/*  35 */   public HashMap<String, ConfigBuff> buffs = new HashMap();
/*     */   public int material;
/*     */   public int material_data;
/*  38 */   public String hemiString = null;
/*  39 */   public Double rarity = null;
/*     */   
/*     */   public static void loadBuffsString(ConfigTradeGood good, String bonus)
/*     */   {
/*  43 */     String[] keys = bonus.split(",");
/*     */     String[] arrayOfString1;
/*  45 */     int j = (arrayOfString1 = keys).length; for (int i = 0; i < j; i++) { String key = arrayOfString1[i];
/*  46 */       ConfigBuff cBuff = (ConfigBuff)CivSettings.buffs.get(key.replace(" ", ""));
/*  47 */       if (cBuff != null) {
/*  48 */         good.buffs.put(key, cBuff);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void loadConfig(FileConfiguration cfg, Map<String, ConfigTradeGood> goods, Map<String, ConfigTradeGood> landGoods, Map<String, ConfigTradeGood> waterGoods)
/*     */   {
/*  56 */     goods.clear();
/*  57 */     List<Map<?, ?>> land_goods = cfg.getMapList("land_goods");
/*  58 */     ConfigTradeGood good; for (Map<?, ?> g : land_goods) {
/*  59 */       good = new ConfigTradeGood();
/*  60 */       good.id = ((String)g.get("id"));
/*  61 */       good.name = ((String)g.get("name"));
/*  62 */       good.value = ((Double)g.get("value")).doubleValue();
/*  63 */       loadBuffsString(good, (String)g.get("buffs"));
/*  64 */       good.water = false;
/*  65 */       good.material = ((Integer)g.get("material")).intValue();
/*  66 */       good.material_data = ((Integer)g.get("material_data")).intValue();
/*  67 */       good.hemiString = ((String)g.get("hemispheres"));
/*  68 */       good.rarity = ((Double)g.get("rarity"));
/*  69 */       if (good.rarity == null) {
/*  70 */         good.rarity = Double.valueOf(1.0D);
/*     */       }
/*  72 */       landGoods.put(good.id, good);
/*  73 */       goods.put(good.id, good);
/*     */     }
/*     */     
/*  76 */     List<Map<?, ?>> water_goods = cfg.getMapList("water_goods");
/*  77 */     for (Object g : water_goods) {
/*  78 */       ConfigTradeGood good = new ConfigTradeGood();
/*  79 */       good.id = ((String)((Map)g).get("id"));
/*  80 */       good.name = ((String)((Map)g).get("name"));
/*  81 */       good.value = ((Double)((Map)g).get("value")).doubleValue();
/*  82 */       loadBuffsString(good, (String)((Map)g).get("buffs"));
/*  83 */       good.water = true;
/*  84 */       good.material = ((Integer)((Map)g).get("material")).intValue();
/*  85 */       good.material_data = ((Integer)((Map)g).get("material_data")).intValue();
/*  86 */       good.hemiString = ((String)((Map)g).get("hemispheres"));
/*  87 */       good.rarity = ((Double)((Map)g).get("rarity"));
/*  88 */       if (good.rarity == null) {
/*  89 */         good.rarity = Double.valueOf(1.0D);
/*     */       }
/*     */       
/*     */ 
/*  93 */       waterGoods.put(good.id, good);
/*  94 */       goods.put(good.id, good);
/*     */     }
/*     */     
/*  97 */     CivLog.info("Loaded " + goods.size() + " Trade Goods.");
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(ConfigTradeGood otherGood)
/*     */   {
/* 103 */     if (this.rarity.doubleValue() < otherGood.rarity.doubleValue())
/*     */     {
/* 105 */       return 1; }
/* 106 */     if (this.rarity == otherGood.rarity) {
/* 107 */       return 0;
/*     */     }
/* 109 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigTradeGood.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */