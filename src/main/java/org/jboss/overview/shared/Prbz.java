package org.jboss.overview.shared;

import java.io.Serializable;
import java.util.List;

/**
 * Class representing one row in overview table
 *
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public class Prbz implements Serializable {

    private static final long serialVersionUID = 3517381106431978474L;

    //columns
    private String branch;
    private PullRequestInfo pullRequestInfo;
    private String buildResult;
    private List<PullRequestInfo> upstreamPRs;
    private List<IssueInfo> issues;
    private String summary;
    private Boolean mergeable;
    private Boolean mergeRequest;
    private String pullState;


    public Prbz() {
    }

    public Prbz(String branch, PullRequestInfo pullRequestInfo, String buildResult, List<PullRequestInfo> upstreamPRs,
                List<IssueInfo> issues, String summary, Boolean mergeable,
                Boolean mergeRequest, String pullState) {
        this.branch = branch;
        this.pullRequestInfo = pullRequestInfo;
        this.buildResult = buildResult;
        this.upstreamPRs = upstreamPRs;
        this.issues = issues;
        this.summary = summary;
        this.mergeable = mergeable;
        this.mergeRequest = mergeRequest;
        this.pullState = pullState;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public PullRequestInfo getPullRequestInfo() {
        return pullRequestInfo;
    }

    public void setPullRequestInfo(PullRequestInfo pullRequestInfo) {
        this.pullRequestInfo = pullRequestInfo;
    }

    public String getBuildResult() {
        return buildResult;
    }

    public void setBuildResult(String buildResult) {
        this.buildResult = buildResult;
    }

    public List<PullRequestInfo> getUpstreamPRs() {
        return upstreamPRs;
    }

    public void setUpstreamPRs(List<PullRequestInfo> upstreamPRs) {
        this.upstreamPRs = upstreamPRs;
    }

    public List<IssueInfo> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueInfo> issues) {
        this.issues = issues;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getMergeable() {
        return mergeable;
    }

    public void setMergeable(Boolean mergeable) {
        this.mergeable = mergeable;
    }

    public Boolean getMergeRequest() {
        return mergeRequest;
    }

    public void setMergeRequest(Boolean mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    public String getPullState() {
        return pullState;
    }

    public void setPullState(String pullState) {
        this.pullState = pullState;
    }

}
