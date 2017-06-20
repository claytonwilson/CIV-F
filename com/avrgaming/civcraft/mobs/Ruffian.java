/*     */ package com.avrgaming.civcraft.mobs;
/*     */ 
/*     */ import com.avrgaming.civcraft.mobs.components.MobComponentDefense;
/*     */ import com.avrgaming.civcraft.threading.TaskMaster;
/*     */ import com.avrgaming.civcraft.threading.tasks.FireWorkTask;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.avrgaming.mob.ICustomMob;
/*     */ import com.avrgaming.mob.MobBaseWitch;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.v1_7_R2.AxisAlignedBB;
/*     */ import net.minecraft.server.v1_7_R2.DamageSource;
/*     */ import net.minecraft.server.v1_7_R2.EntityCreature;
/*     */ import net.minecraft.server.v1_7_R2.EntityHuman;
/*     */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*     */ import net.minecraft.server.v1_7_R2.EntityLiving;
/*     */ import net.minecraft.server.v1_7_R2.EntityPlayer;
/*     */ import net.minecraft.server.v1_7_R2.IRangedEntity;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalArrowAttack;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
/*     */ import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
/*     */ import net.minecraft.server.v1_7_R2.WorldServer;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.FireworkEffect;
/*     */ import org.bukkit.FireworkEffect.Builder;
/*     */ import org.bukkit.FireworkEffect.Type;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.block.Biome;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class Ruffian
/*     */   extends CommonCustomMob implements ICustomMob
/*     */ {
/*     */   private double damage;
/*     */   
/*     */   public void onCreate()
/*     */   {
/*  50 */     initLevelAndType();
/*     */     
/*  52 */     getGoalSelector().a(1, new PathfinderGoalFloat((EntityInsentient)this.entity));
/*  53 */     getGoalSelector().a(2, new PathfinderGoalArrowAttack((IRangedEntity)this.entity, 1.0D, 60, 10.0F));
/*  54 */     getGoalSelector().a(2, new PathfinderGoalRandomStroll((EntityCreature)this.entity, 1.0D));
/*  55 */     getGoalSelector().a(3, new PathfinderGoalLookAtPlayer((EntityInsentient)this.entity, EntityHuman.class, 8.0F));
/*  56 */     getGoalSelector().a(3, new PathfinderGoalRandomLookaround((EntityInsentient)this.entity));
/*  57 */     getTargetSelector().a(1, new PathfinderGoalHurtByTarget((EntityCreature)this.entity, false));
/*  58 */     getTargetSelector().a(2, new PathfinderGoalNearestAttackableTarget((EntityCreature)this.entity, EntityHuman.class, 0, true));
/*  59 */     setName(this.level.getName() + " " + this.type.getName());
/*     */   }
/*     */   
/*     */   public void onCreateAttributes()
/*     */   {
/*  64 */     setKnockbackResistance(0.5D);
/*  65 */     setMovementSpeed(0.2D);
/*     */     MobComponentDefense defense;
/*  67 */     switch (this.level) {
/*     */     case BRUTAL: 
/*  69 */       MobComponentDefense defense = new MobComponentDefense(3.5D);
/*  70 */       setMaxHealth(10.0D);
/*  71 */       modifySpeed(1.8D);
/*  72 */       this.damage = 15.0D;
/*     */       
/*  74 */       addDrop("mat_ionic_crystal_fragment_1", 0.05D);
/*     */       
/*  76 */       addDrop("mat_forged_clay", 0.1D);
/*  77 */       addDrop("mat_crafted_reeds", 0.1D);
/*  78 */       addDrop("mat_crafted_sticks", 0.1D);
/*  79 */       addVanillaDrop(ItemManager.getId(Material.LEATHER), (short)0, 0.4D);
/*  80 */       addDrop("mat_refined_sulphur", 0.15D);
/*  81 */       coinDrop(1, 25);
/*     */       
/*  83 */       break;
/*     */     case ELITE: 
/*  85 */       MobComponentDefense defense = new MobComponentDefense(10.0D);
/*  86 */       setMaxHealth(15.0D);
/*  87 */       modifySpeed(1.9D);
/*  88 */       this.damage = 20.0D;
/*     */       
/*  90 */       addDrop("mat_ionic_crystal_fragment_2", 0.05D);
/*     */       
/*  92 */       addDrop("mat_carved_leather", 0.1D);
/*  93 */       addDrop("mat_crafted_string", 0.03D);
/*  94 */       addDrop("mat_refined_slime", 0.05D);
/*     */       
/*  96 */       addDrop("mat_varnish", 0.01D);
/*  97 */       addDrop("mat_sticky_resin", 0.01D);
/*  98 */       addDrop("mat_refined_sulphur", 0.25D);
/*  99 */       coinDrop(10, 50);
/*     */       
/* 101 */       break;
/*     */     case GREATER: 
/* 103 */       MobComponentDefense defense = new MobComponentDefense(16.0D);
/* 104 */       setMaxHealth(20.0D);
/* 105 */       modifySpeed(2.0D);
/* 106 */       this.damage = 25.0D;
/*     */       
/* 108 */       addDrop("mat_ionic_crystal_fragment_3", 0.05D);
/*     */       
/* 110 */       addDrop("mat_leather_straps", 0.1D);
/* 111 */       addDrop("mat_crafted_string", 0.05D);
/* 112 */       addDrop("mat_refined_slime", 0.05D);
/*     */       
/* 114 */       addDrop("mat_varnish", 0.01D);
/* 115 */       addDrop("mat_sticky_resin", 0.01D);
/* 116 */       addDrop("mat_refined_sulphur", 0.35D);
/* 117 */       coinDrop(20, 80);
/*     */       
/* 119 */       break;
/*     */     case LESSER: 
/* 121 */       MobComponentDefense defense = new MobComponentDefense(20.0D);
/* 122 */       setMaxHealth(30.0D);
/* 123 */       modifySpeed(2.0D);
/* 124 */       this.damage = 32.0D;
/*     */       
/* 126 */       addDrop("mat_ionic_crystal_fragment_4", 0.05D);
/*     */       
/* 128 */       addDrop("mat_artisan_leather", 0.1D);
/* 129 */       addDrop("mat_crafted_string", 0.1D);
/* 130 */       addDrop("mat_refined_slime", 0.05D);
/*     */       
/* 132 */       addDrop("mat_varnish", 0.01D);
/* 133 */       addDrop("mat_sticky_resin", 0.01D);
/* 134 */       addDrop("mat_refined_sulphur", 0.5D);
/* 135 */       coinDrop(20, 150);
/*     */       
/* 137 */       break;
/*     */     default: 
/* 139 */       defense = new MobComponentDefense(2.0D);
/*     */     }
/*     */     
/*     */     
/* 143 */     addComponent(defense);
/*     */   }
/*     */   
/*     */   public String getBaseEntity()
/*     */   {
/* 148 */     return MobBaseWitch.class.getName();
/*     */   }
/*     */   
/*     */   public void onRangedAttack(net.minecraft.server.v1_7_R2.Entity target)
/*     */   {
/* 153 */     if (!(target instanceof EntityPlayer)) {
/* 154 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 284 */     Runnable follow = new Runnable()
/*     */     {
/*     */       public Ruffian.1RuffianProjectile proj;
/*     */       
/*     */       public void run()
/*     */       {
/* 276 */         if (this.proj.advance()) {
/* 277 */           this.proj = null;
/* 278 */           return;
/*     */         }
/* 280 */         TaskMaster.syncTask(this, 1L);
/*     */       }
/*     */       
/*     */ 
/* 284 */     };
/* 285 */     Object proj = new Object()
/*     */     {
/*     */       Location loc;
/*     */       Location target;
/*     */       org.bukkit.entity.Entity attacker;
/* 161 */       int speed = 1;
/*     */       double damage;
/* 163 */       int splash = 6;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Vector getVectorBetween(Location to, Location from)
/*     */       {
/* 173 */         Vector dir = new Vector();
/*     */         
/* 175 */         dir.setX(to.getX() - from.getX());
/* 176 */         dir.setY(to.getY() - from.getY());
/* 177 */         dir.setZ(to.getZ() - from.getZ());
/*     */         
/* 179 */         return dir;
/*     */       }
/*     */       
/*     */       public boolean advance() {
/* 183 */         Vector dir = getVectorBetween(this.target, this.loc).normalize();
/* 184 */         double distance = this.loc.distanceSquared(this.target);
/* 185 */         dir.multiply(this.speed);
/*     */         
/* 187 */         this.loc.add(dir);
/* 188 */         this.loc.getWorld().createExplosion(this.loc, 0.0F, false);
/* 189 */         distance = this.loc.distanceSquared(this.target);
/*     */         
/* 191 */         if (distance < this.speed * 1.5D) {
/* 192 */           this.loc.setX(this.target.getX());
/* 193 */           this.loc.setY(this.target.getY());
/* 194 */           this.loc.setZ(this.target.getZ());
/* 195 */           onHit();
/* 196 */           return true;
/*     */         }
/*     */         
/* 199 */         return false;
/*     */       }
/*     */       
/*     */       public void onHit() {
/* 203 */         int spread = 3;
/* 204 */         int[][] offset = { { -1 }, { 1 }, { 0, -1 }, { 0, 1 } };
/* 205 */         for (int i = 0; i < 4; i++) {
/* 206 */           int x = offset[i][0] * spread;
/* 207 */           int y = 0;
/* 208 */           int z = offset[i][1] * spread;
/*     */           
/* 210 */           Location location = new Location(this.loc.getWorld(), this.loc.getX(), this.loc.getY(), this.loc.getZ());
/* 211 */           location = location.add(x, y, z);
/*     */           
/* 213 */           launchExplodeFirework(location);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 218 */         launchExplodeFirework(this.loc);
/*     */         
/* 220 */         damagePlayers(this.loc, this.splash);
/*     */       }
/*     */       
/*     */       private void damagePlayers(Location loc, int radius)
/*     */       {
/* 225 */         double x = loc.getX() + 0.5D;
/* 226 */         double y = loc.getY() + 0.5D;
/* 227 */         double z = loc.getZ() + 0.5D;
/* 228 */         double r = radius;
/*     */         
/* 230 */         CraftWorld craftWorld = (CraftWorld)this.attacker.getWorld();
/*     */         
/* 232 */         AxisAlignedBB bb = AxisAlignedBB.a(x - r, y - r, z - r, x + r, y + r, z + r);
/*     */         
/*     */ 
/* 235 */         List<net.minecraft.server.v1_7_R2.Entity> entities = craftWorld.getHandle().getEntities(((CraftEntity)this.attacker).getHandle(), bb);
/*     */         
/* 237 */         for (net.minecraft.server.v1_7_R2.Entity e : entities) {
/* 238 */           if ((e instanceof EntityPlayer)) {
/* 239 */             EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(this.attacker, ((EntityPlayer)e).getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, this.damage);
/* 240 */             Bukkit.getServer().getPluginManager().callEvent(event);
/* 241 */             e.damageEntity(DamageSource.GENERIC, (float)event.getDamage());
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       private void launchExplodeFirework(Location loc)
/*     */       {
/* 264 */         FireworkEffect fe = FireworkEffect.builder().withColor(Color.ORANGE).withColor(Color.YELLOW).flicker(true).with(FireworkEffect.Type.BURST).build();
/* 265 */         TaskMaster.syncTask(new FireWorkTask(fe, loc.getWorld(), loc, 3), 0L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 286 */     };
/* 287 */     follow.proj = proj;
/* 288 */     TaskMaster.syncTask(follow);
/*     */   }
/*     */   
/*     */   public Location getLocation(EntityPlayer p) {
/* 292 */     org.bukkit.World world = Bukkit.getWorld(p.world.getWorld().getName());
/* 293 */     Location loc = new Location(world, p.locX, p.locY, p.locZ);
/* 294 */     return loc;
/*     */   }
/*     */   
/*     */   public String getClassName()
/*     */   {
/* 299 */     return Ruffian.class.getName();
/*     */   }
/*     */   
/*     */   public static void register() {
/* 303 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.LESSER, Biome.JUNGLE);
/* 304 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.LESSER, Biome.MEGA_TAIGA);
/* 305 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.LESSER, Biome.JUNGLE_EDGE);
/* 306 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.LESSER, Biome.JUNGLE_EDGE_MOUNTAINS);
/* 307 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.LESSER, Biome.SWAMPLAND);
/*     */     
/*     */ 
/* 310 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.GREATER, Biome.MEGA_SPRUCE_TAIGA_HILLS);
/* 311 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.GREATER, Biome.MEGA_SPRUCE_TAIGA_HILLS);
/* 312 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.GREATER, Biome.JUNGLE_HILLS);
/*     */     
/*     */ 
/* 315 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.ELITE, Biome.BIRCH_FOREST_HILLS_MOUNTAINS);
/* 316 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.ELITE, Biome.ROOFED_FOREST_MOUNTAINS);
/* 317 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.BRUTAL, Biome.JUNGLE_MOUNTAINS);
/* 318 */     setValidBiome(MobSpawner.CustomMobType.RUFFIAN, MobSpawner.CustomMobLevel.BRUTAL, Biome.SWAMPLAND_MOUNTAINS);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\mobs\Ruffian.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */