/*******************************************************************************
 * Copyright (c) 2010-2013, Abel Hegedus, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Abel Hegedus - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.transformation.evm.specific.job;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.viatra.query.runtime.api.IMatchProcessor;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.transformation.evm.api.Activation;
import org.eclipse.viatra.transformation.evm.api.Context;
import org.eclipse.viatra.transformation.evm.api.Job;
import org.eclipse.viatra.transformation.evm.specific.crud.CRUDActivationStateEnum;

/**
 * This class represents a {@link Job} that uses an {@link IMatchProcessor} 
 * on the match of the activation when executed.
 * 
 * @author Abel Hegedus
 */
public class StatelessJob<Match extends IPatternMatch> extends Job<Match> {

    private IMatchProcessor<Match> matchProcessor;
    
    /**
     * @return the matchProcessor executed by the job
     */
    public IMatchProcessor<Match> getMatchProcessor() {
        return matchProcessor;
    }

    /**
     * Creates a stateless job for the given state and processor.
     */
    public StatelessJob(final CRUDActivationStateEnum activationStateEnum, final IMatchProcessor<Match> matchProcessor) {
        super(activationStateEnum);
        this.matchProcessor = checkNotNull(matchProcessor,
                "StatelessJob cannot be instantiated with null match processor");
    }

    @Override
    protected void execute(final Activation<? extends Match> activation, final Context context) {
        matchProcessor.process(activation.getAtom());
    }

    @Override
    protected void handleError(final Activation<? extends Match> activation, final Exception exception, final Context context) {
        throw new IllegalStateException("Exception " + exception.getMessage() + " was thrown when executing " + activation
                + "! Stateless job doesn't handle errors!", exception);
    }
}
