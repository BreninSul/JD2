package com.gmail.breninsul.jd2.pojo;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;

import javax.cache.annotation.CacheResult;
import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
@Log
public class Image extends BaseEntity {
    public static final int DB_IMAGE_W = 200;
    public static final int DB_IMAGE_H = 200;
    @Access(AccessType.PROPERTY)
    @Column(columnDefinition = "blob", nullable = true)
    private byte[] img = null;

    public Image(byte[] img) {
        this.img = img;
    }

    public Image(BufferedImage img) throws NullPointerException{
        if (!setImg(img)){
            throw new NullPointerException("there is problem setting img");
        }
    }

    public Image(InputStream img) throws NullPointerException{
        if (!setImg(img)){
            throw new NullPointerException("there is problem setting img");
        }
    }
    public Image(String link) throws NullPointerException{
       if (!setImg(link)){
           throw new NullPointerException("there is problem setting img");
       }
    }
    private void setImg(byte[] img) {
        this.img = img;
    }

    public boolean setImg(InputStream img) {
        try {
            BufferedImage image = ImageIO.read(img);
            return setImg(image);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setImg(String link) {
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            log.warning("URL is'nt correct!");
            return false;
        }
        try {
            BufferedImage img = ImageIO.read(url);
            return setImg(img);
        } catch (IOException e) {
            log.warning("Error getting img from url!");
            return false;
        }
    }

    /**
     * Main method with scaling
     */
    public boolean setImg(BufferedImage img) {
        if (img == null) {
            log.info("There is no BufferedImage");
            return false;
        }
        byte[] imageInByte = null;
        java.awt.Image scaledImage = img.getScaledInstance(DB_IMAGE_W, DB_IMAGE_W, java.awt.Image.SCALE_SMOOTH);
        BufferedImage imageBuff = new BufferedImage(DB_IMAGE_W, DB_IMAGE_W, BufferedImage.TYPE_INT_RGB);
        imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            ImageIO.write(imageBuff, "jpg", buffer);
            buffer.flush();
            imageInByte = buffer.toByteArray();
            buffer.close();
            setImg(imageInByte);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BufferedImage getAsBufferedImage() {
        ByteArrayInputStream bais = new ByteArrayInputStream(img);
        try {
            return ImageIO.read(bais);
        } catch (IOException | NullPointerException e) {
            log.warning("There is a problem to contert byte[] to BufferedImage, returning null");
        }
        return null;
    }


}
