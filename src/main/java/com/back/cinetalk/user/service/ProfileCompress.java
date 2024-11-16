package com.back.cinetalk.user.service;

import com.back.cinetalk.user.dto.ProfileDTO;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ProfileCompress {

    public ProfileDTO compressImage(BufferedImage image) throws IOException {

        return ProfileDTO.builder()
                .profile(MakingImage(image,100,0.1f))
                .profile_hd(MakingImage(image,200,1.0f))
                .build();
    }

    public byte[] MakingImage(BufferedImage image,int Width,float quality) throws IOException {

        // 원본 이미지의 너비와 높이
        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        // 원본 비율 계산
        double aspectRatio = (double) originalWidth / originalHeight;

        // 비율에 맞춰 세로를 계산
        int targetHeight = (int) (Width / aspectRatio);

        // 이미지 리사이징
        Image scaledImage = image.getScaledInstance(Width, targetHeight, Image.SCALE_SMOOTH);

        // 새로운 크기에서 BufferedImage로 변환
        BufferedImage resizedImage = new BufferedImage(Width, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();  // 자원 해제

        // 바이트 배열로 변환 (JPEG 형식으로 압축)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // JPEGWriter와 압축 품질 설정
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);

        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(quality);  // 품질을 설정
        jpegParams.setOptimizeHuffmanTables(true);  // 허프만 테이블 최적화
        jpegParams.setProgressiveMode(JPEGImageWriteParam.MODE_DEFAULT);  // 프로그레시브 압축

        writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
        writer.write(null, new IIOImage(resizedImage, null, null), jpegParams);
        writer.dispose();

        return byteArrayOutputStream.toByteArray();
    }
}
