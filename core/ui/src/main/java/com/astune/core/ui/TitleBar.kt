package com.astune.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.astune.core.ui.design.MCenterTheme
import com.astune.core.ui.design.ThemePreview

@Composable
fun TitleBar(
    modifier: Modifier,
    title: String = "title",
    leftBtn: @Composable (Modifier) -> Unit = {},
    rightBtn: @Composable (Modifier) -> Unit = {},
){
    Surface (modifier = modifier.height(60.dp).fillMaxWidth(),
        color = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (left, right, titleT) = createRefs()

            Text(text = title,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = modifier.constrainAs(titleT){
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, 0.dp)
                })

            leftBtn(modifier.height(35.dp)
                .width(35.dp)
                .padding(0.dp)
                .constrainAs(left){
                    bottom.linkTo(parent.bottom, 0.dp)
                    start.linkTo(parent.start, 5.dp)
                },)

            rightBtn(modifier.height(35.dp)
                .width(35.dp)
                .padding(0.dp)
                .constrainAs(right){
                    bottom.linkTo(parent.bottom, 0.dp)
                    end.linkTo(parent.end, 5.dp)
                })

        }
    }
}

@Composable
fun ButtonTitlebar(
    modifier: Modifier = Modifier,
    titleBarValues: TitleBarValues = TitleBarValues.Default,
    onLeftBtnClicked: () -> Unit = {},
    onRightBtnClicked: () -> Unit = {},
    ){
    TitleBar(modifier,
        title = titleBarValues.title,
        leftBtn = {
            if(titleBarValues.enableLeft){
                IconButton(
                    modifier = it,
                    onClick = onLeftBtnClicked,
                ) {
                    Icon(
                        imageVector = titleBarValues.leftButtonImg,
                        "leftBtn",
                    )
                }
            }
        },
        rightBtn = {
            if (titleBarValues.enableRight){
                IconButton(
                    modifier = it,
                    onClick = onRightBtnClicked,
                ) {
                    Icon(
                        imageVector = titleBarValues.rightButtonImg,
                        "rightBtn",
                    )
                }
            }
        })
}


@ThemePreview
@Composable
fun TitleBarPreview(){
    MCenterTheme {
        ButtonTitlebar()
    }
}