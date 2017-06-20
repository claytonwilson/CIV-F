/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import com.avrgaming.global.perks.Perk;
/*    */ import com.avrgaming.global.perks.components.CustomTemplate;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class InteractiveCustomTemplateConfirm implements InteractiveResponse
/*    */ {
/*    */   String playerName;
/*    */   CustomTemplate customTemplate;
/*    */   
/*    */   public InteractiveCustomTemplateConfirm(String playerName, CustomTemplate customTemplate)
/*    */   {
/* 21 */     this.playerName = playerName;
/* 22 */     this.customTemplate = customTemplate;
/* 23 */     displayQuestion();
/*    */   }
/*    */   
/*    */   public void displayQuestion()
/*    */   {
/*    */     try {
/* 29 */       player = CivGlobal.getPlayer(this.playerName);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 34 */     Resident resident = CivGlobal.getResident(player);
/* 35 */     Town town = resident.getTown();
/* 36 */     Perk perk = this.customTemplate.getParent();
/*    */     
/* 38 */     CivMessage.sendHeading(player, "Confirm Template Binding");
/* 39 */     CivMessage.send(player, "§a" + CivColor.BOLD + "You are about to bind the " + perk.getDisplayName() + " template to your town of " + town.getName());
/* 40 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Once the template is bound, you will be able to build the custom template by");
/* 41 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Using the normal build command. This action consumes the perk, and cannot be undone.");
/* 42 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Are you sure you want to bind the template? Type " + "§e" + CivColor.BOLD + "yes");
/* 43 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "Type anything else to abort.");
/*    */   }
/*    */   
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 50 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return; }
/*    */     Player player;
/* 54 */     resident.clearInteractiveMode();
/*    */     
/* 56 */     if (!message.equalsIgnoreCase("yes")) {
/* 57 */       CivMessage.sendError(player, "Template Bind Cancelled.");
/* 58 */       return;
/*    */     }
/*    */     
/* 61 */     this.customTemplate.bindTemplateToTown(resident.getTown(), resident);
/* 62 */     this.customTemplate.markAsUsed(resident);
/* 63 */     CivMessage.sendSuccess(player, "Bound " + this.customTemplate.getParent().getDisplayName() + " to " + resident.getTown().getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveCustomTemplateConfirm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */