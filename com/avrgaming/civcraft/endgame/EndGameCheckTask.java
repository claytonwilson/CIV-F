/*    */ package com.avrgaming.civcraft.endgame;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*    */ import java.util.ArrayList;
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
/*    */ public class EndGameCheckTask
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 29 */     if (CivGlobal.isCasualMode()) {
/* 30 */       return;
/*    */     }
/*    */     
/* 33 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("endgame:winningCiv");
/* 34 */     if (entries.size() != 0) {
/* 35 */       CivMessage.global(((SessionEntry)entries.get(0)).value);
/* 36 */       return;
/*    */     }
/*    */     
/* 39 */     for (Civilization civ : CivGlobal.getCivs()) {
/* 40 */       if (!civ.isAdminCiv())
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/* 45 */         for (EndGameCondition cond : EndGameCondition.endConditions) {
/* 46 */           if (cond.check(civ)) {
/* 47 */             cond.onSuccess(civ);
/*    */           } else {
/* 49 */             cond.onFailure(civ);
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\EndGameCheckTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */