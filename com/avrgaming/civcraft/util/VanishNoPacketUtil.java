/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class VanishNoPacketUtil
/*    */ {
/*    */   public static boolean isVanished(Player player)
/*    */   {
/*    */     try
/*    */     {
/* 11 */       return org.kitteh.vanish.staticaccess.VanishNoPacket.isVanished(player.getName());
/*    */     } catch (org.kitteh.vanish.staticaccess.VanishNotLoadedException|NoClassDefFoundError e) {}
/* 13 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\VanishNoPacketUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */