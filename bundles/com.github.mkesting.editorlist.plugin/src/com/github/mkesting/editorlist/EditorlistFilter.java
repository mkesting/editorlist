/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class EditorlistFilter extends ViewerFilter {

    private final Set<Pattern> patterns = new HashSet<>();

    public enum Pattern {
        JAVA(".java", "jcu_obj.gif", "Java"),
        CLASS(".class", "classf_obj.png", "Class Files"),
        XML(".xml", "xmldoc.gif", "Xml"),
        PROPERTIES(".properties", "properties.gif", "Properties"),
        OTHERS("", "file_obj.png", "Others");

        private final String value;
        private final String actionIcon;
        private final String toolTipText;

        private Pattern(final String value, final String actionIcon, final String toolTipText) {
            this.value = value;
            this.actionIcon = actionIcon;
            this.toolTipText = toolTipText;
        }

        public String getValue() {
            return value;
        }

        public String getActionIcon() {
            return actionIcon;
        }

        public String getToolTipText() {
            return toolTipText;
        }
    }

    public void addPattern(final Pattern pattern) {
        patterns.add(pattern);
    }

    public void removePattern(final Pattern pattern) {
        patterns.remove(pattern);
    }

    @Override
    public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
        if (patterns.isEmpty()) {
            return true;
        }

        final String title = ((EditorlistElement) element).getEditorReference().getTitle();
        final boolean hasMatch = patterns.stream().filter(p -> p != Pattern.OTHERS)
                .anyMatch(p -> title.endsWith(p.getValue()));
        return hasMatch || isOthers(title);
    }

    private boolean isOthers(final String title) {
        return patterns.contains(Pattern.OTHERS) && Arrays.stream(Pattern.values()).filter(p -> p != Pattern.OTHERS)
                .noneMatch(p -> title.endsWith(p.getValue()));
    }
}
