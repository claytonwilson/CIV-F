/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.util.FireworkEffectPlayer;
/*    */ import org.bukkit.FireworkEffect;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
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
/*    */ public class FireWorkTask
/*    */   implements Runnable
/*    */ {
/* 29 */   FireworkEffectPlayer fplayer = new FireworkEffectPlayer();
/*    */   FireworkEffect fe;
/*    */   int repeats;
/*    */   World world;
/*    */   Location loc;
/*    */   
/*    */   public FireWorkTask(FireworkEffect fe, World world, Location loc, int repeats) {
/* 36 */     this.fe = fe;
/* 37 */     this.repeats = repeats;
/* 38 */     this.world = world;
/* 39 */     this.loc = loc;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 44 */     for (int i = 0; i < this.repeats; i++) {
/*    */       try {
/* 46 */         this.fplayer.playFirework(this.world, this.loc, this.fe);
/*    */       } catch (Exception e) {
/* 48 */         e.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\FireWorkTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */