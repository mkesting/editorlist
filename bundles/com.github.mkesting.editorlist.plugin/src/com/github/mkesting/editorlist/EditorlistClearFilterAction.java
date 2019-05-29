package com.github.mkesting.editorlist;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class EditorlistClearFilterAction extends Action {

    private final EditorlistViewer viewer;
    private final EditorlistFilter filter;
    private final EditorlistFilterAction[] allFilters;

    public EditorlistClearFilterAction(final EditorlistViewer viewer, final EditorlistFilter filter,
            final EditorlistFilterAction[] allFilters) {
        super("Clear filter", Action.AS_PUSH_BUTTON);

        this.filter = filter;
        this.viewer = viewer;
        this.allFilters = allFilters;

        setImageDescriptor(
                ImageDescriptor.createFromURL(EditorlistPlugin.getDefault().getImageURL("clear_co.png")));
    }

    @Override
    public void run() {
        filter.clear();
        viewer.refresh();

        for (final EditorlistFilterAction editorlistFilterAction : allFilters) {
            editorlistFilterAction.uncheck();
        }
    }
}
