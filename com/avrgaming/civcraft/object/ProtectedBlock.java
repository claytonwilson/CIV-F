/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivLog;
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
/*     */ public class ProtectedBlock
/*     */   extends SQLObject
/*     */ {
/*     */   private BlockCoord coord;
/*     */   private Type type;
/*     */   public static final String TABLE_NAME = "PROTECTED_BLOCKS";
/*     */   
/*     */   public ProtectedBlock(BlockCoord coord, Type type)
/*     */   {
/*  38 */     this.coord = coord;
/*  39 */     this.type = type;
/*     */   }
/*     */   
/*     */ 
/*     */   public ProtectedBlock(ResultSet rs)
/*     */     throws SQLException, InvalidNameException
/*     */   {
/*  46 */     load(rs);
/*     */   }
/*     */   
/*     */   public static enum Type {
/*  50 */     NONE, 
/*  51 */     TRADE_MARKER, 
/*  52 */     PROTECTED_RAILWAY;
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException
/*     */   {
/*  57 */     if (!SQL.hasTable("PROTECTED_BLOCKS")) {
/*  58 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "PROTECTED_BLOCKS" + " (" + 
/*  59 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  60 */         "`coord` mediumtext NOT NULL," + 
/*  61 */         "`type` mediumtext NOT NULL," + 
/*  62 */         "`structure_id` int(11) DEFAULT 0," + 
/*  63 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  65 */       SQL.makeTable(table_create);
/*  66 */       CivLog.info("Created PROTECTED_BLOCKS table");
/*     */     } else {
/*  68 */       CivLog.info("PROTECTED_BLOCKS table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException, InvalidNameException
/*     */   {
/*  74 */     this.coord = new BlockCoord(rs.getString("coord"));
/*  75 */     this.type = Type.valueOf(rs.getString("type"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save()
/*     */   {
/*  86 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/*  91 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/*  93 */     hashmap.put("coord", this.coord.toString());
/*  94 */     hashmap.put("type", this.type.name());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */     SQL.updateNamedObject(this, hashmap, "PROTECTED_BLOCKS");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getType()
/*     */   {
/* 118 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(Type type) {
/* 122 */     this.type = type;
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord() {
/* 126 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(BlockCoord coord) {
/* 130 */     this.coord = coord;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\ProtectedBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */