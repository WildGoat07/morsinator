# dépendances
Morsinator: ConversionRow ConversionReader TextualConversionReader BinaryConversionReader MorsiList MorsiBinaryTree ConversionReaderException
ConversionReader: MorsiList ConversionRow MorsiBinaryTree ConversionReaderException
TextualConversionReader: ConversionRow ConversionReader ConversionReaderException MorsiBinaryTree MorsiList
BinaryConversionReader: ConversionRow ConversionReader ConversionReaderException MorsiBinaryTree MorsiList

# alias
Morsinator: bin/morsinator/Morsinator.class
ConversionRow: bin/morsinator/reader/ConversionRow.class
ConversionReader: bin/morsinator/reader/ConversionReader.class
TextualConversionReader: bin/morsinator/reader/TextualConversionReader.class
BinaryConversionReader: bin/morsinator/reader/BinaryConversionReader.class
ConversionReaderException: bin/morsinator/reader/ConversionReaderException.class
MorsiList: bin/morsinator/collections/MorsiList.class
MorsiBinaryTree: bin/morsinator/collections/MorsiBinaryTree.class

# règles de compilation individuelle

bin/morsinator/Morsinator.class: src/morsinator/Morsinator.java
	javac --source-path src -d bin -implicit:none src/morsinator/Morsinator.java

bin/morsinator/reader/ConversionRow.class: src/morsinator/reader/ConversionRow.java
	javac --source-path src -d bin -implicit:none src/morsinator/reader/ConversionRow.java

bin/morsinator/reader/ConversionReader.class: src/morsinator/reader/ConversionReader.java
	javac --source-path src -d bin -implicit:none src/morsinator/reader/ConversionReader.java

bin/morsinator/reader/TextualConversionReader.class: src/morsinator/reader/TextualConversionReader.java
	javac --source-path src -d bin -implicit:none src/morsinator/reader/TextualConversionReader.java

bin/morsinator/reader/BinaryConversionReader.class: src/morsinator/reader/BinaryConversionReader.java
	javac --source-path src -d bin -implicit:none src/morsinator/reader/BinaryConversionReader.java

bin/morsinator/reader/ConversionReaderException.class: src/morsinator/reader/ConversionReaderException.java
	javac --source-path src -d bin -implicit:none src/morsinator/reader/ConversionReaderException.java

bin/morsinator/collections/MorsiList.class: src/morsinator/collections/MorsiList.java
	javac --source-path src -d bin -implicit:none src/morsinator/collections/MorsiList.java

bin/morsinator/collections/MorsiBinaryTree.class: src/morsinator/collections/MorsiBinaryTree.java
	javac --source-path src -d bin -implicit:none src/morsinator/collections/MorsiBinaryTree.java

# règle de nettoyage
clean:
	rm -rf bin