/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.components.Component;
/*    */ import com.avrgaming.civcraft.components.ProjectileComponent;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import java.util.ArrayList;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.locks.ReentrantLock;
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
/*    */ public class ProjectileComponentTimer
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 33 */       if (!CivGlobal.towersEnabled) {
/* 34 */         return;
/*    */       }
/*    */       
/* 37 */       Component.componentsLock.lock();
/*    */       try {
/* 39 */         ArrayList<Component> projectileComponents = (ArrayList)Component.componentsByType.get(ProjectileComponent.class.getName());
/*    */         
/* 41 */         if (projectileComponents == null) {
/* 42 */           return;
/*    */         }
/*    */         
/* 45 */         for (Component c : projectileComponents) {
/* 46 */           ProjectileComponent projectileComponent = (ProjectileComponent)c;
/* 47 */           projectileComponent.process();
/*    */         }
/*    */       } finally {
/* 50 */         Component.componentsLock.unlock(); } Component.componentsLock.unlock();
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 54 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\ProjectileComponentTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */