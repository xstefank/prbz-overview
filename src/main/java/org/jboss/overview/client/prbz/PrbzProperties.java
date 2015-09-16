package org.jboss.overview.client.prbz;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author Martin Stefanko (mstefank@redhat.com)
 */
public interface PrbzProperties extends PropertyAccess<Prbz> {

    @Editor.Path("name")
    ModelKeyProvider<Prbz> key();


//    LabelProvider<Prbz> value();

    ValueProvider<Prbz, String> name();

    ValueProvider<Prbz, String> value();
}
