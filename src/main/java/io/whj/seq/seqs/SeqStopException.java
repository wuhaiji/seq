package io.whj.seq.seqs;


final class SeqStopException extends RuntimeException {
    public static final SeqStopException INSTANCE = new SeqStopException();
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    
    private SeqStopException() {
        super();
    }
    
    static <T> void stop() {
        throw SeqStopException.INSTANCE;
    }
}