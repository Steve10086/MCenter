package com.astune.mcenter;

import com.astune.mcenter.utils.enums.LinkType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class generalTest {
    @Test
    public void test(){
        assertEquals(LinkType.values()[0].getName(), "[]");
    }
}
