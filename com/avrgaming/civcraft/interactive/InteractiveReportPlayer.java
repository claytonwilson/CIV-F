/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.global.reports.ReportManager;
/*    */ import com.avrgaming.global.reports.ReportManager.ReportType;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class InteractiveReportPlayer
/*    */   implements InteractiveResponse
/*    */ {
/*    */   String playerName;
/*    */   
/*    */   public InteractiveReportPlayer(String playerName)
/*    */   {
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
/* 31 */     if (message.equalsIgnoreCase("cancel")) {
/* 32 */       CivMessage.send(player, "§a" + ChatColor.BOLD + "Report cancelled.");
/* 33 */       resident.clearInteractiveMode();
/* 34 */       return;
/*    */     }
/*    */     
/* 37 */     ReportManager.ReportType selectedType = null;
/* 38 */     ReportManager.ReportType[] arrayOfReportType; int j = (arrayOfReportType = ReportManager.ReportType.values()).length; for (int i = 0; i < j; i++) { ReportManager.ReportType type = arrayOfReportType[i];
/* 39 */       if (message.equalsIgnoreCase(type.name())) {
/* 40 */         selectedType = type;
/* 41 */         break;
/*    */       }
/*    */     }
/*    */     
/* 45 */     if (selectedType == null) {
/* 46 */       CivMessage.sendError(player, "You must select a valid category to report. (" + ReportManager.getReportTypes() + ")");
/* 47 */       return;
/*    */     }
/*    */     
/* 50 */     CivMessage.send(player, "§e" + ChatColor.BOLD + "Please enter a description of what happened:");
/* 51 */     resident.setInteractiveMode(new InteractiveReportPlayerMessage(this.playerName, selectedType));
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveReportPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */