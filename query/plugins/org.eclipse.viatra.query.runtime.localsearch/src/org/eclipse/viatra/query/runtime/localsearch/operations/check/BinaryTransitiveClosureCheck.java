/*******************************************************************************
 * Copyright (c) 2010-2013, Zoltan Ujhelyi, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zoltan Ujhelyi - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.query.runtime.localsearch.operations.check;

import java.util.List;
import java.util.Set;

import org.eclipse.viatra.query.runtime.localsearch.MatchingFrame;
import org.eclipse.viatra.query.runtime.localsearch.exceptions.LocalSearchException;
import org.eclipse.viatra.query.runtime.localsearch.matcher.ISearchContext;
import org.eclipse.viatra.query.runtime.localsearch.matcher.LocalSearchMatcher;
import org.eclipse.viatra.query.runtime.localsearch.matcher.MatcherReference;
import org.eclipse.viatra.query.runtime.localsearch.operations.IMatcherBasedOperation;
import org.eclipse.viatra.query.runtime.matchers.backend.IQueryResultProvider;
import org.eclipse.viatra.query.runtime.matchers.context.IQueryBackendContext;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PQuery;
import org.eclipse.viatra.query.runtime.matchers.tuple.Tuple;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Checking for a transitive closure expressed as a local search pattern matcher. The matched pattern must have two
 * parameters of the same model type.
 * 
 * @author Zoltan Ujhelyi
 * 
 */
public class BinaryTransitiveClosureCheck extends CheckOperation implements IMatcherBasedOperation {

    private PQuery calledQuery;
    private IQueryResultProvider matcher;
    private final int sourcePosition;
    private final int targetPosition;
    private final ImmutableSet<PParameter> adornment;

    /**
     * @since 1.5
     */
    @Override
	public IQueryResultProvider getAndPrepareCalledMatcher(MatchingFrame frame, ISearchContext context) throws LocalSearchException {
        matcher = context.getMatcher(new MatcherReference(calledQuery, adornment));
        return matcher;
	}

    /**
     * @since 1.5
     */
    @Override
    public IQueryResultProvider getCalledMatcher(){
    	return matcher;
    }
    
    /**
     * The source position will be matched in the called pattern to the first parameter; while target to the second.
     * 
     * @param calledQuery
     * @param sourcePosition
     * @param targetPosition
     */
    public BinaryTransitiveClosureCheck(PQuery calledQuery, int sourcePosition, int targetPosition) {
        super();
        this.calledQuery = calledQuery;
        this.sourcePosition = sourcePosition;
        this.targetPosition = targetPosition;
        // Second parameter is NOT bound during execution, but the first is
        this.adornment = ImmutableSet.of(calledQuery.getParameters().get(0));
    }

    public PQuery getCalledQuery() {
        return calledQuery;
    }

    @Override
    public void onInitialize(MatchingFrame frame, ISearchContext context) throws LocalSearchException {
        super.onInitialize(frame, context);
		getAndPrepareCalledMatcher(frame, context);
    }
    
    @Override
    protected boolean check(MatchingFrame frame) throws LocalSearchException {
        Object targetValue = frame.get(targetPosition);
        Set<Object> sourcesToEvaluate = Sets.newLinkedHashSet();
        sourcesToEvaluate.add(frame.get(sourcePosition));
        Set<Object> sourceEvaluated = Sets.newHashSet();
        do {
            Object currentValue = sourcesToEvaluate.iterator().next();
            sourcesToEvaluate.remove(currentValue);
            sourceEvaluated.add(currentValue);
            final Object[] mappedFrame = new Object[]{currentValue, null};
            for (Tuple match : matcher.getAllMatches(mappedFrame)) {
                Object foundTarget = match.get(1);
                if (targetValue.equals(foundTarget)) {
                    return true;
                } else if (!sourceEvaluated.contains(foundTarget)) {
                    sourcesToEvaluate.add(foundTarget);
                }
            }
        } while (!sourcesToEvaluate.isEmpty());
        return false;
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("Check binary transitive closure of ")
    		.append(calledQuery.getFullyQualifiedName().substring(calledQuery.getFullyQualifiedName().lastIndexOf('.') + 1))
    		.append(" from ").append(sourcePosition).append(" to ").append(targetPosition);
    	return builder.toString();
    }

	@Override
	public List<Integer> getVariablePositions() {
		return Lists.asList(sourcePosition, targetPosition, new Integer[0]);
	}



}
