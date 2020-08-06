package com.lxl.video.utis;

public class ConverVideoTest {
    public void run() {
        try {
////            String filePath = "E:\\project\\conver_video\\111.rmvb";
            String filePath = "E:\\project\\conver_video\\222.mp4";
            ConverVideoUtils cv = new ConverVideoUtils("E:\\project\\conver_video\\", "E:\\project\\conver_video\\ffmpeg.exe", "E:\\project\\conver_video\\mencoder.exe");
//            String targetExtension = ".avi";
//            boolean isDelSourseFile = false;
//            boolean beginConver = cv.beginConver(filePath, targetExtension, isDelSourseFile);
//            System.out.println("转换并截图：" + beginConver);

            System.out.println("去水印：" + cv.removeWatermark(filePath, "E:\\project\\conver_video\\"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ConverVideoTest c = new ConverVideoTest();
        c.run();
    }
}
