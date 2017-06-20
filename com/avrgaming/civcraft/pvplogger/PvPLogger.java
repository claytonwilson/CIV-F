/*     */ package com.avrgaming.civcraft.pvplogger;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementSoulBound;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.moblib.MobLib;
/*     */ import com.avrgaming.moblib.MobLibEntity;
/*     */ import gpl.ImprovedOfflinePlayer;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Arrow;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.entity.EntityCombustEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.event.player.PlayerLoginEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PvPLogger
/*     */   implements Listener, Runnable
/*     */ {
/*     */   class ZombiePlayer
/*     */   {
/*     */     Date spawnTime;
/*     */     UUID id;
/*     */     String playerName;
/*     */     
/*     */     public ZombiePlayer(Date spawnTime, UUID id, String playerName)
/*     */     {
/*  56 */       this.spawnTime = spawnTime;
/*  57 */       this.id = id;
/*  58 */       this.playerName = playerName;
/*     */     }
/*     */   }
/*     */   
/*  62 */   public static HashMap<String, Date> taggedPlayers = new HashMap();
/*  63 */   public static HashMap<String, ZombiePlayer> zombiePlayers = new HashMap();
/*     */   
/*     */   @EventHandler(priority=EventPriority.MONITOR)
/*     */   public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
/*  67 */     if (event.isCancelled()) {
/*  68 */       return;
/*     */     }
/*     */     
/*  71 */     Player defender = null;
/*     */     
/*  73 */     if ((event.getEntity() instanceof Player)) {
/*  74 */       defender = (Player)event.getEntity();
/*     */     }
/*     */     else {
/*  77 */       return;
/*     */     }
/*     */     
/*  80 */     if ((event.getDamager() instanceof Arrow)) {
/*  81 */       if ((((Arrow)event.getDamager()).getShooter() instanceof Player)) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*  87 */     else if (!(event.getDamager() instanceof Player))
/*     */     {
/*     */ 
/*     */ 
/*  91 */       return;
/*     */     }
/*     */     
/*  94 */     if (!taggedPlayers.containsKey(defender.getName())) {
/*     */       try
/*     */       {
/*  97 */         int logoutSeconds = CivSettings.getInteger(CivSettings.warConfig, "war.logout_time").intValue();
/*  98 */         CivMessage.send(defender, "§7" + CivColor.BOLD + "You've been PvP tagged. If you log out within " + logoutSeconds + 
/*  99 */           " seconds you can be killed while offline by ANYONE!");
/*     */       } catch (InvalidConfiguration e) {
/* 101 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 105 */     taggedPlayers.put(defender.getName(), new Date());
/*     */   }
/*     */   
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onPlayerQuit(PlayerQuitEvent event)
/*     */   {
/* 112 */     if (taggedPlayers.containsKey(event.getPlayer().getName())) {
/* 113 */       Date lastHit = (Date)taggedPlayers.get(event.getPlayer().getName());
/* 114 */       Calendar now = Calendar.getInstance();
/* 115 */       Calendar expire = Calendar.getInstance();
/* 116 */       expire.setTime(lastHit);
/*     */       try
/*     */       {
/* 119 */         int logoutSeconds = CivSettings.getInteger(CivSettings.warConfig, "war.logout_time").intValue();
/* 120 */         expire.add(13, logoutSeconds);
/*     */         
/* 122 */         if (now.before(expire))
/*     */         {
/*     */ 
/*     */ 
/* 126 */           MobLibEntity entity = MobLib.createNamedEntity("com.avrgaming.civcraft.mobs.LoboZombie", event.getPlayer().getLocation(), event.getPlayer().getName());
/*     */           
/* 128 */           zombiePlayers.put(event.getPlayer().getName(), new ZombiePlayer(new Date(), entity.getUid(), event.getPlayer().getName()));
/*     */         }
/*     */         else {
/* 131 */           taggedPlayers.remove(event.getPlayer().getName());
/*     */         }
/*     */       }
/*     */       catch (InvalidConfiguration e) {
/* 135 */         e.printStackTrace();
/* 136 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onEntityCombust(EntityCombustEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onPlayerLogin(PlayerLoginEvent event)
/*     */   {
/* 154 */     ZombiePlayer zombiePlayer = (ZombiePlayer)zombiePlayers.get(event.getPlayer().getName());
/* 155 */     if (zombiePlayer == null) {
/* 156 */       return;
/*     */     }
/*     */     
/*     */ 
/* 160 */     MobLib.removeEntity(zombiePlayer.id);
/* 161 */     zombiePlayers.remove(event.getPlayer().getName());
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.LOWEST)
/*     */   public void onEntityDeath(EntityDeathEvent event)
/*     */   {
/* 167 */     if (MobLib.isMobLibEntity(event.getEntity()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 172 */       String playerName = event.getEntity().getCustomName();
/* 173 */       ZombiePlayer zombiePlayer = (ZombiePlayer)zombiePlayers.get(playerName);
/* 174 */       if (zombiePlayer == null) {
/* 175 */         return;
/*     */       }
/*     */       
/* 178 */       Date spawnTime = zombiePlayer.spawnTime;
/* 179 */       if (spawnTime == null)
/*     */       {
/* 181 */         event.getEntity().remove();
/* 182 */         return;
/*     */       }
/*     */       
/* 185 */       Calendar expire = Calendar.getInstance();
/* 186 */       Calendar now = Calendar.getInstance();
/*     */       
/* 188 */       expire.setTime(spawnTime);
/*     */       try
/*     */       {
/* 191 */         int zombieSeconds = CivSettings.getInteger(CivSettings.warConfig, "war.zombie_time").intValue();
/* 192 */         expire.add(13, zombieSeconds);
/*     */         
/* 194 */         if (now.after(expire))
/*     */         {
/* 196 */           event.getEntity().remove();
/* 197 */           zombiePlayers.remove(playerName);
/* 198 */           taggedPlayers.remove(playerName);
/* 199 */           return;
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 204 */           CivGlobal.getPlayer(event.getEntity().getCustomName());
/*     */           
/* 206 */           event.getEntity().remove();
/* 207 */           zombiePlayers.remove(playerName);
/* 208 */           taggedPlayers.remove(playerName);
/* 209 */           return;
/*     */ 
/*     */         }
/*     */         catch (CivException localCivException)
/*     */         {
/* 214 */           LinkedList<ItemStack> droppedStuff = new LinkedList();
/*     */           
/*     */ 
/* 217 */           ImprovedOfflinePlayer playerOffline = new ImprovedOfflinePlayer(event.getEntity().getCustomName());
/*     */           
/*     */ 
/* 220 */           PlayerInventory inv = playerOffline.getInventory();
/* 221 */           ItemStack[] armorContents = inv.getArmorContents();
/* 222 */           for (int i = 0; i < armorContents.length; i++) {
/* 223 */             ItemStack stack = armorContents[i];
/* 224 */             if ((stack != null) && (ItemManager.getId(stack) != 0))
/*     */             {
/*     */ 
/*     */ 
/* 228 */               LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 229 */               if (craftMat != null) {
/* 230 */                 boolean found = false;
/* 231 */                 for (LoreEnhancement ench : LoreCraftableMaterial.getEnhancements(stack)) {
/* 232 */                   if ((ench instanceof LoreEnhancementSoulBound)) {
/* 233 */                     found = true;
/* 234 */                     break;
/*     */                   }
/*     */                 }
/*     */                 
/* 238 */                 if (found) {}
/*     */ 
/*     */               }
/*     */               else
/*     */               {
/*     */ 
/* 244 */                 droppedStuff.add(stack);
/* 245 */                 armorContents[i] = null;
/*     */               } } }
/* 247 */           inv.setArmorContents(armorContents);
/*     */           
/*     */ 
/* 250 */           ItemStack[] contents = inv.getContents();
/* 251 */           ItemStack stack; for (int i = 0; i < contents.length; i++) {
/* 252 */             stack = contents[i];
/* 253 */             if ((stack != null) && (ItemManager.getId(stack) != 0))
/*     */             {
/*     */ 
/*     */ 
/* 257 */               LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(stack);
/* 258 */               if (craftMat != null) {
/* 259 */                 boolean found = false;
/* 260 */                 for (LoreEnhancement ench : LoreCraftableMaterial.getEnhancements(stack)) {
/* 261 */                   if ((ench instanceof LoreEnhancementSoulBound)) {
/* 262 */                     found = true;
/* 263 */                     break;
/*     */                   }
/*     */                 }
/*     */                 
/* 267 */                 if (found) {}
/*     */ 
/*     */               }
/*     */               else
/*     */               {
/*     */ 
/* 273 */                 droppedStuff.add(stack);
/* 274 */                 contents[i] = null;
/*     */               } } }
/* 276 */           inv.setContents(contents);
/* 277 */           playerOffline.setInventory(inv);
/*     */           
/*     */ 
/* 280 */           for (ItemStack stack : droppedStuff) {
/* 281 */             if (ItemManager.getId(stack) != 0)
/*     */             {
/*     */ 
/* 284 */               event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), stack);
/*     */             }
/*     */           }
/*     */           
/* 288 */           zombiePlayers.remove(playerName);
/* 289 */           taggedPlayers.remove(playerName);
/* 290 */           CivGlobal.getSessionDB().add("pvplogger:death:" + playerName, "", 0, 0, 0);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 296 */         return;
/*     */       }
/*     */       catch (InvalidConfiguration e)
/*     */       {
/* 292 */         e.printStackTrace();
/* 293 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 303 */     LinkedList<String> removedKeys = new LinkedList();
/* 304 */     for (String playerName : zombiePlayers.keySet()) {
/* 305 */       ZombiePlayer zombiePlayer = (ZombiePlayer)zombiePlayers.get(playerName);
/* 306 */       Date spawnTime = zombiePlayer.spawnTime;
/* 307 */       if (spawnTime != null)
/*     */       {
/*     */ 
/*     */ 
/* 311 */         Calendar expire = Calendar.getInstance();
/* 312 */         Calendar now = Calendar.getInstance();
/*     */         
/* 314 */         expire.setTime(spawnTime);
/*     */         try
/*     */         {
/* 317 */           int zombieSeconds = CivSettings.getInteger(CivSettings.warConfig, "war.zombie_time").intValue();
/* 318 */           expire.add(13, zombieSeconds);
/*     */           
/* 320 */           if (now.after(expire))
/*     */           {
/* 322 */             removedKeys.add(playerName);
/*     */             
/* 324 */             MobLib.removeEntity(zombiePlayer.id);
/*     */           }
/*     */         } catch (InvalidConfiguration e) {
/* 327 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 331 */     for (String name : removedKeys) {
/* 332 */       zombiePlayers.remove(name);
/* 333 */       taggedPlayers.remove(name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\pvplogger\PvPLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */