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
    
### query by range

* iteration style

```java

    public static void main ( String [] args ) {
        String label_file_path = "....";
        String image_file_path = "....";
        MnistDb db = MnistUtil.loadDb(label_file_path, image_file_path);
        
        int startIndex = 10; // inclusive
        int endIndex = 20;   // exclusive
        MnistLoop loop = db.queryByRange(startIndex, endIndex);
        
        for ( Mnistlet mlet : loop ) {
            System.out.println(String.format("index:%3d, number: '%s'", mlet.index(), mlet.number()));
            // mlet.rawbytes();
        }
    }
    
```

* index style

```java

    public static void main ( String [] args ) {
        String label_file_path = "....";
        String image_file_path = "....";
        MnistDb db = MnistUtil.loadDb(label_file_path, image_file_path);
        
        int startIndex = 10; // inclusive
        int endIndex = 20;   // exclusive
        MnistLoop loop = db.queryByRange(startIndex, endIndex);
        
        for ( int i = 0 ; i < loop.size(); i++ ) {
            Mnistlet mlet = loop.get(i);
            System.out.println(String.format("index:%3d, number: '%s'", mlet.index(), mlet.number()));
        }
    }
    
```

