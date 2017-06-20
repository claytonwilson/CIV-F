/*     */ package com.avrgaming.civcraft.road;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.exception.InvalidObjectException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.SQLObject;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.StructureBlockHitEvent;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class RoadBlock
/*     */   extends SQLObject
/*     */   implements BuildableDamageBlock
/*     */ {
/*     */   private BlockCoord coord;
/*     */   private Road road;
/*  32 */   private boolean aboveRoadBlock = false;
/*     */   private int oldType;
/*     */   private int oldData;
/*     */   public static final String TABLE_NAME = "ROADBLOCKS";
/*     */   
/*     */   public RoadBlock(ResultSet rs)
/*     */     throws SQLException, InvalidNameException, InvalidObjectException, CivException
/*     */   {
/*  40 */     load(rs);
/*     */   }
/*     */   
/*     */   public RoadBlock(int oldType, int oldData) {
/*  44 */     this.oldType = oldType;
/*  45 */     this.oldData = oldData;
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException {
/*  49 */     if (!SQL.hasTable("ROADBLOCKS")) {
/*  50 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "ROADBLOCKS" + " (" + 
/*  51 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  52 */         "`road_id` int(11) NOT NULL DEFAULT 0," + 
/*  53 */         "`old_type` int(11) NOT NULL DEFAULT 0," + 
/*  54 */         "`old_data` int(11) NOT NULL DEFAULT 0," + 
/*  55 */         "`above_road` bool DEFAULT 0," + 
/*  56 */         "`coord` mediumtext DEFAULT NULL," + 
/*  57 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  59 */       SQL.makeTable(table_create);
/*  60 */       CivLog.info("Created ROADBLOCKS table");
/*     */     }
/*     */     else {
/*  63 */       if (!SQL.hasColumn("ROADBLOCKS", "old_type")) {
/*  64 */         CivLog.info("\tCouldn't find old_type column for roadblock.");
/*  65 */         SQL.addColumn("ROADBLOCKS", "`old_type` int(11) NOT NULL DEFAULT 0");
/*     */       }
/*     */       
/*  68 */       if (!SQL.hasColumn("ROADBLOCKS", "old_data")) {
/*  69 */         CivLog.info("\tCouldn't find old_data column for roadblock.");
/*  70 */         SQL.addColumn("ROADBLOCKS", "`old_data` int(11) NOT NULL DEFAULT 0");
/*     */       }
/*     */       
/*  73 */       if (!SQL.hasColumn("ROADBLOCKS", "above_road")) {
/*  74 */         CivLog.info("\tCouldn't find above_road column for roadblock.");
/*  75 */         SQL.addColumn("ROADBLOCKS", "`above_road` bool DEFAULT 0");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, InvalidNameException, InvalidObjectException, CivException
/*     */   {
/*  83 */     setId(rs.getInt("id"));
/*  84 */     setRoad((Road)CivGlobal.getStructureById(rs.getInt("road_id")));
/*  85 */     this.oldData = rs.getInt("old_data");
/*  86 */     this.oldType = rs.getInt("old_type");
/*  87 */     this.aboveRoadBlock = rs.getBoolean("above_road");
/*  88 */     if (this.road == null) {
/*  89 */       Integer id = Integer.valueOf(rs.getInt("road_id"));
/*  90 */       delete();
/*  91 */       throw new CivException("Couldn't load road block, could not find structure:" + id);
/*     */     }
/*     */     
/*  94 */     setCoord(new BlockCoord(rs.getString("coord")));
/*  95 */     this.road.addRoadBlock(this);
/*     */   }
/*     */   
/*     */   public void save() {
/*  99 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 104 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/* 106 */     hashmap.put("road_id", Integer.valueOf(getRoad().getId()));
/* 107 */     hashmap.put("coord", getCoord().toString());
/* 108 */     hashmap.put("old_type", Integer.valueOf(getOldType()));
/* 109 */     hashmap.put("old_data", Integer.valueOf(getOldData()));
/* 110 */     hashmap.put("above_road", Boolean.valueOf(this.aboveRoadBlock));
/*     */     
/* 112 */     SQL.updateNamedObject(this, hashmap, "ROADBLOCKS");
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 117 */     if ((this.coord != null) && (this.road != null)) {
/* 118 */       this.road.removeRoadBlock(this);
/*     */     }
/*     */     
/* 121 */     SQL.deleteNamedObject(this, "ROADBLOCKS");
/*     */   }
/*     */   
/*     */   public Road getRoad() {
/* 125 */     return this.road;
/*     */   }
/*     */   
/*     */   public void setRoad(Road road) {
/* 129 */     this.road = road;
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord() {
/* 133 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(BlockCoord coord) {
/* 137 */     this.coord = coord;
/*     */   }
/*     */   
/*     */   public boolean isAboveRoadBlock() {
/* 141 */     return this.aboveRoadBlock;
/*     */   }
/*     */   
/*     */   public void setAboveRoadBlock(boolean aboveRoadBlock) {
/* 145 */     this.aboveRoadBlock = aboveRoadBlock;
/*     */   }
/*     */   
/*     */   public boolean canHit() {
/* 149 */     Date now = new Date();
/*     */     
/* 151 */     if (now.after(this.road.getNextRaidDate())) {
/* 152 */       return true;
/*     */     }
/*     */     
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   public void onHit(Player player) {
/* 159 */     if (canHit()) {
/* 160 */       TaskMaster.syncTask(new StructureBlockHitEvent(player.getName(), getCoord(), this, player.getWorld()), 0L);
/*     */     } else {
/* 162 */       SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
/* 163 */       CivMessage.send(player, "§cCannot damage the road owned by " + getOwner().getCiv().getName() + " until " + sdf.format(this.road.getNextRaidDate()));
/*     */     }
/*     */   }
/*     */   
/*     */   public Buildable getOwner()
/*     */   {
/* 169 */     return this.road;
/*     */   }
/*     */   
/*     */   public void setOwner(Buildable owner)
/*     */   {
/* 174 */     this.road = ((Road)owner);
/*     */   }
/*     */   
/*     */   public Town getTown()
/*     */   {
/* 179 */     return this.road.getTown();
/*     */   }
/*     */   
/*     */   public Civilization getCiv()
/*     */   {
/* 184 */     return this.road.getCiv();
/*     */   }
/*     */   
/*     */   public int getX()
/*     */   {
/* 189 */     return this.coord.getX();
/*     */   }
/*     */   
/*     */   public int getY()
/*     */   {
/* 194 */     return this.coord.getY();
/*     */   }
/*     */   
/*     */   public int getZ()
/*     */   {
/* 199 */     return this.coord.getZ();
/*     */   }
/*     */   
/*     */   public String getWorldname()
/*     */   {
/* 204 */     return this.coord.getWorldname();
/*     */   }
/*     */   
/*     */   public boolean isDamageable()
/*     */   {
/* 209 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDamageable(boolean damageable) {}
/*     */   
/*     */ 
/*     */   public boolean canDestroyOnlyDuringWar()
/*     */   {
/* 219 */     return false;
/*     */   }
/*     */   
/*     */   public boolean allowDamageNow(Player player)
/*     */   {
/* 224 */     return true;
/*     */   }
/*     */   
/*     */   public int getOldType() {
/* 228 */     return this.oldType;
/*     */   }
/*     */   
/*     */   public void setOldType(int oldType) {
/* 232 */     this.oldType = oldType;
/*     */   }
/*     */   
/*     */   public int getOldData() {
/* 236 */     return this.oldData;
/*     */   }
/*     */   
/*     */   public void setOldData(int oldData) {
/* 240 */     this.oldData = oldData;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\road\RoadBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */