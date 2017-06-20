/*     */ package com.avrgaming.civcraft.util;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Firework;
/*     */ import org.bukkit.inventory.meta.FireworkMeta;
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
/*     */ public class FireworkEffectPlayer
/*     */ {
/*  43 */   private Method world_getHandle = null;
/*  44 */   private Method nms_world_broadcastEntityEffect = null;
/*  45 */   private Method firework_getHandle = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playFirework(World world, Location loc, FireworkEffect fe)
/*     */     throws Exception
/*     */   {
/*  56 */     Firework fw = (Firework)world.spawn(loc, Firework.class);
/*     */     
/*  58 */     Object nms_world = null;
/*  59 */     Object nms_firework = null;
/*     */     
/*     */ 
/*     */ 
/*  63 */     if (this.world_getHandle == null)
/*     */     {
/*  65 */       this.world_getHandle = getMethod(world.getClass(), "getHandle");
/*  66 */       this.firework_getHandle = getMethod(fw.getClass(), "getHandle");
/*     */     }
/*     */     
/*  69 */     nms_world = this.world_getHandle.invoke(world, null);
/*  70 */     nms_firework = this.firework_getHandle.invoke(fw, null);
/*     */     
/*  72 */     if (this.nms_world_broadcastEntityEffect == null)
/*     */     {
/*  74 */       this.nms_world_broadcastEntityEffect = getMethod(nms_world.getClass(), "broadcastEntityEffect");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  80 */     FireworkMeta data = fw.getFireworkMeta();
/*     */     
/*  82 */     data.clearEffects();
/*     */     
/*  84 */     data.setPower(1);
/*     */     
/*  86 */     data.addEffect(fe);
/*     */     
/*  88 */     fw.setFireworkMeta(data);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  93 */     this.nms_world_broadcastEntityEffect.invoke(nms_world, new Object[] { nms_firework, Byte.valueOf(17) });
/*     */     
/*     */ 
/*  96 */     fw.remove();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Method getMethod(Class<?> cl, String method)
/*     */   {
/*     */     Method[] arrayOfMethod;
/*     */     
/*     */ 
/* 106 */     int j = (arrayOfMethod = cl.getMethods()).length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod[i];
/* 107 */       if (m.getName().equals(method)) {
/* 108 */         return m;
/*     */       }
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\FireworkEffectPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */