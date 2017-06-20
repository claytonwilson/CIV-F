/*    */ package com.avrgaming.civcraft.endgame;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class EndConditionNotificationTask
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 16 */     for (EndGameCondition endCond : EndGameCondition.endConditions) {
/* 17 */       ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(endCond.getSessionKey());
/* 18 */       if (entries.size() != 0)
/*    */       {
/*    */ 
/*    */ 
/* 22 */         for (SessionEntry entry : entries) {
/* 23 */           Civilization civ = EndGameCondition.getCivFromSessionData(entry.value);
/* 24 */           Integer daysLeft = Integer.valueOf(endCond.getDaysToHold() - endCond.getDaysHeldFromSessionData(entry.value).intValue());
/* 25 */           CivMessage.global("§b" + CivColor.BOLD + civ.getName() + "§f" + " is " + 
/* 26 */             "§e" + CivColor.BOLD + daysLeft + "§f" + " days away from a " + "§d" + CivColor.BOLD + endCond.getVictoryName() + 
/* 27 */             "§f" + " victory! Capture their capital to prevent it!");
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\EndConditionNotificationTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */