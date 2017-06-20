/*    */ package com.avrgaming.civcraft.randomevents.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.EconObject;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class PayPlayer extends com.avrgaming.civcraft.randomevents.RandomEventComponent
/*    */ {
/*    */   public void process()
/*    */   {
/* 14 */     String playerName = (String)getParent().componentVars.get(getString("playername_var"));
/* 15 */     if (playerName == null) {
/* 16 */       CivLog.warning("No playername var for pay player.");
/* 17 */       return;
/*    */     }
/*    */     
/* 20 */     Resident resident = com.avrgaming.civcraft.main.CivGlobal.getResident(playerName);
/* 21 */     double coins = getDouble("amount");
/* 22 */     resident.getTreasury().deposit(coins);
/* 23 */     CivMessage.send(resident, "You've recieved " + coins + " coins!");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\components\PayPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */