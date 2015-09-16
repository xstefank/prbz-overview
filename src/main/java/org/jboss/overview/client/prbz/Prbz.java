package org.jboss.overview.client.prbz;

import java.io.Serializable;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public class Prbz implements Serializable {

    private static final long serialVersionUID = 3517381106431978474L;

    private String name;
    private String value;

    public Prbz() {
    }

    public Prbz(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
