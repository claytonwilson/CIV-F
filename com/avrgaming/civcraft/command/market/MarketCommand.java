/*    */ package com.avrgaming.civcraft.command.market;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import java.util.HashMap;
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
/*    */ public class MarketCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 28 */     this.command = "/market";
/* 29 */     this.displayName = "Market";
/*    */     
/* 31 */     this.commands.put("buy", "Buy things from the market, see whats for sale.");
/*    */   }
/*    */   
/*    */   public void buy_cmd()
/*    */   {
/* 36 */     MarketBuyCommand cmd = new MarketBuyCommand();
/* 37 */     cmd.onCommand(this.sender, null, "buy", stripArgs(this.args, 1));
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 42 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 47 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\market\MarketCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */