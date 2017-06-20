/*    */ package com.avrgaming.civcraft.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.bukkit.inventory.meta.BookMeta;
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
/*    */ 
/*    */ 
/*    */ public class BookUtil
/*    */ {
/*    */   public static final int LINES_PER_PAGE = 12;
/*    */   public static final int CHARS_PER_LINE = 18;
/*    */   
/*    */   public static void paginate(BookMeta meta, String longString)
/*    */   {
/* 38 */     int count = 0;
/*    */     
/* 40 */     ArrayList<String> lines = new ArrayList();
/*    */     
/* 42 */     String line = "";
/* 43 */     char[] arrayOfChar; int j = (arrayOfChar = longString.toCharArray()).length; for (int i = 0; i < j; i++) { char c = arrayOfChar[i];
/* 44 */       count++;
/* 45 */       if ((c == '\n') || (count > 18)) {
/* 46 */         lines.add(line);
/* 47 */         line = "";
/* 48 */         count = 0;
/*    */       }
/* 50 */       if (c != '\n') {
/* 51 */         line = line + c;
/*    */       }
/*    */     }
/*    */     
/* 55 */     linePageinate(meta, lines);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void linePageinate(BookMeta meta, ArrayList<String> lines)
/*    */   {
/* 65 */     int count = 0;
/* 66 */     String page = "";
/* 67 */     for (String line : lines) {
/* 68 */       count++;
/* 69 */       if (count > 12) {
/* 70 */         meta.addPage(new String[] { page });
/* 71 */         count = 0;
/* 72 */         page = "";
/*    */       }
/* 74 */       page = page + line + "\n";
/*    */     }
/*    */     
/* 77 */     meta.addPage(new String[] { page });
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\util\BookUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */