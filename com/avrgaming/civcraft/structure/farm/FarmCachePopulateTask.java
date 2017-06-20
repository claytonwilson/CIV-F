/*    */ package com.avrgaming.civcraft.structure.farm;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import java.util.LinkedList;
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
/*    */ public class FarmCachePopulateTask
/*    */   implements Runnable
/*    */ {
/*    */   LinkedList<FarmChunk> farms;
/*    */   
/*    */   public FarmCachePopulateTask(LinkedList<FarmChunk> farms)
/*    */   {
/* 31 */     this.farms = farms;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 36 */     if (!CivGlobal.growthEnabled) {
/* 37 */       return;
/*    */     }
/*    */     
/* 40 */     for (FarmChunk fc : this.farms) {
/*    */       try {
/* 42 */         fc.populateCropLocationCache();
/*    */       } catch (Exception e) {
/* 44 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\farm\FarmCachePopulateTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */