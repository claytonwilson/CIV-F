/*   */ package com.avrgaming.civcraft.arena;
/*   */ 
/*   */ import java.util.HashMap;
/*   */ 
/*   */ public class ArenaTimer implements Runnable
/*   */ {
/*   */   public void run() {
/* 8 */     for (Arena arena : ArenaManager.activeArenas.values()) {
/* 9 */       arena.decrementTimer();
/*   */     }
/*   */   }
/*   */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\arena\ArenaTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */