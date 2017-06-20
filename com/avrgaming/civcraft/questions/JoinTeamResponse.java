/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ public class JoinTeamResponse
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public ArenaTeam team;
/*    */   public Resident resident;
/*    */   public Player sender;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 19 */     if (param.equalsIgnoreCase("accept")) {
/* 20 */       CivMessage.send(this.sender, "§7" + this.resident.getName() + " accepted our team invitation.");
/*    */       try
/*    */       {
/* 23 */         ArenaTeam.addMember(this.team.getName(), this.resident);
/*    */       } catch (CivException e) {
/* 25 */         CivMessage.sendError(this.sender, e.getMessage());
/* 26 */         return;
/*    */       }
/*    */       
/* 29 */       CivMessage.sendTeam(this.team, this.resident.getName() + " has joined the team.");
/*    */     } else {
/* 31 */       CivMessage.send(this.sender, "§7" + this.resident.getName() + " denied our team invitation.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 37 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\JoinTeamResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */