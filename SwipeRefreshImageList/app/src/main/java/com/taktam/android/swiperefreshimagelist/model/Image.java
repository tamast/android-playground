package com.taktam.android.swiperefreshimagelist.model;

import org.simpleframework.xml.Element;

@Element
public class Image {

    @Element
    public String url;

    @Element
    public String id;

    @Element(name = "source_url")
    public String sourceUrl;
}
