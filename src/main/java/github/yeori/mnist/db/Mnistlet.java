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

import java.awt.image.BufferedImage;

import github.yeori.mnist.io.MnistIO;

/**
 * represents a number character and raw bytes associated it.
 * 
 * @author chmin.seo
 *
 */
public class Mnistlet {
    /**
     * order from db
     */
    private int index;
    /**
     * one of '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'  
     */
    private char number;
    /**
     * bytes of a image
     */
    private byte [] data = new byte[28*28] ;
    
    public Mnistlet() {}
    
    /*Mnistlet(char number, byte[] data) {
        super();
        this.number = number;
        this.data = data;
    }*/

    public int index() {
        return this.index;
    }
    public char number() {
        return number;
    }
    
    public byte[] rawbytes() {
        return data;
    }

    void set(int idx, char num, byte[] rawbytes) {
        index = idx;
        number = num;
        data = rawbytes;
    }
    
    public BufferedImage asImage() {
    	return MnistIO.toImage(data);
    }
}
