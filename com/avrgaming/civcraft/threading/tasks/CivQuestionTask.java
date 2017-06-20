/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.object.Civilization;
/*     */ import com.avrgaming.civcraft.object.Resident;
/*     */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*     */ import com.avrgaming.civcraft.questions.QuestionResponseInterface;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CivQuestionTask
/*     */   implements Runnable
/*     */ {
/*     */   Civilization askedCiv;
/*     */   Civilization questionCiv;
/*     */   String question;
/*     */   long timeout;
/*     */   QuestionResponseInterface finishedFunction;
/*  39 */   private String response = new String();
/*  40 */   private Boolean responded = new Boolean(false);
/*     */   
/*     */ 
/*     */   public CivQuestionTask(Civilization askedciv, Civilization questionciv, String question, long timeout, QuestionResponseInterface finishedFunction)
/*     */   {
/*  45 */     this.askedCiv = askedciv;
/*  46 */     this.questionCiv = questionciv;
/*  47 */     this.question = question;
/*  48 */     this.timeout = timeout;
/*  49 */     this.finishedFunction = finishedFunction;
/*     */   }
/*     */   
/*     */   public void askPlayer(Player player)
/*     */   {
/*  54 */     CivMessage.send(player, "§7Request from: §b" + this.questionCiv.getName());
/*  55 */     CivMessage.send(player, this.question);
/*  56 */     CivMessage.send(player, "§7Respond by typing §b/civ dip respond yes§7 or §b/civ dip respond no");
/*     */   }
/*     */   
/*     */   public void notifyExpired(Player player) {
/*  60 */     CivMessage.send(player, "§7The offer from " + this.questionCiv.getName() + " expired.");
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*  66 */     for (Resident res : this.askedCiv.getLeaderGroup().getMemberList()) {
/*     */       try {
/*  68 */         askPlayer(CivGlobal.getPlayer(res));
/*     */       }
/*     */       catch (CivException localCivException) {}
/*     */     }
/*     */     
/*  73 */     for (Resident res : this.askedCiv.getAdviserGroup().getMemberList()) {
/*     */       try {
/*  75 */         askPlayer(CivGlobal.getPlayer(res));
/*     */       }
/*     */       catch (CivException localCivException1) {}
/*     */     }
/*     */     try
/*     */     {
/*  81 */       synchronized (this) {
/*  82 */         wait(this.timeout);
/*     */       }
/*     */     } catch (InterruptedException e) {
/*  85 */       cleanup();
/*  86 */       return;
/*     */     }
/*     */     
/*  89 */     if (this.responded.booleanValue()) {
/*  90 */       this.finishedFunction.processResponse(this.response);
/*  91 */       cleanup();
/*  92 */       return;
/*     */     }
/*     */     
/*  95 */     for (Resident res : this.askedCiv.getLeaderGroup().getMemberList()) {
/*     */       try {
/*  97 */         notifyExpired(CivGlobal.getPlayer(res));
/*     */       }
/*     */       catch (CivException localCivException2) {}
/*     */     }
/*     */     
/* 102 */     for (Resident res : this.askedCiv.getAdviserGroup().getMemberList()) {
/*     */       try {
/* 104 */         notifyExpired(CivGlobal.getPlayer(res));
/*     */       }
/*     */       catch (CivException localCivException3) {}
/*     */     }
/*     */     
/* 109 */     CivMessage.sendCiv(this.questionCiv, "§7" + this.askedCiv.getName() + " gave no response to our offer.");
/* 110 */     cleanup();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Boolean getResponded()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 35	com/avrgaming/civcraft/threading/tasks/CivQuestionTask:responded	Ljava/lang/Boolean;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 35	com/avrgaming/civcraft/threading/tasks/CivQuestionTask:responded	Ljava/lang/Boolean;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: athrow
/*     */     // Line number table:
/*     */     //   Java source line #114	-> byte code offset #0
/*     */     //   Java source line #115	-> byte code offset #7
/*     */     //   Java source line #114	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	17	0	this	CivQuestionTask
/*     */     //   5	10	1	Ljava/lang/Object;	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	16	14	finally
/*     */   }
/*     */   
/*     */   public void setResponded(Boolean response)
/*     */   {
/* 120 */     synchronized (this.responded) {
/* 121 */       this.responded = response;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String getResponse()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 28	com/avrgaming/civcraft/threading/tasks/CivQuestionTask:response	Ljava/lang/String;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 28	com/avrgaming/civcraft/threading/tasks/CivQuestionTask:response	Ljava/lang/String;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: athrow
/*     */     // Line number table:
/*     */     //   Java source line #126	-> byte code offset #0
/*     */     //   Java source line #127	-> byte code offset #7
/*     */     //   Java source line #126	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	17	0	this	CivQuestionTask
/*     */     //   5	10	1	Ljava/lang/Object;	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	16	14	finally
/*     */   }
/*     */   
/*     */   public void setResponse(String response)
/*     */   {
/* 132 */     synchronized (this.response) {
/* 133 */       setResponded(Boolean.valueOf(true));
/* 134 */       this.response = response;
/*     */     }
/*     */   }
/*     */   
/*     */   private void cleanup()
/*     */   {
/* 140 */     CivGlobal.removeRequest(this.askedCiv.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\CivQuestionTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */