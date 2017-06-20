/*     */ package com.avrgaming.civcraft.threading;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.util.BukkitObjects;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ import org.bukkit.scheduler.BukkitTask;
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
/*     */ public class TaskMaster
/*     */ {
/*  34 */   private static HashMap<String, BukkitTask> tasks = new HashMap();
/*  35 */   private static HashMap<String, BukkitTask> timers = new HashMap();
/*     */   
/*     */   public static long getTicksTilDate(Date date)
/*     */   {
/*  39 */     Calendar c = Calendar.getInstance();
/*     */     
/*  41 */     if (c.getTime().after(date)) {
/*  42 */       return 0L;
/*     */     }
/*     */     
/*  45 */     long timeInSeconds = (date.getTime() - c.getTime().getTime()) / 1000L;
/*  46 */     return timeInSeconds * 20L;
/*     */   }
/*     */   
/*     */   public static long getTicksToNextHour() {
/*  50 */     Calendar c = Calendar.getInstance();
/*  51 */     Date now = c.getTime();
/*     */     
/*  53 */     c.add(11, 1);
/*  54 */     c.set(12, 0);
/*  55 */     c.set(13, 0);
/*     */     
/*  57 */     Date nextHour = c.getTime();
/*     */     
/*  59 */     long timeInSeconds = (nextHour.getTime() - now.getTime()) / 1000L;
/*  60 */     return timeInSeconds * 20L;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void syncTask(Runnable runnable)
/*     */   {
/*  66 */     BukkitObjects.scheduleSyncDelayedTask(runnable, 0L);
/*     */   }
/*     */   
/*     */   public static void syncTask(Runnable runnable, long l) {
/*  70 */     BukkitObjects.scheduleSyncDelayedTask(runnable, l);
/*     */   }
/*     */   
/*     */   public static void asyncTimer(String name, Runnable runnable, long delay, long repeat)
/*     */   {
/*  75 */     addTimer(name, BukkitObjects.scheduleAsyncRepeatingTask(runnable, delay, repeat));
/*     */   }
/*     */   
/*     */   public static void asyncTimer(String name, Runnable runnable, long time) {
/*  79 */     addTimer(name, BukkitObjects.scheduleAsyncRepeatingTask(runnable, time, time));
/*     */   }
/*     */   
/*     */   public static void asyncTask(String name, Runnable runnable, long delay) {
/*  83 */     addTask(name, BukkitObjects.scheduleAsyncDelayedTask(runnable, delay));
/*     */   }
/*     */   
/*     */   public static void asyncTask(Runnable runnable, long delay) {
/*  87 */     BukkitObjects.scheduleAsyncDelayedTask(runnable, delay);
/*     */   }
/*     */   
/*     */   private static void addTimer(String name, BukkitTask timer) {
/*  91 */     timers.put(name, timer);
/*     */   }
/*     */   
/*     */   private static void addTask(String name, BukkitTask task)
/*     */   {
/*  96 */     tasks.put(name, task);
/*     */   }
/*     */   
/*     */   public static void stopAll() {
/* 100 */     stopAllTasks();
/* 101 */     stopAllTimers();
/*     */   }
/*     */   
/*     */   public static void stopAllTasks() {
/* 105 */     for (BukkitTask task : tasks.values()) {
/* 106 */       task.cancel();
/*     */     }
/* 108 */     tasks.clear();
/*     */   }
/*     */   
/*     */   public static void stopAllTimers() {
/* 112 */     for (BukkitTask timer : timers.values()) {
/* 113 */       timer.cancel();
/*     */     }
/*     */     
/*     */ 
/* 117 */     timers.clear();
/*     */   }
/*     */   
/*     */   public static void cancelTask(String name) {
/* 121 */     BukkitTask task = (BukkitTask)tasks.get(name);
/* 122 */     if (task != null) {
/* 123 */       task.cancel();
/*     */     }
/*     */     
/*     */ 
/* 127 */     tasks.remove(name);
/*     */   }
/*     */   
/*     */   public static void cancelTimer(String name) {
/* 131 */     BukkitTask timer = (BukkitTask)tasks.get(name);
/* 132 */     if (timer != null) {
/* 133 */       timer.cancel();
/*     */     }
/*     */     
/*     */ 
/* 137 */     timers.remove(name);
/*     */   }
/*     */   
/*     */   public static BukkitTask getTimer(String name) {
/* 141 */     return (BukkitTask)timers.get(name);
/*     */   }
/*     */   
/*     */   public static BukkitTask getTask(String name) {
/* 145 */     return (BukkitTask)tasks.get(name);
/*     */   }
/*     */   
/*     */   public static List<String> getTimersList() {
/* 149 */     List<String> out = new ArrayList();
/*     */     
/* 151 */     out.add(CivMessage.buildTitle("Timers Running"));
/* 152 */     for (String name : timers.keySet()) {
/* 153 */       out.add("Timer: " + name + " running.");
/*     */     }
/*     */     
/* 156 */     return out;
/*     */   }
/*     */   
/*     */   public static void syncTimer(String name, Runnable runnable, long time) {
/* 160 */     BukkitObjects.scheduleSyncRepeatingTask(runnable, time, time);
/*     */   }
/*     */   
/*     */   public static void syncTimer(String name, Runnable runnable, long delay, long repeat) {
/* 164 */     BukkitObjects.scheduleSyncRepeatingTask(runnable, delay, repeat);
/*     */   }
/*     */   
/*     */   public static boolean hasTask(String key)
/*     */   {
/* 169 */     BukkitTask task = (BukkitTask)tasks.get(key);
/*     */     
/* 171 */     if (task == null) {
/* 172 */       return false;
/*     */     }
/*     */     
/* 175 */     if ((BukkitObjects.getScheduler().isCurrentlyRunning(task.getTaskId())) || (BukkitObjects.getScheduler().isQueued(task.getTaskId()))) {
/* 176 */       return true;
/*     */     }
/*     */     
/* 179 */     tasks.remove(key);
/*     */     
/* 181 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\TaskMaster.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */