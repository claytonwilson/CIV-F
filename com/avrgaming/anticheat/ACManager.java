/*     */ package com.avrgaming.anticheat;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivCraft;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.TimeTools;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import com.avrgaming.civcraft.war.WarAntiCheat;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Random;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.IllegalBlockSizeException;
/*     */ import javax.crypto.NoSuchPaddingException;
/*     */ import javax.crypto.ShortBufferException;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.messaging.Messenger;
/*     */ import org.bukkit.plugin.messaging.PluginMessageListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ACManager
/*     */   implements PluginMessageListener
/*     */ {
/*     */   static String versionNumber;
/*     */   static String key;
/*  45 */   static HashMap<String, Long> acceptedMods = new HashMap();
/*  46 */   static HashMap<String, Long> ivSpecs = new HashMap();
/*     */   static byte[] decrypted;
/*     */   
/*     */   public static void init() {
/*  50 */     Bukkit.getMessenger().registerOutgoingPluginChannel(CivCraft.getPlugin(), "CAC");
/*  51 */     Bukkit.getMessenger().registerIncomingPluginChannel(CivCraft.getPlugin(), "CAC", new ACManager());
/*     */     try
/*     */     {
/*  54 */       versionNumber = CivSettings.getString(CivSettings.nocheatConfig, "civcraft_ac_version");
/*  55 */       key = CivSettings.getString(CivSettings.nocheatConfig, "civcraft_ac_key");
/*  56 */       decrypted = new byte[32768];
/*     */     } catch (InvalidConfiguration e) {
/*  58 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void generateIvSpec(Player player, ByteBuffer buffer) {
/*  63 */     Random rand = new Random();
/*  64 */     long r = rand.nextLong();
/*     */     
/*  66 */     ivSpecs.put(player.getName(), Long.valueOf(r));
/*  67 */     buffer.putLong(r);
/*     */   }
/*     */   
/*     */   private static void writeKey(ByteBuffer buffer) {
/*  71 */     for (int i = 0; i < key.length(); i++) {
/*  72 */       buffer.putChar(key.charAt(i));
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
/*     */   public static void sendChallenge(Player player)
/*     */   {
/* 103 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  92 */           Player player = CivGlobal.getPlayer(ACManager.this);
/*  93 */           ByteBuffer buffer = ByteBuffer.allocate(24);
/*  94 */           ACManager.generateIvSpec(player, buffer);
/*  95 */           ACManager.writeKey(buffer);
/*     */           
/*  97 */           player.sendPluginMessage(CivCraft.getPlugin(), "CAC", buffer.array());
/*     */ 
/*     */         }
/*     */         catch (CivException localCivException) {}
/*     */       }
/*     */       
/* 103 */     }, TimeTools.toTicks(3L));
/*     */     
/* 105 */     if ((War.isWarTime()) && (!player.isOp()))
/*     */     {
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
/* 130 */       TaskMaster.syncTask(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try
/*     */           {
/* 117 */             Player player = CivGlobal.getPlayer(ACManager.this);
/* 118 */             Resident resident = CivGlobal.getResident(player);
/*     */             
/* 120 */             if (!resident.isUsesAntiCheat()) {
/* 121 */               WarAntiCheat.onWarTimePlayerCheck(resident);
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           catch (CivException localCivException) {}
/*     */         }
/*     */         
/*     */ 
/* 130 */       }, TimeTools.toTicks(30L));
/*     */     }
/*     */     
/* 133 */     Resident resident = CivGlobal.getResident(player);
/* 134 */     if ((resident != null) && (resident.isInsideArena()))
/*     */     {
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
/* 166 */       TaskMaster.syncTask(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try
/*     */           {
/* 145 */             Player player = CivGlobal.getPlayer(ACManager.this);
/* 146 */             Resident resident = CivGlobal.getResident(player);
/*     */             
/* 148 */             if (!resident.isUsesAntiCheat())
/*     */             {
/*     */ 
/* 151 */               resident.teleportHome();
/* 152 */               resident.restoreInventory();
/* 153 */               resident.setInsideArena(false);
/* 154 */               resident.save();
/*     */               
/* 156 */               CivMessage.send(resident, "§7You've been teleported home since you cannot be inside an arena without anti-cheat.");
/*     */ 
/*     */             }
/*     */             
/*     */ 
/*     */           }
/*     */           catch (CivException localCivException) {}
/*     */         }
/*     */         
/*     */ 
/* 166 */       }, TimeTools.toTicks(30L));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onPluginMessageReceived(String channel, Player player, byte[] messageRaw)
/*     */   {
/* 173 */     byte[] message = new byte[messageRaw.length - 1];
/* 174 */     for (int i = 1; i < messageRaw.length; i++) {
/* 175 */       message[(i - 1)] = messageRaw[i];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */     SecretKeySpec key = new SecretKeySpec(key.getBytes(), "DES");
/* 184 */     Long iv = (Long)ivSpecs.get(player.getName());
/* 185 */     if (iv == null) {
/* 186 */       CivMessage.sendError(player, "Invalid Auth Message(0xFFFFEEC1)");
/* 187 */       return;
/*     */     }
/*     */     
/* 190 */     IvParameterSpec ivSpec = new IvParameterSpec(ByteBuffer.allocate(8).putLong(iv.longValue()).array());
/*     */     
/*     */     try
/*     */     {
/* 194 */       Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
/* 195 */       cipher.init(2, key, ivSpec);
/* 196 */       decrypted = new byte[cipher.getOutputSize(message.length)];
/* 197 */       int dec_len = cipher.update(message, 0, message.length, decrypted, 0);
/* 198 */       dec_len += cipher.doFinal(decrypted, dec_len);
/*     */     }
/*     */     catch (NoSuchAlgorithmException|NoSuchPaddingException|InvalidKeyException|InvalidAlgorithmParameterException|ShortBufferException|IllegalBlockSizeException|BadPaddingException e1)
/*     */     {
/* 202 */       e1.printStackTrace();
/* 203 */       CivMessage.sendError(player, "Invalid Auth Message(0xFFFFEEA1)");
/* 204 */       return;
/*     */     }
/*     */     
/* 207 */     String decoded = "";
/* 208 */     byte[] arrayOfByte1; int j = (arrayOfByte1 = decrypted).length; for (int i = 0; i < j; i++) { byte b = arrayOfByte1[i];
/* 209 */       if (b != 0)
/*     */       {
/* 211 */         decoded = decoded + (char)b;
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 216 */       validate(player, decoded);
/* 217 */       Resident resident = CivGlobal.getResident(player);
/* 218 */       if (resident != null) {
/* 219 */         resident.setUsesAntiCheat(true);
/*     */       }
/*     */     }
/*     */     catch (CivException e) {
/* 223 */       CivMessage.sendError(player, "[CivCraft Anti-Cheat] Couldn't Verify your client");
/* 224 */       CivMessage.sendError(player, e.getMessage());
/* 225 */       CivLog.info("Failed to validate player:" + player.getName() + " Message:" + e.getMessage());
/*     */       
/* 227 */       return;
/*     */     }
/*     */     
/*     */ 
/* 231 */     CivMessage.sendSuccess(player, "You've been validated by CivCraft Anti-Cheat");
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void validate(Player player, String decodedMessage)
/*     */     throws CivException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: ldc_w 346
/*     */     //   4: invokevirtual 348	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
/*     */     //   7: astore_3
/*     */     //   8: aload_3
/*     */     //   9: arraylength
/*     */     //   10: iconst_1
/*     */     //   11: if_icmpge +14 -> 25
/*     */     //   14: new 291	com/avrgaming/civcraft/exception/CivException
/*     */     //   17: dup
/*     */     //   18: ldc_w 352
/*     */     //   21: invokespecial 354	com/avrgaming/civcraft/exception/CivException:<init>	(Ljava/lang/String;)V
/*     */     //   24: athrow
/*     */     //   25: aload_3
/*     */     //   26: iconst_0
/*     */     //   27: aaload
/*     */     //   28: ldc_w 355
/*     */     //   31: invokevirtual 348	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
/*     */     //   34: astore 4
/*     */     //   36: aload 4
/*     */     //   38: arraylength
/*     */     //   39: iconst_3
/*     */     //   40: if_icmpeq +14 -> 54
/*     */     //   43: new 291	com/avrgaming/civcraft/exception/CivException
/*     */     //   46: dup
/*     */     //   47: ldc_w 357
/*     */     //   50: invokespecial 354	com/avrgaming/civcraft/exception/CivException:<init>	(Ljava/lang/String;)V
/*     */     //   53: athrow
/*     */     //   54: aload 4
/*     */     //   56: iconst_2
/*     */     //   57: aaload
/*     */     //   58: invokestatic 359	java/lang/Boolean:valueOf	(Ljava/lang/String;)Ljava/lang/Boolean;
/*     */     //   61: invokevirtual 364	java/lang/Boolean:booleanValue	()Z
/*     */     //   64: istore 5
/*     */     //   66: iload 5
/*     */     //   68: ifeq +109 -> 177
/*     */     //   71: aconst_null
/*     */     //   72: astore 6
/*     */     //   74: aconst_null
/*     */     //   75: astore 7
/*     */     //   77: new 367	java/io/PrintWriter
/*     */     //   80: dup
/*     */     //   81: new 369	java/io/BufferedWriter
/*     */     //   84: dup
/*     */     //   85: new 371	java/io/FileWriter
/*     */     //   88: dup
/*     */     //   89: ldc_w 373
/*     */     //   92: iconst_1
/*     */     //   93: invokespecial 375	java/io/FileWriter:<init>	(Ljava/lang/String;Z)V
/*     */     //   96: invokespecial 378	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
/*     */     //   99: invokespecial 381	java/io/PrintWriter:<init>	(Ljava/io/Writer;)V
/*     */     //   102: astore 8
/*     */     //   104: aload 8
/*     */     //   106: aload_1
/*     */     //   107: invokeinterface 98 1 0
/*     */     //   112: invokevirtual 382	java/io/PrintWriter:println	(Ljava/lang/String;)V
/*     */     //   115: aload 8
/*     */     //   117: ifnull +59 -> 176
/*     */     //   120: aload 8
/*     */     //   122: invokevirtual 385	java/io/PrintWriter:close	()V
/*     */     //   125: goto +51 -> 176
/*     */     //   128: astore 6
/*     */     //   130: aload 8
/*     */     //   132: ifnull +8 -> 140
/*     */     //   135: aload 8
/*     */     //   137: invokevirtual 385	java/io/PrintWriter:close	()V
/*     */     //   140: aload 6
/*     */     //   142: athrow
/*     */     //   143: astore 7
/*     */     //   145: aload 6
/*     */     //   147: ifnonnull +10 -> 157
/*     */     //   150: aload 7
/*     */     //   152: astore 6
/*     */     //   154: goto +17 -> 171
/*     */     //   157: aload 6
/*     */     //   159: aload 7
/*     */     //   161: if_acmpeq +10 -> 171
/*     */     //   164: aload 6
/*     */     //   166: aload 7
/*     */     //   168: invokevirtual 388	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   171: aload 6
/*     */     //   173: athrow
/*     */     //   174: astore 6
/*     */     //   176: return
/*     */     //   177: aload 4
/*     */     //   179: iconst_1
/*     */     //   180: aaload
/*     */     //   181: getstatic 72	com/avrgaming/anticheat/ACManager:versionNumber	Ljava/lang/String;
/*     */     //   184: invokevirtual 394	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   187: ifne +14 -> 201
/*     */     //   190: new 291	com/avrgaming/civcraft/exception/CivException
/*     */     //   193: dup
/*     */     //   194: ldc_w 398
/*     */     //   197: invokespecial 354	com/avrgaming/civcraft/exception/CivException:<init>	(Ljava/lang/String;)V
/*     */     //   200: athrow
/*     */     //   201: iconst_1
/*     */     //   202: istore 6
/*     */     //   204: goto +237 -> 441
/*     */     //   207: aload_3
/*     */     //   208: iload 6
/*     */     //   210: aaload
/*     */     //   211: ldc_w 355
/*     */     //   214: invokevirtual 348	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
/*     */     //   217: astore 7
/*     */     //   219: aload 7
/*     */     //   221: arraylength
/*     */     //   222: iconst_1
/*     */     //   223: if_icmpge +14 -> 237
/*     */     //   226: new 291	com/avrgaming/civcraft/exception/CivException
/*     */     //   229: dup
/*     */     //   230: ldc_w 400
/*     */     //   233: invokespecial 354	com/avrgaming/civcraft/exception/CivException:<init>	(Ljava/lang/String;)V
/*     */     //   236: athrow
/*     */     //   237: getstatic 402	com/avrgaming/civcraft/config/CivSettings:validMods	Ljava/util/HashMap;
/*     */     //   240: aload 7
/*     */     //   242: iconst_0
/*     */     //   243: aaload
/*     */     //   244: invokevirtual 210	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   247: checkcast 405	com/avrgaming/civcraft/config/ConfigValidMod
/*     */     //   250: astore 8
/*     */     //   252: aload 8
/*     */     //   254: ifnonnull +50 -> 304
/*     */     //   257: new 291	com/avrgaming/civcraft/exception/CivException
/*     */     //   260: dup
/*     */     //   261: new 267	java/lang/StringBuilder
/*     */     //   264: dup
/*     */     //   265: ldc_w 407
/*     */     //   268: invokespecial 272	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   271: aload 7
/*     */     //   273: iconst_0
/*     */     //   274: aaload
/*     */     //   275: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   278: ldc_w 409
/*     */     //   281: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   284: aload 7
/*     */     //   286: iconst_1
/*     */     //   287: aaload
/*     */     //   288: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   291: ldc_w 411
/*     */     //   294: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   297: invokevirtual 277	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   300: invokespecial 354	com/avrgaming/civcraft/exception/CivException:<init>	(Ljava/lang/String;)V
/*     */     //   303: athrow
/*     */     //   304: iconst_0
/*     */     //   305: istore 9
/*     */     //   307: aload 8
/*     */     //   309: getfield 413	com/avrgaming/civcraft/config/ConfigValidMod:checksums	Ljava/util/LinkedList;
/*     */     //   312: invokevirtual 417	java/util/LinkedList:iterator	()Ljava/util/Iterator;
/*     */     //   315: astore 11
/*     */     //   317: goto +36 -> 353
/*     */     //   320: aload 11
/*     */     //   322: invokeinterface 423 1 0
/*     */     //   327: checkcast 105	java/lang/Long
/*     */     //   330: astore 10
/*     */     //   332: aload 7
/*     */     //   334: iconst_1
/*     */     //   335: aaload
/*     */     //   336: invokestatic 429	java/lang/Long:valueOf	(Ljava/lang/String;)Ljava/lang/Long;
/*     */     //   339: aload 10
/*     */     //   341: invokevirtual 432	java/lang/Long:equals	(Ljava/lang/Object;)Z
/*     */     //   344: ifeq +9 -> 353
/*     */     //   347: iconst_1
/*     */     //   348: istore 9
/*     */     //   350: goto +13 -> 363
/*     */     //   353: aload 11
/*     */     //   355: invokeinterface 433 1 0
/*     */     //   360: ifne -40 -> 320
/*     */     //   363: iload 9
/*     */     //   365: ifne +73 -> 438
/*     */     //   368: aload 7
/*     */     //   370: iconst_0
/*     */     //   371: aaload
/*     */     //   372: ldc_w 436
/*     */     //   375: invokevirtual 394	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   378: ifeq +10 -> 388
/*     */     //   381: aload 7
/*     */     //   383: iconst_0
/*     */     //   384: ldc_w 438
/*     */     //   387: aastore
/*     */     //   388: new 291	com/avrgaming/civcraft/exception/CivException
/*     */     //   391: dup
/*     */     //   392: new 267	java/lang/StringBuilder
/*     */     //   395: dup
/*     */     //   396: aload 7
/*     */     //   398: iconst_0
/*     */     //   399: aaload
/*     */     //   400: invokestatic 269	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   403: invokespecial 272	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   406: ldc_w 409
/*     */     //   409: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   412: aload 7
/*     */     //   414: iconst_1
/*     */     //   415: aaload
/*     */     //   416: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   419: ldc_w 411
/*     */     //   422: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   425: ldc_w 440
/*     */     //   428: invokevirtual 297	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   431: invokevirtual 277	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   434: invokespecial 354	com/avrgaming/civcraft/exception/CivException:<init>	(Ljava/lang/String;)V
/*     */     //   437: athrow
/*     */     //   438: iinc 6 1
/*     */     //   441: iload 6
/*     */     //   443: aload_3
/*     */     //   444: arraylength
/*     */     //   445: if_icmplt -238 -> 207
/*     */     //   448: return
/*     */     // Line number table:
/*     */     //   Java source line #235	-> byte code offset #0
/*     */     //   Java source line #236	-> byte code offset #8
/*     */     //   Java source line #237	-> byte code offset #14
/*     */     //   Java source line #240	-> byte code offset #25
/*     */     //   Java source line #241	-> byte code offset #36
/*     */     //   Java source line #242	-> byte code offset #43
/*     */     //   Java source line #245	-> byte code offset #54
/*     */     //   Java source line #246	-> byte code offset #66
/*     */     //   Java source line #247	-> byte code offset #71
/*     */     //   Java source line #247	-> byte code offset #77
/*     */     //   Java source line #248	-> byte code offset #104
/*     */     //   Java source line #249	-> byte code offset #115
/*     */     //   Java source line #252	-> byte code offset #176
/*     */     //   Java source line #255	-> byte code offset #177
/*     */     //   Java source line #256	-> byte code offset #190
/*     */     //   Java source line #259	-> byte code offset #201
/*     */     //   Java source line #260	-> byte code offset #207
/*     */     //   Java source line #261	-> byte code offset #219
/*     */     //   Java source line #262	-> byte code offset #226
/*     */     //   Java source line #265	-> byte code offset #237
/*     */     //   Java source line #266	-> byte code offset #252
/*     */     //   Java source line #267	-> byte code offset #257
/*     */     //   Java source line #270	-> byte code offset #304
/*     */     //   Java source line #271	-> byte code offset #307
/*     */     //   Java source line #272	-> byte code offset #332
/*     */     //   Java source line #273	-> byte code offset #347
/*     */     //   Java source line #274	-> byte code offset #350
/*     */     //   Java source line #271	-> byte code offset #353
/*     */     //   Java source line #278	-> byte code offset #363
/*     */     //   Java source line #279	-> byte code offset #368
/*     */     //   Java source line #280	-> byte code offset #381
/*     */     //   Java source line #282	-> byte code offset #388
/*     */     //   Java source line #259	-> byte code offset #438
/*     */     //   Java source line #287	-> byte code offset #448
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	449	0	this	ACManager
/*     */     //   0	449	1	player	Player
/*     */     //   0	449	2	decodedMessage	String
/*     */     //   7	437	3	mods	String[]
/*     */     //   34	144	4	versionArray	String[]
/*     */     //   64	3	5	validTrap	boolean
/*     */     //   72	1	6	localObject1	Object
/*     */     //   128	18	6	localObject2	Object
/*     */     //   152	20	6	localObject3	Object
/*     */     //   174	1	6	localIOException	java.io.IOException
/*     */     //   174	1	6	localIOException1	java.io.IOException
/*     */     //   202	244	6	i	int
/*     */     //   75	1	7	localObject4	Object
/*     */     //   143	24	7	localThrowable	Throwable
/*     */     //   217	196	7	modArray	String[]
/*     */     //   102	34	8	out	java.io.PrintWriter
/*     */     //   250	58	8	mod	com.avrgaming.civcraft.config.ConfigValidMod
/*     */     //   305	59	9	valid	boolean
/*     */     //   330	10	10	checksum	Long
/*     */     //   315	39	11	localIterator	java.util.Iterator
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   104	115	128	finally
/*     */     //   77	143	143	finally
/*     */     //   71	174	174	java/io/IOException
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\anticheat\ACManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */