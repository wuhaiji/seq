package io.whj.seq;


final class SeqStopException extends RuntimeException {
    public static final SeqStopException INSTANCE = new SeqStopException();
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    
    private SeqStopException() {
        super();
    }
    
    static <T> T stop() {
        throw SeqStopException.INSTANCE;
    }
}