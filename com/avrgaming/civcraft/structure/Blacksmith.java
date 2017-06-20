/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.NonMemberFeeComponent;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.items.components.Catalyst;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivData;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.threading.tasks.NotificationTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.BukkitObjects;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.TimeTools;
/*     */ import gpl.AttributeUtil;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.Inventory;
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
/*     */ public class Blacksmith
/*     */   extends Structure
/*     */ {
/*     */   private static final long COOLDOWN = 5L;
/*  63 */   public static int SMELT_TIME_SECONDS = 10800;
/*  64 */   public static double YIELD_RATE = 1.25D;
/*     */   
/*  66 */   private Date lastUse = new Date();
/*     */   
/*     */   private NonMemberFeeComponent nonMemberFeeComponent;
/*     */   
/*  70 */   public static HashMap<BlockCoord, Blacksmith> blacksmithAnvils = new HashMap();
/*     */   
/*     */   protected Blacksmith(Location center, String id, Town town) throws CivException
/*     */   {
/*  74 */     super(center, id, town);
/*  75 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  76 */     this.nonMemberFeeComponent.onSave();
/*     */   }
/*     */   
/*     */   public Blacksmith(ResultSet rs) throws SQLException, CivException {
/*  80 */     super(rs);
/*  81 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/*  82 */     this.nonMemberFeeComponent.onLoad();
/*     */   }
/*     */   
/*     */   public double getNonResidentFee() {
/*  86 */     return this.nonMemberFeeComponent.getFeeRate();
/*     */   }
/*     */   
/*     */   public void setNonResidentFee(double nonResidentFee) {
/*  90 */     this.nonMemberFeeComponent.setFeeRate(nonResidentFee);
/*     */   }
/*     */   
/*     */   private String getNonResidentFeeString() {
/*  94 */     return "Fee: " + new StringBuilder(String.valueOf((int)(this.nonMemberFeeComponent.getFeeRate() * 100.0D))).append("%").toString().toString();
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 104 */     return "factory";
/*     */   }
/*     */   
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event) throws CivException
/*     */   {
/* 109 */     int special_id = Integer.valueOf(sign.getAction()).intValue();
/*     */     
/* 111 */     Date now = new Date();
/*     */     
/* 113 */     long diff = now.getTime() - this.lastUse.getTime();
/* 114 */     diff /= 1000L;
/*     */     
/* 116 */     if (diff < 5L) {
/* 117 */       throw new CivException("Blacksmith is on cooldown. Please wait another " + (5L - diff) + " seconds.");
/*     */     }
/*     */     
/* 120 */     this.lastUse = now;
/*     */     
/* 122 */     switch (special_id) {
/*     */     case 0: 
/* 124 */       deposit_forge(player);
/* 125 */       break;
/*     */     case 1: 
/* 127 */       double cost = CivSettings.getDoubleStructure("blacksmith.forge_cost").doubleValue();
/* 128 */       perform_forge(player, cost);
/* 129 */       break;
/*     */     case 2: 
/* 131 */       depositSmelt(player, player.getItemInHand());
/* 132 */       break;
/*     */     case 3: 
/* 134 */       withdrawSmelt(player);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateSignText()
/*     */   {
/* 142 */     double cost = CivSettings.getDoubleStructure("blacksmith.forge_cost").doubleValue();
/*     */     
/* 144 */     for (StructureSign sign : getSigns()) {
/* 145 */       int special_id = Integer.valueOf(sign.getAction()).intValue();
/*     */       
/* 147 */       switch (special_id) {
/*     */       case 0: 
/* 149 */         sign.setText("Deposit\nWithdraw\nCatalyst");
/* 150 */         break;
/*     */       case 1: 
/* 152 */         sign.setText("Forge!\nFor " + 
/* 153 */           cost + " Coins\n" + 
/* 154 */           getNonResidentFeeString());
/* 155 */         break;
/*     */       case 2: 
/* 157 */         sign.setText("Deposit\nOre\nResidents Only");
/* 158 */         break;
/*     */       case 3: 
/* 160 */         sign.setText("Withdraw\nOre\nResidents Only");
/*     */       }
/*     */       
/*     */       
/* 164 */       sign.update();
/* 165 */       sign.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public String getkey(Player player, Structure struct, String tag) {
/* 170 */     return player.getName() + "_" + struct.getConfigId() + "_" + struct.getCorner().toString() + "_" + tag;
/*     */   }
/*     */   
/*     */   public void saveItem(ItemStack item, String key)
/*     */   {
/* 175 */     String value = ItemManager.getId(item) + ":";
/*     */     
/* 177 */     for (Enchantment e : item.getEnchantments().keySet()) {
/* 178 */       value = value + ItemManager.getId(e) + "," + item.getEnchantmentLevel(e);
/* 179 */       value = value + ":";
/*     */     }
/*     */     
/* 182 */     sessionAdd(key, value);
/*     */   }
/*     */   
/*     */   public void saveCatalyst(LoreCraftableMaterial craftMat, String key) {
/* 186 */     String value = craftMat.getConfigId();
/* 187 */     sessionAdd(key, value);
/*     */   }
/*     */   
/*     */   public static boolean canSmelt(int blockid) {
/* 191 */     switch (blockid) {
/*     */     case 14: 
/*     */     case 15: 
/* 194 */       return true;
/*     */     }
/* 196 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int convertType(int blockid)
/*     */   {
/* 203 */     switch (blockid) {
/*     */     case 14: 
/* 205 */       return 266;
/*     */     case 15: 
/* 207 */       return 265;
/*     */     }
/* 209 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deposit_forge(Player player)
/*     */     throws CivException
/*     */   {
/* 219 */     ItemStack item = player.getItemInHand();
/*     */     
/* 221 */     ArrayList<SessionEntry> sessions = null;
/* 222 */     String key = getkey(player, this, "forge");
/* 223 */     sessions = CivGlobal.getSessionDB().lookup(key);
/*     */     
/* 225 */     if ((sessions == null) || (sessions.size() == 0))
/*     */     {
/* 227 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(item);
/* 228 */       if ((craftMat == null) || (!craftMat.hasComponent("Catalyst"))) {
/* 229 */         throw new CivException("You must deposit a catalyst into the forge.");
/*     */       }
/*     */       
/*     */ 
/* 233 */       saveCatalyst(craftMat, key);
/* 234 */       if (item.getAmount() > 1) {
/* 235 */         item.setAmount(item.getAmount() - 1);
/*     */       } else {
/* 237 */         player.setItemInHand(new ItemStack(Material.AIR));
/*     */       }
/*     */       
/*     */ 
/* 241 */       CivMessage.sendSuccess(player, "Deposited Catalyst.");
/*     */     }
/*     */     else {
/* 244 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(((SessionEntry)sessions.get(0)).value);
/* 245 */       if (craftMat == null) {
/* 246 */         throw new CivException("Error withdrawing catalyst from blacksmith. File a bug report!");
/*     */       }
/*     */       
/* 249 */       ItemStack stack = LoreMaterial.spawn(craftMat);
/* 250 */       HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(new ItemStack[] { stack });
/* 251 */       if (leftovers.size() > 0) {
/* 252 */         for (ItemStack is : leftovers.values()) {
/* 253 */           player.getWorld().dropItem(player.getLocation(), is);
/*     */         }
/*     */       }
/* 256 */       CivGlobal.getSessionDB().delete_all(key);
/* 257 */       CivMessage.sendSuccess(player, "Withdrawn Catalyst");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void perform_forge(Player player, double cost)
/*     */     throws CivException
/*     */   {
/* 269 */     String key = getkey(player, this, "forge");
/* 270 */     ArrayList<SessionEntry> sessions = CivGlobal.getSessionDB().lookup(key);
/*     */     
/*     */ 
/* 273 */     ItemStack stack = player.getItemInHand();
/* 274 */     AttributeUtil attrs = new AttributeUtil(stack);
/*     */     
/*     */ 
/*     */ 
/* 278 */     String freeStr = attrs.getCivCraftProperty("freeCatalyst");
/* 279 */     Catalyst catalyst; if (freeStr == null)
/*     */     {
/* 281 */       if ((sessions == null) || (sessions.size() == 0)) {
/* 282 */         throw new CivException("No catalyst in the forge. Deposit one first.");
/*     */       }
/*     */       
/* 285 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(((SessionEntry)sessions.get(0)).value);
/* 286 */       if (craftMat == null) {
/* 287 */         throw new CivException("Error getting catalyst from blacksmith. File a bug report!");
/*     */       }
/*     */       
/* 290 */       Catalyst catalyst = (Catalyst)craftMat.getComponent("Catalyst");
/* 291 */       if (catalyst == null) {
/* 292 */         throw new CivException("Error getting catalyst from blacksmith. Please file a bug report.");
/*     */       }
/*     */     } else {
/* 295 */       String[] split = freeStr.split(":");
/* 296 */       Double level = Double.valueOf(split[0]);
/* 297 */       String mid = split[1];
/*     */       
/* 299 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(mid);
/* 300 */       if (craftMat == null) {
/* 301 */         throw new CivException("Error getting catalyst from blacksmith. File a bug report!");
/*     */       }
/*     */       
/* 304 */       catalyst = (Catalyst)craftMat.getComponent("Catalyst");
/* 305 */       if (catalyst == null) {
/* 306 */         throw new CivException("Error getting catalyst from blacksmith. Please file a bug report.");
/*     */       }
/*     */       
/*     */ 
/* 310 */       level = Double.valueOf(level.doubleValue() - 1.0D);
/*     */       
/* 312 */       String[] lore = attrs.getLore();
/* 313 */       for (int i = 0; i < lore.length; i++) {
/* 314 */         String str = lore[i];
/* 315 */         if (str.contains("free enhancements")) {
/* 316 */           if (level.doubleValue() != 0.0D) {
/* 317 */             lore[i] = ("§b" + level + " free enhancements! Redeem at blacksmith.");
/* 318 */             break; }
/* 319 */           lore[i] = "";
/*     */           
/* 321 */           break;
/*     */         }
/*     */       }
/* 324 */       attrs.setLore(lore);
/*     */       
/* 326 */       if (level.doubleValue() != 0.0D) {
/* 327 */         attrs.setCivCraftProperty("freeCatalyst", level + ":" + mid);
/*     */       } else {
/* 329 */         attrs.removeCivCraftProperty("freeCatalyst");
/*     */       }
/*     */       
/* 332 */       player.setItemInHand(attrs.getStack());
/*     */     }
/*     */     
/*     */ 
/* 336 */     stack = player.getItemInHand();
/* 337 */     ItemStack enhancedItem = catalyst.getEnchantedItem(stack);
/*     */     
/* 339 */     if (enhancedItem == null) {
/* 340 */       throw new CivException("You cannot use this catalyst on this item.");
/*     */     }
/*     */     
/*     */ 
/* 344 */     CivGlobal.getSessionDB().delete_all(key);
/*     */     
/* 346 */     if (!catalyst.enchantSuccess(enhancedItem))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 351 */       player.setItemInHand(ItemManager.createItemStack(0, 1));
/* 352 */       CivMessage.sendError(player, "Enhancement failed. Item has broken.");
/* 353 */       return;
/*     */     }
/* 355 */     player.setItemInHand(enhancedItem);
/* 356 */     CivMessage.sendSuccess(player, "Enhancement succeeded!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void depositSmelt(Player player, ItemStack itemsInHand)
/*     */     throws CivException
/*     */   {
/* 368 */     if (!canSmelt(itemsInHand.getTypeId())) {
/* 369 */       throw new CivException("Can only smelt gold and iron ore.");
/*     */     }
/*     */     
/*     */ 
/* 373 */     Resident res = CivGlobal.getResident(player.getName());
/* 374 */     if ((!res.hasTown()) || (getTown() != res.getTown())) {
/* 375 */       throw new CivException("Can only use the smelter if you are a town member.");
/*     */     }
/*     */     
/* 378 */     String value = convertType(itemsInHand.getTypeId()) + ":" + itemsInHand.getAmount() * YIELD_RATE;
/* 379 */     String key = getkey(player, this, "smelt");
/*     */     
/*     */ 
/* 382 */     sessionAdd(key, value);
/*     */     
/*     */ 
/* 385 */     player.getInventory().removeItem(new ItemStack[] { itemsInHand });
/*     */     
/*     */ 
/* 388 */     BukkitObjects.scheduleAsyncDelayedTask(new NotificationTask(player.getName(), 
/* 389 */       "§a Your stack of " + itemsInHand.getAmount() + " " + 
/* 390 */       CivData.getDisplayName(itemsInHand.getTypeId()) + " has finished smelting."), 
/* 391 */       TimeTools.toTicks(SMELT_TIME_SECONDS));
/*     */     
/* 393 */     CivMessage.send(player, "§aDeposited " + itemsInHand.getAmount() + " ore.");
/*     */     
/* 395 */     player.updateInventory();
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
/*     */   public void withdrawSmelt(Player player)
/*     */     throws CivException
/*     */   {
/* 413 */     String key = getkey(player, this, "smelt");
/* 414 */     ArrayList<SessionEntry> entries = null;
/*     */     
/*     */ 
/* 417 */     Resident res = CivGlobal.getResident(player.getName());
/* 418 */     if ((!res.hasTown()) || (getTown() != res.getTown())) {
/* 419 */       throw new CivException("Can only use the smelter if you are a town member.");
/*     */     }
/*     */     
/* 422 */     entries = CivGlobal.getSessionDB().lookup(key);
/*     */     
/* 424 */     if ((entries == null) || (entries.size() == 0)) {
/* 425 */       throw new CivException("No items to withdraw");
/*     */     }
/*     */     
/* 428 */     Inventory inv = player.getInventory();
/* 429 */     HashMap<Integer, ItemStack> leftovers = null;
/*     */     
/* 431 */     for (SessionEntry se : entries) {
/* 432 */       String[] split = se.value.split(":");
/* 433 */       int itemId = Integer.valueOf(split[0]).intValue();
/* 434 */       double amount = Double.valueOf(split[1]).doubleValue();
/* 435 */       long now = System.currentTimeMillis();
/* 436 */       int secondsBetween = CivGlobal.getSecondsBetween(se.time, now);
/*     */       
/*     */ 
/* 439 */       if (secondsBetween < SMELT_TIME_SECONDS) {
/* 440 */         DecimalFormat df1 = new DecimalFormat("0.##");
/*     */         
/* 442 */         double timeLeft = (SMELT_TIME_SECONDS - secondsBetween) / 60.0D;
/*     */         
/* 444 */         CivMessage.send(player, "§eStack of " + amount + " " + 
/* 445 */           CivData.getDisplayName(itemId) + " will be finished in " + df1.format(timeLeft) + " minutes.");
/*     */       }
/*     */       else
/*     */       {
/* 449 */         ItemStack stack = new ItemStack(itemId, (int)amount, (short)0);
/* 450 */         if (stack != null) {
/* 451 */           leftovers = inv.addItem(new ItemStack[] { stack });
/*     */         }
/*     */         
/* 454 */         if (leftovers.size() == 0) {
/* 455 */           CivGlobal.getSessionDB().delete(se.request_id, se.key);
/* 456 */           CivMessage.send(player, "§aWithdrew " + amount + " " + CivData.getDisplayName(itemId));
/* 457 */           break;
/*     */         }
/*     */         
/* 460 */         CivMessage.send(player, "§cNot enough inventory space for all items.");
/*     */         
/*     */ 
/* 463 */         int leftoverAmount = CivGlobal.getLeftoverSize(leftovers);
/*     */         
/* 465 */         if (leftoverAmount != amount)
/*     */         {
/*     */ 
/*     */ 
/* 469 */           if (leftoverAmount == 0)
/*     */           {
/* 471 */             CivGlobal.getSessionDB().delete(se.request_id, se.key);
/* 472 */             break;
/*     */           }
/*     */           
/*     */ 
/* 476 */           String newValue = itemId + ":" + leftoverAmount;
/* 477 */           CivGlobal.getSessionDB().update(se.request_id, se.key, newValue);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 482 */           break;
/*     */         }
/*     */       } }
/* 485 */     player.updateInventory();
/*     */   }
/*     */   
/*     */   public void onPostBuild(BlockCoord absCoord, SimpleBlock commandBlock) {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Blacksmith.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */