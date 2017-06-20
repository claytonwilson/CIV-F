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
/*    */ 
/*    */ public class AcceptCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 37 */     if (!(sender instanceof Player)) {
/* 38 */       CivMessage.sendError(sender, "Only a player can execute this command.");
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     Player player = (Player)sender;
/*    */     
/* 44 */     PlayerQuestionTask task = (PlayerQuestionTask)CivGlobal.getQuestionTask(player.getName());
/* 45 */     if (task != null)
/*    */     {
/* 47 */       synchronized (task) {
/* 48 */         task.setResponse("accept");
/* 49 */         task.notifyAll();
/*    */       }
/* 51 */       return true;
/*    */     }
/*    */     
/* 54 */     Resident resident = CivGlobal.getResident(player);
/* 55 */     if ((resident.hasTown()) && 
/* 56 */       (resident.getCiv().getLeaderGroup().hasMember(resident))) {
/* 57 */       CivLeaderQuestionTask civTask = (CivLeaderQuestionTask)CivGlobal.getQuestionTask("civ:" + resident.getCiv().getName());
/*    */       
/* 59 */       synchronized (civTask) {
/* 60 */         civTask.setResponse("accept");
/* 61 */         civTask.setResponder(resident);
/* 62 */         civTask.notifyAll();
/*    */       }
/* 64 */       return true;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 69 */     CivMessage.sendError(sender, "No question to respond to.");
/* 70 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\AcceptCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */