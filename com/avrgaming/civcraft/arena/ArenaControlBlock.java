/*    */ package com.avrgaming.civcraft.arena;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.util.BlockCoord;
/*    */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*    */ import com.avrgaming.civcraft.util.ItemManager;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Color;
/*    */ import org.bukkit.FireworkEffect;
/*    */ import org.bukkit.FireworkEffect.Builder;
/*    */ import org.bukkit.FireworkEffect.Type;
/*    */ import org.bukkit.Sound;
/*    */ import org.bukkit.World;
/*    */ 
/*    */ public class ArenaControlBlock
/*    */ {
/*    */   public BlockCoord coord;
/*    */   public int teamID;
/*    */   public int maxHP;
/*    */   public int curHP;
/*    */   public Arena arena;
/*    */   
/*    */   public ArenaControlBlock(BlockCoord c, int teamID, int maxHP, Arena arena)
/*    */   {
/* 26 */     this.coord = c;
/* 27 */     this.teamID = teamID;
/* 28 */     this.maxHP = maxHP;
/* 29 */     this.curHP = maxHP;
/* 30 */     this.arena = arena;
/*    */   }
/*    */   
/*    */   public void onBreak(Resident resident) {
/* 34 */     if (resident.getTeam() == this.arena.getTeamFromID(this.teamID)) {
/* 35 */       CivMessage.sendError(resident, "Can't damage our own control blocks.");
/* 36 */       return;
/*    */     }
/*    */     
/* 39 */     if (this.curHP == 0) {
/* 40 */       return;
/*    */     }
/*    */     
/* 43 */     this.curHP -= 1;
/*    */     
/* 45 */     this.arena.decrementScoreForTeamID(this.teamID);
/*    */     
/* 47 */     if (this.curHP <= 0)
/*    */     {
/* 49 */       explode();
/* 50 */       this.arena.onControlBlockDestroy(this.teamID, resident.getTeam());
/*    */     }
/*    */     else {
/* 53 */       CivMessage.sendTeam(resident.getTeam(), "§a" + resident.getName() + " hit an enemy control block! (" + this.curHP + " / " + this.maxHP + ")");
/* 54 */       CivMessage.sendTeam(this.arena.getTeamFromID(this.teamID), "§cOur control block was hit by " + resident.getName() + " (" + this.curHP + " / " + this.maxHP + ")");
/*    */     }
/*    */   }
/*    */   
/*    */   public void explode()
/*    */   {
/* 60 */     World world = Bukkit.getWorld(this.coord.getWorldname());
/* 61 */     ItemManager.setTypeId(this.coord.getLocation().getBlock(), 0);
/* 62 */     world.playSound(this.coord.getLocation(), Sound.ANVIL_BREAK, 1.0F, -1.0F);
/* 63 */     world.playSound(this.coord.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
/*    */     
/* 65 */     FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.YELLOW).withColor(Color.RED).withTrail().withFlicker().build();
/* 66 */     FireworkEffectPlayer fePlayer = new FireworkEffectPlayer();
/* 67 */     for (int i = 0; i < 3; i++) {
/*    */       try {
/* 69 */         fePlayer.playFirework(world, this.coord.getLocation(), effect);
/*    */       } catch (Exception e) {
/* 71 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\arena\ArenaControlBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */