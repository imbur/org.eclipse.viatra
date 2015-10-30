package org.eclipse.viatra.debug.example.transformation

import org.eclipse.incquery.runtime.evm.api.ExecutionSchema
import org.eclipse.viatra.emf.runtime.adapter.impl.AdaptableExecutor
import org.eclipse.viatra.emf.runtime.debug.breakpoints.impl.TransformationBreakpoint
import org.eclipse.viatra.emf.runtime.debug.configuration.TransformationDebuggerConfiguration
import org.eclipse.viatra.emf.runtime.debug.ui.impl.ViewersDebuggerUI
import org.eclipse.viatra.emf.runtime.transformation.eventdriven.ExecutionSchemaBuilder

class ViatraViewersDebugTransformation extends BaseTransformation{
	override ExecutionSchema createExecutionSchema(){
		val debugAdapterConfiguration = new TransformationDebuggerConfiguration(
				new ViewersDebuggerUI(engine, specifications),
                new TransformationBreakpoint(ruleProvider.classRule.ruleSpecification)
        );
		
		executor = createAdaptableExecutor()
			.setIncQueryEngine(engine)
			.addConfiguration(debugAdapterConfiguration).build() as AdaptableExecutor
		
		val ExecutionSchemaBuilder builder= new ExecutionSchemaBuilder()
			.setEngine(engine)
			.setExecutor(executor)
			.setConflictResolver(createConflictResolver)

		return builder.build()
	}
}