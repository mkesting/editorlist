/*******************************************************************
 * Copyright (c) 2019, Martin Kesting, All rights reserved.
 *
 * This software is licenced under the Eclipse Public License v2.0,
 * see the LICENSE file or http://www.eclipse.org/legal/epl-v20.html
 * for details.
 *******************************************************************/
package com.github.mkesting.editorlist;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EditorlistPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.github.mkesting.editorlist"; //$NON-NLS-1$

	// The shared instance
	private static EditorlistPlugin plugin;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static EditorlistPlugin getDefault() {
		return plugin;
	}

	public URL getImageURL(final String imageFilename) {
        try {
            return new URL(getBundle().getEntry("/"), "icons/" + imageFilename);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
