/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ public class ActivateOnBiome extends Component
/*    */ {
/*    */   public static enum EffectType
/*    */   {
/* 10 */     ALL, 
/* 11 */     THIS;
/*    */   }
/*    */   
/* 14 */   private HashSet<String> biomeList = new HashSet();
/*    */   private String attribute;
/*    */   private double value;
/*    */   private EffectType effectType;
/*    */   
/*    */   public void createComponent(Buildable buildable, boolean async)
/*    */   {
/* 21 */     super.createComponent(buildable, async);
/*    */     
/* 23 */     String[] biomes = getString("biomes").split(",");
/* 24 */     String[] arrayOfString1; int j = (arrayOfString1 = biomes).length; for (int i = 0; i < j; i++) { String biome = arrayOfString1[i];
/* 25 */       this.biomeList.add(biome.trim().toUpperCase());
/*    */     }
/*    */     
/* 28 */     setAttribute(getString("attribute"));
/* 29 */     setValue(getDouble("value"));
/* 30 */     setEffectType(EffectType.valueOf(getString("effect").toUpperCase()));
/*    */   }
/*    */   
/*    */   public String getAttribute() {
/* 34 */     return this.attribute;
/*    */   }
/*    */   
/*    */   public void setAttribute(String attribute) {
/* 38 */     this.attribute = attribute;
/*    */   }
/*    */   
/*    */   public double getValue() {
/* 42 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(double value) {
/* 46 */     this.value = value;
/*    */   }
/*    */   
/*    */   public EffectType getEffectType() {
/* 50 */     return this.effectType;
/*    */   }
/*    */   
/*    */   public void setEffectType(EffectType effectType) {
/* 54 */     this.effectType = effectType;
/*    */   }
/*    */   
/*    */   public boolean isValidBiome(String biomeName) {
/* 58 */     return this.biomeList.contains(biomeName);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\ActivateOnBiome.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */