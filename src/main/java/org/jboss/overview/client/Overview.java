/**
 * Sencha GXT 3.1.3 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.jboss.overview.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.state.client.CookieProvider;
import com.sencha.gxt.state.client.StateManager;

import java.util.logging.Logger;

public class Overview implements EntryPoint {

    private Logger log = Logger.getLogger("");

    private RootPanel rootPanel = RootPanel.get();

    @Override
    public void onModuleLoad() {
        // State manager, initialize the state options
        StateManager.get().setProvider(new CookieProvider("/", null, null, GXT.isSecure()));

        //page intialization
        rootPanel.setStyleName("rootPanel");

        Label heading = new Label();
        heading.setText("Pull Request Bugzilla overview");
        heading.setStyleName("appHeading");

        rootPanel.add(heading);
        rootPanel.add(new OverviewTable());

//        RootPanel.get().add(asWidget());

    }
}