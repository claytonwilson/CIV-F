/*    */ package com.avrgaming.civcraft.event;
/*    */ 
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.CultureChunk;
/*    */ import com.avrgaming.civcraft.object.Town;
/*    */ import com.avrgaming.civcraft.object.TownChunk;
/*    */ import com.avrgaming.civcraft.threading.TaskMaster;
/*    */ import com.avrgaming.civcraft.util.ChunkCoord;
/*    */ import java.util.Calendar;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SpawnRegenEvent
/*    */   implements EventInterface
/*    */ {
/*    */   public void process()
/*    */   {
/* 39 */     CivLog.info("TimerEvent: SpawnRegenEvent -------------------------------------");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 55 */     int tickDelay = 0;
/* 56 */     for (Civilization civ : CivGlobal.getAdminCivs()) {
/* 57 */       if (civ.isAdminCiv()) { Iterator localIterator3;
/* 58 */         for (Iterator localIterator2 = civ.getTowns().iterator(); localIterator2.hasNext(); 
/* 59 */             localIterator3.hasNext())
/*    */         {
/* 58 */           Town town = (Town)localIterator2.next();
/* 59 */           localIterator3 = town.getCultureChunks().iterator(); continue;CultureChunk cc = (CultureChunk)localIterator3.next();
/* 60 */           TownChunk tc = CivGlobal.getTownChunk(cc.getChunkCoord());
/* 61 */           if (tc == null) {
/* 62 */             TaskMaster.syncTask(new Runnable()
/*    */             {
/*    */               CultureChunk cc;
/*    */               
/*    */               public void run()
/*    */               {
/* 51 */                 Bukkit.getWorld("world").regenerateChunk(this.cc.getChunkCoord().getX(), this.cc.getChunkCoord().getZ());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */               }
/*    */               
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 62 */             }, tickDelay);
/* 63 */             tickDelay++;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Calendar getNextDate()
/*    */     throws InvalidConfiguration
/*    */   {
/* 75 */     Calendar cal = EventTimer.getCalendarInServerTimeZone();
/* 76 */     int regen_hour = CivSettings.getInteger(CivSettings.civConfig, "global.regen_spawn_hour").intValue();
/* 77 */     cal.set(13, 0);
/* 78 */     cal.set(12, 0);
/* 79 */     cal.add(11, regen_hour);
/*    */     
/* 81 */     Calendar now = Calendar.getInstance();
/* 82 */     if (now.after(cal)) {
/* 83 */       cal.add(5, 1);
/* 84 */       cal.set(11, regen_hour);
/* 85 */       cal.set(12, 0);
/* 86 */       cal.set(13, 0);
/*    */     }
/*    */     
/* 89 */     return cal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\SpawnRegenEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */