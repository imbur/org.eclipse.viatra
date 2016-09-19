/*******************************************************************************
 * Copyright (c) 2004-2010 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred;

import java.util.Collections;
import java.util.Set;

import org.eclipse.viatra.query.runtime.matchers.planning.QueryProcessingException;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.VariableDeferredPConstraint;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;

/**
 * @author Gabor Bergmann
 *
 */
public class ExportedParameter extends VariableDeferredPConstraint {
    PVariable parameterVariable;
    final String parameterName;
    final PParameter patternParameter;
    
    /**
     * @since 1.4
     */
    public ExportedParameter(PBody pBody, PVariable parameterVariable,
            PParameter patternParameter) {
        super(pBody, Collections.singleton(parameterVariable));
        this.parameterVariable = parameterVariable;
        this.patternParameter = patternParameter;
        parameterName = patternParameter.getName();
    }
    
    
    /**
     * @deprecated Use {@link #ExportedParameter(PBody, PVariable, PParameter)} instead
     */
    @Deprecated
    public ExportedParameter(PBody pBody, PVariable parameterVariable,
            String parameterName) {
        super(pBody, Collections.singleton(parameterVariable));
        this.parameterVariable = parameterVariable;
        this.parameterName = parameterVariable.getName();
        this.patternParameter = null;
    }

    @Override
    public void doReplaceVariable(PVariable obsolete, PVariable replacement) {
        if (obsolete.equals(parameterVariable))
            parameterVariable = replacement;
    }

    @Override
    protected String toStringRest() {
        Object varName = parameterVariable.getName();
        return parameterName.equals(varName) ? parameterName : parameterName + "(" + varName + ")";
    }

    @Override
    public Set<PVariable> getDeducedVariables() {
        return Collections.emptySet();
    }

    /**
     * The name of the parameter; usually, it is expected that {@link #getParameterVariable()} is more useful, except
     * maybe for debugging purposes.
     * 
     * @return a non-null name of the parameter
     */
    public String getParameterName() {
        return parameterName;
    }

    public PVariable getParameterVariable() {
        return parameterVariable;
    }

    /**
     * @since 1.4
     */
    public PParameter getPatternParameter() {
        if (patternParameter == null) {
            PQuery query = pBody.getPattern();
            Integer index = query.getPositionOfParameter(parameterName);
            if (index == null) {
                throw new IllegalStateException(String.format("Pattern %s does not have a parameter named %s",
                        query.getFullyQualifiedName(), parameterName));
            }
            return query.getParameters().get(index);
        } else {
            return patternParameter;
        }
    }
    
    @Override
    public Set<PVariable> getDeferringVariables() {
        return Collections.singleton(parameterVariable);
    }

    @Override
    public void checkSanity() throws QueryProcessingException {
        super.checkSanity();
        if (!parameterVariable.isDeducable()) {
            String[] args = { parameterName };
            String msg = "Impossible to match pattern: "
                    + "exported pattern variable {1} can not be determined based on the pattern constraints. "
                    + "HINT: certain constructs (e.g. negative patterns or check expressions) cannot output symbolic parameters.";
            String shortMsg = "Could not deduce value of parameter";
            throw new QueryProcessingException(msg, args, shortMsg, null);
        }

    }

}
