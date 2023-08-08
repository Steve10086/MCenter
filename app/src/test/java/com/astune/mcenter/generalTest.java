package com.astune.mcenter;

import com.astune.mcenter.utils.enums.LinkType;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class generalTest {
    @Test
    public void test() throws IOException {
        assertTrue(InetAddress.getByName("192.168.1.116").isReachable(5000));
    }
}
