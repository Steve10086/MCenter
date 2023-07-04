package com.astune.mcenter.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.astune.mcenter.utils.enums.Environment;
import com.astune.mcenter.utils.enums.Properties;
import com.astune.mcenter.utils.FileUtil;
import com.astune.mcenter.utils.PropertiesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SettingActivityViewModel extends ViewModel {
    private final MutableLiveData<Map<String, String>> settingMap = new MutableLiveData<>(new HashMap<>());
    public LiveData<Map<String, String>> getSettingMap(){return settingMap;}

    public void getData(Context context){
        try {
            settingMap.getValue().putAll(PropertiesUtil.getProperty(
                    context.getFilesDir() + Environment.SETTING_PROPERTIES,
                    Properties.EMAIL,
                    Properties.THEME,
                    Properties.ZEROTIER,
                    Properties.PASSWORD)
            );
        } catch (FileNotFoundException e) {
            Log.w("FileIO", "property not found");
            if( new File(context.getFilesDir() + Environment.SETTING_PROPERTIES).isFile()
                    || new File(context.getFilesDir() + "/properties").mkdirs()) {
                Log.i("FileIO", "property copy start");
                try {
                    new File(context.getFilesDir() + Environment.SETTING_PROPERTIES).deleteOnExit();
                    Files.copy(context.getAssets().open("userSetting.properties"), Paths.get(context.getFilesDir() + Environment.SETTING_PROPERTIES));
                    Log.i("FileIO", "property copied");
                    getData(context);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                FileUtil.delete(context.getFilesDir() + "/properties");
                getData(context);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void setData(Context context, Map<String, String> newMap) throws IOException {
        PropertiesUtil.setProperty(context.getFilesDir() + Environment.SETTING_PROPERTIES, newMap);
    }

    public Bitmap getAvatar(Context context){
        try {
            return BitmapFactory.decodeStream(Files.newInputStream(Paths.get(context.getFilesDir() + Environment.AVATAR_PATH)));
        }catch (IOException exception){

            if( new File(context.getFilesDir() + Environment.AVATAR_PATH).isFile()
                    || new File(context.getFilesDir().toString() + "/img").mkdirs()){
                Log.w("File", "Avatar not found");
                try {

                    BitmapFactory.decodeStream(context.getAssets().open("avatar.jpg"))
                            .compress(Bitmap.CompressFormat.JPEG
                                    , 100
                                    , Files.newOutputStream(Paths.get(context.getFilesDir() + Environment.AVATAR_PATH)));

                } catch (IOException e) {
                    throw new RuntimeException(e);

                }
                return getAvatar(context);
            }
            throw new RuntimeException();
        }
    }

    public void setAvatar(Uri uri, Context context) throws IOException {
        Log.i("Uri", uri.getPath());
        if( new File(context.getFilesDir() + Environment.AVATAR_PATH).isFile()
                || new File(context.getFilesDir() + "/img").mkdirs()) {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri)).compress(Bitmap.CompressFormat.JPEG, 100, Files.newOutputStream(Paths.get(context.getFilesDir() + Environment.AVATAR_PATH)));
        } else {
            FileUtil.delete(context.getFilesDir() + "/img");
        }
    }

    public boolean getTheme(){
        return Boolean.parseBoolean(settingMap.getValue().get(Properties.THEME));
    }
    public String getEmail(){
        return settingMap.getValue().get(Properties.EMAIL);
    }

    public void updateMap(String key, String value){
        settingMap.getValue().put(key, value);
        settingMap.setValue(settingMap.getValue());
        Log.i("map", key + " updated!");
    }

    public void saveData(Context context) throws IOException {
        PropertiesUtil.setProperty(context.getFilesDir() + Environment.SETTING_PROPERTIES, settingMap.getValue());
    }

}
