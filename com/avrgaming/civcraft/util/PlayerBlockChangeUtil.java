/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import net.minecraft.server.v1_7_R2.PacketPlayOutMultiBlockChange;
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
/*     */ public class PlayerBlockChangeUtil
/*     */ {
/*  28 */   HashMap<String, HashMap<ChunkCoord, LinkedList<SimpleBlock>>> blocksInChunkToUpdate = new HashMap();
/*     */   
/*     */ 
/*  31 */   TreeMap<String, PacketPlayOutMultiBlockChange> preparedPackets = new TreeMap();
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
/*     */   public void addUpdateBlock(String playerName, BlockCoord bcoord, int type_id, int data)
/*     */   {
/*  47 */     HashMap<ChunkCoord, LinkedList<SimpleBlock>> blocksInChunk = (HashMap)this.blocksInChunkToUpdate.get(playerName);
/*  48 */     if (blocksInChunk == null) {
/*  49 */       blocksInChunk = new HashMap();
/*     */     }
/*     */     
/*     */ 
/*  53 */     ChunkCoord coord = new ChunkCoord(bcoord);
/*  54 */     SimpleBlock sb2 = new SimpleBlock(type_id, data);
/*  55 */     sb2.worldname = bcoord.getWorldname();
/*  56 */     sb2.x = bcoord.getX();
/*  57 */     sb2.y = bcoord.getY();
/*  58 */     sb2.z = bcoord.getZ();
/*     */     
/*  60 */     LinkedList<SimpleBlock> blocks = (LinkedList)blocksInChunk.get(coord);
/*  61 */     if (blocks == null) {
/*  62 */       blocks = new LinkedList();
/*     */     }
/*     */     
/*  65 */     blocks.add(sb2);
/*  66 */     blocksInChunk.put(coord, blocks);
/*  67 */     this.blocksInChunkToUpdate.put(playerName, blocksInChunk);
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
/*     */   public void sendUpdate(String playerName)
/*     */   {
/* 110 */     HashMap<ChunkCoord, LinkedList<SimpleBlock>> blocksInChunk = (HashMap)this.blocksInChunkToUpdate.get(playerName);
/* 111 */     if (blocksInChunk == null) {
/*     */       return;
/*     */     }
/*     */     Iterator localIterator2;
/* 115 */     for (Iterator localIterator1 = blocksInChunk.keySet().iterator(); localIterator1.hasNext(); 
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
/* 126 */         localIterator2.hasNext())
/*     */     {
/* 115 */       ChunkCoord chunk = (ChunkCoord)localIterator1.next();
/* 116 */       LinkedList<SimpleBlock> blocks = (LinkedList)blocksInChunk.get(chunk);
/*     */       
/*     */       try
/*     */       {
/* 120 */         player = CivGlobal.getPlayer(playerName);
/*     */       } catch (CivException e) { Player player;
/* 122 */         e.printStackTrace(); return;
/*     */       }
/*     */       
/*     */       Player player;
/* 126 */       localIterator2 = blocks.iterator(); continue;SimpleBlock sb = (SimpleBlock)localIterator2.next();
/*     */       
/* 128 */       ItemManager.sendBlockChange(player, sb.getLocation(), sb.getType(), sb.getData());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\PlayerBlockChangeUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */