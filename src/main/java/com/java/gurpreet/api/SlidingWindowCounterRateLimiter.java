package com.java.gurpreet.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowCounterRateLimiter implements RateLimiter {

    private Map<String, RequestCounters> ratelimiterMap;

    public SlidingWindowCounterRateLimiter() {
        ratelimiterMap = new ConcurrentHashMap<String, RequestCounters>();
    }

    @Override
    public void addUser(String userId, int requests, int windowTimeInSec) {
        if (!ratelimiterMap.containsKey(userId)) {
            int bucketSize = 10;
            ratelimiterMap.put(userId, new RequestCounters(requests, windowTimeInSec, bucketSize));
        }
    }

    @Override
    public void removeUser(String userId) {
        if (ratelimiterMap.containsKey(userId)) {
            ratelimiterMap.remove(userId);
        }

    }

    @Override
    public boolean shouldAllowServiceCall(String userId) {
        if (!ratelimiterMap.containsKey(userId)) {
            System.out.println("User Not Allowed");
            return false;
        }
        RequestCounters userTimestamps = ratelimiterMap.get(userId);
        try {
            long currentTimestamp = System.currentTimeMillis() / 1000;
            Thread.sleep(2000);
            long endTimestamp = System.currentTimeMillis() / 1000;
            System.out.println("waited for "+(endTimestamp-currentTimestamp));
        }catch(InterruptedException exception){
            System.out.println("Thread has been interrupted....");
        }
        synchronized (userTimestamps) {
            long currentTimestamp = System.currentTimeMillis() / 1000;
            userTimestamps.evictOlderBuckets(currentTimestamp);
            long currentBucket = userTimestamps.getBucket(currentTimestamp);
            Integer currentCount = userTimestamps.getCounts().get(currentBucket) ;
            if(currentCount == null)
            {
                currentCount=0;
            }
            userTimestamps.getCounts().put(currentTimestamp,currentCount + 1);
            int tc = (userTimestamps.getTotalCounts().get()) + 1;
            userTimestamps.getTotalCounts().set(tc);
            if (userTimestamps.getTotalCounts().get() > userTimestamps.getRequests()) {
                return false;
            }
        }
        return true;
    }
}
