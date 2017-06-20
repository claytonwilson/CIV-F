/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigTownLevel;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
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
/*     */ 
/*     */ public class TownChunk
/*     */   extends SQLObject
/*     */ {
/*     */   private ChunkCoord chunkLocation;
/*     */   private Town town;
/*     */   private boolean forSale;
/*     */   private double value;
/*     */   private double price;
/*     */   private boolean outpost;
/*  58 */   public PlotPermissions perms = new PlotPermissions();
/*     */   public static final String TABLE_NAME = "TOWNCHUNKS";
/*     */   
/*     */   public TownChunk(ResultSet rs) throws SQLException, CivException
/*     */   {
/*  63 */     load(rs);
/*     */   }
/*     */   
/*     */   public TownChunk(Town newTown, Location location) {
/*  67 */     ChunkCoord coord = new ChunkCoord(location);
/*  68 */     setTown(newTown);
/*  69 */     setChunkCord(coord);
/*  70 */     this.perms.addGroup(newTown.getDefaultGroup());
/*     */   }
/*     */   
/*     */   public TownChunk(Town newTown, ChunkCoord chunkLocation) {
/*  74 */     setTown(newTown);
/*  75 */     setChunkCord(chunkLocation);
/*  76 */     this.perms.addGroup(newTown.getDefaultGroup());
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException {
/*  80 */     if (!SQL.hasTable("TOWNCHUNKS")) {
/*  81 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "TOWNCHUNKS" + " (" + 
/*  82 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  83 */         "`town_id` int(11) unsigned NOT NULL," + 
/*  84 */         "`world` VARCHAR(32) NOT NULL," + 
/*  85 */         "`x` bigint(20) NOT NULL," + 
/*  86 */         "`z` bigint(20) NOT NULL," + 
/*  87 */         "`owner_id` int(11) unsigned DEFAULT NULL," + 
/*  88 */         "`groups` mediumtext DEFAULT NULL," + 
/*  89 */         "`permissions` mediumtext NOT NULL," + 
/*  90 */         "`for_sale` bool NOT NULL DEFAULT '0'," + 
/*  91 */         "`value` float NOT NULL DEFAULT '0'," + 
/*  92 */         "`price` float NOT NULL DEFAULT '0'," + 
/*  93 */         "`outpost` bool DEFAULT '0'," + 
/*     */         
/*     */ 
/*  96 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  98 */       SQL.makeTable(table_create);
/*  99 */       CivLog.info("Created TOWNCHUNKS table");
/*     */     } else {
/* 101 */       CivLog.info("TOWNCHUNKS table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException, CivException
/*     */   {
/* 107 */     setId(rs.getInt("id"));
/* 108 */     setTown(CivGlobal.getTownFromId(rs.getInt("town_id")));
/* 109 */     if (getTown() == null) {
/* 110 */       CivLog.warning("TownChunk tried to load without a town...");
/* 111 */       if (CivGlobal.testFileFlag("cleanupDatabase")) {
/* 112 */         CivLog.info("CLEANING");
/* 113 */         delete();
/*     */       }
/* 115 */       throw new CivException("No town(" + rs.getInt("town_id") + ") to load this town chunk(" + rs.getInt("id"));
/*     */     }
/*     */     
/* 118 */     ChunkCoord cord = new ChunkCoord(rs.getString("world"), rs.getInt("x"), rs.getInt("z"));
/* 119 */     setChunkCord(cord);
/*     */     try
/*     */     {
/* 122 */       this.perms.loadFromSaveString(this.town, rs.getString("permissions"));
/*     */     } catch (CivException e) {
/* 124 */       e.printStackTrace();
/*     */     }
/*     */     
/* 127 */     this.perms.setOwner(CivGlobal.getResidentFromId(rs.getInt("owner_id")));
/*     */     
/* 129 */     String grpString = rs.getString("groups");
/* 130 */     if (grpString != null) {
/* 131 */       String[] groups = grpString.split(":");
/* 132 */       String[] arrayOfString1; int j = (arrayOfString1 = groups).length; for (int i = 0; i < j; i++) { String grp = arrayOfString1[i];
/* 133 */         this.perms.addGroup(CivGlobal.getPermissionGroup(getTown(), Integer.valueOf(grp)));
/*     */       }
/*     */     }
/*     */     
/* 137 */     this.forSale = rs.getBoolean("for_sale");
/* 138 */     this.value = rs.getDouble("value");
/* 139 */     this.price = rs.getDouble("price");
/* 140 */     this.outpost = rs.getBoolean("outpost");
/*     */     
/* 142 */     if (!this.outpost) {
/*     */       try {
/* 144 */         getTown().addTownChunk(this);
/*     */       } catch (AlreadyRegisteredException e1) {
/* 146 */         e1.printStackTrace();
/*     */       }
/*     */     } else {
/*     */       try {
/* 150 */         getTown().addOutpostChunk(this);
/*     */       } catch (AlreadyRegisteredException e) {
/* 152 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void save()
/*     */   {
/* 160 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 165 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/* 167 */     hashmap.put("id", Integer.valueOf(getId()));
/* 168 */     hashmap.put("town_id", Integer.valueOf(getTown().getId()));
/* 169 */     hashmap.put("world", getChunkCoord().getWorldname());
/* 170 */     hashmap.put("x", Integer.valueOf(getChunkCoord().getX()));
/* 171 */     hashmap.put("z", Integer.valueOf(getChunkCoord().getZ()));
/* 172 */     hashmap.put("permissions", this.perms.getSaveString());
/* 173 */     hashmap.put("for_sale", Boolean.valueOf(isForSale()));
/* 174 */     hashmap.put("value", Double.valueOf(getValue()));
/* 175 */     hashmap.put("price", Double.valueOf(getPrice()));
/* 176 */     hashmap.put("outpost", Boolean.valueOf(this.outpost));
/*     */     
/* 178 */     if (this.perms.getOwner() != null) {
/* 179 */       hashmap.put("owner_id", Integer.valueOf(this.perms.getOwner().getId()));
/*     */     } else {
/* 181 */       hashmap.put("owner_id", null);
/*     */     }
/*     */     
/* 184 */     if (this.perms.getGroups().size() != 0) {
/* 185 */       String out = "";
/* 186 */       for (PermissionGroup grp : this.perms.getGroups()) {
/* 187 */         out = out + grp.getId() + ":";
/*     */       }
/* 189 */       hashmap.put("groups", out);
/*     */     } else {
/* 191 */       hashmap.put("groups", null);
/*     */     }
/*     */     
/* 194 */     SQL.updateNamedObject(this, hashmap, "TOWNCHUNKS");
/*     */   }
/*     */   
/*     */   public Town getTown() {
/* 198 */     return this.town;
/*     */   }
/*     */   
/*     */   public void setTown(Town town) {
/* 202 */     this.town = town;
/*     */   }
/*     */   
/*     */   public ChunkCoord getChunkCoord() {
/* 206 */     return this.chunkLocation;
/*     */   }
/*     */   
/*     */   public void setChunkCord(ChunkCoord chunkLocation) {
/* 210 */     this.chunkLocation = chunkLocation;
/*     */   }
/*     */   
/*     */   public static double getNextPlotCost(Town town)
/*     */   {
/* 215 */     ConfigTownLevel effectiveTownLevel = (ConfigTownLevel)CivSettings.townLevels.get(Integer.valueOf(CivSettings.townLevels.size()));
/* 216 */     int currentPlotCount = town.getTownChunks().size();
/*     */     
/* 218 */     for (ConfigTownLevel lvl : CivSettings.townLevels.values()) {
/* 219 */       if ((currentPlotCount < lvl.plots) && 
/* 220 */         (effectiveTownLevel.plots > lvl.plots)) {
/* 221 */         effectiveTownLevel = lvl;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 227 */     return effectiveTownLevel.plot_cost;
/*     */   }
/*     */   
/*     */   public static TownChunk claim(Town town, ChunkCoord coord, boolean outpost) throws CivException {
/* 231 */     if (CivGlobal.getTownChunk(coord) != null) {
/* 232 */       throw new CivException("This plot is already claimed.");
/*     */     }
/*     */     
/*     */ 
/* 236 */     double cost = getNextPlotCost(town);
/*     */     
/* 238 */     if (!town.hasEnough(cost)) {
/* 239 */       throw new CivException("The town does not have the required " + cost + " coins to claim this plot.");
/*     */     }
/*     */     
/* 242 */     CultureChunk cultureChunk = CivGlobal.getCultureChunk(coord);
/* 243 */     if ((cultureChunk == null) || (cultureChunk.getCiv() != town.getCiv())) {
/* 244 */       throw new CivException("Cannot claim a town chunk when not in your culture.");
/*     */     }
/*     */     
/* 247 */     TownChunk tc = new TownChunk(town, coord);
/*     */     
/* 249 */     if (!outpost) {
/* 250 */       if (!tc.isOnEdgeOfOwnership()) {
/* 251 */         throw new CivException("Can only claim on the edge of town's ownership.");
/*     */       }
/*     */       
/* 254 */       if (!town.canClaim()) {
/* 255 */         throw new CivException("Town is unable to claim, doesn't have enough plots for this town level.");
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 261 */       int min_distance = CivSettings.getInteger(CivSettings.civConfig, "civ.min_distance").intValue();
/*     */       
/* 263 */       for (TownChunk cc : CivGlobal.getTownChunks()) {
/* 264 */         if (cc.getCiv() != town.getCiv()) {
/* 265 */           double dist = coord.distance(cc.getChunkCoord());
/* 266 */           if (dist <= min_distance) {
/* 267 */             throw new CivException("Too close to the culture of " + cc.getCiv().getName() + ", cannot claim here.");
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (InvalidConfiguration e1) {
/* 272 */       e1.printStackTrace();
/* 273 */       throw new CivException("Internal configuration exception.");
/*     */     }
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
/* 289 */     if (!outpost) {
/*     */       try {
/* 291 */         town.addTownChunk(tc);
/*     */       } catch (AlreadyRegisteredException e1) {
/* 293 */         e1.printStackTrace();
/* 294 */         throw new CivException("Internal Error Occurred.");
/*     */       }
/*     */     } else {
/*     */       try
/*     */       {
/* 299 */         town.addOutpostChunk(tc);
/*     */       } catch (AlreadyRegisteredException e) {
/* 301 */         e.printStackTrace();
/* 302 */         throw new CivException("Internal Error Occurred.");
/*     */       }
/*     */     }
/*     */     
/* 306 */     Camp camp = CivGlobal.getCampFromChunk(coord);
/* 307 */     if (camp != null) {
/* 308 */       CivMessage.sendCamp(camp, "§e" + ChatColor.BOLD + "Our camp's land was claimed by the town of " + town.getName() + " and has been disbaned!");
/* 309 */       camp.disband();
/*     */     }
/*     */     
/* 312 */     tc.setOutpost(outpost);
/* 313 */     tc.save();
/* 314 */     town.withdraw(cost);
/* 315 */     CivGlobal.addTownChunk(tc);
/* 316 */     CivGlobal.processCulture();
/* 317 */     return tc;
/*     */   }
/*     */   
/*     */   public static TownChunk claim(Town town, Player player, boolean outpost) throws CivException
/*     */   {
/* 322 */     double cost = getNextPlotCost(town);
/* 323 */     TownChunk tc = claim(town, new ChunkCoord(player.getLocation()), outpost);
/* 324 */     CivMessage.sendSuccess(player, "Claimed chunk at " + tc.getChunkCoord() + " for " + "§e" + cost + "§a" + " coins.");
/* 325 */     return tc;
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
/*     */   private Civilization getCiv()
/*     */   {
/* 373 */     return getTown().getCiv();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TownChunk townHallClaim(Town town, ChunkCoord coord)
/*     */     throws CivException
/*     */   {
/* 384 */     if (CivGlobal.getTownChunk(coord) != null) {
/* 385 */       throw new CivException("This plot is already claimed.");
/*     */     }
/*     */     
/* 388 */     TownChunk tc = new TownChunk(town, coord);
/*     */     try
/*     */     {
/* 391 */       town.addTownChunk(tc);
/*     */     } catch (AlreadyRegisteredException e1) {
/* 393 */       e1.printStackTrace();
/* 394 */       throw new CivException("Internal Error Occurred.");
/*     */     }
/*     */     
/*     */ 
/* 398 */     Camp camp = CivGlobal.getCampFromChunk(coord);
/* 399 */     if (camp != null) {
/* 400 */       CivMessage.sendCamp(camp, "§e" + ChatColor.BOLD + "Our camp's land was claimed by the town of " + town.getName() + " and has been disbaned!");
/* 401 */       camp.disband();
/*     */     }
/*     */     
/* 404 */     CivGlobal.addTownChunk(tc);
/* 405 */     tc.save();
/* 406 */     return tc;
/*     */   }
/*     */   
/*     */   private boolean isOnEdgeOfOwnership()
/*     */   {
/* 411 */     int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/*     */     
/* 413 */     for (int i = 0; i < 4; i++) {
/* 414 */       TownChunk tc = CivGlobal.getTownChunk(new ChunkCoord(getChunkCoord().getWorldname(), 
/* 415 */         getChunkCoord().getX() + offset[i][0], 
/* 416 */         getChunkCoord().getZ() + offset[i][1]));
/* 417 */       if ((tc != null) && 
/* 418 */         (tc.getTown() == getTown()) && 
/* 419 */         (!tc.isOutpost())) {
/* 420 */         return true;
/*     */       }
/*     */     }
/* 423 */     return false;
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 428 */     SQL.deleteNamedObject(this, "TOWNCHUNKS");
/* 429 */     CivGlobal.removeTownChunk(this);
/*     */   }
/*     */   
/*     */   public boolean isForSale() {
/* 433 */     return this.forSale;
/*     */   }
/*     */   
/*     */   public void setForSale(boolean forSale) {
/* 437 */     this.forSale = forSale;
/*     */   }
/*     */   
/*     */   public double getValue() {
/* 441 */     return this.value;
/*     */   }
/*     */   
/*     */   public String getOnEnterString(Player player, TownChunk fromTc)
/*     */   {
/* 446 */     String out = "";
/*     */     
/* 448 */     if (this.perms.getOwner() != null) {
/* 449 */       out = out + "§7[Owned by: §a" + this.perms.getOwner().getName() + "§7" + "]";
/*     */     }
/*     */     
/* 452 */     if ((this.perms.getOwner() == null) && (fromTc != null) && (fromTc.perms.getOwner() != null)) {
/* 453 */       out = out + "§7[Unowned]";
/*     */     }
/*     */     
/* 456 */     if (isForSale()) {
/* 457 */       out = out + "§e[For Sale: " + this.price + " coins]";
/*     */     }
/*     */     
/* 460 */     return out;
/*     */   }
/*     */   
/*     */   public void purchase(Resident resident) throws CivException
/*     */   {
/* 465 */     if (!resident.getTreasury().hasEnough(this.price)) {
/* 466 */       throw new CivException("You do not have the required " + this.price + " coins to purchase this plot.");
/*     */     }
/*     */     
/* 469 */     if (this.perms.getOwner() == null) {
/* 470 */       resident.getTreasury().payTo(getTown().getTreasury(), this.price);
/*     */     } else {
/* 472 */       resident.getTreasury().payTo(this.perms.getOwner().getTreasury(), this.price);
/*     */     }
/*     */     
/* 475 */     this.value = this.price;
/* 476 */     this.price = 0.0D;
/* 477 */     this.forSale = false;
/* 478 */     this.perms.setOwner(resident);
/* 479 */     this.perms.clearGroups();
/*     */     
/* 481 */     save();
/*     */   }
/*     */   
/*     */   public double getPrice() {
/* 485 */     return this.price;
/*     */   }
/*     */   
/*     */   public void setPrice(double price) {
/* 489 */     this.price = price;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCenterString()
/*     */   {
/* 501 */     return this.chunkLocation.toString();
/*     */   }
/*     */   
/*     */   public boolean isEdgeBlock()
/*     */   {
/* 506 */     int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/*     */     
/* 508 */     if (isOutpost()) {
/* 509 */       return false;
/*     */     }
/*     */     
/* 512 */     for (int i = 0; i < 4; i++) {
/* 513 */       TownChunk next = CivGlobal.getTownChunk(new ChunkCoord(this.chunkLocation.getWorldname(), 
/* 514 */         this.chunkLocation.getX() + offset[i][0], 
/* 515 */         this.chunkLocation.getZ() + offset[i][1]));
/* 516 */       if ((next == null) || (next.isOutpost())) {
/* 517 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 521 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void unclaim(TownChunk tc)
/*     */     throws CivException
/*     */   {
/* 531 */     tc.getTown().removeTownChunk(tc);
/*     */     try {
/* 533 */       tc.delete();
/*     */     } catch (SQLException e) {
/* 535 */       e.printStackTrace();
/* 536 */       throw new CivException("Internal database error.");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOutpost()
/*     */   {
/* 542 */     return this.outpost;
/*     */   }
/*     */   
/*     */   public void setOutpost(boolean outpost) {
/* 546 */     this.outpost = outpost;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\TownChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */