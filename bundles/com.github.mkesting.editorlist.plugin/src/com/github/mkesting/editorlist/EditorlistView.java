/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import java.util.List;
import java.util.Optional;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.PartSite;
import org.eclipse.ui.part.ViewPart;

import com.github.mkesting.editorlist.EditorlistFilter.Pattern;

@SuppressWarnings("restriction")
public class EditorlistView extends ViewPart {

    private EditorlistViewer viewer;
    private EditorlistFilter filter;
    private EditorlistListener listener;

    @Override
    public void createPartControl(final Composite parent) {
        filter = new EditorlistFilter();
        listener = new EditorlistListener(this);

        viewer = new EditorlistViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, filter);
        viewer.setInput(getSite().getPage());

        initViewerContents();
        createContextMenu();
        createToolbarActions();

        getSite().setSelectionProvider(viewer);

        viewer.addSelectionChangedListener(listener);
        getSite().getPage().addPartListener(listener);
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPageListener(listener);
    }

    private void initViewerContents() {
        final IEditorReference[] references = getSite().getPage().getEditorReferences();
        for (int i = 0; i < references.length; i++) {
            addEditorReference(references[i]);
        }
    }

    private void createToolbarActions() {
        final IToolBarManager toolbar = getViewSite().getActionBars().getToolBarManager();

        final EditorlistFilterAction[] allFilters = {
                new EditorlistFilterAction(viewer, filter, Pattern.JAVA),
                new EditorlistFilterAction(viewer, filter, Pattern.CLASS),
                new EditorlistFilterAction(viewer, filter, Pattern.XML),
                new EditorlistFilterAction(viewer, filter, Pattern.PROPERTIES),
                new EditorlistFilterAction(viewer, filter, Pattern.OTHERS)
        };

        for (final EditorlistFilterAction editorlistFilterAction : allFilters) {
            toolbar.add(editorlistFilterAction);
        }
        toolbar.add(new Separator());
        toolbar.add(new EditorlistClearFilterAction(viewer, filter, allFilters));
    }

    private void createContextMenu() {
        final MenuManager menuMgr = new MenuManager("#PopUp"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(final IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });

        final Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }

    private void fillContextMenu(final IMenuManager menu) {
        menu.add(new EditorlistCloseEditorAction(this));
        menu.add(new Separator());
        menu.add(new EditorlistCloseFilteredEditorsAction(this));
    }

    private void addEditorReference(final IEditorReference reference) {
        reference.addPropertyListener(listener);
        viewer.add(reference);
    }

    private void removeEditorReference(final IEditorReference reference) {
        reference.removePropertyListener(listener);
        viewer.remove(reference);
    }

    private static IEditorReference getEditorReference(final IEditorPart part) {
        return (IEditorReference) ((PartSite) part.getSite()).getPartReference();
    }

    private IEditorReference getSelectedEditor() {
        final ISelectionProvider provider = getViewSite().getSelectionProvider();
        if (provider == null) {
            return null;
        }

        final IStructuredSelection selection = (IStructuredSelection) provider.getSelection();
        if (selection == null || selection.size() != 1) {
            return null;
        }
        return ((EditorlistElement) selection.getFirstElement()).getEditorReference();
    }

    private IEditorPart getRestoredEditor(final IEditorReference reference) {
        final IEditorPart part = reference.getEditor(true);
        if (part == null) {
            removeEditorReference(reference);
        }
        return part;
    }

    private void closeEditors(final List<EditorlistElement> editorElements) {
        editorElements.stream().map(EditorlistElement::getEditorReference)
                .map(e -> Optional.ofNullable(getRestoredEditor(e))).filter(Optional::isPresent).map(Optional::get)
                .forEach(editor -> getSite().getPage().closeEditor(editor, true));
    }

    public void addEditorToViewer(final IEditorPart editor) {
        final IEditorReference reference = getEditorReference(editor);

        for (final EditorlistElement element : viewer.getElements()) {
            if (element.getEditorReference() == reference) {
                return;
            }
        }
        addEditorReference(reference);
    }

    public void removeEditorFromViewer(final IEditorPart editor) {
        removeEditorReference(getEditorReference(editor));
    }

    public void selectEditorInViewer(final IEditorPart editor) {
        final IEditorReference selectedEditor = getSelectedEditor();
        final IEditorReference newEditor = getEditorReference(editor);

        if (selectedEditor != null && selectedEditor.equals(newEditor)) {
            return;
        }

        final EditorlistElement element = viewer.getElement(newEditor);
        if (element != null) {
            viewer.removeSelectionChangedListener(listener);
            viewer.setSelection(new StructuredSelection(element), true);
            viewer.addSelectionChangedListener(listener);
        }
    }

    public void activateSelectedEditor() {
        final IEditorReference reference = getSelectedEditor();
        if (reference != null) {
            getSite().getPage().activate(getRestoredEditor(reference));
        }
    }

    @SuppressWarnings("unchecked")
    public void closeSelectedEditors() {
        final StructuredSelection selection = (StructuredSelection) viewer.getSelection();
        final List<EditorlistElement> editorElements = (List<EditorlistElement>) selection.toList();

        closeEditors(editorElements);
    }

    public void closeFilteredEditors() {
        closeEditors(viewer.getFilteredElements());
    }

    public void refreshContents() {
        viewer.refreshContents();
    }

    public void refreshLabel(final IEditorPart editor) {
        viewer.refresh(getEditorReference(editor));
    }

    @Override
    public void setFocus() {
        viewer.setFocus();
    }

    public void setSorted(final boolean sorted) {
        viewer.setSorted(sorted);
    }

    @Override
    public void dispose() {
        viewer.dispose();
        super.dispose();
    }
}