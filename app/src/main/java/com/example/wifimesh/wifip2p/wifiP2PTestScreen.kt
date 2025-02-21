package com.example.wifimesh.wifip2p

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wifimesh.ui.theme.Theme

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun WifiP2PTestScreen(permission: WifiP2pPermissions) {

    Theme {
        val context = LocalContext.current

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 5.dp)
        ) { _ ->
            run {
                // enable section
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    LaunchedEffect(Unit) {
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
                    }


                    Button(onClick = { permission.enable() })
                    {
                        Text(text = "Ask Permissions")
                    }
                }
            }
        }
    }
}