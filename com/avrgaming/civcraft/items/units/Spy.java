/*     */ package com.avrgaming.civcraft.items.units;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.ConfigUnit;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.MissionLogger;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.util.BookUtil;
/*     */ import gpl.AttributeUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.inventory.meta.BookMeta;
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
/*     */ public class Spy
/*     */   extends UnitMaterial
/*     */ {
/*     */   public static final int BOOK_ID = 403;
/*  46 */   public ArrayList<UnitItemMaterial> missionBooks = new ArrayList();
/*     */   
/*     */   public Spy(String id, ConfigUnit configUnit) {
/*  49 */     super(id, configUnit);
/*     */   }
/*     */   
/*     */   public static void spawn(Inventory inv, Town town) throws CivException {
/*  53 */     ItemStack is = LoreMaterial.spawn(Unit.SPY_UNIT);
/*     */     
/*  55 */     UnitMaterial.setOwningTown(town, is);
/*  56 */     if (!Unit.addItemNoStack(inv, is)) {
/*  57 */       throw new CivException("Cannot make " + Unit.SPY_UNIT.getUnit().name + ". Barracks chest is full! Make Room!");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void spawn(Inventory inv) throws CivException {
/*  62 */     ItemStack is = LoreMaterial.spawn(Unit.SPY_UNIT);
/*     */     
/*  64 */     if (!Unit.addItemNoStack(inv, is)) {
/*  65 */       throw new CivException("Cannot make " + Unit.SPY_UNIT.getUnit().name + ". Barracks chest is full! Make Room!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addMissionBook(UnitItemMaterial umat) {
/*  70 */     this.missionBooks.add(umat);
/*  71 */     this.allowedSubslots.add(Integer.valueOf(umat.getSocketSlot()));
/*     */   }
/*     */   
/*     */   public void giveMissionBooks(Player player)
/*     */   {
/*  76 */     Inventory inv = player.getInventory();
/*     */     
/*  78 */     for (UnitItemMaterial book : this.missionBooks)
/*     */     {
/*     */ 
/*  81 */       ItemStack stack = inv.getItem(book.getSocketSlot());
/*     */       
/*  83 */       ItemStack is = LoreMaterial.spawn(book);
/*  84 */       AttributeUtil attrs = new AttributeUtil(is);
/*  85 */       attrs.setLore(book.getLore());
/*  86 */       is = attrs.getStack();
/*     */       
/*  88 */       if (!player.getInventory().contains(is)) {
/*  89 */         inv.setItem(book.getSocketSlot(), is);
/*     */         
/*     */ 
/*  92 */         if (stack != null) {
/*  93 */           HashMap<Integer, ItemStack> leftovers = inv.addItem(new ItemStack[] { stack });
/*     */           
/*  95 */           for (ItemStack s : leftovers.values()) {
/*  96 */             player.getWorld().dropItem(player.getLocation(), s);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onItemToPlayer(Player player, ItemStack stack)
/*     */   {
/* 107 */     giveMissionBooks(player);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onItemFromPlayer(Player player, ItemStack stack)
/*     */   {
/* 113 */     removeChildren(player.getInventory());
/*     */   }
/*     */   
/*     */ 
/*     */   public void onPlayerDeath(EntityDeathEvent event, ItemStack stack)
/*     */   {
/* 119 */     Player player = (Player)event.getEntity();
/* 120 */     Resident resident = CivGlobal.getResident(player);
/* 121 */     if ((resident == null) || (!resident.hasTown())) {
/* 122 */       return;
/*     */     }
/*     */     
/*     */ 
/* 126 */     ArrayList<String> bookout = MissionLogger.getMissionLogs(resident.getTown());
/*     */     
/* 128 */     ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
/* 129 */     BookMeta meta = (BookMeta)book.getItemMeta();
/* 130 */     ArrayList<String> lore = new ArrayList();
/* 131 */     lore.add("Mission Report");
/* 132 */     meta.setAuthor("Mission Reports");
/* 133 */     meta.setTitle("Missions From " + resident.getTown().getName());
/*     */     
/* 135 */     String out = "";
/* 136 */     for (String str : bookout) {
/* 137 */       out = out + str + "\n";
/*     */     }
/* 139 */     BookUtil.paginate(meta, out);
/*     */     
/*     */ 
/* 142 */     meta.setLore(lore);
/* 143 */     book.setItemMeta(meta);
/* 144 */     player.getWorld().dropItem(player.getLocation(), book);
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\units\Spy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */