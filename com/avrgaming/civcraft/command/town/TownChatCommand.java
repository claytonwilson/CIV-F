/*    */ package com.avrgaming.civcraft.command.town;
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
/*    */ 
/*    */ public class TownChatCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 37 */     if (!(sender instanceof Player)) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     Player player = (Player)sender;
/* 42 */     Resident resident = CivGlobal.getResident(player);
/* 43 */     if (resident == null) {
/* 44 */       CivMessage.sendError(sender, "You are not a resident? Relogin please..");
/* 45 */       return false;
/*    */     }
/*    */     
/* 48 */     if (args.length == 0) {
/* 49 */       resident.setTownChat(!resident.isTownChat());
/* 50 */       resident.setCivChat(false);
/* 51 */       CivMessage.sendSuccess(sender, "Town chat mode set to " + resident.isTownChat());
/* 52 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 56 */     String fullArgs = "";
/* 57 */     String[] arrayOfString; int j = (arrayOfString = args).length; for (int i = 0; i < j; i++) { String arg = arrayOfString[i];
/* 58 */       fullArgs = fullArgs + arg + " ";
/*    */     }
/*    */     
/* 61 */     if (resident.getTown() == null) {
/* 62 */       player.sendMessage("§cYou are not part of a town, nobody hears you.");
/* 63 */       return false;
/*    */     }
/* 65 */     CivMessage.sendTownChat(resident.getTown(), resident, "<%s> %s", fullArgs);
/* 66 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownChatCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */