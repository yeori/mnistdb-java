package github.yeori.mnist.io;

/*-
 * #%L
 * JMnistDB
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 yeori
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import github.yeori.mnist.util.Util;

class ImgWriter implements MnistWriter {

    private File outDir;
    private int imageWidth = 28 ;
    private int imageHeight = 28 ;
    
    public enum ImgType {
        JPG("jpg"), PNG("png") ;
        private String ext;

        private ImgType ( String ext){
            this.ext = ext;
        }
        
        public String ext() {
            return ext;
        }
    };
    
    private Map<Integer, Color> colors = new HashMap<>();
    
    private BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    private Graphics g ;
    private ImgType type;
    
    public ImgWriter(File outDir ) {
        this ( outDir, ImgType.PNG);
    }
    
    public ImgWriter(File outDir, ImgType type) {
        Util.notNull ( "output dir is null", outDir);
        Util.should_be_true (String.format("not a directory: %s", outDir.getPath()),  outDir.isDirectory());
        
        this.outDir = outDir;
        g = img.getGraphics();
        this.type = type;
        
        colorCache ( );
    }
    
    private void colorCache() {
        for( int rgb = 0; rgb < 256 ; rgb ++) {
            colors.put(rgb, new Color( rgb, rgb, rgb));
        }
    }

    @Override
    public void write(String fname, byte[] data ) throws IOException {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);
        
        for (int i = 0; i < data.length; i++) {
            int ir = i / imageWidth;
            int ic = i % imageHeight;
            /*
             *    0 means white in mnist(but is black in graphics)
             *  255 means black in mnist(but is white in graphcis)
             */
            int rgb = 255 - Util.b2i ( data[i] );
            
            if ( rgb < 255 ) {
                Color c= colors.get(rgb);
                g.setColor(c);
                g.fillRect(ic, ir, 1, 1);
            }
        }
        
        File imgFile = new File ( outDir, fname + "." + type.ext() );
        FileOutputStream fos = new FileOutputStream(imgFile);
        ImageIO.write(img, type.ext(), fos);
        fos.close();
    }
}
