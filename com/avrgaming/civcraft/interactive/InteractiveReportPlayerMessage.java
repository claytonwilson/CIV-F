/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.global.reports.ReportManager;
/*    */ import com.avrgaming.global.reports.ReportManager.ReportType;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class InteractiveReportPlayerMessage implements InteractiveResponse
/*    */ {
/*    */   ReportManager.ReportType type;
/*    */   String playerName;
/*    */   
/*    */   public InteractiveReportPlayerMessage(String playerName, ReportManager.ReportType type)
/*    */   {
/* 18 */     this.type = type;
/* 19 */     this.playerName = playerName;
/*    */   }
/*    */   
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 26 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 31 */     ReportManager.reportPlayer(this.playerName, this.type, message, resident.getName());
/* 32 */     CivMessage.sendSuccess(player, this.playerName + " was reported. Thank you.");
/* 33 */     resident.clearInteractiveMode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveReportPlayerMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */