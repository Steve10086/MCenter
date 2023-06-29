package com.astune.mcenter.ui;

import androidx.lifecycle.ViewModel;
import com.astune.mcenter.utils.ID.propertiesID;
import com.astune.mcenter.utils.PropertiesUtil;

import java.io.IOException;
import java.util.Map;

public class SettingActivityViewModel extends ViewModel {
    private String email;
    private String theme;
    private String backgroundPath;
    private String avatarPath;
    private Map<String, String> settingMap;


    public void getData(String basePath) {
        try {

            settingMap = PropertiesUtil.getProperty(
                    basePath + "userSetting",
                    propertiesID.EMAIL,
                    propertiesID.THEME,
                    propertiesID.BACKGROUND_PATH,
                    propertiesID.AVATAR_PATH
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
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
