/*     */ package com.avrgaming.civcraft.arena;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigArena;
/*     */ import com.avrgaming.civcraft.config.ConfigArenaTeam;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.scoreboard.Objective;
/*     */ import org.bukkit.scoreboard.Score;
/*     */ import org.bukkit.scoreboard.Scoreboard;
/*     */ import org.bukkit.scoreboard.ScoreboardManager;
/*     */ import org.bukkit.scoreboard.Team;
/*     */ 
/*     */ public class Arena
/*     */ {
/*     */   public ConfigArena config;
/*     */   public int instanceID;
/*  36 */   private HashMap<Integer, ArenaTeam> teams = new HashMap();
/*  37 */   private HashMap<Integer, Integer> teamIDmap = new HashMap();
/*  38 */   private HashMap<Integer, Integer> teamHP = new HashMap();
/*  39 */   private HashMap<String, Inventory> playerInvs = new HashMap();
/*  40 */   public HashMap<String, Scoreboard> scoreboards = new HashMap();
/*  41 */   public HashMap<String, Objective> objectives = new HashMap();
/*     */   public int timeleft;
/*  43 */   public boolean ended = false;
/*     */   
/*  45 */   int teamCount = 0;
/*     */   
/*  47 */   public static int nextInstanceID = 0;
/*     */   
/*     */   public Arena(ConfigArena a) throws CivException {
/*  50 */     this.config = a;
/*     */     
/*     */ 
/*  53 */     boolean found = false;
/*  54 */     int id = 0;
/*  55 */     for (int i = 0; i < 1; i++) {
/*  56 */       String possibleName = getInstanceName(id, this.config);
/*  57 */       if (ArenaManager.activeArenas.containsKey(possibleName)) {
/*  58 */         id++;
/*     */       } else {
/*  60 */         found = true;
/*  61 */         break;
/*     */       }
/*     */     }
/*     */     
/*  65 */     if (!found) {
/*  66 */       throw new CivException("Couldn't find a free instance ID!");
/*     */     }
/*     */     try
/*     */     {
/*  70 */       this.timeleft = CivSettings.getInteger(CivSettings.arenaConfig, "timeout").intValue();
/*     */     } catch (InvalidConfiguration e) {
/*  72 */       e.printStackTrace();
/*     */     }
/*  74 */     this.instanceID = id;
/*     */   }
/*     */   
/*     */   public static String getInstanceName(int id, ConfigArena config)
/*     */   {
/*  79 */     String instanceWorldName = config.world_source + "_" + "instance_" + id;
/*  80 */     return instanceWorldName;
/*     */   }
/*     */   
/*     */   public String getInstanceName() {
/*  84 */     return getInstanceName(this.instanceID, this.config);
/*     */   }
/*     */   
/*     */   public void addTeam(ArenaTeam team) throws CivException {
/*  88 */     this.scoreboards.put(team.getName(), ArenaManager.scoreboardManager.getNewScoreboard());
/*     */     
/*  90 */     this.teams.put(Integer.valueOf(this.teamCount), team);
/*  91 */     this.teamIDmap.put(Integer.valueOf(team.getId()), Integer.valueOf(this.teamCount));
/*  92 */     this.teamHP.put(Integer.valueOf(this.teamCount), Integer.valueOf(((ConfigArenaTeam)this.config.teams.get(this.teamCount)).controlPoints.size()));
/*  93 */     team.setScoreboardTeam(getScoreboard(team.getName()).registerNewTeam(team.getName()));
/*  94 */     team.getScoreboardTeam().setAllowFriendlyFire(false);
/*  95 */     if (this.teamCount == 0) {
/*  96 */       team.setTeamColor("§3");
/*     */     } else {
/*  98 */       team.setTeamColor("§6");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 103 */     for (Resident resident : team.teamMembers) {
/*     */       try {
/* 105 */         CivGlobal.getPlayer(resident);
/*     */       }
/*     */       catch (CivException e) {
/*     */         continue;
/*     */       }
/* 110 */       if (!resident.isUsesAntiCheat()) {
/* 111 */         throw new CivException(resident.getName() + " must be using anti-cheat in order to join the arena.");
/*     */       }
/*     */       try
/*     */       {
/* 115 */         teleportToRandomRevivePoint(resident, this.teamCount);
/* 116 */         createInventory(resident);
/* 117 */         team.getScoreboardTeam().addPlayer(Bukkit.getOfflinePlayer(resident.getName()));
/*     */       }
/*     */       catch (CivException e)
/*     */       {
/* 121 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 126 */     this.teamCount += 1;
/*     */   }
/*     */   
/*     */   private void addCivCraftItemToInventory(String id, Inventory inv) {
/* 130 */     LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(id);
/* 131 */     ItemStack stack = LoreCraftableMaterial.spawn(craftMat);
/* 132 */     stack = LoreCraftableMaterial.addEnhancement(stack, (LoreEnhancement)LoreEnhancement.enhancements.get("LoreEnhancementSoulBound"));
/* 133 */     stack = LoreCraftableMaterial.addEnhancement(stack, (LoreEnhancement)LoreEnhancement.enhancements.get("LoreEnhancementArenaItem"));
/* 134 */     inv.addItem(new ItemStack[] { stack });
/*     */   }
/*     */   
/*     */   private void addItemToInventory(Material mat, Inventory inv, int amount) {
/* 138 */     ItemStack stack = ItemManager.createItemStack(ItemManager.getId(mat), amount);
/* 139 */     stack = LoreCraftableMaterial.addEnhancement(stack, (LoreEnhancement)LoreEnhancement.enhancements.get("LoreEnhancementSoulBound"));
/* 140 */     stack = LoreCraftableMaterial.addEnhancement(stack, (LoreEnhancement)LoreEnhancement.enhancements.get("LoreEnhancementArenaItem"));
/* 141 */     inv.addItem(new ItemStack[] { stack });
/*     */   }
/*     */   
/*     */   private void createInventory(Resident resident)
/*     */   {
/*     */     try {
/* 147 */       Player player = CivGlobal.getPlayer(resident);
/* 148 */       Inventory inv = Bukkit.createInventory(player, 54, resident.getName() + "'s Gear");
/*     */       
/* 150 */       for (int i = 0; i < 3; i++) {
/* 151 */         addCivCraftItemToInventory("mat_tungsten_sword", inv);
/* 152 */         addCivCraftItemToInventory("mat_tungsten_boots", inv);
/* 153 */         addCivCraftItemToInventory("mat_tungsten_chestplate", inv);
/* 154 */         addCivCraftItemToInventory("mat_tungsten_leggings", inv);
/* 155 */         addCivCraftItemToInventory("mat_tungsten_helmet", inv);
/*     */         
/* 157 */         addCivCraftItemToInventory("mat_marksmen_bow", inv);
/* 158 */         addCivCraftItemToInventory("mat_composite_leather_boots", inv);
/* 159 */         addCivCraftItemToInventory("mat_composite_leather_chestplate", inv);
/* 160 */         addCivCraftItemToInventory("mat_composite_leather_leggings", inv);
/* 161 */         addCivCraftItemToInventory("mat_composite_leather_helmet", inv);
/*     */       }
/*     */       
/* 164 */       addCivCraftItemToInventory("mat_vanilla_diamond_pickaxe", inv);
/*     */       
/* 166 */       addItemToInventory(Material.ARROW, inv, 64);
/* 167 */       addItemToInventory(Material.ARROW, inv, 64);
/* 168 */       addItemToInventory(Material.ARROW, inv, 64);
/* 169 */       addItemToInventory(Material.ARROW, inv, 64);
/* 170 */       addItemToInventory(Material.ARROW, inv, 64);
/* 171 */       addItemToInventory(Material.ARROW, inv, 64);
/* 172 */       addItemToInventory(Material.PUMPKIN_PIE, inv, 64);
/* 173 */       addItemToInventory(Material.PUMPKIN_PIE, inv, 64);
/*     */       
/*     */ 
/* 176 */       this.playerInvs.put(resident.getName(), inv);
/*     */     }
/*     */     catch (CivException e) {
/* 179 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private ConfigArenaTeam getConfigTeam(int id) throws CivException
/*     */   {
/* 185 */     for (ConfigArenaTeam ct : this.config.teams) {
/* 186 */       if (ct.number.intValue() == id) {
/* 187 */         return ct;
/*     */       }
/*     */     }
/* 190 */     throw new CivException("Couldn't find configuration for team id:" + id);
/*     */   }
/*     */   
/*     */   public void teleportToRandomRevivePoint(Resident r, int teamID) throws CivException {
/* 194 */     ConfigArenaTeam ct = getConfigTeam(teamID);
/* 195 */     Random rand = new Random();
/* 196 */     int index = rand.nextInt(ct.revivePoints.size());
/*     */     
/* 198 */     int i = 0;
/* 199 */     for (BlockCoord coord : ct.revivePoints)
/*     */     {
/* 201 */       if (index == i) {
/*     */         try {
/* 203 */           Player player = CivGlobal.getPlayer(r);
/* 204 */           coord.setWorldname(getInstanceName());
/* 205 */           player.teleport(coord.getLocation());
/*     */         }
/*     */         catch (CivException e) {
/* 208 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 211 */       i++;
/*     */     }
/*     */   }
/*     */   
/*     */   public void returnPlayers() {
/*     */     Iterator localIterator2;
/* 217 */     for (ArenaTeam team : this.teams.values()) {
/* 218 */       localIterator2 = team.teamMembers.iterator(); continue; for (;;) { Resident r = (Resident)localIterator2.next();
/*     */         try
/*     */         {
/*     */           try {
/* 222 */             Player player = CivGlobal.getPlayer(r);
/* 223 */             player.setScoreboard(ArenaManager.scoreboardManager.getNewScoreboard());
/*     */             
/* 225 */             r.setInsideArena(false);
/* 226 */             r.restoreInventory();
/* 227 */             r.teleportHome();
/* 228 */             r.save();
/* 229 */             CivMessage.send(r, "§7We've been teleported back to our home since the arena has ended.");
/*     */           }
/*     */           catch (CivException localCivException) {}
/* 218 */           if (localIterator2.hasNext()) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 235 */           r.teleportHome();
/* 236 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Collection<ArenaTeam> getTeams() {
/* 243 */     return this.teams.values();
/*     */   }
/*     */   
/*     */   public ArenaTeam getTeamFromID(int id) {
/* 247 */     return (ArenaTeam)this.teams.get(Integer.valueOf(id));
/*     */   }
/*     */   
/*     */   public void onControlBlockDestroy(int teamID, ArenaTeam attackingTeam) {
/* 251 */     Integer hp = (Integer)this.teamHP.get(Integer.valueOf(teamID));
/* 252 */     hp = Integer.valueOf(hp.intValue() - 1);
/* 253 */     this.teamHP.put(Integer.valueOf(teamID), hp);
/*     */     
/* 255 */     ArenaTeam team = (ArenaTeam)this.teams.get(Integer.valueOf(teamID));
/*     */     
/* 257 */     if (hp.intValue() <= 0) {
/* 258 */       ArenaManager.declareVictor(this, team, attackingTeam);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearTeams()
/*     */   {
/* 264 */     for (ArenaTeam team : this.teams.values()) {
/* 265 */       team.setCurrentArena(null);
/*     */     }
/*     */     
/* 268 */     this.teams.clear();
/*     */   }
/*     */   
/*     */   public Location getRespawnLocation(Resident resident) {
/* 272 */     int teamID = ((Integer)this.teamIDmap.get(Integer.valueOf(resident.getTeam().getId()))).intValue();
/* 273 */     for (int i = 0; i < this.config.teams.size(); i++) {
/* 274 */       ConfigArenaTeam configTeam = (ConfigArenaTeam)this.config.teams.get(i);
/* 275 */       if (configTeam.number.intValue() == teamID) {
/* 276 */         Random rand = new Random();
/* 277 */         int index = rand.nextInt(configTeam.respawnPoints.size());
/* 278 */         return ((BlockCoord)configTeam.respawnPoints.get(index)).getCenteredLocation();
/*     */       }
/*     */     }
/*     */     
/* 282 */     return null;
/*     */   }
/*     */   
/*     */   public BlockCoord getRandomReviveLocation(Resident resident) {
/* 286 */     int teamID = ((Integer)this.teamIDmap.get(Integer.valueOf(resident.getTeam().getId()))).intValue();
/* 287 */     for (int i = 0; i < this.config.teams.size(); i++) {
/* 288 */       ConfigArenaTeam configTeam = (ConfigArenaTeam)this.config.teams.get(i);
/* 289 */       if (configTeam.number.intValue() == teamID) {
/* 290 */         Random rand = new Random();
/* 291 */         int index = rand.nextInt(configTeam.respawnPoints.size());
/* 292 */         return (BlockCoord)configTeam.revivePoints.get(index);
/*     */       }
/*     */     }
/*     */     
/* 296 */     return null;
/*     */   }
/*     */   
/*     */   public Inventory getInventory(Resident resident) {
/* 300 */     return (Inventory)this.playerInvs.get(resident.getName());
/*     */   }
/*     */   
/*     */   public Scoreboard getScoreboard(String name) {
/* 304 */     return (Scoreboard)this.scoreboards.get(name);
/*     */   }
/*     */   
/*     */   public void decrementScoreForTeamID(int teamID) {
/* 308 */     ArenaTeam team = getTeamFromID(teamID);
/*     */     Iterator localIterator2;
/* 310 */     for (Iterator localIterator1 = this.teams.values().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/* 313 */         localIterator2.hasNext())
/*     */     {
/* 310 */       ArenaTeam t = (ArenaTeam)localIterator1.next();
/* 311 */       Objective obj = (Objective)this.objectives.get(t.getName() + ";score");
/*     */       
/* 313 */       localIterator2 = this.teams.values().iterator(); continue;ArenaTeam t2 = (ArenaTeam)localIterator2.next();
/* 314 */       Score score = obj.getScore(t2.getTeamScoreboardName());
/* 315 */       if (t2.getName().equals(team.getName())) {
/* 316 */         score.setScore(score.getScore() - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void decrementTimer()
/*     */   {
/* 323 */     if (this.timeleft <= 0) {
/* 324 */       if (!this.ended) {
/* 325 */         CivMessage.sendArena(this, "Time is up! Nobody Wins!");
/* 326 */         ArenaManager.declareDraw(this);
/* 327 */         this.ended = true;
/*     */       }
/*     */     } else {
/* 330 */       this.timeleft -= 1;
/*     */       
/* 332 */       for (ArenaTeam team : this.teams.values()) {
/* 333 */         Objective obj = (Objective)this.objectives.get(team.getName() + ";score");
/* 334 */         Score score = obj.getScore(Bukkit.getOfflinePlayer("Time Left"));
/* 335 */         score.setScore(this.timeleft);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\arena\Arena.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */