/*    */ package com.avrgaming.civcraft.threading.timers;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivLog;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class AnnouncementTimer
/*    */   implements Runnable
/*    */ {
/*    */   List<String> announcements;
/*    */   
/*    */   public AnnouncementTimer(String filename)
/*    */   {
/* 40 */     File file = new File(filename);
/*    */     
/* 42 */     this.announcements = new ArrayList();
/*    */     
/* 44 */     if (!file.exists()) {
/* 45 */       CivLog.warning("No " + filename + " to run announcements on.");
/* 46 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 50 */       BufferedReader br = new BufferedReader(new FileReader(file));
/*    */       try
/*    */       {
/*    */         String line;
/* 54 */         while ((line = br.readLine()) != null) { String line;
/* 55 */           this.announcements.add(line);
/*    */         }
/*    */         
/* 58 */         br.close();
/*    */       } catch (IOException e) {
/* 60 */         e.printStackTrace(); return;
/*    */       }
/*    */       return;
/*    */     }
/*    */     catch (FileNotFoundException e) {
/* 65 */       e.printStackTrace(); return;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/* 75 */     for (String str : this.announcements) {
/* 76 */       CivMessage.sendAll("§6Tip: §f" + str);
/*    */       try
/*    */       {
/* 79 */         Thread.sleep(300000L);
/*    */       } catch (InterruptedException e) {
/* 81 */         e.printStackTrace();
/* 82 */         return;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\timers\AnnouncementTimer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */