/*    */ package com.avrgaming.civcraft.threading.sync.request;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.MultiInventory;
/*    */ import java.util.concurrent.locks.ReentrantLock;
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
/*    */ public class UpdateInventoryRequest
/*    */   extends AsyncRequest
/*    */ {
/*    */   public MultiInventory inv;
/*    */   public ItemStack stack;
/*    */   public Action action;
/*    */   
/*    */   public UpdateInventoryRequest(ReentrantLock lock)
/*    */   {
/* 30 */     super(lock);
/*    */   }
/*    */   
/*    */   public static enum Action {
/* 34 */     ADD, 
/* 35 */     REMOVE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\sync\request\UpdateInventoryRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */