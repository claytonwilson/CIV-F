/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ public class ConfigMaterialCategory
/*    */ {
/*  9 */   private static TreeMap<String, ConfigMaterialCategory> categories = new TreeMap();
/*    */   
/*    */   public String name;
/* 12 */   public HashMap<String, ConfigMaterial> materials = new HashMap();
/* 13 */   public int craftableCount = 0;
/*    */   
/*    */   public static void addMaterial(ConfigMaterial mat) {
/* 16 */     ConfigMaterialCategory cat = (ConfigMaterialCategory)categories.get(mat.categoryCivColortripped);
/* 17 */     if (cat == null) {
/* 18 */       cat = new ConfigMaterialCategory();
/* 19 */       cat.name = mat.category;
/*    */     }
/*    */     
/* 22 */     cat.materials.put(mat.id, mat);
/* 23 */     if (mat.craftable) {
/* 24 */       cat.craftableCount += 1;
/*    */     }
/* 26 */     categories.put(mat.categoryCivColortripped, cat);
/*    */   }
/*    */   
/*    */   public static Collection<ConfigMaterialCategory> getCategories()
/*    */   {
/* 31 */     return categories.values();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigMaterialCategory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */