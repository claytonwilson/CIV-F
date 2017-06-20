package com.avrgaming.civcraft.event;

import com.avrgaming.civcraft.exception.InvalidConfiguration;
import java.util.Calendar;

public abstract interface EventInterface
{
  public abstract void process();
  
  public abstract Calendar getNextDate()
    throws InvalidConfiguration;
}


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\event\EventInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */