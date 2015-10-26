package org.jboss.overview.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.jboss.overview.shared.Prbz;

import java.util.List;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
@RemoteServiceRelativePath("PullRequestService")
public interface PullRequestService extends RemoteService {

    List<Prbz> getDataList();

    List<String> getBranches();

    class Util {
        private static PullRequestServiceAsync instance;

        public static PullRequestServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(PullRequestService.class);
            }
            return instance;
        }
    }

}
