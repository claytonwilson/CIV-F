/*    */ package com.avrgaming.civcraft.mobs;
/*    */ 
/*    */ import com.avrgaming.mob.ICustomMob;
/*    */ import com.avrgaming.mob.MobBaseZombie;
/*    */ import net.minecraft.server.v1_7_R2.DamageSource;
/*    */ import net.minecraft.server.v1_7_R2.Entity;
/*    */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*    */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*    */ 
/*    */ public class AngryBird extends CommonCustomMob implements ICustomMob
/*    */ {
/*    */   public void onCreate()
/*    */   {
/* 14 */     initLevelAndType();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 20 */     setName("Angry Bird");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onCreateAttributes() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onTick() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getBaseEntity()
/*    */   {
/* 38 */     return MobBaseZombie.class.getName();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector) {}
/*    */   
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
/* 59 */     return AngryBird.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\AngryBird.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */