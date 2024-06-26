package com.astune.link.subPanels

import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.astune.core.ui.design.MCenterTheme
import com.astune.core.ui.design.ThemePreview
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

@Composable
fun WebLinkPage(
    uri:String,
    onExit: () -> Unit,
){
    WebViewScreen(
        uri = uri,
        onCreate = { initSetting(it) },
        onExit = onExit
    )
}

@Composable
fun WebViewScreen(
    uri:String = "",
    onCreate: (WebView) -> Unit = {},
    onExit: () -> Unit = {},
){

    val navigator = rememberWebViewNavigator()

    Surface(modifier = Modifier.fillMaxSize()) {

        var progress by remember { mutableFloatStateOf(0f) }
        var titleText by remember { mutableStateOf("Loading...$progress%") }

        Column {

            Surface(modifier = Modifier.height(60.dp).fillMaxWidth(),
                color = MaterialTheme.colorScheme.inverseOnSurface) {
                Column {
                    Spacer(
                        modifier = Modifier.height(15.dp)
                    )
                    Row (modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ){
                        IconButton(
                            onClick = onExit
                        ){
                            Icon(imageVector = Icons.Default.ArrowBack, "Exit")
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(35.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant),
                            contentAlignment = Alignment.CenterStart){
                            Text(titleText,
                                modifier = Modifier.padding(start = 10.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                overflow = TextOverflow.Ellipsis)
                        }

                        IconButton(
                            onClick = {
                                if (progress == 1f){
                                    navigator.reload()
                                }else{
                                    navigator.stopLoading()
                                    progress = 1f
                                }
                            }
                        ){
                            Icon(if(progress < 1f) Icons.Default.Close else Icons.Default.Refresh, "")
                        }
                    }
                }

            }

            AnimatedVisibility(visible = (progress > 0f && progress < 1f)){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = progress)
            }

            Box(){
                val chromeClient = object : AccompanistWebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) { // bind the loading progressBar
                        Log.i("webPage", "Loading...${newProgress}")
                        progress = newProgress / 100f
                    }
                    override fun onReceivedTitle(view: WebView?, title: String?) { // bind the web title
                        super.onReceivedTitle(view, title)
                        Log.i("webPage",title.toString())
                        titleText = title?:"Loading"
                    }
                }

                WebView(
                    modifier = Modifier.fillMaxSize(),
                    captureBackPresses = true,
                    state = rememberWebViewState(uri),
                    navigator = navigator,
                    chromeClient = chromeClient,
                    onCreated = {
                        onCreate.invoke(it)
                    },
                )
            }

        }
    }
}

fun initSetting(webView: WebView){
    val settings = webView.settings
    settings.pluginState = WebSettings.PluginState.ON
    settings.blockNetworkLoads = false

    //appearance

    //appearance
    settings.builtInZoomControls = true
    settings.useWideViewPort = true
    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
    settings.loadWithOverviewMode = true

    //accessibility

    //accessibility
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.databaseEnabled = true
    settings.javaScriptCanOpenWindowsAutomatically = true
    settings.allowFileAccess = true
    settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
    settings.displayZoomControls = false
}
@ThemePreview
@Composable
internal fun AppPreview(){
    MCenterTheme {
        WebViewScreen()
    }
}
