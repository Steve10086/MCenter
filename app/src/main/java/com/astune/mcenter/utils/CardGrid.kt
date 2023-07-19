package com.astune.mcenter.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.astune.mcenter.R
import com.astune.mcenter.`object`.Link.Link
import com.astune.mcenter.`object`.Room.WebLink
import com.astune.mcenter.utils.enums.LinkType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinkCard(link: Link, modifier: Modifier, onClick: (Link) -> Unit = {}, onLongClick: (Link) -> Unit = {}){
    Surface (shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .sizeIn(maxHeight = 100.dp, maxWidth = 100.dp)
            .width(100.dp)
            .height(100.dp)
            .combinedClickable (
                onClick = { onClick(link) },
                onLongClick = { onLongClick(link) }
            ),
        elevation = 3.dp, color = Color.White
    ){
        ConstraintLayout {
            val (icon, name, info) = createRefs()
            val painter: Painter
            when (link.type){
                LinkType.WEB_LINK -> painter = painterResource(R.drawable.web_icon)

                LinkType.SSH_LINK -> painter = painterResource(R.drawable.ssh_icon)

                else -> {
                    painter = painterResource(R.drawable.round_shape)
                }
            }
            Image(painter = painter,
                "drawable/icon",
                modifier = Modifier
                    .defaultMinSize(minWidth = 80.dp, minHeight = 60.dp)
                    .width(80.dp).height(60.dp)
                    .constrainAs(icon){
                top.linkTo(parent.top, 5.dp)
                start.linkTo(parent.start, 10.dp)
            })

            Text(text = link.name, modifier = Modifier.constrainAs(name){
                top.linkTo(icon.bottom, 0.dp)
                start.linkTo(parent.start, 5.dp)
            })

            Text(text = link.info, fontSize = 10.sp, modifier = Modifier.constrainAs(info){
                top.linkTo(name.bottom, 0.dp)
                start.linkTo(parent.start, 5.dp)
            })

        }
    }
}

@Preview
@Composable
fun LinkCardPerview(){
    val link= WebLink(0, 0, "test", "12345")
    LinkCard(link, Modifier){
        Log.i("Link", link.name + " Clicked")
    }
}

@Preview
@Composable
fun CardListPerview(){
    val testLink: MutableList<Link> = ArrayList()
    for (i in 0..24) {
        testLink.add(WebLink(i, 0, "test$i", "123.456.789"))
    }
    LazyVerticalGrid(verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        columns = GridCells.Adaptive(minSize = 100.dp))
    {
        items(testLink){ card ->
            Row(modifier = Modifier, horizontalArrangement = Arrangement.Center){
                LinkCard(card, Modifier){

                }
            }

        }
    }
}


class LinkCardGrid @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AbstractComposeView(context, attrs){
    private var cardList = mutableStateOf(emptyList<Link>())
    private var onClick: ((Link) -> Unit) = {}
    private var onLongClick: ((Link) -> Unit) = {}

    fun setCard(link: List<Link>){
        Log.i("CardGrid" ,"val updated")
        cardList.value = link
    }

    fun setOnclickListener(onClick: (Link) -> Unit){
        this.onClick = onClick
    }

    fun setOnLongClickListener(onLongClick: (Link) -> Unit){
        this.onLongClick = onLongClick
    }

    /**
     * The Jetpack Compose UI content for this view.
     * Subclasses must implement this method to provide content. Initial composition will
     * occur when the view becomes attached to a window or when [createComposition] is called,
     * whichever comes first.
     */
    @Composable
    override fun Content() {
        LazyVerticalGrid(contentPadding = PaddingValues(20.dp)
            , verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            columns = GridCells.Adaptive(100.dp)){
            items(cardList.value){ card ->
                Row(horizontalArrangement = Arrangement.Center){
                    LinkCard(card, Modifier, onClick, onLongClick)
                }
            }
        }
    }
}