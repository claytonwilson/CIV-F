/*    */ package com.avrgaming.civcraft.command.admin;
/*    */ 
/*    */ import com.avrgaming.civcraft.command.CommandBase;
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementArenaItem;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementAttack;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementDefense;
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancementSoulBound;
/*    */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdminItemCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public void init()
/*    */   {
/* 24 */     this.command = "/ad item";
/* 25 */     this.displayName = "Admin Item";
/*    */     
/* 27 */     this.commands.put("enhance", "[name] - Adds the specified enhancement.");
/*    */   }
/*    */   
/*    */   public void enhance_cmd() throws CivException {
/* 31 */     Player player = getPlayer();
/* 32 */     HashMap<String, LoreEnhancement> enhancements = new HashMap();
/* 33 */     ItemStack inHand = getPlayer().getItemInHand();
/*    */     
/* 35 */     enhancements.put("soulbound", new LoreEnhancementSoulBound());
/* 36 */     enhancements.put("attack", new LoreEnhancementAttack());
/* 37 */     enhancements.put("defence", new LoreEnhancementDefense());
/* 38 */     enhancements.put("arena", new LoreEnhancementArenaItem());
/*    */     
/* 40 */     if ((inHand == null) || (ItemManager.getId(inHand) == 0)) {
/* 41 */       throw new CivException("You must have an item in your hand to enhance it.");
/*    */     }
/*    */     
/* 44 */     if (this.args.length < 2) {
/* 45 */       CivMessage.sendHeading(this.sender, "Possible Enchants");
/* 46 */       String out = "";
/* 47 */       for (String str : enhancements.keySet()) {
/* 48 */         out = out + str + ", ";
/*    */       }
/* 50 */       CivMessage.send(this.sender, out);
/* 51 */       return;
/*    */     }
/*    */     
/* 54 */     String name = getNamedString(1, "enchantname");
/* 55 */     name.toLowerCase();
/* 56 */     for (String str : enhancements.keySet()) {
/* 57 */       if (name.equals(str)) {
/* 58 */         LoreEnhancement enh = (LoreEnhancement)enhancements.get(str);
/* 59 */         ItemStack stack = LoreMaterial.addEnhancement(inHand, enh);
/* 60 */         player.setItemInHand(stack);
/* 61 */         CivMessage.sendSuccess(this.sender, "Enhanced with " + name);
/* 62 */         return;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void doDefaultAction() throws CivException
/*    */   {
/* 69 */     showHelp();
/*    */   }
/*    */   
/*    */   public void showHelp()
/*    */   {
/* 74 */     showBasicHelp();
/*    */   }
/*    */   
/*    */   public void permissionCheck()
/*    */     throws CivException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminItemCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */