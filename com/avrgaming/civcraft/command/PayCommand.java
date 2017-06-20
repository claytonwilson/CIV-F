/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.EconObject;
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
/*    */ public class PayCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 37 */       Player player = CivGlobal.getPlayer(sender.getName());
/* 38 */       Resident resident = CivGlobal.getResident(player);
/* 39 */       if (resident == null) {
/* 40 */         CivMessage.sendError(sender, "Couldn't find yourself... ???");
/* 41 */         return false;
/*    */       }
/*    */       
/* 44 */       if (args.length < 2) {
/* 45 */         throw new CivException("Enter a player and an amount to pay /pay [player] [amount]");
/*    */       }
/*    */       
/* 48 */       Resident payTo = CivGlobal.getResident(args[0]);
/* 49 */       if (payTo == null) {
/* 50 */         throw new CivException("Couldn't find player " + args[0] + " to pay.");
/*    */       }
/*    */       
/* 53 */       if (resident == payTo) {
/* 54 */         throw new CivException("Don't pay yourself.");
/*    */       }
/*    */       
/*    */       try
/*    */       {
/* 59 */         Double amount = Double.valueOf(args[1]);
/* 60 */         if (!resident.getTreasury().hasEnough(amount.doubleValue())) {
/* 61 */           throw new CivException("You do not have enough coins.");
/*    */         }
/*    */       } catch (NumberFormatException e) {
/* 64 */         throw new CivException("Please enter a number.");
/*    */       }
/*    */       
/* 67 */       if (amount.doubleValue() < 1.0D) {
/* 68 */         throw new CivException("Cannot pay someone less than one coin.");
/*    */       }
/* 70 */       Double amount = Double.valueOf(Math.floor(amount.doubleValue()));
/*    */       
/* 72 */       resident.getTreasury().withdraw(amount.doubleValue());
/* 73 */       payTo.getTreasury().deposit(amount.doubleValue());
/*    */       
/* 75 */       CivMessage.sendSuccess(player, "Paid " + payTo.getName() + " " + amount + " coins");
/*    */       try
/*    */       {
/* 78 */         Player payToPlayer = CivGlobal.getPlayer(payTo);
/* 79 */         CivMessage.sendSuccess(payToPlayer, "Got " + amount + " coins from " + resident.getName());
/*    */       }
/*    */       catch (CivException localCivException1) {}
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 87 */       return true;
/*    */     }
/*    */     catch (CivException e)
/*    */     {
/* 84 */       CivMessage.sendError(sender, e.getMessage());
/* 85 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\PayCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */