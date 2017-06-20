/*     */ package gpl;
/*     */ 
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.util.ItemManager;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.v1_7_R2.NBTBase;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagCompound;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagInt;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagList;
/*     */ import net.minecraft.server.v1_7_R2.NBTTagString;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
/*     */ 
/*     */ public class AttributeUtil
/*     */ {
/*     */   public net.minecraft.server.v1_7_R2.ItemStack nmsStack;
/*     */   private NBTTagCompound parent;
/*     */   private NBTTagList attributes;
/*     */   
/*     */   public static enum Operation
/*     */   {
/*  35 */     ADD_NUMBER(0), 
/*  36 */     MULTIPLY_PERCENTAGE(1), 
/*  37 */     ADD_PERCENTAGE(2);
/*     */     
/*     */     private int id;
/*     */     
/*  41 */     private Operation(int id) { this.id = id; }
/*     */     
/*     */     public int getId()
/*     */     {
/*  45 */       return this.id;
/*     */     }
/*     */     
/*     */     public static Operation fromId(int id) {
/*     */       Operation[] arrayOfOperation;
/*  50 */       int j = (arrayOfOperation = values()).length; for (int i = 0; i < j; i++) { Operation op = arrayOfOperation[i];
/*  51 */         if (op.getId() == id) {
/*  52 */           return op;
/*     */         }
/*     */       }
/*  55 */       throw new IllegalArgumentException("Corrupt operation ID " + id + " detected.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class AttributeType
/*     */   {
/*  63 */     private static ConcurrentMap<String, AttributeType> LOOKUP = ;
/*  64 */     public static final AttributeType GENERIC_MAX_HEALTH = new AttributeType("generic.maxHealth").register();
/*  65 */     public static final AttributeType GENERIC_FOLLOW_RANGE = new AttributeType("generic.followRange").register();
/*  66 */     public static final AttributeType GENERIC_ATTACK_DAMAGE = new AttributeType("generic.attackDamage").register();
/*  67 */     public static final AttributeType GENERIC_MOVEMENT_SPEED = new AttributeType("generic.movementSpeed").register();
/*  68 */     public static final AttributeType GENERIC_KNOCKBACK_RESISTANCE = new AttributeType("generic.knockbackResistance").register();
/*     */     
/*     */ 
/*     */ 
/*     */     private final String minecraftId;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public AttributeType(String minecraftId)
/*     */     {
/*  79 */       this.minecraftId = minecraftId;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getMinecraftId()
/*     */     {
/*  87 */       return this.minecraftId;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public AttributeType register()
/*     */     {
/*  96 */       AttributeType old = (AttributeType)LOOKUP.putIfAbsent(this.minecraftId, this);
/*  97 */       return old != null ? old : this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static AttributeType fromId(String minecraftId)
/*     */     {
/* 106 */       return (AttributeType)LOOKUP.get(minecraftId);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Iterable<AttributeType> values()
/*     */     {
/* 114 */       return LOOKUP.values();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Attribute {
/*     */     private NBTTagCompound data;
/*     */     
/*     */     private Attribute(Builder builder) {
/* 122 */       this.data = new NBTTagCompound();
/* 123 */       setAmount(builder.amount);
/* 124 */       setOperation(builder.operation);
/* 125 */       setAttributeType(builder.type);
/* 126 */       setName(builder.name);
/* 127 */       setUUID(builder.uuid);
/*     */     }
/*     */     
/*     */     private Attribute(NBTTagCompound data) {
/* 131 */       this.data = data;
/*     */     }
/*     */     
/*     */     public double getAmount() {
/* 135 */       return this.data.getDouble("Amount");
/*     */     }
/*     */     
/*     */     public void setAmount(double amount) {
/* 139 */       this.data.setDouble("Amount", amount);
/*     */     }
/*     */     
/*     */     public AttributeUtil.Operation getOperation() {
/* 143 */       return AttributeUtil.Operation.fromId(this.data.getInt("Operation"));
/*     */     }
/*     */     
/*     */     public void setOperation(@Nonnull AttributeUtil.Operation operation) {
/* 147 */       Preconditions.checkNotNull(operation, "operation cannot be NULL.");
/* 148 */       this.data.setInt("Operation", operation.getId());
/*     */     }
/*     */     
/*     */     public AttributeUtil.AttributeType getAttributeType() {
/* 152 */       return AttributeUtil.AttributeType.fromId(this.data.getString("AttributeName").replace("\"", ""));
/*     */     }
/*     */     
/*     */     public void setAttributeType(@Nonnull AttributeUtil.AttributeType type) {
/* 156 */       Preconditions.checkNotNull(type, "type cannot be NULL.");
/* 157 */       this.data.setString("AttributeName", type.getMinecraftId());
/*     */     }
/*     */     
/*     */     public String getName() {
/* 161 */       return this.data.getString("Name").replace("\"", "");
/*     */     }
/*     */     
/*     */     public void setName(@Nonnull String name) {
/* 165 */       this.data.setString("Name", name);
/*     */     }
/*     */     
/*     */     public UUID getUUID() {
/* 169 */       return new UUID(this.data.getLong("UUIDMost"), this.data.getLong("UUIDLeast"));
/*     */     }
/*     */     
/*     */     public void setUUID(@Nonnull UUID id) {
/* 173 */       Preconditions.checkNotNull("id", "id cannot be NULL.");
/* 174 */       this.data.setLong("UUIDLeast", id.getLeastSignificantBits());
/* 175 */       this.data.setLong("UUIDMost", id.getMostSignificantBits());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static Builder newBuilder()
/*     */     {
/* 183 */       return new Builder(null).uuid(UUID.randomUUID()).operation(AttributeUtil.Operation.ADD_NUMBER);
/*     */     }
/*     */     
/*     */     public static class Builder
/*     */     {
/*     */       private double amount;
/* 189 */       private AttributeUtil.Operation operation = AttributeUtil.Operation.ADD_NUMBER;
/*     */       
/*     */       private AttributeUtil.AttributeType type;
/*     */       
/*     */       private String name;
/*     */       
/*     */       private UUID uuid;
/*     */       
/*     */       public Builder amount(double amount)
/*     */       {
/* 199 */         this.amount = amount;
/* 200 */         return this;
/*     */       }
/*     */       
/* 203 */       public Builder operation(AttributeUtil.Operation operation) { this.operation = operation;
/* 204 */         return this;
/*     */       }
/*     */       
/* 207 */       public Builder type(AttributeUtil.AttributeType type) { this.type = type;
/* 208 */         return this;
/*     */       }
/*     */       
/* 211 */       public Builder name(String name) { this.name = name;
/* 212 */         return this;
/*     */       }
/*     */       
/* 215 */       public Builder uuid(UUID uuid) { this.uuid = uuid;
/* 216 */         return this;
/*     */       }
/*     */       
/* 219 */       public AttributeUtil.Attribute build() { return new AttributeUtil.Attribute(this, null); }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AttributeUtil(org.bukkit.inventory.ItemStack stack)
/*     */   {
/* 232 */     this.nmsStack = CraftItemStack.asNMSCopy(stack);
/*     */     
/* 234 */     if (this.nmsStack == null) {
/* 235 */       return;
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
/* 247 */     if (this.nmsStack.tag == null) {
/* 248 */       this.parent = (this.nmsStack.tag = new NBTTagCompound());
/*     */     } else {
/* 250 */       this.parent = this.nmsStack.tag;
/*     */     }
/*     */     
/*     */ 
/* 254 */     if (this.parent.hasKey("AttributeModifiers")) {
/* 255 */       this.attributes = this.parent.getList("AttributeModifiers", 10);
/*     */     }
/*     */     else {
/* 258 */       this.attributes = new NBTTagList();
/* 259 */       this.parent.set("AttributeModifiers", this.attributes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public org.bukkit.inventory.ItemStack getStack()
/*     */   {
/* 268 */     if (this.nmsStack == null) {
/* 269 */       return ItemManager.createItemStack(35, 0);
/*     */     }
/*     */     
/* 272 */     if ((this.nmsStack.tag != null) && 
/* 273 */       (this.attributes.size() == 0)) {
/* 274 */       this.parent.remove("AttributeModifiers");
/*     */     }
/*     */     
/*     */ 
/* 278 */     return CraftItemStack.asCraftMirror(this.nmsStack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 286 */     return this.attributes.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(Attribute attribute)
/*     */   {
/* 294 */     this.attributes.add(attribute.data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean remove(Attribute attribute)
/*     */   {
/* 305 */     UUID uuid = attribute.getUUID();
/*     */     
/* 307 */     for (Iterator<Attribute> it = values().iterator(); it.hasNext();) {
/* 308 */       if (Objects.equal(((Attribute)it.next()).getUUID(), uuid)) {
/* 309 */         it.remove();
/* 310 */         return true;
/*     */       }
/*     */     }
/* 313 */     return false;
/*     */   }
/*     */   
/*     */   public void removeAll() {
/* 317 */     this.attributes = new NBTTagList();
/* 318 */     this.parent.set("AttributeModifiers", this.attributes);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 323 */     this.parent.set("AttributeModifiers", this.attributes = new NBTTagList());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Attribute get(int index)
/*     */   {
/* 332 */     return new Attribute(this.attributes.get(index), null);
/*     */   }
/*     */   
/*     */   public Iterable<Attribute> values()
/*     */   {
/* 337 */     final List<NBTBase> list = getList();
/*     */     
/* 339 */     new Iterable()
/*     */     {
/*     */       public Iterator<AttributeUtil.Attribute> iterator()
/*     */       {
/* 343 */         Iterators.transform(
/* 344 */           list.iterator(), new Function()
/*     */           {
/*     */             public AttributeUtil.Attribute apply(@Nullable NBTBase data)
/*     */             {
/* 348 */               return new AttributeUtil.Attribute((NBTTagCompound)data, null);
/*     */             }
/*     */           });
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private <T> List<T> getList()
/*     */   {
/*     */     try {
/* 358 */       Field listField = NBTTagList.class.getDeclaredField("list");
/* 359 */       listField.setAccessible(true);
/* 360 */       return (List)listField.get(this.attributes);
/*     */     }
/*     */     catch (Exception e) {
/* 363 */       throw new RuntimeException("Unable to access reflection.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addLore(String str) {
/* 368 */     if (this.nmsStack == null) {
/* 369 */       return;
/*     */     }
/*     */     
/* 372 */     if (this.nmsStack.tag == null) {
/* 373 */       this.nmsStack.tag = new NBTTagCompound();
/*     */     }
/*     */     
/* 376 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/*     */     
/* 378 */     if (displayCompound == null) {
/* 379 */       displayCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 382 */     NBTTagList loreList = displayCompound.getList("Lore", 8);
/* 383 */     if (loreList == null) {
/* 384 */       loreList = new NBTTagList();
/*     */     }
/*     */     
/* 387 */     loreList.add(new NBTTagString(str));
/* 388 */     displayCompound.set("Lore", loreList);
/* 389 */     this.nmsStack.tag.set("display", displayCompound);
/*     */   }
/*     */   
/*     */   public String[] getLore() {
/* 393 */     if (this.nmsStack == null) {
/* 394 */       return null;
/*     */     }
/*     */     
/* 397 */     if (this.nmsStack.tag == null) {
/* 398 */       return null;
/*     */     }
/*     */     
/* 401 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/*     */     
/* 403 */     if (displayCompound == null) {
/* 404 */       return null;
/*     */     }
/*     */     
/* 407 */     NBTTagList loreList = displayCompound.getList("Lore", 8);
/* 408 */     if (loreList == null) {
/* 409 */       return null;
/*     */     }
/*     */     
/* 412 */     if (loreList.size() < 1) {
/* 413 */       return null;
/*     */     }
/*     */     
/* 416 */     String[] lore = new String[loreList.size()];
/* 417 */     for (int i = 0; i < loreList.size(); i++) {
/* 418 */       lore[i] = loreList.f(i).replace("\"", "");
/*     */     }
/*     */     
/* 421 */     return lore;
/*     */   }
/*     */   
/*     */   public void setLore(String string) {
/* 425 */     String[] strings = new String[1];
/* 426 */     strings[0] = string;
/* 427 */     setLore(strings);
/*     */   }
/*     */   
/*     */   public void setLore(String[] strings)
/*     */   {
/* 432 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/*     */     
/* 434 */     if (displayCompound == null) {
/* 435 */       displayCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 438 */     NBTTagList loreList = new NBTTagList();
/*     */     String[] arrayOfString;
/* 440 */     int j = (arrayOfString = strings).length; for (int i = 0; i < j; i++) { String str = arrayOfString[i];
/* 441 */       loreList.add(new NBTTagString(str));
/*     */     }
/*     */     
/* 444 */     displayCompound.set("Lore", loreList);
/* 445 */     this.nmsStack.tag.set("display", displayCompound);
/*     */   }
/*     */   
/*     */   public void addEnhancement(String enhancementName, String key, String value) {
/* 449 */     if (enhancementName.equalsIgnoreCase("name")) {
/* 450 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 453 */     NBTTagCompound compound = this.nmsStack.tag.getCompound("item_enhancements");
/*     */     
/* 455 */     if (compound == null) {
/* 456 */       compound = new NBTTagCompound();
/*     */     }
/*     */     
/* 459 */     NBTTagCompound enhCompound = compound.getCompound(enhancementName);
/* 460 */     if (enhCompound == null) {
/* 461 */       enhCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 464 */     if (key != null) {
/* 465 */       _setEnhancementData(enhCompound, key, value);
/*     */     }
/* 467 */     enhCompound.set("name", new NBTTagString(enhancementName));
/*     */     
/* 469 */     compound.set(enhancementName, enhCompound);
/* 470 */     this.nmsStack.tag.set("item_enhancements", compound);
/*     */   }
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
/*     */   private void _setEnhancementData(NBTTagCompound enhCompound, String key, String value)
/*     */   {
/* 492 */     if (key.equalsIgnoreCase("name")) {
/* 493 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 496 */     enhCompound.set(key, new NBTTagString(value));
/*     */   }
/*     */   
/*     */   public void setEnhancementData(String enhancementName, String key, String value)
/*     */   {
/* 501 */     addEnhancement(enhancementName, key, value);
/*     */   }
/*     */   
/*     */   public String getEnhancementData(String enhName, String key)
/*     */   {
/* 506 */     if (!hasEnhancement(enhName)) {
/* 507 */       return null;
/*     */     }
/*     */     
/* 510 */     NBTTagCompound compound = this.nmsStack.tag.getCompound("item_enhancements");
/* 511 */     NBTTagCompound enhCompound = compound.getCompound(enhName);
/*     */     
/* 513 */     if (!enhCompound.hasKey(key)) {
/* 514 */       return null;
/*     */     }
/*     */     
/* 517 */     return enhCompound.getString(key);
/*     */   }
/*     */   
/*     */   public LinkedList<LoreEnhancement> getEnhancements() {
/* 521 */     LinkedList<LoreEnhancement> returnList = new LinkedList();
/*     */     
/* 523 */     if (!hasEnhancements()) {
/* 524 */       return returnList;
/*     */     }
/*     */     
/* 527 */     NBTTagCompound compound = this.nmsStack.tag.getCompound("item_enhancements");
/*     */     
/* 529 */     for (Object keyObj : compound.c()) {
/* 530 */       if ((keyObj instanceof String))
/*     */       {
/*     */ 
/*     */ 
/* 534 */         String key = (String)keyObj;
/* 535 */         Object obj = compound.get(key);
/*     */         
/* 537 */         if ((obj instanceof NBTTagCompound)) {
/* 538 */           NBTTagCompound enhCompound = (NBTTagCompound)obj;
/* 539 */           String name = enhCompound.getString("name").replace("\"", "");
/*     */           
/* 541 */           if (name != null) {
/* 542 */             LoreEnhancement enh = (LoreEnhancement)LoreEnhancement.enhancements.get(name);
/* 543 */             if (enh != null) {
/* 544 */               returnList.add(enh);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 550 */     return returnList;
/*     */   }
/*     */   
/*     */   public boolean hasEnhancement(String enhName) {
/* 554 */     NBTTagCompound compound = this.nmsStack.tag.getCompound("item_enhancements");
/* 555 */     if (compound == null) {
/* 556 */       return false;
/*     */     }
/*     */     
/* 559 */     return compound.hasKey(enhName);
/*     */   }
/*     */   
/*     */   public boolean hasEnhancements() {
/* 563 */     if (this.nmsStack == null) {
/* 564 */       return false;
/*     */     }
/*     */     
/* 567 */     if (this.nmsStack.tag == null) {
/* 568 */       return false;
/*     */     }
/*     */     
/* 571 */     return this.nmsStack.tag.hasKey("item_enhancements");
/*     */   }
/*     */   
/*     */   public void setCivCraftProperty(String key, String value)
/*     */   {
/* 576 */     if (this.nmsStack == null) {
/* 577 */       return;
/*     */     }
/*     */     
/* 580 */     if (this.nmsStack.tag == null) {
/* 581 */       this.nmsStack.tag = new NBTTagCompound();
/*     */     }
/*     */     
/* 584 */     NBTTagCompound civcraftCompound = this.nmsStack.tag.getCompound("civcraft");
/*     */     
/* 586 */     if (civcraftCompound == null) {
/* 587 */       civcraftCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 590 */     civcraftCompound.set(key, new NBTTagString(value));
/* 591 */     this.nmsStack.tag.set("civcraft", civcraftCompound);
/*     */   }
/*     */   
/*     */   public String getCivCraftProperty(String key) {
/* 595 */     if (this.nmsStack == null) {
/* 596 */       return null;
/*     */     }
/* 598 */     NBTTagCompound civcraftCompound = this.nmsStack.tag.getCompound("civcraft");
/*     */     
/* 600 */     if (civcraftCompound == null) {
/* 601 */       return null;
/*     */     }
/*     */     
/* 604 */     NBTTagString strTag = (NBTTagString)civcraftCompound.get(key);
/* 605 */     if (strTag == null) {
/* 606 */       return null;
/*     */     }
/*     */     
/* 609 */     return strTag.toString().replace("\"", "");
/*     */   }
/*     */   
/*     */   public void removeCivCraftProperty(String string) {
/* 613 */     if (this.nmsStack == null) {
/* 614 */       return;
/*     */     }
/*     */     
/* 617 */     NBTTagCompound civcraftCompound = this.nmsStack.tag.getCompound("civcraft");
/* 618 */     if (civcraftCompound == null) {
/* 619 */       return;
/*     */     }
/*     */     
/* 622 */     civcraftCompound.remove(string);
/*     */     
/* 624 */     if (civcraftCompound.isEmpty()) {
/* 625 */       removeCivCraftCompound();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 630 */     if (this.nmsStack == null) {
/* 631 */       return;
/*     */     }
/*     */     
/* 634 */     if (this.nmsStack.tag == null) {
/* 635 */       this.nmsStack.tag = new NBTTagCompound();
/*     */     }
/*     */     
/* 638 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/*     */     
/* 640 */     if (displayCompound == null) {
/* 641 */       displayCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 644 */     displayCompound.set("Name", new NBTTagString(ChatColor.RESET + name));
/* 645 */     this.nmsStack.tag.set("display", displayCompound);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 649 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/*     */     
/* 651 */     if (displayCompound == null) {
/* 652 */       displayCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 655 */     String name = displayCompound.getString("Name").toString();
/* 656 */     name = name.replace("\"", "");
/* 657 */     return name;
/*     */   }
/*     */   
/*     */   public void setColor(Long long1)
/*     */   {
/* 662 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/*     */     
/* 664 */     if (displayCompound == null) {
/* 665 */       displayCompound = new NBTTagCompound();
/*     */     }
/*     */     
/* 668 */     displayCompound.set("color", new NBTTagInt(long1.intValue()));
/* 669 */     this.nmsStack.tag.set("display", displayCompound);
/*     */   }
/*     */   
/*     */   public int getColor() {
/* 673 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/* 674 */     if (displayCompound == null) {
/* 675 */       return 0;
/*     */     }
/*     */     
/* 678 */     return displayCompound.getInt("color");
/*     */   }
/*     */   
/*     */   public boolean hasColor() {
/* 682 */     if (this.nmsStack == null) {
/* 683 */       return false;
/*     */     }
/*     */     
/* 686 */     if (this.nmsStack.tag == null) {
/* 687 */       return false;
/*     */     }
/*     */     
/* 690 */     NBTTagCompound displayCompound = this.nmsStack.tag.getCompound("display");
/* 691 */     if (displayCompound == null) {
/* 692 */       return false;
/*     */     }
/*     */     
/* 695 */     return displayCompound.hasKey("color");
/*     */   }
/*     */   
/*     */   public void setLore(LinkedList<String> lore) {
/* 699 */     String[] strs = new String[lore.size()];
/*     */     
/* 701 */     for (int i = 0; i < lore.size(); i++) {
/* 702 */       strs[i] = ((String)lore.get(i));
/*     */     }
/*     */     
/* 705 */     setLore(strs);
/*     */   }
/*     */   
/*     */   public void removeCivCraftCompound() {
/* 709 */     if (this.nmsStack == null) {
/* 710 */       return;
/*     */     }
/*     */     
/* 713 */     NBTTagCompound civcraftCompound = this.nmsStack.tag.getCompound("civcraft");
/* 714 */     if (civcraftCompound == null) {
/* 715 */       return;
/*     */     }
/*     */     
/* 718 */     this.nmsStack.tag.remove("civcraft");
/*     */   }
/*     */   
/*     */   public boolean hasLegacyEnhancements() {
/* 722 */     if (this.nmsStack == null) {
/* 723 */       return false;
/*     */     }
/*     */     
/* 726 */     if (this.nmsStack.tag == null) {
/* 727 */       return false;
/*     */     }
/*     */     
/* 730 */     return this.nmsStack.tag.hasKey("civ_enhancements");
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\gpl\AttributeUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */