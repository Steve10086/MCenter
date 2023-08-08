package com.astune.core.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import com.astune.database.Link.Link
import com.astune.database.Link.NewLink
import com.astune.database.WebLink
import com.astune.model.LinkType.*


class RoundedCornerRectangleShape(
    private val cornerRadius: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(
            RoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                topLeftCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
                topRightCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
                bottomLeftCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
                bottomRightCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
            ),
        )
    }
}

/**
 * a single link grid
 * structure:
 * normal: surface(ConstraintLayout(icon, name, info))
 * newLink: surface(row(icon))
 *
 * clickable: onClick & onLongClick both detect on surface
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinkCard(link: Link,
             modifier: Modifier,
             onClick: (Link) -> Unit = {},
             onLongClick: (Link) -> Unit = {}){
    Surface (shape = RoundedCornerRectangleShape(25.dp),
        modifier = modifier
            .sizeIn(maxHeight = 100.dp, maxWidth = 80.dp)
            .width(80.dp)
            .height(100.dp)
            .combinedClickable (
                onClick = { onClick(link) },
                onLongClick = { onLongClick(link) }
            ),
        elevation = 3.dp, color = Color.White
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

@Preview
@Composable
fun LinkCardPerview(){
    val link= WebLink(0, "test",0,  "12345")
    LinkCard(link, Modifier){
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
fun LinkCardGrid(modifier: Modifier, cardList: MutableList<Link>, onClick: (Link) -> Unit = {}, onLongClick: (Link) -> Unit = {}){
    LazyVerticalGrid(contentPadding = PaddingValues(20.dp)
        , verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        columns = GridCells.Adaptive(80.dp)){
        items(cardList){ card ->
            Row(horizontalArrangement = Arrangement.Center){
                LinkCard(card, modifier, onClick, onLongClick)
            }
        }
    }
}
enum class SubCardInfill{ICON, DEFAULT}