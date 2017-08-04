/**
 * 
 */
package com.weatherpred.util.test;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import com.weatherpred.util.CmdLineHelper;

/**
 * Test Class for Command Line Helper 
 * 
 * Date : August 3, 2017
 * 
 * @author Poornima Tom
 * 
 * @version 1.0
 *
 */
public class CmdLineHelperTest {

	/**
	 * Command Line Helper object
	 */
	private static CmdLineHelper cmdLineHelper;
	
	@Before
	public void loadData() {
		String[] args = new String[] { "--lat", "34.0522342", "--long",
				"-118.2436849", "--ele", "86.8470916748", "--time",
				"1425582000", "--out", "/home/putom/output.txt" };
		cmdLineHelper = new CmdLineHelper(args);
	}

	/**
	 * Test method for {@link com.weatherpred.util.CmdLineHelper#parse()}.
	 */
	@Test
	public void testParse() {

		assertNotEquals(cmdLineHelper.parse(), null);
	}

}
