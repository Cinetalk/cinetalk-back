package com.back.cinetalk.movie.service;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

//사진 url 입력시 바이트별로 rgb값의 평균값을 hex색상코드로 반환 함수 --김태욱
@Component
public class MainColorExtract {

    public String ColorExtract(String url) throws IOException {
        
        // 이미지 파일 로드
        BufferedImage image = ImageIO.read(new URL(url));

        // 이미지의 폭과 높이
        int width = image.getWidth();
        int height = image.getHeight();

        // 모든 픽셀의 RGB 값을 합산
        long totalRed = 0;
        long totalGreen = 0;
        long totalBlue = 0;
        long totalPixels = width * height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                totalRed += (rgb >> 16) & 0xFF;
                totalGreen += (rgb >> 8) & 0xFF;
                totalBlue += rgb & 0xFF;
            }
        }

        // RGB 값의 평균 계산
        int avgRed = (int) (totalRed / totalPixels);
        int avgGreen = (int) (totalGreen / totalPixels);
        int avgBlue = (int) (totalBlue / totalPixels);

        // 평균 RGB 값을 Hex 코드로 반환
        return String.format("#%02x%02x%02x", avgRed, avgGreen, avgBlue);
    }
}
