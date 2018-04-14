package com.learn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Counter {
    private Map<String, Entry> mp;

    public Counter() {
        mp = new ConcurrentHashMap<String, Entry>();
    }

    public synchronized void add(String srcIp, String desIp, long inc) {
        String hash = srcIp + desIp;
        if (mp.containsKey(hash)) {
            mp.get(hash).add(inc);
        } else {
            Entry e = new Entry(srcIp, desIp);
            e.setFlow(inc);
            mp.put(srcIp + desIp, e);
        }
    }
    
    public synchronized List<Entry> sort() {
        List<Entry> res = new ArrayList<Entry>(mp.size());
        for(Map.Entry<String, Entry>e:mp.entrySet()) {
            res.add(e.getValue());
        }
        res.sort(new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return (int)(o2.getFlow()-o1.getFlow());
            }
        });
        return res;
    }
}
