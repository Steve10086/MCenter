package com.astune.mcenter.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.astune.core.ui.*
import com.astune.core.ui.design.MCenterTheme
import com.astune.mcenter.R
import com.astune.mcenter.navigation.McNavHost
import com.astune.mcenter.navigation.TopLevelRoute
import com.astune.model.UserInfo

@Composable
fun McApp(
    mcBackgroundViewModel:MCBackgroundViewModel = hiltViewModel(),
){
    val mcAppState = rememberMCAppState()


    McBackground (
        onCaptureUserInfo = {mcBackgroundViewModel.getUserInfo()},
        mcAppState = mcAppState,
        onNavigationToSetting = {
            mcAppState.navigateTo(TopLevelRoute.SETTING.routeName)
                                },
    ){
        McNavHost(
            modifier = Modifier.fillMaxSize(),
            mcAppState = mcAppState,
        )
    }
}

@Composable
internal fun McBackground(
    mcAppState: MCAppState,
    onNavigationToSetting: () ->Unit = {},
    onCaptureUserInfo: () -> UserInfo,
    content: @Composable () -> Unit = {},
){


    var screenSize by remember { mutableStateOf(Size.Zero) }

    var isSideBar by remember { mutableStateOf(false) }

    var userInfo by remember { mutableStateOf(UserInfo()) }

    val uiState by remember { mutableStateOf(UIState()) }

    val des = mcAppState.currentDestination?.route

    BackHandler(enabled = isSideBar){
        isSideBar = false
    }

    Column{
        if(mcAppState.currentDestination?.route in TopLevelRoute.nameList()){
            ButtonTitlebar(
                modifier = Modifier,
                titleBarValues = if(isSideBar)
                    TitleBarValues.SIDE_BAR else mcAppState.getTitleBarValues(mcAppState.currentDestination?.route),
                onLeftBtnClicked = {
                    uiState.leftBtnClicked.invoke()

                    des?.let { mcAppState.onLeftBtnClicked(it) }

                    if(des == TopLevelRoute.DEVICE.routeName){
                        if(isSideBar){
                            isSideBar = false
                            onNavigationToSetting.invoke()
                        }else{
                            isSideBar = true
                        }
                    }

                                   },
                onRightBtnClicked = {
                    uiState.rightBtnClicked.invoke()

                    des?.let { mcAppState.onRightBtnClicked(it) }
                }
            )
        }

        Box{
            var position by remember { mutableStateOf(Offset.Zero) }
            var background by remember { mutableStateOf(ImageBitmap(1, 1)) }

            CaptureBitmap(
                key = mcAppState.currentDestination?.route == "device",
                onBitmapCaptured = {
                        background = it.asImageBitmap()
                        //Log.i("Background", "getBitmap")
                                       },
            ){
                    Image(
                        modifier = Modifier
                            .onSizeChanged { screenSize = it.toSize() }
                            .fillMaxSize(),
                        painter = painterResource(R.drawable.background),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight)

                    Box(modifier =
                    Modifier.background(
                        brush = Brush.verticalGradient(
                            listOf(MaterialTheme.colorScheme.inverseOnSurface,
                                Color.Transparent, Color.Transparent)
                    )).fillMaxSize()){
                    }
               }

            Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
                    position = it.positionInRoot()
                }){

                val offset by animateDpAsState(
                    targetValue = if(!isSideBar) 0.dp else with(LocalDensity.current){ (screenSize.width / 2).toDp()},
                    label = "slide out")

                Box(modifier = Modifier.offset(offset)){
                    CompositionLocalProvider(
                        LocalRootUIState provides uiState.merge(
                            UIState(
                                background = background,
                                screenSize = screenSize,
                                positionInRoot = position)
                        )
                    ){
                        content()
                    }
                }

                BackHandler(isSideBar){
                    isSideBar = false
                }

                Box {
                    if(isSideBar) {
                        userInfo = onCaptureUserInfo.invoke()

                        Box(
                            modifier = Modifier.alpha(0.1f).fillMaxSize().background(brush = Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black)
                            )).clickable {
                                isSideBar = false
                            }) {
                        }
                    }

                    Column {
                        AnimatedVisibility(
                            visible = isSideBar,
                            enter = expandHorizontally(),
                            exit = shrinkHorizontally()
                        ){
                            SideBar(
                                offset = position,
                                background = background,
                                modifier = Modifier
                                    .fillMaxWidth(0.4F),
                                scrSize = screenSize,
                                userInfo = userInfo)
                        }
                    }

                }

                }
        }
    }
}
@Composable
internal fun SideBar(
    modifier: Modifier,
    scrSize:Size = Size(1024F, 2114F),
    userInfo: UserInfo,
    background: ImageBitmap = LocalRootUIState.current.background,
    offset: Offset = Offset.Zero
){
    //val screenSize by remember { mutableStateOf(scrSize) }

    Surface (
        modifier = modifier.fillMaxSize())
    {
        Box(modifier = Modifier.fillMaxSize()){

            ClippedBackground(
                modifier = Modifier.blur(radius = 25.dp),
                offset = offset,
                backgroundImg = background,
                backgroundSize = scrSize
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.height(130.dp).fillMaxWidth()){
                    Image(bitmap = userInfo.avatar.asImageBitmap(), "", modifier = Modifier.size(100.dp, 100.dp))
                }
                Box(modifier = Modifier.fillMaxWidth(0.8f)){
                    Column {
                        Text(userInfo.name, style = MaterialTheme.typography.bodyLarge)
                        Text("Email:", style = MaterialTheme.typography.headlineSmall)
                        Text(userInfo.email, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SideBarPreview(){
    var size by remember { mutableStateOf(Size(1024F, 2414F)) }
    Box(modifier = Modifier.onSizeChanged {
        size = it.toSize()
    }.fillMaxSize()){
        SideBar(
            modifier = Modifier.fillMaxWidth(0.5f),
            scrSize = size,
            userInfo = UserInfo(
                email = "test@gmail.com"
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Composable
fun BackgroundPreview(){
    MCenterTheme {
        McBackground(onCaptureUserInfo = { UserInfo() }, mcAppState = MCAppState(rememberNavController())){
        }
    }
}