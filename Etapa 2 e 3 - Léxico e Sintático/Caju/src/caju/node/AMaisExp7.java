/* This file was generated by SableCC (http://www.sablecc.org/). */

package caju.node;

import caju.analysis.*;

@SuppressWarnings("nls")
public final class AMaisExp7 extends PExp7
{
    private PExp7 _exp7_;
    private TMais _mais_;
    private PExp8 _exp8_;

    public AMaisExp7()
    {
        // Constructor
    }

    public AMaisExp7(
        @SuppressWarnings("hiding") PExp7 _exp7_,
        @SuppressWarnings("hiding") TMais _mais_,
        @SuppressWarnings("hiding") PExp8 _exp8_)
    {
        // Constructor
        setExp7(_exp7_);

        setMais(_mais_);

        setExp8(_exp8_);

    }

    @Override
    public Object clone()
    {
        return new AMaisExp7(
            cloneNode(this._exp7_),
            cloneNode(this._mais_),
            cloneNode(this._exp8_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMaisExp7(this);
    }

    public PExp7 getExp7()
    {
        return this._exp7_;
    }

    public void setExp7(PExp7 node)
    {
        if(this._exp7_ != null)
        {
            this._exp7_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._exp7_ = node;
    }

    public TMais getMais()
    {
        return this._mais_;
    }

    public void setMais(TMais node)
    {
        if(this._mais_ != null)
        {
            this._mais_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._mais_ = node;
    }

    public PExp8 getExp8()
    {
        return this._exp8_;
    }

    public void setExp8(PExp8 node)
    {
        if(this._exp8_ != null)
        {
            this._exp8_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._exp8_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._exp7_)
            + toString(this._mais_)
            + toString(this._exp8_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._exp7_ == child)
        {
            this._exp7_ = null;
            return;
        }

        if(this._mais_ == child)
        {
            this._mais_ = null;
            return;
        }

        if(this._exp8_ == child)
        {
            this._exp8_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._exp7_ == oldChild)
        {
            setExp7((PExp7) newChild);
            return;
        }

        if(this._mais_ == oldChild)
        {
            setMais((TMais) newChild);
            return;
        }

        if(this._exp8_ == oldChild)
        {
            setExp8((PExp8) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
