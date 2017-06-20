/*     */ package com.avrgaming.civcraft.mobs;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.mob.ICustomMob;
/*     */ import com.avrgaming.moblib.MobLib;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import net.minecraft.server.v1_7_R2.EntityLiving;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.block.Block;
/*     */ 
/*     */ public class MobSpawner
/*     */ {
/*     */   public static enum CustomMobLevel
/*     */   {
/*  17 */     LESSER("§aLesser"), 
/*  18 */     GREATER("§eGreater"), 
/*  19 */     ELITE("§dElite"), 
/*  20 */     BRUTAL("§cBrutal");
/*     */     
/*     */     private String name;
/*     */     
/*     */     private CustomMobLevel(String name) {
/*  25 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  29 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum CustomMobType {
/*  34 */     YOBO("Yobo", "com.avrgaming.civcraft.mobs.Yobo"), 
/*  35 */     YOBOBOSS("Yobo Boss", "com.avrgaming.civcraft.mobs.YoboBoss"), 
/*  36 */     RUFFIAN("Ruffian", "com.avrgaming.civcraft.mobs.Ruffian"), 
/*  37 */     BEHEMOTH("Behemoth", "com.avrgaming.civcraft.mobs.Behemoth"), 
/*  38 */     SAVAGE("Cannibal", "com.avrgaming.civcraft.mobs.Savage"), 
/*  39 */     ANGRYYOBO("Angry Yobo", "com.avrgaming.civcraft.mobs.AngryYobo");
/*     */     
/*     */     private String name;
/*     */     private String className;
/*     */     
/*     */     private CustomMobType(String name, String className) {
/*  45 */       this.name = name;
/*  46 */       this.className = className;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  50 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getClassName() {
/*  54 */       return this.className;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void register() {
/*  59 */     Yobo.register();
/*  60 */     Behemoth.register();
/*  61 */     Savage.register();
/*  62 */     Ruffian.register();
/*     */   }
/*     */   
/*     */   public static void despawnAll() {
/*  66 */     for (CommonCustomMob mob : CommonCustomMob.customMobs.values()) {
/*  67 */       mob.entity.getBukkitEntity().remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public static CommonCustomMob spawnCustomMob(CustomMobType type, CustomMobLevel level, Location loc) throws CivException {
/*  72 */     ICustomMob custom = MobLib.spawnCustom(type.className, loc);
/*  73 */     if (custom == null) {
/*  74 */       throw new CivException("Couldn't spawn custom mob type:" + type.toString());
/*     */     }
/*     */     
/*  77 */     custom.setData("type", type.toString().toUpperCase());
/*  78 */     custom.setData("level", level.toString().toUpperCase());
/*     */     
/*  80 */     custom.onCreate();
/*  81 */     custom.onCreateAttributes();
/*     */     
/*  83 */     CommonCustomMob common = (CommonCustomMob)custom;
/*  84 */     CommonCustomMob.customMobs.put(common.entity.getUniqueID(), common);
/*  85 */     return common;
/*     */   }
/*     */   
/*     */   public static void spawnRandomCustomMob(Location location) {
/*  89 */     LinkedList<TypeLevel> validMobs = CommonCustomMob.getValidMobsForBiome(location.getBlock().getBiome());
/*  90 */     if (validMobs.size() == 0) {
/*  91 */       return;
/*     */     }
/*     */     
/*  94 */     Random random = new Random();
/*  95 */     int idx = random.nextInt(validMobs.size());
/*     */     
/*  97 */     CustomMobType type = ((TypeLevel)validMobs.get(idx)).type;
/*  98 */     CustomMobLevel level = ((TypeLevel)validMobs.get(idx)).level;
/*     */     try
/*     */     {
/* 101 */       spawnCustomMob(type, level, location);
/*     */     } catch (CivException e) {
/* 103 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\MobSpawner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */