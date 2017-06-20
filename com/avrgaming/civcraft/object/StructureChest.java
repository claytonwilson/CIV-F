/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.InvalidObjectException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
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
/*     */ public class StructureChest
/*     */   extends SQLObject
/*     */ {
/*     */   private BlockCoord coord;
/*     */   private Buildable owner;
/*     */   private int direction;
/*     */   private int chestId;
/*     */   
/*     */   public StructureChest(BlockCoord coord, Buildable owner)
/*     */   {
/*  43 */     setCoord(coord);
/*  44 */     setOwner(owner);
/*     */   }
/*     */   
/*     */   public StructureChest(ResultSet rs) throws SQLException, InvalidObjectException {
/*  48 */     load(rs);
/*     */   }
/*     */   
/*  51 */   public static String TABLE_NAME = "STRUCTURE_CHESTS";
/*     */   
/*  53 */   public static void init() throws SQLException { if (!SQL.hasTable(TABLE_NAME)) {
/*  54 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/*  55 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  56 */         "`structure_id` int(11), " + 
/*  57 */         "`chest_id` int(11), " + 
/*  58 */         "`coordHash` mediumtext, " + 
/*  59 */         "`direction` int(11), " + 
/*  60 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  62 */       SQL.makeTable(table_create);
/*  63 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/*  65 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException, InvalidObjectException
/*     */   {
/*  71 */     setId(rs.getInt("id"));
/*  72 */     Buildable owner = CivGlobal.getStructureById(rs.getInt("structure_id"));
/*  73 */     if (owner == null) {
/*  74 */       CivLog.warning("Couldn't find structure id:" + rs.getInt("structure_id") + " while loading structure chests.");
/*  75 */       throw new InvalidObjectException("Couldn't find structure id:" + rs.getInt("structure_id"));
/*     */     }
/*     */     
/*  78 */     this.owner = owner;
/*  79 */     this.direction = rs.getInt("direction");
/*  80 */     this.coord = new BlockCoord(rs.getString("coordHash"));
/*  81 */     this.chestId = rs.getInt("chest_id");
/*  82 */     owner.addStructureChest(this);
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/*  87 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/*  92 */     HashMap<String, Object> hashmap = new HashMap();
/*  93 */     hashmap.put("structure_id", Integer.valueOf(this.owner.getId()));
/*  94 */     hashmap.put("coordHash", this.coord.toString());
/*  95 */     hashmap.put("direction", Integer.valueOf(this.direction));
/*  96 */     hashmap.put("chest_id", Integer.valueOf(this.chestId));
/*     */     
/*  98 */     SQL.updateNamedObject(this, hashmap, TABLE_NAME);
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 103 */     SQL.deleteNamedObject(this, TABLE_NAME);
/* 104 */     CivGlobal.removeStructureChest(this);
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord() {
/* 108 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(BlockCoord coord) {
/* 112 */     this.coord = coord;
/*     */   }
/*     */   
/*     */   public Buildable getOwner() {
/* 116 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(Buildable owner) {
/* 120 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public int getDirection() {
/* 124 */     return this.direction;
/*     */   }
/*     */   
/*     */   public void setDirection(int direction) {
/* 128 */     this.direction = direction;
/*     */   }
/*     */   
/*     */   public int getChestId() {
/* 132 */     return this.chestId;
/*     */   }
/*     */   
/*     */   public void setChestId(int chestId) {
/* 136 */     this.chestId = chestId;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\StructureChest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */