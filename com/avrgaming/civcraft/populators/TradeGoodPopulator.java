/*     */ package com.avrgaming.civcraft.populators;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigTradeGood;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.ProtectedBlock;
/*     */ import com.avrgaming.civcraft.object.ProtectedBlock.Type;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.TradeGood;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Chunk;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.generator.BlockPopulator;
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
/*     */ public class TradeGoodPopulator
/*     */   extends BlockPopulator
/*     */ {
/*     */   private static final int FLAG_HEIGHT = 3;
/*     */   
/*     */   public static void buildTradeGoodie(ConfigTradeGood good, BlockCoord coord, World world, boolean sync)
/*     */   {
/*  51 */     TradeGood new_good = new TradeGood(good, coord);
/*  52 */     CivGlobal.addTradeGood(new_good);
/*     */     
/*  54 */     BlockFace direction = null;
/*  55 */     Block top = null;
/*  56 */     Random random = new Random();
/*  57 */     int dir = random.nextInt(4);
/*  58 */     if (dir == 0) {
/*  59 */       direction = BlockFace.NORTH;
/*  60 */     } else if (dir == 1) {
/*  61 */       direction = BlockFace.EAST;
/*  62 */     } else if (dir == 2) {
/*  63 */       direction = BlockFace.SOUTH;
/*     */     } else {
/*  65 */       direction = BlockFace.WEST;
/*     */     }
/*     */     
/*     */ 
/*  69 */     for (int y = coord.getY(); y < 256; y++) {
/*  70 */       top = world.getBlockAt(coord.getX(), y, coord.getZ());
/*  71 */       if (ItemManager.getId(top) == 7) {
/*  72 */         ItemManager.setTypeId(top, 0);
/*     */       }
/*     */     }
/*     */     
/*  76 */     for (int y = coord.getY(); y < coord.getY() + 3; y++) {
/*  77 */       top = world.getBlockAt(coord.getX(), y, coord.getZ());
/*  78 */       top.setType(Material.BEDROCK);
/*     */       
/*  80 */       ProtectedBlock pb = new ProtectedBlock(new BlockCoord(top), ProtectedBlock.Type.TRADE_MARKER);
/*  81 */       CivGlobal.addProtectedBlock(pb);
/*  82 */       if (sync) {
/*     */         try {
/*  84 */           pb.saveNow();
/*     */         } catch (SQLException e) {
/*  86 */           e.printStackTrace();
/*     */         }
/*     */       } else {
/*  89 */         pb.save();
/*     */       }
/*     */     }
/*     */     
/*  93 */     Block signBlock = top.getRelative(direction);
/*  94 */     signBlock.setType(Material.WALL_SIGN);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  99 */     BlockState state = signBlock.getState();
/*     */     
/* 101 */     if ((state instanceof org.bukkit.block.Sign)) {
/* 102 */       org.bukkit.block.Sign sign = (org.bukkit.block.Sign)state;
/* 103 */       org.bukkit.material.Sign data = (org.bukkit.material.Sign)state.getData();
/*     */       
/* 105 */       data.setFacingDirection(direction);
/* 106 */       sign.setLine(0, "Trade Resource");
/* 107 */       sign.setLine(1, "----");
/* 108 */       sign.setLine(2, good.name);
/* 109 */       sign.setLine(3, "");
/* 110 */       sign.update(true);
/*     */       
/* 112 */       StructureSign structSign = new StructureSign(new BlockCoord(signBlock), null);
/* 113 */       structSign.setAction("");
/* 114 */       structSign.setType("");
/* 115 */       structSign.setText(sign.getLines());
/* 116 */       structSign.setDirection(ItemManager.getData(sign.getData()));
/* 117 */       CivGlobal.addStructureSign(structSign);
/* 118 */       if (sync) {
/*     */         try {
/* 120 */           structSign.saveNow();
/*     */         } catch (SQLException e) {
/* 122 */           e.printStackTrace();
/*     */         }
/*     */       } else {
/* 125 */         structSign.save();
/*     */       }
/*     */     }
/* 128 */     if (sync) {
/*     */       try {
/* 130 */         new_good.saveNow();
/*     */       } catch (SQLException e) {
/* 132 */         e.printStackTrace();
/*     */       }
/*     */     } else {
/* 135 */       new_good.save();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean checkForDuplicateTradeGood(String worldName, int centerX, int centerY, int centerZ)
/*     */   {
/* 144 */     BlockCoord coord = new BlockCoord(worldName, centerX, centerY, centerZ);
/* 145 */     for (int y = centerY; y > 0; y--) {
/* 146 */       coord.setY(y);
/*     */       
/* 148 */       if (CivGlobal.getTradeGood(coord) != null)
/*     */       {
/* 150 */         return true;
/*     */       }
/*     */     }
/* 153 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void populate(World world, Random random, Chunk source)
/*     */   {
/* 159 */     ChunkCoord cCoord = new ChunkCoord(source);
/* 160 */     TradeGoodPick pick = (TradeGoodPick)CivGlobal.preGenerator.goodPicks.get(cCoord);
/* 161 */     if (pick != null) {
/* 162 */       int centerX = (source.getX() << 4) + 8;
/* 163 */       int centerZ = (source.getZ() << 4) + 8;
/* 164 */       int centerY = world.getHighestBlockYAt(centerX, centerZ);
/* 165 */       BlockCoord coord = new BlockCoord(world.getName(), centerX, centerY, centerZ);
/*     */       
/* 167 */       if (checkForDuplicateTradeGood(world.getName(), centerX, centerY, centerZ)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       ConfigTradeGood good;
/*     */       ConfigTradeGood good;
/* 173 */       if ((ItemManager.getBlockTypeIdAt(world, centerX, centerY - 1, centerZ) == 9) || 
/* 174 */         (ItemManager.getBlockTypeIdAt(world, centerX, centerY - 1, centerZ) == 8)) {
/* 175 */         good = pick.waterPick;
/*     */       } else {
/* 177 */         good = pick.landPick;
/*     */       }
/*     */       
/*     */ 
/* 181 */       if (good == null) {
/* 182 */         System.out.println("Could not find suitable good type during populate! aborting.");
/* 183 */         return;
/*     */       }
/*     */       
/*     */ 
/* 187 */       buildTradeGoodie(good, coord, world, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\populators\TradeGoodPopulator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */