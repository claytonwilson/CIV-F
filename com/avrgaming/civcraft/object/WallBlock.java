/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.exception.InvalidObjectException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.Wall;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class WallBlock
/*     */   extends SQLObject
/*     */ {
/*     */   private BlockCoord coord;
/*     */   private Wall struct;
/*     */   int old_id;
/*     */   int old_data;
/*     */   int type_id;
/*     */   int data;
/*     */   public static final String TABLE_NAME = "WALLBLOCKS";
/*     */   
/*     */   public WallBlock(BlockCoord coord, Structure struct, int old_id, int old_data, int type, int data)
/*     */     throws SQLException
/*     */   {
/*  47 */     this.coord = coord;
/*  48 */     this.struct = ((Wall)struct);
/*  49 */     this.old_data = old_data;
/*  50 */     this.old_id = old_id;
/*  51 */     this.type_id = type;
/*  52 */     this.data = data;
/*     */   }
/*     */   
/*     */   public WallBlock(ResultSet rs) throws SQLException, InvalidNameException, InvalidObjectException, CivException {
/*  56 */     load(rs);
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord() {
/*  60 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(BlockCoord coord) {
/*  64 */     this.coord = coord;
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException
/*     */   {
/*  69 */     if (!SQL.hasTable("WALLBLOCKS")) {
/*  70 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "WALLBLOCKS" + " (" + 
/*  71 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  72 */         "`struct_id` int(11) NOT NULL DEFAULT 0," + 
/*  73 */         "`coord` mediumtext DEFAULT NULL," + 
/*  74 */         "`type_id` int(11) DEFAULT 0," + 
/*  75 */         "`data` int(11) DEFAULT 0," + 
/*  76 */         "`old_id` int(11) DEFAULT 0," + 
/*  77 */         "`old_data` int(11) DEFAULT 0," + 
/*  78 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  80 */       SQL.makeTable(table_create);
/*  81 */       CivLog.info("Created WALLBLOCKS table");
/*     */     } else {
/*  83 */       CivLog.info("WALLBLOCKS table OK!");
/*     */       
/*  85 */       if (!SQL.hasColumn("WALLBLOCKS", "type_id")) {
/*  86 */         CivLog.info("\tCouldn't find type_id column for wallblock.");
/*  87 */         SQL.addColumn("WALLBLOCKS", "`type_id` int(11) default 0");
/*     */       }
/*     */       
/*  90 */       if (!SQL.hasColumn("WALLBLOCKS", "data")) {
/*  91 */         CivLog.info("\tCouldn't find data column for wallblock.");
/*  92 */         SQL.addColumn("WALLBLOCKS", "`data` int(11) default 0");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, InvalidNameException, InvalidObjectException, CivException
/*     */   {
/* 100 */     setId(rs.getInt("id"));
/* 101 */     setStruct(CivGlobal.getStructureById(rs.getInt("struct_id")));
/* 102 */     if (this.struct == null) {
/* 103 */       int id = rs.getInt("struct_id");
/* 104 */       delete();
/* 105 */       throw new CivException("Could not load WallBlock, could not find structure:" + id);
/*     */     }
/*     */     
/* 108 */     setCoord(new BlockCoord(rs.getString("coord")));
/*     */     
/* 110 */     CivGlobal.addWallChunk(this.struct, new ChunkCoord(getCoord().getLocation()));
/* 111 */     this.struct.addStructureBlock(getCoord(), true);
/* 112 */     this.struct.wallBlocks.put(getCoord(), this);
/* 113 */     this.old_id = rs.getInt("old_id");
/* 114 */     this.old_data = rs.getInt("old_data");
/* 115 */     this.type_id = rs.getInt("type_id");
/* 116 */     this.data = rs.getInt("data");
/*     */   }
/*     */   
/*     */ 
/*     */   public void save()
/*     */   {
/* 122 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 127 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/* 129 */     hashmap.put("struct_id", Integer.valueOf(getStruct().getId()));
/* 130 */     hashmap.put("coord", getCoord().toString());
/* 131 */     hashmap.put("old_id", Integer.valueOf(this.old_id));
/* 132 */     hashmap.put("old_data", Integer.valueOf(this.old_data));
/* 133 */     hashmap.put("type_id", Integer.valueOf(this.type_id));
/* 134 */     hashmap.put("data", Integer.valueOf(this.data));
/*     */     
/* 136 */     SQL.updateNamedObject(this, hashmap, "WALLBLOCKS");
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 141 */     if (this.coord != null) {
/* 142 */       CivGlobal.removeStructureBlock(this.coord);
/*     */     }
/* 144 */     SQL.deleteNamedObject(this, "WALLBLOCKS");
/*     */   }
/*     */   
/*     */   public Structure getStruct() {
/* 148 */     return this.struct;
/*     */   }
/*     */   
/*     */   public void setStruct(Structure struct) {
/* 152 */     this.struct = ((Wall)struct);
/*     */   }
/*     */   
/*     */   public int getOldId() {
/* 156 */     return this.old_id;
/*     */   }
/*     */   
/*     */   public byte getOldData() {
/* 160 */     return (byte)this.old_data;
/*     */   }
/*     */   
/*     */   public int getTypeId() {
/* 164 */     return this.type_id;
/*     */   }
/*     */   
/*     */   public int getData() {
/* 168 */     return this.data;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\WallBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */