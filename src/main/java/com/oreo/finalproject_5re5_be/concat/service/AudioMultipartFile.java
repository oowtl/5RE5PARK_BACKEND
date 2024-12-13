package com.oreo.finalproject_5re5_be.concat.service;

import java.io.*;
import org.springframework.web.multipart.MultipartFile;

public class AudioMultipartFile implements MultipartFile {
    private final byte[] data;
    private final String fileName;
    private final String contentType;

    public AudioMultipartFile(byte[] data, String fileName, String contentType) {
        this.data = data;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return data.length == 0;
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getBytes() {
        return data;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream out = new FileOutputStream(dest)) {
            out.write(data);
        }
    }
}
