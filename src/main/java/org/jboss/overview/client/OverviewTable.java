package org.jboss.overview.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.theme.base.client.toolbar.SeparatorToolItemDefaultAppearance;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.jboss.overview.client.service.PullRequestService;
import org.jboss.overview.client.service.PullRequestServiceAsync;
import org.jboss.overview.shared.IssueInfo;
import org.jboss.overview.shared.Prbz;
import org.jboss.overview.shared.PrbzProperties;
import org.jboss.overview.shared.PullRequestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public class OverviewTable implements IsWidget {

    public static final HtmlTemplates TEMPLATES = GWT.create(HtmlTemplates.class);

    private Logger log = Logger.getLogger("");

    private ContentPanel panel;
    private Grid<Prbz> grid;
    private Label prCounter = new Label("0");

    private static final PrbzProperties props = GWT.create(PrbzProperties.class);

    private PullRequestServiceAsync pullRequestService = PullRequestService.Util.getInstance();


    @Override
    public Widget asWidget() {
        //TODO rewrite this
        final Timer timer = new Timer() {
            public void run() {
                loadGridData();
                schedule(1000 * 5);
            }
        };
        timer.schedule(1000 * 5);


        SafeHtmlBuilder buildResultHeader = new SafeHtmlBuilder();
        buildResultHeader.append(TEMPLATES.cell(makeImage(MyResources.INSTANCE.flag())));

        if (panel == null) {
            ColumnConfig<Prbz, String> branchCol = new ColumnConfig<>(props.branch(), 100, SafeHtmlUtils.fromTrustedString("<b>Branch</b>"));
            final ColumnConfig<Prbz, PullRequestInfo> pullRequestCol = new ColumnConfig<>(props.pullRequestInfo(), 130, "Pull Request");
            ColumnConfig<Prbz, String> buildResultCol = new ColumnConfig<>(props.buildResult(), 50, buildResultHeader.toSafeHtml());
            ColumnConfig<Prbz, List<PullRequestInfo>> upstreamPRCol = new ColumnConfig<>(props.upstreamPRs(), 150, "Upstream PRs");
            final ColumnConfig<Prbz, List<IssueInfo>> issueCol = new ColumnConfig<>(props.issues(), 150, "Issue");
            ColumnConfig<Prbz, List<IssueInfo>> issueStatusCol = new ColumnConfig<>(props.issues(), 150, "Issue status");
            ColumnConfig<Prbz, List<IssueInfo>> issueFlagsCol = new ColumnConfig<>(props.issues(), 150, "Issue flags");
            ColumnConfig<Prbz, String> summaryCol = new ColumnConfig<>(props.summary(), 250, "Summary");
            ColumnConfig<Prbz, Boolean> mergeableCol = new ColumnConfig<>(props.mergeable(), 100, "Mergeable");
            ColumnConfig<Prbz, Boolean> mergeReqCol = new ColumnConfig<>(props.mergeRequest(), 100, "Merge Req");
            ColumnConfig<Prbz, String> pullStateCol = new ColumnConfig<>(props.pullState(), 150, "Pull state");

            //set cells

            pullRequestCol.setCell(new AbstractCell<PullRequestInfo>() {
                @Override
                public void render(Context context, PullRequestInfo pullRequestInfo, SafeHtmlBuilder shb) {
                    shb.append(TEMPLATES.cell(SafeStylesUtils.fromTrustedString("cursor: pointer"),
                            TEMPLATES.pullRequest(pullRequestInfo.getPullRequestId(),
                                    UriUtils.fromTrustedString(pullRequestInfo.getUrl()))));
                }
            });


            buildResultCol.setCell(new AbstractCell<String>() {
                @Override
                public void render(Context context, String str, SafeHtmlBuilder shb) {

                    SafeHtml imageDiv;

                    switch (str) {
                        case "SUCCESS":
                            imageDiv = makeImage(MyResources.INSTANCE.success());
                            break;
                        case "FAILURE":
                            imageDiv = makeImage(MyResources.INSTANCE.failure());
                            break;
                        case "ABORTED":
                            imageDiv = makeImage(MyResources.INSTANCE.aborted());
                            break;
                        case "UNKNOWN":
                            imageDiv = makeImage(MyResources.INSTANCE.unknown());
                            break;
                        default:
                            imageDiv = SafeHtmlUtils.EMPTY_SAFE_HTML;
                    }

                    shb.append(TEMPLATES.cell(imageDiv));
                }
            });

            upstreamPRCol.setCell(new AbstractCell<List<PullRequestInfo>>() {
                @Override
                public void render(Context context, List<PullRequestInfo> data, SafeHtmlBuilder shb) {

                    SafeHtmlBuilder content = new SafeHtmlBuilder();

                    if (data.isEmpty()) {
                        content.appendHtmlConstant("No upstream required");
                    } else {
                        for (PullRequestInfo prInfo : data) {
                            content.appendHtmlConstant(TEMPLATES.pullRequest(prInfo.getPullRequestId(),
                                    UriUtils.fromTrustedString(prInfo.getUrl())).asString());
                        }
                    }

                    shb.append(TEMPLATES.cell("multiLineColumn", SafeStylesUtils.fromTrustedString("cursor: pointer"),
                            content.toSafeHtml()));
                }
            });


            issueCol.setCell(new AbstractCell<List<IssueInfo>>() {
                @Override
                public void render(Context context, List<IssueInfo> data, SafeHtmlBuilder shb) {

                    SafeHtmlBuilder content = new SafeHtmlBuilder();

                    if (data.isEmpty()) {
                        content.appendHtmlConstant("No issue provided");
                    } else {
                        for (IssueInfo issueInfo : data) {
                            content.appendHtmlConstant(TEMPLATES.bugzilla(issueInfo.getNumber(),
                                    UriUtils.fromTrustedString(issueInfo.getUrl())).asString());
                        }
                    }

                    shb.append(TEMPLATES.cell("multiLineColumn", SafeStylesUtils.fromTrustedString("cursor: pointer"),
                            content.toSafeHtml()));
                }
            });

            issueStatusCol.setCell(new AbstractCell<List<IssueInfo>>() {
                @Override
                public void render(Context context, List<IssueInfo> data, SafeHtmlBuilder shb) {

                    SafeHtmlBuilder content = new SafeHtmlBuilder();

                    if (data.isEmpty()) {
                        content.appendHtmlConstant("-");
                    } else {
                        for (IssueInfo issueInfo : data) {
                            content.appendHtmlConstant(TEMPLATES.issueStatus(issueInfo.getStatus()).asString());
                        }
                    }

                    shb.append(TEMPLATES.cell("multiLineColumn", SafeStylesUtils.fromTrustedString("cursor: pointer"),
                            content.toSafeHtml()));
                }
            });

            issueFlagsCol.setCell(new AbstractCell<List<IssueInfo>>() {
                @Override
                public void render(Context context, List<IssueInfo> data, SafeHtmlBuilder shb) {

                    SafeHtmlBuilder content = new SafeHtmlBuilder();

                    if (data.isEmpty()) {
                        content.appendHtmlConstant("-");
                    } else {
                        for (IssueInfo issueInfo : data) {
                            content.appendHtmlConstant(TEMPLATES.issueStatus(issueInfo.getFlags()).asString());
                        }
                    }

                    shb.append(TEMPLATES.cell("multiLineColumn", SafeStylesUtils.fromTrustedString("cursor: pointer"),
                            content.toSafeHtml()));
                }
            });

            summaryCol.setCell(new AbstractCell<String>() {
                @Override
                public void render(Context context, String s, SafeHtmlBuilder shb) {
                    shb.append(TEMPLATES.cell("multiLineColumn", SafeStylesUtils.fromTrustedString("cursor: pointer"),
                            SafeHtmlUtils.fromTrustedString(s.isEmpty() ? "-" : s)));
                }
            });

            mergeableCol.setCell(new AbstractCell<Boolean>() {
                @Override
                public void render(Context context, Boolean aBoolean, SafeHtmlBuilder shb) {
                    addBooleanIcon(aBoolean, shb);
                }
            });

            mergeReqCol.setCell(new AbstractCell<Boolean>() {
                @Override
                public void render(Context context, Boolean aBoolean, SafeHtmlBuilder shb) {
                    addBooleanIcon(aBoolean, shb);
                }
            });

            List<ColumnConfig<Prbz, ?>> columns = new ArrayList<>();
            columns.add(branchCol);
            columns.add(pullRequestCol);
            columns.add(buildResultCol);
            columns.add(upstreamPRCol);
            columns.add(issueCol);
            columns.add(issueStatusCol);
            columns.add(issueFlagsCol);
            columns.add(summaryCol);
            columns.add(mergeableCol);
            columns.add(mergeReqCol);
            columns.add(pullStateCol);

            /*align content in the middle*/
            for (ColumnConfig<Prbz, ?> column : columns) {
                column.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            }

            ColumnModel<Prbz> columnModel = new ColumnModel<>(columns);

            ListStore<Prbz> store = new ListStore<>(props.key());

            grid = new Grid<>(store, columnModel);

            loadGridData();

            grid.setView(new CustomGxtGridView<Prbz>());

            grid.setBorders(false);
            grid.getView().setStripeRows(true);

            grid.getView().setForceFit(true);

            grid.getStore().setEnableFilters(true);


            //branch selection
            final StringComboBox typeCombo = new StringComboBox();
            typeCombo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
            typeCombo.setEditable(false);
            typeCombo.setWidth(100);


            pullRequestService.getBranches(new AsyncCallback<List<String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(List<String> branches) {
                    typeCombo.add(branches);
                    typeCombo.setValue(branches.get(0));
                }
            });

            typeCombo.addSelectionHandler(new SelectionHandler<String>() {
                @Override
                public void onSelection(final SelectionEvent<String> selectionEvent) {
                    grid.getStore().removeFilters();

                    if (!selectionEvent.getSelectedItem().equals("All branches")) {

                        grid.getStore().addFilter(new Store.StoreFilter<Prbz>() {
                            @Override
                            public boolean select(Store<Prbz> store, Prbz prbz, Prbz m1) {
                                return m1.getBranch().equals(selectionEvent.getSelectedItem());
                            }
                        });
                    }
                }
            });

            ToolBar toolBar = new ToolBar();
            toolBar.setEnableOverflow(false);
            LabelToolItem lti = new LabelToolItem("Branch: ");
            toolBar.add(lti);
            toolBar.add(typeCombo);

            toolBar.add(new SeparatorToolItem(new SeparatorToolItemDefaultAppearance()));
            toolBar.add(new LabelToolItem("All pull requests: "));
            toolBar.add(prCounter);

            VerticalLayoutContainer con = new VerticalLayoutContainer();
            con.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            con.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, -1));

            panel = new ContentPanel();
            panel.add(con);
            panel.setStyleName("dataTablePanel");
        }

        return panel;
    }

    private void addBooleanIcon(Boolean aBoolean, SafeHtmlBuilder shb) {
        if (aBoolean) {
            shb.append(TEMPLATES.cell(makeImage(MyResources.INSTANCE.trueIcon())));
        } else {
            shb.append(TEMPLATES.cell(makeImage(MyResources.INSTANCE.falseIcon())));
        }
    }

    private void loadGridData() {
        log.log(Level.SEVERE, "loadGridData()");
        pullRequestService.getDataList(new AsyncCallback<List<Prbz>>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(List<Prbz> dataList) {
                grid.getStore().replaceAll(dataList);
                prCounter.setText(String.valueOf(dataList.size()));
            }
        });
    }


    /**
     * GridView that enables the scrollbar to be hidden
     *
     * @param <M> type of content
     */
    private class CustomGxtGridView<M> extends GridView<M> {

        private int SCROLL_OFFSET = 1;

        public CustomGxtGridView() {
            super();
            this.scrollOffset = SCROLL_OFFSET;
            this.vbar = false;
            this.setAdjustForHScroll(false);
        }

        @Override
        protected int getScrollAdjust() {
            return -10;
        }
    }

    /**
     * Make icons available as SafeHtml
     *
     * @param resource image
     * @return SafeHtml instance containing image
     */
    private static SafeHtml makeImage(ImageResource resource) {
        AbstractImagePrototype proto = AbstractImagePrototype.create(resource);
        return proto.getSafeHtml();
    }


}
