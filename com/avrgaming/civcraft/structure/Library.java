/*     */ package com.avrgaming.civcraft.structure;
/*     */ 
/*     */ import com.avrgaming.civcraft.components.AttributeBiome;
/*     */ import com.avrgaming.civcraft.components.NonMemberFeeComponent;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivLog;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.EconObject;
/*     */ import com.avrgaming.civcraft.object.LibraryEnchantment;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.StructureSign;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Library
/*     */   extends Structure
/*     */ {
/*     */   private int level;
/*     */   public AttributeBiome cultureBeakers;
/*  49 */   ArrayList<LibraryEnchantment> enchantments = new ArrayList();
/*     */   
/*     */   private NonMemberFeeComponent nonMemberFeeComponent;
/*     */   
/*     */ 
/*     */   public static Enchantment getEnchantFromString(String name)
/*     */   {
/*  56 */     if (name.equalsIgnoreCase("protection")) {
/*  57 */       return Enchantment.PROTECTION_ENVIRONMENTAL;
/*     */     }
/*  59 */     if (name.equalsIgnoreCase("fire_protection")) {
/*  60 */       return Enchantment.PROTECTION_FIRE;
/*     */     }
/*  62 */     if (name.equalsIgnoreCase("feather_falling")) {
/*  63 */       return Enchantment.PROTECTION_FALL;
/*     */     }
/*  65 */     if (name.equalsIgnoreCase("blast_protection")) {
/*  66 */       return Enchantment.PROTECTION_EXPLOSIONS;
/*     */     }
/*  68 */     if (name.equalsIgnoreCase("projectile_protection")) {
/*  69 */       return Enchantment.PROTECTION_PROJECTILE;
/*     */     }
/*  71 */     if (name.equalsIgnoreCase("respiration")) {
/*  72 */       return Enchantment.OXYGEN;
/*     */     }
/*  74 */     if (name.equalsIgnoreCase("aqua_affinity")) {
/*  75 */       return Enchantment.WATER_WORKER;
/*     */     }
/*     */     
/*     */ 
/*  79 */     if (name.equalsIgnoreCase("sharpness")) {
/*  80 */       return Enchantment.DAMAGE_ALL;
/*     */     }
/*  82 */     if (name.equalsIgnoreCase("smite")) {
/*  83 */       return Enchantment.DAMAGE_UNDEAD;
/*     */     }
/*  85 */     if (name.equalsIgnoreCase("bane_of_arthropods")) {
/*  86 */       return Enchantment.DAMAGE_ARTHROPODS;
/*     */     }
/*  88 */     if (name.equalsIgnoreCase("knockback")) {
/*  89 */       return Enchantment.KNOCKBACK;
/*     */     }
/*  91 */     if (name.equalsIgnoreCase("fire_aspect")) {
/*  92 */       return Enchantment.FIRE_ASPECT;
/*     */     }
/*  94 */     if (name.equalsIgnoreCase("looting")) {
/*  95 */       return Enchantment.LOOT_BONUS_MOBS;
/*     */     }
/*     */     
/*     */ 
/*  99 */     if (name.equalsIgnoreCase("efficiency")) {
/* 100 */       return Enchantment.DIG_SPEED;
/*     */     }
/* 102 */     if (name.equalsIgnoreCase("silk_touch")) {
/* 103 */       return Enchantment.SILK_TOUCH;
/*     */     }
/* 105 */     if (name.equalsIgnoreCase("unbreaking")) {
/* 106 */       return Enchantment.DURABILITY;
/*     */     }
/* 108 */     if (name.equalsIgnoreCase("fortune")) {
/* 109 */       return Enchantment.LOOT_BONUS_BLOCKS;
/*     */     }
/*     */     
/*     */ 
/* 113 */     if (name.equalsIgnoreCase("power")) {
/* 114 */       return Enchantment.ARROW_DAMAGE;
/*     */     }
/* 116 */     if (name.equalsIgnoreCase("punch")) {
/* 117 */       return Enchantment.ARROW_KNOCKBACK;
/*     */     }
/* 119 */     if (name.equalsIgnoreCase("flame")) {
/* 120 */       return Enchantment.ARROW_FIRE;
/*     */     }
/* 122 */     if (name.equalsIgnoreCase("infinity")) {
/* 123 */       return Enchantment.ARROW_INFINITE;
/*     */     }
/*     */     
/* 126 */     return null;
/*     */   }
/*     */   
/*     */   public double getNonResidentFee()
/*     */   {
/* 131 */     return this.nonMemberFeeComponent.getFeeRate();
/*     */   }
/*     */   
/*     */   public void setNonResidentFee(double nonResidentFee) {
/* 135 */     this.nonMemberFeeComponent.setFeeRate(nonResidentFee);
/*     */   }
/*     */   
/*     */   private String getNonResidentFeeString() {
/* 139 */     return "Fee: " + new StringBuilder(String.valueOf((int)(getNonResidentFee() * 100.0D))).append("%").toString().toString();
/*     */   }
/*     */   
/*     */   protected Library(Location center, String id, Town town) throws CivException
/*     */   {
/* 144 */     super(center, id, town);
/* 145 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/* 146 */     this.nonMemberFeeComponent.onSave();
/*     */   }
/*     */   
/*     */   public Library(ResultSet rs) throws SQLException, CivException {
/* 150 */     super(rs);
/* 151 */     this.nonMemberFeeComponent = new NonMemberFeeComponent(this);
/* 152 */     this.nonMemberFeeComponent.onLoad();
/*     */   }
/*     */   
/*     */   public void loadSettings()
/*     */   {
/* 157 */     super.loadSettings();
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 161 */     return this.level;
/*     */   }
/*     */   
/*     */   public void setLevel(int level)
/*     */   {
/* 166 */     this.level = level;
/*     */   }
/*     */   
/*     */   private StructureSign getSignFromSpecialId(int special_id) {
/* 170 */     for (StructureSign sign : getSigns()) {
/* 171 */       int id = Integer.valueOf(sign.getAction()).intValue();
/* 172 */       if (id == special_id) {
/* 173 */         return sign;
/*     */       }
/*     */     }
/* 176 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateSignText()
/*     */   {
/* 182 */     int count = 0;
/*     */     
/* 184 */     for (LibraryEnchantment enchant : this.enchantments) {
/* 185 */       StructureSign sign = getSignFromSpecialId(count);
/* 186 */       if (sign == null) {
/* 187 */         CivLog.error("sign from special id was null, id:" + count);
/* 188 */         return;
/*     */       }
/* 190 */       sign.setText(enchant.displayName + "\n" + 
/* 191 */         "Level " + enchant.level + "\n" + 
/* 192 */         getNonResidentFeeString() + "\n" + 
/* 193 */         "For " + enchant.price);
/* 194 */       sign.update();
/* 195 */       count++;
/*     */     }
/* 198 */     for (; 
/* 198 */         count < getSigns().size(); count++) {
/* 199 */       StructureSign sign = getSignFromSpecialId(count);
/* 200 */       sign.setText("Library Slot\nEmpty");
/* 201 */       sign.update();
/* 202 */       sign.save();
/*     */     }
/*     */   }
/*     */   
/*     */   public void validateEnchantment(ItemStack item, LibraryEnchantment ench) throws CivException {
/* 207 */     if (ench.enchant != null)
/*     */     {
/* 209 */       if (!ench.enchant.canEnchantItem(item)) {
/* 210 */         throw new CivException("You cannot enchant this item with this enchantment.");
/*     */       }
/*     */       
/* 213 */       if ((item.containsEnchantment(ench.enchant)) && (item.getEnchantmentLevel(ench.enchant) > ench.level)) {
/* 214 */         throw new CivException("You already have this enchantment at this level, or better.");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 219 */       if (!ench.enhancement.canEnchantItem(item)) {
/* 220 */         throw new CivException("You cannot enchant this item with this enchantment.");
/*     */       }
/*     */       
/* 223 */       if (ench.enhancement.hasEnchantment(item)) {
/* 224 */         throw new CivException("You already have this enchantment.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemStack addEnchantment(ItemStack item, LibraryEnchantment ench) {
/* 230 */     if (ench.enchant != null) {
/* 231 */       item.addUnsafeEnchantment(ench.enchant, ench.level);
/*     */     } else {
/* 233 */       item = LoreMaterial.addEnhancement(item, ench.enhancement);
/*     */     }
/* 235 */     return item;
/*     */   }
/*     */   
/*     */   public void add_enchantment_to_tool(Player player, StructureSign sign, PlayerInteractEvent event) throws CivException {
/* 239 */     int special_id = Integer.valueOf(sign.getAction()).intValue();
/*     */     
/* 241 */     if (!event.hasItem()) {
/* 242 */       CivMessage.send(player, "§cYou must have the item you wish to enchant in hand.");
/* 243 */       return;
/*     */     }
/* 245 */     ItemStack item = event.getItem();
/*     */     
/* 247 */     if (special_id >= this.enchantments.size()) {
/* 248 */       throw new CivException("Library enchantment not ready.");
/*     */     }
/*     */     
/*     */ 
/* 252 */     LibraryEnchantment ench = (LibraryEnchantment)this.enchantments.get(special_id);
/* 253 */     validateEnchantment(item, ench);
/*     */     
/* 255 */     int payToTown = (int)Math.round(ench.price * getNonResidentFee());
/*     */     
/*     */ 
/* 258 */     Resident resident = CivGlobal.getResident(player.getName());
/* 259 */     Town t = resident.getTown();
/* 260 */     if (t == getTown())
/*     */     {
/* 262 */       payToTown = 0;
/*     */     }
/*     */     
/*     */ 
/* 266 */     if (!resident.getTreasury().hasEnough(ench.price + payToTown)) {
/* 267 */       CivMessage.send(player, "§cYou do not have enough money, you need " + ench.price + payToTown + " coins.");
/* 268 */       return;
/*     */     }
/*     */     
/*     */ 
/* 272 */     resident.getTreasury().withdraw(ench.price);
/*     */     
/*     */ 
/* 275 */     if (payToTown != 0) {
/* 276 */       getTown().depositDirect(payToTown);
/*     */       
/* 278 */       CivMessage.send(player, "§ePaid " + payToTown + " coins in non-resident taxes.");
/*     */     }
/*     */     
/*     */ 
/* 282 */     ItemStack newStack = addEnchantment(item, ench);
/* 283 */     player.setItemInHand(newStack);
/* 284 */     CivMessage.send(player, "§aEnchanted with " + ench.displayName + "!");
/*     */   }
/*     */   
/*     */   public void processSignAction(Player player, StructureSign sign, PlayerInteractEvent event)
/*     */   {
/*     */     try {
/* 290 */       add_enchantment_to_tool(player, sign, event);
/*     */     } catch (CivException e) {
/* 292 */       CivMessage.send(player, "§c" + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public String getDynmapDescription()
/*     */   {
/* 298 */     String out = "<u><b>Library</u></b><br/>";
/*     */     
/* 300 */     if (this.enchantments.size() == 0) {
/* 301 */       out = out + "Nothing stocked.";
/*     */     }
/*     */     else {
/* 304 */       for (LibraryEnchantment mat : this.enchantments) {
/* 305 */         out = out + mat.displayName + " for " + mat.price + "<br/>";
/*     */       }
/*     */     }
/* 308 */     return out;
/*     */   }
/*     */   
/*     */   public ArrayList<LibraryEnchantment> getEnchants()
/*     */   {
/* 313 */     return this.enchantments;
/*     */   }
/*     */   
/*     */   public void addEnchant(LibraryEnchantment enchant) throws CivException
/*     */   {
/* 318 */     if (this.enchantments.size() >= 4) {
/* 319 */       throw new CivException("Library is full.");
/*     */     }
/* 321 */     this.enchantments.add(enchant);
/*     */   }
/*     */   
/*     */   public String getMarkerIconName()
/*     */   {
/* 326 */     return "bookshelf";
/*     */   }
/*     */   
/*     */   public void reset() {
/* 330 */     this.enchantments.clear();
/* 331 */     updateSignText();
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\Library.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */