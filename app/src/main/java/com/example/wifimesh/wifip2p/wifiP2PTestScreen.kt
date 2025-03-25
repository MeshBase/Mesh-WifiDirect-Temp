package com.example.wifimesh.wifip2p

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.wifimesh.global_interfaces.Device
import com.example.wifimesh.ui.theme.Theme

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun WifiP2PTestScreen(
    permission: WifiP2pPermissions,
    handler: WifiP2pHandler,
    connectedDevice: Device?,
    discoveredDevices: SnapshotStateList<Device>
) {
    Theme {
        val context = LocalContext.current

        permission.setListener(
            object : WifiP2pPermissions.StatusListener {
                override fun onEnabled() {
                    Toast.makeText(
                        context,
                        "Wifi P2P Enabled!",
                        Toast.LENGTH_SHORT,
                    ).show();
                };
                override fun onDisabled() {
                    Toast.makeText(
                        context,
                        "Wifi P2P Disabled!",
                        Toast.LENGTH_SHORT,
                    ).show();
                }
            }
        )

        var message by remember { mutableStateOf(TextFieldValue("")) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        )
        { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Ask Permissions Button
                Button(onClick = { permission.enable() }) {
                    Text(text = "Ask Permissions")
                }

                // Start & Stop Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { handler.start() }) {
                        Text(text = "Start Handler")
                    }
                    Button(onClick = { handler.stop() }) {
                        Text(text = "Stop Handler")
                    }
                }

                // List of Discovered Devices
                Text(
                    text = "Discovered Devices:",
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(discoveredDevices) { device ->
                        Text(text = device.name)
                    }
                }

                // Message Input & Send Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Enter message") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = {
                        connectedDevice?.let {
                            handler.send(message.text.toByteArray())
                            Toast.makeText(context, "Message Sent!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }) {
                        Text(text = "Send")
                    }
                }
            }
        }
    }
}
