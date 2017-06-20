/*     */ package gpl;
/*     */ 
/*     */ import com.google.common.io.Files;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.v1_7_R2.AttributeMapBase;
/*     */ import net.minecraft.server.v1_7_R2.AttributeMapServer;
/*     */ import net.minecraft.server.v1_7_R2.GenericAttributes;
/*     */ import net.minecraft.server.v1_7_R2.InventoryEnderChest;
/*     */ import net.minecraft.server.v1_7_R2.NBTCompressedStreamTools;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagCompound;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagDouble;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagFloat;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagList;
/*     */ import net.minecraft.server.v1_7_R2.PlayerAbilities;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.World.Environment;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftInventory;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftInventoryPlayer;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ import org.bukkit.util.Vector;
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
/*     */ public class ImprovedOfflinePlayer
/*     */ {
/*     */   private String player;
/*     */   private File file;
/*     */   private NBTTagCompound compound;
/*  74 */   private boolean exists = false;
/*  75 */   private boolean autosave = true;
/*     */   
/*  77 */   public ImprovedOfflinePlayer(String playername) { this.exists = loadPlayerData(playername); }
/*     */   
/*     */ 
/*  80 */   public ImprovedOfflinePlayer(OfflinePlayer offlineplayer) { this.exists = loadPlayerData(offlineplayer.getName()); }
/*     */   
/*     */   private boolean loadPlayerData(String name) {
/*     */     try {
/*  84 */       this.player = name;
/*  85 */       for (World w : Bukkit.getWorlds()) {
/*  86 */         this.file = new File(w.getWorldFolder(), "players" + File.separator + this.player + ".dat");
/*  87 */         if (this.file.exists()) {
/*  88 */           this.compound = NBTCompressedStreamTools.a(new FileInputStream(this.file));
/*  89 */           this.player = this.file.getCanonicalFile().getName().replace(".dat", "");
/*  90 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  95 */       e.printStackTrace();
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/* 100 */   public void savePlayerData() { if (this.exists)
/*     */       try {
/* 102 */         NBTCompressedStreamTools.a(this.compound, new FileOutputStream(this.file));
/*     */       }
/*     */       catch (Exception e) {
/* 105 */         e.printStackTrace();
/*     */       }
/*     */   }
/*     */   
/*     */   public boolean exists() {
/* 110 */     return this.exists;
/*     */   }
/*     */   
/* 113 */   public boolean getAutoSave() { return this.autosave; }
/*     */   
/*     */   public void setAutoSave(boolean autosave) {
/* 116 */     this.autosave = autosave;
/*     */   }
/*     */   
/*     */   public void copyDataTo(String playername) {
/*     */     try {
/* 121 */       if (!playername.equalsIgnoreCase(this.player)) {
/* 122 */         Player to = Bukkit.getPlayerExact(playername);
/* 123 */         Player from = Bukkit.getPlayerExact(this.player);
/* 124 */         if (from != null) {
/* 125 */           from.saveData();
/*     */         }
/* 127 */         Files.copy(this.file, new File(this.file.getParentFile(), playername + ".dat"));
/* 128 */         if (to != null) {
/* 129 */           to.teleport(from == null ? getLocation() : from.getLocation());
/* 130 */           to.loadData();
/*     */         }
/*     */       }
/*     */       else {
/* 134 */         Player player = Bukkit.getPlayerExact(this.player);
/* 135 */         if (player != null) {
/* 136 */           player.saveData();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 141 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/* 145 */   public PlayerAbilities getAbilities() { PlayerAbilities pa = new PlayerAbilities();
/* 146 */     pa.a(this.compound);
/* 147 */     return pa;
/*     */   }
/*     */   
/* 150 */   public void setAbilities(PlayerAbilities abilities) { abilities.a(this.compound);
/* 151 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 154 */   public float getAbsorptionAmount() { return this.compound.getFloat("AbsorptionAmount"); }
/*     */   
/*     */   public void setAbsorptionAmount(float input) {
/* 157 */     this.compound.setFloat("AbsorptionAmount", input);
/* 158 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 161 */   public AttributeMapBase getAttributes() { AttributeMapBase amb = new AttributeMapServer();
/* 162 */     GenericAttributes.a(amb, this.compound.getList("Attributes", 10));
/* 163 */     return amb;
/*     */   }
/*     */   
/* 166 */   public void setAttributes(AttributeMapBase attributes) { this.compound.set("Attributes", GenericAttributes.a(attributes));
/* 167 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 170 */   public Location getBedSpawnLocation() { return new Location(
/* 171 */       Bukkit.getWorld(this.compound.getString("SpawnWorld")), 
/* 172 */       this.compound.getInt("SpawnX"), 
/* 173 */       this.compound.getInt("SpawnY"), 
/* 174 */       this.compound.getInt("SpawnZ"));
/*     */   }
/*     */   
/*     */   public void setBedSpawnLocation(Location location, Boolean override) {
/* 178 */     this.compound.setInt("SpawnX", (int)location.getX());
/* 179 */     this.compound.setInt("SpawnY", (int)location.getY());
/* 180 */     this.compound.setInt("SpawnZ", (int)location.getZ());
/* 181 */     this.compound.setString("SpawnWorld", location.getWorld().getName());
/* 182 */     this.compound.setBoolean("SpawnForced", override == null ? false : override.booleanValue());
/* 183 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 186 */   public Inventory getEnderChest() { InventoryEnderChest endchest = new InventoryEnderChest();
/* 187 */     endchest.a(this.compound.getList("EnderItems", 10));
/* 188 */     return new CraftInventory(endchest);
/*     */   }
/*     */   
/* 191 */   public void setEnderChest(Inventory inventory) { this.compound.set("EnderItems", ((InventoryEnderChest)((CraftInventory)inventory).getInventory()).h());
/* 192 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 195 */   public float getExhaustion() { return this.compound.getFloat("foodExhaustionLevel"); }
/*     */   
/*     */   public void setExhaustion(float input) {
/* 198 */     this.compound.setFloat("foodExhaustionLevel", input);
/* 199 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 202 */   public float getExp() { return this.compound.getFloat("XpP"); }
/*     */   
/*     */   public void setExp(float input) {
/* 205 */     this.compound.setFloat("XpP", input);
/* 206 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 209 */   public float getFallDistance() { return this.compound.getFloat("FallDistance"); }
/*     */   
/*     */   public void setFallDistance(float input) {
/* 212 */     this.compound.setFloat("FallDistance", input);
/* 213 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 216 */   public int getFireTicks() { return this.compound.getShort("Fire"); }
/*     */   
/*     */   public void setFireTicks(int input) {
/* 219 */     this.compound.setShort("Fire", (short)input);
/* 220 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 223 */   public float getFlySpeed() { return this.compound.getCompound("abilities").getFloat("flySpeed"); }
/*     */   
/*     */   public void setFlySpeed(float speed) {
/* 226 */     this.compound.getCompound("abilities").setFloat("flySpeed", speed);
/* 227 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 230 */   public int getFoodLevel() { return this.compound.getInt("foodLevel"); }
/*     */   
/*     */   public void setFoodLevel(int input) {
/* 233 */     this.compound.setInt("foodLevel", input);
/* 234 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 237 */   public int getFoodTickTimer() { return this.compound.getInt("foodTickTimer"); }
/*     */   
/*     */   public void setFoodTickTimer(int input) {
/* 240 */     this.compound.setInt("foodTickTimer", input);
/* 241 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 244 */   public GameMode getGameMode() { return GameMode.values()[this.compound.getInt("playerGameType")]; }
/*     */   
/*     */   public void setGameMode(GameMode input) {
/* 247 */     this.compound.setInt("playerGameType", input.ordinal());
/* 248 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 251 */   public float getHealthFloat() { return this.compound.getFloat("HealF"); }
/*     */   
/*     */   public void setHealthFloat(float input) {
/* 254 */     this.compound.setFloat("HealF", input);
/* 255 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 258 */   public int getHealthInt() { return this.compound.getShort("Health"); }
/*     */   
/*     */   public void setHealthInt(int input) {
/* 261 */     this.compound.setShort("Health", (short)input);
/* 262 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 265 */   public org.bukkit.inventory.PlayerInventory getInventory() { net.minecraft.server.v1_7_R2.PlayerInventory inventory = new net.minecraft.server.v1_7_R2.PlayerInventory(null);
/* 266 */     inventory.b(this.compound.getList("Inventory", 10));
/* 267 */     return new CraftInventoryPlayer(inventory);
/*     */   }
/*     */   
/* 270 */   public void setInventory(org.bukkit.inventory.PlayerInventory inventory) { this.compound.set("Inventory", ((CraftInventoryPlayer)inventory).getInventory().a(new NBTTagList()));
/* 271 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 274 */   public boolean getIsInvulnerable() { return this.compound.getBoolean("Invulnerable"); }
/*     */   
/*     */   public void setIsInvulnerable(boolean input) {
/* 277 */     this.compound.setBoolean("Invulnerable", input);
/* 278 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 281 */   public boolean getIsOnGround() { return this.compound.getBoolean("OnGround"); }
/*     */   
/*     */   public void setIsOnGround(boolean input) {
/* 284 */     this.compound.setBoolean("OnGround", input);
/* 285 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 288 */   public boolean getIsSleeping() { return this.compound.getBoolean("Sleeping"); }
/*     */   
/*     */   public void setIsSleeping(boolean input) {
/* 291 */     this.compound.setBoolean("Sleeping", input);
/* 292 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 295 */   public int getItemInHand() { return this.compound.getInt("SelectedItemSlot"); }
/*     */   
/*     */   public void setItemInHand(int input) {
/* 298 */     this.compound.setInt("SelectedItemSlot", input);
/* 299 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 302 */   public int getLevel() { return this.compound.getInt("XpLevel"); }
/*     */   
/*     */   public void setLevel(int input) {
/* 305 */     this.compound.setInt("XpLevel", input);
/* 306 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 310 */     NBTTagList position = this.compound.getList("Pos", 6);
/* 311 */     NBTTagList rotation = this.compound.getList("Rotation", 5);
/*     */     
/* 313 */     return new Location(
/* 314 */       Bukkit.getWorld(new UUID(this.compound.getLong("WorldUUIDMost"), this.compound.getLong("WorldUUIDLeast"))), 
/* 315 */       position.d(0), position.d(1), position.d(2), rotation.e(0), rotation.e(1));
/*     */   }
/*     */   
/*     */   public void setLocation(Location location) {
/* 319 */     World w = location.getWorld();
/* 320 */     UUID uuid = w.getUID();
/* 321 */     this.compound.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
/* 322 */     this.compound.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
/* 323 */     this.compound.setInt("Dimension", w.getEnvironment().ordinal());
/* 324 */     NBTTagList position = new NBTTagList();
/* 325 */     position.add(new NBTTagDouble(location.getX()));
/* 326 */     position.add(new NBTTagDouble(location.getY()));
/* 327 */     position.add(new NBTTagDouble(location.getZ()));
/* 328 */     this.compound.set("Pos", position);
/* 329 */     NBTTagList rotation = new NBTTagList();
/* 330 */     rotation.add(new NBTTagFloat(location.getYaw()));
/* 331 */     rotation.add(new NBTTagFloat(location.getPitch()));
/* 332 */     this.compound.set("Rotation", rotation);
/* 333 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 336 */   public String getName() { return this.player; }
/*     */   
/*     */ 
/*     */ 
/* 340 */   public int getPortalCooldown() { return this.compound.getInt("PortalCooldown"); }
/*     */   
/*     */   public void setPortalCooldown(int input) {
/* 343 */     this.compound.setInt("PortalCooldown", input);
/* 344 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void setPotionEffects(ArrayList<PotionEffect> effects) {
/* 349 */     if (effects.isEmpty()) {
/* 350 */       this.compound.remove("ActiveEffects");
/* 351 */       if (this.autosave) savePlayerData();
/* 352 */       return;
/*     */     }
/* 354 */     NBTTagList activeEffects = new NBTTagList();
/* 355 */     for (PotionEffect pe : effects) {
/* 356 */       NBTTagCompound eCompound = new NBTTagCompound();
/* 357 */       eCompound.setByte("Amplifier", (byte)pe.getAmplifier());
/* 358 */       eCompound.setByte("Id", (byte)pe.getType().getId());
/* 359 */       eCompound.setInt("Duration", pe.getDuration());
/* 360 */       activeEffects.add(eCompound);
/*     */     }
/* 362 */     this.compound.set("ActiveEffects", activeEffects);
/* 363 */     if (this.autosave) { savePlayerData();
/*     */     }
/*     */   }
/*     */   
/* 367 */   public int getRemainingAir() { return this.compound.getShort("Air"); }
/*     */   
/*     */   public void setRemainingAir(int input) {
/* 370 */     this.compound.setShort("Air", (short)input);
/* 371 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 374 */   public float getSaturation() { return this.compound.getFloat("foodSaturationLevel"); }
/*     */   
/*     */   public void setSaturation(float input) {
/* 377 */     this.compound.setFloat("foodSaturationLevel", input);
/* 378 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 381 */   public float getScore() { return this.compound.getFloat("foodSaturationLevel"); }
/*     */   
/*     */   public void setScore(int input) {
/* 384 */     this.compound.setInt("Score", input);
/* 385 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 388 */   public short getTimeAttack() { return this.compound.getShort("AttackTime"); }
/*     */   
/*     */   public void setTimeAttack(short input) {
/* 391 */     this.compound.setShort("AttackTime", input);
/* 392 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 395 */   public short getTimeDeath() { return this.compound.getShort("DeathTime"); }
/*     */   
/*     */   public void setTimeDeath(short input) {
/* 398 */     this.compound.setShort("DeathTime", input);
/* 399 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 402 */   public short getTimeHurt() { return this.compound.getShort("HurtTime"); }
/*     */   
/*     */   public void setTimeHurt(short input) {
/* 405 */     this.compound.setShort("HurtTime", input);
/* 406 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 409 */   public short getTimeSleep() { return this.compound.getShort("SleepTimer"); }
/*     */   
/*     */   public void setTimeSleep(short input) {
/* 412 */     this.compound.setShort("SleepTimer", input);
/* 413 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 416 */   public int getTotalExperience() { return this.compound.getInt("XpTotal"); }
/*     */   
/*     */   public void setTotalExperience(int input) {
/* 419 */     this.compound.setInt("XpTotal", input);
/* 420 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/*     */   public Vector getVelocity() {
/* 424 */     NBTTagList list = this.compound.getList("Motion", 6);
/* 425 */     return new Vector(list.d(0), list.d(2), list.d(3));
/*     */   }
/*     */   
/*     */   public void setVelocity(Vector vector) {
/* 429 */     NBTTagList motion = new NBTTagList();
/* 430 */     motion.add(new NBTTagDouble(vector.getX()));
/* 431 */     motion.add(new NBTTagDouble(vector.getY()));
/* 432 */     motion.add(new NBTTagDouble(vector.getZ()));
/* 433 */     this.compound.set("Motion", motion);
/* 434 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */   
/* 437 */   public float getWalkSpeed() { return this.compound.getCompound("abilities").getFloat("walkSpeed"); }
/*     */   
/*     */   public void setWalkSpeed(float speed) {
/* 440 */     this.compound.getCompound("abilities").setFloat("walkSpeed", speed);
/* 441 */     if (this.autosave) savePlayerData();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\gpl\ImprovedOfflinePlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */