package com.example.wifimesh

import androidx.activity.compose.setContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.wifimesh.wifip2p.WifiP2pPermissions
import com.example.wifimesh.wifip2p.WifiP2PTestScreen

class MainActivity : ComponentActivity() {
    private lateinit var wifiP2pPermissions: WifiP2pPermissions;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WifiP2PTestScreen(permission = wifiP2pPermissions)
        }
    }
}
