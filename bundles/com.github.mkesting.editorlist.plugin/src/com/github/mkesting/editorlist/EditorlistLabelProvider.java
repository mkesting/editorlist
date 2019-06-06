/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;

public class EditorlistLabelProvider extends LabelProvider {

    private final Map<Long, Image> images = new HashMap<Long, Image>();

    @Override
    public Image getImage(final Object element) {
        if (element instanceof EditorlistElement) {
            final Image titleImage = ((EditorlistElement) element).getEditorReference().getTitleImage();
            return images.computeIfAbsent(titleImage.handle,
                    k -> new Image(Display.getCurrent(), titleImage, SWT.IMAGE_COPY));
        }
        return super.getImage(element);
    }

    @Override
    public String getText(final Object element) {
        if (element instanceof EditorlistElement) {
            final IEditorReference reference = ((EditorlistElement) element).getEditorReference();
            return reference.getTitle();
        }
        return super.getText(element);
    }

    public void dispose() {
        images.values().stream().filter(e -> !e.isDisposed()).forEach(e -> e.dispose());
    }
}
