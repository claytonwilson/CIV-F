/*     */ package com.avrgaming.civcraft.main;
/*     */ 
/*     */ import com.avrgaming.anticheat.ACManager;
/*     */ import com.avrgaming.civcraft.arena.ArenaListener;
/*     */ import com.avrgaming.civcraft.arena.ArenaManager;
/*     */ import com.avrgaming.civcraft.arena.ArenaTimer;
/*     */ import com.avrgaming.civcraft.command.AcceptCommand;
/*     */ import com.avrgaming.civcraft.command.BuildCommand;
/*     */ import com.avrgaming.civcraft.command.DenyCommand;
/*     */ import com.avrgaming.civcraft.command.EconCommand;
/*     */ import com.avrgaming.civcraft.command.HereCommand;
/*     */ import com.avrgaming.civcraft.command.KillCommand;
/*     */ import com.avrgaming.civcraft.command.PayCommand;
/*     */ import com.avrgaming.civcraft.command.ReportCommand;
/*     */ import com.avrgaming.civcraft.command.SelectCommand;
/*     */ import com.avrgaming.civcraft.command.TradeCommand;
/*     */ import com.avrgaming.civcraft.command.VoteCommand;
/*     */ import com.avrgaming.civcraft.command.admin.AdminCommand;
/*     */ import com.avrgaming.civcraft.command.camp.CampCommand;
/*     */ import com.avrgaming.civcraft.command.civ.CivChatCommand;
/*     */ import com.avrgaming.civcraft.command.civ.CivCommand;
/*     */ import com.avrgaming.civcraft.command.debug.DebugCommand;
/*     */ import com.avrgaming.civcraft.command.market.MarketCommand;
/*     */ import com.avrgaming.civcraft.command.plot.PlotCommand;
/*     */ import com.avrgaming.civcraft.command.resident.ResidentCommand;
/*     */ import com.avrgaming.civcraft.command.team.TeamCommand;
/*     */ import com.avrgaming.civcraft.command.town.TownChatCommand;
/*     */ import com.avrgaming.civcraft.command.town.TownCommand;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.endgame.EndConditionNotificationTask;
/*     */ import com.avrgaming.civcraft.event.EventTimerTask;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.fishing.FishingListener;
/*     */ import com.avrgaming.civcraft.listener.BlockListener;
/*     */ import com.avrgaming.civcraft.listener.BonusGoodieManager;
/*     */ import com.avrgaming.civcraft.listener.ChatListener;
/*     */ import com.avrgaming.civcraft.listener.CustomItemManager;
/*     */ import com.avrgaming.civcraft.listener.DebugListener;
/*     */ import com.avrgaming.civcraft.listener.DisableXPListener;
/*     */ import com.avrgaming.civcraft.listener.HeroChatListener;
/*     */ import com.avrgaming.civcraft.listener.MarkerPlacementManager;
/*     */ import com.avrgaming.civcraft.listener.PlayerListener;
/*     */ import com.avrgaming.civcraft.listener.TagAPIListener;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementArenaItem;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterialListener;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
/*     */ import com.avrgaming.civcraft.mobs.MobSpawner;
/*     */ import com.avrgaming.civcraft.mobs.listeners.MobListener;
/*     */ import com.avrgaming.civcraft.mobs.timers.MobSpawnerTimer;
/*     */ import com.avrgaming.civcraft.nocheat.NoCheatPlusSurvialFlyHandler;
/*     */ import com.avrgaming.civcraft.populators.TradeGoodPopulator;
/*     */ import com.avrgaming.civcraft.pvplogger.PvPLogger;
/*     */ import com.avrgaming.civcraft.randomevents.RandomEventSweeper;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDBAsyncTimer;
/*     */ import com.avrgaming.civcraft.siege.CannonListener;
/*     */ import com.avrgaming.civcraft.structure.Farm;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmGrowthSyncTask;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmPreCachePopulateTimer;
/*     */ import com.avrgaming.civcraft.structurevalidation.StructureValidationChecker;
/*     */ import com.avrgaming.civcraft.structurevalidation.StructureValidationPunisher;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncBuildUpdateTask;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncGetChestInventory;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncGrowTask;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncLoadChunk;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncUpdateChunks;
/*     */ import com.avrgaming.civcraft.threading.sync.SyncUpdateInventory;
/*     */ import com.avrgaming.civcraft.threading.tasks.ArrowProjectileTask;
/*     */ import com.avrgaming.civcraft.threading.tasks.ProjectileComponentTimer;
/*     */ import com.avrgaming.civcraft.threading.tasks.ScoutTowerTask;
/*     */ import com.avrgaming.civcraft.threading.timers.AnnouncementTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.BeakerTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.ChangeGovernmentTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.PlayerLocationCacheUpdate;
/*     */ import com.avrgaming.civcraft.threading.timers.PlayerProximityComponentTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.ReduceExposureTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.RegenTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.UnitTrainTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.UpdateEventTimer;
/*     */ import com.avrgaming.civcraft.threading.timers.WindmillTimer;
/*     */ import com.avrgaming.civcraft.trade.TradeInventoryListener;
/*     */ import com.avrgaming.civcraft.util.BukkitObjects;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.TimeTools;
/*     */ import com.avrgaming.civcraft.war.WarListener;
/*     */ import com.avrgaming.global.perks.PlatinumManager;
/*     */ import com.avrgaming.global.scores.CalculateScoreTimer;
/*     */ import com.avrgaming.moblib.MobLib;
/*     */ import com.avrgaming.sls.SLSManager;
/*     */ import java.io.IOException;
/*     */ import java.sql.SQLException;
/*     */ import java.util.List;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.PluginCommand;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import pvptimer.PvPListener;
/*     */ import pvptimer.PvPTimer;
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
/*     */ public final class CivCraft
/*     */   extends JavaPlugin
/*     */ {
/* 125 */   private boolean isError = false;
/*     */   private static JavaPlugin plugin;
/*     */   
/*     */   private void startTimers()
/*     */   {
/* 130 */     TaskMaster.asyncTimer("SQLUpdate", new SQLUpdate(), 5L);
/*     */     
/*     */ 
/* 133 */     TaskMaster.syncTimer(SyncBuildUpdateTask.class.getName(), 
/* 134 */       new SyncBuildUpdateTask(), 0L, 1L);
/*     */     
/* 136 */     TaskMaster.syncTimer(SyncUpdateChunks.class.getName(), 
/* 137 */       new SyncUpdateChunks(), 0L, TimeTools.toTicks(1L));
/*     */     
/* 139 */     TaskMaster.syncTimer(SyncLoadChunk.class.getName(), 
/* 140 */       new SyncLoadChunk(), 0L, 1L);
/*     */     
/* 142 */     TaskMaster.syncTimer(SyncGetChestInventory.class.getName(), 
/* 143 */       new SyncGetChestInventory(), 0L, 1L);
/*     */     
/* 145 */     TaskMaster.syncTimer(SyncUpdateInventory.class.getName(), 
/* 146 */       new SyncUpdateInventory(), 0L, 1L);
/*     */     
/* 148 */     TaskMaster.syncTimer(SyncGrowTask.class.getName(), 
/* 149 */       new SyncGrowTask(), 0L, 1L);
/*     */     
/* 151 */     TaskMaster.syncTimer(PlayerLocationCacheUpdate.class.getName(), 
/* 152 */       new PlayerLocationCacheUpdate(), 0L, 10L);
/*     */     
/* 154 */     TaskMaster.asyncTimer("RandomEventSweeper", new RandomEventSweeper(), 0L, TimeTools.toTicks(10L));
/*     */     
/*     */ 
/* 157 */     TaskMaster.asyncTimer("UpdateEventTimer", new UpdateEventTimer(), TimeTools.toTicks(1L));
/* 158 */     TaskMaster.asyncTimer("RegenTimer", new RegenTimer(), TimeTools.toTicks(5L));
/*     */     
/* 160 */     TaskMaster.asyncTimer("BeakerTimer", new BeakerTimer(60), TimeTools.toTicks(60L));
/* 161 */     TaskMaster.syncTimer("UnitTrainTimer", new UnitTrainTimer(), TimeTools.toTicks(1L));
/* 162 */     TaskMaster.asyncTimer("ReduceExposureTimer", new ReduceExposureTimer(), 0L, TimeTools.toTicks(5L));
/*     */     try
/*     */     {
/* 165 */       double arrow_firerate = CivSettings.getDouble(CivSettings.warConfig, "arrow_tower.fire_rate");
/* 166 */       TaskMaster.syncTimer("arrowTower", new ProjectileComponentTimer(), (int)(arrow_firerate * 20.0D));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 171 */       TaskMaster.asyncTimer("ScoutTowerTask", new ScoutTowerTask(), TimeTools.toTicks(1L));
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/* 174 */       e.printStackTrace();
/* 175 */       return;
/*     */     }
/* 177 */     TaskMaster.syncTimer("arrowhomingtask", new ArrowProjectileTask(), 5L);
/*     */     
/*     */ 
/* 180 */     TaskMaster.syncTimer("FarmCropCache", new FarmPreCachePopulateTimer(), TimeTools.toTicks(30L));
/*     */     
/* 182 */     TaskMaster.asyncTimer("FarmGrowthTimer", 
/* 183 */       new FarmGrowthSyncTask(), TimeTools.toTicks(Farm.GROW_RATE));
/*     */     
/* 185 */     TaskMaster.asyncTimer("announcer", new AnnouncementTimer("tips.txt"), 0L, TimeTools.toTicks(3600L));
/*     */     
/* 187 */     TaskMaster.asyncTimer("ChangeGovernmentTimer", new ChangeGovernmentTimer(), TimeTools.toTicks(60L));
/* 188 */     TaskMaster.asyncTimer("CalculateScoreTimer", new CalculateScoreTimer(), 0L, TimeTools.toTicks(60L));
/*     */     
/* 190 */     TaskMaster.asyncTimer(PlayerProximityComponentTimer.class.getName(), 
/* 191 */       new PlayerProximityComponentTimer(), TimeTools.toTicks(1L));
/*     */     
/* 193 */     TaskMaster.asyncTimer(EventTimerTask.class.getName(), new EventTimerTask(), TimeTools.toTicks(5L));
/*     */     
/* 195 */     if (PlatinumManager.isEnabled()) {
/* 196 */       TaskMaster.asyncTimer(PlatinumManager.class.getName(), new PlatinumManager(), TimeTools.toTicks(5L));
/*     */     }
/*     */     
/* 199 */     TaskMaster.syncTimer("PvPLogger", new PvPLogger(), TimeTools.toTicks(5L));
/* 200 */     TaskMaster.syncTimer("WindmillTimer", new WindmillTimer(), TimeTools.toTicks(60L));
/* 201 */     TaskMaster.asyncTimer("EndGameNotification", new EndConditionNotificationTask(), TimeTools.toTicks(3600L));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */     TaskMaster.asyncTask(new StructureValidationChecker(), TimeTools.toTicks(120L));
/* 209 */     TaskMaster.asyncTimer("StructureValidationPunisher", new StructureValidationPunisher(), TimeTools.toTicks(3600L));
/* 210 */     TaskMaster.asyncTimer("SessionDBAsyncTimer", new SessionDBAsyncTimer(), 10L);
/* 211 */     TaskMaster.asyncTimer("pvptimer", new PvPTimer(), TimeTools.toTicks(30L));
/*     */     
/*     */ 
/* 214 */     TaskMaster.syncTimer("MobSpawner", new MobSpawnerTimer(), TimeTools.toTicks(2L));
/* 215 */     TaskMaster.syncTimer("ArenaTimer", new ArenaManager(), TimeTools.toTicks(30L));
/* 216 */     TaskMaster.syncTimer("ArenaTimeoutTimer", new ArenaTimer(), TimeTools.toTicks(1L));
/*     */   }
/*     */   
/*     */   private void registerEvents()
/*     */   {
/* 221 */     PluginManager pluginManager = getServer().getPluginManager();
/* 222 */     pluginManager.registerEvents(new BlockListener(), this);
/* 223 */     pluginManager.registerEvents(new ChatListener(), this);
/* 224 */     pluginManager.registerEvents(new BonusGoodieManager(), this);
/* 225 */     pluginManager.registerEvents(new MarkerPlacementManager(), this);
/* 226 */     pluginManager.registerEvents(new CustomItemManager(), this);
/* 227 */     pluginManager.registerEvents(new PlayerListener(), this);
/* 228 */     pluginManager.registerEvents(new DebugListener(), this);
/* 229 */     pluginManager.registerEvents(new LoreCraftableMaterialListener(), this);
/* 230 */     pluginManager.registerEvents(new LoreGuiItemListener(), this);
/* 231 */     pluginManager.registerEvents(new DisableXPListener(), this);
/* 232 */     pluginManager.registerEvents(new PvPLogger(), this);
/* 233 */     pluginManager.registerEvents(new TradeInventoryListener(), this);
/* 234 */     pluginManager.registerEvents(new MobListener(), this);
/* 235 */     pluginManager.registerEvents(new ArenaListener(), this);
/* 236 */     pluginManager.registerEvents(new CannonListener(), this);
/* 237 */     pluginManager.registerEvents(new WarListener(), this);
/* 238 */     pluginManager.registerEvents(new FishingListener(), this);
/* 239 */     pluginManager.registerEvents(new PvPListener(), this);
/* 240 */     pluginManager.registerEvents(new LoreEnhancementArenaItem(), this);
/*     */     
/* 242 */     if (hasPlugin("TagAPI")) {
/* 243 */       pluginManager.registerEvents(new TagAPIListener(), this);
/*     */     }
/*     */     
/* 246 */     if (hasPlugin("HeroChat")) {
/* 247 */       pluginManager.registerEvents(new HeroChatListener(), this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void registerNPCHooks() {}
/*     */   
/*     */ 
/*     */   public void onEnable()
/*     */   {
/* 257 */     setPlugin(this);
/* 258 */     saveDefaultConfig();
/*     */     
/* 260 */     CivLog.init(this);
/* 261 */     BukkitObjects.initialize(this);
/*     */     
/*     */ 
/* 264 */     ((World)BukkitObjects.getWorlds().get(0)).getPopulators().add(new TradeGoodPopulator());
/*     */     try
/*     */     {
/* 267 */       CivSettings.init(this);
/* 268 */       SQL.initialize();
/* 269 */       SQL.initCivObjectTables();
/* 270 */       ChunkCoord.buildWorldList();
/* 271 */       CivGlobal.loadGlobals();
/*     */       
/* 273 */       ACManager.init();
/*     */       try {
/* 275 */         SLSManager.init();
/*     */       } catch (CivException e1) {
/* 277 */         e1.printStackTrace();
/*     */       } catch (InvalidConfiguration e1) {
/* 279 */         e1.printStackTrace();
/*     */       }
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
/* 291 */       getCommand("town").setExecutor(new TownCommand());
/*     */     }
/*     */     catch (InvalidConfiguration|SQLException|IOException|InvalidConfigurationException|CivException|ClassNotFoundException e)
/*     */     {
/* 284 */       e.printStackTrace();
/* 285 */       setError(true);
/* 286 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 292 */     getCommand("resident").setExecutor(new ResidentCommand());
/* 293 */     getCommand("dbg").setExecutor(new DebugCommand());
/* 294 */     getCommand("plot").setExecutor(new PlotCommand());
/* 295 */     getCommand("accept").setExecutor(new AcceptCommand());
/* 296 */     getCommand("deny").setExecutor(new DenyCommand());
/* 297 */     getCommand("civ").setExecutor(new CivCommand());
/* 298 */     getCommand("tc").setExecutor(new TownChatCommand());
/* 299 */     getCommand("cc").setExecutor(new CivChatCommand());
/*     */     
/* 301 */     getCommand("ad").setExecutor(new AdminCommand());
/* 302 */     getCommand("econ").setExecutor(new EconCommand());
/* 303 */     getCommand("pay").setExecutor(new PayCommand());
/* 304 */     getCommand("build").setExecutor(new BuildCommand());
/* 305 */     getCommand("market").setExecutor(new MarketCommand());
/* 306 */     getCommand("select").setExecutor(new SelectCommand());
/* 307 */     getCommand("here").setExecutor(new HereCommand());
/* 308 */     getCommand("camp").setExecutor(new CampCommand());
/* 309 */     getCommand("report").setExecutor(new ReportCommand());
/* 310 */     getCommand("vote").setExecutor(new VoteCommand());
/* 311 */     getCommand("trade").setExecutor(new TradeCommand());
/* 312 */     getCommand("kill").setExecutor(new KillCommand());
/* 313 */     getCommand("team").setExecutor(new TeamCommand());
/*     */     
/* 315 */     registerEvents();
/*     */     
/* 317 */     if (hasPlugin("NoCheatPlus")) {
/* 318 */       registerNPCHooks();
/*     */     } else {
/* 320 */       CivLog.warning("NoCheatPlus not found, not registering NCP hooks. This is fine if you're not using NCP.");
/*     */     }
/* 322 */     MobLib.registerAllEntities();
/* 323 */     startTimers();
/* 324 */     MobSpawner.register();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasPlugin(String name)
/*     */   {
/* 331 */     Plugin p = getServer().getPluginManager().getPlugin(name);
/* 332 */     return p != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDisable() {}
/*     */   
/*     */ 
/*     */   public boolean isError()
/*     */   {
/* 341 */     return this.isError;
/*     */   }
/*     */   
/*     */   public void setError(boolean isError) {
/* 345 */     this.isError = isError;
/*     */   }
/*     */   
/*     */   public static JavaPlugin getPlugin()
/*     */   {
/* 350 */     return plugin;
/*     */   }
/*     */   
/*     */   public static void setPlugin(JavaPlugin plugin)
/*     */   {
/* 355 */     plugin = plugin;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\main\CivCraft.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */