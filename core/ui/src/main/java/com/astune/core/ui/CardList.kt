package com.astune.core.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.astune.database.Device


/**
 * single device card
 * structure: surface(ConstraintLayout(name n/ ip n/ state | btn))
 *
 * Clickable: onClick detect on btn, onLongClick detect on surface
 */
@Composable
fun DeviceCard (device: Device,
                modifier: Modifier,
                onButtonClick: (Device) -> Unit = {},
                onLongClick: (Offset, Device) -> Unit = { _,_ -> },
) {
    var globalPosition = Offset(0F, 0F)
    Box(modifier = Modifier.pointerInput(Unit)
    {
        detectTapGestures(
            onLongPress = {
                onLongClick(globalPosition + it, device)
            })
    }.onGloballyPositioned
    {
        globalPosition = it.positionInParent()
    }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier
                .padding(5.dp)
                .defaultMinSize(minWidth = 284.dp, minHeight = 140.dp)
                .width(300.dp)
                .height(100.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            ConstraintLayout(Modifier.height(164.dp)) {
                val (name, ip, lastOnline, infoBtn) = createRefs()

                Text(
                    text = device.name,
                    Modifier.width(201.dp)
                        .padding(0.dp, 0.dp, 20.dp, 0.dp)
                        .constrainAs(name) {
                            top.linkTo(parent.top, 5.dp)
                            start.linkTo(parent.start, 0.dp)
                            end.linkTo(infoBtn.start, 0.dp)
                        },
                    fontSize = 25.sp,
                    textAlign = TextAlign.Left,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Text(device.ip, Modifier.constrainAs(ip) {
                    top.linkTo(name.bottom, 2.dp)
                    start.linkTo(parent.start, 15.dp)
                })
                if(device.enableDelay == 1){
                    if (device.loading){
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp).constrainAs(lastOnline){
                                bottom.linkTo(parent.bottom, 10.dp)
                                start.linkTo(parent.start, 15.dp)
                            }
                        )
                    }else{
                        Text(
                            if(device.delay == "") "offline" else device.delay,
                            Modifier.constrainAs(lastOnline) {
                                bottom.linkTo(parent.bottom, 10.dp)
                                start.linkTo(parent.start, 15.dp)
                            })
                    }
                }

                Button(onClick = { onButtonClick(device) }, Modifier
                    .constrainAs(infoBtn) {
                        end.linkTo(parent.end, 5.dp)
                    }
                    .defaultMinSize(minWidth = 64.dp)
                    .fillMaxHeight(),
                    shape = RectangleShape,
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(text = ">", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CardPerview(){
    val device = Device(0, "testDeviceNameeeeeeeeeeeeeeeeeeeee", "192.168.1.test", null)
    device.loading = true
    DeviceCard(
        device = device,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun CardListPerview(){
    val list = ArrayList<Device>()
    for(id in 0..20){
        list.add(Device(0, "testDeviceNameeeeeeeeeeeeeeeeeeeee", "192.168.1.test", "0"))
        id.takeIf {
            it % 2 == 0
        }?.let {
            list[id].loading = true
        }
    }
    DeviceCardList(
        modifier = Modifier.padding(8.dp),
        cardList = list
    )
}

@Composable
fun DeviceCardList(
    modifier: Modifier,
    cardList: List<Device>,
    onLongClick: (Offset, Device) -> Unit = { _, _ ->},
    onButtonClick: (Device) -> Unit = {},
){
    LazyColumn(
        modifier = modifier
            .padding(all = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(cardList) { card ->
            DeviceCard(card, modifier = Modifier.padding(10.dp), onButtonClick = onButtonClick, onLongClick = onLongClick)
        }
    }
}