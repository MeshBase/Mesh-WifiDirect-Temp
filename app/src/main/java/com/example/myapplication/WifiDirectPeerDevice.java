package com.example.myapplication;

import android.net.wifi.p2p.WifiP2pDevice;

import java.util.UUID;

public class WifiDirectPeerDevice extends Device {
    public UUID uuid;
    public String name;
    public WifiP2pDevice p2pDevice;

    WifiDirectPeerDevice(UUID uuid, String name, WifiP2pDevice device) {
        super(uuid, name);
        this.uuid = uuid;
        this.name = name;
        this.p2pDevice = device;
    }
}
