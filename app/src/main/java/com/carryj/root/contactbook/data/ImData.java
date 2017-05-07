package com.carryj.root.contactbook.data;

import java.io.Serializable;

/**
 * Created by root on 17/5/6.
 */

public class ImData implements Serializable {

    private String im;
    private String imType;
    private String _id;

    public ImData(){

    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getImType() {
        return imType;
    }

    public void setImType(String imType) {
        this.imType = imType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
