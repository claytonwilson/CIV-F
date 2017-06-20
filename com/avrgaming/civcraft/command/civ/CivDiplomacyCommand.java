/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Relation;
/*     */ import com.avrgaming.civcraft.object.Relation.Status;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.questions.CapitulateRequest;
/*     */ import com.avrgaming.civcraft.questions.ChangeRelationResponse;
/*     */ import com.avrgaming.civcraft.threading.tasks.CivQuestionTask;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import org.bukkit.ChatColor;
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
/*     */ public class CivDiplomacyCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public static final long INVITE_TIMEOUT = 30000L;
/*     */   
/*     */   public void init()
/*     */   {
/*  46 */     this.command = "/civ dip";
/*  47 */     this.displayName = "Civ Diplomacy";
/*     */     
/*  49 */     this.commands.put("show", "[civ] - Lists all current diplomatic relations for [civ].");
/*  50 */     this.commands.put("declare", "[civ] [hostile|war] - Sets your relationship with this civ.");
/*  51 */     this.commands.put("request", "[civ] [neutral|peace|ally] - Sends a request to the other civ to change your relations.");
/*  52 */     this.commands.put("gift", "Sends a gift to another civilization.");
/*  53 */     this.commands.put("global", "Shows diplomatic relations for entire server.");
/*  54 */     this.commands.put("wars", "Shows only the wars going on in the entire server.");
/*  55 */     this.commands.put("respond", "[yes|no] - Responds to a request sent by another civ.");
/*  56 */     this.commands.put("liberate", "[town] - Gives this town back to its rightful owner, it if's a capitol the civlization is restored.");
/*  57 */     this.commands.put("capitulate", "[town] - Capitulates this town, if it is conquered, to it's current owner. Requires confirmation.");
/*     */   }
/*     */   
/*     */   public void capitulate_cmd() throws CivException {
/*  61 */     if (War.isWarTime()) {
/*  62 */       throw new CivException("You cannot use this diplomacy command while it is WarTime.");
/*     */     }
/*  64 */     Town town = getNamedTown(1);
/*  65 */     Resident resident = getResident();
/*  66 */     boolean entireCiv = false;
/*     */     
/*  68 */     Civilization motherCiv = town.getMotherCiv();
/*     */     
/*  70 */     if (motherCiv == null) {
/*  71 */       throw new CivException("Cannot capitulate unless captured by another civilization.");
/*     */     }
/*     */     
/*  74 */     if (!town.getMotherCiv().getLeaderGroup().hasMember(resident)) {
/*  75 */       throw new CivException("You must be the leader of the captured civilization in order to capitulate.");
/*     */     }
/*     */     
/*  78 */     if (town.getMotherCiv().getCapitolName().equals(town.getName())) {
/*  79 */       entireCiv = true;
/*     */     }
/*     */     
/*  82 */     String requestMessage = "";
/*  83 */     CapitulateRequest capitulateResponse = new CapitulateRequest();
/*     */     
/*  85 */     if ((this.args.length < 3) || (!this.args[2].equalsIgnoreCase("yes"))) {
/*  86 */       if (entireCiv) {
/*  87 */         CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "Capitualting means that this civ will be DELETED and all of its towns will become a normal towns in " + 
/*  88 */           town.getCiv().getName() + " and can no longer revolt. Are you sure?");
/*  89 */         CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "If you're sure, type /civ dip capitulate " + town.getName() + " yes");
/*     */       } else {
/*  91 */         CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "Capitualting means that this town will become a normal town in " + town.getCiv().getName() + " and can no longer revolt. Are you sure?");
/*  92 */         CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "If you're sure, type /civ dip capitulate " + town.getName() + " yes");
/*     */       }
/*  94 */       return;
/*     */     }
/*     */     
/*  97 */     if (entireCiv) {
/*  98 */       requestMessage = "§e" + ChatColor.BOLD + "The Civilization of " + motherCiv.getName() + " would like to capitulate. Bringing in more towns will increase civ-wide unhappiness. Do we accept?";
/*  99 */       capitulateResponse.from = town.getMotherCiv().getName();
/*     */     } else {
/* 101 */       capitulateResponse.from = ("Town of " + town.getName());
/* 102 */       requestMessage = "§e" + ChatColor.BOLD + "The Town of " + town.getName() + " would like to capitulate. If we accept this town become ours and we" + 
/* 103 */         " will have to pay distance upkeep to it. Do we accept?";
/*     */     }
/*     */     
/* 106 */     capitulateResponse.playerName = resident.getName();
/* 107 */     capitulateResponse.capitulator = town;
/* 108 */     capitulateResponse.to = town.getCiv().getName();
/*     */     
/* 110 */     CivGlobal.requestRelation(motherCiv, town.getCiv(), requestMessage, 
/* 111 */       30000L, capitulateResponse);
/* 112 */     CivMessage.sendSuccess(this.sender, "Sent capitulate request.");
/*     */   }
/*     */   
/*     */   public void liberate_cmd() throws CivException
/*     */   {
/* 117 */     if (War.isWarTime()) {
/* 118 */       throw new CivException("You cannot use this diplomacy command while it is WarTime.");
/*     */     }
/* 120 */     validLeader();
/* 121 */     Town town = getNamedTown(1);
/* 122 */     Civilization civ = getSenderCiv();
/*     */     
/* 124 */     if (town.getCiv() != civ) {
/* 125 */       throw new CivException("This town does not belong to your civlization.");
/*     */     }
/*     */     
/* 128 */     Civilization motherCiv = town.getMotherCiv();
/* 129 */     if (motherCiv == null) {
/* 130 */       throw new CivException("This town has not been captured, you cannot liberate it.");
/*     */     }
/*     */     
/* 133 */     if (town.getName().equals(motherCiv.getCapitolName())) {
/* 134 */       Civilization capitolOwnerCiv = town.getCiv();
/*     */       
/*     */ 
/* 137 */       for (Town t : CivGlobal.getTowns()) {
/* 138 */         if ((t.getMotherCiv() == motherCiv) && (t.getCiv() == capitolOwnerCiv)) {
/* 139 */           t.changeCiv(motherCiv);
/* 140 */           t.setMotherCiv(null);
/* 141 */           t.save();
/*     */         }
/*     */       }
/*     */       
/* 145 */       motherCiv.setConquered(false);
/* 146 */       CivGlobal.removeConqueredCiv(motherCiv);
/* 147 */       CivGlobal.addCiv(motherCiv);
/* 148 */       motherCiv.save();
/* 149 */       CivMessage.global("The civilization of " + motherCiv.getName() + " has been liberated by the good graces of its owner " + civ.getName());
/*     */     } else {
/* 151 */       if (motherCiv.isConquered()) {
/* 152 */         throw new CivException("The mother civilization of " + town.getName() + " is conquered. You cannot liberate this town at the moment.");
/*     */       }
/*     */       
/*     */ 
/* 156 */       town.changeCiv(motherCiv);
/* 157 */       town.setMotherCiv(null);
/* 158 */       town.save();
/* 159 */       CivMessage.global("The town of " + town.getName() + " has been liberated by the good graces of its owner " + civ.getName() + 
/* 160 */         ". It has joined its homeland " + motherCiv.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void gift_cmd() throws CivException {
/* 165 */     CivDiplomacyGiftCommand cmd = new CivDiplomacyGiftCommand();
/* 166 */     if (War.isWarTime()) {
/* 167 */       throw new CivException("You cannot use this diplomacy command while it is WarTime.");
/*     */     }
/* 169 */     cmd.onCommand(this.sender, null, "gift", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void global_cmd() {
/* 173 */     CivMessage.sendHeading(this.sender, "Global Relations");
/*     */     Iterator localIterator2;
/* 175 */     for (Iterator localIterator1 = CivGlobal.getCivs().iterator(); localIterator1.hasNext(); 
/* 176 */         localIterator2.hasNext())
/*     */     {
/* 175 */       Civilization civ = (Civilization)localIterator1.next();
/* 176 */       localIterator2 = civ.getDiplomacyManager().getRelations().iterator(); continue;Relation relation = (Relation)localIterator2.next();
/* 177 */       CivMessage.send(this.sender, civ.getName() + ": " + relation.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public void wars_cmd()
/*     */   {
/* 183 */     CivMessage.sendHeading(this.sender, "Wars");
/* 184 */     HashSet<String> usedRelations = new HashSet();
/*     */     Iterator localIterator2;
/* 186 */     for (Iterator localIterator1 = CivGlobal.getCivs().iterator(); localIterator1.hasNext(); 
/* 187 */         localIterator2.hasNext())
/*     */     {
/* 186 */       Civilization civ = (Civilization)localIterator1.next();
/* 187 */       localIterator2 = civ.getDiplomacyManager().getRelations().iterator(); continue;Relation relation = (Relation)localIterator2.next();
/* 188 */       if ((relation.getStatus().equals(Relation.Status.WAR)) && 
/* 189 */         (!usedRelations.contains(relation.getPairKey()))) {
/* 190 */         CivMessage.send(this.sender, 
/* 191 */           "§b" + CivColor.BOLD + relation.getCiv().getName() + "§c" + " <-- WAR --> " + "§b" + CivColor.BOLD + relation.getOtherCiv().getName());
/* 192 */         usedRelations.add(relation.getPairKey());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void respond_cmd()
/*     */     throws CivException
/*     */   {
/* 200 */     validLeaderAdvisor();
/* 201 */     if (War.isWarTime()) {
/* 202 */       throw new CivException("You cannot use this diplomacy command while it is WarTime.");
/*     */     }
/*     */     
/* 205 */     if (this.args.length < 2) {
/* 206 */       throw new CivException("Please enter 'yes' or 'no'");
/*     */     }
/*     */     
/* 209 */     CivQuestionTask task = CivGlobal.getCivQuestionTask(getSenderCiv());
/* 210 */     if (task == null) {
/* 211 */       throw new CivException("No offer to respond to.");
/*     */     }
/*     */     
/* 214 */     if (this.args[1].equalsIgnoreCase("yes"))
/* 215 */       synchronized (task) {
/* 216 */         task.setResponse("accept");
/* 217 */         task.notifyAll();
/*     */       }
/* 219 */     if (this.args[1].equalsIgnoreCase("no")) {
/* 220 */       synchronized (task) {
/* 221 */         task.setResponse("decline");
/* 222 */         task.notifyAll();
/*     */       }
/*     */     }
/* 225 */     throw new CivException("Please enter 'yes' or 'no'");
/*     */     
/*     */ 
/* 228 */     CivMessage.sendSuccess(this.sender, "Response sent.");
/*     */   }
/*     */   
/*     */   public void request_cmd() throws CivException {
/* 232 */     validLeaderAdvisor();
/* 233 */     Civilization ourCiv = getSenderCiv();
/* 234 */     if (War.isWarTime()) {
/* 235 */       throw new CivException("You cannot use this diplomacy command while it is WarTime.");
/*     */     }
/*     */     
/* 238 */     if (this.args.length < 3) {
/* 239 */       throw new CivException("Enter a civ name followed by 'neutral', 'peace', or 'ally'");
/*     */     }
/*     */     
/* 242 */     Civilization otherCiv = getNamedCiv(1);
/*     */     
/* 244 */     if (ourCiv.getId() == otherCiv.getId()) {
/* 245 */       throw new CivException("Cannot request anything on from your own civilization");
/*     */     }
/*     */     try
/*     */     {
/* 249 */       Relation.Status status = Relation.Status.valueOf(this.args[2].toUpperCase());
/* 250 */       Relation.Status currentStatus = ourCiv.getDiplomacyManager().getRelationStatus(otherCiv);
/*     */       
/* 252 */       if (currentStatus == status) {
/* 253 */         throw new CivException("Already " + status.name() + " with " + otherCiv.getName());
/*     */       }
/*     */       
/* 256 */       String message = "§a" + ChatColor.BOLD + ourCiv.getName() + " has requested ";
/* 257 */       switch (status) {
/*     */       case ALLY: 
/* 259 */         message = message + "a NEUTRAL relationship";
/* 260 */         break;
/*     */       case PEACE: 
/* 262 */         message = message + "a PEACE treaty";
/* 263 */         break;
/*     */       case WAR: 
/* 265 */         message = message + "an ALLY";
/*     */         
/* 267 */         if ((War.isWithinWarDeclareDays()) && (
/* 268 */           (ourCiv.getDiplomacyManager().isAtWar()) || (otherCiv.getDiplomacyManager().isAtWar()))) {
/* 269 */           throw new CivException("Cannot make new allies within " + War.getTimeDeclareDays() + " before WarTime when one of you is at war.");
/*     */         }
/*     */         
/*     */         break;
/*     */       case NEUTRAL: 
/* 274 */         if (!CivGlobal.isCasualMode()) {
/* 275 */           throw new CivException("Can only request war in casual mode.");
/*     */         }
/*     */         
/* 278 */         message = message + "a WAR";
/* 279 */         break;
/*     */       case HOSTILE: default: 
/* 281 */         throw new CivException("Options are 'neutral', 'peace', 'ally' or 'war'");
/*     */       }
/* 283 */       message = message + ". Do we accept?";
/*     */       
/* 285 */       ChangeRelationResponse relationresponse = new ChangeRelationResponse();
/* 286 */       relationresponse.fromCiv = ourCiv;
/* 287 */       relationresponse.toCiv = otherCiv;
/* 288 */       relationresponse.status = status;
/*     */       
/* 290 */       CivGlobal.requestRelation(ourCiv, otherCiv, 
/* 291 */         message, 
/* 292 */         30000L, relationresponse);
/*     */       
/* 294 */       CivMessage.sendSuccess(this.sender, "Request sent.");
/*     */     } catch (IllegalArgumentException e) {
/* 296 */       throw new CivException("Unknown relationship type, options are 'neutral', 'peace', 'ally' or 'war'");
/*     */     }
/*     */   }
/*     */   
/*     */   public void declare_cmd() throws CivException
/*     */   {
/* 302 */     validLeaderAdvisor();
/* 303 */     Civilization ourCiv = getSenderCiv();
/* 304 */     if (War.isWarTime()) {
/* 305 */       throw new CivException("You cannot use this diplomacy command while it is WarTime.");
/*     */     }
/*     */     
/* 308 */     if (this.args.length < 3) {
/* 309 */       throw new CivException("Enter a civ name, followed by 'hostile', or 'war'.");
/*     */     }
/*     */     
/* 312 */     Civilization otherCiv = getNamedCiv(1);
/*     */     
/* 314 */     if (ourCiv.getId() == otherCiv.getId()) {
/* 315 */       throw new CivException("Cannot declare anything on your own civilization.");
/*     */     }
/*     */     
/* 318 */     if (otherCiv.isAdminCiv()) {
/* 319 */       throw new CivException("Cannot declare war on an admin civilization.");
/*     */     }
/*     */     try
/*     */     {
/* 323 */       Relation.Status status = Relation.Status.valueOf(this.args[2].toUpperCase());
/* 324 */       Relation.Status currentStatus = ourCiv.getDiplomacyManager().getRelationStatus(otherCiv);
/*     */       
/*     */ 
/* 327 */       if (currentStatus == status) {
/* 328 */         throw new CivException("Already " + status.name() + " with " + otherCiv.getName());
/*     */       }
/*     */       
/* 331 */       switch (status) {
/*     */       case HOSTILE: 
/* 333 */         if (currentStatus == Relation.Status.WAR) {
/* 334 */           throw new CivException("Cannot declare " + status.name() + " when at war.");
/*     */         }
/*     */         break;
/*     */       case NEUTRAL: 
/* 338 */         if (CivGlobal.isCasualMode()) {
/* 339 */           throw new CivException("Cannot declare war in casual mode. Use '/civ dip request' instead.");
/*     */         }
/*     */         
/* 342 */         if (War.isWarTime()) {
/* 343 */           throw new CivException("Cannot declare war during WarTime.");
/*     */         }
/*     */         
/* 346 */         if (War.isWithinWarDeclareDays()) {
/* 347 */           if (War.isCivAggressorToAlly(otherCiv, ourCiv)) {
/* 348 */             if (War.isWithinAllyDeclareHours()) {
/* 349 */               throw new CivException("Too soon to next WarTime. Allies can only aid other allies within " + War.getAllyDeclareHours() + " hours before WarTime.");
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/* 354 */             throw new CivException("Too soon to next WarTime. Cannot declare " + War.getTimeDeclareDays() + " before WarTime.");
/*     */           }
/*     */         }
/*     */         
/* 358 */         if (ourCiv.getTreasury().inDebt()) {
/* 359 */           throw new CivException("Cannot declare ware while our civilization is in debt.");
/*     */         }
/*     */         
/*     */         break;
/*     */       default: 
/* 364 */         throw new CivException("Options are hostile or war");
/*     */       }
/*     */       
/* 367 */       CivGlobal.setRelation(ourCiv, otherCiv, status);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 375 */       CivGlobal.setAggressor(ourCiv, otherCiv, ourCiv);
/*     */     }
/*     */     catch (IllegalArgumentException e) {
/* 378 */       throw new CivException("Unknown relationship type, options hostile or war");
/*     */     }
/*     */   }
/*     */   
/*     */   public void show_cmd() throws CivException
/*     */   {
/* 384 */     if (this.args.length < 2) {
/* 385 */       show(getSenderCiv());
/* 386 */       return;
/*     */     }
/*     */     
/* 389 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 391 */     show(civ);
/*     */   }
/*     */   
/*     */   public void show(Civilization civ) {
/* 395 */     CivMessage.sendHeading(this.sender, "Diplomatic Relations for §e" + civ.getName());
/*     */     
/* 397 */     for (Relation relation : civ.getDiplomacyManager().getRelations()) {
/* 398 */       if (relation.getStatus() != Relation.Status.NEUTRAL)
/*     */       {
/*     */ 
/* 401 */         CivMessage.send(this.sender, relation.toString());
/*     */       }
/*     */     }
/* 404 */     int warCount = civ.getDiplomacyManager().getWarCount();
/* 405 */     if (warCount != 0) {
/* 406 */       CivMessage.send(this.sender, "§cYour civilization is currently engaged in " + warCount + " wars.");
/*     */     }
/* 408 */     CivMessage.send(this.sender, "§7Not shown means NEUTRAL.");
/*     */   }
/*     */   
/*     */ 
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 415 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 420 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivDiplomacyCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */