package com.connective.android.saldo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

public class TrafficStatsFile {

private static final String mobileRxFile_1 = "/sys/class/net/rmnet0/statistics/rx_bytes";
private static final String mobileRxFile_2 = "/sys/class/net/ppp0/statistics/rx_bytes";
private static final String mobileTxFile_1 = "/sys/class/net/rmnet0/statistics/tx_bytes";
private static final String mobileTxFile_2 = "/sys/class/net/ppp0/statistics/tx_bytes";

private static final String LOGGING_TAG = TrafficStatsFile.class.getSimpleName();

public long getMobileRxBytes() {
    return tryBoth(mobileRxFile_1, mobileRxFile_2);
}

public long getMobileTxBytes() {
    return tryBoth(mobileTxFile_1, mobileTxFile_2);
}

// Return the number from the first file which exists and contains data
private static long tryBoth(String a, String b) {
    long num = readNumber(a);
    return num >= 0 ? num : readNumber(b);
}

// Returns an ASCII decimal number read from the specified file, -1 on error.
private static long readNumber(String filename) {
    try {
        RandomAccessFile f = new RandomAccessFile(filename, "r");
        try {
           // Log.d(LOGGING_TAG, "f.length() = " + f.length());
            String contents = f.readLine();
            if(!contents.isEmpty() && contents!=null) {
                try {
                    return Long.parseLong(contents);
                }
                catch(NumberFormatException nfex) {
                    Log.w(LOGGING_TAG, "File contents are not numeric: " + filename); 
                }
            }
            else {
                Log.w(LOGGING_TAG, "File contents are empty: " + filename); 
            }
        }
        catch (FileNotFoundException fnfex) {
            Log.w(LOGGING_TAG, "File not found: " + filename, fnfex);
        }
        catch(IOException ioex) {
            Log.w(LOGGING_TAG, "IOException: " + filename, ioex);
        }   
    }catch(FileNotFoundException ffe){
        Log.w(LOGGING_TAG, "File not found: " + filename, ffe);
    }
    return -1;
}

}