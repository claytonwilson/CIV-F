/*    */ package com.avrgaming.civcraft.interactive;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.ConfigMission;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.items.units.MissionBook;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.threading.tasks.EspionageMissionTask;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import java.text.DecimalFormat;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class InteractiveSpyMission
/*    */   implements InteractiveResponse
/*    */ {
/*    */   public ConfigMission mission;
/*    */   public String playerName;
/*    */   public Location playerLocation;
/*    */   public Town target;
/*    */   
/*    */   public InteractiveSpyMission(ConfigMission mission, String playerName, Location playerLocation, Town target)
/*    */   {
/* 28 */     this.mission = mission;
/* 29 */     this.playerName = playerName;
/* 30 */     this.playerLocation = playerLocation;
/* 31 */     this.target = target;
/* 32 */     displayQuestion();
/*    */   }
/*    */   
/*    */   public void displayQuestion()
/*    */   {
/*    */     try {
/* 38 */       player = CivGlobal.getPlayer(this.playerName);
/*    */     } catch (CivException e) { Player player;
/*    */       return;
/*    */     }
/*    */     Player player;
/* 43 */     CivMessage.sendHeading(player, "Mission: " + this.mission.name);
/*    */     
/* 45 */     double failChance = MissionBook.getMissionFailChance(this.mission, this.target);
/* 46 */     double compChance = MissionBook.getMissionCompromiseChance(this.mission, this.target);
/* 47 */     DecimalFormat df = new DecimalFormat();
/*    */     
/* 49 */     String successChance = df.format((1.0D - failChance) * 100.0D) + "%";
/* 50 */     String compromiseChance = df.format(compChance) + "%";
/* 51 */     String length = "";
/*    */     
/* 53 */     int mins = this.mission.length.intValue() / 60;
/* 54 */     int seconds = this.mission.length.intValue() % 60;
/* 55 */     if (mins > 0) {
/* 56 */       length = length + mins + " mins";
/* 57 */       if (seconds > 0) {
/* 58 */         length = length + " and ";
/*    */       }
/*    */     }
/*    */     
/* 62 */     if (seconds > 0) {
/* 63 */       length = length + seconds + " seconds";
/*    */     }
/*    */     
/* 66 */     CivMessage.send(player, "§2" + CivColor.BOLD + "We have a " + "§a" + successChance + "§2" + CivColor.BOLD + " chance of success.");
/* 67 */     CivMessage.send(player, "§2" + CivColor.BOLD + "If we fail, the chance of being compromised is " + "§a" + compromiseChance);
/* 68 */     CivMessage.send(player, "§2" + CivColor.BOLD + "It will cost our town " + "§e" + this.mission.cost + "§2" + CivColor.BOLD + " coins to perform this mission.");
/* 69 */     CivMessage.send(player, "§2" + CivColor.BOLD + "The mission will take " + "§e" + length + "§2" + CivColor.BOLD + " to complete.");
/* 70 */     CivMessage.send(player, "§2" + CivColor.BOLD + "You must remain within the civ's borders during the mission, otherwise you'll fail the mission.");
/* 71 */     CivMessage.send(player, "§2" + CivColor.BOLD + "If these conditions are acceptible, type " + "§e" + "yes");
/* 72 */     CivMessage.send(player, "§2" + ChatColor.BOLD + "Type anything else to abort.");
/*    */   }
/*    */   
/*    */ 
/*    */   public void respond(String message, Resident resident)
/*    */   {
/*    */     try
/*    */     {
/* 80 */       player = CivGlobal.getPlayer(resident);
/*    */     } catch (CivException e) { Player player;
/*    */       return; }
/*    */     Player player;
/* 84 */     resident.clearInteractiveMode();
/*    */     
/* 86 */     if (!message.equalsIgnoreCase("yes")) {
/* 87 */       CivMessage.sendError(player, "Mission Aborted.");
/* 88 */       return;
/*    */     }
/*    */     
/* 91 */     if (!TaskMaster.hasTask("missiondelay:" + this.playerName)) {
/* 92 */       TaskMaster.asyncTask("missiondelay:" + this.playerName, new EspionageMissionTask(this.mission, this.playerName, this.playerLocation, this.target, this.mission.length.intValue()), 0L);
/*    */     } else {
/* 94 */       CivMessage.sendError(player, "Waiting on countdown to start mission.");
/* 95 */       return;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveSpyMission.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */