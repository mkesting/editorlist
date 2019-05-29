/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.github.mkesting.editorlist.EditorlistFilter.Pattern;

public class EditorlistFilterAction extends Action {

    private final Pattern pattern;
    private final EditorlistFilter filter;
    private final EditorlistViewer viewer;

    public EditorlistFilterAction(final EditorlistViewer viewer, final EditorlistFilter filter, final Pattern pattern) {
        this.viewer = viewer;
        this.filter = filter;
        this.pattern = pattern;

        setToolTipText(pattern.getToolTipText());
        setImageDescriptor(
                ImageDescriptor.createFromURL(EditorlistPlugin.getDefault().getImageURL(pattern.getActionIcon())));
        setChecked(Boolean.parseBoolean(EditorlistPlugin.getDefault().getDialogSettings().get("filter" + pattern)));

        if (isChecked()) {
            this.filter.addPattern(pattern);
        }
    }

    @Override
    public void run() {
        EditorlistPlugin.getDefault().getDialogSettings().put("filter" + pattern, isChecked());

        boolean filterChanged = false;

        if (isChecked()) {
            filterChanged = filter.addPattern(pattern);
        } else {
            filterChanged = filter.removePattern(pattern);
        }

        if (filterChanged) {
            viewer.refresh();
        }
    }

    public void uncheck() {
        setChecked(false);
        run();
    }
}
