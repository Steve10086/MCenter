package com.astune.core.ui.design

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCompat(
    modifier: Modifier = Modifier,
    titleText:String,
    value:String,
    onValueChange: (String) -> Unit,
    enabled:Boolean = true,
    singleLine:Boolean = true,
    contentPadding:PaddingValues = PaddingValues(start = 10.dp)
){
    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically){
        Text(titleText, style = MaterialTheme.typography.titleLarge)

        BasicTextField(
            modifier = Modifier.height(50.dp).fillMaxWidth(0.8f),
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyLarge.merge(TextStyle(color = MaterialTheme.colorScheme.onSurface)),
            singleLine = singleLine,
            enabled = enabled,
        ) { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                value = value,
                innerTextField = innerTextField,
                visualTransformation = VisualTransformation.None,
                singleLine = singleLine,
                enabled = enabled,
                interactionSource = interactionSource,
                contentPadding = contentPadding,
            )
        }
    }
}

@ThemePreview
@Composable
fun TextCompatPreview(){
    MCenterTheme {
        Surface(modifier = Modifier.padding(10.dp).size(300.dp, 100.dp), ) {
            TextCompat(
                titleText = "Title",
                value = "value",
                onValueChange = {}
            )
        }
    }


}

@Composable
fun Spinner(
    modifier: Modifier = Modifier,
    titleText: String,
    values:List<String>,
    onValueChange: (String) -> Unit,
    initialValue:String = "",
    ){
    var value by remember { mutableStateOf(initialValue) }
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier, contentAlignment = Alignment.Center){
        Row(verticalAlignment = Alignment.CenterVertically){
            Box{
                TextCompat(titleText = titleText, value = value, onValueChange = {}, enabled = false)
                DropdownMenu(
                    offset = DpOffset(50.dp, 0.dp),
                    expanded = expanded,
                    onDismissRequest = {expanded = !expanded},
                ){
                    for (v in values){
                        DropdownMenuItem(
                            text = { Text(v) },
                            onClick = {
                                value = v
                                expanded = !expanded
                                onValueChange(v)
                            }
                        )
                    }
                }
            }
            Button(onClick = {expanded = !expanded}, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), contentPadding = PaddingValues(0.dp)){
                when(expanded){
                    false -> Icon(Icons.Sharp.ArrowBack, "", tint = MaterialTheme.colorScheme.onBackground)
                    true -> Icon(Icons.Sharp.KeyboardArrowDown, "", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

    }

}

@Preview
@Composable
fun SpinnerPreview(){
    val list = listOf("test1", "test2", "test3")
    Spinner(titleText = "title",
        values = list,
        onValueChange = {})
}