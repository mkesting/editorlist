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

public class EditorlistCloseEditorAction extends Action {

    private final EditorlistView view;

    public EditorlistCloseEditorAction(final EditorlistView view) {
        this.view = view;

        setText("Close");
        setImageDescriptor(ImageDescriptor.createFromURL(EditorlistPlugin.getDefault().getImageURL("remove.png")));
    }

    @Override
    public void run() {
      view.closeSelectedEditors();
    }
}
