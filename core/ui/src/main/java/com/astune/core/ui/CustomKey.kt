package com.astune.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.astune.core.ui.design.MCenterTheme
import com.astune.core.ui.design.ThemePreview

@Composable
fun Key(
    modifier: Modifier = Modifier,
    key: CustomKey,
    onKeyClicked: (String) -> Unit,
){
    Surface(
        modifier = modifier
            .padding(5.dp)
            .clickable {
                       onKeyClicked(key.id)
                       },
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.onPrimary,
    ) {
        Box(modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            contentAlignment =  Alignment.Center){
            Text(
                text = key.text,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun CustomKeyColumn(
    modifier: Modifier = Modifier,
    keyList: List<CustomKey> = emptyList(),
    onKeyClicked: (String) -> Unit = {}
    ){
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(contentAlignment = Alignment.CenterStart){
            LazyRow(
                modifier = modifier.padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                items(keyList){key ->
                    Key(
                        key = key,
                        onKeyClicked = onKeyClicked
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
            keyList = CustomKeyLists.Shell.keyList
        )
    }
}

@ThemePreview
@Composable
fun CustomKeyPreview(){
    MCenterTheme {
        Key(
            key = CustomKey.Ctrl,
            onKeyClicked = {}
        )
    }
}