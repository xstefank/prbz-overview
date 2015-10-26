package org.jboss.overview.shared;

import java.io.Serializable;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public class PullRequestInfo implements Serializable {


    private static final long serialVersionUID = -8725023731973225198L;
    private Integer pullRequestId;
    private String url;

    public PullRequestInfo(Integer pullRequestId, String url) {
        this.pullRequestId = pullRequestId;
        this.url = url;
    }

    public PullRequestInfo() {
    }

    public Integer getPullRequestId() {
        return pullRequestId;
    }

    public void setPullRequestId(Integer pullRequestId) {
        this.pullRequestId = pullRequestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}