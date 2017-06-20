/*    */ package com.avrgaming.civcraft.util;
/*    */ 
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
/*    */ public class WorldCord
/*    */ {
/*    */   private String worldname;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   
/*    */   public WorldCord(String worldname, int x, int y, int z)
/*    */   {
/* 32 */     this.worldname = worldname;
/* 33 */     this.x = x;
/* 34 */     this.y = y;
/* 35 */     this.z = z;
/*    */   }
/*    */   
/*    */   public WorldCord(Location location)
/*    */   {
/* 40 */     this.worldname = location.getWorld().getName();
/* 41 */     this.x = location.getBlockX();
/* 42 */     this.y = location.getBlockY();
/* 43 */     this.z = location.getBlockZ();
/*    */   }
/*    */   
/*    */   public String getWorldname() {
/* 47 */     return this.worldname;
/*    */   }
/*    */   
/* 50 */   public void setWorldname(String worldname) { this.worldname = worldname; }
/*    */   
/*    */   public int getX() {
/* 53 */     return this.x;
/*    */   }
/*    */   
/* 56 */   public void setX(int x) { this.x = x; }
/*    */   
/*    */   public int getY() {
/* 59 */     return this.y;
/*    */   }
/*    */   
/* 62 */   public void setY(int y) { this.y = y; }
/*    */   
/*    */   public int getZ() {
/* 65 */     return this.z;
/*    */   }
/*    */   
/* 68 */   public void setZ(int z) { this.z = z; }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\WorldCord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */