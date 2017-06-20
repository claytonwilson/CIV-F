/*    */ package com.avrgaming.civcraft.questions;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.tasks.FoundTownSync;
/*    */ 
/*    */ public class TownNewRequest
/*    */   implements QuestionResponseInterface
/*    */ {
/*    */   public Resident resident;
/*    */   public Resident leader;
/*    */   public Civilization civ;
/*    */   public String name;
/*    */   
/*    */   public void processResponse(String param)
/*    */   {
/* 19 */     if (param.equalsIgnoreCase("accept")) {
/* 20 */       CivMessage.send(this.civ, "§aOur Civilization leader " + this.leader.getName() + " has accepted the request to found the town of " + this.name);
/* 21 */       TaskMaster.syncTask(new FoundTownSync(this.resident));
/*    */     } else {
/* 23 */       CivMessage.send(this.resident, "§7Our request to found a town has been denied.");
/*    */     }
/*    */   }
/*    */   
/*    */   public void processResponse(String response, Resident responder)
/*    */   {
/* 29 */     this.leader = responder;
/* 30 */     processResponse(response);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\TownNewRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */