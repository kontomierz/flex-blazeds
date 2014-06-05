package flex.data.messages;

import flex.messaging.io.PropertyProxy;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.messages.BatchableMessage;
import flex.messaging.messages.Message;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;

public class DataMessage extends AsyncMessage
    implements BatchableMessage
{
  private static final long serialVersionUID = -233562911865716601L;
  public static final String LOG_CATEGORY = "Message.Data";
  public static final int CREATE_OPERATION = 0;
  public static final int FILL_OPERATION = 1;
  public static final int GET_OPERATION = 2;
  public static final int UPDATE_OPERATION = 3;
  public static final int DELETE_OPERATION = 4;
  public static final int BATCHED_OPERATION = 5;
  public static final int MULTI_BATCH_OPERATION = 6;
  public static final int TRANSACTED_OPERATION = 7;
  public static final int PAGE_OPERATION = 8;
  public static final int COUNT_OPERATION = 9;
  public static final int GET_OR_CREATE_OPERATION = 10;
  public static final int CREATE_AND_SEQUENCE_OPERATION = 11;
  public static final int GET_SEQUENCE_ID_OPERATION = 12;
  public static final int FILLIDS_OPERATION = 15;
  public static final int REFRESH_FILL_OPERATION = 16;
  public static final int UPDATE_COLLECTION_OPERATION = 17;
  public static final int RELEASE_COLLECTION_OPERATION = 18;
  public static final int RELEASE_ITEM_OPERATION = 19;
  public static final int PAGE_ITEMS_OPERATION = 20;
  public static final int UPDATE_BODY_CHANGES = 0;
  public static final int UPDATE_BODY_PREV = 1;
  public static final int UPDATE_BODY_NEW = 2;
  public static final String DYNAMIC_SIZING_HEADER = "dynamicSizing";
  static final String[] operationNames = { "create", "fill", "get", "update", "delete", "batched", "multi_batch", "transacted", "page", "count", "get_or_create", "create_and_sequence", "get_sequence_id", "unused13", "unused14", "fillids", "refresh_fill", "update_collection", "release_collection", "release_item", "page_items" };
  private static final short IDENTITY_FLAG = 1;
  private static final short OPERATION_FLAG = 2;
  private Map identity;
  private int operation;

  public DataMessage()
  {
    this.timestamp = System.currentTimeMillis();
  }

  public Map getIdentity()
  {
    return this.identity;
  }

  public void setIdentity(Map identity)
  {
    this.identity = identity;
  }

  public int getOperation()
  {
    return this.operation;
  }

  public void setOperation(int operation)
  {
    this.operation = operation;
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
          this.identity = ((Map)input.readObject());
        }
        if ((flags & 0x2) != 0)
          this.operation = ((Number)input.readObject()).intValue();
        else {
          this.operation = 0;
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
    if (getClass() == DataMessage.class)
      return new DataMessageExt(this);
    return null;
  }

  public void writeExternal(ObjectOutput output)
      throws IOException
  {
    super.writeExternal(output);

    int flags = 0;

    if (this.identity != null) {
      flags |= 1;
    }
    if (this.operation != 0) {
      flags |= 2;
    }
    output.writeByte(flags);

    if (this.identity != null) {
      output.writeObject(this.identity);
    }
    if (this.operation != 0)
      output.writeObject(Integer.valueOf(this.operation));
  }

  protected String toStringFields(int indentLevel)
  {
    String s = super.toStringFields(indentLevel);
    String sep = getFieldSeparator(indentLevel);
    StringBuffer buf = new StringBuffer();
    buf.append(sep);
    buf.append("operation = ");
    buf.append(operationToString(this.operation));
    buf.append(sep);
    buf.append("id = ");
    buf.append(this.identity);
    buf.append(s);

    return buf.toString();
  }

  public Object unwrapBody()
  {
    Object body = super.getBody();
    if ((body instanceof PropertyProxy))
      return ((PropertyProxy)body).getDefaultInstance();
    return body;
  }

  public void setUpdateBody(int which, Object item)
  {
    if ((getBody() instanceof List))
      ((List)getBody()).set(which, item);
    else
      ((Object[])getBody())[which] = item;
  }

  public static String operationToString(int operation)
  {
    if ((operation < 0) || (operation > operationNames.length))
      return "invalid";
    return operationNames[operation];
  }

  public String logCategory()
  {
    return "Message.Data." + operationToString(this.operation);
  }

  public boolean isBatched()
  {
    return this.operation == 5;
  }

  public boolean isCreate()
  {
    return (this.operation == 0) || (this.operation == 11);
  }
}