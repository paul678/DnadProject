import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoInstruction {

    public static final String ORIGINAL_HASH = originalHash;
    private static final long MAX_INTERVAL = maxInterval;
    private static final long MIN_INTERVAL = minInterval;

    public static void main(String[] args) {
        int availableProc = Runtime.getRuntime().availableProcessors();
        int threadCount = (int) (availableProc + (MAX_INTERVAL - MIN_INTERVAL) % availableProc);
        long step = (MAX_INTERVAL - MIN_INTERVAL) / threadCount;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Collection<PasswordCracker> collection = new ArrayList<>( );

        long start = MIN_INTERVAL;
        long end = start + step;
        for(int i=0; i< threadCount; ++i){
            PasswordCracker task = new PasswordCracker("Worker_"  + i, start, end);
            collection.add(task);
            start = end + 1;
            end = start + step;
        }

        try {
            //TODO: send to server
            String result = executor.invokeAny(collection);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

    private static class PasswordCracker implements Callable<String> {

        private final String mInstanceName;
        private final long mIntervalStart;
        private final long mIntervalEnd;

        public PasswordCracker(String instanceName, long intervalMax, long intervalDivision) {
            this.mInstanceName = instanceName;
            this.mIntervalStart = intervalMax;
            this.mIntervalEnd = intervalDivision;
        }

        @Override
        public String call() throws Exception {
            for (long index = mIntervalStart; index <= mIntervalEnd; ++index) {
                String numberHash = getHash(String.valueOf(index), "SHA-256");
                if(ORIGINAL_HASH.equals(numberHash)) {
                    return String.valueOf(index);
                }
            }
            return null;
        }
    }

    public static String getHash(String toHash, String hashAlgorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
        byte[] out = messageDigest.digest(toHash.getBytes());
        return Hex.encodeHexString(out);
    }

}