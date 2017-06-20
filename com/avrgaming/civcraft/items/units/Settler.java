/*     */ package com.avrgaming.civcraft.items.units;
/*     */ 
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigBuildableInfo;
/*     */ import com.avrgaming.civcraft.config.ConfigUnit;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.interactive.InteractiveTownName;
/*     */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.structure.Buildable;
/*     */ import com.avrgaming.civcraft.structure.TownHall;
/*     */ import com.avrgaming.civcraft.util.BlockCoord;
/*     */ import com.avrgaming.civcraft.util.CallbackInterface;
/*     */ import com.avrgaming.civcraft.util.CivColor;
/*     */ import gpl.AttributeUtil;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Map;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.Inventory;
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
/*     */ public class Settler
/*     */   extends UnitMaterial
/*     */   implements CallbackInterface
/*     */ {
/*     */   public Settler(String id, ConfigUnit configUnit)
/*     */   {
/*  51 */     super(id, configUnit);
/*     */   }
/*     */   
/*     */   public static void spawn(Inventory inv, Town town) throws CivException
/*     */   {
/*  56 */     ItemStack is = LoreMaterial.spawn(Unit.SETTLER_UNIT);
/*     */     
/*  58 */     UnitMaterial.setOwningTown(town, is);
/*     */     
/*  60 */     AttributeUtil attrs = new AttributeUtil(is);
/*  61 */     attrs.addLore("§cOnly Usable In Civ: §b" + town.getCiv().getName());
/*  62 */     attrs.addLore("§6Right Click To Found Town");
/*  63 */     attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
/*  64 */     attrs.addLore("§6Soulbound");
/*     */     
/*  66 */     attrs.setCivCraftProperty("owner_civ_id", town.getCiv().getId());
/*  67 */     is = attrs.getStack();
/*     */     
/*     */ 
/*  70 */     if (!Unit.addItemNoStack(inv, is)) {
/*  71 */       throw new CivException("Cannot make " + Unit.SETTLER_UNIT.getUnit().name + ". Barracks chest is full! Make Room!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onInteract(PlayerInteractEvent event)
/*     */   {
/*  79 */     event.setCancelled(true);
/*  80 */     Player player = event.getPlayer();
/*  81 */     player.updateInventory();
/*  82 */     Resident resident = CivGlobal.getResident(player);
/*     */     
/*  84 */     if ((resident == null) || (!resident.hasTown())) {
/*  85 */       CivMessage.sendError(player, "You are not part of a civilization.");
/*  86 */       return;
/*     */     }
/*     */     
/*  89 */     AttributeUtil attrs = new AttributeUtil(event.getItem());
/*  90 */     String ownerIdString = attrs.getCivCraftProperty("owner_civ_id");
/*  91 */     if (ownerIdString == null) {
/*  92 */       CivMessage.sendError(player, "Cannot find owner civilization ID. This settler is broken. Report this to an admin.");
/*  93 */       return;
/*     */     }
/*     */     
/*  96 */     int civ_id = Integer.valueOf(ownerIdString).intValue();
/*  97 */     if (civ_id != resident.getCiv().getId()) {
/*  98 */       CivMessage.sendError(player, "You cannot use this settler unit. Your civilization is not the owner.");
/*  99 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 104 */       minDistance = CivSettings.getDouble(CivSettings.townConfig, "town.min_town_distance");
/*     */     } catch (InvalidConfiguration e) { double minDistance;
/* 106 */       CivMessage.sendError(player, "Internal configuration error.");
/* 107 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     double minDistance;
/* 111 */     for (Town town : CivGlobal.getTowns()) {
/* 112 */       TownHall townhall = town.getTownHall();
/* 113 */       if (townhall != null)
/*     */       {
/*     */ 
/*     */ 
/* 117 */         double dist = townhall.getCenterLocation().distance(new BlockCoord(event.getPlayer().getLocation()));
/* 118 */         if (dist < minDistance) {
/* 119 */           DecimalFormat df = new DecimalFormat();
/* 120 */           CivMessage.sendError(player, "Cannot build town here. Too close to the town of " + town.getName() + ". Distance is " + df.format(dist) + " and needs to be " + minDistance);
/* 121 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 129 */     CivMessage.send(player, "§a" + CivColor.BOLD + "Checking structure position...Please wait.");
/* 130 */     ConfigBuildableInfo info = (ConfigBuildableInfo)CivSettings.structures.get("s_townhall");
/*     */     try {
/* 132 */       Buildable.buildVerifyStatic(player, info, player.getLocation(), this);
/*     */     } catch (CivException e) {
/* 134 */       CivMessage.sendError(player, e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   public void execute(String playerName)
/*     */   {
/*     */     try
/*     */     {
/* 142 */       player = CivGlobal.getPlayer(playerName);
/*     */     } catch (CivException e) { Player player;
/*     */       return; }
/*     */     Player player;
/* 146 */     Resident resident = CivGlobal.getResident(playerName);
/* 147 */     resident.desiredTownLocation = player.getLocation();
/*     */     
/* 149 */     CivMessage.sendHeading(player, "Founding A New Town");
/* 150 */     CivMessage.send(player, "§aThis looks like a good place to settle!");
/* 151 */     CivMessage.send(player, " ");
/* 152 */     CivMessage.send(player, "§a" + ChatColor.BOLD + "What shall your new Town be called?");
/* 153 */     CivMessage.send(player, "§7(To cancel, type 'cancel')");
/*     */     
/* 155 */     resident.setInteractiveMode(new InteractiveTownName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\items\units\Settler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */