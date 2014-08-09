package com.taktam.android.swiperefreshimagelist.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

@Element
public class Data {

    @ElementList
    public List<Image> images;
}
