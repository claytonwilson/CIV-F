/*     */ package com.avrgaming.civcraft.randomevents;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.exception.InvalidObjectException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.SQLObject;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.randomevents.components.HammerRate;
/*     */ import com.avrgaming.civcraft.randomevents.components.Happiness;
/*     */ import com.avrgaming.civcraft.randomevents.components.Unhappiness;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.mysql.jdbc.StringUtils;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RandomEvent
/*     */   extends SQLObject
/*     */ {
/*     */   public ConfigRandomEvent configRandomEvent;
/*  36 */   public HashMap<String, RandomEventComponent> actions = new HashMap();
/*  37 */   public HashMap<String, RandomEventComponent> requirements = new HashMap();
/*  38 */   public HashMap<String, RandomEventComponent> success = new HashMap();
/*  39 */   public HashMap<String, RandomEventComponent> failure = new HashMap();
/*     */   
/*  41 */   private Town town = null;
/*  42 */   private Date startDate = null;
/*  43 */   private boolean active = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   public HashMap<String, String> componentVars = new HashMap();
/*  49 */   public LinkedList<String> savedMessages = new LinkedList();
/*     */   public static final String TABLE_NAME = "RANDOMEVENTS";
/*     */   
/*     */   public static void init() throws SQLException {
/*  53 */     if (!SQL.hasTable("RANDOMEVENTS")) {
/*  54 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "RANDOMEVENTS" + " (" + 
/*  55 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/*  56 */         "`config_id` mediumtext," + 
/*  57 */         "`town_id` int(11)," + 
/*  58 */         "`start_date` long NOT NULL," + 
/*  59 */         "`active` boolean DEFAULT false," + 
/*  60 */         "`component_vars` mediumtext," + 
/*  61 */         "`saved_messages` mediumtext," + 
/*  62 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/*  64 */       SQL.makeTable(table_create);
/*  65 */       CivLog.info("Created RANDOMEVENTS table");
/*     */     } else {
/*  67 */       SQL.makeCol("active", "boolean", "RANDOMEVENTS");
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs)
/*     */     throws SQLException, InvalidNameException, InvalidObjectException, CivException
/*     */   {
/*  74 */     setId(rs.getInt("id"));
/*  75 */     this.configRandomEvent = ((ConfigRandomEvent)CivSettings.randomEvents.get(rs.getString("config_id")));
/*  76 */     if (this.configRandomEvent == null)
/*     */     {
/*  78 */       delete();
/*  79 */       throw new CivException("Couldn't find random event config id:" + rs.getString("config_id"));
/*     */     }
/*     */     
/*  82 */     this.town = CivGlobal.getTownFromId(rs.getInt("town_id"));
/*  83 */     if (this.town == null) {
/*  84 */       delete();
/*  85 */       throw new CivException("Couldn't find town id:" + rs.getInt("town_id") + " while loading random event.");
/*     */     }
/*     */     
/*  88 */     this.startDate = new Date(rs.getLong("start_date"));
/*  89 */     this.active = rs.getBoolean("active");
/*     */     
/*  91 */     loadComponentVars(rs.getString("component_vars"));
/*  92 */     loadSavedMessages(rs.getString("saved_messages"));
/*     */     
/*     */ 
/*     */ 
/*  96 */     buildComponents();
/*  97 */     for (RandomEventComponent comp : this.actions.values()) {
/*  98 */       comp.onStart();
/*     */     }
/* 100 */     for (RandomEventComponent comp : this.requirements.values()) {
/* 101 */       comp.onStart();
/*     */     }
/* 103 */     for (RandomEventComponent comp : this.success.values()) {
/* 104 */       comp.onStart();
/*     */     }
/* 106 */     for (RandomEventComponent comp : this.failure.values()) {
/* 107 */       comp.onStart();
/*     */     }
/*     */     
/* 110 */     RandomEventSweeper.register(this);
/*     */   }
/*     */   
/*     */   private void loadComponentVars(String input) {
/* 114 */     if ((input == null) || (input.equals(""))) {
/* 115 */       return;
/*     */     }
/* 117 */     String[] keyValues = input.split(",");
/*     */     String[] arrayOfString1;
/* 119 */     int j = (arrayOfString1 = keyValues).length; for (int i = 0; i < j; i++) { String kvs = arrayOfString1[i];
/* 120 */       String[] split = kvs.split(":");
/* 121 */       String keyEncoded = split[0];
/* 122 */       String valueEncoded = split[1];
/*     */       
/* 124 */       String key = StringUtils.toAsciiString(Base64Coder.decode(keyEncoded));
/* 125 */       String value = StringUtils.toAsciiString(Base64Coder.decode(valueEncoded));
/*     */       
/* 127 */       this.componentVars.put(key, value);
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadSavedMessages(String input) {
/* 132 */     String[] messages = input.split(",");
/*     */     String[] arrayOfString1;
/* 134 */     int j = (arrayOfString1 = messages).length; for (int i = 0; i < j; i++) { String encodedMessage = arrayOfString1[i];
/* 135 */       String message = StringUtils.toAsciiString(Base64Coder.decode(encodedMessage));
/* 136 */       this.savedMessages.add(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 142 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void saveNow()
/*     */     throws SQLException
/*     */   {
/* 148 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/* 150 */     hashmap.put("config_id", this.configRandomEvent.id);
/* 151 */     hashmap.put("town_id", Integer.valueOf(getTown().getId()));
/* 152 */     hashmap.put("start_date", Long.valueOf(this.startDate.getTime()));
/* 153 */     hashmap.put("component_vars", getComponentVarsSaveString());
/* 154 */     hashmap.put("saved_messages", getSavedMessagesSaveString());
/* 155 */     hashmap.put("active", Boolean.valueOf(this.active));
/*     */     
/* 157 */     SQL.updateNamedObject(this, hashmap, "RANDOMEVENTS");
/*     */   }
/*     */   
/*     */   private String getComponentVarsSaveString() {
/* 161 */     String out = "";
/*     */     
/* 163 */     for (String key : this.componentVars.keySet()) {
/* 164 */       String value = (String)this.componentVars.get(key);
/*     */       
/* 166 */       String keyEncoded = new String(Base64Coder.encode(key.getBytes()));
/* 167 */       String valueEncoded = new String(Base64Coder.encode(value.getBytes()));
/*     */       
/* 169 */       out = out + keyEncoded + ":" + valueEncoded + ",";
/*     */     }
/*     */     
/*     */ 
/* 173 */     return out;
/*     */   }
/*     */   
/*     */   private String getSavedMessagesSaveString() {
/* 177 */     String out = "";
/*     */     
/* 179 */     for (String message : this.savedMessages)
/*     */     {
/* 181 */       String msgEncoded = new String(Base64Coder.encode(message.getBytes()));
/* 182 */       out = out + msgEncoded + ",";
/*     */     }
/*     */     
/* 185 */     return out;
/*     */   }
/*     */   
/*     */   public void delete()
/*     */     throws SQLException
/*     */   {
/* 191 */     SQL.deleteNamedObject(this, "RANDOMEVENTS");
/*     */   }
/*     */   
/*     */   public RandomEvent(ConfigRandomEvent config) {
/* 195 */     this.configRandomEvent = config;
/* 196 */     buildComponents();
/*     */   }
/*     */   
/*     */   public RandomEvent(ResultSet rs) throws SQLException, InvalidNameException, InvalidObjectException, CivException {
/* 200 */     load(rs);
/*     */     
/*     */ 
/* 203 */     this.town.setActiveEvent(this);
/*     */   }
/*     */   
/*     */   public void buildComponents() {
/* 207 */     buildComponents("com.avrgaming.civcraft.randomevents.components.", this.configRandomEvent.actions, this.actions);
/* 208 */     buildComponents("com.avrgaming.civcraft.randomevents.components.", this.configRandomEvent.requirements, this.requirements);
/* 209 */     buildComponents("com.avrgaming.civcraft.randomevents.components.", this.configRandomEvent.success, this.success);
/* 210 */     buildComponents("com.avrgaming.civcraft.randomevents.components.", this.configRandomEvent.failure, this.failure);
/*     */   }
/*     */   
/*     */   public void buildComponents(String classPath, List<HashMap<String, String>> compInfoList, HashMap<String, RandomEventComponent> components) {
/* 214 */     if (compInfoList != null) {
/* 215 */       for (HashMap<String, String> compInfo : compInfoList) {
/* 216 */         String className = classPath + (String)compInfo.get("name");
/*     */         
/*     */         try
/*     */         {
/* 220 */           Class<?> someClass = Class.forName(className);
/*     */           
/* 222 */           RandomEventComponent perkCompClass = (RandomEventComponent)someClass.newInstance();
/* 223 */           perkCompClass.setName((String)compInfo.get("name"));
/*     */           
/* 225 */           for (String key : compInfo.keySet()) {
/* 226 */             perkCompClass.setAttribute(key, (String)compInfo.get(key));
/*     */           }
/*     */           
/* 229 */           perkCompClass.createComponent(this);
/* 230 */           components.put(perkCompClass.getName(), perkCompClass);
/*     */         } catch (InstantiationException e) {
/* 232 */           e.printStackTrace();
/*     */         } catch (IllegalAccessException e) {
/* 234 */           e.printStackTrace();
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/* 237 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void start()
/*     */   {
/* 249 */     for (RandomEventComponent comp : this.actions.values()) {
/* 250 */       comp.onStart();
/*     */     }
/* 252 */     for (RandomEventComponent comp : this.requirements.values()) {
/* 253 */       comp.onStart();
/*     */     }
/* 255 */     for (RandomEventComponent comp : this.success.values()) {
/* 256 */       comp.onStart();
/*     */     }
/* 258 */     for (RandomEventComponent comp : this.failure.values()) {
/* 259 */       comp.onStart();
/*     */     }
/*     */     
/*     */ 
/* 263 */     boolean requireActivation = false;
/* 264 */     for (RandomEventComponent comp : this.actions.values()) {
/* 265 */       if (!comp.requiresActivation()) {
/* 266 */         comp.process();
/*     */       } else {
/* 268 */         requireActivation = true;
/* 269 */         CivMessage.sendTown(this.town, "§eThis event requires activation! use '/town event activate' to activate it.");
/*     */       }
/*     */     }
/*     */     
/* 273 */     if (!requireActivation) {
/* 274 */       this.active = true;
/*     */     }
/*     */     
/*     */ 
/* 278 */     RandomEventSweeper.register(this);
/*     */     
/*     */ 
/* 281 */     this.startDate = new Date();
/*     */     
/* 283 */     save();
/*     */   }
/*     */   
/*     */   public Town getTown() {
/* 287 */     return this.town;
/*     */   }
/*     */   
/*     */   public void setTown(Town town) {
/* 291 */     this.town = town;
/*     */   }
/*     */   
/*     */   public void cleanup()
/*     */   {
/* 296 */     for (RandomEventComponent comp : this.actions.values()) {
/* 297 */       comp.onCleanup();
/*     */     }
/* 299 */     for (RandomEventComponent comp : this.requirements.values()) {
/* 300 */       comp.onCleanup();
/*     */     }
/* 302 */     for (RandomEventComponent comp : this.success.values()) {
/* 303 */       comp.onCleanup();
/*     */     }
/* 305 */     for (RandomEventComponent comp : this.failure.values()) {
/* 306 */       comp.onCleanup();
/*     */     }
/*     */     
/* 309 */     this.town.setActiveEvent(null);
/*     */     try {
/* 311 */       delete();
/*     */     } catch (SQLException e) {
/* 313 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public Date getStartDate() {
/* 318 */     return this.startDate;
/*     */   }
/*     */   
/*     */   public void setStartDate(Date startDate) {
/* 322 */     this.startDate = startDate;
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 326 */     return this.configRandomEvent.length;
/*     */   }
/*     */   
/*     */   public void start(Town town) {
/* 330 */     this.town = town;
/*     */     
/*     */ 
/* 333 */     CivMessage.sendTownHeading(town, "Event: " + this.configRandomEvent.name);
/* 334 */     for (String str : this.configRandomEvent.message) {
/* 335 */       CivMessage.sendTown(town, str);
/* 336 */       this.savedMessages.add(str);
/*     */     }
/*     */     
/*     */ 
/* 340 */     town.setActiveEvent(this);
/* 341 */     start();
/*     */   }
/*     */   
/*     */ 
/*     */   public static double getUnhappiness(Town town)
/*     */   {
/* 347 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(Unhappiness.getKey(town));
/* 348 */     double unhappy = 0.0D;
/*     */     
/* 350 */     ArrayList<SessionEntry> removed = new ArrayList();
/* 351 */     for (SessionEntry entry : entries) {
/* 352 */       String[] split = entry.value.split(":");
/* 353 */       int unhappiness = Integer.valueOf(split[0]).intValue();
/* 354 */       int duration = Integer.valueOf(split[1]).intValue();
/*     */       
/*     */ 
/* 357 */       Date start = new Date(entry.time);
/* 358 */       Date now = new Date();
/*     */       
/* 360 */       if (now.getTime() > start.getTime() + duration * 3600000)
/*     */       {
/* 362 */         removed.add(entry);
/*     */       }
/*     */       else
/*     */       {
/* 366 */         unhappy += unhappiness;
/*     */       }
/*     */     }
/*     */     
/* 370 */     for (SessionEntry entry : removed) {
/* 371 */       CivGlobal.getSessionDB().delete(entry.request_id, entry.key);
/*     */     }
/*     */     
/* 374 */     return unhappy;
/*     */   }
/*     */   
/*     */   public static double getHappiness(Town town) {
/* 378 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(Happiness.getKey(town));
/* 379 */     double happy = 0.0D;
/*     */     
/* 381 */     ArrayList<SessionEntry> removed = new ArrayList();
/* 382 */     for (SessionEntry entry : entries) {
/* 383 */       String[] split = entry.value.split(":");
/* 384 */       int happiness = Integer.valueOf(split[0]).intValue();
/* 385 */       int duration = Integer.valueOf(split[1]).intValue();
/*     */       
/*     */ 
/* 388 */       Date start = new Date(entry.time);
/* 389 */       Date now = new Date();
/*     */       
/* 391 */       if (now.getTime() > start.getTime() + duration * 3600000)
/*     */       {
/* 393 */         removed.add(entry);
/*     */       }
/*     */       else
/*     */       {
/* 397 */         happy += happiness;
/*     */       }
/*     */     }
/*     */     
/* 401 */     for (SessionEntry entry : removed) {
/* 402 */       CivGlobal.getSessionDB().delete(entry.request_id, entry.key);
/*     */     }
/*     */     
/* 405 */     return happy;
/*     */   }
/*     */   
/*     */   public static double getHammerRate(Town town) {
/* 409 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(HammerRate.getKey(town));
/* 410 */     double hammerrate = 1.0D;
/*     */     
/* 412 */     ArrayList<SessionEntry> removed = new ArrayList();
/* 413 */     for (SessionEntry entry : entries) {
/* 414 */       String[] split = entry.value.split(":");
/* 415 */       double rate = Double.valueOf(split[0]).doubleValue();
/* 416 */       int duration = Integer.valueOf(split[1]).intValue();
/*     */       
/*     */ 
/* 419 */       Date start = new Date(entry.time);
/* 420 */       Date now = new Date();
/*     */       
/* 422 */       if (now.getTime() > start.getTime() + duration * 3600000)
/*     */       {
/* 424 */         removed.add(entry);
/*     */       }
/*     */       else
/*     */       {
/* 428 */         hammerrate *= rate;
/*     */       }
/*     */     }
/*     */     
/* 432 */     for (SessionEntry entry : removed) {
/* 433 */       CivGlobal.getSessionDB().delete(entry.request_id, entry.key);
/*     */     }
/*     */     
/* 436 */     return hammerrate;
/*     */   }
/*     */   
/*     */   public List<String> getMessages() {
/* 440 */     return this.savedMessages;
/*     */   }
/*     */   
/*     */   public Date getEndDate() {
/* 444 */     Date end = new Date(this.startDate.getTime() + this.configRandomEvent.length * 3600000);
/* 445 */     return end;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 449 */     return this.active;
/*     */   }
/*     */   
/*     */   public void setActive(boolean active) {
/* 453 */     this.active = active;
/*     */   }
/*     */   
/*     */   public void activate() throws CivException {
/* 457 */     if (this.active) {
/* 458 */       throw new CivException("Event is already active!");
/*     */     }
/*     */     
/* 461 */     this.active = true;
/*     */     
/* 463 */     for (RandomEventComponent comp : this.actions.values()) {
/* 464 */       comp.process();
/*     */     }
/*     */     
/* 467 */     save();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\randomevents\RandomEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */