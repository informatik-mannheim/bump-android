package hs_mannheim.bump;

public interface IBumpDetector {
    void registerListener(BumpEventListener listener);
    void setThreshold(Threshold threshold);
    void startMonitoring(int delayInMillis);
    void startMonitoring();
    void stopMonitoring();
}
