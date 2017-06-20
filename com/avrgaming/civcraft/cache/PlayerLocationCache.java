/*     */ package com.avrgaming.civcraft.cache;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.VanishNoPacketUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerLocationCache
/*     */ {
/*     */   private BlockCoord coord;
/*     */   private String name;
/*     */   private Resident resident;
/*     */   private boolean isDead;
/*     */   private boolean vanished;
/*  42 */   private static ConcurrentHashMap<String, PlayerLocationCache> cache = new ConcurrentHashMap();
/*     */   
/*     */   public static PlayerLocationCache get(String name)
/*     */   {
/*  46 */     return (PlayerLocationCache)cache.get(name);
/*     */   }
/*     */   
/*     */   public static void add(Player player)
/*     */   {
/*  51 */     if (cache.containsKey(player.getName())) {
/*  52 */       return;
/*     */     }
/*     */     
/*  55 */     Resident resident = CivGlobal.getResident(player);
/*  56 */     if (resident == null) {
/*  57 */       return;
/*     */     }
/*     */     
/*  60 */     PlayerLocationCache pc = new PlayerLocationCache();
/*  61 */     pc.setCoord(new BlockCoord(player.getLocation()));
/*  62 */     pc.setResident(resident);
/*  63 */     pc.setName(player.getName());
/*  64 */     pc.setDead(player.isDead());
/*  65 */     pc.setVanished(false);
/*     */     
/*  67 */     if (CivSettings.hasVanishNoPacket) {
/*  68 */       pc.setVanished(VanishNoPacketUtil.isVanished(player));
/*     */     }
/*     */     
/*  71 */     cache.put(pc.getName(), pc);
/*     */   }
/*     */   
/*     */   public static void remove(String playerName) {
/*  75 */     cache.remove(playerName);
/*     */   }
/*     */   
/*     */   public static void updateLocation(Player player)
/*     */   {
/*  80 */     PlayerLocationCache pc = get(player.getName());
/*  81 */     if (pc == null) {
/*  82 */       add(player);
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     pc.getCoord().setFromLocation(player.getLocation());
/*  87 */     pc.setDead(player.isDead());
/*     */     
/*  89 */     Resident resident = CivGlobal.getResident(player);
/*  90 */     if (resident != null) {
/*  91 */       resident.onRoadTest(pc.getCoord(), player);
/*     */     }
/*     */     
/*  94 */     if (CivSettings.hasVanishNoPacket) {
/*  95 */       pc.setVanished(VanishNoPacketUtil.isVanished(player));
/*     */     } else {
/*  97 */       pc.setVanished(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Collection<PlayerLocationCache> getCache() {
/* 102 */     return cache.values();
/*     */   }
/*     */   
/*     */   public static List<PlayerLocationCache> getNearbyPlayers(BlockCoord bcoord, double radiusSquared) {
/* 106 */     LinkedList<PlayerLocationCache> list = new LinkedList();
/*     */     
/* 108 */     for (PlayerLocationCache pc : cache.values()) {
/* 109 */       if (pc.getCoord().distanceSquared(bcoord) < radiusSquared) {
/* 110 */         list.add(pc);
/*     */       }
/*     */     }
/*     */     
/* 114 */     return list;
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord() {
/* 118 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(BlockCoord coord) {
/* 122 */     this.coord = coord;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 126 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 130 */     this.name = name;
/*     */   }
/*     */   
/*     */   public Resident getResident() {
/* 134 */     return this.resident;
/*     */   }
/*     */   
/*     */   public void setResident(Resident resident) {
/* 138 */     this.resident = resident;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 144 */     return this.name.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 149 */     if ((other instanceof PlayerLocationCache)) {
/* 150 */       PlayerLocationCache otherCache = (PlayerLocationCache)other;
/* 151 */       return otherCache.getName().equalsIgnoreCase(getName());
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDead() {
/* 157 */     return this.isDead;
/*     */   }
/*     */   
/*     */   public void setDead(boolean isDead) {
/* 161 */     this.isDead = isDead;
/*     */   }
/*     */   
/*     */   public boolean isVanished() {
/* 165 */     return this.vanished;
/*     */   }
/*     */   
/*     */   public void setVanished(boolean vanished) {
/* 169 */     this.vanished = vanished;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\cache\PlayerLocationCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */