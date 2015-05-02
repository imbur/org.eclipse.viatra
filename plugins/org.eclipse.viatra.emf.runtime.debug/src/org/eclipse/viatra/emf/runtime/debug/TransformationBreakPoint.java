/*******************************************************************************
 * Copyright (c) 2004-2015, Peter Lunk, Zoltan Ujhelyi and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Peter Lunk - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.emf.runtime.debug;

import org.eclipse.incquery.runtime.evm.api.Activation;
import org.eclipse.incquery.runtime.evm.api.RuleSpecification;
import org.eclipse.incquery.runtime.evm.api.event.ActivationState;

/**
 * Class that can be used to identify individual EVM rule activations. It is mainly used by 
 * the VIATRA DebugTransformationAdapter class.
 * 
 * @author Lunk Péter
 *
 */
public class TransformationBreakPoint {
    private RuleSpecification<?> ruleSpecification;
    private Object eventAtom;
    private ActivationState activationState;

    public TransformationBreakPoint(RuleSpecification<?> ruleSpecification, Object eventAtom,
            ActivationState activationState) {
        super();
        this.ruleSpecification = ruleSpecification;
        this.eventAtom = eventAtom;
        this.activationState = activationState;
    }

    public TransformationBreakPoint(RuleSpecification<?> ruleSpecification) {
        super();
        this.ruleSpecification = ruleSpecification;
        this.eventAtom = null;
        this.activationState = null;
    }

    public TransformationBreakPoint(RuleSpecification<?> ruleSpecification, ActivationState activationState) {
        super();
        this.ruleSpecification = ruleSpecification;
        this.eventAtom = null;
        this.activationState = activationState;
    }

    public TransformationBreakPoint(RuleSpecification<?> ruleSpecification, Object eventAtom) {
        super();
        this.ruleSpecification = ruleSpecification;
        this.eventAtom = eventAtom;
        this.activationState = null;
    }

    public RuleSpecification<?> getRuleSpecification() {
        return ruleSpecification;
    }

    public Object getEventAtom() {
        return eventAtom;
    }

    public ActivationState getActivationState() {
        return activationState;
    }

    /**
     * Checks if the given EVM rule activation matches the specified transformation breakpoint.
     * @param a
     * @return
     */
    public boolean matchingActivation(Activation<?> a) {
        if (!ruleSpecification.equals(a.getInstance().getSpecification())) {
            return false;
        }
        if (eventAtom != null) {
            if (!eventAtom.equals(a.getAtom()))
                return false;
        }
        if (activationState != null) {
            if (!activationState.equals(a.getState()))
                return false;
        }
        return true;

    }
}