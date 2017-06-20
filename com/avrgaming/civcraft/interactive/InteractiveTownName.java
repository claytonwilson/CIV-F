/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.town.TownCommand;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.bukkit.ChatColor;
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
/*    */ public class InteractiveTownName
/*    */   implements InteractiveResponse
/*    */ {
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 41 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 46 */     if (message.equalsIgnoreCase("cancel")) {
/* 47 */       CivMessage.send(player, "Town creation cancelled.");
/* 48 */       resident.clearInteractiveMode();
/* 49 */       return;
/*    */     }
/*    */     
/* 52 */     if (!StringUtils.isAlpha(message)) {
/* 53 */       CivMessage.send(player, "§c" + ChatColor.BOLD + "Town names must only contain letters(A-Z). Enter another name.");
/* 54 */       return;
/*    */     }
/*    */     
/* 57 */     message = message.replace(" ", "_");
/* 58 */     message = message.replace("\"", "");
/* 59 */     message = message.replace("'", "");
/*    */     
/* 61 */     resident.desiredTownName = message;
/* 62 */     CivMessage.send(player, "§aThe Town shall be called §e" + resident.desiredTownName + "§a" + "!");
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
/* 97 */     TaskMaster.syncTask(new Runnable()
/*    */     {
/*    */       Resident resident;
/*    */       
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 76 */           player = CivGlobal.getPlayer(this.resident);
/*    */         } catch (CivException e) { Player player;
/*    */           return;
/*    */         }
/*    */         Player player;
/* 81 */         CivMessage.sendHeading(player, "Survey Results");
/* 82 */         CivMessage.send(player, TownCommand.survey(player.getLocation()));
/*    */         
/* 84 */         Location capLoc = this.resident.getCiv().getCapitolTownHallLocation();
/* 85 */         if (capLoc == null) {
/* 86 */           CivMessage.sendError(player, "Could not find the capitol town hall location. Make sure it's built before you build more towns...");
/* 87 */           this.resident.clearInteractiveMode();
/* 88 */           return;
/*    */         }
/*    */         
/* 91 */         CivMessage.send(player, "§a" + ChatColor.BOLD + "Are you sure? Type 'yes' and I will create this Town. Type anything else, and I will forget the whole thing.");
/*    */         
/* 93 */         this.resident.setInteractiveMode(new InteractiveConfirmTownCreation());
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveTownName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */