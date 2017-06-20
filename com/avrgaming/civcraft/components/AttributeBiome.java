/*    */ package com.avrgaming.civcraft.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.CultureChunk;
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import java.util.HashSet;
/*    */ import org.bukkit.block.Biome;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeBiome
/*    */   extends AttributeBiomeBase
/*    */ {
/* 16 */   private HashSet<String> biomeList = new HashSet();
/*    */   
/*    */   private String attribute;
/*    */   
/*    */   private double value;
/*    */   
/*    */ 
/*    */   public double getGenerated(CultureChunk cc)
/*    */   {
/* 25 */     if (!getBuildable().isActive()) {
/* 26 */       return 0.0D;
/*    */     }
/*    */     
/* 29 */     if (!this.biomeList.contains(cc.getBiome().name().toUpperCase())) {
/* 30 */       return 0.0D;
/*    */     }
/*    */     
/* 33 */     return this.value;
/*    */   }
/*    */   
/*    */   public void createComponent(Buildable buildable, boolean async)
/*    */   {
/* 38 */     super.createComponent(buildable, async);
/*    */     
/* 40 */     String[] biomes = getString("biomes").split(",");
/* 41 */     String[] arrayOfString1; int j = (arrayOfString1 = biomes).length; for (int i = 0; i < j; i++) { String biome = arrayOfString1[i];
/* 42 */       this.biomeList.add(biome.trim().toUpperCase());
/*    */     }
/*    */     
/* 45 */     this.attribute = getString("attribute");
/* 46 */     this.value = getDouble("value");
/*    */   }
/*    */   
/*    */ 
/*    */   public String getAttribute()
/*    */   {
/* 52 */     return this.attribute;
/*    */   }
/*    */   
/*    */   public void setAttribute(String attribute)
/*    */   {
/* 57 */     this.attribute = attribute;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\AttributeBiome.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */