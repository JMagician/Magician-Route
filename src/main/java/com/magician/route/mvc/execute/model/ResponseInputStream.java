package com.magician.route.mvc.execute.model;

import java.util.UUID;

/**
 * The type that route needs to return when downloading a file
 */
public class ResponseInputStream {

    /**
     * file name
     */
    private String name = UUID.randomUUID().toString() + ".data";

    /**
     * file byte array
     */
    private byte[] bytes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
