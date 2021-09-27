bin/morsinator/Morsinator.class: src/morsinator/Morsinator.java bin/morsinator/reader/ConversionRow.class bin/morsinator/reader/ConversionReader.class bin/morsinator/reader/TextualConversionReader.class bin/morsinator/reader/ConversionReaderException.class bin/morsinator/collections/MorsiBinaryTree.class bin/morsinator/collections/MorsiList.class
	javac --source-path src -d bin -implicit:none src/morsinator/Morsinator.java

bin/morsinator/reader/ConversionRow.class: src/morsinator/reader/ConversionRow.java
	javac --source-path src -d bin -implicit:none src/morsinator/reader/ConversionRow.java

bin/morsinator/reader/ConversionReader.class: src/morsinator/reader/ConversionReader.java bin/morsinator/collections/MorsiList.class bin/morsinator/reader/ConversionRow.class bin/morsinator/collections/MorsiBinaryTree.class bin/morsinator/reader/ConversionReaderException.class
	javac --source-path src -d bin -implicit:none src/morsinator/reader/ConversionReader.java

bin/morsinator/reader/TextualConversionReader.class: src/morsinator/reader/TextualConversionReader.java bin/morsinator/reader/ConversionRow.class bin/morsinator/reader/ConversionReader.class bin/morsinator/reader/ConversionReaderException.class bin/morsinator/collections/MorsiBinaryTree.class bin/morsinator/collections/MorsiList.class
	javac --source-path src -d bin -implicit:none src/morsinator/reader/TextualConversionReader.java

bin/morsinator/reader/ConversionReaderException.class: src/morsinator/reader/ConversionReaderException.java
	javac --source-path src -d bin -implicit:none src/morsinator/reader/ConversionReaderException.java

bin/morsinator/collections/MorsiList.class: src/morsinator/collections/MorsiList.java
	javac --source-path src -d bin -implicit:none src/morsinator/collections/MorsiList.java

bin/morsinator/collections/MorsiBinaryTree.class: src/morsinator/collections/MorsiBinaryTree.java
	javac --source-path src -d bin -implicit:none src/morsinator/collections/MorsiBinaryTree.java

clean:
	rm -rf bin