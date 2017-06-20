/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
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
/*     */ public class StructureSign
/*     */   extends SQLObject
/*     */ {
/*     */   private String text;
/*     */   private Buildable owner;
/*     */   private String type;
/*     */   private String action;
/*     */   private BlockCoord coord;
/*     */   private int direction;
/*  44 */   private boolean allowRightClick = false;
/*     */   
/*     */   public StructureSign(BlockCoord coord, Buildable owner) {
/*  47 */     this.coord = coord;
/*  48 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public StructureSign(ResultSet rs) throws SQLException {
/*  52 */     load(rs);
/*     */   }
/*     */   
/*  55 */   public static String TABLE_NAME = "STRUCTURE_SIGNS";
/*     */   
/*  57 */   public static void init() throws SQLException { if (!SQL.hasTable(TABLE_NAME)) {
/*  58 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/*  59 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  60 */         "`text` mediumtext, " + 
/*  61 */         "`structure_id` int(11), " + 
/*  62 */         "`wonder_id` int(11)," + 
/*  63 */         "`type` mediumtext, " + 
/*  64 */         "`action` mediumtext, " + 
/*  65 */         "`coordHash` mediumtext, " + 
/*  66 */         "`direction` int(11), " + 
/*  67 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  69 */       SQL.makeTable(table_create);
/*  70 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/*  72 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException
/*     */   {
/*  78 */     setId(rs.getInt("id"));
/*  79 */     this.text = rs.getString("text");
/*  80 */     this.action = rs.getString("action");
/*  81 */     this.type = rs.getString("type");
/*  82 */     int structure_id = rs.getInt("structure_id");
/*  83 */     int wonder_id = rs.getInt("wonder_id");
/*  84 */     this.owner = null;
/*     */     
/*  86 */     if (structure_id != 0) {
/*  87 */       this.owner = CivGlobal.getStructureById(structure_id);
/*  88 */     } else if (wonder_id != 0) {
/*  89 */       this.owner = CivGlobal.getWonderById(wonder_id);
/*     */     }
/*     */     
/*     */ 
/*  93 */     this.coord = new BlockCoord(rs.getString("coordHash"));
/*  94 */     this.direction = rs.getInt("direction");
/*     */     
/*  96 */     if (this.owner != null) {
/*  97 */       this.owner.addStructureSign(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 103 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 108 */     HashMap<String, Object> hashmap = new HashMap();
/* 109 */     hashmap.put("text", getText());
/*     */     
/* 111 */     if (this.owner == null) {
/* 112 */       hashmap.put("structure_id", Integer.valueOf(0));
/* 113 */       hashmap.put("wonder_id", Integer.valueOf(0));
/* 114 */     } else if ((this.owner instanceof Structure)) {
/* 115 */       hashmap.put("structure_id", Integer.valueOf(this.owner.getId()));
/* 116 */       hashmap.put("wonder_id", Integer.valueOf(0));
/* 117 */     } else if ((this.owner instanceof Wonder)) {
/* 118 */       hashmap.put("structure_id", Integer.valueOf(0));
/* 119 */       hashmap.put("wonder_id", Integer.valueOf(this.owner.getId()));
/*     */     }
/*     */     
/* 122 */     hashmap.put("type", getType());
/* 123 */     hashmap.put("action", getAction());
/* 124 */     hashmap.put("coordHash", this.coord.toString());
/* 125 */     hashmap.put("direction", Integer.valueOf(this.direction));
/*     */     
/* 127 */     SQL.updateNamedObject(this, hashmap, TABLE_NAME);
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 132 */     SQL.deleteNamedObject(this, TABLE_NAME);
/* 133 */     CivGlobal.removeStructureSign(this);
/*     */   }
/*     */   
/*     */   public String getText() {
/* 137 */     return this.text;
/*     */   }
/*     */   
/*     */   public void setText(String text) {
/* 141 */     this.text = text;
/*     */   }
/*     */   
/*     */   public Buildable getOwner() {
/* 145 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(Buildable owner) {
/* 149 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public String getType() {
/* 153 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 157 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getAction() {
/* 161 */     return this.action;
/*     */   }
/*     */   
/*     */   public void setAction(String action) {
/* 165 */     this.action = action;
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord() {
/* 169 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(BlockCoord coord) {
/* 173 */     this.coord = coord;
/*     */   }
/*     */   
/*     */   public int getDirection() {
/* 177 */     return this.direction;
/*     */   }
/*     */   
/*     */   public void setDirection(int direction) {
/* 181 */     this.direction = direction;
/*     */   }
/*     */   
/*     */   public void setText(String[] message) {
/* 185 */     this.text = "";
/* 186 */     String[] arrayOfString; int j = (arrayOfString = message).length; for (int i = 0; i < j; i++) { String str = arrayOfString[i];
/* 187 */       this.text = (this.text + str + "\n");
/*     */     }
/*     */   }
/*     */   
/*     */   public void update() {
/* 192 */     if ((this.coord.getBlock().getState() instanceof Sign)) {
/* 193 */       Sign sign = (Sign)this.coord.getBlock().getState();
/* 194 */       String[] lines = this.text.split("\\n");
/*     */       
/* 196 */       for (int i = 0; i < 4; i++) {
/* 197 */         if (i < lines.length) {
/* 198 */           sign.setLine(i, lines[i]);
/*     */         } else {
/* 200 */           sign.setLine(i, "");
/*     */         }
/*     */       }
/* 203 */       sign.update();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAllowRightClick() {
/* 208 */     return this.allowRightClick;
/*     */   }
/*     */   
/*     */   public void setAllowRightClick(boolean allowRightClick) {
/* 212 */     this.allowRightClick = allowRightClick;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\StructureSign.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */