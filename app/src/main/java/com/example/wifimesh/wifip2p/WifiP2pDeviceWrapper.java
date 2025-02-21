package com.example.wifimesh.wifip2p;

import android.net.wifi.p2p.WifiP2pDevice;
import com.example.wifimesh.global_interfaces.Device;
import java.util.UUID;

public class WifiP2pDeviceWrapper extends Device {
    private final WifiP2pDevice internalDevice;

    // Construct from an existing WifiP2pDevice instance.
    public WifiP2pDeviceWrapper(WifiP2pDevice device) {
        // You might generate a UUID based on the device address, or use random.
        super(UUID.randomUUID(), device.deviceName);
        this.internalDevice = device;
    }

    public WifiP2pDevice getInternalDevice() {
        return internalDevice;
    }

    // Optionally, add helper methods to expose WifiP2pDevice attributes.
    public String getDeviceAddress() {
        return internalDevice.deviceAddress;
    }
}
