/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
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
/*    */ public class SurrenderRequest
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public Civilization fromCiv;
/*    */   public Civilization toCiv;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 33 */     if (param.equalsIgnoreCase("accept")) {
/* 34 */       this.fromCiv.onDefeat(this.toCiv);
/* 35 */       CivMessage.global(this.fromCiv.getName() + " has surrendered to " + this.toCiv.getName());
/*    */     } else {
/* 37 */       CivMessage.sendCiv(this.fromCiv, "§7" + this.toCiv.getName() + " declined our offer.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 43 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\SurrenderRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */