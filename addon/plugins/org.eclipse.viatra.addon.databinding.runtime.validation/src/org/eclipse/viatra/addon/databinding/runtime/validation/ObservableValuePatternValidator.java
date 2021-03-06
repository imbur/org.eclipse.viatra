/*******************************************************************************
 * Copyright (c) 2010-2012, Zoltan Ujhelyi, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zoltan Ujhelyi - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.addon.databinding.runtime.validation;

import org.eclipse.viatra.query.patternlanguage.annotations.IPatternAnnotationAdditionalValidator;
import org.eclipse.viatra.query.patternlanguage.emf.annotations.AnnotationExpressionValidator;
import org.eclipse.viatra.query.patternlanguage.helper.CorePatternLanguageHelper;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Annotation;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.Pattern;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.PatternLanguagePackage;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.StringValue;
import org.eclipse.viatra.query.patternlanguage.patternLanguage.ValueReference;
import org.eclipse.viatra.query.patternlanguage.validation.IIssueCallback;

import com.google.inject.Inject;

/**
 * A validator for observable value annotations
 * 
 * <p/>Note that this class uses the optional dependency org.eclipse.viatra.query.patternlanguage.emf!
 * 
 * @author Zoltan Ujhelyi
 * 
 */
public class ObservableValuePatternValidator implements IPatternAnnotationAdditionalValidator {

    private static final String VALIDATOR_BASE_CODE = "org.eclipse.viatra.addon.databinding.";
    public static final String GENERAL_ISSUE_CODE = VALIDATOR_BASE_CODE + "general";
    public static final String EXPRESSION_MISMATCH_ISSUE_CODE = VALIDATOR_BASE_CODE + "expressionmismatch";

    @Inject
    private AnnotationExpressionValidator expressionValidator;

    @Override
    public void executeAdditionalValidation(Annotation annotation, IIssueCallback validator) {
        if (annotation.getParameters().isEmpty())
            return;
        Pattern pattern = (Pattern) annotation.eContainer();
        ValueReference ref = CorePatternLanguageHelper.getFirstAnnotationParameter(annotation, "expression");
        ValueReference labelRef = CorePatternLanguageHelper.getFirstAnnotationParameter(annotation, "labelExpression");

        if (ref == null && labelRef == null) {
            validator.error("Specify either the parameter 'expression' or 'labelExpression'", annotation,
                    PatternLanguagePackage.Literals.ANNOTATION__PARAMETERS, EXPRESSION_MISMATCH_ISSUE_CODE);
        }
        if (ref != null && labelRef != null) {
            validator.error("Specify only one of the parameter 'expression' or 'labelExpression'", annotation,
                    PatternLanguagePackage.Literals.ANNOTATION__PARAMETERS, EXPRESSION_MISMATCH_ISSUE_CODE);
        }

        if (ref instanceof StringValue) {
            String value = ((StringValue) ref).getValue();
            if (value.contains("$")) {
                validator.warning("The expressions are not required to be escaped using $ characters.", ref,
                        PatternLanguagePackage.Literals.STRING_VALUE__VALUE, GENERAL_ISSUE_CODE);
            }

            expressionValidator.validateModelExpression(value, pattern, ref, validator);
        }
        if (labelRef instanceof StringValue) {
            String value = ((StringValue) labelRef).getValue();
            if (!value.contains("$")) {
                validator.warning("The label expressions should contain escaped references using $ characters.", ref,
                        PatternLanguagePackage.Literals.STRING_VALUE__VALUE, GENERAL_ISSUE_CODE);
            }

            expressionValidator.validateStringExpression(value, pattern, labelRef, validator);
        }

    }

}
