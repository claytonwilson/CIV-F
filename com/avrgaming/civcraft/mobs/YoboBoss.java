/*    */ package com.avrgaming.civcraft.mobs;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
/*    */ import com.avrgaming.mob.ICustomMob;
/*    */ import com.avrgaming.mob.MobBaseZombieGiant;
/*    */ import net.minecraft.server.v1_7_R2.AttributeInstance;
/*    */ import net.minecraft.server.v1_7_R2.DamageSource;
/*    */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*    */ import net.minecraft.server.v1_7_R2.EntityHuman;
/*    */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*    */ import net.minecraft.server.v1_7_R2.GenericAttributes;
/*    */ import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
/*    */ import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
/*    */ import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
/*    */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*    */ 
/*    */ public class YoboBoss extends CommonCustomMob implements ICustomMob
/*    */ {
/* 20 */   private String entityType = MobBaseZombieGiant.class.getName();
/*    */   
/*    */   public void onCreate()
/*    */   {
/* 24 */     initLevelAndType();
/*    */     
/* 26 */     MobBaseZombieGiant zombie = (MobBaseZombieGiant)this.entity;
/* 27 */     zombie.height *= 6.0F;
/*    */     
/* 29 */     getGoalSelector().a(7, new PathfinderGoalRandomStroll((EntityCreature)this.entity, 100.0D));
/* 30 */     getGoalSelector().a(8, new net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer((EntityInsentient)this.entity, EntityHuman.class, 8.0F));
/* 31 */     getGoalSelector().a(2, new PathfinderGoalMeleeAttack((EntityCreature)this.entity, EntityHuman.class, 100.0D, false));
/*    */     
/* 33 */     getTargetSelector().a(2, new PathfinderGoalNearestAttackableTarget((EntityCreature)this.entity, EntityHuman.class, 0, true));
/*    */     
/* 35 */     MobComponentDefense defense = new MobComponentDefense(9.0D);
/* 36 */     addComponent(defense);
/*    */     
/* 38 */     setName(this.level.getName() + " " + this.type.getName());
/*    */   }
/*    */   
/*    */   public void onCreateAttributes() {
/* 42 */     MobBaseZombieGiant zombie = (MobBaseZombieGiant)this.entity;
/* 43 */     zombie.getAttributeInstance(GenericAttributes.e).setValue(200.0D);
/* 44 */     zombie.getAttributeInstance(GenericAttributes.a).setValue(5000.0D);
/* 45 */     zombie.getAttributeInstance(GenericAttributes.c).setValue(1.0D);
/* 46 */     zombie.getAttributeInstance(GenericAttributes.d).setValue(200000.0D);
/*    */     
/* 48 */     zombie.setHealth(5000.0F);
/*    */   }
/*    */   
/*    */   public String getBaseEntity()
/*    */   {
/* 53 */     return this.entityType;
/*    */   }
/*    */   
/*    */   public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector)
/*    */   {
/* 58 */     goalSelector.a(2, new PathfinderGoalMeleeAttack(e, EntityHuman.class, 1.0D, false));
/* 59 */     for (int i = 0; i < 6; i++) {
/*    */       try {
/* 61 */         MobSpawner.spawnCustomMob(MobSpawner.CustomMobType.ANGRYYOBO, this.level, getLocation(e));
/*    */       } catch (CivException e1) {
/* 63 */         e1.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public String getClassName()
/*    */   {
/* 70 */     return YoboBoss.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\YoboBoss.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */