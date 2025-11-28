import java.util.*;

public class HuffmanEncoder {
    // Huffman Node class
    static class HuffmanNode {
        char character;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;
        public HuffmanNode(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }
        public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
            this.character = '\0'; // Internal node, no specific character
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
        // Check if it's a leaf node
        boolean isLeaf() {
            return left == null && right == null;
        }
    }
    // Comparator for PriorityQueue to order by frequency
    static class NodeComparator implements Comparator<HuffmanNode> {
        public int compare(HuffmanNode n1, HuffmanNode n2) {
            return n1.frequency - n2.frequency;
        }
    }
    /**      * Builds the frequency table for the given text.      *      * @param text The input text.      * @return A map where keys are characters and values are their frequencies.      */
    public static Map<Character, Integer> buildFrequencyTable(String text) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyTable.put(c, frequencyTable.getOrDefault(c, 0) + 1);
        }
        return frequencyTable;
    }

    public static HuffmanNode generateHuffmanTree(Map<Character, Integer> frequencyTable) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(new NodeComparator());
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
            priorityQueue.add(parent);
        }
        return priorityQueue.poll(); // The root of the Huffman tree
    }
    /**      * Generates Huffman codes by traversing the Huffman tree.      *      * @param root The root of the Huffman tree.      * @return A map where keys are characters and values are their Huffman codes.      */
    public static Map<Character, String> generateHuffmanCodes(HuffmanNode root) {
        Map<Character, String> huffmanCodes = new HashMap<>();
        traverseTree(root, "", huffmanCodes);
        return huffmanCodes;
    }
    private static void traverseTree(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            huffmanCodes.put(node.character, code);
        } else {
            traverseTree(node.left, code + "0", huffmanCodes);
            traverseTree(node.right, code + "1", huffmanCodes);
        }
    }
    /**      * Encodes the input text using the generated Huffman codes.      *      * @param text The input text to encode.      * @param huffmanCodes The map of Huffman codes.      * @return The encoded binary string.      */
    public static String encodeText(String text, Map<Character, String> huffmanCodes) {
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedText.append(huffmanCodes.get(c));
        }
        return encodedText.toString();
    }
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        // Prompt the user

        System.out.print("Please provide input: ");
        String inputText =scanner.nextLine(); // Reads an integer
        // 1. Build frequency table
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputText);
        System.out.println("Frequency Table: " + frequencyTable);
        // 2. Build Huffman tree
        HuffmanNode root = generateHuffmanTree(frequencyTable);
        // 3. Generate Huffman codes
        Map<Character, String> huffmanCodes = generateHuffmanCodes(root);
        System.out.println("Huffman Codes: " + huffmanCodes);
        // 4. Encode the text
        String encodedText = encodeText(inputText, huffmanCodes);
        System.out.println("Encoded Text: " + encodedText);
    }
}
 