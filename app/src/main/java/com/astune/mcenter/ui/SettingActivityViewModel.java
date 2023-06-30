package com.astune.mcenter.ui;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.room.util.FileUtil;
import com.astune.mcenter.utils.ID.propertiesID;
import com.astune.mcenter.utils.PropertiesUtil;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class SettingActivityViewModel extends ViewModel {
    private String email;
    private String theme;
    private String backgroundPath;
    private String avatarPath;
    private Map<String, String> settingMap;


    public void getData(Context context){
        try {
            settingMap = PropertiesUtil.getProperty(
                    context.getFilesDir() + "userSetting",
                    propertiesID.EMAIL,
                    propertiesID.THEME,
                    propertiesID.BACKGROUND_PATH,
                    propertiesID.AVATAR_PATH
            );
        } catch (IOException e) {
            try {
                Files.copy(context.getAssets().open("userSetting.properties"), Paths.get(context.getFilesDir() + "userSetting.properties"));
            }catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        this.email = settingMap.get(propertiesID.EMAIL);
        this.theme = settingMap.get(propertiesID.THEME);
        this.backgroundPath = settingMap.get(propertiesID.BACKGROUND_PATH);
        this.avatarPath = settingMap.get(propertiesID.AVATAR_PATH);
    }

    public void setData(String basePath) throws IOException {
        PropertiesUtil.setProperty(basePath, settingMap);
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public String getEmail() {
        return email;
    }

    public String getTheme() {
        return theme;
    }
}
