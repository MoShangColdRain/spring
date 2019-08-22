package com.example.demo.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoImageUtils {

	private static final Logger logger = LoggerFactory.getLogger(VideoImageUtils.class);

	private static final String IMAGEMAT = "png";
	private static final String ROTATE = "rotate";
	
	/**
	 * 默认截取视频的中间帧为封面
	 */
	private static final int MOD = 1;

	/**
	 * 获取视频缩略图
	 * @param filePath：视频路径
	 * @param lpath: 图片存放地址
	 * @param mod：视频长度/mod获取第几帧
	 * @throws Exception
	 */
	public static String randomGrabberFFmpegImage(String filePath, String lpath, int mod) throws Exception {
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
					f = converter.convert(rotate(src, Integer.valueOf(rotate)));
				}
				// 根据视频路径生成缩略图存放路径
				targetFilePath = getImagePath(lpath, i);
				doExecuteFrame(f, targetFilePath);
				break;
			}
			i++;
		}
		ff.stop();
		return targetFilePath;
	}

	/**
	 * 获取视频缩略图
	 * @param filePath：视频路径
	 * @param mod：视频长度/mod获取第几帧
	 * @throws Exception
	 */
	public static InputStream randomGrabberFFmpegImage(String filePath, int mod) throws Exception {
		String targetFilePath = "";
		FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
		ff.start();
		String rotate = ff.getVideoMetadata(ROTATE);
		int ffLength = ff.getLengthInFrames();
		Frame f;
		int i = 0;
		int index = mod;
		InputStream inputStream = null;
		while (i < ffLength) {
			f = ff.grabImage();
			if(i == index){
				if (null != rotate && rotate.length() > 1) {
					OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
					IplImage src = converter.convert(f);
					f = converter.convert(rotate(src, Integer.valueOf(rotate)));
				}
				// 根据视频路径生成缩略图存放路径
				inputStream = doExecuteFrame(f);
				break;
			}
			i++;
		}
		ff.stop();
		return inputStream;
	}

	/**
	 * 获取视频缩略图
	 * @param file：视频文件
	 * @param mod：视频长度/mod获取第几帧
	 * @throws Exception
	 */
	public static InputStream randomGrabberFFmpegImage(File file, int mod) throws Exception {
		String targetFilePath = "";
		FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(file);
		ff.start();
		String rotate = ff.getVideoMetadata(ROTATE);
		int ffLength = ff.getLengthInFrames();
		Frame f;
		int i = 0;
		int index = mod;
		InputStream inputStream = null;
		while (i < ffLength) {
			f = ff.grabImage();
			if(i == index){
				if (null != rotate && rotate.length() > 1) {
					OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
					IplImage src = converter.convert(f);
					f = converter.convert(rotate(src, Integer.valueOf(rotate)));
				}
				// 根据视频路径生成缩略图存放路径
				//inputStream = doExecuteFrame(f);
				targetFilePath = getImagePath("/Users/liuxiang/knowledge/1546930942625207.mp4", i);
				doExecuteFrame(f, targetFilePath);
				break;
			}
			i++;
		}
		ff.stop();
		return inputStream;
	}

	/**
	 * 根据视频路径生成缩略图存放路径
	 * @param filePath：视频路径
	 * @param index：第几帧
	 * @return：缩略图的存放路径
	 */
	private static String getImagePath(String filePath, int index){
		if(filePath.contains(".") && filePath.lastIndexOf(".") < filePath.length() - 1){
			filePath = filePath.substring(0, filePath.lastIndexOf(".")).concat("_").concat(String.valueOf(index)).concat(".").concat(IMAGEMAT);
		}
		return filePath;
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
	 * 截取缩略图
	 * @param f
	 * @param targerFilePath:封面图片
	 * @throws IOException 
	 */
	private static void doExecuteFrame(Frame f, String targerFilePath) {
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
	 * 截取缩略图
	 * @param f
	 * @throws IOException
	 */
	private static InputStream doExecuteFrame(Frame f) {
		if (null == f || null == f.image) {
			return null;
		}
		Java2DFrameConverter converter = new Java2DFrameConverter();
		BufferedImage bi = converter.getBufferedImage(f);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageOutputStream imageOutput;
		InputStream inputStream;
		try {
			imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
			ImageIO.write(bi, IMAGEMAT, imageOutput);
			inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			//	e.printStackTrace();
			return null;
		}
		return inputStream;
	}

	/**
	 * 根据视频长度随机生成随机数集合
	 * @param baseNum:基础数字，此处为视频长度
	 * @param length：随机数集合长度
	 * @return:随机数集合
	 */
	private static List<Integer> random(int baseNum, int length) {
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

	public static void main(String[] args) throws Exception {
		System.out.println(randomGrabberFFmpegImage("https://souche-devqa-video-out.oss-cn-hangzhou.aliyuncs.com/material-infiniti-test-out/videos/Act-ss-mp4-hd/efeae5cfeec74129b47dd119f3143e75/f40f202d2416471faca863b80c29b615_efeae5cfeec74129b47dd119f3143e75.mp4", 1));
	}

}
