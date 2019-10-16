package tests.sharedclasses.options.junit;

import tests.sharedclasses.TestUtils;

/**
 * For every testcase inherited from the superclass this will set the controlDir.
 */
public class TestOptionsControlDir extends TestOptionsBase {

	String currentCacheDir;
	String tmpdir;
	
	protected void setUp() throws Exception {
		super.setUp();
		currentCacheDir = TestUtils.getControlDir();
		//FOR testing only
		System.out.println("+++++setting in Control Dir using getControlDir is " + currentCacheDir);
		tmpdir= TestUtils.createTemporaryDirectory("TestOptionsControlDir");
		TestUtils.setControlDir(tmpdir);
		System.out.println("Running  :" + this.getName() + "  (Test suite : " + this.getClass().getSimpleName() + ")");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		TestUtils.runDestroyAllCaches();
		TestUtils.setControlDir(currentCacheDir);
		TestUtils.deleteTemporaryDirectory(tmpdir);
	}	
}
