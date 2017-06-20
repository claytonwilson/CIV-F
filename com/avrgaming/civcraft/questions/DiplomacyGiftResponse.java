/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.EconObject;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
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
/*    */ public class DiplomacyGiftResponse
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public Object giftedObject;
/*    */   public Civilization fromCiv;
/*    */   public Civilization toCiv;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 36 */     if (param.equalsIgnoreCase("accept"))
/*    */     {
/* 38 */       if ((this.giftedObject instanceof Town)) {
/* 39 */         Town town = (Town)this.giftedObject;
/*    */         
/* 41 */         if (!this.toCiv.getTreasury().hasEnough(town.getGiftCost())) {
/* 42 */           CivMessage.sendCiv(this.toCiv, "§c We cannot accept the town of " + town.getName() + " as a gift because we do not have the required " + town.getGiftCost() + " coins.");
/* 43 */           CivMessage.sendCiv(this.fromCiv, "§c" + this.toCiv.getName() + " cannot accept the town of " + town.getName() + " as a gift because they did not have the required " + 
/* 44 */             town.getGiftCost() + " coins.");
/* 45 */           return;
/*    */         }
/*    */         
/* 48 */         this.toCiv.getTreasury().withdraw(town.getGiftCost());
/* 49 */         town.changeCiv(this.toCiv);
/* 50 */         CivMessage.sendCiv(this.fromCiv, "§7" + this.toCiv.getName() + " has accepted the offer of our town of " + town.getName());
/* 51 */         return; }
/* 52 */       if ((this.giftedObject instanceof Civilization)) {
/* 53 */         int coins = this.fromCiv.getMergeCost();
/*    */         
/* 55 */         if (!this.toCiv.getTreasury().hasEnough(coins)) {
/* 56 */           CivMessage.sendCiv(this.toCiv, "§c We cannot accept the merge of " + this.fromCiv.getName() + " because we do not have the required " + coins + " coins.");
/* 57 */           CivMessage.sendCiv(this.fromCiv, "§c" + this.toCiv.getName() + " cannot accept the merge of " + this.fromCiv.getName() + " because they do not have the required " + coins + " coins.");
/* 58 */           return;
/*    */         }
/*    */         
/* 61 */         this.toCiv.getTreasury().withdraw(coins);
/* 62 */         CivMessage.sendCiv(this.fromCiv, "§e" + this.toCiv.getName() + " has accepted the offer, our civ is now merging with theirs!");
/* 63 */         this.toCiv.mergeInCiv(this.fromCiv);
/* 64 */         CivMessage.global("The Civilization of " + this.fromCiv.getName() + " has agreed to merge into the Civilizaiton of " + this.toCiv.getName());
/* 65 */         return;
/*    */       }
/* 67 */       CivLog.error("Unexpected object in gift response:" + this.giftedObject);
/* 68 */       return;
/*    */     }
/*    */     
/* 71 */     CivMessage.sendCiv(this.fromCiv, "§7" + this.toCiv.getName() + " declined our offer.");
/*    */   }
/*    */   
/*    */ 
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 77 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\DiplomacyGiftResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */