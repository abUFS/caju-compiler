/* This file was generated by SableCC (http://www.sablecc.org/). */

package caju.node;

import caju.analysis.*;

@SuppressWarnings("nls")
public final class TParaCada extends Token
{
    public TParaCada()
    {
        super.setText("para cada");
    }

    public TParaCada(int line, int pos)
    {
        super.setText("para cada");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TParaCada(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTParaCada(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TParaCada text.");
    }
}
