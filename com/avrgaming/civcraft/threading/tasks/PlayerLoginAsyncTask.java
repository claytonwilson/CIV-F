/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.anticheat.ACManager;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigPlatinumReward;
/*     */ import com.avrgaming.civcraft.endgame.EndConditionDiplomacy;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Relation.Status;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.tutorial.CivTutorial;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import com.avrgaming.global.perks.PlatinumManager;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerLoginAsyncTask
/*     */   implements Runnable
/*     */ {
/*     */   volatile Player player;
/*     */   
/*     */   public PlayerLoginAsyncTask(Player player)
/*     */   {
/*  51 */     this.player = player;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  56 */     if (this.player == null) {
/*  57 */       return;
/*     */     }
/*     */     
/*  60 */     CivLog.info("Running PlayerLoginAsyncTask for " + this.player.getName());
/*  61 */     Resident resident = CivGlobal.getResident(this.player);
/*     */     
/*  63 */     if (resident == null) {
/*  64 */       CivLog.info("No resident found. Creating for " + this.player.getName());
/*     */       try {
/*  66 */         resident = new Resident(this.player.getName());
/*     */       } catch (InvalidNameException e) {
/*  68 */         TaskMaster.syncTask(new PlayerKickBan(this.player.getName(), true, false, "You have an invalid name. Sorry."));
/*  69 */         return;
/*     */       }
/*     */       
/*  72 */       CivGlobal.addResident(resident);
/*  73 */       CivLog.info("Added resident:" + resident.getName());
/*  74 */       resident.setRegistered(System.currentTimeMillis());
/*  75 */       CivTutorial.showTutorialInventory(this.player);
/*  76 */       resident.setisProtected(true);
/*     */       try
/*     */       {
/*  79 */         mins = CivSettings.getInteger(CivSettings.civConfig, "global.pvp_timer").intValue();
/*     */       } catch (InvalidConfiguration e1) { int mins;
/*  81 */         e1.printStackTrace(); return;
/*     */       }
/*     */       int mins;
/*  84 */       CivMessage.send(resident, "§7You have a PvP timer enabled for " + mins + " mins. You cannot attack or be attacked until it expires.");
/*  85 */       CivMessage.send(resident, "§7To remove it, type /resident pvptimer");
/*     */     }
/*     */     
/*     */ 
/*  89 */     if (!resident.isGivenKit()) {
/*  90 */       TaskMaster.syncTask(new GivePlayerStartingKit(resident.getName()));
/*     */     }
/*     */     
/*  93 */     if ((War.isWarTime()) && (War.isOnlyWarriors()) && 
/*  94 */       (!this.player.isOp()) && (!this.player.hasPermission("civ.admin")))
/*     */     {
/*  96 */       if ((!resident.hasTown()) || (!resident.getTown().getCiv().getDiplomacyManager().isAtWar())) {
/*  97 */         TaskMaster.syncTask(new PlayerKickBan(this.player.getName(), true, false, "Only players in civilizations at war can connect right now. Sorry."));
/*  98 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 103 */     if ((this.player.hasPermission("civ.moderator")) || (this.player.hasPermission("civ.admin"))) {
/* 104 */       resident.allchat = true;
/* 105 */       Resident.allchatters.add(resident.getName());
/*     */     }
/*     */     
/* 108 */     if (resident.getTreasury().inDebt()) {
/* 109 */       TaskMaster.asyncTask("", new PlayerDelayedDebtWarning(resident), 1000L);
/*     */     }
/*     */     String relationName;
/* 112 */     if (!this.player.isOp()) {
/* 113 */       CultureChunk cc = CivGlobal.getCultureChunk(new ChunkCoord(this.player.getLocation()));
/* 114 */       if ((cc != null) && (cc.getCiv() != resident.getCiv())) {
/* 115 */         Relation.Status status = cc.getCiv().getDiplomacyManager().getRelationStatus(this.player);
/* 116 */         String color = PlayerChunkNotifyAsyncTask.getNotifyColor(cc, status, this.player);
/* 117 */         relationName = status.name();
/*     */         
/* 119 */         if ((War.isWarTime()) && (status.equals(Relation.Status.WAR)))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */           if (resident.getLastOnline() < War.getStart().getTime()) {
/* 127 */             resident.teleportHome();
/* 128 */             CivMessage.send(resident, "§7You've been teleported back to your home since you've logged into enemy during WarTime.");
/*     */           }
/*     */         }
/*     */         
/* 132 */         CivMessage.sendCiv(cc.getCiv(), color + this.player.getDisplayName() + "(" + relationName + ") has logged-in to our borders.");
/*     */       }
/*     */     }
/*     */     
/* 136 */     if (this.player == null) {
/* 137 */       return;
/*     */     }
/*     */     
/* 140 */     resident.setLastOnline(System.currentTimeMillis());
/* 141 */     resident.setLastIP(this.player.getAddress().getAddress().getHostAddress());
/* 142 */     resident.setSpyExposure(resident.getSpyExposure());
/* 143 */     resident.save();
/*     */     
/*     */ 
/*     */ 
/* 147 */     resident.showWarnings(this.player);
/* 148 */     resident.loadPerks();
/*     */     try
/*     */     {
/* 151 */       if (CivSettings.getString(CivSettings.perkConfig, "system.free_perks").equalsIgnoreCase("true")) {
/* 152 */         resident.giveAllFreePerks();
/* 153 */       } else if ((CivSettings.getString(CivSettings.perkConfig, "system.free_admin_perks").equalsIgnoreCase("true")) && 
/* 154 */         (this.player.hasPermission("civ.admin"))) {
/* 155 */         resident.giveAllFreePerks();
/*     */       }
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 159 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 164 */     resident.setUsesAntiCheat(false);
/* 165 */     ACManager.sendChallenge(this.player);
/*     */     
/*     */ 
/* 168 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("global:respawnPlayer");
/* 169 */     ArrayList<SessionEntry> deleted = new ArrayList();
/*     */     
/* 171 */     for (SessionEntry e : entries) {
/* 172 */       String[] split = e.value.split(":");
/*     */       
/* 174 */       BlockCoord coord = new BlockCoord(split[1]);
/* 175 */       this.player.teleport(coord.getLocation());
/* 176 */       deleted.add(e);
/*     */     }
/*     */     
/* 179 */     for (SessionEntry e : deleted) {
/* 180 */       CivGlobal.getSessionDB().delete(e.request_id, "global:respawnPlayer");
/*     */     }
/*     */     try
/*     */     {
/* 184 */       Player p = CivGlobal.getPlayer(resident);
/* 185 */       PlatinumManager.givePlatinumDaily(resident, 
/* 186 */         ((ConfigPlatinumReward)CivSettings.platinumRewards.get("loginDaily")).name, 
/* 187 */         Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("loginDaily")).amount), 
/* 188 */         "Welcome back to CivCraft! Here is %d for logging in today!");
/*     */       
/*     */ 
/* 191 */       ArrayList<SessionEntry> deathEvents = CivGlobal.getSessionDB().lookup("pvplogger:death:" + resident.getName());
/* 192 */       if (deathEvents.size() != 0) {
/* 193 */         CivMessage.send(resident, "§c" + CivColor.BOLD + "You were killed while offline because you logged out while in PvP!");
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
/*     */ 
/*     */ 
/* 214 */         TaskMaster.syncTask(new Runnable()
/*     */         {
/*     */           String playerName;
/*     */           
/*     */           public void run()
/*     */           {
/*     */             try
/*     */             {
/* 205 */               Player p = CivGlobal.getPlayer(this.playerName);
/* 206 */               p.setHealth(0.0D);
/* 207 */               CivGlobal.getSessionDB().delete_all("pvplogger:death:" + p.getName());
/*     */             }
/*     */             catch (CivException localCivException) {}
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 221 */     if (EndConditionDiplomacy.canPeopleVote()) {
/* 222 */       CivMessage.send(resident, "§aThe Council of Eight is built! Use /vote to vote for your favorite Civilization!");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PlayerLoginAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */