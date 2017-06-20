/*    */ package com.avrgaming.civcraft.items;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.SQLObject;
/*    */ import java.util.List;
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
/*    */ public abstract class LoreItem
/*    */   extends SQLObject
/*    */ {
/*    */   private Type type;
/*    */   public abstract void load();
/*    */   
/*    */   public static enum Type
/*    */   {
/* 33 */     NORMAL, 
/* 34 */     BONUSGOODIE;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setLore(ItemStack stack, List<String> lore)
/*    */   {
/* 45 */     ItemMeta meta = stack.getItemMeta();
/* 46 */     meta.setLore(lore);
/* 47 */     stack.setItemMeta(meta);
/*    */   }
/*    */   
/*    */   public List<String> getLore(ItemStack stack) {
/* 51 */     return stack.getItemMeta().getLore();
/*    */   }
/*    */   
/*    */   public void setDisplayName(ItemStack stack, String name) {
/* 55 */     ItemMeta meta = stack.getItemMeta();
/* 56 */     meta.setDisplayName(name);
/* 57 */     stack.setItemMeta(meta);
/*    */   }
/*    */   
/*    */   public String getDisplayName(ItemStack stack) {
/* 61 */     return stack.getItemMeta().getDisplayName();
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 65 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(Type type) {
/* 69 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\LoreItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */