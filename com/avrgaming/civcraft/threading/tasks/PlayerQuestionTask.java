/*     */ package com.avrgaming.civcraft.threading.tasks;
/*     */ 
/*     */ import com.avrgaming.civcraft.main.CivGlobal;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import com.avrgaming.civcraft.questions.QuestionBaseTask;
/*     */ import com.avrgaming.civcraft.questions.QuestionResponseInterface;
/*     */ import com.avrgaming.civcraft.util.CivColor;
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
/*     */ 
/*     */ public class PlayerQuestionTask
/*     */   extends QuestionBaseTask
/*     */   implements Runnable
/*     */ {
/*     */   Player askedPlayer;
/*     */   Player questionPlayer;
/*     */   String question;
/*     */   long timeout;
/*     */   QuestionResponseInterface finishedFunction;
/*  39 */   protected String response = new String();
/*  40 */   protected Boolean responded = new Boolean(false);
/*     */   
/*     */ 
/*     */   public PlayerQuestionTask() {}
/*     */   
/*     */ 
/*     */   public PlayerQuestionTask(Player askedplayer, Player questionplayer, String question, long timeout, QuestionResponseInterface finishedFunction)
/*     */   {
/*  48 */     this.askedPlayer = askedplayer;
/*  49 */     this.questionPlayer = questionplayer;
/*  50 */     this.question = question;
/*  51 */     this.timeout = timeout;
/*  52 */     this.finishedFunction = finishedFunction;
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*  58 */     CivMessage.send(this.askedPlayer, "§7Question from: §b" + this.questionPlayer.getName());
/*  59 */     CivMessage.send(this.askedPlayer, "§d" + CivColor.BOLD + this.question);
/*  60 */     CivMessage.send(this.askedPlayer, "§7Respond by typing §b/accept§7 or §b/deny");
/*     */     try
/*     */     {
/*  63 */       synchronized (this) {
/*  64 */         wait(this.timeout);
/*     */       }
/*     */     } catch (InterruptedException e) {
/*  67 */       cleanup();
/*  68 */       return;
/*     */     }
/*     */     
/*  71 */     if (this.responded.booleanValue()) {
/*  72 */       this.finishedFunction.processResponse(this.response);
/*  73 */       cleanup();
/*  74 */       return;
/*     */     }
/*     */     
/*  77 */     CivMessage.send(this.askedPlayer, "§7You failed to respond to the question from " + this.questionPlayer.getName() + " in time.");
/*  78 */     CivMessage.send(this.questionPlayer, "§7" + this.askedPlayer.getName() + " failed to answer the question in time.");
/*  79 */     cleanup();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Boolean getResponded()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 34	com/avrgaming/civcraft/threading/tasks/PlayerQuestionTask:responded	Ljava/lang/Boolean;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 34	com/avrgaming/civcraft/threading/tasks/PlayerQuestionTask:responded	Ljava/lang/Boolean;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: athrow
/*     */     // Line number table:
/*     */     //   Java source line #83	-> byte code offset #0
/*     */     //   Java source line #84	-> byte code offset #7
/*     */     //   Java source line #83	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	17	0	this	PlayerQuestionTask
/*     */     //   5	10	1	Ljava/lang/Object;	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	16	14	finally
/*     */   }
/*     */   
/*     */   public void setResponded(Boolean response)
/*     */   {
/*  89 */     synchronized (this.responded) {
/*  90 */       this.responded = response;
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String getResponse()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 27	com/avrgaming/civcraft/threading/tasks/PlayerQuestionTask:response	Ljava/lang/String;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 27	com/avrgaming/civcraft/threading/tasks/PlayerQuestionTask:response	Ljava/lang/String;
/*     */     //   11: aload_1
/*     */     //   12: monitorexit
/*     */     //   13: areturn
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: athrow
/*     */     // Line number table:
/*     */     //   Java source line #95	-> byte code offset #0
/*     */     //   Java source line #96	-> byte code offset #7
/*     */     //   Java source line #95	-> byte code offset #14
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	17	0	this	PlayerQuestionTask
/*     */     //   5	10	1	Ljava/lang/Object;	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	13	14	finally
/*     */     //   14	16	14	finally
/*     */   }
/*     */   
/*     */   public void setResponse(String response)
/*     */   {
/* 101 */     synchronized (this.response) {
/* 102 */       setResponded(Boolean.valueOf(true));
/* 103 */       this.response = response;
/*     */     }
/*     */   }
/*     */   
/*     */   private void cleanup()
/*     */   {
/* 109 */     CivGlobal.removeQuestion(this.askedPlayer.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\PlayerQuestionTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */