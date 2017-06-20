/*     */ package com.avrgaming.civcraft.command.civ;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.endgame.EndConditionDiplomacy;
/*     */ import com.avrgaming.civcraft.endgame.EndGameCondition;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Relation.Status;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import java.sql.SQLException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeMap;
/*     */ import org.bukkit.ChatColor;
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
/*     */ public class CivCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  52 */     this.command = "/civ";
/*  53 */     this.displayName = "Civ";
/*     */     
/*  55 */     this.commands.put("townlist", "Shows a list of all towns in the civilization.");
/*  56 */     this.commands.put("deposit", "[amount] - deposits this amount into the civ's treasury.");
/*  57 */     this.commands.put("withdraw", "[amount] - withdraws this amount from the civ's treasury.");
/*  58 */     this.commands.put("info", "Shows information about this Civilization");
/*  59 */     this.commands.put("show", "[name] gives you information about the civ named [name].");
/*  60 */     this.commands.put("list", "(name) - shows all civs in the world, or the towns for the civ named (name).");
/*  61 */     this.commands.put("research", "Manage civilization's research.");
/*  62 */     this.commands.put("gov", "Manage your civilizations government.");
/*  63 */     this.commands.put("time", "View information about upcoming events.");
/*  64 */     this.commands.put("set", "Set various civilization properties such as taxes and border color");
/*  65 */     this.commands.put("group", "Manage the leaders and advisers group.");
/*  66 */     this.commands.put("dip", "Manage civilization's diplomacy.");
/*  67 */     this.commands.put("victory", "Show which civs are close to victory.");
/*  68 */     this.commands.put("votes", "Shows the diplomatic votes for all civs.");
/*  69 */     this.commands.put("top5", "Show the top 5 civilizations in the world.");
/*  70 */     this.commands.put("disbandtown", "[town] Disbands this town. Mayor must also issue /town disbandtown");
/*  71 */     this.commands.put("revolution", "stages a revolution for the mother civilization!");
/*  72 */     this.commands.put("claimleader", "claim yourself as leader of this civ. All current leaders must be inactive.");
/*     */   }
/*     */   
/*     */   public void claimleader_cmd() throws CivException {
/*  76 */     Civilization civ = getSenderCiv();
/*  77 */     Resident resident = getResident();
/*     */     
/*  79 */     if (!civ.areLeadersInactive()) {
/*  80 */       throw new CivException("At least one leader is not inactive for your civ. Cannot claim leadership.");
/*     */     }
/*     */     
/*  83 */     civ.getLeaderGroup().addMember(resident);
/*  84 */     civ.getLeaderGroup().save();
/*  85 */     CivMessage.sendSuccess(this.sender, "You are now a leader in " + civ.getName());
/*  86 */     CivMessage.sendCiv(civ, resident.getName() + " has assumed control of the civilization due to inactive leadership.");
/*     */   }
/*     */   
/*     */   public void votes_cmd() throws CivException
/*     */   {
/*  91 */     CivMessage.sendHeading(this.sender, "Diplomatic Votes");
/*  92 */     for (Civilization civ : CivGlobal.getCivs()) {
/*  93 */       Integer votes = EndConditionDiplomacy.getVotesFor(civ);
/*  94 */       if (votes.intValue() != 0) {
/*  95 */         CivMessage.send(this.sender, "§b" + 
/*  96 */           CivColor.BOLD + civ.getName() + "§f" + " has " + 
/*  97 */           "§d" + CivColor.BOLD + votes + "§f" + " votes");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void victory_cmd()
/*     */   {
/* 104 */     CivMessage.sendHeading(this.sender, "Civs Close To Victory");
/* 105 */     boolean anybody = false;
/*     */     
/* 107 */     for (EndGameCondition endCond : EndGameCondition.endConditions) {
/* 108 */       ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(endCond.getSessionKey());
/* 109 */       if (entries.size() != 0)
/*     */       {
/*     */ 
/*     */ 
/* 113 */         anybody = true;
/* 114 */         for (SessionEntry entry : entries) {
/* 115 */           Civilization civ = EndGameCondition.getCivFromSessionData(entry.value);
/* 116 */           Integer daysLeft = Integer.valueOf(endCond.getDaysToHold() - endCond.getDaysHeldFromSessionData(entry.value).intValue());
/* 117 */           CivMessage.send(this.sender, "§b" + CivColor.BOLD + civ.getName() + "§f" + " is " + 
/* 118 */             "§e" + CivColor.BOLD + daysLeft + "§f" + " days away from a " + "§d" + CivColor.BOLD + endCond.getVictoryName() + 
/* 119 */             "§f" + " victory!");
/*     */         }
/*     */       }
/*     */     }
/* 123 */     if (!anybody) {
/* 124 */       CivMessage.send(this.sender, "§7Nobody yet...");
/*     */     }
/*     */   }
/*     */   
/*     */   public void revolution_cmd() throws CivException
/*     */   {
/* 130 */     Town town = getSelectedTown();
/*     */     
/* 132 */     if ((War.isWarTime()) || (War.isWithinWarDeclareDays())) {
/* 133 */       throw new CivException("Can not start a revolution during WarTime or " + War.getTimeDeclareDays() + " days before WarTime");
/*     */     }
/*     */     
/* 136 */     if (town.getMotherCiv() == null) {
/* 137 */       throw new CivException("Cannot start a revolution unless captured by another civilization.");
/*     */     }
/*     */     
/* 140 */     Civilization motherCiv = town.getMotherCiv();
/*     */     
/* 142 */     if (!motherCiv.getCapitolName().equals(town.getName())) {
/* 143 */       throw new CivException("Can only start a revolution from your mother civilization's capitol town(" + motherCiv.getCapitolName() + ").");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 148 */       int revolution_cooldown = CivSettings.getInteger(CivSettings.civConfig, "civ.revolution_cooldown").intValue();
/*     */       
/* 150 */       Calendar cal = Calendar.getInstance();
/* 151 */       Calendar revCal = Calendar.getInstance();
/*     */       
/* 153 */       Date conquered = town.getMotherCiv().getConqueredDate();
/* 154 */       if (conquered == null) {
/* 155 */         throw new CivException("You must have been conquered to start a revolution.");
/*     */       }
/*     */       
/* 158 */       revCal.setTime(town.getMotherCiv().getConqueredDate());
/* 159 */       revCal.add(5, revolution_cooldown);
/*     */       
/* 161 */       if (!cal.after(revCal)) {
/* 162 */         throw new CivException("Cannot start a revolution within " + revolution_cooldown + " of being conquered.");
/*     */       }
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 166 */       e.printStackTrace();
/* 167 */       throw new CivException("Internal Configuration Error.");
/*     */     }
/*     */     
/*     */ 
/* 171 */     double revolutionFee = motherCiv.getRevolutionFee();
/*     */     
/* 173 */     if ((this.args.length < 2) || (!this.args[1].equalsIgnoreCase("yes"))) {
/* 174 */       CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "For a measly " + revolutionFee + " we could fund a revolution and get our old civ back!");
/* 175 */       CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "Funding a revolution will put us AT WAR with any town that currently owns one of our native towns. To win the revolution, we" + 
/* 176 */         "only need to survive the wars. Are you sure you want to do this?");
/* 177 */       CivMessage.send(this.sender, "§aType '/civ revolution yes' to start the revolution.");
/* 178 */       return;
/*     */     }
/*     */     
/* 181 */     if (!town.getTreasury().hasEnough(revolutionFee)) {
/* 182 */       throw new CivException("The capitol town doesnt have the required " + revolutionFee + " in order to start a revolution.");
/*     */     }
/*     */     
/*     */ 
/* 186 */     HashSet<String> warCivs = new HashSet();
/* 187 */     for (Town t : CivGlobal.getTowns()) {
/* 188 */       if (t.getMotherCiv() == motherCiv) {
/* 189 */         warCivs.add(t.getCiv().getName());
/* 190 */         t.changeCiv(motherCiv);
/* 191 */         t.setMotherCiv(null);
/* 192 */         t.save();
/*     */       }
/*     */     }
/*     */     
/* 196 */     for (String warCivName : warCivs) {
/* 197 */       Civilization civ = CivGlobal.getCiv(warCivName);
/* 198 */       if (civ != null) {
/* 199 */         CivGlobal.setRelation(civ, motherCiv, Relation.Status.WAR);
/*     */         
/* 201 */         CivGlobal.setAggressor(civ, motherCiv, civ);
/*     */       }
/*     */     }
/*     */     
/* 205 */     motherCiv.setConquered(false);
/* 206 */     CivGlobal.removeConqueredCiv(motherCiv);
/* 207 */     CivGlobal.addCiv(motherCiv);
/* 208 */     motherCiv.save();
/*     */     
/*     */ 
/* 211 */     town.getTreasury().withdraw(revolutionFee);
/* 212 */     CivMessage.global("§e" + ChatColor.BOLD + "The civilization of " + motherCiv.getName() + " demands its freedom and has started a revolution! It has declared WAR on any civ that owns its old towns.");
/* 213 */     CivMessage.global("§cRED!§a The blood of angry men! §7BLACK!§a The dark of ages past!");
/* 214 */     CivMessage.global("§cRED!§a A world about to dawn! §7BLACK!§a The night that ends at last!");
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disbandtown_cmd()
/*     */     throws CivException
/*     */   {
/* 247 */     validLeaderAdvisor();
/* 248 */     Town town = getNamedTown(1);
/*     */     
/* 250 */     if (town.getMotherCiv() != null) {
/* 251 */       throw new CivException("You cannot disband towns that are currently conquered.");
/*     */     }
/*     */     
/* 254 */     if (town.leaderWantsToDisband) {
/* 255 */       town.leaderWantsToDisband = false;
/* 256 */       CivMessage.send(this.sender, "No longer want to disband.");
/* 257 */       return;
/*     */     }
/*     */     
/* 260 */     town.leaderWantsToDisband = true;
/*     */     
/* 262 */     if ((town.leaderWantsToDisband) && (town.mayorWantsToDisband)) {
/* 263 */       CivMessage.sendCiv(town.getCiv(), "Town " + town.getName() + " is being disbanded by agreement from the civ leader and the mayor");
/* 264 */       town.disband();
/*     */     }
/*     */     
/* 267 */     CivMessage.send(this.sender, "§eWaiting on mayor to type /town disbandtown");
/*     */   }
/*     */   
/*     */   public void top5_cmd() {
/* 271 */     CivMessage.sendHeading(this.sender, "Top 5 Civilizations");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 281 */     synchronized (CivGlobal.civilizationScores) {
/* 282 */       int i = 1;
/* 283 */       for (Integer score : CivGlobal.civilizationScores.descendingKeySet()) {
/* 284 */         CivMessage.send(this.sender, i + ") " + "§6" + ((Civilization)CivGlobal.civilizationScores.get(score)).getName() + "§f" + " - " + score + " points");
/* 285 */         i++;
/* 286 */         if (i > 5) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void dip_cmd()
/*     */   {
/* 295 */     CivDiplomacyCommand cmd = new CivDiplomacyCommand();
/* 296 */     cmd.onCommand(this.sender, null, "dip", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void group_cmd() {
/* 300 */     CivGroupCommand cmd = new CivGroupCommand();
/* 301 */     cmd.onCommand(this.sender, null, "group", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void set_cmd() {
/* 305 */     CivSetCommand cmd = new CivSetCommand();
/* 306 */     cmd.onCommand(this.sender, null, "set", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void time_cmd() throws CivException {
/* 310 */     CivMessage.sendHeading(this.sender, "CivCraft Timers");
/* 311 */     Resident resident = getResident();
/* 312 */     ArrayList<String> out = new ArrayList();
/* 313 */     SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/*     */     
/* 315 */     Calendar cal = Calendar.getInstance();
/* 316 */     cal.setTimeZone(TimeZone.getTimeZone(resident.getTimezone()));
/* 317 */     sdf.setTimeZone(cal.getTimeZone());
/*     */     
/*     */ 
/* 320 */     out.add("§2Current Server Time: §a" + sdf.format(cal.getTime()));
/*     */     
/* 322 */     cal.setTime(CivGlobal.getNextUpkeepDate());
/* 323 */     out.add("§2Next Upkeep: §a" + sdf.format(cal.getTime()));
/*     */     
/* 325 */     cal.setTime(CivGlobal.getNextHourlyTickDate());
/* 326 */     out.add("§2Next Hourly Tick: §a" + sdf.format(cal.getTime()));
/*     */     
/* 328 */     cal.setTime(CivGlobal.getNextRepoTime());
/* 329 */     out.add("§2Next Trade Good Repo: §a" + sdf.format(cal.getTime()));
/*     */     
/* 331 */     if (War.isWarTime()) {
/* 332 */       out.add("§eWarTime is now!");
/* 333 */       cal.setTime(War.getStart());
/* 334 */       out.add("§e    Started: §a" + sdf.format(cal.getTime()));
/*     */       
/* 336 */       cal.setTime(War.getEnd());
/* 337 */       out.add("§e    Ends: §a" + sdf.format(cal.getTime()));
/*     */     } else {
/* 339 */       cal.setTime(War.getNextWarTime());
/* 340 */       out.add("§2Next WarTime: §a" + sdf.format(cal.getTime()));
/*     */     }
/*     */     
/* 343 */     Player player = null;
/*     */     try {
/* 345 */       player = getPlayer();
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */     
/* 349 */     if ((player == null) || (player.hasPermission("civ.admin")) || (player.isOp())) {
/* 350 */       cal.setTime(CivGlobal.getTodaysSpawnRegenDate());
/* 351 */       out.add("§dNext Spawn Regen: §a" + sdf.format(cal.getTime()));
/*     */       
/* 353 */       cal.setTime(CivGlobal.getNextRandomEventTime());
/* 354 */       out.add("§dNext Random Event: §a" + sdf.format(cal.getTime()));
/*     */     }
/*     */     
/* 357 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void gov_cmd() {
/* 361 */     CivGovCommand cmd = new CivGovCommand();
/* 362 */     cmd.onCommand(this.sender, null, "gov", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void research_cmd() {
/* 366 */     CivResearchCommand cmd = new CivResearchCommand();
/* 367 */     cmd.onCommand(this.sender, null, "research", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void list_cmd() throws CivException {
/* 371 */     if (this.args.length < 2) {
/* 372 */       String out = "";
/* 373 */       CivMessage.sendHeading(this.sender, "Civs in the World");
/* 374 */       for (Civilization civ : CivGlobal.getCivs()) {
/* 375 */         out = out + civ.getName() + ", ";
/*     */       }
/*     */       
/* 378 */       CivMessage.send(this.sender, out);
/* 379 */       return;
/*     */     }
/*     */     
/* 382 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 384 */     String out = "";
/* 385 */     CivMessage.sendHeading(this.sender, "Towns in " + this.args[1]);
/*     */     
/* 387 */     for (Town t : civ.getTowns()) {
/* 388 */       out = out + t.getName() + ", ";
/*     */     }
/*     */     
/* 391 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void show_cmd() throws CivException {
/* 395 */     if (this.args.length < 2) {
/* 396 */       throw new CivException("You need to enter the civ name you wish to know about.");
/*     */     }
/*     */     
/* 399 */     Civilization civ = getNamedCiv(1);
/* 400 */     if ((this.sender instanceof Player)) {
/* 401 */       CivInfoCommand.show(this.sender, getResident(), civ);
/*     */     } else {
/* 403 */       CivInfoCommand.show(this.sender, null, civ);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deposit_cmd() throws CivException {
/* 408 */     if (this.args.length < 2) {
/* 409 */       throw new CivException("Enter the amount you want to deposit.");
/*     */     }
/*     */     
/* 412 */     Resident resident = getResident();
/* 413 */     Civilization civ = getSenderCiv();
/*     */     try
/*     */     {
/* 416 */       Double amount = Double.valueOf(this.args[1]);
/* 417 */       if (amount.doubleValue() < 1.0D) {
/* 418 */         throw new CivException("Cannot deposit less than 1");
/*     */       }
/* 420 */       amount = Double.valueOf(Math.floor(amount.doubleValue()));
/*     */       
/* 422 */       civ.depositFromResident(resident, Double.valueOf(this.args[1]));
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 425 */       throw new CivException(this.args[1] + " is not a valid number.");
/*     */     } catch (SQLException e) {
/* 427 */       e.printStackTrace();
/* 428 */       throw new CivException("Internal Database Exception");
/*     */     }
/*     */     
/* 431 */     CivMessage.sendSuccess(this.sender, "Deposited " + this.args[1] + " coins.");
/*     */   }
/*     */   
/*     */   public void withdraw_cmd() throws CivException {
/* 435 */     if (this.args.length < 2) {
/* 436 */       throw new CivException("Enter the amount you want to withdraw.");
/*     */     }
/*     */     
/* 439 */     Civilization civ = getSenderCiv();
/* 440 */     Resident resident = getResident();
/*     */     
/* 442 */     if (!civ.getLeaderGroup().hasMember(resident.getName())) {
/* 443 */       throw new CivException("Only leaders can use this command.");
/*     */     }
/*     */     try
/*     */     {
/* 447 */       Double amount = Double.valueOf(this.args[1]);
/* 448 */       if (amount.doubleValue() < 1.0D) {
/* 449 */         throw new CivException("Cannot withdraw less than 1");
/*     */       }
/* 451 */       amount = Double.valueOf(Math.floor(amount.doubleValue()));
/*     */       
/* 453 */       if (!civ.getTreasury().payTo(resident.getTreasury(), Double.valueOf(this.args[1]).doubleValue())) {
/* 454 */         throw new CivException("The civ does not have that much.");
/*     */       }
/*     */     } catch (NumberFormatException e) {
/* 457 */       throw new CivException(this.args[1] + " is not a valid number.");
/*     */     }
/*     */     
/* 460 */     CivMessage.sendSuccess(this.sender, "Withdrew " + this.args[1] + " coins.");
/*     */   }
/*     */   
/*     */   public void townlist_cmd() throws CivException {
/* 464 */     Civilization civ = getSenderCiv();
/*     */     
/* 466 */     CivMessage.sendHeading(this.sender, civ.getName() + " Town List");
/* 467 */     String out = "";
/* 468 */     for (Town town : civ.getTowns()) {
/* 469 */       out = out + town.getName() + ",";
/*     */     }
/* 471 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException {
/* 475 */     CivInfoCommand cmd = new CivInfoCommand();
/* 476 */     cmd.onCommand(this.sender, null, "info", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 481 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 486 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\civ\CivCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */