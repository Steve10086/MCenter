package com.astune.mcenter;

import com.astune.mcenter.utils.enums.LinkType;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

public class generalTest {
    @Test
    public void test() throws IOException {
        assertEquals(InetAddress.getByName("192.168.1.116").isReachable(5000), true);
    }
}
