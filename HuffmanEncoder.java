import java.io.*;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

/**
 *
 * Encoder to handle methods to take a Decompressed File and convert into a Compressed File or vice versa
 *
 * @author Phuc Tran, COSC 10, 21 Winter
 *
 */

public class HuffmanEncoder {
    private Map<Character, Integer> frequencyTable;                          // contains a Map that holds the frequency
    // of each character(i.e. a has appeared
    // 2 times)

    private PriorityQueue<HuffmanTree<Character, Integer>> queueHuffman;    // contains a Queue that holds individual
    // trees that will hold two values:
    // the character and the frequency

    private Map<Character, String> matchCodeWords;                          // contains a Queue that will hold all
    // Huffman Tree of character and their
    // code bits


    public HuffmanEncoder() {
        //initializes frequencyTable
        frequencyTable = new TreeMap<>();

    }


    // Getters to check code
    public Map<Character, Integer> getFrequencyTable() {
        return frequencyTable;
    }

    public PriorityQueue<HuffmanTree<Character, Integer>> getQueueHuffman() {
        return queueHuffman;
    }

    public Map<Character, String> getMatchCodeWords() {
        return matchCodeWords;
    }


    /**
     * @param filename name of the file being converted to String
     * @return String that holds all the characters and "\n" of a File
     * @throws Exception note: remember to close reader
     */
    public String loadFileIntoString(String filename) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String str = "", line;
        while ((line = in.readLine()) != null) {
            str += line + "\n";

        }
        in.close();
        return str;
    }

    /**
     * @param fileString String that holds all the characters of the File into frequencyTable
     */
    public void loadStringIntoMap(String fileString) {
        // iterate through the long String
        for (int i = 0; i < fileString.length(); i++) {
            char curr = fileString.charAt(i);

            // increase frequency's value if key is contained, otherwise, create new
            if (frequencyTable.containsKey(curr)) {
                frequencyTable.put(curr, frequencyTable.get(curr) + 1);
            } else {
                frequencyTable.put(curr, 1);
            }
        }
    }

    /**
     * loading the frequencyTable into a Queue that holds individual HuffmanTree
     * refer to HuffmanTree.java to understand specific method calls
     */
    public void loadMapIntoQueue() {

        // TreeComparator overrides Comparator; currently is a MaxHeap comparator
        Comparator<HuffmanTree<Character, Integer>> frequencyCompare = new TreeComparator();

        // pass frequencyCompare as a queueHuffman's constructor
        queueHuffman = new PriorityQueue<>(frequencyCompare);

        // iterate through the frequencyTable and add new trees; automatically is in order from TreeComparator()
        for (Map.Entry<Character, Integer> entryTree : frequencyTable.entrySet()) {
            queueHuffman.add(new HuffmanTree<>(entryTree.getKey(), entryTree.getValue()));
        }

    }

    /**
     * create a HuffmanCodeTree which is essentially just a manipulation of a Queue and having one giant tree contained
     * in the queue
     */
    public void createHuffmanCodeTree() {

        // exits queue when queueHuffman has achieved one giant connected Tree
        while (queueHuffman.size() > 1) {
            // take the two lowest frequency HuffmanTree
            HuffmanTree<Character, Integer> t1 = queueHuffman.remove();
            HuffmanTree<Character, Integer> t2 = queueHuffman.remove();

            // create a new Tree with the combined frequency as the value; character is irrelevant
            HuffmanTree<Character, Integer> tempTree = new HuffmanTree<Character, Integer>(t1, t2);
            tempTree.setFrequency(t1.getFrequency() + t2.getFrequency());

            // add that Tree to the queue
            queueHuffman.add(tempTree);
        }
    }


    /**
     * creates Huffman Tree of character and their code bits
     * calls the Queue that will hold all HuffmanTree of character and their code bits
     */
    public void codeRetrieval() {
        if (queueHuffman.size() == 1) {
            // queueHuffman.peek() = the first HuffmanTree; peek() instead of remove() to maintain Tree
            matchCodeWords = queueHuffman.peek().traverseTree("");
        }
    }

    /**
     * @param filename           file we are trying to read and compress
     * @param compressedfilename file we are writing for the compressed bits
     * @throws IOException
     */
    public void compression(String filename, String compressedfilename) throws Exception {

        // opens FileReader and BitWriter
        BufferedReader input = new BufferedReader(new FileReader(filename));
        BufferedBitWriter bitOutput = new BufferedBitWriter(compressedfilename);

        // read each character
        int cInt = input.read();

        // iterate until a character is -1 --> reached the end of file
        while (cInt != -1) {
            char c = (char) cInt;

            if (matchCodeWords.containsKey(c)) {

                // iterate each character's code bits and convert to false's and true's to later transverse the tree
                for (int i = 0; i < matchCodeWords.get(c).length(); i++) {
                    if (matchCodeWords.get(c).charAt(i) == '0') {
                        bitOutput.writeBit(false);
                    }
                    if (matchCodeWords.get(c).charAt(i) == '1') {
                        bitOutput.writeBit(true);
                    }
                }
            }

            // iterator: read the next character
            cInt = input.read();
        }

        // close the reader and bitwriter
        input.close();
        bitOutput.close();
    }

    /**
     * @param compressedfilename   compressed file with bits that we are trying to read
     * @param decompressedfilename decompressed file that we are trying to write back into comprehensible characters
     * @throws IOException
     */
    public void decompression(String compressedfilename, String decompressedfilename)
            throws IOException {
        BufferedBitReader bitInput = new BufferedBitReader(compressedfilename);
        BufferedWriter output = new BufferedWriter(new FileWriter(decompressedfilename));

        // initialize curr to the current root of the Huffman Tree
        HuffmanTree<Character, Integer> curr = queueHuffman.peek();

        // reads the next bit of the compressed file until hasNext() returns false (no more bits/file is complete)
        while (bitInput.hasNext()) {
            boolean bit = bitInput.readBit();

            // making sure to always reach the leaf node and not a null node
            if (curr != null) {

                // true (1) = traverse right, false (0) = traverse left
                if (bit) {
                    curr = curr.getRight();
                } else {
                    curr = curr.getLeft();
                }

                // if we have reached a leaf, write the character that the Tree contains and reset curr to root
                if (curr.isLeaf()) {
                    output.write(curr.getCharacter());
                    curr = queueHuffman.peek();
                }
            }
        }
        // close the bitReader and reader
        bitInput.close();
        output.close();
    }

    public static void main(String[] args) throws Exception {

        // testing WarAndPeace.txt
        try{
            HuffmanEncoder warAndPeaceTest = new HuffmanEncoder();
            String warAndPeaceTestString = "text/WarAndPeace.txt";
            BufferedReader input = new BufferedReader(new FileReader(warAndPeaceTestString));

            int cInt = input.read();

            // handling Empty File
            if (cInt == -1) {
                warAndPeaceTest.loadStringIntoMap("");
            }
            else{
                String loadWarAndPeaceString = warAndPeaceTest.loadFileIntoString(warAndPeaceTestString);
                warAndPeaceTest.loadStringIntoMap(loadWarAndPeaceString);
            }

            String compressedWarAndPeaceTestString = warAndPeaceTestString.substring(0, 16) + "_compressed.txt";
            String decompressedWarAndPeaceTestString = warAndPeaceTestString.substring(0, 16) + "_decompressed.txt";

            warAndPeaceTest.loadMapIntoQueue();

            if(warAndPeaceTest.getQueueHuffman().size() == 2){
                // handling One Char File or File with Repeating Same Char
                // handle createHuffmanCodeTree --> One Char: left child is the new space and right child is the char
                System.out.println("Handling One Char File");

                HuffmanTree<Character, Integer> spaceTree = warAndPeaceTest.getQueueHuffman().remove();
                HuffmanTree<Character, Integer> tempTree = warAndPeaceTest.getQueueHuffman().remove();
                if (tempTree != null) {
                    HuffmanTree<Character, Integer> singularTree = new HuffmanTree<Character, Integer>(tempTree,
                            spaceTree);
                    singularTree.setFrequency(tempTree.getFrequency() + spaceTree.getFrequency());
                    warAndPeaceTest.getQueueHuffman().add(singularTree);
                }

            }
            else if(warAndPeaceTest.getQueueHuffman().size() > 2){
                // calling all the specific methods to eventually compress and decompress a file
                warAndPeaceTest.createHuffmanCodeTree();
            }

            warAndPeaceTest.codeRetrieval();

            warAndPeaceTest.compression(warAndPeaceTestString, compressedWarAndPeaceTestString);

            warAndPeaceTest.decompression(compressedWarAndPeaceTestString, decompressedWarAndPeaceTestString);
        }
        catch(FileNotFoundException fnf){
            System.out.println("File Not Found");
        }



        // testing USConstitution.txt
        try {
            // testing WarAndPeace.txt
            HuffmanEncoder usConstitution = new HuffmanEncoder();
            String usConstitutionString = "text/USConstitution.txt";
            BufferedReader input = new BufferedReader(new FileReader(usConstitutionString));

            int cInt = input.read();

            // handling Empty File
            if (cInt == -1) {
                usConstitution.loadStringIntoMap("");
            }
            else{
                String loadUSConstitutionString = usConstitution.loadFileIntoString(usConstitutionString);
                usConstitution.loadStringIntoMap(loadUSConstitutionString);
            }

            String compressedUSConstitutionString = usConstitutionString.substring(0, 19) + "_compressed.txt";
            String decompressedUSConstitutionString = usConstitutionString.substring(0, 19) + "_decompressed.txt";

            usConstitution.loadMapIntoQueue();

            if(usConstitution.getQueueHuffman().size() == 2){
                // handling One Char File or File with Repeating Same Char
                // handle createHuffmanCodeTree --> One Char: left child is the new space and right child is the char
                System.out.println("Handling One Char File");

                HuffmanTree<Character, Integer> spaceTree = usConstitution.getQueueHuffman().remove();
                HuffmanTree<Character, Integer> tempTree = usConstitution.getQueueHuffman().remove();
                if (tempTree != null) {
                    HuffmanTree<Character, Integer> singularTree = new HuffmanTree<Character, Integer>(tempTree,
                            spaceTree);
                    singularTree.setFrequency(tempTree.getFrequency() + spaceTree.getFrequency());
                    usConstitution.getQueueHuffman().add(singularTree);
                }
            }
            else if(usConstitution.getQueueHuffman().size() > 2){
                // calling all the specific methods to eventually compress and decompress a file
                usConstitution.createHuffmanCodeTree();
            }

            usConstitution.codeRetrieval();

            usConstitution.compression(usConstitutionString, compressedUSConstitutionString);

            usConstitution.decompression(compressedUSConstitutionString, decompressedUSConstitutionString);

            System.out.println(usConstitution.getMatchCodeWords());
            System.out.println(usConstitution.getQueueHuffman());
        }
        catch(FileNotFoundException fnf){
            System.out.println("File Not Found");
        }
        catch(NullPointerException npe){
            System.out.println("Empty File");
        }


        // testing File Not Found
        try{
            String fileNotFoundstring = "text/Macbeth.txt";
            BufferedReader input = new BufferedReader(new FileReader(fileNotFoundstring));

            int cInt = input.read();
        }
        catch(FileNotFoundException fnf){
            System.out.println("File Not Found");
        }


        // testing Empty File
        try {
            HuffmanEncoder emptyFile = new HuffmanEncoder();
            String emptyFileString = "text/EmptyFile.txt";
            BufferedReader input = new BufferedReader(new FileReader(emptyFileString));

            int cInt = input.read();

            // handling Empty File
            if (cInt == -1) {
                emptyFile.loadStringIntoMap("");
            }
            else{
                String loadEmptyFileString = emptyFile.loadFileIntoString(emptyFileString);
                emptyFile.loadStringIntoMap(loadEmptyFileString);
            }

            String compressedEmptyFileString = emptyFileString.substring(0, 10) + "_compressed.txt";
            String decompressedEmptyFileString = emptyFileString.substring(0, 10) + "_decompressed.txt";

            emptyFile.loadMapIntoQueue();

            if(emptyFile.getQueueHuffman().size() == 2){
                // handling One Char File or File with Repeating Same Char
                // handle createHuffmanCodeTree --> One Char: left child is the new space and right child is the char
                System.out.println("Handling One Char File");

                HuffmanTree<Character, Integer> spaceTree = emptyFile.getQueueHuffman().remove();
                HuffmanTree<Character, Integer> tempTree = emptyFile.getQueueHuffman().remove();
                if (tempTree != null) {
                    HuffmanTree<Character, Integer> singularTree = new HuffmanTree<Character, Integer>(tempTree,
                            spaceTree);
                    singularTree.setFrequency(tempTree.getFrequency() + spaceTree.getFrequency());
                    emptyFile.getQueueHuffman().add(singularTree);
                }
            }
            else if(emptyFile.getQueueHuffman().size() > 2){
                // calling all the specific methods to eventually compress and decompress a file
                emptyFile.createHuffmanCodeTree();
            }

            emptyFile.codeRetrieval();

            emptyFile.compression(emptyFileString, compressedEmptyFileString);

            emptyFile.decompression(compressedEmptyFileString, decompressedEmptyFileString);
        }
        catch(FileNotFoundException fnf){
            System.out.println("File Not Found");
        }



        // testing One Char File
        try {
            // testing WarAndPeace.txt
            HuffmanEncoder oneCharFile = new HuffmanEncoder();
            String oneCharString = "text/OneCharFile.txt";
            BufferedReader input = new BufferedReader(new FileReader(oneCharString));

            int cInt = input.read();

            if(cInt == -1){
                throw new NullPointerException();
            }

            String loadOneCharString = oneCharFile.loadFileIntoString(oneCharString);
            String compressedOneCharString = oneCharString.substring(0, 12) + "_compressed.txt";
            String decompressedOneCharString = oneCharString.substring(0, 12) + "_decompressed.txt";

            oneCharFile.loadStringIntoMap(loadOneCharString);

            oneCharFile.loadMapIntoQueue();

            if(oneCharFile.getQueueHuffman().size() == 2){
                // handling One Char File or File with Repeating Same Char
                // handle createHuffmanCodeTree --> One Char: left child is the new space and right child is the char
                System.out.println("Handling One Char File");

                HuffmanTree<Character, Integer> spaceTree = oneCharFile.getQueueHuffman().remove();
                HuffmanTree<Character, Integer> tempTree = oneCharFile.getQueueHuffman().remove();
                if (tempTree != null) {
                    HuffmanTree<Character, Integer> singularTree = new HuffmanTree<Character, Integer>(tempTree,
                            spaceTree);
                    singularTree.setFrequency(tempTree.getFrequency() + spaceTree.getFrequency());
                    oneCharFile.getQueueHuffman().add(singularTree);
                }
            }
            else if(oneCharFile.getQueueHuffman().size() > 2){
                // calling all the specific methods to eventually compress and decompress a file
                oneCharFile.createHuffmanCodeTree();
            }

            oneCharFile.codeRetrieval();

            oneCharFile.compression(oneCharString, compressedOneCharString);

            oneCharFile.decompression(compressedOneCharString, decompressedOneCharString);
        }
        catch(FileNotFoundException fnf){
            System.out.println("File Not Found");
        }



        // testing hello
        try {
            // testing WarAndPeace.txt
            HuffmanEncoder helloFile = new HuffmanEncoder();
            String helloString = "text/hello.txt";
            BufferedReader input = new BufferedReader(new FileReader(helloString));

            int cInt = input.read();

            // handling Empty File
            if (cInt == -1) {
                helloFile.loadStringIntoMap("");
            }
            else{
                String loadHelloString = helloFile.loadFileIntoString(helloString);
                helloFile.loadStringIntoMap(loadHelloString);
            }

            String compressedHello = helloString.substring(0, 4) + "_compressed.txt";
            String decompressedHello = helloString.substring(0, 4) + "_decompressed.txt";

            helloFile.loadMapIntoQueue();

            if(helloFile.getQueueHuffman().size() == 2){
                // handling One Char File or File with Repeating Same Char
                // handle createHuffmanCodeTree --> One Char: left child is the new space and right child is the char
                System.out.println("Handling One Char File");

                HuffmanTree<Character, Integer> spaceTree = helloFile.getQueueHuffman().remove();
                HuffmanTree<Character, Integer> tempTree = helloFile.getQueueHuffman().remove();
                if (tempTree != null) {
                    HuffmanTree<Character, Integer> singularTree = new HuffmanTree<Character, Integer>(tempTree,
                            spaceTree);
                    singularTree.setFrequency(tempTree.getFrequency() + spaceTree.getFrequency());
                    helloFile.getQueueHuffman().add(singularTree);
                }
            }
            else if(helloFile.getQueueHuffman().size() > 2){
                // calling all the specific methods to eventually compress and decompress a file
                helloFile.createHuffmanCodeTree();
            }

            helloFile.codeRetrieval();

            helloFile.compression(helloString, compressedHello);

            helloFile.decompression(compressedHello, decompressedHello);
        }
        catch(FileNotFoundException fnf){
            System.out.println("File Not Found");
        }


//         testing specific data structures of warAndPeace
//        System.out.println(warAndPeaceTest.getQueueHuffman());
//        System.out.println(warAndPeaceTest.getFrequencyTable());
//        System.out.println(warAndPeaceTest.getMatchCodeWords());
    }
}
