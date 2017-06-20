/*    */ package com.avrgaming.civcraft.lorestorage;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoreStoreage
/*    */ {
/*    */   public static void setMatID(int id, ItemStack stack)
/*    */   {
/* 34 */     ItemMeta meta = stack.getItemMeta();
/*    */     List<String> lore;
/*    */     List<String> lore;
/* 37 */     if (meta.hasLore()) {
/* 38 */       lore = meta.getLore();
/*    */     } else {
/* 40 */       lore = new ArrayList();
/*    */     }
/*    */     
/* 43 */     lore.set(0, "§0MID:" + id);
/* 44 */     meta.setLore(lore);
/* 45 */     stack.setItemMeta(meta);
/*    */   }
/*    */   
/*    */ 
/*    */   public static void setItemName(String name, ItemStack stack)
/*    */   {
/* 51 */     ItemMeta meta = stack.getItemMeta();
/* 52 */     meta.setDisplayName(name);
/* 53 */     stack.setItemMeta(meta);
/*    */   }
/*    */   
/*    */   public static void saveLoreMap(String type, Map<String, String> map, ItemStack stack)
/*    */   {
/* 58 */     ItemMeta meta = stack.getItemMeta();
/* 59 */     ArrayList<String> lore = new ArrayList();
/*    */     
/* 61 */     lore.add("§3" + type);
/*    */     
/* 63 */     for (String key : map.keySet()) {
/* 64 */       String value = (String)map.get(key);
/* 65 */       lore.add("§8" + key + ":" + value);
/*    */     }
/*    */     
/* 68 */     meta.setLore(lore);
/* 69 */     stack.setItemMeta(meta);
/*    */   }
/*    */   
/*    */   public static String getType(ItemStack stack)
/*    */   {
/* 74 */     ItemMeta meta = stack.getItemMeta();
/*    */     
/* 76 */     if (meta.hasLore()) {
/* 77 */       List<String> lore = meta.getLore();
/* 78 */       return (String)lore.get(0);
/*    */     }
/* 80 */     return "none";
/*    */   }
/*    */   
/*    */   public static Map<String, String> getLoreMap(ItemStack stack)
/*    */   {
/* 85 */     HashMap<String, String> map = new HashMap();
/*    */     
/* 87 */     ItemMeta meta = stack.getItemMeta();
/* 88 */     if (meta.hasLore()) {
/* 89 */       List<String> lore = meta.getLore();
/* 90 */       for (String str : lore) {
/* 91 */         String[] split = str.split(":");
/* 92 */         if (split.length > 2) {
/* 93 */           map.put(split[0], split[1]);
/*    */         }
/*    */       }
/*    */     }
/* 97 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\lorestorage\LoreStoreage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */