# mnistdb-java
java implementation for mnist handwritten db

[http://yann.lecun.com/exdb/mnist/](http://yann.lecun.com/exdb/mnist/)

## prerequisite

* jdk(or jre) 1.8+

## how to use

* download db files from [http://yann.lecun.com/exdb/mnist/](http://yann.lecun.com/exdb/mnist/)

* creates db instance like


```
	public static void main(String[] args) {
		
		String label_file_path = "path to label file";
		String image_file_path = "path to image file";
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
```	
	

