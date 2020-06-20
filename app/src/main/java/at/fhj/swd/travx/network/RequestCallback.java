package at.fhj.swd.travx.network;

public interface RequestCallback {
    void onResult(String result);
    void onRequestStart();
}
