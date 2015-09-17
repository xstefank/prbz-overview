/**
 * Sencha GXT 3.1.3 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package org.jboss.overview.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.state.client.CookieProvider;
import com.sencha.gxt.state.client.StateManager;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import org.jboss.overview.client.prbz.Prbz;
import org.jboss.overview.client.prbz.PrbzProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Overview implements IsWidget, EntryPoint {

    private Logger log = Logger.getLogger("");

//    private static final StockProperties props = GWT.create(StockProperties.class);
    private static final PrbzProperties props = GWT.create(PrbzProperties.class);

    private ContentPanel panel;

    @Override
    public Widget asWidget() {
        if (panel == null) {
            ColumnConfig<Prbz, String> nameCol = new ColumnConfig<>(props.name(), 50, SafeHtmlUtils.fromTrustedString("<b>Param name</b>"));
            ColumnConfig<Prbz, String> valueCol = new ColumnConfig<>(props.value(), 150, "Param Value");

            //set cells

            List<ColumnConfig<Prbz, ?>> columns = new ArrayList<>();
            columns.add(nameCol);
            columns.add(valueCol);

            ColumnModel<Prbz> columnModel = new ColumnModel<>(columns);

            List<Prbz> list = new ArrayList<>();
            list.add(new Prbz("name1", "value1"));
            list.add(new Prbz("name2", "value2"));
            list.add(new Prbz("name3", "value3"));
            list.add(new Prbz("name4", "value4"));

            ListStore<Prbz> store = new ListStore<>(props.key());
            store.addAll(list);

            Grid<Prbz> grid = new Grid<>(store, columnModel);


            VerticalLayoutContainer con = new VerticalLayoutContainer();
            con.add(grid, new VerticalLayoutData(1, 1));

            panel = new ContentPanel();
            panel.setHeadingText("Not so Basic Grid");
            panel.getHeader().setIcon(null);
            panel.addStyleName("margin-10");

            /*final Resizable resizable = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
            panel.addExpandHandler(new ExpandHandler() {
                @Override
                public void onExpand(ExpandEvent event) {
                    resizable.setEnabled(true);
                }
            });
            panel.addCollapseHandler(new CollapseHandler() {
                @Override
                public void onCollapse(CollapseEvent event) {
                    resizable.setEnabled(false);
                }
            });*/
            panel.setWidget(grid);

            /*ColumnConfig<Stock, String> branchCol = new ColumnConfig<Stock, String>(props.name(), 50, SafeHtmlUtils.fromTrustedString("<b>Branch</b>"));
            ColumnConfig<Stock, String> symbolCol = new ColumnConfig<Stock, String>(props.symbol(), 100, "Symbol");
            ColumnConfig<Stock, Double> lastCol = new ColumnConfig<Stock, Double>(props.last(), 75, "Last");
            ColumnConfig<Stock, Double> changeCol = new ColumnConfig<Stock, Double>(props.change(), 100, "Change");
            ColumnConfig<Stock, Date> lastTransCol = new ColumnConfig<Stock, Date>(props.lastTrans(), 100, "Last Updated");

            final NumberFormat number = NumberFormat.getFormat("0.00");
            changeCol.setCell(new AbstractCell<Double>() {
                @Override
                public void render(Context context, Double value, SafeHtmlBuilder sb) {
                    String style = "style='color: " + (value < 0 ? "red" : "green") + "'";
                    String v = number.format(value);
                    sb.appendHtmlConstant("<span " + style + " qtitle='Change' qtip='" + v + "'>" + v + "</span>");
                }
            });

            lastTransCol.setCell(new DateCell(DateTimeFormat.getFormat("MM/dd/yyyy")));

            List<ColumnConfig<Stock, ?>> columns = new ArrayList<ColumnConfig<Stock, ?>>();
            columns.add(nameCol);
            columns.add(symbolCol);
            columns.add(lastCol);
            columns.add(changeCol);
            columns.add(lastTransCol);

            ColumnModel<Stock> cm = new ColumnModel<Stock>(columns);

            ListStore<Stock> store = new ListStore<Stock>(props.key());

            List<Stock> testData = new ArrayList<Stock>();
            testData.add(new Stock("RedHat", 1.00, 1.00, 1.00, new Date(), "IT"));
            testData.add(new Stock("RedHat01", 1.00, 1.00, 1.00, new Date(), "IT"));
            store.addAll(testData);

            ToolTipConfig config = new ToolTipConfig("Example Info", "This examples includes resizable panel, reorderable columns and grid state. Text selection is allowed.");
            config.setMaxWidth(225);
            ToolButton info = new ToolButton(ToolButton.QUESTION);
            info.setToolTipConfig(config);

            final Grid<Stock> grid = new Grid<Stock>(store, cm);
            grid.setAllowTextSelection(true);
            grid.getView().setAutoExpandColumn(nameCol);
            grid.getView().setStripeRows(true);
            grid.getView().setColumnLines(true);
            grid.setBorders(false);
            grid.setColumnReordering(true);

            // Stage manager, turn on state management
            grid.setStateful(true);
            grid.setStateId("gridExample");

            // Stage manager, load previous state
            GridStateHandler<Stock> state = new GridStateHandler<Stock>(grid);
            state.loadState();

            SimpleComboBox<String> typeCombo = new SimpleComboBox<String>(new StringLabelProvider<String>());
            typeCombo.setTriggerAction(TriggerAction.ALL);
            typeCombo.setEditable(false);
            typeCombo.setWidth(100);
            typeCombo.add("Row");
            typeCombo.add("Cell");
            typeCombo.setValue("Row");
            // we want to change selection model on select, not value change which fires on blur
            typeCombo.addSelectionHandler(new SelectionHandler<String>() {
                @Override
                public void onSelection(SelectionEvent<String> event) {
                    boolean cell = event.getSelectedItem().equals("Cell");
                    if (cell) {
                        CellSelectionModel<Stock> c = new CellSelectionModel<Stock>();
                        c.addCellSelectionChangedHandler(new CellSelectionChangedHandler<Stock>() {
                            @Override
                            public void onCellSelectionChanged(CellSelectionChangedEvent<Stock> event) {
                            }
                        });
                        grid.setSelectionModel(c);
                    } else {
                        grid.setSelectionModel(new GridSelectionModel<Stock>());
                    }
                }
            });
            typeCombo.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                }
            });

            ToolBar toolBar = new ToolBar();
            toolBar.setEnableOverflow(false);
            toolBar.add(new LabelToolItem("Selection Mode: "));
            toolBar.add(typeCombo);

            VerticalLayoutContainer con = new VerticalLayoutContainer();
            con.add(toolBar, new VerticalLayoutData(1, -1));
            con.add(grid, new VerticalLayoutData(1, 1));

            panel = new ContentPanel();
            panel.setHeadingText("Basic Grid");
            panel.getHeader().setIcon(null);
            panel.setPixelSize(600, 300);
            panel.addStyleName("margin-10");
            panel.addTool(info);

            final Resizable resizable = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
            panel.addExpandHandler(new ExpandHandler() {
                @Override
                public void onExpand(ExpandEvent event) {
                    resizable.setEnabled(true);
                }
            });
            panel.addCollapseHandler(new CollapseHandler() {
                @Override
                public void onCollapse(CollapseEvent event) {
                    resizable.setEnabled(false);
                }
            });
            panel.setWidget(con);

            // Enables quicktips (qtitle for the heading and qtip for the
            // content) that are setup in the change GridCellRenderer
            new QuickTip(grid);*/
        }

        return panel;
    }

    @Override
    public void onModuleLoad() {
        // State manager, initialize the state options
        StateManager.get().setProvider(new CookieProvider("/", null, null, GXT.isSecure()));

        RootPanel.get().add(asWidget());

    }
}