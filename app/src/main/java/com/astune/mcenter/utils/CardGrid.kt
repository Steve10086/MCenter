package com.astune.mcenter.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.astune.mcenter.R
import com.astune.mcenter.`object`.Link.Link
import com.astune.mcenter.`object`.Room.WebLink

@Composable
fun LinkCard(link: Link, modifier: Modifier, onClick: () -> Unit){
    Surface (shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .padding(5.dp)
            .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
            .width(50.dp)
            .height(50.dp),
        elevation = 3.dp, color = Color.White){
        ConstraintLayout {
            val (icon, name, info) = createRefs()

            Image(painter = painterResource(R.drawable.ssh_icon),
                "drawable/icon",
                modifier = Modifier
                    .defaultMinSize(minWidth = 80.dp, minHeight = 60.dp)
                    .width(60.dp).height(60.dp)
                    .constrainAs(icon){
                top.linkTo(parent.top, 5.dp)
                start.linkTo(parent.start, 10.dp)
            })
        }
    }
}

@Preview
@Composable
fun LinkCardPerview(){
    val link= WebLink(0, 0, "test", "12345")
    LinkCard(link, modifier = Modifier){
        Log.i("Link", link.name + " Clicked")
    }
}


class LinkCardGrid @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AbstractComposeView(context, attrs){
    private var cardList = mutableStateOf(emptyList<Link>())
    private var onClick: ((Link) -> Unit)? = null

    fun setCard(link: List<Link>){
        Log.i("CardGrid" ,"val updated")
        cardList.value = link
    }

    fun setOnclickListener(onClick: (Link) -> Unit){
        this.onClick = onClick
    }

    /**
     * The Jetpack Compose UI content for this view.
     * Subclasses must implement this method to provide content. Initial composition will
     * occur when the view becomes attached to a window or when [createComposition] is called,
     * whichever comes first.
     */
    @Composable
    override fun Content() {
        LazyVerticalGrid(columns = GridCells.Adaptive(16.dp)){
            items(cardList.value){ card ->
                LinkCard(card, modifier = Modifier.padding(10.dp)){
                    onClick?.invoke(card)
                }
            }
        }
        TODO("Not yet implemented")
    }
}