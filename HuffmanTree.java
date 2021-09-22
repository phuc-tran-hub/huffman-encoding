import java.util.*;

/**
 *
 HuffmanTree: holds a character and frequency (how often the character appears)
 *
 * @author Phuc Tran, COSC 10, 21 Winter
 *
 */
public class HuffmanTree<C, F>{

    //generic types
    private C character;
    private F frequency;

    // children are also HuffmanTre
    private HuffmanTree<C, F> left, right;


    /**
     * Constructs leaf node -- left and right are null
     */
    public HuffmanTree(C character, F frequency) {
        this.character = character; this.frequency = frequency;
    }

    public HuffmanTree(HuffmanTree<C,F> left) {
        this.left = left;;
    }

    /**
     * Constructs inner node
     */
    public HuffmanTree(HuffmanTree<C,F> left, HuffmanTree<C, F> right) {
        this.left = left; this.right = right;
    }


    // setter functions
    public void setFrequency(F frequency){
        this.frequency = frequency;
    }


    // getter functions
    public C getCharacter() {
        return character;
    }

    public F getFrequency() {
        return frequency;
    }

    public HuffmanTree<C, F> getLeft() {
        return left;
    }

    public HuffmanTree<C, F> getRight() {
        return right;
    }

    /**
     * Is it a leaf node?
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /**
     * Does it have a left child?
     */
    public boolean hasLeft() {
        return left != null;
    }

    /**
     * Does it have a right child?
     */
    public boolean hasRight() {
        return right != null;
    }

    /**
     * Traverse a HuffmanTree and return a Map that contains Generic C character as keys and String bit codes as values
     */
    public Map<C, String> traverseTree(String codeWords){
        Map<C, String> matchCodeWords = new TreeMap<>();
        traverseTreeHelper(matchCodeWords, codeWords);
        return matchCodeWords;

    }

    /**
     * Helper function for traverseTree()
     */
    public void traverseTreeHelper(Map<C, String> matchCodeWords, String codeWords){

        // base case: add a new key and value
        if(isLeaf()){
            matchCodeWords.put(character, codeWords);
        }

        // if the Tree has a left child, recursively call with the String including "0"
        if(hasLeft()){
            left.traverseTreeHelper(matchCodeWords, codeWords + "0");
        }

        // if the Tree has a right child, recursively call with the String including "1"
        if(hasRight()){
            right.traverseTreeHelper(matchCodeWords, codeWords + "1");
        }
    }


    /**
     * Returns a string representation of the tree
     */
    public String toString() {
        return toStringHelper("");
    }


    /**
     * Recursively constructs a String representation of the tree from this node,
     * starting with the given indentation and indenting further going down the tree
     */
    public String toStringHelper(String indent) {
        String res = indent + "character: " + character + " frequency: " + frequency + "\n";
        if (hasLeft()) res += left.toStringHelper(indent+"  ");
        if (hasRight()) res += right.toStringHelper(indent+"  ");
        return res;
    }

}
