/*     */ package com.avrgaming.civcraft.command.town;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.config.ConfigCultureBiomeInfo;
/*     */ import com.avrgaming.civcraft.config.ConfigCultureLevel;
/*     */ import com.avrgaming.civcraft.config.ConfigPerk;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*     */ import com.avrgaming.civcraft.questions.JoinTownResponse;
/*     */ import com.avrgaming.civcraft.structure.Capitol;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import com.avrgaming.global.perks.Perk;
/*     */ import com.avrgaming.global.perks.components.CustomTemplate;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.TreeMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Biome;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class TownCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public static final long INVITE_TIMEOUT = 30000L;
/*     */   
/*     */   public void init()
/*     */   {
/*  68 */     this.command = "/town";
/*  69 */     this.displayName = "Town";
/*     */     
/*  71 */     this.commands.put("claim", "Claim the plot you are standing in for this town.");
/*  72 */     this.commands.put("unclaim", "Unclaim the plot you are standing on, no refunds.");
/*  73 */     this.commands.put("group", "Manage town permission groups.");
/*  74 */     this.commands.put("upgrade", "Manage town upgrades.");
/*  75 */     this.commands.put("info", "Show information about this town.");
/*  76 */     this.commands.put("add", "[name] - invites resident to town.");
/*  77 */     this.commands.put("members", "Show a list of members in this town.");
/*  78 */     this.commands.put("deposit", "[amount] - deposits this amount into the town's treasury.");
/*  79 */     this.commands.put("withdraw", "[amount] - withdraws this amount from the town's treasury.");
/*  80 */     this.commands.put("set", "Change various town properties.");
/*  81 */     this.commands.put("leave", "leaves the town you are currently in.");
/*  82 */     this.commands.put("show", "[name] show info for town of this name.");
/*  83 */     this.commands.put("evict", "[name] - evicts the resident named from town");
/*  84 */     this.commands.put("list", "shows a list of all towns in the world.");
/*  85 */     this.commands.put("reset", "Resets certain structures, action depends on structure.");
/*  86 */     this.commands.put("top5", "Shows the top 5 towns in the world.");
/*  87 */     this.commands.put("disbandtown", "Disbands this town, requres leader to type disbandtown as well.");
/*  88 */     this.commands.put("outlaw", "Manage town outlaws.");
/*  89 */     this.commands.put("leavegroup", "[town] [group] - Leaves the group in [town] named [group]");
/*  90 */     this.commands.put("select", "[town] - Switches your control to this town, if you have the proper permissions.");
/*     */     
/*  92 */     this.commands.put("capitulate", " gives this town over to the currently owner civ. It will no longer remember its native civilization and will not revolt.");
/*  93 */     this.commands.put("survey", "Surveys the land, estimates what kinds of bonuses you would get from building here.");
/*  94 */     this.commands.put("templates", "Displays all templates bound to this town.");
/*  95 */     this.commands.put("event", "Displays information about the current random event going down.");
/*  96 */     this.commands.put("claimmayor", "claim yourself as mayor of this town. All current mayors must be inactive.");
/*  97 */     this.commands.put("movestructure", "[coord] [town] moves the structure specified by the coord to the specfied town.");
/*  98 */     this.commands.put("enablestructure", "[coord] attempts to enable the specified structure if its currently disabled.");
/*     */   }
/*     */   
/*     */   public void enablestructure_cmd() throws CivException {
/* 102 */     Town town = getSelectedTown();
/* 103 */     Resident resident = getResident();
/* 104 */     String coordString = getNamedString(1, "Coordinate of structure. Example: world,555,65,444");
/*     */     try
/*     */     {
/* 107 */       struct = CivGlobal.getStructure(new BlockCoord(coordString));
/*     */     } catch (Exception e) { Structure struct;
/* 109 */       throw new CivException("Invalid structure coordinate. Example: world,555,65,444");
/*     */     }
/*     */     Structure struct;
/* 112 */     if (War.isWarTime()) {
/* 113 */       throw new CivException("Cannot move structures during war time.");
/*     */     }
/*     */     
/* 116 */     if (struct == null) {
/* 117 */       throw new CivException("Structure at:" + coordString + " is not found.");
/*     */     }
/*     */     
/* 120 */     if (!resident.getCiv().getLeaderGroup().hasMember(resident)) {
/* 121 */       throw new CivException("You must be the civ's leader in order to do this.");
/*     */     }
/*     */     
/* 124 */     if (!town.isStructureAddable(struct)) {
/* 125 */       throw new CivException("Structure still puts town over limits, cannot be re-enabled.");
/*     */     }
/*     */     
/*     */ 
/* 129 */     town.removeStructure(struct);
/* 130 */     town.addStructure(struct);
/* 131 */     CivMessage.sendSuccess(this.sender, "Re-enabled structure.");
/*     */   }
/*     */   
/*     */   public void movestructure_cmd() throws CivException {
/* 135 */     Town town = getSelectedTown();
/* 136 */     Resident resident = getResident();
/* 137 */     String coordString = getNamedString(1, "Coordinate of structure. Example: world,555,65,444");
/* 138 */     Town targetTown = getNamedTown(2);
/*     */     
/*     */     try
/*     */     {
/* 142 */       struct = CivGlobal.getStructure(new BlockCoord(coordString));
/*     */     } catch (Exception e) { Structure struct;
/* 144 */       throw new CivException("Invalid structure coordinate. Example: world,555,65,444");
/*     */     }
/*     */     Structure struct;
/* 147 */     if (((struct instanceof TownHall)) || ((struct instanceof Capitol))) {
/* 148 */       throw new CivException("Cannot move town halls or capitols.");
/*     */     }
/*     */     
/* 151 */     if (War.isWarTime()) {
/* 152 */       throw new CivException("Cannot move structures during war time.");
/*     */     }
/*     */     
/* 155 */     if (struct == null) {
/* 156 */       throw new CivException("Structure at:" + coordString + " is not found.");
/*     */     }
/*     */     
/* 159 */     if (!resident.getCiv().getLeaderGroup().hasMember(resident)) {
/* 160 */       throw new CivException("You must be the civ's leader in order to do this.");
/*     */     }
/*     */     
/* 163 */     if (town.getCiv() != targetTown.getCiv()) {
/* 164 */       throw new CivException("You can only move structures between towns in your own civ.");
/*     */     }
/*     */     
/* 167 */     town.removeStructure(struct);
/* 168 */     targetTown.addStructure(struct);
/* 169 */     struct.setTown(targetTown);
/* 170 */     struct.save();
/*     */     
/* 172 */     CivMessage.sendSuccess(this.sender, "Moved structure " + coordString + " to town " + targetTown.getName());
/*     */   }
/*     */   
/*     */   public void claimmayor_cmd() throws CivException {
/* 176 */     Town town = getSelectedTown();
/* 177 */     Resident resident = getResident();
/*     */     
/* 179 */     if (resident.getTown() != town) {
/* 180 */       throw new CivException("You can only claim mayorship in the town you are in. Use /town select to select your home town.");
/*     */     }
/*     */     
/* 183 */     if (!town.areMayorsInactive()) {
/* 184 */       throw new CivException("At least one mayor is not inactive in this town. Cannot claim mayorship.");
/*     */     }
/*     */     
/* 187 */     town.getMayorGroup().addMember(resident);
/* 188 */     town.getMayorGroup().save();
/* 189 */     CivMessage.sendSuccess(this.sender, "You are now a mayor in " + town.getName());
/* 190 */     CivMessage.sendTown(town, resident.getName() + " has assumed control of the town due to inactive mayorship.");
/*     */   }
/*     */   
/*     */   public void event_cmd() throws CivException {
/* 194 */     TownEventCommand cmd = new TownEventCommand();
/* 195 */     cmd.onCommand(this.sender, null, "event", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void templates_cmd() throws CivException {
/* 199 */     Player player = getPlayer();
/* 200 */     Town town = getSelectedTown();
/* 201 */     Inventory inv = Bukkit.getServer().createInventory(player, 54, town.getName() + " Perks");
/*     */     Iterator localIterator2;
/* 203 */     for (Iterator localIterator1 = CivSettings.structures.values().iterator(); localIterator1.hasNext(); 
/* 204 */         localIterator2.hasNext())
/*     */     {
/* 203 */       ConfigBuildableInfo info = (ConfigBuildableInfo)localIterator1.next();
/* 204 */       localIterator2 = CustomTemplate.getTemplatePerksForBuildable(town, info.template_base_name).iterator(); continue;Perk p = (Perk)localIterator2.next();
/*     */       
/* 206 */       ItemStack stack = LoreGuiItem.build(p.configPerk.display_name, 
/* 207 */         p.configPerk.type_id.intValue(), 
/* 208 */         p.configPerk.data.intValue(), new String[] {
/* 209 */         "§8Provided by: §b" + p.provider });
/* 210 */       inv.addItem(new ItemStack[] { stack });
/*     */     }
/*     */     
/*     */ 
/* 214 */     player.openInventory(inv);
/*     */   }
/*     */   
/*     */   public static ArrayList<String> survey(Location loc) {
/* 218 */     ChunkCoord start = new ChunkCoord(loc);
/* 219 */     ConfigCultureLevel lvl = (ConfigCultureLevel)CivSettings.cultureLevels.get(Integer.valueOf(1));
/*     */     
/* 221 */     ArrayList<String> outList = new ArrayList();
/*     */     
/* 223 */     Queue<ChunkCoord> closedSet = new LinkedList();
/* 224 */     Queue<ChunkCoord> openSet = new LinkedList();
/* 225 */     openSet.add(start);
/*     */     
/*     */ 
/* 228 */     while (!openSet.isEmpty()) {
/* 229 */       ChunkCoord node = (ChunkCoord)openSet.poll();
/*     */       
/* 231 */       if (!closedSet.contains(node))
/*     */       {
/*     */ 
/*     */ 
/* 235 */         if (node.manhattanDistance(start) <= lvl.chunks)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 240 */           closedSet.add(node);
/*     */           
/*     */ 
/* 243 */           int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/* 244 */           for (int i = 0; i < 4; i++) {
/* 245 */             ChunkCoord nextCoord = new ChunkCoord(node.getWorldname(), 
/* 246 */               node.getX() + offset[i][0], 
/* 247 */               node.getZ() + offset[i][1]);
/*     */             
/* 249 */             if (!closedSet.contains(nextCoord))
/*     */             {
/*     */ 
/*     */ 
/* 253 */               openSet.add(nextCoord); }
/*     */           }
/*     */         } }
/*     */     }
/* 257 */     HashMap<String, Integer> biomes = new HashMap();
/*     */     
/*     */ 
/* 260 */     double hammers = 0.0D;
/* 261 */     double growth = 0.0D;
/* 262 */     double happiness = 0.0D;
/* 263 */     double beakers = 0.0D;
/* 264 */     DecimalFormat df = new DecimalFormat();
/*     */     Biome biome;
/* 266 */     for (ChunkCoord c : closedSet)
/*     */     {
/* 268 */       biome = c.getChunk().getWorld().getBiome(c.getX() * 16, c.getZ() * 16);
/*     */       
/* 270 */       if (!biomes.containsKey(biome.name())) {
/* 271 */         biomes.put(biome.name(), Integer.valueOf(1));
/*     */       } else {
/* 273 */         Integer value = (Integer)biomes.get(biome.name());
/* 274 */         biomes.put(biome.name(), Integer.valueOf(value.intValue() + 1));
/*     */       }
/*     */       
/* 277 */       ConfigCultureBiomeInfo info = CivSettings.getCultureBiome(biome.name());
/*     */       
/*     */ 
/* 280 */       hammers += info.hammers;
/* 281 */       growth += info.growth;
/* 282 */       happiness += info.happiness;
/* 283 */       beakers += info.beakers;
/*     */     }
/*     */     
/* 286 */     outList.add("§bBiome Counts");
/*     */     
/* 288 */     String out = "";
/* 289 */     for (String biome : biomes.keySet()) {
/* 290 */       Integer count = (Integer)biomes.get(biome);
/* 291 */       out = out + "§2" + biome + ": " + "§a" + count + "§2" + ", ";
/*     */     }
/*     */     
/* 294 */     outList.add(out);
/*     */     
/*     */ 
/* 297 */     outList.add("§bTotals");
/* 298 */     outList.add("§2 Happiness:§a" + df.format(happiness) + 
/* 299 */       "§2" + " Hammers:" + "§a" + df.format(hammers) + 
/* 300 */       "§2" + " Growth:" + "§a" + df.format(growth) + 
/* 301 */       "§2" + " Beakers:" + "§a" + df.format(beakers));
/* 302 */     return outList;
/*     */   }
/*     */   
/*     */   public void survey_cmd() throws CivException {
/* 306 */     Player player = getPlayer();
/* 307 */     CivMessage.send(player, survey(player.getLocation()));
/*     */   }
/*     */   
/*     */   public void capitulate_cmd() throws CivException {
/* 311 */     validMayor();
/* 312 */     Town town = getSelectedTown();
/*     */     
/* 314 */     if (town.getMotherCiv() == null) {
/* 315 */       throw new CivException("Cannot capitulate unless captured by another civilization.");
/*     */     }
/*     */     
/* 318 */     if (town.getMotherCiv().getCapitolName().equals(town.getName())) {
/* 319 */       throw new CivException("Cannot capitulate your capitol town. Use /civ capitulate instead to capitulate your entire civ.");
/*     */     }
/*     */     
/* 322 */     if ((this.args.length < 2) || (!this.args[1].equalsIgnoreCase("yes"))) {
/* 323 */       CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "Capitualting means that this town will become a normal town in " + town.getCiv().getName() + " and can no longer revolt. Are you sure?");
/* 324 */       CivMessage.send(this.sender, "§e" + ChatColor.BOLD + "If you're sure, type /town capitulate yes");
/* 325 */       return;
/*     */     }
/*     */     
/*     */ 
/* 329 */     town.setMotherCiv(null);
/* 330 */     town.save();
/*     */     
/* 332 */     CivMessage.global("The conquered town of " + town.getName() + " has capitualted to " + town.getCiv().getName() + " and can no longer revolt.");
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
/*     */   public void select_cmd()
/*     */     throws CivException
/*     */   {
/* 375 */     Resident resident = getResident();
/* 376 */     Town selectTown = getNamedTown(1);
/*     */     
/* 378 */     if ((resident.getSelectedTown() == null) && 
/* 379 */       (resident.getTown() == selectTown)) {
/* 380 */       throw new CivException("You already have " + selectTown.getName() + " selected.");
/*     */     }
/*     */     
/*     */ 
/* 384 */     if (resident.getSelectedTown() == selectTown) {
/* 385 */       throw new CivException("You already have " + selectTown.getName() + " selected.");
/*     */     }
/*     */     
/* 388 */     selectTown.validateResidentSelect(resident);
/*     */     
/* 390 */     resident.setSelectedTown(selectTown);
/* 391 */     CivMessage.sendSuccess(this.sender, "You have selected " + selectTown.getName() + ".");
/*     */   }
/*     */   
/*     */   public void leavegroup_cmd() throws CivException {
/* 395 */     Town town = getNamedTown(1);
/* 396 */     PermissionGroup grp = getNamedPermissionGroup(town, 2);
/* 397 */     Resident resident = getResident();
/*     */     
/* 399 */     if (!grp.hasMember(resident)) {
/* 400 */       throw new CivException("You are not a member of the group " + grp.getName() + " in town " + town.getName());
/*     */     }
/*     */     
/* 403 */     if ((grp == town.getMayorGroup()) && (grp.getMemberCount() == 1)) {
/* 404 */       throw new CivException("You cannot leave the mayor group if you're the last mayor.");
/*     */     }
/*     */     
/* 407 */     if ((grp == town.getCiv().getLeaderGroup()) && (grp.getMemberCount() == 1)) {
/* 408 */       throw new CivException("You cannot leave the leaders group if you're the last leader.");
/*     */     }
/*     */     
/* 411 */     grp.removeMember(resident);
/* 412 */     grp.save();
/* 413 */     CivMessage.sendSuccess(this.sender, "You are no longer a member of the " + grp.getName() + " group in town " + town.getName());
/*     */   }
/*     */   
/*     */   public void outlaw_cmd() {
/* 417 */     TownOutlawCommand cmd = new TownOutlawCommand();
/* 418 */     cmd.onCommand(this.sender, null, "outlaw", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void disbandtown_cmd() throws CivException {
/* 422 */     validMayor();
/* 423 */     Town town = getSelectedTown();
/*     */     
/* 425 */     if (town.getMotherCiv() != null) {
/* 426 */       throw new CivException("You cannot disband a town that is currently captured.");
/*     */     }
/*     */     
/* 429 */     if (town.isCapitol()) {
/* 430 */       throw new CivException("You cannot disband the capitol town.");
/*     */     }
/*     */     
/* 433 */     if (town.mayorWantsToDisband) {
/* 434 */       town.mayorWantsToDisband = false;
/* 435 */       CivMessage.send(this.sender, "No longer want to disband.");
/* 436 */       return;
/*     */     }
/*     */     
/* 439 */     town.mayorWantsToDisband = true;
/*     */     
/*     */ 
/* 442 */     if ((town.leaderWantsToDisband) && (town.mayorWantsToDisband)) {
/* 443 */       CivMessage.sendCiv(town.getCiv(), "Town " + town.getName() + " is being disbanded by agreement from the civ leader and the mayor");
/* 444 */       town.disband();
/*     */     }
/*     */     
/* 447 */     CivMessage.send(this.sender, "Waiting on leader to type /civ disbandtown");
/*     */   }
/*     */   
/*     */   public void top5_cmd() {
/* 451 */     CivMessage.sendHeading(this.sender, "Top 5 Towns");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 461 */     synchronized (CivGlobal.townScores) {
/* 462 */       int i = 1;
/* 463 */       for (Integer score : CivGlobal.townScores.descendingKeySet()) {
/* 464 */         CivMessage.send(this.sender, i + ") " + "§6" + ((Town)CivGlobal.townScores.get(score)).getName() + "§f" + " - " + score + " points");
/* 465 */         i++;
/* 466 */         if (i > 5) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void list_cmd()
/*     */   {
/* 475 */     String out = "";
/*     */     
/* 477 */     CivMessage.sendHeading(this.sender, "Towns in the World");
/* 478 */     for (Town town : CivGlobal.getTowns()) {
/* 479 */       out = out + town.getName() + "(" + town.getCiv().getName() + ")" + ", ";
/*     */     }
/*     */     
/* 482 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void evict_cmd() throws CivException {
/* 486 */     Town town = getSelectedTown();
/* 487 */     Resident resident = getResident();
/*     */     
/* 489 */     if (this.args.length < 2) {
/* 490 */       throw new CivException("Enter the name of who you want to evict.");
/*     */     }
/*     */     
/* 493 */     Resident residentToKick = getNamedResident(1);
/*     */     
/* 495 */     if (residentToKick.getTown() != town) {
/* 496 */       throw new CivException(this.args[1] + " is not a member of this town.");
/*     */     }
/*     */     
/* 499 */     if ((!town.isInGroup("mayors", resident)) && (!town.isInGroup("assistants", resident))) {
/* 500 */       throw new CivException("Only mayors and assistants of this town can evict residents.");
/*     */     }
/*     */     
/* 503 */     if ((town.isInGroup("mayors", residentToKick)) || (town.isInGroup("assistants", residentToKick))) {
/* 504 */       throw new CivException("Mayors and assistants cannot be evicted from town, demote them first.");
/*     */     }
/*     */     
/* 507 */     if (!residentToKick.isLandOwner()) {
/* 508 */       town.removeResident(residentToKick);
/*     */       try
/*     */       {
/* 511 */         CivMessage.send(CivGlobal.getPlayer(residentToKick), "§eYou have been evicted from town!");
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */       
/* 515 */       CivMessage.sendTown(town, residentToKick.getName() + " has been evicted from town by " + resident.getName());
/* 516 */       return;
/*     */     }
/*     */     
/* 519 */     residentToKick.setDaysTilEvict(3);
/* 520 */     residentToKick.warnEvict();
/* 521 */     residentToKick.save();
/* 522 */     CivMessage.sendSuccess(this.sender, this.args[1] + " will be evicted from town in " + 3 + " days.");
/*     */   }
/*     */   
/*     */   public void show_cmd() throws CivException {
/* 526 */     if (this.args.length < 2) {
/* 527 */       throw new CivException("You need to enter the town name you wish to look at.");
/*     */     }
/*     */     
/* 530 */     Town town = getNamedTown(1);
/* 531 */     if ((this.sender instanceof Player)) {
/* 532 */       TownInfoCommand.show(this.sender, getResident(), town, town.getCiv(), this);
/*     */     } else {
/* 534 */       TownInfoCommand.show(this.sender, null, town, town.getCiv(), this);
/*     */     }
/*     */     try
/*     */     {
/* 538 */       Civilization civ = getSenderCiv();
/* 539 */       if ((town.getCiv() != civ) && 
/* 540 */         ((this.sender instanceof Player))) {
/* 541 */         Player player = (Player)this.sender;
/* 542 */         Location ourCapLoc = civ.getCapitolTownHallLocation();
/*     */         
/* 544 */         if (ourCapLoc == null) {
/* 545 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */         try
/*     */         {
/* 551 */           if (town.getTownHall() != null) {
/* 552 */             Location theirTownHallLoc = town.getTownHall().getCenterLocation().getLocation();
/* 553 */             double potentialDistanceLow = civ.getDistanceUpkeepAtLocation(ourCapLoc, theirTownHallLoc, true);
/* 554 */             double potentialDistanceHigh = civ.getDistanceUpkeepAtLocation(ourCapLoc, theirTownHallLoc, false);
/*     */             
/* 556 */             CivMessage.send(player, "§eYour civilization would pay " + potentialDistanceLow + " if it or owned it.");
/* 557 */             CivMessage.send(player, "§eYour civilization would pay 0 upkeep if you conquered it and it remains uncapitulated");
/* 558 */             CivMessage.send(player, "§eIf this town's culture is not connected to your captial's culture and it was owned fully by your civ, you would pay " + potentialDistanceHigh + " coins in distance upkeep if you owned it.");
/*     */           } else {
/* 560 */             CivMessage.send(player, "§eThis town has no town hall! Cannot calculate distance upkeep to it.");
/*     */           }
/*     */         } catch (InvalidConfiguration e) {
/* 563 */           e.printStackTrace();
/* 564 */           CivMessage.sendError(this.sender, "Internal Configuration error."); return;
/*     */         }
/*     */       }
/*     */       
/*     */       return;
/*     */     }
/*     */     catch (CivException localCivException) {}
/*     */   }
/*     */   
/*     */   public void leave_cmd()
/*     */     throws CivException
/*     */   {
/* 576 */     Town town = getSelectedTown();
/* 577 */     Resident resident = getResident();
/*     */     
/* 579 */     if (town != resident.getTown()) {
/* 580 */       throw new CivException("You must have your own town selected in order to leave it.");
/*     */     }
/*     */     
/* 583 */     if ((town.getMayorGroup().getMemberCount() == 1) && 
/* 584 */       (town.getMayorGroup().hasMember(resident))) {
/* 585 */       throw new CivException("You cannot leave town since you are it's only mayor.");
/*     */     }
/*     */     
/* 588 */     town.removeResident(resident);
/* 589 */     if (resident.isCivChat()) {
/* 590 */       resident.setCivChat(false);
/*     */     }
/*     */     
/* 593 */     if (resident.isTownChat()) {
/* 594 */       resident.setTownChat(false);
/* 595 */       CivMessage.send(this.sender, "§7You've been removed from town chat since you've left the town.");
/*     */     }
/*     */     
/* 598 */     CivMessage.sendSuccess(this.sender, "You left the town of " + town.getName());
/* 599 */     CivMessage.sendTown(town, resident.getName() + " has left the town.");
/*     */     
/* 601 */     town.save();
/* 602 */     resident.save();
/*     */   }
/*     */   
/*     */   public void set_cmd() {
/* 606 */     TownSetCommand cmd = new TownSetCommand();
/* 607 */     cmd.onCommand(this.sender, null, "set", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void reset_cmd() throws CivException {
/* 611 */     TownResetCommand cmd = new TownResetCommand();
/* 612 */     cmd.onCommand(this.sender, null, "reset", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void upgrade_cmd() throws CivException {
/* 616 */     TownUpgradeCommand cmd = new TownUpgradeCommand();
/* 617 */     cmd.onCommand(this.sender, null, "upgrade", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void withdraw_cmd() throws CivException {
/* 621 */     if (this.args.length < 2) {
/* 622 */       throw new CivException("Enter the amount you want to withdraw.");
/*     */     }
/*     */     
/* 625 */     Town town = getSelectedTown();
/* 626 */     Player player = getPlayer();
/* 627 */     Resident resident = getResident();
/*     */     
/* 629 */     if (!town.playerIsInGroupName("mayors", player)) {
/* 630 */       throw new CivException("Only mayors can use this command.");
/*     */     }
/*     */     try
/*     */     {
/* 634 */       Double amount = Double.valueOf(this.args[1]);
/* 635 */       if (amount.doubleValue() < 1.0D) {
/* 636 */         throw new CivException("Cannot withdraw less than 1");
/*     */       }
/* 638 */       amount = Double.valueOf(Math.floor(amount.doubleValue()));
/*     */       
/* 640 */       if (!town.getTreasury().payTo(resident.getTreasury(), Double.valueOf(this.args[1]).doubleValue())) {
/* 641 */         throw new CivException("The town does not have that much.");
/*     */       }
/*     */     } catch (NumberFormatException e) {
/* 644 */       throw new CivException(this.args[1] + " is not a valid number.");
/*     */     }
/*     */     
/* 647 */     CivMessage.sendSuccess(this.sender, "Withdrew " + this.args[1] + " coins.");
/*     */   }
/*     */   
/*     */   public void deposit_cmd() throws CivException {
/* 651 */     if (this.args.length < 2) {
/* 652 */       throw new CivException("Enter the amount you want to deposit.");
/*     */     }
/*     */     
/* 655 */     Resident resident = getResident();
/* 656 */     Town town = getSelectedTown();
/* 657 */     Double amount = getNamedDouble(1);
/*     */     try
/*     */     {
/* 660 */       if (amount.doubleValue() < 1.0D) {
/* 661 */         throw new CivException("Cannot deposit less than 1");
/*     */       }
/* 663 */       amount = Double.valueOf(Math.floor(amount.doubleValue()));
/* 664 */       town.depositFromResident(amount, resident);
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 667 */       throw new CivException(this.args[1] + " is not a valid number.");
/*     */     }
/*     */     
/* 670 */     CivMessage.sendSuccess(this.sender, "Deposited " + this.args[1] + " coins.");
/*     */   }
/*     */   
/*     */   public void add_cmd() throws CivException {
/* 674 */     validMayorAssistantLeader();
/*     */     
/* 676 */     Resident newResident = getNamedResident(1);
/* 677 */     Player player = getPlayer();
/* 678 */     Town town = getSelectedTown();
/*     */     
/* 680 */     if (War.isWarTime()) {
/* 681 */       throw new CivException("Cannot invite players to town during WarTime.");
/*     */     }
/*     */     
/* 684 */     if ((War.isWithinWarDeclareDays()) && (town.getCiv().getDiplomacyManager().isAtWar())) {
/* 685 */       throw new CivException("Cannot invite players to a civ that is at war within " + War.getTimeDeclareDays() + " days before WarTime.");
/*     */     }
/*     */     
/* 688 */     if (newResident.hasCamp()) {
/*     */       try {
/* 690 */         Player resPlayer = CivGlobal.getPlayer(newResident);
/* 691 */         CivMessage.send(resPlayer, "§e" + player.getName() + " tried to invite you to the town of " + town.getName() + 
/* 692 */           " but cannot since you are in a camp. Leave camp first using /camp leave");
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */       
/* 696 */       throw new CivException("You cannot invite " + newResident.getName() + " to town since he is part of a camp. Have him leave camp first with /camp leave.");
/*     */     }
/*     */     
/* 699 */     if (town.hasResident(newResident)) {
/* 700 */       throw new CivException(newResident.getName() + " is already a member of town.");
/*     */     }
/*     */     
/* 703 */     if (newResident.getTown() != null) {
/* 704 */       throw new CivException(newResident.getName() + " is already in town " + newResident.getTown().getName());
/*     */     }
/*     */     
/* 707 */     JoinTownResponse join = new JoinTownResponse();
/* 708 */     join.town = town;
/* 709 */     join.resident = newResident;
/* 710 */     join.sender = player;
/*     */     
/* 712 */     newResident.validateJoinTown(town);
/*     */     
/* 714 */     CivGlobal.questionPlayer(player, CivGlobal.getPlayer(newResident), 
/* 715 */       "Would you like to join the town of " + town.getName() + "?", 
/* 716 */       30000L, join);
/*     */     
/* 718 */     CivMessage.sendSuccess(this.sender, "§7Invited to " + this.args[1] + " to town " + town.getName());
/*     */   }
/*     */   
/*     */   public void info_cmd() throws CivException {
/* 722 */     TownInfoCommand cmd = new TownInfoCommand();
/* 723 */     cmd.onCommand(this.sender, null, "info", stripArgs(this.args, 1));
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
/*     */   public void claim_cmd()
/*     */     throws CivException
/*     */   {
/* 754 */     Player player = getPlayer();
/* 755 */     Town town = getSelectedTown();
/*     */     
/* 757 */     if ((!town.playerIsInGroupName("mayors", player)) && (!town.playerIsInGroupName("assistants", player))) {
/* 758 */       throw new CivException("Only mayors and assistants can use this command.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 767 */     TownChunk.claim(town, player, false);
/*     */   }
/*     */   
/*     */   public void unclaim_cmd() throws CivException {
/* 771 */     Town town = getSelectedTown();
/* 772 */     Player player = getPlayer();
/* 773 */     Resident resident = getResident();
/* 774 */     TownChunk tc = getStandingTownChunk();
/*     */     
/*     */ 
/* 777 */     if ((!town.playerIsInGroupName("mayors", player)) && (!town.playerIsInGroupName("assistants", player))) {
/* 778 */       throw new CivException("Only mayors and assistants can use this command.");
/*     */     }
/*     */     
/* 781 */     if (town.getTownChunks().size() <= 1) {
/* 782 */       throw new CivException("Cannot unclaim your last town chunk.");
/*     */     }
/*     */     
/* 785 */     if (tc.getTown() != resident.getTown()) {
/* 786 */       throw new CivException("You cannot unclaim a town chunk that isn't yours.");
/*     */     }
/*     */     
/* 789 */     if ((tc.perms.getOwner() != null) && (tc.perms.getOwner() != resident)) {
/* 790 */       throw new CivException("You cannot unclaim a chunk that belongs to another resident.");
/*     */     }
/*     */     
/* 793 */     TownChunk.unclaim(tc);
/* 794 */     if (tc.isOutpost()) {
/* 795 */       CivMessage.sendSuccess(this.sender, "Unclaimed Outpost at " + tc.getCenterString());
/*     */     } else {
/* 797 */       CivMessage.sendSuccess(this.sender, "Unclaimed " + tc.getCenterString());
/*     */     }
/*     */   }
/*     */   
/*     */   public void group_cmd() throws CivException
/*     */   {
/* 803 */     TownGroupCommand cmd = new TownGroupCommand();
/* 804 */     cmd.onCommand(this.sender, null, "group", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void members_cmd() throws CivException
/*     */   {
/* 809 */     Town town = getSelectedTown();
/*     */     
/* 811 */     CivMessage.sendHeading(this.sender, town.getName() + " Members");
/* 812 */     String out = "";
/* 813 */     for (Resident res : town.getResidents()) {
/* 814 */       out = out + res.getName() + ", ";
/*     */     }
/* 816 */     CivMessage.send(this.sender, out);
/*     */   }
/*     */   
/*     */   public void showHelp() {
/* 820 */     showBasicHelp();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void permissionCheck() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void doDefaultAction()
/*     */   {
/* 831 */     showHelp();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\town\TownCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */