/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.util.TimeTools;
/*    */ import com.avrgaming.civcraft.war.War;
/*    */ import java.util.Date;
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
/*    */ public class WarEndCheckTask
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 32 */     Date now = new Date();
/* 33 */     if (War.isWarTime()) {
/* 34 */       if ((War.getEnd() == null) || (now.after(War.getEnd()))) {
/* 35 */         War.setWarTime(false);
/*    */       } else {
/* 37 */         TaskMaster.syncTask(this, TimeTools.toTicks(1L));
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\WarEndCheckTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */