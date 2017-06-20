/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.object.Resident;
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
/*    */ public class PlayerDelayedDebtWarning
/*    */   implements Runnable
/*    */ {
/*    */   Resident resident;
/*    */   
/*    */   public PlayerDelayedDebtWarning(Resident resident)
/*    */   {
/* 28 */     this.resident = resident;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 33 */     this.resident.warnDebt();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PlayerDelayedDebtWarning.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */