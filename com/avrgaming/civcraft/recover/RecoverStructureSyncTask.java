/*     */ package com.avrgaming.civcraft.recover;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.structure.Structure;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock;
/*     */ import com.avrgaming.civcraft.util.SimpleBlock.Type;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
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
/*     */ public class RecoverStructureSyncTask
/*     */   implements Runnable
/*     */ {
/*     */   ArrayList<Structure> structures;
/*     */   CommandSender sender;
/*     */   
/*     */   public RecoverStructureSyncTask(CommandSender sender, ArrayList<Structure> structs)
/*     */   {
/*  44 */     this.structures = structs;
/*  45 */     this.sender = sender;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void repairStructure(Structure struct)
/*     */   {
/*     */     try
/*     */     {
/*  56 */       tpl = Template.getTemplate(struct.getSavedTemplatePath(), null);
/*     */     } catch (IOException|CivException e) { Template tpl;
/*  58 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     Template tpl;
/*  62 */     Block cornerBlock = struct.getCorner().getBlock();
/*  63 */     for (int x = 0; x < tpl.size_x; x++) {
/*  64 */       for (int y = 0; y < tpl.size_y; y++) {
/*  65 */         for (int z = 0; z < tpl.size_z; z++) {
/*  66 */           Block nextBlock = cornerBlock.getRelative(x, y, z);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */           if (tpl.blocks[x][y][z].specialType == SimpleBlock.Type.NORMAL)
/*     */           {
/*     */ 
/*     */ 
/*  80 */             if ((ItemManager.getId(nextBlock) == 7) || 
/*  81 */               (!tpl.blocks[x][y][z].isAir()))
/*     */             {
/*     */ 
/*     */               try
/*     */               {
/*     */ 
/*  87 */                 if (ItemManager.getId(nextBlock) != tpl.blocks[x][y][z].getType()) {
/*  88 */                   ItemManager.setTypeId(nextBlock, tpl.blocks[x][y][z].getType());
/*  89 */                   ItemManager.setData(nextBlock, tpl.blocks[x][y][z].getData());
/*     */                 }
/*     */               } catch (Exception e) {
/*  92 */                 CivLog.error(e.getMessage());
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/* 102 */     for (Structure struct : this.structures) {
/* 103 */       CivMessage.send(this.sender, "Reparing " + struct.getDisplayName() + " at " + "§e" + struct.getCorner());
/* 104 */       repairStructure(struct);
/*     */     }
/*     */     
/* 107 */     CivMessage.send(this.sender, "Structure repair finished.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\recover\RecoverStructureSyncTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */