/*     */ package com.avrgaming.civcraft.recover;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivData;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.command.CommandSender;
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
/*     */ public class RecoverStructuresAsyncTask
/*     */   implements Runnable
/*     */ {
/*  45 */   public static HashSet<Integer> ignoreBlocks = new HashSet();
/*  46 */   boolean listOnly = false;
/*     */   CommandSender sender;
/*     */   
/*     */   public RecoverStructuresAsyncTask(CommandSender sender, boolean listonly) {
/*  50 */     this.sender = sender;
/*  51 */     this.listOnly = listonly;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isBrokenStructure(Structure struct)
/*     */   {
/*     */     try
/*     */     {
/*  59 */       tpl = Template.getTemplate(struct.getSavedTemplatePath(), null);
/*     */     } catch (IOException|CivException e) { Template tpl;
/*  61 */       e.printStackTrace();
/*  62 */       return false;
/*     */     }
/*     */     
/*     */     Template tpl;
/*  66 */     Block cornerBlock = struct.getCorner().getBlock();
/*  67 */     for (int x = 0; x < tpl.size_x; x++) {
/*  68 */       for (int y = 0; y < tpl.size_y; y++) {
/*  69 */         for (int z = 0; z < tpl.size_z; z++) {
/*  70 */           Block nextBlock = cornerBlock.getRelative(x, y, z);
/*     */           
/*  72 */           if (!ignoreBlocks.contains(Integer.valueOf(ItemManager.getId(nextBlock))))
/*     */           {
/*     */ 
/*     */ 
/*  76 */             if (!ignoreBlocks.contains(Integer.valueOf(tpl.blocks[x][y][z].getType())))
/*     */             {
/*     */ 
/*     */ 
/*  80 */               if (tpl.blocks[x][y][z].specialType == SimpleBlock.Type.NORMAL)
/*     */               {
/*     */ 
/*     */ 
/*  84 */                 if (!tpl.blocks[x][y][z].isAir())
/*     */                 {
/*     */ 
/*     */ 
/*  88 */                   if (ItemManager.getId(nextBlock) != tpl.blocks[x][y][z].getType())
/*     */                   {
/*     */ 
/*  91 */                     return true; } } }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  97 */     return false;
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
/*     */   public void run()
/*     */   {
/* 112 */     ignoreBlocks.add(Integer.valueOf(49));
/* 113 */     ignoreBlocks.add(Integer.valueOf(85));
/* 114 */     ignoreBlocks.add(CivData.LADDER);
/*     */     
/* 116 */     ArrayList<Structure> repairStructures = new ArrayList();
/*     */     
/* 118 */     Iterator<Map.Entry<BlockCoord, Structure>> iter = CivGlobal.getStructureIterator();
/* 119 */     while (iter.hasNext())
/*     */     {
/* 121 */       Structure struct = (Structure)((Map.Entry)iter.next()).getValue();
/* 122 */       if (struct.getSavedTemplatePath() != null)
/*     */       {
/*     */ 
/*     */ 
/* 126 */         if (struct.canRestoreFromTemplate())
/*     */         {
/*     */           try
/*     */           {
/*     */ 
/* 131 */             if (isBrokenStructure(struct)) {
/* 132 */               if (this.listOnly) {
/* 133 */                 CivMessage.send(this.sender, struct.getDisplayName() + " at " + "§e" + struct.getCorner());
/*     */               }
/*     */               
/*     */ 
/* 137 */               if (!this.listOnly) {
/* 138 */                 repairStructures.add(struct);
/*     */               }
/*     */             }
/*     */           } catch (Exception e) {
/* 142 */             e.printStackTrace();
/*     */           } }
/*     */       }
/*     */     }
/* 146 */     TaskMaster.syncTask(new RecoverStructureSyncTask(this.sender, repairStructures));
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\recover\RecoverStructuresAsyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */