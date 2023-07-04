package com.astune.mcenter;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import androidx.test.platform.app.InstrumentationRegistry;
import com.astune.mcenter.utils.FileUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

public class resetInternalSpace {
    @Test
    public void reset() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Log.i("files", Arrays.toString(appContext.getFilesDir().list()));

        FileUtil.delete(String.valueOf(appContext.getFilesDir()));
    }
}
