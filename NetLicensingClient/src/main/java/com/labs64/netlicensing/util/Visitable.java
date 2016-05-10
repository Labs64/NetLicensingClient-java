package com.labs64.netlicensing.util;

public class Visitable {

    public void accept(final Visitor visitor) throws Exception {
        visitor.visit(this);
    }

}
