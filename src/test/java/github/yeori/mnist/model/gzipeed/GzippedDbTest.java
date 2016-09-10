package github.yeori.mnist.model.gzipeed;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import github.yeori.mnist.MnistUtil;
import github.yeori.mnist.db.MnistDb;
import github.yeori.mnist.db.MnistLoop;
import github.yeori.mnist.db.Mnistlet;
import github.yeori.mnist.io.ImgWriter;
import github.yeori.mnist.io.ImgWriter.ImgType;

public class GzippedDbTest {

	private static MnistDb db;

	@BeforeClass
	public static void loadDB() {
		db = MnistUtil.loadDb("e:/mnist/t10k-labels-idx1-ubyte.gz", "e:/mnist/t10k-images-idx3-ubyte.gz");
		
	}
	
	@Test
	public void test_file_in_path_is_gzipped() {
		assertEquals ( 10000, db.size() );
	}
	
	@Test
	public void test_first_10_images () {
		char [] first10numbs= "7210414959".toCharArray();
		MnistLoop loop = db.queryByRange(0, 10);
		
		char [] ch = new char[10];
		int idx = 0;
		for (int i = 0 ; i < loop.size(); i++ ) {
			ch[i] = loop.get(i).number();
		}
		assertArrayEquals(first10numbs, ch);
	}
	
	@Ignore @Test
	public void test_exports_images() throws IOException {
		MnistLoop loop = db.queryByRange(100, 200);
		ImgWriter imgWirter = new ImgWriter(new File("tmp"), ImgType.PNG);
		for ( Mnistlet mlet : loop ) {
			byte [] rawb = mlet.rawbytes();
			imgWirter.write(String.format("%04d-%s", mlet.index(), mlet.number()), rawb);
		}
	}

}
