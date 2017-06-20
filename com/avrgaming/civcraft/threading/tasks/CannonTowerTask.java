/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
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
/*    */ public class CannonTowerTask
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 28 */       if (!CivGlobal.towersEnabled) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\CannonTowerTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */