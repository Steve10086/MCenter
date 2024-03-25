package com.astune.sshclient

import android.util.Log
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astune.core.ui.design.MCenterSshTheme
import com.astune.core.ui.design.SshThemes
import com.astune.core.ui.design.antaFamily
import com.astune.core.ui.design.firaCodeFamily
import com.astune.core.ui.design.ssh.MCSshTheme
import com.astune.core.ui.spToPx
import com.astune.sshclient.fake.getTopContent
import kotlin.math.roundToInt

@Composable
fun SshShellPanel(
    viewModel: SshViewModel = hiltViewModel(),
    onExit: () -> Unit
){
    MCenterSshTheme(
        theme = viewModel.getClientTheme()
    ){
        SshShell(
            user = viewModel.link?.username ?:"user",
            delay = viewModel.delay,
            isLoading = viewModel.isLoading,
            onWindowsSizeChanged = {
                viewModel.setWindowSize(Size(it.width, it.height))
            },
            onEdit = {viewModel.send(it)},
            onExit = {
                viewModel.stop()
                onExit.invoke()
            },
            displayText = viewModel.displayText
        )
    }
}

@Composable
fun SshShell(
    user:String = "user",
    delay:String = " ...",
    isLoading:Boolean = false,
    onWindowsSizeChanged:(IntSize) -> Unit = {},
    onExit:() -> Unit = {},
    onEdit:(Char) -> Unit = {},
    displayText:AnnotatedString,
){
    val contentSize = spToPx(15f, LocalContext.current).roundToInt()
    val contentHeight = spToPx(24f, LocalContext.current).roundToInt()
    Column(
        modifier = Modifier.fillMaxWidth().background(color = MCSshTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        SshClientHeader(
            user, delay, onExit
        )
        if(isLoading){
            Box(
                modifier = Modifier.
                    fillMaxSize().
                    onGloballyPositioned {
                        val width = it.size.width / contentSize
                        val height = it.size.height / contentHeight
                        onWindowsSizeChanged(IntSize(width, height))
                                         },
                contentAlignment = Alignment.Center
            ){
                LinearProgressIndicator()
            }
        }else{
            SshClientContent(
                text = displayText,
                onEdit = onEdit
            )
        }
    }
}

@Composable
fun SshClientHeader(
    user:String = "user",
    delay:String = " ...",
    onExit:() -> Unit = {}
){
    Surface(
        modifier = Modifier.height(60.dp),
        color = MCSshTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.large
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user + delay,
                color = MCSshTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp).offset(y = (-2).dp),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = antaFamily,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                IconButton(
                    onClick = onExit
                ){
                    Icon(imageVector = Icons.Default.ArrowBack, tint = MCSshTheme.colorScheme.onPrimary, contentDescription = "Exit")
                }
            }
        }
    }
}

@Composable
fun SshClientContent(
    text:AnnotatedString,
    onEdit: (Char) -> Unit,
){
    Surface(
        modifier = Modifier.fillMaxSize().padding(top = 0.dp).verticalScroll(rememberScrollState()),
        color = Color.Transparent,
    ) {

        Text(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            text = text,
            fontFamily = firaCodeFamily,
            color = MCSshTheme.colorScheme.onSecondary,
            fontSize = 15.sp,
            lineHeight = 20.sp
        )

        val dummyInput = TextFieldValue("     ", selection = TextRange(4))
        BasicTextField(modifier = Modifier.fillMaxSize(), value = dummyInput, onValueChange = {
            if(it.text.length > dummyInput.text.length){
                Log.d("SSH", "sending a ${it.text[it.selection.start - 1]}")
                onEdit(it.text[it.selection.start - 1])
            }else{
                Log.d("SSH", "sending a backspace")
                onEdit((8).toChar())
            }
        }, cursorBrush = Brush.horizontalGradient(colors = listOf(Color.Transparent, Color.Transparent)))

    }
}

@Composable
@Preview(widthDp = 412)
fun SSHClientPreview(){
    MCenterSshTheme(
        theme = SshThemes.black
    ){
        SshShell(
            displayText = getTopContent().toAnnotatedString(),
            isLoading = false
        )
    }
}

