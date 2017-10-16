package org.pltw.examples.poptartinventory;

/**
 * Created by mrisk on 11/9/2016.
 */
public class PopTart {
    private String mName;
    private int mCount;
    private int mMinimum;
    private Boolean delete;

    public PopTart(String mName, int mCount, int mMinimum) {

        this.mName = mName;
        this.mCount = mCount;
        this.mMinimum = mMinimum;
        this.delete = false;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getMinimum() {
        return mMinimum;
    }

    public void setMinimum(int mMinimum) {
        this.mMinimum = mMinimum;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
}
