package com.astune.mcenter;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import com.astune.mcenter.utils.enums.Environment;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class generalTest {
    @Test
    public void test() throws IOException {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        if(! new File(context.getFilesDir() + Environment.AVATAR_PATH).isFile()) throw new IOException();
        System.out.print(Files.newInputStream(Paths.get(context.getFilesDir() + Environment.AVATAR_PATH)));
    }
}
