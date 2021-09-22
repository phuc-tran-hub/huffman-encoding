//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//
//public class HuffmanDriver extends HuffmanEncoder{
//    public static void main(String[] args) throws Exception {
//
//        // testing WarAndPeace.txt
//        HuffmanEncoder warAndPeaceTest = new HuffmanEncoder();
//        String warAndPeaceTestString = "text/WarAndPeace.txt";
//        String compressedWarAndPeaceTestString = warAndPeaceTestString.substring(0,16) + "_compressed.txt";
//        String decompressedWarAndPeaceTestString = warAndPeaceTestString.substring(0,16) + "_decompressed.txt";
//
//        // calling all the specific methods to eventually compress and decompress a file
//        String loadMapWarAndPeaceString = warAndPeaceTest.loadFileIntoString(warAndPeaceTestString);
//        warAndPeaceTest.loadStringIntoMap(loadMapWarAndPeaceString);
//
//        warAndPeaceTest.loadMapIntoQueue();
//
//        warAndPeaceTest.createHuffmanCodeTree();
//
//        warAndPeaceTest.codeRetrieval();
//
//        warAndPeaceTest.compression(warAndPeaceTestString, compressedWarAndPeaceTestString);
//
//        warAndPeaceTest.decompression(compressedWarAndPeaceTestString, decompressedWarAndPeaceTestString);
//
//
//
//        // testing USConstitution.txt
//        HuffmanEncoder usConstitution = new HuffmanEncoder();
//        String usConstitutionString = "text/USConstitution.txt";
//        String compressedUSConstitutionString = usConstitutionString.substring(0,19) + "_compressed.txt";
//        String decompressedUSConstitutionString = usConstitutionString.substring(0,19) + "_decompressed.txt";
//
//        // calling all the specific methods to eventually compress and decompress a file
//        String loadMapConstitutionString = usConstitution.loadFileIntoString(usConstitutionString);
//        usConstitution.loadStringIntoMap(loadMapConstitutionString);
//
//        usConstitution.loadMapIntoQueue();
//
//        usConstitution.createHuffmanCodeTree();
//
//        usConstitution.codeRetrieval();
//
//        usConstitution.compression(usConstitutionString, compressedUSConstitutionString);
//
//        usConstitution.decompression(compressedUSConstitutionString, decompressedUSConstitutionString);
//
//
//
//        // testing specific data structures of warAndPeace
////        System.out.println(warAndPeaceTest.getQueueHuffman());
////        System.out.println(warAndPeaceTest.getFrequencyTable());
////        System.out.println(warAndPeaceTest.getMatchCodeWords());
//
//    }
//}
