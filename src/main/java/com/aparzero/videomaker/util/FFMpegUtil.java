package com.aparzero.videomaker.util;

public class FFMpegUtil {


    public static String createCreateCommandWithScale(final String trimFile,
                                                      final String audioFile,
                                                      final String imageFile,
                                                      final int startTime,
                                                      final int endTime,
                                                      final String outputFile) {
        // Create the filter complex part
        String filterComplex = String.format("[0:v]scale=trunc(iw/2)*2:-1[img]; " +
                "[img]pad=width=ceil(iw/2)*2:height=ceil(ih/2)*2:color=black[padded_img]; " +
                "[padded_img][2:v]overlay=(main_w-overlay_w)/2:(main_h-overlay_h)/2:enable='between(t,%d,%d)'[ovr]; " +
                "[0:a][1:a]concat=n=2:v=0:a=1[a]", startTime, endTime);

        // Create the full command
        return String.format("ffmpeg -i %s -i %s -i %s -filter_complex \"%s\" -map \"[ovr]\" -map \"[a]\" -c:v libx264 -c:a aac -strict experimental %s",
                trimFile, audioFile, imageFile, filterComplex, outputFile);


    }



        public static String createCreateCommand(final String trimFile,
                                                 final String audioFile,
                                                 final String imageFile,
                                                 final int startTime,
                                                 final int endTime,
                                                 final String outputFile) {

            return "ffmpeg -i " + trimFile + " -i " + audioFile + " -i " + imageFile + " " +
                    "-filter_complex \"[0:v]scale=trunc(iw*0.75):-1[img]; " +
                    "[img][2:v]overlay=(main_w-overlay_w)/2:(main_h-overlay_h)/2:enable='between(t," +
                    startTime + "," + endTime + ")'[v]\" " +
                    "-map [v] -map 1:a -c:v libx264 -c:a aac -strict experimental " + outputFile;
        }








}
