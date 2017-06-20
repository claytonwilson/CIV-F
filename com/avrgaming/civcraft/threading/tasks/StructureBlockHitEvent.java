/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
/*    */ import com.avrgaming.civcraft.lorestorage.LoreMaterial;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.BuildableDamageBlock;
/*    */ import com.avrgaming.civcraft.structure.Buildable;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import gpl.AttributeUtil;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StructureBlockHitEvent
/*    */   implements Runnable
/*    */ {
/*    */   String playerName;
/*    */   BlockCoord coord;
/*    */   BuildableDamageBlock dmgBlock;
/*    */   World world;
/*    */   
/*    */   public StructureBlockHitEvent(String player, BlockCoord coord, BuildableDamageBlock dmgBlock, World world)
/*    */   {
/* 48 */     this.playerName = player;
/* 49 */     this.coord = coord;
/* 50 */     this.dmgBlock = dmgBlock;
/* 51 */     this.world = world;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 57 */     if (this.playerName == null) {
/* 58 */       return;
/*    */     }
/* 60 */     Player player = Bukkit.getPlayer(this.playerName);
/* 61 */     if (this.dmgBlock.allowDamageNow(player))
/*    */     {
/* 63 */       int damage = 1;
/* 64 */       LoreMaterial material = LoreMaterial.getMaterial(player.getItemInHand());
/* 65 */       if (material != null) {
/* 66 */         damage = material.onStructureBlockBreak(this.dmgBlock, damage);
/*    */       }
/*    */       
/* 69 */       if ((player.getItemInHand() != null) && (!player.getItemInHand().getType().equals(net.minecraft.server.v1_7_R2.Material.AIR))) {
/* 70 */         AttributeUtil attrs = new AttributeUtil(player.getItemInHand());
/* 71 */         for (LoreEnhancement enhance : attrs.getEnhancements()) {
/* 72 */           damage = enhance.onStructureBlockBreak(this.dmgBlock, damage);
/*    */         }
/*    */       }
/*    */       
/* 76 */       if (damage > 1) {
/* 77 */         CivMessage.send(player, "§7Punchout does " + (damage - 1) + " extra damage!");
/*    */       }
/*    */       
/* 80 */       this.dmgBlock.getOwner().onDamage(damage, this.world, player, this.dmgBlock.getCoord(), this.dmgBlock);
/*    */     } else {
/* 82 */       CivMessage.sendErrorNoRepeat(player, 
/* 83 */         "This block belongs to a " + this.dmgBlock.getOwner().getDisplayName() + " and cannot be destroyed right now.");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\StructureBlockHitEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */