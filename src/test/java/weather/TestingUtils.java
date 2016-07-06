package weather;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestingUtils {

    /**
     * Copy a resource from the classpath onto a physical location
     *
     * @param classPath        - the classpath
     * @param absoluteLocation - the location on disk
     */
    public static void copyFromTo(String classPath, File absoluteLocation) {
        try {
            FileUtils.copyFile(new File(TestingUtils.class.getResource(classPath).getFile()), absoluteLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete all the files in the array
     *
     * @param files - all files.
     */
    public static void deleteFiles(File... files) {
        for (File file : files) {
            deleteFile(file);
        }
    }

    /**
     * Delete a single file
     * @param file - the file
     */
    private static void deleteFile(File file) {
        try {
            FileUtils.forceDeleteOnExit(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delay the current thread.
     *
     * @param duration - the duration
     * @param timeUnit - the time unit
     */
    public static void waitFor(long duration, TimeUnit timeUnit) {
        try {
            Thread.sleep(timeUnit.toMillis(duration));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
    }
}
