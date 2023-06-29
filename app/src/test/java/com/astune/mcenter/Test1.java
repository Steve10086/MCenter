package com.astune.mcenter;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Test1 {
    @Test
    public void test(){
        String a;
        String b;
        String c;
        assertEquals(new ArrayList<>(List.of(new String[]{"ss", "wa"})).toArray(new String[]{""}), new String[]{"ss", "wa"});
    }
}
