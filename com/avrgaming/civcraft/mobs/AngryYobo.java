/*     */ package com.avrgaming.civcraft.mobs;
/*     */ 
/*     */ import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
/*     */ import com.avrgaming.mob.ICustomMob;
/*     */ import com.avrgaming.mob.MobBaseZombie;
/*     */ import net.minecraft.server.v1_7_R2.DamageSource;
/*     */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*     */ import net.minecraft.server.v1_7_R2.EntityHuman;
/*     */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*     */ import org.bukkit.event.entity.EntityTargetEvent;
/*     */ import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
/*     */ 
/*     */ 
/*     */ public class AngryYobo
/*     */   extends CommonCustomMob
/*     */   implements ICustomMob
/*     */ {
/*     */   public void onCreate()
/*     */   {
/*  25 */     initLevelAndType();
/*     */     
/*  27 */     getGoalSelector().a(0, new PathfinderGoalFloat((EntityInsentient)this.entity));
/*  28 */     getGoalSelector().a(2, new PathfinderGoalMeleeAttack((EntityCreature)this.entity, EntityHuman.class, 1.0D, false));
/*  29 */     getGoalSelector().a(8, new PathfinderGoalLookAtPlayer((EntityInsentient)this.entity, EntityHuman.class, 8.0F));
/*     */     
/*  31 */     getTargetSelector().a(2, new PathfinderGoalNearestAttackableTarget((EntityCreature)this.entity, EntityHuman.class, 0, true));
/*  32 */     setName(this.level.getName() + " " + this.type.getName());
/*  33 */     MobBaseZombie zombie = (MobBaseZombie)this.entity;
/*  34 */     zombie.setBaby(true);
/*     */   }
/*     */   
/*     */   public void onTick()
/*     */   {
/*  39 */     super.onTick();
/*     */   }
/*     */   
/*     */   public String getBaseEntity()
/*     */   {
/*  44 */     return MobBaseZombie.class.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector) {}
/*     */   
/*     */ 
/*     */   public void onCreateAttributes()
/*     */   {
/*  54 */     setKnockbackResistance(0.99D);
/*     */     MobComponentDefense defense;
/*  56 */     switch (this.level) {
/*     */     case BRUTAL: 
/*  58 */       MobComponentDefense defense = new MobComponentDefense(3.5D);
/*  59 */       setMaxHealth(10.0D);
/*  60 */       setAttack(5.0D);
/*  61 */       addDrop("mat_metallic_crystal_fragment_1", 0.05D);
/*     */       
/*  63 */       addDrop("mat_forged_clay", 0.1D);
/*  64 */       addDrop("mat_crafted_reeds", 0.1D);
/*  65 */       addDrop("mat_crafted_sticks", 0.1D);
/*  66 */       coinDrop(1, 25);
/*     */       
/*  68 */       break;
/*     */     case ELITE: 
/*  70 */       MobComponentDefense defense = new MobComponentDefense(10.0D);
/*  71 */       setMaxHealth(15.0D);
/*  72 */       setAttack(8.0D);
/*     */       
/*  74 */       addDrop("mat_metallic_crystal_fragment_2", 0.05D);
/*     */       
/*  76 */       addDrop("mat_clay_steel_cast", 0.05D);
/*  77 */       addDrop("mat_steel_ingot", 0.05D);
/*     */       
/*  79 */       addDrop("mat_varnish", 0.01D);
/*  80 */       addDrop("mat_sticky_resin", 0.01D);
/*  81 */       coinDrop(10, 50);
/*  82 */       break;
/*     */     case GREATER: 
/*  84 */       MobComponentDefense defense = new MobComponentDefense(16.0D);
/*  85 */       setMaxHealth(20.0D);
/*  86 */       setAttack(13.0D);
/*     */       
/*  88 */       addDrop("mat_metallic_crystal_fragment_3", 0.05D);
/*     */       
/*  90 */       addDrop("mat_clay_steel_cast", 0.05D);
/*  91 */       addDrop("mat_carbide_steel_ingot", 0.05D);
/*     */       
/*  93 */       addDrop("mat_sticky_resin", 0.1D);
/*  94 */       addDrop("mat_smithy_resin", 0.01D);
/*  95 */       coinDrop(20, 80);
/*  96 */       break;
/*     */     case LESSER: 
/*  98 */       MobComponentDefense defense = new MobComponentDefense(20.0D);
/*  99 */       setMaxHealth(30.0D);
/* 100 */       setAttack(18.0D);
/*     */       
/* 102 */       addDrop("mat_metallic_crystal_fragment_4", 0.05D);
/*     */       
/* 104 */       addDrop("mat_tungsten_ingot", 0.05D);
/* 105 */       addDrop("mat_clay_tungsten_casting", 0.05D);
/*     */       
/* 107 */       addDrop("mat_sticky_resin", 0.1D);
/* 108 */       addDrop("mat_smithy_resin", 0.01D);
/* 109 */       coinDrop(20, 150);
/* 110 */       break;
/*     */     default: 
/* 112 */       defense = new MobComponentDefense(2.0D);
/*     */     }
/*     */     
/*     */     
/* 116 */     addComponent(defense);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onRangedAttack(net.minecraft.server.v1_7_R2.Entity target) {}
/*     */   
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 126 */     return AngryYobo.class.getName();
/*     */   }
/*     */   
/*     */   public void onTarget(EntityTargetEvent event)
/*     */   {
/* 131 */     super.onTarget(event);
/*     */     
/* 133 */     if ((event.getReason().equals(EntityTargetEvent.TargetReason.FORGOT_TARGET)) || 
/* 134 */       (event.getReason().equals(EntityTargetEvent.TargetReason.TARGET_DIED))) {
/* 135 */       event.getEntity().remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\AngryYobo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */