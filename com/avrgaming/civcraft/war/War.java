/*     */ package com.avrgaming.civcraft.war;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigPlatinumReward;
/*     */ import com.avrgaming.civcraft.endgame.EndGameCondition;
/*     */ import com.avrgaming.civcraft.event.EventTimer;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.Relation;
/*     */ import com.avrgaming.civcraft.object.Relation.Status;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.siege.Cannon;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.global.perks.PlatinumManager;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class War
/*     */ {
/*     */   private static boolean warTime;
/*  52 */   private static Date start = null;
/*  53 */   private static Date end = null;
/*     */   
/*  55 */   private static boolean onlyWarriors = false;
/*     */   
/*  57 */   private static HashMap<String, Civilization> defeatedTowns = new HashMap();
/*  58 */   private static HashMap<String, Civilization> defeatedCivs = new HashMap();
/*     */   
/*     */   public static void saveDefeatedTown(String townName, Civilization master) {
/*  61 */     defeatedTowns.put(townName, master);
/*     */     
/*  63 */     String key = "capturedTown";
/*  64 */     String value = townName + ":" + master.getId();
/*     */     
/*  66 */     CivGlobal.getSessionDB().add(key, value, master.getId(), 0, 0);
/*     */   }
/*     */   
/*     */   public static void saveDefeatedCiv(Civilization defeated, Civilization master) {
/*  70 */     defeatedCivs.put(defeated.getName(), master);
/*     */     
/*  72 */     String key = "capturedCiv";
/*  73 */     String value = defeated.getName() + ":" + master.getId();
/*     */     Iterator localIterator2;
/*  75 */     for (Iterator localIterator1 = master.getTowns().iterator(); localIterator1.hasNext(); 
/*  76 */         localIterator2.hasNext())
/*     */     {
/*  75 */       Town town = (Town)localIterator1.next();
/*  76 */       localIterator2 = town.getResidents().iterator(); continue;Resident resident = (Resident)localIterator2.next();
/*  77 */       PlatinumManager.givePlatinum(resident, 
/*  78 */         Integer.valueOf(((ConfigPlatinumReward)CivSettings.platinumRewards.get("winningWar")).amount), 
/*  79 */         "Spoils to the victor! You've earned %d");
/*     */     }
/*     */     
/*     */ 
/*  83 */     EndGameCondition.onCivilizationWarDefeat(defeated);
/*  84 */     CivGlobal.getSessionDB().add(key, value, master.getId(), 0, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void resaveAllDefeatedCivs()
/*     */   {
/*  91 */     CivGlobal.getSessionDB().delete_all("capturedCiv");
/*     */     
/*  93 */     for (String civName : defeatedCivs.keySet()) {
/*  94 */       Civilization master = (Civilization)defeatedCivs.get(civName);
/*  95 */       saveDefeatedCiv(CivGlobal.getCiv(civName), master);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void resaveAllDefeatedTowns() {
/* 100 */     CivGlobal.getSessionDB().delete_all("capturedTown");
/*     */     
/* 102 */     for (String townName : defeatedTowns.keySet()) {
/* 103 */       Civilization master = (Civilization)defeatedTowns.get(townName);
/* 104 */       saveDefeatedTown(townName, master);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void loadDefeatedTowns() {
/* 109 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("capturedTown");
/*     */     
/* 111 */     for (SessionEntry entry : entries) {
/* 112 */       String[] split = entry.value.split(":");
/* 113 */       defeatedTowns.put(split[0], CivGlobal.getCivFromId(Integer.valueOf(split[1]).intValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void loadDefeatedCivs() {
/* 118 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup("capturedCiv");
/*     */     
/* 120 */     for (SessionEntry entry : entries) {
/* 121 */       String[] split = entry.value.split(":");
/* 122 */       defeatedCivs.put(split[0], CivGlobal.getCivFromId(Integer.valueOf(split[1]).intValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clearSavedDefeats() {
/* 127 */     CivGlobal.getSessionDB().delete_all("capturedTown");
/* 128 */     CivGlobal.getSessionDB().delete_all("capturedCiv");
/*     */   }
/*     */   
/*     */   public static void init() {
/* 132 */     loadDefeatedTowns();
/* 133 */     loadDefeatedCivs();
/* 134 */     processDefeated();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isWarTime()
/*     */   {
/* 141 */     return warTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setWarTime(boolean warTime)
/*     */   {
/* 149 */     if (!warTime)
/*     */     {
/* 151 */       setStart(null);
/* 152 */       setEnd(null);
/* 153 */       restoreAllTowns();
/* 154 */       repositionPlayers("You've been teleported back to your town hall. WarTime ended and you were in enemy territory.");
/* 155 */       processDefeated();
/*     */       
/* 157 */       CivGlobal.growthEnabled = true;
/* 158 */       CivGlobal.trommelsEnabled = true;
/* 159 */       CivGlobal.tradeEnabled = true;
/*     */       
/*     */ 
/* 162 */       File file = new File("wartime");
/* 163 */       file.delete();
/*     */       
/* 165 */       CivMessage.globalHeading(CivColor.BOLD + "WarTime Has Ended");
/*     */       
/* 167 */       CivMessage.global("Most Lethal: " + WarStats.getTopKiller());
/* 168 */       List<String> civs = WarStats.getCapturedCivs();
/* 169 */       if (civs.size() > 0) {
/* 170 */         for (String str : civs) {
/* 171 */           CivMessage.global(str);
/*     */         }
/*     */       }
/* 174 */       WarStats.clearStats();
/*     */       
/* 176 */       for (Civilization civ : CivGlobal.getCivs()) {
/* 177 */         civ.onWarEnd();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 182 */       CivMessage.globalHeading(CivColor.BOLD + "WarTime Has Started");
/* 183 */       setStart(new Date());
/* 184 */       repositionPlayers("You've been teleported back to your town hall. WarTime has started and you were in enemy territory.");
/*     */       
/* 186 */       resetTownClaimFlags();
/* 187 */       WarAntiCheat.kickUnvalidatedPlayers();
/*     */       
/*     */ 
/* 190 */       File file = new File("wartime");
/*     */       try {
/* 192 */         file.createNewFile();
/*     */       } catch (IOException e1) {
/* 194 */         e1.printStackTrace();
/*     */       }
/*     */       
/* 197 */       CivGlobal.growthEnabled = false;
/* 198 */       CivGlobal.trommelsEnabled = false;
/* 199 */       CivGlobal.tradeEnabled = false;
/*     */       try
/*     */       {
/* 202 */         int mins = CivSettings.getInteger(CivSettings.warConfig, "war.time_length").intValue();
/* 203 */         Calendar endCal = Calendar.getInstance();
/* 204 */         endCal.add(12, mins);
/*     */         
/* 206 */         setEnd(endCal.getTime());
/*     */       } catch (InvalidConfiguration e) {
/* 208 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 212 */     warTime = warTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void transferDefeated(Civilization loser, Civilization winner)
/*     */   {
/* 224 */     ArrayList<String> removeUs = new ArrayList();
/*     */     
/* 226 */     for (String townName : defeatedTowns.keySet()) {
/* 227 */       Civilization civ = (Civilization)defeatedTowns.get(townName);
/* 228 */       if (civ == loser) {
/* 229 */         Town town = CivGlobal.getTown(townName);
/* 230 */         if (town.getCiv() == winner) {
/* 231 */           removeUs.add(townName);
/*     */         } else {
/* 233 */           defeatedTowns.put(townName, winner);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 238 */     for (String townName : removeUs) {
/* 239 */       defeatedTowns.remove(townName);
/*     */     }
/* 241 */     resaveAllDefeatedTowns();
/*     */     
/*     */ 
/* 244 */     for (String civName : defeatedCivs.keySet()) {
/* 245 */       Civilization civ = (Civilization)defeatedCivs.get(civName);
/*     */       
/*     */ 
/* 248 */       if (civ == loser) {
/* 249 */         defeatedCivs.put(civName, winner);
/*     */       }
/*     */     }
/* 252 */     resaveAllDefeatedCivs();
/*     */   }
/*     */   
/*     */ 
/*     */   private static void processDefeated()
/*     */   {
/* 258 */     if (!CivGlobal.isCasualMode()) {
/* 259 */       for (String townName : defeatedTowns.keySet()) {
/*     */         try {
/* 261 */           Town town = CivGlobal.getTown(townName);
/* 262 */           if (town != null)
/*     */           {
/*     */ 
/*     */ 
/* 266 */             Civilization winner = (Civilization)defeatedTowns.get(townName);
/*     */             
/* 268 */             town.onDefeat(winner);
/* 269 */             CivMessage.sendTown(town, "§bWelcome our new overlords " + winner.getName());
/*     */           }
/* 271 */         } catch (Exception e) { e.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/* 275 */       for (String civName : defeatedCivs.keySet()) {
/*     */         try {
/* 277 */           Civilization civ = CivGlobal.getCiv(civName);
/* 278 */           if (civ == null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 283 */             CivLog.error("Couldn't find civilization named " + civName + " in civ hash table while trying to process it's defeat.");
/*     */           }
/*     */           else
/*     */           {
/* 287 */             Civilization winner = (Civilization)defeatedCivs.get(civName);
/* 288 */             CivMessage.sendCiv(civ, "§bWelcome our new overlords " + winner.getName());
/* 289 */             civ.onDefeat(winner);
/*     */           }
/* 291 */         } catch (Exception e) { e.printStackTrace();
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 300 */       for (String civName : defeatedCivs.keySet()) {
/*     */         try {
/* 302 */           Civilization civ = CivGlobal.getCiv(civName);
/* 303 */           if (civ == null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 308 */             CivLog.error("Couldn't find civilization named " + civName + " in civ hash table while trying to process it's defeat.");
/*     */           }
/*     */           else
/*     */           {
/* 312 */             Civilization winner = (Civilization)defeatedCivs.get(civName);
/* 313 */             CivMessage.sendCiv(winner, " has honorably defeated " + civName + ". Their differences settled, they now revert to a neutral relationship.");
/* 314 */             CivMessage.sendCiv(civ, " has honorably defeated " + civName + ". Their differences settled, they now revert to a neutral relationship.");
/* 315 */             CivGlobal.setRelation(winner, civ, Relation.Status.NEUTRAL);
/*     */           }
/* 317 */         } catch (Exception e) { e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 322 */     defeatedTowns.clear();
/* 323 */     defeatedCivs.clear();
/* 324 */     clearSavedDefeats();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void resetTownClaimFlags()
/*     */   {
/* 332 */     for (Town t : ) {
/* 333 */       t.claimed = false;
/* 334 */       t.defeated = false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void repositionPlayers(String reason) {
/* 339 */     for (Civilization civ : ) {
/*     */       try {
/* 341 */         civ.repositionPlayers(reason);
/*     */       } catch (Exception e) {
/* 343 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void restoreAllTowns() {
/* 349 */     for (Town town : ) {
/*     */       try {
/* 351 */         WarRegen.restoreBlocksFor(town.getName());
/* 352 */         if ((town.getTownHall() != null) && (town.getTownHall().isActive())) {
/* 353 */           town.getTownHall().regenControlBlocks();
/*     */         }
/*     */       } catch (Exception e) {
/* 356 */         CivLog.error("Error encountered while restoring town:" + town.getName());
/* 357 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 361 */     WarRegen.restoreBlocksFor("special:WarCamps");
/* 362 */     WarRegen.restoreBlocksFor("special:Cannons");
/* 363 */     Cannon.cleanupAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Date getStart()
/*     */   {
/* 370 */     return start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setStart(Date start)
/*     */   {
/* 378 */     start = start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Date getEnd()
/*     */   {
/* 385 */     return end;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setEnd(Date end)
/*     */   {
/* 392 */     end = end;
/*     */   }
/*     */   
/*     */   public static Date getNextWarTime() {
/* 396 */     EventTimer warTimer = (EventTimer)EventTimer.timers.get("war");
/* 397 */     return warTimer.getNext().getTime();
/*     */   }
/*     */   
/*     */   public static boolean isOnlyWarriors() {
/* 401 */     return onlyWarriors;
/*     */   }
/*     */   
/*     */   public static void setOnlyWarriors(boolean onlyWarriors) {
/* 405 */     onlyWarriors = onlyWarriors;
/*     */   }
/*     */   
/*     */   public static boolean isWithinWarDeclareDays() {
/* 409 */     Date nextWar = getNextWarTime();
/* 410 */     Date now = new Date();
/* 411 */     int time_declare_days = getTimeDeclareDays();
/*     */     
/* 413 */     if (now.getTime() + time_declare_days * 86400000 >= nextWar.getTime()) {
/* 414 */       return true;
/*     */     }
/*     */     
/* 417 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isWithinAllyDeclareHours() {
/* 421 */     Date nextWar = getNextWarTime();
/* 422 */     int ally_declare_hours = getAllyDeclareHours();
/*     */     
/* 424 */     Calendar cal = Calendar.getInstance();
/* 425 */     cal.add(11, ally_declare_hours);
/*     */     
/* 427 */     if (cal.getTime().after(nextWar)) {
/* 428 */       return true;
/*     */     }
/*     */     
/* 431 */     return false;
/*     */   }
/*     */   
/*     */   public static int getTimeDeclareDays() {
/*     */     try {
/* 436 */       return CivSettings.getInteger(CivSettings.warConfig, "war.time_declare_days").intValue();
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 439 */       e.printStackTrace(); }
/* 440 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getAllyDeclareHours()
/*     */   {
/*     */     try {
/* 446 */       return CivSettings.getInteger(CivSettings.warConfig, "war.ally_declare_hours").intValue();
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 449 */       e.printStackTrace(); }
/* 450 */     return 0;
/*     */   }
/*     */   
/*     */   public static boolean isCivAggressor(Civilization civ)
/*     */   {
/* 455 */     for (Relation relation : civ.getDiplomacyManager().getRelations()) {
/* 456 */       if ((relation.getStatus() == Relation.Status.WAR) && 
/* 457 */         (relation.getAggressor() == civ)) {
/* 458 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 462 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isCivAggressorToAlly(Civilization enemy, Civilization ourCiv) {
/* 466 */     for (Relation relation : ourCiv.getDiplomacyManager().getRelations()) {
/* 467 */       if (relation.getStatus() == Relation.Status.ALLY) {
/* 468 */         Civilization ally = relation.getOtherCiv();
/* 469 */         Relation allyRelation = ally.getDiplomacyManager().getRelation(enemy);
/* 470 */         if (allyRelation != null)
/*     */         {
/*     */ 
/*     */ 
/* 474 */           if ((allyRelation.getStatus() == Relation.Status.WAR) && (allyRelation.getAggressor() == enemy))
/* 475 */             return true;
/*     */         }
/*     */       }
/*     */     }
/* 479 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\war\War.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */