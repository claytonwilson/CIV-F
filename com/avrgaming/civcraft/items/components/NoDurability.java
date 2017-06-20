/*    */ package com.avrgaming.civcraft.items.components;
/*    */ 
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoDurability
/*    */   extends ItemComponent
/*    */ {
/*    */   public void onPrepareCreate(AttributeUtil attrUtil) {}
/*    */   
/*    */   public void onInventoryOpen(InventoryOpenEvent event, ItemStack stack)
/*    */   {
/* 16 */     stack.setDurability((short)0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\components\NoDurability.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */