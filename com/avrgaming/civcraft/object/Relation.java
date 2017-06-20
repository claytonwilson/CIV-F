/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
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
/*     */ public class Relation
/*     */   extends SQLObject
/*     */ {
/*     */   private Civilization civ;
/*     */   private Civilization other_civ;
/*  38 */   private Civilization aggressor_civ = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Status
/*     */   {
/*  44 */     NEUTRAL, 
/*  45 */     HOSTILE, 
/*  46 */     WAR, 
/*  47 */     PEACE, 
/*  48 */     ALLY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  53 */   private Status relation = Status.NEUTRAL;
/*  54 */   private Date created = null;
/*  55 */   private Date expires = null;
/*     */   public static final String TABLE_NAME = "RELATIONS";
/*     */   
/*     */   public Relation(Civilization civ, Civilization otherCiv, Status status, Date expires)
/*     */   {
/*  60 */     this.civ = civ;
/*  61 */     this.other_civ = otherCiv;
/*  62 */     this.relation = status;
/*  63 */     this.created = new Date();
/*  64 */     this.expires = expires;
/*     */     
/*  66 */     save();
/*     */   }
/*     */   
/*     */   public Relation(ResultSet rs) throws SQLException, InvalidNameException {
/*  70 */     load(rs);
/*  71 */     if (this.civ != null) {
/*  72 */       this.civ.getDiplomacyManager().addRelation(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException {
/*  77 */     if (!SQL.hasTable("RELATIONS")) {
/*  78 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "RELATIONS" + " (" + 
/*  79 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  80 */         "`civ_id` int(11) NOT NULL DEFAULT 0," + 
/*  81 */         "`other_civ_id` int(11) NOT NULL DEFAULT 0," + 
/*  82 */         "`relation` mediumtext DEFAULT NULL," + 
/*  83 */         "`aggressor_civ_id` int(11) NOT NULL DEFAULT 0," + 
/*  84 */         "`created` long," + 
/*  85 */         "`expires` long," + 
/*  86 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  88 */       SQL.makeTable(table_create);
/*  89 */       CivLog.info("Created RELATIONS table");
/*     */     } else {
/*  91 */       CivLog.info("RELATIONS table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException, InvalidNameException
/*     */   {
/*  97 */     setId(rs.getInt("id"));
/*  98 */     this.civ = CivGlobal.getCivFromId(rs.getInt("civ_id"));
/*  99 */     if (this.civ == null) {
/* 100 */       CivLog.warning("Couldn't find civ id:" + rs.getInt("civ_id") + " deleting this relation.");
/* 101 */       delete();
/* 102 */       return;
/*     */     }
/*     */     
/* 105 */     this.other_civ = CivGlobal.getCivFromId(rs.getInt("other_civ_id"));
/* 106 */     if (this.other_civ == null) {
/* 107 */       CivLog.warning("Couldn't find other civ id:" + rs.getInt("other_civ_id") + " deleting this relation.");
/* 108 */       this.civ = null;
/* 109 */       delete();
/* 110 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 114 */       this.relation = Status.valueOf(rs.getString("relation"));
/*     */     } catch (IllegalArgumentException e) {
/* 116 */       this.relation = Status.WAR;
/*     */     }
/*     */     
/* 119 */     int aggressor_id = rs.getInt("aggressor_civ_id");
/* 120 */     if (aggressor_id != 0) {
/* 121 */       setAggressor(CivGlobal.getCivFromId(aggressor_id));
/*     */     }
/*     */     
/*     */ 
/* 125 */     Long createdLong = Long.valueOf(rs.getLong("created"));
/* 126 */     Long expiresLong = Long.valueOf(rs.getLong("expires"));
/*     */     
/* 128 */     if ((createdLong != null) && (createdLong.longValue() != 0L)) {
/* 129 */       this.created = new Date(createdLong.longValue());
/*     */     }
/*     */     
/* 132 */     if ((expiresLong != null) && (expiresLong.longValue() != 0L)) {
/* 133 */       this.expires = new Date(expiresLong.longValue());
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
/*     */     
/* 146 */     hashmap.put("civ_id", Integer.valueOf(this.civ.getId()));
/* 147 */     hashmap.put("other_civ_id", Integer.valueOf(this.other_civ.getId()));
/* 148 */     hashmap.put("relation", this.relation.name());
/* 149 */     if (this.aggressor_civ != null) {
/* 150 */       hashmap.put("aggressor_civ_id", Integer.valueOf(this.aggressor_civ.getId()));
/*     */     }
/*     */     
/* 153 */     if (this.created != null) {
/* 154 */       hashmap.put("created", Long.valueOf(this.created.getTime()));
/*     */     } else {
/* 156 */       hashmap.put("created", Integer.valueOf(0));
/*     */     }
/*     */     
/* 159 */     if (this.expires != null) {
/* 160 */       hashmap.put("expires", Long.valueOf(this.expires.getTime()));
/*     */     } else {
/* 162 */       hashmap.put("expires", Integer.valueOf(0));
/*     */     }
/*     */     
/* 165 */     SQL.updateNamedObject(this, hashmap, "RELATIONS");
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 170 */     SQL.deleteNamedObject(this, "RELATIONS");
/*     */   }
/*     */   
/*     */   public Status getStatus() {
/* 174 */     return this.relation;
/*     */   }
/*     */   
/*     */   public Civilization getOtherCiv() {
/* 178 */     return this.other_civ;
/*     */   }
/*     */   
/*     */   public void setStatus(Status status) {
/* 182 */     this.relation = status;
/* 183 */     save();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 188 */     String color = "§f";
/* 189 */     String out = "";
/*     */     
/* 191 */     out = this.relation.name() + "§f" + " with " + this.other_civ.getName();
/* 192 */     switch (this.relation) {
/*     */     case ALLY: 
/*     */       break;
/*     */     case HOSTILE: 
/* 196 */       color = "§e";
/* 197 */       break;
/*     */     case NEUTRAL: 
/* 199 */       color = "§c";
/* 200 */       break;
/*     */     case PEACE: 
/* 202 */       color = "§b";
/* 203 */       break;
/*     */     case WAR: 
/* 205 */       color = "§2";
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
/* 217 */     String expireString = "";
/* 218 */     if (this.expires != null) {
/* 219 */       SimpleDateFormat sdf = new SimpleDateFormat("M/d/y k:m:s z");
/* 220 */       expireString = "§7 (Expires " + sdf.format(this.expires) + ")";
/*     */     }
/*     */     
/* 223 */     return color + out + expireString;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String getRelationColor(Status status)
/*     */   {
/* 229 */     switch (status) {
/*     */     case ALLY: 
/* 231 */       return "§f";
/*     */     case HOSTILE: 
/* 233 */       return "§e";
/*     */     case NEUTRAL: 
/* 235 */       return "§c";
/*     */     case PEACE: 
/* 237 */       return "§b";
/*     */     case WAR: 
/* 239 */       return "§2";
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/* 245 */     return "§f";
/*     */   }
/*     */   
/*     */   public Date getExpireDate()
/*     */   {
/* 250 */     return this.expires;
/*     */   }
/*     */   
/*     */   public void setExpires(Date expires2) {
/* 254 */     this.expires = expires2;
/*     */   }
/*     */   
/*     */   public Civilization getCiv() {
/* 258 */     return this.civ;
/*     */   }
/*     */   
/*     */   public Civilization getAggressor() {
/* 262 */     return this.aggressor_civ;
/*     */   }
/*     */   
/*     */   public void setAggressor(Civilization aggressor_civ) {
/* 266 */     this.aggressor_civ = aggressor_civ;
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
/*     */   public String getPairKey()
/*     */   {
/* 281 */     String key = "";
/*     */     
/* 283 */     if (getCiv().getId() < getOtherCiv().getId()) {
/* 284 */       key = key + getCiv().getId() + ":" + getOtherCiv().getId();
/*     */     } else {
/* 286 */       key = key + getOtherCiv().getId() + ":" + getCiv().getId();
/*     */     }
/*     */     
/* 289 */     return key;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\Relation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */