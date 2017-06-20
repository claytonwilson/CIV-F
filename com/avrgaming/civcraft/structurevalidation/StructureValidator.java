/*     */ package com.avrgaming.civcraft.structurevalidation;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.BuildableLayer;
/*     */ import com.avrgaming.civcraft.template.TemplateStream;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.CallbackInterface;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.ChunkSnapshot;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureValidator
/*     */   implements Runnable
/*     */ {
/*  31 */   private static String playerName = null;
/*  32 */   private static Buildable buildable = null;
/*  33 */   private static String templateFilepath = null;
/*  34 */   private static BlockCoord cornerLoc = null;
/*  35 */   private static CallbackInterface callback = null;
/*     */   
/*     */ 
/*     */ 
/*  39 */   private static ReentrantLock validationLock = new ReentrantLock();
/*  40 */   private static TemplateStream tplStream = null;
/*     */   
/*     */ 
/*     */ 
/*  44 */   private static SyncLoadSnapshotsFromLayer layerLoadTask = new SyncLoadSnapshotsFromLayer();
/*     */   
/*     */ 
/*  47 */   private static HashMap<ChunkCoord, ChunkSnapshot> chunks = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private String iPlayerName = null;
/*  55 */   private Buildable iBuildable = null;
/*  56 */   private String iTemplateName = null;
/*  57 */   private BlockCoord iCornerLoc = null;
/*  58 */   private CallbackInterface iCallback = null;
/*     */   
/*     */   public static boolean isEnabled()
/*     */   {
/*     */     try {
/*  63 */       enabledStr = CivSettings.getString(CivSettings.civConfig, "global.structure_validation");
/*     */     } catch (InvalidConfiguration e) { String enabledStr;
/*  65 */       e.printStackTrace();
/*  66 */       return false;
/*     */     }
/*     */     String enabledStr;
/*  69 */     if (enabledStr.equalsIgnoreCase("true")) {
/*  70 */       return true;
/*     */     }
/*     */     
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   public StructureValidator(Player player, Buildable bld) {
/*  77 */     if (player != null) {
/*  78 */       this.iPlayerName = player.getName();
/*     */     }
/*  80 */     this.iBuildable = bld;
/*     */   }
/*     */   
/*     */   public StructureValidator(Player player, String templateName, Location cornerLoc, CallbackInterface callback) {
/*  84 */     if (player != null) {
/*  85 */       this.iPlayerName = player.getName();
/*     */     }
/*     */     
/*  88 */     this.iTemplateName = templateName;
/*  89 */     this.iCornerLoc = new BlockCoord(cornerLoc);
/*  90 */     this.iCallback = callback;
/*     */   }
/*     */   
/*     */   private static class SyncLoadSnapshotsFromLayer
/*     */     implements Runnable
/*     */   {
/*     */     public List<SimpleBlock> bottomLayer;
/*     */     public StructureValidator notifyTask;
/*     */     
/*     */     public void run()
/*     */     {
/* 101 */       BlockCoord corner = new BlockCoord(StructureValidator.cornerLoc);
/*     */       
/*     */ 
/*     */ 
/* 105 */       StructureValidator.chunks.clear();
/*     */       
/* 107 */       for (SimpleBlock sb : this.bottomLayer) {
/* 108 */         Block next = corner.getBlock().getRelative(sb.x, corner.getY(), sb.z);
/* 109 */         ChunkCoord coord = new ChunkCoord(next.getLocation());
/*     */         
/* 111 */         if (!StructureValidator.chunks.containsKey(coord))
/*     */         {
/*     */ 
/*     */ 
/* 115 */           StructureValidator.chunks.put(coord, next.getChunk().getChunkSnapshot());
/*     */         }
/*     */       }
/* 118 */       synchronized (this.notifyTask) {
/* 119 */         this.notifyTask.notify();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void finishValidate(HashMap<ChunkCoord, ChunkSnapshot> chunks, List<SimpleBlock> bottomLayer)
/*     */   {
/* 126 */     Player player = null;
/*     */     try {
/* 128 */       if (playerName != null) {
/* 129 */         player = CivGlobal.getPlayer(playerName);
/*     */       }
/*     */     }
/*     */     catch (CivException localCivException1) {}
/*     */     
/* 134 */     int checkedLevelCount = 0;
/* 135 */     boolean valid = true;
/* 136 */     String message = "";
/*     */     
/* 138 */     for (int y = cornerLoc.getY() - 1; y > 0; y--) {
/* 139 */       checkedLevelCount++;
/* 140 */       double totalBlocks = 0.0D;
/* 141 */       double reinforcementValue = 0.0D;
/*     */       
/* 143 */       for (SimpleBlock sb : bottomLayer)
/*     */       {
/* 145 */         if (sb.getType() != 0)
/*     */         {
/*     */ 
/*     */           try
/*     */           {
/*     */ 
/*     */ 
/* 152 */             int absX = cornerLoc.getX() + sb.x;
/* 153 */             int absZ = cornerLoc.getZ() + sb.z;
/*     */             
/* 155 */             int type = Buildable.getBlockIDFromSnapshotMap(chunks, absX, y, absZ, cornerLoc.getWorldname());
/* 156 */             totalBlocks += 1.0D;
/* 157 */             reinforcementValue += Buildable.getReinforcementValue(type);
/*     */           } catch (CivException e) {
/* 159 */             e.printStackTrace();
/* 160 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 164 */       double percentValid = reinforcementValue / totalBlocks;
/* 165 */       if (buildable != null)
/*     */       {
/* 167 */         buildable.layerValidPercentages.put(Integer.valueOf(y), new BuildableLayer((int)reinforcementValue, (int)totalBlocks));
/*     */       }
/*     */       
/* 170 */       if ((valid) && 
/* 171 */         (percentValid < Buildable.getReinforcementRequirementForLevel(checkedLevelCount))) {
/* 172 */         DecimalFormat df = new DecimalFormat();
/* 173 */         message = "Layer: " + y + " is " + df.format(percentValid * 100.0D) + "%(" + reinforcementValue + "/" + totalBlocks + 
/* 174 */           ") valid, it needs to be " + df.format(Buildable.validPercentRequirement * 100.0D) + "%.";
/* 175 */         valid = false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 181 */     if (buildable != null) {
/* 182 */       buildable.validated = true;
/* 183 */       buildable.invalidLayerMessage = message;
/* 184 */       buildable.setValid(valid);
/*     */     }
/*     */     
/* 187 */     if (player != null) {
/* 188 */       CivMessage.sendError(player, message);
/* 189 */       if (player.isOp()) {
/* 190 */         CivMessage.send(player, "§7Since you're OP we'll let you build here anyway.");
/* 191 */         valid = true;
/*     */       }
/*     */       
/* 194 */       if (valid) {
/* 195 */         CivMessage.send(player, "§aStructure position is valid.");
/* 196 */         if (buildable != null) {
/* 197 */           buildable.setValid(true);
/* 198 */           buildable.invalidLayerMessage = "";
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 203 */     if ((callback != null) && (
/* 204 */       (valid) || ((player != null) && (player.isOp())))) {
/* 205 */       callback.execute(playerName);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void cleanup()
/*     */   {
/* 211 */     playerName = null;
/* 212 */     buildable = null;
/* 213 */     cornerLoc = null;
/* 214 */     templateFilepath = null;
/* 215 */     callback = null;
/* 216 */     validationLock.unlock();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 314	com/avrgaming/civcraft/structurevalidation/StructureValidator:isEnabled	()Z
/*     */     //   3: ifne +20 -> 23
/*     */     //   6: aload_0
/*     */     //   7: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   10: iconst_1
/*     */     //   11: putfield 260	com/avrgaming/civcraft/structure/Buildable:validated	Z
/*     */     //   14: aload_0
/*     */     //   15: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   18: iconst_1
/*     */     //   19: invokevirtual 267	com/avrgaming/civcraft/structure/Buildable:setValid	(Z)V
/*     */     //   22: return
/*     */     //   23: getstatic 49	com/avrgaming/civcraft/structurevalidation/StructureValidator:validationLock	Ljava/util/concurrent/locks/ReentrantLock;
/*     */     //   26: invokevirtual 316	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */     //   29: aload_0
/*     */     //   30: getfield 98	com/avrgaming/civcraft/structurevalidation/StructureValidator:iPlayerName	Ljava/lang/String;
/*     */     //   33: putstatic 34	com/avrgaming/civcraft/structurevalidation/StructureValidator:playerName	Ljava/lang/String;
/*     */     //   36: aload_0
/*     */     //   37: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   40: ifnull +63 -> 103
/*     */     //   43: aload_0
/*     */     //   44: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   47: invokevirtual 319	com/avrgaming/civcraft/structure/Buildable:isIgnoreFloating	()Z
/*     */     //   50: ifeq +23 -> 73
/*     */     //   53: aload_0
/*     */     //   54: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   57: iconst_1
/*     */     //   58: putfield 260	com/avrgaming/civcraft/structure/Buildable:validated	Z
/*     */     //   61: aload_0
/*     */     //   62: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   65: iconst_1
/*     */     //   66: invokevirtual 267	com/avrgaming/civcraft/structure/Buildable:setValid	(Z)V
/*     */     //   69: invokestatic 322	com/avrgaming/civcraft/structurevalidation/StructureValidator:cleanup	()V
/*     */     //   72: return
/*     */     //   73: aload_0
/*     */     //   74: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   77: putstatic 36	com/avrgaming/civcraft/structurevalidation/StructureValidator:buildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   80: aload_0
/*     */     //   81: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   84: invokevirtual 324	com/avrgaming/civcraft/structure/Buildable:getCorner	()Lcom/avrgaming/civcraft/util/BlockCoord;
/*     */     //   87: putstatic 40	com/avrgaming/civcraft/structurevalidation/StructureValidator:cornerLoc	Lcom/avrgaming/civcraft/util/BlockCoord;
/*     */     //   90: aload_0
/*     */     //   91: getfield 100	com/avrgaming/civcraft/structurevalidation/StructureValidator:iBuildable	Lcom/avrgaming/civcraft/structure/Buildable;
/*     */     //   94: invokevirtual 328	com/avrgaming/civcraft/structure/Buildable:getSavedTemplatePath	()Ljava/lang/String;
/*     */     //   97: putstatic 38	com/avrgaming/civcraft/structurevalidation/StructureValidator:templateFilepath	Ljava/lang/String;
/*     */     //   100: goto +17 -> 117
/*     */     //   103: aload_0
/*     */     //   104: getfield 104	com/avrgaming/civcraft/structurevalidation/StructureValidator:iCornerLoc	Lcom/avrgaming/civcraft/util/BlockCoord;
/*     */     //   107: putstatic 40	com/avrgaming/civcraft/structurevalidation/StructureValidator:cornerLoc	Lcom/avrgaming/civcraft/util/BlockCoord;
/*     */     //   110: aload_0
/*     */     //   111: getfield 102	com/avrgaming/civcraft/structurevalidation/StructureValidator:iTemplateName	Ljava/lang/String;
/*     */     //   114: putstatic 38	com/avrgaming/civcraft/structurevalidation/StructureValidator:templateFilepath	Ljava/lang/String;
/*     */     //   117: aload_0
/*     */     //   118: getfield 106	com/avrgaming/civcraft/structurevalidation/StructureValidator:iCallback	Lcom/avrgaming/civcraft/util/CallbackInterface;
/*     */     //   121: putstatic 42	com/avrgaming/civcraft/structurevalidation/StructureValidator:callback	Lcom/avrgaming/civcraft/util/CallbackInterface;
/*     */     //   124: getstatic 51	com/avrgaming/civcraft/structurevalidation/StructureValidator:tplStream	Lcom/avrgaming/civcraft/template/TemplateStream;
/*     */     //   127: ifnonnull +19 -> 146
/*     */     //   130: new 331	com/avrgaming/civcraft/template/TemplateStream
/*     */     //   133: dup
/*     */     //   134: getstatic 38	com/avrgaming/civcraft/structurevalidation/StructureValidator:templateFilepath	Ljava/lang/String;
/*     */     //   137: invokespecial 333	com/avrgaming/civcraft/template/TemplateStream:<init>	(Ljava/lang/String;)V
/*     */     //   140: putstatic 51	com/avrgaming/civcraft/structurevalidation/StructureValidator:tplStream	Lcom/avrgaming/civcraft/template/TemplateStream;
/*     */     //   143: goto +12 -> 155
/*     */     //   146: getstatic 51	com/avrgaming/civcraft/structurevalidation/StructureValidator:tplStream	Lcom/avrgaming/civcraft/template/TemplateStream;
/*     */     //   149: getstatic 38	com/avrgaming/civcraft/structurevalidation/StructureValidator:templateFilepath	Ljava/lang/String;
/*     */     //   152: invokevirtual 334	com/avrgaming/civcraft/template/TemplateStream:setSource	(Ljava/lang/String;)V
/*     */     //   155: getstatic 51	com/avrgaming/civcraft/structurevalidation/StructureValidator:tplStream	Lcom/avrgaming/civcraft/template/TemplateStream;
/*     */     //   158: iconst_0
/*     */     //   159: invokevirtual 337	com/avrgaming/civcraft/template/TemplateStream:getBlocksForLayer	(I)Ljava/util/List;
/*     */     //   162: astore_1
/*     */     //   163: getstatic 56	com/avrgaming/civcraft/structurevalidation/StructureValidator:layerLoadTask	Lcom/avrgaming/civcraft/structurevalidation/StructureValidator$SyncLoadSnapshotsFromLayer;
/*     */     //   166: aload_1
/*     */     //   167: putfield 341	com/avrgaming/civcraft/structurevalidation/StructureValidator$SyncLoadSnapshotsFromLayer:bottomLayer	Ljava/util/List;
/*     */     //   170: getstatic 56	com/avrgaming/civcraft/structurevalidation/StructureValidator:layerLoadTask	Lcom/avrgaming/civcraft/structurevalidation/StructureValidator$SyncLoadSnapshotsFromLayer;
/*     */     //   173: aload_0
/*     */     //   174: putfield 343	com/avrgaming/civcraft/structurevalidation/StructureValidator$SyncLoadSnapshotsFromLayer:notifyTask	Lcom/avrgaming/civcraft/structurevalidation/StructureValidator;
/*     */     //   177: getstatic 56	com/avrgaming/civcraft/structurevalidation/StructureValidator:layerLoadTask	Lcom/avrgaming/civcraft/structurevalidation/StructureValidator$SyncLoadSnapshotsFromLayer;
/*     */     //   180: invokestatic 346	com/avrgaming/civcraft/threading/TaskMaster:syncTask	(Ljava/lang/Runnable;)V
/*     */     //   183: aload_0
/*     */     //   184: dup
/*     */     //   185: astore_2
/*     */     //   186: monitorenter
/*     */     //   187: aload_0
/*     */     //   188: invokevirtual 352	java/lang/Object:wait	()V
/*     */     //   191: aload_2
/*     */     //   192: monitorexit
/*     */     //   193: goto +6 -> 199
/*     */     //   196: aload_2
/*     */     //   197: monitorexit
/*     */     //   198: athrow
/*     */     //   199: aload_0
/*     */     //   200: getstatic 61	com/avrgaming/civcraft/structurevalidation/StructureValidator:chunks	Ljava/util/HashMap;
/*     */     //   203: aload_1
/*     */     //   204: invokevirtual 355	com/avrgaming/civcraft/structurevalidation/StructureValidator:finishValidate	(Ljava/util/HashMap;Ljava/util/List;)V
/*     */     //   207: goto +20 -> 227
/*     */     //   210: astore_1
/*     */     //   211: aload_1
/*     */     //   212: invokevirtual 357	java/lang/Exception:printStackTrace	()V
/*     */     //   215: invokestatic 322	com/avrgaming/civcraft/structurevalidation/StructureValidator:cleanup	()V
/*     */     //   218: goto +12 -> 230
/*     */     //   221: astore_3
/*     */     //   222: invokestatic 322	com/avrgaming/civcraft/structurevalidation/StructureValidator:cleanup	()V
/*     */     //   225: aload_3
/*     */     //   226: athrow
/*     */     //   227: invokestatic 322	com/avrgaming/civcraft/structurevalidation/StructureValidator:cleanup	()V
/*     */     //   230: return
/*     */     // Line number table:
/*     */     //   Java source line #222	-> byte code offset #0
/*     */     //   Java source line #223	-> byte code offset #6
/*     */     //   Java source line #224	-> byte code offset #14
/*     */     //   Java source line #225	-> byte code offset #22
/*     */     //   Java source line #229	-> byte code offset #23
/*     */     //   Java source line #233	-> byte code offset #29
/*     */     //   Java source line #234	-> byte code offset #36
/*     */     //   Java source line #235	-> byte code offset #43
/*     */     //   Java source line #236	-> byte code offset #53
/*     */     //   Java source line #237	-> byte code offset #61
/*     */     //   Java source line #274	-> byte code offset #69
/*     */     //   Java source line #238	-> byte code offset #72
/*     */     //   Java source line #240	-> byte code offset #73
/*     */     //   Java source line #241	-> byte code offset #80
/*     */     //   Java source line #242	-> byte code offset #90
/*     */     //   Java source line #243	-> byte code offset #100
/*     */     //   Java source line #244	-> byte code offset #103
/*     */     //   Java source line #245	-> byte code offset #110
/*     */     //   Java source line #247	-> byte code offset #117
/*     */     //   Java source line #252	-> byte code offset #124
/*     */     //   Java source line #253	-> byte code offset #130
/*     */     //   Java source line #254	-> byte code offset #143
/*     */     //   Java source line #255	-> byte code offset #146
/*     */     //   Java source line #258	-> byte code offset #155
/*     */     //   Java source line #261	-> byte code offset #163
/*     */     //   Java source line #262	-> byte code offset #170
/*     */     //   Java source line #263	-> byte code offset #177
/*     */     //   Java source line #266	-> byte code offset #183
/*     */     //   Java source line #267	-> byte code offset #187
/*     */     //   Java source line #266	-> byte code offset #191
/*     */     //   Java source line #270	-> byte code offset #199
/*     */     //   Java source line #271	-> byte code offset #207
/*     */     //   Java source line #272	-> byte code offset #211
/*     */     //   Java source line #274	-> byte code offset #215
/*     */     //   Java source line #273	-> byte code offset #221
/*     */     //   Java source line #274	-> byte code offset #222
/*     */     //   Java source line #275	-> byte code offset #225
/*     */     //   Java source line #274	-> byte code offset #227
/*     */     //   Java source line #276	-> byte code offset #230
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	231	0	this	StructureValidator
/*     */     //   162	42	1	bottomLayer	List<SimpleBlock>
/*     */     //   210	2	1	e	Exception
/*     */     //   185	12	2	Ljava/lang/Object;	Object
/*     */     //   221	5	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   187	193	196	finally
/*     */     //   196	198	196	finally
/*     */     //   29	69	210	java/lang/Exception
/*     */     //   73	207	210	java/lang/Exception
/*     */     //   29	69	221	finally
/*     */     //   73	215	221	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structurevalidation\StructureValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */