/* This file was generated by SableCC (http://www.sablecc.org/). */

package caju.node;

import caju.analysis.*;

@SuppressWarnings("nls")
public final class TBaseNumero extends Token
{
    public TBaseNumero()
    {
        super.setText("numero");
    }

    public TBaseNumero(int line, int pos)
    {
        super.setText("numero");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TBaseNumero(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTBaseNumero(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TBaseNumero text.");
    }
}