package com.github.mkesting.editorlist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class EditorlistCloseFilteredEditorsAction extends Action {

    private final EditorlistView view;

    public EditorlistCloseFilteredEditorsAction(final EditorlistView view) {
        this.view = view;

        setText("Close All");
        setImageDescriptor(ImageDescriptor.createFromURL(EditorlistPlugin.getDefault().getImageURL("remove_all.png")));
    }

    @Override
    public void run() {
      view.closeFilteredEditors();
    }
}
