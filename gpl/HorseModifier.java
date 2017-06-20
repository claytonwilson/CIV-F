/*     */ package gpl;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.v1_7_R2.AttributeInstance;
/*     */ import net.minecraft.server.v1_7_R2.AttributeModifier;
/*     */ import net.minecraft.server.v1_7_R2.EntityInsentient;
/*     */ import net.minecraft.server.v1_7_R2.GenericAttributes;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.entity.CraftLivingEntity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
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
/*     */ public class HorseModifier
/*     */ {
/*     */   private Object entityHorse;
/*     */   private Object nbtTagCompound;
/*  34 */   private static final UUID movementSpeedUID = UUID.fromString("206a89dc-ae78-4c4d-b42c-3b31db3f5a7c");
/*     */   
/*     */ 
/*     */ 
/*     */   public HorseModifier(LivingEntity horse)
/*     */   {
/*  40 */     if (!isHorse(horse)) {
/*  41 */       throw new IllegalArgumentException("Entity has to be a horse!");
/*     */     }
/*     */     try {
/*  44 */       this.entityHorse = ReflectionUtil.getMethod("getHandle", horse.getClass(), 0).invoke(horse, new Object[0]);
/*  45 */       this.nbtTagCompound = NBTUtil.getNBTTagCompound(this.entityHorse);
/*     */     } catch (Exception e) {
/*  47 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private HorseModifier(Object entityHorse)
/*     */   {
/*  55 */     this.entityHorse = entityHorse;
/*     */     try {
/*  57 */       this.nbtTagCompound = NBTUtil.getNBTTagCompound(entityHorse);
/*     */     } catch (Exception e) {
/*  59 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static HorseModifier spawn(Location loc)
/*     */   {
/*  67 */     World w = loc.getWorld();
/*     */     try {
/*  69 */       Object worldServer = ReflectionUtil.getMethod("getHandle", w.getClass(), 0).invoke(w, new Object[0]);
/*  70 */       Object entityHorse = ReflectionUtil.getClass("EntityHorse", new Object[] { worldServer });
/*  71 */       ReflectionUtil.getMethod("setPosition", entityHorse.getClass(), 3).invoke(entityHorse, new Object[] { Double.valueOf(loc.getX()), Double.valueOf(loc.getY()), Double.valueOf(loc.getZ()) });
/*  72 */       ReflectionUtil.getMethod("addEntity", worldServer.getClass(), 1).invoke(worldServer, new Object[] { entityHorse });
/*  73 */       return new HorseModifier(entityHorse);
/*     */     } catch (Exception e) {
/*  75 */       e.printStackTrace(); }
/*  76 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isHorse(LivingEntity le)
/*     */   {
/*     */     try
/*     */     {
/*  85 */       Object entityLiving = ReflectionUtil.getMethod("getHandle", le.getClass(), 0).invoke(le, new Object[0]);
/*  86 */       Object nbtTagCompound = NBTUtil.getNBTTagCompound(entityLiving);
/*  87 */       return NBTUtil.hasKeys(nbtTagCompound, new String[] { "EatingHaystack", "ChestedHorse", "HasReproduced", "Bred", "Type", "Variant", "Temper", "Tame" });
/*     */     } catch (Exception e) {
/*  89 */       e.printStackTrace(); }
/*  90 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void setHorseSpeed(LivingEntity entity, double amount)
/*     */   {
/*  96 */     if (!isHorse(entity)) {
/*  97 */       return;
/*     */     }
/*     */     
/* 100 */     EntityInsentient nmsEntity = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
/* 101 */     AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.d);
/* 102 */     AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "civcraft horse movement speed", amount, 0);
/* 103 */     attributes.b(modifier);
/* 104 */     attributes.a(modifier);
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isCivCraftHorse(LivingEntity entity)
/*     */   {
/* 110 */     if (!isHorse(entity)) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     EntityInsentient nmsEntity = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
/* 115 */     AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.d);
/*     */     
/* 117 */     if (attributes.a(movementSpeedUID) == null) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setType(HorseType type)
/*     */   {
/* 132 */     setHorseValue("Type", Integer.valueOf(type.getId()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setChested(boolean chested)
/*     */   {
/* 139 */     setHorseValue("ChestedHorse", Boolean.valueOf(chested));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setEating(boolean eating)
/*     */   {
/* 146 */     setHorseValue("EatingHaystack", Boolean.valueOf(eating));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBred(boolean bred)
/*     */   {
/* 153 */     setHorseValue("Bred", Boolean.valueOf(bred));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setVariant(HorseVariant variant)
/*     */   {
/* 160 */     setHorseValue("Variant", Integer.valueOf(variant.getId()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTemper(int temper)
/*     */   {
/* 167 */     setHorseValue("Temper", Integer.valueOf(temper));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTamed(boolean tamed)
/*     */   {
/* 174 */     setHorseValue("Tame", Boolean.valueOf(tamed));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSaddled(boolean saddled)
/*     */   {
/* 181 */     setHorseValue("Saddle", Boolean.valueOf(saddled));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setArmorItem(ItemStack i)
/*     */   {
/* 188 */     if (i != null) {
/*     */       try {
/* 190 */         Object itemTag = ReflectionUtil.getClass("NBTTagCompound", new Object[] { "ArmorItem" });
/* 191 */         Object itemStack = ReflectionUtil.getMethod("asNMSCopy", Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack"), 1).invoke(this, new Object[] { i });
/* 192 */         ReflectionUtil.getMethod("save", itemStack.getClass(), 1).invoke(itemStack, new Object[] { itemTag });
/* 193 */         setHorseValue("ArmorItem", itemTag);
/*     */       } catch (Exception e) {
/* 195 */         e.printStackTrace();
/*     */       }
/*     */     } else {
/* 198 */       setHorseValue("ArmorItem", null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HorseType getType()
/*     */   {
/* 206 */     return HorseType.fromId(((Integer)NBTUtil.getValue(this.nbtTagCompound, Integer.class, "Type")).intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isChested()
/*     */   {
/* 213 */     return ((Boolean)NBTUtil.getValue(this.nbtTagCompound, Boolean.class, "ChestedHorse")).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEating()
/*     */   {
/* 220 */     return ((Boolean)NBTUtil.getValue(this.nbtTagCompound, Boolean.class, "EatingHaystack")).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isBred()
/*     */   {
/* 227 */     return ((Boolean)NBTUtil.getValue(this.nbtTagCompound, Boolean.class, "Bred")).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HorseVariant getVariant()
/*     */   {
/* 234 */     return HorseVariant.fromId(((Integer)NBTUtil.getValue(this.nbtTagCompound, Integer.class, "Variant")).intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getTemper()
/*     */   {
/* 241 */     return ((Integer)NBTUtil.getValue(this.nbtTagCompound, Integer.class, "Temper")).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isTamed()
/*     */   {
/* 248 */     return ((Boolean)NBTUtil.getValue(this.nbtTagCompound, Boolean.class, "Tame")).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSaddled()
/*     */   {
/* 255 */     return ((Boolean)NBTUtil.getValue(this.nbtTagCompound, Boolean.class, "Saddle")).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack getArmorItem()
/*     */   {
/*     */     try
/*     */     {
/* 263 */       Object itemTag = NBTUtil.getValue(this.nbtTagCompound, this.nbtTagCompound.getClass(), "ArmorItem");
/* 264 */       Object itemStack = ReflectionUtil.getMethod("createStack", Class.forName(ReflectionUtil.getPackageName() + ".ItemStack"), 1).invoke(this, new Object[] { itemTag });
/* 265 */       return (ItemStack)ReflectionUtil.getMethod("asCraftMirror", Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack"), 1).invoke(this, new Object[] { itemStack });
/*     */     } catch (Exception e) {
/* 267 */       e.printStackTrace(); }
/* 268 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void openInventory(Player p)
/*     */   {
/*     */     try
/*     */     {
/* 277 */       Object entityPlayer = ReflectionUtil.getMethod("getHandle", p.getClass(), 0).invoke(p, new Object[0]);
/* 278 */       ReflectionUtil.getMethod("f", this.entityHorse.getClass(), 1).invoke(this.entityHorse, new Object[] { entityPlayer });
/*     */     } catch (Exception e) {
/* 280 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public LivingEntity getHorse()
/*     */   {
/*     */     try
/*     */     {
/* 289 */       return (LivingEntity)ReflectionUtil.getMethod("getBukkitEntity", this.entityHorse.getClass(), 0).invoke(this.entityHorse, new Object[0]);
/*     */     } catch (Exception e) {
/* 291 */       e.printStackTrace(); }
/* 292 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setHorseValue(String key, Object value)
/*     */   {
/* 300 */     NBTUtil.setValue(this.nbtTagCompound, key, value);
/* 301 */     NBTUtil.updateNBTTagCompound(this.entityHorse, this.nbtTagCompound);
/*     */   }
/*     */   
/*     */   public static enum HorseType
/*     */   {
/* 306 */     NORMAL("normal", 0),  DONKEY("donkey", 1),  MULE("mule", 2),  UNDEAD("undead", 3),  SKELETAL("skeletal", 4);
/*     */     
/*     */     private String name;
/*     */     private int id;
/*     */     
/*     */     private HorseType(String name, int id) {
/* 312 */       this.name = name;
/* 313 */       this.id = id;
/*     */     }
/*     */     
/*     */ 
/* 317 */     public String getName() { return this.name; }
/*     */     
/*     */     private static final Map<String, HorseType> NAME_MAP;
/*     */     private static final Map<Integer, HorseType> ID_MAP;
/* 321 */     public int getId() { return this.id; }
/*     */     
/*     */     static {
/* 324 */       NAME_MAP = new HashMap();
/* 325 */       ID_MAP = new HashMap();
/*     */       HorseType[] arrayOfHorseType;
/* 327 */       int j = (arrayOfHorseType = values()).length; for (int i = 0; i < j; i++) { HorseType effect = arrayOfHorseType[i];
/* 328 */         NAME_MAP.put(effect.name, effect);
/* 329 */         ID_MAP.put(Integer.valueOf(effect.id), effect);
/*     */       }
/*     */     }
/*     */     
/*     */     public static HorseType fromName(String name) {
/* 334 */       if (name == null) {
/* 335 */         return null;
/*     */       }
/* 337 */       for (Map.Entry<String, HorseType> e : NAME_MAP.entrySet()) {
/* 338 */         if (((String)e.getKey()).equalsIgnoreCase(name)) {
/* 339 */           return (HorseType)e.getValue();
/*     */         }
/*     */       }
/* 342 */       return null;
/*     */     }
/*     */     
/*     */     public static HorseType fromId(int id) {
/* 346 */       return (HorseType)ID_MAP.get(Integer.valueOf(id));
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum HorseVariant {
/* 351 */     WHITE("white", 0),  CREAMY("creamy", 1),  CHESTNUT("chestnut", 2),  BROWN("brown", 3),  BLACK("black", 4),  GRAY("gray", 5),  DARK_BROWN("dark brown", 6),  INVISIBLE("invisible", 7),  WHITE_WHITE(
/* 352 */       "white-white", 256),  CREAMY_WHITE("creamy-white", 257),  CHESTNUT_WHITE("chestnut-white", 258),  BROWN_WHITE("brown-white", 259),  BLACK_WHITE("black-white", 260),  GRAY_WHITE("gray-white", 261),  DARK_BROWN_WHITE(
/* 353 */       "dark brown-white", 262),  WHITE_WHITE_FIELD("white-white field", 512),  CREAMY_WHITE_FIELD("creamy-white field", 513),  CHESTNUT_WHITE_FIELD("chestnut-white field", 514),  BROWN_WHITE_FIELD(
/* 354 */       "brown-white field", 515),  BLACK_WHITE_FIELD("black-white field", 516),  GRAY_WHITE_FIELD("gray-white field", 517),  DARK_BROWN_WHITE_FIELD("dark brown-white field", 518),  WHITE_WHITE_DOTS(
/* 355 */       "white-white dots", 768),  CREAMY_WHITE_DOTS("creamy-white dots", 769),  CHESTNUT_WHITE_DOTS("chestnut-white dots", 770),  BROWN_WHITE_DOTS("brown-white dots", 771),  BLACK_WHITE_DOTS(
/* 356 */       "black-white dots", 772),  GRAY_WHITE_DOTS("gray-white dots", 773),  DARK_BROWN_WHITE_DOTS("dark brown-white dots", 774),  WHITE_BLACK_DOTS("white-black dots", 1024),  CREAMY_BLACK_DOTS(
/* 357 */       "creamy-black dots", 1025),  CHESTNUT_BLACK_DOTS("chestnut-black dots", 1026),  BROWN_BLACK_DOTS("brown-black dots", 1027),  BLACK_BLACK_DOTS("black-black dots", 1028),  GRAY_BLACK_DOTS(
/* 358 */       "gray-black dots", 1029),  DARK_BROWN_BLACK_DOTS("dark brown-black dots", 1030);
/*     */     
/*     */     private String name;
/*     */     private int id;
/*     */     
/*     */     private HorseVariant(String name, int id) {
/* 364 */       this.name = name;
/* 365 */       this.id = id;
/*     */     }
/*     */     
/*     */ 
/* 369 */     public String getName() { return this.name; }
/*     */     
/*     */     private static final Map<String, HorseVariant> NAME_MAP;
/*     */     private static final Map<Integer, HorseVariant> ID_MAP;
/* 373 */     public int getId() { return this.id; }
/*     */     
/*     */     static {
/* 376 */       NAME_MAP = new HashMap();
/* 377 */       ID_MAP = new HashMap();
/*     */       HorseVariant[] arrayOfHorseVariant;
/* 379 */       int j = (arrayOfHorseVariant = values()).length; for (int i = 0; i < j; i++) { HorseVariant effect = arrayOfHorseVariant[i];
/* 380 */         NAME_MAP.put(effect.name, effect);
/* 381 */         ID_MAP.put(Integer.valueOf(effect.id), effect);
/*     */       }
/*     */     }
/*     */     
/*     */     public static HorseVariant fromName(String name) {
/* 386 */       if (name == null) {
/* 387 */         return null;
/*     */       }
/* 389 */       for (Map.Entry<String, HorseVariant> e : NAME_MAP.entrySet()) {
/* 390 */         if (((String)e.getKey()).equalsIgnoreCase(name)) {
/* 391 */           return (HorseVariant)e.getValue();
/*     */         }
/*     */       }
/* 394 */       return null;
/*     */     }
/*     */     
/*     */     public static HorseVariant fromId(int id) {
/* 398 */       return (HorseVariant)ID_MAP.get(Integer.valueOf(id));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NBTUtil {
/*     */     public static Object getNBTTagCompound(Object entity) {
/*     */       try {
/* 405 */         Object nbtTagCompound = HorseModifier.ReflectionUtil.getClass("NBTTagCompound", new Object[0]);
/* 406 */         Method[] arrayOfMethod; int j = (arrayOfMethod = entity.getClass().getMethods()).length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod[i];
/* 407 */           Class[] pt = m.getParameterTypes();
/* 408 */           if ((m.getName().equals("b")) && (pt.length == 1) && (pt[0].getName().contains("NBTTagCompound"))) {
/* 409 */             m.invoke(entity, new Object[] { nbtTagCompound });
/*     */           }
/*     */         }
/* 412 */         return nbtTagCompound;
/*     */       } catch (Exception e) {
/* 414 */         e.printStackTrace(); }
/* 415 */       return null;
/*     */     }
/*     */     
/*     */     public static void updateNBTTagCompound(Object entity, Object nbtTagCompound) {
/*     */       try {
/*     */         Method[] arrayOfMethod;
/* 421 */         int j = (arrayOfMethod = entity.getClass().getMethods()).length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod[i];
/* 422 */           Class[] pt = m.getParameterTypes();
/* 423 */           if ((m.getName().equals("a")) && (pt.length == 1) && (pt[0].getName().contains("NBTTagCompound"))) {
/* 424 */             m.invoke(entity, new Object[] { nbtTagCompound });
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/* 428 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public static void setValue(Object nbtTagCompound, String key, Object value) {
/*     */       try {
/* 434 */         if ((value instanceof Integer)) {
/* 435 */           HorseModifier.ReflectionUtil.getMethod("setInt", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, new Object[] { key, (Integer)value });
/* 436 */           return; }
/* 437 */         if ((value instanceof Boolean)) {
/* 438 */           HorseModifier.ReflectionUtil.getMethod("setBoolean", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, new Object[] { key, (Boolean)value });
/* 439 */           return;
/*     */         }
/* 441 */         HorseModifier.ReflectionUtil.getMethod("set", nbtTagCompound.getClass(), 2).invoke(nbtTagCompound, new Object[] { key, value });
/*     */       }
/*     */       catch (Exception e) {
/* 444 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public static Object getValue(Object nbtTagCompound, Class<?> c, String key) {
/*     */       try {
/* 450 */         if (c == Integer.class)
/* 451 */           return HorseModifier.ReflectionUtil.getMethod("getInt", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, new Object[] { key });
/* 452 */         if (c == Boolean.class) {
/* 453 */           return HorseModifier.ReflectionUtil.getMethod("getBoolean", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, new Object[] { key });
/*     */         }
/* 455 */         return HorseModifier.ReflectionUtil.getMethod("getCompound", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, new Object[] { key });
/*     */       }
/*     */       catch (Exception e) {
/* 458 */         e.printStackTrace(); }
/* 459 */       return null;
/*     */     }
/*     */     
/*     */     public static boolean hasKey(Object nbtTagCompound, String key)
/*     */     {
/*     */       try {
/* 465 */         return ((Boolean)HorseModifier.ReflectionUtil.getMethod("hasKey", nbtTagCompound.getClass(), 1).invoke(nbtTagCompound, new Object[] { key })).booleanValue();
/*     */       } catch (Exception e) {
/* 467 */         e.printStackTrace(); }
/* 468 */       return false;
/*     */     }
/*     */     
/*     */     public static boolean hasKeys(Object nbtTagCompound, String[] keys) {
/*     */       String[] arrayOfString;
/* 473 */       int j = (arrayOfString = keys).length; for (int i = 0; i < j; i++) { String key = arrayOfString[i];
/* 474 */         if (!hasKey(nbtTagCompound, key)) {
/* 475 */           return false;
/*     */         }
/*     */       }
/* 478 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ReflectionUtil {
/*     */     public static Object getClass(String name, Object... args) throws Exception {
/* 484 */       Class<?> c = Class.forName(getPackageName() + "." + name);
/* 485 */       int params = 0;
/* 486 */       if (args != null)
/* 487 */         params = args.length;
/*     */       Constructor[] arrayOfConstructor;
/* 489 */       int j = (arrayOfConstructor = c.getConstructors()).length; for (int i = 0; i < j; i++) { Constructor<?> co = arrayOfConstructor[i];
/* 490 */         if (co.getParameterTypes().length == params) {
/* 491 */           return co.newInstance(args);
/*     */         }
/*     */       }
/* 494 */       return null;
/*     */     }
/*     */     
/*     */     public static Method getMethod(String name, Class<?> c, int params) { Method[] arrayOfMethod;
/* 498 */       int j = (arrayOfMethod = c.getMethods()).length; for (int i = 0; i < j; i++) { Method m = arrayOfMethod[i];
/* 499 */         if ((m.getName().equals(name)) && (m.getParameterTypes().length == params)) {
/* 500 */           return m;
/*     */         }
/*     */       }
/* 503 */       return null;
/*     */     }
/*     */     
/*     */     public static String getPackageName() {
/* 507 */       return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\gpl\HorseModifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */