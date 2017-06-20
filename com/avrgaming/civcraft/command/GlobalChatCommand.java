/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
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
/*    */ 
/*    */ 
/*    */ public class GlobalChatCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 36 */     if (!(sender instanceof Player)) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     Player player = (Player)sender;
/* 41 */     Resident resident = CivGlobal.getResident(player);
/* 42 */     if (resident == null) {
/* 43 */       CivMessage.sendError(sender, "You are not a resident? Relogin please..");
/* 44 */       return false;
/*    */     }
/*    */     
/* 47 */     if (args.length == 0) {
/* 48 */       resident.setCivChat(false);
/* 49 */       resident.setTownChat(false);
/* 50 */       CivMessage.sendSuccess(sender, "Enabled global chat mode.");
/* 51 */       return true;
/*    */     }
/*    */     
/* 54 */     CivMessage.sendError(sender, "Global chat command /gc <message> disabled, using HeroChat /ch global instead. use /gc to exit civ or town chat.");
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\GlobalChatCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */