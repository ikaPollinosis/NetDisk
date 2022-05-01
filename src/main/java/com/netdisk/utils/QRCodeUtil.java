package com.netdisk.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * @ClassName: QRCodeUtil
 * @Description: 二维码工具类
 * @Date: 2022/4/28 16:13
 */
public class QRCodeUtil {
    /**
     * 图像格式
     */
    private static final String FORMAT="JPG";

    /**
     * 字符集
     */
    private static final String CHARSET="utf-8";

    /**
     *  图像尺寸(像素)
     */
    private static final int QRCODESIZE=300;

    /**
     * 中央图像宽、高
     */
    private static final int CENTREWIDTH=60;
    private static final int CENTREHEIGHT=60;

    public static BufferedImage generateImage(String text,String centrepath) throws WriterException {
        /**
        * @Description:  生成二维码图像
        * @Param: [text, centrepath]
        * @return: java.awt.image.BufferedImage
        * @Date: 2022/4/28
        */
        Hashtable<EncodeHintType,Object>hints=new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.MARGIN,1);
        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET,CHARSET);
        BitMatrix bitMatrix=new MultiFormatWriter().encode(text,BarcodeFormat.QR_CODE,QRCODESIZE,QRCODESIZE,hints);
        int width= bitMatrix.getWidth();
        int height=bitMatrix.getHeight();
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        // 设置二维码像素颜色
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                image.setRGB(x,y,bitMatrix.get(x,y)?0xFF000000:0xFFFFFFFF);
            }
        }
        if(centrepath.equals("")){
            return image;
        }
        QRCodeUtil.insertImage(image,centrepath);
        return image;
    }



    private static void insertImage(BufferedImage source,String centrepath){
        /**
        * @Description:  插入二维码中央图片
        * @Param: [source, centrepath]
        * @return: void
        * @Date: 2022/4/28
        */
        InputStream instream=null;
        try{
            instream=QRCodeUtil.class.getResourceAsStream(centrepath);  // 获取中央图像文件的输入流
            Image src= ImageIO.read(instream);
            // 获取图像宽高
            int width= src.getWidth(null);
            int height=src.getHeight(null);
            // 调整图像尺寸,进行压缩
            width=width>=CENTREWIDTH?CENTREWIDTH:width;
            height=height>=CENTREHEIGHT?CENTREHEIGHT:height;
            Image image=src.getScaledInstance(width,height,Image.SCALE_SMOOTH);
            BufferedImage tag=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            Graphics g=tag.getGraphics();
            g.drawImage(image,0,0,null);
            g.dispose();
            src=image;
            // 将图像插入二维码
            Graphics2D graph=source.createGraphics();
            int qrwidth= (QRCODESIZE-width)/2;
            int qrheight=(QRCODESIZE-height)/2;
            graph.drawImage(src,qrwidth,qrheight,null);
            Shape shape=new RoundRectangle2D.Float(qrwidth,qrheight,width,height,6,6);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();
        } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    } finally {
            if (instream != null) {
                try {
                    instream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void encode(String text,String centrepath,OutputStream outstream)throws Exception{
    /**
    * @Description:  编码并生成二维码图像
    * @Param: [text, centrepath, outstream]
    * @return: void
    * @Date: 2022/4/28
    */
        BufferedImage image=QRCodeUtil.generateImage(text,centrepath);
        ImageIO.write(image,FORMAT,outstream);
    }



}
