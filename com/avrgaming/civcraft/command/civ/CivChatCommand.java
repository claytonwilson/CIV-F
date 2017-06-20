/*    */ package com.avrgaming.civcraft.command.civ;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
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
/*    */ public class CivChatCommand
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
/* 49 */       resident.setCivChat(!resident.isCivChat());
/* 50 */       resident.setTownChat(false);
/* 51 */       CivMessage.sendSuccess(sender, "Civ chat mode set to " + resident.isCivChat());
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
/* 62 */       player.sendMessage("§cYou are not part of a civ, nobody hears you.");
/* 63 */       return false;
/*    */     }
/*    */     
/* 66 */     CivMessage.sendCivChat(resident.getTown().getCiv(), resident, "<%s> %s", fullArgs);
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivChatCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */