package com.example.demo.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoUtil {
    
    public static String host ="http://************";//服务器IP
    public static String rootPath= "E:\\tomcat/webapps/***（项目名）/image/";//服务器路径
    
    private static final String IMAGEMAT = "png";  //图片格式
    private static final String ROTATE = "rotate";  
    public static final int MOD = 1;//第一帧
    
//    public static void main(String[] args) throws Exception {
//        
//        //System.out.println(VideoUtil.randomGrabberFFmpegVideoImage("1528887431390.png", MOD));
//    }
    /** 
     * 获取视频缩略图 
     * @param lpath 
     * @param filePath：视频路径 
     * @param mod：视频长度/mod获取第几帧 
     * @throws Exception 
     */  
    public static String randomGrabberFFmpegVideoImage(String filePath, String lpath, int mod) throws Exception {  
        String targetFilePath = "";  
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);  
        ff.start();  
        String rotate = ff.getVideoMetadata(ROTATE);  
        int ffLength = ff.getLengthInFrames();  
        Frame f;  
        int i = 0;  
        int index = mod;  
        while (i < ffLength) {  
            f = ff.grabImage();  
            if(i == index){  
                if (null != rotate && rotate.length() > 1) {  
                    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();  
                    IplImage src = converter.convert(f);  
                    f = converter.convert(rotate(src, Integer.valueOf(rotate)));  //旋转图片
                }  
                targetFilePath = getImagePath(lpath, i);  //根据视频路径生成缩略图存放路径 
                doExecuteFrame(f, targetFilePath);  //获取缩略图
                break;  
            }  
            i++;  
        }  
        ff.stop();  
        return targetFilePath;  //返回的是视频第一帧
    }  

    /** 
     * 旋转图片 
     * @param src 
     * @param angle 
     * @return 
     */  
    public static IplImage rotate(IplImage src, int angle) {  
        IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());  
        opencv_core.cvTranspose(src, img);  
		opencv_core.cvFlip(img, img, angle);  
        return img;  
    } 
    
    /** 
     * 根据视频路径生成缩略图存放路径 
     * @param filePath：视频路径 
     * @param index：第几帧 
     * @return：缩略图的存放路径 
     */  
    private static String getImagePath(String filePath, int index){   
        return rootPath+filePath;
    } 
    /** 
     * 截取缩略图 
     * @param f 
     * @param targerFilePath:封面图片 
     */  
    public static void doExecuteFrame(Frame f, String targerFilePath) {  
        if (null == f || null == f.image) {  
            return;  
        }  
        Java2DFrameConverter converter = new Java2DFrameConverter();  
        BufferedImage bi = converter.getBufferedImage(f);  
        File output = new File(targerFilePath);  
        try {
            ImageIO.write(bi, IMAGEMAT, output);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }  

    /** 
     * 根据视频长度随机生成随机数集合 
     * @param baseNum:基础数字，此处为视频长度 
     * @param length：随机数集合长度 
     * @return:随机数集合 
     */  
    public static List<Integer> random(int baseNum, int length) {  
        List<Integer> list = new ArrayList<Integer>(length);  
        while (list.size() < length) {  
            Integer next = (int) (Math.random() * baseNum);  
            if (list.contains(next)) {  
                continue;  
            }  
            list.add(next);  
        }  
        Collections.sort(list);  
        return list;  
    }  
    
    public static void main(String[] args) {
		String path ="http://***********/image/1532505427121.mp4";

		String lpath = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."))+".png";//自定义输出图片名截取与视频名一样

		System.out.println(lpath);
	}


}