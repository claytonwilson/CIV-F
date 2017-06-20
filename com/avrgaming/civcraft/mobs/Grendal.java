/*    */ package com.avrgaming.civcraft.mobs;
/*    */ 
/*    */ import com.avrgaming.mob.ICustomMob;
/*    */ import com.avrgaming.mob.MobBaseWither;
/*    */ import net.minecraft.server.v1_7_R2.DamageSource;
/*    */ import net.minecraft.server.v1_7_R2.Entity;
/*    */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*    */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*    */ 
/*    */ public class Grendal extends CommonCustomMob implements ICustomMob
/*    */ {
/*    */   public void onCreate()
/*    */   {
/* 14 */     initLevelAndType();
/*    */     
/* 16 */     setName("Grendal");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void onCreateAttributes() {}
/*    */   
/*    */ 
/*    */   public void onTick() {}
/*    */   
/*    */ 
/*    */   public String getBaseEntity()
/*    */   {
/* 29 */     return MobBaseWither.class.getName();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void onDeath(EntityCreature e) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void onRangedAttack(Entity target) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public String getClassName()
/*    */   {
/* 48 */     return Grendal.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\Grendal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */