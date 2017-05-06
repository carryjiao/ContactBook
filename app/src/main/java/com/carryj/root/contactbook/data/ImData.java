package com.carryj.root.contactbook.data;

import java.io.Serializable;

/**
 * Created by root on 17/5/6.
 */

public class ImData implements Serializable {

    private String im;
    private String imType;

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
}
