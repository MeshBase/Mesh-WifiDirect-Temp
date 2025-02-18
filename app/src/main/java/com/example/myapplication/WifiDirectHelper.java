package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class WifiDirectHelper extends ConnectionHandler {
    private static final String TAG = "WifiDirectHelper";
    private static final int REQUEST_CODE = 100;
    private static final int PORT = 8888;

    private HashMap<String, WifiDirectPeerDevice> connectedDevices;
    private ArrayList<Device> discoveredDevices;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private Activity activity;
    private BroadcastReceiver wifiDirectReceiver;
    private IntentFilter intentFilter;

    public WifiDirectHelper(Activity activity,
                            NeighborConnectedListener neighborConnectedListener,
                            NeighborDisconnectedListener neighborDisconnectedListener,
                            NeighborDiscoveredListener neighborDiscoveredListener,
                            DisconnectedListener disconnectedListener,
                            DataListener dataListener,
                            NearbyDevicesListener nearbyDevicesListener) {
        super(neighborConnectedListener, neighborDisconnectedListener, neighborDiscoveredListener,
                disconnectedListener, dataListener, nearbyDevicesListener);
        this.activity = activity;
        this.connectedDevices = new HashMap<>();
        this.discoveredDevices = new ArrayList<>();
        this.wifiP2pManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
    }

    @Override
    public ArrayList<Device> getNeighbourDevices() {
        // Return connected devices as an ArrayList
        return new ArrayList<>(connectedDevices.values());
    }

    @Override
    public ArrayList<Device> getNearbyDevices() {
        // Return the last discovered peer devices
        return discoveredDevices;
    }

    @Override
    public void start() throws Exception {
        // Check for necessary permission(s)
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            throw new Exception("Location permission not granted. Requested permission, please restart start().");
        }

        // Initialize the WiFiP2pManager channel
        channel = wifiP2pManager.initialize(activity, Looper.getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                Log.e(TAG, "WiFi P2P Channel disconnected.");
            }
        });

        // Create a group (this device becomes group owner)
        wifiP2pManager.createGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Group created successfully.");
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "Failed to create group. Reason: " + reason);
            }
        });

        // Set up a broadcast receiver to listen for peer changes, connection changes, etc.
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifiDirectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                    // Update the list of peers
                    wifiP2pManager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList peerList) {
                            discoveredDevices.clear();
                            for (WifiP2pDevice device : peerList.getDeviceList()) {
                                // Wrap the device in our own WifiDirectPeerDevice.
                                // Here, we assume a unique key (e.g. deviceAddress)
                                WifiDirectPeerDevice peer = new WifiDirectPeerDevice(
                                        java.util.UUID.randomUUID(), device.deviceName, device);
                                // Optionally, update peer.ipAddress when connection info is available.
                                discoveredDevices.add(peer);
                                neighborDiscoveredListener.onEvent(peer);
                            }
                            nearbyDevicesListener.onEvent(discoveredDevices);
                        }
                    });
                } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                    // You can request connection info here and update connectedDevices accordingly.
                    // For example, after a successful connection, call:
                    wifiP2pManager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                        @Override
                        public void onConnectionInfoAvailable(WifiP2pInfo info) {
                            // If this device is group owner, then it will have clients connecting.
                            // If not, then info.groupOwnerAddress is the remote device.
                            // Update your connectedDevices map accordingly.
                            // (Implementation here depends on your connection management design.)
                            Log.d(TAG, "Connection info available. Group Owner: " + info.isGroupOwner);
                        }
                    });
                }
            }
        };

        // Register the receiver
        activity.registerReceiver(wifiDirectReceiver, intentFilter);
    }

    @Override
    public void stop() {
        // Unregister the broadcast receiver
        try {
            activity.unregisterReceiver(wifiDirectReceiver);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Receiver not registered.");
        }
        // Remove the group
        wifiP2pManager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Group removed successfully.");
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "Failed to remove group. Reason: " + reason);
            }
        });
    }

    @Override
    public void send(byte[] data) throws SendError {
        // Flood the message to all connected devices
        for (WifiDirectPeerDevice device : connectedDevices.values()) {
            try {
                sendDataToDevice(device, data);
            } catch (IOException e) {
                throw new SendError("Error sending data to " + device.name + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void send(byte[] data, Device neighbor) throws SendError {
        if (!(neighbor instanceof WifiDirectPeerDevice)) {
            throw new SendError("Incompatible device type.");
        }
        WifiDirectPeerDevice device = (WifiDirectPeerDevice) neighbor;
        try {
            sendDataToDevice(device, data);
        } catch (IOException e) {
            throw new SendError("Error sending data to " + device.name + ": " + e.getMessage());
        }
    }

    /**
     * Private helper method to send data to a specific WifiDirectPeerDevice.
     * Assumes the device's ipAddress field has been set (for example, via connection info).
     */
    private void sendDataToDevice(WifiDirectPeerDevice device, byte[] data) throws IOException, SendError {
        if (device.p2pDevice.deviceAddress == null) {
            throw new SendError("No device address available for device " + device.name);
        }
        // Send data in a separate thread to avoid blocking the UI thread
        new Thread(() -> {
            try (Socket socket = new Socket(device.p2pDevice.deviceAddress, PORT);
                 OutputStream out = socket.getOutputStream()) {
                out.write(data);
                out.flush();
                Log.d(TAG, "Data sent to " + device.name);
            } catch (IOException e) {
                Log.e(TAG, "Error sending data to " + device.name + ": " + e.getMessage());
            }
        }).start();
    }
}
