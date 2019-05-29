/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import org.eclipse.ui.IEditorReference;

public class EditorlistElement {

    private IEditorReference editorReference;

    public EditorlistElement(final IEditorReference editorReference) {
        this.editorReference = editorReference;
    }

    public IEditorReference getEditorReference() {
        return editorReference;
    }

    public void setEditorReference(IEditorReference editorReference) {
        this.editorReference = editorReference;
    }
}
