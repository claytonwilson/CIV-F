/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.structure.Barracks;
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
/*    */ public class UnitSaveAsyncTask
/*    */   implements Runnable
/*    */ {
/*    */   Barracks barracks;
/*    */   
/*    */   public UnitSaveAsyncTask(Barracks barracks)
/*    */   {
/* 28 */     this.barracks = barracks;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 33 */     this.barracks.saveProgress();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\UnitSaveAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */