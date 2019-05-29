/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;

public class EditorlistContentProvider implements IStructuredContentProvider {

    private final Map<IEditorReference, EditorlistElement> elements = new HashMap<>(0);

    @Override
    public Object[] getElements(final Object inputElement) {
        final IEditorReference[] references = ((IWorkbenchPage) inputElement).getEditorReferences();

        elements.clear();
        elements.putAll(Arrays.stream(references).map(EditorlistElement::new)
                .collect(Collectors.toMap(e -> e.getEditorReference(), e -> e)));

        return elements.values().toArray(new EditorlistElement[elements.values().size()]);
    }

    public EditorlistElement getElement(final IEditorReference reference) {
        return (EditorlistElement) elements.get(reference);
    }

    public EditorlistElement removeElement(final IEditorReference reference) {
        return (EditorlistElement) elements.remove(reference);
    }

    public EditorlistElement addElement(final IEditorReference reference) {
        final EditorlistElement element = new EditorlistElement(reference);
        elements.put(reference, element);
        return element;
    }
}
