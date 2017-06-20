/*     */ package com.avrgaming.civcraft.config;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
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
/*     */ public class ConfigMarketItem
/*     */ {
/*     */   public int id;
/*     */   public String name;
/*     */   public int type_id;
/*     */   public String custom_id;
/*     */   public int data;
/*     */   public int inital_value;
/*     */   private int buy_value;
/*     */   private int buy_bulk;
/*     */   private int sell_value;
/*     */   private int sell_bulk;
/*     */   private int bought;
/*     */   private int sold;
/*  58 */   private int buysell_count = 0;
/*  59 */   private boolean stackable = true;
/*     */   
/*     */   private int step;
/*  62 */   public static int BASE_ITEM_AMOUNT = 1;
/*  63 */   public static int STEP = 1;
/*  64 */   public static int STEP_COUNT = 256;
/*  65 */   public static double RATE = 0.15D;
/*     */   
/*     */   public static enum LastAction {
/*  68 */     NEUTRAL, 
/*  69 */     BUY, 
/*  70 */     SELL;
/*     */   }
/*     */   
/*  73 */   public LastAction lastaction = LastAction.NEUTRAL;
/*     */   public static final String TABLE_NAME = "MARKET_ITEMS";
/*     */   
/*  76 */   public static void loadConfig(FileConfiguration cfg, Map<Integer, ConfigMarketItem> items) { items.clear();
/*  77 */     List<Map<?, ?>> culture_levels = cfg.getMapList("items");
/*     */     try
/*     */     {
/*  80 */       STEP = CivSettings.getInteger(CivSettings.marketConfig, "step").intValue();
/*  81 */       STEP_COUNT = CivSettings.getInteger(CivSettings.marketConfig, "step_count").intValue();
/*  82 */       RATE = CivSettings.getDouble(CivSettings.marketConfig, "rate");
/*     */     }
/*     */     catch (InvalidConfiguration e) {
/*  85 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*  89 */     for (Map<?, ?> level : culture_levels)
/*     */     {
/*  91 */       ConfigMarketItem item = new ConfigMarketItem();
/*  92 */       item.id = ((Integer)level.get("id")).intValue();
/*  93 */       item.name = ((String)level.get("name"));
/*  94 */       item.type_id = ((Integer)level.get("type_id")).intValue();
/*  95 */       item.data = ((Integer)level.get("data")).intValue();
/*     */       
/*  97 */       item.inital_value = ((Integer)level.get("value")).intValue();
/*     */       
/*  99 */       if (level.get("custom_id") != null) {
/* 100 */         item.custom_id = ((String)level.get("custom_id"));
/*     */       } else {
/* 102 */         item.custom_id = null;
/*     */       }
/*     */       
/* 105 */       if (level.get("step") != null) {
/* 106 */         item.step = ((Integer)level.get("step")).intValue();
/*     */       } else {
/* 108 */         item.step = STEP;
/*     */       }
/*     */       
/* 111 */       Boolean stackable = (Boolean)level.get("stackable");
/* 112 */       if ((stackable != null) && (!stackable.booleanValue())) {
/* 113 */         item.stackable = stackable.booleanValue();
/*     */       }
/*     */       
/* 116 */       items.put(Integer.valueOf(item.id), item);
/*     */     }
/* 118 */     CivLog.info("Loaded " + items.size() + " market items.");
/*     */   }
/*     */   
/*     */   public static void init() throws SQLException
/*     */   {
/* 123 */     if (!SQL.hasTable("MARKET_ITEMS")) {
/* 124 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "MARKET_ITEMS" + " (" + 
/* 125 */         "`ident` VARCHAR(64) NOT NULL," + 
/* 126 */         "`buy_value` int(11) NOT NULL DEFAULT 105," + 
/* 127 */         "`buy_bulk` int(11) NOT NULL DEFAULT 1," + 
/* 128 */         "`sell_value` int(11) NOT NULL DEFAULT 95," + 
/* 129 */         "`sell_bulk` int(11) NOT NULL DEFAULT 1," + 
/* 130 */         "`buysell` int(11) NOT NULL DEFAULT 0," + 
/* 131 */         "`bought` int(11) NOT NULL DEFAULT 0," + 
/* 132 */         "`sold` int(11) NOT NULL DEFAULT 0," + 
/* 133 */         "`last_action` mediumtext, " + 
/* 134 */         "PRIMARY KEY (`ident`)" + ")";
/*     */       
/* 136 */       SQL.makeTable(table_create);
/* 137 */       CivLog.info("Created MARKET_ITEMS table");
/*     */     } else {
/* 139 */       CivLog.info("MARKET_ITEMS table OK!");
/*     */     }
/*     */     
/* 142 */     for (ConfigMarketItem item : CivSettings.marketItems.values()) {
/* 143 */       item.load();
/*     */     }
/*     */   }
/*     */   
/*     */   private String getIdent() {
/* 148 */     if (this.custom_id == null) {
/* 149 */       return this.type_id + ":" + this.data;
/*     */     }
/* 151 */     return this.custom_id;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void load()
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aconst_null
/*     */     //   3: astore_2
/*     */     //   4: aconst_null
/*     */     //   5: astore_3
/*     */     //   6: new 170	java/lang/StringBuilder
/*     */     //   9: dup
/*     */     //   10: ldc_w 278
/*     */     //   13: invokespecial 174	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   16: getstatic 224	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   19: invokevirtual 186	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   22: ldc 32
/*     */     //   24: invokevirtual 186	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   27: ldc_w 280
/*     */     //   30: invokevirtual 186	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   33: invokevirtual 189	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   36: astore 4
/*     */     //   38: invokestatic 282	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   41: astore_1
/*     */     //   42: aload_1
/*     */     //   43: aload 4
/*     */     //   45: invokeinterface 286 2 0
/*     */     //   50: astore_3
/*     */     //   51: aload_3
/*     */     //   52: iconst_1
/*     */     //   53: aload_0
/*     */     //   54: invokespecial 292	com/avrgaming/civcraft/config/ConfigMarketItem:getIdent	()Ljava/lang/String;
/*     */     //   57: invokeinterface 294 3 0
/*     */     //   62: aload_3
/*     */     //   63: invokeinterface 300 1 0
/*     */     //   68: astore_2
/*     */     //   69: aload_2
/*     */     //   70: invokeinterface 304 1 0
/*     */     //   75: ifeq +155 -> 230
/*     */     //   78: aload_0
/*     */     //   79: aload_2
/*     */     //   80: ldc_w 308
/*     */     //   83: invokeinterface 309 2 0
/*     */     //   88: invokestatic 159	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   91: invokevirtual 93	java/lang/Integer:intValue	()I
/*     */     //   94: putfield 313	com/avrgaming/civcraft/config/ConfigMarketItem:buy_value	I
/*     */     //   97: aload_0
/*     */     //   98: aload_2
/*     */     //   99: ldc_w 315
/*     */     //   102: invokeinterface 309 2 0
/*     */     //   107: invokestatic 159	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   110: invokevirtual 93	java/lang/Integer:intValue	()I
/*     */     //   113: putfield 316	com/avrgaming/civcraft/config/ConfigMarketItem:buy_bulk	I
/*     */     //   116: aload_0
/*     */     //   117: aload_2
/*     */     //   118: ldc_w 318
/*     */     //   121: invokeinterface 309 2 0
/*     */     //   126: invokestatic 159	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   129: invokevirtual 93	java/lang/Integer:intValue	()I
/*     */     //   132: putfield 319	com/avrgaming/civcraft/config/ConfigMarketItem:sell_value	I
/*     */     //   135: aload_0
/*     */     //   136: aload_2
/*     */     //   137: ldc_w 321
/*     */     //   140: invokeinterface 309 2 0
/*     */     //   145: invokestatic 159	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   148: invokevirtual 93	java/lang/Integer:intValue	()I
/*     */     //   151: putfield 322	com/avrgaming/civcraft/config/ConfigMarketItem:sell_bulk	I
/*     */     //   154: aload_0
/*     */     //   155: aload_2
/*     */     //   156: ldc_w 324
/*     */     //   159: invokeinterface 309 2 0
/*     */     //   164: invokestatic 159	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   167: invokevirtual 93	java/lang/Integer:intValue	()I
/*     */     //   170: putfield 325	com/avrgaming/civcraft/config/ConfigMarketItem:bought	I
/*     */     //   173: aload_0
/*     */     //   174: aload_2
/*     */     //   175: ldc_w 327
/*     */     //   178: invokeinterface 309 2 0
/*     */     //   183: invokestatic 159	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   186: invokevirtual 93	java/lang/Integer:intValue	()I
/*     */     //   189: putfield 328	com/avrgaming/civcraft/config/ConfigMarketItem:sold	I
/*     */     //   192: aload_0
/*     */     //   193: aload_2
/*     */     //   194: ldc_w 330
/*     */     //   197: invokeinterface 332 2 0
/*     */     //   202: invokestatic 336	com/avrgaming/civcraft/config/ConfigMarketItem$LastAction:valueOf	(Ljava/lang/String;)Lcom/avrgaming/civcraft/config/ConfigMarketItem$LastAction;
/*     */     //   205: putfield 61	com/avrgaming/civcraft/config/ConfigMarketItem:lastaction	Lcom/avrgaming/civcraft/config/ConfigMarketItem$LastAction;
/*     */     //   208: aload_0
/*     */     //   209: aload_2
/*     */     //   210: ldc_w 339
/*     */     //   213: invokeinterface 309 2 0
/*     */     //   218: invokestatic 159	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   221: invokevirtual 93	java/lang/Integer:intValue	()I
/*     */     //   224: putfield 52	com/avrgaming/civcraft/config/ConfigMarketItem:buysell_count	I
/*     */     //   227: goto +89 -> 316
/*     */     //   230: aload_0
/*     */     //   231: iconst_0
/*     */     //   232: putfield 325	com/avrgaming/civcraft/config/ConfigMarketItem:bought	I
/*     */     //   235: aload_0
/*     */     //   236: iconst_0
/*     */     //   237: putfield 328	com/avrgaming/civcraft/config/ConfigMarketItem:sold	I
/*     */     //   240: aload_0
/*     */     //   241: iconst_1
/*     */     //   242: putfield 316	com/avrgaming/civcraft/config/ConfigMarketItem:buy_bulk	I
/*     */     //   245: aload_0
/*     */     //   246: iconst_1
/*     */     //   247: putfield 322	com/avrgaming/civcraft/config/ConfigMarketItem:sell_bulk	I
/*     */     //   250: aload_0
/*     */     //   251: aload_0
/*     */     //   252: getfield 145	com/avrgaming/civcraft/config/ConfigMarketItem:inital_value	I
/*     */     //   255: aload_0
/*     */     //   256: getfield 145	com/avrgaming/civcraft/config/ConfigMarketItem:inital_value	I
/*     */     //   259: i2d
/*     */     //   260: getstatic 45	com/avrgaming/civcraft/config/ConfigMarketItem:RATE	D
/*     */     //   263: dmul
/*     */     //   264: d2i
/*     */     //   265: iadd
/*     */     //   266: putfield 313	com/avrgaming/civcraft/config/ConfigMarketItem:buy_value	I
/*     */     //   269: aload_0
/*     */     //   270: aload_0
/*     */     //   271: getfield 145	com/avrgaming/civcraft/config/ConfigMarketItem:inital_value	I
/*     */     //   274: putfield 319	com/avrgaming/civcraft/config/ConfigMarketItem:sell_value	I
/*     */     //   277: aload_0
/*     */     //   278: getfield 313	com/avrgaming/civcraft/config/ConfigMarketItem:buy_value	I
/*     */     //   281: aload_0
/*     */     //   282: getfield 319	com/avrgaming/civcraft/config/ConfigMarketItem:sell_value	I
/*     */     //   285: if_icmpne +13 -> 298
/*     */     //   288: aload_0
/*     */     //   289: dup
/*     */     //   290: getfield 313	com/avrgaming/civcraft/config/ConfigMarketItem:buy_value	I
/*     */     //   293: iconst_1
/*     */     //   294: iadd
/*     */     //   295: putfield 313	com/avrgaming/civcraft/config/ConfigMarketItem:buy_value	I
/*     */     //   298: aload_0
/*     */     //   299: invokevirtual 341	com/avrgaming/civcraft/config/ConfigMarketItem:saveItemNow	()V
/*     */     //   302: goto +14 -> 316
/*     */     //   305: astore 5
/*     */     //   307: aload_2
/*     */     //   308: aload_3
/*     */     //   309: aload_1
/*     */     //   310: invokestatic 344	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   313: aload 5
/*     */     //   315: athrow
/*     */     //   316: aload_2
/*     */     //   317: aload_3
/*     */     //   318: aload_1
/*     */     //   319: invokestatic 344	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   322: return
/*     */     // Line number table:
/*     */     //   Java source line #156	-> byte code offset #0
/*     */     //   Java source line #157	-> byte code offset #2
/*     */     //   Java source line #158	-> byte code offset #4
/*     */     //   Java source line #160	-> byte code offset #6
/*     */     //   Java source line #161	-> byte code offset #38
/*     */     //   Java source line #162	-> byte code offset #42
/*     */     //   Java source line #163	-> byte code offset #51
/*     */     //   Java source line #164	-> byte code offset #62
/*     */     //   Java source line #166	-> byte code offset #69
/*     */     //   Java source line #167	-> byte code offset #78
/*     */     //   Java source line #168	-> byte code offset #97
/*     */     //   Java source line #169	-> byte code offset #116
/*     */     //   Java source line #170	-> byte code offset #135
/*     */     //   Java source line #171	-> byte code offset #154
/*     */     //   Java source line #172	-> byte code offset #173
/*     */     //   Java source line #173	-> byte code offset #192
/*     */     //   Java source line #174	-> byte code offset #208
/*     */     //   Java source line #175	-> byte code offset #227
/*     */     //   Java source line #176	-> byte code offset #230
/*     */     //   Java source line #177	-> byte code offset #235
/*     */     //   Java source line #178	-> byte code offset #240
/*     */     //   Java source line #179	-> byte code offset #245
/*     */     //   Java source line #180	-> byte code offset #250
/*     */     //   Java source line #181	-> byte code offset #269
/*     */     //   Java source line #183	-> byte code offset #277
/*     */     //   Java source line #184	-> byte code offset #288
/*     */     //   Java source line #187	-> byte code offset #298
/*     */     //   Java source line #190	-> byte code offset #302
/*     */     //   Java source line #191	-> byte code offset #307
/*     */     //   Java source line #192	-> byte code offset #313
/*     */     //   Java source line #191	-> byte code offset #316
/*     */     //   Java source line #193	-> byte code offset #322
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	323	0	this	ConfigMarketItem
/*     */     //   1	318	1	context	Connection
/*     */     //   3	314	2	rs	java.sql.ResultSet
/*     */     //   5	313	3	ps	PreparedStatement
/*     */     //   36	8	4	query	String
/*     */     //   305	9	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	305	305	finally
/*     */   }
/*     */   
/*     */   public void saveItemNow()
/*     */     throws SQLException
/*     */   {
/* 196 */     Connection context = null;
/* 197 */     PreparedStatement ps = null;
/*     */     try
/*     */     {
/* 200 */       String query = "INSERT INTO `" + SQL.tb_prefix + "MARKET_ITEMS" + "` (`ident`, `buy_value`, `buy_bulk`, `sell_value`, `sell_bulk`, `bought`, `sold`, `last_action`, `buysell`) " + 
/* 201 */         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `buy_value`=?, `buy_bulk`=?, `sell_value`=?, `sell_bulk`=?, `bought`=?, `sold`=?, `last_action`=?, `buysell`=?";
/* 202 */       context = SQL.getGameConnection();
/* 203 */       ps = context.prepareStatement(query);
/*     */       
/* 205 */       ps.setString(1, getIdent());
/* 206 */       ps.setInt(2, this.buy_value);
/* 207 */       ps.setInt(3, this.buy_bulk);
/* 208 */       ps.setInt(4, this.sell_value);
/* 209 */       ps.setInt(5, this.sell_bulk);
/* 210 */       ps.setInt(6, this.bought);
/* 211 */       ps.setInt(7, this.sold);
/* 212 */       ps.setString(8, this.lastaction.toString());
/* 213 */       ps.setInt(9, this.buysell_count);
/* 214 */       ps.setInt(10, this.buy_value);
/* 215 */       ps.setInt(11, this.buy_bulk);
/* 216 */       ps.setInt(12, this.sell_value);
/* 217 */       ps.setInt(13, this.sell_bulk);
/* 218 */       ps.setInt(14, this.bought);
/* 219 */       ps.setInt(15, this.sold);
/* 220 */       ps.setString(16, this.lastaction.toString());
/* 221 */       ps.setInt(17, this.buysell_count);
/*     */       
/* 223 */       int rs = ps.executeUpdate();
/* 224 */       if (rs == 0) {
/* 225 */         throw new SQLException("Could not execute SQL code:" + query);
/*     */       }
/*     */     } finally {
/* 228 */       SQL.close(null, ps, context); } SQL.close(null, ps, context);
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
/*     */   public void save()
/*     */   {
/* 244 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/* 238 */           ConfigMarketItem.this.saveItemNow();
/*     */         } catch (SQLException e) {
/* 240 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public int getCoinsCostForAmount(int amount, int value, int dir)
/*     */   {
/* 248 */     int sum = 0;
/* 249 */     int current = value;
/* 250 */     int buysell = 0;
/*     */     
/* 252 */     for (int i = 0; i < amount; i++) {
/* 253 */       sum += current;
/* 254 */       buysell += dir;
/*     */       
/* 256 */       if (dir * buysell % (dir * STEP_COUNT) == 0) {
/* 257 */         current += dir * this.step;
/* 258 */         if (current < this.step) {
/* 259 */           current = this.step;
/*     */         }
/*     */       }
/*     */     }
/* 263 */     return sum;
/*     */   }
/*     */   
/*     */   public int getBuyCostForAmount(int amount) {
/* 267 */     int additional = getCoinsCostForAmount(amount, this.buy_value, 1);
/*     */     
/* 269 */     additional *= 2;
/* 270 */     return additional;
/*     */   }
/*     */   
/*     */   public int getSellCostForAmount(int amount) {
/* 274 */     int detremental = getCoinsCostForAmount(amount, this.sell_value, -1);
/* 275 */     return detremental;
/*     */   }
/*     */   
/*     */   public void buy(Resident resident, Player player, int amount) throws CivException
/*     */   {
/* 280 */     int total_items = 0;
/*     */     
/* 282 */     double coins = resident.getTreasury().getBalance();
/* 283 */     double cost = getBuyCostForAmount(amount);
/*     */     
/* 285 */     if (coins < cost) {
/* 286 */       throw new CivException("You do not have the required " + cost);
/*     */     }
/*     */     
/* 289 */     for (int i = 0; i < amount; i++) {
/* 290 */       coins -= this.buy_value;
/* 291 */       total_items += BASE_ITEM_AMOUNT;
/* 292 */       increment();
/*     */     }
/*     */     
/*     */ 
/* 296 */     resident.getTreasury().withdraw(cost);
/*     */     ItemStack newStack;
/*     */     ItemStack newStack;
/* 299 */     if (this.custom_id == null) {
/* 300 */       newStack = new ItemStack(this.type_id, amount, (short)this.data);
/*     */     } else {
/* 302 */       newStack = LoreMaterial.spawn((LoreMaterial)LoreMaterial.materialMap.get(this.custom_id));
/* 303 */       newStack.setAmount(amount);
/*     */     }
/*     */     
/* 306 */     HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack[] { newStack });
/* 307 */     for (ItemStack stack : leftovers.values()) {
/* 308 */       player.getWorld().dropItem(player.getLocation(), stack);
/*     */     }
/*     */     
/* 311 */     CivMessage.sendSuccess(player, "Bought " + total_items + " " + this.name + " for " + cost + " coins.");
/* 312 */     player.updateInventory();
/*     */   }
/*     */   
/*     */   public void sell(Resident resident, Player player, int amount) throws CivException
/*     */   {
/* 317 */     int total_coins = 0;
/* 318 */     int total_items = 0;
/*     */     
/* 320 */     MultiInventory inv = new MultiInventory();
/* 321 */     inv.addInventory(player.getInventory());
/*     */     
/* 323 */     if (!inv.contains(this.custom_id, this.type_id, (short)this.data, amount)) {
/* 324 */       throw new CivException("You do not have " + amount + " " + this.name + " to sell.");
/*     */     }
/*     */     
/* 327 */     for (int i = 0; i < amount; i++) {
/* 328 */       total_coins += this.sell_value;
/* 329 */       total_items += BASE_ITEM_AMOUNT;
/*     */       
/* 331 */       decrement();
/*     */     }
/*     */     
/* 334 */     if (!inv.removeItem(this.custom_id, this.type_id, (short)this.data, amount)) {
/* 335 */       throw new CivException("Sorry, you don't have enough " + this.name + " in your inventory.");
/*     */     }
/*     */     
/* 338 */     resident.getTreasury().deposit(total_coins);
/* 339 */     CivMessage.sendSuccess(player, "Sold " + total_items + " " + this.name + " for " + total_coins);
/* 340 */     player.updateInventory();
/*     */   }
/*     */   
/*     */   public void increment()
/*     */   {
/* 345 */     this.buysell_count += 1;
/* 346 */     if ((this.buysell_count % STEP_COUNT == 0) || (!this.stackable)) {
/* 347 */       this.sell_value += this.step;
/* 348 */       this.buy_value = (this.sell_value + (int)(this.sell_value * RATE));
/*     */       
/* 350 */       if (this.buy_value == this.sell_value) {
/* 351 */         this.buy_value += 1;
/*     */       }
/*     */       
/*     */ 
/* 355 */       this.buysell_count = 0;
/* 356 */       this.lastaction = LastAction.BUY;
/*     */     }
/* 358 */     save();
/*     */   }
/*     */   
/*     */   public void decrement() {
/* 362 */     this.buysell_count -= 1;
/*     */     
/* 364 */     if ((-this.buysell_count % -STEP_COUNT == 0) || (!this.stackable)) {
/* 365 */       this.sell_value -= this.step;
/* 366 */       this.buy_value = (this.sell_value + (int)(this.sell_value * RATE));
/*     */       
/* 368 */       if (this.buy_value == this.sell_value) {
/* 369 */         this.buy_value += 1;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 375 */       if (this.sell_value < this.step)
/*     */       {
/* 377 */         this.sell_value = this.step;
/* 378 */         this.buy_value = (this.step * 2);
/*     */       }
/* 380 */       this.lastaction = LastAction.SELL;
/* 381 */       this.buysell_count = 0;
/*     */     }
/* 383 */     save();
/*     */   }
/*     */   
/*     */   public boolean isStackable() {
/* 387 */     return this.stackable;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\config\ConfigMarketItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */