package com.example.wifimesh.wifip2p;

import com.example.wifimesh.global_interfaces.ConnectionHandler;
import com.example.wifimesh.global_interfaces.DataListener;
import com.example.wifimesh.global_interfaces.Device;
import com.example.wifimesh.global_interfaces.DisconnectedListener;
import com.example.wifimesh.global_interfaces.NearbyDevicesListener;
import com.example.wifimesh.global_interfaces.NeighborConnectedListener;
import com.example.wifimesh.global_interfaces.NeighborDisconnectedListener;
import com.example.wifimesh.global_interfaces.NeighborDiscoveredListener;
import com.example.wifimesh.global_interfaces.SendError;

import java.util.ArrayList;

public class WifiP2PHandler extends ConnectionHandler {

    public WifiP2PHandler(
            NeighborConnectedListener neighborConnectedListener,
            NeighborDisconnectedListener neighborDisconnectedListener,
            NeighborDiscoveredListener neighborDiscoveredListener,
            DisconnectedListener disconnectedListener,
            DataListener dataListener,
            NearbyDevicesListener nearbyDevicesListener
    ) {
        super(
                neighborConnectedListener,
                neighborDisconnectedListener,
                neighborDiscoveredListener,
                disconnectedListener,
                dataListener,
                nearbyDevicesListener
        );
    }

    /**
     * @return
     */
    @Override
    public ArrayList<Device> getNeighbourDevices() {
        return null;
    }

    /**
     * @throws Exception 
     */
    @Override
    public void start() throws Exception {

    }

    /**
     * 
     */
    @Override
    public void stop() {

    }

    /**
     * @return 
     */
    @Override
    public ArrayList<Device> getNearbyDevices() {
        return null;
    }

    /**
     * @param data 
     * @throws SendError
     */
    @Override
    public void send(byte[] data) throws SendError {

    }

    /**
     * @param data 
     * @param neighbor
     * @throws SendError
     */
    @Override
    public void send(byte[] data, Device neighbor) throws SendError {

    }
}
