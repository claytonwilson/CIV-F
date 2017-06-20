/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
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
/*     */ public class Farm
/*     */   extends Structure
/*     */ {
/*  43 */   public static final long GROW_RATE = CivSettings.getIntegerStructure("farm.grow_tick_rate");
/*     */   
/*     */   public static final int CROP_GROW_LIGHT_LEVEL = 9;
/*     */   public static final int MUSHROOM_GROW_LIGHT_LEVEL = 12;
/*     */   public static final int MAX_SUGARCANE_HEIGHT = 3;
/*  48 */   private FarmChunk fc = null;
/*  49 */   private double lastEffectiveGrowthRate = 0.0D;
/*     */   
/*     */   protected Farm(Location center, String id, Town town) throws CivException {
/*  52 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public Farm(ResultSet rs) throws SQLException, CivException {
/*  56 */     super(rs);
/*  57 */     build_farm(getCorner().getLocation());
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/*  62 */     if (getCorner() != null) {
/*  63 */       ChunkCoord coord = new ChunkCoord(getCorner().getLocation());
/*  64 */       CivGlobal.removeFarmChunk(coord);
/*  65 */       CivGlobal.getSessionDB().delete_all(getSessionKey());
/*     */     }
/*  67 */     super.delete();
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/*  72 */     return null;
/*     */   }
/*     */   
/*     */   public boolean canRestoreFromTemplate()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/*  82 */     return "basket";
/*     */   }
/*     */   
/*     */   public void build_farm(Location centerLoc)
/*     */   {
/*  87 */     Chunk chunk = centerLoc.getChunk();
/*  88 */     FarmChunk fc = new FarmChunk(chunk, getTown(), this);
/*  89 */     CivGlobal.addFarmChunk(fc.getCoord(), fc);
/*  90 */     this.fc = fc;
/*     */   }
/*     */   
/*     */   public static boolean isBlockControlled(Block b)
/*     */   {
/*  95 */     switch (ItemManager.getId(b))
/*     */     {
/*     */ 
/*     */ 
/*     */     case 59: 
/*     */     case 86: 
/*     */     case 103: 
/*     */     case 104: 
/*     */     case 105: 
/*     */     case 115: 
/*     */     case 127: 
/*     */     case 141: 
/*     */     case 142: 
/* 108 */       return true;
/*     */     }
/*     */     
/* 111 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveMissedGrowths()
/*     */   {
/* 146 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       Farm farm;
/*     */       int missedTicks;
/*     */       
/*     */       public void run()
/*     */       {
/* 128 */         ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(Farm.this.getSessionKey());
/*     */         
/* 130 */         if (((entries == null) || (entries.size() == 0)) && 
/* 131 */           (this.missedTicks > 0)) {
/* 132 */           this.farm.sessionAdd(Farm.this.getSessionKey(), this.missedTicks);
/* 133 */           return;
/*     */         }
/*     */         
/*     */ 
/* 137 */         if (this.missedTicks == 0) {
/* 138 */           CivGlobal.getSessionDB().delete_all(Farm.this.getSessionKey());
/*     */         } else {
/* 140 */           CivGlobal.getSessionDB().update(((SessionEntry)entries.get(0)).request_id, Farm.this.getSessionKey(), this.missedTicks);
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/* 146 */     }, 0L);
/*     */   }
/*     */   
/*     */   public String getSessionKey() {
/* 150 */     return "FarmMissedGrowth:" + getCorner().toString();
/*     */   }
/*     */   
/*     */   public void onLoad()
/*     */   {
/* 155 */     ArrayList<SessionEntry> entries = new ArrayList();
/* 156 */     entries = CivGlobal.getSessionDB().lookup(getSessionKey());
/* 157 */     int missedGrowths = 0;
/*     */     
/* 159 */     if (entries.size() > 0) {
/* 160 */       missedGrowths = Integer.valueOf(((SessionEntry)entries.get(0)).value).intValue();
/*     */     }
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
/* 179 */     TaskMaster.asyncTask(new CivAsyncTask()
/*     */     {
/*     */       int missedGrowths;
/*     */       
/*     */       public void run()
/*     */       {
/* 173 */         Farm.this.fc.setMissedGrowthTicks(this.missedGrowths);
/* 174 */         Farm.this.fc.processMissedGrowths(true, this);
/* 175 */         Farm.this.saveMissedGrowths();
/*     */       }
/*     */       
/*     */ 
/* 179 */     }, 0L);
/*     */   }
/*     */   
/*     */   public void setLastEffectiveGrowth(double effectiveGrowthRate) {
/* 183 */     this.lastEffectiveGrowthRate = effectiveGrowthRate;
/*     */   }
/*     */   
/*     */   public double getLastEffectiveGrowthRate() {
/* 187 */     return this.lastEffectiveGrowthRate;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Farm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */