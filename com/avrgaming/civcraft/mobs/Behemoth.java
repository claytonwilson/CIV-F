/*     */ package com.avrgaming.civcraft.mobs;
/*     */ 
/*     */ import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
/*     */ import com.avrgaming.mob.ICustomMob;
/*     */ import com.avrgaming.mob.MobBaseIronGolem;
/*     */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*     */ import net.minecraft.server.v1_7_R2.EntityHuman;
/*     */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*     */ import org.bukkit.block.Biome;
/*     */ 
/*     */ public class Behemoth
/*     */   extends CommonCustomMob
/*     */   implements ICustomMob
/*     */ {
/*     */   public void onCreate()
/*     */   {
/*  22 */     initLevelAndType();
/*     */     
/*  24 */     getGoalSelector().a(7, new PathfinderGoalRandomStroll((EntityCreature)this.entity, 1.0D));
/*  25 */     getGoalSelector().a(8, new PathfinderGoalLookAtPlayer((EntityInsentient)this.entity, EntityHuman.class, 8.0F));
/*  26 */     getGoalSelector().a(2, new PathfinderGoalMeleeAttack((EntityCreature)this.entity, EntityHuman.class, 1.0D, false));
/*  27 */     getTargetSelector().a(2, new PathfinderGoalNearestAttackableTarget((EntityCreature)this.entity, EntityHuman.class, 0, true));
/*     */     
/*  29 */     setName(this.level.getName() + " " + this.type.getName());
/*     */   }
/*     */   
/*     */   public void onCreateAttributes()
/*     */   {
/*  34 */     setKnockbackResistance(1.0D);
/*  35 */     setMovementSpeed(0.15D);
/*     */     MobComponentDefense defense;
/*  37 */     switch (this.level) {
/*     */     case BRUTAL: 
/*  39 */       MobComponentDefense defense = new MobComponentDefense(3.5D);
/*  40 */       setMaxHealth(75.0D);
/*  41 */       modifySpeed(1.3D);
/*  42 */       addDrop("mat_ionic_crystal_fragment_1", 0.05D);
/*     */       
/*  44 */       addDrop("mat_forged_clay", 0.1D);
/*  45 */       addDrop("mat_crafted_reeds", 0.1D);
/*  46 */       addDrop("mat_crafted_sticks", 0.1D);
/*  47 */       coinDrop(1, 25);
/*  48 */       break;
/*     */     case ELITE: 
/*  50 */       MobComponentDefense defense = new MobComponentDefense(10.0D);
/*  51 */       setMaxHealth(125.0D);
/*  52 */       modifySpeed(1.4D);
/*  53 */       addDrop("mat_ionic_crystal_fragment_2", 0.05D);
/*     */       
/*  55 */       addDrop("mat_steel_plate", 0.1D);
/*  56 */       addDrop("mat_steel_ingot", 0.05D);
/*  57 */       addDrop("mat_clay_molding", 0.05D);
/*     */       
/*  59 */       addDrop("mat_varnish", 0.01D);
/*  60 */       addDrop("mat_sticky_resin", 0.05D);
/*  61 */       coinDrop(10, 50);
/*  62 */       break;
/*     */     case GREATER: 
/*  64 */       MobComponentDefense defense = new MobComponentDefense(16.0D);
/*  65 */       setMaxHealth(150.0D);
/*  66 */       modifySpeed(1.5D);
/*  67 */       addDrop("mat_ionic_crystal_fragment_3", 0.05D);
/*     */       
/*  69 */       addDrop("mat_carbide_steel_plate", 0.1D);
/*  70 */       addDrop("mat_carbide_steel_ingot", 0.05D);
/*  71 */       addDrop("mat_clay_molding", 0.05D);
/*     */       
/*  73 */       addDrop("mat_sticky_resin", 0.1D);
/*  74 */       addDrop("mat_smithy_resin", 0.05D);
/*  75 */       coinDrop(20, 80);
/*  76 */       break;
/*     */     case LESSER: 
/*  78 */       addDrop("mat_ionic_crystal_fragment_4", 0.05D);
/*     */       
/*  80 */       addDrop("mat_tungsten_plate", 0.1D);
/*  81 */       addDrop("mat_tungsten_ingot", 0.05D);
/*  82 */       addDrop("mat_clay_tungsten_casting", 0.05D);
/*     */       
/*  84 */       addDrop("mat_sticky_resin", 0.1D);
/*  85 */       addDrop("mat_smithy_resin", 0.05D);
/*     */       
/*  87 */       MobComponentDefense defense = new MobComponentDefense(20.0D);
/*  88 */       setMaxHealth(160.0D);
/*  89 */       modifySpeed(1.6D);
/*  90 */       coinDrop(20, 150);
/*     */       
/*  92 */       break;
/*     */     default: 
/*  94 */       defense = new MobComponentDefense(2.0D);
/*     */     }
/*     */     
/*     */     
/*  98 */     addComponent(defense);
/*     */   }
/*     */   
/*     */   public String getBaseEntity()
/*     */   {
/* 103 */     return MobBaseIronGolem.class.getName();
/*     */   }
/*     */   
/*     */   public String getClassName()
/*     */   {
/* 108 */     return Behemoth.class.getName();
/*     */   }
/*     */   
/*     */   public static void register()
/*     */   {
/* 113 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.LESSER, Biome.FROZEN_RIVER);
/* 114 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.LESSER, Biome.FROZEN_OCEAN);
/* 115 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.LESSER, Biome.COLD_BEACH);
/* 116 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.LESSER, Biome.COLD_TAIGA);
/*     */     
/* 118 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.GREATER, Biome.COLD_TAIGA_HILLS);
/* 119 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.GREATER, Biome.COLD_TAIGA_MOUNTAINS);
/*     */     
/* 121 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.ELITE, Biome.ICE_PLAINS);
/* 122 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.GREATER, Biome.ICE_MOUNTAINS);
/*     */     
/* 124 */     setValidBiome(MobSpawner.CustomMobType.BEHEMOTH, MobSpawner.CustomMobLevel.BRUTAL, Biome.ICE_PLAINS_SPIKES);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\Behemoth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */