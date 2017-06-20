/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.command.ReportChestsTask;
/*     */ import com.avrgaming.civcraft.command.town.TownInfoCommand;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.randomevents.ConfigRandomEvent;
/*     */ import com.avrgaming.civcraft.randomevents.RandomEvent;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import org.bukkit.Bukkit;
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
/*     */ public class AdminTownCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  54 */     this.command = "/ad town";
/*  55 */     this.displayName = "Admin town";
/*     */     
/*  57 */     this.commands.put("disband", "[town] - disbands this town");
/*  58 */     this.commands.put("claim", "[town] - forcibly claims the plot you stand on for this named town.");
/*  59 */     this.commands.put("unclaim", "forcibly unclaims the plot you stand on.");
/*  60 */     this.commands.put("hammerrate", "[town] [amount] set this town's hammer rate to this amount.");
/*  61 */     this.commands.put("addmayor", "[town] [player] - adds the player as a mayor of this town.");
/*  62 */     this.commands.put("addassistant", "[town] [player] - adds this player as an assistant to this town.");
/*  63 */     this.commands.put("rmmayor", "[town] [player] - remove this player as a mayor from this town.");
/*  64 */     this.commands.put("rmassistant", "[town] [player] - remove this player as an assistant from this town.");
/*  65 */     this.commands.put("tp", "[town] - teleports to this town's town hall");
/*  66 */     this.commands.put("culture", "[town] [amount] - gives this town this amount of culture.");
/*  67 */     this.commands.put("info", "[town] - shows information for this town as-if you were a resident.");
/*  68 */     this.commands.put("setciv", "[town] [civ] - changes this town's civilization to the named civ.");
/*  69 */     this.commands.put("select", "[town] - selects this town as if you were the owner.");
/*  70 */     this.commands.put("claimradius", "[radius] - claims chunks in this radius.");
/*  71 */     this.commands.put("chestreport", "[town] Report on the chests town.");
/*  72 */     this.commands.put("rebuildgroups", "[town] - Remakes town's protected groups if they are not present.");
/*  73 */     this.commands.put("capture", "[winner civ] [loser town] - Captures the named town for this civ.");
/*  74 */     this.commands.put("setmotherciv", "[town] [motherciv] - Changes the mother civ of this town.");
/*  75 */     this.commands.put("sethappy", "[town] [amount] - Sets a magical base happiness for this town.");
/*  76 */     this.commands.put("setunhappy", "[town] [amount] - sets a magical base unhappiness for this town.");
/*  77 */     this.commands.put("event", "[town] [event_id] - Runs the named random event in this town.");
/*  78 */     this.commands.put("rename", "[town] [new_name] - Renames this town.");
/*     */   }
/*     */   
/*     */   public void rename_cmd() throws CivException, InvalidNameException {
/*  82 */     Town town = getNamedTown(1);
/*  83 */     String name = getNamedString(2, "Name for new town.");
/*     */     
/*  85 */     if (this.args.length < 3) {
/*  86 */       throw new CivException("Use underscores for names with spaces.");
/*     */     }
/*     */     
/*  89 */     town.rename(name);
/*  90 */     CivMessage.sendSuccess(this.sender, "Renamed town.");
/*     */   }
/*     */   
/*     */   public void event_cmd() throws CivException {
/*  94 */     Town town = getNamedTown(1);
/*     */     
/*  96 */     if (this.args.length < 3) {
/*  97 */       CivMessage.sendHeading(this.sender, "Available Events");
/*  98 */       String out = "";
/*  99 */       for (ConfigRandomEvent configEvent : CivSettings.randomEvents.values()) {
/* 100 */         out = out + configEvent.id + ",";
/*     */       }
/* 102 */       CivMessage.send(this.sender, out);
/* 103 */       return;
/*     */     }
/*     */     
/* 106 */     ConfigRandomEvent event = (ConfigRandomEvent)CivSettings.randomEvents.get(this.args[2]);
/* 107 */     RandomEvent randEvent = new RandomEvent(event);
/* 108 */     randEvent.start(town);
/* 109 */     CivMessage.sendSuccess(this.sender, "Started event:" + event.name);
/*     */   }
/*     */   
/*     */   public void setunhappy_cmd() throws CivException {
/* 113 */     Town town = getNamedTown(1);
/* 114 */     double happy = getNamedDouble(2).doubleValue();
/*     */     
/* 116 */     town.setBaseUnhappy(happy);
/* 117 */     CivMessage.sendSuccess(this.sender, "Set unhappiness.");
/*     */   }
/*     */   
/*     */   public void sethappy_cmd() throws CivException
/*     */   {
/* 122 */     Town town = getNamedTown(1);
/* 123 */     double happy = getNamedDouble(2).doubleValue();
/*     */     
/* 125 */     town.setBaseHappiness(happy);
/* 126 */     CivMessage.sendSuccess(this.sender, "Set happiness.");
/*     */   }
/*     */   
/*     */   public void setmotherciv_cmd() throws CivException
/*     */   {
/* 131 */     Town town = getNamedTown(1);
/* 132 */     Civilization civ = getNamedCiv(2);
/*     */     
/* 134 */     town.setMotherCiv(civ);
/* 135 */     town.save();
/*     */     
/* 137 */     CivMessage.sendSuccess(this.sender, "Set town " + town.getName() + " to civ " + civ.getName());
/*     */   }
/*     */   
/*     */   public void capture_cmd() throws CivException {
/* 141 */     Civilization civ = getNamedCiv(1);
/* 142 */     Town town = getNamedTown(2);
/*     */     
/* 144 */     town.onDefeat(civ);
/* 145 */     CivMessage.sendSuccess(this.sender, "Captured.");
/*     */   }
/*     */   
/*     */   public void rebuildgroups_cmd() throws CivException {
/* 149 */     Town town = getNamedTown(1);
/*     */     
/* 151 */     if (town.getDefaultGroup() == null) {
/*     */       try
/*     */       {
/* 154 */         PermissionGroup residents = new PermissionGroup(town, "residents");
/* 155 */         town.setDefaultGroup(residents);
/*     */         try {
/* 157 */           residents.saveNow();
/* 158 */           town.saveNow();
/*     */         } catch (SQLException e) {
/* 160 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 165 */         CivMessage.sendSuccess(this.sender, "Created residents group.");
/*     */       }
/*     */       catch (InvalidNameException e1)
/*     */       {
/* 163 */         e1.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 168 */     if (town.getAssistantGroup() == null) {
/*     */       try
/*     */       {
/* 171 */         PermissionGroup assistant = new PermissionGroup(town, "assistants");
/*     */         
/* 173 */         town.setAssistantGroup(assistant);
/*     */         try {
/* 175 */           assistant.saveNow();
/* 176 */           town.saveNow();
/*     */         } catch (SQLException e) {
/* 178 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 184 */         CivMessage.sendSuccess(this.sender, "Created assistants group.");
/*     */       }
/*     */       catch (InvalidNameException e)
/*     */       {
/* 182 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 187 */     if (town.getMayorGroup() == null) {
/*     */       try
/*     */       {
/* 190 */         PermissionGroup mayor = new PermissionGroup(town, "mayors");
/* 191 */         town.setMayorGroup(mayor);
/*     */         try {
/* 193 */           mayor.saveNow();
/* 194 */           town.saveNow();
/*     */         } catch (SQLException e) {
/* 196 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 201 */         CivMessage.sendSuccess(this.sender, "Created mayors groups.");
/*     */       }
/*     */       catch (InvalidNameException e)
/*     */       {
/* 199 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void chestreport_cmd()
/*     */     throws CivException
/*     */   {
/* 207 */     Town town = getNamedTown(1);
/*     */     
/* 209 */     Queue<ChunkCoord> coords = new LinkedList();
/* 210 */     for (TownChunk tc : town.getTownChunks()) {
/* 211 */       ChunkCoord coord = tc.getChunkCoord();
/* 212 */       coords.add(coord);
/*     */     }
/*     */     
/* 215 */     CivMessage.sendHeading(this.sender, "Chests with Goodies in " + town.getName());
/* 216 */     CivMessage.send(this.sender, "Processing (this may take a while)");
/* 217 */     TaskMaster.syncTask(new ReportChestsTask(this.sender, coords), 0L);
/*     */   }
/*     */   
/*     */   public static int claimradius(Town town, Location loc, Integer radius)
/*     */   {
/* 222 */     ChunkCoord coord = new ChunkCoord(loc);
/*     */     
/* 224 */     int count = 0;
/* 225 */     for (int x = -radius.intValue(); x < radius.intValue(); x++) {
/* 226 */       for (int z = -radius.intValue(); z < radius.intValue(); z++) {
/*     */         try {
/* 228 */           ChunkCoord next = new ChunkCoord(coord.getWorldname(), coord.getX() + x, coord.getZ() + z);
/* 229 */           TownChunk.townHallClaim(town, next);
/* 230 */           count++;
/*     */         }
/*     */         catch (CivException localCivException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 237 */     town.save();
/* 238 */     return count;
/*     */   }
/*     */   
/*     */   public void claimradius_cmd() throws CivException {
/* 242 */     Town town = getSelectedTown();
/* 243 */     Integer radius = getNamedInteger(1);
/*     */     
/* 245 */     int count = claimradius(town, getPlayer().getLocation(), radius);
/* 246 */     CivMessage.sendSuccess(this.sender, "Claimed " + count + " chunks");
/*     */   }
/*     */   
/*     */   public void select_cmd() throws CivException {
/* 250 */     Resident resident = getResident();
/* 251 */     Town selectTown = getNamedTown(1);
/*     */     
/* 253 */     if ((resident.getSelectedTown() == null) && 
/* 254 */       (resident.getTown() == selectTown)) {
/* 255 */       throw new CivException("You already have " + selectTown.getName() + " selected.");
/*     */     }
/*     */     
/*     */ 
/* 259 */     if (resident.getSelectedTown() == selectTown) {
/* 260 */       throw new CivException("You already have " + selectTown.getName() + " selected.");
/*     */     }
/*     */     
/* 263 */     resident.setSelectedTown(selectTown);
/* 264 */     CivMessage.sendSuccess(this.sender, "You have selected " + selectTown.getName() + ".");
/*     */   }
/*     */   
/*     */   public void setciv_cmd() throws CivException
/*     */   {
/* 269 */     Town town = getNamedTown(1);
/* 270 */     Civilization civ = getNamedCiv(2);
/*     */     
/* 272 */     if (town.getCiv() == civ) {
/* 273 */       throw new CivException("Town already belongs to civilization " + civ.getName());
/*     */     }
/*     */     
/* 276 */     if (town.isCapitol()) {
/* 277 */       throw new CivException("Cannot move the capitol town.");
/*     */     }
/*     */     
/* 280 */     town.changeCiv(civ);
/* 281 */     CivGlobal.processCulture();
/* 282 */     CivMessage.global("An admin has moved the town of " + town.getName() + " to civilization " + civ.getName());
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException
/*     */   {
/* 287 */     Town town = getNamedTown(1);
/*     */     
/* 289 */     TownInfoCommand cmd = new TownInfoCommand();
/* 290 */     cmd.senderTownOverride = town;
/* 291 */     cmd.senderCivOverride = town.getCiv();
/* 292 */     cmd.onCommand(this.sender, null, "info", stripArgs(this.args, 2));
/*     */   }
/*     */   
/*     */   public void culture_cmd() throws CivException {
/* 296 */     Town town = getNamedTown(1);
/* 297 */     Integer culture = getNamedInteger(2);
/*     */     
/* 299 */     town.addAccumulatedCulture(culture.intValue());
/* 300 */     town.save();
/*     */     
/* 302 */     CivMessage.sendSuccess(this.sender, "Gave " + town.getName() + " " + culture + " culture points.");
/*     */   }
/*     */   
/*     */   public void tp_cmd() throws CivException {
/* 306 */     Town town = getNamedTown(1);
/*     */     
/* 308 */     TownHall townhall = town.getTownHall();
/*     */     
/* 310 */     if ((this.sender instanceof Player)) {
/* 311 */       if ((townhall != null) && (townhall.isComplete())) {
/* 312 */         BlockCoord bcoord = townhall.getRandomRevivePoint();
/* 313 */         ((Player)this.sender).teleport(bcoord.getLocation());
/* 314 */         CivMessage.sendSuccess(this.sender, "Teleported to " + town.getName());
/* 315 */         return;
/*     */       }
/* 317 */       if (town.getTownChunks().size() > 0) {
/* 318 */         ChunkCoord coord = ((TownChunk)town.getTownChunks().iterator().next()).getChunkCoord();
/*     */         
/* 320 */         Location loc = new Location(Bukkit.getWorld(coord.getWorldname()), coord.getX() << 4, 100.0D, coord.getZ() << 4);
/* 321 */         ((Player)this.sender).teleport(loc);
/* 322 */         CivMessage.sendSuccess(this.sender, "Teleported to " + town.getName());
/* 323 */         return;
/*     */       }
/*     */       
/*     */ 
/* 327 */       throw new CivException("Couldn't find a town hall or a town chunk to teleport to.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void rmassistant_cmd()
/*     */     throws CivException
/*     */   {
/* 334 */     Town town = getNamedTown(1);
/* 335 */     Resident resident = getNamedResident(2);
/*     */     
/* 337 */     if (!town.getAssistantGroup().hasMember(resident)) {
/* 338 */       throw new CivException(resident.getName() + " is not in the assistants group in " + town.getName());
/*     */     }
/*     */     
/* 341 */     town.getAssistantGroup().removeMember(resident);
/*     */     try {
/* 343 */       town.getAssistantGroup().saveNow();
/*     */     } catch (SQLException e) {
/* 345 */       e.printStackTrace();
/*     */     }
/*     */     
/* 348 */     CivMessage.sendSuccess(this.sender, "Removed" + resident.getName() + " to assistants group in " + town.getName());
/*     */   }
/*     */   
/*     */   public void rmmayor_cmd() throws CivException
/*     */   {
/* 353 */     Town town = getNamedTown(1);
/* 354 */     Resident resident = getNamedResident(2);
/*     */     
/* 356 */     if (!town.getMayorGroup().hasMember(resident)) {
/* 357 */       throw new CivException(resident.getName() + " is not in the mayors group in " + town.getName());
/*     */     }
/*     */     
/* 360 */     town.getMayorGroup().removeMember(resident);
/*     */     try {
/* 362 */       town.getMayorGroup().saveNow();
/*     */     } catch (SQLException e) {
/* 364 */       e.printStackTrace();
/*     */     }
/*     */     
/* 367 */     CivMessage.sendSuccess(this.sender, "Removed" + resident.getName() + " to mayors group in " + town.getName());
/*     */   }
/*     */   
/*     */   public void addassistant_cmd() throws CivException
/*     */   {
/* 372 */     Town town = getNamedTown(1);
/* 373 */     Resident resident = getNamedResident(2);
/*     */     
/* 375 */     town.getAssistantGroup().addMember(resident);
/*     */     try {
/* 377 */       town.getAssistantGroup().saveNow();
/*     */     } catch (SQLException e) {
/* 379 */       e.printStackTrace();
/*     */     }
/*     */     
/* 382 */     CivMessage.sendSuccess(this.sender, "Added " + resident.getName() + " to assistants group in " + town.getName());
/*     */   }
/*     */   
/*     */   public void addmayor_cmd() throws CivException
/*     */   {
/* 387 */     Town town = getNamedTown(1);
/* 388 */     Resident resident = getNamedResident(2);
/*     */     
/* 390 */     town.getMayorGroup().addMember(resident);
/*     */     try {
/* 392 */       town.getMayorGroup().saveNow();
/*     */     } catch (SQLException e) {
/* 394 */       e.printStackTrace();
/*     */     }
/*     */     
/* 397 */     CivMessage.sendSuccess(this.sender, "Added " + resident.getName() + " to mayors group in " + town.getName());
/*     */   }
/*     */   
/*     */   public void disband_cmd() throws CivException
/*     */   {
/* 402 */     Town town = getNamedTown(1);
/*     */     
/* 404 */     if (town.isCapitol()) {
/* 405 */       throw new CivException("Cannot disband the capitol town, disband the civilization instead.");
/*     */     }
/*     */     
/* 408 */     CivMessage.sendTown(town, "Your town is has disbanded by an admin!");
/*     */     try {
/* 410 */       town.delete();
/*     */     } catch (SQLException e) {
/* 412 */       e.printStackTrace();
/*     */     }
/*     */     
/* 415 */     CivMessage.sendSuccess(this.sender, "Town disbanded");
/*     */   }
/*     */   
/*     */   public void hammerrate_cmd() throws CivException {
/* 419 */     if (this.args.length < 3) {
/* 420 */       throw new CivException("Enter a town name and amount");
/*     */     }
/*     */     
/* 423 */     Town town = getNamedTown(1);
/*     */     try
/*     */     {
/* 426 */       town.setHammerRate(Double.valueOf(this.args[2]).doubleValue());
/* 427 */       CivMessage.sendSuccess(this.sender, "Set " + this.args[1] + " hammer rate to " + this.args[2]);
/*     */     } catch (NumberFormatException e) {
/* 429 */       throw new CivException(this.args[2] + " is not a number.");
/*     */     }
/*     */     
/* 432 */     town.save();
/*     */   }
/*     */   
/*     */   public void unclaim_cmd() throws CivException {
/* 436 */     Town town = getNamedTown(1);
/* 437 */     Player player = getPlayer();
/*     */     
/* 439 */     TownChunk tc = CivGlobal.getTownChunk(player.getLocation());
/* 440 */     if (tc != null)
/*     */     {
/* 442 */       tc.getTown().removeTownChunk(tc);
/* 443 */       CivGlobal.removeTownChunk(tc);
/*     */       try {
/* 445 */         tc.delete();
/*     */       } catch (SQLException e) {
/* 447 */         e.printStackTrace();
/*     */       }
/*     */       
/* 450 */       town.save();
/*     */       
/* 452 */       CivMessage.sendSuccess(player, "Unclaimed plot from " + town.getName());
/*     */     } else {
/* 454 */       CivMessage.sendError(this.sender, "This plot is not owned.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void claim_cmd() throws CivException
/*     */   {
/* 460 */     Town town = getNamedTown(1);
/* 461 */     Player player = getPlayer();
/*     */     
/* 463 */     TownChunk tc = CivGlobal.getTownChunk(player.getLocation());
/* 464 */     if (tc == null) {
/* 465 */       tc = new TownChunk(town, player.getLocation());
/* 466 */       CivGlobal.addTownChunk(tc);
/*     */       try {
/* 468 */         town.addTownChunk(tc);
/*     */       } catch (AlreadyRegisteredException e) {
/* 470 */         e.printStackTrace();
/*     */       }
/*     */       
/* 473 */       tc.save();
/* 474 */       town.save();
/*     */       
/* 476 */       CivMessage.sendSuccess(player, "Claimed plot for " + town.getName());
/*     */     } else {
/* 478 */       CivMessage.sendError(this.sender, "This plot is already owned by town " + town.getName() + " use unclaim first.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 486 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 491 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminTownCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */