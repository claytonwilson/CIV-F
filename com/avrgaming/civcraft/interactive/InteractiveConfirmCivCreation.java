/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.tasks.FoundCivSync;
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
/*    */ 
/*    */ public class InteractiveConfirmCivCreation
/*    */   implements InteractiveResponse
/*    */ {
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 38 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 43 */     resident.clearInteractiveMode();
/*    */     
/* 45 */     if (!message.equalsIgnoreCase("yes")) {
/* 46 */       CivMessage.send(player, "Civilization creation cancelled.");
/* 47 */       return;
/*    */     }
/*    */     
/* 50 */     if ((resident.desiredCapitolName == null) || (resident.desiredCivName == null)) {
/* 51 */       CivMessage.send(player, "§cInternal Error Creating Civ... =(");
/* 52 */       return;
/*    */     }
/*    */     
/* 55 */     TaskMaster.syncTask(new FoundCivSync(resident));
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveConfirmCivCreation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */