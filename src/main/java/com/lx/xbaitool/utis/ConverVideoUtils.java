package com.lx.xbaitool.utis;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class ConverVideoUtils {
    private Date date;
    private String sourceVideoPath;//源视频路径
    private String filerealname; // 文件名 不包括扩展名
    private String filename; // 包括扩展名
    private String videofolder;// 需要被转换格式的视频目录
    private String outPath; // 转换后目录
    private String ffmpegpath;//ffmpeg的安装位置
    private String mencoderpath; // mencoder的目录

    public ConverVideoUtils(String outPath, String ffmpegpath, String mencoderpath) {
        this.outPath = outPath;
        this.ffmpegpath = ffmpegpath;
        this.mencoderpath = mencoderpath;
    }

    /**
     * 转换视频格式
     *
     * @param targetExtension 转换成何种格式  .xxx
     * @param isDelSourseFile 转换完成后是否删除源文件
     * @return
     */
    public boolean beginConver(String sourceVideoPath, String targetExtension, boolean isDelSourseFile) {
        this.date = new Date();
        this.sourceVideoPath = sourceVideoPath;
        File file = new File(sourceVideoPath);
        this.filename = file.getName();//文件名
        this.filerealname = filename.substring(0, filename.lastIndexOf(".")).toLowerCase();//没有后缀名文件名
        this.videofolder = this.sourceVideoPath.substring(0, this.sourceVideoPath.indexOf(filename));//视频路径
        System.out.println("接收到文件" + sourceVideoPath);
        if (!checkfile(sourceVideoPath)) {
            System.out.println("不是文件!");
            return false;
        }
        System.out.println("-----------------开始转文件(" + sourceVideoPath + ")----------------------");
        if (process(targetExtension, isDelSourseFile)) {
            System.out.println("-----------------转换成功!----------------- ");
            long totaltime = ((System.currentTimeMillis() - date.getTime()) / (1000));
            System.out.println("-----------------转换视频格式共用了:" + totaltime + "秒----------------- ");
            if (processImg(sourceVideoPath)) {
                System.out.println("截图成功了！ ");
            } else {
                System.out.println("截图失败了！ ");
            }
            if (isDelSourseFile) {
                deleteFile(sourceVideoPath);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 对视频进行截图
     *
     * @param sourceVideoPath 需要被截图的视频路径（包含文件名和扩展名）
     * @return
     */
    public boolean processImg(String sourceVideoPath) {
        if (!checkfile(sourceVideoPath)) {
            return false;
        }
        File fi = new File(sourceVideoPath);
        filename = fi.getName();
        filerealname = filename.substring(0, filename.lastIndexOf(".")).toLowerCase();
        List<String> commend = new java.util.ArrayList<String>();
        //第一帧： 00:00:01
        //time ffmpeg -ss 00:00:01 -i test1.flv -f image2 -y test1.jpg
        commend.add(ffmpegpath);
        //      commend.add("-i");
        //      commend.add(videoRealPath + filerealname + ".flv");
        //      commend.add("-y");
        //      commend.add("-f");
        //      commend.add("image2");
        //      commend.add("-ss");
        //      commend.add("38");
        //      commend.add("-t");
        //      commend.add("0.001");
        //      commend.add("-s");
        //      commend.add("320x240");
        commend.add("-ss");
        commend.add("00:00:01");
        commend.add("-i");
        commend.add(sourceVideoPath);
        commend.add("-f");
        commend.add("image2");
        commend.add("-y");
        commend.add(videofolder + filerealname + ".jpg");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            doWaitFor(p);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 实际转换视频格式的方法
     *
     * @param targetExtension 目标视频扩展名
     * @param isDelSourseFile 转换完成后是否删除源文件
     * @return
     */
    private boolean process(String targetExtension, boolean isDelSourseFile) {
        int type = checkContentType();
        boolean status = false;
        if (type == 0) {
            //如果type为0用ffmpeg直接转换
            status = processVideoFormat(sourceVideoPath, targetExtension);
        } else if (type == 1) {
            //如果type为1，将其他文件先转换为avi，然后在用ffmpeg转换为指定格式
            System.out.println("将视频先转成avi");
            String avifilepath = processAVI();
            if (avifilepath == null) {
                return false;
            } else {
                System.out.println("将avi转到目标格式");
                status = processVideoFormat(avifilepath, targetExtension);
                if (isDelSourseFile) {
                    deleteFile(avifilepath);
                }
            }
        }
        return status;
    }

    /**
     * 检查文件类型
     *
     * @return
     */
    private int checkContentType() {
        String type = sourceVideoPath.substring(sourceVideoPath.lastIndexOf(".") + 1, sourceVideoPath.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        switch (type) {
            case "avi":
                return 0;
            case "mpg":
                return 0;
            case "wmv":
                return 0;
            case "3gp":
                return 0;
            case "mov":
                return 0;
            case "mp4":
                return 0;
            case "asf":
                return 0;
            case "asx":
                return 0;
            case "flv":
                return 0;
            case "wmv9":
                return 1;
            case "rm":
                return 1;
            case "rmvb":
                return 1;
            default:
                return 2;
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param path
     * @return
     */
    private boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
     *
     * @return
     */
    private String processAVI() {
        List<String> commend = new java.util.ArrayList<String>();
        commend.add(mencoderpath);
        commend.add(sourceVideoPath);
        commend.add("-oac");
        commend.add("mp3lame");
        commend.add("-lameopts");
        commend.add("preset=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add(videofolder + filerealname + "_copy.avi");
        // 命令类型：mencoder 1.rmvb -oac mp3lame -lameopts preset=64 -ovc xvid
        // -xvidencopts bitrate=600 -of avi -o rmvb.avi
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            doWaitFor(p);
            return videofolder + filerealname + ".avi";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转换为指定格式
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     *
     * @param oldfilepath
     * @param targetExtension 目标格式扩展名 .xxx
     * @return
     */
    private boolean processVideoFormat(String oldfilepath, String targetExtension) {
        if (!checkfile(oldfilepath)) {
            return false;
        }
        //ffmpeg -i FILE_NAME.flv -ar 22050 NEW_FILE_NAME.mp4
        List<String> commend = new java.util.ArrayList<>();
        commend.add(ffmpegpath);
        commend.add("-i");
        commend.add(oldfilepath);
        commend.add("-ar");
        commend.add("22050");
        commend.add(videofolder + filerealname + "_copy" + targetExtension);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            doWaitFor(p);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     *
     * @param oldfilepath
     * @return
     */
    private boolean processFLV(String oldfilepath) {
        if (!checkfile(sourceVideoPath)) {
            System.out.println(oldfilepath + " is not file");
            return false;
        }
        List<String> commend = new java.util.ArrayList<>();
        commend.add(ffmpegpath);
        commend.add("-i");
        commend.add(oldfilepath);
        commend.add("-ab");
        commend.add("64");
        commend.add("-acodec");
        commend.add("mp3");
        commend.add("-ac");
        commend.add("2");
        commend.add("-ar");
        commend.add("22050");
        commend.add("-b");
        commend.add("230");
        commend.add("-r");
        commend.add("24");
        commend.add("-y");
        commend.add(videofolder + filerealname + ".flv");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String cmd = commend.toString();
            builder.command(commend);
            Process p = builder.start();
            doWaitFor(p);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1;
        try {
            System.out.println("comeing");
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false;

            while (!finished) {
                try {
                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                        System.out.print(c);
                    }
                    while (err.available() > 0) {
                        Character c = new Character((char) err.read());
                        System.out.print(c);
                    }

                    exitValue = p.exitValue();
                    finished = true;

                } catch (IllegalThreadStateException e) {
                    Thread.currentThread().sleep(500);
                }
            }
        } catch (Exception e) {
            System.err.println("doWaitFor();: unexpected exception - " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return exitValue;
    }

    public void deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                file.delete();
            } catch (Exception e) {
                System.out.println("删除出错！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 去除水印
     *
     * @param sourceVideoPath
     * @return
     */
    public boolean removeWatermark(String sourceVideoPath, String videofolder) {
        if (!checkfile(sourceVideoPath)) {
            return false;
        }
        this.videofolder = videofolder;
        File fi = new File(sourceVideoPath);
        filename = fi.getName();
        filerealname = filename.substring(0, filename.lastIndexOf(".")).toLowerCase();
        List<String> commend = new java.util.ArrayList<String>();
        // ffmpeg -i logo.mp4 -filter_complex "delogo=x=100:y=100:w=100:h=100" delogo.mp4
        commend.add(ffmpegpath);
        commend.add("-i");
        commend.add(sourceVideoPath);
        commend.add("-vf");
        commend.add("delogo=x=970:y=20:w=280:h=90:show=0");
        commend.add("-c:a");
        commend.add("copy");
        commend.add(videofolder + filerealname + "_copy_" + System.currentTimeMillis() + ".mp4");
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            doWaitFor(p);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
