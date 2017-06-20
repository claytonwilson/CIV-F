/*     */ package com.avrgaming.civcraft.command;
/*     */ 
/*     */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CommandBase
/*     */   implements CommandExecutor
/*     */ {
/*     */   private static final int MATCH_LIMIT = 5;
/*  52 */   protected HashMap<String, String> commands = new HashMap();
/*     */   
/*     */   protected String[] args;
/*     */   
/*     */   protected CommandSender sender;
/*  57 */   protected String command = "FIXME";
/*  58 */   protected String displayName = "FIXME";
/*  59 */   protected boolean sendUnknownToDefault = false;
/*  60 */   protected DecimalFormat df = new DecimalFormat();
/*     */   
/*  62 */   public Town senderTownOverride = null;
/*  63 */   public Civilization senderCivOverride = null;
/*     */   
/*     */ 
/*     */   public abstract void init();
/*     */   
/*     */   public abstract void doDefaultAction()
/*     */     throws CivException;
/*     */   
/*     */   public abstract void showHelp();
/*     */   
/*     */   public abstract void permissionCheck()
/*     */     throws CivException;
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*     */   {
/*  78 */     init();
/*     */     
/*  80 */     this.args = args;
/*  81 */     this.sender = sender;
/*     */     try
/*     */     {
/*  84 */       permissionCheck();
/*     */     } catch (CivException e1) {
/*  86 */       CivMessage.sendError(sender, e1.getMessage());
/*  87 */       return false;
/*     */     }
/*     */     
/*  90 */     doLogging();
/*     */     
/*  92 */     if (args.length == 0) {
/*     */       try {
/*  94 */         doDefaultAction();
/*     */       } catch (CivException e) {
/*  96 */         CivMessage.sendError(sender, e.getMessage());
/*     */       }
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     if (args[0].equalsIgnoreCase("help")) {
/* 102 */       showHelp();
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     for (String c : this.commands.keySet()) {
/* 107 */       if (c.equalsIgnoreCase(args[0])) {
/*     */         try {
/* 109 */           Method method = getClass().getMethod(args[0].toLowerCase() + "_cmd", new Class[0]);
/*     */           try {
/* 111 */             method.invoke(this, new Object[0]);
/* 112 */             return true;
/*     */           } catch (IllegalAccessException|IllegalArgumentException e) {
/* 114 */             e.printStackTrace();
/* 115 */             CivMessage.sendError(sender, "Internal Command Error.");
/*     */           } catch (InvocationTargetException e) {
/* 117 */             if ((e.getCause() instanceof CivException)) {
/* 118 */               CivMessage.sendError(sender, e.getCause().getMessage());
/*     */             } else {
/* 120 */               CivMessage.sendError(sender, "Internal Command Error.");
/* 121 */               e.getCause().printStackTrace();
/*     */             }
/*     */           }
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
/* 137 */           return true;
/*     */         }
/*     */         catch (NoSuchMethodException e)
/*     */         {
/* 127 */           if (this.sendUnknownToDefault) {
/*     */             try {
/* 129 */               doDefaultAction();
/*     */             } catch (CivException e1) {
/* 131 */               CivMessage.sendError(sender, e.getMessage());
/*     */             }
/* 133 */             return false;
/*     */           }
/* 135 */           CivMessage.sendError(sender, "Unknown method " + args[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 141 */     if (this.sendUnknownToDefault) {
/*     */       try {
/* 143 */         doDefaultAction();
/*     */       } catch (CivException e) {
/* 145 */         CivMessage.sendError(sender, e.getMessage());
/*     */       }
/* 147 */       return false;
/*     */     }
/*     */     
/* 150 */     CivMessage.sendError(sender, "Unknown command " + args[0]);
/* 151 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void doLogging() {}
/*     */   
/*     */   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
/*     */   {
/* 159 */     List<String> al = new ArrayList();
/* 160 */     al.add("sub1");
/* 161 */     al.add("barg");
/* 162 */     al.add("borg");
/* 163 */     return al;
/*     */   }
/*     */   
/*     */   public void showBasicHelp() {
/* 167 */     CivMessage.sendHeading(this.sender, this.displayName + " Command Help");
/* 168 */     for (String c : this.commands.keySet()) {
/* 169 */       String info = (String)this.commands.get(c);
/*     */       
/* 171 */       info = info.replace("[", "§e[");
/* 172 */       info = info.replace("]", "]§7");
/* 173 */       info = info.replace("(", "§e(");
/* 174 */       info = info.replace(")", ")§7");
/*     */       
/* 176 */       CivMessage.send(this.sender, "§d" + this.command + " " + c + "§7" + " " + info);
/*     */     }
/*     */   }
/*     */   
/*     */   public Resident getResident() throws CivException {
/* 181 */     Player player = getPlayer();
/* 182 */     Resident res = CivGlobal.getResident(player);
/* 183 */     if (res == null) {
/* 184 */       throw new CivException("Resident " + player.getName() + " could not be found.");
/*     */     }
/* 186 */     return res;
/*     */   }
/*     */   
/*     */   public Player getPlayer() throws CivException {
/* 190 */     if ((this.sender instanceof Player)) {
/* 191 */       return (Player)this.sender;
/*     */     }
/* 193 */     throw new CivException("Only players can do this.");
/*     */   }
/*     */   
/*     */   public Town getSelectedTown() throws CivException {
/* 197 */     if (this.senderTownOverride != null) {
/* 198 */       return this.senderTownOverride;
/*     */     }
/*     */     
/* 201 */     if ((this.sender instanceof Player)) {
/* 202 */       Player player = (Player)this.sender;
/* 203 */       Resident res = CivGlobal.getResident(player);
/* 204 */       if ((res != null) && (res.getTown() != null))
/*     */       {
/* 206 */         if (res.getSelectedTown() != null) {
/*     */           try {
/* 208 */             res.getSelectedTown().validateResidentSelect(res);
/*     */           } catch (CivException e) {
/* 210 */             CivMessage.send(player, "§eYou can no longer use the selected town " + res.getSelectedTown().getName() + ", switched back to " + res.getTown().getName());
/* 211 */             res.setSelectedTown(res.getTown());
/* 212 */             return res.getTown();
/*     */           }
/*     */           
/* 215 */           return res.getSelectedTown();
/*     */         }
/* 217 */         return res.getTown();
/*     */       }
/*     */     }
/*     */     
/* 221 */     throw new CivException("You are not part of a town.");
/*     */   }
/*     */   
/*     */   public TownChunk getStandingTownChunk() throws CivException {
/* 225 */     Player player = getPlayer();
/*     */     
/* 227 */     TownChunk tc = CivGlobal.getTownChunk(player.getLocation());
/* 228 */     if (tc == null) {
/* 229 */       throw new CivException("This plot is not owned.");
/*     */     }
/* 231 */     return tc;
/*     */   }
/*     */   
/*     */   protected String[] stripArgs(String[] someArgs, int amount) {
/* 235 */     if (amount >= someArgs.length) {
/* 236 */       return new String[0];
/*     */     }
/*     */     
/*     */ 
/* 240 */     String[] argsLeft = new String[someArgs.length - amount];
/* 241 */     for (int i = 0; i < argsLeft.length; i++) {
/* 242 */       argsLeft[i] = someArgs[(i + amount)];
/*     */     }
/*     */     
/* 245 */     return argsLeft;
/*     */   }
/*     */   
/*     */   protected String combineArgs(String[] someArgs) {
/* 249 */     String combined = "";
/* 250 */     String[] arrayOfString; int j = (arrayOfString = someArgs).length; for (int i = 0; i < j; i++) { String str = arrayOfString[i];
/* 251 */       combined = combined + str + " ";
/*     */     }
/* 253 */     combined = combined.trim();
/* 254 */     return combined;
/*     */   }
/*     */   
/*     */   public void validMayor() throws CivException {
/* 258 */     Player player = getPlayer();
/* 259 */     Town town = getSelectedTown();
/*     */     
/* 261 */     if (!town.playerIsInGroupName("mayors", player)) {
/* 262 */       throw new CivException("Only mayors can use this command.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void validMayorAssistantLeader() throws CivException
/*     */   {
/* 268 */     Resident resident = getResident();
/* 269 */     Town town = getSelectedTown();
/*     */     
/*     */     Civilization civ;
/*     */     
/*     */     Civilization civ;
/*     */     
/* 275 */     if (town.getMotherCiv() != null) {
/* 276 */       civ = town.getMotherCiv();
/*     */     } else {
/* 278 */       civ = getSenderCiv();
/*     */     }
/*     */     
/* 281 */     if ((town.getMayorGroup() == null) || (town.getAssistantGroup() == null) || 
/* 282 */       (civ.getLeaderGroup() == null)) {
/* 283 */       throw new CivException("ERROR: This town(" + town.getName() + ") or civ(" + civ.getName() + ") is missing a special group. Please contact and admin.");
/*     */     }
/*     */     
/* 286 */     if ((!town.getMayorGroup().hasMember(resident)) && (!town.getAssistantGroup().hasMember(resident)) && 
/* 287 */       (!civ.getLeaderGroup().hasMember(resident))) {
/* 288 */       throw new CivException("Only mayors, assistants and civ leaders of the mother civilization can use this command.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void validLeaderAdvisor() throws CivException {
/* 293 */     Resident res = getResident();
/* 294 */     Civilization civ = getSenderCiv();
/*     */     
/*     */ 
/* 297 */     if ((!civ.getLeaderGroup().hasMember(res)) && (!civ.getAdviserGroup().hasMember(res))) {
/* 298 */       throw new CivException("Only leaders and advisers can use this command.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void validLeader() throws CivException {
/* 303 */     Resident res = getResident();
/* 304 */     Civilization civ = getSenderCiv();
/*     */     
/* 306 */     if (!civ.getLeaderGroup().hasMember(res)) {
/* 307 */       throw new CivException("Only leaders can use this command.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void validPlotOwner() throws CivException {
/* 312 */     Resident resident = getResident();
/* 313 */     TownChunk tc = getStandingTownChunk();
/*     */     
/* 315 */     if (tc.perms.getOwner() == null) {
/* 316 */       validMayorAssistantLeader();
/* 317 */       if (tc.getTown() != resident.getTown()) {
/* 318 */         throw new CivException("You cannot manage a plot not in your town.");
/*     */       }
/*     */     }
/* 321 */     else if (resident != tc.perms.getOwner()) {
/* 322 */       throw new CivException("You are not the owner of this plot.");
/*     */     }
/*     */   }
/*     */   
/*     */   public Civilization getSenderCiv()
/*     */     throws CivException
/*     */   {
/* 329 */     if (this.senderCivOverride != null) {
/* 330 */       return this.senderCivOverride;
/*     */     }
/*     */     
/* 333 */     Resident resident = getResident();
/*     */     
/* 335 */     if (resident.getTown() == null) {
/* 336 */       throw new CivException("You are not a citizen of a civilization.");
/*     */     }
/*     */     
/* 339 */     if (resident.getTown().getCiv() == null)
/*     */     {
/* 341 */       throw new CivException("You are not a citizen of a civilization.");
/*     */     }
/*     */     
/* 344 */     return resident.getTown().getCiv();
/*     */   }
/*     */   
/*     */   protected Double getNamedDouble(int index) throws CivException {
/* 348 */     if (this.args.length < index + 1) {
/* 349 */       throw new CivException("Enter a number.");
/*     */     }
/*     */     try
/*     */     {
/* 353 */       return Double.valueOf(this.args[index]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 356 */       throw new CivException(this.args[index] + " is not a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected Integer getNamedInteger(int index) throws CivException
/*     */   {
/* 362 */     if (this.args.length < index + 1) {
/* 363 */       throw new CivException("Enter a number.");
/*     */     }
/*     */     try
/*     */     {
/* 367 */       return Integer.valueOf(this.args[index]);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 370 */       throw new CivException(this.args[index] + " is not whole a number.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected Resident getNamedResident(int index) throws CivException
/*     */   {
/* 376 */     if (this.args.length < index + 1) {
/* 377 */       throw new CivException("Enter a resident name.");
/*     */     }
/*     */     
/* 380 */     String name = this.args[index].toLowerCase();
/* 381 */     name = name.replace("%", "(\\w*)");
/*     */     
/* 383 */     ArrayList<Resident> potentialMatches = new ArrayList();
/* 384 */     String str; for (Resident resident : CivGlobal.getResidents()) {
/* 385 */       str = resident.getName().toLowerCase();
/*     */       try {
/* 387 */         if (str.matches(name)) {
/* 388 */           potentialMatches.add(resident);
/*     */         }
/*     */       } catch (Exception e) {
/* 391 */         throw new CivException("Invalid pattern.");
/*     */       }
/*     */       
/* 394 */       if (potentialMatches.size() > 5) {
/* 395 */         throw new CivException("Too many potential matches. Refine your search.");
/*     */       }
/*     */     }
/*     */     
/* 399 */     if (potentialMatches.size() == 0) {
/* 400 */       throw new CivException("No resident matching that name.");
/*     */     }
/*     */     
/* 403 */     if (potentialMatches.size() != 1) {
/* 404 */       CivMessage.send(this.sender, "§d" + ChatColor.UNDERLINE + "Potential Matches");
/* 405 */       CivMessage.send(this.sender, " ");
/* 406 */       String out = "";
/* 407 */       for (Resident resident : potentialMatches) {
/* 408 */         out = out + resident.getName() + ", ";
/*     */       }
/*     */       
/* 411 */       CivMessage.send(this.sender, "§b" + ChatColor.ITALIC + out);
/* 412 */       throw new CivException("More than one resident matches, please clarify.");
/*     */     }
/*     */     
/* 415 */     return (Resident)potentialMatches.get(0);
/*     */   }
/*     */   
/*     */   protected Civilization getNamedCiv(int index) throws CivException {
/* 419 */     if (this.args.length < index + 1) {
/* 420 */       throw new CivException("Enter a civ name.");
/*     */     }
/*     */     
/* 423 */     String name = this.args[index].toLowerCase();
/* 424 */     name = name.replace("%", "(\\w*)");
/*     */     
/* 426 */     ArrayList<Civilization> potentialMatches = new ArrayList();
/* 427 */     String str; for (Civilization civ : CivGlobal.getCivs()) {
/* 428 */       str = civ.getName().toLowerCase();
/*     */       try {
/* 430 */         if (str.matches(name)) {
/* 431 */           potentialMatches.add(civ);
/*     */         }
/*     */       } catch (Exception e) {
/* 434 */         throw new CivException("Invalid pattern.");
/*     */       }
/*     */       
/* 437 */       if (potentialMatches.size() > 5) {
/* 438 */         throw new CivException("Too many potential matches. Refine your search.");
/*     */       }
/*     */     }
/*     */     
/* 442 */     if (potentialMatches.size() == 0) {
/* 443 */       throw new CivException("No civ matching the name '" + this.args[index] + "'");
/*     */     }
/*     */     
/* 446 */     if (potentialMatches.size() != 1) {
/* 447 */       CivMessage.send(this.sender, "§d" + ChatColor.UNDERLINE + "Potential Matches");
/* 448 */       CivMessage.send(this.sender, " ");
/* 449 */       String out = "";
/* 450 */       for (Civilization civ : potentialMatches) {
/* 451 */         out = out + civ.getName() + ", ";
/*     */       }
/*     */       
/* 454 */       CivMessage.send(this.sender, "§b" + ChatColor.ITALIC + out);
/* 455 */       throw new CivException("More than one civ matches, please clarify.");
/*     */     }
/*     */     
/* 458 */     return (Civilization)potentialMatches.get(0);
/*     */   }
/*     */   
/*     */   protected Civilization getNamedCapturedCiv(int index) throws CivException {
/* 462 */     if (this.args.length < index + 1) {
/* 463 */       throw new CivException("Enter a civ name.");
/*     */     }
/*     */     
/* 466 */     String name = this.args[index].toLowerCase();
/* 467 */     name = name.replace("%", "(\\w*)");
/*     */     
/* 469 */     ArrayList<Civilization> potentialMatches = new ArrayList();
/* 470 */     String str; for (Civilization civ : CivGlobal.getConqueredCivs()) {
/* 471 */       str = civ.getName().toLowerCase();
/*     */       try {
/* 473 */         if (str.matches(name)) {
/* 474 */           potentialMatches.add(civ);
/*     */         }
/*     */       } catch (Exception e) {
/* 477 */         throw new CivException("Invalid pattern.");
/*     */       }
/*     */       
/* 480 */       if (potentialMatches.size() > 5) {
/* 481 */         throw new CivException("Too many potential matches. Refine your search.");
/*     */       }
/*     */     }
/*     */     
/* 485 */     if (potentialMatches.size() == 0) {
/* 486 */       throw new CivException("No civ matching the name '" + this.args[index] + "'");
/*     */     }
/*     */     
/* 489 */     if (potentialMatches.size() != 1) {
/* 490 */       CivMessage.send(this.sender, "§d" + ChatColor.UNDERLINE + "Potential Matches");
/* 491 */       CivMessage.send(this.sender, " ");
/* 492 */       String out = "";
/* 493 */       for (Civilization civ : potentialMatches) {
/* 494 */         out = out + civ.getName() + ", ";
/*     */       }
/*     */       
/* 497 */       CivMessage.send(this.sender, "§b" + ChatColor.ITALIC + out);
/* 498 */       throw new CivException("More than one civ matches, please clarify.");
/*     */     }
/*     */     
/* 501 */     return (Civilization)potentialMatches.get(0);
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
/*     */   protected Town getNamedTown(int index)
/*     */     throws CivException
/*     */   {
/* 517 */     if (this.args.length < index + 1) {
/* 518 */       throw new CivException("Enter a town name.");
/*     */     }
/*     */     
/* 521 */     String name = this.args[index].toLowerCase();
/* 522 */     name = name.replace("%", "(\\w*)");
/*     */     
/* 524 */     ArrayList<Town> potentialMatches = new ArrayList();
/* 525 */     String str; for (Town town : CivGlobal.getTowns()) {
/* 526 */       str = town.getName().toLowerCase();
/*     */       try {
/* 528 */         if (str.matches(name)) {
/* 529 */           potentialMatches.add(town);
/*     */         }
/*     */       } catch (Exception e) {
/* 532 */         throw new CivException("Invalid pattern.");
/*     */       }
/*     */       
/* 535 */       if (potentialMatches.size() > 5) {
/* 536 */         throw new CivException("Too many potential matches. Refine your search.");
/*     */       }
/*     */     }
/*     */     
/* 540 */     if (potentialMatches.size() == 0) {
/* 541 */       throw new CivException("No town matching that name.");
/*     */     }
/*     */     
/* 544 */     if (potentialMatches.size() != 1) {
/* 545 */       CivMessage.send(this.sender, "§d" + ChatColor.UNDERLINE + "Potential Matches");
/* 546 */       CivMessage.send(this.sender, " ");
/* 547 */       String out = "";
/* 548 */       for (Town town : potentialMatches) {
/* 549 */         out = out + town.getName() + ", ";
/*     */       }
/*     */       
/* 552 */       CivMessage.send(this.sender, "§b" + ChatColor.ITALIC + out);
/* 553 */       throw new CivException("More than one town matches, please clarify.");
/*     */     }
/*     */     
/* 556 */     return (Town)potentialMatches.get(0);
/*     */   }
/*     */   
/*     */   public String getNamedString(int index, String message) throws CivException {
/* 560 */     if (this.args.length < index + 1) {
/* 561 */       throw new CivException(message);
/*     */     }
/*     */     
/* 564 */     return this.args[index];
/*     */   }
/*     */   
/*     */   protected OfflinePlayer getNamedOfflinePlayer(int index) throws CivException {
/* 568 */     if (this.args.length < index + 1) {
/* 569 */       throw new CivException("Enter a player name");
/*     */     }
/*     */     
/* 572 */     OfflinePlayer offplayer = Bukkit.getOfflinePlayer(this.args[index]);
/* 573 */     if (offplayer == null) {
/* 574 */       throw new CivException("No player named:" + this.args[index]);
/*     */     }
/*     */     
/* 577 */     return offplayer;
/*     */   }
/*     */   
/*     */   public String makeInfoString(HashMap<String, String> kvs, String lowColor, String highColor)
/*     */   {
/* 582 */     String out = "";
/* 583 */     for (String key : kvs.keySet()) {
/* 584 */       out = out + lowColor + key + ": " + highColor + (String)kvs.get(key) + " ";
/*     */     }
/*     */     
/* 587 */     return out;
/*     */   }
/*     */   
/*     */   protected PermissionGroup getNamedPermissionGroup(Town town, int index) throws CivException {
/* 591 */     if (this.args.length < index + 1) {
/* 592 */       throw new CivException("Enter a group name");
/*     */     }
/*     */     
/* 595 */     PermissionGroup grp = CivGlobal.getPermissionGroupFromName(town, this.args[index]);
/* 596 */     if (grp == null) {
/* 597 */       throw new CivException("No group named:" + this.args[index] + " in town " + town.getName());
/*     */     }
/*     */     
/* 600 */     return grp;
/*     */   }
/*     */   
/*     */   protected void validCampOwner() throws CivException {
/* 604 */     Resident resident = getResident();
/*     */     
/* 606 */     if (!resident.hasCamp()) {
/* 607 */       throw new CivException("You are not currently in a camp.");
/*     */     }
/*     */     
/* 610 */     if (resident.getCamp().getOwner() != resident) {
/* 611 */       throw new CivException("Only the owner of the camp(" + resident.getCamp().getOwnerName() + ") is allowed to do this.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected Camp getCurrentCamp() throws CivException {
/* 616 */     Resident resident = getResident();
/*     */     
/* 618 */     if (!resident.hasCamp()) {
/* 619 */       throw new CivException("You are not currently in a camp.");
/*     */     }
/*     */     
/* 622 */     return resident.getCamp();
/*     */   }
/*     */   
/*     */   protected Camp getNamedCamp(int index) throws CivException {
/* 626 */     if (this.args.length < index + 1) {
/* 627 */       throw new CivException("Enter a camp name.");
/*     */     }
/*     */     
/* 630 */     String name = this.args[index].toLowerCase();
/* 631 */     name = name.replace("%", "(\\w*)");
/*     */     
/* 633 */     ArrayList<Camp> potentialMatches = new ArrayList();
/* 634 */     String str; for (Camp camp : CivGlobal.getCamps()) {
/* 635 */       str = camp.getName().toLowerCase();
/*     */       try {
/* 637 */         if (str.matches(name)) {
/* 638 */           potentialMatches.add(camp);
/*     */         }
/*     */       } catch (Exception e) {
/* 641 */         throw new CivException("Invalid pattern.");
/*     */       }
/*     */       
/* 644 */       if (potentialMatches.size() > 5) {
/* 645 */         throw new CivException("Too many potential matches. Refine your search.");
/*     */       }
/*     */     }
/*     */     
/* 649 */     if (potentialMatches.size() == 0) {
/* 650 */       throw new CivException("No camp matching that name.");
/*     */     }
/*     */     
/*     */ 
/* 654 */     if (potentialMatches.size() != 1) {
/* 655 */       CivMessage.send(this.sender, "§d" + ChatColor.UNDERLINE + "Potential Matches");
/* 656 */       CivMessage.send(this.sender, " ");
/* 657 */       String out = "";
/* 658 */       for (Camp camp : potentialMatches) {
/* 659 */         out = out + camp.getName() + ", ";
/*     */       }
/*     */       
/* 662 */       CivMessage.send(this.sender, "§b" + ChatColor.ITALIC + out);
/* 663 */       throw new CivException("More than one camp matches, please clarify.");
/*     */     }
/*     */     
/* 666 */     return (Camp)potentialMatches.get(0);
/*     */   }
/*     */   
/*     */   protected ArenaTeam getNamedTeam(int index) throws CivException {
/* 670 */     if (this.args.length < index + 1) {
/* 671 */       throw new CivException("Enter a team name.");
/*     */     }
/*     */     
/* 674 */     String name = this.args[index].toLowerCase();
/* 675 */     name = name.replace("%", "(\\w*)");
/*     */     
/* 677 */     ArrayList<ArenaTeam> potentialMatches = new ArrayList();
/* 678 */     String str; for (ArenaTeam team : ArenaTeam.arenaTeams.values()) {
/* 679 */       str = team.getName().toLowerCase();
/*     */       try {
/* 681 */         if (str.matches(name)) {
/* 682 */           potentialMatches.add(team);
/*     */         }
/*     */       } catch (Exception e) {
/* 685 */         throw new CivException("Invalid pattern.");
/*     */       }
/*     */       
/* 688 */       if (potentialMatches.size() > 5) {
/* 689 */         throw new CivException("Too many potential matches. Refine your search.");
/*     */       }
/*     */     }
/*     */     
/* 693 */     if (potentialMatches.size() == 0) {
/* 694 */       throw new CivException("No team matching that name.");
/*     */     }
/*     */     
/* 697 */     if (potentialMatches.size() != 1) {
/* 698 */       CivMessage.send(this.sender, "§d" + ChatColor.UNDERLINE + "Potential Matches");
/* 699 */       CivMessage.send(this.sender, " ");
/* 700 */       String out = "";
/* 701 */       for (ArenaTeam team : potentialMatches) {
/* 702 */         out = out + team.getName() + ", ";
/*     */       }
/*     */       
/* 705 */       CivMessage.send(this.sender, "§b" + ChatColor.ITALIC + out);
/* 706 */       throw new CivException("More than one team matches, please clarify.");
/*     */     }
/*     */     
/* 709 */     return (ArenaTeam)potentialMatches.get(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\CommandBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */