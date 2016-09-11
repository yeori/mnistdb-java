package e03_export_image;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import github.yeori.mnist.MnistUtil;
import github.yeori.mnist.db.MnistDb;
import github.yeori.mnist.db.MnistLoop;
import github.yeori.mnist.db.Mnistlet;
import github.yeori.mnist.io.MnistIO;

public class ExportingImages {
	public static void main(String[] args) {
		String label_file_path = "e:/mnist/train-labels-idx1-ubyte.gz";
		String image_file_path = "e:/mnist/train-images-idx3-ubyte.gz";
		MnistDb db = MnistUtil.loadDb(label_file_path, image_file_path);
		
		// exports first 10 images
		MnistLoop loop = db.queryByRange(0, 10);
		File outDir = new File("tmp");
		for ( Mnistlet data : loop ) {
			try {
				File imageFile = new File(outDir, String.format("n-%05d-%s.png", data.index(), data.number()));
				ImageIO.write( data.asImage(), "PNG", imageFile );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
