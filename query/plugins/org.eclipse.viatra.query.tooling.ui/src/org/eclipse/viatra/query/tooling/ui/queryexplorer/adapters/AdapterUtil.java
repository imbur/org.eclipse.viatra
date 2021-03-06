/*******************************************************************************
 * Copyright (c) 2010-2012, Andras Okros, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Andras Okros - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.query.tooling.ui.queryexplorer.adapters;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.viatra.query.tooling.ui.ViatraQueryGUIPlugin;
import org.eclipse.viatra.query.tooling.ui.queryexplorer.IModelConnector;

/**
 * A simple util class for the adapter calls. Returns typesafe objects and checks for errors as well. It will load the
 * required adapters, if they're not present in the system (due to lazy loading of plugins).
 */
public class AdapterUtil {

    private static ILog logger = ViatraQueryGUIPlugin.getDefault().getLog();

    /**
     * @param editorPart
     *            which can be loaded into the system
     * @return a {@link ResourceSet} instance which is used by the given editorpart
     */
    public static IModelConnector getModelConnectorFromIEditorPart(IEditorPart editorPart) {
        if (editorPart != null) {
            Object adaptedObject = editorPart.getAdapter(IModelConnector.class);
            if (adaptedObject != null) {
                return (IModelConnector) adaptedObject;
            }

            Platform.getAdapterManager().loadAdapter(editorPart, IModelConnector.class.getName());
            adaptedObject = editorPart.getAdapter(IModelConnector.class);
            if (adaptedObject != null) {
                return (IModelConnector) adaptedObject;
            } else {
                logger.log(new Status(IStatus.ERROR, ViatraQueryGUIPlugin.PLUGIN_ID, "EditorPart " + editorPart.getTitle()
                        + " (type: " + editorPart.getClass().getSimpleName()
                        + ") cannot provide a ModelConnector object for the QueryExplorer."));
            }
        }
        return null;
    }

}
