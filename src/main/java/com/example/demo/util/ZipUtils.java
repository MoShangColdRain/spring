package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @description:
 * @author: lx
 * @create: 2019-03-08
 **/
public class ZipUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 压缩图片urlList
     * @param request
     * @param response
     */
    public static void compressImageUrlList(HttpServletRequest request, HttpServletResponse response, List<String> urlList){
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream());
            for (int i = 0; i < urlList.size(); i++) {
                URL url = new URL(urlList.get(i));
                zos.putNextEntry(new ZipEntry(i + ".jpg"));
                InputStream fis = url.openConnection().getInputStream();
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
        } catch (IOException e) {
            logger.error("compressImageUrlList is error", e);
        } finally {
            try {
                if (zos != null){
                    zos.close();
                }
            } catch (Exception e2){
                logger.error("compressImageUrlList is error", e2);
            }
        }
    }

    public static void compressBytes(HttpServletRequest request, HttpServletResponse response, byte[] bytes){
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream());
            zos.putNextEntry(new ZipEntry("交车确认函" + ".pdf"));
            byte[] buffer = new byte[1024];
            InputStream fis = new ByteArrayInputStream(bytes);
            int r = 0;
            while ((r = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, r);
            }
            fis.close();
            zos.flush();
        } catch (IOException e) {
            logger.error("compressBytes is error", e);
        } finally {
            try {
                if (zos != null){
                    zos.close();
                }
            } catch (Exception e2){
                logger.error("compressBytes is error", e2);
            }
        }

    }
}
