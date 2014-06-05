package flex.data.messages;

import flex.messaging.io.ClassAlias;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;

public class PagedMessageExt extends PagedMessage
    implements Externalizable, ClassAlias
{
  private static final long serialVersionUID = 1682118816581839523L;
  public static final String CLASS_ALIAS = "DSP";
  private PagedMessage _message;

  public PagedMessageExt()
  {
  }

  public PagedMessageExt(PagedMessage message)
  {
    this._message = message;
  }

  public String getAlias()
  {
    return "DSP";
  }

  public void writeExternal(ObjectOutput output) throws IOException
  {
    if (this._message != null)
      this._message.writeExternal(output);
    else
      super.writeExternal(output);
  }
}