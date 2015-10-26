package org.jboss.overview.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 *
 *
 * icons: https://www.iconfinder.com/iconsets/softwaredemo
 */
public interface MyResources extends ClientBundle {
    MyResources INSTANCE =  GWT.create(MyResources.class);

    @Source("org/jboss/overview/client/icons/SUCCESS.png")
    @ImageResource.ImageOptions(width = 25)
    ImageResource success();

    @Source("org/jboss/overview/client/icons/FAILURE.png")
    @ImageResource.ImageOptions(width = 25)
    ImageResource failure();

    @Source("org/jboss/overview/client/icons/ABORTED.png")
    @ImageResource.ImageOptions(width = 25)
    ImageResource aborted();

    @Source("org/jboss/overview/client/icons/UNKNOWN.png")
    @ImageResource.ImageOptions(width = 25)
    ImageResource unknown();

    @Source("org/jboss/overview/client/icons/FLAG.png")
    @ImageResource.ImageOptions(width = 25)
    ImageResource flag();

    @Source("org/jboss/overview/client/icons/TRUE.png")
    @ImageResource.ImageOptions(width = 25)
    ImageResource trueIcon();

    @Source("org/jboss/overview/client/icons/FALSE.png")
    @ImageResource.ImageOptions(width = 25)
    ImageResource falseIcon();
}
