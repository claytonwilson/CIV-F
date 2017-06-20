/*     */ package com.avrgaming.civcraft.command.debug;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmGrowthSyncTask;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmPreCachePopulateTimer;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Effect;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
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
/*     */ public class DebugFarmCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  40 */     this.command = "/dbg farm ";
/*  41 */     this.displayName = "Farm Commands";
/*     */     
/*  43 */     this.commands.put("showgrowth", "Highlight the crops that grew last tick.");
/*  44 */     this.commands.put("grow", "[x] grows ALL farm chunks x many times.");
/*  45 */     this.commands.put("cropcache", "show the crop cache for this plot.");
/*  46 */     this.commands.put("unloadchunk", "[x] [z] unloads this farm chunk");
/*  47 */     this.commands.put("cache", "Runs the crop cache task.");
/*     */   }
/*     */   
/*     */   public void unloadchunk_cmd()
/*     */     throws CivException
/*     */   {
/*  53 */     int x = getNamedInteger(1).intValue();
/*  54 */     int z = getNamedInteger(2).intValue();
/*     */     
/*  56 */     Bukkit.getWorld("world").unloadChunk(x, z);
/*  57 */     CivMessage.sendSuccess(this.sender, "Chunk " + x + "," + z + " unloaded");
/*     */   }
/*     */   
/*     */   public void showgrowth_cmd() throws CivException {
/*  61 */     Player player = getPlayer();
/*     */     
/*  63 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/*  64 */     FarmChunk fc = CivGlobal.getFarmChunk(coord);
/*  65 */     if (fc == null) {
/*  66 */       throw new CivException("This is not a farm.");
/*     */     }
/*     */     
/*  69 */     for (BlockCoord bcoord : fc.getLastGrownCrops()) {
/*  70 */       bcoord.getBlock().getWorld().playEffect(bcoord.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
/*     */     }
/*     */     
/*  73 */     CivMessage.sendSuccess(player, "Flashed last grown crops");
/*     */   }
/*     */   
/*     */   public void cropcache_cmd() throws CivException
/*     */   {
/*  78 */     Player player = getPlayer();
/*     */     
/*  80 */     ChunkCoord coord = new ChunkCoord(player.getLocation());
/*  81 */     FarmChunk fc = CivGlobal.getFarmChunk(coord);
/*  82 */     if (fc == null) {
/*  83 */       throw new CivException("This is not a farm.");
/*     */     }
/*     */     
/*  86 */     for (BlockCoord bcoord : fc.cropLocationCache) {
/*  87 */       bcoord.getBlock().getWorld().playEffect(bcoord.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
/*     */     }
/*  89 */     CivMessage.sendSuccess(player, "Flashed cached crops.");
/*     */   }
/*     */   
/*     */   public void grow_cmd() throws CivException
/*     */   {
/*  94 */     int count = getNamedInteger(1).intValue();
/*  95 */     for (int i = 0; i < count; i++) {
/*  96 */       TaskMaster.asyncTask(new FarmGrowthSyncTask(), 0L);
/*     */     }
/*  98 */     CivMessage.sendSuccess(this.sender, "Grew all farms.");
/*     */   }
/*     */   
/*     */   public void cache_cmd() {
/* 102 */     TaskMaster.syncTask(new FarmPreCachePopulateTimer());
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 107 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 112 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\debug\DebugFarmCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */