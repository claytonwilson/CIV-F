/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ 
/*    */ public class InteractiveWarCampFound implements InteractiveResponse
/*    */ {
/*    */   ConfigBuildableInfo info;
/*    */   
/*    */   public InteractiveWarCampFound(ConfigBuildableInfo info)
/*    */   {
/* 13 */     this.info = info;
/*    */   }
/*    */   
/*    */   public void respond(String message, Resident resident)
/*    */   {
/* 18 */     resident.clearInteractiveMode();
/*    */     
/* 20 */     if (!message.equalsIgnoreCase("yes")) {
/* 21 */       CivMessage.send(resident, "War Camp creation cancelled.");
/* 22 */       return;
/*    */     }
/*    */     
/* 25 */     com.avrgaming.civcraft.camp.WarCamp.newCamp(resident, this.info);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveWarCampFound.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */