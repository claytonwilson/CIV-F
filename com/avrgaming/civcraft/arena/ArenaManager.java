/*     */ package com.avrgaming.civcraft.arena;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigArena;
/*     */ import com.avrgaming.civcraft.config.ConfigArenaTeam;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.TimeTools;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import java.util.Random;
/*     */ import net.minecraft.util.org.apache.commons.io.FileUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.World.Environment;
/*     */ import org.bukkit.WorldCreator;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.scoreboard.DisplaySlot;
/*     */ import org.bukkit.scoreboard.Objective;
/*     */ import org.bukkit.scoreboard.Score;
/*     */ import org.bukkit.scoreboard.Scoreboard;
/*     */ import org.bukkit.scoreboard.ScoreboardManager;
/*     */ 
/*     */ public class ArenaManager implements Runnable
/*     */ {
/*  48 */   public static HashMap<BlockCoord, ArenaControlBlock> arenaControlBlocks = new HashMap();
/*  49 */   public static HashMap<BlockCoord, Arena> chests = new HashMap();
/*  50 */   public static HashMap<BlockCoord, Arena> respawnSigns = new HashMap();
/*  51 */   public static HashMap<String, Arena> activeArenas = new HashMap();
/*     */   
/*  53 */   public static Queue<ArenaTeam> teamQueue = new LinkedList();
/*     */   public static final int MAX_INSTANCES = 1;
/*  55 */   public static ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
/*  56 */   public static boolean enabled = true;
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     Random rand;
/*     */     
/*  64 */     if ((activeArenas.size() < 1) && (enabled)) {
/*  65 */       ArenaTeam team1 = (ArenaTeam)teamQueue.poll();
/*  66 */       if (team1 == null)
/*     */       {
/*  68 */         return;
/*     */       }
/*     */       
/*  71 */       ArenaTeam team2 = (ArenaTeam)teamQueue.poll();
/*  72 */       if (team2 == null)
/*     */       {
/*  74 */         CivMessage.sendTeam(team1, "No other teams waiting in queue yet, please wait.");
/*  75 */         teamQueue.add(team1);
/*  76 */         return;
/*     */       }
/*     */       
/*     */ 
/*  80 */       rand = new Random();
/*  81 */       int index = rand.nextInt(CivSettings.arenas.size());
/*     */       
/*  83 */       int i = 0;
/*  84 */       ConfigArena arena = null;
/*     */       
/*  86 */       for (ConfigArena a : CivSettings.arenas.values()) {
/*  87 */         if (i == index) {
/*  88 */           arena = a;
/*  89 */           break;
/*     */         }
/*  91 */         i++;
/*     */       }
/*     */       
/*  94 */       if (arena == null) {
/*  95 */         CivLog.error("Couldn't find an arena configured....");
/*  96 */         return;
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 101 */         Arena activeArena = createArena(arena);
/* 102 */         CivMessage.sendTeam(team1, "Teleporting our team to the arena in 10 seconds...");
/* 103 */         CivMessage.sendTeam(team2, "Teleporting our team to the arena in 10 seconds...");
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
/* 135 */         TaskMaster.syncTask(new Runnable()
/*     */         {
/*     */           Arena arena;
/*     */           ArenaTeam team1;
/*     */           ArenaTeam team2;
/*     */           
/*     */           public void run()
/*     */           {
/*     */             try
/*     */             {
/* 119 */               ArenaManager.addTeamToArena(this.team1, this.team2, this.arena);
/* 120 */               ArenaManager.addTeamToArena(this.team2, this.team1, this.arena);
/* 121 */               ArenaManager.startArenaMatch(this.arena, this.team1, this.team2);
/*     */             } catch (CivException e) {
/* 123 */               CivMessage.sendTeam(this.team1, "An error has occured and your team has been kicked from the arena queue.");
/* 124 */               CivMessage.sendTeam(this.team2, "An error has occured and your team has been kicked from the arena queue.");
/*     */               
/* 126 */               CivMessage.sendTeam(this.team1, "Error:" + e.getMessage());
/* 127 */               CivMessage.sendTeam(this.team2, "Error:" + e.getMessage());
/*     */               
/* 129 */               e.printStackTrace();
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */ 
/* 135 */         }, TimeTools.toTicks(10L));
/*     */       } catch (CivException e) {
/* 137 */         e.printStackTrace();
/* 138 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */     int i = 0;
/* 148 */     for (ArenaTeam team : teamQueue) {
/* 149 */       if (!enabled) {
/* 150 */         CivMessage.sendTeam(team, "Arenas are disabled via and admin. Please wait for them to be re-enabled.");
/*     */       }
/* 152 */       else if (i < 2) {
/* 153 */         CivMessage.sendTeam(team, "Waiting to join arena. We are next! All arena instances are busy.");
/*     */       } else {
/* 155 */         CivMessage.sendTeam(team, "Waiting to join arena. There are " + i + " teams ahead of us in line.");
/*     */       }
/*     */       
/* 158 */       i++;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void startArenaMatch(Arena activeArena, ArenaTeam team1, ArenaTeam team2)
/*     */   {
/* 166 */     Objective points1 = activeArena.getScoreboard(team1.getName()).registerNewObjective("teampoints1", "dummy");
/* 167 */     Objective points2 = activeArena.getScoreboard(team2.getName()).registerNewObjective("teampoints2", "dummy");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 172 */     points1.setDisplaySlot(DisplaySlot.SIDEBAR);
/* 173 */     points1.setDisplayName("Team Hitpoints");
/* 174 */     points2.setDisplaySlot(DisplaySlot.SIDEBAR);
/* 175 */     points2.setDisplayName("Team Hitpoints");
/*     */     
/* 177 */     Score score1Team1 = points1.getScore(team1.getTeamScoreboardName());
/* 178 */     Score score1Team2 = points1.getScore(team2.getTeamScoreboardName());
/* 179 */     Score timeout1 = points1.getScore(Bukkit.getOfflinePlayer("Time Left"));
/*     */     try {
/* 181 */       timeout1.setScore(CivSettings.getInteger(CivSettings.arenaConfig, "timeout").intValue());
/*     */     } catch (IllegalStateException e1) {
/* 183 */       e1.printStackTrace();
/*     */     } catch (InvalidConfiguration e1) {
/* 185 */       e1.printStackTrace();
/*     */     }
/*     */     
/* 188 */     score1Team1.setScore(((ConfigArenaTeam)activeArena.config.teams.get(0)).controlPoints.size() * activeArena.config.control_block_hp);
/* 189 */     score1Team2.setScore(((ConfigArenaTeam)activeArena.config.teams.get(1)).controlPoints.size() * activeArena.config.control_block_hp);
/*     */     
/* 191 */     Score score2Team1 = points2.getScore(team1.getTeamScoreboardName());
/* 192 */     Score score2Team2 = points2.getScore(team2.getTeamScoreboardName());
/* 193 */     Score timeout2 = points1.getScore(Bukkit.getOfflinePlayer("Time Left"));
/*     */     try {
/* 195 */       timeout2.setScore(CivSettings.getInteger(CivSettings.arenaConfig, "timeout").intValue());
/*     */     } catch (IllegalStateException e1) {
/* 197 */       e1.printStackTrace();
/*     */     } catch (InvalidConfiguration e1) {
/* 199 */       e1.printStackTrace();
/*     */     }
/*     */     
/* 202 */     score2Team1.setScore(((ConfigArenaTeam)activeArena.config.teams.get(0)).controlPoints.size() * activeArena.config.control_block_hp);
/* 203 */     score2Team2.setScore(((ConfigArenaTeam)activeArena.config.teams.get(1)).controlPoints.size() * activeArena.config.control_block_hp);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 211 */     activeArena.objectives.put(team1.getName() + ";score", points1);
/* 212 */     activeArena.objectives.put(team2.getName() + ";score", points2);
/*     */     
/*     */ 
/* 215 */     for (Resident resident : team1.teamMembers) {
/* 216 */       resident.saveInventory();
/* 217 */       resident.clearInventory();
/* 218 */       resident.setInsideArena(true);
/* 219 */       resident.save();
/*     */       try
/*     */       {
/* 222 */         Player player = CivGlobal.getPlayer(resident);
/* 223 */         player.setScoreboard(activeArena.getScoreboard(resident.getTeam().getName()));
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */     
/*     */ 
/* 229 */     for (Resident resident : team2.teamMembers) {
/* 230 */       resident.saveInventory();
/* 231 */       resident.clearInventory();
/* 232 */       resident.setInsideArena(true);
/* 233 */       resident.save();
/*     */       try
/*     */       {
/* 236 */         Player player = CivGlobal.getPlayer(resident);
/* 237 */         player.setScoreboard(activeArena.getScoreboard(resident.getTeam().getName()));
/*     */       }
/*     */       catch (CivException localCivException1) {}
/*     */     }
/*     */     
/*     */ 
/* 243 */     CivMessage.sendArena(activeArena, "Arena Match Started!");
/*     */   }
/*     */   
/*     */   public static void addTeamToQueue(ArenaTeam team) throws CivException {
/* 247 */     if (teamQueue.contains(team)) {
/* 248 */       throw new CivException("Your team is already in the queue.");
/*     */     }
/*     */     
/* 251 */     for (Resident resident : team.teamMembers) {
/*     */       try {
/* 253 */         CivGlobal.getPlayer(resident);
/*     */       }
/*     */       catch (CivException e)
/*     */       {
/*     */         continue;
/*     */       }
/* 259 */       if (!resident.isUsesAntiCheat()) {
/* 260 */         throw new CivException("Cannot join arena: " + resident.getName() + " is not validated by CivCraft's anti-cheat.");
/*     */       }
/*     */     }
/*     */     
/* 264 */     CivMessage.sendTeam(team, "Added our team to the queue...");
/* 265 */     if (teamQueue.size() > 2) {
/* 266 */       CivMessage.sendTeam(team, "There are " + teamQueue.size() + " teams ahead of us in line.");
/*     */     }
/* 268 */     teamQueue.add(team);
/*     */   }
/*     */   
/*     */   public static void addTeamToArena(ArenaTeam team, ArenaTeam otherTeam, Arena arena) throws CivException {
/* 272 */     arena.addTeam(team);
/* 273 */     team.setCurrentArena(arena);
/*     */     
/* 275 */     CivMessage.sendTeamHeading(team, "Arena Match");
/* 276 */     CivMessage.sendTeam(team, "Arena: §e" + CivColor.BOLD + arena.config.name);
/* 277 */     CivMessage.sendTeam(team, "§a" + CivColor.BOLD + team.getName() + CivColor.RESET + " VS " + "§c" + CivColor.BOLD + otherTeam.getName());
/* 278 */     CivMessage.sendTeam(team, "Our Score: §a" + team.getLadderPoints() + " " + getFavoredString(team, otherTeam));
/* 279 */     CivMessage.sendTeam(team, "Their Score: §a" + otherTeam.getLadderPoints() + " " + getFavoredString(otherTeam, team));
/* 280 */     CivMessage.sendTeam(team, "Their team members: " + otherTeam.getMemberListSaveString());
/*     */   }
/*     */   
/*     */   public static Arena createArena(ConfigArena arena)
/*     */     throws CivException
/*     */   {
/* 286 */     File srcFolder = new File("arenas/" + arena.world_source);
/*     */     
/* 288 */     if (!srcFolder.exists()) {
/* 289 */       throw new CivException("No world source found at:" + arena.world_source);
/*     */     }
/*     */     
/* 292 */     Arena activeArena = new Arena(arena);
/* 293 */     String instanceWorldName = activeArena.getInstanceName();
/*     */     
/* 295 */     File destFolder = new File(instanceWorldName);
/*     */     try
/*     */     {
/* 298 */       FileUtils.deleteDirectory(destFolder);
/*     */     } catch (IOException e) {
/* 300 */       e.printStackTrace();
/*     */     }
/*     */     try
/*     */     {
/* 304 */       copyFolder(srcFolder, destFolder);
/*     */     } catch (IOException e) {
/* 306 */       e.printStackTrace();
/* 307 */       return null;
/*     */     }
/*     */     
/* 310 */     World world = createArenaWorld(arena, instanceWorldName);
/* 311 */     createArenaControlPoints(arena, world, activeArena);
/*     */     
/* 313 */     activeArenas.put(instanceWorldName, activeArena);
/* 314 */     return activeArena;
/*     */   }
/*     */   
/*     */   public static void destroyArena(String instanceName) throws CivException {
/* 318 */     Arena arena = (Arena)activeArenas.get(instanceName);
/* 319 */     if (arena == null) {
/* 320 */       throw new CivException("No arena with instance name:" + instanceName);
/*     */     }
/*     */     
/* 323 */     LinkedList<BlockCoord> removeUs = new LinkedList();
/* 324 */     for (BlockCoord bcoord : arenaControlBlocks.keySet()) {
/* 325 */       if (bcoord.getWorldname().equals(instanceName)) {
/* 326 */         removeUs.add(bcoord);
/*     */       }
/*     */     }
/* 329 */     for (BlockCoord bcoord : removeUs) {
/* 330 */       arenaControlBlocks.remove(bcoord);
/*     */     }
/* 332 */     removeUs.clear();
/*     */     
/* 334 */     for (BlockCoord bcoord : respawnSigns.keySet()) {
/* 335 */       if (bcoord.getWorldname().equals(instanceName)) {
/* 336 */         removeUs.add(bcoord);
/*     */       }
/*     */     }
/* 339 */     for (BlockCoord bcoord : removeUs) {
/* 340 */       respawnSigns.remove(bcoord);
/*     */     }
/* 342 */     removeUs.clear();
/*     */     
/* 344 */     for (BlockCoord bcoord : chests.keySet()) {
/* 345 */       if (bcoord.getWorldname().equals(instanceName)) {
/* 346 */         removeUs.add(bcoord);
/*     */       }
/*     */     }
/* 349 */     for (BlockCoord bcoord : removeUs) {
/* 350 */       chests.remove(bcoord);
/*     */     }
/*     */     
/* 353 */     arena.returnPlayers();
/* 354 */     arena.clearTeams();
/*     */     
/* 356 */     activeArenas.remove(instanceName);
/* 357 */     Bukkit.getServer().unloadWorld(instanceName, false);
/*     */     try
/*     */     {
/* 360 */       FileUtils.deleteDirectory(new File(instanceName));
/*     */     } catch (IOException e) {
/* 362 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void createArenaControlPoints(ConfigArena arena, World world, Arena activeArena)
/*     */     throws CivException
/*     */   {
/*     */     ArenaControlBlock acb;
/*     */     
/* 372 */     for (Iterator localIterator1 = arena.teams.iterator(); localIterator1.hasNext(); 
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
/* 399 */         acb.hasNext())
/*     */     {
/* 372 */       ConfigArenaTeam team = (ConfigArenaTeam)localIterator1.next();
/* 373 */       for (BlockCoord c : team.controlPoints)
/*     */       {
/* 375 */         BlockCoord bcoord = new BlockCoord(world.getName(), c.getX(), c.getY(), c.getZ());
/* 376 */         acb = new ArenaControlBlock(bcoord, team.number.intValue(), arena.control_block_hp, activeArena);
/* 377 */         arenaControlBlocks.put(bcoord, acb);
/*     */       }
/*     */       
/*     */ 
/* 381 */       BlockCoord coord = team.respawnSign;
/* 382 */       Location loc = coord.getCenteredLocation();
/* 383 */       loc.setWorld(world);
/*     */       
/* 385 */       if ((loc.getBlock().getType().equals(Material.SIGN_POST)) || 
/* 386 */         (loc.getBlock().getType().equals(Material.WALL_SIGN))) {
/* 387 */         Sign sign = (Sign)loc.getBlock().getState();
/* 388 */         sign.setLine(0, "");
/* 389 */         sign.setLine(1, "Respawn");
/* 390 */         sign.setLine(2, "At Arena");
/* 391 */         sign.setLine(3, "");
/*     */         
/* 393 */         sign.update();
/* 394 */         respawnSigns.put(new BlockCoord(loc), activeArena);
/*     */       } else {
/* 396 */         CivLog.error("Couldn't find sign for respawn sign for arena:" + arena.name);
/*     */       }
/*     */       
/* 399 */       acb = team.chests.iterator(); continue;BlockCoord c = (BlockCoord)acb.next();
/* 400 */       BlockCoord bcoord = new BlockCoord(world.getName(), c.getX(), c.getY(), c.getZ());
/* 401 */       chests.put(bcoord, activeArena);
/*     */       
/* 403 */       ItemManager.setTypeId(bcoord.getBlock(), ItemManager.getId(Material.ENDER_CHEST));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static World createArenaWorld(ConfigArena arena, String name)
/*     */   {
/* 412 */     World world = Bukkit.getServer().getWorld(name);
/* 413 */     if (world == null) {
/* 414 */       WorldCreator wc = new WorldCreator(name);
/* 415 */       wc.environment(World.Environment.NORMAL);
/* 416 */       wc.type(org.bukkit.WorldType.FLAT);
/* 417 */       wc.generateStructures(false);
/*     */       
/* 419 */       world = Bukkit.getServer().createWorld(wc);
/* 420 */       world.setAutoSave(false);
/* 421 */       world.setSpawnFlags(false, false);
/* 422 */       world.setKeepSpawnInMemory(false);
/* 423 */       ChunkCoord.addWorld(world);
/*     */     }
/*     */     
/* 426 */     return world;
/*     */   }
/*     */   
/*     */   private static void copyFolder(File src, File dest) throws IOException
/*     */   {
/* 431 */     if (src.isDirectory())
/*     */     {
/*     */ 
/* 434 */       if (!dest.exists()) {
/* 435 */         dest.mkdir();
/* 436 */         System.out.println("Directory copied from " + 
/* 437 */           src + "  to " + dest);
/*     */       }
/*     */       
/*     */ 
/* 441 */       String[] files = src.list();
/*     */       String[] arrayOfString1;
/* 443 */       int j = (arrayOfString1 = files).length; for (int i = 0; i < j; i++) { String file = arrayOfString1[i];
/*     */         
/* 445 */         File srcFile = new File(src, file);
/* 446 */         File destFile = new File(dest, file);
/*     */         
/* 448 */         copyFolder(srcFile, destFile);
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 454 */       InputStream in = new java.io.FileInputStream(src);
/* 455 */       OutputStream out = new FileOutputStream(dest);
/*     */       
/* 457 */       byte[] buffer = new byte['Ѐ'];
/*     */       
/*     */       int length;
/*     */       
/* 461 */       while ((length = in.read(buffer)) > 0) { int length;
/* 462 */         out.write(buffer, 0, length);
/*     */       }
/*     */       
/* 465 */       in.close();
/* 466 */       out.close();
/* 467 */       System.out.println("File copied from " + src + " to " + dest);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getFavoredString(ArenaTeam target, ArenaTeam other) {
/* 472 */     if (target.getLadderPoints() < other.getLadderPoints()) {
/* 473 */       return "";
/*     */     }
/*     */     try
/*     */     {
/* 477 */       int slightly_favored_points = CivSettings.getInteger(CivSettings.arenaConfig, "slightly_favored_points").intValue();
/* 478 */       int favored_points = CivSettings.getInteger(CivSettings.arenaConfig, "favored_points").intValue();
/*     */       
/* 480 */       int diff = target.getLadderPoints() - other.getLadderPoints();
/* 481 */       if (diff > favored_points)
/* 482 */         return "§cFavored";
/* 483 */       if (diff > slightly_favored_points) {
/* 484 */         return "§eSlightly Favored";
/*     */       }
/*     */       
/* 487 */       return "";
/*     */     } catch (InvalidConfiguration e) {
/* 489 */       e.printStackTrace();
/*     */     }
/*     */     
/* 492 */     return "";
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
/*     */   public static void declareVictor(Arena arena, ArenaTeam loser, ArenaTeam winner)
/*     */   {
/* 562 */     CivMessage.sendArena(arena, "§a" + CivColor.BOLD + winner.getName() + 
/* 563 */       CivColor.RESET + " has defeated " + 
/* 564 */       "§c" + CivColor.BOLD + loser.getName());
/* 565 */     CivMessage.sendArena(arena, "Leaving arena in 10 seconds...");
/* 566 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       ArenaTeam loser;
/*     */       ArenaTeam winner;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*     */           try
/*     */           {
/* 514 */             int base_points = CivSettings.getInteger(CivSettings.arenaConfig, "base_ladder_points").intValue();
/* 515 */             int slightly_favored_points = CivSettings.getInteger(CivSettings.arenaConfig, "slightly_favored_points").intValue();
/* 516 */             int favored_points = CivSettings.getInteger(CivSettings.arenaConfig, "favored_points").intValue();
/* 517 */             double slightly_favored_modifier = CivSettings.getDouble(CivSettings.arenaConfig, "slightly_favored_modifier");
/* 518 */             double favored_modifier = CivSettings.getDouble(CivSettings.arenaConfig, "favored_modifier");
/*     */             
/*     */ 
/* 521 */             int winnerDifference = this.winner.getLadderPoints() - this.loser.getLadderPoints();
/* 522 */             int points = base_points;
/*     */             
/* 524 */             if (winnerDifference > favored_points)
/*     */             {
/* 526 */               points = (int)(base_points * favored_modifier);
/* 527 */             } else if (winnerDifference > slightly_favored_points)
/*     */             {
/* 529 */               points = (int)(base_points * slightly_favored_modifier);
/* 530 */             } else if (winnerDifference > 0)
/*     */             {
/* 532 */               points = base_points;
/* 533 */             } else if (winnerDifference < -favored_points)
/*     */             {
/* 535 */               points = base_points + (int)(base_points * (1.0D - favored_modifier));
/* 536 */             } else if (winnerDifference < -slightly_favored_points)
/*     */             {
/* 538 */               points = base_points + (int)(base_points * (1.0D - slightly_favored_modifier));
/*     */             }
/*     */             
/* 541 */             this.winner.setLadderPoints(this.winner.getLadderPoints() + points);
/* 542 */             this.loser.setLadderPoints(this.loser.getLadderPoints() - points);
/*     */             
/* 544 */             this.winner.save();
/* 545 */             this.loser.save();
/*     */             
/* 547 */             CivMessage.global("§a" + CivColor.BOLD + this.winner.getName() + "(+" + points + ")" + CivColor.RESET + " defeated " + 
/* 548 */               "§c" + CivColor.BOLD + this.loser.getName() + "(-" + points + ")" + CivColor.RESET + " in Arena!");
/*     */           }
/*     */           catch (InvalidConfiguration e) {
/* 551 */             e.printStackTrace();
/*     */           }
/*     */           
/* 554 */           ArenaManager.destroyArena(ArenaManager.this.getInstanceName());
/*     */         } catch (CivException e) {
/* 556 */           e.printStackTrace();
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 566 */     }, TimeTools.toTicks(10L));
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
/*     */   public static void declareDraw(Arena arena)
/*     */   {
/* 589 */     CivMessage.sendArena(arena, "Leaving arena in 10 seconds...");
/* 590 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/* 582 */           ArenaManager.destroyArena(ArenaManager.this.getInstanceName());
/*     */         } catch (CivException e) {
/* 584 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/* 590 */     }, TimeTools.toTicks(10L));
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\arena\ArenaManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */