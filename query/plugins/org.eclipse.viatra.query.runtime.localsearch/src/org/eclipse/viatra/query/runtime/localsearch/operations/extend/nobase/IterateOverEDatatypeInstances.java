/*******************************************************************************
 * Copyright (c) 2010-2015, Marton Bur, Zoltan Ujhelyi, Akos Horvath, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Marton Bur, Zoltan Ujhelyi, Akos Horvath - initial API and implementation
 *******************************************************************************/
package org.eclipse.viatra.query.runtime.localsearch.operations.extend.nobase;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.viatra.query.runtime.base.api.NavigationHelper;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.emf.types.EDataTypeInSlotsKey;
import org.eclipse.viatra.query.runtime.localsearch.MatchingFrame;
import org.eclipse.viatra.query.runtime.localsearch.matcher.ISearchContext;
import org.eclipse.viatra.query.runtime.localsearch.operations.IIteratingSearchOperation;
import org.eclipse.viatra.query.runtime.matchers.context.IInputKey;
import org.eclipse.viatra.query.runtime.matchers.util.IProvider;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

/**
 * Iterates over all {@link EDataType} instances without using an {@link NavigationHelper VIATRA Base indexer}.
 * 
 */
public class IterateOverEDatatypeInstances extends AbstractIteratingExtendOperation<Object> implements IIteratingSearchOperation {

    private EDataType dataType;
    
    public IterateOverEDatatypeInstances(int position, EDataType dataType, EMFScope scope) {
        super(position, scope);
        this.dataType = dataType;
    }
    
    protected Iterator<EAttribute> doGetEAttributes(EClass eclass, ISearchContext context){
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Table<EDataType, EClass, Set<EAttribute>> cache = context.accessBackendLevelCache(getClass(), Table.class, new IProvider<Table>() {

            @Override
            public Table<EDataType, EClass, Set<EAttribute>> get() {
                return HashBasedTable.create();
            }
        });
        if(!cache.contains(dataType, eclass)){
            EList<EAttribute> eAllAttributes = eclass.getEAllAttributes();
            cache.put(dataType, eclass, Sets.filter(Sets.newHashSet(eAllAttributes), new Predicate<EAttribute>() {
                @Override
                public boolean apply(EAttribute input) {
                    return input.getEType().equals(dataType);
                }
            }));
        }
        return cache.get(dataType, eclass).iterator();
    }

    public EDataType getDataType() {
        return dataType;
    }

    @Override
    public void onInitialize(MatchingFrame frame, final ISearchContext context) {
        it = Iterators.concat(Iterators.transform(Iterators.filter(getModelContents(), EObject.class), new Function<EObject, Iterator<Object>>(){

            @Override
            public Iterator<Object> apply(final EObject input) {
                Iterator<EAttribute> features = doGetEAttributes(input.eClass(), context);
                return Iterators.concat(
                        Iterators.transform(features, new Function<EAttribute, Iterator<?>>() {

                            @Override
                            public Iterator<?> apply(EAttribute attribute) {
                                if (attribute.isMany()){
                                    return ((List<?>)input.eGet(attribute)).iterator();
                                }else{
                                    Object o = input.eGet(attribute);
                                    return o == null ? Collections.emptyIterator() : Iterators.singletonIterator(o);
                                }
                            }
                        })
                    );
            }
            
        }));
    }
    
    
    @Override
    public String toString() {
        return "extend    "+dataType.getName()+"(-"+position+") iterating";
    }
    
    @Override
    public List<Integer> getVariablePositions() {
        return Lists.asList(position, new Integer[0]);
    }
    
    /**
     * @since 1.4
     */
    @Override
    public IInputKey getIteratedInputKey() {
        return new EDataTypeInSlotsKey(dataType);
    }

}
