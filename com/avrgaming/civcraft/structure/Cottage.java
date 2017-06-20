/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.ConsumeLevelComponent;
/*     */ import com.avrgaming.civcraft.components.ConsumeLevelComponent.Result;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigCottageLevel;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.CivTaskAbortException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.inventory.Inventory;
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
/*     */ public class Cottage
/*     */   extends Structure
/*     */ {
/*  49 */   private ConsumeLevelComponent consumeComp = null;
/*     */   
/*     */   protected Cottage(Location center, String id, Town town) throws CivException
/*     */   {
/*  53 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public Cottage(ResultSet rs) throws SQLException, CivException {
/*  57 */     super(rs);
/*     */   }
/*     */   
/*     */   public ConsumeLevelComponent getConsumeComponent() {
/*  61 */     if (this.consumeComp == null) {
/*  62 */       this.consumeComp = ((ConsumeLevelComponent)getComponent(ConsumeLevelComponent.class.getSimpleName()));
/*     */     }
/*  64 */     return this.consumeComp;
/*     */   }
/*     */   
/*     */   public void loadSettings()
/*     */   {
/*  69 */     super.loadSettings();
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
/*     */   public String getDynmapDescription()
/*     */   {
/*  82 */     if (getConsumeComponent() == null) {
/*  83 */       return "";
/*     */     }
/*     */     
/*  86 */     String out = "";
/*  87 */     out = out + "Level: " + getConsumeComponent().getLevel() + " " + getConsumeComponent().getCountString();
/*  88 */     return out;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/*  93 */     return "house";
/*     */   }
/*     */   
/*     */   public String getkey() {
/*  97 */     return getTown().getName() + "_" + getConfigId() + "_" + getCorner().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean processPoison(MultiInventory inv)
/*     */   {
/* 105 */     String key = "posiongranary:" + getTown().getName();
/*     */     
/* 107 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(key);
/* 108 */     int max_poison_ticks = -1;
/* 109 */     for (SessionEntry entry : entries) {
/* 110 */       int next = Integer.valueOf(entry.value).intValue();
/*     */       
/* 112 */       if (next > max_poison_ticks) {
/* 113 */         max_poison_ticks = next;
/*     */       }
/*     */     }
/*     */     
/* 117 */     if (max_poison_ticks > 0) {
/* 118 */       CivGlobal.getSessionDB().delete_all(key);
/* 119 */       max_poison_ticks--;
/*     */       
/* 121 */       if (max_poison_ticks > 0) {
/* 122 */         CivGlobal.getSessionDB().add(key, max_poison_ticks, getTown().getCiv().getId(), getTown().getId(), getId());
/*     */       }
/*     */       
/* 125 */       CivMessage.sendTown(getTown(), "§cOur granaries have been poisoned!!");
/* 126 */       inv.addItem(ItemManager.createItemStack(367, 4));
/* 127 */       return true;
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   public void generateCoins(CivAsyncTask task)
/*     */   {
/* 134 */     if (!isActive()) {
/* 135 */       return;
/*     */     }
/*     */     
/*     */ 
/* 139 */     MultiInventory multiInv = new MultiInventory();
/*     */     
/* 141 */     for (Structure struct : getTown().getStructures()) {
/* 142 */       if ((struct instanceof Granary)) {
/* 143 */         ArrayList<StructureChest> chests = struct.getAllChestsById(1);
/*     */         
/*     */         try
/*     */         {
/* 147 */           for (StructureChest c : chests) {
/* 148 */             task.syncLoadChunk(c.getCoord().getWorldname(), c.getCoord().getX(), c.getCoord().getZ());
/*     */             try
/*     */             {
/* 151 */               Inventory tmp = task.getChestInventory(c.getCoord().getWorldname(), c.getCoord().getX(), c.getCoord().getY(), c.getCoord().getZ(), true);
/* 152 */               multiInv.addInventory(tmp);
/*     */             } catch (CivTaskAbortException e) {
/* 154 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         } catch (InterruptedException e) {
/* 158 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/* 162 */     getConsumeComponent().setSource(multiInv);
/*     */     
/* 164 */     double cottage_consume_mod = 1.0D;
/*     */     
/* 166 */     if (getTown().getBuffManager().hasBuff("buff_preservative")) {
/* 167 */       cottage_consume_mod *= getTown().getBuffManager().getEffectiveDouble("buff_preservative");
/*     */     }
/* 169 */     if (getTown().getBuffManager().hasBuff("buff_pyramid_cottage_consume")) {
/* 170 */       cottage_consume_mod *= getTown().getBuffManager().getEffectiveDouble("buff_pyramid_cottage_consume");
/*     */     }
/*     */     
/* 173 */     if (getTown().getBuffManager().hasBuff("buff_fishing"))
/*     */     {
/* 175 */       int breadPerFish = getTown().getBuffManager().getEffectiveInt("buff_fishing");
/* 176 */       getConsumeComponent().addEquivExchange(297, 349, breadPerFish);
/*     */     }
/*     */     
/* 179 */     getConsumeComponent().setConsumeRate(cottage_consume_mod);
/* 180 */     ConsumeLevelComponent.Result result = getConsumeComponent().processConsumption();
/* 181 */     getConsumeComponent().onSave();
/* 182 */     getConsumeComponent().clearEquivExchanges();
/*     */     
/*     */ 
/* 185 */     switch (result) {
/*     */     case LEVELUP: 
/* 187 */       CivMessage.sendTown(getTown(), "§aA level " + getConsumeComponent().getLevel() + " cottage " + "§c" + "starved" + 
/* 188 */         getConsumeComponent().getCountString() + "§a" + " and generated no coins.");
/* 189 */       return;
/*     */     case STAGNATE: 
/* 191 */       CivMessage.sendTown(getTown(), "§aA level " + (getConsumeComponent().getLevel() + 1) + " cottage " + "§4" + "leveled-down" + "§a" + " and generated no coins.");
/* 192 */       return;
/*     */     case GROW: 
/* 194 */       CivMessage.sendTown(getTown(), "§aA level " + getConsumeComponent().getLevel() + " cottage " + "§e" + "stagnated" + getConsumeComponent().getCountString() + "§a" + " and generated no coins.");
/* 195 */       return;
/*     */     case UNKNOWN: 
/* 197 */       CivMessage.sendTown(getTown(), "§a§aSomething §9unknown§a happened to a cottage. It generates no coins.");
/* 198 */       return;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 203 */     if (processPoison(multiInv)) {
/* 204 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 209 */     ConfigCottageLevel lvl = null;
/* 210 */     if (result == ConsumeLevelComponent.Result.LEVELUP) {
/* 211 */       lvl = (ConfigCottageLevel)CivSettings.cottageLevels.get(Integer.valueOf(getConsumeComponent().getLevel() - 1));
/*     */     } else {
/* 213 */       lvl = (ConfigCottageLevel)CivSettings.cottageLevels.get(Integer.valueOf(getConsumeComponent().getLevel()));
/*     */     }
/*     */     
/* 216 */     int total_coins = (int)Math.round(lvl.coins * getTown().getCottageRate());
/* 217 */     if (getTown().getBuffManager().hasBuff("buff_pyramid_cottage_bonus")) {
/* 218 */       total_coins = (int)(total_coins * getTown().getBuffManager().getEffectiveDouble("buff_pyramid_cottage_bonus"));
/*     */     }
/*     */     
/* 221 */     if (getCiv().hasTechnology("tech_taxation")) {
/*     */       try
/*     */       {
/* 224 */         double taxation_bonus = CivSettings.getDouble(CivSettings.techsConfig, "taxation_cottage_buff");
/* 225 */         total_coins = (int)(total_coins * taxation_bonus);
/*     */       } catch (InvalidConfiguration e) {
/* 227 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 233 */     double taxesPaid = total_coins * getTown().getDepositCiv().getIncomeTaxRate();
/*     */     
/* 235 */     String stateMessage = "";
/* 236 */     switch (result) {
/*     */     case LEVELDOWN: 
/* 238 */       stateMessage = "§2grew" + getConsumeComponent().getCountString() + "§a";
/* 239 */       break;
/*     */     case MAXED: 
/* 241 */       stateMessage = "§2leveled up§a";
/* 242 */       break;
/*     */     case STARVE: 
/* 244 */       stateMessage = "§2is maxed" + getConsumeComponent().getCountString() + "§a";
/* 245 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 250 */     if (taxesPaid > 0.0D) {
/* 251 */       CivMessage.sendTown(getTown(), "§aA level " + getConsumeComponent().getLevel() + " cottage " + stateMessage + " and generated " + total_coins + " coins!" + 
/* 252 */         "§e" + " (Paid " + taxesPaid + " in taxes to " + getTown().getDepositCiv().getName() + ")");
/*     */     } else {
/* 254 */       CivMessage.sendTown(getTown(), "§aA level " + getConsumeComponent().getLevel() + " cottage " + stateMessage + " and generated " + total_coins + " coins!");
/*     */     }
/*     */     
/* 257 */     getTown().getTreasury().deposit(total_coins - taxesPaid);
/* 258 */     getTown().getDepositCiv().taxPayment(getTown(), taxesPaid);
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 262 */     return getConsumeComponent().getLevel();
/*     */   }
/*     */   
/*     */   public ConsumeLevelComponent.Result getLastResult() {
/* 266 */     return getConsumeComponent().getLastResult();
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 270 */     return getConsumeComponent().getCount();
/*     */   }
/*     */   
/*     */   public int getMaxCount() {
/* 274 */     int level = getLevel();
/*     */     
/* 276 */     ConfigCottageLevel lvl = (ConfigCottageLevel)CivSettings.cottageLevels.get(Integer.valueOf(level));
/* 277 */     return lvl.count;
/*     */   }
/*     */   
/*     */   public double getCoinsGenerated() {
/* 281 */     int level = getLevel();
/*     */     
/* 283 */     ConfigCottageLevel lvl = (ConfigCottageLevel)CivSettings.cottageLevels.get(Integer.valueOf(level));
/* 284 */     if (lvl == null) {
/* 285 */       return 0.0D;
/*     */     }
/* 287 */     return lvl.coins;
/*     */   }
/*     */   
/*     */   public void delevel() {
/* 291 */     int currentLevel = getLevel();
/*     */     
/* 293 */     if (currentLevel > 1) {
/* 294 */       getConsumeComponent().setLevel(getLevel() - 1);
/* 295 */       getConsumeComponent().setCount(0);
/* 296 */       getConsumeComponent().onSave();
/*     */     }
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 302 */     super.delete();
/* 303 */     if (getConsumeComponent() != null) {
/* 304 */       getConsumeComponent().onDelete();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDestroy() {
/* 309 */     super.onDestroy();
/*     */     
/* 311 */     getConsumeComponent().setLevel(1);
/* 312 */     getConsumeComponent().setCount(0);
/* 313 */     getConsumeComponent().onSave();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Cottage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */