/*******************************************************************************
 * Copyright (c) 2010-2014, Miklos Foldenyi, Andras Szabolcs Nagy, Abel Hegedus, Akos Horvath, Zoltan Ujhelyi and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *   Miklos Foldenyi - initial API and implementation
 *   Andras Szabolcs Nagy - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.dse.guidance;

import java.util.List;
import java.util.Map;

import org.eclipse.viatra.dse.api.TransformationRule;

public class PetriAbstractionSolution {

    private final List<TransformationRule<?, ?>> trajectory;
    private final Map<TransformationRule<?, ?>, Integer> occurrence;

    public PetriAbstractionSolution(Map<TransformationRule<?, ?>, Integer> occurrence,
            List<TransformationRule<?, ?>> trajectory) {
        this.trajectory = trajectory;
        this.occurrence = occurrence;
    }

    public Map<TransformationRule<?, ?>, Integer> getOccurrence() {
        return occurrence;
    }

    public List<TransformationRule<?, ?>> getTrajectory() {
        return trajectory;
    }

}