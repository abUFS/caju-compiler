/* This file was generated by SableCC (http://www.sablecc.org/). */

package caju.node;

import caju.analysis.*;

@SuppressWarnings("nls")
public final class AMenosExp8 extends PExp8
{
    private PExp8 _exp8_;
    private TMenos _menos_;
    private PExp9 _exp9_;

    public AMenosExp8()
    {
        // Constructor
    }

    public AMenosExp8(
        @SuppressWarnings("hiding") PExp8 _exp8_,
        @SuppressWarnings("hiding") TMenos _menos_,
        @SuppressWarnings("hiding") PExp9 _exp9_)
    {
        // Constructor
        setExp8(_exp8_);

        setMenos(_menos_);

        setExp9(_exp9_);

    }

    @Override
    public Object clone()
    {
        return new AMenosExp8(
            cloneNode(this._exp8_),
            cloneNode(this._menos_),
            cloneNode(this._exp9_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMenosExp8(this);
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

    public TMenos getMenos()
    {
        return this._menos_;
    }

    public void setMenos(TMenos node)
    {
        if(this._menos_ != null)
        {
            this._menos_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._menos_ = node;
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
            + toString(this._exp8_)
            + toString(this._menos_)
            + toString(this._exp9_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._exp8_ == child)
        {
            this._exp8_ = null;
            return;
        }

        if(this._menos_ == child)
        {
            this._menos_ = null;
            return;
        }

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
        if(this._exp8_ == oldChild)
        {
            setExp8((PExp8) newChild);
            return;
        }

        if(this._menos_ == oldChild)
        {
            setMenos((TMenos) newChild);
            return;
        }

        if(this._exp9_ == oldChild)
        {
            setExp9((PExp9) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
