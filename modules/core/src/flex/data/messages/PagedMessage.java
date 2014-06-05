package flex.data.messages;

import flex.messaging.messages.Message;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class PagedMessage extends SequencedMessage
{
  private static final long serialVersionUID = 7965904230365674840L;
  private static final short PAGE_COUNT_FLAG = 1;
  private static final short PAGE_INDEX_FLAG = 2;
  private int pageCount;
  private int pageIndex;

  public int getPageCount()
  {
    return this.pageCount;
  }

  public void setPageCount(int pageCount)
  {
    this.pageCount = pageCount;
  }

  public int getPageIndex()
  {
    return this.pageIndex;
  }

  public void setPageIndex(int pageIndex)
  {
    this.pageIndex = pageIndex;
  }

  public void readExternal(ObjectInput input)
      throws IOException, ClassNotFoundException
  {
    super.readExternal(input);

    short[] flagsArray = readFlags(input);
    for (int i = 0; i < flagsArray.length; i++)
    {
      short flags = flagsArray[i];
      short reservedPosition = 0;

      if (i == 0)
      {
        if ((flags & 0x1) != 0) {
          this.pageCount = ((Number)input.readObject()).intValue();
        }
        if ((flags & 0x2) != 0) {
          this.pageIndex = ((Number)input.readObject()).intValue();
        }
        reservedPosition = 2;
      }

      if (flags >> reservedPosition != 0)
      {
        for (short j = reservedPosition; j < 6; j = (short)(j + 1))
        {
          if ((flags >> j & 0x1) != 0)
          {
            input.readObject();
          }
        }
      }
    }
  }

  public Message getSmallMessage()
  {
    if (getClass() == PagedMessage.class)
      return new PagedMessageExt(this);
    return null;
  }

  public void writeExternal(ObjectOutput output)
      throws IOException
  {
    super.writeExternal(output);

    int flags = 0;

    if (this.pageCount != 0) {
      flags |= 1;
    }
    if (this.pageIndex != 0) {
      flags |= 2;
    }
    output.writeByte(flags);

    if (this.pageCount != 0) {
      output.writeObject(Integer.valueOf(this.pageCount));
    }
    if (this.pageIndex != 0)
      output.writeObject(Integer.valueOf(this.pageIndex));
  }
}