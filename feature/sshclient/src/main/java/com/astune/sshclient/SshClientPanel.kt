package com.astune.sshclient

import android.os.Build
import android.util.Log
import android.util.Size
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.astune.core.ui.CustomKeyColumn
import com.astune.core.ui.design.MCenterSshTheme
import com.astune.core.ui.design.SshThemes
import com.astune.core.ui.design.antaFamily
import com.astune.core.ui.design.firaCodeFamily
import com.astune.core.ui.design.ssh.MCSshTheme
import com.astune.model.ssh.CustomKey
import com.astune.model.ssh.CustomKeyLists
import com.astune.sshclient.fake.getContent

@Composable
fun SshClientPanel(
    viewModel: SshViewModel = hiltViewModel(),
    onExit: () -> Unit
){
    MCenterSshTheme(
        theme = viewModel.getClientTheme()
    ){
        SshCilent(
            user = viewModel.link?.username ?:"user",
            delay = viewModel.delay,
            isLoading = viewModel.isLoading,
            onWindowsSizeChanged = {
                viewModel.startWithWindowSize(Size(it.width, it.height))
            },
            onEdit = {viewModel.send(it)},
            onExit = {
                viewModel.stop()
                onExit.invoke()
            },
            onKeyClicked = {name, state -> viewModel.onKeyClicked(name, state) },
            displayText = viewModel.displayText,
            considerInsets = viewModel.considerInsets()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SshCilent(
    user:String = "user",
    delay:String = " ...",
    isLoading:Boolean = false,
    onWindowsSizeChanged:(IntSize) -> Unit = {},
    onExit:() -> Unit = {},
    onEdit:(Char) -> Unit = {},
    onKeyClicked:(CustomKey, Boolean) -> Boolean = { _, _->true},
    displayText:AnnotatedString,
    considerInsets: Boolean = false
){
    Column(
        modifier = Modifier.fillMaxWidth().background(color = MCSshTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())
        SshClientHeader(
            user, delay, onExit
        )
        Box{
            SshClientContent(
                text = displayText,
                onEdit = onEdit,
                isLoading = isLoading,
                considerInsets = considerInsets,
                onWindowsSizeChanged = onWindowsSizeChanged,
            )
            androidx.compose.animation.AnimatedVisibility(
                visible = Build.VERSION.SDK_INT < 30 || WindowInsets.isImeVisible,
                enter = slideIn {IntOffset(0, it.height)},
                exit = slideOut {IntOffset(0, it.height)},
                content = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        CustomKeyColumn(
                            modifier = Modifier.imePadding(),
                            keyList = CustomKeyLists.Shell.keyList,
                            backGroundColor = MCSshTheme.colorScheme.tertiary,
                            keyColor = MCSshTheme.colorScheme.secondary,
                            textColor = MCSshTheme.colorScheme.onSecondary,
                            onKeyClicked = onKeyClicked
                        )
                    }
                }
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)
@Composable
fun SshClientContent(
    text:AnnotatedString,
    onEdit: (Char) -> Unit,
    isLoading: Boolean,
    considerInsets: Boolean,
    onWindowsSizeChanged: (IntSize) -> Unit,
){
    val scrollState = rememberScrollState(0)
    var contentHeight by remember { mutableIntStateOf(0) }
    LaunchedEffect(contentHeight){
        scrollState.scrollTo(scrollState.maxValue)
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp)
            .verticalScroll(scrollState)
            .onSizeChanged {
                contentHeight = it.height
                           },
        color = Color.Transparent,
    ) {
        val dummyInput = TextFieldValue("          \n", selection = TextRange(1))
        val focusRequester = remember{ FocusRequester() }
        var onInput by remember { mutableStateOf(false) }
        var fontWidth = 0
        var fontHeight = 0
        Box(modifier = Modifier.sizeIn(minWidth = 0.dp, minHeight = 0.dp)){
            BasicTextField(
                modifier = Modifier
                    .onGloballyPositioned {
                        fontWidth = it.size.width / 9
                        fontHeight = it.size.height / 2
                    }
                    .focusRequester(focusRequester),
                value = dummyInput,
                onValueChange = {
                    if(it.text.length > dummyInput.text.length){
                        Log.d("SSH", "sending a ${it.text[it.selection.start - 1]}")
                        onEdit(it.text[it.selection.start - 1])
                    }else if (it.text.length < dummyInput.text.length){
                        Log.d("SSH", "sending a backspace")
                        onEdit((8).toChar())
                    }
                },
                textStyle = TextStyle(fontFamily = firaCodeFamily, fontSize = 15.sp, lineHeight = 15.sp),
                cursorBrush = Brush.horizontalGradient(colors = listOf(Color.Transparent, Color.Transparent))
            )
        }
        if(isLoading){
            Box(
                modifier = Modifier.
                fillMaxSize().
                onGloballyPositioned {
                    val width = it.size.width / fontWidth - 3
                    val height = it.size.height / fontHeight
                    onWindowsSizeChanged(IntSize(width, height))
                },
                contentAlignment = Alignment.Center
            ){
                LinearProgressIndicator()
            }
        }else{
            val softwareKeyboardController = LocalSoftwareKeyboardController.current
            var isImeOpen by remember { mutableStateOf(true) }
            if(Build.VERSION.SDK_INT >= 30) {
                isImeOpen = WindowInsets.isImeVisible
            }
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() })
                        {
                            onInput = if(!onInput || !isImeOpen){
                                focusRequester.requestFocus()
                                softwareKeyboardController?.show()
                                true
                            }else{
                                focusRequester.freeFocus()
                                softwareKeyboardController?.hide()
                                false
                            }
                        }.then(
                            if (considerInsets) {
                                Modifier.imePadding()
                            } else {
                                Modifier
                            }
                        ),
                    text = text,
                    fontFamily = firaCodeFamily,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    color = MCSshTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
@Preview(widthDp = 412)
fun SSHClientPreview(){
    MCenterSshTheme(
        theme = SshThemes.black
    ){
        SshCilent(
            displayText = getContent().toAnnotatedString(),
            isLoading = false
        )
    }
}

