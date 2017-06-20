/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.cache.ArrowFiredCache;
/*    */ import com.avrgaming.civcraft.cache.CivCache;
/*    */ import com.avrgaming.civcraft.components.ProjectileArrowComponent;
/*    */ import com.avrgaming.civcraft.config.CivSettings;
/*    */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Calendar;
/*    */ import java.util.Map;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Arrow;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.util.Vector;
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
/*    */ public class ArrowProjectileTask
/*    */   implements Runnable
/*    */ {
/* 34 */   private double homing_stop_distance = 0.0D;
/*    */   
/*    */   public ArrowProjectileTask() {
/*    */     try {
/* 38 */       this.homing_stop_distance = CivSettings.getDouble(CivSettings.warConfig, "arrow_tower.homing_stop_distance");
/* 39 */       this.homing_stop_distance *= this.homing_stop_distance;
/*    */     } catch (InvalidConfiguration e) {
/* 41 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 47 */     Calendar now = Calendar.getInstance();
/* 48 */     ArrayList<ArrowFiredCache> removeUs = new ArrayList();
/* 49 */     for (ArrowFiredCache afc : CivCache.arrowsFired.values()) {
/* 50 */       Arrow arrow = afc.getArrow();
/* 51 */       if ((!arrow.isValid()) || (arrow.isOnGround()) || (arrow.isDead()) || (afc.isHit())) {
/* 52 */         removeUs.add(afc);
/*    */ 
/*    */ 
/*    */ 
/*    */       }
/* 57 */       else if (now.after(afc.getExpired())) {
/* 58 */         removeUs.add(afc);
/*    */       }
/*    */       else
/*    */       {
/* 62 */         double distance = afc.getArrow().getLocation().distanceSquared(afc.getTarget());
/*    */         
/* 64 */         if (distance < 1.0D) {
/* 65 */           removeUs.add(afc);
/*    */         }
/*    */         else
/*    */         {
/* 69 */           if (distance > this.homing_stop_distance) {
/* 70 */             afc.setTarget(afc.getTargetEntity().getLocation().add(0.0D, 1.0D, 0.0D));
/*    */           }
/*    */           
/* 73 */           Vector dir = afc.getFromTower().getVectorBetween(afc.getTarget(), arrow.getLocation()).normalize();
/* 74 */           afc.getArrow().setVelocity(dir.multiply(afc.getFromTower().getPower()));
/*    */         }
/*    */       }
/*    */     }
/* 78 */     for (ArrowFiredCache afc : removeUs) {
/* 79 */       afc.destroy(afc.getArrow());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\ArrowProjectileTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */