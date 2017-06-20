/*     */ package com.avrgaming.civcraft.arena;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.DateUtil;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockIgniteEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerLoginEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.event.player.PlayerRespawnEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ public class ArenaListener implements Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onBlockIgniteEvent(BlockIgniteEvent event)
/*     */   {
/*  36 */     if (ArenaManager.activeArenas.containsKey(event.getBlock().getWorld().getName())) {
/*  37 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onPlayerLogin(PlayerLoginEvent event) {
/*  43 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*  44 */     if (resident == null) {
/*  45 */       return;
/*     */     }
/*     */     
/*  48 */     if (resident.isInsideArena()) {
/*  49 */       if (resident.getCurrentArena() != null)
/*     */       {
/*  51 */         CivMessage.sendArena(resident.getCurrentArena(), event.getPlayer().getName() + " has rejoined the arena.");
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
/*  72 */         TaskMaster.syncTask(new Runnable()
/*     */         {
/*     */           String name;
/*     */           
/*     */           public void run()
/*     */           {
/*     */             try
/*     */             {
/*  64 */               Resident resident = CivGlobal.getResident(this.name);
/*  65 */               Player player = CivGlobal.getPlayer(resident);
/*  66 */               player.setScoreboard(resident.getCurrentArena().getScoreboard(resident.getTeam().getName()));
/*     */ 
/*     */             }
/*     */             catch (CivException localCivException) {}
/*     */           }
/*     */           
/*  72 */         });
/*  73 */         return;
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
/*  99 */       event.getPlayer().getInventory().clear();
/* 100 */       TaskMaster.syncTask(new Runnable()
/*     */       {
/*     */         String name;
/*     */         
/*     */         public void run()
/*     */         {
/*  85 */           Resident resident = CivGlobal.getResident(this.name);
/*     */           
/*     */ 
/*  88 */           resident.teleportHome();
/*  89 */           resident.restoreInventory();
/*  90 */           resident.setInsideArena(false);
/*  91 */           resident.save();
/*     */           
/*     */ 
/*     */ 
/*  95 */           CivMessage.send(resident, "§7You've been teleported home since the arena you were in no longer exists.");
/*     */ 
/*     */         }
/*     */         
/*     */ 
/* 100 */       }, 10L);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onPlayerQuit(PlayerQuitEvent event)
/*     */   {
/* 107 */     String worldName = event.getPlayer().getWorld().getName();
/* 108 */     if (!ArenaManager.activeArenas.containsKey(worldName)) {
/* 109 */       return;
/*     */     }
/*     */     
/*     */ 
/* 113 */     CivMessage.sendArena((Arena)ArenaManager.activeArenas.get(worldName), event.getPlayer().getName() + " has logged out of the arena.");
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onBlockPlaceEvent(BlockPlaceEvent event) {
/* 118 */     String worldName = event.getBlock().getWorld().getName();
/* 119 */     if (!ArenaManager.activeArenas.containsKey(worldName)) {
/* 120 */       return;
/*     */     }
/*     */     
/* 123 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 124 */     if (resident.isSBPermOverride()) {
/* 125 */       return;
/*     */     }
/*     */     
/* 128 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */ 
/* 132 */   public static BlockCoord bcoord = new BlockCoord();
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/* 135 */   public void onBlockBreakEvent(BlockBreakEvent event) { String worldName = event.getBlock().getWorld().getName();
/* 136 */     if (!ArenaManager.activeArenas.containsKey(worldName)) {
/* 137 */       return;
/*     */     }
/*     */     
/* 140 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/* 141 */     if (resident.isSBPermOverride()) {
/* 142 */       return;
/*     */     }
/*     */     
/* 145 */     bcoord.setFromLocation(event.getBlock().getLocation());
/* 146 */     ArenaControlBlock acb = (ArenaControlBlock)ArenaManager.arenaControlBlocks.get(bcoord);
/* 147 */     if (acb != null) {
/* 148 */       acb.onBreak(resident);
/*     */     }
/*     */     
/* 151 */     event.setCancelled(true);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGHEST)
/*     */   public void onPlayerRespawn(PlayerRespawnEvent event) {
/* 156 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*     */     
/* 158 */     if (!resident.isInsideArena()) {
/* 159 */       return;
/*     */     }
/*     */     
/* 162 */     Arena arena = resident.getCurrentArena();
/* 163 */     if (arena == null) {
/* 164 */       return;
/*     */     }
/*     */     
/* 167 */     Location loc = arena.getRespawnLocation(resident);
/* 168 */     if (loc != null) {
/* 169 */       CivMessage.send(resident, "§7Respawned in arena.");
/* 170 */       World world = Bukkit.getWorld(arena.getInstanceName());
/* 171 */       loc.setWorld(world);
/*     */       
/* 173 */       resident.setLastKilledTime(new Date());
/* 174 */       event.setRespawnLocation(loc);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onPlayerInteract(PlayerInteractEvent event) {
/* 180 */     Resident resident = CivGlobal.getResident(event.getPlayer());
/*     */     
/* 182 */     if (!resident.isInsideArena())
/*     */     {
/* 184 */       return;
/*     */     }
/*     */     
/* 187 */     Arena arena = resident.getCurrentArena();
/* 188 */     if (arena == null) {
/* 189 */       return;
/*     */     }
/*     */     
/* 192 */     if (!event.hasBlock()) {
/* 193 */       return;
/*     */     }
/*     */     
/* 196 */     BlockCoord bcoord = new BlockCoord(event.getClickedBlock().getLocation());
/* 197 */     if ((ArenaManager.chests.containsKey(bcoord)) && 
/* 198 */       (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
/*     */     {
/*     */       try {
/* 201 */         Player player = CivGlobal.getPlayer(resident);
/* 202 */         player.closeInventory();
/*     */       }
/*     */       catch (CivException localCivException) {}
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
/* 229 */       TaskMaster.syncTask(new Runnable()
/*     */       {
/*     */         Arena arena;
/*     */         Resident resident;
/*     */         
/*     */         public void run()
/*     */         {
/*     */           try
/*     */           {
/* 219 */             Player player = CivGlobal.getPlayer(this.resident);
/* 220 */             Inventory inv = this.arena.getInventory(this.resident);
/* 221 */             player.openInventory(inv);
/*     */ 
/*     */ 
/*     */           }
/*     */           catch (CivException localCivException) {}
/*     */         }
/*     */         
/*     */ 
/* 229 */       }, 0L);
/* 230 */       event.setCancelled(true);
/* 231 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 237 */     if (ArenaManager.respawnSigns.containsKey(bcoord))
/*     */     {
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
/* 276 */       TaskMaster.syncTask(new Runnable()
/*     */       {
/*     */         Resident resident;
/*     */         Arena arena;
/*     */         
/*     */         public void run()
/*     */         {
/* 249 */           if (!DateUtil.isAfterSeconds(this.resident.getLastKilledTime(), 30)) {
/* 250 */             Date now = new Date();
/* 251 */             long secondsLeft = (now.getTime() - this.resident.getLastKilledTime().getTime()) / 1000L;
/* 252 */             secondsLeft = 30L - secondsLeft;
/*     */             
/* 254 */             CivMessage.sendError(this.resident, "Respawning back into arena in " + secondsLeft + " seconds.");
/* 255 */             TaskMaster.syncTask(this, com.avrgaming.civcraft.util.TimeTools.toTicks(1L));
/*     */           } else {
/* 257 */             BlockCoord revive = this.arena.getRandomReviveLocation(this.resident);
/* 258 */             if (revive != null) {
/* 259 */               Location loc = revive.getCenteredLocation();
/* 260 */               World world = Bukkit.getWorld(this.arena.getInstanceName());
/* 261 */               loc.setWorld(world);
/* 262 */               CivMessage.send(this.resident, "§7Revived in arena.");
/*     */               
/*     */               try
/*     */               {
/* 266 */                 Player player = CivGlobal.getPlayer(this.resident);
/* 267 */                 player.teleport(loc);
/*     */               }
/*     */               catch (CivException e) {}
/*     */             }
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\arena\ArenaListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */