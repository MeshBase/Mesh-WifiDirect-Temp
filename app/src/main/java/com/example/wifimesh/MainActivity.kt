package com.example.wifimesh
// MainActivity.kt
import androidx.activity.compose.setContent
import com.example.wifimesh.ui.theme.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.wifimesh.wifip2p.wifiP2PTestScreen

class MainActivity : ComponentActivity() {

    // var bluetoothHandler: BLEHandler? = null
    // var bleEnabler: BLEEnabler? = null
    // val id: UUID = UUID.randomUUID()
    // val tag= "my_main_activity_screen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Theme {
                wifiP2PTestScreen()
            }
        }
    }
}

//@Composable
//fun MainScreen(navController: NavController, modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val tag = "my_main_screen"
//
//    val permissionsLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        if (permissions.all { it.value }) {
//            Toast.makeText(context, "All permissions granted!", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(context, "Some permissions denied.", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun requestPermissions() {
//        Log.d(tag, "Requesting Permissions")
//        var permissions = arrayOf(
//            Manifest.permission.ACCESS_WIFI_STATE,
//            Manifest.permission.CHANGE_WIFI_STATE,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.INTERNET
//        )
//
//        Log.d(tag, Build.VERSION.SDK_INT.toString())
//        if (Build.VERSION.SDK_INT == 33) {
//            permissions += Manifest.permission.NEARBY_WIFI_DEVICES
//        }
//
//        permissionsLauncher.launch(permissions)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "WiFi Direct P2P Communication",
////            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = { requestPermissions() },
//            modifier = Modifier.fillMaxWidth(0.7f)
//        ) {
//            Text("Request Permissions")
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 50.dp),
//            horizontalAlignment = Alignment.Start
//        ) {
//            Text(text = "Permissions to be requested:")
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = "✔ Access and Change WiFi State")
//            Text(text = "✔ Access Fine Location")
//            Text(text = "✔ Internet")
//            if (Build.VERSION.SDK_INT == 33) {
//                Text(text = "✔ Nearby WiFi Devices")
//            }
//        }
//    }
//}
