/*     */ package com.avrgaming.civcraft.event;
/*     */ 
/*     */ import com.avrgaming.civcraft.database.SQL;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.randomevents.RandomEventTimer;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
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
/*     */ public class EventTimer
/*     */ {
/*     */   private Calendar next;
/*     */   private Calendar last;
/*     */   private String name;
/*     */   private EventInterface eventFunction;
/*  42 */   public static HashMap<String, EventTimer> timers = new HashMap();
/*     */   
/*  44 */   public static String TABLE_NAME = "TIMERS";
/*     */   
/*  46 */   public static void init() throws SQLException { if (!SQL.hasTable(TABLE_NAME)) {
/*  47 */       String table_create = "CREATE TABLE " + SQL.tb_prefix + TABLE_NAME + " (" + 
/*  48 */         "`name` VARCHAR(64) NOT NULL," + 
/*  49 */         "`nextEvent` long," + 
/*  50 */         "`lastEvent` long," + 
/*  51 */         "PRIMARY KEY (`name`)" + ")";
/*     */       
/*  53 */       SQL.makeTable(table_create);
/*  54 */       CivLog.info("Created " + TABLE_NAME + " table");
/*     */     } else {
/*  56 */       CivLog.info(TABLE_NAME + " table OK!");
/*     */     }
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
/*     */   public static void loadGlobalEvents()
/*     */   {
/*     */     try
/*     */     {
/*  72 */       DailyEvent upkeepEvent = new DailyEvent();
/*  73 */       new EventTimer("daily", upkeepEvent, upkeepEvent.getNextDate());
/*     */     } catch (InvalidConfiguration e) {
/*  75 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  80 */       HourlyTickEvent hourlyTickEvent = new HourlyTickEvent();
/*  81 */       new EventTimer("hourly", hourlyTickEvent, hourlyTickEvent.getNextDate());
/*     */     } catch (InvalidConfiguration e) {
/*  83 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  88 */       SpawnRegenEvent spawnRegenEvent = new SpawnRegenEvent();
/*  89 */       new EventTimer("spawn-regen", spawnRegenEvent, spawnRegenEvent.getNextDate());
/*     */     } catch (InvalidConfiguration e) {
/*  91 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  96 */       WarEvent WarEvent = new WarEvent();
/*  97 */       new EventTimer("war", WarEvent, WarEvent.getNextDate());
/*     */     } catch (InvalidConfiguration e) {
/*  99 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 104 */       GoodieRepoEvent repoEvent = new GoodieRepoEvent();
/* 105 */       new EventTimer("repo-goodies", repoEvent, repoEvent.getNextDate());
/*     */     } catch (InvalidConfiguration e) {
/* 107 */       e.printStackTrace();
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 112 */       RandomEventTimer randEvent = new RandomEventTimer();
/* 113 */       new EventTimer("random", randEvent, randEvent.getNextDate());
/*     */     } catch (InvalidConfiguration e) {
/* 115 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public EventTimer(String name, EventInterface eventFunction, Calendar start)
/*     */   {
/*     */     try
/*     */     {
/* 123 */       load(name, eventFunction, start);
/*     */     } catch (SQLException e) {
/* 125 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void load(String timerName, EventInterface eventFunction, Calendar start)
/*     */     throws SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore 4
/*     */     //   3: aconst_null
/*     */     //   4: astore 5
/*     */     //   6: aconst_null
/*     */     //   7: astore 6
/*     */     //   9: new 43	java/lang/StringBuilder
/*     */     //   12: dup
/*     */     //   13: ldc -86
/*     */     //   15: invokespecial 47	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   18: getstatic 50	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   21: invokevirtual 53	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   24: getstatic 29	com/avrgaming/civcraft/event/EventTimer:TABLE_NAME	Ljava/lang/String;
/*     */     //   27: invokevirtual 53	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   30: ldc -84
/*     */     //   32: invokevirtual 53	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   35: invokevirtual 69	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   38: astore 7
/*     */     //   40: invokestatic 174	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   43: astore 4
/*     */     //   45: aload 4
/*     */     //   47: aload 7
/*     */     //   49: invokeinterface 178 2 0
/*     */     //   54: astore 6
/*     */     //   56: aload 6
/*     */     //   58: iconst_1
/*     */     //   59: aload_1
/*     */     //   60: invokeinterface 184 3 0
/*     */     //   65: aload 6
/*     */     //   67: invokeinterface 190 1 0
/*     */     //   72: astore 5
/*     */     //   74: aload_0
/*     */     //   75: aload_1
/*     */     //   76: putfield 194	com/avrgaming/civcraft/event/EventTimer:name	Ljava/lang/String;
/*     */     //   79: aload_0
/*     */     //   80: aload_2
/*     */     //   81: putfield 196	com/avrgaming/civcraft/event/EventTimer:eventFunction	Lcom/avrgaming/civcraft/event/EventInterface;
/*     */     //   84: aload 5
/*     */     //   86: invokeinterface 198 1 0
/*     */     //   91: ifeq +52 -> 143
/*     */     //   94: aload_0
/*     */     //   95: invokestatic 203	com/avrgaming/civcraft/event/EventTimer:getCalendarInServerTimeZone	()Ljava/util/Calendar;
/*     */     //   98: putfield 206	com/avrgaming/civcraft/event/EventTimer:last	Ljava/util/Calendar;
/*     */     //   101: aload_0
/*     */     //   102: getfield 206	com/avrgaming/civcraft/event/EventTimer:last	Ljava/util/Calendar;
/*     */     //   105: aload 5
/*     */     //   107: ldc -48
/*     */     //   109: invokeinterface 210 2 0
/*     */     //   114: invokevirtual 214	java/util/Calendar:setTimeInMillis	(J)V
/*     */     //   117: aload_0
/*     */     //   118: invokestatic 203	com/avrgaming/civcraft/event/EventTimer:getCalendarInServerTimeZone	()Ljava/util/Calendar;
/*     */     //   121: putfield 218	com/avrgaming/civcraft/event/EventTimer:next	Ljava/util/Calendar;
/*     */     //   124: aload_0
/*     */     //   125: getfield 218	com/avrgaming/civcraft/event/EventTimer:next	Ljava/util/Calendar;
/*     */     //   128: aload 5
/*     */     //   130: ldc -36
/*     */     //   132: invokeinterface 210 2 0
/*     */     //   137: invokevirtual 214	java/util/Calendar:setTimeInMillis	(J)V
/*     */     //   140: goto +27 -> 167
/*     */     //   143: aload_0
/*     */     //   144: invokestatic 203	com/avrgaming/civcraft/event/EventTimer:getCalendarInServerTimeZone	()Ljava/util/Calendar;
/*     */     //   147: putfield 206	com/avrgaming/civcraft/event/EventTimer:last	Ljava/util/Calendar;
/*     */     //   150: aload_0
/*     */     //   151: getfield 206	com/avrgaming/civcraft/event/EventTimer:last	Ljava/util/Calendar;
/*     */     //   154: lconst_0
/*     */     //   155: invokevirtual 214	java/util/Calendar:setTimeInMillis	(J)V
/*     */     //   158: aload_0
/*     */     //   159: aload_3
/*     */     //   160: putfield 218	com/avrgaming/civcraft/event/EventTimer:next	Ljava/util/Calendar;
/*     */     //   163: aload_0
/*     */     //   164: invokevirtual 222	com/avrgaming/civcraft/event/EventTimer:save	()V
/*     */     //   167: aload_0
/*     */     //   168: invokespecial 225	com/avrgaming/civcraft/event/EventTimer:register	()V
/*     */     //   171: goto +17 -> 188
/*     */     //   174: astore 8
/*     */     //   176: aload 5
/*     */     //   178: aload 6
/*     */     //   180: aload 4
/*     */     //   182: invokestatic 228	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   185: aload 8
/*     */     //   187: athrow
/*     */     //   188: aload 5
/*     */     //   190: aload 6
/*     */     //   192: aload 4
/*     */     //   194: invokestatic 228	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   197: return
/*     */     // Line number table:
/*     */     //   Java source line #135	-> byte code offset #0
/*     */     //   Java source line #136	-> byte code offset #3
/*     */     //   Java source line #137	-> byte code offset #6
/*     */     //   Java source line #140	-> byte code offset #9
/*     */     //   Java source line #141	-> byte code offset #40
/*     */     //   Java source line #142	-> byte code offset #45
/*     */     //   Java source line #143	-> byte code offset #56
/*     */     //   Java source line #144	-> byte code offset #65
/*     */     //   Java source line #146	-> byte code offset #74
/*     */     //   Java source line #147	-> byte code offset #79
/*     */     //   Java source line #149	-> byte code offset #84
/*     */     //   Java source line #150	-> byte code offset #94
/*     */     //   Java source line #151	-> byte code offset #101
/*     */     //   Java source line #153	-> byte code offset #117
/*     */     //   Java source line #154	-> byte code offset #124
/*     */     //   Java source line #155	-> byte code offset #140
/*     */     //   Java source line #156	-> byte code offset #143
/*     */     //   Java source line #157	-> byte code offset #150
/*     */     //   Java source line #159	-> byte code offset #158
/*     */     //   Java source line #160	-> byte code offset #163
/*     */     //   Java source line #162	-> byte code offset #167
/*     */     //   Java source line #163	-> byte code offset #171
/*     */     //   Java source line #164	-> byte code offset #176
/*     */     //   Java source line #165	-> byte code offset #185
/*     */     //   Java source line #164	-> byte code offset #188
/*     */     //   Java source line #166	-> byte code offset #197
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	198	0	this	EventTimer
/*     */     //   0	198	1	timerName	String
/*     */     //   0	198	2	eventFunction	EventInterface
/*     */     //   0	198	3	start	Calendar
/*     */     //   1	192	4	context	Connection
/*     */     //   4	185	5	rs	java.sql.ResultSet
/*     */     //   7	184	6	ps	PreparedStatement
/*     */     //   38	10	7	query	String
/*     */     //   174	12	8	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   9	174	174	finally
/*     */   }
/*     */   
/*     */   private void register()
/*     */   {
/* 169 */     timers.put(this.name, this);
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
/*     */   public void save()
/*     */   {
/* 184 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       EventTimer timer;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/* 178 */           this.timer.saveNow();
/*     */         } catch (SQLException e) {
/* 180 */           e.printStackTrace();
/*     */         }
/*     */         
/*     */       }
/* 184 */     }, 0L);
/*     */   }
/*     */   
/*     */   public void saveNow() throws SQLException {
/* 188 */     Connection context = null;
/* 189 */     PreparedStatement ps = null;
/*     */     try
/*     */     {
/* 192 */       String query = "INSERT INTO `" + SQL.tb_prefix + TABLE_NAME + "` (`name`, `nextEvent`, `lastEvent`) " + 
/* 193 */         "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `nextEvent`=?, `lastEvent`=?";
/* 194 */       context = SQL.getGameConnection();
/* 195 */       ps = context.prepareStatement(query);
/*     */       
/* 197 */       ps.setString(1, this.name);
/* 198 */       ps.setLong(2, this.next.getTime().getTime());
/* 199 */       ps.setLong(3, this.last.getTime().getTime());
/* 200 */       ps.setLong(4, this.next.getTime().getTime());
/* 201 */       ps.setLong(5, this.last.getTime().getTime());
/*     */       
/* 203 */       int rs = ps.executeUpdate();
/* 204 */       if (rs == 0) {
/* 205 */         throw new SQLException("Could not execute SQL code:" + query);
/*     */       }
/*     */     } finally {
/* 208 */       SQL.close(null, ps, context); } SQL.close(null, ps, context);
/*     */   }
/*     */   
/*     */ 
/*     */   public Calendar getNext()
/*     */   {
/* 214 */     return this.next;
/*     */   }
/*     */   
/*     */   public Calendar getLast() {
/* 218 */     return this.last;
/*     */   }
/*     */   
/*     */   public void setNext(Calendar next2) {
/* 222 */     this.next = next2;
/*     */   }
/*     */   
/*     */   public void setLast(Calendar last) {
/* 226 */     this.last = last;
/*     */   }
/*     */   
/*     */   public EventInterface getEventFunction() {
/* 230 */     return this.eventFunction;
/*     */   }
/*     */   
/*     */   public void setEventFunction(EventInterface eventFunction) {
/* 234 */     this.eventFunction = eventFunction;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 238 */     return this.name;
/*     */   }
/*     */   
/*     */   public static Calendar getCalendarInServerTimeZone() {
/* 242 */     Calendar cal = Calendar.getInstance();
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
/* 255 */     return cal;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\EventTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */