/*******************************************************************************
 * Copyright (c) 2010-2012, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tamas Szabo, Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra.query.runtime.base.exception;

public class ViatraBaseException extends Exception {

    private static final long serialVersionUID = -5145445047912938251L;

    public static final String EMPTY_REF_LIST = "At least one EReference must be provided!";
    public static final String INVALID_EMFROOT = "Emf navigation helper can only be attached on the contents of an EMF EObject, Resource, or ResourceSet.";

    public ViatraBaseException(String s) {
        super(s);
    }

}
