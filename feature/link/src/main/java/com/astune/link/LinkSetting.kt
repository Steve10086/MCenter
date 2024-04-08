package com.astune.link

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.astune.core.ui.design.Spinner
import com.astune.core.ui.design.TextCompat
import com.astune.database.Link.EmptyLink
import com.astune.database.Link.Link
import com.astune.database.SSHLink
import com.astune.database.WebLink
import com.astune.model.LinkType.*


@Composable
fun LinkSetting(
    link: Link = EmptyLink(),
    parentId: Int,
    onComplete: (Link) -> Unit = {},
    onDismiss: () -> Unit = {}
){
    var name by remember { mutableStateOf(link.name) }
    val info = remember { mutableStateMapOf<String,String>() }
    var type by remember{ mutableStateOf(link.type.getName()) }
    val context = LocalContext.current

    Surface(modifier = Modifier.width(300.dp), shape = AlertDialogDefaults.shape) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
            Column(modifier = Modifier.padding(20.dp)){
                TextCompat(
                    titleText = "Name: ",
                    value = name,
                    onValueChange = {name = it}
                )
                Spinner(
                    titleText = "Type: ",
                    values = getNames(getApplicableList()),
                    onValueChange = {
                        type = it
                                    },
                    initialValue = type,
                )
                when(type){
                    WEB_LINK.getName() ->{
                        TextCompat(
                            titleText = "Address: ",
                            value = info.getOrDefault("address", defaultValue = "80"),
                            onValueChange = {info["address"] = it}
                        )
                    }

                    SSH_LINK.getName() ->{
                        TextCompat(
                            titleText = "port: ",
                            value = info.getOrDefault("port", defaultValue = "22"),
                            onValueChange = {info["port"] = it}
                        )
                        TextCompat(
                            titleText = "user: ",
                            value = info.getOrDefault("user", defaultValue = ""),
                            onValueChange = {info["user"] = it}
                        )
                        TextCompat(
                            titleText = "password: ",
                            value = info.getOrDefault("pass", defaultValue = ""),
                            onValueChange = {info["pass"] = it}
                        )
                    }
                }


            }
            Row (modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = {
                    if(name == "" || type == ""){
                        Toast.makeText(context, "empty name", Toast.LENGTH_SHORT).show()
                    } else {
                        onComplete.invoke(
                            when(type){
                                WEB_LINK.getName()
                                -> WebLink(
                                    link.id,
                                    name,
                                    parentId,
                                    info.getOrDefault("address", ""))

                                SSH_LINK.getName()
                                -> SSHLink(
                                    id = link.id,
                                    name = name,
                                    parent = parentId,
                                    info = info.getOrDefault("port", "22"),
                                    username = info.getOrDefault("user", ""),
                                    password = info.getOrDefault("pass", ""))

                                else -> throw IllegalArgumentException("illegal link type!")
                            })
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Composable
internal fun DeviceInfoPreview(){
    Dialog(
        onDismissRequest = {}
    ){
        LinkSetting(parentId = 0)
    }
}