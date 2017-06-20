/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
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
/*    */ public class JoinTownResponse
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public Town town;
/*    */   public Resident resident;
/*    */   public Player sender;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 37 */     if (param.equalsIgnoreCase("accept")) {
/* 38 */       CivMessage.send(this.sender, "§7" + this.resident.getName() + " accepted our town invitation.");
/*    */       try
/*    */       {
/* 41 */         this.town.addResident(this.resident);
/*    */       } catch (AlreadyRegisteredException e) {
/* 43 */         CivMessage.sendError(this.sender, this.resident.getName() + " is already a town member.");
/* 44 */         return;
/*    */       }
/*    */       
/* 47 */       CivMessage.sendTown(this.town, this.resident.getName() + " has joined the town.");
/* 48 */       this.resident.save();
/*    */     } else {
/* 50 */       CivMessage.send(this.sender, "§7" + this.resident.getName() + " denied our town invitation.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 56 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\JoinTownResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */