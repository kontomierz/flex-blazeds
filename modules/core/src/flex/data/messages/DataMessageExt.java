package flex.data.messages;

import flex.messaging.io.ClassAlias;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;

public class DataMessageExt extends DataMessage
    implements Externalizable, ClassAlias
{
  private static final long serialVersionUID = 5751140938289428053L;
  public static final String CLASS_ALIAS = "DSD";
  private DataMessage _message;

  public DataMessageExt()
  {
  }

  public DataMessageExt(DataMessage message)
  {
    this._message = message;
  }

  public String getAlias()
  {
    return "DSD";
  }

  public void writeExternal(ObjectOutput output) throws IOException
  {
    if (this._message != null)
      this._message.writeExternal(output);
    else
      super.writeExternal(output);
  }
}