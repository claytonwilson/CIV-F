/*     */ package com.avrgaming.civcraft.mobs;
/*     */ 
/*     */ import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
/*     */ import com.avrgaming.mob.ICustomMob;
/*     */ import com.avrgaming.mob.MobBasePigZombie;
/*     */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*     */ import net.minecraft.server.v1_7_R2.EntityHuman;
/*     */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*     */ import org.bukkit.block.Biome;
/*     */ 
/*     */ public class Savage
/*     */   extends CommonCustomMob
/*     */   implements ICustomMob
/*     */ {
/*     */   public void onCreate()
/*     */   {
/*  22 */     initLevelAndType();
/*     */     
/*  24 */     getGoalSelector().a(0, new PathfinderGoalFloat((EntityInsentient)this.entity));
/*  25 */     getGoalSelector().a(2, new PathfinderGoalMeleeAttack((EntityCreature)this.entity, EntityHuman.class, 1.0D, false));
/*  26 */     getGoalSelector().a(8, new PathfinderGoalLookAtPlayer((EntityInsentient)this.entity, EntityHuman.class, 8.0F));
/*     */     
/*  28 */     getTargetSelector().a(2, new PathfinderGoalNearestAttackableTarget((EntityCreature)this.entity, EntityHuman.class, 0, true));
/*  29 */     setName(this.level.getName() + " " + this.type.getName());
/*     */   }
/*     */   
/*     */   public void onCreateAttributes()
/*     */   {
/*  34 */     setKnockbackResistance(0.5D);
/*  35 */     setMovementSpeed(0.2D);
/*  36 */     setFollowRange(10.0D);
/*     */     MobComponentDefense defense;
/*  38 */     switch (this.level) {
/*     */     case BRUTAL: 
/*  40 */       MobComponentDefense defense = new MobComponentDefense(3.5D);
/*  41 */       setMaxHealth(10.0D);
/*  42 */       modifySpeed(1.8D);
/*  43 */       setAttack(5.0D);
/*     */       
/*  45 */       addDrop("mat_metallic_crystal_fragment_1", 0.05D);
/*     */       
/*  47 */       addDrop("mat_forged_clay", 0.1D);
/*  48 */       addDrop("mat_crafted_reeds", 0.1D);
/*  49 */       addDrop("mat_crafted_sticks", 0.1D);
/*  50 */       coinDrop(1, 25);
/*     */       
/*  52 */       break;
/*     */     case ELITE: 
/*  54 */       MobComponentDefense defense = new MobComponentDefense(10.0D);
/*  55 */       setMaxHealth(20.0D);
/*  56 */       modifySpeed(1.9D);
/*  57 */       setAttack(10.0D);
/*     */       
/*  59 */       addDrop("mat_metallic_crystal_fragment_2", 0.05D);
/*     */       
/*  61 */       addDrop("mat_aged_wood_stave", 0.1D);
/*  62 */       addDrop("mat_crafted_string", 0.05D);
/*     */       
/*  64 */       addDrop("mat_varnish", 0.01D);
/*  65 */       addDrop("mat_sticky_resin", 0.01D);
/*  66 */       coinDrop(10, 50);
/*     */       
/*  68 */       break;
/*     */     case GREATER: 
/*  70 */       MobComponentDefense defense = new MobComponentDefense(16.0D);
/*  71 */       setMaxHealth(40.0D);
/*  72 */       modifySpeed(2.0D);
/*  73 */       setAttack(15.0D);
/*     */       
/*  75 */       addDrop("mat_metallic_crystal_fragment_3", 0.05D);
/*     */       
/*  77 */       addDrop("mat_aged_wood_stave", 0.1D);
/*     */       
/*  79 */       addDrop("mat_varnish", 0.05D);
/*  80 */       addDrop("mat_woven_threading", 0.1D);
/*     */       
/*  82 */       addDrop("mat_sticky_resin", 0.1D);
/*  83 */       addDrop("mat_smithy_resin", 0.01D);
/*  84 */       coinDrop(20, 80);
/*     */       
/*  86 */       break;
/*     */     case LESSER: 
/*  88 */       MobComponentDefense defense = new MobComponentDefense(20.0D);
/*  89 */       setMaxHealth(80.0D);
/*  90 */       modifySpeed(2.0D);
/*  91 */       setAttack(25.0D);
/*     */       
/*  93 */       addDrop("mat_metallic_crystal_fragment_4", 0.05D);
/*     */       
/*  95 */       addDrop("mat_longbow_stave", 0.1D);
/*  96 */       addDrop("mat_reinforced_braid", 0.15D);
/*  97 */       addDrop("mat_leather_straps", 0.1D);
/*     */       
/*  99 */       addDrop("mat_sticky_resin", 0.1D);
/* 100 */       addDrop("mat_smithy_resin", 0.01D);
/* 101 */       coinDrop(20, 150);
/*     */       
/* 103 */       break;
/*     */     default: 
/* 105 */       defense = new MobComponentDefense(2.0D);
/*     */     }
/*     */     
/*     */     
/* 109 */     addComponent(defense);
/*     */   }
/*     */   
/*     */   public String getBaseEntity()
/*     */   {
/* 114 */     return MobBasePigZombie.class.getName();
/*     */   }
/*     */   
/*     */   public String getClassName()
/*     */   {
/* 119 */     return Savage.class.getName();
/*     */   }
/*     */   
/*     */   public static void register() {
/* 123 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.LESSER, Biome.DESERT);
/* 124 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.LESSER, Biome.DESERT_HILLS);
/* 125 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.LESSER, Biome.DESERT_MOUNTAINS);
/*     */     
/* 127 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.GREATER, Biome.SAVANNA);
/* 128 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.GREATER, Biome.SAVANNA_MOUNTAINS);
/* 129 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.GREATER, Biome.SAVANNA_PLATEAU);
/* 130 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.GREATER, Biome.SAVANNA_PLATEAU_MOUNTAINS);
/*     */     
/* 132 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.ELITE, Biome.MESA);
/* 133 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.ELITE, Biome.MESA_PLATEAU);
/* 134 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.ELITE, Biome.MEGA_TAIGA);
/* 135 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.ELITE, Biome.MEGA_SPRUCE_TAIGA);
/*     */     
/*     */ 
/* 138 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.BRUTAL, Biome.MESA_BRYCE);
/* 139 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.ELITE, Biome.MESA_PLATEAU_FOREST);
/* 140 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.BRUTAL, Biome.MESA_PLATEAU_MOUNTAINS);
/* 141 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.BRUTAL, Biome.MESA_PLATEAU_FOREST_MOUNTAINS);
/* 142 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.ELITE, Biome.MEGA_SPRUCE_TAIGA_HILLS);
/* 143 */     setValidBiome(MobSpawner.CustomMobType.SAVAGE, MobSpawner.CustomMobLevel.ELITE, Biome.MEGA_TAIGA_HILLS);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\Savage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */