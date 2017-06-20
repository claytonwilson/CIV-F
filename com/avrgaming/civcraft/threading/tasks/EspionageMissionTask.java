/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.cache.PlayerLocationCache;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMission;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.items.units.Unit;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.ScoutTower;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import java.util.List;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EspionageMissionTask
/*     */   implements Runnable
/*     */ {
/*     */   ConfigMission mission;
/*     */   String playerName;
/*     */   Town target;
/*     */   int secondsLeft;
/*     */   Location startLocation;
/*     */   
/*     */   public EspionageMissionTask(ConfigMission mission, String playerName, Location startLocation, Town target, int seconds)
/*     */   {
/*  52 */     this.mission = mission;
/*  53 */     this.playerName = playerName;
/*  54 */     this.target = target;
/*  55 */     this.startLocation = startLocation;
/*  56 */     this.secondsLeft = seconds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  65 */       int exposePerSecond = CivSettings.getInteger(CivSettings.espionageConfig, "espionage.exposure_per_second").intValue();
/*  66 */       int exposePerPlayer = CivSettings.getInteger(CivSettings.espionageConfig, "espionage.exposure_per_player").intValue();
/*  67 */       exposePerScout = CivSettings.getInteger(CivSettings.espionageConfig, "espionage.exposure_per_scout").intValue();
/*     */     } catch (InvalidConfiguration e) { int exposePerScout;
/*  69 */       e.printStackTrace(); return;
/*     */     }
/*     */     int exposePerScout;
/*     */     int exposePerPlayer;
/*     */     int exposePerSecond;
/*     */     try {
/*  75 */       player = CivGlobal.getPlayer(this.playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return; }
/*     */     Player player;
/*  79 */     Resident resident = CivGlobal.getResident(player);
/*  80 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Mission Started.");
/*     */     
/*  82 */     while (this.secondsLeft > 0)
/*     */     {
/*  84 */       if (this.secondsLeft > 0) {
/*  85 */         this.secondsLeft -= 1;
/*     */         
/*     */ 
/*  88 */         resident.setPerformingMission(true);
/*  89 */         resident.setSpyExposure(resident.getSpyExposure() + exposePerSecond);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  94 */         int playerCount = PlayerLocationCache.getNearbyPlayers(new BlockCoord(player.getLocation()), 600.0D).size();
/*  95 */         playerCount--;
/*  96 */         resident.setSpyExposure(resident.getSpyExposure() + playerCount * exposePerPlayer);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */         int amount = 0;
/*     */         try
/*     */         {
/* 105 */           range = CivSettings.getDouble(CivSettings.warConfig, "scout_tower.range");
/*     */         } catch (InvalidConfiguration e) { double range;
/* 107 */           e.printStackTrace();
/* 108 */           resident.setPerformingMission(false); return;
/*     */         }
/*     */         
/*     */         double range;
/* 112 */         BlockCoord bcoord = new BlockCoord(player.getLocation());
/*     */         
/* 114 */         for (Structure struct : this.target.getStructures()) {
/* 115 */           if (struct.isActive())
/*     */           {
/*     */ 
/*     */ 
/* 119 */             if (((struct instanceof ScoutTower)) && 
/* 120 */               (bcoord.distance(struct.getCenterLocation()) < range)) {
/* 121 */               amount += exposePerScout;
/*     */             }
/*     */           }
/*     */         }
/* 125 */         resident.setSpyExposure(resident.getSpyExposure() + amount);
/*     */         
/*     */ 
/* 128 */         if (this.target.processSpyExposure(resident)) {
/* 129 */           CivMessage.global(
/*     */           
/* 131 */             "§eINTERNATIONAL INCIDENT!§f " + player.getName() + " was caught trying to perform a " + this.mission.name + " spy mission in " + this.target.getName() + "!");
/* 132 */           CivMessage.send(player, "§cYou've been compromised! (Exposure got too high) Spy unit was destroyed!");
/* 133 */           Unit.removeUnit(player);
/* 134 */           resident.setPerformingMission(false);
/* 135 */           return;
/*     */         }
/*     */         
/* 138 */         if (this.secondsLeft % 15 == 0) {
/* 139 */           CivMessage.send(player, "§e" + CivColor.BOLD + this.secondsLeft + " seconds remain");
/* 140 */         } else if (this.secondsLeft < 15) {
/* 141 */           CivMessage.send(player, "§e" + CivColor.BOLD + this.secondsLeft + " seconds remain");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 146 */       ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 147 */       CultureChunk cc = CivGlobal.getCultureChunk(coord);
/*     */       
/* 149 */       if ((cc == null) || (cc.getCiv() != this.target.getCiv())) {
/* 150 */         CivMessage.sendError(player, "You've left the civ borders. Mission Failed.");
/* 151 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 155 */         Thread.sleep(1000L);
/*     */       } catch (InterruptedException e) {
/* 157 */         return;
/*     */       }
/*     */     }
/*     */     
/* 161 */     resident.setPerformingMission(false);
/* 162 */     TaskMaster.syncTask(new PerformMissionTask(this.mission, this.playerName));
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\EspionageMissionTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */