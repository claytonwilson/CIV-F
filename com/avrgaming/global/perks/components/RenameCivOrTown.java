/*    */ package com.avrgaming.global.perks.components;
/*    */ 
/*    */ import com.avrgaming.civcraft.interactive.InteractiveRenameCivOrTown;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ 
/*    */ 
/*    */ public class RenameCivOrTown
/*    */   extends PerkComponent
/*    */ {
/*    */   public void onActivate(Resident resident)
/*    */   {
/* 13 */     if (!resident.hasTown()) {
/* 14 */       CivMessage.sendError(resident, "You must be part of a civilization or town in order to rename it.");
/* 15 */       return;
/*    */     }
/*    */     
/* 18 */     resident.setInteractiveMode(new InteractiveRenameCivOrTown(resident, this));
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\global\perks\components\RenameCivOrTown.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */