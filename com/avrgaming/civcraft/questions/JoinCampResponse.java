/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.camp.Camp;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
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
/*    */ 
/*    */ 
/*    */ public class JoinCampResponse
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public Camp camp;
/*    */   public Resident resident;
/*    */   public Player sender;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 36 */     if (param.equalsIgnoreCase("accept")) {
/* 37 */       CivMessage.send(this.sender, "§7" + this.resident.getName() + " accepted our camp invitation.");
/*    */       
/* 39 */       if (!this.camp.hasMember(this.resident.getName())) {
/* 40 */         this.camp.addMember(this.resident);
/* 41 */         CivMessage.sendCamp(this.camp, this.resident.getName() + " has joined the camp.");
/* 42 */         this.resident.save();
/*    */       }
/*    */     } else {
/* 45 */       CivMessage.send(this.sender, "§7" + this.resident.getName() + " denied our camp invitation.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 51 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\JoinCampResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */