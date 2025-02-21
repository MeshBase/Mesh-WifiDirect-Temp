package com.example.wifimesh.global_interfaces;

import java.util.ArrayList;

public interface NearbyDevicesListener {
    void onEvent(ArrayList<Device> devices);
}
