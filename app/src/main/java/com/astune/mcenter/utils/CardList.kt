package com.astune.mcenter.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.astune.mcenter.`object`.Room.Device

/**
 * single device card
 * structure: surface(ConstraintLayout(name n/ ip n/ state | btn))
 *
 * Clickable: onClick detect on btn, onLongClick detect on surface
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceCard (device: Device, modifier: Modifier, onClick: (Device) -> Unit = {}, onLongClick: (Device) -> Unit = {}){
    Surface (
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(5.dp)
            .defaultMinSize(minWidth = 284.dp, minHeight = 140.dp)
            .width(300.dp)
            .height(100.dp)
            .combinedClickable(
            onClick = {},
            onLongClick = { onLongClick(device) }
        ),
        elevation = 3.dp, color = Color.White
    ) {
        ConstraintLayout(Modifier.height(164.dp)) {
            val (name, ip, lastOnline, infoBtn) = createRefs()

            Text(text = device.name,
                Modifier.width(201.dp)
                    .padding(0.dp, 0.dp, 20.dp, 20.dp)
                    .constrainAs(name) {
                        top.linkTo(parent.top, 5.dp)
                        start.linkTo(parent.start, 0.dp)
                        end.linkTo(infoBtn.start, 0.dp)
                                       },
                fontSize = 25.sp,
                textAlign = TextAlign.Left,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2)

            Text(device.ip, Modifier.constrainAs(ip) {
                bottom.linkTo(lastOnline.top, 5.dp)
                start.linkTo(parent.start, 15.dp)
            })

            Text(
                if (device.lastOnline != null) device.lastOnline.orEmpty() else "online",
                Modifier.constrainAs(lastOnline) {
                    bottom.linkTo(parent.bottom, 10.dp)
                    start.linkTo(parent.start, 15.dp)
            })

            Button(onClick = { onClick(device) }, Modifier
                .constrainAs(infoBtn){
                    end.linkTo(parent.end, 5.dp)
                }
                .defaultMinSize(minWidth = 64.dp)
                .fillMaxHeight(),
                shape = RectangleShape,
                elevation = ButtonDefaults.elevation(0.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, contentColor = Color.Black)
            ){
                Text(text = ">", textAlign = TextAlign.Center, fontSize = 30.sp)
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun CardPerview(){
    val device = Device(0, "testDeviceNameeeeeeeeeeeeeeeeeeeee", "192.168.1.test", null)
    DeviceCard(
        device = device,
        modifier = Modifier.padding(8.dp)
    ){
        Log.i("Card", device.id.toString() + " is clicked")
    }
}

class DeviceCardList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AbstractComposeView(context, attrs) {
    private var cardList = mutableStateOf(emptyList<Device>())
    private var onClick: ((Device) -> Unit) = { }
    private var onLongClick: ((Device) -> Unit) = { }
    fun setCard(devices: List<Device>){
        Log.i("CardList" ,"val updated")
        cardList.value = devices
    }

    fun setOnclickListener(onClick: (Device) -> Unit){
        this.onClick = onClick
    }

    fun setOnLongClickListener(onLongClick: (Device) -> Unit){
        this.onLongClick = onLongClick
    }

    /**
     * A lazy column containing device cards
     */
    @Composable
    override fun Content() {
        LazyColumn(modifier = Modifier.padding(all = 5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            items(cardList.value) { card ->
                DeviceCard(card, modifier = Modifier.padding(10.dp), onClick = onClick, onLongClick = onLongClick)
            }
        }
    }
}