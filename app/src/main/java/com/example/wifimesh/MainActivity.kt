package com.example.wifimesh

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.wifimesh.global_interfaces.Device
import com.example.wifimesh.wifip2p.WifiP2PTestScreen
import com.example.wifimesh.wifip2p.WifiP2pHandler
import com.example.wifimesh.wifip2p.WifiP2pPermissions


class MainActivity : ComponentActivity() {
    private lateinit var wifiP2pPermissions: WifiP2pPermissions;
    private lateinit var wifiP2pHandler: WifiP2pHandler;
    private val TAG = "my_main activity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectedDevice = mutableStateOf<Device?>(null)
        val discoveredDevices = mutableStateListOf<Device>()

        wifiP2pPermissions = WifiP2pPermissions(this);

        wifiP2pHandler = WifiP2pHandler(
            { device -> connectedDevice.value = device },
            { device -> if (connectedDevice.value == device) connectedDevice.value = null },
            { device -> if (!discoveredDevices.contains(device)) discoveredDevices.add(device) },
            { connectedDevice.value = null },
            { data, device -> Log.d(TAG, "Got Data from ${device.name}") },
            { devices -> discoveredDevices.clear(); discoveredDevices.addAll(devices) },
            this
        )


        setContent {
            WifiP2PTestScreen(
                permission = wifiP2pPermissions,
                handler = wifiP2pHandler,
                connectedDevice = connectedDevice.value,
                discoveredDevices = discoveredDevices
            )
        }


    }
}