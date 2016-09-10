package github.yeori.mnist.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import github.yeori.mnist.MnistConfig;
import github.yeori.mnist.MnistUtil;
import github.yeori.mnist.db.MnistLoop;
import github.yeori.mnist.db.Mnistlet;
import github.yeori.mnist.db.MnistDb;

public class MnistDbTest {

//	private static String labelPath, imgPath;
	private static MnistDb db;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		InputStream in = MnistDbTest.class.getResourceAsStream("/test.config");
		Properties prop = new Properties();
		prop.load(in);
		
		String labelPath = prop.getProperty("path.training.label");
		String imgPath = prop.getProperty("path.training.img");

		db = MnistUtil.loadDb(labelPath, imgPath);
		
	}
	
	@Before
	public void initDb() {
		
	}
	
    @Test
    public void test_trainingdb_size_should_be_60000 () {
        
        assertEquals( 60000, db.size());
        
        MnistLoop itr = db.iterator();
        
        int cnt = 0;
        while ( itr.hasNext() ) {
            
            itr.next();
            cnt ++ ;
        }
        assertEquals ( 60000, cnt);
    }
    
    @Test
    public void test_by_index() {
        
        MnistLoop loop = db.queryByRange(10, 20);
        List<Integer> index = new ArrayList<>();
        for ( Mnistlet mlet : loop ) {
        	index.add(mlet.index());
        }
        assertEquals ( 10, index.size());
        assertEquals ( 10, index.get(0).intValue());
        assertEquals ( 19, index.get(9).intValue());
    }
    
    @Test
    public void test_query_by_num() {
        MnistLoop loop = db.queryByNum('5');
        while ( loop.hasNext() ) {
            assertEquals ('5', loop.next().number());
        }
        
        loop = db.queryByNum('1');
        while ( loop.hasNext() ) {
        	assertEquals ('1', loop.next().number());
        }
    }
    
    @Test
    public void test_imagebytes() {
        MnistDb db = MnistUtil.loadDb("e:/mnist/train-labels.idx1-ubyte", "e:/mnist/train-images.idx3-ubyte");
        byte[] buf = db.readImages(0, 2); // two(0, 1) images
        assertEquals ( 28*28*2, buf.length);
        
        /* 3 bytes reading test */
        byte [] each_3bytes = new byte [ 28*28 * 3 ];
        db.readImages(10, each_3bytes, 28*28*0); // 10th image
        db.readImages(11, each_3bytes, 28*28*1); // 11th image
        db.readImages(12, each_3bytes, 28*28*2); // 12th image
        byte [] batch_3bytes   = db.readImages(10, 3);
        assertArrayEquals(each_3bytes, batch_3bytes);
        
        /* all bytes */
        byte [] all = new byte [ db.size() * 28 * 28 ];
        db.readImages(0, db.size(), all);
        
        byte [] each = new byte[ all.length];
        for ( int index = 0 ; index < db.size(); index ++ ) {
            db.readImages(index, each, index * MnistDb.BYTES_PER_IMG );
        }
        assertArrayEquals(all, each);
        
    }

}
