/*     */ package com.avrgaming.civcraft.command;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Chest;
/*     */ import org.bukkit.block.Furnace;
/*     */ import org.bukkit.block.Hopper;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.minecart.HopperMinecart;
/*     */ import org.bukkit.entity.minecart.StorageMinecart;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class ReportChestsTask
/*     */   implements Runnable
/*     */ {
/*  47 */   public Queue<ChunkCoord> coords = new LinkedList();
/*     */   CommandSender sender;
/*     */   
/*     */   public ReportChestsTask(CommandSender sender, Queue<ChunkCoord> coords) {
/*  51 */     this.coords = coords;
/*  52 */     this.sender = sender;
/*     */   }
/*     */   
/*     */   private int countItem(Inventory inv, int id) {
/*  56 */     int total = 0;
/*  57 */     for (ItemStack stack : inv.all(ItemManager.getMaterial(id)).values()) {
/*  58 */       total += stack.getAmount();
/*     */     }
/*     */     
/*  61 */     return total;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  66 */     ChunkCoord coord = (ChunkCoord)this.coords.poll();
/*  67 */     if (coord == null) {
/*  68 */       CivMessage.send(this.sender, "Done.");
/*  69 */       return;
/*     */     }
/*  71 */     Chunk chunk = coord.getChunk();
/*     */     Block block;
/*  73 */     for (int x = 0; x < 16; x++) {
/*  74 */       for (y = 0; y < 256; y++) {
/*  75 */         for (z = 0; z < 16; z++) {
/*  76 */           block = chunk.getBlock(x, y, z);
/*  77 */           Inventory inv = null;
/*  78 */           if ((block.getState() instanceof Chest)) {
/*  79 */             inv = ((Chest)block.getState()).getBlockInventory();
/*  80 */           } else if ((block.getState() instanceof Furnace)) {
/*  81 */             inv = ((Furnace)block.getState()).getInventory();
/*  82 */           } else if ((block.getState() instanceof Hopper)) {
/*  83 */             inv = ((Hopper)block.getState()).getInventory();
/*     */           }
/*     */           
/*  86 */           if (inv != null) {
/*  87 */             BlockCoord bcoord = new BlockCoord(coord.getWorldname(), (coord.getX() << 4) + x, 
/*  88 */               y, (coord.getZ() << 4) + z);
/*     */             
/*  90 */             int diamondBlocks = countItem(inv, ItemManager.getId(Material.DIAMOND_BLOCK));
/*  91 */             int diamonds = countItem(inv, ItemManager.getId(Material.DIAMOND));
/*  92 */             int goldBlocks = countItem(inv, ItemManager.getId(Material.GOLD_BLOCK));
/*  93 */             int gold = countItem(inv, ItemManager.getId(Material.GOLD_INGOT));
/*  94 */             int emeraldBlocks = countItem(inv, ItemManager.getId(Material.EMERALD_BLOCK));
/*  95 */             int emeralds = countItem(inv, ItemManager.getId(Material.EMERALD));
/*  96 */             int diamondOre = countItem(inv, ItemManager.getId(Material.DIAMOND_ORE));
/*  97 */             int goldOre = countItem(inv, ItemManager.getId(Material.GOLD_ORE));
/*  98 */             int emeraldOre = countItem(inv, ItemManager.getId(Material.EMERALD_ORE));
/*     */             
/* 100 */             String out = block.getType().name() + ": " + "§d" + bcoord + "§f" + " DB:" + diamondBlocks + " EB:" + emeraldBlocks + " GB:" + goldBlocks + " D:" + 
/* 101 */               diamonds + " E:" + emeralds + " G:" + gold + " DO:" + diamondOre + " EO:" + emeraldOre + " GO:" + goldOre;
/* 102 */             if ((diamondBlocks != 0) || (diamonds != 0) || (goldBlocks != 0) || (gold != 0) || (emeraldBlocks != 0) || 
/* 103 */               (emeralds != 0) || (diamondOre != 0) || (goldOre != 0) || (emeraldOre != 0)) {
/* 104 */               CivMessage.send(this.sender, out);
/* 105 */               CivLog.info("REPORT: " + out);
/*     */             }
/* 107 */             inv = null;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 113 */     int z = (block = chunk.getEntities()).length; for (int y = 0; y < z; y++) { Entity e = block[y];
/* 114 */       Inventory inv = null;
/*     */       
/* 116 */       if (e.getType() == EntityType.MINECART_CHEST) {
/* 117 */         StorageMinecart chest = (StorageMinecart)e;
/* 118 */         inv = chest.getInventory();
/*     */       }
/*     */       
/* 121 */       if (e.getType() == EntityType.MINECART_HOPPER) {
/* 122 */         HopperMinecart chest = (HopperMinecart)e;
/* 123 */         inv = chest.getInventory();
/*     */       }
/*     */       
/* 126 */       if (inv != null) {
/* 127 */         BlockCoord bcoord = new BlockCoord(e.getLocation());
/*     */         
/* 129 */         int diamondBlocks = countItem(inv, ItemManager.getId(Material.DIAMOND_BLOCK));
/* 130 */         int diamonds = countItem(inv, ItemManager.getId(Material.DIAMOND));
/* 131 */         int goldBlocks = countItem(inv, ItemManager.getId(Material.GOLD_BLOCK));
/* 132 */         int gold = countItem(inv, ItemManager.getId(Material.GOLD_INGOT));
/* 133 */         int emeraldBlocks = countItem(inv, ItemManager.getId(Material.EMERALD_BLOCK));
/* 134 */         int emeralds = countItem(inv, ItemManager.getId(Material.EMERALD));
/* 135 */         int diamondOre = countItem(inv, ItemManager.getId(Material.DIAMOND_ORE));
/* 136 */         int goldOre = countItem(inv, ItemManager.getId(Material.GOLD_ORE));
/* 137 */         int emeraldOre = countItem(inv, ItemManager.getId(Material.EMERALD_ORE));
/*     */         
/* 139 */         String out = e.getType().name() + ": " + "§d" + bcoord + "§f" + " DB:" + diamondBlocks + " EB:" + emeraldBlocks + " GB:" + goldBlocks + " D:" + 
/* 140 */           diamonds + " E:" + emeralds + " G:" + gold + " DO:" + diamondOre + " EO:" + emeraldOre + " GO:" + goldOre;
/* 141 */         if ((diamondBlocks != 0) || (diamonds != 0) || (goldBlocks != 0) || (gold != 0) || (emeraldBlocks != 0) || 
/* 142 */           (emeralds != 0) || (diamondOre != 0) || (goldOre != 0) || (emeraldOre != 0)) {
/* 143 */           CivMessage.send(this.sender, out);
/* 144 */           CivLog.info("REPORT: " + out);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 149 */     TaskMaster.syncTask(new ReportChestsTask(this.sender, this.coords), 5L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\ReportChestsTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */