/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*    */ import com.avrgaming.civcraft.threading.tasks.CivLeaderQuestionTask;
/*    */ import com.avrgaming.civcraft.threading.tasks.PlayerQuestionTask;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
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
/*    */ public class DenyCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 36 */     if (!(sender instanceof Player)) {
/* 37 */       CivMessage.sendError(sender, "Only a player can execute this command.");
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     Player player = (Player)sender;
/*    */     
/* 43 */     PlayerQuestionTask task = (PlayerQuestionTask)CivGlobal.getQuestionTask(player.getName());
/* 44 */     if (task != null)
/*    */     {
/* 46 */       synchronized (task) {
/* 47 */         task.setResponse("deny");
/* 48 */         task.notifyAll();
/*    */       }
/* 50 */       return true;
/*    */     }
/*    */     
/* 53 */     Resident resident = CivGlobal.getResident(player);
/* 54 */     if (resident.getCiv().getLeaderGroup().hasMember(resident)) {
/* 55 */       CivLeaderQuestionTask civTask = (CivLeaderQuestionTask)CivGlobal.getQuestionTask("civ:" + resident.getCiv().getName());
/* 56 */       if (civTask != null) {
/* 57 */         synchronized (civTask) {
/* 58 */           civTask.setResponse("deny");
/* 59 */           civTask.setResponder(resident);
/* 60 */           civTask.notifyAll();
/*    */         }
/*    */       }
/* 63 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 67 */     CivMessage.sendError(sender, "No question to respond to.");
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\DenyCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */