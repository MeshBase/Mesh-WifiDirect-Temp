package com.example.wifimesh.global_interfaces;

public interface DataListener {
    void onEvent(byte[] data, Device neighbor);
}
