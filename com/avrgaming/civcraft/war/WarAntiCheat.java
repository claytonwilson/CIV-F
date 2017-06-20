/*    */ package com.avrgaming.civcraft.war;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.tasks.PlayerKickBan;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class WarAntiCheat
/*    */ {
/*    */   public static void kickUnvalidatedPlayers()
/*    */   {
/* 18 */     if (CivGlobal.isCasualMode()) {
/*    */       return;
/*    */     }
/*    */     Player[] arrayOfPlayer;
/* 22 */     int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/* 23 */       if (!player.isOp())
/*    */       {
/*    */ 
/*    */ 
/* 27 */         Resident resident = CivGlobal.getResident(player);
/* 28 */         onWarTimePlayerCheck(resident);
/*    */       }
/*    */     }
/* 31 */     CivMessage.global("§7All 'at war' players not using CivCraft's Anti-Cheat have been expelled during WarTime.");
/*    */   }
/*    */   
/*    */   public static void onWarTimePlayerCheck(Resident resident) {
/* 35 */     if (!resident.hasTown()) {
/* 36 */       return;
/*    */     }
/*    */     
/* 39 */     if (!resident.getCiv().getDiplomacyManager().isAtWar()) {
/* 40 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 44 */       if (!resident.isUsesAntiCheat()) {
/* 45 */         TaskMaster.syncTask(new PlayerKickBan(resident.getName(), true, false, 
/* 46 */           "Kicked: You are required to have CivCraft's Anti-Cheat plugin installed to participate in WarTime.Visit http://civcraft.net to get it."));
/*    */       }
/*    */     }
/*    */     catch (CivException localCivException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\war\WarAntiCheat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */