/*     */ package com.avrgaming.civcraft.structure.wonders;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.object.Town;
/*     */ import com.avrgaming.civcraft.object.TownChunk;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.entity.Player;
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
/*     */ public class TheHangingGardens
/*     */   extends Wonder
/*     */ {
/*     */   public TheHangingGardens(ResultSet rs)
/*     */     throws SQLException, CivException
/*     */   {
/*  36 */     super(rs);
/*     */   }
/*     */   
/*     */   public TheHangingGardens(Location center, String id, Town town) throws CivException
/*     */   {
/*  41 */     super(center, id, town);
/*     */   }
/*     */   
/*     */   protected void addBuffs()
/*     */   {
/*  46 */     addBuffToCiv(getCiv(), "buff_hanging_gardens_growth");
/*  47 */     addBuffToCiv(getCiv(), "buff_hanging_gardens_additional_growth");
/*  48 */     addBuffToTown(getTown(), "buff_hanging_gardens_regen");
/*     */   }
/*     */   
/*     */   protected void removeBuffs()
/*     */   {
/*  53 */     removeBuffFromCiv(getCiv(), "buff_hanging_gardens_growth");
/*  54 */     removeBuffFromCiv(getCiv(), "buff_hanging_gardens_additional_growth");
/*  55 */     removeBuffFromTown(getTown(), "buff_hanging_gardens_regen");
/*     */   }
/*     */   
/*     */   public void onLoad()
/*     */   {
/*  60 */     if (isActive()) {
/*  61 */       addBuffs();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onComplete()
/*     */   {
/*  67 */     addBuffs();
/*     */   }
/*     */   
/*     */   public void onDestroy()
/*     */   {
/*  72 */     super.onDestroy();
/*  73 */     removeBuffs();
/*     */   }
/*     */   
/*     */   public void onUpdate()
/*     */   {
/*  78 */     super.onUpdate();
/*     */     Iterator localIterator2;
/*  80 */     for (Iterator localIterator1 = getTown().getCiv().getTowns().iterator(); localIterator1.hasNext(); 
/*  81 */         localIterator2.hasNext())
/*     */     {
/*  80 */       Town t = (Town)localIterator1.next();
/*  81 */       localIterator2 = t.getResidents().iterator(); continue;Resident res = (Resident)localIterator2.next();
/*     */       try {
/*  83 */         Player player = CivGlobal.getPlayer(res);
/*     */         
/*  85 */         if ((!player.isDead()) && (player.isValid()))
/*     */         {
/*     */ 
/*     */ 
/*  89 */           if (player.getHealth() < 20.0D)
/*     */           {
/*     */ 
/*     */ 
/*  93 */             TownChunk tc = CivGlobal.getTownChunk(player.getLocation());
/*  94 */             if ((tc != null) && (tc.getTown() == getTown()))
/*     */             {
/*     */ 
/*     */ 
/*  98 */               if (player.getHealth() >= 19.0D) {
/*  99 */                 player.setHealth(20.0D);
/*     */               } else {
/* 101 */                 player.setHealth(player.getHealth() + 1.0D);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\structure\wonders\TheHangingGardens.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */