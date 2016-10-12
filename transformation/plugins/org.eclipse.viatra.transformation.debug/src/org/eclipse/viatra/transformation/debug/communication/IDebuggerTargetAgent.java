/**
 * Copyright (c) 2010-2016, Peter Lunk, IncQuery Labs Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Peter Lunk - initial API and implementation
 */
package org.eclipse.viatra.transformation.debug.communication;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.viatra.transformation.debug.model.breakpoint.ITransformationBreakpoint;
import org.eclipse.viatra.transformation.evm.api.Activation;
import org.eclipse.viatra.transformation.evm.api.RuleSpecification;
import org.eclipse.viatra.transformation.evm.api.event.EventFilter;

public interface IDebuggerTargetAgent {
    public void suspended();
    
    public void breakpointHit(ITransformationBreakpoint breakpoint);
    
    public void terminated() throws CoreException;
    
    public void conflictSetChanged(Set<Activation<?>> nextActivations, Set<Activation<?>> conflictingActivations);
    
    public void activationFired(Activation<?> activation);
    
    public void activationFiring(Activation<?> activation);
    
    public void nextActivationChanged(Activation<?> activation);

    public void addedRule(RuleSpecification<?> specification, EventFilter<?> filter);

    public void removedRule(RuleSpecification<?> specification, EventFilter<?> filter);
    
}