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
package org.eclipse.viatra.transformation.debug.transformationtrace.util

import org.eclipse.emf.common.util.EList
import org.eclipse.viatra.transformation.debug.transformationtrace.transformationtrace.ActivationTrace
import org.eclipse.viatra.transformation.debug.transformationtrace.transformationtrace.RuleParameterTrace

class ActivationTraceUtil {
    def static boolean compareActivationCodes(ActivationTrace a1, ActivationTrace a2) {
        var boolean retVal = false
        var EList<RuleParameterTrace> a1RuleParameterTraces = a1.getRuleParameterTraces()
        var EList<RuleParameterTrace> a2ruleParameterTraces = a2.getRuleParameterTraces()

        if (a1RuleParameterTraces.size == a2ruleParameterTraces.size) {
            var boolean temp = true
            for (var int i = 0; i < a1RuleParameterTraces.size(); i++) {
                var RuleParameterTrace a1Trace = a1RuleParameterTraces.get(i)
                var RuleParameterTrace a2Trace = a2ruleParameterTraces.get(i)
                if (!a1Trace.getParameterName().equals(a2Trace.getParameterName()) ||
                    !a1Trace.getObjectId().equals(a2Trace.getObjectId())) {
                    temp = false
                }
            }
            retVal = temp
            return retVal
        }
        return false;

    }
}
