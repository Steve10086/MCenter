package com.astune.mcenter.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.astune.mcenter.object.HookedViewModel;
import com.astune.mcenter.utils.enums.Environment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InformationPageViewModel extends HookedViewModel {
    public Bitmap getAvatar(Context context) throws IOException {
        try {
            return BitmapFactory.decodeStream(Files.newInputStream(Paths.get(context.getFilesDir() + Environment.AVATAR_PATH)));
        }catch (IOException exception){
            return BitmapFactory.decodeStream(context.getAssets().open("avatar.jpg"));
        }
    }

    // TODO: Implement the ViewModel
}