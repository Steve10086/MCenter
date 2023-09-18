package com.astune.device

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.astune.core.ui.LocalRootUIState
import com.astune.core.ui.design.MCenterTheme
import com.astune.core.ui.design.TextCompat
import com.astune.core.ui.design.ThemePreview
import com.astune.core.ui.setOnRightBtnClicked
import com.astune.database.Device
import com.astune.ui.DeviceCardList

@Composable
fun DevicePanel(
    modifier: Modifier = Modifier,
    deviceViewModel: DeviceViewModel = hiltViewModel(),
    onNavigateToLink: (String) -> Unit,
){
    val deviceList = deviceViewModel.getDeviceList()

    DeviceScreen(
        modifier = modifier,
        deviceList = deviceList,
        onDeviceCardBtnClicked = {device ->  onNavigateToLink(device.id.toString())},
        deleteDevice = {device ->  deviceViewModel.delete(device) },
        insertDevice = {device ->  deviceViewModel.insert(device) },
        editDevice = {device ->  deviceViewModel.insert(device) }
    )

}



@SuppressLint("UnrememberedMutableState")
@Composable
internal fun DeviceScreen(
    modifier: Modifier,
    deviceList: List<Device>,
    onDeviceCardBtnClicked: (Device) -> Unit = {},
    deleteDevice: (Device) -> Unit = {},
    editDevice: (Device) -> Unit = {},
    insertDevice: (Device) -> Unit = {},
    ) {
    var expended by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showInsertDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(Offset.Zero) }
    var currentDevice by remember { mutableStateOf(Device(-1, "", "", "0")) }

    LocalRootUIState.current.setOnRightBtnClicked(key = "device") {
        showInsertDialog = true
    }


    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {

        Box(
            modifier = Modifier.onGloballyPositioned {
                position -= it.positionInParent()
            }
        ){DropdownMenu(
            expanded = expended,
            onDismissRequest = { expended = false },
            offset = with(LocalDensity.current) { DpOffset(position.x.toDp(), position.y.toDp()) }
        ) {

            DropdownMenuItem(
                text = { Text("delete") },
                onClick =
                {
                    showDeleteDialog = true
                    expended = false
                }
            )
            DropdownMenuItem(
                text = { Text("edit") },
                onClick =
                {
                    expended = false
                    showEditDialog = true
                }
            )
        }}


        DeviceCardList(
            modifier = Modifier,
            cardList = deviceList,
            onButtonClick = onDeviceCardBtnClicked,
            onLongClick = { offset, device ->
                position = offset
                expended = true
                currentDevice = device
                //Log.i("devicepanel", "long click $offset")
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                               },
            confirmButton = {
                Button(
                    onClick = {
                    deleteDevice(currentDevice)
                    showDeleteDialog = false
                },
                    colors = ButtonDefaults.buttonColors(Color.Transparent, Color.Transparent)
                ){
                    Text("confirm", color = Color.Black)
                }
            },
            dismissButton = { Button(onClick = {
                showDeleteDialog = false
            }){
                Text("dismiss", color = Color.Black)
            }},
            title = { Text("Delete?") },
            text = { Text("Do you really want to delete this device, it will be lost for a long time(very long)!") }
        )
    }

    if (showInsertDialog){
        Dialog(
            onDismissRequest = {
                showInsertDialog = false
            }
        ){
            DeviceSetting(
                onComplete = {
                    insertDevice.invoke(it)
                    showInsertDialog = false
                },
                onDismiss = {showInsertDialog = false}
            )
        }
    }

    if (showEditDialog){
        Dialog(
            onDismissRequest = {
                showEditDialog = false
            }
        ){
            DeviceSetting(
                device = currentDevice,
                onComplete = {
                    editDevice.invoke(it)
                    showEditDialog = false
                },
                onDismiss = {showEditDialog = false}
            )
        }
    }
}

@Composable
fun DeviceSetting(
    device: Device = Device(0, "", "", null),
    onComplete: (Device) -> Unit = {},
    onDismiss: () -> Unit = {}
){
    var name by remember { mutableStateOf(device.name) }
    var ip by remember { mutableStateOf(device.ip) }
    val context = LocalContext.current

    Surface(modifier = Modifier.size(width = 300.dp, height = 200.dp), shape = AlertDialogDefaults.shape) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceEvenly){
            TextCompat(
                titleText = "Name: ",
                value = name,
                onValueChange = {name = it}
            )
            TextCompat(
                titleText = "Address: ",
                value = ip,
                onValueChange = {ip = it}
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = {
                    if(name == "" || ip == ""){
                        Toast.makeText(context, "empty name or ip", Toast.LENGTH_SHORT).show()
                    } else {
                        onComplete.invoke(Device(device.id, name, ip, null))
                    }
                }
                ){
                    Text("Save")
                }
                Button(onClick = onDismiss){
                    Text("Dismiss")
                }
            }
        }
    }

}

@ThemePreview
@Composable
internal fun DeviceInfoPreview(){
    val device = Device(0, "testDevice", "0", null)
    Dialog(
        onDismissRequest = {}
    ){
        DeviceSetting(device)
    }
}

@ThemePreview
@Composable
internal fun PanelPreview(){
    val deviceList = ArrayList<Device>()
    for(i in 1..15){
        deviceList.add(Device(0, "testdevice", "null", null))
    }
    MCenterTheme {
        DeviceScreen(modifier = Modifier.fillMaxSize(), deviceList)
    }
}

