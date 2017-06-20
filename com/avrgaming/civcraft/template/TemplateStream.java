/*     */ package com.avrgaming.civcraft.template;
/*     */ 
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Location;
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
/*     */ public class TemplateStream
/*     */ {
/*     */   private String source;
/*     */   private int sizeX;
/*     */   private int sizeY;
/*     */   private int sizeZ;
/*  63 */   ArrayList<SimpleBlock> blocks = new ArrayList();
/*  64 */   private int currentBlockCount = 0;
/*     */   
/*  66 */   private File sourceFile = null;
/*     */   
/*     */   public TemplateStream(String filepath) throws IOException {
/*  69 */     setSource(filepath);
/*     */   }
/*     */   
/*     */   private SimpleBlock getSimpleBlockFromLine(String line) {
/*  73 */     String[] locTypeSplit = line.split(",");
/*  74 */     String location = locTypeSplit[0];
/*  75 */     String type = locTypeSplit[1];
/*     */     
/*     */ 
/*  78 */     String[] locationSplit = location.split(":");
/*  79 */     int blockX = Integer.valueOf(locationSplit[0]).intValue();
/*  80 */     int blockY = Integer.valueOf(locationSplit[1]).intValue();
/*  81 */     int blockZ = Integer.valueOf(locationSplit[2]).intValue();
/*     */     
/*     */ 
/*  84 */     String[] typeSplit = type.split(":");
/*  85 */     int blockId = Integer.valueOf(typeSplit[0]).intValue();
/*  86 */     int blockData = Integer.valueOf(typeSplit[1]).intValue();
/*     */     
/*     */     SimpleBlock block;
/*     */     SimpleBlock block;
/*  90 */     if (this.currentBlockCount < this.blocks.size())
/*     */     {
/*  92 */       block = (SimpleBlock)this.blocks.get(this.currentBlockCount);
/*     */     }
/*     */     else {
/*  95 */       block = new SimpleBlock(blockId, blockData);
/*  96 */       this.blocks.add(block);
/*     */     }
/*  98 */     this.currentBlockCount += 1;
/*     */     
/* 100 */     block.x = blockX;
/* 101 */     block.y = blockY;
/* 102 */     block.z = blockZ;
/* 103 */     return block;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<SimpleBlock> getBlocksForLayer(int y)
/*     */     throws IOException
/*     */   {
/* 110 */     if (y > this.sizeY) {
/* 111 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 114 */     BufferedReader reader = new BufferedReader(new FileReader(this.sourceFile));
/* 115 */     LinkedList<SimpleBlock> returnBlocks = new LinkedList();
/*     */     
/*     */     try
/*     */     {
/* 119 */       reader.readLine();
/*     */       
/* 121 */       String line = reader.readLine();
/* 122 */       while (line != null) {
/* 123 */         String[] locTypeSplit = line.split(",");
/* 124 */         String location = locTypeSplit[0];
/* 125 */         String[] locationSplit = location.split(":");
/* 126 */         int blockY = Integer.valueOf(locationSplit[1]).intValue();
/*     */         
/* 128 */         if (blockY == y) {
/* 129 */           returnBlocks.add(getSimpleBlockFromLine(line));
/*     */         }
/*     */         
/* 132 */         line = reader.readLine();
/*     */       }
/* 134 */       return returnBlocks;
/*     */     } finally {
/* 136 */       reader.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<SimpleBlock> getTemplateBlocks()
/*     */     throws IOException
/*     */   {
/* 144 */     BufferedReader reader = new BufferedReader(new FileReader(this.sourceFile));
/* 145 */     LinkedList<SimpleBlock> returnBlocks = new LinkedList();
/*     */     
/*     */     try
/*     */     {
/* 149 */       reader.readLine();
/*     */       
/* 151 */       String line = reader.readLine();
/* 152 */       while (line != null) {
/* 153 */         returnBlocks.add(getSimpleBlockFromLine(line));
/* 154 */         line = reader.readLine();
/*     */       }
/* 156 */       return returnBlocks;
/*     */     } finally {
/* 158 */       reader.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void debugBuildBlocksHere(Location location)
/*     */   {
/* 167 */     for (SimpleBlock block : this.blocks) {
/* 168 */       BlockCoord bcoord = new BlockCoord(location);
/* 169 */       bcoord.setX(bcoord.getX() + block.x);
/* 170 */       bcoord.setY(bcoord.getY() + block.y);
/* 171 */       bcoord.setZ(bcoord.getZ() + block.z);
/* 172 */       ItemManager.setTypeIdAndData(bcoord.getBlock(), block.getType(), block.getData(), false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getSource()
/*     */   {
/* 179 */     return this.source;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void setSource(String filepath)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: putfield 210	com/avrgaming/civcraft/template/TemplateStream:source	Ljava/lang/String;
/*     */     //   5: aload_0
/*     */     //   6: new 212	java/io/File
/*     */     //   9: dup
/*     */     //   10: aload_1
/*     */     //   11: invokespecial 214	java/io/File:<init>	(Ljava/lang/String;)V
/*     */     //   14: putfield 34	com/avrgaming/civcraft/template/TemplateStream:sourceFile	Ljava/io/File;
/*     */     //   17: new 115	java/io/BufferedReader
/*     */     //   20: dup
/*     */     //   21: new 117	java/io/FileReader
/*     */     //   24: dup
/*     */     //   25: aload_0
/*     */     //   26: getfield 34	com/avrgaming/civcraft/template/TemplateStream:sourceFile	Ljava/io/File;
/*     */     //   29: invokespecial 119	java/io/FileReader:<init>	(Ljava/io/File;)V
/*     */     //   32: invokespecial 122	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
/*     */     //   35: astore_2
/*     */     //   36: aconst_null
/*     */     //   37: astore_3
/*     */     //   38: aload_2
/*     */     //   39: invokevirtual 128	java/io/BufferedReader:readLine	()Ljava/lang/String;
/*     */     //   42: astore_3
/*     */     //   43: aload_3
/*     */     //   44: ifnonnull +27 -> 71
/*     */     //   47: new 21	java/io/IOException
/*     */     //   50: dup
/*     */     //   51: new 216	java/lang/StringBuilder
/*     */     //   54: dup
/*     */     //   55: ldc -38
/*     */     //   57: invokespecial 220	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   60: aload_1
/*     */     //   61: invokevirtual 221	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   64: invokevirtual 225	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   67: invokespecial 228	java/io/IOException:<init>	(Ljava/lang/String;)V
/*     */     //   70: athrow
/*     */     //   71: aload_3
/*     */     //   72: ldc -27
/*     */     //   74: invokevirtual 48	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
/*     */     //   77: astore 4
/*     */     //   79: aload_0
/*     */     //   80: aload 4
/*     */     //   82: iconst_0
/*     */     //   83: aaload
/*     */     //   84: invokestatic 56	java/lang/Integer:valueOf	(Ljava/lang/String;)Ljava/lang/Integer;
/*     */     //   87: invokevirtual 62	java/lang/Integer:intValue	()I
/*     */     //   90: putfield 231	com/avrgaming/civcraft/template/TemplateStream:sizeX	I
/*     */     //   93: aload_0
/*     */     //   94: aload 4
/*     */     //   96: iconst_1
/*     */     //   97: aaload
/*     */     //   98: invokestatic 56	java/lang/Integer:valueOf	(Ljava/lang/String;)Ljava/lang/Integer;
/*     */     //   101: invokevirtual 62	java/lang/Integer:intValue	()I
/*     */     //   104: putfield 110	com/avrgaming/civcraft/template/TemplateStream:sizeY	I
/*     */     //   107: aload_0
/*     */     //   108: aload 4
/*     */     //   110: iconst_2
/*     */     //   111: aaload
/*     */     //   112: invokestatic 56	java/lang/Integer:valueOf	(Ljava/lang/String;)Ljava/lang/Integer;
/*     */     //   115: invokevirtual 62	java/lang/Integer:intValue	()I
/*     */     //   118: putfield 233	com/avrgaming/civcraft/template/TemplateStream:sizeZ	I
/*     */     //   121: goto +12 -> 133
/*     */     //   124: astore 5
/*     */     //   126: aload_2
/*     */     //   127: invokevirtual 135	java/io/BufferedReader:close	()V
/*     */     //   130: aload 5
/*     */     //   132: athrow
/*     */     //   133: aload_2
/*     */     //   134: invokevirtual 135	java/io/BufferedReader:close	()V
/*     */     //   137: return
/*     */     // Line number table:
/*     */     //   Java source line #183	-> byte code offset #0
/*     */     //   Java source line #184	-> byte code offset #5
/*     */     //   Java source line #185	-> byte code offset #17
/*     */     //   Java source line #188	-> byte code offset #36
/*     */     //   Java source line #189	-> byte code offset #38
/*     */     //   Java source line #190	-> byte code offset #43
/*     */     //   Java source line #191	-> byte code offset #47
/*     */     //   Java source line #194	-> byte code offset #71
/*     */     //   Java source line #195	-> byte code offset #79
/*     */     //   Java source line #196	-> byte code offset #93
/*     */     //   Java source line #197	-> byte code offset #107
/*     */     //   Java source line #198	-> byte code offset #121
/*     */     //   Java source line #199	-> byte code offset #126
/*     */     //   Java source line #200	-> byte code offset #130
/*     */     //   Java source line #199	-> byte code offset #133
/*     */     //   Java source line #201	-> byte code offset #137
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	138	0	this	TemplateStream
/*     */     //   0	138	1	filepath	String
/*     */     //   35	99	2	reader	BufferedReader
/*     */     //   37	35	3	line	String
/*     */     //   77	32	4	split	String[]
/*     */     //   124	7	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   36	124	124	finally
/*     */   }
/*     */   
/*     */   public int getSizeX()
/*     */   {
/* 204 */     return this.sizeX;
/*     */   }
/*     */   
/*     */   public void setSizeX(int sizeX) {
/* 208 */     this.sizeX = sizeX;
/*     */   }
/*     */   
/*     */   public int getSizeY() {
/* 212 */     return this.sizeY;
/*     */   }
/*     */   
/*     */   public void setSizeY(int sizeY) {
/* 216 */     this.sizeY = sizeY;
/*     */   }
/*     */   
/*     */   public int getSizeZ() {
/* 220 */     return this.sizeZ;
/*     */   }
/*     */   
/*     */   public void setSizeZ(int sizeZ) {
/* 224 */     this.sizeZ = sizeZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\template\TemplateStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */