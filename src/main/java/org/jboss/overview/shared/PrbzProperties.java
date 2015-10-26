package org.jboss.overview.shared;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import org.jboss.pull.shared.connectors.RedhatPullRequest;

import java.util.List;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public interface PrbzProperties extends PropertyAccess<Prbz> {

    @Editor.Path("pullRequestInfo")
    ModelKeyProvider<Prbz> key();


//    LabelProvider<Prbz> value();

    ValueProvider<Prbz, String> branch();

    ValueProvider<Prbz, PullRequestInfo> pullRequestInfo();

    ValueProvider<Prbz, String> buildResult();

    ValueProvider<Prbz, List<PullRequestInfo>> upstreamPRs();

    ValueProvider<Prbz, List<IssueInfo>> issues();

    ValueProvider<Prbz, String> summary();

    ValueProvider<Prbz, Boolean> mergeable();

    ValueProvider<Prbz, Boolean> mergeRequest();

    ValueProvider<Prbz, String> pullState();
}
