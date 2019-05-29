/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorReference;

public class EditorlistLabelProvider extends LabelProvider {

    @Override
    public Image getImage(final Object element) {
        if (element instanceof EditorlistElement) {
            return ((EditorlistElement) element).getEditorReference().getTitleImage();
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
}
