/*     */ package com.avrgaming.civcraft.interactive;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.wonders.Wonder;
/*     */ import com.avrgaming.civcraft.template.Template;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
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
/*     */ public class InteractiveBuildCommand
/*     */   implements InteractiveResponse
/*     */ {
/*     */   Town town;
/*     */   Buildable buildable;
/*     */   Location center;
/*     */   Template tpl;
/*     */   
/*     */   public InteractiveBuildCommand(Town town, Buildable buildable, Location center, Template tpl)
/*     */   {
/*  42 */     this.town = town;
/*  43 */     this.buildable = buildable;
/*  44 */     this.center = center.clone();
/*  45 */     this.tpl = tpl;
/*     */   }
/*     */   
/*     */   public void respond(String message, Resident resident)
/*     */   {
/*     */     try
/*     */     {
/*  52 */       player = CivGlobal.getPlayer(resident);
/*     */     } catch (CivException e) { Player player;
/*     */       return;
/*     */     }
/*     */     Player player;
/*  57 */     if (!message.equalsIgnoreCase("yes")) {
/*  58 */       CivMessage.sendError(player, "Build cancelled.");
/*  59 */       resident.clearInteractiveMode();
/*  60 */       resident.undoPreview();
/*  61 */       return;
/*     */     }
/*     */     
/*     */ 
/*  65 */     if (!this.buildable.validated) {
/*  66 */       CivMessage.sendError(player, "Structure position is not yet validated, please wait.");
/*  67 */       return;
/*     */     }
/*     */     
/*  70 */     if ((!this.buildable.isValid()) && (!player.isOp())) {
/*  71 */       CivMessage.sendError(player, "Structure is in an invalid position. The blocks below would not support the structure.");
/*  72 */       return;
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
/* 104 */     TaskMaster.syncTask(new Runnable()
/*     */     {
/*     */       Resident resident;
/*     */       
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  86 */           player = CivGlobal.getPlayer(this.resident);
/*     */         } catch (CivException e) { Player player;
/*  88 */           return;
/*     */         }
/*     */         try
/*     */         {
/*  92 */           if ((InteractiveBuildCommand.this.buildable instanceof Wonder)) {
/*  93 */             InteractiveBuildCommand.this.town.buildWonder(player, InteractiveBuildCommand.this.buildable.getConfigId(), InteractiveBuildCommand.this.center, InteractiveBuildCommand.this.tpl);
/*     */           } else {
/*  95 */             InteractiveBuildCommand.this.town.buildStructure(player, InteractiveBuildCommand.this.buildable.getConfigId(), InteractiveBuildCommand.this.center, InteractiveBuildCommand.this.tpl);
/*     */           }
/*  97 */           this.resident.clearInteractiveMode();
/*     */         } catch (CivException e) { Player player;
/*  99 */           CivMessage.sendError(player, e.getMessage());
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\interactive\InteractiveBuildCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */