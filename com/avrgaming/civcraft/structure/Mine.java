/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.AttributeBiomeRadiusPerLevel;
/*     */ import com.avrgaming.civcraft.components.ConsumeLevelComponent;
/*     */ import com.avrgaming.civcraft.components.ConsumeLevelComponent.Result;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigMineLevel;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.CivTaskAbortException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.BuffManager;
/*     */ import com.avrgaming.civcraft.object.StructureChest;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.MultiInventory;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ public class Mine
/*     */   extends Structure
/*     */ {
/*  45 */   private ConsumeLevelComponent consumeComp = null;
/*     */   
/*     */   protected Mine(Location center, String id, Town town) throws CivException {
/*  48 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public Mine(ResultSet rs) throws SQLException, CivException {
/*  52 */     super(rs);
/*     */   }
/*     */   
/*     */   public void loadSettings()
/*     */   {
/*  57 */     super.loadSettings();
/*     */   }
/*     */   
/*     */   public String getkey() {
/*  61 */     return getTown().getName() + "_" + getConfigId() + "_" + getCorner().toString();
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/*  66 */     return null;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/*  71 */     return "hammer";
/*     */   }
/*     */   
/*     */   public ConsumeLevelComponent getConsumeComponent() {
/*  75 */     if (this.consumeComp == null) {
/*  76 */       this.consumeComp = ((ConsumeLevelComponent)getComponent(ConsumeLevelComponent.class.getSimpleName()));
/*     */     }
/*  78 */     return this.consumeComp;
/*     */   }
/*     */   
/*     */   public ConsumeLevelComponent.Result consume(CivAsyncTask task)
/*     */     throws InterruptedException
/*     */   {
/*  84 */     if (getChests().size() == 0) {
/*  85 */       return ConsumeLevelComponent.Result.STAGNATE;
/*     */     }
/*  87 */     MultiInventory multiInv = new MultiInventory();
/*     */     
/*  89 */     ArrayList<StructureChest> chests = getAllChestsById(0);
/*     */     
/*     */ 
/*  92 */     for (StructureChest c : chests) {
/*  93 */       task.syncLoadChunk(c.getCoord().getWorldname(), c.getCoord().getX(), c.getCoord().getZ());
/*     */       try
/*     */       {
/*  96 */         tmp = task.getChestInventory(c.getCoord().getWorldname(), c.getCoord().getX(), c.getCoord().getY(), c.getCoord().getZ(), true);
/*     */       } catch (CivTaskAbortException e) { Inventory tmp;
/*  98 */         return ConsumeLevelComponent.Result.STAGNATE; }
/*     */       Inventory tmp;
/* 100 */       multiInv.addInventory(tmp);
/*     */     }
/* 102 */     getConsumeComponent().setSource(multiInv);
/* 103 */     getConsumeComponent().setConsumeRate(1.0D);
/* 104 */     ConsumeLevelComponent.Result result = getConsumeComponent().processConsumption();
/* 105 */     getConsumeComponent().onSave();
/* 106 */     return result;
/*     */   }
/*     */   
/*     */   public void process_mine(CivAsyncTask task) throws InterruptedException {
/* 110 */     ConsumeLevelComponent.Result result = consume(task);
/* 111 */     switch (result) {
/*     */     case LEVELUP: 
/* 113 */       CivMessage.sendTown(getTown(), "§aA level " + getConsumeComponent().getLevel() + " mine's production " + 
/* 114 */         "§c" + "fell. " + "§a" + getConsumeComponent().getCountString());
/* 115 */       break;
/*     */     case STAGNATE: 
/* 117 */       CivMessage.sendTown(getTown(), "§aA mine ran out of redstone and §clost§a a level. It is now level " + 
/* 118 */         getConsumeComponent().getLevel());
/* 119 */       break;
/*     */     case GROW: 
/* 121 */       CivMessage.sendTown(getTown(), "§aA level " + 
/* 122 */         getConsumeComponent().getLevel() + " mine " + "§e" + "stagnated " + "§a" + getConsumeComponent().getCountString());
/* 123 */       break;
/*     */     case LEVELDOWN: 
/* 125 */       CivMessage.sendTown(getTown(), "§aA level " + getConsumeComponent().getLevel() + " mine's production " + 
/* 126 */         "§2" + "rose. " + "§a" + getConsumeComponent().getCountString());
/* 127 */       break;
/*     */     case MAXED: 
/* 129 */       CivMessage.sendTown(getTown(), "§aA mine §2gained§a a level. It is now level " + 
/* 130 */         getConsumeComponent().getLevel());
/* 131 */       break;
/*     */     case STARVE: 
/* 133 */       CivMessage.sendTown(getTown(), "§aA level " + getConsumeComponent().getLevel() + " mine is " + 
/* 134 */         "§2" + "maxed. " + "§a" + getConsumeComponent().getCountString());
/* 135 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public int getLevel()
/*     */   {
/* 142 */     return getConsumeComponent().getLevel();
/*     */   }
/*     */   
/*     */   public double getHammersPerTile() {
/* 146 */     AttributeBiomeRadiusPerLevel attrBiome = (AttributeBiomeRadiusPerLevel)getComponent("AttributeBiomeRadiusPerLevel");
/* 147 */     double base = attrBiome.getBaseValue();
/*     */     
/* 149 */     double rate = 1.0D;
/* 150 */     rate += getTown().getBuffManager().getEffectiveDouble("buff_advanced_tooling");
/* 151 */     return rate * base;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 155 */     return getConsumeComponent().getCount();
/*     */   }
/*     */   
/*     */   public int getMaxCount() {
/* 159 */     int level = getLevel();
/*     */     
/* 161 */     ConfigMineLevel lvl = (ConfigMineLevel)CivSettings.mineLevels.get(Integer.valueOf(level));
/* 162 */     return lvl.count;
/*     */   }
/*     */   
/*     */   public ConsumeLevelComponent.Result getLastResult() {
/* 166 */     return getConsumeComponent().getLastResult();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Mine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */