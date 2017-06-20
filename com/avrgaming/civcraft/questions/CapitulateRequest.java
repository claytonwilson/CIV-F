/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ 
/*    */ public class CapitulateRequest
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public Town capitulator;
/*    */   public String from;
/*    */   public String to;
/*    */   public String playerName;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 17 */     if (param.equalsIgnoreCase("accept")) {
/* 18 */       this.capitulator.capitulate();
/* 19 */       CivMessage.global(this.from + " has capitulated to " + this.to);
/*    */     } else {
/* 21 */       CivMessage.send(this.playerName, "§7" + this.to + " declined our offer.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 27 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\CapitulateRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */