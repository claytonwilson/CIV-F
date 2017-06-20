/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.camp.Camp;
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.config.CivSettings;
/*     */ import com.avrgaming.civcraft.config.ConfigPlatinumReward;
/*     */ import com.avrgaming.civcraft.exception.AlreadyRegisteredException;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.global.perks.PlatinumManager;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.bukkit.enchantments.Enchantment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdminResCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  40 */     this.command = "/ad res";
/*  41 */     this.displayName = "Admin Resident";
/*     */     
/*  43 */     this.commands.put("settown", "[player] [town] - puts this player in this town.");
/*  44 */     this.commands.put("setcamp", "[player] [camp] - puts this player in this camp.");
/*  45 */     this.commands.put("cleartown", "[resident] - clears this residents town.");
/*  46 */     this.commands.put("enchant", "[enchant] [level] - Adds the enchantment with level to the item in your hand.");
/*  47 */     this.commands.put("giveplat", "[player] [amount] - Gives this player the specified amount of platinum.");
/*  48 */     this.commands.put("givereward", "[player] [rewardID] - Gives player this achievement with its plat rewards.");
/*     */   }
/*     */   
/*     */   public void givereward_cmd() throws CivException {
/*  52 */     Resident resident = getNamedResident(1);
/*  53 */     String rewardID = getNamedString(2, "Enter a Reward ID");
/*     */     label158:
/*  55 */     label171: for (ConfigPlatinumReward reward : CivSettings.platinumRewards.values()) {
/*  56 */       if (reward.name.equals(rewardID)) { String str1;
/*  57 */         switch ((str1 = reward.occurs).hashCode()) {case 3415681:  if (str1.equals("once")) break; break; case 95346201:  if (!str1.equals("daily")) {
/*     */             break label158;
/*  59 */             PlatinumManager.givePlatinumOnce(resident, reward.name, Integer.valueOf(reward.amount), "Sweet! An admin gave you a platinum reward of %d");
/*     */             break label171;
/*     */           } else {
/*  62 */             PlatinumManager.givePlatinumDaily(resident, reward.name, Integer.valueOf(reward.amount), "Sweet! An admin gave you a platinum reward of %d"); }
/*  63 */           break;
/*     */         }
/*  65 */         PlatinumManager.givePlatinum(resident, Integer.valueOf(reward.amount), "Sweet! An admin gave you a platinum reward of %d");
/*     */         
/*     */ 
/*  68 */         CivMessage.sendSuccess(this.sender, "Reward Given.");
/*  69 */         return;
/*     */       }
/*     */     }
/*     */     
/*  73 */     CivMessage.sendError(this.sender, "Couldn't find reward named:" + rewardID);
/*     */   }
/*     */   
/*     */   public void giveplat_cmd() throws CivException
/*     */   {
/*  78 */     Resident resident = getNamedResident(1);
/*  79 */     Integer plat = getNamedInteger(2);
/*     */     
/*  81 */     PlatinumManager.givePlatinum(resident, plat, "Sweet! You were given %d by an admin!");
/*  82 */     CivMessage.sendSuccess(this.sender, "Gave " + resident.getName() + " " + plat + " platinum");
/*     */   }
/*     */   
/*     */   public void enchant_cmd() throws CivException {
/*  86 */     Player player = getPlayer();
/*  87 */     String enchant = getNamedString(1, "Enchant name");
/*  88 */     int level = getNamedInteger(2).intValue();
/*     */     
/*     */ 
/*  91 */     ItemStack stack = player.getItemInHand();
/*  92 */     Enchantment ench = Enchantment.getByName(enchant);
/*  93 */     if (ench == null) {
/*  94 */       String out = "";
/*  95 */       Enchantment[] arrayOfEnchantment; int j = (arrayOfEnchantment = Enchantment.values()).length; for (int i = 0; i < j; i++) { Enchantment ench2 = arrayOfEnchantment[i];
/*  96 */         out = out + ench2.getName() + ",";
/*     */       }
/*  98 */       throw new CivException("No enchantment called " + enchant + " Options:" + out);
/*     */     }
/*     */     
/* 101 */     stack.addUnsafeEnchantment(ench, level);
/* 102 */     CivMessage.sendSuccess(this.sender, "Enchanted.");
/*     */   }
/*     */   
/*     */   public void cleartown_cmd() throws CivException {
/* 106 */     if (this.args.length < 2) {
/* 107 */       throw new CivException("Enter a player name");
/*     */     }
/*     */     
/* 110 */     Resident resident = getNamedResident(1);
/*     */     
/* 112 */     if (resident.hasTown()) {
/* 113 */       resident.getTown().removeResident(resident);
/*     */     }
/*     */     
/* 116 */     resident.save();
/* 117 */     CivMessage.sendSuccess(this.sender, "Cleared " + resident.getName() + " from any town.");
/*     */   }
/*     */   
/*     */   public void setcamp_cmd() throws CivException
/*     */   {
/* 122 */     Resident resident = getNamedResident(1);
/* 123 */     Camp camp = getNamedCamp(2);
/*     */     
/* 125 */     if (resident.hasCamp()) {
/* 126 */       resident.getCamp().removeMember(resident);
/*     */     }
/*     */     
/* 129 */     camp.addMember(resident);
/*     */     
/* 131 */     camp.save();
/* 132 */     resident.save();
/* 133 */     CivMessage.sendSuccess(this.sender, "Moved " + resident.getName() + " into camp " + camp.getName());
/*     */   }
/*     */   
/*     */   public void settown_cmd()
/*     */     throws CivException
/*     */   {
/* 139 */     if (this.args.length < 3) {
/* 140 */       throw new CivException("Enter player and its new town.");
/*     */     }
/*     */     
/* 143 */     Resident resident = getNamedResident(1);
/*     */     
/* 145 */     Town town = getNamedTown(2);
/*     */     
/* 147 */     if (resident.hasTown()) {
/* 148 */       resident.getTown().removeResident(resident);
/*     */     }
/*     */     try
/*     */     {
/* 152 */       town.addResident(resident);
/*     */     } catch (AlreadyRegisteredException e) {
/* 154 */       e.printStackTrace();
/* 155 */       throw new CivException("Already in this town?");
/*     */     }
/*     */     
/* 158 */     town.save();
/* 159 */     resident.save();
/* 160 */     CivMessage.sendSuccess(this.sender, "Moved " + resident.getName() + " into town " + town.getName());
/*     */   }
/*     */   
/*     */   public void doDefaultAction() throws CivException
/*     */   {
/* 165 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 170 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminResCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */