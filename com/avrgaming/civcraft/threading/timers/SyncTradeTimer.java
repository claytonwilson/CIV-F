/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.EconObject;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.object.TradeGood;
/*    */ import java.text.DecimalFormat;
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
/*    */ 
/*    */ 
/*    */ public class SyncTradeTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void processTownsTradePayments(Town town)
/*    */   {
/* 39 */     double payment = TradeGood.getTownTradePayment(town);
/* 40 */     DecimalFormat df = new DecimalFormat();
/*    */     
/* 42 */     if (payment > 0.0D)
/*    */     {
/* 44 */       double taxesPaid = payment * town.getDepositCiv().getIncomeTaxRate();
/* 45 */       if (taxesPaid > 0.0D) {
/* 46 */         CivMessage.sendTown(town, "§aGenerated §e" + df.format(payment) + "§a" + " coins from trade." + 
/* 47 */           "§e" + " (Paid " + df.format(taxesPaid) + " in taxes to " + town.getDepositCiv().getName() + ")");
/*    */       } else {
/* 49 */         CivMessage.sendTown(town, "§aGenerated §e" + df.format(payment) + "§a" + " coins from trade.");
/*    */       }
/*    */       
/* 52 */       town.getTreasury().deposit(payment - taxesPaid);
/* 53 */       town.getDepositCiv().taxPayment(town, taxesPaid);
/*    */     }
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 59 */     if (!CivGlobal.tradeEnabled) {
/* 60 */       return;
/*    */     }
/*    */     
/* 63 */     CivGlobal.checkForDuplicateGoodies();
/*    */     
/* 65 */     for (Town town : CivGlobal.getTowns()) {
/*    */       try {
/* 67 */         processTownsTradePayments(town);
/*    */       } catch (Exception e) {
/* 69 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\SyncTradeTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */