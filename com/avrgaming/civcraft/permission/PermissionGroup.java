/*     */ package com.avrgaming.civcraft.permission;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.SQLObject;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class PermissionGroup
/*     */   extends SQLObject
/*     */ {
/*  40 */   private Map<String, Resident> members = new ConcurrentHashMap();
/*     */   
/*  42 */   private Town cacheTown = null;
/*     */   private int civId;
/*     */   private int townId;
/*     */   public static final String TABLE_NAME = "GROUPS";
/*     */   
/*     */   public PermissionGroup(Civilization civ, String name) throws InvalidNameException {
/*  48 */     this.civId = civ.getId();
/*  49 */     setName(name);
/*     */   }
/*     */   
/*     */   public PermissionGroup(Town town, String name) throws InvalidNameException {
/*  53 */     this.townId = town.getId();
/*  54 */     this.cacheTown = town;
/*  55 */     setName(name);
/*     */   }
/*     */   
/*     */   public PermissionGroup(ResultSet rs) throws SQLException, InvalidNameException {
/*  59 */     load(rs);
/*     */   }
/*     */   
/*     */   public void addMember(Resident res) {
/*  63 */     this.members.put(res.getName(), res);
/*     */   }
/*     */   
/*     */   public void removeMember(Resident res) {
/*  67 */     this.members.remove(res.getName());
/*     */   }
/*     */   
/*     */   public boolean hasMember(Resident res) {
/*  71 */     return this.members.containsKey(res.getName());
/*     */   }
/*     */   
/*     */   public boolean hasMember(String name) {
/*  75 */     return this.members.containsKey(name);
/*     */   }
/*     */   
/*     */   public void clearMembers() {
/*  79 */     this.members.clear();
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException
/*     */   {
/*  84 */     if (!SQL.hasTable("GROUPS")) {
/*  85 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "GROUPS" + " (" + 
/*  86 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  87 */         "`name` VARCHAR(64) NOT NULL," + 
/*  88 */         "`town_id` int(11)," + 
/*  89 */         "`civ_id` int(11)," + 
/*  90 */         "`members` mediumtext," + 
/*     */         
/*     */ 
/*  93 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  95 */       SQL.makeTable(table_create);
/*  96 */       CivLog.info("Created GROUPS table");
/*     */     } else {
/*  98 */       CivLog.info("GROUPS table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException, InvalidNameException
/*     */   {
/* 104 */     setId(rs.getInt("id"));
/* 105 */     setName(rs.getString("name"));
/* 106 */     setTownId(rs.getInt("town_id"));
/* 107 */     setCivId(rs.getInt("civ_id"));
/* 108 */     loadMembersFromSaveString(rs.getString("members"));
/*     */     
/* 110 */     if (getTownId() != 0) {
/* 111 */       this.cacheTown = CivGlobal.getTownFromId(getTownId());
/* 112 */       getTown().addGroup(this);
/*     */     } else {
/* 114 */       Civilization civ = CivGlobal.getCivFromId(getCivId());
/* 115 */       if (civ == null) {
/* 116 */         civ = CivGlobal.getConqueredCivFromId(getCivId());
/* 117 */         if (civ == null) {
/* 118 */           CivLog.warning("COUlD NOT FIND CIV ID:" + getCivId() + " for group: " + getName() + " to load.");
/* 119 */           return;
/*     */         }
/*     */       }
/*     */       
/* 123 */       civ.addGroup(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 129 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/* 134 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/* 136 */     hashmap.put("name", getName());
/* 137 */     hashmap.put("members", getMembersSaveString());
/* 138 */     hashmap.put("town_id", Integer.valueOf(getTownId()));
/* 139 */     hashmap.put("civ_id", Integer.valueOf(getCivId()));
/*     */     
/* 141 */     SQL.updateNamedObject(this, hashmap, "GROUPS");
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 146 */     SQL.deleteNamedObject(this, "GROUPS");
/*     */   }
/*     */   
/*     */   private String getMembersSaveString() {
/* 150 */     String ret = "";
/*     */     
/* 152 */     for (String name : this.members.keySet()) {
/* 153 */       ret = ret + name + ",";
/*     */     }
/*     */     
/* 156 */     return ret;
/*     */   }
/*     */   
/*     */   private void loadMembersFromSaveString(String src) {
/* 160 */     String[] names = src.split(",");
/*     */     String[] arrayOfString1;
/* 162 */     int j = (arrayOfString1 = names).length; for (int i = 0; i < j; i++) { String n = arrayOfString1[i];
/* 163 */       Resident res = CivGlobal.getResident(n);
/* 164 */       if (res != null) {
/* 165 */         this.members.put(n, res);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Town getTown() {
/* 171 */     return this.cacheTown;
/*     */   }
/*     */   
/*     */   public void setTown(Town town) {
/* 175 */     this.cacheTown = town;
/*     */   }
/*     */   
/*     */   public int getMemberCount() {
/* 179 */     return this.members.size();
/*     */   }
/*     */   
/*     */   public Collection<Resident> getMemberList() {
/* 183 */     return this.members.values();
/*     */   }
/*     */   
/*     */   public Civilization getCiv() {
/* 187 */     if (this.cacheTown == null) {
/* 188 */       return null;
/*     */     }
/*     */     
/* 191 */     return this.cacheTown.getCiv();
/*     */   }
/*     */   
/*     */   public boolean isProtectedGroup() {
/* 195 */     return (isTownProtectedGroup(getName())) || (isCivProtectedGroup(getName()));
/*     */   }
/*     */   
/*     */   public static boolean isProtectedGroupName(String name) {
/* 199 */     return (isTownProtectedGroup(name)) || (isCivProtectedGroup(name));
/*     */   }
/*     */   
/*     */   public boolean isTownProtectedGroup() {
/* 203 */     return isTownProtectedGroup(getName());
/*     */   }
/*     */   
/*     */   public boolean isCivProtectedGroup() {
/* 207 */     return isCivProtectedGroup(getName());
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private static boolean isTownProtectedGroup(String name)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 326	java/lang/String:toLowerCase	()Ljava/lang/String;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: invokevirtual 329	java/lang/String:hashCode	()I
/*     */     //   9: lookupswitch	default:+76->85, -1081093269:+35->44, 1375005013:+48->57, 2124045603:+61->70
/*     */     //   44: aload_1
/*     */     //   45: ldc_w 332
/*     */     //   48: invokevirtual 334	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   51: ifne +32 -> 83
/*     */     //   54: goto +31 -> 85
/*     */     //   57: aload_1
/*     */     //   58: ldc_w 337
/*     */     //   61: invokevirtual 334	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   64: ifne +19 -> 83
/*     */     //   67: goto +18 -> 85
/*     */     //   70: aload_1
/*     */     //   71: ldc_w 339
/*     */     //   74: invokevirtual 334	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   77: ifne +6 -> 83
/*     */     //   80: goto +5 -> 85
/*     */     //   83: iconst_1
/*     */     //   84: ireturn
/*     */     //   85: iconst_0
/*     */     //   86: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #211	-> byte code offset #0
/*     */     //   Java source line #215	-> byte code offset #83
/*     */     //   Java source line #217	-> byte code offset #85
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	87	0	name	String
/*     */     //   5	66	1	str	String
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private static boolean isCivProtectedGroup(String name)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 326	java/lang/String:toLowerCase	()Ljava/lang/String;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: invokevirtual 329	java/lang/String:hashCode	()I
/*     */     //   9: lookupswitch	default:+55->64, -711298263:+27->36, 50355338:+40->49
/*     */     //   36: aload_1
/*     */     //   37: ldc_w 341
/*     */     //   40: invokevirtual 334	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   43: ifne +19 -> 62
/*     */     //   46: goto +18 -> 64
/*     */     //   49: aload_1
/*     */     //   50: ldc_w 343
/*     */     //   53: invokevirtual 334	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   56: ifne +6 -> 62
/*     */     //   59: goto +5 -> 64
/*     */     //   62: iconst_1
/*     */     //   63: ireturn
/*     */     //   64: iconst_0
/*     */     //   65: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #221	-> byte code offset #0
/*     */     //   Java source line #224	-> byte code offset #62
/*     */     //   Java source line #226	-> byte code offset #64
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	66	0	name	String
/*     */     //   5	45	1	str	String
/*     */   }
/*     */   
/*     */   public String getMembersString()
/*     */   {
/* 230 */     String out = "";
/* 231 */     for (String name : this.members.keySet()) {
/* 232 */       out = out + name + ", ";
/*     */     }
/* 234 */     return out;
/*     */   }
/*     */   
/*     */   public int getCivId() {
/* 238 */     return this.civId;
/*     */   }
/*     */   
/*     */   public void setCivId(int civId) {
/* 242 */     this.civId = civId;
/*     */   }
/*     */   
/*     */   public int getTownId() {
/* 246 */     return this.townId;
/*     */   }
/*     */   
/*     */   public void setTownId(int townId) {
/* 250 */     this.townId = townId;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\permission\PermissionGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */