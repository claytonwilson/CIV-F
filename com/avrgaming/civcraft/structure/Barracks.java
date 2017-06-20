/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMaterial;
/*     */ import com.avrgaming.civcraft.config.ConfigUnit;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveRepairItem;
/*     */ import com.avrgaming.civcraft.items.components.RepairCost;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.AttrSource;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.UnitSaveAsyncTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class Barracks
/*     */   extends Structure
/*     */ {
/*     */   private static final long SAVE_INTERVAL = 60000L;
/*  64 */   private int index = 0;
/*     */   
/*     */   private StructureSign unitNameSign;
/*  67 */   private ConfigUnit trainingUnit = null;
/*  68 */   private double currentHammers = 0.0D;
/*     */   
/*  70 */   private TreeMap<Integer, StructureSign> progresBar = new TreeMap();
/*  71 */   private Date lastSave = null;
/*     */   
/*     */   protected Barracks(Location center, String id, Town town) throws CivException
/*     */   {
/*  75 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public Barracks(ResultSet rs) throws SQLException, CivException {
/*  79 */     super(rs);
/*     */   }
/*     */   
/*     */   private String getUnitSignText(int index) throws IndexOutOfBoundsException {
/*  83 */     ArrayList<ConfigUnit> unitList = getTown().getAvailableUnits();
/*     */     
/*  85 */     if (unitList.size() == 0) {
/*  86 */       return "\n§7None\n§7Available";
/*     */     }
/*     */     
/*  89 */     ConfigUnit unit = (ConfigUnit)unitList.get(index);
/*  90 */     String out = "\n";
/*  91 */     out = out + "§d" + unit.name + "\n";
/*  92 */     out = out + "§e" + unit.cost + "\n";
/*  93 */     out = out + "§ecoins";
/*     */     
/*  95 */     return out;
/*     */   }
/*     */   
/*     */   private void changeIndex(int newIndex) {
/*  99 */     if (this.unitNameSign != null) {
/*     */       try {
/* 101 */         this.unitNameSign.setText(getUnitSignText(newIndex));
/* 102 */         this.index = newIndex;
/*     */       }
/*     */       catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}
/*     */       
/*     */ 
/* 107 */       this.unitNameSign.update();
/*     */     } else {
/* 109 */       CivLog.warning("Could not find unit name sign for barracks:" + getId() + " at " + getCorner());
/*     */     }
/*     */   }
/*     */   
/*     */   private void train(Resident whoClicked) throws CivException
/*     */   {
/* 115 */     ArrayList<ConfigUnit> unitList = getTown().getAvailableUnits();
/*     */     
/* 117 */     ConfigUnit unit = (ConfigUnit)unitList.get(this.index);
/* 118 */     if (unit == null) {
/* 119 */       throw new CivException("Unknown unit type.");
/*     */     }
/*     */     
/* 122 */     if ((unit.limit != 0) && (unit.limit < getTown().getUnitTypeCount(unit.id))) {
/* 123 */       throw new CivException("We've reached the maximum number of " + unit.name + " units we can have.");
/*     */     }
/*     */     
/* 126 */     if (!getTown().getTreasury().hasEnough(unit.cost)) {
/* 127 */       throw new CivException("Not enough coins to train unit. We require " + unit.cost + " coins.");
/*     */     }
/*     */     
/* 130 */     if (!unit.isAvailable(getTown())) {
/* 131 */       throw new CivException("This unit is unavailable.");
/*     */     }
/*     */     
/* 134 */     if (this.trainingUnit != null) {
/* 135 */       throw new CivException("Already training a " + this.trainingUnit.name + ".");
/*     */     }
/*     */     
/* 138 */     if ((unit.id.equals("u_settler")) && 
/* 139 */       (!getCiv().getLeaderGroup().hasMember(whoClicked)) && (!getCiv().getAdviserGroup().hasMember(whoClicked))) {
/* 140 */       throw new CivException("You must be an adivser to the civilization in order to build a Settler.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 145 */     getTown().getTreasury().withdraw(unit.cost);
/*     */     
/*     */ 
/* 148 */     setCurrentHammers(0.0D);
/* 149 */     setTrainingUnit(unit);
/* 150 */     CivMessage.sendTown(getTown(), "We've begun training a " + unit.name + "!");
/* 151 */     updateTraining();
/*     */   }
/*     */   
/*     */ 
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/* 157 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 159 */     if (resident == null) {
/*     */       return;
/*     */     }
/*     */     String str;
/* 163 */     switch ((str = sign.getAction()).hashCode()) {case 3377907:  if (str.equals("next")) {} break; case 3449395:  if (str.equals("prev")) break; break; case 110621192:  if (str.equals("train")) {} break; case 1668727941:  if (!str.equals("repair_item"))
/*     */       {
/* 165 */         return;changeIndex(this.index - 1);
/* 166 */         return;
/*     */         
/* 168 */         changeIndex(this.index + 1);
/* 169 */         return;
/*     */         
/* 171 */         if (resident.hasTown()) {
/*     */           try {
/* 173 */             if ((getTown().getAssistantGroup().hasMember(resident)) || (getTown().getMayorGroup().hasMember(resident))) {
/* 174 */               train(resident);
/* 175 */               return; }
/* 176 */             throw new CivException("Only Mayors and Assistants may train units.");
/*     */           }
/*     */           catch (CivException e) {
/* 179 */             CivMessage.send(player, "§c" + e.getMessage());
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 184 */         repairItem(player, resident, event);
/*     */       }
/*     */       break; }
/*     */   }
/*     */   
/*     */   private void repairItem(Player player, Resident resident, PlayerInteractEvent event) {
/*     */     try {
/* 191 */       ItemStack inHand = player.getItemInHand();
/* 192 */       if ((inHand == null) || (inHand.getType().equals(Material.AIR))) {
/* 193 */         throw new CivException("Must have an item in your hand in order to repair it.");
/*     */       }
/*     */       
/* 196 */       if (inHand.getType().getMaxDurability() == 0) {
/* 197 */         throw new CivException("Can only repair items that use durability.");
/*     */       }
/*     */       
/* 200 */       if (inHand.getDurability() == 0) {
/* 201 */         throw new CivException("This item is already at full health.");
/*     */       }
/*     */       
/* 204 */       LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterial(inHand);
/* 205 */       if (craftMat == null) {
/* 206 */         throw new CivException("Cannot repair this item.");
/*     */       }
/*     */       try {
/*     */         double totalCost;
/*     */         double totalCost;
/* 211 */         if (craftMat.hasComponent("RepairCost")) {
/* 212 */           RepairCost repairCost = (RepairCost)craftMat.getComponent("RepairCost");
/* 213 */           totalCost = repairCost.getDouble("value");
/*     */         } else {
/* 215 */           double baseTierRepair = CivSettings.getDouble(CivSettings.structureConfig, "barracks.base_tier_repair");
/* 216 */           double tierDamp = CivSettings.getDouble(CivSettings.structureConfig, "barracks.tier_damp");
/* 217 */           double tierCost = Math.pow(craftMat.getConfigMaterial().tier, tierDamp);
/* 218 */           double fromTier = Math.pow(baseTierRepair, tierCost);
/* 219 */           totalCost = Math.round(fromTier + 0.0D);
/*     */         }
/*     */         
/* 222 */         InteractiveRepairItem repairItem = new InteractiveRepairItem(totalCost, player.getName(), craftMat);
/* 223 */         repairItem.displayMessage();
/* 224 */         resident.setInteractiveMode(repairItem);
/* 225 */         return;
/*     */       }
/*     */       catch (InvalidConfiguration e) {
/* 228 */         e.printStackTrace();
/* 229 */         throw new CivException("Internal configuration error");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */       return;
/*     */     }
/*     */     catch (CivException e)
/*     */     {
/* 235 */       CivMessage.sendError(player, e.getMessage());
/* 236 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void repairItemInHand(double cost, String playerName, LoreCraftableMaterial craftMat)
/*     */   {
/*     */     try
/*     */     {
/* 244 */       player = CivGlobal.getPlayer(playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return;
/*     */     }
/*     */     Player player;
/* 249 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/* 251 */     if (!resident.getTreasury().hasEnough(cost)) {
/* 252 */       CivMessage.sendError(player, "Sorry, but you don't have the required " + cost + " coins.");
/* 253 */       return;
/*     */     }
/*     */     
/* 256 */     LoreCraftableMaterial craftMatInHand = LoreCraftableMaterial.getCraftMaterial(player.getItemInHand());
/*     */     
/* 258 */     if (!craftMatInHand.getConfigId().equals(craftMat.getConfigId())) {
/* 259 */       CivMessage.sendError(player, "You're not holding the item that you started the repair with.");
/* 260 */       return;
/*     */     }
/*     */     
/* 263 */     resident.getTreasury().withdraw(cost);
/* 264 */     player.getItemInHand().setDurability((short)0);
/*     */     
/* 266 */     CivMessage.sendSuccess(player, "Repaired " + craftMat.getName() + " for " + cost + " coins.");
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
/*     */   public void onTechUpdate()
/*     */   {
/* 289 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       StructureSign unitNameSign;
/*     */       
/*     */       public void run()
/*     */       {
/* 284 */         this.unitNameSign.setText(Barracks.this.getUnitSignText(Barracks.this.index));
/* 285 */         this.unitNameSign.update();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onPostBuild(BlockCoord absCoord, SimpleBlock sb)
/*     */   {
/*     */     String str;
/*     */     
/*     */ 
/* 297 */     switch ((str = sb.command).hashCode()) {case -52092068:  if (str.equals("/progress")) {} break; case 46783394:  if (str.equals("/next")) {} break; case 46854882:  if (str.equals("/prev")) break; break; case 657085246:  if (str.equals("/unitname")) {} break; case 1456191289:  if (str.equals("/train")) {} break; case 2123432060:  if (!str.equals("/repair"))
/*     */       {
/* 299 */         return;ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 300 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/* 301 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 302 */         structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Prev Unit");
/* 303 */         structSign.setDirection(sb.getData());
/* 304 */         structSign.setAction("prev");
/* 305 */         structSign.update();
/* 306 */         addStructureSign(structSign);
/* 307 */         CivGlobal.addStructureSign(structSign);
/*     */         
/* 309 */         structSign.save();
/* 310 */         return;
/*     */         
/* 312 */         ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 313 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/*     */         
/* 315 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 316 */         structSign.setText(getUnitSignText(0));
/* 317 */         structSign.setDirection(sb.getData());
/* 318 */         structSign.setAction("info");
/* 319 */         structSign.update();
/*     */         
/* 321 */         this.unitNameSign = structSign;
/*     */         
/* 323 */         addStructureSign(structSign);
/* 324 */         CivGlobal.addStructureSign(structSign);
/*     */         
/* 326 */         structSign.save();
/* 327 */         return;
/*     */         
/* 329 */         ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 330 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/*     */         
/* 332 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 333 */         structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Next Unit");
/* 334 */         structSign.setDirection(sb.getData());
/* 335 */         structSign.setAction("next");
/* 336 */         structSign.update();
/* 337 */         addStructureSign(structSign);
/* 338 */         CivGlobal.addStructureSign(structSign);
/*     */         
/* 340 */         structSign.save();
/*     */         
/* 342 */         return;
/*     */         
/* 344 */         ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 345 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/*     */         
/* 347 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 348 */         structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Train");
/* 349 */         structSign.setDirection(sb.getData());
/* 350 */         structSign.setAction("train");
/* 351 */         structSign.update();
/* 352 */         addStructureSign(structSign);
/* 353 */         CivGlobal.addStructureSign(structSign);
/*     */         
/* 355 */         structSign.save();
/* 356 */         return;
/*     */         
/* 358 */         ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 359 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/*     */         
/* 361 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 362 */         structSign.setText("");
/* 363 */         structSign.setDirection(sb.getData());
/* 364 */         structSign.setAction("");
/* 365 */         structSign.update();
/* 366 */         addStructureSign(structSign);
/* 367 */         CivGlobal.addStructureSign(structSign);
/*     */         
/* 369 */         this.progresBar.put(Integer.valueOf((String)sb.keyvalues.get("id")), structSign);
/*     */         
/* 371 */         structSign.save();
/*     */       }
/*     */       else {
/* 374 */         ItemManager.setTypeId(absCoord.getBlock(), sb.getType());
/* 375 */         ItemManager.setData(absCoord.getBlock(), sb.getData());
/*     */         
/* 377 */         StructureSign structSign = new StructureSign(absCoord, this);
/* 378 */         structSign.setText("\n" + ChatColor.BOLD + ChatColor.UNDERLINE + "Repair Item");
/* 379 */         structSign.setDirection(sb.getData());
/* 380 */         structSign.setAction("repair_item");
/* 381 */         structSign.update();
/* 382 */         addStructureSign(structSign);
/* 383 */         CivGlobal.addStructureSign(structSign);
/*     */         
/* 385 */         structSign.save();
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 392 */     return this.index;
/*     */   }
/*     */   
/*     */   public void setIndex(int index) {
/* 396 */     this.index = index;
/*     */   }
/*     */   
/*     */   public ConfigUnit getTrainingUnit() {
/* 400 */     return this.trainingUnit;
/*     */   }
/*     */   
/*     */   public void setTrainingUnit(ConfigUnit trainingUnit) {
/* 404 */     this.trainingUnit = trainingUnit;
/*     */   }
/*     */   
/*     */   public double getCurrentHammers() {
/* 408 */     return this.currentHammers;
/*     */   }
/*     */   
/*     */   public void setCurrentHammers(double currentHammers) {
/* 412 */     this.currentHammers = currentHammers;
/*     */   }
/*     */   
/*     */ 
/*     */   public void createUnit(ConfigUnit unit)
/*     */   {
/* 418 */     ArrayList<StructureChest> chests = getAllChestsById(0);
/* 419 */     if (chests.size() == 0) {
/* 420 */       return;
/*     */     }
/*     */     
/* 423 */     Chest chest = (Chest)((StructureChest)chests.get(0)).getCoord().getBlock().getState();
/*     */     try
/*     */     {
/* 426 */       Class<?> c = Class.forName(unit.class_name);
/* 427 */       Method m = c.getMethod("spawn", new Class[] { Inventory.class, Town.class });
/* 428 */       m.invoke(null, new Object[] { chest.getInventory(), getTown() });
/*     */       
/* 430 */       CivMessage.sendTown(getTown(), "Completed a " + unit.name + "!");
/* 431 */       this.trainingUnit = null;
/* 432 */       this.currentHammers = 0.0D;
/*     */       
/* 434 */       CivGlobal.getSessionDB().delete_all(getSessionKey());
/*     */     }
/*     */     catch (ClassNotFoundException|SecurityException|IllegalAccessException|IllegalArgumentException|NoSuchMethodException e)
/*     */     {
/* 438 */       this.trainingUnit = null;
/* 439 */       this.currentHammers = 0.0D;
/* 440 */       CivMessage.sendTown(getTown(), "§4ERROR couldn't find class?:" + e.getMessage());
/*     */     } catch (InvocationTargetException e) {
/* 442 */       CivMessage.sendTown(getTown(), "§c" + e.getCause().getMessage());
/* 443 */       this.currentHammers -= 20.0D;
/* 444 */       if (this.currentHammers < 0.0D) {
/* 445 */         this.currentHammers = 0.0D;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateProgressBar()
/*     */   {
/* 455 */     double percentageDone = 0.0D;
/*     */     
/* 457 */     percentageDone = this.currentHammers / this.trainingUnit.hammer_cost;
/* 458 */     int size = this.progresBar.size();
/* 459 */     int textCount = (int)(size * 16 * percentageDone);
/* 460 */     int textIndex = 0;
/*     */     
/* 462 */     for (int i = 0; i < size; i++) {
/* 463 */       StructureSign structSign = (StructureSign)this.progresBar.get(Integer.valueOf(i));
/* 464 */       String[] text = new String[4];
/* 465 */       text[0] = "";
/* 466 */       text[1] = "";
/* 467 */       text[2] = "";
/* 468 */       text[3] = "";
/* 469 */       for (int j = 0; tmp107_106 < 16; tmp107_106++) {
/* 470 */         if (textIndex == 0) {
/* 471 */           int tmp107_106 = 2; String[] tmp107_104 = text;tmp107_104[tmp107_106] = (tmp107_104[tmp107_106] + "[");
/* 472 */         } else if (textIndex == size * 15 + 3) {
/* 473 */           int tmp147_146 = 2; String[] tmp147_144 = text;tmp147_144[tmp147_146] = (tmp147_144[tmp147_146] + "]");
/* 474 */         } else if (textIndex < textCount) {
/* 475 */           int tmp183_182 = 2; String[] tmp183_180 = text;tmp183_180[tmp183_182] = (tmp183_180[tmp183_182] + "=");
/*     */         } else {
/* 477 */           int tmp212_211 = 2; String[] tmp212_209 = text;tmp212_209[tmp212_211] = (tmp212_209[tmp212_211] + "_");
/*     */         }
/*     */         
/* 480 */         textIndex++;
/*     */       }
/*     */       
/* 483 */       if (i == size / 2) {
/* 484 */         text[1] = ("§a" + this.trainingUnit.name);
/*     */       }
/*     */       
/* 487 */       structSign.setText(text);
/* 488 */       structSign.update();
/*     */     }
/*     */   }
/*     */   
/*     */   public String getSessionKey()
/*     */   {
/* 494 */     return getTown().getName() + ":" + "barracks" + ":" + getId();
/*     */   }
/*     */   
/*     */   public void saveProgress() {
/* 498 */     if (getTrainingUnit() != null) {
/* 499 */       String key = getSessionKey();
/* 500 */       String value = getTrainingUnit().id + ":" + this.currentHammers;
/* 501 */       ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/*     */       
/* 503 */       if (entries.size() > 0) {
/* 504 */         SessionEntry entry = (SessionEntry)entries.get(0);
/* 505 */         CivGlobal.getSessionDB().update(entry.request_id, key, value);
/*     */         
/*     */ 
/* 508 */         for (int i = 1; i < entries.size(); i++) {
/* 509 */           SessionEntry bad_entry = (SessionEntry)entries.get(i);
/* 510 */           CivGlobal.getSessionDB().delete(bad_entry.request_id, key);
/*     */         }
/*     */       } else {
/* 513 */         sessionAdd(key, value);
/*     */       }
/*     */       
/* 516 */       this.lastSave = new Date();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onUnload()
/*     */   {
/* 522 */     saveProgress();
/*     */   }
/*     */   
/*     */   public void onLoad()
/*     */   {
/* 527 */     String key = getSessionKey();
/* 528 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/*     */     
/* 530 */     if (entries.size() > 0) {
/* 531 */       SessionEntry entry = (SessionEntry)entries.get(0);
/* 532 */       String[] values = entry.value.split(":");
/*     */       
/* 534 */       this.trainingUnit = ((ConfigUnit)CivSettings.units.get(values[0]));
/*     */       
/* 536 */       if (this.trainingUnit == null) {
/* 537 */         CivLog.error("Couldn't find in-progress unit id:" + values[0] + " for town " + getTown().getName());
/* 538 */         return;
/*     */       }
/*     */       
/* 541 */       this.currentHammers = Double.valueOf(values[1]).doubleValue();
/*     */       
/*     */ 
/* 544 */       for (int i = 1; i < entries.size(); i++) {
/* 545 */         SessionEntry bad_entry = (SessionEntry)entries.get(i);
/* 546 */         CivGlobal.getSessionDB().delete(bad_entry.request_id, key);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateTraining()
/*     */   {
/* 553 */     if (this.trainingUnit != null)
/*     */     {
/* 555 */       double addedHammers = getTown().getHammers().total / 60.0D / 60.0D;
/* 556 */       this.currentHammers += addedHammers;
/*     */       
/*     */ 
/* 559 */       updateProgressBar();
/* 560 */       Date now = new Date();
/*     */       
/* 562 */       if ((this.lastSave == null) || (this.lastSave.getTime() + 60000L < now.getTime())) {
/* 563 */         TaskMaster.asyncTask(new UnitSaveAsyncTask(this), 0L);
/*     */       }
/*     */       
/* 566 */       if (this.currentHammers >= this.trainingUnit.hammer_cost) {
/* 567 */         this.currentHammers = this.trainingUnit.hammer_cost;
/* 568 */         createUnit(this.trainingUnit);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Barracks.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */