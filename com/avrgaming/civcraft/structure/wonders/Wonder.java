/*     */ package com.avrgaming.civcraft.structure.wonders;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuff;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.config.ConfigWonderBuff;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.io.IOException;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
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
/*     */ public abstract class Wonder
/*     */   extends Buildable
/*     */ {
/*  48 */   public static String TABLE_NAME = "WONDERS";
/*  49 */   private ConfigWonderBuff wonderBuffs = null;
/*     */   
/*     */   public Wonder(ResultSet rs) throws SQLException, CivException {
/*  52 */     load(rs);
/*     */     
/*  54 */     if (this.hitpoints == 0) {
/*  55 */       delete();
/*     */     }
/*     */   }
/*     */   
/*     */   public Wonder(Location center, String id, Town town) throws CivException
/*     */   {
/*  61 */     this.info = ((ConfigBuildableInfo)CivSettings.wonders.get(id));
/*  62 */     setTown(town);
/*  63 */     setCorner(new BlockCoord(center));
/*  64 */     this.hitpoints = this.info.max_hitpoints;
/*     */     
/*     */ 
/*  67 */     Wonder wonder = CivGlobal.getWonder(getCorner());
/*  68 */     if (wonder != null) {
/*  69 */       throw new CivException("There is a wonder already here.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadSettings() {
/*  74 */     this.wonderBuffs = ((ConfigWonderBuff)CivSettings.wonderBuffs.get(getConfigId()));
/*     */     
/*  76 */     if ((isComplete()) && (isActive())) {
/*  77 */       addWonderBuffsToTown();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException {
/*  82 */     if (!SQL.hasTable(TABLE_NAME)) {
/*  83 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/*  84 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  85 */         "`type_id` mediumtext NOT NULL," + 
/*  86 */         "`town_id` int(11) DEFAULT NULL," + 
/*  87 */         "`complete` bool NOT NULL DEFAULT '0'," + 
/*  88 */         "`builtBlockCount` int(11) DEFAULT NULL, " + 
/*  89 */         "`cornerBlockHash` mediumtext DEFAULT NULL," + 
/*  90 */         "`template_name` mediumtext DEFAULT NULL, " + 
/*  91 */         "`template_x` int(11) DEFAULT NULL, " + 
/*  92 */         "`template_y` int(11) DEFAULT NULL, " + 
/*  93 */         "`template_z` int(11) DEFAULT NULL, " + 
/*  94 */         "`hitpoints` int(11) DEFAULT '100'," + 
/*  95 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  97 */       SQL.makeTable(table_create);
/*  98 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/* 100 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, CivException
/*     */   {
/* 107 */     setId(rs.getInt("id"));
/* 108 */     this.info = ((ConfigBuildableInfo)CivSettings.wonders.get(rs.getString("type_id")));
/* 109 */     setTown(CivGlobal.getTownFromId(rs.getInt("town_id")));
/* 110 */     if (getTown() == null)
/*     */     {
/* 112 */       throw new CivException("Coudln't find town ID:" + rs.getInt("town_id") + " for wonder " + getDisplayName() + " ID:" + getId());
/*     */     }
/*     */     
/* 115 */     setCorner(new BlockCoord(rs.getString("cornerBlockHash")));
/* 116 */     this.hitpoints = rs.getInt("hitpoints");
/* 117 */     setTemplateName(rs.getString("template_name"));
/* 118 */     setTemplateX(rs.getInt("template_x"));
/* 119 */     setTemplateY(rs.getInt("template_y"));
/* 120 */     setTemplateZ(rs.getInt("template_z"));
/* 121 */     setComplete(rs.getBoolean("complete"));
/* 122 */     setBuiltBlockCount(rs.getInt("builtBlockCount"));
/*     */     
/*     */ 
/* 125 */     getTown().addWonder(this);
/* 126 */     bindStructureBlocks();
/*     */     
/* 128 */     if (!isComplete()) {
/*     */       try {
/* 130 */         resumeBuildFromTemplate();
/*     */       } catch (Exception e) {
/* 132 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 139 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 144 */     HashMap<String, Object> hashmap = new HashMap();
/* 145 */     hashmap.put("type_id", getConfigId());
/* 146 */     hashmap.put("town_id", Integer.valueOf(getTown().getId()));
/* 147 */     hashmap.put("complete", Boolean.valueOf(isComplete()));
/* 148 */     hashmap.put("builtBlockCount", Integer.valueOf(getBuiltBlockCount()));
/* 149 */     hashmap.put("cornerBlockHash", getCorner().toString());
/* 150 */     hashmap.put("hitpoints", Integer.valueOf(getHitpoints()));
/* 151 */     hashmap.put("template_name", getSavedTemplatePath());
/* 152 */     hashmap.put("template_x", Integer.valueOf(getTemplateX()));
/* 153 */     hashmap.put("template_y", Integer.valueOf(getTemplateY()));
/* 154 */     hashmap.put("template_z", Integer.valueOf(getTemplateZ()));
/* 155 */     SQL.updateNamedObject(this, hashmap, TABLE_NAME);
/*     */   }
/*     */   
/*     */   public void delete()
/*     */     throws SQLException
/*     */   {
/* 161 */     super.delete();
/*     */     
/* 163 */     if (this.wonderBuffs != null) {
/* 164 */       for (ConfigBuff buff : this.wonderBuffs.buffs) {
/* 165 */         getTown().getBuffManager().removeBuff(buff.id);
/*     */       }
/*     */     }
/*     */     
/* 169 */     SQL.deleteNamedObject(this, TABLE_NAME);
/* 170 */     CivGlobal.removeWonder(this);
/*     */   }
/*     */   
/*     */   public void updateBuildProgess()
/*     */   {
/* 175 */     if (getId() != 0) {
/* 176 */       HashMap<String, Object> struct_hm = new HashMap();
/* 177 */       struct_hm.put("id", Integer.valueOf(getId()));
/* 178 */       struct_hm.put("type_id", getConfigId());
/* 179 */       struct_hm.put("complete", Boolean.valueOf(isComplete()));
/* 180 */       struct_hm.put("builtBlockCount", Integer.valueOf(this.savedBlockCount));
/*     */       try
/*     */       {
/* 183 */         SQL.updateNamedObjectAsync(this, struct_hm, TABLE_NAME);
/*     */       } catch (SQLException e) {
/* 185 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isWonderAvailable(String configId) {
/* 191 */     if (CivGlobal.isCasualMode()) {
/* 192 */       return true;
/*     */     }
/*     */     
/* 195 */     for (Wonder wonder : CivGlobal.getWonders()) {
/* 196 */       if ((wonder.getConfigId().equals(configId)) && 
/* 197 */         (wonder.isComplete())) {
/* 198 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 203 */     return true;
/*     */   }
/*     */   
/*     */   public void processUndo() throws CivException
/*     */   {
/*     */     try
/*     */     {
/* 210 */       undoFromTemplate();
/*     */     } catch (IOException e1) {
/* 212 */       e1.printStackTrace();
/* 213 */       CivMessage.sendTown(getTown(), "§cCouldn't find undo data! Destroying structure instead.");
/* 214 */       fancyDestroyStructureBlocks();
/*     */     }
/*     */     
/* 217 */     CivMessage.global(
/* 218 */       "The §a" + getDisplayName() + "§f" + " has been unbuilt by " + getTown().getName() + "(" + getTown().getCiv().getName() + ") with the undo command.");
/*     */     
/* 220 */     double refund = getCost();
/* 221 */     getTown().depositDirect(refund);
/* 222 */     CivMessage.sendTown(getTown(), "Town refunded " + refund + " coins.");
/*     */     
/* 224 */     unbindStructureBlocks();
/*     */     try
/*     */     {
/* 227 */       delete();
/* 228 */       getTown().removeWonder(this);
/*     */     } catch (SQLException e) {
/* 230 */       e.printStackTrace();
/* 231 */       throw new CivException("Internal database error.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void build(Player player, Location centerLoc, Template tpl)
/*     */     throws Exception
/*     */   {
/* 240 */     Location savedLocation = centerLoc.clone();
/*     */     
/* 242 */     centerLoc = repositionCenter(centerLoc, tpl.dir(), tpl.size_x, tpl.size_z);
/* 243 */     Block centerBlock = centerLoc.getBlock();
/*     */     
/*     */ 
/* 246 */     setTotalBlockCount(tpl.size_x * tpl.size_y * tpl.size_z);
/*     */     
/*     */ 
/* 249 */     setTemplateName(tpl.getFilepath());
/* 250 */     setTemplateX(tpl.size_x);
/* 251 */     setTemplateY(tpl.size_y);
/* 252 */     setTemplateZ(tpl.size_z);
/* 253 */     setTemplateAABB(new BlockCoord(centerLoc), tpl);
/*     */     
/* 255 */     checkBlockPermissionsAndRestrictions(player, centerBlock, tpl.size_x, tpl.size_y, tpl.size_z, savedLocation);
/* 256 */     runOnBuild(centerLoc, tpl);
/*     */     
/*     */ 
/* 259 */     getTown().lastBuildableBuilt = this;
/* 260 */     tpl.saveUndoTemplate(getCorner().toString(), getTown().getName(), centerLoc);
/* 261 */     tpl.buildScaffolding(centerLoc);
/*     */     
/*     */ 
/* 264 */     startBuildTask(tpl, centerLoc);
/*     */     
/* 266 */     save();
/* 267 */     CivGlobal.addWonder(this);
/* 268 */     CivMessage.global(getCiv().getName() + " has started construction of " + getDisplayName() + " in the town of " + getTown().getName());
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDynmapDescription()
/*     */   {
/* 274 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMarkerIconName()
/*     */   {
/* 280 */     return "beer";
/*     */   }
/*     */   
/*     */   protected void runOnBuild(Location centerLoc, Template tpl)
/*     */     throws CivException
/*     */   {}
/*     */   
/*     */   public void onDestroy()
/*     */   {
/* 289 */     if (!CivGlobal.isCasualMode())
/*     */     {
/* 291 */       CivMessage.global(getDisplayName() + " in " + getTown().getName() + " has been destroyed! Any town may now build it again!");
/*     */       try {
/* 293 */         getTown().removeWonder(this);
/* 294 */         fancyDestroyStructureBlocks();
/* 295 */         unbindStructureBlocks();
/* 296 */         delete();
/*     */       } catch (SQLException e) {
/* 298 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static Wonder newWonder(Location center, String id, Town town) throws CivException {
/*     */     try {
/* 305 */       return _newWonder(center, id, town, null);
/*     */     }
/*     */     catch (SQLException e) {
/* 308 */       e.printStackTrace(); }
/* 309 */     return null;
/*     */   }
/*     */   
/*     */   public static Wonder _newWonder(Location center, String id, Town town, ResultSet rs) throws CivException, SQLException {
/*     */     String str;
/*     */     Wonder wonder;
/* 315 */     switch ((str = id).hashCode()) {case -1847496618:  if (str.equals("w_greatlibrary")) break; break; case 489390797:  if (str.equals("w_council_of_eight")) {} break; case 856715638:  if (str.equals("w_hanginggardens")) {} break; case 1398468103:  if (str.equals("w_chichen_itza")) {} break; case 1744750582:  if (str.equals("w_notre_dame")) {} break; case 1846438709:  if (str.equals("w_colossus")) {} case 1913111944:  if ((goto 394) && (str.equals("w_pyramid"))) {
/*     */         Wonder wonder;
/* 317 */         if (rs == null) {
/* 318 */           wonder = new TheGreatPyramid(center, id, town);
/*     */         } else {
/* 320 */           Wonder wonder = new TheGreatPyramid(rs);
/*     */           
/*     */           break label419;
/*     */           Wonder wonder;
/* 324 */           if (rs == null) {
/* 325 */             wonder = new GreatLibrary(center, id, town);
/*     */           } else {
/* 327 */             Wonder wonder = new GreatLibrary(rs);
/*     */             
/*     */             break label419;
/*     */             Wonder wonder;
/* 331 */             if (rs == null) {
/* 332 */               wonder = new TheHangingGardens(center, id, town);
/*     */             } else {
/* 334 */               Wonder wonder = new TheHangingGardens(rs);
/*     */               
/*     */               break label419;
/*     */               Wonder wonder;
/* 338 */               if (rs == null) {
/* 339 */                 wonder = new TheColossus(center, id, town);
/*     */               } else {
/* 341 */                 Wonder wonder = new TheColossus(rs);
/*     */                 
/*     */                 break label419;
/*     */                 Wonder wonder;
/* 345 */                 if (rs == null) {
/* 346 */                   wonder = new NotreDame(center, id, town);
/*     */                 } else {
/* 348 */                   Wonder wonder = new NotreDame(rs);
/*     */                   
/*     */                   break label419;
/*     */                   Wonder wonder;
/* 352 */                   if (rs == null) {
/* 353 */                     wonder = new ChichenItza(center, id, town);
/*     */                   } else {
/* 355 */                     Wonder wonder = new ChichenItza(rs);
/*     */                     
/*     */                     break label419;
/*     */                     Wonder wonder;
/* 359 */                     if (rs == null) {
/* 360 */                       wonder = new CouncilOfEight(center, id, town);
/*     */                     } else
/* 362 */                       wonder = new CouncilOfEight(rs);
/*     */                   } } } } } } }
/* 364 */       break;
/*     */     }
/* 366 */     throw new CivException("Unknown wonder type " + id);
/*     */     label419:
/*     */     Wonder wonder;
/* 369 */     wonder.loadSettings();
/* 370 */     return wonder;
/*     */   }
/*     */   
/*     */   public void addWonderBuffsToTown()
/*     */   {
/* 375 */     if (this.wonderBuffs == null) {
/* 376 */       return;
/*     */     }
/*     */     
/* 379 */     for (ConfigBuff buff : this.wonderBuffs.buffs) {
/*     */       try {
/* 381 */         getTown().getBuffManager().addBuff("wonder:" + getDisplayName() + ":" + getCorner() + ":" + buff.id, 
/* 382 */           buff.id, getDisplayName());
/*     */       } catch (CivException e) {
/* 384 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onComplete()
/*     */   {
/* 391 */     addWonderBuffsToTown();
/*     */   }
/*     */   
/*     */   public ConfigWonderBuff getWonderBuffs() {
/* 395 */     return this.wonderBuffs;
/*     */   }
/*     */   
/*     */   public void setWonderBuffs(ConfigWonderBuff wonderBuffs)
/*     */   {
/* 400 */     this.wonderBuffs = wonderBuffs;
/*     */   }
/*     */   
/*     */   public static Wonder newWonder(ResultSet rs) throws CivException, SQLException {
/* 404 */     return _newWonder(null, rs.getString("type_id"), null, rs);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onLoad() {}
/*     */   
/*     */ 
/*     */   public void onUnload() {}
/*     */   
/*     */   protected void addBuffToTown(Town town, String id)
/*     */   {
/*     */     try
/*     */     {
/* 417 */       town.getBuffManager().addBuff(id, id, getDisplayName() + " in " + getTown().getName());
/*     */     } catch (CivException e) {
/* 419 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addBuffToCiv(Civilization civ, String id) {
/* 424 */     for (Town t : civ.getTowns()) {
/* 425 */       addBuffToTown(t, id);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void removeBuffFromTown(Town town, String id) {
/* 430 */     town.getBuffManager().removeBuff(id);
/*     */   }
/*     */   
/*     */   protected void removeBuffFromCiv(Civilization civ, String id) {
/* 434 */     for (Town t : civ.getTowns())
/* 435 */       removeBuffFromTown(t, id);
/*     */   }
/*     */   
/*     */   protected abstract void removeBuffs();
/*     */   
/*     */   protected abstract void addBuffs();
/*     */   
/*     */   public void processCoinsFromCulture() {
/* 443 */     int cultureCount = 0;
/* 444 */     for (Town t : getCiv().getTowns()) {
/* 445 */       cultureCount += t.getCultureChunks().size();
/*     */     }
/*     */     
/* 448 */     double coinsPerCulture = Double.valueOf(((ConfigBuff)CivSettings.buffs.get("buff_colossus_coins_from_culture")).value).doubleValue();
/*     */     
/* 450 */     double total = coinsPerCulture * cultureCount;
/* 451 */     getCiv().getTreasury().deposit(total);
/*     */     
/* 453 */     CivMessage.sendCiv(getCiv(), "§aThe Colossus generated §e" + total + "§a" + " coins from culture.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\Wonder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */