package com.aparzero.videomaker.domain;


import lombok.Data;

import java.io.ByteArrayOutputStream;

@Data
public class ProcessObject {

    public ProcessObject(final Process process,
                         final ByteArrayOutputStream outputStream,
                         final ByteArrayOutputStream errorStream ) {

        this.process = process;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
    }



    private Process process;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
}
