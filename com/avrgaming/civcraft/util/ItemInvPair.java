/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import org.bukkit.inventory.Inventory;
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
/*    */ public class ItemInvPair
/*    */ {
/*    */   public Inventory inv;
/*    */   public String mid;
/*    */   public int type;
/*    */   public short data;
/*    */   public int amount;
/*    */   
/*    */   public ItemInvPair(Inventory inv, String mid, int type, short data, int amount)
/*    */   {
/* 31 */     this.inv = inv;
/* 32 */     this.mid = mid;
/* 33 */     this.type = type;
/* 34 */     this.data = data;
/* 35 */     this.amount = amount;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\ItemInvPair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */