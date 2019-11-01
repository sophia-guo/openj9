package tests.sharedclasses.options.junit;

import tests.sharedclasses.options.*;
import junit.framework.TestCase;

/**
 * A subset of tests that don't rely on any persistent cache support
 */
public class TestOptionsNonpersistent extends TestCase {
	
	//public void testIncompatibleCaches01() { TestIncompatibleCaches01.main(null);}

	//CMVC 146158: these tests leave artifacts on the system so i am removing them
	//public void testMovingControlFiles01() { TestMovingControlFiles01.main(null);}
	//public void testMovingControlFiles02() { TestMovingControlFiles02.main(null);}

		// support all unix flavours
//	public void testSemaphoreMeddling01() throws Exception { TestSemaphoreMeddling01.main(null);}
//
//	public void testSharedMemoryMeddling01() throws Exception { TestSharedMemoryMeddling01.main(null);}

}
