/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.endgame.EndConditionDiplomacy;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class VoteCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/* 18 */     if (args.length < 1) {
/* 19 */       CivMessage.sendError(sender, "/vote [civ name] - votes for your favorite civ for a diplomatic victory!");
/* 20 */       return false;
/*    */     }
/*    */     
/* 23 */     if ((sender instanceof Player)) {
/* 24 */       Player player = (Player)sender;
/* 25 */       Resident resident = CivGlobal.getResident(player);
/*    */       
/* 27 */       if (!resident.hasTown()) {
/* 28 */         CivMessage.sendError(sender, "You must be a member of a town in order to cast a vote.");
/* 29 */         return false;
/*    */       }
/*    */       
/* 32 */       Civilization civ = CivGlobal.getCiv(args[0]);
/* 33 */       if (civ == null) {
/* 34 */         CivMessage.sendError(sender, "Couldn't find eligable civ named '" + args[0] + "'.");
/* 35 */         return false;
/*    */       }
/*    */       
/* 38 */       if (!EndConditionDiplomacy.canPeopleVote()) {
/* 39 */         CivMessage.sendError(sender, "Council of Eight not yet built. Cannot vote for civs until then.");
/* 40 */         return false;
/*    */       }
/*    */       
/* 43 */       EndConditionDiplomacy.addVote(civ, resident);
/* 44 */       return true;
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\VoteCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */