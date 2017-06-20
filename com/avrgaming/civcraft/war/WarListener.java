/*     */ package com.avrgaming.civcraft.war;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityExplodeEvent;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class WarListener implements Listener
/*     */ {
/*  27 */   ChunkCoord coord = new ChunkCoord();
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*  30 */   public void onBlockBreak(BlockBreakEvent event) { if (event.isCancelled()) {
/*  31 */       CivLog.debug("This event was cancelled.");
/*  32 */       return;
/*     */     }
/*     */     
/*  35 */     if (!War.isWarTime()) {
/*  36 */       return;
/*     */     }
/*     */     
/*  39 */     this.coord.setFromLocation(event.getBlock().getLocation());
/*  40 */     CultureChunk cc = CivGlobal.getCultureChunk(this.coord);
/*     */     
/*  42 */     if (cc == null) {
/*  43 */       CivLog.debug("This is not a culture chunk.");
/*  44 */       return;
/*     */     }
/*     */     
/*  47 */     if (!cc.getCiv().getDiplomacyManager().isAtWar()) {
/*  48 */       return;
/*     */     }
/*     */     
/*  51 */     if ((event.getBlock().getType().equals(Material.DIRT)) || 
/*  52 */       (event.getBlock().getType().equals(Material.GRASS)) || 
/*  53 */       (event.getBlock().getType().equals(Material.SAND)) || 
/*  54 */       (event.getBlock().getType().equals(Material.GRAVEL)) || 
/*  55 */       (event.getBlock().getType().equals(Material.TORCH)) || 
/*  56 */       (event.getBlock().getType().equals(Material.REDSTONE_TORCH_OFF)) || 
/*  57 */       (event.getBlock().getType().equals(Material.REDSTONE_TORCH_ON)) || 
/*  58 */       (event.getBlock().getType().equals(Material.REDSTONE)) || 
/*  59 */       (event.getBlock().getType().equals(Material.TNT)) || 
/*  60 */       (event.getBlock().getType().equals(Material.LADDER)) || 
/*  61 */       (event.getBlock().getType().equals(Material.VINE)) || 
/*  62 */       (!event.getBlock().getType().isSolid())) {
/*  63 */       return;
/*     */     }
/*     */     
/*  66 */     CivMessage.sendError(event.getPlayer(), "Must use TNT to break blocks in at-war civilization cultures during WarTime.");
/*  67 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onBlockPlace(BlockPlaceEvent event) {
/*  72 */     if (event.isCancelled()) {
/*  73 */       return;
/*     */     }
/*     */     
/*  76 */     if (!War.isWarTime()) {
/*  77 */       return;
/*     */     }
/*     */     
/*  80 */     this.coord.setFromLocation(event.getBlock().getLocation());
/*  81 */     CultureChunk cc = CivGlobal.getCultureChunk(this.coord);
/*     */     
/*  83 */     if (!cc.getCiv().getDiplomacyManager().isAtWar()) {
/*  84 */       return;
/*     */     }
/*     */     
/*  87 */     if ((event.getBlock().getType().equals(Material.DIRT)) || 
/*  88 */       (event.getBlock().getType().equals(Material.GRASS)) || 
/*  89 */       (event.getBlock().getType().equals(Material.SAND)) || 
/*  90 */       (event.getBlock().getType().equals(Material.GRAVEL)) || 
/*  91 */       (event.getBlock().getType().equals(Material.TORCH)) || 
/*  92 */       (event.getBlock().getType().equals(Material.REDSTONE_TORCH_OFF)) || 
/*  93 */       (event.getBlock().getType().equals(Material.REDSTONE_TORCH_ON)) || 
/*  94 */       (event.getBlock().getType().equals(Material.REDSTONE)) || 
/*  95 */       (event.getBlock().getType().equals(Material.LADDER)) || 
/*  96 */       (event.getBlock().getType().equals(Material.VINE)) || 
/*  97 */       (event.getBlock().getType().equals(Material.TNT)))
/*     */     {
/*  99 */       if (event.getBlock().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR) {
/* 100 */         return;
/*     */       }
/*     */       
/* 103 */       event.getBlock().getWorld().spawnFallingBlock(event.getBlock().getLocation(), event.getBlock().getType(), (byte)0);
/* 104 */       event.getBlock().setType(Material.AIR);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 109 */       return;
/*     */     }
/*     */     
/* 112 */     CivMessage.sendError(event.getPlayer(), "Can only place grass, dirt, and TNT blocks in at-war civilization cultures during WarTime.");
/* 113 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onEntityExplode(EntityExplodeEvent event)
/*     */   {
/* 119 */     if (event.isCancelled()) {
/* 120 */       return;
/*     */     }
/*     */     
/* 123 */     if (!War.isWarTime()) {
/* 124 */       return;
/*     */     }
/*     */     
/* 127 */     if (event.getEntity() == null) {
/* 128 */       return;
/*     */     }
/*     */     
/* 131 */     if (event.getEntityType().equals(EntityType.UNKNOWN)) {
/* 132 */       return;
/*     */     }
/*     */     
/* 135 */     if ((event.getEntityType().equals(EntityType.PRIMED_TNT)) || 
/* 136 */       (event.getEntityType().equals(EntityType.MINECART_TNT)))
/*     */     {
/*     */       try
/*     */       {
/* 140 */         yield = CivSettings.getInteger(CivSettings.warConfig, "cannon.yield").intValue();
/*     */       } catch (InvalidConfiguration e) { int yield;
/* 142 */         e.printStackTrace(); return;
/*     */       }
/*     */       
/*     */       int yield;
/* 146 */       yield /= 2;
/*     */       
/* 148 */       for (int y = -yield; y <= yield; y++) {
/* 149 */         for (int x = -yield; x <= yield; x++) {
/* 150 */           for (int z = -yield; z <= yield; z++) {
/* 151 */             Location loc = event.getLocation().clone().add(new Vector(x, y, z));
/*     */             
/* 153 */             if (loc.distance(event.getLocation()) < yield) {
/* 154 */               WarRegen.saveBlock(loc.getBlock(), "special:Cannons", false);
/* 155 */               ItemManager.setTypeIdAndData(loc.getBlock(), 0, 0, false);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 160 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\war\WarListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */