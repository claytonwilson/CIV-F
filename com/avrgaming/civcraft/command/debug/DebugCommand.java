/*      */ package com.avrgaming.civcraft.command.debug;
/*      */ 
/*      */ import com.avrgaming.civcraft.arena.Arena;
/*      */ import com.avrgaming.civcraft.arena.ArenaTeam;
/*      */ import com.avrgaming.civcraft.command.CommandBase;
/*      */ import com.avrgaming.civcraft.command.admin.AdminTownCommand;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.config.ConfigBuff;
/*      */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*      */ import com.avrgaming.civcraft.config.ConfigPerk;
/*      */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*      */ import com.avrgaming.civcraft.database.ConnectionPool;
/*      */ import com.avrgaming.civcraft.database.SQL;
/*      */ import com.avrgaming.civcraft.event.EventTimer;
/*      */ import com.avrgaming.civcraft.event.GoodieRepoEvent;
/*      */ import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*      */ import com.avrgaming.civcraft.items.BonusGoodie;
/*      */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementSoulBound;
/*      */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*      */ import com.avrgaming.civcraft.lorestorage.LoreStoreage;
/*      */ import com.avrgaming.civcraft.main.CivCraft;
/*      */ import com.avrgaming.civcraft.main.CivGlobal;
/*      */ import com.avrgaming.civcraft.main.CivLog;
/*      */ import com.avrgaming.civcraft.main.CivMessage;
/*      */ import com.avrgaming.civcraft.mobs.MobSpawner;
/*      */ import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobLevel;
/*      */ import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobType;
/*      */ import com.avrgaming.civcraft.object.BuffManager;
/*      */ import com.avrgaming.civcraft.object.Civilization;
/*      */ import com.avrgaming.civcraft.object.CultureChunk;
/*      */ import com.avrgaming.civcraft.object.EconObject;
/*      */ import com.avrgaming.civcraft.object.Resident;
/*      */ import com.avrgaming.civcraft.object.StructureSign;
/*      */ import com.avrgaming.civcraft.object.Town;
/*      */ import com.avrgaming.civcraft.object.TownChunk;
/*      */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*      */ import com.avrgaming.civcraft.populators.TradeGoodPopulator;
/*      */ import com.avrgaming.civcraft.populators.TradeGoodPreGenerate;
/*      */ import com.avrgaming.civcraft.road.Road;
/*      */ import com.avrgaming.civcraft.siege.Cannon;
/*      */ import com.avrgaming.civcraft.structure.ArrowTower;
/*      */ import com.avrgaming.civcraft.structure.Buildable;
/*      */ import com.avrgaming.civcraft.structure.Capitol;
/*      */ import com.avrgaming.civcraft.structure.Structure;
/*      */ import com.avrgaming.civcraft.structure.TownHall;
/*      */ import com.avrgaming.civcraft.structure.Wall;
/*      */ import com.avrgaming.civcraft.tasks.TradeGoodSignCleanupTask;
/*      */ import com.avrgaming.civcraft.template.Template;
/*      */ import com.avrgaming.civcraft.template.TemplateStream;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.threading.tasks.ChunkGenerateTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.CultureProcessAsyncTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.PostBuildSyncTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.TradeGoodPostGenTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.TrommelAsyncTask;
/*      */ import com.avrgaming.civcraft.threading.timers.DailyTimer;
/*      */ import com.avrgaming.civcraft.tutorial.CivTutorial;
/*      */ import com.avrgaming.civcraft.util.AsciiMap;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.ChunkCoord;
/*      */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*      */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*      */ import com.avrgaming.civcraft.util.ItemManager;
/*      */ import com.avrgaming.civcraft.util.SimpleBlock;
/*      */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*      */ import com.avrgaming.global.perks.Perk;
/*      */ import com.jolbox.bonecp.Statistics;
/*      */ import gpl.AttributeUtil;
/*      */ import java.io.IOException;
/*      */ import java.math.BigInteger;
/*      */ import java.security.SecureRandom;
/*      */ import java.sql.SQLException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.UUID;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.Chunk;
/*      */ import org.bukkit.Color;
/*      */ import org.bukkit.Effect;
/*      */ import org.bukkit.FireworkEffect;
/*      */ import org.bukkit.FireworkEffect.Builder;
/*      */ import org.bukkit.FireworkEffect.Type;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.Sound;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.block.Biome;
/*      */ import org.bukkit.block.Block;
/*      */ import org.bukkit.block.BlockFace;
/*      */ import org.bukkit.block.Sign;
/*      */ import org.bukkit.command.CommandSender;
/*      */ import org.bukkit.entity.Entity;
/*      */ import org.bukkit.entity.EntityType;
/*      */ import org.bukkit.entity.Item;
/*      */ import org.bukkit.entity.ItemFrame;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.inventory.PlayerInventory;
/*      */ import org.bukkit.inventory.meta.ItemMeta;
/*      */ import org.bukkit.plugin.PluginManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DebugCommand
/*      */   extends CommandBase
/*      */ {
/*      */   public void init()
/*      */   {
/*  120 */     this.command = "/dbg";
/*  121 */     this.displayName = "Debug";
/*      */     
/*  123 */     this.commands.put("resident", "[name] - prints out the resident identified by name.");
/*  124 */     this.commands.put("town", "[name] - prints out the town identified by name.");
/*  125 */     this.commands.put("townchunk", " gets the town chunk you are standing in and prints it.");
/*  126 */     this.commands.put("newday", "Runs the new day code, collects taxes ... etc.");
/*  127 */     this.commands.put("civ", "[name] prints out civ info.");
/*  128 */     this.commands.put("map", "shows a town chunk map of the current area.");
/*  129 */     this.commands.put("culturechunk", "gets the culture chunk you are standing in and prints it.");
/*  130 */     this.commands.put("runculture", "runs the culture process algorithm.");
/*  131 */     this.commands.put("repo", "repos all goods back to outpost.");
/*  132 */     this.commands.put("show", "shows entity ids in this chunk.");
/*  133 */     this.commands.put("moveframes", "[x] [y] [z] moves item frames in this chunk to x,y,z");
/*  134 */     this.commands.put("frame", "gets player's town and shows the goodie frames in this town.");
/*  135 */     this.commands.put("makeframe", "[loc] [direction]");
/*  136 */     this.commands.put("dupe", "duplicates the item in your hand.");
/*  137 */     this.commands.put("test", "Run test suite commands.");
/*  138 */     this.commands.put("printgoodie", "[id] - prints the goodie in memory with this id.");
/*  139 */     this.commands.put("repogoodie", "[id] - repos the goodie with id.");
/*  140 */     this.commands.put("firework", "fires off a firework here.");
/*  141 */     this.commands.put("sound", "[name] [pitch]");
/*  142 */     this.commands.put("arrow", "[power] change arrow's power.");
/*  143 */     this.commands.put("wall", "wall Info about the chunk you're on.");
/*  144 */     this.commands.put("processculture", "forces a culture reprocess");
/*  145 */     this.commands.put("givebuff", "[id] gives this id buff to a town.");
/*  146 */     this.commands.put("unloadchunk", "[x] [z] - unloads this chunk.");
/*  147 */     this.commands.put("setspeed", "[speed] - set your speed to this");
/*  148 */     this.commands.put("tradegenerate", "generates trade goods at picked locations");
/*  149 */     this.commands.put("createtradegood", "[good_id] - creates a trade goodie here.");
/*  150 */     this.commands.put("cleartradesigns", "clears extra trade signs above trade outpots");
/*  151 */     this.commands.put("restoresigns", "restores all structure signs");
/*  152 */     this.commands.put("regenchunk", "regens every chunk that has a trade good in it");
/*  153 */     this.commands.put("quickcodereload", "Reloads the quick code plugin");
/*  154 */     this.commands.put("loadbans", "Loads bans from ban list into global table");
/*  155 */     this.commands.put("setallculture", "[amount] - sets all towns culture in the world to this amount.");
/*  156 */     this.commands.put("timers", "show all the timer information.");
/*  157 */     this.commands.put("shownbt", "shows the current nbt data for this item in the logs");
/*  158 */     this.commands.put("addnbt", "adds a custom tag to the item in your hand.");
/*  159 */     this.commands.put("loretest", "tests if the magic lore is set.");
/*  160 */     this.commands.put("loreset", "adds magic lore tag.");
/*  161 */     this.commands.put("giveold", "[name] [first lore]");
/*  162 */     this.commands.put("farm", "show debug commands for farms");
/*  163 */     this.commands.put("flashedges", "[town] flash edge blocks for town.");
/*  164 */     this.commands.put("refreshchunk", "refreshes the chunk you're standing in.. for science.");
/*  165 */     this.commands.put("touches", "[town] - prints a list of friendly touches for this town's culture.");
/*  166 */     this.commands.put("listconquered", "shows a list of conquered civilizations.");
/*  167 */     this.commands.put("camp", "Debugs camps.");
/*  168 */     this.commands.put("blockinfo", "[x] [y] [z] shows block info for this block.");
/*  169 */     this.commands.put("trommel", "[name] - turn on this town's trommel debugging.");
/*  170 */     this.commands.put("fakeresidents", "[town] [count] - Adds this many fake residents to a town.");
/*  171 */     this.commands.put("clearresidents", "[town] - clears this town of it's random residents.");
/*  172 */     this.commands.put("biomehere", "- shows you biome info where you're standing.");
/*  173 */     this.commands.put("scout", "[civ] - enables debugging for scout towers in this civ.");
/*  174 */     this.commands.put("getit", "gives you an item.");
/*  175 */     this.commands.put("showinv", "shows you an inventory");
/*  176 */     this.commands.put("showcraftinv", "shows you crafting inventory");
/*  177 */     this.commands.put("setspecial", "sets special stuff");
/*  178 */     this.commands.put("getspecial", "gets the special stuff");
/*  179 */     this.commands.put("setcivnbt", "[key] [value] - adds this key.");
/*  180 */     this.commands.put("getcivnbt", "[key] - gets this key");
/*  181 */     this.commands.put("getmid", "Gets the MID of this item.");
/*  182 */     this.commands.put("getdura", "gets the durability of an item");
/*  183 */     this.commands.put("setdura", "sets the durability of an item");
/*  184 */     this.commands.put("togglebookcheck", "Toggles checking for enchanted books on and off.");
/*  185 */     this.commands.put("setexposure", "[int] sets your exposure to this ammount.");
/*  186 */     this.commands.put("circle", "[int] - draws a circle at your location, with this radius.");
/*  187 */     this.commands.put("loadperks", "loads perks for yourself");
/*  188 */     this.commands.put("colorme", "[hex] adds nbt color value to item held.");
/*  189 */     this.commands.put("preview", "show a single block preview at your feet.");
/*  190 */     this.commands.put("sql", "Show SQL health info.");
/*  191 */     this.commands.put("templatetest", "tests out some new template stream code.");
/*  192 */     this.commands.put("buildspawn", "[civname] [capitolname] Builds spawn from spawn template.");
/*  193 */     this.commands.put("matmap", "prints the material map.");
/*  194 */     this.commands.put("ping", "print something.");
/*  195 */     this.commands.put("datebypass", "Bypasses certain date restrictions");
/*  196 */     this.commands.put("spawn", "remote entities test");
/*  197 */     this.commands.put("heal", "heals you....");
/*  198 */     this.commands.put("skull", "[player] [title]");
/*  199 */     this.commands.put("giveperk", "<id> gives yourself this perk id.");
/*  200 */     this.commands.put("packet", "sends custom auth packet.");
/*  201 */     this.commands.put("disablemap", "disables zan's minimap");
/*  202 */     this.commands.put("world", "Show world debug options");
/*  203 */     this.commands.put("cannon", "builds a war cannon.");
/*  204 */     this.commands.put("saveinv", "save an inventory");
/*  205 */     this.commands.put("restoreinv", "restore your inventory.");
/*  206 */     this.commands.put("arenainfo", "Shows arena info for this player.");
/*      */   }
/*      */   
/*      */   public void arenainfo_cmd() throws CivException {
/*  210 */     Resident resident = getResident();
/*  211 */     String arenaName = "";
/*      */     
/*  213 */     if ((resident.getTeam() != null) && (resident.getTeam().getCurrentArena() != null)) {
/*  214 */       arenaName = resident.getTeam().getCurrentArena().getInstanceName();
/*      */     }
/*      */     
/*      */ 
/*  218 */     CivMessage.send(this.sender, "InsideArena:" + resident.isInsideArena() + " Team Active arena:" + arenaName);
/*      */   }
/*      */   
/*      */   public void saveinv_cmd() throws CivException {
/*  222 */     Resident resident = getResident();
/*  223 */     resident.saveInventory();
/*  224 */     CivMessage.sendSuccess(resident, "saved inventory.");
/*      */   }
/*      */   
/*      */   public void restoreinv_cmd() throws CivException {
/*  228 */     Resident resident = getResident();
/*  229 */     resident.restoreInventory();
/*  230 */     CivMessage.sendSuccess(resident, "restore inventory.");
/*      */   }
/*      */   
/*      */   public void cannon_cmd() throws CivException {
/*  234 */     Resident resident = getResident();
/*  235 */     Cannon.newCannon(resident);
/*      */     
/*  237 */     CivMessage.sendSuccess(resident, "built cannon.");
/*      */   }
/*      */   
/*      */   public void world_cmd() {
/*  241 */     DebugWorldCommand cmd = new DebugWorldCommand();
/*  242 */     cmd.onCommand(this.sender, null, "world", stripArgs(this.args, 1));
/*      */   }
/*      */   
/*      */   public void disablemap_cmd() throws CivException {
/*  246 */     Player player = getPlayer();
/*  247 */     player.sendMessage("§3§6§3§6§3§6§e");
/*  248 */     player.sendMessage("§3§6§3§6§3§6§d");
/*  249 */     CivMessage.sendSuccess(player, "Disabled.");
/*      */   }
/*      */   
/*      */   public void packet_cmd() throws CivException {
/*  253 */     Player player = getPlayer();
/*  254 */     player.sendPluginMessage(CivCraft.getPlugin(), "CAC", "Test Message".getBytes());
/*  255 */     CivMessage.sendSuccess(player, "Sent test message");
/*      */   }
/*      */   
/*      */   public void giveperk_cmd() throws CivException {
/*  259 */     Resident resident = getResident();
/*  260 */     String perkId = getNamedString(1, "Enter a perk ID");
/*  261 */     ConfigPerk configPerk = (ConfigPerk)CivSettings.perks.get(perkId);
/*      */     
/*  263 */     Perk p2 = (Perk)resident.perks.get(configPerk.id);
/*  264 */     if (p2 != null) {
/*  265 */       p2.count += 1;
/*  266 */       resident.perks.put(p2.getIdent(), p2);
/*      */     } else {
/*  268 */       Perk p = new Perk(configPerk);
/*  269 */       resident.perks.put(p.getIdent(), p);
/*  270 */       p2 = p;
/*      */     }
/*      */     
/*  273 */     CivMessage.sendSuccess(resident, "Added perk:" + p2.getDisplayName());
/*      */   }
/*      */   
/*      */   public void skull_cmd() throws CivException {
/*  277 */     Player player = getPlayer();
/*  278 */     String playerName = getNamedString(1, "Enter a player name");
/*  279 */     String message = getNamedString(2, "Enter a title.");
/*      */     
/*  281 */     ItemStack skull = ItemManager.spawnPlayerHead(playerName, message);
/*  282 */     player.getInventory().addItem(new ItemStack[] { skull });
/*  283 */     CivMessage.sendSuccess(player, "Added skull item.");
/*      */   }
/*      */   
/*      */   public void heal_cmd() throws CivException {
/*  287 */     Player player = getPlayer();
/*  288 */     player.setHealth(player.getMaxHealth());
/*  289 */     player.setFoodLevel(50);
/*  290 */     CivMessage.send(player, "Healed....");
/*      */   }
/*      */   
/*      */   public void spawn_cmd() throws CivException {
/*  294 */     Player player = getPlayer();
/*  295 */     String mob = getNamedString(1, "name");
/*  296 */     String lvl = getNamedString(2, "level");
/*      */     
/*  298 */     MobSpawner.CustomMobType type = MobSpawner.CustomMobType.valueOf(mob.toUpperCase());
/*  299 */     MobSpawner.CustomMobLevel level = MobSpawner.CustomMobLevel.valueOf(lvl.toUpperCase());
/*      */     
/*  301 */     if (type == null) {
/*  302 */       throw new CivException("no mob named:" + mob);
/*      */     }
/*      */     
/*  305 */     if (level == null) {
/*  306 */       throw new CivException("no level named:" + lvl);
/*      */     }
/*      */     
/*  309 */     MobSpawner.spawnCustomMob(type, level, player.getLocation());
/*      */   }
/*      */   
/*      */   public void datebypass_cmd() {
/*  313 */     CivGlobal.debugDateBypass = !CivGlobal.debugDateBypass;
/*  314 */     CivMessage.send(this.sender, "Date bypass is now:" + CivGlobal.debugDateBypass);
/*      */   }
/*      */   
/*      */   public void ping_cmd() {
/*  318 */     CivMessage.send(this.sender, "test....");
/*      */   }
/*      */   
/*      */   public void matmap_cmd() throws CivException {
/*  322 */     Player player = getPlayer();
/*      */     
/*  324 */     for (String mid : LoreMaterial.materialMap.keySet()) {
/*  325 */       CivMessage.send(player, mid);
/*  326 */       LoreMaterial mat = (LoreMaterial)LoreMaterial.materialMap.get(mid);
/*  327 */       CivLog.info("material map:" + mid + " mat:" + mat);
/*      */     }
/*      */   }
/*      */   
/*      */   public void buildspawn_cmd()
/*      */     throws CivException
/*      */   {
/*  334 */     String civName = getNamedString(1, "Enter a Civ name/");
/*  335 */     String capitolName = getNamedString(2, "Enter a capitol name.");
/*  336 */     Resident resident = getResident();
/*      */     
/*      */     try
/*      */     {
/*  340 */       Civilization spawnCiv = new Civilization(civName, capitolName, resident);
/*  341 */       spawnCiv.saveNow();
/*      */       
/*      */ 
/*  344 */       Town spawnCapitol = new Town(capitolName, resident, spawnCiv);
/*  345 */       spawnCapitol.saveNow();
/*      */       
/*  347 */       PermissionGroup leaders = new PermissionGroup(spawnCiv, "leaders");
/*  348 */       spawnCiv.addGroup(leaders);
/*  349 */       leaders.addMember(resident);
/*  350 */       spawnCiv.setLeader(resident);
/*  351 */       spawnCiv.setLeaderGroup(leaders);
/*  352 */       leaders.save();
/*      */       
/*  354 */       PermissionGroup advisers = new PermissionGroup(spawnCiv, "advisers");
/*  355 */       spawnCiv.addGroup(advisers);
/*  356 */       spawnCiv.setAdviserGroup(advisers);
/*  357 */       advisers.save();
/*      */       
/*  359 */       PermissionGroup mayors = new PermissionGroup(spawnCapitol, "mayors");
/*  360 */       spawnCapitol.addGroup(mayors);
/*  361 */       spawnCapitol.setMayorGroup(mayors);
/*  362 */       mayors.addMember(resident);
/*  363 */       mayors.save();
/*      */       
/*  365 */       PermissionGroup assistants = new PermissionGroup(spawnCapitol, "assistants");
/*  366 */       spawnCapitol.addGroup(assistants);
/*  367 */       spawnCapitol.setAssistantGroup(assistants);
/*  368 */       assistants.save();
/*      */       
/*  370 */       PermissionGroup residents = new PermissionGroup(spawnCapitol, "residents");
/*  371 */       spawnCapitol.addGroup(residents);
/*  372 */       spawnCapitol.setDefaultGroup(residents);
/*  373 */       residents.save();
/*      */       
/*  375 */       spawnCiv.addTown(spawnCapitol);
/*  376 */       spawnCiv.setCapitolName(spawnCapitol.getName());
/*      */       
/*  378 */       spawnCiv.setAdminCiv(true);
/*  379 */       spawnCiv.save();
/*  380 */       spawnCapitol.save();
/*  381 */       resident.save();
/*      */       
/*  383 */       CivGlobal.addTown(spawnCapitol);
/*  384 */       CivGlobal.addCiv(spawnCiv);
/*      */       
/*      */       try
/*      */       {
/*  388 */         spawnCapitol.addResident(resident);
/*      */       } catch (AlreadyRegisteredException e) {
/*  390 */         e.printStackTrace();
/*  391 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  533 */       TaskMaster.syncTask(new Runnable()
/*      */       {
/*      */         CommandSender sender;
/*      */         int start_x;
/*      */         int start_y;
/*      */         int start_z;
/*      */         Town spawnCapitol;
/*      */         
/*      */         public void run()
/*      */         {
/*      */           try
/*      */           {
/*  414 */             Template tpl = new Template();
/*      */             try {
/*  416 */               tpl.load_template("templates/spawn.def");
/*      */             } catch (IOException e) {
/*  418 */               e.printStackTrace();
/*  419 */               throw new CivException("IO Error.");
/*      */             }
/*      */             
/*  422 */             Player player = (Player)this.sender;
/*  423 */             ConfigBuildableInfo info = new ConfigBuildableInfo();
/*  424 */             info.tile_improvement = Boolean.valueOf(false);
/*  425 */             info.templateYShift = 0;
/*  426 */             Location center = Buildable.repositionCenterStatic(player.getLocation(), info, 
/*  427 */               Template.getDirection(player.getLocation()), tpl.size_x, tpl.size_z);
/*      */             
/*  429 */             CivMessage.send(this.sender, "Building from " + this.start_x + "," + this.start_y + "," + this.start_z);
/*  430 */             for (int y = this.start_y; y < tpl.size_y; y++) {
/*  431 */               for (int x = this.start_x; x < tpl.size_x; x++) {
/*  432 */                 for (int z = this.start_z; z < tpl.size_z; z++) {
/*  433 */                   BlockCoord next = new BlockCoord(center);
/*  434 */                   next.setX(next.getX() + x);
/*  435 */                   next.setY(next.getY() + y);
/*  436 */                   next.setZ(next.getZ() + z);
/*      */                   
/*  438 */                   SimpleBlock sb = tpl.blocks[x][y][z];
/*      */                   
/*  440 */                   if (sb.specialType.equals(SimpleBlock.Type.COMMAND)) {
/*  441 */                     String buildableName = sb.command.replace("/", "");
/*      */                     
/*      */ 
/*  444 */                     info = null;
/*  445 */                     for (ConfigBuildableInfo buildInfo : CivSettings.structures.values()) {
/*  446 */                       if (buildInfo.displayName.equalsIgnoreCase(buildableName)) {
/*  447 */                         info = buildInfo;
/*  448 */                         break;
/*      */                       }
/*      */                     }
/*  451 */                     if (info == null) {
/*      */                       try {
/*  453 */                         Block block = next.getBlock();
/*  454 */                         ItemManager.setTypeIdAndData(block, 0, 0, false);
/*      */                       }
/*      */                       catch (Exception e) {
/*  457 */                         e.printStackTrace();
/*      */                       }
/*      */                     }
/*      */                     else
/*      */                     {
/*  462 */                       CivMessage.send(this.sender, "Setting up " + buildableName);
/*  463 */                       int yShift = 0;
/*  464 */                       String[] lines = sb.getKeyValueString().split(",");
/*  465 */                       String[] split = lines[0].split(":");
/*  466 */                       String dir = split[0];
/*  467 */                       yShift = Integer.valueOf(split[1]).intValue();
/*      */                       
/*  469 */                       Location loc = next.getLocation();
/*  470 */                       loc.setY(loc.getY() + yShift);
/*      */                       
/*  472 */                       Structure struct = Structure.newStructure(loc, info.id, this.spawnCapitol);
/*  473 */                       if ((struct instanceof Capitol)) {
/*  474 */                         AdminTownCommand.claimradius(this.spawnCapitol, center, Integer.valueOf(15));
/*      */                       }
/*  476 */                       struct.setTemplateName("templates/themes/default/structures/" + info.template_base_name + "/" + info.template_base_name + "_" + dir + ".def");
/*  477 */                       struct.bindStructureBlocks();
/*  478 */                       struct.setComplete(true);
/*  479 */                       struct.setHitpoints(info.max_hitpoints);
/*  480 */                       CivGlobal.addStructure(struct);
/*  481 */                       this.spawnCapitol.addStructure(struct);
/*      */                       
/*      */                       try
/*      */                       {
/*  485 */                         Template tplStruct = Template.getTemplate(struct.getSavedTemplatePath(), null);
/*  486 */                         TaskMaster.syncTask(new PostBuildSyncTask(tplStruct, struct));
/*      */                       } catch (IOException e) {
/*  488 */                         e.printStackTrace();
/*  489 */                         throw new CivException("IO Exception.");
/*      */                       }
/*      */                       Template tplStruct;
/*  492 */                       struct.save();
/*  493 */                       this.spawnCapitol.save();
/*      */                     }
/*  495 */                   } else if (sb.specialType.equals(SimpleBlock.Type.LITERAL)) {
/*      */                     try {
/*  497 */                       Block block = next.getBlock();
/*  498 */                       ItemManager.setTypeIdAndData(block, sb.getType(), sb.getData(), false);
/*      */                       
/*  500 */                       Sign s = (Sign)block.getState();
/*  501 */                       for (int j = 0; j < 4; j++) {
/*  502 */                         s.setLine(j, sb.message[j]);
/*      */                       }
/*      */                       
/*  505 */                       s.update();
/*      */                     } catch (Exception e) {
/*  507 */                       e.printStackTrace();
/*      */                     }
/*      */                   } else {
/*      */                     try {
/*  511 */                       Block block = next.getBlock();
/*  512 */                       ItemManager.setTypeIdAndData(block, sb.getType(), sb.getData(), false);
/*      */                     } catch (Exception e) {
/*  514 */                       e.printStackTrace();
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*  521 */             CivMessage.send(this.sender, "Finished building.");
/*      */             
/*  523 */             this.spawnCapitol.addAccumulatedCulture(6.0E7D);
/*  524 */             this.spawnCapitol.save();
/*      */           }
/*      */           catch (CivException e) {
/*  527 */             e.printStackTrace();
/*  528 */             CivMessage.send(this.sender, e.getMessage());
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */     catch (InvalidNameException e)
/*      */     {
/*  535 */       throw new CivException(e.getMessage());
/*      */     } catch (SQLException e) {
/*  537 */       e.printStackTrace();
/*  538 */       throw new CivException("Internal DB Error.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  543 */   public static TemplateStream tplStream = null;
/*      */   
/*  545 */   public void templatetest_cmd() throws CivException { Player player = getPlayer();
/*  546 */     String filename = getNamedString(1, "Enter a filename");
/*  547 */     Integer yLayer = getNamedInteger(2);
/*      */     
/*  549 */     if (tplStream == null) {
/*      */       try {
/*  551 */         tplStream = new TemplateStream(filename);
/*      */       } catch (IOException e) {
/*  553 */         e.printStackTrace();
/*  554 */         return;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  559 */       tplStream.getBlocksForLayer(yLayer.intValue());
/*      */     } catch (IOException e) {
/*  561 */       e.printStackTrace();
/*      */     }
/*  563 */     tplStream.debugBuildBlocksHere(player.getLocation());
/*      */   }
/*      */   
/*      */   public void sql_cmd()
/*      */   {
/*  568 */     HashMap<String, String> stats = new HashMap();
/*  569 */     stats.put("ConnectionsRequested", SQL.gameDatabase.getStats().getConnectionsRequested());
/*  570 */     stats.put("Free Pool Members", SQL.gameDatabase.getStats().getTotalFree());
/*  571 */     stats.put("Leased Pool Members", SQL.gameDatabase.getStats().getTotalLeased());
/*  572 */     CivMessage.send(this.sender, makeInfoString(stats, "§2", "§a"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void preview_cmd()
/*      */     throws CivException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void colorme_cmd()
/*      */     throws CivException
/*      */   {
/*  595 */     Player player = getPlayer();
/*  596 */     String hex = getNamedString(1, "color code");
/*  597 */     long value = Long.decode(hex).longValue();
/*      */     
/*  599 */     ItemStack inHand = player.getItemInHand();
/*  600 */     if ((inHand == null) || (ItemManager.getId(inHand) == 0)) {
/*  601 */       throw new CivException("please have an item in your hand.");
/*      */     }
/*      */     
/*  604 */     AttributeUtil attrs = new AttributeUtil(inHand);
/*  605 */     attrs.setColor(Long.valueOf(value));
/*  606 */     player.setItemInHand(attrs.getStack());
/*  607 */     CivMessage.sendSuccess(player, "Set color.");
/*      */   }
/*      */   
/*      */   public void circle_cmd() throws CivException {
/*  611 */     Player player = getPlayer();
/*  612 */     int radius = getNamedInteger(1).intValue();
/*      */     
/*  614 */     HashMap<String, SimpleBlock> simpleBlocks = new HashMap();
/*  615 */     Road.getCircle(player.getLocation().getBlockX(), 
/*  616 */       player.getLocation().getBlockY() - 1, 
/*  617 */       player.getLocation().getBlockZ(), 
/*  618 */       player.getLocation().getWorld().getName(), 
/*  619 */       radius, simpleBlocks);
/*      */     
/*  621 */     for (SimpleBlock sb : simpleBlocks.values()) {
/*  622 */       Block block = player.getWorld().getBlockAt(sb.x, sb.y, sb.z);
/*  623 */       ItemManager.setTypeId(block, sb.getType());
/*      */     }
/*      */     
/*  626 */     CivMessage.sendSuccess(player, "Built a circle at your feet.");
/*      */   }
/*      */   
/*      */   public void setexposure_cmd() throws CivException {
/*  630 */     Resident resident = getResident();
/*  631 */     Player player = getPlayer();
/*  632 */     Double exp = getNamedDouble(1);
/*  633 */     resident.setSpyExposure(exp.doubleValue());
/*      */     
/*  635 */     CivMessage.sendSuccess(player, "Set Exposure.");
/*      */   }
/*      */   
/*      */   public void togglebookcheck_cmd() {
/*  639 */     CivGlobal.checkForBooks = !CivGlobal.checkForBooks;
/*  640 */     CivMessage.sendSuccess(this.sender, "Check for books is:" + CivGlobal.checkForBooks);
/*      */   }
/*      */   
/*      */   public void setcivnbt_cmd() throws CivException {
/*  644 */     Player player = getPlayer();
/*  645 */     String key = getNamedString(1, "key");
/*  646 */     String value = getNamedString(2, "value");
/*      */     
/*  648 */     ItemStack inHand = player.getItemInHand();
/*  649 */     if (inHand == null) {
/*  650 */       throw new CivException("You must have an item in hand.");
/*      */     }
/*      */     
/*  653 */     AttributeUtil attrs = new AttributeUtil(inHand);
/*  654 */     attrs.setCivCraftProperty(key, value);
/*  655 */     player.setItemInHand(attrs.getStack());
/*  656 */     CivMessage.sendSuccess(player, "Set property.");
/*      */   }
/*      */   
/*      */   public void getcivnbt_cmd() throws CivException
/*      */   {
/*  661 */     Player player = getPlayer();
/*  662 */     String key = getNamedString(1, "key");
/*      */     
/*  664 */     ItemStack inHand = player.getItemInHand();
/*  665 */     if (inHand == null) {
/*  666 */       throw new CivException("You must have an item in hand.");
/*      */     }
/*      */     
/*  669 */     AttributeUtil attrs = new AttributeUtil(inHand);
/*  670 */     String value = attrs.getCivCraftProperty(key);
/*  671 */     CivMessage.sendSuccess(player, "got property:" + value);
/*      */   }
/*      */   
/*      */   public void getdura_cmd() throws CivException {
/*  675 */     Player player = getPlayer();
/*  676 */     ItemStack inHand = player.getItemInHand();
/*  677 */     CivMessage.send(player, "Durability:" + inHand.getDurability());
/*  678 */     CivMessage.send(player, "MaxDura:" + inHand.getType().getMaxDurability());
/*      */   }
/*      */   
/*      */   public void setdura_cmd() throws CivException
/*      */   {
/*  683 */     Player player = getPlayer();
/*  684 */     Integer dura = getNamedInteger(1);
/*      */     
/*  686 */     ItemStack inHand = player.getItemInHand();
/*  687 */     inHand.setDurability(dura.shortValue());
/*      */     
/*  689 */     CivMessage.send(player, "Set Durability:" + inHand.getDurability());
/*  690 */     CivMessage.send(player, "MaxDura:" + inHand.getType().getMaxDurability());
/*      */   }
/*      */   
/*      */   public void getmid_cmd() throws CivException
/*      */   {
/*  695 */     Player player = getPlayer();
/*  696 */     ItemStack inHand = player.getItemInHand();
/*  697 */     if (inHand == null) {
/*  698 */       throw new CivException("You need an item in your hand.");
/*      */     }
/*      */     
/*  701 */     CivMessage.send(player, "MID:" + LoreMaterial.getMID(inHand));
/*      */   }
/*      */   
/*      */   public void setspecial_cmd() throws CivException {
/*  705 */     Player player = getPlayer();
/*  706 */     ItemStack inHand = player.getItemInHand();
/*  707 */     if (inHand == null) {
/*  708 */       throw new CivException("You need an item in your hand.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  713 */     ItemStack stack = LoreMaterial.addEnhancement(inHand, new LoreEnhancementSoulBound());
/*  714 */     player.setItemInHand(stack);
/*  715 */     CivMessage.send(player, "Set it.");
/*      */   }
/*      */   
/*      */   public void getspecial_cmd() throws CivException {
/*  719 */     Player player = getPlayer();
/*  720 */     ItemStack inHand = player.getItemInHand();
/*  721 */     if (inHand == null) {
/*  722 */       throw new CivException("You need an item in your hand.");
/*      */     }
/*      */     
/*  725 */     AttributeUtil attrs = new AttributeUtil(inHand);
/*  726 */     String value = attrs.getCivCraftProperty("soulbound");
/*      */     
/*  728 */     CivMessage.send(player, "Got:" + value);
/*      */   }
/*      */   
/*      */   public void showinv_cmd() throws CivException {
/*  732 */     CivTutorial.spawnGuiBook(getPlayer());
/*      */   }
/*      */   
/*      */   public void showcraftinv_cmd() throws CivException {
/*  736 */     CivTutorial.showCraftingHelp(getPlayer());
/*      */   }
/*      */   
/*      */   public void scout_cmd() throws CivException
/*      */   {
/*  741 */     Civilization civ = getNamedCiv(1);
/*      */     
/*  743 */     if (!civ.scoutDebug) {
/*  744 */       civ.scoutDebug = true;
/*  745 */       civ.scoutDebugPlayer = getPlayer().getName();
/*  746 */       CivMessage.sendSuccess(this.sender, "Enabled scout tower debugging in " + civ.getName());
/*      */     } else {
/*  748 */       civ.scoutDebug = false;
/*  749 */       civ.scoutDebugPlayer = null;
/*  750 */       CivMessage.sendSuccess(this.sender, "Disabled scout tower debugging in " + civ.getName());
/*      */     }
/*      */   }
/*      */   
/*      */   public void biomehere_cmd() throws CivException {
/*  755 */     Player player = getPlayer();
/*      */     
/*  757 */     Biome biome = player.getWorld().getBiome(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
/*  758 */     CivMessage.send(player, "Got biome:" + biome.name());
/*      */   }
/*      */   
/*      */   public void clearresidents_cmd() throws CivException {
/*  762 */     Town town = getNamedTown(1);
/*      */     
/*  764 */     ArrayList<Resident> removeUs = new ArrayList();
/*  765 */     for (Resident resident : town.getResidents()) {
/*  766 */       if (resident.getName().startsWith("RANDOM_")) {
/*  767 */         removeUs.add(resident);
/*      */       }
/*      */     }
/*      */     
/*  771 */     for (Resident resident : removeUs) {
/*  772 */       town.removeResident(resident);
/*      */     }
/*      */   }
/*      */   
/*      */   public void fakeresidents_cmd() throws CivException {
/*  777 */     Town town = getNamedTown(1);
/*  778 */     Integer count = getNamedInteger(2);
/*      */     
/*  780 */     for (int i = 0; i < count.intValue(); i++) {
/*  781 */       SecureRandom random = new SecureRandom();
/*  782 */       String name = new BigInteger(130, random).toString(32);
/*      */       try
/*      */       {
/*  785 */         Resident fake = new Resident("RANDOM_" + name);
/*  786 */         town.addResident(fake);
/*  787 */         town.addFakeResident(fake);
/*      */       }
/*      */       catch (AlreadyRegisteredException localAlreadyRegisteredException) {}catch (InvalidNameException localInvalidNameException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  794 */     CivMessage.sendSuccess(this.sender, "Added " + count + " residents.");
/*      */   }
/*      */   
/*      */   public void trommel_cmd() throws CivException {
/*  798 */     Town town = getNamedTown(1);
/*      */     
/*  800 */     if (TrommelAsyncTask.debugTowns.contains(town.getName())) {
/*  801 */       TrommelAsyncTask.debugTowns.remove(town.getName());
/*      */     } else {
/*  803 */       TrommelAsyncTask.debugTowns.add(town.getName());
/*      */     }
/*      */     
/*  806 */     CivMessage.send(this.sender, "Trommel toggled.");
/*      */   }
/*      */   
/*      */   public void blockinfo_cmd() throws CivException {
/*  810 */     int x = getNamedInteger(1).intValue();
/*  811 */     int y = getNamedInteger(2).intValue();
/*  812 */     int z = getNamedInteger(3).intValue();
/*      */     
/*  814 */     Block b = Bukkit.getWorld("world").getBlockAt(x, y, z);
/*      */     
/*  816 */     CivMessage.send(this.sender, "type:" + ItemManager.getId(b) + " data:" + ItemManager.getData(b) + " name:" + b.getType().name());
/*      */   }
/*      */   
/*      */   public void camp_cmd()
/*      */   {
/*  821 */     DebugCampCommand cmd = new DebugCampCommand();
/*  822 */     cmd.onCommand(this.sender, null, "farm", stripArgs(this.args, 1));
/*      */   }
/*      */   
/*      */   public void listconquered_cmd() {
/*  826 */     CivMessage.sendHeading(this.sender, "Conquered Civs");
/*  827 */     String out = "";
/*  828 */     for (Civilization civ : CivGlobal.getConqueredCivs()) {
/*  829 */       out = out + civ.getName() + ", ";
/*      */     }
/*  831 */     CivMessage.send(this.sender, out);
/*      */   }
/*      */   
/*      */   public void touches_cmd() throws CivException {
/*  835 */     Town town = getNamedTown(1);
/*      */     
/*  837 */     CivMessage.sendHeading(this.sender, "Touching Towns");
/*  838 */     String out = "";
/*  839 */     for (Town t : town.townTouchList) {
/*  840 */       out = out + t.getName() + ", ";
/*      */     }
/*      */     
/*  843 */     if (town.touchesCapitolCulture(new HashSet())) {
/*  844 */       CivMessage.send(this.sender, "§aTouches capitol.");
/*      */     } else {
/*  846 */       CivMessage.send(this.sender, "§cDoes NOT touch capitol.");
/*      */     }
/*      */     
/*  849 */     CivMessage.send(this.sender, out);
/*      */   }
/*      */   
/*      */   public void refreshchunk_cmd() throws CivException {
/*  853 */     Player you = getPlayer();
/*  854 */     ChunkCoord coord = new ChunkCoord(you.getLocation());
/*      */     Player[] arrayOfPlayer;
/*  856 */     int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length; for (int i = 0; i < j; i++) { Player player = arrayOfPlayer[i];
/*  857 */       player.getWorld().refreshChunk(coord.getX(), coord.getZ());
/*      */     }
/*      */   }
/*      */   
/*      */   public void flashedges_cmd() throws CivException {
/*  862 */     Town town = getNamedTown(1);
/*      */     int x;
/*  864 */     for (Iterator localIterator = town.savedEdgeBlocks.iterator(); localIterator.hasNext(); 
/*  865 */         x < 16)
/*      */     {
/*  864 */       TownChunk chunk = (TownChunk)localIterator.next();
/*  865 */       x = 0; continue;
/*  866 */       for (int z = 0; z < 16; z++) {
/*  867 */         Block b = Bukkit.getWorld("world").getHighestBlockAt((chunk.getChunkCoord().getX() + x << 4) + x, 
/*  868 */           (chunk.getChunkCoord().getZ() << 4) + z);
/*  869 */         Bukkit.getWorld("world").playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
/*      */       }
/*  865 */       x++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  873 */     CivMessage.sendSuccess(this.sender, "flashed");
/*      */   }
/*      */   
/*      */   public void farm_cmd() {
/*  877 */     DebugFarmCommand cmd = new DebugFarmCommand();
/*  878 */     cmd.onCommand(this.sender, null, "farm", stripArgs(this.args, 1));
/*      */   }
/*      */   
/*      */   public void giveold_cmd() throws CivException {
/*  882 */     Player player = getPlayer();
/*      */     
/*  884 */     if (this.args.length < 3) {
/*  885 */       throw new CivException("Enter name and first lore line.");
/*      */     }
/*      */     
/*  888 */     ItemStack inHand = player.getItemInHand();
/*  889 */     if (inHand != null)
/*      */     {
/*      */ 
/*  892 */       ItemMeta meta = inHand.getItemMeta();
/*  893 */       meta.setDisplayName(this.args[1]);
/*      */       
/*  895 */       ArrayList<String> lore = new ArrayList();
/*  896 */       lore.add(combineArgs(stripArgs(this.args, 2)));
/*  897 */       meta.setLore(lore);
/*      */       
/*  899 */       inHand.setItemMeta(meta);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void loretest_cmd()
/*      */     throws CivException
/*      */   {
/*  915 */     Player player = getPlayer();
/*      */     
/*  917 */     ItemStack inHand = player.getItemInHand();
/*  918 */     if (inHand != null) {
/*  919 */       ItemMeta meta = inHand.getItemMeta();
/*  920 */       List<String> newLore = meta.getLore();
/*  921 */       if ((newLore != null) && (newLore.size() > 0) && (((String)newLore.get(0)).equalsIgnoreCase("RJMAGIC"))) {
/*  922 */         CivMessage.sendSuccess(player, "found magic lore");
/*      */       } else {
/*  924 */         CivMessage.sendSuccess(player, "No magic lore.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void loreset_cmd() throws CivException {
/*  930 */     Player player = getPlayer();
/*      */     
/*  932 */     ItemStack inHand = player.getItemInHand();
/*  933 */     if (inHand != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  942 */       LoreStoreage.setMatID(1337, inHand);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void timers_cmd()
/*      */   {
/*  966 */     CivMessage.sendHeading(this.sender, "Timers");
/*  967 */     SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/*      */     
/*  969 */     CivMessage.send(this.sender, "Now:" + sdf.format(new Date()));
/*  970 */     for (EventTimer timer : EventTimer.timers.values())
/*      */     {
/*      */ 
/*  973 */       CivMessage.send(this.sender, timer.getName());
/*  974 */       CivMessage.send(this.sender, "    next:" + sdf.format(timer.getNext()));
/*  975 */       if (timer.getLast().getTime().getTime() == 0L) {
/*  976 */         CivMessage.send(this.sender, "    last: never");
/*      */       } else {
/*  978 */         CivMessage.send(this.sender, "    last:" + sdf.format(timer.getLast()));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setallculture_cmd()
/*      */     throws CivException
/*      */   {
/*  986 */     Integer culture = getNamedInteger(1);
/*      */     
/*  988 */     for (Town town : CivGlobal.getTowns()) {
/*  989 */       town.addAccumulatedCulture(culture.intValue());
/*  990 */       town.save();
/*      */     }
/*      */     
/*  993 */     CivGlobal.processCulture();
/*  994 */     CivMessage.sendSuccess(this.sender, "Set all town culture to " + culture + " points.");
/*      */   }
/*      */   
/*      */   public void quickcodereload_cmd()
/*      */   {
/*  999 */     Bukkit.getPluginManager().getPlugin("QuickCode");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void regenchunk_cmd()
/*      */   {
/* 1006 */     World world = Bukkit.getWorld("world");
/*      */     
/* 1008 */     for (ChunkCoord coord : CivGlobal.preGenerator.goodPicks.keySet())
/*      */     {
/* 1010 */       world.regenerateChunk(coord.getX(), coord.getZ());
/* 1011 */       CivMessage.send(this.sender, "Regened:" + coord);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void restoresigns_cmd()
/*      */   {
/* 1019 */     CivMessage.send(this.sender, "restoring....");
/* 1020 */     for (StructureSign sign : CivGlobal.getStructureSigns())
/*      */     {
/* 1022 */       BlockCoord bcoord = sign.getCoord();
/* 1023 */       Block block = bcoord.getBlock();
/* 1024 */       ItemManager.setTypeId(block, 68);
/* 1025 */       ItemManager.setData(block, sign.getDirection());
/*      */       
/* 1027 */       Sign s = (Sign)block.getState();
/* 1028 */       String[] lines = sign.getText().split("\n");
/*      */       
/* 1030 */       if (lines.length > 0) {
/* 1031 */         s.setLine(0, lines[0]);
/*      */       }
/* 1033 */       if (lines.length > 1) {
/* 1034 */         s.setLine(1, lines[1]);
/*      */       }
/*      */       
/* 1037 */       if (lines.length > 2) {
/* 1038 */         s.setLine(2, lines[2]);
/*      */       }
/*      */       
/* 1041 */       if (lines.length > 3) {
/* 1042 */         s.setLine(3, lines[3]);
/*      */       }
/* 1044 */       s.update();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void cleartradesigns_cmd()
/*      */     throws CivException
/*      */   {
/* 1053 */     CivMessage.send(this.sender, "Starting task");
/*      */     
/* 1055 */     if (this.args.length < 3) {
/* 1056 */       throw new CivException("bad arg count");
/*      */     }
/*      */     try
/*      */     {
/* 1060 */       Integer xoff = Integer.valueOf(this.args[1]);
/* 1061 */       Integer zoff = Integer.valueOf(this.args[2]);
/* 1062 */       TaskMaster.syncTask(new TradeGoodSignCleanupTask(getPlayer().getName(), xoff.intValue(), zoff.intValue()));
/*      */     }
/*      */     catch (NumberFormatException e) {
/* 1065 */       throw new CivException("Bad number format");
/*      */     }
/*      */   }
/*      */   
/*      */   public void tradegenerate_cmd()
/*      */     throws CivException
/*      */   {
/*      */     String playerName;
/*      */     String playerName;
/* 1074 */     if ((this.sender instanceof Player)) {
/* 1075 */       playerName = this.sender.getName();
/*      */     } else {
/* 1077 */       playerName = null;
/*      */     }
/*      */     
/* 1080 */     CivMessage.send(this.sender, "Starting Trade Generation task...");
/* 1081 */     TaskMaster.asyncTask(new TradeGoodPostGenTask(playerName, 0), 0L);
/*      */   }
/*      */   
/*      */   public void createtradegood_cmd() throws CivException
/*      */   {
/* 1086 */     if (this.args.length < 2) {
/* 1087 */       throw new CivException("Enter trade goodie id");
/*      */     }
/*      */     
/* 1090 */     ConfigTradeGood good = (ConfigTradeGood)CivSettings.goods.get(this.args[1]);
/* 1091 */     if (good == null) {
/* 1092 */       throw new CivException("Unknown trade good id:" + this.args[1]);
/*      */     }
/*      */     
/* 1095 */     BlockCoord coord = new BlockCoord(getPlayer().getLocation());
/* 1096 */     TradeGoodPopulator.buildTradeGoodie(good, coord, getPlayer().getLocation().getWorld(), false);
/* 1097 */     CivMessage.sendSuccess(this.sender, "Created a " + good.name + " here.");
/*      */   }
/*      */   
/*      */   public void generate_cmd() throws CivException {
/* 1101 */     if (this.args.length < 5) {
/* 1102 */       throw new CivException("Enter chunk coords to generate.");
/*      */     }
/*      */     try
/*      */     {
/* 1106 */       int startX = Integer.valueOf(this.args[1]).intValue();
/* 1107 */       int startZ = Integer.valueOf(this.args[2]).intValue();
/* 1108 */       int stopX = Integer.valueOf(this.args[3]).intValue();
/* 1109 */       int stopZ = Integer.valueOf(this.args[4]).intValue();
/*      */       
/* 1111 */       TaskMaster.syncTask(new ChunkGenerateTask(startX, startZ, stopX, stopZ));
/*      */     }
/*      */     catch (NumberFormatException e) {
/* 1114 */       throw new CivException(e.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setspeed_cmd()
/*      */     throws CivException
/*      */   {
/* 1122 */     Player player = getPlayer();
/*      */     
/* 1124 */     if (this.args.length < 2) {
/* 1125 */       throw new CivException("Enter a speed.");
/*      */     }
/*      */     
/* 1128 */     player.setWalkSpeed(Float.valueOf(this.args[1]).floatValue());
/* 1129 */     CivMessage.sendSuccess(player, "speed changed");
/*      */   }
/*      */   
/*      */   public void unloadchunk_cmd() throws CivException {
/* 1133 */     if (this.args.length < 3) {
/* 1134 */       throw new CivException("Enter an x and z");
/*      */     }
/*      */     
/* 1137 */     getPlayer().getWorld().unloadChunk(Integer.valueOf(this.args[1]).intValue(), Integer.valueOf(this.args[2]).intValue());
/*      */     
/* 1139 */     CivMessage.sendSuccess(this.sender, "unloaded.");
/*      */   }
/*      */   
/*      */   public void givebuff_cmd() throws CivException
/*      */   {
/* 1144 */     if (this.args.length < 2) {
/* 1145 */       throw new CivException("Enter the buff id");
/*      */     }
/*      */     
/* 1148 */     ConfigBuff buff = (ConfigBuff)CivSettings.buffs.get(this.args[1]);
/* 1149 */     if (buff == null) {
/* 1150 */       throw new CivException("No buff id:" + this.args[1]);
/*      */     }
/*      */     
/* 1153 */     getSelectedTown().getBuffManager().addBuff(buff.id, buff.id, "Debug");
/* 1154 */     CivMessage.sendSuccess(this.sender, "Gave buff " + buff.name + " to town");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void addteam_cmd()
/*      */     throws IllegalStateException, IllegalArgumentException, CivException
/*      */   {}
/*      */   
/*      */ 
/*      */   public void processculture_cmd()
/*      */   {
/* 1166 */     CivGlobal.processCulture();
/* 1167 */     CivMessage.sendSuccess(this.sender, "Forced process of culture");
/*      */   }
/*      */   
/*      */   public void wall_cmd() throws CivException {
/* 1171 */     Player player = getPlayer();
/*      */     
/* 1173 */     HashSet<Wall> walls = CivGlobal.getWallChunk(new ChunkCoord(player.getLocation()));
/* 1174 */     if (walls == null) {
/* 1175 */       CivMessage.sendError(player, "Sorry, this is not a wall chunk.");
/* 1176 */       return;
/*      */     }
/*      */     
/* 1179 */     for (Wall wall : walls) {
/* 1180 */       CivMessage.send(player, "Wall:" + wall.getId() + " town:" + wall.getTown() + " chunk:" + new ChunkCoord(player.getLocation()));
/*      */     }
/*      */   }
/*      */   
/*      */   public void arrow_cmd() throws CivException
/*      */   {
/* 1186 */     if (this.args.length < 2) {
/* 1187 */       throw new CivException("/arrow [power]");
/*      */     }
/*      */     
/*      */     Iterator localIterator2;
/* 1191 */     for (Iterator localIterator1 = CivGlobal.getTowns().iterator(); localIterator1.hasNext(); 
/* 1192 */         localIterator2.hasNext())
/*      */     {
/* 1191 */       Town town = (Town)localIterator1.next();
/* 1192 */       localIterator2 = town.getStructures().iterator(); continue;Structure struct = (Structure)localIterator2.next();
/* 1193 */       if ((struct instanceof ArrowTower)) {
/* 1194 */         ((ArrowTower)struct).setPower(Float.valueOf(this.args[1]).floatValue());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void sound_cmd()
/*      */     throws CivException
/*      */   {
/* 1203 */     Player player = getPlayer();
/*      */     
/* 1205 */     if (this.args.length < 3) {
/* 1206 */       throw new CivException("Enter sound enum name and pitch.");
/*      */     }
/*      */     
/* 1209 */     player.getWorld().playSound(player.getLocation(), Sound.valueOf(this.args[1].toUpperCase()), 1.0F, Float.valueOf(this.args[2]).floatValue());
/*      */   }
/*      */   
/*      */   public void firework_cmd() throws CivException {
/* 1213 */     Player player = getPlayer();
/*      */     
/* 1215 */     FireworkEffectPlayer fw = new FireworkEffectPlayer();
/*      */     try {
/* 1217 */       fw.playFirework(player.getWorld(), player.getLocation(), FireworkEffect.builder().withColor(Color.RED).flicker(true).with(FireworkEffect.Type.BURST).build());
/*      */     } catch (Exception e) {
/* 1219 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void repogoodie_cmd() throws CivException
/*      */   {
/* 1225 */     if (this.args.length < 2) {
/* 1226 */       throw new CivException("Enter the id of the goodie you want to repo.");
/*      */     }
/*      */     
/* 1229 */     for (BonusGoodie goodie : CivGlobal.getBonusGoodies()) {
/* 1230 */       if (goodie.getId() == Integer.valueOf(this.args[1]).intValue()) {
/* 1231 */         CivMessage.send(this.sender, "Repo'd Goodie " + goodie.getId() + " (" + goodie.getDisplayName() + ")");
/* 1232 */         goodie.replenish();
/* 1233 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void printgoodie_cmd()
/*      */     throws CivException
/*      */   {
/* 1242 */     if (this.args.length < 2) {
/* 1243 */       throw new CivException("Enter the id of the goodie you want to inspect.");
/*      */     }
/*      */     
/* 1246 */     for (BonusGoodie goodie : CivGlobal.getBonusGoodies()) {
/* 1247 */       if (goodie.getId() == Integer.valueOf(this.args[1]).intValue()) {
/* 1248 */         CivMessage.sendHeading(this.sender, "Goodie " + goodie.getId() + " (" + goodie.getDisplayName() + ")");
/*      */         
/* 1250 */         if (goodie.getItem() != null) {
/* 1251 */           CivMessage.send(this.sender, "Item: " + goodie.getItem().getUniqueId() + " loc:" + goodie.getItem().getLocation());
/*      */         } else {
/* 1253 */           CivMessage.send(this.sender, "Item: null");
/*      */         }
/*      */         
/* 1256 */         if (goodie.getFrame() != null) {
/* 1257 */           CivMessage.send(this.sender, "Frame: " + goodie.getFrame().getUUID() + " loc:" + goodie.getFrame().getLocation());
/*      */         } else {
/* 1259 */           CivMessage.send(this.sender, "Frame: null");
/*      */         }
/*      */         
/* 1262 */         if (goodie.getHolder() != null) {
/* 1263 */           CivMessage.send(this.sender, "Holder: " + goodie.getHolder().toString());
/*      */         } else {
/* 1265 */           CivMessage.send(this.sender, "holder: null");
/*      */         }
/*      */         
/* 1268 */         ItemStack stack = goodie.getStack();
/* 1269 */         if (stack != null) {
/* 1270 */           CivMessage.send(this.sender, "Stack: " + stack.toString());
/*      */         } else {
/* 1272 */           CivMessage.send(this.sender, "Stack: null");
/*      */         }
/* 1274 */         return;
/*      */       }
/*      */     }
/* 1277 */     CivMessage.send(this.sender, "No goodie found.");
/*      */   }
/*      */   
/*      */   public void test_cmd() throws CivException
/*      */   {
/* 1282 */     DebugTestCommand cmd = new DebugTestCommand();
/* 1283 */     cmd.onCommand(this.sender, null, "test", stripArgs(this.args, 1));
/*      */   }
/*      */   
/*      */   public void dupe_cmd() throws CivException
/*      */   {
/* 1288 */     Player player = getPlayer();
/*      */     
/* 1290 */     if ((player.getItemInHand() == null) || (ItemManager.getId(player.getItemInHand()) == 0)) {
/* 1291 */       throw new CivException("No item in hand.");
/*      */     }
/*      */     
/* 1294 */     player.getInventory().addItem(new ItemStack[] { player.getItemInHand() });
/* 1295 */     CivMessage.sendSuccess(player, player.getItemInHand().getType().name() + "duplicated.");
/*      */   }
/*      */   
/*      */   public void makeframe_cmd() throws CivException {
/* 1299 */     if (this.args.length > 3) {
/* 1300 */       throw new CivException("Provide a x,y,z and a direction (n,s,e,w)");
/*      */     }
/*      */     
/* 1303 */     String locationString = "world," + this.args[1];
/*      */     String str1;
/*      */     BlockFace face;
/* 1306 */     switch ((str1 = this.args[2]).hashCode()) {case 101:  if (str1.equals("e")) {} break; case 110:  if (str1.equals("n")) break; break; case 115:  if (str1.equals("s")) {} break; case 119:  if (!str1.equals("w")) {
/*      */         break label176;
/* 1308 */         BlockFace face = BlockFace.NORTH;
/*      */         
/*      */         break label187;
/*      */         
/* 1312 */         BlockFace face = BlockFace.SOUTH;
/*      */         
/*      */         break label187;
/*      */         
/* 1316 */         BlockFace face = BlockFace.EAST;
/*      */         break label187;
/*      */       }
/*      */       else {
/* 1320 */         face = BlockFace.WEST; }
/* 1321 */       break; }
/*      */     label176:
/* 1323 */     throw new CivException("Invalid direction, use n,s,e,w");
/*      */     label187:
/*      */     BlockFace face;
/* 1326 */     Location loc = CivGlobal.getLocationFromHash(locationString);
/* 1327 */     new ItemFrameStorage(loc, face);
/* 1328 */     CivMessage.send(this.sender, "Created frame.");
/*      */   }
/*      */   
/*      */   public void show_cmd() throws CivException {
/* 1332 */     Player player = getPlayer();
/* 1333 */     Chunk chunk = player.getLocation().getChunk();
/*      */     Entity[] arrayOfEntity;
/* 1335 */     int j = (arrayOfEntity = chunk.getEntities()).length; for (int i = 0; i < j; i++) { Entity entity = arrayOfEntity[i];
/* 1336 */       CivMessage.send(player, "E:" + entity.getType().name() + " UUID:" + entity.getUniqueId().toString());
/* 1337 */       CivLog.info("E:" + entity.getType().name() + " UUID:" + entity.getUniqueId().toString());
/*      */     }
/*      */   }
/*      */   
/*      */   public void moveframes_cmd() throws CivException {
/* 1342 */     Player player = getPlayer();
/* 1343 */     Chunk chunk = player.getLocation().getChunk();
/*      */     
/*      */ 
/*      */ 
/*      */     Entity[] arrayOfEntity;
/*      */     
/*      */ 
/*      */ 
/* 1351 */     int j = (arrayOfEntity = chunk.getEntities()).length; for (int i = 0; i < j; i++) { Entity entity = arrayOfEntity[i];
/* 1352 */       if ((entity instanceof ItemFrame)) {
/* 1353 */         CivMessage.send(this.sender, "Teleported...");
/* 1354 */         entity.teleport(entity.getLocation());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void frame_cmd() throws CivException
/*      */   {
/* 1361 */     Town town = getSelectedTown();
/*      */     
/* 1363 */     TownHall townhall = town.getTownHall();
/* 1364 */     if (townhall == null) {
/* 1365 */       throw new CivException("No town hall?");
/*      */     }
/*      */     
/* 1368 */     for (ItemFrameStorage itemstore : townhall.getGoodieFrames()) {
/* 1369 */       String itemString = "empty";
/*      */       
/* 1371 */       if (!itemstore.isEmpty()) {
/* 1372 */         BonusGoodie goodie = CivGlobal.getBonusGoodie(itemstore.getItem());
/* 1373 */         itemString = goodie.getDisplayName();
/*      */       }
/* 1375 */       CivMessage.send(this.sender, "GoodieFrame UUID:" + itemstore.getUUID() + " item:" + itemString);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void repo_cmd() {}
/*      */   
/*      */ 
/*      */   public void culturechunk_cmd()
/*      */   {
/* 1385 */     if ((this.sender instanceof Player)) {
/* 1386 */       Player player = (Player)this.sender;
/*      */       
/* 1388 */       CultureChunk cc = CivGlobal.getCultureChunk(player.getLocation());
/*      */       
/* 1390 */       if (cc == null) {
/* 1391 */         CivMessage.send(this.sender, "No culture chunk found here.");
/* 1392 */         return;
/*      */       }
/*      */       
/* 1395 */       CivMessage.send(this.sender, "loc:" + cc.getChunkCoord() + " town:" + cc.getTown().getName() + " civ:" + cc.getCiv().getName() + 
/* 1396 */         " distanceToNearest:" + cc.getDistanceToNearestEdge(cc.getTown().savedEdgeBlocks));
/*      */     }
/*      */   }
/*      */   
/*      */   public void runculture_cmd() {
/* 1401 */     TaskMaster.asyncTask("cultureProcess", new CultureProcessAsyncTask(), 0L);
/* 1402 */     CivMessage.sendSuccess(this.sender, "Processed culture.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void map_cmd()
/*      */     throws CivException
/*      */   {
/* 1424 */     Player player = getPlayer();
/*      */     
/* 1426 */     CivMessage.send(player, AsciiMap.getMapAsString(player.getLocation()));
/*      */   }
/*      */   
/*      */   public void civ_cmd() throws CivException
/*      */   {
/* 1431 */     if (this.args.length < 2) {
/* 1432 */       throw new CivException("Specify a civ name.");
/*      */     }
/*      */     
/* 1435 */     Civilization civ = getNamedCiv(1);
/*      */     
/* 1437 */     CivMessage.sendHeading(this.sender, "Civ " + civ.getName());
/* 1438 */     CivMessage.send(this.sender, "id:" + civ.getId() + " debt: " + civ.getTreasury().getDebt() + " balance:" + civ.getTreasury().getBalance());
/*      */   }
/*      */   
/*      */   public void newday_cmd() {
/* 1442 */     CivMessage.send(this.sender, "Starting a new day...");
/* 1443 */     TaskMaster.syncTask(new DailyTimer(), 0L);
/*      */   }
/*      */   
/*      */   public void showHelp()
/*      */   {
/* 1448 */     showBasicHelp();
/*      */   }
/*      */   
/*      */   public void town_cmd() throws CivException {
/* 1452 */     if (this.args.length < 2) {
/* 1453 */       CivMessage.sendError(this.sender, "Specifiy a town name.");
/* 1454 */       return;
/*      */     }
/*      */     
/* 1457 */     Town town = getNamedTown(1);
/*      */     
/* 1459 */     CivMessage.sendHeading(this.sender, "Town " + town.getName());
/* 1460 */     CivMessage.send(this.sender, "id:" + town.getId() + " level: " + town.getLevel());
/*      */   }
/*      */   
/*      */   public void townchunk_cmd()
/*      */   {
/* 1465 */     if ((this.sender instanceof Player)) {
/* 1466 */       Player player = (Player)this.sender;
/*      */       
/* 1468 */       TownChunk tc = CivGlobal.getTownChunk(player.getLocation());
/*      */       
/* 1470 */       if (tc == null) {
/* 1471 */         CivMessage.send(this.sender, "No town chunk found here.");
/* 1472 */         return;
/*      */       }
/*      */       
/* 1475 */       CivMessage.send(this.sender, "id:" + tc.getId() + " coord:" + tc.getChunkCoord());
/*      */     }
/*      */   }
/*      */   
/*      */   public void resident_cmd() throws CivException {
/* 1480 */     if (this.args.length < 2) {
/* 1481 */       CivMessage.sendError(this.sender, "Specifiy a resident name.");
/* 1482 */       return;
/*      */     }
/*      */     
/* 1485 */     Resident res = getNamedResident(1);
/*      */     
/* 1487 */     CivMessage.sendHeading(this.sender, "Resident " + res.getName());
/* 1488 */     CivMessage.send(this.sender, "id: " + res.getId() + " lastOnline: " + res.getLastOnline() + " registered: " + res.getRegistered());
/* 1489 */     CivMessage.send(this.sender, "debt: " + res.getTreasury().getDebt());
/*      */   }
/*      */   
/*      */   public void permissionCheck()
/*      */     throws CivException
/*      */   {
/* 1495 */     if ((this.sender instanceof Player)) {
/* 1496 */       if (!((Player)this.sender).isOp()) {}
/*      */ 
/*      */     }
/*      */     else {
/* 1500 */       return;
/*      */     }
/* 1502 */     throw new CivException("Only OP can do this.");
/*      */   }
/*      */   
/*      */   public void doDefaultAction() throws CivException
/*      */   {
/* 1507 */     showHelp();
/*      */   }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\debug\DebugCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */