package e01_query_by_index_range;

import github.yeori.mnist.MnistUtil;
import github.yeori.mnist.db.MnistDb;
import github.yeori.mnist.db.MnistLoop;
import github.yeori.mnist.db.Mnistlet;

public class QueryByRange {

	public static void main(String[] args) {
		
		String label_file_path = "e:/mnist/train-labels-idx1-ubyte.gz";
		String image_file_path = "e:/mnist/train-images-idx3-ubyte.gz";
		MnistDb db = MnistUtil.loadDb(label_file_path, image_file_path);
		
		int startIndex = 10;
		int endIndex = 20;
		MnistLoop loop = db.queryByRange(startIndex, endIndex);
		
		for ( Mnistlet mlet : loop ) {
			System.out.println(String.format("index:%3d, number: '%s'", mlet.index(), mlet.number()));
			// mlet.rawbytes();
		}
		
		for ( int i = 0 ; i < loop.size(); i++ ) {
			Mnistlet mlet = loop.get(i);
			System.out.println(String.format("index:%3d, number: '%s'", mlet.index(), mlet.number()));
		}
		
	}
		
}
