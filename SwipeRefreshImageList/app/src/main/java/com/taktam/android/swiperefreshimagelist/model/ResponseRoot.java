package com.taktam.android.swiperefreshimagelist.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "response")
public class ResponseRoot {

    @Element
    public Data data;

}
