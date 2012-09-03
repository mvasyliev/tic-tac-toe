package com.ticTacToeApp.client.resourses;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface ClientResourses extends ClientBundle {
    public static ClientResourses INSTANCE = GWT.create(ClientResourses.class);

    @Source("TicTacToeApp.css")
    public CssResource css();

    @Source("img/cross.gif")
    public ImageResource crossImage();

    @Source("img/circle.gif")
    public ImageResource circleImage();

}
