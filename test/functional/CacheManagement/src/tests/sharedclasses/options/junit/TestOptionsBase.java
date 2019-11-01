package tests.sharedclasses.options.junit;

import tests.sharedclasses.options.*;

/**
 * Gather together all the tests into one testcase - see the subclasses for more info.
 */
public abstract class TestOptionsBase extends TestOptionsNonpersistent {


	//public void testCacheGenerations01() { TestCacheGenerations01.main(null);}
	//public void testCacheGenerations02() { TestCacheGenerations02.main(null);}
	//public void testCacheGenerations03() { TestCacheGenerations03.main(null);}
	//public void testCacheGenerations04() { TestCacheGenerations04.main(null);}
	//public void testCacheGenerations05() { TestCacheGenerations01.main(null);}
	//public void testCacheGenerations06() { TestCacheGenerations06.main(null);}
	//public void testCacheGenerations07() { TestCacheGenerations07.main(null);}
	//public void testCacheGenerations08() { TestCacheGenerations08.main(null);}


//	public void testCacheDir02() { TestCacheDir02.main(null);}

//	public void testCacheDir03() { TestCacheDir03.main(null);}
	
//	public void testCacheDir05() { TestCacheDir05.main(null);}
	
	
	//public void testIncompatibleCaches02() { TestIncompatibleCaches02.main(null);}

	//public void testIncompatibleCaches03() { TestIncompatibleCaches03.main(null);}

	//public void testIncompatibleCaches04() { TestIncompatibleCaches04.main(null);}



	public void testPersistentCacheMoving01() { TestPersistentCacheMoving01.main(null);}

	public void testPersistentCacheMoving02() { TestPersistentCacheMoving02.main(null);}

	public void testPersistentCacheMoving03() { TestPersistentCacheMoving03.main(null);}

	public void testZipCacheStoresAllBootstrapJar(){
		String jvmLevel = System.getProperty("java.specification.version");
		/* No bootstrap jars in Java 9, skip this test on Java 9 and later. */
		if (Double.parseDouble(jvmLevel) < 9) {
			TestZipCacheStoresAllBootstrapJar.main(null);
		}
	}
	
	public void testSharedCacheEnableBCI() { TestSharedCacheEnableBCI.main(null); }
}
