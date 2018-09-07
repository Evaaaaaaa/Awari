/****************************************************************
 * Utility.java
 * Do not modify this file!
 * This file is a utility class. You do not need to read or understand this class.
 */

public class Utility {
    public static void tSleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
