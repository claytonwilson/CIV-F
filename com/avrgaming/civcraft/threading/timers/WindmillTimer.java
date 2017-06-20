/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.structure.Structure;
/*    */ import com.avrgaming.civcraft.structure.Windmill;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import com.avrgaming.civcraft.war.War;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class WindmillTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 16 */     if (War.isWarTime()) {
/* 17 */       return;
/*    */     }
/*    */     
/* 20 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/* 21 */     while (iter.hasNext()) {
/* 22 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/* 23 */       if ((struct instanceof Windmill)) {
/* 24 */         ((Windmill)struct).processWindmill();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\WindmillTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */