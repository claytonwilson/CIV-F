/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivCraft;
/*    */ import java.util.List;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitScheduler;
/*    */ import org.bukkit.scheduler.BukkitTask;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BukkitObjects
/*    */ {
/* 33 */   private static CivCraft plugin = null;
/* 34 */   private static Server server = null;
/*    */   
/*    */   public static void initialize(CivCraft plugin)
/*    */   {
/* 38 */     plugin = plugin;
/* 39 */     server = plugin.getServer();
/*    */   }
/*    */   
/*    */   public static Player[] getOnlinePlayers() {
/* 43 */     return getServer().getOnlinePlayers();
/*    */   }
/*    */   
/*    */   public static List<Player> matchPlayer(String name) {
/* 47 */     return getServer().matchPlayer(name);
/*    */   }
/*    */   
/*    */   public static Player getPlayerExact(String name) {
/* 51 */     return getServer().getPlayerExact(name);
/*    */   }
/*    */   
/*    */   public static Player getPlayer(String name) {
/* 55 */     return getServer().getPlayer(name);
/*    */   }
/*    */   
/*    */   public static List<World> getWorlds() {
/* 59 */     return getServer().getWorlds();
/*    */   }
/*    */   
/*    */   public static World getWorld(String name) {
/* 63 */     return getServer().getWorld(name);
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public static Server getServer()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: getstatic 14	com/avrgaming/civcraft/util/BukkitObjects:server	Lorg/bukkit/Server;
/*    */     //   3: dup
/*    */     //   4: astore_0
/*    */     //   5: monitorenter
/*    */     //   6: getstatic 14	com/avrgaming/civcraft/util/BukkitObjects:server	Lorg/bukkit/Server;
/*    */     //   9: aload_0
/*    */     //   10: monitorexit
/*    */     //   11: areturn
/*    */     //   12: aload_0
/*    */     //   13: monitorexit
/*    */     //   14: athrow
/*    */     // Line number table:
/*    */     //   Java source line #67	-> byte code offset #0
/*    */     //   Java source line #68	-> byte code offset #6
/*    */     //   Java source line #67	-> byte code offset #12
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   4	9	0	Ljava/lang/Object;	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   6	11	12	finally
/*    */     //   12	14	12	finally
/*    */   }
/*    */   
/*    */   public static BukkitScheduler getScheduler()
/*    */   {
/* 73 */     return getServer().getScheduler();
/*    */   }
/*    */   
/*    */   public static int scheduleSyncDelayedTask(Runnable task, long delay) {
/* 77 */     return getScheduler().scheduleSyncDelayedTask(plugin, task, delay);
/*    */   }
/*    */   
/*    */   public static BukkitTask scheduleAsyncDelayedTask(Runnable task, long delay) {
/* 81 */     return getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
/*    */   }
/*    */   
/*    */   public static int scheduleSyncRepeatingTask(Runnable task, long delay, long repeat) {
/* 85 */     return getScheduler().scheduleSyncRepeatingTask(plugin, task, delay, repeat);
/*    */   }
/*    */   
/*    */   public static BukkitTask scheduleAsyncRepeatingTask(Runnable task, long delay, long repeat) {
/* 89 */     return getScheduler().runTaskTimerAsynchronously(plugin, task, delay, repeat);
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\BukkitObjects.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */