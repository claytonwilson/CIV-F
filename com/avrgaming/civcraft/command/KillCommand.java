/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ public class KillCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 16 */     if (!(sender instanceof Player)) {
/* 17 */       CivMessage.sendError(sender, "Only a player can execute this command.");
/* 18 */       return false;
/*    */     }
/*    */     
/* 21 */     Player player = (Player)sender;
/* 22 */     player.setHealth(0.0D);
/*    */     
/* 24 */     CivMessage.send(sender, "§e" + CivColor.BOLD + "You couldn't take it anymore.");
/*    */     
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\KillCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */