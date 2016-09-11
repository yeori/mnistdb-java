package e02_query_by_number;

import github.yeori.mnist.MnistUtil;
import github.yeori.mnist.db.MnistDb;
import github.yeori.mnist.db.MnistLoop;
import github.yeori.mnist.db.Mnistlet;

public class QueryByNumber {

	public static void main(String[] args) {
		
		String label_file_path = "e:/mnist/train-labels-idx1-ubyte.gz";
		String image_file_path = "e:/mnist/train-images-idx3-ubyte.gz";
		MnistDb db = MnistUtil.loadDb(label_file_path, image_file_path);
		
		MnistLoop loop = db.queryByNum('9');
		
		System.out.println(String.format("db has %d of '9'", loop.size()));
		for( Mnistlet nine : loop ) {
			System.out.println("index " + nine.index());
		}
		
	}
		
}
