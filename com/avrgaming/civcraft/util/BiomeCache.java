/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.object.CultureChunk;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Biome;
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
/*     */ public class BiomeCache
/*     */ {
/*  26 */   public static HashMap<String, String> biomeCache = new HashMap();
/*     */   
/*  28 */   public static String TABLE_NAME = "CHUNK_BIOMES";
/*     */   
/*     */   /* Error */
/*     */   public static void init()
/*     */     throws java.sql.SQLException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 34	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   3: ldc 40
/*     */     //   5: invokevirtual 42	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   8: getstatic 23	com/avrgaming/civcraft/util/BiomeCache:TABLE_NAME	Ljava/lang/String;
/*     */     //   11: invokestatic 48	com/avrgaming/civcraft/database/SQL:hasTable	(Ljava/lang/String;)Z
/*     */     //   14: ifne +86 -> 100
/*     */     //   17: new 54	java/lang/StringBuilder
/*     */     //   20: dup
/*     */     //   21: ldc 56
/*     */     //   23: invokespecial 58	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   26: getstatic 60	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   29: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   32: getstatic 23	com/avrgaming/civcraft/util/BiomeCache:TABLE_NAME	Ljava/lang/String;
/*     */     //   35: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   38: ldc 67
/*     */     //   40: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   43: ldc 69
/*     */     //   45: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   48: ldc 71
/*     */     //   50: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   53: ldc 73
/*     */     //   55: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   58: ldc 75
/*     */     //   60: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   63: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   66: astore_0
/*     */     //   67: aload_0
/*     */     //   68: invokestatic 81	com/avrgaming/civcraft/database/SQL:makeTable	(Ljava/lang/String;)V
/*     */     //   71: new 54	java/lang/StringBuilder
/*     */     //   74: dup
/*     */     //   75: ldc 84
/*     */     //   77: invokespecial 58	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   80: getstatic 23	com/avrgaming/civcraft/util/BiomeCache:TABLE_NAME	Ljava/lang/String;
/*     */     //   83: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   86: ldc 86
/*     */     //   88: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   91: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   94: invokestatic 88	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   97: goto +27 -> 124
/*     */     //   100: new 54	java/lang/StringBuilder
/*     */     //   103: dup
/*     */     //   104: getstatic 23	com/avrgaming/civcraft/util/BiomeCache:TABLE_NAME	Ljava/lang/String;
/*     */     //   107: invokestatic 93	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   110: invokespecial 58	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   113: ldc 99
/*     */     //   115: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   118: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   121: invokestatic 88	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   124: aconst_null
/*     */     //   125: astore_0
/*     */     //   126: aconst_null
/*     */     //   127: astore_1
/*     */     //   128: aconst_null
/*     */     //   129: astore_2
/*     */     //   130: iconst_0
/*     */     //   131: istore_3
/*     */     //   132: invokestatic 101	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */     //   135: astore_0
/*     */     //   136: aload_0
/*     */     //   137: new 54	java/lang/StringBuilder
/*     */     //   140: dup
/*     */     //   141: ldc 105
/*     */     //   143: invokespecial 58	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   146: getstatic 60	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */     //   149: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   152: getstatic 23	com/avrgaming/civcraft/util/BiomeCache:TABLE_NAME	Ljava/lang/String;
/*     */     //   155: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   158: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   161: invokeinterface 107 2 0
/*     */     //   166: astore_2
/*     */     //   167: aload_2
/*     */     //   168: invokeinterface 113 1 0
/*     */     //   173: astore_1
/*     */     //   174: goto +37 -> 211
/*     */     //   177: iinc 3 1
/*     */     //   180: aload_1
/*     */     //   181: ldc 119
/*     */     //   183: invokeinterface 121 2 0
/*     */     //   188: astore 4
/*     */     //   190: aload_1
/*     */     //   191: ldc 127
/*     */     //   193: invokeinterface 121 2 0
/*     */     //   198: astore 5
/*     */     //   200: getstatic 19	com/avrgaming/civcraft/util/BiomeCache:biomeCache	Ljava/util/HashMap;
/*     */     //   203: aload 4
/*     */     //   205: aload 5
/*     */     //   207: invokevirtual 129	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   210: pop
/*     */     //   211: aload_1
/*     */     //   212: invokeinterface 133 1 0
/*     */     //   217: ifne -40 -> 177
/*     */     //   220: new 54	java/lang/StringBuilder
/*     */     //   223: dup
/*     */     //   224: ldc -119
/*     */     //   226: invokespecial 58	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   229: iload_3
/*     */     //   230: invokevirtual 139	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   233: ldc -114
/*     */     //   235: invokevirtual 63	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   238: invokevirtual 77	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   241: invokestatic 88	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*     */     //   244: goto +14 -> 258
/*     */     //   247: astore 6
/*     */     //   249: aload_1
/*     */     //   250: aload_2
/*     */     //   251: aload_0
/*     */     //   252: invokestatic 144	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   255: aload 6
/*     */     //   257: athrow
/*     */     //   258: aload_1
/*     */     //   259: aload_2
/*     */     //   260: aload_0
/*     */     //   261: invokestatic 144	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*     */     //   264: getstatic 34	java/lang/System:out	Ljava/io/PrintStream;
/*     */     //   267: ldc -108
/*     */     //   269: invokevirtual 42	java/io/PrintStream:println	(Ljava/lang/String;)V
/*     */     //   272: return
/*     */     // Line number table:
/*     */     //   Java source line #30	-> byte code offset #0
/*     */     //   Java source line #31	-> byte code offset #8
/*     */     //   Java source line #32	-> byte code offset #17
/*     */     //   Java source line #33	-> byte code offset #43
/*     */     //   Java source line #34	-> byte code offset #48
/*     */     //   Java source line #35	-> byte code offset #53
/*     */     //   Java source line #32	-> byte code offset #63
/*     */     //   Java source line #37	-> byte code offset #67
/*     */     //   Java source line #38	-> byte code offset #71
/*     */     //   Java source line #39	-> byte code offset #97
/*     */     //   Java source line #40	-> byte code offset #100
/*     */     //   Java source line #43	-> byte code offset #124
/*     */     //   Java source line #44	-> byte code offset #126
/*     */     //   Java source line #45	-> byte code offset #128
/*     */     //   Java source line #48	-> byte code offset #130
/*     */     //   Java source line #49	-> byte code offset #132
/*     */     //   Java source line #50	-> byte code offset #136
/*     */     //   Java source line #51	-> byte code offset #167
/*     */     //   Java source line #53	-> byte code offset #174
/*     */     //   Java source line #54	-> byte code offset #177
/*     */     //   Java source line #55	-> byte code offset #180
/*     */     //   Java source line #56	-> byte code offset #190
/*     */     //   Java source line #57	-> byte code offset #200
/*     */     //   Java source line #53	-> byte code offset #211
/*     */     //   Java source line #60	-> byte code offset #220
/*     */     //   Java source line #61	-> byte code offset #244
/*     */     //   Java source line #62	-> byte code offset #249
/*     */     //   Java source line #63	-> byte code offset #255
/*     */     //   Java source line #62	-> byte code offset #258
/*     */     //   Java source line #65	-> byte code offset #264
/*     */     //   Java source line #66	-> byte code offset #272
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   66	2	0	table_create	String
/*     */     //   125	136	0	context	java.sql.Connection
/*     */     //   127	132	1	rs	java.sql.ResultSet
/*     */     //   129	131	2	ps	java.sql.PreparedStatement
/*     */     //   131	99	3	count	int
/*     */     //   188	16	4	key	String
/*     */     //   198	8	5	value	String
/*     */     //   247	9	6	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   130	247	247	finally
/*     */   }
/*     */   
/*     */   public static void saveBiomeInfo(CultureChunk cc)
/*     */   {
/* 108 */     TaskMaster.asyncTask(new Runnable()
/*     */     {
/*     */       /* Error */
/*     */       public void run()
/*     */       {
/*     */         // Byte code:
/*     */         //   0: aconst_null
/*     */         //   1: astore_1
/*     */         //   2: aconst_null
/*     */         //   3: astore_2
/*     */         //   4: invokestatic 22	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*     */         //   7: astore_1
/*     */         //   8: aload_1
/*     */         //   9: new 28	java/lang/StringBuilder
/*     */         //   12: dup
/*     */         //   13: ldc 30
/*     */         //   15: invokespecial 32	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */         //   18: getstatic 35	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*     */         //   21: invokevirtual 39	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   24: getstatic 43	com/avrgaming/civcraft/util/BiomeCache:TABLE_NAME	Ljava/lang/String;
/*     */         //   27: invokevirtual 39	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   30: ldc 48
/*     */         //   32: invokevirtual 39	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   35: ldc 50
/*     */         //   37: invokevirtual 39	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   40: invokevirtual 52	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   43: invokeinterface 56 2 0
/*     */         //   48: astore_2
/*     */         //   49: aload_2
/*     */         //   50: iconst_1
/*     */         //   51: aload_0
/*     */         //   52: getfield 15	com/avrgaming/civcraft/util/BiomeCache$1AsyncTask:cc	Lcom/avrgaming/civcraft/object/CultureChunk;
/*     */         //   55: invokevirtual 62	com/avrgaming/civcraft/object/CultureChunk:getChunkCoord	()Lcom/avrgaming/civcraft/util/ChunkCoord;
/*     */         //   58: invokevirtual 68	com/avrgaming/civcraft/util/ChunkCoord:toString	()Ljava/lang/String;
/*     */         //   61: invokeinterface 71 3 0
/*     */         //   66: aload_2
/*     */         //   67: iconst_2
/*     */         //   68: aload_0
/*     */         //   69: getfield 15	com/avrgaming/civcraft/util/BiomeCache$1AsyncTask:cc	Lcom/avrgaming/civcraft/object/CultureChunk;
/*     */         //   72: invokevirtual 77	com/avrgaming/civcraft/object/CultureChunk:getBiome	()Lorg/bukkit/block/Biome;
/*     */         //   75: invokevirtual 81	org/bukkit/block/Biome:name	()Ljava/lang/String;
/*     */         //   78: invokeinterface 71 3 0
/*     */         //   83: aload_2
/*     */         //   84: iconst_3
/*     */         //   85: aload_0
/*     */         //   86: getfield 15	com/avrgaming/civcraft/util/BiomeCache$1AsyncTask:cc	Lcom/avrgaming/civcraft/object/CultureChunk;
/*     */         //   89: invokevirtual 77	com/avrgaming/civcraft/object/CultureChunk:getBiome	()Lorg/bukkit/block/Biome;
/*     */         //   92: invokevirtual 81	org/bukkit/block/Biome:name	()Ljava/lang/String;
/*     */         //   95: invokeinterface 71 3 0
/*     */         //   100: aload_2
/*     */         //   101: invokeinterface 86 1 0
/*     */         //   106: istore_3
/*     */         //   107: iload_3
/*     */         //   108: ifne +105 -> 213
/*     */         //   111: new 28	java/lang/StringBuilder
/*     */         //   114: dup
/*     */         //   115: ldc 90
/*     */         //   117: invokespecial 32	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */         //   120: aload_0
/*     */         //   121: getfield 15	com/avrgaming/civcraft/util/BiomeCache$1AsyncTask:cc	Lcom/avrgaming/civcraft/object/CultureChunk;
/*     */         //   124: invokevirtual 62	com/avrgaming/civcraft/object/CultureChunk:getChunkCoord	()Lcom/avrgaming/civcraft/util/ChunkCoord;
/*     */         //   127: invokevirtual 68	com/avrgaming/civcraft/util/ChunkCoord:toString	()Ljava/lang/String;
/*     */         //   130: invokevirtual 39	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   133: ldc 92
/*     */         //   135: invokevirtual 39	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   138: aload_0
/*     */         //   139: getfield 15	com/avrgaming/civcraft/util/BiomeCache$1AsyncTask:cc	Lcom/avrgaming/civcraft/object/CultureChunk;
/*     */         //   142: invokevirtual 77	com/avrgaming/civcraft/object/CultureChunk:getBiome	()Lorg/bukkit/block/Biome;
/*     */         //   145: invokevirtual 81	org/bukkit/block/Biome:name	()Ljava/lang/String;
/*     */         //   148: invokevirtual 39	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */         //   151: invokevirtual 52	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */         //   154: invokestatic 94	com/avrgaming/civcraft/main/CivLog:error	(Ljava/lang/String;)V
/*     */         //   157: goto +56 -> 213
/*     */         //   160: astore_3
/*     */         //   161: aload_3
/*     */         //   162: invokevirtual 99	java/sql/SQLException:printStackTrace	()V
/*     */         //   165: aload_1
/*     */         //   166: ifnull +67 -> 233
/*     */         //   169: aload_1
/*     */         //   170: invokeinterface 104 1 0
/*     */         //   175: goto +58 -> 233
/*     */         //   178: astore 5
/*     */         //   180: aload 5
/*     */         //   182: invokevirtual 99	java/sql/SQLException:printStackTrace	()V
/*     */         //   185: goto +48 -> 233
/*     */         //   188: astore 4
/*     */         //   190: aload_1
/*     */         //   191: ifnull +19 -> 210
/*     */         //   194: aload_1
/*     */         //   195: invokeinterface 104 1 0
/*     */         //   200: goto +10 -> 210
/*     */         //   203: astore 5
/*     */         //   205: aload 5
/*     */         //   207: invokevirtual 99	java/sql/SQLException:printStackTrace	()V
/*     */         //   210: aload 4
/*     */         //   212: athrow
/*     */         //   213: aload_1
/*     */         //   214: ifnull +19 -> 233
/*     */         //   217: aload_1
/*     */         //   218: invokeinterface 104 1 0
/*     */         //   223: goto +10 -> 233
/*     */         //   226: astore 5
/*     */         //   228: aload 5
/*     */         //   230: invokevirtual 99	java/sql/SQLException:printStackTrace	()V
/*     */         //   233: return
/*     */         // Line number table:
/*     */         //   Java source line #78	-> byte code offset #0
/*     */         //   Java source line #79	-> byte code offset #2
/*     */         //   Java source line #82	-> byte code offset #4
/*     */         //   Java source line #83	-> byte code offset #8
/*     */         //   Java source line #84	-> byte code offset #35
/*     */         //   Java source line #83	-> byte code offset #43
/*     */         //   Java source line #85	-> byte code offset #49
/*     */         //   Java source line #86	-> byte code offset #66
/*     */         //   Java source line #87	-> byte code offset #83
/*     */         //   Java source line #89	-> byte code offset #100
/*     */         //   Java source line #90	-> byte code offset #107
/*     */         //   Java source line #91	-> byte code offset #111
/*     */         //   Java source line #94	-> byte code offset #157
/*     */         //   Java source line #95	-> byte code offset #161
/*     */         //   Java source line #97	-> byte code offset #165
/*     */         //   Java source line #99	-> byte code offset #169
/*     */         //   Java source line #100	-> byte code offset #175
/*     */         //   Java source line #101	-> byte code offset #180
/*     */         //   Java source line #96	-> byte code offset #188
/*     */         //   Java source line #97	-> byte code offset #190
/*     */         //   Java source line #99	-> byte code offset #194
/*     */         //   Java source line #100	-> byte code offset #200
/*     */         //   Java source line #101	-> byte code offset #205
/*     */         //   Java source line #104	-> byte code offset #210
/*     */         //   Java source line #97	-> byte code offset #213
/*     */         //   Java source line #99	-> byte code offset #217
/*     */         //   Java source line #100	-> byte code offset #223
/*     */         //   Java source line #101	-> byte code offset #228
/*     */         //   Java source line #105	-> byte code offset #233
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	signature
/*     */         //   0	234	0	this	1AsyncTask
/*     */         //   1	217	1	context	java.sql.Connection
/*     */         //   3	98	2	ps	java.sql.PreparedStatement
/*     */         //   106	2	3	rs	int
/*     */         //   160	2	3	e	java.sql.SQLException
/*     */         //   188	23	4	localObject	Object
/*     */         //   178	3	5	e	java.sql.SQLException
/*     */         //   203	3	5	e	java.sql.SQLException
/*     */         //   226	3	5	e	java.sql.SQLException
/*     */         // Exception table:
/*     */         //   from	to	target	type
/*     */         //   4	157	160	java/sql/SQLException
/*     */         //   169	175	178	java/sql/SQLException
/*     */         //   4	165	188	finally
/*     */         //   194	200	203	java/sql/SQLException
/*     */         //   217	223	226	java/sql/SQLException
/*     */       }
/* 108 */     }, 0L);
/*     */   }
/*     */   
/*     */   public static Biome getBiome(CultureChunk cc) {
/* 112 */     if (biomeCache.containsKey(cc.getChunkCoord().toString())) {
/* 113 */       return Biome.valueOf((String)biomeCache.get(cc.getChunkCoord().toString()));
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
/* 130 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 124 */         Chunk chunk = BiomeCache.this.getChunkCoord().getChunk();
/* 125 */         BiomeCache.this.setBiome(chunk.getWorld().getBiome(chunk.getX() * 16, chunk.getZ() * 16));
/* 126 */         BiomeCache.saveBiomeInfo(BiomeCache.this);
/*     */       }
/*     */       
/*     */ 
/* 130 */     });
/* 131 */     return Biome.HELL;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\BiomeCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */