package e00_createing_db;

import java.util.Arrays;

import github.yeori.mnist.MnistUtil;
import github.yeori.mnist.db.MnistDb;
import github.yeori.mnist.db.Mnistlet;

public class CreatingDB {

	public static void main(String[] args) {
		
		String label_file_path = "e:/mnist/train-labels-idx1-ubyte.gz";
		String image_file_path = "e:/mnist/train-images-idx3-ubyte.gz";
		MnistDb trainingdb = MnistUtil.loadDb(label_file_path, image_file_path);
		
		System.out.println("# of samples : " + trainingdb.size());
		
		Mnistlet firstImage = trainingdb.get(0); // is '5'
		System.out.println();
		System.out.println("[first image info]");
		System.out.println("index    : " + firstImage.index());
		System.out.println("number   : " + firstImage.number());
		System.out.println("raw bytes: " + Arrays.toString(firstImage.rawbytes()));

		Mnistlet last = trainingdb.get(trainingdb.size() - 1);
		System.out.println();
		System.out.println("[last image info]");
		System.out.println("index    : " + last.index());
		System.out.println("number   : " + last.number());
		System.out.println("raw bytes: " + Arrays.toString(last.rawbytes()));
	}
}
