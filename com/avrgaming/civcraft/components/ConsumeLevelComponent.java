/*     */ package com.avrgaming.civcraft.components;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigCottageLevel;
/*     */ import com.avrgaming.civcraft.config.ConfigMineLevel;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.Cottage;
/*     */ import com.avrgaming.civcraft.structure.Mine;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class ConsumeLevelComponent
/*     */   extends Component
/*     */ {
/*     */   private int level;
/*     */   private int count;
/*     */   private Result lastResult;
/*     */   private double consumeRate;
/*  63 */   private HashMap<Integer, Map<Integer, Integer>> consumptions = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   private HashMap<Integer, ConsumeLevelEquivExchange> exchanges = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<Integer, Integer> foundCounts;
/*     */   
/*     */ 
/*     */ 
/*  76 */   private HashMap<Integer, Integer> levelCounts = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private MultiInventory source;
/*     */   
/*     */ 
/*     */ 
/*     */   public void createComponent(Buildable buildable, boolean async)
/*     */   {
/*  86 */     super.createComponent(buildable, async);
/*     */     
/*     */ 
/*     */ 
/*  90 */     if ((buildable instanceof Cottage)) {
/*  91 */       for (ConfigCottageLevel lvl : CivSettings.cottageLevels.values()) {
/*  92 */         addLevel(lvl.level, lvl.count);
/*  93 */         setConsumes(lvl.level, lvl.consumes);
/*     */       }
/*  95 */     } else if ((buildable instanceof Mine)) {
/*  96 */       for (ConfigMineLevel lvl : CivSettings.mineLevels.values()) {
/*  97 */         addLevel(lvl.level, lvl.count);
/*     */         
/*  99 */         HashMap<Integer, Integer> redstoneAmounts = new HashMap();
/* 100 */         redstoneAmounts.put(Integer.valueOf(331), Integer.valueOf(lvl.amount));
/* 101 */         setConsumes(lvl.level, redstoneAmounts);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Result
/*     */   {
/* 110 */     STAGNATE, 
/* 111 */     GROW, 
/* 112 */     STARVE, 
/* 113 */     LEVELUP, 
/* 114 */     LEVELDOWN, 
/* 115 */     MAXED, 
/* 116 */     UNKNOWN;
/*     */   }
/*     */   
/*     */   public ConsumeLevelComponent() {
/* 120 */     this.level = 1;
/* 121 */     this.count = 0;
/* 122 */     this.consumeRate = 1.0D;
/* 123 */     this.lastResult = Result.UNKNOWN;
/*     */   }
/*     */   
/*     */   private String getKey() {
/* 127 */     return getBuildable().getDisplayName() + ":" + getBuildable().getId() + ":" + "levelcount";
/*     */   }
/*     */   
/*     */   private String getValue() {
/* 131 */     return this.level + ":" + this.count;
/*     */   }
/*     */   
/*     */   public void onLoad()
/*     */   {
/* 136 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getKey());
/*     */     
/* 138 */     if (entries.size() == 0) {
/* 139 */       getBuildable().sessionAdd(getKey(), getValue());
/* 140 */       return;
/*     */     }
/*     */     
/* 143 */     String[] split = ((SessionEntry)entries.get(0)).value.split(":");
/* 144 */     this.level = Integer.valueOf(split[0]).intValue();
/* 145 */     this.count = Integer.valueOf(split[1]).intValue();
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
/*     */   public void onSave()
/*     */   {
/* 165 */     if (getBuildable().getId() != 0) {
/* 166 */       TaskMaster.asyncTask(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 154 */           ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(ConsumeLevelComponent.this.getKey());
/*     */           
/* 156 */           if (entries.size() == 0) {
/* 157 */             ConsumeLevelComponent.this.getBuildable().sessionAdd(ConsumeLevelComponent.this.getKey(), ConsumeLevelComponent.this.getValue());
/* 158 */             return;
/*     */           }
/*     */           
/* 161 */           CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, ConsumeLevelComponent.this.getKey(), ConsumeLevelComponent.this.getValue());
/*     */ 
/*     */         }
/*     */         
/*     */ 
/* 166 */       }, 0L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDelete()
/*     */   {
/* 178 */     if (getBuildable().getId() != 0) {
/* 179 */       TaskMaster.asyncTask(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 174 */           CivGlobal.getSessionDB().delete_all(ConsumeLevelComponent.this.getKey());
/*     */ 
/*     */         }
/*     */         
/*     */ 
/* 179 */       }, 0L);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addLevel(int level, int count) {
/* 184 */     this.levelCounts.put(Integer.valueOf(level), Integer.valueOf(count));
/*     */   }
/*     */   
/*     */   public void setConsumes(int level, Map<Integer, Integer> consumes) {
/* 188 */     this.consumptions.put(Integer.valueOf(level), consumes);
/*     */   }
/*     */   
/*     */   public void addEquivExchange(int baseType, int altType, int basePerAlt) {
/* 192 */     ConsumeLevelEquivExchange ee = new ConsumeLevelEquivExchange();
/* 193 */     ee.baseType = baseType;
/* 194 */     ee.altType = altType;
/* 195 */     ee.basePerAlt = basePerAlt;
/* 196 */     this.exchanges.put(Integer.valueOf(baseType), ee);
/*     */   }
/*     */   
/*     */   public void removeEquivExchange(int baseType) {
/* 200 */     this.exchanges.remove(Integer.valueOf(baseType));
/*     */   }
/*     */   
/*     */   public void setSource(MultiInventory source) {
/* 204 */     this.source = source;
/*     */   }
/*     */   
/*     */   public int getConsumedAmount(int amount) {
/* 208 */     return (int)Math.max(1.0D, amount * this.consumeRate);
/*     */   }
/*     */   
/*     */   private boolean hasEnoughToConsume()
/*     */   {
/* 213 */     Map<Integer, Integer> thisLevelConsumptions = (Map)this.consumptions.get(Integer.valueOf(this.level));
/* 214 */     if (thisLevelConsumptions == null) {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     this.foundCounts = new HashMap();
/* 219 */     ItemStack[] arrayOfItemStack; int j = (arrayOfItemStack = this.source.getContents()).length; for (int i = 0; i < j; i++) { ItemStack stack = arrayOfItemStack[i];
/* 220 */       if (stack != null)
/*     */       {
/*     */ 
/*     */ 
/* 224 */         boolean isRequire = thisLevelConsumptions.containsKey(Integer.valueOf(ItemManager.getId(stack)));
/* 225 */         boolean isEquiv = false;
/*     */         
/* 227 */         for (ConsumeLevelEquivExchange ee : this.exchanges.values()) {
/* 228 */           if (ee.altType == ItemManager.getId(stack)) {
/* 229 */             isEquiv = true;
/* 230 */             break;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 235 */         if ((isRequire) || (isEquiv))
/*     */         {
/*     */ 
/*     */ 
/* 239 */           Integer count = (Integer)this.foundCounts.get(Integer.valueOf(ItemManager.getId(stack)));
/* 240 */           if (count == null) {
/* 241 */             count = Integer.valueOf(stack.getAmount());
/*     */           } else {
/* 243 */             count = Integer.valueOf(count.intValue() + stack.getAmount());
/*     */           }
/* 245 */           this.foundCounts.put(Integer.valueOf(ItemManager.getId(stack)), count);
/*     */         }
/*     */       } }
/* 248 */     boolean found = true;
/* 249 */     for (Integer typeID : thisLevelConsumptions.keySet()) {
/* 250 */       Integer foundCount = (Integer)this.foundCounts.get(typeID);
/* 251 */       Integer requireCount = (Integer)thisLevelConsumptions.get(typeID);
/* 252 */       ConsumeLevelEquivExchange ee = (ConsumeLevelEquivExchange)this.exchanges.get(typeID);
/*     */       
/* 254 */       if (foundCount == null) {
/* 255 */         foundCount = Integer.valueOf(0);
/*     */       }
/*     */       
/* 258 */       if (foundCount.intValue() < getConsumedAmount(requireCount.intValue())) {
/* 259 */         if (ee == null) {
/* 260 */           found = false;
/* 261 */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */         Integer totalBase = (Integer)this.foundCounts.get(Integer.valueOf(ee.baseType));
/* 269 */         if (totalBase == null) {
/* 270 */           totalBase = Integer.valueOf(0);
/*     */         }
/*     */         
/* 273 */         Integer totalAlt = (Integer)this.foundCounts.get(Integer.valueOf(ee.altType));
/* 274 */         if (totalAlt == null) {
/* 275 */           totalAlt = Integer.valueOf(0);
/*     */         }
/*     */         
/* 278 */         int total = totalBase.intValue() + totalAlt.intValue() * ee.basePerAlt;
/* 279 */         if (total < getConsumedAmount(requireCount.intValue())) {
/* 280 */           found = false;
/* 281 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 287 */     return found;
/*     */   }
/*     */   
/*     */   private void consumeFromInventory() {
/* 291 */     if (this.foundCounts == null) {
/* 292 */       return;
/*     */     }
/*     */     
/* 295 */     Map<Integer, Integer> thisLevelConsumptions = (Map)this.consumptions.get(Integer.valueOf(this.level));
/*     */     
/* 297 */     for (Integer typeID : thisLevelConsumptions.keySet()) {
/* 298 */       Integer amount = (Integer)thisLevelConsumptions.get(typeID);
/* 299 */       Integer count = (Integer)this.foundCounts.get(typeID);
/*     */       
/* 301 */       amount = Integer.valueOf(getConsumedAmount(amount.intValue()));
/*     */       
/* 303 */       if (count == null) {
/* 304 */         count = Integer.valueOf(0);
/*     */       }
/*     */       
/* 307 */       if (count.intValue() < amount.intValue())
/*     */       {
/* 309 */         ConsumeLevelEquivExchange ee = (ConsumeLevelEquivExchange)this.exchanges.get(typeID);
/* 310 */         if (ee == null)
/*     */         {
/* 312 */           CivLog.warning("Couldn't consume enough " + typeID + " and no EE was found!");
/*     */         }
/*     */         else
/*     */         {
/* 316 */           Integer totalBase = (Integer)this.foundCounts.get(Integer.valueOf(ee.baseType));
/* 317 */           if (totalBase == null) {
/* 318 */             totalBase = Integer.valueOf(0);
/*     */           }
/*     */           
/* 321 */           Integer totalAlt = (Integer)this.foundCounts.get(Integer.valueOf(ee.altType));
/* 322 */           if (totalAlt == null) {
/* 323 */             totalAlt = Integer.valueOf(0);
/*     */           }
/*     */           
/*     */ 
/* 327 */           int total = totalBase.intValue() + totalAlt.intValue() * ee.basePerAlt;
/* 328 */           if (total < amount.intValue())
/*     */           {
/* 330 */             CivLog.warning("Couldn't find a total big enough with EE!");
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 335 */             total -= amount.intValue();
/*     */             
/*     */ 
/* 338 */             int leftOverAlt = total / ee.basePerAlt;
/* 339 */             int leftOverBase = total % ee.basePerAlt;
/*     */             
/* 341 */             int totalAltConsumed = totalAlt.intValue() - leftOverAlt;
/* 342 */             int totalBaseConsumed = totalBase.intValue() - leftOverBase;
/*     */             try
/*     */             {
/* 345 */               this.source.removeItem(ee.altType, totalAltConsumed);
/*     */             } catch (CivException e) {
/* 347 */               e.printStackTrace();
/*     */             }
/* 349 */             if (totalBaseConsumed > 0) {
/*     */               try {
/* 351 */                 this.source.removeItem(ee.baseType, totalBaseConsumed);
/*     */               } catch (CivException e) {
/* 353 */                 e.printStackTrace();
/*     */               }
/*     */               
/* 356 */             } else if (totalBaseConsumed != 0)
/*     */             {
/* 358 */               this.source.addItem(ItemManager.createItemStack(ee.baseType, -1 * totalBaseConsumed));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/*     */         try {
/* 365 */           this.source.removeItem(typeID.intValue(), getConsumedAmount(amount.intValue()));
/*     */         } catch (CivException e) {
/* 367 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Result processConsumption()
/*     */   {
/* 375 */     Integer currentCountMax = (Integer)this.levelCounts.get(Integer.valueOf(this.level));
/* 376 */     if (currentCountMax == null) {
/* 377 */       CivLog.error("Couldn't get current level count, level was:" + this.level);
/* 378 */       this.lastResult = Result.UNKNOWN;
/* 379 */       return this.lastResult;
/*     */     }
/*     */     
/* 382 */     if (hasEnoughToConsume()) {
/* 383 */       consumeFromInventory();
/*     */       
/* 385 */       if (this.count + 1 >= currentCountMax.intValue())
/*     */       {
/* 387 */         Integer nextCountMax = (Integer)this.levelCounts.get(Integer.valueOf(this.level + 1));
/* 388 */         if (nextCountMax == null) {
/* 389 */           this.lastResult = Result.MAXED;
/* 390 */           return this.lastResult;
/*     */         }
/*     */         
/* 393 */         this.count = 0;
/* 394 */         if (hasEnoughToConsume())
/*     */         {
/* 396 */           this.level += 1;
/* 397 */           this.lastResult = Result.LEVELUP;
/* 398 */           return this.lastResult;
/*     */         }
/*     */         
/* 401 */         this.count = currentCountMax.intValue();
/* 402 */         this.lastResult = Result.MAXED;
/* 403 */         return this.lastResult;
/*     */       }
/*     */       
/*     */ 
/* 407 */       this.count += 1;
/* 408 */       this.lastResult = Result.GROW;
/* 409 */       return this.lastResult;
/*     */     }
/*     */     
/*     */ 
/* 413 */     if (this.count - 1 < 0)
/*     */     {
/* 415 */       Integer lastCountMax = (Integer)this.levelCounts.get(Integer.valueOf(this.level - 1));
/* 416 */       if (lastCountMax == null) {
/* 417 */         this.lastResult = Result.STAGNATE;
/* 418 */         return this.lastResult;
/*     */       }
/*     */       
/* 421 */       this.count = lastCountMax.intValue();
/* 422 */       this.level -= 1;
/* 423 */       this.lastResult = Result.LEVELDOWN;
/* 424 */       return this.lastResult;
/*     */     }
/*     */     
/*     */ 
/* 428 */     this.count -= 1;
/* 429 */     this.lastResult = Result.STARVE;
/* 430 */     return this.lastResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCountString()
/*     */   {
/* 438 */     String out = "(" + this.count + "/";
/* 439 */     Integer currentCountMax = (Integer)this.levelCounts.get(Integer.valueOf(this.level));
/* 440 */     if (currentCountMax != null) {
/* 441 */       out = out + currentCountMax + ")";
/*     */     } else {
/* 443 */       out = out + "?)";
/*     */     }
/*     */     
/* 446 */     return out;
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 450 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(int level) {
/* 454 */     this.level = level;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 458 */     return this.count;
/*     */   }
/*     */   
/*     */   public void setCount(int count) {
/* 462 */     this.count = count;
/*     */   }
/*     */   
/*     */   public double getConsumeRate() {
/* 466 */     return this.consumeRate;
/*     */   }
/*     */   
/*     */   public void setConsumeRate(double consumeRate) {
/* 470 */     this.consumeRate = consumeRate;
/*     */   }
/*     */   
/*     */   public Result getLastResult() {
/* 474 */     return this.lastResult;
/*     */   }
/*     */   
/*     */   public void clearEquivExchanges() {
/* 478 */     this.exchanges.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\components\ConsumeLevelComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */