/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.structure.Barracks;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class InteractiveRepairItem implements InteractiveResponse
/*    */ {
/*    */   double cost;
/*    */   String playerName;
/*    */   LoreCraftableMaterial craftMat;
/*    */   
/*    */   public InteractiveRepairItem(double cost, String playerName, LoreCraftableMaterial craftMat)
/*    */   {
/* 20 */     this.cost = cost;
/* 21 */     this.playerName = playerName;
/* 22 */     this.craftMat = craftMat;
/*    */   }
/*    */   
/*    */   public void displayMessage()
/*    */   {
/*    */     try {
/* 28 */       player = CivGlobal.getPlayer(this.playerName);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 33 */     CivMessage.sendHeading(player, "Repair!");
/* 34 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Hello there! Would you like to repair your " + this.craftMat.getName() + "?");
/* 35 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Looks like we can get you fixed up for " + "§e" + CivColor.BOLD + this.cost + " coins.");
/* 36 */     CivMessage.send(player, "§a" + CivColor.BOLD + "If that's ok, please type 'yes'. Type anything else to cancel.");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void respond(String message, Resident resident)
/*    */   {
/* 43 */     resident.clearInteractiveMode();
/*    */     
/* 45 */     if (!message.equalsIgnoreCase("yes")) {
/* 46 */       CivMessage.send(resident, "§7Repair cancelled.");
/* 47 */       return;
/*    */     }
/*    */     
/* 50 */     Barracks.repairItemInHand(this.cost, resident.getName(), this.craftMat);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveRepairItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */