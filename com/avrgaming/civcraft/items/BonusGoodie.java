/*     */ package com.avrgaming.civcraft.items;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuff;
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.database.SQLUpdate;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidNameException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.TradeOutpost;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.EntityUtil;
/*     */ import com.avrgaming.civcraft.util.InventoryHolderStorage;
/*     */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.DoubleChest;
/*     */ import org.bukkit.entity.Item;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.DoubleChestInventory;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
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
/*     */ public class BonusGoodie
/*     */   extends LoreItem
/*     */ {
/*     */   public static final String LORE_TYPE = "Bonus Goodie";
/*     */   private InventoryHolderStorage holderStore;
/*     */   private Item item;
/*     */   private ItemFrameStorage frameStore;
/*     */   private TradeOutpost outpost;
/*     */   private ConfigTradeGood config;
/*     */   public static final String TABLE_NAME = "GOODIE_ITEMS";
/*     */   
/*     */   public static enum LoreIndex
/*     */   {
/*  70 */     TYPE, 
/*  71 */     OUTPOSTLOCATION;
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
/*     */   public static void init()
/*     */     throws SQLException
/*     */   {
/*  98 */     if (!SQL.hasTable("GOODIE_ITEMS")) {
/*  99 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + "GOODIE_ITEMS" + " (" + 
/* 100 */         "`id` int(11) unsigned NOT NULL auto_increment," + 
/* 101 */         "`holder_location` mediumtext DEFAULT NULL," + 
/* 102 */         "`player_name` mediumtext DEFAULT NULL," + 
/* 103 */         "`frame_location` mediumtext DEFAULT NULL," + 
/* 104 */         "`frame_uid` mediumtext DEFAULT NULL," + 
/* 105 */         "`item_uid` mediumtext DEFAULT NULL," + 
/* 106 */         "`outpost_location` mediumtext DEFAULT NULL," + 
/* 107 */         "PRIMARY KEY (`id`)" + ")";
/*     */       
/* 109 */       SQL.makeTable(table_create);
/* 110 */       CivLog.info("Created GOODIE_ITEMS table");
/*     */     } else {
/* 112 */       CivLog.info("GOODIE_ITEMS table OK!");
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public BonusGoodie(TradeOutpost outpost)
/*     */     throws SQLException, InvalidNameException, CivException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokespecial 95	com/avrgaming/civcraft/items/LoreItem:<init>	()V
/*     */     //   4: aload_0
/*     */     //   5: aconst_null
/*     */     //   6: putfield 97	com/avrgaming/civcraft/items/BonusGoodie:holderStore	Lcom/avrgaming/civcraft/util/InventoryHolderStorage;
/*     */     //   9: aload_0
/*     */     //   10: aconst_null
/*     */     //   11: putfield 99	com/avrgaming/civcraft/items/BonusGoodie:item	Lorg/bukkit/entity/Item;
/*     */     //   14: aload_0
/*     */     //   15: aconst_null
/*     */     //   16: putfield 101	com/avrgaming/civcraft/items/BonusGoodie:frameStore	Lcom/avrgaming/civcraft/util/ItemFrameStorage;
/*     */     //   19: aload_0
/*     */     //   20: aconst_null
/*     */     //   21: putfield 103	com/avrgaming/civcraft/items/BonusGoodie:outpost	Lcom/avrgaming/civcraft/structure/TradeOutpost;
/*     */     //   24: aconst_null
/*     */     //   25: astore_2
/*     */     //   26: aconst_null
/*     */     //   27: astore_3
/*     */     //   28: aconst_null
/*     */     //   29: astore 4
/*     */     //   31: aload_1
/*     */     //   32: invokevirtual 105	com/avrgaming/civcraft/structure/TradeOutpost:getCorner	()Lcom/avrgaming/civcraft/util/BlockCoord;
/*     */     //   35: invokevirtual 111	com/avrgaming/civcraft/util/BlockCoord:toString	()Ljava/lang/String;
/*     */     //   38: astore 5
/*     */     //   40: aload_0
/*     */     //   41: aload_1
/*     */     //   42: invokevirtual 114	com/avrgaming/civcraft/structure/TradeOutpost:getGood	()Lcom/avrgaming/civcraft/object/TradeGood;
/*     */     //   45: invokevirtual 118	com/avrgaming/civcraft/object/TradeGood:getInfo	()Lcom/avrgaming/civcraft/config/ConfigTradeGood;
/*     */     //   48: putfield 124	com/avrgaming/civcraft/items/BonusGoodie:config	Lcom/avrgaming/civcraft/config/ConfigTradeGood;
/*     */     //   51: invokestatic 126	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   54: astore_2
/*     */     //   55: aload_2
/*     */     //   56: new 35	java/lang/StringBuilder
/*     */     //   59: dup
/*     */     //   60: ldc -126
/*     */     //   62: invokespecial 39	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   65: getstatic 43	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   68: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   71: ldc 21
/*     */     //   73: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   76: ldc -124
/*     */     //   78: invokevirtual 46	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   81: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   84: invokeinterface 134 2 0
/*     */     //   89: astore 4
/*     */     //   91: aload 4
/*     */     //   93: iconst_1
/*     */     //   94: aload 5
/*     */     //   96: invokeinterface 140 3 0
/*     */     //   101: aload 4
/*     */     //   103: invokeinterface 146 1 0
/*     */     //   108: astore_3
/*     */     //   109: aload_3
/*     */     //   110: invokeinterface 150 1 0
/*     */     //   115: ifne +15 -> 130
/*     */     //   118: aload_0
/*     */     //   119: aload_1
/*     */     //   120: putfield 103	com/avrgaming/civcraft/items/BonusGoodie:outpost	Lcom/avrgaming/civcraft/structure/TradeOutpost;
/*     */     //   123: aload_0
/*     */     //   124: invokespecial 156	com/avrgaming/civcraft/items/BonusGoodie:createGoodieAtOutpost	()V
/*     */     //   127: goto +23 -> 150
/*     */     //   130: aload_0
/*     */     //   131: aload_3
/*     */     //   132: invokevirtual 159	com/avrgaming/civcraft/items/BonusGoodie:load	(Ljava/sql/ResultSet;)V
/*     */     //   135: goto +15 -> 150
/*     */     //   138: astore 6
/*     */     //   140: aload_3
/*     */     //   141: aload 4
/*     */     //   143: aload_2
/*     */     //   144: invokestatic 163	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   147: aload 6
/*     */     //   149: athrow
/*     */     //   150: aload_3
/*     */     //   151: aload 4
/*     */     //   153: aload_2
/*     */     //   154: invokestatic 163	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   157: return
/*     */     // Line number table:
/*     */     //   Java source line #122	-> byte code offset #0
/*     */     //   Java source line #77	-> byte code offset #4
/*     */     //   Java source line #85	-> byte code offset #9
/*     */     //   Java source line #88	-> byte code offset #14
/*     */     //   Java source line #91	-> byte code offset #19
/*     */     //   Java source line #123	-> byte code offset #24
/*     */     //   Java source line #124	-> byte code offset #26
/*     */     //   Java source line #125	-> byte code offset #28
/*     */     //   Java source line #128	-> byte code offset #31
/*     */     //   Java source line #129	-> byte code offset #40
/*     */     //   Java source line #131	-> byte code offset #51
/*     */     //   Java source line #132	-> byte code offset #55
/*     */     //   Java source line #133	-> byte code offset #91
/*     */     //   Java source line #134	-> byte code offset #101
/*     */     //   Java source line #136	-> byte code offset #109
/*     */     //   Java source line #138	-> byte code offset #118
/*     */     //   Java source line #139	-> byte code offset #123
/*     */     //   Java source line #141	-> byte code offset #127
/*     */     //   Java source line #142	-> byte code offset #130
/*     */     //   Java source line #145	-> byte code offset #135
/*     */     //   Java source line #146	-> byte code offset #140
/*     */     //   Java source line #147	-> byte code offset #147
/*     */     //   Java source line #146	-> byte code offset #150
/*     */     //   Java source line #148	-> byte code offset #157
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	158	0	this	BonusGoodie
/*     */     //   0	158	1	outpost	TradeOutpost
/*     */     //   25	129	2	context	java.sql.Connection
/*     */     //   27	124	3	rs	ResultSet
/*     */     //   29	123	4	ps	java.sql.PreparedStatement
/*     */     //   38	57	5	outpost_location	String
/*     */     //   138	10	6	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   31	138	138	finally
/*     */   }
/*     */   
/*     */   private void createGoodieAtOutpost()
/*     */     throws CivException
/*     */   {
/* 151 */     ItemFrameStorage outpostFrame = this.outpost.getItemFrameStore();
/* 152 */     if (outpostFrame == null) {
/* 153 */       throw new CivException("Couldn't find an item frame to construct outpost with.");
/*     */     }
/*     */     
/* 156 */     ItemStack stack = ItemManager.createItemStack(this.config.material, 1, (short)this.config.material_data);
/*     */     
/* 158 */     updateLore(stack);
/*     */     
/* 160 */     outpostFrame.setItem(stack);
/* 161 */     setFrame(outpostFrame);
/*     */     
/* 163 */     CivGlobal.addBonusGoodie(this);
/* 164 */     update(false);
/*     */   }
/*     */   
/*     */   public void updateLore(ItemStack stack)
/*     */   {
/* 169 */     TradeGood good = this.outpost.getGood();
/*     */     
/* 171 */     ArrayList<String> lore = new ArrayList();
/* 172 */     lore.add("Bonus Goodie");
/* 173 */     lore.add(this.outpost.getCorner().toString());
/*     */     
/* 175 */     String[] split = getBonusDisplayString().split(";");
/* 176 */     String[] arrayOfString1; int j = (arrayOfString1 = split).length; for (int i = 0; i < j; i++) { String str = arrayOfString1[i];
/* 177 */       lore.add("§e" + str);
/*     */     }
/*     */     
/* 180 */     if (isStackable()) {
/* 181 */       lore.add("§bStackable");
/*     */     }
/*     */     
/* 184 */     setLore(stack, lore);
/* 185 */     setDisplayName(stack, good.getInfo().name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ItemStack findStack()
/*     */   {
/* 195 */     if (this.holderStore != null)
/*     */     {
/*     */       try {
/* 198 */         holder = this.holderStore.getHolder();
/*     */       } catch (CivException e) { InventoryHolder holder;
/* 200 */         e.printStackTrace();
/* 201 */         return null; }
/*     */       InventoryHolder holder;
/* 203 */       Iterator localIterator2; for (Iterator localIterator1 = CivSettings.goods.values().iterator(); localIterator1.hasNext(); 
/* 204 */           localIterator2.hasNext())
/*     */       {
/* 203 */         ConfigTradeGood good = (ConfigTradeGood)localIterator1.next();
/* 204 */         localIterator2 = holder.getInventory().all(ItemManager.getMaterial(good.material)).entrySet().iterator(); continue;Map.Entry<Integer, ? extends ItemStack> itemEntry = (Map.Entry)localIterator2.next();
/* 205 */         if (ItemManager.getData((ItemStack)itemEntry.getValue()) == good.material_data)
/*     */         {
/*     */ 
/* 208 */           ItemStack stack = (ItemStack)itemEntry.getValue();
/*     */           
/* 210 */           if (isItemStackOurs(stack))
/*     */           {
/* 212 */             return stack;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 219 */     if (this.frameStore != null) {
/*     */       try {
/* 221 */         if ((this.frameStore.isEmpty()) || (!isItemStackOurs(this.frameStore.getItem()))) {
/* 222 */           CivLog.warning("Found frame, but item was wrong, trying to recover by spawning item.");
/*     */           
/* 224 */           ItemStack stack = ItemManager.createItemStack(this.config.material, 1, (short)this.config.material_data);
/* 225 */           updateLore(stack);
/*     */           
/* 227 */           this.frameStore.setItem(stack);
/* 228 */           return stack;
/*     */         }
/*     */       } catch (CivException e) {
/* 231 */         e.printStackTrace();
/*     */         try {
/* 233 */           deleteAndReset();
/*     */         } catch (SQLException e1) {
/* 235 */           e1.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/* 239 */       return this.frameStore.getItem();
/*     */     }
/*     */     
/* 242 */     if (this.item != null) {
/* 243 */       return this.item.getItemStack();
/*     */     }
/* 245 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void replenish(ItemStack itemStack, Item itemEntity, Inventory inventory, ItemFrameStorage frame)
/*     */   {
/* 255 */     if (((itemEntity == null) && (inventory == null) && (frame == null)) || (itemStack == null)) {
/*     */       try {
/* 257 */         deleteAndReset();
/*     */       } catch (SQLException e) {
/* 259 */         e.printStackTrace();
/*     */       }
/* 261 */       return;
/*     */     }
/*     */     
/*     */ 
/* 265 */     ItemFrameStorage frameStore = this.outpost.getItemFrameStore();
/* 266 */     if (frameStore != null) {
/* 267 */       frameStore.setItem(new ItemStack(itemStack));
/* 268 */       setFrame(frameStore);
/*     */     } else {
/* 270 */       CivLog.warning("Couldn't replenish good, item frame was missing.");
/* 271 */       return;
/*     */     }
/*     */     
/*     */ 
/* 275 */     if (inventory != null) {
/* 276 */       if ((inventory instanceof DoubleChestInventory)) {
/* 277 */         DoubleChestInventory dv = (DoubleChestInventory)inventory;
/* 278 */         dv.remove(itemStack);
/*     */       } else {
/* 280 */         inventory.remove(itemStack);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 285 */     if ((frame != null) && (frame.getUUID() != frameStore.getUUID())) {
/* 286 */       frame.clearItem();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 291 */     if (itemEntity != null) {
/* 292 */       itemEntity.remove();
/*     */     }
/*     */     try
/*     */     {
/* 296 */       update(false);
/* 297 */       updateLore(itemStack);
/*     */     } catch (CivException e) {
/* 299 */       e.printStackTrace();
/*     */     }
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
/*     */   public void replenish()
/*     */   {
/* 329 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 313 */         if (BonusGoodie.this.holderStore != null) {
/*     */           try {
/* 315 */             BonusGoodie.this.replenish(BonusGoodie.this.findStack(), null, BonusGoodie.this.holderStore.getHolder().getInventory(), null);
/*     */           } catch (CivException e) {
/* 317 */             e.printStackTrace();
/*     */           }
/*     */           
/* 320 */         } else if (BonusGoodie.this.frameStore != null) {
/* 321 */           BonusGoodie.this.replenish(BonusGoodie.this.findStack(), null, null, BonusGoodie.this.frameStore);
/*     */         }
/*     */         else
/*     */         {
/* 325 */           BonusGoodie.this.replenish(BonusGoodie.this.findStack(), BonusGoodie.this.item, null, null);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(boolean sync)
/*     */     throws CivException
/*     */   {
/* 338 */     HashMap<String, Object> hashmap = new HashMap();
/*     */     
/*     */ 
/* 341 */     Structure struct = CivGlobal.getStructure(this.outpost.getCorner());
/* 342 */     if (struct == null) {
/*     */       try {
/* 344 */         delete();
/*     */       } catch (SQLException e) {
/* 346 */         e.printStackTrace();
/*     */       }
/* 348 */       return;
/*     */     }
/*     */     
/* 351 */     if (this.holderStore != null) {
/* 352 */       if ((this.holderStore.getHolder() instanceof Chest)) {
/* 353 */         Location holderLocation = ((Chest)this.holderStore.getHolder()).getLocation();
/*     */         
/* 355 */         hashmap.put("holder_location", new BlockCoord(holderLocation).toString());
/* 356 */         hashmap.put("player_name", null);
/* 357 */         hashmap.put("frame_uid", null);
/* 358 */         hashmap.put("frame_location", null);
/* 359 */         hashmap.put("item_uid", null);
/*     */ 
/*     */       }
/* 362 */       else if ((this.holderStore.getHolder() instanceof DoubleChest)) {
/* 363 */         Location holderLocation = ((DoubleChest)this.holderStore.getHolder()).getLocation();
/*     */         
/* 365 */         hashmap.put("holder_location", new BlockCoord(holderLocation).toString());
/* 366 */         hashmap.put("player_name", null);
/* 367 */         hashmap.put("frame_uid", null);
/* 368 */         hashmap.put("frame_location", null);
/* 369 */         hashmap.put("item_uid", null);
/*     */ 
/*     */       }
/* 372 */       else if ((this.holderStore.getHolder() instanceof Player))
/*     */       {
/* 374 */         hashmap.put("player_name", ((Player)this.holderStore.getHolder()).getName());
/* 375 */         hashmap.put("holder_location", null);
/* 376 */         hashmap.put("frame_uid", null);
/* 377 */         hashmap.put("frame_location", null);
/* 378 */         hashmap.put("item_uid", null);
/*     */       }
/*     */     }
/*     */     else {
/* 382 */       hashmap.put("holder_location", null);
/* 383 */       hashmap.put("player_name", null);
/*     */     }
/*     */     
/* 386 */     if (this.frameStore != null) {
/* 387 */       hashmap.put("frame_uid", this.frameStore.getUUID().toString());
/* 388 */       hashmap.put("frame_location", this.frameStore.getCoord().toString());
/* 389 */       hashmap.put("player_name", null);
/* 390 */       hashmap.put("holder_location", null);
/* 391 */       hashmap.put("item_uid", null);
/*     */     }
/*     */     else {
/* 394 */       hashmap.put("frame_uid", null);
/* 395 */       hashmap.put("frame_location", null);
/*     */     }
/*     */     
/* 398 */     if (this.item != null) {
/* 399 */       hashmap.put("item_uid", this.item.getUniqueId().toString());
/* 400 */       hashmap.put("player_name", null);
/* 401 */       hashmap.put("holder_location", null);
/* 402 */       hashmap.put("frame_uid", null);
/* 403 */       hashmap.put("frame_location", null);
/*     */     } else {
/* 405 */       hashmap.put("item_uid", null);
/*     */     }
/*     */     
/* 408 */     hashmap.put("outpost_location", getOutpost().getCorner().toString());
/*     */     try
/*     */     {
/* 411 */       if (sync) {
/* 412 */         SQL.updateNamedObject(this, hashmap, "GOODIE_ITEMS");
/*     */       } else {
/* 414 */         SQL.updateNamedObjectAsync(this, hashmap, "GOODIE_ITEMS");
/*     */       }
/*     */     } catch (SQLException e) {
/* 417 */       CivLog.error("Internal Database error in update of goodie.");
/* 418 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public TradeOutpost getOutpost()
/*     */   {
/* 424 */     return this.outpost;
/*     */   }
/*     */   
/*     */   public void setOutpost(TradeOutpost outpost) {
/* 428 */     this.outpost = outpost;
/*     */   }
/*     */   
/*     */   public InventoryHolder getHolder() {
/* 432 */     if (this.holderStore == null) {
/* 433 */       return null;
/*     */     }
/*     */     try {
/* 436 */       return this.holderStore.getHolder();
/*     */     } catch (CivException e) {
/* 438 */       e.printStackTrace(); }
/* 439 */     return null;
/*     */   }
/*     */   
/*     */   public void setHolder(InventoryHolder holder) throws CivException
/*     */   {
/* 444 */     if (holder == null) {
/* 445 */       return;
/*     */     }
/*     */     
/* 448 */     if (this.holderStore == null) {
/* 449 */       if ((holder instanceof Chest)) {
/* 450 */         this.holderStore = new InventoryHolderStorage(((Chest)holder).getLocation());
/* 451 */       } else if ((holder instanceof Player)) {
/* 452 */         this.holderStore = new InventoryHolderStorage((Player)holder);
/*     */       } else {
/* 454 */         throw new CivException("Invalid holder.");
/*     */       }
/*     */     } else {
/* 457 */       this.holderStore.setHolder(holder);
/*     */     }
/*     */     
/*     */ 
/* 461 */     this.frameStore = null;
/* 462 */     this.item = null;
/*     */   }
/*     */   
/*     */   public ItemStack getStack()
/*     */   {
/* 467 */     return findStack();
/*     */   }
/*     */   
/*     */   public Item getItem() {
/* 471 */     return this.item;
/*     */   }
/*     */   
/*     */   public void setItem(Item item) {
/* 475 */     this.item = item;
/* 476 */     if (item != null)
/*     */     {
/* 478 */       this.frameStore = null;
/* 479 */       this.holderStore = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemFrameStorage getFrame() {
/* 484 */     return this.frameStore;
/*     */   }
/*     */   
/*     */   public void setFrame(ItemFrameStorage frameStore) {
/* 488 */     this.frameStore = frameStore;
/* 489 */     if (frameStore != null)
/*     */     {
/* 491 */       this.holderStore = null;
/* 492 */       this.item = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(ResultSet rs) throws SQLException, InvalidNameException
/*     */   {
/* 498 */     setId(rs.getInt("id"));
/*     */     
/* 500 */     String holderLocString = rs.getString("holder_location");
/* 501 */     String outpostLocString = rs.getString("outpost_location");
/* 502 */     String frameUID = rs.getString("frame_uid");
/* 503 */     String itemUID = rs.getString("item_uid");
/* 504 */     Location outpostLocation = null;
/*     */     
/*     */     try
/*     */     {
/* 508 */       if ((outpostLocString != null) && (!outpostLocString.equals(""))) {
/* 509 */         outpostLocation = CivGlobal.getLocationFromHash(outpostLocString);
/* 510 */         this.outpost = ((TradeOutpost)CivGlobal.getStructure(new BlockCoord(outpostLocation)));
/*     */       }
/*     */     } catch (Exception e) {
/* 513 */       e.printStackTrace();
/* 514 */       return;
/*     */     }
/*     */     
/*     */ 
/* 518 */     if ((this.outpost == null) || (outpostLocation == null))
/*     */     {
/* 520 */       delete();
/* 521 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 527 */     if ((holderLocString != null) && (!holderLocString.equals(""))) {
/* 528 */       Location loc = CivGlobal.getLocationFromHash(holderLocString);
/* 529 */       BlockCoord bcoord = new BlockCoord(loc);
/*     */       
/* 531 */       Block b = bcoord.getBlock();
/* 532 */       if ((b.getState() instanceof Chest))
/*     */       {
/* 534 */         Inventory inv = ((Chest)b.getState()).getInventory();
/*     */         Iterator localIterator2;
/* 536 */         for (Iterator localIterator1 = CivSettings.goods.values().iterator(); localIterator1.hasNext(); 
/* 537 */             localIterator2.hasNext())
/*     */         {
/* 536 */           ConfigTradeGood good = (ConfigTradeGood)localIterator1.next();
/* 537 */           localIterator2 = inv.all(ItemManager.getMaterial(good.material)).entrySet().iterator(); continue;Map.Entry<Integer, ? extends ItemStack> itemEntry = (Map.Entry)localIterator2.next();
/* 538 */           if (ItemManager.getData((ItemStack)itemEntry.getValue()) == good.material_data)
/*     */           {
/*     */ 
/* 541 */             ItemStack stack = (ItemStack)itemEntry.getValue();
/*     */             
/* 543 */             if (isItemStackOurs(stack))
/*     */             {
/* 545 */               this.holderStore = new InventoryHolderStorage(inv.getHolder(), b.getLocation());
/* 546 */               this.frameStore = null;
/* 547 */               this.item = null;
/* 548 */               CivGlobal.addBonusGoodie(this);
/* 549 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 557 */     if ((frameUID != null) && (!frameUID.equals(""))) {
/* 558 */       Location loc = CivGlobal.getLocationFromHash(rs.getString("frame_location"));
/* 559 */       loc.getWorld().loadChunk(loc.getChunk());
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 564 */         this.frameStore = CivGlobal.getProtectedItemFrame(UUID.fromString(frameUID));
/* 565 */         if (this.frameStore == null) {
/* 566 */           throw new CivException("Couldn't find frame loaded from a structure? missing frame:" + frameUID);
/*     */         }
/*     */         
/*     */       }
/*     */       catch (CivException e)
/*     */       {
/* 572 */         CivLog.warning("Couldn't find frame loaded from DB:" + frameUID);
/* 573 */         deleteAndReset();
/* 574 */         return;
/*     */       }
/*     */       
/* 577 */       this.holderStore = null;
/* 578 */       this.item = null;
/*     */       try
/*     */       {
/* 581 */         if ((this.frameStore.isEmpty()) || (!isItemStackOurs(this.frameStore.getItem())))
/*     */         {
/* 583 */           CivLog.warning("Found frame, but item was wrong:" + frameUID);
/* 584 */           deleteAndReset();
/* 585 */           return;
/*     */         }
/*     */       } catch (CivException e) {
/* 588 */         e.printStackTrace();
/* 589 */         deleteAndReset();
/*     */         
/*     */ 
/* 592 */         CivGlobal.addBonusGoodie(this);
/* 593 */         return;
/*     */       }
/*     */     }
/* 596 */     if ((itemUID != null) && (!itemUID.equals(""))) {
/* 597 */       this.item = ((Item)EntityUtil.getEntity(outpostLocation.getWorld(), UUID.fromString(itemUID)));
/* 598 */       if (this.item == null) {
/* 599 */         CivLog.warning("ITEM ON GROUND WAS NULL...deleting goodie");
/* 600 */         delete();
/* 601 */         return;
/*     */       }
/* 603 */       this.frameStore = null;
/* 604 */       this.holderStore = null;
/*     */       
/* 606 */       if (!isItemStackOurs(this.item.getItemStack())) {
/* 607 */         deleteAndReset();
/* 608 */         return;
/*     */       }
/*     */       
/* 611 */       CivGlobal.addBonusGoodie(this);
/* 612 */       return;
/*     */     }
/*     */     
/* 615 */     deleteAndReset();
/*     */   }
/*     */   
/*     */   private void deleteAndReset() throws SQLException {
/* 619 */     delete();
/*     */     try {
/* 621 */       createGoodieAtOutpost();
/*     */     } catch (CivException e) {
/* 623 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isItemStackOurs(ItemStack stack)
/*     */   {
/* 629 */     ItemMeta meta = stack.getItemMeta();
/* 630 */     if (meta == null) {
/* 631 */       return false;
/*     */     }
/*     */     
/* 634 */     if ((meta.hasLore()) && (meta.getLore().size() >= LoreIndex.values().length) && 
/* 635 */       (((String)meta.getLore().get(LoreIndex.TYPE.ordinal())).equals("Bonus Goodie"))) {
/* 636 */       String outpostLoreLoc = (String)meta.getLore().get(LoreIndex.OUTPOSTLOCATION.ordinal());
/* 637 */       Location loc = CivGlobal.getLocationFromHash(outpostLoreLoc);
/*     */       
/* 639 */       if ((loc.getBlockX() == this.outpost.getCorner().getX()) && 
/* 640 */         (loc.getBlockY() == this.outpost.getCorner().getY()) && 
/* 641 */         (loc.getBlockZ() == this.outpost.getCorner().getZ()))
/*     */       {
/* 643 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 647 */     return false;
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException
/*     */   {
/*     */     try {
/* 653 */       update(true);
/*     */     } catch (CivException e) {
/* 655 */       e.printStackTrace();
/*     */     }
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
/* 667 */     SQLUpdate.add(this);
/*     */   }
/*     */   
/*     */   public void delete()
/*     */     throws SQLException
/*     */   {
/* 673 */     if (this.item != null) {
/* 674 */       this.item.remove();
/*     */     }
/*     */     
/* 677 */     if (this.frameStore != null) {
/* 678 */       this.frameStore.setItem(new ItemStack(Material.AIR));
/*     */     }
/*     */     
/* 681 */     if (this.holderStore != null) {
/*     */       try {
/* 683 */         this.holderStore.getHolder().getInventory().remove(findStack());
/*     */       } catch (CivException e) {
/* 685 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 689 */     SQL.deleteNamedObject(this, "GOODIE_ITEMS");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void load() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBonusValue(String key)
/*     */   {
/* 702 */     if (this.config.buffs.containsKey(key)) {
/* 703 */       return ((ConfigBuff)this.config.buffs.get(key)).value;
/*     */     }
/* 705 */     return "";
/*     */   }
/*     */   
/*     */   public ConfigTradeGood getConfigTradeGood() {
/* 709 */     return this.config;
/*     */   }
/*     */   
/*     */   public String getOutpostStringFromLore() {
/* 713 */     return (String)getLore(findStack()).get(1);
/*     */   }
/*     */   
/*     */   public String getBonusDisplayString() {
/* 717 */     String out = "";
/*     */     
/* 719 */     for (ConfigBuff cBuff : this.config.buffs.values()) {
/* 720 */       out = out + ChatColor.UNDERLINE + cBuff.name;
/* 721 */       out = out + ";";
/* 722 */       out = out + "§f" + ChatColor.ITALIC + cBuff.description;
/* 723 */       out = out + ";";
/*     */     }
/*     */     
/* 726 */     return out;
/*     */   }
/*     */   
/*     */   public String getDisplayName() {
/* 730 */     return this.config.name;
/*     */   }
/*     */   
/*     */   public boolean isStackable()
/*     */   {
/* 735 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 740 */     return this.outpost.getCorner().toString().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/* 745 */     if ((other instanceof BonusGoodie)) {
/* 746 */       BonusGoodie otherCoord = (BonusGoodie)other;
/* 747 */       if (otherCoord.getOutpost().getCorner().equals(getOutpost().getCorner().toString())) {
/* 748 */         return true;
/*     */       }
/*     */     }
/* 751 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\BonusGoodie.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */