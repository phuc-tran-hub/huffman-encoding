import java.util.Comparator;

/**
 * Overrides Comparator to make a compare method specific to HuffmanTree
 *
 * @author Phuc Tran, COSC 10, 21 Winter
 */
public class TreeComparator implements Comparator<HuffmanTree<Character, Integer>>{

    // compares the HuffmanTrees' frequencies (currently maximum Frequency)
    @Override
    public int compare(HuffmanTree<Character, Integer> t1, HuffmanTree<Character, Integer> t2) {
        return t1.getFrequency() - t2.getFrequency();
    }
}