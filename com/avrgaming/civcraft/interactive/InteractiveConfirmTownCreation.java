/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.questions.TownNewRequest;
/*    */ import org.bukkit.Location;
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
/*    */ public class InteractiveConfirmTownCreation
/*    */   implements InteractiveResponse
/*    */ {
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 37 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 42 */     resident.clearInteractiveMode();
/*    */     
/* 44 */     if (!message.equalsIgnoreCase("yes")) {
/* 45 */       CivMessage.send(player, "Town creation cancelled.");
/* 46 */       return;
/*    */     }
/*    */     
/* 49 */     if (resident.desiredTownName == null) {
/* 50 */       CivMessage.send(player, "§cInternal Error Creating Town... =(");
/* 51 */       return;
/*    */     }
/*    */     
/* 54 */     TownNewRequest join = new TownNewRequest();
/* 55 */     join.resident = resident;
/* 56 */     join.civ = resident.getCiv();
/*    */     try {
/* 58 */       CivGlobal.questionLeaders(player, resident.getCiv(), player.getName() + " would like to found the town of " + 
/* 59 */         resident.desiredTownName + " at " + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ(), 
/* 60 */         30000L, join);
/*    */     } catch (CivException e) {
/* 62 */       CivMessage.sendError(player, e.getMessage());
/* 63 */       return;
/*    */     }
/*    */     
/* 66 */     CivMessage.send(player, "§eSent request to civilization leaders for this town. Awaiting their reply.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveConfirmTownCreation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */