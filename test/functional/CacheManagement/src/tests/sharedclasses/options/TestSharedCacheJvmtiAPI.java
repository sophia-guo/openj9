package tests.sharedclasses.options;

import tests.sharedclasses.TestUtils;

public class TestSharedCacheJvmtiAPI extends TestUtils {
	private final static int NO_FLAGS = 0;
	private final static int PERSISTENT = 1;
	private final static int NONPERSISTENT = 2;
	private final static int SNAPSHOT = 5;
	private final static int INVALID_CACHE_TYPE = 3;
	private static int cacheCount;
	private static int snapshotCount = 0;
		
    public native static int iterateSharedCache(String cacheDir, int flags, boolean useCommandLineValues);
    public native static boolean destroySharedCache(String cacheDir, String cacheName, int cacheType, boolean useCommandLineValues);

    public static void main(String args[]) {
    	boolean ret = true;
    	String dir = null;
    	int oldCacheCount = 0;
    	int newCacheCount = 0;
    	
        runDestroyAllCaches();
        if (false == isWindows()) {
        	runDestroyAllSnapshots();
        }
        try {
	    	dir = getCacheDir();
	    	System.out.println("initial getCacheDir is  " + dir);
	    	if (dir == null) {
	    		dir = getControlDir();
	    		System.out.println("initial getCacheDir in JVMAPI is  " + dir);
	    	}
	    	
	    	System.out.println("initial getCacheDir or getControldir in JVMAPI is  " + dir);
	    	/* get cache count before creating any new cache */
	    	oldCacheCount = iterateSharedCache(dir, NO_FLAGS, false);
		    if (oldCacheCount == -1) {
		    	fail("iterateSharedCacheFunction failed");
		    }
		    	    
	    	cacheCount = 0;
	    	if (isMVS() == false) {
	    		createPersistentCache("cache1");
	    		checkFileExistsForPersistentCache("cache1");
	    		cacheCount++;
	    		if (isWindows() == false) {
			    	runSimpleJavaProgramWithPersistentCache("cache1_groupaccess", "groupAccess");
					checkFileExistsForPersistentCache("cache1_groupaccess");
					cacheCount++;
				}
	    	}
	    	snapshotCount = 0;
	    	if (realtimeTestsSelected() == false) {
			    createNonPersistentCache("cache2");	  
			    checkFileExistsForNonPersistentCache("cache2");
			    cacheCount++;
				if (isWindows() == false) {
					runSimpleJavaProgramWithNonPersistentCache("cache2_groupaccess", "groupAccess");
					checkFileExistsForNonPersistentCache("cache2_groupaccess");
					cacheCount++;
					
					createCacheSnapshot("cache2");
					checkFileExistsForCacheSnapshot("cache2");
					snapshotCount++;
				}
	    	}
	    	System.out.println("after cache created getCacheDir in JVMAPI is  " + dir);
		    newCacheCount = iterateSharedCache(dir, NO_FLAGS, false);
		    if ((newCacheCount == -1) || (newCacheCount != oldCacheCount + cacheCount + snapshotCount)) {
		    	System.out.println("javajvmapi cache account is oldCacheCount " + oldCacheCount);
		    	System.out.println("javaapi cache account is cacheCount " + cacheCount);
		    	System.out.println("javaapi cache account is snapshotCount " + snapshotCount);
		    	System.out.println("javaapi cache account is newCacheCount " + newCacheCount);
		    	fail("iterateSharedCacheFunction failed");
		    }
		    
		    if (isMVS() == false) {
		    	System.out.println("Doing destroy cache dir in JVMAPI is  " + dir);
			    
		    	destroySharedCache(dir, "cache1", INVALID_CACHE_TYPE, false);
		    	checkFileExistsForPersistentCache("cache1");
		    	destroySharedCache(dir, "cache1", NONPERSISTENT, false);
		    	checkFileExistsForPersistentCache("cache1");
		    	destroySharedCache(dir, "cache1", SNAPSHOT, false);
		    	checkFileExistsForPersistentCache("cache1");
		    	destroySharedCache(dir, "cache1", PERSISTENT, false);
		    	checkFileDoesNotExistForPersistentCache("cache1");
				if (isWindows() == false) {
			    	destroySharedCache(dir, "cache1_groupaccess", PERSISTENT, false);
			    	checkFileDoesNotExistForPersistentCache("cache1_groupaccess");
				}
		    }
		    if (realtimeTestsSelected() == false) {
		    	System.out.println("Doing destroy cache dir in JVMAPI non persistent is  " + dir);
		    	destroySharedCache(dir, "cache2", INVALID_CACHE_TYPE, false);
			    checkFileExistsForNonPersistentCache("cache2");
		    	destroySharedCache(dir, "cache2", PERSISTENT, false);
		    	checkFileExistsForNonPersistentCache("cache2");
		    	destroySharedCache(dir, "cache2", NONPERSISTENT, false);
			    checkFileDoesNotExistForNonPersistentCache("cache2");
			    if (isWindows() == false) {
			    	destroySharedCache(dir, "cache2_groupaccess", NONPERSISTENT, false);
			    	checkFileDoesNotExistForNonPersistentCache("cache2_groupaccess");
			    	
			    	checkFileExistsForCacheSnapshot("cache2");
			    	destroySharedCache(dir, "cache2", SNAPSHOT, false);
			    	checkFileDoesNotExistForCacheSnapshot("cache2");
				}
		    }

		} finally {
			runDestroyAllCaches();
			if (false == isWindows()) {
				runDestroyAllSnapshots();
			}
		}
	}
}
