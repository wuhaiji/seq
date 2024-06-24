package io.whj.seq.seqs;


final class Stop extends RuntimeException {
    public static final Stop INSTANCE = new Stop();
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    
    private Stop() {
        super();
    }
    
    static <T> void stop() {
        throw Stop.INSTANCE;
    }
}