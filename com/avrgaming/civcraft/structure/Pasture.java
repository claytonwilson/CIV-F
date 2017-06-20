/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*     */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.LoadPastureEntityTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class Pasture
/*     */   extends Structure
/*     */ {
/*  34 */   public static Map<ChunkCoord, Pasture> pastureChunks = new ConcurrentHashMap();
/*  35 */   public static Map<UUID, Pasture> pastureEntities = new ConcurrentHashMap();
/*     */   
/*     */ 
/*  38 */   public HashSet<ChunkCoord> chunks = new HashSet();
/*  39 */   public HashSet<UUID> entities = new HashSet();
/*  40 */   public ReentrantLock lock = new ReentrantLock();
/*     */   
/*  42 */   private int pendingBreeds = 0;
/*     */   
/*     */   protected Pasture(Location center, String id, Town town) throws CivException
/*     */   {
/*  46 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   public Pasture(ResultSet rs) throws SQLException, CivException {
/*  50 */     super(rs);
/*     */   }
/*     */   
/*     */   public int getMobCount() {
/*  54 */     return this.entities.size();
/*     */   }
/*     */   
/*     */   public int getMobMax()
/*     */   {
/*     */     try {
/*  60 */       max = CivSettings.getInteger(CivSettings.structureConfig, "pasture.max_mobs").intValue();
/*     */     } catch (InvalidConfiguration e) { int max;
/*  62 */       e.printStackTrace();
/*  63 */       return 0; }
/*     */     int max;
/*  65 */     return max;
/*     */   }
/*     */   
/*     */   public boolean processMobBreed(Player player, EntityType type)
/*     */   {
/*  70 */     if (!isActive()) {
/*  71 */       CivMessage.sendError(player, "Pasture is destroyed or currently building. Cannot breed yet.");
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     if (getMobCount() >= getMobMax()) {
/*  76 */       CivMessage.sendError(player, "Pasture is the maximum number of mobs that it can support. Slaughter some before you breed.");
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (getPendingBreeds() + getMobCount() >= getMobMax()) {
/*  81 */       CivMessage.sendError(player, "Pasture has too many breed events pending. Pasture is probably at the maximum number of mobs it can support. Slaughter some before you breed.");
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     return true;
/*     */   }
/*     */   
/*     */   public void bindPastureChunks() {
/*  89 */     for (BlockCoord bcoord : this.structureBlocks.keySet()) {
/*  90 */       ChunkCoord coord = new ChunkCoord(bcoord);
/*  91 */       this.chunks.add(coord);
/*  92 */       pastureChunks.put(coord, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void unbindPastureChunks() {
/*  97 */     for (ChunkCoord coord : this.chunks) {
/*  98 */       pastureChunks.remove(coord);
/*     */     }
/*     */     
/* 101 */     this.entities.clear();
/* 102 */     this.chunks.clear();
/*     */     
/* 104 */     LinkedList<UUID> removeUs = new LinkedList();
/* 105 */     for (UUID id : pastureEntities.keySet()) {
/* 106 */       Pasture pasture = (Pasture)pastureEntities.get(id);
/* 107 */       if (pasture == this) {
/* 108 */         removeUs.add(id);
/*     */       }
/*     */     }
/*     */     
/* 112 */     for (UUID id : removeUs) {
/* 113 */       pastureEntities.remove(id);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onComplete()
/*     */   {
/* 120 */     bindPastureChunks();
/*     */   }
/*     */   
/*     */   public void onLoad() throws CivException
/*     */   {
/* 125 */     bindPastureChunks();
/* 126 */     loadEntities();
/*     */   }
/*     */   
/*     */   public void delete() throws SQLException
/*     */   {
/* 131 */     super.delete();
/* 132 */     unbindPastureChunks();
/* 133 */     clearEntities();
/*     */   }
/*     */   
/*     */ 
/*     */   public void clearEntities() {}
/*     */   
/*     */   public void onBreed(LivingEntity entity)
/*     */   {
/* 141 */     saveEntity(entity.getWorld().getName(), entity.getUniqueId());
/* 142 */     setPendingBreeds(getPendingBreeds() - 1);
/*     */   }
/*     */   
/*     */   public String getEntityKey() {
/* 146 */     return "pasture:" + getId();
/*     */   }
/*     */   
/*     */   public String getValue(String worldName, UUID id) {
/* 150 */     return worldName + ":" + id;
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
/*     */   public void saveEntity(String worldName, UUID id)
/*     */   {
/* 178 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       Pasture pasture;
/*     */       UUID id;
/*     */       String worldName;
/*     */       
/*     */       /* Error */
/*     */       public void run()
/*     */       {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: getfield 22	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   4: aload_0
/*     */         //   5: getfield 17	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   8: invokevirtual 33	com/avrgaming/civcraft/structure/Pasture:getEntityKey	()Ljava/lang/String;
/*     */         //   11: aload_0
/*     */         //   12: getfield 17	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   15: aload_0
/*     */         //   16: getfield 26	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:worldName	Ljava/lang/String;
/*     */         //   19: aload_0
/*     */         //   20: getfield 24	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:id	Ljava/util/UUID;
/*     */         //   23: invokevirtual 39	com/avrgaming/civcraft/structure/Pasture:getValue	(Ljava/lang/String;Ljava/util/UUID;)Ljava/lang/String;
/*     */         //   26: invokevirtual 43	com/avrgaming/civcraft/structure/Pasture:sessionAdd	(Ljava/lang/String;Ljava/lang/String;)V
/*     */         //   29: aload_0
/*     */         //   30: getfield 17	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   33: getfield 47	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */         //   36: invokevirtual 51	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */         //   39: aload_0
/*     */         //   40: getfield 17	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   43: getfield 55	com/avrgaming/civcraft/structure/Pasture:entities	Ljava/util/HashSet;
/*     */         //   46: aload_0
/*     */         //   47: getfield 24	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:id	Ljava/util/UUID;
/*     */         //   50: invokevirtual 59	java/util/HashSet:add	(Ljava/lang/Object;)Z
/*     */         //   53: pop
/*     */         //   54: getstatic 65	com/avrgaming/civcraft/structure/Pasture:pastureEntities	Ljava/util/Map;
/*     */         //   57: aload_0
/*     */         //   58: getfield 24	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:id	Ljava/util/UUID;
/*     */         //   61: aload_0
/*     */         //   62: getfield 22	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:pasture	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   65: invokeinterface 69 3 0
/*     */         //   70: pop
/*     */         //   71: goto +16 -> 87
/*     */         //   74: astore_1
/*     */         //   75: aload_0
/*     */         //   76: getfield 17	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   79: getfield 47	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */         //   82: invokevirtual 75	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */         //   85: aload_1
/*     */         //   86: athrow
/*     */         //   87: aload_0
/*     */         //   88: getfield 17	com/avrgaming/civcraft/structure/Pasture$1AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   91: getfield 47	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */         //   94: invokevirtual 75	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */         //   97: return
/*     */         // Line number table:
/*     */         //   Java source line #167	-> byte code offset #0
/*     */         //   Java source line #168	-> byte code offset #29
/*     */         //   Java source line #170	-> byte code offset #39
/*     */         //   Java source line #171	-> byte code offset #54
/*     */         //   Java source line #172	-> byte code offset #71
/*     */         //   Java source line #173	-> byte code offset #75
/*     */         //   Java source line #174	-> byte code offset #85
/*     */         //   Java source line #173	-> byte code offset #87
/*     */         //   Java source line #175	-> byte code offset #97
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	98	0	this	1AsyncTask
/*     */         //   74	12	1	localObject	Object
/*     */         // Exception table:
/*     */         //   from	to	target	type
/*     */         //   39	74	74	finally
/*     */       }
/* 178 */     }, 0L);
/*     */   }
/*     */   
/*     */   public void loadEntities() {
/* 182 */     Queue<SessionEntry> entriesToLoad = new LinkedList();
/* 183 */     ArrayList<SessionEntry> entries = CivGlobal.getSessionDB().lookup(getEntityKey());
/* 184 */     entriesToLoad.addAll(entries);
/* 185 */     TaskMaster.syncTask(new LoadPastureEntityTask(entriesToLoad, this));
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
/*     */   public void onEntityDeath(LivingEntity entity)
/*     */   {
/* 210 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       LivingEntity entity;
/*     */       
/*     */       /* Error */
/*     */       public void run()
/*     */       {
/*     */         // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: getfield 14	com/avrgaming/civcraft/structure/Pasture$2AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   4: getfield 26	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */         //   7: invokevirtual 32	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */         //   10: aload_0
/*     */         //   11: getfield 14	com/avrgaming/civcraft/structure/Pasture$2AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   14: getfield 36	com/avrgaming/civcraft/structure/Pasture:entities	Ljava/util/HashSet;
/*     */         //   17: aload_0
/*     */         //   18: getfield 19	com/avrgaming/civcraft/structure/Pasture$2AsyncTask:entity	Lorg/bukkit/entity/LivingEntity;
/*     */         //   21: invokeinterface 40 1 0
/*     */         //   26: invokevirtual 46	java/util/HashSet:remove	(Ljava/lang/Object;)Z
/*     */         //   29: pop
/*     */         //   30: getstatic 52	com/avrgaming/civcraft/structure/Pasture:pastureEntities	Ljava/util/Map;
/*     */         //   33: aload_0
/*     */         //   34: getfield 19	com/avrgaming/civcraft/structure/Pasture$2AsyncTask:entity	Lorg/bukkit/entity/LivingEntity;
/*     */         //   37: invokeinterface 40 1 0
/*     */         //   42: invokeinterface 56 2 0
/*     */         //   47: pop
/*     */         //   48: goto +16 -> 64
/*     */         //   51: astore_1
/*     */         //   52: aload_0
/*     */         //   53: getfield 14	com/avrgaming/civcraft/structure/Pasture$2AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   56: getfield 26	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */         //   59: invokevirtual 61	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */         //   62: aload_1
/*     */         //   63: athrow
/*     */         //   64: aload_0
/*     */         //   65: getfield 14	com/avrgaming/civcraft/structure/Pasture$2AsyncTask:this$0	Lcom/avrgaming/civcraft/structure/Pasture;
/*     */         //   68: getfield 26	com/avrgaming/civcraft/structure/Pasture:lock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */         //   71: invokevirtual 61	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */         //   74: return
/*     */         // Line number table:
/*     */         //   Java source line #199	-> byte code offset #0
/*     */         //   Java source line #201	-> byte code offset #10
/*     */         //   Java source line #202	-> byte code offset #30
/*     */         //   Java source line #203	-> byte code offset #48
/*     */         //   Java source line #204	-> byte code offset #52
/*     */         //   Java source line #205	-> byte code offset #62
/*     */         //   Java source line #204	-> byte code offset #64
/*     */         //   Java source line #206	-> byte code offset #74
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	75	0	this	2AsyncTask
/*     */         //   51	12	1	localObject	Object
/*     */         // Exception table:
/*     */         //   from	to	target	type
/*     */         //   10	51	51	finally
/*     */       }
/* 210 */     }, 0L);
/*     */   }
/*     */   
/*     */   public int getPendingBreeds() {
/* 214 */     return this.pendingBreeds;
/*     */   }
/*     */   
/*     */   public void setPendingBreeds(int pendingBreeds) {
/* 218 */     this.pendingBreeds = pendingBreeds;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Pasture.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */