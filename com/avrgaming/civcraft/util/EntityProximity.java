/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.v1_7_R2.AxisAlignedBB;
/*    */ import net.minecraft.server.v1_7_R2.WorldServer;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
/*    */ import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
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
/*    */ public class EntityProximity
/*    */ {
/*    */   public static LinkedList<org.bukkit.entity.Entity> getNearbyEntities(org.bukkit.entity.Entity exempt, Location loc, double radius, Class<?> filter)
/*    */   {
/* 24 */     LinkedList<org.bukkit.entity.Entity> entities = new LinkedList();
/*    */     
/* 26 */     double x = loc.getX() + 0.5D;
/* 27 */     double y = loc.getY() + 0.5D;
/* 28 */     double z = loc.getZ() + 0.5D;
/* 29 */     double r = radius;
/*    */     
/* 31 */     CraftWorld craftWorld = (CraftWorld)loc.getWorld();
/* 32 */     AxisAlignedBB bb = AxisAlignedBB.a(x - r, y - r, z - r, x + r, y + r, z + r);
/*    */     List<net.minecraft.server.v1_7_R2.Entity> eList;
/*    */     List<net.minecraft.server.v1_7_R2.Entity> eList;
/* 35 */     if (exempt != null) {
/* 36 */       eList = craftWorld.getHandle().getEntities(((CraftEntity)exempt).getHandle(), bb);
/*    */     } else {
/* 38 */       eList = craftWorld.getHandle().getEntities(null, bb);
/*    */     }
/*    */     
/* 41 */     for (net.minecraft.server.v1_7_R2.Entity e : eList)
/*    */     {
/*    */ 
/* 44 */       if ((filter == null) || (filter.isInstance(e))) {
/* 45 */         entities.add(e.getBukkitEntity());
/*    */       }
/*    */     }
/*    */     
/* 49 */     return entities;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\EntityProximity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */