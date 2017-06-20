/*    */ package com.avrgaming.civcraft.endgame;
/*    */ 
/*    */ import com.avrgaming.civcraft.exception.CivException;
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.mobs.MobSpawner;
/*    */ import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobLevel;
/*    */ import com.avrgaming.civcraft.mobs.MobSpawner.CustomMobType;
/*    */ import com.avrgaming.civcraft.object.CultureChunk;
/*    */ import com.avrgaming.civcraft.util.ChunkCoord;
/*    */ import java.util.Random;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Apocalypse
/*    */   implements Runnable
/*    */ {
/*    */   public void run()
/*    */   {
/* 22 */     if (!CivGlobal.endWorld) {
/* 23 */       return;
/*    */     }
/*    */     
/* 26 */     for (int i = 0; i < 40; i++) {
/* 27 */       Random rand = new Random();
/* 28 */       int ran = rand.nextInt(Bukkit.getOnlinePlayers().length);
/*    */       
/* 30 */       Player player = Bukkit.getOnlinePlayers()[ran];
/*    */       
/* 32 */       CultureChunk cc = CivGlobal.getCultureChunk(new ChunkCoord(player.getLocation()));
/* 33 */       if (cc == null)
/*    */       {
/*    */ 
/*    */ 
/* 37 */         Location newLoc = player.getLocation().add(rand.nextInt(20) + 20, 0.0D, rand.nextInt(20) + 20);
/*    */         try
/*    */         {
/* 40 */           MobSpawner.spawnCustomMob(MobSpawner.CustomMobType.YOBOBOSS, MobSpawner.CustomMobLevel.BRUTAL, newLoc);
/*    */         } catch (CivException e) {
/* 42 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\endgame\Apocalypse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */