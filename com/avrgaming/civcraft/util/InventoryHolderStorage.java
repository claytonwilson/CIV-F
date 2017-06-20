/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.DoubleChest;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.InventoryHolder;
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
/*     */ public class InventoryHolderStorage
/*     */ {
/*     */   private Location blockLocation;
/*     */   private String playerName;
/*     */   
/*     */   public InventoryHolderStorage(InventoryHolder holder, Location holderLocation)
/*     */   {
/*  40 */     if ((holder instanceof Player)) {
/*  41 */       Player player = (Player)holder;
/*  42 */       this.playerName = player.getName();
/*  43 */       this.blockLocation = null;
/*     */     } else {
/*  45 */       this.blockLocation = holderLocation;
/*     */     }
/*     */   }
/*     */   
/*     */   public InventoryHolderStorage(Location blockLoc) {
/*  50 */     this.blockLocation = blockLoc;
/*  51 */     this.playerName = null;
/*     */   }
/*     */   
/*     */   public InventoryHolderStorage(Player player) {
/*  55 */     this.blockLocation = null;
/*  56 */     this.playerName = player.getName();
/*     */   }
/*     */   
/*     */   public InventoryHolder getHolder() throws CivException {
/*  60 */     if (this.playerName != null) {
/*  61 */       Player player = CivGlobal.getPlayer(this.playerName);
/*  62 */       return player;
/*     */     }
/*     */     
/*  65 */     if (this.blockLocation != null)
/*     */     {
/*     */ 
/*  68 */       if ((!this.blockLocation.getChunk().isLoaded()) && 
/*  69 */         (!this.blockLocation.getChunk().load())) {
/*  70 */         throw new CivException("Couldn't load chunk at " + this.blockLocation + " where holder should reside.");
/*     */       }
/*     */       
/*  73 */       if (!(this.blockLocation.getBlock().getState() instanceof Chest)) {
/*  74 */         throw new CivException("Holder location is not a chest, invalid.");
/*     */       }
/*     */       
/*  77 */       Chest chest = (Chest)this.blockLocation.getBlock().getState();
/*  78 */       return chest.getInventory().getHolder();
/*     */     }
/*     */     
/*  81 */     throw new CivException("Invalid holder.");
/*     */   }
/*     */   
/*     */   public void setHolder(InventoryHolder holder) throws CivException {
/*  85 */     if ((holder instanceof Player)) {
/*  86 */       Player player = (Player)holder;
/*  87 */       this.playerName = player.getName();
/*  88 */       this.blockLocation = null;
/*  89 */       return;
/*     */     }
/*     */     
/*  92 */     if ((holder instanceof Chest)) {
/*  93 */       Chest chest = (Chest)holder;
/*  94 */       this.playerName = null;
/*  95 */       this.blockLocation = chest.getLocation();
/*  96 */       return;
/*     */     }
/*     */     
/*  99 */     if ((holder instanceof DoubleChest)) {
/* 100 */       DoubleChest dchest = (DoubleChest)holder;
/* 101 */       this.playerName = null;
/* 102 */       this.blockLocation = dchest.getLocation();
/* 103 */       return;
/*     */     }
/*     */     
/* 106 */     throw new CivException("Invalid holder passed to set holder:" + holder.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\InventoryHolderStorage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */