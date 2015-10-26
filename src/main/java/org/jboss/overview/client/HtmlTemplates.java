package org.jboss.overview.client;

import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public interface HtmlTemplates extends SafeHtmlTemplates {

    @Template("<div>{0}</div>")
    SafeHtml cell(SafeHtml value);

    @Template("<div style=\"{0}\">{1}</div>")
    SafeHtml cell(SafeStyles style, SafeHtml value);

    @Template("<div class=\"{0}\" style=\"{1}\">{2}</div>")
    SafeHtml cell(String className, SafeStyles style, SafeHtml value);

    @Template("<a href=\"{1}\" target=\"blank\" >#{0}</a>")
    SafeHtml pullRequest(Integer number, SafeUri url);

    @Template("<a href=\"{1}\" target=\"blank\" >BZ{0}</a> <br />")
    SafeHtml bugzilla(String number, SafeUri url);

    @Template("{0} <br />")
    SafeHtml issueStatus(String status);

}
