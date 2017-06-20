/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.command.ReportChestsTask;
/*     */ import com.avrgaming.civcraft.command.ReportPlayerInventoryTask;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*     */ import com.avrgaming.civcraft.config.ConfigMaterialCategory;
/*     */ import com.avrgaming.civcraft.config.ConfigUnit;
/*     */ import com.avrgaming.civcraft.endgame.EndGameCondition;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.command.CommandSender;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdminCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  62 */     this.command = "/ad";
/*  63 */     this.displayName = "Admin";
/*     */     
/*  65 */     this.commands.put("perm", "toggles your permission overrides, if on, ignores all plot permissions.");
/*  66 */     this.commands.put("sbperm", "Allows breaking of structure blocks");
/*  67 */     this.commands.put("cbinstantbreak", "Allows instant breaking of control blocks.");
/*     */     
/*  69 */     this.commands.put("recover", "Manage recovery commands");
/*  70 */     this.commands.put("server", "shows the name of this server");
/*  71 */     this.commands.put("spawnunit", "[unit-id] [town] spawn the unit with this id for this town.");
/*     */     
/*  73 */     this.commands.put("chestreport", "[radius] check in this radius for chests");
/*  74 */     this.commands.put("playerreport", "shows all player ender chest reports.");
/*     */     
/*  76 */     this.commands.put("civ", "Admin an individual civilization");
/*  77 */     this.commands.put("town", "Admin a town.");
/*  78 */     this.commands.put("war", "Manage war settings, turn wars off and on.... etc.");
/*  79 */     this.commands.put("lag", "Manage lag on the server by disabling expensive tasks.");
/*  80 */     this.commands.put("camp", "Shows camp management subcommands.");
/*  81 */     this.commands.put("chat", "Manage admin chat options, tc, cc, listen etc");
/*  82 */     this.commands.put("res", "Manage resident options, settown, setcamp etc");
/*  83 */     this.commands.put("build", "Manage buildings. Demolish/repair wonders etc.");
/*  84 */     this.commands.put("items", "Opens inventory which allows you to spawn in custom items.");
/*  85 */     this.commands.put("item", "Does special things to the item in your hand.");
/*  86 */     this.commands.put("timer", "Manage timers.");
/*  87 */     this.commands.put("road", "Road management commands");
/*  88 */     this.commands.put("clearendgame", "[key] [civ] - clears this end game condition for this civ.");
/*  89 */     this.commands.put("endworld", "Starts the Apocalypse.");
/*  90 */     this.commands.put("arena", "Arena management commands.");
/*     */   }
/*     */   
/*     */ 
/*     */   public void endworld_cmd()
/*     */   {
/*  96 */     CivGlobal.endWorld = !CivGlobal.endWorld;
/*  97 */     if (CivGlobal.endWorld) {
/*  98 */       CivMessage.sendSuccess(this.sender, "It's the end of the world as we know it.");
/*     */     } else {
/* 100 */       CivMessage.sendSuccess(this.sender, "I feel fine.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearendgame_cmd() throws CivException {
/* 105 */     String key = getNamedString(1, "enter key.");
/* 106 */     Civilization civ = getNamedCiv(2);
/*     */     
/* 108 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/* 109 */     if (entries.size() == 0) {
/* 110 */       throw new CivException("No end games by that key.");
/*     */     }
/*     */     
/* 113 */     for (SessionEntry entry : entries) {
/* 114 */       if (EndGameCondition.getCivFromSessionData(entry.value) == civ) {
/* 115 */         CivGlobal.getSessionDB().delete(entry.request_id, entry.key);
/* 116 */         CivMessage.sendSuccess(this.sender, "Deleted for " + civ.getName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void cbinstantbreak_cmd() throws CivException {
/* 122 */     Resident resident = getResident();
/*     */     
/* 124 */     resident.setControlBlockInstantBreak(!resident.isControlBlockInstantBreak());
/* 125 */     CivMessage.sendSuccess(this.sender, "Set control block instant break:" + resident.isControlBlockInstantBreak());
/*     */   }
/*     */   
/* 128 */   public static Inventory spawnInventory = null;
/*     */   
/* 130 */   public void items_cmd() throws CivException { Player player = getPlayer();
/*     */     
/* 132 */     if (spawnInventory == null) {
/* 133 */       spawnInventory = Bukkit.createInventory(player, 54, "Admin Item Spawn");
/*     */       
/*     */       Iterator localIterator2;
/* 136 */       for (Iterator localIterator1 = ConfigMaterialCategory.getCategories().iterator(); localIterator1.hasNext(); 
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
/* 147 */           localIterator2.hasNext())
/*     */       {
/* 136 */         ConfigMaterialCategory cat = (ConfigMaterialCategory)localIterator1.next();
/* 137 */         ItemStack infoRec = LoreGuiItem.build(cat.name, 
/* 138 */           ItemManager.getId(Material.WRITTEN_BOOK), 
/* 139 */           0, new String[] {
/* 140 */           "§b" + cat.materials.size() + " Items", 
/* 141 */           "§6<Click To Open>" });
/* 142 */         infoRec = LoreGuiItem.setAction(infoRec, "openinv:showGuiInv:" + cat.name + " Spawn");
/* 143 */         spawnInventory.addItem(new ItemStack[] { infoRec });
/*     */         
/*     */ 
/* 146 */         Inventory inv = Bukkit.createInventory(player, 54, cat.name + " Spawn");
/* 147 */         localIterator2 = cat.materials.values().iterator(); continue;ConfigMaterial mat = (ConfigMaterial)localIterator2.next();
/* 148 */         LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(mat.id);
/* 149 */         ItemStack stack = LoreMaterial.spawn(craftMat);
/* 150 */         stack = LoreGuiItem.asGuiItem(stack);
/* 151 */         stack = LoreGuiItem.setAction(stack, "spawn");
/* 152 */         inv.addItem(new ItemStack[] { stack });
/* 153 */         LoreGuiItemListener.guiInventories.put(inv.getName(), inv);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 160 */     player.openInventory(spawnInventory);
/*     */   }
/*     */   
/*     */   public void arena_cmd() {
/* 164 */     AdminArenaCommand cmd = new AdminArenaCommand();
/* 165 */     cmd.onCommand(this.sender, null, "arena", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void road_cmd() {
/* 169 */     AdminRoadCommand cmd = new AdminRoadCommand();
/* 170 */     cmd.onCommand(this.sender, null, "camp", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void item_cmd() {
/* 174 */     AdminItemCommand cmd = new AdminItemCommand();
/* 175 */     cmd.onCommand(this.sender, null, "camp", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void timer_cmd() {
/* 179 */     AdminTimerCommand cmd = new AdminTimerCommand();
/* 180 */     cmd.onCommand(this.sender, null, "camp", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void camp_cmd() {
/* 184 */     AdminCampCommand cmd = new AdminCampCommand();
/* 185 */     cmd.onCommand(this.sender, null, "camp", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void playerreport_cmd()
/*     */   {
/* 190 */     LinkedList<OfflinePlayer> offplayers = new LinkedList();
/* 191 */     OfflinePlayer[] arrayOfOfflinePlayer; int j = (arrayOfOfflinePlayer = Bukkit.getOfflinePlayers()).length; for (int i = 0; i < j; i++) { OfflinePlayer offplayer = arrayOfOfflinePlayer[i];
/* 192 */       offplayers.add(offplayer);
/*     */     }
/*     */     
/* 195 */     CivMessage.sendHeading(this.sender, "Players with Goodies");
/* 196 */     CivMessage.send(this.sender, "Processing (this may take a while)");
/* 197 */     TaskMaster.syncTask(new ReportPlayerInventoryTask(this.sender, offplayers), 0L);
/*     */   }
/*     */   
/*     */   public void chestreport_cmd() throws CivException {
/* 201 */     Integer radius = getNamedInteger(1);
/* 202 */     Player player = getPlayer();
/*     */     
/* 204 */     LinkedList<ChunkCoord> coords = new LinkedList();
/* 205 */     for (int x = -radius.intValue(); x < radius.intValue(); x++) {
/* 206 */       for (int z = -radius.intValue(); z < radius.intValue(); z++) {
/* 207 */         ChunkCoord coord = new ChunkCoord(player.getLocation());
/* 208 */         coord.setX(coord.getX() + x);coord.setZ(coord.getZ() + z);
/*     */         
/* 210 */         coords.add(coord);
/*     */       }
/*     */     }
/*     */     
/* 214 */     CivMessage.sendHeading(this.sender, "Chests with Goodies");
/* 215 */     CivMessage.send(this.sender, "Processing (this may take a while)");
/* 216 */     TaskMaster.syncTask(new ReportChestsTask(this.sender, coords), 0L);
/*     */   }
/*     */   
/*     */   public void spawnunit_cmd() throws CivException {
/* 220 */     if (this.args.length < 2) {
/* 221 */       throw new CivException("Enter a unit id.");
/*     */     }
/*     */     
/* 224 */     ConfigUnit unit = (ConfigUnit)CivSettings.units.get(this.args[1]);
/* 225 */     if (unit == null) {
/* 226 */       throw new CivException("No unit called " + this.args[1]);
/*     */     }
/*     */     
/* 229 */     Player player = getPlayer();
/* 230 */     Town town = getNamedTown(2);
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
/*     */     try
/*     */     {
/* 244 */       Class<?> c = Class.forName(unit.class_name);
/* 245 */       Method m = c.getMethod("spawn", new Class[] { Inventory.class, Town.class });
/* 246 */       m.invoke(null, new Object[] { player.getInventory(), town });
/*     */     }
/*     */     catch (ClassNotFoundException|NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 249 */       throw new CivException(e.getMessage());
/*     */     }
/*     */     
/*     */     Class<?> c;
/* 253 */     CivMessage.sendSuccess(this.sender, "Spawned a " + unit.name);
/*     */   }
/*     */   
/*     */   public void server_cmd() {
/* 257 */     CivMessage.send(this.sender, Bukkit.getServerName());
/*     */   }
/*     */   
/*     */   public void recover_cmd()
/*     */   {
/* 262 */     AdminRecoverCommand cmd = new AdminRecoverCommand();
/* 263 */     cmd.onCommand(this.sender, null, "recover", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void town_cmd() {
/* 267 */     AdminTownCommand cmd = new AdminTownCommand();
/* 268 */     cmd.onCommand(this.sender, null, "town", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void civ_cmd() {
/* 272 */     AdminCivCommand cmd = new AdminCivCommand();
/* 273 */     cmd.onCommand(this.sender, null, "civ", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void setfullmessage_cmd() {
/* 277 */     if (this.args.length < 2) {
/* 278 */       CivMessage.send(this.sender, "Current:" + CivGlobal.fullMessage);
/* 279 */       return;
/*     */     }
/*     */     
/* 282 */     synchronized (CivGlobal.maxPlayers) {
/* 283 */       CivGlobal.fullMessage = this.args[1];
/*     */     }
/*     */     
/* 286 */     CivMessage.sendSuccess(this.sender, "Set to:" + this.args[1]);
/*     */   }
/*     */   
/*     */   public void unban_cmd()
/*     */     throws CivException
/*     */   {
/* 292 */     if (this.args.length < 2) {
/* 293 */       throw new CivException("Enter a player name to ban");
/*     */     }
/*     */     
/* 296 */     OfflinePlayer offplayer = Bukkit.getOfflinePlayer(this.args[1]);
/* 297 */     if ((offplayer != null) && (offplayer.isBanned())) {
/* 298 */       offplayer.setBanned(false);
/* 299 */       Resident resident = CivGlobal.getResident(offplayer.getName());
/* 300 */       if (resident != null) {
/* 301 */         resident.setBanned(false);
/* 302 */         resident.save();
/*     */       }
/* 304 */       CivMessage.sendSuccess(this.sender, "Unbanned " + this.args[1]);
/*     */     } else {
/* 306 */       CivMessage.sendSuccess(this.sender, "Couldn't find " + this.args[1] + " or he is not banned.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void res_cmd() {
/* 311 */     AdminResCommand cmd = new AdminResCommand();
/* 312 */     cmd.onCommand(this.sender, null, "war", stripArgs(this.args, 1));
/*     */   }
/*     */   
/* 315 */   public void chat_cmd() { AdminChatCommand cmd = new AdminChatCommand();
/* 316 */     cmd.onCommand(this.sender, null, "war", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void war_cmd() {
/* 320 */     AdminWarCommand cmd = new AdminWarCommand();
/* 321 */     cmd.onCommand(this.sender, null, "war", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void lag_cmd() {
/* 325 */     AdminLagCommand cmd = new AdminLagCommand();
/* 326 */     cmd.onCommand(this.sender, null, "war", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void build_cmd() {
/* 330 */     AdminBuildCommand cmd = new AdminBuildCommand();
/* 331 */     cmd.onCommand(this.sender, null, "war", stripArgs(this.args, 1));
/*     */   }
/*     */   
/*     */   public void perm_cmd() throws CivException {
/* 335 */     Resident resident = getResident();
/*     */     
/* 337 */     if (resident.isPermOverride()) {
/* 338 */       resident.setPermOverride(false);
/* 339 */       CivMessage.sendSuccess(this.sender, "Permission override off.");
/* 340 */       return;
/*     */     }
/*     */     
/* 343 */     resident.setPermOverride(true);
/* 344 */     CivMessage.sendSuccess(this.sender, "Permission override on.");
/*     */   }
/*     */   
/*     */   public void sbperm_cmd() throws CivException
/*     */   {
/* 349 */     Resident resident = getResident();
/* 350 */     if (resident.isSBPermOverride()) {
/* 351 */       resident.setSBPermOverride(false);
/* 352 */       CivMessage.sendSuccess(this.sender, "Structure Permission override off.");
/* 353 */       return;
/*     */     }
/*     */     
/* 356 */     resident.setSBPermOverride(true);
/* 357 */     CivMessage.sendSuccess(this.sender, "Structure Permission override on.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/* 365 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 370 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {
/* 376 */     if (((this.sender instanceof Player)) && 
/* 377 */       (((Player)this.sender).hasPermission("civ.admin"))) {
/* 378 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 383 */     if (!this.sender.isOp()) {
/* 384 */       throw new CivException("Only admins can use this command.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doLogging()
/*     */   {
/* 390 */     CivLog.adminlog(this.sender.getName(), "/ad " + combineArgs(this.args));
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */