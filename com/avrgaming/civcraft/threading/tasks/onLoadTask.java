/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
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
/*     */ public class onLoadTask
/*     */   implements Runnable
/*     */ {
/*     */   public void run()
/*     */   {
/*  43 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/*  44 */     while (iter.hasNext()) {
/*  45 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/*     */       try
/*     */       {
/*     */         try {
/*  49 */           if ((struct.getSavedTemplatePath() == null) && (struct.hasTemplate())) {
/*  50 */             CivLog.warning("structure:" + struct.getDisplayName() + " did not have a template name set but says it needs one!");
/*  51 */             continue;
/*     */           }
/*     */           
/*  54 */           if (!struct.hasTemplate()) {
/*     */             continue;
/*     */           }
/*     */           
/*     */ 
/*  59 */           tpl = Template.getTemplate(struct.getSavedTemplatePath(), null);
/*     */         } catch (CivException|IOException e) { Template tpl;
/*  61 */           e.printStackTrace(); return;
/*     */         }
/*     */         
/*     */         Template tpl;
/*     */         
/*  66 */         if (struct.isActive()) {
/*  67 */           PostBuildSyncTask.start(tpl, struct);
/*     */         }
/*     */       } catch (Exception e) {
/*  70 */         CivLog.error("-----ON LOAD EXCEPTION-----");
/*  71 */         if (struct != null) {
/*  72 */           CivLog.error("Structure:" + struct.getDisplayName());
/*  73 */           if (struct.getTown() != null) {
/*  74 */             CivLog.error("Town:" + struct.getTown().getName());
/*     */           }
/*     */         }
/*     */         
/*  78 */         CivLog.error(e.getMessage());
/*  79 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*  83 */     for (Wonder wonder : CivGlobal.getWonders())
/*     */     {
/*     */       try
/*     */       {
/*     */         try
/*     */         {
/*  89 */           tpl = Template.getTemplate(wonder.getSavedTemplatePath(), null);
/*     */         } catch (CivException|IOException e) { Template tpl;
/*  91 */           e.printStackTrace(); return;
/*     */         }
/*     */         
/*     */         Template tpl;
/*  95 */         if (wonder.isActive())
/*     */         {
/*  97 */           PostBuildSyncTask.start(tpl, wonder);
/*     */         }
/*     */       } catch (Exception e) {
/* 100 */         CivLog.error("-----ON LOAD EXCEPTION-----");
/* 101 */         if (wonder != null) {
/* 102 */           CivLog.error("Structure:" + wonder.getDisplayName());
/* 103 */           if (wonder.getTown() != null) {
/* 104 */             CivLog.error("Town:" + wonder.getTown().getName());
/*     */           }
/*     */         }
/* 107 */         CivLog.error(e.getMessage());
/* 108 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 115 */     iter = CivGlobal.getStructureIterator();
/* 116 */     while (iter.hasNext()) {
/* 117 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/*     */       try {
/* 119 */         struct.onLoad();
/*     */       } catch (Exception e) {
/* 121 */         CivLog.error("-----ON LOAD EXCEPTION-----");
/* 122 */         if (struct != null) {
/* 123 */           CivLog.error("Structure:" + struct.getDisplayName());
/* 124 */           if (struct.getTown() != null) {
/* 125 */             CivLog.error("Town:" + struct.getTown().getName());
/*     */           }
/*     */         }
/* 128 */         CivLog.error(e.getMessage());
/* 129 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 134 */     for (Wonder wonder : CivGlobal.getWonders()) {
/*     */       try {
/* 136 */         wonder.onLoad();
/*     */       } catch (Exception e) {
/* 138 */         CivLog.error("-----ON LOAD EXCEPTION-----");
/* 139 */         if (wonder != null) {
/* 140 */           CivLog.error("Structure:" + wonder.getDisplayName());
/* 141 */           if (wonder.getTown() != null) {
/* 142 */             CivLog.error("Town:" + wonder.getTown().getName());
/*     */           }
/*     */         }
/* 145 */         CivLog.error(e.getMessage());
/* 146 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\onLoadTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */