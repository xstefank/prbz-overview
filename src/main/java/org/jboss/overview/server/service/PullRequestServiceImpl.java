package org.jboss.overview.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.web.bindery.requestfactory.shared.Service;
import org.infinispan.api.BasicCache;
import org.jboss.overview.client.service.PullRequestService;
import org.jboss.overview.shared.IssueInfo;
import org.jboss.overview.shared.Prbz;
import org.jboss.overview.server.OverviewData;
import org.jboss.overview.server.bean.DataTableScrollerBean;
import org.jboss.overview.server.bean.SingletonAider;
import org.jboss.overview.shared.PullRequestInfo;
import org.jboss.pull.shared.connectors.RedhatPullRequest;
import org.jboss.pull.shared.connectors.common.Flag;
import org.jboss.pull.shared.connectors.common.Issue;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
@Service(PullRequestService.class)
public class PullRequestServiceImpl extends RemoteServiceServlet implements PullRequestService {

    private static final long serialVersionUID = 8597946809878681737L;
    private Logger log = Logger.getLogger("");

    @EJB
    private DataTableScrollerBean dataTableScrollerBean;

    @EJB
    private SingletonAider singletonAider;


    @Override
    public List<Prbz> getDataList() {
        List<Prbz> dataList = new ArrayList<>();

//        dataList.add(new Prbz("name1", 1, "UNSTABLE", "123", "BZ1111111", "-", "someAcks", "summary", true, true, "INCOMPLETE"));
//        dataList.add(new Prbz("name2", 2, "STABLE", "456", "BZ2222222", "-", "someAcks", "summary", false, true, "INCOMPLETE"));
//        dataList.add(new Prbz("name3", 3, "FAILED", "789", "BZ3333333", "-", "someAcks", "summary", true, false, "INCOMPLETE"));
//        dataList.add(new Prbz("name4", 4, "UNSTABLE", "121", "BZ4444444", "-", "someAcks", "summary", true, false, "INCOMPLETE"));

        BasicCache<Integer, OverviewData> cache = singletonAider.getCache();
//          List<OverviewData> overviews = dataTableScrollerBean.getDataList();

        for (OverviewData overviewData : cache.values()) {

            RedhatPullRequest pullRequest = overviewData.getPullRequest();


            List<IssueInfo> issueInfos = new ArrayList<>();
            for (Issue issue : overviewData.getIssues()) {
                issueInfos.add(new IssueInfo(issue.getNumber(), issue.getUrl().toString(),
                        issue.getStatus(), parseFlags(issue.getFlags())));
            }

            String overallState = printList(overviewData.getOverallState());

            dataList.add(new Prbz(pullRequest.getTargetBranchTitle(),
                    new PullRequestInfo(pullRequest.getNumber(), pullRequest.getHtmlUrl()),
                    pullRequest.getBuildResult().toString(),
                    getUpstreamPRs(overviewData.getPullRequestUpStreams()), issueInfos,
                    overallState, overviewData.isMergeable(), overviewData.isReviewed(), pullRequest.getState()));
        }

        log.log(Level.SEVERE, "getDataList() called, " + cache.size());

        return dataList;
    }


    @Override
    public List<String> getBranches() {
        return dataTableScrollerBean.getAllBranches();
    }

    private String printList(List<?> list) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i) + System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    private List<PullRequestInfo> getUpstreamPRs(List<RedhatPullRequest> upstreamPRs) {

        List<PullRequestInfo> prInfoList = new ArrayList<>();

        for (RedhatPullRequest upstreamPR : upstreamPRs) {
            prInfoList.add(new PullRequestInfo(upstreamPR.getNumber(), upstreamPR.getHtmlUrl()));
        }

        return prInfoList;
    }

    private String parseFlags(List<Flag> flags) {
        StringBuilder sb = new StringBuilder();

        for (Flag flag : flags) {
            sb.append(flag.getName() + flag.getConvertedFlag() + System.lineSeparator());
        }

        return sb.toString();
    }
}
