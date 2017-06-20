/*     */ package com.avrgaming.civcraft.command.admin;
/*     */ 
/*     */ import com.avrgaming.civcraft.command.CommandBase;
/*     */ import com.avrgaming.civcraft.event.EventInterface;
/*     */ import com.avrgaming.civcraft.event.EventTimer;
/*     */ import com.avrgaming.civcraft.exception.CivException;
/*     */ import com.avrgaming.civcraft.exception.InvalidConfiguration;
/*     */ import com.avrgaming.civcraft.main.CivMessage;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
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
/*     */ public class AdminTimerCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public void init()
/*     */   {
/*  35 */     this.command = "/ad timer";
/*  36 */     this.displayName = "Admin Timer";
/*     */     
/*  38 */     this.commands.put("set", "[name] [date] DAY:MONTH:YEAR:HOUR:MIN (24 hour time)");
/*  39 */     this.commands.put("run", "[name] Runs this timer event.");
/*     */   }
/*     */   
/*     */   public void run_cmd() throws CivException
/*     */   {
/*  44 */     if (this.args.length < 2) {
/*  45 */       throw new CivException("Enter a timer name");
/*     */     }
/*     */     
/*  48 */     EventTimer timer = (EventTimer)EventTimer.timers.get(this.args[1]);
/*  49 */     if (timer == null) {
/*  50 */       throw new CivException("No timer named " + this.args[1]);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  55 */       next = timer.getEventFunction().getNextDate();
/*     */     } catch (InvalidConfiguration e) { Calendar next;
/*  57 */       e.printStackTrace();
/*  58 */       throw new CivException("Invalid configuration, cant run timer.");
/*     */     }
/*     */     Calendar next;
/*  61 */     timer.getEventFunction().process();
/*  62 */     timer.setLast(EventTimer.getCalendarInServerTimeZone());
/*  63 */     timer.setNext(next);
/*  64 */     timer.save();
/*     */     
/*  66 */     CivMessage.sendSuccess(this.sender, "Timer Ran");
/*     */   }
/*     */   
/*     */   public void set_cmd() throws CivException {
/*  70 */     if (this.args.length < 3) {
/*  71 */       throw new CivException("Enter a timer name and date like DAY:MONTH:YEAR:HOUR:MIN");
/*     */     }
/*     */     
/*  74 */     String timerName = this.args[1];
/*  75 */     EventTimer timer = (EventTimer)EventTimer.timers.get(timerName);
/*  76 */     if (timer == null) {
/*  77 */       throw new CivException("No timer called: " + this.args[1]);
/*     */     }
/*     */     
/*  80 */     String dateStr = this.args[2];
/*  81 */     SimpleDateFormat parser = new SimpleDateFormat("d:M:y:H:m");
/*     */     
/*  83 */     Calendar next = EventTimer.getCalendarInServerTimeZone();
/*     */     try {
/*  85 */       next.setTime(parser.parse(dateStr));
/*  86 */       timer.setNext(next);
/*  87 */       timer.save();
/*  88 */       CivMessage.sendSuccess(this.sender, "Set timer " + timer.getName() + " to " + parser.format(next));
/*     */     } catch (ParseException e) {
/*  90 */       throw new CivException("Couldnt parse " + this.args[2] + " into a date, use format: DAY:MONTH:YEAR:HOUR:MIN");
/*     */     }
/*     */   }
/*     */   
/*     */   public void doDefaultAction()
/*     */     throws CivException
/*     */   {
/*  97 */     showHelp();
/*     */   }
/*     */   
/*     */   public void showHelp()
/*     */   {
/* 102 */     showBasicHelp();
/*     */   }
/*     */   
/*     */   public void permissionCheck()
/*     */     throws CivException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\command\admin\AdminTimerCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */