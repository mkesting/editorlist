/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorReference;

public class EditorlistViewer implements ISelectionProvider {

    private TableViewer viewer;

    private final ViewerFilter viewerFilter;
    private final ViewerComparator viewerComparator;

    private final EditorlistLabelProvider labelProvider;
    private final EditorlistContentProvider contentProvider;

    public EditorlistViewer(final Composite parent, final int style, final ViewerFilter viewerFilter) {
        this.viewerFilter = viewerFilter;
        this.labelProvider = new EditorlistLabelProvider();
        this.contentProvider = new EditorlistContentProvider();
        this.viewerComparator = new ViewerComparator(Collator.getInstance());

        this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.viewer.setContentProvider(contentProvider);
        this.viewer.setLabelProvider(labelProvider);
        this.viewer.addFilter(viewerFilter);
        this.viewer.setComparator(viewerComparator);
    }

    public List<EditorlistElement> getElements() {
        if (viewer != null) {
            return Arrays.stream(viewer.getTable().getItems()).map(e -> (EditorlistElement) e.getData())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<EditorlistElement> getFilteredElements() {
        if (viewer != null) {
            return Arrays.stream(viewer.getTable().getItems()).map(e -> (EditorlistElement) e.getData())
                    .filter(e -> viewerFilter.select(viewer, null, e)).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public EditorlistElement getElement(final IEditorReference reference) {
        return contentProvider.getElement(reference);
    }

    public void setSorted(boolean sorted) {
        if (viewer != null) {
            viewer.setComparator(sorted ? viewerComparator : null);
        }
    }

    public void setInput(final Object input) {
        if (viewer != null) {
            viewer.setInput(input);
        }
    }

    public void add(final IEditorReference reference) {
        if (viewer != null) {
            viewer.add(contentProvider.addElement(reference));
        }
    }

    public void remove(final IEditorReference reference) {
        if (viewer != null) {
            viewer.remove(contentProvider.removeElement(reference));
        }
    }
    public void refresh() {
        if (viewer != null) {
            viewer.refresh();
        }
    }

    public void refresh(final IEditorReference reference) {
        if (viewer != null) {
            viewer.refresh(contentProvider.getElement(reference));
        }
    }

    public void refreshContents() {
        if (viewer != null) {
            viewer.getTable().removeAll();
            viewer.refresh();
        }
    }

    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void dispose() {
        if (viewer != null) {
            viewer.getControl().dispose();
            viewer = null;
            labelProvider.dispose();
        }
    }

    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener listener) {
        if (viewer != null) {
            viewer.addSelectionChangedListener(listener);
        }
    }

    @Override
    public ISelection getSelection() {
        return viewer != null ? viewer.getSelection() : null;
    }

    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
        if (viewer != null) {
            viewer.removeSelectionChangedListener(listener);
        }
    }

    @Override
    public void setSelection(final ISelection selection) {
        if (viewer != null) {
            viewer.setSelection(selection);
        }
    }

    public void setSelection(final ISelection selection, boolean reveal) {
        if (viewer != null) {
            viewer.setSelection(selection, reveal);
        }
    }

    public Control getControl() {
        return viewer != null ? viewer.getControl() : null;
    }
}
