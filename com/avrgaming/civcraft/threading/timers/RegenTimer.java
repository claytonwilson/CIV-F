/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class RegenTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 15 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/*    */     
/* 17 */     while (iter.hasNext()) {
/* 18 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/* 19 */       struct.processRegen();
/*    */     }
/*    */     
/* 22 */     for (Wonder wonder : CivGlobal.getWonders()) {
/* 23 */       wonder.processRegen();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\RegenTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */