package tests.sharedclasses.options;

import tests.sharedclasses.TestUtils;

public class TestSharedCacheJvmtiAPI extends TestUtils {
	private final static int NO_FLAGS = 0;
	private final static int PERSISTENT = 1;
	private final static int NONPERSISTENT = 2;
	private final static int SNAPSHOT = 5;
	private final static int INVALID_CACHE_TYPE = 3;
	private static int cacheCount;
	private static int cacheGroupAccessCount = 0;
	private static int snapshotCount = 0;

    public native static int iterateSharedCache(String cacheDir, int flags, boolean useCommandLineValues);
    public native static boolean destroySharedCache(String cacheDir, String cacheName, int cacheType, boolean useCommandLineValues);

    public static void main(String args[]) {
    	boolean ret = true;
    	String dir = null;
    	String dirGroupAccess = null;
    	String dirNonPersistent = null;
    	String dirGroupAccessNonPersistent = null;
    	int oldCacheCount = 0;
    	int newCacheCount = 0;
    	int oldCacheGroupAccessCount = 0;
    	int newCacheGroupAccessCount = 0;
    	int index = 0;
   
        runDestroyAllCaches();
        runDestroyAllGroupAccessCaches();
        if (false == isWindows()) {
        	runDestroyAllSnapshots();
        }

        try {
        	dir = getCacheDir("Foo", false);
        	System.out.println("+++++++++before JVMTI cacheDir is " + dir );
        	dirNonPersistent = getCacheNonPersistentDir(dir);
        	System.out.println("+++++++++before JVMTI dirNonPersistent is " + dirNonPersistent );
	    	if (dir == null) {
	    	}
	    	/* get cache count before creating any new cache */
	    	oldCacheCount = iterateSharedCache(dir, NO_FLAGS, false) + iterateSharedCache(dirNonPersistent, NO_FLAGS, false);
	    	System.out.println("+++++++++before JVMTI oldachecount is " + oldCacheCount );
	    	if (oldCacheCount == -1) {
		    	fail("iterateSharedCacheFunction failed");
		    }
	    	
	    	if (false == isWindows()) {
			    dirGroupAccess = getCacheDir("Foo_groupaccess", false);
		    	dirGroupAccessNonPersistent = getCacheNonPersistentDir(dirGroupAccess);
		    	System.out.println("+++++++++before JVMTI cacheGroupAccessDir is " + dirGroupAccess );
		    	System.out.println("+++++++++before JVMTI cacheGroupAccessDirNonPersistent is " + dirGroupAccessNonPersistent );
		    	oldCacheGroupAccessCount = iterateSharedCache(dirGroupAccess, NO_FLAGS, false) + iterateSharedCache(dirGroupAccessNonPersistent, NO_FLAGS, false);
		    	System.out.println("+++++++++before JVMTI oldCacheGroupAccessCount is " + oldCacheGroupAccessCount );
		    	if (oldCacheGroupAccessCount == -1) {
			    	fail("iterateSharedCacheFunction failed");
			    }
	    	}
	    	cacheCount = 0;
	    	if (isMVS() == false) {
	    		createPersistentCache("cache1");
	    		checkFileExistsForPersistentCache("cache1");
	    		cacheCount++;
	    		if (isWindows() == false) {
			    	runSimpleJavaProgramWithPersistentCache("cache1_groupaccess", "groupAccess");
					checkFileExistsForPersistentCache("cache1_groupaccess");
					cacheGroupAccessCount++;
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
					cacheGroupAccessCount++;
					createCacheSnapshot("cache2");
					checkFileExistsForCacheSnapshot("cache2");
					snapshotCount++;
				}
	    	}
		    
		    newCacheCount = iterateSharedCache(dir, NO_FLAGS, false) + iterateSharedCache(dirNonPersistent, NO_FLAGS, false);
		    newCacheGroupAccessCount = iterateSharedCache(dirGroupAccess, NO_FLAGS, false) + iterateSharedCache(dirGroupAccessNonPersistent, NO_FLAGS, false);
		    
		    System.out.println("+++++++++iterateSharedCache jvmti with javaresource home persistent cache is " + iterateSharedCache(dir, NO_FLAGS, false) );
		    System.out.println("+++++++++iterateSharedCachejvmti with javaresourcedirNonPersistent cache is" + iterateSharedCache(dirNonPersistent, NO_FLAGS, false));
		    
		    System.out.println("+++++++++iterateSharedCache /home/dirGroupAccess/ cache is" + iterateSharedCache(dirGroupAccess, NO_FLAGS, false) );
		    System.out.println("+++++++++iterateSharedCache dirGroupAccessNonPersistent cache is" + iterateSharedCache(dirGroupAccessNonPersistent, NO_FLAGS, false));
		    System.out.println("+++++++++after JVMTI cacheDir is " + dir );
		    System.out.println("+++++++++after JVMTI dirGroupAccess is " + dirGroupAccess );
//ToDO, new ccount for groupaccess
		    if (isWindows() == false) {
			    if ((newCacheCount == -1) || (newCacheGroupAccessCount == -1)|| ((newCacheCount + newCacheGroupAccessCount) != (oldCacheCount + oldCacheGroupAccessCount + cacheCount + cacheGroupAccessCount + snapshotCount))) {
				    System.out.println("+++++++++newCacheCount is " + newCacheCount +"newCacheGroupAccessCount is " + newCacheGroupAccessCount );
				    System.out.println("+++++++++pldCacheCount is " + oldCacheCount +"oldCacheGroupAccessCount is " + oldCacheGroupAccessCount );
				    System.out.println("snapshotCount is " + snapshotCount + "cacheGroupAccessCount is" + cacheGroupAccessCount);
				    System.out.println("+++++++++cacheCount is " + cacheCount);
				    fail("Invalid number of cache found\t" +
			    			"expected: " + (oldCacheCount + oldCacheGroupAccessCount + cacheCount + cacheGroupAccessCount + snapshotCount) + "\tfound: " + (newCacheCount + newCacheGroupAccessCount));
			    }
		    } else {
			    if ((newCacheCount == -1) || (newCacheGroupAccessCount == -1)|| ((newCacheCount) != (oldCacheCount + cacheCount + snapshotCount))) {
				    System.out.println("+++++++++newCacheCount is " + newCacheCount +"newCacheGroupAccessCount is " + newCacheGroupAccessCount );
				    System.out.println("+++++++++pldCacheCount is " + oldCacheCount +"oldCacheGroupAccessCount is " + oldCacheGroupAccessCount );
				    System.out.println("snapshotCount is " + snapshotCount + "cacheGroupAccessCount is" + cacheGroupAccessCount);
				    System.out.println("+++++++++cacheCount is " + cacheCount);
				    fail("Invalid number of cache found\t" +
			    			"expected: " + (oldCacheCount + cacheCount + snapshotCount) + "\tfound: " + newCacheCount);

			    }
		    }
		    
		   if (isMVS() == false) {
		    	destroySharedCache(dir, "cache1", INVALID_CACHE_TYPE, false);
		    	checkFileExistsForPersistentCache("cache1");
		    	destroySharedCache(dir, "cache1", NONPERSISTENT, false);
		    	checkFileExistsForPersistentCache("cache1");
		    	destroySharedCache(dir, "cache1", SNAPSHOT, false);
		    	checkFileExistsForPersistentCache("cache1");
		    	destroySharedCache(dir, "cache1", PERSISTENT, false);
		    	checkFileDoesNotExistForPersistentCache("cache1");
				if (isWindows() == false) {
					destroySharedCache(dirGroupAccess, "cache1_groupaccess", PERSISTENT, false);
			    	checkFileDoesNotExistForPersistentCache("cache1_groupaccess");
				}
		    }
		    if (realtimeTestsSelected() == false) {
		    	destroySharedCache(dirNonPersistent, "cache2", INVALID_CACHE_TYPE, false);
			    checkFileExistsForNonPersistentCache("cache2");
		    	destroySharedCache(dirNonPersistent, "cache2", PERSISTENT, false);
		    	checkFileExistsForNonPersistentCache("cache2");
		    	destroySharedCache(dirNonPersistent, "cache2", NONPERSISTENT, false);
			    checkFileDoesNotExistForNonPersistentCache("cache2");
			    if (isWindows() == false) {
			    	destroySharedCache(dirGroupAccessNonPersistent, "cache2_groupaccess", NONPERSISTENT, false);
			    	checkFileDoesNotExistForNonPersistentCache("cache2_groupaccess");
			    	
			    	checkFileExistsForCacheSnapshot("cache2");
			    	destroySharedCache(dirNonPersistent, "cache2", SNAPSHOT, false);
			    	checkFileDoesNotExistForCacheSnapshot("cache2");
				}
		    }

		} finally {
			runDestroyAllCaches();
			runDestroyAllGroupAccessCaches();
			if (false == isWindows()) {
				runDestroyAllSnapshots();
			}
		}
	}
}
