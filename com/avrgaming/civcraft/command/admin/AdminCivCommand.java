/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.command.civ.CivInfoCommand;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigGovernment;
/*     */ import com.avrgaming.civcraft.config.ConfigTech;
/*     */ import com.avrgaming.civcraft.endgame.EndConditionDiplomacy;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Relation.Status;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class AdminCivCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  46 */     this.command = "/ad civ";
/*  47 */     this.displayName = "Admin civ";
/*     */     
/*  49 */     this.commands.put("disband", "[civ] - disbands this civilization");
/*  50 */     this.commands.put("addleader", "[civ] [player] - adds this player to the leaders group.");
/*  51 */     this.commands.put("addadviser", "[civ] [player] - adds this player to the advisers group.");
/*  52 */     this.commands.put("rmleader", "[civ] [player] - removes this player from the leaders group.");
/*  53 */     this.commands.put("rmadviser", "[civ] [player] - removes this player from the advisers group.");
/*  54 */     this.commands.put("givetech", "[civ] [tech_id] - gives this civilization this technology.");
/*  55 */     this.commands.put("beakerrate", "[civ] [amount] set this towns's beaker rate to this amount.");
/*  56 */     this.commands.put("toggleadminciv", "[civ] - sets/unsets this civilization to an admin civ. Prevents war.");
/*  57 */     this.commands.put("alltech", "[civ] - gives this civilization every technology.");
/*  58 */     this.commands.put("setrelation", "[civ] [otherCiv] [NEUTRAL|HOSTILE|WAR|PEACE|ALLY] sets the relationship between [civ] and [otherCiv].");
/*  59 */     this.commands.put("info", "[civ] - Processes /civ info command as if you were a member of this civilization.");
/*  60 */     this.commands.put("merge", "[oldciv] [newciv] - Merges oldciv into newciv. oldciv is then destroyed");
/*  61 */     this.commands.put("setgov", "[civ] [government] - Changes this civilization's government immediatly. Removes any anarchy timers.");
/*  62 */     this.commands.put("bankrupt", "[civ] Clear the coins of all towns, and all members of this civ. requires confirmation.");
/*  63 */     this.commands.put("setgov", "[civ] [gov_id] - sets this civ to this government, no anarchy");
/*  64 */     this.commands.put("conquered", "[civ] - Marks this civ as a conquered civ");
/*  65 */     this.commands.put("unconquer", "[civ] - Unmarks this civ as a conquered civ");
/*  66 */     this.commands.put("liberate", "[civ] - Liberates the specified civ if it is conquered.");
/*  67 */     this.commands.put("setvotes", "[civ] [votes] - sets this civ's diplomatic votes to this amount.");
/*  68 */     this.commands.put("rename", "[civ] [new name] - Renames this civ.");
/*     */   }
/*     */   
/*     */   public void liberate_cmd() throws CivException {
/*  72 */     Civilization motherCiv = getNamedCiv(1);
/*     */     
/*     */ 
/*  75 */     for (Town t : CivGlobal.getTowns()) {
/*  76 */       if (t.getMotherCiv() == motherCiv) {
/*  77 */         t.changeCiv(motherCiv);
/*  78 */         t.setMotherCiv(null);
/*  79 */         t.save();
/*     */       }
/*     */     }
/*     */     
/*  83 */     motherCiv.setConquered(false);
/*  84 */     CivGlobal.removeConqueredCiv(motherCiv);
/*  85 */     CivGlobal.addCiv(motherCiv);
/*  86 */     motherCiv.save();
/*  87 */     CivMessage.sendSuccess(this.sender, "Liberated " + motherCiv.getName());
/*     */   }
/*     */   
/*     */   public void rename_cmd() throws CivException, InvalidNameException {
/*  91 */     Civilization civ = getNamedCiv(1);
/*  92 */     String name = getNamedString(2, "Name for new civ.");
/*     */     
/*  94 */     if (this.args.length < 3) {
/*  95 */       throw new CivException("Use underscores for names with spaces.");
/*     */     }
/*     */     
/*  98 */     civ.rename(name);
/*  99 */     CivMessage.sendSuccess(this.sender, "Renamed civ.");
/*     */   }
/*     */   
/*     */   public void setvotes_cmd() throws CivException {
/* 103 */     Civilization civ = getNamedCiv(1);
/* 104 */     Integer votes = getNamedInteger(2);
/* 105 */     EndConditionDiplomacy.setVotes(civ, votes);
/* 106 */     CivMessage.sendSuccess(this.sender, "Set votes for " + civ.getName() + " to " + votes);
/*     */   }
/*     */   
/*     */   public void conquered_cmd() throws CivException {
/* 110 */     Civilization civ = getNamedCiv(1);
/* 111 */     civ.setConquered(true);
/* 112 */     CivGlobal.removeCiv(civ);
/* 113 */     CivGlobal.addConqueredCiv(civ);
/* 114 */     civ.save();
/*     */     
/* 116 */     CivMessage.sendSuccess(this.sender, "civ is now conquered.");
/*     */   }
/*     */   
/*     */   public void unconquer_cmd() throws CivException {
/* 120 */     String conquerCiv = getNamedString(1, "conquered civ");
/*     */     
/* 122 */     Civilization civ = CivGlobal.getConqueredCiv(conquerCiv);
/* 123 */     if (civ == null) {
/* 124 */       civ = CivGlobal.getCiv(conquerCiv);
/*     */     }
/*     */     
/* 127 */     if (civ == null) {
/* 128 */       throw new CivException("No civ called " + conquerCiv);
/*     */     }
/*     */     
/* 131 */     civ.setConquered(false);
/* 132 */     CivGlobal.removeConqueredCiv(civ);
/* 133 */     CivGlobal.addCiv(civ);
/* 134 */     civ.save();
/*     */     
/* 136 */     CivMessage.sendSuccess(this.sender, "Civ is now unconquered.");
/*     */   }
/*     */   
/*     */   public void bankrupt_cmd() throws CivException
/*     */   {
/* 141 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 143 */     if (this.args.length < 3) {
/* 144 */       CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "Are you absolutely sure you want to wipe ALL COINS from ALL RESIDENTS and ALL TOWNS of this civ?");
/* 145 */       CivMessage.send(this.sender, "use /civ bankrupt yes if you do.");
/*     */     }
/*     */     
/* 148 */     civ.getTreasury().setBalance(0.0D);
/*     */     Iterator localIterator2;
/* 150 */     for (Iterator localIterator1 = civ.getTowns().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/* 154 */         localIterator2.hasNext())
/*     */     {
/* 150 */       Town town = (Town)localIterator1.next();
/* 151 */       town.getTreasury().setBalance(0.0D);
/* 152 */       town.save();
/*     */       
/* 154 */       localIterator2 = town.getResidents().iterator(); continue;Resident resident = (Resident)localIterator2.next();
/* 155 */       resident.getTreasury().setBalance(0.0D);
/* 156 */       resident.save();
/*     */     }
/*     */     
/*     */ 
/* 160 */     civ.save();
/* 161 */     CivMessage.sendSuccess(this.sender, "Bankrupted " + civ.getName());
/*     */   }
/*     */   
/*     */   public void setgov_cmd() throws CivException {
/* 165 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 167 */     if (this.args.length < 3) {
/* 168 */       throw new CivException("Enter a government name");
/*     */     }
/*     */     
/* 171 */     ConfigGovernment gov = (ConfigGovernment)CivSettings.governments.get(this.args[2]);
/* 172 */     if (gov == null) {
/* 173 */       throw new CivException("No government with that id.. try gov_monarchy, gov_depostism... etc");
/*     */     }
/*     */     
/* 176 */     String key = "changegov_" + civ.getId();
/* 177 */     CivGlobal.getSessionDB().delete_all(key);
/*     */     
/* 179 */     civ.setGovernment(gov.id);
/* 180 */     CivMessage.global(civ.getName() + " has emerged from anarchy and has adopted " + ((ConfigGovernment)CivSettings.governments.get(gov.id)).displayName);
/* 181 */     CivMessage.sendSuccess(this.sender, "Successfully changed government");
/*     */   }
/*     */   
/*     */   public void merge_cmd() throws CivException
/*     */   {
/* 186 */     Civilization oldciv = getNamedCiv(1);
/* 187 */     Civilization newciv = getNamedCiv(2);
/*     */     
/* 189 */     if (oldciv == newciv) {
/* 190 */       throw new CivException("Cannot merge a civ into itself.");
/*     */     }
/*     */     
/* 193 */     newciv.mergeInCiv(oldciv);
/* 194 */     CivMessage.global("An admin has merged " + oldciv.getName() + " into " + newciv.getName());
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException {
/* 198 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 200 */     CivInfoCommand cmd = new CivInfoCommand();
/* 201 */     cmd.senderCivOverride = civ;
/* 202 */     cmd.onCommand(this.sender, null, "info", stripArgs(this.args, 2));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setmaster_cmd() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setrelation_cmd()
/*     */     throws CivException
/*     */   {
/* 223 */     if (this.args.length < 4) {
/* 224 */       throw new CivException("Usage: [civ] [otherCiv] [NEUTRAL|HOSTILE|WAR|PEACE|ALLY]");
/*     */     }
/*     */     
/* 227 */     Civilization civ = getNamedCiv(1);
/* 228 */     Civilization otherCiv = getNamedCiv(2);
/*     */     
/* 230 */     Relation.Status status = Relation.Status.valueOf(this.args[3].toUpperCase());
/*     */     
/* 232 */     CivGlobal.setRelation(civ, otherCiv, status);
/* 233 */     if (status.equals(Relation.Status.WAR)) {
/* 234 */       CivGlobal.setAggressor(civ, otherCiv, civ);
/* 235 */       CivGlobal.setAggressor(otherCiv, civ, civ);
/*     */     }
/* 237 */     CivMessage.sendSuccess(this.sender, "Set relationship between " + civ.getName() + " and " + otherCiv.getName() + " to " + status.name());
/*     */   }
/*     */   
/*     */   public void alltech_cmd()
/*     */     throws CivException
/*     */   {
/* 243 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 245 */     for (ConfigTech tech : CivSettings.techs.values()) {
/* 246 */       civ.addTech(tech);
/*     */     }
/*     */     
/* 249 */     civ.save();
/*     */     
/* 251 */     CivMessage.sendSuccess(this.sender, "All techs awarded.");
/*     */   }
/*     */   
/*     */   public void toggleadminciv_cmd() throws CivException {
/* 255 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 257 */     civ.setAdminCiv(!civ.isAdminCiv());
/* 258 */     civ.save();
/* 259 */     CivMessage.sendSuccess(this.sender, civ.getName() + " admin civ is now:" + civ.isAdminCiv());
/*     */   }
/*     */   
/*     */   public void beakerrate_cmd() throws CivException {
/* 263 */     Civilization civ = getNamedCiv(1);
/* 264 */     Double amount = getNamedDouble(2);
/*     */     
/* 266 */     civ.setBaseBeakers(amount.doubleValue());
/* 267 */     civ.save();
/*     */     
/* 269 */     CivMessage.sendSuccess(this.sender, "Set " + civ.getName() + " beaker rate to " + amount);
/*     */   }
/*     */   
/*     */   public void givetech_cmd() throws CivException {
/* 273 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 275 */     if (this.args.length < 3) {
/* 276 */       throw new CivException("Enter a tech ID");
/*     */     }
/*     */     
/* 279 */     ConfigTech tech = (ConfigTech)CivSettings.techs.get(this.args[2]);
/* 280 */     if (tech == null) {
/* 281 */       throw new CivException("No tech with ID:" + this.args[2]);
/*     */     }
/*     */     
/* 284 */     if (civ.hasTechnology(tech.id)) {
/* 285 */       throw new CivException("Civ " + civ.getName() + " already has tech id:" + tech.id);
/*     */     }
/*     */     
/* 288 */     civ.addTech(tech);
/* 289 */     civ.save();
/*     */     
/* 291 */     CivMessage.sendSuccess(this.sender, "Added " + tech.name + " to " + civ.getName());
/*     */   }
/*     */   
/*     */   public void rmadviser_cmd() throws CivException
/*     */   {
/* 296 */     Civilization civ = getNamedCiv(1);
/* 297 */     Resident resident = getNamedResident(2);
/*     */     
/* 299 */     if (civ.getAdviserGroup().hasMember(resident)) {
/* 300 */       civ.getAdviserGroup().removeMember(resident);
/* 301 */       civ.save();
/* 302 */       CivMessage.sendSuccess(this.sender, "Removed " + resident.getName() + " to advisers group in " + civ.getName());
/*     */     } else {
/* 304 */       CivMessage.sendError(this.sender, resident.getName() + " is not currently in the advisers group for " + civ.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void rmleader_cmd() throws CivException {
/* 309 */     Civilization civ = getNamedCiv(1);
/* 310 */     Resident resident = getNamedResident(2);
/*     */     
/* 312 */     if (civ.getLeaderGroup().hasMember(resident)) {
/* 313 */       civ.getLeaderGroup().removeMember(resident);
/* 314 */       civ.save();
/* 315 */       CivMessage.sendSuccess(this.sender, "Removed " + resident.getName() + " to leaders group in " + civ.getName());
/*     */     } else {
/* 317 */       CivMessage.sendError(this.sender, resident.getName() + " is not currently in the leaders group for " + civ.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void addadviser_cmd() throws CivException {
/* 322 */     Civilization civ = getNamedCiv(1);
/* 323 */     Resident resident = getNamedResident(2);
/*     */     
/* 325 */     civ.getAdviserGroup().addMember(resident);
/* 326 */     civ.getAdviserGroup().save();
/* 327 */     civ.save();
/*     */     
/* 329 */     CivMessage.sendSuccess(this.sender, "Added " + resident.getName() + " to advisers group in " + civ.getName());
/*     */   }
/*     */   
/*     */   public void addleader_cmd() throws CivException {
/* 333 */     Civilization civ = getNamedCiv(1);
/* 334 */     Resident resident = getNamedResident(2);
/*     */     
/* 336 */     civ.getLeaderGroup().addMember(resident);
/* 337 */     civ.getLeaderGroup().save();
/* 338 */     civ.save();
/*     */     
/* 340 */     CivMessage.sendSuccess(this.sender, "Added " + resident.getName() + " to leaders group in " + civ.getName());
/*     */   }
/*     */   
/*     */   public void disband_cmd() throws CivException {
/* 344 */     Civilization civ = getNamedCiv(1);
/*     */     
/* 346 */     CivMessage.sendCiv(civ, "Your civ is has disbanded by an admin!");
/*     */     try {
/* 348 */       civ.delete();
/*     */     } catch (SQLException e) {
/* 350 */       e.printStackTrace();
/*     */     }
/*     */     
/* 353 */     CivMessage.sendSuccess(this.sender, "Civ disbanded");
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 358 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 363 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminCivCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */