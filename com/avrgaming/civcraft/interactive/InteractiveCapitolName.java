/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.town.TownCommand;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ import org.bukkit.ChatColor;
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
/*    */ public class InteractiveCapitolName
/*    */   implements InteractiveResponse
/*    */ {
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 40 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 45 */     if (message.equalsIgnoreCase("cancel")) {
/* 46 */       CivMessage.send(player, "Civilization creation cancelled.");
/* 47 */       resident.clearInteractiveMode();
/* 48 */       return;
/*    */     }
/*    */     
/* 51 */     if (!StringUtils.isAlpha(message)) {
/* 52 */       CivMessage.send(player, "§c" + ChatColor.BOLD + "Town names must only contain letters(A-Z). Enter another name.");
/* 53 */       return;
/*    */     }
/*    */     
/* 56 */     message = message.replace(" ", "_");
/* 57 */     message = message.replace("\"", "");
/* 58 */     message = message.replace("'", "");
/*    */     
/* 60 */     resident.desiredCapitolName = message;
/* 61 */     CivMessage.send(player, "§aThe Civilization of §e" + resident.desiredCivName + 
/* 62 */       "§a" + "! And its capitol will be " + "§e" + resident.desiredCapitolName + "§a" + "!");
/* 63 */     CivMessage.sendHeading(player, "Survey Results");
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
/* 95 */     TaskMaster.syncTask(new Runnable()
/*    */     {
/*    */       String playerName;
/*    */       
/*    */       public void run()
/*    */       {
/*    */         try
/*    */         {
/* 77 */           player = CivGlobal.getPlayer(this.playerName);
/*    */         } catch (CivException e) { Player player;
/*    */           return;
/*    */         }
/*    */         Player player;
/* 82 */         Resident resident = CivGlobal.getResident(this.playerName);
/* 83 */         if (resident == null) {
/* 84 */           return;
/*    */         }
/*    */         
/* 87 */         CivMessage.send(player, TownCommand.survey(player.getLocation()));
/* 88 */         CivMessage.send(player, "");
/* 89 */         CivMessage.send(player, "§a" + ChatColor.BOLD + "Are you sure? Type 'yes' and I will create this Civilization. Type anything else, and I will forget the whole thing.");
/* 90 */         resident.setInteractiveMode(new InteractiveConfirmCivCreation());
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveCapitolName.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */