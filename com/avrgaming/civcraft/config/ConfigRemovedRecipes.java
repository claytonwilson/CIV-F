/*    */ package com.avrgaming.civcraft.config;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.Recipe;
/*    */ import org.bukkit.inventory.ShapedRecipe;
/*    */ 
/*    */ public class ConfigRemovedRecipes
/*    */ {
/*    */   public int type_id;
/*    */   public int data;
/*    */   
/*    */   public static void removeRecipes(FileConfiguration cfg, HashMap<Integer, ConfigRemovedRecipes> removedRecipies)
/*    */   {
/* 22 */     List<Map<?, ?>> configMaterials = cfg.getMapList("removed_recipes");
/* 23 */     for (Map<?, ?> b : configMaterials) {
/* 24 */       ConfigRemovedRecipes item = new ConfigRemovedRecipes();
/* 25 */       item.type_id = ((Integer)b.get("type_id")).intValue();
/* 26 */       item.data = ((Integer)b.get("data")).intValue();
/*    */       
/* 28 */       removedRecipies.put(Integer.valueOf(item.type_id), item);
/*    */       
/* 30 */       Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
/* 31 */       while (it.hasNext()) {
/* 32 */         Recipe recipe = (Recipe)it.next();
/*    */         
/* 34 */         if ((recipe instanceof ShapedRecipe)) {
/* 35 */           ShapedRecipe shapedRecipe = (ShapedRecipe)recipe;
/* 36 */           if ((ItemManager.getId(shapedRecipe.getResult()) == item.type_id) && 
/* 37 */             (shapedRecipe.getResult().getDurability() == (short)item.data)) {
/* 38 */             it.remove();
/* 39 */             break;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigRemovedRecipes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */