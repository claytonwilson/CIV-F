/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.Inventory;
/*    */ import org.bukkit.inventory.ItemStack;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelayMoveInventoryItem
/*    */   implements Runnable
/*    */ {
/*    */   public int fromSlot;
/*    */   public int toSlot;
/*    */   public Inventory inv;
/*    */   public String playerName;
/*    */   
/*    */   public void run()
/*    */   {
/* 46 */     ItemStack fromStack = this.inv.getItem(this.fromSlot);
/* 47 */     ItemStack toStack = this.inv.getItem(this.toSlot);
/*    */     
/* 49 */     if (fromStack != null) {
/* 50 */       this.inv.setItem(this.toSlot, fromStack);
/* 51 */       this.inv.setItem(this.fromSlot, toStack);
/* 52 */       if (this.playerName != null) {
/*    */         try
/*    */         {
/* 55 */           Player player = CivGlobal.getPlayer(this.playerName);
/* 56 */           player.updateInventory();
/*    */         }
/*    */         catch (CivException localCivException) {}
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\DelayMoveInventoryItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */