package com.aparzero.videomaker.constant;

import org.apache.http.conn.util.PublicSuffixList;

public class FFMpegConstant {

    public static final String GET_AUDIO_LENGTH_COMMAND = "ffprobe -v quiet -show_format -print_format json -select_streams a:0 ";
    public static final String TRIM_VIDEO_COMMAND = "ffmpeg -ss 00:00:00 -i %s -t %d -c copy %s";

    public static final String FFMPEG_EXCEPTION = "Error executing FFmpeg command: ";

}

