/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import org.bukkit.World;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuildAsyncTask
/*     */   extends CivAsyncTask
/*     */ {
/*     */   public Buildable buildable;
/*     */   public int speed;
/*     */   public int blocks_per_tick;
/*     */   public Template tpl;
/*     */   public Block centerBlock;
/*     */   private int count;
/*     */   private int extra_blocks;
/*     */   private int percent_complete;
/*     */   private Queue<SimpleBlock> sbs;
/*  59 */   public Boolean aborted = Boolean.valueOf(false);
/*     */   
/*     */   public Date lastSave;
/*  62 */   private final int SAVE_INTERVAL = 5000;
/*     */   
/*     */   public BuildAsyncTask(Buildable bld, Template t, int s, int blocks_per_tick, Block center) {
/*  65 */     this.buildable = bld;
/*  66 */     this.speed = s;
/*  67 */     this.tpl = t;
/*  68 */     this.centerBlock = center;
/*  69 */     this.blocks_per_tick = blocks_per_tick;
/*  70 */     this.percent_complete = 0;
/*  71 */     this.sbs = new LinkedList();
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  78 */       start();
/*     */     }
/*     */     catch (Exception e) {
/*  81 */       CivLog.exception("BuildAsyncTask town:" + this.buildable.getTown() + " struct:" + this.buildable.getDisplayName() + " template:" + this.tpl.dir(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean start()
/*     */   {
/*  87 */     this.lastSave = new Date();
/*  89 */     for (; 
/*  89 */         this.buildable.getBuiltBlockCount() < this.tpl.size_x * this.tpl.size_y * this.tpl.size_z; this.buildable.builtBlockCount += 1) {
/*  90 */       this.speed = this.buildable.getBuildSpeed();
/*  91 */       this.blocks_per_tick = this.buildable.getBlocksPerTick();
/*     */       
/*  93 */       synchronized (this.aborted) {
/*  94 */         if (this.aborted.booleanValue()) {
/*  95 */           return this.aborted.booleanValue();
/*     */         }
/*     */       }
/*     */       
/*  99 */       if (this.buildable.isComplete()) {
/*     */         break;
/*     */       }
/*     */       
/*     */ 
/* 104 */       if ((this.buildable instanceof Wonder)) {
/* 105 */         if (this.buildable.getTown().getMotherCiv() != null) {
/* 106 */           CivMessage.sendTown(this.buildable.getTown(), "Wonder production halted while we're conquered by " + this.buildable.getTown().getCiv().getName());
/*     */           try {
/* 108 */             Thread.sleep(1800000L);
/*     */           } catch (InterruptedException e) {
/* 110 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         
/* 114 */         Buildable inProgress = this.buildable.getTown().getCurrentStructureInProgress();
/* 115 */         if ((inProgress != null) && (inProgress != this.buildable)) {
/* 116 */           CivMessage.sendTown(this.buildable.getTown(), "Wonder production halted while we're constructing a " + inProgress.getDisplayName());
/*     */           try {
/* 118 */             Thread.sleep(600000L);
/*     */           } catch (InterruptedException e) {
/* 120 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         
/* 124 */         if (this.buildable.getTown().getTownHall() == null) {
/* 125 */           CivMessage.sendTown(this.buildable.getTown(), "Wonder production halted while you have no town hall.");
/*     */           try {
/* 127 */             Thread.sleep(600000L);
/*     */           } catch (InterruptedException e) {
/* 129 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 134 */       if (!build())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 139 */         Date now = new Date();
/* 140 */         if (now.getTime() > this.lastSave.getTime() + 5000L) {
/* 141 */           this.buildable.updateBuildProgess();
/* 142 */           this.lastSave = now;
/*     */         }
/*     */         
/* 145 */         this.count = 0;
/*     */         
/* 147 */         synchronized (this.aborted) {
/* 148 */           if (!this.aborted.booleanValue()) {
/* 149 */             updateBlocksQueue(this.sbs);
/* 150 */             this.sbs.clear();
/*     */           } else {
/* 152 */             return this.aborted.booleanValue();
/*     */           }
/*     */         }
/*     */         try
/*     */         {
/* 157 */           int nextPercentComplete = (int)(this.buildable.getBuiltBlockCount() / this.buildable.getTotalBlockCount() * 100.0D);
/* 158 */           if (nextPercentComplete > this.percent_complete) {
/* 159 */             this.percent_complete = nextPercentComplete;
/* 160 */             if (this.percent_complete % 10 == 0) {
/* 161 */               if ((this.buildable instanceof Wonder)) {
/* 162 */                 CivMessage.global(this.buildable.getDisplayName() + " in " + this.buildable.getTown().getName() + " is " + nextPercentComplete + "% complete.");
/*     */               }
/*     */               else {
/* 165 */                 CivMessage.sendTown(this.buildable.getTown(), 
/* 166 */                   "§eThe " + this.buildable.getDisplayName() + " is now " + nextPercentComplete + "% complete.");
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 171 */           int timeleft = this.speed;
/* 172 */           while (timeleft > 0) {
/* 173 */             int min = Math.min(10000, timeleft);
/* 174 */             Thread.sleep(min);
/* 175 */             timeleft -= 10000;
/*     */             
/*     */ 
/* 178 */             int newSpeed = this.buildable.getBuildSpeed();
/* 179 */             if (newSpeed != this.speed) {
/* 180 */               this.speed = newSpeed;
/* 181 */               timeleft = newSpeed;
/*     */             }
/*     */           }
/*     */           
/* 185 */           if ((this.buildable instanceof Wonder)) {
/* 186 */             if (checkOtherWonderAlreadyBuilt()) {
/* 187 */               processWonderAbort();
/* 188 */               return false;
/*     */             }
/*     */             
/* 191 */             if (this.buildable.isDestroyed()) {
/* 192 */               CivMessage.sendTown(this.buildable.getTown(), this.buildable.getDisplayName() + " was destroyed while it was building!");
/* 193 */               abortWonder();
/* 194 */               return false;
/*     */             }
/*     */             
/* 197 */             if (this.buildable.getTown().getMotherCiv() == null) {}
/*     */           }
/*     */         }
/*     */         catch (InterruptedException localInterruptedException1) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */     if (this.sbs.size() > 0) {
/* 209 */       updateBlocksQueue(this.sbs);
/* 210 */       this.sbs.clear();
/*     */     }
/*     */     
/* 213 */     if (((this.buildable instanceof Wonder)) && 
/* 214 */       (checkOtherWonderAlreadyBuilt())) {
/* 215 */       processWonderAbort();
/* 216 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 220 */     this.buildable.setComplete(true);
/* 221 */     if ((this.buildable instanceof Wonder)) {
/* 222 */       this.buildable.getTown().setCurrentWonderInProgress(null);
/*     */     } else {
/* 224 */       this.buildable.getTown().setCurrentStructureInProgress(null);
/*     */     }
/* 226 */     this.buildable.savedBlockCount = this.buildable.builtBlockCount;
/* 227 */     this.buildable.updateBuildProgess();
/* 228 */     this.buildable.save();
/*     */     
/* 230 */     this.tpl.deleteInProgessTemplate(this.buildable.getCorner().toString(), this.buildable.getTown());
/* 231 */     this.buildable.getTown().build_tasks.remove(this);
/* 232 */     TaskMaster.syncTask(new PostBuildSyncTask(this.tpl, this.buildable));
/* 233 */     CivMessage.global("The town of " + this.buildable.getTown().getName() + " has completed a " + this.buildable.getDisplayName() + "!");
/* 234 */     this.buildable.onComplete();
/* 235 */     return false;
/*     */   }
/*     */   
/*     */   public boolean build()
/*     */   {
/* 240 */     boolean skipToNext = false;
/*     */     
/*     */ 
/* 243 */     if (this.extra_blocks > 0)
/* 244 */       synchronized (this) {
/* 245 */         this.extra_blocks -= 1;
/* 246 */         skipToNext = true;
/*     */       }
/* 248 */     if (this.count < this.blocks_per_tick) {
/* 249 */       this.count += 1;
/* 250 */       skipToNext = true;
/*     */     }
/*     */     
/*     */ 
/* 254 */     int y = this.buildable.getBuiltBlockCount() / (this.tpl.size_x * this.tpl.size_z);
/*     */     
/* 256 */     int z = this.buildable.getBuiltBlockCount() / this.tpl.size_x % this.tpl.size_z;
/* 257 */     int x = this.buildable.getBuiltBlockCount() % this.tpl.size_x;
/*     */     
/* 259 */     SimpleBlock sb = this.tpl.blocks[x][y][z];
/*     */     
/*     */ 
/* 262 */     sb.x = (x + this.centerBlock.getX());
/* 263 */     sb.y = (y + this.centerBlock.getY());
/* 264 */     sb.z = (z + this.centerBlock.getZ());
/* 265 */     sb.worldname = this.centerBlock.getWorld().getName();
/* 266 */     sb.buildable = this.buildable;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 274 */     synchronized (this.aborted) {
/* 275 */       if (!this.aborted.booleanValue()) {
/* 276 */         if ((sb.getType() != 64) && (sb.getType() != 71))
/*     */         {
/*     */ 
/*     */ 
/* 280 */           this.sbs.add(sb);
/*     */         }
/*     */         
/* 283 */         if ((!this.buildable.isDestroyable()) && (sb.getType() != 0) && 
/* 284 */           (sb.specialType != SimpleBlock.Type.COMMAND)) {
/* 285 */           BlockCoord coord = new BlockCoord(sb.worldname, sb.x, sb.y, sb.z);
/* 286 */           if (sb.y == 0) {
/* 287 */             this.buildable.addStructureBlock(coord, false);
/*     */           } else {
/* 289 */             this.buildable.addStructureBlock(coord, true);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 294 */         this.sbs.clear();
/* 295 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 299 */     return skipToNext;
/*     */   }
/*     */   
/*     */   private boolean checkOtherWonderAlreadyBuilt()
/*     */   {
/* 304 */     if (this.buildable.isComplete()) {
/* 305 */       return false;
/*     */     }
/*     */     
/* 308 */     return !Wonder.isWonderAvailable(this.buildable.getConfigId());
/*     */   }
/*     */   
/*     */   private void processWonderAbort() {
/* 312 */     CivMessage.sendTown(this.buildable.getTown(), "§cYou can no longer build " + this.buildable.getDisplayName() + " since it was built in a far away land.");
/*     */     
/*     */ 
/* 315 */     double refund = (int)(this.buildable.getCost() / 2.0D);
/* 316 */     this.buildable.getTown().depositDirect(refund);
/*     */     
/* 318 */     CivMessage.sendTown(this.buildable.getTown(), "§eTown was refunded 50% (" + refund + " coins) of the cost to build the wonder.");
/* 319 */     abortWonder();
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
/*     */   private void abortWonder()
/*     */   {
/* 346 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 328 */         BuildAsyncTask.this.buildable.getTown().build_tasks.remove(this);
/* 329 */         BuildAsyncTask.this.buildable.unbindStructureBlocks();
/*     */         
/*     */ 
/* 332 */         synchronized (BuildAsyncTask.this.buildable.getTown())
/*     */         {
/* 334 */           BuildAsyncTask.this.buildable.getTown().removeWonder(BuildAsyncTask.this.buildable);
/*     */         }
/*     */         
/*     */ 
/* 338 */         BuildAsyncTask.this.tpl.removeScaffolding(BuildAsyncTask.this.buildable.getCorner().getLocation());
/*     */         try {
/* 340 */           ((Wonder)BuildAsyncTask.this.buildable).delete();
/*     */         } catch (SQLException e) {
/* 342 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double setExtraHammers(double extra_hammers)
/*     */   {
/* 352 */     double leftover_hammers = 0.0D;
/*     */     
/* 354 */     synchronized (this) {
/* 355 */       this.extra_blocks = ((int)(this.buildable.getBlocksPerHammer() * extra_hammers));
/* 356 */       int blocks_left = this.buildable.getTotalBlockCount() - this.buildable.getBuiltBlockCount();
/* 357 */       if (this.extra_blocks > blocks_left) {
/* 358 */         leftover_hammers = (this.extra_blocks - blocks_left) / this.buildable.getBlocksPerHammer();
/*     */       }
/*     */     }
/* 361 */     return leftover_hammers;
/*     */   }
/*     */   
/*     */   public void abort() {
/* 365 */     synchronized (this.aborted) {
/* 366 */       this.aborted = Boolean.valueOf(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\BuildAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */