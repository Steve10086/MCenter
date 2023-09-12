package com.astune.core.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.astune.core.ui.design.RoundedCornerRectangleShape
import com.astune.database.Link.Link
import com.astune.database.Link.NewLink
import com.astune.database.WebLink
import com.astune.model.LinkType.*


/**
 * a single link grid
 * structure:
 * normal: surface(ConstraintLayout(icon, name, info))
 * newLink: surface(row(icon))
 *
 * clickable: onClick & onLongClick both detect on surface
 */
@Composable
fun LinkCard(link: Link,
             modifier: Modifier,
             onClick: (Link) -> Unit = {},
             onLongClick: (Offset, Link) -> Unit = {_,_->},
             ){
    Box (modifier = Modifier.pointerInput(Unit){
        detectTapGestures(
            onLongPress = {
                onLongClick(it, link)
            },
            onTap = {
                onClick(link)
            }
        )
    }){
        Surface (shape = RoundedCornerRectangleShape(25.dp),
            modifier = modifier
                .sizeIn(maxHeight = 100.dp, maxWidth = 80.dp)
                .width(80.dp)
                .height(100.dp),
            color = MaterialTheme.colorScheme.surface,
        ){
            if(link.type == NEW_LINK){
                Row(horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(20.dp)){
                    Image(painterResource(R.drawable.plus_icon),
                        "insert_link_pic",
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp))
                }
            }else{
                val icon:Painter = when(link.type){
                    SSH_LINK -> painterResource(R.drawable.ssh_icon)
                    WEB_LINK -> painterResource(R.drawable.web_icon)
                    NEW_LINK -> painterResource(R.drawable.plus_icon)
                    EMPTY_LINK -> throw IllegalArgumentException("illegal link type!")
                }
                ConstraintLayout {
                    val (iconRef, titleRef, infoRef) = createRefs()
                    Image(painter = icon,
                        "icon",
                        modifier = Modifier
                            .defaultMinSize(minWidth = 80.dp, minHeight = 60.dp)
                            .width(80.dp).height(60.dp)
                            .constrainAs(iconRef){
                                top.linkTo(parent.top, 5.dp)
                            })

                    Text(text = link.name, modifier = Modifier.constrainAs(titleRef){
                        top.linkTo(iconRef.bottom, 0.dp)
                        start.linkTo(parent.start, 5.dp)
                    })

                    Text(text = link.info, fontSize = 10.sp, modifier = Modifier.constrainAs(infoRef){
                        top.linkTo(titleRef.bottom, 0.dp)
                        start.linkTo(parent.start, 5.dp)
                    })
                }
            }
        }
    }

}

@Preview
@Composable
fun LinkCardPerview(){
    val link= WebLink(0, "test",0,  "12345")
    LinkCard(link, Modifier){_,_->
        Log.i("Link", link.name + " Clicked")
    }
}

@Preview
@Composable
fun CardListPerview(){
    val testLink: MutableList<Link> = ArrayList()
    for (i in 0..20) {
        testLink.add(WebLink(i, "test$i", 0, "123.456.789"))
    }
    testLink.add(NewLink("test", 0))
    LinkCardGrid(modifier = Modifier, testLink)
}

@Composable
fun LinkCardGrid(modifier: Modifier = Modifier,
                 cardList: List<Link>, onClick: (Link) -> Unit = {},
                 onLongClick: (Offset, Link) -> Unit = {_,_->}){
    LazyVerticalGrid(contentPadding = PaddingValues(20.dp)
        , verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        columns = GridCells.Adaptive(80.dp)){
        items(cardList,key = {it.id}){ card ->
            var globalPosition by remember { mutableStateOf(Offset.Zero) }
            Row(modifier = Modifier.onGloballyPositioned {
                globalPosition = it.positionInParent()
            },horizontalArrangement = Arrangement.Center){
                LinkCard(card, modifier, onClick, onLongClick = {offset,link-> onLongClick(globalPosition+offset, link) })
            }
        }
    }
}
enum class SubCardInfill{ICON, DEFAULT}