package com.avrgaming.civcraft.questions;

import com.avrgaming.civcraft.object.Resident;

public abstract interface QuestionResponseInterface
{
  public abstract void processResponse(String paramString);
  
  public abstract void processResponse(String paramString, Resident paramResident);
}


/* Location:              C:\Users\Adrian\Desktop\CivCraft1.1.jar!\com\avrgaming\civcraft\questions\QuestionResponseInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */