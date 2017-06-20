/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigTech;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.threading.CivAsyncTask;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
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
/*     */ public class UpdateTechBar
/*     */   extends CivAsyncTask
/*     */ {
/*     */   private Civilization civ;
/*     */   
/*     */   public UpdateTechBar(Civilization civ)
/*     */   {
/*  38 */     this.civ = civ;
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*  44 */     Queue<SimpleBlock> sbs = new LinkedList();
/*     */     
/*  46 */     for (Town town : this.civ.getTowns()) {
/*  47 */       double percentageDone = 0.0D;
/*  48 */       TownHall townhall = town.getTownHall();
/*     */       
/*  50 */       if (townhall == null) {
/*  51 */         return;
/*     */       }
/*     */       
/*  54 */       if (!townhall.isActive()) {
/*  55 */         return;
/*     */       }
/*     */       
/*     */ 
/*  59 */       if (this.civ.getResearchTech() != null) {
/*  60 */         percentageDone = this.civ.getResearchProgress() / this.civ.getResearchTech().beaker_cost;
/*     */         
/*  62 */         int size = townhall.getTechBarSize();
/*  63 */         int blockCount = (int)(percentageDone * townhall.getTechBarSize());
/*     */         
/*  65 */         for (int i = 0; i < size; i++) {
/*  66 */           BlockCoord bcoord = townhall.getTechBarBlockCoord(i);
/*  67 */           if (bcoord != null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*  72 */             if (i <= blockCount) {
/*  73 */               SimpleBlock sb = new SimpleBlock(35, 5);
/*  74 */               sb.x = bcoord.getX();sb.y = bcoord.getY();sb.z = bcoord.getZ();
/*  75 */               sb.worldname = bcoord.getWorldname();
/*  76 */               sbs.add(sb);
/*     */             } else {
/*  78 */               SimpleBlock sb = new SimpleBlock(35, 15);
/*  79 */               sb.x = bcoord.getX();sb.y = bcoord.getY();sb.z = bcoord.getZ();
/*  80 */               sb.worldname = bcoord.getWorldname();
/*  81 */               sbs.add(sb);
/*     */             }
/*     */             
/*  84 */             townhall.addStructureBlock(townhall.getTechBar(i), false);
/*     */           }
/*     */         }
/*     */       } else {
/*  88 */         int size = townhall.getTechBarSize();
/*  89 */         for (int i = 0; i < size; i++) {
/*  90 */           BlockCoord bcoord = townhall.getTechBarBlockCoord(i);
/*  91 */           if (bcoord != null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*  96 */             SimpleBlock sb = new SimpleBlock(35, 15);
/*  97 */             sb.x = bcoord.getX();sb.y = bcoord.getY();sb.z = bcoord.getZ();
/*  98 */             sb.worldname = bcoord.getWorldname();
/*  99 */             sbs.add(sb);
/* 100 */             townhall.addStructureBlock(townhall.getTechBar(i), false);
/*     */           }
/*     */         }
/*     */       }
/* 104 */       if (townhall.getTechnameSign() != null) {
/* 105 */         BlockCoord bcoord = townhall.getTechnameSign();
/* 106 */         SimpleBlock sb = new SimpleBlock(68, townhall.getTechnameSignData());
/* 107 */         sb.x = bcoord.getX();sb.y = bcoord.getY();sb.z = bcoord.getZ();
/* 108 */         sb.worldname = bcoord.getWorldname();
/* 109 */         sb.specialType = SimpleBlock.Type.LITERAL;
/*     */         
/* 111 */         if (this.civ.getResearchTech() != null) {
/* 112 */           sb.message[0] = "Researching";
/* 113 */           sb.message[1] = "";
/* 114 */           sb.message[2] = this.civ.getResearchTech().name;
/* 115 */           sb.message[3] = "";
/*     */         } else {
/* 117 */           sb.message[0] = "Researching";
/* 118 */           sb.message[1] = "";
/* 119 */           sb.message[2] = "Nothing";
/* 120 */           sb.message[3] = "";
/*     */         }
/* 122 */         sbs.add(sb);
/*     */         
/* 124 */         townhall.addStructureBlock(townhall.getTechnameSign(), false);
/*     */       }
/*     */       
/*     */ 
/* 128 */       if (townhall.getTechdataSign() != null) {
/* 129 */         BlockCoord bcoord = townhall.getTechdataSign();
/* 130 */         SimpleBlock sb = new SimpleBlock(68, townhall.getTechdataSignData());
/* 131 */         sb.x = bcoord.getX();sb.y = bcoord.getY();sb.z = bcoord.getZ();
/* 132 */         sb.worldname = bcoord.getWorldname();
/* 133 */         sb.specialType = SimpleBlock.Type.LITERAL;
/*     */         
/* 135 */         if (this.civ.getResearchTech() != null) {
/* 136 */           percentageDone = Math.round(percentageDone * 100.0D);
/*     */           
/* 138 */           sb.message[0] = "Percent";
/* 139 */           sb.message[1] = "Complete";
/* 140 */           sb.message[2] = (percentageDone + "%");
/* 141 */           sb.message[3] = "";
/*     */         }
/*     */         else {
/* 144 */           sb.message[0] = "Use";
/* 145 */           sb.message[1] = "/civ research";
/* 146 */           sb.message[2] = "to start";
/* 147 */           sb.message[3] = "";
/*     */         }
/* 149 */         sbs.add(sb);
/*     */         
/*     */ 
/* 152 */         townhall.addStructureBlock(townhall.getTechdataSign(), false);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 157 */     updateBlocksQueue(sbs);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\UpdateTechBar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */