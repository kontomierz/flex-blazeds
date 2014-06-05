package flex.data.messages;

import flex.messaging.messages.AcknowledgeMessage;
import flex.messaging.messages.Message;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SequencedMessage extends AcknowledgeMessage
{
  private static final long serialVersionUID = -3003621477974161629L;
  private static short DATA_MESSAGE_FLAG = 1;
  private static short DYNAMIC_SIZING_FLAG = 2;
  private static short SEQUENCE_ID_FLAG = 4;
  private static short SEQUENCE_PROXIES_FLAG = 8;
  private static short SEQUENCE_SIZE_FLAG = 16;
  private int sequenceId;
  private Object[] sequenceProxies;
  private int sequenceSize;
  private DataMessage dataMessage;
  private int dynamicSizing = 0;

  public int getDynamicSizing()
  {
    return this.dynamicSizing;
  }

  public void setDynamicSizing(int size)
  {
    this.dynamicSizing = size;
  }

  public int getSequenceId()
  {
    return this.sequenceId;
  }

  public void setSequenceId(int sequenceId)
  {
    this.sequenceId = sequenceId;
  }

  public void setSequenceProxies(Object[] proxies)
  {
    this.sequenceProxies = proxies;
  }

  public Object[] getSequenceProxies() {
    return this.sequenceProxies;
  }

  public int getSequenceSize()
  {
    return this.sequenceSize;
  }

  public void setSequenceSize(int sequenceSize)
  {
    this.sequenceSize = sequenceSize;
  }

  public DataMessage getDataMessage()
  {
    return this.dataMessage;
  }

  public void setDataMessage(DataMessage msg) {
    this.dataMessage = msg;
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
        if ((flags & DATA_MESSAGE_FLAG) != 0) {
          this.dataMessage = ((DataMessage)input.readObject());
        }
        if ((flags & DYNAMIC_SIZING_FLAG) != 0) {
          this.dynamicSizing = ((Number)input.readObject()).intValue();
        }
        if ((flags & SEQUENCE_ID_FLAG) != 0) {
          this.sequenceId = ((Number)input.readObject()).intValue();
        }
        if ((flags & SEQUENCE_PROXIES_FLAG) != 0) {
          this.sequenceProxies = ((Object[])input.readObject());
        }
        if ((flags & SEQUENCE_SIZE_FLAG) != 0) {
          this.sequenceSize = ((Number)input.readObject()).intValue();
        }
        reservedPosition = 5;
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
    if (getClass() == SequencedMessage.class)
      return new SequencedMessageExt(this);
    return null;
  }

  public void writeExternal(ObjectOutput output)
      throws IOException
  {
    super.writeExternal(output);

    int flags = 0;

    if (this.dataMessage != null) {
      flags |= DATA_MESSAGE_FLAG;
    }
    if (this.dynamicSizing != 0) {
      flags |= DYNAMIC_SIZING_FLAG;
    }
    if (this.sequenceId != 0) {
      flags |= SEQUENCE_ID_FLAG;
    }
    if (this.sequenceProxies != null) {
      flags |= SEQUENCE_PROXIES_FLAG;
    }
    if (this.sequenceSize != 0) {
      flags |= SEQUENCE_SIZE_FLAG;
    }
    output.writeByte(flags);

    if (this.dataMessage != null) {
      output.writeObject(this.dataMessage);
    }
    if (this.dynamicSizing != 0) {
      output.writeObject(Integer.valueOf(this.dynamicSizing));
    }
    if (this.sequenceId != 0) {
      output.writeObject(Integer.valueOf(this.sequenceId));
    }
    if (this.sequenceProxies != null) {
      output.writeObject(this.sequenceProxies);
    }
    if (this.sequenceSize != 0)
      output.writeObject(Integer.valueOf(this.sequenceSize));
  }

  protected String toStringFields(int indentLevel)
  {
    String sep = getFieldSeparator(indentLevel);
    String sepplus = getFieldSeparator(indentLevel + 1);
    StringBuffer sb = new StringBuffer();
    sb.append(sep).append("sequenceId = ").append(this.sequenceId);
    sb.append(sep).append("sequenceSize = ").append(this.sequenceSize);
    sb.append(sep).append("dynamicSizing = ").append(this.dynamicSizing);

    if ((this.sequenceProxies == null) || (this.sequenceProxies.length == 0)) {
      sb.append(sep).append("(no sequence proxies)");
    }
    else {
      sb.append(sep).append(this.sequenceProxies.length).append(" sequenceProxies ");
      for (int i = 0; i < this.sequenceProxies.length; i++)
        sb.append(sepplus).append("[").append(i).append("] = ").append(bodyToString(this.sequenceProxies[i], indentLevel + 1));
    }
    if (this.dataMessage != null)
      sb.append(sep).append("dataMessage = ").append(this.dataMessage.toString(indentLevel + 1));
    sb.append(super.toStringFields(indentLevel));
    return sb.toString();
  }
}