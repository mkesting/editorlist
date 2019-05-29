/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

public class EditorlistListener implements IPartListener, IPageListener, IPropertyListener, ISelectionChangedListener {

    private final EditorlistView editorlistView;

    public EditorlistListener(final EditorlistView editorlistView) {
        this.editorlistView = editorlistView;
    }

    @Override
    public void partActivated(final IWorkbenchPart part) {
        if (part instanceof IEditorPart) {
            editorlistView.selectEditorInViewer((IEditorPart) part);
        }
    }

    @Override
    public void partBroughtToTop(final IWorkbenchPart part) {
        if (part instanceof IEditorPart) {
            editorlistView.selectEditorInViewer((IEditorPart) part);
        }
    }

    @Override
    public void partDeactivated(final IWorkbenchPart part) {
        // empty
    }

    @Override
    public void partOpened(final IWorkbenchPart part) {
        if (part instanceof IEditorPart) {
            editorlistView.addEditorToViewer((IEditorPart) part);
        } else if (part == editorlistView) {
            editorlistView.refreshContents();
        }
    }

    @Override
    public void partClosed(final IWorkbenchPart part) {
        if (part instanceof IEditorPart) {
            editorlistView.removeEditorFromViewer((IEditorPart) part);
        }
    }

    @Override
    public void pageOpened(final IWorkbenchPage page) {
        editorlistView.refreshContents();
    }

    @Override
    public void pageActivated(final IWorkbenchPage page) {
        editorlistView.refreshContents();
    }

    @Override
    public void pageClosed(final IWorkbenchPage page) {
        // empty
    }

    @Override
    public void propertyChanged(final Object source, final int propId) {
        if (source instanceof IEditorPart && propId == IWorkbenchPart.PROP_TITLE) {
            Display.getDefault().asyncExec(() -> editorlistView.refreshLabel((IEditorPart) source));
        }
    }

    @Override
    public void selectionChanged(final SelectionChangedEvent event) {
        editorlistView.activateSelectedEditor();
    }
}
