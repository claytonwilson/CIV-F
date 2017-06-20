/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.trade.TradeInventoryListener;
/*    */ import com.avrgaming.civcraft.trade.TradeInventoryPair;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class TradeRequest implements QuestionResponseInterface
/*    */ {
/*    */   public Resident resident;
/*    */   public Resident trader;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 16 */     if (param.equalsIgnoreCase("accept")) {
/* 17 */       TradeInventoryPair pair = new TradeInventoryPair();
/* 18 */       pair.inv = this.trader.startTradeWith(this.resident);
/* 19 */       if (pair.inv == null) {
/* 20 */         return;
/*    */       }
/*    */       
/* 23 */       pair.otherInv = this.resident.startTradeWith(this.trader);
/* 24 */       if (pair.otherInv == null) {
/* 25 */         return;
/*    */       }
/*    */       
/* 28 */       pair.resident = this.trader;
/* 29 */       pair.otherResident = this.resident;
/* 30 */       TradeInventoryListener.tradeInventories.put(TradeInventoryListener.getTradeInventoryKey(this.trader), pair);
/*    */       
/* 32 */       TradeInventoryPair otherPair = new TradeInventoryPair();
/* 33 */       otherPair.inv = pair.otherInv;
/* 34 */       otherPair.otherInv = pair.inv;
/* 35 */       otherPair.resident = pair.otherResident;
/* 36 */       otherPair.otherResident = pair.resident;
/* 37 */       TradeInventoryListener.tradeInventories.put(TradeInventoryListener.getTradeInventoryKey(this.resident), otherPair);
/*    */     } else {
/* 39 */       CivMessage.send(this.trader, "§7" + this.resident.getName() + " denied our trade invitation.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 45 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\TradeRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */