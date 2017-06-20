/*     */ package com.avrgaming.civcraft.mobs;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.mobs.components.MobComponent;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import com.avrgaming.civcraft.util.ChunkCoord;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.civcraft.war.War;
/*     */ import com.avrgaming.mob.ICustomMob;
/*     */ import com.avrgaming.mob.ISpawnable;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.v1_7_R2.AttributeInstance;
/*     */ import net.minecraft.server.v1_7_R2.DamageSource;
/*     */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*     */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*     */ import net.minecraft.server.v1_7_R2.EntityLiving;
/*     */ import net.minecraft.server.v1_7_R2.GenericAttributes;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Biome;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
/*     */ import org.bukkit.entity.ExperienceOrb;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.entity.EntityTargetEvent;
/*     */ import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CommonCustomMob
/*     */   implements ICustomMob
/*     */ {
/*  48 */   public static HashMap<UUID, CommonCustomMob> customMobs = new HashMap();
/*  49 */   public static HashMap<String, LinkedList<TypeLevel>> biomes = new HashMap();
/*     */   
/*     */   public MobSpawner.CustomMobType type;
/*     */   
/*     */   public MobSpawner.CustomMobLevel level;
/*     */   public EntityLiving entity;
/*  55 */   public HashMap<String, String> dataMap = new HashMap();
/*  56 */   public HashMap<String, MobComponent> components = new HashMap();
/*  57 */   public LinkedList<MobDrop> drops = new LinkedList();
/*     */   
/*     */ 
/*     */   private String targetName;
/*     */   
/*     */   private Location lastLocation;
/*     */   
/*  64 */   private int coinMin = 0;
/*  65 */   private int coinMax = 0;
/*     */   
/*     */   public void setName(String name) {
/*  68 */     ((EntityInsentient)this.entity).setCustomName(name);
/*  69 */     ((EntityInsentient)this.entity).setCustomNameVisible(true);
/*     */   }
/*     */   
/*     */   public PathfinderGoalSelector getGoalSelector() {
/*  73 */     if (this.entity == null) {
/*  74 */       return null;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  79 */       Field gsa = EntityInsentient.class.getDeclaredField("goalSelector");
/*  80 */       gsa.setAccessible(true);
/*  81 */       return (PathfinderGoalSelector)gsa.get((EntityInsentient)this.entity);
/*     */     } catch (NoSuchFieldException e) {
/*  83 */       e.printStackTrace();
/*     */     } catch (SecurityException e) {
/*  85 */       e.printStackTrace();
/*     */     } catch (IllegalArgumentException e) {
/*  87 */       e.printStackTrace();
/*     */     } catch (IllegalAccessException e) {
/*  89 */       e.printStackTrace();
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */   
/*     */   public PathfinderGoalSelector getTargetSelector()
/*     */   {
/*     */     try {
/*  97 */       Field gsa = EntityInsentient.class.getDeclaredField("targetSelector");
/*  98 */       gsa.setAccessible(true);
/*  99 */       return (PathfinderGoalSelector)gsa.get((EntityInsentient)this.entity);
/*     */     } catch (NoSuchFieldException e) {
/* 101 */       e.printStackTrace();
/*     */     } catch (SecurityException e) {
/* 103 */       e.printStackTrace();
/*     */     } catch (IllegalArgumentException e) {
/* 105 */       e.printStackTrace();
/*     */     } catch (IllegalAccessException e) {
/* 107 */       e.printStackTrace();
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */   
/*     */   protected void initLevelAndType() {
/* 113 */     this.type = MobSpawner.CustomMobType.valueOf(getData("type"));
/* 114 */     this.level = MobSpawner.CustomMobLevel.valueOf(getData("level"));
/*     */     
/*     */ 
/* 117 */     addVanillaDrop(ItemManager.getId(Material.BONE), (short)0, 0.1D);
/* 118 */     addVanillaDrop(ItemManager.getId(Material.SUGAR), (short)0, 0.1D);
/* 119 */     addVanillaDrop(ItemManager.getId(Material.SULPHUR), (short)0, 0.25D);
/* 120 */     addVanillaDrop(ItemManager.getId(Material.POTATO_ITEM), (short)0, 0.1D);
/* 121 */     addVanillaDrop(ItemManager.getId(Material.CARROT_ITEM), (short)0, 0.1D);
/* 122 */     addVanillaDrop(ItemManager.getId(Material.COAL), (short)0, 0.1D);
/* 123 */     addVanillaDrop(ItemManager.getId(Material.STRING), (short)0, 0.1D);
/* 124 */     addVanillaDrop(ItemManager.getId(Material.SLIME_BALL), (short)0, 0.02D);
/*     */   }
/*     */   
/*     */   public Location getLocation(EntityLiving entity2)
/*     */   {
/* 129 */     org.bukkit.World world = Bukkit.getWorld(entity2.world.getWorld().getName());
/* 130 */     Location loc = new Location(world, entity2.locX, entity2.locY, entity2.locZ);
/* 131 */     return loc;
/*     */   }
/*     */   
/*     */   public void printGoals(PathfinderGoalSelector goals) {
/* 135 */     System.out.println("Printing goals:");
/*     */     try
/*     */     {
/* 138 */       Field gsa = PathfinderGoalSelector.class.getDeclaredField("b");
/* 139 */       gsa.setAccessible(true);
/*     */       
/* 141 */       UnsafeList<?> list = (UnsafeList)gsa.get(goals);
/* 142 */       for (Object obj : list) {
/* 143 */         System.out.println("Obj:" + obj.toString());
/*     */       }
/*     */     }
/*     */     catch (NoSuchFieldException e) {
/* 147 */       e.printStackTrace();
/*     */     } catch (SecurityException e) {
/* 149 */       e.printStackTrace();
/*     */     } catch (IllegalArgumentException e) {
/* 151 */       e.printStackTrace();
/*     */     } catch (IllegalAccessException e) {
/* 153 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getBaseEntity()
/*     */   {
/* 161 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onCreate() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onCreateAttributes() {}
/*     */   
/*     */ 
/*     */   public void onDamage(EntityCreature e, DamageSource damagesource, PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector) {}
/*     */   
/*     */ 
/*     */   public void onDeath(EntityCreature arg0)
/*     */   {
/* 178 */     dropItems();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onRangedAttack(net.minecraft.server.v1_7_R2.Entity arg1) {}
/*     */   
/*     */   private void checkForStuck()
/*     */   {
/* 186 */     if ((this.targetName != null) && (this.lastLocation != null)) {
/* 187 */       Location loc = getLocation(this.entity);
/* 188 */       if (loc.distance(this.lastLocation) < 0.5D) {
/*     */         try
/*     */         {
/* 191 */           Player player = CivGlobal.getPlayer(this.targetName);
/* 192 */           this.entity.getBukkitEntity().teleport(player.getLocation());
/*     */         }
/*     */         catch (CivException e) {
/* 195 */           this.targetName = null;
/* 196 */           this.lastLocation = null;
/*     */         }
/*     */       }
/* 199 */       this.lastLocation = loc;
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkForTownBorders() {
/* 204 */     Location loc = getLocation(this.entity);
/* 205 */     TownChunk tc = CivGlobal.getTownChunk(loc);
/* 206 */     if (tc != null) {
/* 207 */       this.entity.getBukkitEntity().remove();
/*     */     }
/*     */     
/* 210 */     Camp camp = CivGlobal.getCampFromChunk(new ChunkCoord(loc));
/* 211 */     if (camp != null) {
/* 212 */       this.entity.getBukkitEntity().remove();
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkForisWarTime() {
/* 217 */     if (War.isWarTime()) {
/* 218 */       this.entity.getBukkitEntity().remove();
/*     */     }
/*     */   }
/*     */   
/* 222 */   private int tickCount = 0;
/*     */   
/*     */   public void onTick() {
/* 225 */     if (this.entity == null) {
/* 226 */       return;
/*     */     }
/*     */     
/* 229 */     this.tickCount += 1;
/* 230 */     if (this.tickCount > 90) {
/* 231 */       checkForStuck();
/* 232 */       checkForTownBorders();
/* 233 */       checkForisWarTime();
/* 234 */       this.tickCount = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setData(String key, String value) {
/* 239 */     this.dataMap.put(key, value);
/*     */   }
/*     */   
/*     */   public String getData(String key)
/*     */   {
/* 244 */     return (String)this.dataMap.get(key);
/*     */   }
/*     */   
/*     */   public void setEntity(EntityLiving e)
/*     */   {
/* 249 */     this.entity = e;
/*     */   }
/*     */   
/*     */   public Collection<MobComponent> getMobComponents() {
/* 253 */     return this.components.values();
/*     */   }
/*     */   
/*     */   public void addComponent(MobComponent comp) {
/* 257 */     this.components.put(comp.getClass().getName(), comp);
/*     */   }
/*     */   
/*     */   public static CommonCustomMob getCCM(net.minecraft.server.v1_7_R2.Entity e) {
/* 261 */     if (!(e instanceof ISpawnable)) {
/* 262 */       return null;
/*     */     }
/*     */     
/* 265 */     ISpawnable spawn = (ISpawnable)e;
/* 266 */     return (CommonCustomMob)spawn.getCustomMobInterface();
/*     */   }
/*     */   
/*     */   public static CommonCustomMob getCCM(org.bukkit.entity.Entity entity) {
/* 270 */     net.minecraft.server.v1_7_R2.Entity e = ((CraftEntity)entity).getHandle();
/* 271 */     return getCCM(e);
/*     */   }
/*     */   
/*     */   public void setAttack(double attack)
/*     */   {
/* 276 */     this.entity.getAttributeInstance(GenericAttributes.e).setValue(attack);
/*     */   }
/*     */   
/*     */   public void setMovementSpeed(double speed) {
/* 280 */     this.entity.getAttributeInstance(GenericAttributes.d).setValue(speed);
/*     */   }
/*     */   
/*     */   public void setFollowRange(double range) {
/* 284 */     this.entity.getAttributeInstance(GenericAttributes.b).setValue(range);
/*     */   }
/*     */   
/*     */   public double getFollowRange() {
/*     */     double value;
/*     */     try {
/* 290 */       value = this.entity.getAttributeInstance(GenericAttributes.b).getValue();
/*     */     } catch (NullPointerException e) { double value;
/* 292 */       value = 32.0D;
/*     */     }
/*     */     
/* 295 */     return value;
/*     */   }
/*     */   
/*     */   public void modifySpeed(double percent) {
/* 299 */     double speed = this.entity.getAttributeInstance(GenericAttributes.d).getValue();
/* 300 */     speed *= percent;
/* 301 */     setMovementSpeed(speed);
/*     */   }
/*     */   
/*     */   public void setMaxHealth(double health) {
/* 305 */     this.entity.getAttributeInstance(GenericAttributes.a).setValue(health);
/* 306 */     this.entity.setHealth((float)health);
/*     */   }
/*     */   
/*     */   public void setKnockbackResistance(double resist) {
/* 310 */     this.entity.getAttributeInstance(GenericAttributes.c).setValue(resist);
/*     */   }
/*     */   
/*     */   protected void printAttributes()
/*     */   {
/*     */     try
/*     */     {
/* 317 */       if (this.entity == null) {
/* 318 */         CivLog.info("Entity was null!");
/*     */       }
/* 320 */       CivLog.info("Speed:" + this.entity.getAttributeInstance(GenericAttributes.d).getValue());
/* 321 */       CivLog.info("MaxHealth:" + this.entity.getAttributeInstance(GenericAttributes.a).getValue() + " Health:" + this.entity.getHealth());
/*     */     } catch (Exception e) {
/* 323 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public String getSaveString()
/*     */   {
/* 329 */     return getData("type") + ":" + getData("level");
/*     */   }
/*     */   
/*     */   public void loadSaveString(String str)
/*     */   {
/* 334 */     String[] split = str.split(":");
/* 335 */     setData("type", split[0]);
/* 336 */     setData("level", split[1]);
/* 337 */     if (this.entity == null) {
/* 338 */       return;
/*     */     }
/*     */     
/* 341 */     onCreate();
/* 342 */     onCreateAttributes();
/*     */   }
/*     */   
/*     */   public abstract String getClassName();
/*     */   
/*     */   public static void setValidBiome(MobSpawner.CustomMobType type, MobSpawner.CustomMobLevel level, Biome biome)
/*     */   {
/* 349 */     LinkedList<TypeLevel> mobs = (LinkedList)biomes.get(biome);
/* 350 */     if (mobs == null) {
/* 351 */       mobs = new LinkedList();
/*     */     }
/*     */     
/* 354 */     mobs.add(new TypeLevel(type, level));
/* 355 */     biomes.put(biome.name(), mobs);
/*     */   }
/*     */   
/*     */   public static LinkedList<TypeLevel> getValidMobsForBiome(Biome biome) {
/* 359 */     LinkedList<TypeLevel> mobs = (LinkedList)biomes.get(biome.name());
/* 360 */     if (mobs == null) {
/* 361 */       mobs = new LinkedList();
/*     */     }
/*     */     
/* 364 */     return mobs;
/*     */   }
/*     */   
/*     */   public void onTarget(EntityTargetEvent event) {
/* 368 */     if (event.isCancelled()) {
/* 369 */       return;
/*     */     }
/*     */     
/* 372 */     if (((event.getReason().equals(EntityTargetEvent.TargetReason.CLOSEST_PLAYER)) || 
/* 373 */       (event.getReason().equals(EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET))) && 
/* 374 */       ((event.getTarget() instanceof Player)))
/*     */     {
/* 376 */       double followRange = getFollowRange();
/* 377 */       double distance = event.getEntity().getLocation().distance(event.getTarget().getLocation());
/* 378 */       if (distance - 0.5D <= followRange) {
/* 379 */         this.targetName = ((Player)event.getTarget()).getName();
/* 380 */         this.lastLocation = event.getEntity().getLocation();
/*     */       }
/*     */     } else {
/* 383 */       this.targetName = null;
/* 384 */       this.lastLocation = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void addVanillaDrop(int type, short data, double chance) {
/* 389 */     MobDrop drop = new MobDrop();
/* 390 */     drop.isVanillaDrop = true;
/* 391 */     drop.vanillaType = type;
/* 392 */     drop.vanillaData = data;
/* 393 */     drop.chance = chance;
/*     */     
/* 395 */     this.drops.add(drop);
/*     */   }
/*     */   
/*     */   public void addDrop(String craftMatId, double chance) {
/* 399 */     MobDrop drop = new MobDrop();
/* 400 */     drop.isVanillaDrop = false;
/* 401 */     drop.craftMatId = craftMatId;
/* 402 */     drop.chance = chance;
/*     */     
/*     */ 
/*     */ 
/* 406 */     this.drops.add(drop);
/*     */   }
/*     */   
/*     */   public LinkedList<MobDrop> getRandomDrops() {
/* 410 */     Random rand = new Random();
/* 411 */     LinkedList<MobDrop> dropped = new LinkedList();
/*     */     
/* 413 */     for (MobDrop d : this.drops) {
/* 414 */       int chance = rand.nextInt(1000);
/* 415 */       if (chance < d.chance * 1000.0D)
/*     */       {
/* 417 */         dropped.add(d);
/*     */       }
/*     */     }
/*     */     
/* 421 */     return dropped;
/*     */   }
/*     */   
/*     */   public void dropItems() {
/*     */     try {
/* 426 */       if (this.entity == null) {
/* 427 */         return;
/*     */       }
/*     */       
/* 430 */       LinkedList<MobDrop> dropped = getRandomDrops();
/* 431 */       org.bukkit.World world = this.entity.getBukkitEntity().getWorld();
/* 432 */       Location loc = getLocation(this.entity);
/*     */       
/* 434 */       for (MobDrop d : dropped) { ItemStack stack;
/*     */         ItemStack stack;
/* 436 */         if (d.isVanillaDrop) {
/* 437 */           stack = ItemManager.createItemStack(d.vanillaType, 1, d.vanillaData);
/*     */         } else {
/* 439 */           CivLog.info("Dropping item:" + d.craftMatId);
/* 440 */           LoreCraftableMaterial craftMat = LoreCraftableMaterial.getCraftMaterialFromId(d.craftMatId);
/* 441 */           stack = LoreCraftableMaterial.spawn(craftMat);
/*     */         }
/*     */         
/* 444 */         world.dropItem(loc, stack);
/*     */       }
/*     */       
/* 447 */       if ((this.coinMax != 0) && (this.coinMin != 0)) {
/* 448 */         ExperienceOrb orb = (ExperienceOrb)world.spawn(loc, ExperienceOrb.class);
/* 449 */         Random random = new Random();
/* 450 */         int coins = random.nextInt(this.coinMax - this.coinMin) + this.coinMin;
/* 451 */         orb.setExperience(coins);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 455 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void coinDrop(int min, int max) {
/* 460 */     this.coinMin = min;
/* 461 */     this.coinMax = max;
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\CommonCustomMob.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */