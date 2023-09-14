package com.astune.link

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.astune.core.ui.ClippedBackground
import com.astune.core.ui.LinkCardGrid
import com.astune.core.ui.LocalRootUIState
import com.astune.core.ui.design.MCenterTheme
import com.astune.database.Device
import com.astune.database.Link.EmptyLink
import com.astune.database.Link.Link
import com.astune.database.Link.NewLink
import com.astune.database.SSHLink
import com.astune.database.WebLink
import com.astune.model.LinkType.*

@Composable
fun LinkPanel(
    parentId:Int,
    linkViewModel: LinkViewModel = hiltViewModel(),
    onNavigationToSubApplication: (String) -> Unit,
){
    val linkList = linkViewModel.getLinkList(parentId) + NewLink("", parentId)

    LinkScreen(
        linkList = linkList,
        parentDevice = linkViewModel.getParentDevice(parentId),
        onLinkInserted = { linkViewModel.insertLink(it) },
        onLinkDeleted = { linkViewModel.deleteLink(it) },
        onNavigationToSubApplication = onNavigationToSubApplication,
    )

}

@Composable
internal fun LinkScreen(
    linkList:List<Link>,
    parentDevice: Device,
    onLinkInserted: (Link) -> Unit = {},
    onLinkDeleted: (Link) -> Unit = {},
    onNavigationToSubApplication: (String) -> Unit = {},
){
    val parentId = parentDevice.id

    var showInsertDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    var position by remember { mutableStateOf(Offset.Zero) }
    var currentLink by remember { mutableStateOf<Link>(WebLink(-1, "", -1, "")) }

    var expended by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter){
        ClippedBackground(
            modifier = Modifier.blur(25.dp),
            offset = LocalRootUIState.current.positionInRoot,
            backgroundImg = LocalRootUIState.current.background,
            backgroundSize = LocalRootUIState.current.screenSize
        )
        Box(
            modifier = Modifier.onGloballyPositioned {
                position -= it.positionInRoot()
            }
        ){
            DropdownMenu(
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
                        showInsertDialog = true
                        showEditDialog = true
                        expended = false
                     }
                )
            }
        }

        LinkCardGrid(
            cardList = linkList,
            onClick = {
                      when(it.type){
                          NEW_LINK -> showInsertDialog = true
                          else -> onNavigationToSubApplication(getDestinationRoute(parentDevice, it))
                      }
            },
            onLongClick = {offset, link ->
                position = offset
                if(link.type!=NEW_LINK){
                    expended = true
                    currentLink = link
                }
            })

        if(showInsertDialog){
            Dialog(onDismissRequest = {
                showInsertDialog = false
                showEditDialog = false
            }){
                LinkSetting(
                    parentId = parentId,
                    link = if(showEditDialog) currentLink else EmptyLink(),
                    onComplete = {
                        onLinkInserted.invoke(it)
                        showInsertDialog = !showInsertDialog
                                 },
                    onDismiss = {showInsertDialog = !showInsertDialog})
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onLinkDeleted(currentLink)
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
                }
                },
                title = { Text("Delete?") },
                text = { Text("Do you really want to delete this link, it will be lost for a long time(very long)!") }
            )
        }


    }

}

private fun getDestinationRoute(parentDevice: Device, link: Link) : String{
    return when(link.type){
        WEB_LINK -> "${WEB_LINK.getName()}/${parentDevice.ip}:${link.info}"
        SSH_LINK -> "${SSH_LINK.getName()}/${parentDevice.ip}:${(link as SSHLink).info}"
        else -> throw IllegalArgumentException("illegal link type!")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme", )
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Composable
internal fun PanelPreview(){
    val linkList = ArrayList<Link>()
    for(i in 1..15){
        linkList.add(WebLink(0, "$i", 0, "test info"))
    }
    MCenterTheme {
        LinkScreen(linkList = linkList, Device(0, "0", "0", null))
    }
}