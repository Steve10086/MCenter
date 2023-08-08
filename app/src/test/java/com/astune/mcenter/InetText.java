package com.astune.mcenter;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetText {
    @Test
    public void textIp() throws IOException {
        Assert.assertFalse(InetAddress.getByName("192.168.1.116").isReachable(5000));
    }
}
