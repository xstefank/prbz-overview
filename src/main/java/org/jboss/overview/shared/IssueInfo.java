package org.jboss.overview.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public class IssueInfo implements Serializable {

    private static final long serialVersionUID = 8782181020149373198L;
    private String number;
    private String url;
    private String status;
    private String flags;

    public IssueInfo() {
    }

    public IssueInfo(String number, String url, String status, String flags) {
        this.number = number;
        this.url = url;
        this.status = status;
        this.flags = flags;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }
}
