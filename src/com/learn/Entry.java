package com.learn;

public class Entry {
    private String srcIp;
    private String desIp;
    private long flow;
    
    public Entry(String srcIp,String desIp) {
        this.srcIp=srcIp;
        this.desIp=desIp;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getDesIp() {
        return desIp;
    }

    public void setDesIp(String desIp) {
        this.desIp = desIp;
    }

    public long getFlow() {
        return flow;
    }

    public void setFlow(long flow) {
        this.flow = flow;
    }
    public void add(long inc) {
        flow+=inc;
    }
}
