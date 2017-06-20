/*     */ package com.avrgaming.civcraft.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
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
/*     */ 
/*     */ 
/*     */ public class TradeGoodSignCleanupTask
/*     */   implements Runnable
/*     */ {
/*     */   String playerName;
/*  38 */   int xoff = 0;
/*  39 */   int zoff = 0;
/*     */   
/*     */   public TradeGoodSignCleanupTask(String playername, int xoff, int zoff) {
/*  42 */     this.playerName = playername;
/*  43 */     this.xoff = xoff;
/*  44 */     this.zoff = zoff;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  51 */       player = CivGlobal.getPlayer(this.playerName);
/*     */     } catch (CivException e) { Player player;
/*  53 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     Player player;
/*  57 */     int count = 0;
/*  58 */     int i = 0;
/*     */     
/*  60 */     World world = Bukkit.getWorld("world");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */     for (TradeGood tg : CivGlobal.getTradeGoods()) {
/*  67 */       BlockCoord bcoord2 = tg.getCoord();
/*  68 */       bcoord2.setX(bcoord2.getX() + this.xoff);
/*  69 */       bcoord2.setZ(bcoord2.getZ() + this.zoff);
/*  70 */       bcoord2.setY(0);
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
/*  82 */       while (bcoord2.getY() < 256) {
/*  83 */         Block top = world.getBlockAt(bcoord2.getX(), bcoord2.getY(), bcoord2.getZ());
/*  84 */         ItemManager.setTypeId(top, 0);
/*  85 */         ItemManager.setData(top, 0, true);
/*  86 */         bcoord2.setY(bcoord2.getY() + 1);
/*     */         
/*  88 */         top = top.getRelative(BlockFace.NORTH);
/*  89 */         if ((ItemManager.getId(top) == 68) || (ItemManager.getId(top) == 63)) {
/*  90 */           count++;
/*     */         }
/*  92 */         ItemManager.setTypeId(top, 0);
/*  93 */         ItemManager.setData(top, 0, true);
/*     */         
/*  95 */         top = top.getRelative(BlockFace.SOUTH);
/*  96 */         if ((ItemManager.getId(top) == 68) || (ItemManager.getId(top) == 63)) {
/*  97 */           count++;
/*  98 */           ItemManager.setTypeId(top, 0);
/*  99 */           ItemManager.setData(top, 0, true);
/*     */         }
/*     */         
/*     */ 
/* 103 */         top = top.getRelative(BlockFace.EAST);
/* 104 */         if ((ItemManager.getId(top) == 68) || (ItemManager.getId(top) == 63)) {
/* 105 */           count++;
/* 106 */           ItemManager.setTypeId(top, 0);
/* 107 */           ItemManager.setData(top, 0, true);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 112 */         top = top.getRelative(BlockFace.WEST);
/* 113 */         if ((ItemManager.getId(top) == 68) || (ItemManager.getId(top) == 63)) {
/* 114 */           count++;
/* 115 */           ItemManager.setTypeId(top, 0);
/* 116 */           ItemManager.setData(top, 0, true);
/*     */         }
/*     */       }
/*     */       
/* 120 */       i++;
/* 121 */       if (i % 80 == 0) {
/* 122 */         CivMessage.send(player, "Goodie:" + i + " cleared " + count + " signs...");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 129 */     CivMessage.send(player, "Finished.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\tasks\TradeGoodSignCleanupTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */