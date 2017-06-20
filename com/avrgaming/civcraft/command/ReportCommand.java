/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.interactive.InteractiveReportPlayer;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.global.reports.ReportManager;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.ChatColor;
/*    */ 
/*    */ public class ReportCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 16 */     this.command = "/report";
/* 17 */     this.displayName = "Report";
/*    */     
/* 19 */     this.commands.put("player", "[name] - Reports this player for misconduct.");
/*    */   }
/*    */   
/*    */   public void player_cmd() throws CivException {
/* 23 */     Resident resident = getResident();
/* 24 */     Resident reportedResident = getNamedResident(1);
/*    */     
/* 26 */     CivMessage.sendHeading(this.sender, "Reporting a Player");
/* 27 */     CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "You are reporting " + reportedResident.getName() + " for misconduct.");
/* 28 */     CivMessage.send(this.sender, " ");
/* 29 */     CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "Please select one of the following categories: " + "§a" + ChatColor.BOLD + ReportManager.getReportTypes());
/* 30 */     CivMessage.send(this.sender, " ");
/* 31 */     CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "Reporting players allows our staff to determine patterns of behavior in players," + 
/* 32 */       "if a player gets too many bad reports they may be banned. Please know that filing false reports is also a bannable offense.");
/* 33 */     CivMessage.send(this.sender, "§7" + ChatColor.BOLD + "Type 'cancel' to cancel this report.");
/* 34 */     resident.setInteractiveMode(new InteractiveReportPlayer(reportedResident.getName()));
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 39 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 44 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\ReportCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */