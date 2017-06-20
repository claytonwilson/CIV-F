/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.TownChunk;
/*    */ import com.avrgaming.civcraft.permission.PlotPermissions;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsciiMap
/*    */ {
/*    */   private static final int width = 9;
/*    */   private static final int height = 40;
/*    */   
/*    */   public static List<String> getMapAsString(Location center)
/*    */   {
/* 36 */     ArrayList<String> out = new ArrayList();
/*    */     
/*    */ 
/* 39 */     ChunkCoord centerChunk = new ChunkCoord(center);
/*    */     
/*    */ 
/* 42 */     ChunkCoord currentChunk = new ChunkCoord(center.getWorld().getName(), 
/* 43 */       centerChunk.getX() - 4, 
/* 44 */       centerChunk.getZ() - 20);
/*    */     
/* 46 */     int startX = currentChunk.getX();
/* 47 */     int startZ = currentChunk.getZ();
/*    */     
/* 49 */     out.add(CivMessage.buildTitle("Map"));
/*    */     
/*    */ 
/* 52 */     for (int x = 0; x < 9; x++) {
/* 53 */       String outRow = new String("         ");
/* 54 */       for (int z = 0; z < 40; z++) {
/* 55 */         String color = "§f";
/*    */         
/* 57 */         currentChunk = new ChunkCoord(center.getWorld().getName(), 
/* 58 */           startX + x, startZ + z);
/*    */         
/* 60 */         if (currentChunk.equals(centerChunk)) {
/* 61 */           color = "§e";
/*    */         }
/*    */         
/*    */ 
/* 65 */         TownChunk tc = CivGlobal.getTownChunk(currentChunk);
/* 66 */         if (tc != null)
/*    */         {
/* 68 */           if (color.equals("§f")) {
/* 69 */             if (tc.perms.getOwner() != null) {
/* 70 */               color = "§a";
/*    */             } else {
/* 72 */               color = "§c";
/*    */             }
/*    */           }
/*    */           
/* 76 */           if (tc.isForSale()) {
/* 77 */             outRow = outRow + "§e$";
/* 78 */           } else if (tc.isOutpost()) {
/* 79 */             outRow = outRow + "§eO";
/*    */           } else {
/* 81 */             outRow = outRow + color + "T";
/*    */           }
/*    */         } else {
/* 84 */           outRow = outRow + color + "-";
/*    */         }
/*    */       }
/* 87 */       out.add(outRow);
/*    */     }
/*    */     
/*    */ 
/* 91 */     return out;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\AsciiMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */