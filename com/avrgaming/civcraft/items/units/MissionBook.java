/*     */ package com.avrgaming.civcraft.items.units;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMission;
/*     */ import com.avrgaming.civcraft.config.ConfigTech;
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.config.ConfigUnit;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveSpyMission;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.AttrSource;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.MissionLogger;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.Cottage;
/*     */ import com.avrgaming.civcraft.structure.Granary;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.structure.TradeOutpost;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.BookUtil;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.sql.SQLException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.BookMeta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MissionBook
/*     */   extends UnitItemMaterial
/*     */ {
/*     */   public MissionBook(String id, int minecraftId, short damage)
/*     */   {
/*  68 */     super(id, minecraftId, damage);
/*     */   }
/*     */   
/*     */   public static double getMissionFailChance(ConfigMission mission, Town town) {
/*  72 */     int onlineResidents = town.getOnlineResidents().size();
/*  73 */     double chance = 1.0D - mission.fail_chance.doubleValue();
/*     */     
/*  75 */     if (mission.intel.intValue() != 0) {
/*  76 */       double percentIntel = onlineResidents / mission.intel.intValue();
/*  77 */       if (percentIntel > 1.0D) {
/*  78 */         percentIntel = 1.0D;
/*     */       }
/*     */       
/*  81 */       chance *= percentIntel;
/*     */     }
/*     */     
/*  84 */     chance = 1.0D - chance;
/*  85 */     return chance;
/*     */   }
/*     */   
/*     */   public static double getMissionCompromiseChance(ConfigMission mission, Town town) {
/*  89 */     int onlineResidents = town.getOnlineResidents().size();
/*  90 */     double chance = 1.0D - mission.compromise_chance.doubleValue();
/*     */     
/*  92 */     if (mission.intel.intValue() != 0) {
/*  93 */       double percentIntel = onlineResidents / mission.intel.intValue();
/*  94 */       if (percentIntel > 1.0D) {
/*  95 */         percentIntel = 1.0D;
/*     */       }
/*     */       
/*  98 */       chance *= percentIntel;
/*     */     }
/*     */     
/* 101 */     chance = 1.0D - chance;
/* 102 */     return chance;
/*     */   }
/*     */   
/*     */   public void setupLore(String id) {
/* 106 */     ConfigMission mission = (ConfigMission)CivSettings.missions.get(getId());
/*     */     
/* 108 */     if (mission == null) {
/* 109 */       CivLog.warning("Couldn't find mission with id:" + id + " to set the lore.");
/* 110 */       return;
/*     */     }
/*     */     
/* 113 */     for (String str : mission.description) {
/* 114 */       addLore(str);
/*     */     }
/* 116 */     addLore("§e" + mission.cost + " Coins.");
/* 117 */     addLore("§6Soulbound");
/*     */   }
/*     */   
/*     */ 
/*     */   public void onInteractEntity(PlayerInteractEntityEvent event)
/*     */   {
/* 123 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/*     */     try
/*     */     {
/* 131 */       if (War.isWarTime()) {
/* 132 */         throw new CivException("Cannot use spy missions during war time.");
/*     */       }
/*     */       
/* 135 */       ConfigMission mission = (ConfigMission)CivSettings.missions.get(getId());
/* 136 */       if (mission == null) {
/* 137 */         throw new CivException("Unknown mission " + getId());
/*     */       }
/*     */       
/* 140 */       Resident resident = CivGlobal.getResident(event.getPlayer());
/* 141 */       if ((resident == null) || (!resident.hasTown())) {
/* 142 */         throw new CivException("Only residents of towns can perform spy missions.");
/*     */       }
/*     */       
/* 145 */       Date now = new Date();
/*     */       
/* 147 */       if (!event.getPlayer().isOp()) {
/*     */         try {
/* 149 */           int spyRegisterTime = CivSettings.getInteger(CivSettings.espionageConfig, "espionage.spy_register_time").intValue();
/* 150 */           int spyOnlineTime = CivSettings.getInteger(CivSettings.espionageConfig, "espionage.spy_online_time").intValue();
/*     */           
/* 152 */           long expire = resident.getRegistered() + spyRegisterTime * 60 * 1000;
/* 153 */           if (now.getTime() <= expire) {
/* 154 */             throw new CivException("You cannot use a spy yet, you must play CivCraft a bit longer before you can use it.");
/*     */           }
/*     */           
/* 157 */           expire = resident.getLastOnline() + spyOnlineTime * 60 * 1000;
/* 158 */           if (now.getTime() <= expire) {
/* 159 */             throw new CivException("You must be online longer before you can use a spy.");
/*     */           }
/*     */         } catch (InvalidConfiguration e) {
/* 162 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/* 166 */       ConfigUnit unit = Unit.getPlayerUnit(event.getPlayer());
/* 167 */       if ((unit == null) || (!unit.id.equals("u_spy"))) {
/* 168 */         event.getPlayer().getInventory().remove(event.getItem());
/* 169 */         throw new CivException("Only spies can use mission books.");
/*     */       }
/*     */       
/* 172 */       ChunkCoord coord = new ChunkCoord(event.getPlayer().getLocation());
/* 173 */       CultureChunk cc = CivGlobal.getCultureChunk(coord);
/* 174 */       TownChunk tc = CivGlobal.getTownChunk(coord);
/*     */       
/* 176 */       if ((cc == null) || (cc.getCiv() == resident.getCiv())) {
/* 177 */         throw new CivException("You must be in a civilization's culture that's not your own to spy on them.");
/*     */       }
/*     */       
/* 180 */       if (((cc != null) && (cc.getCiv().isAdminCiv())) || ((tc != null) && (tc.getTown().getCiv().isAdminCiv()))) {
/* 181 */         throw new CivException("You cannot spy on an admin civ.");
/*     */       }
/*     */       
/* 184 */       if ((CivGlobal.isCasualMode()) && 
/* 185 */         (!cc.getCiv().getDiplomacyManager().isHostileWith(resident.getCiv())) && 
/* 186 */         (!cc.getCiv().getDiplomacyManager().atWarWith(resident.getCiv()))) {
/* 187 */         throw new CivException("You must be hostile or at war with " + cc.getCiv().getName() + " in order to perform spy missions in casual mode.");
/*     */       }
/*     */       
/*     */ 
/* 191 */       resident.setInteractiveMode(new InteractiveSpyMission(mission, event.getPlayer().getName(), event.getPlayer().getLocation(), cc.getTown()));
/*     */     } catch (CivException e) {
/* 193 */       CivMessage.sendError(event.getPlayer(), e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void performMission(ConfigMission mission, String playerName)
/*     */   {
/*     */     try
/*     */     {
/* 201 */       player = CivGlobal.getPlayer(playerName);
/*     */     } catch (CivException e1) { Player player;
/* 203 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 207 */       Resident resident = CivGlobal.getResident(playerName);
/* 208 */       if (!resident.getTown().getTreasury().hasEnough(mission.cost.doubleValue())) {
/* 209 */         throw new CivException("Your town requires " + mission.cost + " coins to perform this mission.");
/*     */       }
/*     */       String str;
/* 212 */       switch ((str = mission.id).hashCode()) {case -318455497:  if (str.equals("spy_incite_riots")) {} break; case -221475690:  if (str.equals("spy_steal_treasury")) {} break; case 63523093:  if (str.equals("spy_investigate_town")) break; break; case 321732540:  if (str.equals("spy_poison_granary")) {} break; case 451430972:  if (str.equals("spy_pirate")) {} break; case 2011698377:  if (!str.equals("spy_sabotage"))
/*     */         {
/* 214 */           return;performInvestigateTown(player, mission);
/* 215 */           return;
/*     */           
/* 217 */           performStealTreasury(player, mission);
/* 218 */           return;
/*     */           
/* 220 */           performInciteRiots(player, mission);
/* 221 */           return;
/*     */           
/* 223 */           performPosionGranary(player, mission);
/* 224 */           return;
/*     */           
/* 226 */           performPirate(player, mission);
/*     */         }
/*     */         else {
/* 229 */           performSabotage(player, mission);
/*     */         }
/*     */         break; }
/*     */     } catch (CivException e) {
/*     */       Player player;
/* 234 */       CivMessage.sendError(player, e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean processMissionResult(Player player, Town target, ConfigMission mission) {
/* 239 */     return processMissionResult(player, target, mission, 1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   private static boolean processMissionResult(Player player, Town target, ConfigMission mission, double failModifier, double compromiseModifier)
/*     */   {
/* 244 */     int fail_rate = (int)(getMissionFailChance(mission, target) * failModifier * 100.0D);
/* 245 */     int compromise_rate = (int)(getMissionCompromiseChance(mission, target) * compromiseModifier * 100.0D);
/* 246 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 248 */     if ((resident == null) || (!resident.hasTown())) {
/* 249 */       return false;
/*     */     }
/*     */     
/* 252 */     if (!resident.getTown().getTreasury().hasEnough(mission.cost.doubleValue())) {
/* 253 */       CivMessage.send(player, "§cSuddenly, your town doesn't have enough cash to follow through with the mission.");
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     resident.getTown().getTreasury().withdraw(mission.cost.doubleValue());
/*     */     
/* 259 */     Random rand = new Random();
/* 260 */     String result = "";
/* 261 */     int failnext = rand.nextInt(100);
/* 262 */     if (failnext < fail_rate) {
/* 263 */       int next = rand.nextInt(100);
/* 264 */       result = result + "Failed";
/*     */       
/* 266 */       if (next < compromise_rate) {
/* 267 */         CivMessage.global(
/*     */         
/* 269 */           "§eINTERNATIONAL INCIDENT!§f " + player.getName() + " was caught trying to perform a " + mission.name + " spy mission in " + target.getName() + "!");
/* 270 */         CivMessage.send(player, "§cYou've been compromised! (Rolled " + next + " vs " + compromise_rate + ") Spy unit was destroyed!");
/* 271 */         Unit.removeUnit(player);
/* 272 */         result = result + ", COMPROMISED";
/*     */       }
/*     */       
/* 275 */       MissionLogger.logMission(resident.getTown(), target, player.getName(), mission.name, result);
/* 276 */       CivMessage.send(player, "§cMission Failed! (Rolled " + failnext + " vs " + fail_rate + ")");
/* 277 */       return false;
/*     */     }
/*     */     
/* 280 */     MissionLogger.logMission(resident.getTown(), target, player.getName(), mission.name, "Success");
/* 281 */     return true;
/*     */   }
/*     */   
/*     */   private static void performSabotage(Player player, ConfigMission mission) throws CivException
/*     */   {
/* 286 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*     */ 
/* 289 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 290 */     CultureChunk cc = CivGlobal.getCultureChunk(coord);
/* 291 */     if ((cc == null) || (cc.getCiv() == resident.getTown().getCiv())) {
/* 292 */       throw new CivException("Must be in another civilization's borders.");
/*     */     }
/*     */     
/*     */ 
/* 296 */     Buildable buildable = cc.getTown().getNearestBuildable(player.getLocation());
/* 297 */     if ((buildable instanceof TownHall)) {
/* 298 */       throw new CivException("Nearest structure is a town hall which cannot be destroyed.");
/*     */     }
/* 300 */     if (((buildable instanceof Wonder)) && 
/* 301 */       (buildable.isComplete())) {
/* 302 */       throw new CivException("Cannot sabotage completed wonders.");
/*     */     }
/*     */     
/*     */ 
/* 306 */     double distance = player.getLocation().distance(buildable.getCorner().getLocation());
/* 307 */     if (distance > mission.range.doubleValue()) {
/* 308 */       throw new CivException("Too far away the " + buildable.getDisplayName() + " to sabotage it");
/*     */     }
/*     */     
/* 311 */     if ((buildable instanceof Structure)) {
/* 312 */       if (!buildable.isComplete()) {
/* 313 */         throw new CivException("Cannot sabotage incomplete structures.");
/*     */       }
/*     */       
/* 316 */       if (buildable.isDestroyed()) {
/* 317 */         throw new CivException(buildable.getDisplayName() + " is already destroyed.");
/*     */       }
/*     */     }
/*     */     
/* 321 */     if ((buildable instanceof Wonder))
/*     */     {
/* 323 */       mission = (ConfigMission)CivSettings.missions.get("spy_sabotage_wonder");
/*     */     }
/*     */     
/* 326 */     double failMod = 1.0D;
/* 327 */     if (resident.getTown().getBuffManager().hasBuff("buff_sabotage")) {
/* 328 */       failMod = resident.getTown().getBuffManager().getEffectiveDouble("buff_sabotage");
/* 329 */       CivMessage.send(player, "§7Your goodie buff 'Sabotage' will come in handy here.");
/*     */     }
/*     */     
/* 332 */     if (processMissionResult(player, cc.getTown(), mission, failMod, 1.0D)) {
/* 333 */       CivMessage.global("§eDISASTER!§f A " + buildable.getDisplayName() + " has been destroyed! Foul play is suspected.");
/* 334 */       buildable.setHitpoints(0);
/* 335 */       buildable.fancyDestroyStructureBlocks();
/* 336 */       buildable.save();
/*     */       
/* 338 */       if ((buildable instanceof Wonder)) {
/* 339 */         Wonder wonder = (Wonder)buildable;
/* 340 */         wonder.unbindStructureBlocks();
/*     */         try {
/* 342 */           wonder.delete();
/*     */         } catch (SQLException e) {
/* 344 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void performPirate(Player player, ConfigMission mission)
/*     */     throws CivException
/*     */   {
/* 353 */     Resident resident = CivGlobal.getResident(player);
/* 354 */     if ((resident == null) || (!resident.hasTown())) {
/* 355 */       throw new CivException("Only residents of towns can perform spy missions.");
/*     */     }
/*     */     
/* 358 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 359 */     CultureChunk cc = CivGlobal.getCultureChunk(coord);
/* 360 */     if ((cc == null) || (cc.getCiv() == resident.getTown().getCiv())) {
/* 361 */       throw new CivException("Must be in another civilization's borders.");
/*     */     }
/*     */     
/*     */ 
/* 365 */     Structure tradeoutpost = cc.getCiv().getNearestStructureInTowns(player.getLocation());
/* 366 */     if (!(tradeoutpost instanceof TradeOutpost)) {
/* 367 */       throw new CivException("The closest structure to you must be a trade outpost.");
/*     */     }
/*     */     
/* 370 */     double distance = player.getLocation().distance(((TradeOutpost)tradeoutpost).getTradeOutpostTower().getLocation());
/* 371 */     if (distance > mission.range.doubleValue()) {
/* 372 */       throw new CivException("Too far away from the trade outpost to pirate it.");
/*     */     }
/*     */     
/* 375 */     TradeOutpost outpost = (TradeOutpost)tradeoutpost;
/* 376 */     ItemStack stack = outpost.getItemFrameStore().getItem();
/*     */     
/* 378 */     if ((stack == null) || (ItemManager.getId(stack) == 0)) {
/* 379 */       throw new CivException("No trade goodie item at this location.");
/*     */     }
/*     */     
/* 382 */     if (processMissionResult(player, cc.getTown(), mission)) {
/* 383 */       outpost.getItemFrameStore().clearItem();
/* 384 */       player.getWorld().dropItem(player.getLocation(), stack);
/*     */       
/* 386 */       CivMessage.sendSuccess(player, "Arg! Got the booty!");
/* 387 */       CivMessage.sendTown(cc.getTown(), "§cAvast! Someone stole our trade goodie " + outpost.getGood().getInfo().name + " at " + outpost.getCorner());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void performPosionGranary(Player player, ConfigMission mission) throws CivException {
/* 392 */     Resident resident = CivGlobal.getResident(player);
/* 393 */     if ((resident == null) || (!resident.hasTown())) {
/* 394 */       throw new CivException("Only residents of towns can perform spy missions.");
/*     */     }
/*     */     
/*     */ 
/* 398 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 399 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*     */     
/* 401 */     if ((tc == null) || (tc.getTown().getCiv() == resident.getTown().getCiv())) {
/* 402 */       throw new CivException("Must be in another civilization's town's borders.");
/*     */     }
/*     */     
/*     */ 
/* 406 */     Structure granary = tc.getTown().getNearestStrucutre(player.getLocation());
/* 407 */     if (!(granary instanceof Granary)) {
/* 408 */       throw new CivException("The closest structure to you must be a granary.");
/*     */     }
/*     */     
/* 411 */     double distance = player.getLocation().distance(granary.getCorner().getLocation());
/* 412 */     if (distance > mission.range.doubleValue()) {
/* 413 */       throw new CivException("Too far away from the granary to poison it.");
/*     */     }
/*     */     
/* 416 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("posiongranary:" + tc.getTown().getName());
/* 417 */     if ((entries != null) && (entries.size() != 0)) {
/* 418 */       throw new CivException("Cannot poison granary, already posioned.");
/*     */     }
/*     */     
/* 421 */     double failMod = 1.0D;
/* 422 */     if (resident.getTown().getBuffManager().hasBuff("buff_espionage")) {
/* 423 */       failMod = resident.getTown().getBuffManager().getEffectiveDouble("buff_espionage");
/* 424 */       CivMessage.send(player, "§7Your goodie buff 'Espionage' will come in handy here.");
/*     */     }
/*     */     
/* 427 */     if (processMissionResult(player, tc.getTown(), mission, failMod, 1.0D))
/*     */     {
/*     */       try
/*     */       {
/* 431 */         int min = CivSettings.getInteger(CivSettings.espionageConfig, "espionage.poison_granary_min_ticks").intValue();
/* 432 */         max = CivSettings.getInteger(CivSettings.espionageConfig, "espionage.poison_granary_max_ticks").intValue();
/*     */       } catch (InvalidConfiguration e) { int max;
/* 434 */         e.printStackTrace();
/* 435 */         throw new CivException("Invalid configuration error."); }
/*     */       int max;
/*     */       int min;
/* 438 */       Random rand = new Random();
/* 439 */       int posion_ticks = rand.nextInt(max - min) + min;
/* 440 */       String value = posion_ticks;
/*     */       
/* 442 */       CivGlobal.getSessionDB().add("posiongranary:" + tc.getTown().getName(), value, tc.getTown().getId(), tc.getTown().getId(), granary.getId());
/*     */       try
/*     */       {
/* 445 */         double famine_chance = CivSettings.getDouble(CivSettings.espionageConfig, "espionage.poison_granary_famine_chance");
/*     */         
/* 447 */         if (rand.nextInt(100) < (int)(famine_chance * 100.0D))
/*     */         {
/* 449 */           for (Structure struct : tc.getTown().getStructures()) {
/* 450 */             if ((struct instanceof Cottage)) {
/* 451 */               ((Cottage)struct).delevel();
/*     */             }
/*     */           }
/*     */           
/* 455 */           CivMessage.global(
/* 456 */             "§eDISASTER!§f The cottages in " + tc.getTown().getName() + " have suffered a famine from poison grain! Each cottage loses 1 level.");
/*     */         }
/*     */       } catch (InvalidConfiguration e) {
/* 459 */         e.printStackTrace();
/* 460 */         throw new CivException("Invalid configuration.");
/*     */       }
/*     */       
/* 463 */       CivMessage.sendSuccess(player, "Poisoned the granary for " + posion_ticks + " hours!");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void performStealTreasury(Player player, ConfigMission mission) throws CivException
/*     */   {
/* 469 */     Resident resident = CivGlobal.getResident(player);
/* 470 */     if ((resident == null) || (!resident.hasTown())) {
/* 471 */       throw new CivException("Only residents of towns can perform spy missions.");
/*     */     }
/*     */     
/*     */ 
/* 475 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 476 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*     */     
/* 478 */     if ((tc == null) || (tc.getTown().getCiv() == resident.getTown().getCiv())) {
/* 479 */       throw new CivException("Must be in another civilization's town's borders.");
/*     */     }
/*     */     
/*     */ 
/* 483 */     TownHall townhall = tc.getTown().getTownHall();
/* 484 */     if (townhall == null) {
/* 485 */       throw new CivException("This town doesnt have a town hall... that sucks.");
/*     */     }
/*     */     
/* 488 */     double distance = player.getLocation().distance(townhall.getCorner().getLocation());
/* 489 */     if (distance > mission.range.doubleValue()) {
/* 490 */       throw new CivException("Too far away from town hall to steal treasury.");
/*     */     }
/*     */     
/* 493 */     double failMod = 1.0D;
/* 494 */     if (resident.getTown().getBuffManager().hasBuff("buff_dirty_money")) {
/* 495 */       failMod = resident.getTown().getBuffManager().getEffectiveDouble("buff_dirty_money");
/* 496 */       CivMessage.send(player, "§7Your goodie buff 'Dirty Money' will come in handy here.");
/*     */     }
/*     */     
/* 499 */     if (processMissionResult(player, tc.getTown(), mission, failMod, 1.0D))
/*     */     {
/* 501 */       double amount = (int)(tc.getTown().getTreasury().getBalance() * 0.2D);
/* 502 */       if (amount > 0.0D) {
/* 503 */         tc.getTown().getTreasury().withdraw(amount);
/* 504 */         resident.getTown().getTreasury().deposit(amount);
/*     */       }
/*     */       
/* 507 */       CivMessage.sendSuccess(player, "Success! Stole " + amount + " coins from " + tc.getTown().getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void performInvestigateTown(Player player, ConfigMission mission)
/*     */     throws CivException
/*     */   {
/* 514 */     Resident resident = CivGlobal.getResident(player);
/* 515 */     if ((resident == null) || (!resident.hasTown())) {
/* 516 */       throw new CivException("Only residents of towns can perform spy missions.");
/*     */     }
/*     */     
/*     */ 
/* 520 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 521 */     TownChunk tc = CivGlobal.getTownChunk(coord);
/*     */     
/* 523 */     if ((tc == null) || (tc.getTown().getCiv() == resident.getTown().getCiv())) {
/* 524 */       throw new CivException("Must be in another civilization's town's borders.");
/*     */     }
/*     */     
/* 527 */     if (processMissionResult(player, tc.getTown(), mission)) {
/* 528 */       ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
/* 529 */       BookMeta meta = (BookMeta)book.getItemMeta();
/* 530 */       ArrayList<String> lore = new ArrayList();
/* 531 */       lore.add("Mission Report");
/*     */       
/* 533 */       meta.setAuthor("Mission Reports");
/* 534 */       meta.setTitle("Investigate Town");
/*     */       
/*     */ 
/* 537 */       String out = "";
/*     */       
/* 539 */       out = out + ChatColor.UNDERLINE + "Town:" + tc.getTown().getName() + "\n" + ChatColor.RESET;
/* 540 */       out = out + ChatColor.UNDERLINE + "Civ:" + tc.getTown().getCiv().getName() + "\n\n" + ChatColor.RESET;
/*     */       
/* 542 */       SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/* 543 */       out = out + "Time: " + sdf.format(new Date()) + "\n";
/* 544 */       out = out + "Treasury: " + tc.getTown().getTreasury().getBalance() + "\n";
/* 545 */       out = out + "Hammers: " + tc.getTown().getHammers().total + "\n";
/* 546 */       out = out + "Culture: " + tc.getTown().getCulture().total + "\n";
/* 547 */       out = out + "Growth: " + tc.getTown().getGrowth().total + "\n";
/* 548 */       out = out + "Beakers(civ): " + tc.getTown().getBeakers().total + "\n";
/* 549 */       if (tc.getTown().getCiv().getResearchTech() != null) {
/* 550 */         out = out + "Researching: " + tc.getTown().getCiv().getResearchTech().name + "\n";
/*     */       } else {
/* 552 */         out = out + "Researching:Nothing\n";
/*     */       }
/*     */       
/* 555 */       BookUtil.paginate(meta, out);
/*     */       
/* 557 */       out = ChatColor.UNDERLINE + "Upkeep Info\n\n" + ChatColor.RESET;
/*     */       try {
/* 559 */         out = out + "From Spread:" + tc.getTown().getSpreadUpkeep() + "\n";
/* 560 */         out = out + "From Structures:" + tc.getTown().getStructureUpkeep() + "\n";
/* 561 */         out = out + "Total:" + tc.getTown().getTotalUpkeep();
/* 562 */         BookUtil.paginate(meta, out);
/*     */       } catch (InvalidConfiguration e) {
/* 564 */         e.printStackTrace();
/* 565 */         throw new CivException("Internal configuration exception.");
/*     */       }
/*     */       
/*     */ 
/* 569 */       meta.setLore(lore);
/* 570 */       book.setItemMeta(meta);
/*     */       
/* 572 */       HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack[] { book });
/* 573 */       for (ItemStack stack : leftovers.values()) {
/* 574 */         player.getWorld().dropItem(player.getLocation(), stack);
/*     */       }
/*     */       
/* 577 */       player.updateInventory();
/*     */       
/* 579 */       CivMessage.sendSuccess(player, "Mission Accomplished");
/*     */     }
/*     */   }
/*     */   
/*     */   private static void performInciteRiots(Player player, ConfigMission mission) throws CivException {
/* 584 */     throw new CivException("Not implemented.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\units\MissionBook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */