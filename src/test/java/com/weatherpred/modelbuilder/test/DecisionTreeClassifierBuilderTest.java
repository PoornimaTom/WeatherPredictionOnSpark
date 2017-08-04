/**
 * 
 */
package com.weatherpred.modelbuilder.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder;

/**
 * Test Class for Decision Tree Classifier Builder
 * 
 * Date : August 4, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class DecisionTreeClassifierBuilderTest {

	/**
	 * Test method for {@link com.weatherpred.modelbuilder.DecisionTreeClassifierBuilder#main(java.lang.String[])}.
	 */
	@Test
	public void testMain() {
		DecisionTreeClassifierBuilder classifierBuilder = new DecisionTreeClassifierBuilder();
		assertNotEquals(classifierBuilder.getDecisionTreeClassifierModel(), null);
	}

}
