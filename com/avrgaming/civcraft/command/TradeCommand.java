/*    */ package com.avrgaming.civcraft.command;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.questions.TradeRequest;
/*    */ import com.avrgaming.civcraft.trade.TradeInventoryListener;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class TradeCommand extends CommandBase
/*    */ {
/* 17 */   public static int TRADE_TIMEOUT = 30000;
/*    */   
/*    */   public void init()
/*    */   {
/* 21 */     this.command = "/trade";
/* 22 */     this.displayName = "Trade";
/* 23 */     this.sendUnknownToDefault = true;
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 28 */     Resident resident = getNamedResident(0);
/* 29 */     Resident trader = getResident();
/*    */     
/* 31 */     if ((resident.isInsideArena()) || (trader.isInsideArena())) {
/* 32 */       throw new CivException("You cannot trade items when a player is inside a PvP Arena.");
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 37 */       max_trade_distance = CivSettings.getDouble(CivSettings.civConfig, "global.max_trade_distance");
/*    */     } catch (InvalidConfiguration e) { double max_trade_distance;
/* 39 */       e.printStackTrace(); return;
/*    */     }
/*    */     
/*    */     double max_trade_distance;
/* 43 */     Player traderPlayer = CivGlobal.getPlayer(trader);
/* 44 */     Player residentPlayer = CivGlobal.getPlayer(resident);
/*    */     
/* 46 */     if (trader == resident) {
/* 47 */       throw new CivException("You cannot trade with yourself.");
/*    */     }
/*    */     
/* 50 */     if (traderPlayer.getLocation().distance(residentPlayer.getLocation()) > max_trade_distance) {
/* 51 */       throw new CivException(resident.getName() + " is too far away to trade with.");
/*    */     }
/*    */     
/* 54 */     if (TradeInventoryListener.tradeInventories.containsKey(TradeInventoryListener.getTradeInventoryKey(resident))) {
/* 55 */       throw new CivException(resident.getName() + " is already trading with someone. Please wait.");
/*    */     }
/*    */     
/* 58 */     TradeRequest tradeRequest = new TradeRequest();
/* 59 */     tradeRequest.resident = resident;
/* 60 */     tradeRequest.trader = trader;
/*    */     
/* 62 */     CivGlobal.questionPlayer(traderPlayer, residentPlayer, 
/* 63 */       "Would you like to trade with " + traderPlayer.getName() + "?", 
/* 64 */       TRADE_TIMEOUT, tradeRequest);
/* 65 */     CivMessage.sendSuccess(this.sender, "Trade Invitation Sent");
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 70 */     CivMessage.send(this.sender, "§d" + this.command + " " + "§e" + "[resident name] " + 
/* 71 */       "§7" + "Opens trading window with this player.");
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\TradeCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */