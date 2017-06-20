/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuff;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Map;
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
/*     */ public class Buff
/*     */ {
/*     */   public static final String FINE_ART = "buff_fine_art";
/*     */   public static final String CONSTRUCTION = "buff_construction";
/*     */   public static final String GROWTH_RATE = "buff_year_of_plenty";
/*     */   public static final String TRADE = "buff_monopoly";
/*     */   public static final String REDUCE_CONSUME = "buff_preservative";
/*     */   public static final String SCIENCE_RATE = "buff_innovation";
/*     */   public static final String EXTRA_CULTURE = "buff_doesnotexist";
/*     */   public static final String COTTAGE_RATE = "buff_doesnotexist";
/*     */   public static final String ADVANCED_TOOLING = "buff_advanced_tooling";
/*     */   public static final String BARRICADE = "buff_barricade";
/*     */   public static final String BARTER = "buff_barter";
/*     */   public static final String EXTRACTION = "buff_extraction";
/*     */   public static final String FIRE_BOMB = "buff_fire_bomb";
/*     */   public static final String FISHING = "buff_fishing";
/*     */   public static final String MEDICINE = "buff_medicine";
/*     */   public static final String RUSH = "buff_rush";
/*     */   public static final String DEBUFF_PYRAMID_LEECH = "debuff_pyramid_leech";
/*     */   private ConfigBuff config;
/*     */   private String source;
/*     */   private String key;
/*     */   
/*     */   public Buff(String buffkey, String buff_id, String source)
/*     */   {
/*  52 */     this.config = ((ConfigBuff)CivSettings.buffs.get(buff_id));
/*  53 */     setKey(buffkey);
/*  54 */     this.source = source;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  59 */     return this.config.id.toString().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/*  64 */     if ((other instanceof Buff)) {
/*  65 */       Buff otherBuff = (Buff)other;
/*  66 */       if (otherBuff.getConfig().id.equals(getConfig().id)) {
/*  67 */         return true;
/*     */       }
/*     */     }
/*  70 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSource()
/*     */   {
/*  77 */     return this.source;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSource(String source)
/*     */   {
/*  84 */     this.source = source;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConfigBuff getConfig()
/*     */   {
/*  91 */     return this.config;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setConfig(ConfigBuff config)
/*     */   {
/*  98 */     this.config = config;
/*     */   }
/*     */   
/*     */   public boolean isStackable() {
/* 102 */     return this.config.stackable;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 106 */     return this.config.id;
/*     */   }
/*     */   
/*     */   public Object getParent() {
/* 110 */     return this.config.parent;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 114 */     return this.config.value;
/*     */   }
/*     */   
/*     */   public String getDisplayDouble() {
/*     */     try {
/* 119 */       double d = Double.valueOf(this.config.value).doubleValue();
/* 120 */       DecimalFormat df = new DecimalFormat();
/* 121 */       return df.format(d * 100.0D) + "%";
/*     */     } catch (NumberFormatException e) {}
/* 123 */     return "NAN!";
/*     */   }
/*     */   
/*     */   public String getDisplayInt()
/*     */   {
/*     */     try {
/* 129 */       return Integer.valueOf(this.config.value).intValue();
/*     */     }
/*     */     catch (NumberFormatException e) {}
/* 132 */     return "NAN!";
/*     */   }
/*     */   
/*     */   public String getDisplayName()
/*     */   {
/* 137 */     return this.config.name;
/*     */   }
/*     */   
/*     */   public String getKey() {
/* 141 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/* 145 */     this.key = key;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\Buff.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */