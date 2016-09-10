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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Scanner;

import github.yeori.mnist.MnistConfig;
import github.yeori.mnist.MnistException;
import github.yeori.mnist.io.ImgWriter.ImgType;

public class MnistIO {
    static String path_training_label = "e:/mnist/train-labels.idx1-ubyte";
    static String path_training_immages = "e:/mnist/train-images.idx3-ubyte";
    public static void main(String[] args) throws IOException {
        /*
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        MnistReader reader = new MnistFileReader();
        reader.read(path_training_label, bos);
        
        int [] labels = readLabel (new ByteArrayInputStream( bos.toByteArray()));
        
        MnistWriter writer = new ImgWriter(new File ("training"));
        
        writeImages(new File(path_training_immages), labels, writer, "tr-");
        */
//        MnistConfig config = new MnistConfig("mnist.config");
//        write( config );
        write ( "mnist.config" );
    }
    
    public static void write ( String configPath) throws MnistException {
        MnistConfig config = new MnistConfig(configPath);
        write( config );
    }
    
    public static void write ( MnistConfig config ) throws MnistException {
        
        System.out.printf("MODE             : %s\n", config.getOutMode() );
        System.out.printf("OUTPUT DIR       : %s\n", config.getOutDir().getAbsolutePath());
        System.out.printf("PREFIX(training) : %sxxxx-x.%s\n", config.getTrainingPrefix(), config.getOutMode());
        System.out.printf("PREFIX(test)     : %sxxxx-x.%s\n", config.getTestPrefix(), config.getOutMode());
        System.out.println();
        
        System.out.println("enter to start( Q to cancel)");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim().toUpperCase();
        if ( "Q".equals(line) ){
            return ;
        }
        
        MnistReader reader = new MnistFileReader();
        MnistWriter writer = getWriter ( config );
        
        File labelTraining = config.getTrainingLabel();
        File imgTraining = config.getTrainingImage();
        try {
            writeData (reader, writer, labelTraining, imgTraining, config.getTrainingPrefix());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        File labelTest = config.getTestLabel();
        File imgTest = config.getTestImage();
        try {
            writeData (reader, writer, labelTest, imgTest, config.getTestPrefix());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

    private static MnistWriter getWriter(MnistConfig config) {
        String mode = config.getOutMode();
        File outDir = config.getOutDir();
        if ( "raw".equals(mode) || "".equals(mode) ){
            return new RawByteWriter(outDir);
        } else if ( "png".equals(mode)) {
            return new ImgWriter(outDir, ImgType.PNG);
        } else if ( "jpg".equals(mode) ) {
            return new ImgWriter(outDir, ImgType.JPG);
        } else {
            throw new MnistException("one of %s is allowed, but [%s]", Arrays.asList("raw", "png", "jpg"), mode);
        }
    }

    private static void writeData(
            MnistReader reader, 
            MnistWriter writer, 
            File labelFile, 
            File imgFile, 
            String prefix) throws IOException {
        if ( labelFile == null && imgFile == null ) {
            /*
             * both are null. 
             */
            return ;
        }
        
        if ( labelFile == null ) {
            throw new MnistException("check configuration. label file is null");
        }
        
        if ( imgFile == null ) {
            throw new MnistException("check configuration. image file is null");
        }
        
        System.out.printf("label     : %s(%d bytes)\n", labelFile.getAbsolutePath(), labelFile.length());
        System.out.printf("image     : %s(%d bytes)\n", imgFile.getAbsolutePath(), imgFile.length());
        System.out.printf("prefix    : %s\n", prefix);
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) labelFile.length());
        reader.read(labelFile.getAbsolutePath(), bos);
        try {
            int [] labels = readLabel(new ByteArrayInputStream(bos.toByteArray()));
            writeImages( imgFile, labels, writer, prefix);
        } catch (IOException e) {
            throw new MnistException(e, "IO exception when processing %s", labelFile.getCanonicalPath(), imgFile.getCanonicalPath() );
        }        
    }

    private static int [] readLabel(InputStream in) throws IOException {
        
        DataInputStream dis = new DataInputStream(in);
        
        int magicnum = dis.readInt();
        System.out.println(String.format("magic number: %d", magicnum));
        
        int numOfItems = dis.readInt();
        System.out.println(String.format("# of items : %d", numOfItems));
        
        int [] labels = new int [ numOfItems];
        
        for ( int i = 0 ; i < numOfItems ; i++) {
            labels[i] = dis.readUnsignedByte();
        }
        dis.close();
        return labels;
    }
    
    private static void writeImages (File inputFile, int [] labels, MnistWriter writer, String prefix ) {
        if ( ! inputFile.exists()) {
            throw new RuntimeException("no such file : " + inputFile.getName() );
        }
        
        try( RandomAccessFile raf = new RandomAccessFile(inputFile, "r")) {
            int magicnum = raf.readInt();
            int nImages = raf.readInt(); // expects 60,000 images
            System.out.println("magic num   : " + magicnum);
            System.out.println("# of images : " + nImages);
            
            if ( nImages != labels.length ) {
                throw new MnistException("# of label(%s) != # of images(%s)", labels.length, nImages);
            }
            int R = raf.readInt();
            int C = raf.readInt();
            int pixels = R * C ; // 28*28 pixels per image
            
            byte [] buf = new byte[pixels];
            for ( int i = 0 ; i < nImages ; i++ ) {
                raf.readFully(buf);
                writer.write( String.format(prefix + "%04d-%d", i, labels[i]), buf);
                if ( i > 0 && i % 1000 == 0 ) {
                    System.out.printf("[%d - %d] processed\n", i-1000+1, i);
                }
            }
            
            System.out.println("finished");
            
        } catch (IOException e) {
            throw new MnistException(e, "IO exception " + e.getMessage());
        }
    }
}
