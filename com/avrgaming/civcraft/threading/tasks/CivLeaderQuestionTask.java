/*    */ package com.avrgaming.civcraft.threading.tasks;
/*    */ 
/*    */ import com.avrgaming.civcraft.main.CivGlobal;
/*    */ import com.avrgaming.civcraft.main.CivMessage;
/*    */ import com.avrgaming.civcraft.object.Civilization;
/*    */ import com.avrgaming.civcraft.object.Resident;
/*    */ import com.avrgaming.civcraft.permission.PermissionGroup;
/*    */ import com.avrgaming.civcraft.questions.QuestionBaseTask;
/*    */ import com.avrgaming.civcraft.questions.QuestionResponseInterface;
/*    */ import com.avrgaming.civcraft.util.CivColor;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class CivLeaderQuestionTask
/*    */   extends QuestionBaseTask implements Runnable
/*    */ {
/*    */   Civilization askedCivilization;
/*    */   Player questionPlayer;
/*    */   String question;
/*    */   long timeout;
/*    */   QuestionResponseInterface finishedFunction;
/*    */   Resident responder;
/* 22 */   protected String response = new String();
/* 23 */   protected Boolean responded = new Boolean(false);
/*    */   
/*    */ 
/*    */ 
/*    */   public CivLeaderQuestionTask(Civilization askedplayer, Player questionplayer, String question, long timeout, QuestionResponseInterface finishedFunction)
/*    */   {
/* 29 */     this.askedCivilization = askedplayer;
/* 30 */     this.questionPlayer = questionplayer;
/* 31 */     this.question = question;
/* 32 */     this.timeout = timeout;
/* 33 */     this.finishedFunction = finishedFunction;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/* 40 */     for (Resident resident : this.askedCivilization.getLeaderGroup().getMemberList()) {
/* 41 */       CivMessage.send(resident, "§7Question from: §b" + this.questionPlayer.getName());
/* 42 */       CivMessage.send(resident, "§d" + CivColor.BOLD + this.question);
/* 43 */       CivMessage.send(resident, "§7Respond by typing §b/accept§7 or §b/deny");
/*    */     }
/*    */     try
/*    */     {
/* 47 */       synchronized (this) {
/* 48 */         wait(this.timeout);
/*    */       }
/*    */     } catch (InterruptedException e) {
/* 51 */       CivMessage.send(this.questionPlayer, "§7Task interrupted before a response could be given.");
/* 52 */       cleanup();
/* 53 */       return;
/*    */     }
/*    */     
/* 56 */     if (this.responded.booleanValue()) {
/* 57 */       this.finishedFunction.processResponse(this.response, this.responder);
/* 58 */       cleanup();
/* 59 */       return;
/*    */     }
/*    */     
/* 62 */     CivMessage.send(this.questionPlayer, "§7No Response from civilization leaders");
/* 63 */     cleanup();
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public Boolean getResponded()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 38	com/avrgaming/civcraft/threading/tasks/CivLeaderQuestionTask:responded	Ljava/lang/Boolean;
/*    */     //   4: dup
/*    */     //   5: astore_1
/*    */     //   6: monitorenter
/*    */     //   7: aload_0
/*    */     //   8: getfield 38	com/avrgaming/civcraft/threading/tasks/CivLeaderQuestionTask:responded	Ljava/lang/Boolean;
/*    */     //   11: aload_1
/*    */     //   12: monitorexit
/*    */     //   13: areturn
/*    */     //   14: aload_1
/*    */     //   15: monitorexit
/*    */     //   16: athrow
/*    */     // Line number table:
/*    */     //   Java source line #67	-> byte code offset #0
/*    */     //   Java source line #68	-> byte code offset #7
/*    */     //   Java source line #67	-> byte code offset #14
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	17	0	this	CivLeaderQuestionTask
/*    */     //   5	10	1	Ljava/lang/Object;	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	13	14	finally
/*    */     //   14	16	14	finally
/*    */   }
/*    */   
/*    */   public void setResponded(Boolean response)
/*    */   {
/* 73 */     synchronized (this.responded) {
/* 74 */       this.responded = response;
/*    */     }
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public String getResponse()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 31	com/avrgaming/civcraft/threading/tasks/CivLeaderQuestionTask:response	Ljava/lang/String;
/*    */     //   4: dup
/*    */     //   5: astore_1
/*    */     //   6: monitorenter
/*    */     //   7: aload_0
/*    */     //   8: getfield 31	com/avrgaming/civcraft/threading/tasks/CivLeaderQuestionTask:response	Ljava/lang/String;
/*    */     //   11: aload_1
/*    */     //   12: monitorexit
/*    */     //   13: areturn
/*    */     //   14: aload_1
/*    */     //   15: monitorexit
/*    */     //   16: athrow
/*    */     // Line number table:
/*    */     //   Java source line #79	-> byte code offset #0
/*    */     //   Java source line #80	-> byte code offset #7
/*    */     //   Java source line #79	-> byte code offset #14
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	17	0	this	CivLeaderQuestionTask
/*    */     //   5	10	1	Ljava/lang/Object;	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	13	14	finally
/*    */     //   14	16	14	finally
/*    */   }
/*    */   
/*    */   public void setResponse(String response)
/*    */   {
/* 85 */     synchronized (this.response) {
/* 86 */       setResponded(Boolean.valueOf(true));
/* 87 */       this.response = response;
/*    */     }
/*    */   }
/*    */   
/*    */   private void cleanup()
/*    */   {
/* 93 */     CivGlobal.removeQuestion("civ:" + this.askedCivilization.getName());
/*    */   }
/*    */   
/*    */   public void setResponder(Resident resident) {
/* 97 */     this.responder = resident;
/*    */   }
/*    */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\threading\tasks\CivLeaderQuestionTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */