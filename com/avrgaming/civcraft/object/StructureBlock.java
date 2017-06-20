/*     */ package com.avrgaming.civcraft.object;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import org.bukkit.block.Block;
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
/*     */ public class StructureBlock
/*     */   implements BuildableDamageBlock
/*     */ {
/*  31 */   private BlockCoord coord = null;
/*  32 */   private Buildable owner = null;
/*  33 */   private boolean damageable = true;
/*  34 */   private boolean alwaysDamage = false;
/*     */   
/*     */   public StructureBlock(BlockCoord coord, Buildable owner)
/*     */   {
/*  38 */     this.coord = coord;
/*  39 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public Buildable getOwner() {
/*  43 */     return this.owner;
/*     */   }
/*     */   
/*     */   public void setOwner(Buildable owner) {
/*  47 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public Town getTown() {
/*  51 */     return this.owner.getTown();
/*     */   }
/*     */   
/*     */   public Civilization getCiv() {
/*  55 */     return this.owner.getCiv();
/*     */   }
/*     */   
/*     */   public BlockCoord getCoord() {
/*  59 */     return this.coord;
/*     */   }
/*     */   
/*     */   public void setCoord(BlockCoord coord) {
/*  63 */     this.coord = coord;
/*     */   }
/*     */   
/*     */   public int getX() {
/*  67 */     return this.coord.getX();
/*     */   }
/*     */   
/*     */   public int getY() {
/*  71 */     return this.coord.getY();
/*     */   }
/*     */   
/*     */   public int getZ() {
/*  75 */     return this.coord.getZ();
/*     */   }
/*     */   
/*     */   public String getWorldname() {
/*  79 */     return this.coord.getWorldname();
/*     */   }
/*     */   
/*     */   public boolean isDamageable() {
/*  83 */     return this.damageable;
/*     */   }
/*     */   
/*     */   public void setDamageable(boolean damageable) {
/*  87 */     this.damageable = damageable;
/*     */   }
/*     */   
/*     */   public boolean canDestroyOnlyDuringWar() {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean allowDamageNow(Player player)
/*     */   {
/*  97 */     if (War.isWarTime())
/*     */     {
/*  99 */       if (getOwner().getMaxHitPoints() != 0) {
/* 100 */         Resident res = CivGlobal.getResident(player.getName());
/* 101 */         if (res == null) {
/* 102 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 106 */         if (res.hasTown()) {
/* 107 */           if (res.getTown().defeated) {
/* 108 */             CivMessage.sendError(player, "Cannot damage structures when your town has been defeated.");
/* 109 */             return false;
/*     */           }
/*     */           
/* 112 */           Civilization civ = res.getTown().getCiv();
/*     */           
/*     */ 
/* 115 */           if (civ.getDiplomacyManager().atWarWith(getCiv())) {
/* 116 */             if (this.alwaysDamage) {
/* 117 */               return true;
/*     */             }
/*     */             
/* 120 */             if (!isDamageable()) {
/* 121 */               CivMessage.sendError(player, "Cannot damage this structure block. Choose another.");
/* 122 */             } else if (CivGlobal.willInstantBreak(getCoord().getBlock().getType())) {
/* 123 */               CivMessage.sendError(player, "Cannot damage structure with this block, try another.");
/*     */             } else {
/* 125 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAlwaysDamage() {
/* 135 */     return this.alwaysDamage;
/*     */   }
/*     */   
/*     */   public void setAlwaysDamage(boolean alwaysDamage) {
/* 139 */     this.alwaysDamage = alwaysDamage;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\object\StructureBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */