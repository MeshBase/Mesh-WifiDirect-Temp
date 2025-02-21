package com.example.wifimesh.global_interfaces;

public class SendError extends Exception {
    String message;

    public SendError(String message) {
        this.message = message;
    }
}
