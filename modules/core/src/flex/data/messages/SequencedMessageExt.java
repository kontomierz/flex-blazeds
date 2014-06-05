package flex.data.messages;

import flex.messaging.io.ClassAlias;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;

public class SequencedMessageExt extends SequencedMessage
    implements Externalizable, ClassAlias
{
  private static final long serialVersionUID = 6681017124166275704L;
  public static final String CLASS_ALIAS = "DSQ";
  private SequencedMessage _message;

  public SequencedMessageExt()
  {
  }

  public SequencedMessageExt(SequencedMessage message)
  {
    this._message = message;
  }

  public String getAlias()
  {
    return "DSQ";
  }

  public void writeExternal(ObjectOutput output) throws IOException
  {
    if (this._message != null)
      this._message.writeExternal(output);
    else
      super.writeExternal(output);
  }
}