/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.config.ConfigGovernment;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*    */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Map;
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
/*    */ public class ChangeGovernmentTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 35 */     for (Civilization civ : ) {
/* 36 */       if (civ.getGovernment().id.equalsIgnoreCase("gov_anarchy")) {
/* 37 */         String key = "changegov_" + civ.getId();
/*    */         
/*    */ 
/* 40 */         ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/* 41 */         if ((entries == null) || (entries.size() < 1))
/*    */         {
/* 43 */           civ.setGovernment("gov_tribalism");
/* 44 */           return;
/*    */         }
/*    */         
/*    */ 
/* 48 */         SessionEntry se = (SessionEntry)entries.get(0);
/*    */         
/* 50 */         int duration = 3600;
/* 51 */         if (CivGlobal.testFileFlag("debug")) {
/* 52 */           duration = 1;
/*    */         }
/*    */         
/* 55 */         if (CivGlobal.hasTimeElapsed(se, CivSettings.getIntegerGovernment("anarchy_duration").intValue() * duration))
/*    */         {
/* 57 */           civ.setGovernment(se.value);
/* 58 */           CivMessage.global(civ.getName() + " has emerged from anarchy and has adopted " + ((ConfigGovernment)CivSettings.governments.get(se.value)).displayName);
/*    */           
/* 60 */           CivGlobal.getSessionDB().delete_all(key);
/* 61 */           civ.save();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\ChangeGovernmentTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */