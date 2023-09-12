package com.astune.setting
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.astune.core.ui.LocalRootUIState
import com.astune.core.ui.design.GradientColors
import com.astune.core.ui.design.MCenterTheme
import com.astune.core.ui.design.defaultGradientColor
import com.astune.core.ui.setOnRightBtnClicked
import com.astune.model.UserInfo

@Composable
fun SettingPanel(
    viewModel: SettingViewModel = hiltViewModel(),
){
    val userInfoToUse by remember { mutableStateOf(viewModel.getInfo()) }

    LocalRootUIState.current.setOnRightBtnClicked(key = "setting") {
        viewModel.updateUserInfo(userInfoToUse)
    }

    SettingScreen(userInfo = userInfoToUse,
        onThemeChanged = { viewModel.updateTheme(it) },
        onAvatarChanged = { viewModel.updateAvatar(it) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    gradientColors: GradientColors = defaultGradientColor(),
    userInfo: UserInfo = UserInfo(),
    onThemeChanged: (String) -> Unit = {},
    onAvatarChanged: (Uri) -> Bitmap?
){
    var email by remember { mutableStateOf(userInfo.email) }
    var name by remember { mutableStateOf(userInfo.name) }
    var enableZ by remember { mutableStateOf(userInfo.enabledZerotier) }
    var theme by remember { mutableStateOf(userInfo.theme) }
    var avatar by remember { mutableStateOf(userInfo.avatar) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                Log.i("Setting", "avatarUpdated")
                onAvatarChanged.invoke(it)?.let{avatar = it}
            }
        })


    Box(modifier = modifier){
        Surface(modifier = Modifier.fillMaxSize(), color = gradientColors.container) {
            Box(modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        gradientColors.top,
                        gradientColors.bottom
                    )
                )
            ))
            ConstraintLayout {
                val (avatarImg, nameText, emailText, zerotierSwitch, themeSwitch) = createRefs()
                Box(modifier = Modifier.size(120.dp).constrainAs(avatarImg){
                    top.linkTo(parent.top, 15.dp)
                    start.linkTo(parent.start, 15.dp)
                }){
                    Image(avatar.asImageBitmap(), "avatar")
                    Surface(modifier = Modifier.alpha(0.2f).fillMaxSize(),color = MaterialTheme.colorScheme.scrim){
                        Box(modifier = Modifier.fillMaxSize().clickable {
                            launcher.launch("image/*")
                        },
                            contentAlignment = Alignment.Center)
                        {
                            Text("edit", color = Color.White, fontSize = 30.sp)
                        }
                    }
                }
                Row (modifier = Modifier.constrainAs(nameText){
                    top.linkTo(parent.top, 15.dp)
                    start.linkTo(avatarImg.end, 5.dp)
                    end.linkTo(parent.end, 5.dp)},
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("name", style = MaterialTheme.typography.titleMedium)
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        value = name,
                        onValueChange = {
                            name = it
                            userInfo.name = it
                        },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent))
                }
                Row (modifier = Modifier.constrainAs(emailText){
                    top.linkTo(nameText.bottom, 5.dp)
                    start.linkTo(avatarImg.end, 5.dp)
                    end.linkTo(parent.end, 5.dp)},
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("email", style = MaterialTheme.typography.titleMedium)
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        value = email,
                        onValueChange = {
                            userInfo.email = it
                            email = it
                        },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent))
                }
                Row(modifier = Modifier.constrainAs(themeSwitch){
                    top.linkTo(avatarImg.bottom, 10.dp)
                    start.linkTo(parent.start, 15.dp) },
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text("Theme: $theme ", style = MaterialTheme.typography.titleMedium)
                    Switch(
                        checked = theme == "dark",
                        onCheckedChange = {
                            theme = when(it){
                                false -> {
                                    "light"
                                }

                                true -> {
                                    "dark"
                                }
                            }
                            onThemeChanged.invoke(theme)
                        })
                }
                Row(modifier = Modifier.constrainAs(zerotierSwitch){
                    top.linkTo(themeSwitch.bottom, 10.dp)
                    start.linkTo(parent.start, 15.dp) },
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text("enable Zerotier ", style = MaterialTheme.typography.titleMedium)
                    Switch(
                        checked = enableZ,
                        onCheckedChange = {
                            userInfo.enabledZerotier = it
                            enableZ = it
                        })
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreview

@ThemePreview
@Composable
fun SettingScreenPreview(){
    MCenterTheme {
        SettingScreen(onAvatarChanged = { Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) })
    }

}
