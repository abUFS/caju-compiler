/* This file was generated by SableCC (http://www.sablecc.org/). */

package caju.node;

import caju.analysis.*;

@SuppressWarnings("nls")
public final class AExp8 extends PExp8
{
    private PExp9 _exp9_;

    public AExp8()
    {
        // Constructor
    }

    public AExp8(
        @SuppressWarnings("hiding") PExp9 _exp9_)
    {
        // Constructor
        setExp9(_exp9_);

    }

    @Override
    public Object clone()
    {
        return new AExp8(
            cloneNode(this._exp9_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExp8(this);
    }

    public PExp9 getExp9()
    {
        return this._exp9_;
    }

    public void setExp9(PExp9 node)
    {
        if(this._exp9_ != null)
        {
            this._exp9_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._exp9_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._exp9_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._exp9_ == child)
        {
            this._exp9_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._exp9_ == oldChild)
        {
            setExp9((PExp9) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
