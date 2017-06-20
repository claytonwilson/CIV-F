/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.threading.tasks.PlayerQuestionTask;
/*    */ import com.avrgaming.civcraft.threading.tasks.TemplateSelectQuestionTask;
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
/*    */ 
/*    */ public class SelectCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 35 */     if (!(sender instanceof Player)) {
/* 36 */       CivMessage.sendError(sender, "Only a player can execute this command.");
/* 37 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 41 */     if (args.length < 1) {
/* 42 */       CivMessage.sendError(sender, "Enter a number.");
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     Player player = (Player)sender;
/*    */     
/* 48 */     PlayerQuestionTask task = (PlayerQuestionTask)CivGlobal.getQuestionTask(player.getName());
/* 49 */     if (task == null) {
/* 50 */       CivMessage.sendError(sender, "No question to respond to.");
/* 51 */       return false;
/*    */     }
/*    */     
/* 54 */     if (!(task instanceof TemplateSelectQuestionTask)) {
/* 55 */       CivMessage.sendError(sender, "Cannot respond to the current question.");
/* 56 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 60 */     synchronized (task) {
/* 61 */       task.setResponse(args[0]);
/* 62 */       task.notifyAll();
/*    */     }
/*    */     
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\SelectCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */