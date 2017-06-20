/*     */ package com.avrgaming.civcraft.mobs;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
/*     */ import com.avrgaming.mob.ICustomMob;
/*     */ import com.avrgaming.mob.MobBaseZombie;
/*     */ import java.util.LinkedList;
/*     */ import net.minecraft.server.v1_7_R2.DamageSource;
/*     */ import net.minecraft.server.v1_7_R2.Entity;
/*     */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*     */ import net.minecraft.server.v1_7_R2.EntityDamageSource;
/*     */ import net.minecraft.server.v1_7_R2.EntityHuman;
/*     */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*     */ import org.bukkit.block.Biome;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
/*     */ import org.bukkit.event.entity.EntityTargetEvent;
/*     */ import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Yobo
/*     */   extends CommonCustomMob
/*     */   implements ICustomMob
/*     */ {
/*  30 */   private String entityType = MobBaseZombie.class.getName();
/*  31 */   private boolean angry = false;
/*     */   
/*  33 */   LinkedList<Entity> minions = new LinkedList();
/*     */   
/*     */   public void onCreate() {
/*  36 */     initLevelAndType();
/*     */     
/*  38 */     getGoalSelector().a(7, new PathfinderGoalRandomStroll((EntityCreature)this.entity, 1.0D));
/*  39 */     getGoalSelector().a(8, new PathfinderGoalLookAtPlayer((EntityInsentient)this.entity, EntityHuman.class, 8.0F));
/*  40 */     getTargetSelector().a(1, new PathfinderGoalHurtByTarget((EntityCreature)this.entity, true));
/*     */     
/*  42 */     setName(this.level.getName() + " " + this.type.getName());
/*     */   }
/*     */   
/*     */   public void onCreateAttributes()
/*     */   {
/*  47 */     setKnockbackResistance(0.99D);
/*     */     MobComponentDefense defense;
/*  49 */     switch (this.level) {
/*     */     case BRUTAL: 
/*  51 */       MobComponentDefense defense = new MobComponentDefense(3.5D);
/*  52 */       setMaxHealth(20.0D);
/*  53 */       modifySpeed(1.3D);
/*  54 */       setAttack(8.0D);
/*     */       
/*  56 */       addDrop("mat_metallic_crystal_fragment_1", 0.05D);
/*     */       
/*  58 */       addDrop("mat_forged_clay", 0.1D);
/*  59 */       addDrop("mat_crafted_reeds", 0.1D);
/*  60 */       addDrop("mat_crafted_sticks", 0.1D);
/*  61 */       coinDrop(1, 25);
/*     */       
/*  63 */       break;
/*     */     case ELITE: 
/*  65 */       MobComponentDefense defense = new MobComponentDefense(10.0D);
/*  66 */       setMaxHealth(25.0D);
/*  67 */       modifySpeed(1.4D);
/*  68 */       setAttack(13.0D);
/*     */       
/*  70 */       addDrop("mat_metallic_crystal_fragment_2", 0.05D);
/*     */       
/*  72 */       addDrop("mat_clay_steel_cast", 0.05D);
/*  73 */       addDrop("mat_leather_straps", 0.05D);
/*  74 */       addDrop("mat_steel_ingot", 0.05D);
/*     */       
/*  76 */       addDrop("mat_varnish", 0.01D);
/*  77 */       addDrop("mat_sticky_resin", 0.01D);
/*  78 */       coinDrop(10, 50);
/*     */       
/*  80 */       break;
/*     */     case GREATER: 
/*  82 */       MobComponentDefense defense = new MobComponentDefense(16.0D);
/*  83 */       setMaxHealth(30.0D);
/*  84 */       modifySpeed(1.5D);
/*  85 */       setAttack(15.0D);
/*     */       
/*  87 */       addDrop("mat_metallic_crystal_fragment_3", 0.05D);
/*     */       
/*  89 */       addDrop("mat_clay_steel_cast", 0.05D);
/*  90 */       addDrop("mat_reinforced_braid", 0.05D);
/*  91 */       addDrop("mat_carbide_steel_ingot", 0.05D);
/*     */       
/*  93 */       addDrop("mat_sticky_resin", 0.1D);
/*  94 */       addDrop("mat_smithy_resin", 0.01D);
/*  95 */       coinDrop(20, 80);
/*     */       
/*  97 */       break;
/*     */     case LESSER: 
/*  99 */       MobComponentDefense defense = new MobComponentDefense(16.0D);
/* 100 */       setMaxHealth(40.0D);
/* 101 */       modifySpeed(1.5D);
/* 102 */       setAttack(20.0D);
/*     */       
/* 104 */       addDrop("mat_metallic_crystal_fragment_4", 0.05D);
/*     */       
/* 106 */       addDrop("mat_clay_tungsten_casting", 0.05D);
/* 107 */       addDrop("mat_artisan_leather", 0.05D);
/* 108 */       addDrop("mat_tungsten_ingot", 0.05D);
/*     */       
/* 110 */       addDrop("mat_sticky_resin", 0.1D);
/* 111 */       addDrop("mat_smithy_resin", 0.01D);
/* 112 */       coinDrop(20, 150);
/*     */       
/* 114 */       break;
/*     */     default: 
/* 116 */       defense = new MobComponentDefense(2.0D);
/*     */     }
/*     */     
/*     */     
/* 120 */     addComponent(defense);
/*     */   }
/*     */   
/*     */   public String getBaseEntity()
/*     */   {
/* 125 */     return this.entityType;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector)
/*     */   {
/* 131 */     if (!(damagesource instanceof EntityDamageSource)) {
/* 132 */       return;
/*     */     }
/*     */     
/* 135 */     if (!this.angry) {
/* 136 */       this.angry = true;
/* 137 */       goalSelector.a(2, new PathfinderGoalMeleeAttack(e, EntityHuman.class, 1.0D, false));
/* 138 */       for (int i = 0; i < 4; i++) {
/*     */         try {
/* 140 */           this.minions.add(MobSpawner.spawnCustomMob(MobSpawner.CustomMobType.ANGRYYOBO, this.level, getLocation(e)).entity);
/*     */         } catch (CivException e1) {
/* 142 */           e1.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getClassName()
/*     */   {
/* 150 */     return Yobo.class.getName();
/*     */   }
/*     */   
/*     */   public static void register() {
/* 154 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.LESSER, Biome.PLAINS);
/* 155 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.LESSER, Biome.FOREST);
/* 156 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.LESSER, Biome.BIRCH_FOREST);
/* 157 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.LESSER, Biome.BIRCH_FOREST_HILLS);
/*     */     
/* 159 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.GREATER, Biome.SUNFLOWER_PLAINS);
/* 160 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.GREATER, Biome.FLOWER_FOREST);
/* 161 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.GREATER, Biome.BIRCH_FOREST_HILLS_MOUNTAINS);
/* 162 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.GREATER, Biome.BIRCH_FOREST_MOUNTAINS);
/* 163 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.GREATER, Biome.FOREST_HILLS);
/*     */     
/*     */ 
/* 166 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.ELITE, Biome.EXTREME_HILLS);
/* 167 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.ELITE, Biome.EXTREME_HILLS_PLUS);
/* 168 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.ELITE, Biome.ROOFED_FOREST);
/* 169 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.ELITE, Biome.ROOFED_FOREST_MOUNTAINS);
/*     */     
/*     */ 
/* 172 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.BRUTAL, Biome.MEGA_SPRUCE_TAIGA_HILLS);
/* 173 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.BRUTAL, Biome.EXTREME_HILLS_MOUNTAINS);
/* 174 */     setValidBiome(MobSpawner.CustomMobType.YOBO, MobSpawner.CustomMobLevel.BRUTAL, Biome.EXTREME_HILLS_PLUS_MOUNTAINS);
/*     */   }
/*     */   
/*     */   public void onTarget(EntityTargetEvent event)
/*     */   {
/* 179 */     super.onTarget(event);
/*     */     
/* 181 */     if ((event.getReason().equals(EntityTargetEvent.TargetReason.FORGOT_TARGET)) || 
/* 182 */       (event.getReason().equals(EntityTargetEvent.TargetReason.TARGET_DIED))) {
/* 183 */       this.angry = false;
/* 184 */       for (Entity e : this.minions) {
/* 185 */         e.getBukkitEntity().remove();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\Yobo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */