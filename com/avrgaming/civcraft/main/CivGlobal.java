/*      */ package com.avrgaming.civcraft.main;
/*      */ 
/*      */ import com.avrgaming.civcraft.camp.Camp;
/*      */ import com.avrgaming.civcraft.camp.CampBlock;
/*      */ import com.avrgaming.civcraft.config.CivSettings;
/*      */ import com.avrgaming.civcraft.endgame.EndGameCondition;
/*      */ import com.avrgaming.civcraft.event.EventTimer;
/*      */ import com.avrgaming.civcraft.exception.CivException;
/*      */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*      */ import com.avrgaming.civcraft.items.BonusGoodie;
/*      */ import com.avrgaming.civcraft.items.BonusGoodie.LoreIndex;
/*      */ import com.avrgaming.civcraft.object.Civilization;
/*      */ import com.avrgaming.civcraft.object.CultureChunk;
/*      */ import com.avrgaming.civcraft.object.CustomMapMarker;
/*      */ import com.avrgaming.civcraft.object.DiplomacyManager;
/*      */ import com.avrgaming.civcraft.object.ProtectedBlock;
/*      */ import com.avrgaming.civcraft.object.Relation;
/*      */ import com.avrgaming.civcraft.object.Relation.Status;
/*      */ import com.avrgaming.civcraft.object.Resident;
/*      */ import com.avrgaming.civcraft.object.StructureBlock;
/*      */ import com.avrgaming.civcraft.object.StructureChest;
/*      */ import com.avrgaming.civcraft.object.StructureSign;
/*      */ import com.avrgaming.civcraft.object.Town;
/*      */ import com.avrgaming.civcraft.object.TownChunk;
/*      */ import com.avrgaming.civcraft.object.TradeGood;
/*      */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*      */ import com.avrgaming.civcraft.populators.TradeGoodPreGenerate;
/*      */ import com.avrgaming.civcraft.questions.QuestionBaseTask;
/*      */ import com.avrgaming.civcraft.questions.QuestionResponseInterface;
/*      */ import com.avrgaming.civcraft.road.RoadBlock;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionDatabase;
/*      */ import com.avrgaming.civcraft.sessiondb.SessionEntry;
/*      */ import com.avrgaming.civcraft.structure.Buildable;
/*      */ import com.avrgaming.civcraft.structure.Capitol;
/*      */ import com.avrgaming.civcraft.structure.Market;
/*      */ import com.avrgaming.civcraft.structure.Structure;
/*      */ import com.avrgaming.civcraft.structure.TownHall;
/*      */ import com.avrgaming.civcraft.structure.TradeOutpost;
/*      */ import com.avrgaming.civcraft.structure.Wall;
/*      */ import com.avrgaming.civcraft.structure.farm.FarmChunk;
/*      */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*      */ import com.avrgaming.civcraft.template.Template;
/*      */ import com.avrgaming.civcraft.threading.TaskMaster;
/*      */ import com.avrgaming.civcraft.threading.tasks.CivLeaderQuestionTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.CivQuestionTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.CultureProcessAsyncTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.PlayerQuestionTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.UpdateTagBetweenCivsTask;
/*      */ import com.avrgaming.civcraft.threading.tasks.onLoadTask;
/*      */ import com.avrgaming.civcraft.util.BlockCoord;
/*      */ import com.avrgaming.civcraft.util.BukkitObjects;
/*      */ import com.avrgaming.civcraft.util.ChunkCoord;
/*      */ import com.avrgaming.civcraft.util.ItemFrameStorage;
/*      */ import com.avrgaming.civcraft.util.ItemManager;
/*      */ import com.avrgaming.civcraft.war.War;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import net.milkbowl.vault.economy.Economy;
/*      */ import org.bukkit.Bukkit;
/*      */ import org.bukkit.Chunk;
/*      */ import org.bukkit.Location;
/*      */ import org.bukkit.Material;
/*      */ import org.bukkit.OfflinePlayer;
/*      */ import org.bukkit.World;
/*      */ import org.bukkit.entity.Entity;
/*      */ import org.bukkit.entity.ItemFrame;
/*      */ import org.bukkit.entity.Player;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.bukkit.inventory.meta.ItemMeta;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CivGlobal
/*      */ {
/*      */   public static final double MIN_FRAME_DISTANCE = 3.0D;
/*      */   public static Economy econ;
/*  123 */   private static Map<String, QuestionBaseTask> questions = new ConcurrentHashMap();
/*  124 */   public static Map<String, CivQuestionTask> civQuestions = new ConcurrentHashMap();
/*  125 */   private static Map<String, Resident> residents = new ConcurrentHashMap();
/*  126 */   private static Map<String, Town> towns = new ConcurrentHashMap();
/*  127 */   private static Map<String, Civilization> civs = new ConcurrentHashMap();
/*  128 */   private static Map<String, Civilization> conqueredCivs = new ConcurrentHashMap();
/*  129 */   private static Map<String, Civilization> adminCivs = new ConcurrentHashMap();
/*  130 */   private static Map<ChunkCoord, TownChunk> townChunks = new ConcurrentHashMap();
/*  131 */   private static Map<ChunkCoord, CultureChunk> cultureChunks = new ConcurrentHashMap();
/*  132 */   private static Map<ChunkCoord, Boolean> persistChunks = new ConcurrentHashMap();
/*  133 */   private static Map<BlockCoord, Structure> structures = new ConcurrentHashMap();
/*  134 */   private static Map<BlockCoord, Wonder> wonders = new ConcurrentHashMap();
/*  135 */   private static Map<BlockCoord, StructureBlock> structureBlocks = new ConcurrentHashMap();
/*      */   
/*  137 */   private static Map<String, HashSet<Buildable>> buildablesInChunk = new ConcurrentHashMap();
/*  138 */   private static Map<BlockCoord, CampBlock> campBlocks = new ConcurrentHashMap();
/*  139 */   private static Map<BlockCoord, StructureSign> structureSigns = new ConcurrentHashMap();
/*  140 */   private static Map<BlockCoord, StructureChest> structureChests = new ConcurrentHashMap();
/*  141 */   private static Map<BlockCoord, TradeGood> tradeGoods = new ConcurrentHashMap();
/*  142 */   private static Map<BlockCoord, ProtectedBlock> protectedBlocks = new ConcurrentHashMap();
/*  143 */   private static Map<ChunkCoord, FarmChunk> farmChunks = new ConcurrentHashMap();
/*  144 */   private static Queue<FarmChunk> farmChunkUpdateQueue = new LinkedList();
/*  145 */   private static Queue<FarmChunk> farmGrowQueue = new LinkedList();
/*  146 */   private static Map<UUID, ItemFrameStorage> protectedItemFrames = new ConcurrentHashMap();
/*  147 */   private static Map<BlockCoord, BonusGoodie> bonusGoodies = new ConcurrentHashMap();
/*  148 */   private static Map<ChunkCoord, HashSet<Wall>> wallChunks = new ConcurrentHashMap();
/*  149 */   private static Map<BlockCoord, RoadBlock> roadBlocks = new ConcurrentHashMap();
/*  150 */   private static Map<BlockCoord, CustomMapMarker> customMapMarkers = new ConcurrentHashMap();
/*  151 */   private static Map<String, Camp> camps = new ConcurrentHashMap();
/*  152 */   private static Map<ChunkCoord, Camp> campChunks = new ConcurrentHashMap();
/*  153 */   public static HashSet<BlockCoord> vanillaGrowthLocations = new HashSet();
/*  154 */   private static Map<BlockCoord, Market> markets = new ConcurrentHashMap();
/*  155 */   public static HashSet<String> researchedTechs = new HashSet();
/*      */   
/*      */ 
/*  158 */   public static Map<Integer, Boolean> CivColorInUse = new ConcurrentHashMap();
/*  159 */   public static TradeGoodPreGenerate preGenerator = new TradeGoodPreGenerate();
/*      */   
/*      */ 
/*  162 */   public static TreeMap<Integer, Civilization> civilizationScores = new TreeMap();
/*  163 */   public static TreeMap<Integer, Town> townScores = new TreeMap();
/*      */   
/*  165 */   public static HashMap<String, Date> playerFirstLoginMap = new HashMap();
/*  166 */   public static HashSet<String> banWords = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*  170 */   public static Integer maxPlayers = Integer.valueOf(-1);
/*  171 */   public static HashSet<String> betaPlayers = new HashSet();
/*  172 */   public static String fullMessage = "Server is full for now, come back later.";
/*  173 */   public static Boolean betaOnly = Boolean.valueOf(false);
/*      */   
/*      */ 
/*      */   private static SessionDatabase sdb;
/*      */   
/*  178 */   public static boolean trommelsEnabled = true;
/*  179 */   public static boolean towersEnabled = true;
/*  180 */   public static boolean growthEnabled = true;
/*  181 */   public static Boolean banWordsAlways = Boolean.valueOf(false);
/*  182 */   public static boolean banWordsActive = false;
/*  183 */   public static boolean scoringEnabled = true;
/*  184 */   public static boolean warningsEnabled = true;
/*  185 */   public static boolean tradeEnabled = true;
/*  186 */   public static boolean loadCompleted = false;
/*      */   
/*  188 */   public static ArrayList<Town> orphanTowns = new ArrayList();
/*  189 */   public static ArrayList<Civilization> orphanCivs = new ArrayList();
/*      */   
/*  191 */   public static boolean checkForBooks = true;
/*  192 */   public static boolean debugDateBypass = false;
/*  193 */   public static boolean endWorld = false;
/*      */   
/*      */   public static void loadGlobals() throws SQLException, CivException
/*      */   {
/*  197 */     CivLog.heading("Loading CivCraft Objects From Database");
/*      */     
/*  199 */     sdb = new SessionDatabase();
/*  200 */     loadCamps();
/*  201 */     loadCivs();
/*  202 */     loadRelations();
/*  203 */     loadTowns();
/*  204 */     loadResidents();
/*  205 */     loadPermissionGroups();
/*  206 */     loadTownChunks();
/*  207 */     loadStructures();
/*  208 */     loadWonders();
/*  209 */     loadStructureSigns();
/*  210 */     loadWallBlocks();
/*  211 */     loadRoadBlocks();
/*  212 */     loadStructureChests();
/*  213 */     loadTradeGoods();
/*  214 */     loadTradeGoodies();
/*  215 */     loadRandomEvents();
/*  216 */     loadProtectedBlocks();
/*  217 */     loadTeams();
/*  218 */     EventTimer.loadGlobalEvents();
/*  219 */     EndGameCondition.init();
/*  220 */     War.init();
/*      */     try {
/*  222 */       Template.init();
/*      */     } catch (IOException e1) {
/*  224 */       e1.printStackTrace();
/*      */     }
/*      */     
/*  227 */     CivLog.heading("--- Done <3 ---");
/*      */     
/*      */ 
/*  230 */     processUpgrades();
/*  231 */     processCulture();
/*      */     
/*      */ 
/*  234 */     onLoadTask postBuildSyncTask = new onLoadTask();
/*  235 */     TaskMaster.syncTask(postBuildSyncTask);
/*      */     
/*      */ 
/*  238 */     for (Civilization civ : civs.values()) {
/*  239 */       Town capitol = civ.getTown(civ.getCapitolName());
/*      */       
/*  241 */       if (capitol == null) {
/*  242 */         orphanCivs.add(civ);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  266 */     checkForInvalidStructures();
/*  267 */     loadCompleted = true;
/*      */   }
/*      */   
/*      */   public static void checkForInvalidStructures() {
/*  271 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = getStructureIterator();
/*  272 */     while (iter.hasNext()) {
/*  273 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/*  274 */       if (((struct instanceof Capitol)) && 
/*  275 */         (struct.getTown().getMotherCiv() == null) && 
/*  276 */         (!struct.getTown().isCapitol())) {
/*  277 */         struct.markInvalid();
/*  278 */         struct.setInvalidReason("Capitol structures can only exist in the civilization's capitol. Use '/build town hall' to build a town-hall instead.");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void loadTradeGoods() {}
/*      */   
/*      */   /* Error */
/*      */   private static void loadTeams()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 492
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +18 -> 67
/*      */     //   52: new 509	com/avrgaming/civcraft/arena/ArenaTeam
/*      */     //   55: aload_1
/*      */     //   56: invokespecial 511	com/avrgaming/civcraft/arena/ArenaTeam:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   59: goto +8 -> 67
/*      */     //   62: astore_3
/*      */     //   63: aload_3
/*      */     //   64: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   67: aload_1
/*      */     //   68: invokeinterface 517 1 0
/*      */     //   73: ifne -21 -> 52
/*      */     //   76: getstatic 521	com/avrgaming/civcraft/arena/ArenaTeam:teamRankings	Ljava/util/LinkedList;
/*      */     //   79: invokestatic 525	java/util/Collections:sort	(Ljava/util/List;)V
/*      */     //   82: getstatic 521	com/avrgaming/civcraft/arena/ArenaTeam:teamRankings	Ljava/util/LinkedList;
/*      */     //   85: invokestatic 531	java/util/Collections:reverse	(Ljava/util/List;)V
/*      */     //   88: new 479	java/lang/StringBuilder
/*      */     //   91: dup
/*      */     //   92: ldc_w 534
/*      */     //   95: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   98: getstatic 536	com/avrgaming/civcraft/arena/ArenaTeam:arenaTeams	Ljava/util/HashMap;
/*      */     //   101: invokevirtual 539	java/util/HashMap:size	()I
/*      */     //   104: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   107: ldc_w 546
/*      */     //   110: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   113: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   116: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   119: goto +14 -> 133
/*      */     //   122: astore 4
/*      */     //   124: aload_1
/*      */     //   125: aload_2
/*      */     //   126: aload_0
/*      */     //   127: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   130: aload 4
/*      */     //   132: athrow
/*      */     //   133: aload_1
/*      */     //   134: aload_2
/*      */     //   135: aload_0
/*      */     //   136: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   139: return
/*      */     // Line number table:
/*      */     //   Java source line #290	-> byte code offset #0
/*      */     //   Java source line #291	-> byte code offset #2
/*      */     //   Java source line #292	-> byte code offset #4
/*      */     //   Java source line #295	-> byte code offset #6
/*      */     //   Java source line #296	-> byte code offset #10
/*      */     //   Java source line #297	-> byte code offset #42
/*      */     //   Java source line #299	-> byte code offset #49
/*      */     //   Java source line #301	-> byte code offset #52
/*      */     //   Java source line #302	-> byte code offset #59
/*      */     //   Java source line #303	-> byte code offset #62
/*      */     //   Java source line #304	-> byte code offset #63
/*      */     //   Java source line #299	-> byte code offset #67
/*      */     //   Java source line #308	-> byte code offset #76
/*      */     //   Java source line #309	-> byte code offset #82
/*      */     //   Java source line #311	-> byte code offset #88
/*      */     //   Java source line #312	-> byte code offset #119
/*      */     //   Java source line #313	-> byte code offset #124
/*      */     //   Java source line #314	-> byte code offset #130
/*      */     //   Java source line #313	-> byte code offset #133
/*      */     //   Java source line #315	-> byte code offset #139
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	135	0	context	java.sql.Connection
/*      */     //   3	131	1	rs	java.sql.ResultSet
/*      */     //   5	130	2	ps	java.sql.PreparedStatement
/*      */     //   62	2	3	e	Exception
/*      */     //   122	9	4	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	59	62	com/avrgaming/civcraft/exception/InvalidNameException
/*      */     //   52	59	62	com/avrgaming/civcraft/exception/InvalidObjectException
/*      */     //   52	59	62	com/avrgaming/civcraft/exception/CivException
/*      */     //   6	122	122	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private static void loadTradeGoodies()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 569
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +36 -> 85
/*      */     //   52: new 571	com/avrgaming/civcraft/object/TradeGood
/*      */     //   55: dup
/*      */     //   56: aload_1
/*      */     //   57: invokespecial 573	com/avrgaming/civcraft/object/TradeGood:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   60: astore_3
/*      */     //   61: getstatic 162	com/avrgaming/civcraft/main/CivGlobal:tradeGoods	Ljava/util/Map;
/*      */     //   64: aload_3
/*      */     //   65: invokevirtual 574	com/avrgaming/civcraft/object/TradeGood:getCoord	()Lcom/avrgaming/civcraft/util/BlockCoord;
/*      */     //   68: aload_3
/*      */     //   69: invokeinterface 578 3 0
/*      */     //   74: pop
/*      */     //   75: goto +10 -> 85
/*      */     //   78: astore 4
/*      */     //   80: aload 4
/*      */     //   82: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   85: aload_1
/*      */     //   86: invokeinterface 517 1 0
/*      */     //   91: ifne -39 -> 52
/*      */     //   94: new 479	java/lang/StringBuilder
/*      */     //   97: dup
/*      */     //   98: ldc_w 534
/*      */     //   101: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   104: getstatic 162	com/avrgaming/civcraft/main/CivGlobal:tradeGoods	Ljava/util/Map;
/*      */     //   107: invokeinterface 582 1 0
/*      */     //   112: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   115: ldc_w 583
/*      */     //   118: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   121: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   124: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   127: goto +14 -> 141
/*      */     //   130: astore 5
/*      */     //   132: aload_1
/*      */     //   133: aload_2
/*      */     //   134: aload_0
/*      */     //   135: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   138: aload 5
/*      */     //   140: athrow
/*      */     //   141: aload_1
/*      */     //   142: aload_2
/*      */     //   143: aload_0
/*      */     //   144: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   147: return
/*      */     // Line number table:
/*      */     //   Java source line #318	-> byte code offset #0
/*      */     //   Java source line #319	-> byte code offset #2
/*      */     //   Java source line #320	-> byte code offset #4
/*      */     //   Java source line #323	-> byte code offset #6
/*      */     //   Java source line #324	-> byte code offset #10
/*      */     //   Java source line #325	-> byte code offset #42
/*      */     //   Java source line #327	-> byte code offset #49
/*      */     //   Java source line #330	-> byte code offset #52
/*      */     //   Java source line #331	-> byte code offset #61
/*      */     //   Java source line #332	-> byte code offset #75
/*      */     //   Java source line #333	-> byte code offset #80
/*      */     //   Java source line #327	-> byte code offset #85
/*      */     //   Java source line #337	-> byte code offset #94
/*      */     //   Java source line #338	-> byte code offset #127
/*      */     //   Java source line #339	-> byte code offset #132
/*      */     //   Java source line #340	-> byte code offset #138
/*      */     //   Java source line #339	-> byte code offset #141
/*      */     //   Java source line #341	-> byte code offset #147
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	143	0	context	java.sql.Connection
/*      */     //   3	139	1	rs	java.sql.ResultSet
/*      */     //   5	138	2	ps	java.sql.PreparedStatement
/*      */     //   60	9	3	good	TradeGood
/*      */     //   78	3	4	e	Exception
/*      */     //   130	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	75	78	java/lang/Exception
/*      */     //   6	130	130	finally
/*      */   }
/*      */   
/*      */   private static void processUpgrades()
/*      */     throws CivException
/*      */   {
/*  344 */     for (Town town : towns.values()) {
/*      */       try {
/*  346 */         town.loadUpgrades();
/*      */       } catch (Exception e) {
/*  348 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private static void loadCivs()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: getstatic 591	com/avrgaming/civcraft/object/Civilization:TABLE_NAME	Ljava/lang/String;
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: iconst_0
/*      */     //   50: istore_3
/*      */     //   51: goto +47 -> 98
/*      */     //   54: new 405	com/avrgaming/civcraft/object/Civilization
/*      */     //   57: dup
/*      */     //   58: aload_1
/*      */     //   59: invokespecial 594	com/avrgaming/civcraft/object/Civilization:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   62: astore 4
/*      */     //   64: aload 4
/*      */     //   66: invokevirtual 595	com/avrgaming/civcraft/object/Civilization:isConquered	()Z
/*      */     //   69: ifne +11 -> 80
/*      */     //   72: aload 4
/*      */     //   74: invokestatic 598	com/avrgaming/civcraft/main/CivGlobal:addCiv	(Lcom/avrgaming/civcraft/object/Civilization;)V
/*      */     //   77: goto +8 -> 85
/*      */     //   80: aload 4
/*      */     //   82: invokestatic 602	com/avrgaming/civcraft/main/CivGlobal:addConqueredCiv	(Lcom/avrgaming/civcraft/object/Civilization;)V
/*      */     //   85: iinc 3 1
/*      */     //   88: goto +10 -> 98
/*      */     //   91: astore 4
/*      */     //   93: aload 4
/*      */     //   95: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   98: aload_1
/*      */     //   99: invokeinterface 517 1 0
/*      */     //   104: ifne -50 -> 54
/*      */     //   107: new 479	java/lang/StringBuilder
/*      */     //   110: dup
/*      */     //   111: ldc_w 534
/*      */     //   114: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   117: iload_3
/*      */     //   118: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   121: ldc_w 605
/*      */     //   124: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   127: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   130: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   133: goto +14 -> 147
/*      */     //   136: astore 5
/*      */     //   138: aload_1
/*      */     //   139: aload_2
/*      */     //   140: aload_0
/*      */     //   141: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   144: aload 5
/*      */     //   146: athrow
/*      */     //   147: aload_1
/*      */     //   148: aload_2
/*      */     //   149: aload_0
/*      */     //   150: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   153: return
/*      */     // Line number table:
/*      */     //   Java source line #354	-> byte code offset #0
/*      */     //   Java source line #355	-> byte code offset #2
/*      */     //   Java source line #356	-> byte code offset #4
/*      */     //   Java source line #359	-> byte code offset #6
/*      */     //   Java source line #360	-> byte code offset #10
/*      */     //   Java source line #361	-> byte code offset #42
/*      */     //   Java source line #362	-> byte code offset #49
/*      */     //   Java source line #364	-> byte code offset #51
/*      */     //   Java source line #366	-> byte code offset #54
/*      */     //   Java source line #367	-> byte code offset #64
/*      */     //   Java source line #368	-> byte code offset #72
/*      */     //   Java source line #369	-> byte code offset #77
/*      */     //   Java source line #370	-> byte code offset #80
/*      */     //   Java source line #372	-> byte code offset #85
/*      */     //   Java source line #373	-> byte code offset #88
/*      */     //   Java source line #374	-> byte code offset #93
/*      */     //   Java source line #364	-> byte code offset #98
/*      */     //   Java source line #377	-> byte code offset #107
/*      */     //   Java source line #378	-> byte code offset #133
/*      */     //   Java source line #379	-> byte code offset #138
/*      */     //   Java source line #380	-> byte code offset #144
/*      */     //   Java source line #379	-> byte code offset #147
/*      */     //   Java source line #382	-> byte code offset #153
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	149	0	context	java.sql.Connection
/*      */     //   3	145	1	rs	java.sql.ResultSet
/*      */     //   5	144	2	ps	java.sql.PreparedStatement
/*      */     //   50	68	3	count	int
/*      */     //   62	19	4	civ	Civilization
/*      */     //   91	3	4	e	Exception
/*      */     //   136	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   54	88	91	java/lang/Exception
/*      */     //   6	136	136	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private static void loadRelations()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 609
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: iconst_0
/*      */     //   50: istore_3
/*      */     //   51: goto +23 -> 74
/*      */     //   54: new 611	com/avrgaming/civcraft/object/Relation
/*      */     //   57: aload_1
/*      */     //   58: invokespecial 613	com/avrgaming/civcraft/object/Relation:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   61: goto +10 -> 71
/*      */     //   64: astore 4
/*      */     //   66: aload 4
/*      */     //   68: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   71: iinc 3 1
/*      */     //   74: aload_1
/*      */     //   75: invokeinterface 517 1 0
/*      */     //   80: ifne -26 -> 54
/*      */     //   83: new 479	java/lang/StringBuilder
/*      */     //   86: dup
/*      */     //   87: ldc_w 534
/*      */     //   90: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   93: iload_3
/*      */     //   94: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   97: ldc_w 614
/*      */     //   100: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   103: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   106: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   109: goto +14 -> 123
/*      */     //   112: astore 5
/*      */     //   114: aload_1
/*      */     //   115: aload_2
/*      */     //   116: aload_0
/*      */     //   117: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   120: aload 5
/*      */     //   122: athrow
/*      */     //   123: aload_1
/*      */     //   124: aload_2
/*      */     //   125: aload_0
/*      */     //   126: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   129: return
/*      */     // Line number table:
/*      */     //   Java source line #385	-> byte code offset #0
/*      */     //   Java source line #386	-> byte code offset #2
/*      */     //   Java source line #387	-> byte code offset #4
/*      */     //   Java source line #390	-> byte code offset #6
/*      */     //   Java source line #391	-> byte code offset #10
/*      */     //   Java source line #392	-> byte code offset #42
/*      */     //   Java source line #393	-> byte code offset #49
/*      */     //   Java source line #395	-> byte code offset #51
/*      */     //   Java source line #397	-> byte code offset #54
/*      */     //   Java source line #398	-> byte code offset #61
/*      */     //   Java source line #399	-> byte code offset #66
/*      */     //   Java source line #401	-> byte code offset #71
/*      */     //   Java source line #395	-> byte code offset #74
/*      */     //   Java source line #404	-> byte code offset #83
/*      */     //   Java source line #405	-> byte code offset #109
/*      */     //   Java source line #406	-> byte code offset #114
/*      */     //   Java source line #407	-> byte code offset #120
/*      */     //   Java source line #406	-> byte code offset #123
/*      */     //   Java source line #408	-> byte code offset #129
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	125	0	context	java.sql.Connection
/*      */     //   3	121	1	rs	java.sql.ResultSet
/*      */     //   5	120	2	ps	java.sql.PreparedStatement
/*      */     //   50	44	3	count	int
/*      */     //   64	3	4	e	Exception
/*      */     //   112	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   54	61	64	java/lang/Exception
/*      */     //   6	112	112	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadPermissionGroups()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 616
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: iconst_0
/*      */     //   50: istore_3
/*      */     //   51: goto +23 -> 74
/*      */     //   54: new 618	com/avrgaming/civcraft/permission/PermissionGroup
/*      */     //   57: aload_1
/*      */     //   58: invokespecial 620	com/avrgaming/civcraft/permission/PermissionGroup:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   61: iinc 3 1
/*      */     //   64: goto +10 -> 74
/*      */     //   67: astore 4
/*      */     //   69: aload 4
/*      */     //   71: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   74: aload_1
/*      */     //   75: invokeinterface 517 1 0
/*      */     //   80: ifne -26 -> 54
/*      */     //   83: new 479	java/lang/StringBuilder
/*      */     //   86: dup
/*      */     //   87: ldc_w 534
/*      */     //   90: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   93: iload_3
/*      */     //   94: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   97: ldc_w 621
/*      */     //   100: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   103: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   106: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   109: goto +14 -> 123
/*      */     //   112: astore 5
/*      */     //   114: aload_1
/*      */     //   115: aload_2
/*      */     //   116: aload_0
/*      */     //   117: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   120: aload 5
/*      */     //   122: athrow
/*      */     //   123: aload_1
/*      */     //   124: aload_2
/*      */     //   125: aload_0
/*      */     //   126: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   129: return
/*      */     // Line number table:
/*      */     //   Java source line #411	-> byte code offset #0
/*      */     //   Java source line #412	-> byte code offset #2
/*      */     //   Java source line #413	-> byte code offset #4
/*      */     //   Java source line #416	-> byte code offset #6
/*      */     //   Java source line #417	-> byte code offset #10
/*      */     //   Java source line #418	-> byte code offset #42
/*      */     //   Java source line #419	-> byte code offset #49
/*      */     //   Java source line #421	-> byte code offset #51
/*      */     //   Java source line #423	-> byte code offset #54
/*      */     //   Java source line #424	-> byte code offset #61
/*      */     //   Java source line #425	-> byte code offset #64
/*      */     //   Java source line #426	-> byte code offset #69
/*      */     //   Java source line #421	-> byte code offset #74
/*      */     //   Java source line #430	-> byte code offset #83
/*      */     //   Java source line #431	-> byte code offset #109
/*      */     //   Java source line #432	-> byte code offset #114
/*      */     //   Java source line #433	-> byte code offset #120
/*      */     //   Java source line #432	-> byte code offset #123
/*      */     //   Java source line #434	-> byte code offset #129
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	125	0	context	java.sql.Connection
/*      */     //   3	121	1	rs	java.sql.ResultSet
/*      */     //   5	120	2	ps	java.sql.PreparedStatement
/*      */     //   50	44	3	count	int
/*      */     //   67	3	4	e	Exception
/*      */     //   112	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   54	64	67	java/lang/Exception
/*      */     //   6	112	112	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadResidents()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 623
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +39 -> 88
/*      */     //   52: new 625	com/avrgaming/civcraft/object/Resident
/*      */     //   55: dup
/*      */     //   56: aload_1
/*      */     //   57: invokespecial 627	com/avrgaming/civcraft/object/Resident:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   60: astore_3
/*      */     //   61: getstatic 132	com/avrgaming/civcraft/main/CivGlobal:residents	Ljava/util/Map;
/*      */     //   64: aload_3
/*      */     //   65: invokevirtual 628	com/avrgaming/civcraft/object/Resident:getName	()Ljava/lang/String;
/*      */     //   68: invokevirtual 631	java/lang/String:toLowerCase	()Ljava/lang/String;
/*      */     //   71: aload_3
/*      */     //   72: invokeinterface 578 3 0
/*      */     //   77: pop
/*      */     //   78: goto +10 -> 88
/*      */     //   81: astore 4
/*      */     //   83: aload 4
/*      */     //   85: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   88: aload_1
/*      */     //   89: invokeinterface 517 1 0
/*      */     //   94: ifne -42 -> 52
/*      */     //   97: new 479	java/lang/StringBuilder
/*      */     //   100: dup
/*      */     //   101: ldc_w 534
/*      */     //   104: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   107: getstatic 132	com/avrgaming/civcraft/main/CivGlobal:residents	Ljava/util/Map;
/*      */     //   110: invokeinterface 582 1 0
/*      */     //   115: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   118: ldc_w 636
/*      */     //   121: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   124: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   127: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   130: goto +14 -> 144
/*      */     //   133: astore 5
/*      */     //   135: aload_1
/*      */     //   136: aload_2
/*      */     //   137: aload_0
/*      */     //   138: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   141: aload 5
/*      */     //   143: athrow
/*      */     //   144: aload_1
/*      */     //   145: aload_2
/*      */     //   146: aload_0
/*      */     //   147: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   150: return
/*      */     // Line number table:
/*      */     //   Java source line #437	-> byte code offset #0
/*      */     //   Java source line #438	-> byte code offset #2
/*      */     //   Java source line #439	-> byte code offset #4
/*      */     //   Java source line #442	-> byte code offset #6
/*      */     //   Java source line #443	-> byte code offset #10
/*      */     //   Java source line #444	-> byte code offset #42
/*      */     //   Java source line #446	-> byte code offset #49
/*      */     //   Java source line #449	-> byte code offset #52
/*      */     //   Java source line #450	-> byte code offset #61
/*      */     //   Java source line #453	-> byte code offset #78
/*      */     //   Java source line #454	-> byte code offset #83
/*      */     //   Java source line #446	-> byte code offset #88
/*      */     //   Java source line #458	-> byte code offset #97
/*      */     //   Java source line #459	-> byte code offset #130
/*      */     //   Java source line #460	-> byte code offset #135
/*      */     //   Java source line #461	-> byte code offset #141
/*      */     //   Java source line #460	-> byte code offset #144
/*      */     //   Java source line #462	-> byte code offset #150
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	146	0	context	java.sql.Connection
/*      */     //   3	142	1	rs	java.sql.ResultSet
/*      */     //   5	141	2	ps	java.sql.PreparedStatement
/*      */     //   60	12	3	res	Resident
/*      */     //   81	3	4	e	Exception
/*      */     //   133	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	78	81	java/lang/Exception
/*      */     //   6	133	133	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadTowns()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 640
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +44 -> 93
/*      */     //   52: new 451	com/avrgaming/civcraft/object/Town
/*      */     //   55: dup
/*      */     //   56: aload_1
/*      */     //   57: invokespecial 642	com/avrgaming/civcraft/object/Town:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   60: astore_3
/*      */     //   61: getstatic 134	com/avrgaming/civcraft/main/CivGlobal:towns	Ljava/util/Map;
/*      */     //   64: aload_3
/*      */     //   65: invokevirtual 643	com/avrgaming/civcraft/object/Town:getName	()Ljava/lang/String;
/*      */     //   68: invokevirtual 631	java/lang/String:toLowerCase	()Ljava/lang/String;
/*      */     //   71: aload_3
/*      */     //   72: invokeinterface 578 3 0
/*      */     //   77: pop
/*      */     //   78: aload_3
/*      */     //   79: invokevirtual 643	com/avrgaming/civcraft/object/Town:getName	()Ljava/lang/String;
/*      */     //   82: invokestatic 644	com/avrgaming/civcraft/war/WarRegen:restoreBlocksFor	(Ljava/lang/String;)V
/*      */     //   85: goto +8 -> 93
/*      */     //   88: astore_3
/*      */     //   89: aload_3
/*      */     //   90: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   93: aload_1
/*      */     //   94: invokeinterface 517 1 0
/*      */     //   99: ifne -47 -> 52
/*      */     //   102: ldc_w 649
/*      */     //   105: invokestatic 644	com/avrgaming/civcraft/war/WarRegen:restoreBlocksFor	(Ljava/lang/String;)V
/*      */     //   108: new 479	java/lang/StringBuilder
/*      */     //   111: dup
/*      */     //   112: ldc_w 534
/*      */     //   115: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   118: getstatic 134	com/avrgaming/civcraft/main/CivGlobal:towns	Ljava/util/Map;
/*      */     //   121: invokeinterface 582 1 0
/*      */     //   126: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   129: ldc_w 651
/*      */     //   132: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   135: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   138: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   141: goto +14 -> 155
/*      */     //   144: astore 4
/*      */     //   146: aload_1
/*      */     //   147: aload_2
/*      */     //   148: aload_0
/*      */     //   149: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   152: aload 4
/*      */     //   154: athrow
/*      */     //   155: aload_1
/*      */     //   156: aload_2
/*      */     //   157: aload_0
/*      */     //   158: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   161: return
/*      */     // Line number table:
/*      */     //   Java source line #465	-> byte code offset #0
/*      */     //   Java source line #466	-> byte code offset #2
/*      */     //   Java source line #467	-> byte code offset #4
/*      */     //   Java source line #470	-> byte code offset #6
/*      */     //   Java source line #471	-> byte code offset #10
/*      */     //   Java source line #472	-> byte code offset #42
/*      */     //   Java source line #474	-> byte code offset #49
/*      */     //   Java source line #476	-> byte code offset #52
/*      */     //   Java source line #477	-> byte code offset #61
/*      */     //   Java source line #478	-> byte code offset #78
/*      */     //   Java source line #479	-> byte code offset #85
/*      */     //   Java source line #480	-> byte code offset #89
/*      */     //   Java source line #474	-> byte code offset #93
/*      */     //   Java source line #484	-> byte code offset #102
/*      */     //   Java source line #485	-> byte code offset #108
/*      */     //   Java source line #486	-> byte code offset #141
/*      */     //   Java source line #487	-> byte code offset #146
/*      */     //   Java source line #488	-> byte code offset #152
/*      */     //   Java source line #487	-> byte code offset #155
/*      */     //   Java source line #489	-> byte code offset #161
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	157	0	context	java.sql.Connection
/*      */     //   3	153	1	rs	java.sql.ResultSet
/*      */     //   5	152	2	ps	java.sql.PreparedStatement
/*      */     //   60	19	3	town	Town
/*      */     //   88	2	3	e	Exception
/*      */     //   144	9	4	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	85	88	java/lang/Exception
/*      */     //   6	144	144	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadCamps()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 653
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +24 -> 73
/*      */     //   52: new 655	com/avrgaming/civcraft/camp/Camp
/*      */     //   55: dup
/*      */     //   56: aload_1
/*      */     //   57: invokespecial 657	com/avrgaming/civcraft/camp/Camp:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   60: astore_3
/*      */     //   61: aload_3
/*      */     //   62: invokestatic 658	com/avrgaming/civcraft/main/CivGlobal:addCamp	(Lcom/avrgaming/civcraft/camp/Camp;)V
/*      */     //   65: goto +8 -> 73
/*      */     //   68: astore_3
/*      */     //   69: aload_3
/*      */     //   70: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   73: aload_1
/*      */     //   74: invokeinterface 517 1 0
/*      */     //   79: ifne -27 -> 52
/*      */     //   82: goto +14 -> 96
/*      */     //   85: astore 4
/*      */     //   87: aload_1
/*      */     //   88: aload_2
/*      */     //   89: aload_0
/*      */     //   90: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   93: aload 4
/*      */     //   95: athrow
/*      */     //   96: aload_1
/*      */     //   97: aload_2
/*      */     //   98: aload_0
/*      */     //   99: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   102: new 479	java/lang/StringBuilder
/*      */     //   105: dup
/*      */     //   106: ldc_w 534
/*      */     //   109: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   112: getstatic 185	com/avrgaming/civcraft/main/CivGlobal:camps	Ljava/util/Map;
/*      */     //   115: invokeinterface 582 1 0
/*      */     //   120: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   123: ldc_w 662
/*      */     //   126: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   129: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   132: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   135: return
/*      */     // Line number table:
/*      */     //   Java source line #492	-> byte code offset #0
/*      */     //   Java source line #493	-> byte code offset #2
/*      */     //   Java source line #494	-> byte code offset #4
/*      */     //   Java source line #497	-> byte code offset #6
/*      */     //   Java source line #498	-> byte code offset #10
/*      */     //   Java source line #499	-> byte code offset #42
/*      */     //   Java source line #501	-> byte code offset #49
/*      */     //   Java source line #503	-> byte code offset #52
/*      */     //   Java source line #504	-> byte code offset #61
/*      */     //   Java source line #505	-> byte code offset #65
/*      */     //   Java source line #506	-> byte code offset #69
/*      */     //   Java source line #501	-> byte code offset #73
/*      */     //   Java source line #509	-> byte code offset #82
/*      */     //   Java source line #510	-> byte code offset #87
/*      */     //   Java source line #511	-> byte code offset #93
/*      */     //   Java source line #510	-> byte code offset #96
/*      */     //   Java source line #513	-> byte code offset #102
/*      */     //   Java source line #514	-> byte code offset #135
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	98	0	context	java.sql.Connection
/*      */     //   3	94	1	rs	java.sql.ResultSet
/*      */     //   5	93	2	ps	java.sql.PreparedStatement
/*      */     //   60	2	3	camp	Camp
/*      */     //   68	2	3	e	Exception
/*      */     //   85	9	4	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	65	68	java/lang/Exception
/*      */     //   6	85	85	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadTownChunks()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 666
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +36 -> 85
/*      */     //   52: new 668	com/avrgaming/civcraft/object/TownChunk
/*      */     //   55: dup
/*      */     //   56: aload_1
/*      */     //   57: invokespecial 670	com/avrgaming/civcraft/object/TownChunk:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   60: astore_3
/*      */     //   61: getstatic 142	com/avrgaming/civcraft/main/CivGlobal:townChunks	Ljava/util/Map;
/*      */     //   64: aload_3
/*      */     //   65: invokevirtual 671	com/avrgaming/civcraft/object/TownChunk:getChunkCoord	()Lcom/avrgaming/civcraft/util/ChunkCoord;
/*      */     //   68: aload_3
/*      */     //   69: invokeinterface 578 3 0
/*      */     //   74: pop
/*      */     //   75: goto +10 -> 85
/*      */     //   78: astore 4
/*      */     //   80: aload 4
/*      */     //   82: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   85: aload_1
/*      */     //   86: invokeinterface 517 1 0
/*      */     //   91: ifne -39 -> 52
/*      */     //   94: new 479	java/lang/StringBuilder
/*      */     //   97: dup
/*      */     //   98: ldc_w 534
/*      */     //   101: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   104: getstatic 142	com/avrgaming/civcraft/main/CivGlobal:townChunks	Ljava/util/Map;
/*      */     //   107: invokeinterface 582 1 0
/*      */     //   112: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   115: ldc_w 675
/*      */     //   118: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   121: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   124: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   127: goto +14 -> 141
/*      */     //   130: astore 5
/*      */     //   132: aload_1
/*      */     //   133: aload_2
/*      */     //   134: aload_0
/*      */     //   135: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   138: aload 5
/*      */     //   140: athrow
/*      */     //   141: aload_1
/*      */     //   142: aload_2
/*      */     //   143: aload_0
/*      */     //   144: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   147: return
/*      */     // Line number table:
/*      */     //   Java source line #517	-> byte code offset #0
/*      */     //   Java source line #518	-> byte code offset #2
/*      */     //   Java source line #519	-> byte code offset #4
/*      */     //   Java source line #522	-> byte code offset #6
/*      */     //   Java source line #523	-> byte code offset #10
/*      */     //   Java source line #524	-> byte code offset #42
/*      */     //   Java source line #526	-> byte code offset #49
/*      */     //   Java source line #529	-> byte code offset #52
/*      */     //   Java source line #530	-> byte code offset #61
/*      */     //   Java source line #531	-> byte code offset #75
/*      */     //   Java source line #532	-> byte code offset #80
/*      */     //   Java source line #526	-> byte code offset #85
/*      */     //   Java source line #536	-> byte code offset #94
/*      */     //   Java source line #537	-> byte code offset #127
/*      */     //   Java source line #538	-> byte code offset #132
/*      */     //   Java source line #539	-> byte code offset #138
/*      */     //   Java source line #538	-> byte code offset #141
/*      */     //   Java source line #540	-> byte code offset #147
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	143	0	context	java.sql.Connection
/*      */     //   3	139	1	rs	java.sql.ResultSet
/*      */     //   5	138	2	ps	java.sql.PreparedStatement
/*      */     //   60	9	3	tc	TownChunk
/*      */     //   78	3	4	e	Exception
/*      */     //   130	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	75	78	java/lang/Exception
/*      */     //   6	130	130	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadStructures()
/*      */     throws SQLException, CivException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: getstatic 679	com/avrgaming/civcraft/structure/Structure:TABLE_NAME	Ljava/lang/String;
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +30 -> 79
/*      */     //   52: aload_1
/*      */     //   53: invokestatic 680	com/avrgaming/civcraft/structure/Structure:newStructure	(Ljava/sql/ResultSet;)Lcom/avrgaming/civcraft/structure/Structure;
/*      */     //   56: astore_3
/*      */     //   57: getstatic 148	com/avrgaming/civcraft/main/CivGlobal:structures	Ljava/util/Map;
/*      */     //   60: aload_3
/*      */     //   61: invokevirtual 684	com/avrgaming/civcraft/structure/Structure:getCorner	()Lcom/avrgaming/civcraft/util/BlockCoord;
/*      */     //   64: aload_3
/*      */     //   65: invokeinterface 578 3 0
/*      */     //   70: pop
/*      */     //   71: goto +8 -> 79
/*      */     //   74: astore_3
/*      */     //   75: aload_3
/*      */     //   76: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   79: aload_1
/*      */     //   80: invokeinterface 517 1 0
/*      */     //   85: ifne -33 -> 52
/*      */     //   88: new 479	java/lang/StringBuilder
/*      */     //   91: dup
/*      */     //   92: ldc_w 534
/*      */     //   95: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   98: getstatic 148	com/avrgaming/civcraft/main/CivGlobal:structures	Ljava/util/Map;
/*      */     //   101: invokeinterface 582 1 0
/*      */     //   106: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   109: ldc_w 687
/*      */     //   112: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   115: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   118: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   121: goto +14 -> 135
/*      */     //   124: astore 4
/*      */     //   126: aload_1
/*      */     //   127: aload_2
/*      */     //   128: aload_0
/*      */     //   129: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   132: aload 4
/*      */     //   134: athrow
/*      */     //   135: aload_1
/*      */     //   136: aload_2
/*      */     //   137: aload_0
/*      */     //   138: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   141: return
/*      */     // Line number table:
/*      */     //   Java source line #543	-> byte code offset #0
/*      */     //   Java source line #544	-> byte code offset #2
/*      */     //   Java source line #545	-> byte code offset #4
/*      */     //   Java source line #548	-> byte code offset #6
/*      */     //   Java source line #549	-> byte code offset #10
/*      */     //   Java source line #550	-> byte code offset #42
/*      */     //   Java source line #552	-> byte code offset #49
/*      */     //   Java source line #554	-> byte code offset #52
/*      */     //   Java source line #555	-> byte code offset #57
/*      */     //   Java source line #556	-> byte code offset #71
/*      */     //   Java source line #557	-> byte code offset #75
/*      */     //   Java source line #552	-> byte code offset #79
/*      */     //   Java source line #561	-> byte code offset #88
/*      */     //   Java source line #562	-> byte code offset #121
/*      */     //   Java source line #563	-> byte code offset #126
/*      */     //   Java source line #564	-> byte code offset #132
/*      */     //   Java source line #563	-> byte code offset #135
/*      */     //   Java source line #565	-> byte code offset #141
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	137	0	context	java.sql.Connection
/*      */     //   3	133	1	rs	java.sql.ResultSet
/*      */     //   5	132	2	ps	java.sql.PreparedStatement
/*      */     //   56	9	3	struct	Structure
/*      */     //   74	2	3	e	Exception
/*      */     //   124	9	4	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	71	74	java/lang/Exception
/*      */     //   6	124	124	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadWonders()
/*      */     throws SQLException, CivException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: getstatic 689	com/avrgaming/civcraft/structure/wonders/Wonder:TABLE_NAME	Ljava/lang/String;
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +30 -> 79
/*      */     //   52: aload_1
/*      */     //   53: invokestatic 692	com/avrgaming/civcraft/structure/wonders/Wonder:newWonder	(Ljava/sql/ResultSet;)Lcom/avrgaming/civcraft/structure/wonders/Wonder;
/*      */     //   56: astore_3
/*      */     //   57: getstatic 150	com/avrgaming/civcraft/main/CivGlobal:wonders	Ljava/util/Map;
/*      */     //   60: aload_3
/*      */     //   61: invokevirtual 696	com/avrgaming/civcraft/structure/wonders/Wonder:getCorner	()Lcom/avrgaming/civcraft/util/BlockCoord;
/*      */     //   64: aload_3
/*      */     //   65: invokeinterface 578 3 0
/*      */     //   70: pop
/*      */     //   71: goto +8 -> 79
/*      */     //   74: astore_3
/*      */     //   75: aload_3
/*      */     //   76: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   79: aload_1
/*      */     //   80: invokeinterface 517 1 0
/*      */     //   85: ifne -33 -> 52
/*      */     //   88: new 479	java/lang/StringBuilder
/*      */     //   91: dup
/*      */     //   92: ldc_w 534
/*      */     //   95: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   98: getstatic 150	com/avrgaming/civcraft/main/CivGlobal:wonders	Ljava/util/Map;
/*      */     //   101: invokeinterface 582 1 0
/*      */     //   106: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   109: ldc_w 697
/*      */     //   112: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   115: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   118: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   121: goto +14 -> 135
/*      */     //   124: astore 4
/*      */     //   126: aload_1
/*      */     //   127: aload_2
/*      */     //   128: aload_0
/*      */     //   129: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   132: aload 4
/*      */     //   134: athrow
/*      */     //   135: aload_1
/*      */     //   136: aload_2
/*      */     //   137: aload_0
/*      */     //   138: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   141: return
/*      */     // Line number table:
/*      */     //   Java source line #568	-> byte code offset #0
/*      */     //   Java source line #569	-> byte code offset #2
/*      */     //   Java source line #570	-> byte code offset #4
/*      */     //   Java source line #573	-> byte code offset #6
/*      */     //   Java source line #574	-> byte code offset #10
/*      */     //   Java source line #575	-> byte code offset #42
/*      */     //   Java source line #577	-> byte code offset #49
/*      */     //   Java source line #579	-> byte code offset #52
/*      */     //   Java source line #580	-> byte code offset #57
/*      */     //   Java source line #581	-> byte code offset #71
/*      */     //   Java source line #582	-> byte code offset #75
/*      */     //   Java source line #577	-> byte code offset #79
/*      */     //   Java source line #586	-> byte code offset #88
/*      */     //   Java source line #587	-> byte code offset #121
/*      */     //   Java source line #588	-> byte code offset #126
/*      */     //   Java source line #589	-> byte code offset #132
/*      */     //   Java source line #588	-> byte code offset #135
/*      */     //   Java source line #590	-> byte code offset #141
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	137	0	context	java.sql.Connection
/*      */     //   3	133	1	rs	java.sql.ResultSet
/*      */     //   5	132	2	ps	java.sql.PreparedStatement
/*      */     //   56	9	3	wonder	Wonder
/*      */     //   74	2	3	e	Exception
/*      */     //   124	9	4	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	71	74	java/lang/Exception
/*      */     //   6	124	124	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadStructureSigns()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: getstatic 701	com/avrgaming/civcraft/object/StructureSign:TABLE_NAME	Ljava/lang/String;
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +34 -> 83
/*      */     //   52: new 702	com/avrgaming/civcraft/object/StructureSign
/*      */     //   55: dup
/*      */     //   56: aload_1
/*      */     //   57: invokespecial 704	com/avrgaming/civcraft/object/StructureSign:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   60: astore_3
/*      */     //   61: getstatic 158	com/avrgaming/civcraft/main/CivGlobal:structureSigns	Ljava/util/Map;
/*      */     //   64: aload_3
/*      */     //   65: invokevirtual 705	com/avrgaming/civcraft/object/StructureSign:getCoord	()Lcom/avrgaming/civcraft/util/BlockCoord;
/*      */     //   68: aload_3
/*      */     //   69: invokeinterface 578 3 0
/*      */     //   74: pop
/*      */     //   75: goto +8 -> 83
/*      */     //   78: astore_3
/*      */     //   79: aload_3
/*      */     //   80: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   83: aload_1
/*      */     //   84: invokeinterface 517 1 0
/*      */     //   89: ifne -37 -> 52
/*      */     //   92: new 479	java/lang/StringBuilder
/*      */     //   95: dup
/*      */     //   96: ldc_w 534
/*      */     //   99: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   102: getstatic 158	com/avrgaming/civcraft/main/CivGlobal:structureSigns	Ljava/util/Map;
/*      */     //   105: invokeinterface 582 1 0
/*      */     //   110: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   113: ldc_w 706
/*      */     //   116: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   119: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   122: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   125: goto +14 -> 139
/*      */     //   128: astore 4
/*      */     //   130: aload_1
/*      */     //   131: aload_2
/*      */     //   132: aload_0
/*      */     //   133: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   136: aload 4
/*      */     //   138: athrow
/*      */     //   139: aload_1
/*      */     //   140: aload_2
/*      */     //   141: aload_0
/*      */     //   142: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   145: return
/*      */     // Line number table:
/*      */     //   Java source line #593	-> byte code offset #0
/*      */     //   Java source line #594	-> byte code offset #2
/*      */     //   Java source line #595	-> byte code offset #4
/*      */     //   Java source line #598	-> byte code offset #6
/*      */     //   Java source line #599	-> byte code offset #10
/*      */     //   Java source line #600	-> byte code offset #42
/*      */     //   Java source line #602	-> byte code offset #49
/*      */     //   Java source line #604	-> byte code offset #52
/*      */     //   Java source line #605	-> byte code offset #61
/*      */     //   Java source line #606	-> byte code offset #75
/*      */     //   Java source line #607	-> byte code offset #79
/*      */     //   Java source line #602	-> byte code offset #83
/*      */     //   Java source line #611	-> byte code offset #92
/*      */     //   Java source line #612	-> byte code offset #125
/*      */     //   Java source line #613	-> byte code offset #130
/*      */     //   Java source line #614	-> byte code offset #136
/*      */     //   Java source line #613	-> byte code offset #139
/*      */     //   Java source line #615	-> byte code offset #145
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	141	0	context	java.sql.Connection
/*      */     //   3	137	1	rs	java.sql.ResultSet
/*      */     //   5	136	2	ps	java.sql.PreparedStatement
/*      */     //   60	9	3	sSign	StructureSign
/*      */     //   78	2	3	e	Exception
/*      */     //   128	9	4	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	75	78	java/lang/Exception
/*      */     //   6	128	128	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private static void loadStructureChests()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: getstatic 710	com/avrgaming/civcraft/object/StructureChest:TABLE_NAME	Ljava/lang/String;
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: goto +36 -> 85
/*      */     //   52: new 711	com/avrgaming/civcraft/object/StructureChest
/*      */     //   55: dup
/*      */     //   56: aload_1
/*      */     //   57: invokespecial 713	com/avrgaming/civcraft/object/StructureChest:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   60: astore_3
/*      */     //   61: getstatic 160	com/avrgaming/civcraft/main/CivGlobal:structureChests	Ljava/util/Map;
/*      */     //   64: aload_3
/*      */     //   65: invokevirtual 714	com/avrgaming/civcraft/object/StructureChest:getCoord	()Lcom/avrgaming/civcraft/util/BlockCoord;
/*      */     //   68: aload_3
/*      */     //   69: invokeinterface 578 3 0
/*      */     //   74: pop
/*      */     //   75: goto +10 -> 85
/*      */     //   78: astore 4
/*      */     //   80: aload 4
/*      */     //   82: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   85: aload_1
/*      */     //   86: invokeinterface 517 1 0
/*      */     //   91: ifne -39 -> 52
/*      */     //   94: new 479	java/lang/StringBuilder
/*      */     //   97: dup
/*      */     //   98: ldc_w 534
/*      */     //   101: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   104: getstatic 160	com/avrgaming/civcraft/main/CivGlobal:structureChests	Ljava/util/Map;
/*      */     //   107: invokeinterface 582 1 0
/*      */     //   112: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   115: ldc_w 715
/*      */     //   118: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   121: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   124: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   127: goto +14 -> 141
/*      */     //   130: astore 5
/*      */     //   132: aload_1
/*      */     //   133: aload_2
/*      */     //   134: aload_0
/*      */     //   135: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   138: aload 5
/*      */     //   140: athrow
/*      */     //   141: aload_1
/*      */     //   142: aload_2
/*      */     //   143: aload_0
/*      */     //   144: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   147: return
/*      */     // Line number table:
/*      */     //   Java source line #618	-> byte code offset #0
/*      */     //   Java source line #619	-> byte code offset #2
/*      */     //   Java source line #620	-> byte code offset #4
/*      */     //   Java source line #623	-> byte code offset #6
/*      */     //   Java source line #624	-> byte code offset #10
/*      */     //   Java source line #625	-> byte code offset #42
/*      */     //   Java source line #627	-> byte code offset #49
/*      */     //   Java source line #630	-> byte code offset #52
/*      */     //   Java source line #631	-> byte code offset #61
/*      */     //   Java source line #632	-> byte code offset #75
/*      */     //   Java source line #633	-> byte code offset #80
/*      */     //   Java source line #627	-> byte code offset #85
/*      */     //   Java source line #637	-> byte code offset #94
/*      */     //   Java source line #638	-> byte code offset #127
/*      */     //   Java source line #639	-> byte code offset #132
/*      */     //   Java source line #640	-> byte code offset #138
/*      */     //   Java source line #639	-> byte code offset #141
/*      */     //   Java source line #641	-> byte code offset #147
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	143	0	context	java.sql.Connection
/*      */     //   3	139	1	rs	java.sql.ResultSet
/*      */     //   5	138	2	ps	java.sql.PreparedStatement
/*      */     //   60	9	3	sChest	StructureChest
/*      */     //   78	3	4	e	Exception
/*      */     //   130	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   52	75	78	java/lang/Exception
/*      */     //   6	130	130	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private static void loadWallBlocks()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 719
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: iconst_0
/*      */     //   50: istore_3
/*      */     //   51: goto +26 -> 77
/*      */     //   54: new 721	com/avrgaming/civcraft/object/WallBlock
/*      */     //   57: aload_1
/*      */     //   58: invokespecial 723	com/avrgaming/civcraft/object/WallBlock:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   61: iinc 3 1
/*      */     //   64: goto +13 -> 77
/*      */     //   67: astore 4
/*      */     //   69: aload 4
/*      */     //   71: invokevirtual 724	java/lang/Exception:getMessage	()Ljava/lang/String;
/*      */     //   74: invokestatic 727	com/avrgaming/civcraft/main/CivLog:warning	(Ljava/lang/String;)V
/*      */     //   77: aload_1
/*      */     //   78: invokeinterface 517 1 0
/*      */     //   83: ifne -29 -> 54
/*      */     //   86: new 479	java/lang/StringBuilder
/*      */     //   89: dup
/*      */     //   90: ldc_w 534
/*      */     //   93: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   96: iload_3
/*      */     //   97: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   100: ldc_w 730
/*      */     //   103: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   106: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   109: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   112: goto +14 -> 126
/*      */     //   115: astore 5
/*      */     //   117: aload_1
/*      */     //   118: aload_2
/*      */     //   119: aload_0
/*      */     //   120: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   123: aload 5
/*      */     //   125: athrow
/*      */     //   126: aload_1
/*      */     //   127: aload_2
/*      */     //   128: aload_0
/*      */     //   129: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   132: return
/*      */     // Line number table:
/*      */     //   Java source line #644	-> byte code offset #0
/*      */     //   Java source line #645	-> byte code offset #2
/*      */     //   Java source line #646	-> byte code offset #4
/*      */     //   Java source line #649	-> byte code offset #6
/*      */     //   Java source line #650	-> byte code offset #10
/*      */     //   Java source line #651	-> byte code offset #42
/*      */     //   Java source line #653	-> byte code offset #49
/*      */     //   Java source line #654	-> byte code offset #51
/*      */     //   Java source line #656	-> byte code offset #54
/*      */     //   Java source line #657	-> byte code offset #61
/*      */     //   Java source line #658	-> byte code offset #64
/*      */     //   Java source line #659	-> byte code offset #69
/*      */     //   Java source line #654	-> byte code offset #77
/*      */     //   Java source line #664	-> byte code offset #86
/*      */     //   Java source line #665	-> byte code offset #112
/*      */     //   Java source line #666	-> byte code offset #117
/*      */     //   Java source line #667	-> byte code offset #123
/*      */     //   Java source line #666	-> byte code offset #126
/*      */     //   Java source line #668	-> byte code offset #132
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	128	0	context	java.sql.Connection
/*      */     //   3	124	1	rs	java.sql.ResultSet
/*      */     //   5	123	2	ps	java.sql.PreparedStatement
/*      */     //   50	47	3	count	int
/*      */     //   67	3	4	e	Exception
/*      */     //   115	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   54	64	67	java/lang/Exception
/*      */     //   6	115	115	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private static void loadRoadBlocks()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 732
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: iconst_0
/*      */     //   50: istore_3
/*      */     //   51: goto +31 -> 82
/*      */     //   54: new 734	com/avrgaming/civcraft/road/RoadBlock
/*      */     //   57: aload_1
/*      */     //   58: invokespecial 736	com/avrgaming/civcraft/road/RoadBlock:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   61: iinc 3 1
/*      */     //   64: goto +18 -> 82
/*      */     //   67: astore 4
/*      */     //   69: aload 4
/*      */     //   71: invokevirtual 724	java/lang/Exception:getMessage	()Ljava/lang/String;
/*      */     //   74: invokestatic 727	com/avrgaming/civcraft/main/CivLog:warning	(Ljava/lang/String;)V
/*      */     //   77: aload 4
/*      */     //   79: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   82: aload_1
/*      */     //   83: invokeinterface 517 1 0
/*      */     //   88: ifne -34 -> 54
/*      */     //   91: new 479	java/lang/StringBuilder
/*      */     //   94: dup
/*      */     //   95: ldc_w 534
/*      */     //   98: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   101: iload_3
/*      */     //   102: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   105: ldc_w 737
/*      */     //   108: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   111: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   114: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   117: goto +14 -> 131
/*      */     //   120: astore 5
/*      */     //   122: aload_1
/*      */     //   123: aload_2
/*      */     //   124: aload_0
/*      */     //   125: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   128: aload 5
/*      */     //   130: athrow
/*      */     //   131: aload_1
/*      */     //   132: aload_2
/*      */     //   133: aload_0
/*      */     //   134: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   137: return
/*      */     // Line number table:
/*      */     //   Java source line #671	-> byte code offset #0
/*      */     //   Java source line #672	-> byte code offset #2
/*      */     //   Java source line #673	-> byte code offset #4
/*      */     //   Java source line #676	-> byte code offset #6
/*      */     //   Java source line #677	-> byte code offset #10
/*      */     //   Java source line #678	-> byte code offset #42
/*      */     //   Java source line #680	-> byte code offset #49
/*      */     //   Java source line #681	-> byte code offset #51
/*      */     //   Java source line #683	-> byte code offset #54
/*      */     //   Java source line #684	-> byte code offset #61
/*      */     //   Java source line #685	-> byte code offset #64
/*      */     //   Java source line #686	-> byte code offset #69
/*      */     //   Java source line #687	-> byte code offset #77
/*      */     //   Java source line #681	-> byte code offset #82
/*      */     //   Java source line #691	-> byte code offset #91
/*      */     //   Java source line #692	-> byte code offset #117
/*      */     //   Java source line #693	-> byte code offset #122
/*      */     //   Java source line #694	-> byte code offset #128
/*      */     //   Java source line #693	-> byte code offset #131
/*      */     //   Java source line #695	-> byte code offset #137
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	133	0	context	java.sql.Connection
/*      */     //   3	129	1	rs	java.sql.ResultSet
/*      */     //   5	128	2	ps	java.sql.PreparedStatement
/*      */     //   50	52	3	count	int
/*      */     //   67	11	4	e	Exception
/*      */     //   120	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   54	64	67	java/lang/Exception
/*      */     //   6	120	120	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadRandomEvents()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 739
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: iconst_0
/*      */     //   50: istore_3
/*      */     //   51: goto +23 -> 74
/*      */     //   54: new 741	com/avrgaming/civcraft/randomevents/RandomEvent
/*      */     //   57: aload_1
/*      */     //   58: invokespecial 743	com/avrgaming/civcraft/randomevents/RandomEvent:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   61: iinc 3 1
/*      */     //   64: goto +10 -> 74
/*      */     //   67: astore 4
/*      */     //   69: aload 4
/*      */     //   71: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   74: aload_1
/*      */     //   75: invokeinterface 517 1 0
/*      */     //   80: ifne -26 -> 54
/*      */     //   83: new 479	java/lang/StringBuilder
/*      */     //   86: dup
/*      */     //   87: ldc_w 534
/*      */     //   90: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   93: iload_3
/*      */     //   94: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   97: ldc_w 744
/*      */     //   100: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   103: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   106: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   109: goto +14 -> 123
/*      */     //   112: astore 5
/*      */     //   114: aload_1
/*      */     //   115: aload_2
/*      */     //   116: aload_0
/*      */     //   117: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   120: aload 5
/*      */     //   122: athrow
/*      */     //   123: aload_1
/*      */     //   124: aload_2
/*      */     //   125: aload_0
/*      */     //   126: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   129: return
/*      */     // Line number table:
/*      */     //   Java source line #698	-> byte code offset #0
/*      */     //   Java source line #699	-> byte code offset #2
/*      */     //   Java source line #700	-> byte code offset #4
/*      */     //   Java source line #703	-> byte code offset #6
/*      */     //   Java source line #704	-> byte code offset #10
/*      */     //   Java source line #705	-> byte code offset #42
/*      */     //   Java source line #707	-> byte code offset #49
/*      */     //   Java source line #708	-> byte code offset #51
/*      */     //   Java source line #710	-> byte code offset #54
/*      */     //   Java source line #711	-> byte code offset #61
/*      */     //   Java source line #712	-> byte code offset #64
/*      */     //   Java source line #713	-> byte code offset #69
/*      */     //   Java source line #708	-> byte code offset #74
/*      */     //   Java source line #717	-> byte code offset #83
/*      */     //   Java source line #718	-> byte code offset #109
/*      */     //   Java source line #719	-> byte code offset #114
/*      */     //   Java source line #720	-> byte code offset #120
/*      */     //   Java source line #719	-> byte code offset #123
/*      */     //   Java source line #721	-> byte code offset #129
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	125	0	context	java.sql.Connection
/*      */     //   3	121	1	rs	java.sql.ResultSet
/*      */     //   5	120	2	ps	java.sql.PreparedStatement
/*      */     //   50	44	3	count	int
/*      */     //   67	3	4	e	Exception
/*      */     //   112	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   54	64	67	java/lang/Exception
/*      */     //   6	112	112	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void loadProtectedBlocks()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aconst_null
/*      */     //   1: astore_0
/*      */     //   2: aconst_null
/*      */     //   3: astore_1
/*      */     //   4: aconst_null
/*      */     //   5: astore_2
/*      */     //   6: invokestatic 473	com/avrgaming/civcraft/database/SQL:getGameConnection	()Ljava/sql/Connection;
/*      */     //   9: astore_0
/*      */     //   10: aload_0
/*      */     //   11: new 479	java/lang/StringBuilder
/*      */     //   14: dup
/*      */     //   15: ldc_w 481
/*      */     //   18: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   21: getstatic 485	com/avrgaming/civcraft/database/SQL:tb_prefix	Ljava/lang/String;
/*      */     //   24: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   27: ldc_w 746
/*      */     //   30: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   33: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   36: invokeinterface 497 2 0
/*      */     //   41: astore_2
/*      */     //   42: aload_2
/*      */     //   43: invokeinterface 503 1 0
/*      */     //   48: astore_1
/*      */     //   49: iconst_0
/*      */     //   50: istore_3
/*      */     //   51: goto +31 -> 82
/*      */     //   54: new 748	com/avrgaming/civcraft/object/ProtectedBlock
/*      */     //   57: dup
/*      */     //   58: aload_1
/*      */     //   59: invokespecial 750	com/avrgaming/civcraft/object/ProtectedBlock:<init>	(Ljava/sql/ResultSet;)V
/*      */     //   62: astore 4
/*      */     //   64: aload 4
/*      */     //   66: invokestatic 751	com/avrgaming/civcraft/main/CivGlobal:addProtectedBlock	(Lcom/avrgaming/civcraft/object/ProtectedBlock;)V
/*      */     //   69: iinc 3 1
/*      */     //   72: goto +10 -> 82
/*      */     //   75: astore 4
/*      */     //   77: aload 4
/*      */     //   79: invokevirtual 514	java/lang/Exception:printStackTrace	()V
/*      */     //   82: aload_1
/*      */     //   83: invokeinterface 517 1 0
/*      */     //   88: ifne -34 -> 54
/*      */     //   91: new 479	java/lang/StringBuilder
/*      */     //   94: dup
/*      */     //   95: ldc_w 534
/*      */     //   98: invokespecial 483	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*      */     //   101: iload_3
/*      */     //   102: invokevirtual 543	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   105: ldc_w 755
/*      */     //   108: invokevirtual 488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   111: invokevirtual 494	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   114: invokestatic 548	com/avrgaming/civcraft/main/CivLog:info	(Ljava/lang/String;)V
/*      */     //   117: goto +14 -> 131
/*      */     //   120: astore 5
/*      */     //   122: aload_1
/*      */     //   123: aload_2
/*      */     //   124: aload_0
/*      */     //   125: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   128: aload 5
/*      */     //   130: athrow
/*      */     //   131: aload_1
/*      */     //   132: aload_2
/*      */     //   133: aload_0
/*      */     //   134: invokestatic 551	com/avrgaming/civcraft/database/SQL:close	(Ljava/sql/ResultSet;Ljava/sql/PreparedStatement;Ljava/sql/Connection;)V
/*      */     //   137: return
/*      */     // Line number table:
/*      */     //   Java source line #724	-> byte code offset #0
/*      */     //   Java source line #725	-> byte code offset #2
/*      */     //   Java source line #726	-> byte code offset #4
/*      */     //   Java source line #729	-> byte code offset #6
/*      */     //   Java source line #730	-> byte code offset #10
/*      */     //   Java source line #731	-> byte code offset #42
/*      */     //   Java source line #733	-> byte code offset #49
/*      */     //   Java source line #734	-> byte code offset #51
/*      */     //   Java source line #736	-> byte code offset #54
/*      */     //   Java source line #737	-> byte code offset #64
/*      */     //   Java source line #738	-> byte code offset #69
/*      */     //   Java source line #739	-> byte code offset #72
/*      */     //   Java source line #740	-> byte code offset #77
/*      */     //   Java source line #734	-> byte code offset #82
/*      */     //   Java source line #744	-> byte code offset #91
/*      */     //   Java source line #745	-> byte code offset #117
/*      */     //   Java source line #746	-> byte code offset #122
/*      */     //   Java source line #747	-> byte code offset #128
/*      */     //   Java source line #746	-> byte code offset #131
/*      */     //   Java source line #748	-> byte code offset #137
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   1	133	0	context	java.sql.Connection
/*      */     //   3	129	1	rs	java.sql.ResultSet
/*      */     //   5	128	2	ps	java.sql.PreparedStatement
/*      */     //   50	52	3	count	int
/*      */     //   62	3	4	pb	ProtectedBlock
/*      */     //   75	3	4	e	Exception
/*      */     //   120	9	5	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   54	72	75	java/lang/Exception
/*      */     //   6	120	120	finally
/*      */   }
/*      */   
/*      */   public static Player getPlayer(Resident resident)
/*      */     throws CivException
/*      */   {
/*  751 */     Player player = Bukkit.getPlayer(resident.getName());
/*  752 */     if (player == null)
/*  753 */       throw new CivException("No player named " + resident.getName());
/*  754 */     return player;
/*      */   }
/*      */   
/*      */   public static Resident getResidentFromId(int id)
/*      */   {
/*  759 */     for (Resident resident : residents.values()) {
/*  760 */       if (resident.getId() == id) {
/*  761 */         return resident;
/*      */       }
/*      */     }
/*  764 */     return null;
/*      */   }
/*      */   
/*      */   public static Resident getResident(Player player) {
/*  768 */     return (Resident)residents.get(player.getName().toLowerCase());
/*      */   }
/*      */   
/*      */   public static Resident getResident(Resident resident) {
/*  772 */     return (Resident)residents.get(resident.getName().toLowerCase());
/*      */   }
/*      */   
/*      */   public static boolean hasResident(String name) {
/*  776 */     return residents.containsKey(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public static void addResident(Resident res) {
/*  780 */     residents.put(res.getName().toLowerCase(), res);
/*      */   }
/*      */   
/*      */   public static Resident getResident(String name) {
/*  784 */     return (Resident)residents.get(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public static Town getTown(String name) {
/*  788 */     if (name == null) {
/*  789 */       return null;
/*      */     }
/*  791 */     return (Town)towns.get(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public static Town getTownFromId(int id)
/*      */   {
/*  796 */     for (Town t : towns.values()) {
/*  797 */       if (t.getId() == id) {
/*  798 */         return t;
/*      */       }
/*      */     }
/*  801 */     return null;
/*      */   }
/*      */   
/*      */   public static void addTown(Town town) {
/*  805 */     towns.put(town.getName().toLowerCase(), town);
/*      */   }
/*      */   
/*      */   public static TownChunk getTownChunk(Location location) {
/*  809 */     ChunkCoord coord = new ChunkCoord(location);
/*  810 */     return (TownChunk)townChunks.get(coord);
/*      */   }
/*      */   
/*      */   public static void addTownChunk(TownChunk tc) {
/*  814 */     townChunks.put(tc.getChunkCoord(), tc);
/*      */   }
/*      */   
/*      */   public static void addCiv(Civilization civ)
/*      */   {
/*  819 */     civs.put(civ.getName().toLowerCase(), civ);
/*  820 */     if (civ.isAdminCiv()) {
/*  821 */       addAdminCiv(civ);
/*      */     }
/*      */   }
/*      */   
/*      */   public static Civilization getCiv(String name) {
/*  826 */     return (Civilization)civs.get(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public static PermissionGroup getPermissionGroup(Town town, Integer id) {
/*  830 */     return town.getGroupFromId(id);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static TownChunk getTownChunk(ChunkCoord coord)
/*      */   {
/*  844 */     return (TownChunk)townChunks.get(coord);
/*      */   }
/*      */   
/*      */   public static PermissionGroup getPermissionGroupFromName(Town town, String name) {
/*  848 */     for (PermissionGroup grp : town.getGroups()) {
/*  849 */       if (grp.getName().equalsIgnoreCase(name)) {
/*  850 */         return grp;
/*      */       }
/*      */     }
/*  853 */     return null;
/*      */   }
/*      */   
/*      */   public static void questionPlayer(Player fromPlayer, Player toPlayer, String question, long timeout, QuestionResponseInterface finishedFunction)
/*      */     throws CivException
/*      */   {
/*  859 */     PlayerQuestionTask task = (PlayerQuestionTask)questions.get(toPlayer.getName());
/*  860 */     if (task != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  865 */       throw new CivException("Player already has a question pending, wait 30 seconds and try again.");
/*      */     }
/*      */     
/*  868 */     task = new PlayerQuestionTask(toPlayer, fromPlayer, question, timeout, finishedFunction);
/*  869 */     questions.put(toPlayer.getName(), task);
/*  870 */     TaskMaster.asyncTask("", task, 0L);
/*      */   }
/*      */   
/*      */   public static void questionLeaders(Player fromPlayer, Civilization toCiv, String question, long timeout, QuestionResponseInterface finishedFunction)
/*      */     throws CivException
/*      */   {
/*  876 */     CivLeaderQuestionTask task = (CivLeaderQuestionTask)questions.get("civ:" + toCiv.getName());
/*  877 */     if (task != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  882 */       throw new CivException("Leaders of civ already have a question pending, wait 30 seconds and try again.");
/*      */     }
/*      */     
/*  885 */     task = new CivLeaderQuestionTask(toCiv, fromPlayer, question, timeout, finishedFunction);
/*  886 */     questions.put("civ:" + toCiv.getName(), task);
/*  887 */     TaskMaster.asyncTask("", task, 0L);
/*      */   }
/*      */   
/*      */   public static QuestionBaseTask getQuestionTask(String string) {
/*  891 */     return (QuestionBaseTask)questions.get(string);
/*      */   }
/*      */   
/*      */   public static void removeQuestion(String name) {
/*  895 */     questions.remove(name);
/*      */   }
/*      */   
/*      */   public static Collection<Town> getTowns() {
/*  899 */     return towns.values();
/*      */   }
/*      */   
/*      */   public static Collection<Resident> getResidents() {
/*  903 */     return residents.values();
/*      */   }
/*      */   
/*      */   public static Civilization getCivFromId(int id) {
/*  907 */     for (Civilization civ : civs.values()) {
/*  908 */       if (civ.getId() == id) {
/*  909 */         return civ;
/*      */       }
/*      */     }
/*  912 */     return null;
/*      */   }
/*      */   
/*      */   public static Collection<Civilization> getCivs() {
/*  916 */     return civs.values();
/*      */   }
/*      */   
/*      */   public static void removeCiv(Civilization civilization) {
/*  920 */     civs.remove(civilization.getName().toLowerCase());
/*  921 */     if (civilization.isAdminCiv()) {
/*  922 */       removeAdminCiv(civilization);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void removeTown(Town town) {
/*  927 */     towns.remove(town.getName().toLowerCase());
/*      */   }
/*      */   
/*      */   public static Collection<PermissionGroup> getGroups() {
/*  931 */     ArrayList<PermissionGroup> groups = new ArrayList();
/*      */     Iterator localIterator2;
/*  933 */     for (Iterator localIterator1 = towns.values().iterator(); localIterator1.hasNext(); 
/*  934 */         localIterator2.hasNext())
/*      */     {
/*  933 */       Town t = (Town)localIterator1.next();
/*  934 */       localIterator2 = t.getGroups().iterator(); continue;PermissionGroup grp = (PermissionGroup)localIterator2.next();
/*  935 */       if (grp != null) {
/*  936 */         groups.add(grp);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  941 */     for (Civilization civ : civs.values()) {
/*  942 */       if (civ.getLeaderGroup() != null) {
/*  943 */         groups.add(civ.getLeaderGroup());
/*      */       }
/*  945 */       if (civ.getAdviserGroup() != null) {
/*  946 */         groups.add(civ.getAdviserGroup());
/*      */       }
/*      */     }
/*      */     
/*  950 */     return groups;
/*      */   }
/*      */   
/*      */   public static Player getPlayer(String name) throws CivException {
/*  954 */     Player player = Bukkit.getPlayer(name);
/*  955 */     if (player == null)
/*  956 */       throw new CivException("No player named " + name);
/*  957 */     return player;
/*      */   }
/*      */   
/*      */   public static void addCultureChunk(CultureChunk cc) {
/*  961 */     cultureChunks.put(cc.getChunkCoord(), cc);
/*      */   }
/*      */   
/*      */   public static CultureChunk getCultureChunk(ChunkCoord coord) {
/*  965 */     return (CultureChunk)cultureChunks.get(coord);
/*      */   }
/*      */   
/*      */   public static void removeCultureChunk(CultureChunk cc) {
/*  969 */     cultureChunks.remove(cc.getChunkCoord());
/*      */   }
/*      */   
/*      */   public static CultureChunk getCultureChunk(Location location) {
/*  973 */     ChunkCoord coord = new ChunkCoord(location);
/*  974 */     return getCultureChunk(coord);
/*      */   }
/*      */   
/*      */   public static void processCulture() {
/*  978 */     TaskMaster.asyncTask("culture-process", new CultureProcessAsyncTask(), 0L);
/*      */   }
/*      */   
/*      */   public static void addPersistChunk(Location location, boolean b) {
/*  982 */     ChunkCoord coord = new ChunkCoord(location);
/*  983 */     persistChunks.put(coord, Boolean.valueOf(b));
/*      */   }
/*      */   
/*      */   public static boolean isPersistChunk(Location location) {
/*  987 */     ChunkCoord coord = new ChunkCoord(location);
/*  988 */     return ((Boolean)persistChunks.get(coord)).booleanValue();
/*      */   }
/*      */   
/*      */   public static Boolean isPersistChunk(Chunk chunk) {
/*  992 */     ChunkCoord coord = new ChunkCoord(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
/*  993 */     return (Boolean)persistChunks.get(coord);
/*      */   }
/*      */   
/*      */   public static void addPersistChunk(String worldname, int x, int z, boolean b) {
/*  997 */     ChunkCoord coord = new ChunkCoord(worldname, x, z);
/*  998 */     persistChunks.put(coord, Boolean.valueOf(b));
/*      */   }
/*      */   
/*      */   public static Location getLocationFromHash(String hash) {
/* 1002 */     String[] split = hash.split(",");
/* 1003 */     Location loc = new Location(BukkitObjects.getWorld(split[0]), Double.valueOf(split[1]).doubleValue(), 
/* 1004 */       Double.valueOf(split[2]).doubleValue(), 
/* 1005 */       Double.valueOf(split[3]).doubleValue());
/* 1006 */     return loc;
/*      */   }
/*      */   
/*      */   public static void removeStructure(Structure structure) {
/* 1010 */     structures.remove(structure.getCorner());
/*      */   }
/*      */   
/*      */   public static void addStructure(Structure structure) {
/* 1014 */     structures.put(structure.getCorner(), structure);
/*      */   }
/*      */   
/*      */   public static Structure getStructure(BlockCoord center) {
/* 1018 */     return (Structure)structures.get(center);
/*      */   }
/*      */   
/*      */   public static void addStructureBlock(BlockCoord coord, Buildable owner, boolean damageable) {
/* 1022 */     StructureBlock sb = new StructureBlock(coord, owner);
/* 1023 */     sb.setDamageable(damageable);
/* 1024 */     structureBlocks.put(coord, sb);
/*      */     
/* 1026 */     String key = getXYKey(coord);
/* 1027 */     HashSet<Buildable> buildables = (HashSet)buildablesInChunk.get(key);
/* 1028 */     if (buildables == null) {
/* 1029 */       buildables = new HashSet();
/*      */     }
/* 1031 */     buildables.add(owner);
/* 1032 */     buildablesInChunk.put(key, buildables);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void removeStructureBlock(BlockCoord coord)
/*      */   {
/* 1045 */     StructureBlock sb = (StructureBlock)structureBlocks.get(coord);
/* 1046 */     if (sb == null) {
/* 1047 */       return;
/*      */     }
/* 1049 */     structureBlocks.remove(coord);
/*      */     
/* 1051 */     String key = getXYKey(coord);
/* 1052 */     HashSet<Buildable> buildables = (HashSet)buildablesInChunk.get(key);
/* 1053 */     if (buildables != null) {
/* 1054 */       buildables.remove(sb.getOwner());
/* 1055 */       if (buildables.size() > 0) {
/* 1056 */         buildablesInChunk.put(key, buildables);
/*      */       } else {
/* 1058 */         buildablesInChunk.remove(key);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static StructureBlock getStructureBlock(BlockCoord coord)
/*      */   {
/* 1075 */     return (StructureBlock)structureBlocks.get(coord);
/*      */   }
/*      */   
/*      */   public static HashSet<Buildable> getBuildablesAt(BlockCoord coord) {
/* 1079 */     return (HashSet)buildablesInChunk.get(getXYKey(coord));
/*      */   }
/*      */   
/*      */   public static String getXYKey(BlockCoord coord) {
/* 1083 */     return coord.getX() + ":" + coord.getZ() + ":" + coord.getWorldname();
/*      */   }
/*      */   
/*      */   public static Structure getStructureById(int id) {
/* 1087 */     for (Structure struct : structures.values()) {
/* 1088 */       if (struct.getId() == id) {
/* 1089 */         return struct;
/*      */       }
/*      */     }
/* 1092 */     return null;
/*      */   }
/*      */   
/*      */   public static StructureSign getStructureSign(BlockCoord coord) {
/* 1096 */     return (StructureSign)structureSigns.get(coord);
/*      */   }
/*      */   
/*      */   public static void addStructureSign(StructureSign sign) {
/* 1100 */     structureSigns.put(sign.getCoord(), sign);
/*      */   }
/*      */   
/*      */   public static void addStructureChest(StructureChest structChest) {
/* 1104 */     structureChests.put(structChest.getCoord(), structChest);
/*      */   }
/*      */   
/*      */   public static StructureChest getStructureChest(BlockCoord coord) {
/* 1108 */     return (StructureChest)structureChests.get(coord);
/*      */   }
/*      */   
/*      */   public static Iterator<Map.Entry<BlockCoord, Structure>> getStructureIterator() {
/* 1112 */     return structures.entrySet().iterator();
/*      */   }
/*      */   
/*      */   public static void addTradeGood(TradeGood good) {
/* 1116 */     tradeGoods.put(good.getCoord(), good);
/*      */   }
/*      */   
/*      */   public static TradeGood getTradeGood(BlockCoord coord) {
/* 1120 */     return (TradeGood)tradeGoods.get(coord);
/*      */   }
/*      */   
/*      */   public static Collection<TradeGood> getTradeGoods() {
/* 1124 */     return tradeGoods.values();
/*      */   }
/*      */   
/*      */   public static void addProtectedBlock(ProtectedBlock pb) {
/* 1128 */     protectedBlocks.put(pb.getCoord(), pb);
/*      */   }
/*      */   
/*      */   public static ProtectedBlock getProtectedBlock(BlockCoord coord) {
/* 1132 */     return (ProtectedBlock)protectedBlocks.get(coord);
/*      */   }
/*      */   
/*      */   public static SessionDatabase getSessionDB() {
/* 1136 */     return sdb;
/*      */   }
/*      */   
/*      */   public static int getLeftoverSize(HashMap<Integer, ItemStack> leftovers) {
/* 1140 */     int total = 0;
/* 1141 */     for (ItemStack stack : leftovers.values()) {
/* 1142 */       total += stack.getAmount();
/*      */     }
/* 1144 */     return total;
/*      */   }
/*      */   
/*      */   public static int getSecondsBetween(long t1, long t2) {
/* 1148 */     return (int)((t2 - t1) / 1000L);
/*      */   }
/*      */   
/*      */   public static boolean testFileFlag(String filename) {
/* 1152 */     File f = new File(filename);
/* 1153 */     if (f.exists())
/* 1154 */       return true;
/* 1155 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean hasTimeElapsed(SessionEntry se, int seconds) {
/* 1159 */     long now = System.currentTimeMillis();
/* 1160 */     int secondsBetween = getSecondsBetween(se.time, now);
/*      */     
/*      */ 
/* 1163 */     if (secondsBetween < seconds) {
/* 1164 */       return false;
/*      */     }
/* 1166 */     return true;
/*      */   }
/*      */   
/*      */   public static void removeStructureSign(StructureSign structureSign) {
/* 1170 */     structureSigns.remove(structureSign.getCoord());
/*      */   }
/*      */   
/*      */   public static void removeStructureChest(StructureChest structureChest) {
/* 1174 */     structureChests.remove(structureChest.getCoord());
/*      */   }
/*      */   
/*      */   public static void addFarmChunk(ChunkCoord coord, FarmChunk fc) {
/* 1178 */     farmChunks.put(coord, fc);
/* 1179 */     queueFarmChunk(fc);
/* 1180 */     farmGrowQueue.add(fc);
/*      */   }
/*      */   
/*      */   public static FarmChunk getFarmChunk(ChunkCoord coord) {
/* 1184 */     return (FarmChunk)farmChunks.get(coord);
/*      */   }
/*      */   
/*      */   public static Collection<FarmChunk> getFarmChunks() {
/* 1188 */     return farmChunks.values();
/*      */   }
/*      */   
/*      */   public static Date getNextUpkeepDate()
/*      */   {
/* 1193 */     EventTimer daily = (EventTimer)EventTimer.timers.get("daily");
/* 1194 */     return daily.getNext().getTime();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void removeTownChunk(TownChunk tc)
/*      */   {
/* 1219 */     if (tc.getChunkCoord() != null) {
/* 1220 */       townChunks.remove(tc.getChunkCoord());
/*      */     }
/*      */   }
/*      */   
/*      */   public static Date getNextHourlyTickDate() {
/* 1225 */     EventTimer hourly = (EventTimer)EventTimer.timers.get("hourly");
/* 1226 */     return hourly.getNext().getTime();
/*      */   }
/*      */   
/*      */   public static void removeFarmChunk(ChunkCoord coord) {
/* 1230 */     farmChunks.remove(coord);
/*      */   }
/*      */   
/*      */   public static void addProtectedItemFrame(ItemFrameStorage framestore) {
/* 1234 */     protectedItemFrames.put(framestore.getUUID(), framestore);
/* 1235 */     ItemFrameStorage.attachedBlockMap.put(framestore.getAttachedBlock(), framestore);
/*      */   }
/*      */   
/*      */   public static ItemFrameStorage getProtectedItemFrame(UUID id) {
/* 1239 */     return (ItemFrameStorage)protectedItemFrames.get(id);
/*      */   }
/*      */   
/*      */   public static void removeProtectedItemFrame(UUID id) {
/* 1243 */     ItemFrameStorage store = getProtectedItemFrame(id);
/* 1244 */     ItemFrameStorage.attachedBlockMap.remove(store.getAttachedBlock());
/* 1245 */     protectedItemFrames.remove(id);
/*      */   }
/*      */   
/*      */   public static void addBonusGoodie(BonusGoodie goodie) {
/* 1249 */     bonusGoodies.put(goodie.getOutpost().getCorner(), goodie);
/*      */   }
/*      */   
/*      */   public static BonusGoodie getBonusGoodie(BlockCoord bcoord) {
/* 1253 */     return (BonusGoodie)bonusGoodies.get(bcoord);
/*      */   }
/*      */   
/*      */   public static Entity getEntityAtLocation(Location loc)
/*      */   {
/* 1258 */     Chunk chunk = loc.getChunk();
/* 1259 */     Entity[] arrayOfEntity; int j = (arrayOfEntity = chunk.getEntities()).length; for (int i = 0; i < j; i++) { Entity entity = arrayOfEntity[i];
/* 1260 */       if (entity.getLocation().getBlock().equals(loc.getBlock())) {
/* 1261 */         return entity;
/*      */       }
/*      */     }
/*      */     
/* 1265 */     return null;
/*      */   }
/*      */   
/*      */   public static BonusGoodie getBonusGoodie(ItemStack item) {
/* 1269 */     if (item == null) {
/* 1270 */       return null;
/*      */     }
/*      */     
/* 1273 */     if (ItemManager.getId(item) == 0) {
/* 1274 */       return null;
/*      */     }
/*      */     
/* 1277 */     ItemMeta meta = item.getItemMeta();
/* 1278 */     if (meta == null) {
/* 1279 */       return null;
/*      */     }
/*      */     
/* 1282 */     if ((!meta.hasLore()) || (meta.getLore().size() < BonusGoodie.LoreIndex.values().length)) {
/* 1283 */       return null;
/*      */     }
/*      */     
/* 1286 */     if (!((String)meta.getLore().get(BonusGoodie.LoreIndex.TYPE.ordinal())).equals("Bonus Goodie")) {
/* 1287 */       return null;
/*      */     }
/*      */     
/* 1290 */     String outpostLocation = (String)meta.getLore().get(BonusGoodie.LoreIndex.OUTPOSTLOCATION.ordinal());
/* 1291 */     BlockCoord bcoord = new BlockCoord(outpostLocation);
/* 1292 */     return getBonusGoodie(bcoord);
/*      */   }
/*      */   
/*      */   public static Collection<BonusGoodie> getBonusGoodies() {
/* 1296 */     return bonusGoodies.values();
/*      */   }
/*      */   
/*      */   public static void checkForDuplicateGoodies()
/*      */   {
/* 1301 */     HashMap<String, Boolean> outpostsInFrames = new HashMap();
/*      */     
/* 1303 */     for (ItemFrameStorage fs : protectedItemFrames.values()) {
/*      */       try {
/* 1305 */         if ((fs.noFrame()) || (fs.isEmpty())) {
/*      */           continue;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1313 */         goodie = getBonusGoodie(fs.getItem());
/*      */       }
/*      */       catch (CivException e)
/*      */       {
/* 1309 */         e.printStackTrace();
/*      */       }
/*      */       
/*      */       BonusGoodie goodie;
/*      */       
/* 1314 */       if (goodie != null)
/*      */       {
/*      */ 
/*      */ 
/* 1318 */         if (outpostsInFrames.containsKey(goodie.getOutpost().getCorner().toString()))
/*      */         {
/*      */ 
/*      */ 
/* 1322 */           fs.clearItem();
/*      */         } else {
/* 1324 */           outpostsInFrames.put(goodie.getOutpost().getCorner().toString(), Boolean.valueOf(true));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkForEmptyDuplicateFrames(ItemFrameStorage frame)
/*      */   {
/* 1339 */     if (frame.noFrame()) {
/* 1340 */       return;
/*      */     }
/*      */     
/* 1343 */     Chunk chunk = frame.getLocation().getChunk();
/* 1344 */     ArrayList<Entity> removed = new ArrayList();
/* 1345 */     HashMap<Integer, Boolean> droppedItems = new HashMap();
/*      */     try
/*      */     {
/* 1348 */       if (!frame.isEmpty()) {
/* 1349 */         droppedItems.put(Integer.valueOf(ItemManager.getId(frame.getItem())), Boolean.valueOf(true));
/*      */       }
/*      */     } catch (CivException e1) {
/* 1352 */       e1.printStackTrace();
/*      */     }
/*      */     Entity[] arrayOfEntity;
/* 1355 */     int j = (arrayOfEntity = chunk.getEntities()).length; for (int i = 0; i < j; i++) { Entity entity = arrayOfEntity[i];
/* 1356 */       if (((entity instanceof ItemFrame)) && 
/* 1357 */         (!frame.isOurEntity(entity)))
/*      */       {
/*      */ 
/*      */ 
/* 1361 */         int x = frame.getLocation().getBlockX();
/* 1362 */         int y = frame.getLocation().getBlockY();
/* 1363 */         int z = frame.getLocation().getBlockZ();
/*      */         
/*      */ 
/* 1366 */         if ((x == entity.getLocation().getBlockX()) && 
/* 1367 */           (y == entity.getLocation().getBlockY()) && 
/* 1368 */           (z == entity.getLocation().getBlockZ()))
/*      */         {
/*      */ 
/* 1371 */           ItemFrame eFrame = (ItemFrame)entity;
/* 1372 */           boolean eFrameEmpty = (eFrame.getItem() == null) || (eFrame.getItem().getType().equals(Material.AIR));
/*      */           
/* 1374 */           if (!eFrameEmpty) {
/* 1375 */             Boolean droppedAlready = (Boolean)droppedItems.get(Integer.valueOf(ItemManager.getId(eFrame.getItem())));
/* 1376 */             if ((droppedAlready == null) || (!droppedAlready.booleanValue())) {
/* 1377 */               droppedItems.put(Integer.valueOf(ItemManager.getId(eFrame.getItem())), Boolean.valueOf(true));
/* 1378 */               eFrame.getLocation().getWorld().dropItemNaturally(eFrame.getLocation(), eFrame.getItem());
/*      */             }
/*      */           }
/*      */           
/* 1382 */           removed.add(eFrame);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1388 */     for (Entity e : removed) {
/* 1389 */       e.remove();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static Entity getEntityClassFromUUID(World world, Class<?> c, UUID id)
/*      */   {
/* 1396 */     for (Entity e : world.getEntitiesByClasses(new Class[] { c })) {
/* 1397 */       if (e.getUniqueId().equals(id)) {
/* 1398 */         return e;
/*      */       }
/*      */     }
/* 1401 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Date getNextRandomEventTime()
/*      */   {
/* 1413 */     EventTimer repo = (EventTimer)EventTimer.timers.get("random");
/* 1414 */     return repo.getNext().getTime();
/*      */   }
/*      */   
/*      */   public static Date getNextRepoTime() {
/* 1418 */     EventTimer repo = (EventTimer)EventTimer.timers.get("repo-goodies");
/* 1419 */     return repo.getNext().getTime();
/*      */   }
/*      */   
/*      */   public static Buildable getNearestBuildable(Location location) {
/* 1423 */     Buildable nearest = null;
/* 1424 */     double lowest_distance = Double.MAX_VALUE;
/*      */     
/* 1426 */     for (Buildable struct : structures.values()) {
/* 1427 */       double distance = struct.getCorner().getLocation().distance(location);
/* 1428 */       if (distance < lowest_distance) {
/* 1429 */         lowest_distance = distance;
/* 1430 */         nearest = struct;
/*      */       }
/*      */     }
/*      */     
/* 1434 */     for (Buildable struct : wonders.values()) {
/* 1435 */       double distance = struct.getCorner().getLocation().distance(location);
/* 1436 */       if (distance < lowest_distance) {
/* 1437 */         lowest_distance = distance;
/* 1438 */         nearest = struct;
/*      */       }
/*      */     }
/*      */     
/* 1442 */     return nearest;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void movePlayersFromCulture(Civilization fromCiv, Civilization toCiv) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setAggressor(Civilization civ, Civilization otherCiv, Civilization aggressor)
/*      */   {
/* 1473 */     civ.getDiplomacyManager().setAggressor(aggressor, otherCiv);
/* 1474 */     otherCiv.getDiplomacyManager().setAggressor(aggressor, civ);
/*      */   }
/*      */   
/*      */   public static void setRelation(Civilization civ, Civilization otherCiv, Relation.Status status) {
/* 1478 */     if (civ.getId() == otherCiv.getId()) {
/* 1479 */       return;
/*      */     }
/*      */     
/* 1482 */     civ.getDiplomacyManager().setRelation(otherCiv, status, null);
/* 1483 */     otherCiv.getDiplomacyManager().setRelation(civ, status, null);
/*      */     
/* 1485 */     String out = civ.getName() + " is now ";
/* 1486 */     switch (status) {
/*      */     case ALLY: 
/* 1488 */       out = out + "NEUTRAL with ";
/* 1489 */       break;
/*      */     case HOSTILE: 
/* 1491 */       out = out + "§eHOSTILE§f towards ";
/* 1492 */       break;
/*      */     case NEUTRAL: 
/* 1494 */       out = out + "at §cWAR§f with ";
/* 1495 */       break;
/*      */     case PEACE: 
/* 1497 */       out = out + "at PEACE with ";
/* 1498 */       break;
/*      */     case WAR: 
/* 1500 */       out = out + "§a ALLIED §f with ";
/* 1501 */       break;
/*      */     }
/*      */     
/*      */     
/* 1505 */     out = out + otherCiv.getName();
/* 1506 */     CivMessage.global(out);
/* 1507 */     updateTagsBetween(civ, otherCiv);
/*      */   }
/*      */   
/*      */   private static void updateTagsBetween(Civilization civ, Civilization otherCiv) {
/* 1511 */     TaskMaster.asyncTask(new UpdateTagBetweenCivsTask(civ, otherCiv), 0L);
/*      */   }
/*      */   
/*      */   public static void requestRelation(Civilization fromCiv, Civilization toCiv, String question, long timeout, QuestionResponseInterface finishedFunction)
/*      */     throws CivException
/*      */   {
/* 1517 */     CivQuestionTask task = (CivQuestionTask)civQuestions.get(toCiv.getName());
/* 1518 */     if (task != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1523 */       throw new CivException("Civilization already has an offer pending, wait 30 seconds and try again.");
/*      */     }
/*      */     
/* 1526 */     task = new CivQuestionTask(toCiv, fromCiv, question, timeout, finishedFunction);
/* 1527 */     civQuestions.put(toCiv.getName(), task);
/* 1528 */     TaskMaster.asyncTask("", task, 0L);
/*      */   }
/*      */   
/*      */   public static void requestSurrender(Civilization fromCiv, Civilization toCiv, String question, long timeout, QuestionResponseInterface finishedFunction)
/*      */     throws CivException
/*      */   {
/* 1534 */     CivQuestionTask task = (CivQuestionTask)civQuestions.get(toCiv.getName());
/* 1535 */     if (task != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1540 */       throw new CivException("Civilization already has an offer pending, wait 30 seconds and try again.");
/*      */     }
/*      */     
/* 1543 */     task = new CivQuestionTask(toCiv, fromCiv, question, timeout, finishedFunction);
/* 1544 */     civQuestions.put(toCiv.getName(), task);
/* 1545 */     TaskMaster.asyncTask("", task, 0L);
/*      */   }
/*      */   
/* 1548 */   public static void removeRequest(String name) { civQuestions.remove(name); }
/*      */   
/*      */   public static CivQuestionTask getCivQuestionTask(Civilization senderCiv)
/*      */   {
/* 1552 */     return (CivQuestionTask)civQuestions.get(senderCiv.getName());
/*      */   }
/*      */   
/*      */   public static void checkForExpiredRelations() {
/* 1556 */     Date now = new Date();
/*      */     
/* 1558 */     ArrayList<Relation> deletedRelations = new ArrayList();
/* 1559 */     Iterator localIterator2; for (Iterator localIterator1 = getCivs().iterator(); localIterator1.hasNext(); 
/* 1560 */         localIterator2.hasNext())
/*      */     {
/* 1559 */       Civilization civ = (Civilization)localIterator1.next();
/* 1560 */       localIterator2 = civ.getDiplomacyManager().getRelations().iterator(); continue;Relation relation = (Relation)localIterator2.next();
/* 1561 */       if ((relation.getExpireDate() != null) && (now.after(relation.getExpireDate()))) {
/* 1562 */         deletedRelations.add(relation);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1567 */     for (Relation relation : deletedRelations) {
/*      */       try
/*      */       {
/* 1570 */         relation.delete();
/*      */       }
/*      */       catch (SQLException e) {
/* 1573 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean willInstantBreak(Material type)
/*      */   {
/* 1580 */     switch (type) {
/*      */     case AIR: 
/*      */     case BAKED_POTATO: 
/*      */     case BOOK_AND_QUILL: 
/*      */     case BOWL: 
/*      */     case BROWN_MUSHROOM: 
/*      */     case CAKE_BLOCK: 
/*      */     case CARPET: 
/*      */     case CAULDRON_ITEM: 
/*      */     case CHAINMAIL_CHESTPLATE: 
/*      */     case COAL: 
/*      */     case COBBLESTONE_STAIRS: 
/*      */     case COBBLE_WALL: 
/*      */     case COMPASS: 
/*      */     case COOKIE: 
/*      */     case DIAMOND_BOOTS: 
/*      */     case DIAMOND_PICKAXE: 
/*      */     case DIAMOND_SPADE: 
/*      */     case DIODE: 
/*      */     case DOUBLE_PLANT: 
/*      */     case ENCHANTMENT_TABLE: 
/*      */     case ENDER_CHEST: 
/*      */     case FENCE: 
/*      */     case FERMENTED_SPIDER_EYE: 
/*      */     case FIRE: 
/*      */     case FIREBALL: 
/*      */     case FLINT_AND_STEEL: 
/*      */     case GHAST_TEAR: 
/*      */     case GOLD_LEGGINGS: 
/*      */     case GOLD_NUGGET: 
/*      */     case GRAVEL: 
/*      */     case HAY_BLOCK: 
/*      */     case RECORD_4: 
/*      */     case SEEDS: 
/*      */     case SPONGE: 
/*      */     case STRING: 
/*      */     case TORCH: 
/* 1617 */       return true;
/*      */     }
/* 1619 */     return false;
/*      */   }
/*      */   
/*      */   public static String updateTag(Player namedPlayer, Player player)
/*      */   {
/* 1624 */     Resident namedRes = getResident(namedPlayer);
/* 1625 */     Resident playerRes = getResident(player);
/*      */     
/* 1627 */     if (isMutualOutlaw(namedRes, playerRes)) {
/* 1628 */       return "§4" + namedPlayer.getName();
/*      */     }
/*      */     
/* 1631 */     if ((namedRes == null) || (!namedRes.hasTown())) {
/* 1632 */       return namedPlayer.getName();
/*      */     }
/*      */     
/* 1635 */     if ((playerRes == null) || (!playerRes.hasTown())) {
/* 1636 */       return namedPlayer.getName();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1641 */     String color = "§f";
/* 1642 */     if (namedRes.getTown().getCiv() == playerRes.getTown().getCiv()) {
/* 1643 */       color = "§a";
/*      */     }
/*      */     else {
/* 1646 */       Relation.Status status = playerRes.getTown().getCiv().getDiplomacyManager().getRelationStatus(namedRes.getTown().getCiv());
/* 1647 */       switch (status) {
/*      */       case PEACE: 
/* 1649 */         color = "§b";
/* 1650 */         break;
/*      */       case WAR: 
/* 1652 */         color = "§a";
/* 1653 */         break;
/*      */       case HOSTILE: 
/* 1655 */         color = "§e";
/* 1656 */         break;
/*      */       case NEUTRAL: 
/* 1658 */         color = "§c";
/* 1659 */         break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/* 1665 */     return color + namedPlayer.getName();
/*      */   }
/*      */   
/*      */   public static boolean tradeGoodTooCloseToAnother(Location goodLoc, double radius) {
/* 1669 */     for (TradeGood tg : tradeGoods.values()) {
/* 1670 */       Location tgLoc = tg.getCoord().getLocation();
/*      */       
/* 1672 */       if (tgLoc.distance(goodLoc) < radius) {
/* 1673 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 1677 */     return false;
/*      */   }
/*      */   
/*      */   public static HashSet<Wall> getWallChunk(ChunkCoord coord) {
/* 1681 */     HashSet<Wall> walls = (HashSet)wallChunks.get(coord);
/* 1682 */     if ((walls != null) && (walls.size() > 0)) {
/* 1683 */       return walls;
/*      */     }
/* 1685 */     return null;
/*      */   }
/*      */   
/*      */   public static void addWallChunk(Wall wall, ChunkCoord coord)
/*      */   {
/* 1690 */     HashSet<Wall> walls = (HashSet)wallChunks.get(coord);
/*      */     
/* 1692 */     if (walls == null) {
/* 1693 */       walls = new HashSet();
/*      */     }
/*      */     
/* 1696 */     walls.add(wall);
/* 1697 */     wallChunks.put(coord, walls);
/* 1698 */     wall.wallChunks.add(coord);
/*      */   }
/*      */   
/*      */   public static void removeWallChunk(Wall wall, ChunkCoord coord) {
/* 1702 */     HashSet<Wall> walls = (HashSet)wallChunks.get(coord);
/*      */     
/* 1704 */     if (walls == null) {
/* 1705 */       walls = new HashSet();
/*      */     }
/* 1707 */     walls.remove(wall);
/* 1708 */     wallChunks.put(coord, walls);
/*      */   }
/*      */   
/*      */   public static void addWonder(Wonder wonder) {
/* 1712 */     wonders.put(wonder.getCorner(), wonder);
/*      */   }
/*      */   
/*      */   public static Wonder getWonder(BlockCoord coord) {
/* 1716 */     return (Wonder)wonders.get(coord);
/*      */   }
/*      */   
/*      */   public static void removeWonder(Wonder wonder) {
/* 1720 */     if (wonder.getCorner() != null) {
/* 1721 */       wonders.remove(wonder.getCorner());
/*      */     }
/*      */   }
/*      */   
/*      */   public static Collection<Wonder> getWonders() {
/* 1726 */     return wonders.values();
/*      */   }
/*      */   
/*      */   public static Wonder getWonderByConfigId(String id) {
/* 1730 */     for (Wonder wonder : wonders.values()) {
/* 1731 */       if (wonder.getConfigId().equals(id)) {
/* 1732 */         return wonder;
/*      */       }
/*      */     }
/* 1735 */     return null;
/*      */   }
/*      */   
/*      */   public static Wonder getWonderById(int id) {
/* 1739 */     for (Wonder wonder : wonders.values()) {
/* 1740 */       if (wonder.getId() == id) {
/* 1741 */         return wonder;
/*      */       }
/*      */     }
/*      */     
/* 1745 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static TreeMap<Double, Civilization> findNearestCivilizations(Town town)
/*      */   {
/* 1754 */     TownHall townhall = town.getTownHall();
/* 1755 */     TreeMap<Double, Civilization> returnMap = new TreeMap();
/*      */     
/* 1757 */     if (townhall == null) {
/* 1758 */       return returnMap;
/*      */     }
/*      */     
/* 1761 */     for (Civilization civ : getCivs()) {
/* 1762 */       if (civ != town.getCiv())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1767 */         double shortestDistance = Double.MAX_VALUE;
/* 1768 */         for (Town t : civ.getTowns()) {
/* 1769 */           TownHall tempTownHall = t.getTownHall();
/* 1770 */           if (tempTownHall != null)
/*      */           {
/*      */ 
/*      */ 
/* 1774 */             double tmpDistance = tempTownHall.getCorner().distanceSquared(townhall.getCorner());
/* 1775 */             if (tmpDistance < shortestDistance) {
/* 1776 */               shortestDistance = tmpDistance;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1781 */         returnMap.put(Double.valueOf(shortestDistance), civ);
/*      */       }
/*      */     }
/*      */     
/* 1785 */     return returnMap;
/*      */   }
/*      */   
/*      */   public static OfflinePlayer getFakeOfflinePlayer(String name) {
/* 1789 */     return Bukkit.getOfflinePlayer(name);
/*      */   }
/*      */   
/*      */   public static Collection<CultureChunk> getCultureChunks() {
/* 1793 */     return cultureChunks.values();
/*      */   }
/*      */   
/*      */   public static void addCustomMarker(Location location, String name, String desc, String icon) {
/* 1797 */     CustomMapMarker marker = new CustomMapMarker();
/* 1798 */     marker.name = name;
/* 1799 */     marker.description = desc;
/* 1800 */     marker.icon = icon;
/* 1801 */     customMapMarkers.put(new BlockCoord(location), marker);
/*      */   }
/*      */   
/*      */   public static void removeCustomMarker(Location location) {
/* 1805 */     customMapMarkers.remove(new BlockCoord(location));
/*      */   }
/*      */   
/*      */   public static void removeCustomMarker(BlockCoord coord) {
/* 1809 */     customMapMarkers.remove(coord);
/*      */   }
/*      */   
/*      */   public static Collection<CustomMapMarker> getCustomMarkers() {
/* 1813 */     return customMapMarkers.values();
/*      */   }
/*      */   
/*      */   public static Collection<TownChunk> getTownChunks() {
/* 1817 */     return townChunks.values();
/*      */   }
/*      */   
/*      */   public static Integer getScoreForCiv(Civilization civ) {
/* 1821 */     for (Map.Entry<Integer, Civilization> entry : civilizationScores.entrySet()) {
/* 1822 */       if (civ == entry.getValue()) {
/* 1823 */         return (Integer)entry.getKey();
/*      */       }
/*      */     }
/* 1826 */     return Integer.valueOf(0);
/*      */   }
/*      */   
/*      */   public static Collection<StructureSign> getStructureSigns() {
/* 1830 */     return structureSigns.values();
/*      */   }
/*      */   
/*      */   public static ArrayList<String> getNearbyPlayers(BlockCoord coord, double range) {
/* 1834 */     ArrayList<String> playerNames = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1839 */     return playerNames;
/*      */   }
/*      */   
/*      */   public static boolean isMutualOutlaw(Resident defenderResident, Resident attackerResident)
/*      */   {
/* 1844 */     if ((defenderResident == null) || (attackerResident == null)) {
/* 1845 */       return false;
/*      */     }
/*      */     
/* 1848 */     if ((defenderResident.hasTown()) && (defenderResident.getTown().isOutlaw(attackerResident.getName()))) {
/* 1849 */       return true;
/*      */     }
/*      */     
/* 1852 */     if ((attackerResident.hasTown()) && (attackerResident.getTown().isOutlaw(defenderResident.getName()))) {
/* 1853 */       return true;
/*      */     }
/*      */     
/* 1856 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isOutlawHere(Resident resident, TownChunk tc) {
/* 1860 */     if (tc == null) {
/* 1861 */       return false;
/*      */     }
/*      */     
/* 1864 */     if (tc.getTown() == null) {
/* 1865 */       return false;
/*      */     }
/*      */     
/* 1868 */     if (tc.getTown().isOutlaw(resident.getName())) {
/* 1869 */       return true;
/*      */     }
/* 1871 */     return false;
/*      */   }
/*      */   
/*      */   public static Date getTodaysSpawnRegenDate() {
/* 1875 */     Calendar now = Calendar.getInstance();
/* 1876 */     Calendar nextSpawn = Calendar.getInstance();
/*      */     
/*      */     try
/*      */     {
/* 1880 */       hourOfDay = CivSettings.getInteger(CivSettings.civConfig, "global.regen_spawn_hour").intValue();
/*      */     } catch (InvalidConfiguration e) { int hourOfDay;
/* 1882 */       e.printStackTrace();
/* 1883 */       return null;
/*      */     }
/*      */     int hourOfDay;
/* 1886 */     nextSpawn.set(11, hourOfDay);
/* 1887 */     nextSpawn.set(12, 0);
/* 1888 */     nextSpawn.set(13, 0);
/*      */     
/* 1890 */     if (nextSpawn.after(now)) {
/* 1891 */       return nextSpawn.getTime();
/*      */     }
/*      */     
/* 1894 */     nextSpawn.add(5, 1);
/* 1895 */     nextSpawn.set(11, hourOfDay);
/* 1896 */     nextSpawn.set(12, 0);
/* 1897 */     nextSpawn.set(13, 0);
/*      */     
/* 1899 */     return nextSpawn.getTime();
/*      */   }
/*      */   
/*      */   public static void addConqueredCiv(Civilization civ) {
/* 1903 */     conqueredCivs.put(civ.getName().toLowerCase(), civ);
/*      */   }
/*      */   
/*      */   public static void removeConqueredCiv(Civilization civ) {
/* 1907 */     conqueredCivs.remove(civ.getName().toLowerCase());
/*      */   }
/*      */   
/*      */   public static Civilization getConqueredCiv(String name) {
/* 1911 */     return (Civilization)conqueredCivs.get(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public static Collection<Civilization> getConqueredCivs() {
/* 1915 */     return conqueredCivs.values();
/*      */   }
/*      */   
/*      */   public static Civilization getConqueredCivFromId(int id) {
/* 1919 */     for (Civilization civ : ) {
/* 1920 */       if (civ.getId() == id) {
/* 1921 */         return civ;
/*      */       }
/*      */     }
/* 1924 */     return null;
/*      */   }
/*      */   
/*      */   public static Camp getCamp(String name) {
/* 1928 */     return (Camp)camps.get(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public static void addCamp(Camp camp) {
/* 1932 */     camps.put(camp.getName().toLowerCase(), camp);
/*      */   }
/*      */   
/*      */   public static void removeCamp(String name) {
/* 1936 */     camps.remove(name.toLowerCase());
/*      */   }
/*      */   
/*      */   public static void addCampBlock(CampBlock cb) {
/* 1940 */     campBlocks.put(cb.getCoord(), cb);
/*      */     
/* 1942 */     ChunkCoord coord = new ChunkCoord(cb.getCoord());
/* 1943 */     campChunks.put(coord, cb.getCamp());
/*      */   }
/*      */   
/*      */   public static CampBlock getCampBlock(BlockCoord bcoord) {
/* 1947 */     return (CampBlock)campBlocks.get(bcoord);
/*      */   }
/*      */   
/*      */   public static void removeCampBlock(BlockCoord bcoord) {
/* 1951 */     campBlocks.remove(bcoord);
/*      */   }
/*      */   
/*      */   public static Collection<Camp> getCamps() {
/* 1955 */     return camps.values();
/*      */   }
/*      */   
/*      */   public static Camp getCampFromChunk(ChunkCoord coord) {
/* 1959 */     return (Camp)campChunks.get(coord);
/*      */   }
/*      */   
/*      */   public static void removeCampChunk(ChunkCoord coord) {
/* 1963 */     campChunks.remove(coord);
/*      */   }
/*      */   
/*      */   public static Collection<Market> getMarkets() {
/* 1967 */     return markets.values();
/*      */   }
/*      */   
/*      */   public static void addMarket(Market market) {
/* 1971 */     markets.put(market.getCorner(), market);
/*      */   }
/*      */   
/*      */   public static void removeMarket(Market market) {
/* 1975 */     markets.remove(market.getCorner());
/*      */   }
/*      */   
/*      */   public static Camp getCampFromId(int campID) {
/* 1979 */     for (Camp camp : camps.values()) {
/* 1980 */       if (camp.getId() == campID) {
/* 1981 */         return camp;
/*      */       }
/*      */     }
/* 1984 */     return null;
/*      */   }
/*      */   
/*      */   public static Collection<Structure> getStructures() {
/* 1988 */     return structures.values();
/*      */   }
/*      */   
/*      */   public static void queueFarmChunk(FarmChunk fc) {
/* 1992 */     farmChunkUpdateQueue.add(fc);
/*      */   }
/*      */   
/*      */   public static FarmChunk pollFarmChunk() {
/* 1996 */     return (FarmChunk)farmChunkUpdateQueue.poll();
/*      */   }
/*      */   
/*      */   public static boolean farmChunkValid(FarmChunk fc) {
/* 2000 */     return farmChunks.containsKey(fc.getCoord());
/*      */   }
/*      */   
/*      */   public static Queue<FarmChunk> getFarmGrowQueue() {
/* 2004 */     return farmGrowQueue;
/*      */   }
/*      */   
/*      */   public static void setFarmGrowQueue(Queue<FarmChunk> farmGrowQueue) {
/* 2008 */     farmGrowQueue = farmGrowQueue;
/*      */   }
/*      */   
/*      */   public static void addRoadBlock(RoadBlock rb) {
/* 2012 */     roadBlocks.put(rb.getCoord(), rb);
/*      */   }
/*      */   
/*      */   public static void removeRoadBlock(RoadBlock rb) {
/* 2016 */     roadBlocks.remove(rb.getCoord());
/*      */   }
/*      */   
/*      */   public static RoadBlock getRoadBlock(BlockCoord coord) {
/* 2020 */     return (RoadBlock)roadBlocks.get(coord);
/*      */   }
/*      */   
/*      */   public static Collection<Civilization> getAdminCivs() {
/* 2024 */     return adminCivs.values();
/*      */   }
/*      */   
/*      */   public static void addAdminCiv(Civilization civ) {
/* 2028 */     adminCivs.put(civ.getName(), civ);
/*      */   }
/*      */   
/*      */   public static void removeAdminCiv(Civilization civ) {
/* 2032 */     adminCivs.remove(civ.getName());
/*      */   }
/*      */   
/*      */   public static String getPhase() {
/*      */     try {
/* 2037 */       return CivSettings.getStringBase("server_phase");
/*      */     } catch (InvalidConfiguration e) {
/* 2039 */       e.printStackTrace(); }
/* 2040 */     return "old";
/*      */   }
/*      */   
/*      */   public static boolean isCasualMode()
/*      */   {
/*      */     try {
/* 2046 */       String mode = CivSettings.getString(CivSettings.civConfig, "global.casual_mode");
/* 2047 */       if (mode.equalsIgnoreCase("true")) {
/* 2048 */         return true;
/*      */       }
/* 2050 */       return false;
/*      */     }
/*      */     catch (InvalidConfiguration e) {
/* 2053 */       e.printStackTrace(); }
/* 2054 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\main\CivGlobal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */