/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.structure.Barracks;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
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
/*    */ public class UnitTrainTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 34 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/* 35 */     while (iter.hasNext()) {
/* 36 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/* 37 */       if ((struct instanceof Barracks)) {
/* 38 */         ((Barracks)struct).updateTraining();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\UnitTrainTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */