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
package github.yeori.mnist.db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import github.yeori.mnist.util.Util;

public class MnistDb {
    
    public static final int BYTES_PER_IMG = 28 * 28;

    private int dataSize;
    
    private RandomAccessFile label;
    /*
     * magicnum(4bytes)
     * number of items(4bytes)
     */
    private int offset_label = 4 + 4 ; 
    
    private RandomAccessFile img;
    /*
     * magicnum(4bytes)
     * number of images(4bytes)
     * rows ( 4bytes)
     * cols ( 4bytes)
     */
    private int offset_img = 4 + 4 + 4 + 4 ;
    /*
     * buffer for each images
     */
    private byte [] imgBuf;
    
    private Mnistlet cache;
    
    public MnistDb(RandomAccessFile label, RandomAccessFile img) throws IOException {
        this.label = label;
        this.img = img;
        
        int labelmagic = label.readInt();
        int labelSize = label.readInt();
        
        int imgmagic = img.readInt();
        int imgSize = img.readInt();
        int R = img.readInt();
        int C = img.readInt();
        imgBuf = new byte [ BYTES_PER_IMG];
        
        /* verification*/
        if ( R*C != BYTES_PER_IMG ) {
            throw new MnistDBexception("different image row(%d), col(%d). should be 28",
                    R, C);
        }
        if ( labelSize != imgSize ) {
            throw new MnistDBexception("different data size [%d labels, %s images]",
                    labelSize, imgSize);
        }
        
        if ( labelmagic != 2049 ) {
            throw new MnistDBexception(
                    "different label magicnum [%d expected, but %s]",
                    2049, labelmagic);
        }
        
        if ( imgmagic != 2051 ) {
            throw new MnistDBexception(
                    "different image magicnum [%d expected, but %s]",
                    2051, imgmagic);
            
        }
        dataSize = imgSize;
        cache = new Mnistlet();
    }
    /**
     * total data size(maybe 60,000 for training data, 10,000 for test data)
     * @return
     */
    public int size() {
        return dataSize;
    }
    /**
     * get iterator for all images
     * @return
     */
    public MnistLoop iterator() {
        return new SequentialLoop(this, 0, dataSize);
    }
    /**
     * number characters one of '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
     *   
     * @param index
     * @return one of 0-9 characters
     */
    public char getNumberAt(int index) {
        try {
            this.label.seek(offset_label + index);
            int n = this.label.readUnsignedByte();
            return (char) ('0' + n);
        } catch (IOException e) {
            throw new MnistDBexception(e, "io error! " + e.getMessage());
        }
    }
    /**
     * bytes of i-th image 
     * 
     * @param index
     * @return
     */
    byte [] getRawBytesAt(int index) {
        return readBytes(index, imgBuf);
    }
    /**
     * reads bytes of an i-th image and writes to the given buffer
     * <ul>
     * <li> length of buf should be at least 28*28
     * </ul> 
     * @param index i-th image
     * @param buf bytes of i-th image is written here
     * @return
     */
    public byte [] readBytes(int index, byte[] buf) {
        Util.length ( "at least " + BYTES_PER_IMG + " bytes required, but " + buf.length , buf, 28*28);
        
        readBytes(index, 1, buf, 0);
        return buf;
    }
    
    /**
     * reads bytes of an i-th image and writes to the given buffer
     * <ul>
     * <li> length of buf should be at least 28*28
     * </ul> 
     * @param index i-th image
     * @param buf bytes of i-th image is written here
     * @return
     */
    public byte [] readBytes ( int index, byte[] buf, int bufOffset) {
        Util.length ( String.format(
                        "at least %d bytes required from offset %d, but %d is available",
                              BYTES_PER_IMG,                   bufOffset,     buf.length - bufOffset) , 
                buf, 
                bufOffset + 28*28);
        
        readBytes(index, 1, buf, bufOffset);
        return buf;
    }
    /**
     * read bytes of N images from i-th image
     * <ul>
     *  <li> returns (28 * 28 * nImages) bytes
     *  <li> should (nImages >= 1 )
     * </ul>
     * @param index index of start image(inclusive)
     * @param nImages number of images to read from the given index
     */
    public byte [] readBytes( int index, int nImages) {
        byte [] buf = new byte [ nImages * BYTES_PER_IMG];
        readBytes(index, nImages, buf, 0);
        return buf;
    }
    /**
     * load N images' bytes into the buffer
     * @param index start index to read
     * @param nImages number of images to read from index
     * @param buf bytes of images are witten here
     * @return
     */
    public byte [] readBytes( int index, int nImages, byte [] buf) {
        readBytes(index, nImages, buf, 0);
        return buf;
    }
    private void readBytes ( int index, int nImages, byte[] into, int intoOffset ) {
        if ( nImages < 1 ) {
            throw new MnistDBexception("invalid number of images: %d", nImages);
        }
        
        int fileOffset = offset_img + index * BYTES_PER_IMG;
        int nTotal = nImages * BYTES_PER_IMG; // bytes of n images
        
        try {
            if ( fileOffset + nTotal > this.img.length() ) {
                throw new MnistDBexception("[IMAGE OVERFLOW] cannot read %d images from %d-th image ", nImages, index );
            }
            if ( intoOffset + nTotal > into.length ) {
                throw new MnistDBexception("[BUFFER OVERFLOW] cannot write %d image's bytes into the buffer", nImages, index );
            }
            
            this.img.seek( fileOffset );
            this.img.readFully(into, intoOffset, nTotal );
        } catch (IOException e) {
            throw new MnistDBexception(e, "io error! " + e.getMessage());
        }
        
    }
    /**
     * put data of i-th image into the given mlet
     * 
     * @param index
     * @param mlet holder for i-th image
     * @return 
     */
    public MnistDb get(int index, Mnistlet mlet) {
        readBytes(index, mlet.rawbytes());
        mlet.set(index, getNumberAt(index), mlet.rawbytes());
        return this;
    }
    /**
     * get number character and it's raw bytes of i-th image
     * <ul>
     *  <li> same with {@link #get(int, boolean) get(index, true)}
     * </ul>
     * @param index
     * @return number character and it's raw bytes of i-th image
     */
    public Mnistlet get(int index) {
        get ( index, cache);
        return cache;
    }
    /**
     * number and raw bytes of i-th image
     * 
     * @param index
     * @param useCache use internal mlet instance(true), or create and returns new Mnistlet instance(false)   
     * @return
     */
    public Mnistlet get(int index, boolean useCache) {
        Mnistlet mlet = useCache ? cache : new Mnistlet();
        get ( index, mlet);
        return mlet;
    }
    /**
     * create an interator by each number( one of '0'-'9' )
     * @param num should be one of '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 
     * @return iterator for the given number char
     */
    public MnistLoop queryByNum ( char num ) {
        if ( "0123456789".indexOf(num) < 0 ) {
            throw new MnistDBexception("not a number character: %s", num);
        }
        List<Integer> indexes = new ArrayList<>();
        try {
            this.label.seek(offset_label);
            for ( int i = 0 ; i < dataSize ; i ++ ) {
                char n = (char) ('0' + label.readUnsignedByte() );
                if ( n == num ) {
                    indexes.add(i);
                }
            }
            return new IndexBasedLoop(this, indexes);
        } catch (IOException e) {
            throw new MnistDBexception(e, "io error!: %s", e.getMessage());
        }
    }
    /**
     * create interator by index range
     * 
     * @param startIndex <b>inclusive</b> index
     * @param endIndex <b>exclusive</b> index 
     * @return
     */
    public MnistLoop queryByRange ( int startIndex, int endIndex ) {
        
        return new SequentialLoop(this, startIndex, endIndex);
        
    }
}