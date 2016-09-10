package github.yeori.mnist;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import github.yeori.mnist.db.MnistDb;
import github.yeori.mnist.util.Util;

public class MnistUtil {
    
    public static MnistDb loadDb ( String labelPath, String imgPath) {
        File label = new File(labelPath);
        File img   = new File(imgPath);
        Util.should_exist("check label file path", label);
        Util.should_exist("check image file path", img);
        
        MnistDb db;
        try {
            db = new MnistDb(new RandomAccessFile(label, "r"), new RandomAccessFile(img, "r"));
            return db;
        } catch (FileNotFoundException e) {
            throw new MnistException(e, "check file path!\n    %s\n    %s", labelPath, imgPath);
        } catch (IOException e) {
            throw new MnistException(e, "io error: %s", e.getMessage());
        }
    }
    
}
