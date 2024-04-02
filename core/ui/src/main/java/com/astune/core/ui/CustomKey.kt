package com.astune.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.astune.core.ui.design.MCenterTheme
import com.astune.core.ui.design.ThemePreview

@Composable
fun Key(
    modifier: Modifier = Modifier,
    key: CustomKey,
    onKeyClicked: (CustomKey, Boolean) -> Boolean,
    keyColor: Color = MaterialTheme.colorScheme.onPrimary,
    textColor: Color = MaterialTheme.colorScheme.primary
){
    var state by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .padding(5.dp)
            .clickable {
                       state = onKeyClicked(key, state)
                       },
        shape = MaterialTheme.shapes.extraSmall,
        color = keyColor.copy(
            alpha = (if(state)0.5 else 1).toFloat()
        ),
    ) {
        Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            contentAlignment =  Alignment.Center){
            Text(
                text = key.text,
                color = textColor
            )
        }
    }
}


@Composable
fun CustomKeyColumn(
    modifier: Modifier = Modifier,
    keyList: List<CustomKey> = emptyList(),
    backGroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    keyColor: Color = MaterialTheme.colorScheme.onPrimary,
    textColor: Color = MaterialTheme.colorScheme.primary,
    onKeyClicked: (CustomKey, Boolean) -> Boolean = {_,_->true},
    ){
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backGroundColor
    ) {
        Box(contentAlignment = Alignment.CenterStart){
            LazyRow(
                modifier = modifier.padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                items(keyList){key ->
                    Key(
                        key = key,
                        onKeyClicked = onKeyClicked,
                        keyColor = keyColor,
                        textColor = textColor
                    )
                }
            }
        }
    }
}

@ThemePreview
@Composable
fun CustomKeyColumnPreview(){
    MCenterTheme {
        CustomKeyColumn(
            keyList = CustomKeyLists.Shell.keyList,
            onKeyClicked = {_,_->false}
        )
    }
}

@ThemePreview
@Composable
fun CustomKeyPreview(){
    MCenterTheme {
        Key(
            key = CustomKey.Ctrl,
            onKeyClicked = {_,_->true}
        )
    }
}