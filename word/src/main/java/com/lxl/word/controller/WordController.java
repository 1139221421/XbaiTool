package com.lxl.word.controller;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping("/word")
public class WordController {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String GBK_CHARSET = "GBK";

    @RequestMapping("/download")
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response, String url) {
        //创建 POIFSFileSystem 对象
        POIFSFileSystem poifs = new POIFSFileSystem();
        //获取DirectoryEntry
        DirectoryEntry directory = poifs.getRoot();
        //创建输出流
        OutputStream out = null;
        InputStream in = null;
        FileInputStream fin = null;
        File file = null;
        try {
            URL uri = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            // 设置10秒的相应时间
            conn.setConnectTimeout(10 * 1000);
            //模拟浏览器访问，防止网站屏蔽
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.19 Safari/537.36");
            //得到输入流
            in = conn.getInputStream();
            file = getTemplateFile(in);
            if (file != null) {
                fin = new FileInputStream(file);
                //创建文档,1.格式,2.HTML文件输入流
                directory.createDocument("WordDocument", fin);
                //输出文件
                response.setCharacterEncoding(DEFAULT_CHARSET);
                //设置word格式
                response.setContentType("application/msword");
                response.setHeader("Content-disposition", "attachment;filename=word_" + System.currentTimeMillis() + ".doc");
                out = response.getOutputStream();
                poifs.writeFilesystem(out);
            } else {
                this.errorMsg(request, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.errorMsg(request, response);
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                poifs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) {
                file.delete();
            }
        }
    }

    private void errorMsg(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + "index.html?msg=" + java.net.URLEncoder.encode("地址解析失败，请查看地址是否正确！", DEFAULT_CHARSET));
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * 创建临时文件,将流写进临时文件
     *
     * @param inputStream
     * @return
     */
    private static File getTemplateFile(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        File file = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("temp_file_", null);
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @RequestMapping("/index.html")
    public String word() {
        return "index";
    }

}
