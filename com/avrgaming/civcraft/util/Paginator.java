/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ public class Paginator
/*    */ {
/*  7 */   public LinkedList<Object> page = new LinkedList();
/*  8 */   public boolean hasNextPage = false;
/*  9 */   public boolean hasPrevPage = false;
/* 10 */   public int displayLimit = 44;
/*    */   
/*    */   public void paginate(java.util.Collection<?> source, int pageNumber) {
/* 13 */     int start = pageNumber * this.displayLimit;
/* 14 */     int i = 0;
/* 15 */     int d = 0;
/* 16 */     for (Object perk : source) {
/* 17 */       if (i < start) {
/* 18 */         this.hasPrevPage = true;
/* 19 */         i++;
/*    */       }
/*    */       else
/*    */       {
/* 23 */         if (d > this.displayLimit) {
/* 24 */           this.hasNextPage = true;
/* 25 */           break;
/*    */         }
/*    */         
/* 28 */         this.page.add(perk);
/* 29 */         d++;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\Paginator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */