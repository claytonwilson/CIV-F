/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Entity;
/*    */ 
/*    */ 
/*    */ public class EntityUtil
/*    */ {
/*    */   public static Entity getEntity(World world, UUID uuid)
/*    */   {
/* 12 */     for (Entity ent : world.getEntities()) {
/* 13 */       if (ent.getUniqueId().equals(uuid)) {
/* 14 */         return ent;
/*    */       }
/*    */     }
/*    */     
/* 18 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\EntityUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */