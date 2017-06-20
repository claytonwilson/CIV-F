/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Relation.Status;
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
/*    */ public class ChangeRelationResponse
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public Civilization fromCiv;
/*    */   public Civilization toCiv;
/*    */   public Relation.Status status;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 36 */     if (param.equalsIgnoreCase("accept")) {
/* 37 */       CivGlobal.setRelation(this.fromCiv, this.toCiv, this.status);
/*    */     } else {
/* 39 */       CivMessage.sendCiv(this.fromCiv, "§7" + this.toCiv.getName() + " declined our offer.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder) {
/* 44 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\ChangeRelationResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */