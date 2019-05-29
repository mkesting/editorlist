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

public class EditorlistSortAction extends Action {

    private final EditorlistView view;

    public EditorlistSortAction(final EditorlistView view) {
        setImageDescriptor(
                ImageDescriptor.createFromURL(EditorlistPlugin.getDefault().getImageURL("alphab_sort_co.png")));
        setChecked(Boolean.parseBoolean(EditorlistPlugin.getDefault().getDialogSettings().get("sorted")));

        this.view = view;
        this.view.setSorted(isChecked());
    }

    @Override
    public void run() {
        EditorlistPlugin.getDefault().getDialogSettings().put("sorted", isChecked());
        view.setSorted(isChecked());
    }
}
