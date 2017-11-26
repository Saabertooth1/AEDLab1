package aed.huffman;

import es.upm.aedlib.Position;
import es.upm.aedlib.tree.LinkedBinaryTree;


/**
 * Defines metodos for Huffman encoding of text strings.
 */
public class Huffman {
    private LinkedBinaryTree<Character> huffmanTree;

    public Huffman(LinkedBinaryTree<Character> huffmanTree) {
        // NO CAMBIA ESTE METODO!!! Esta usado durante las pruebas
        this.huffmanTree = huffmanTree;
    }

    /**
     * Creates a Huffman tree (and stores it in the attribute huffmanTree).
     * The shape of the (binary) tree is
     * defined by the array of char-codes.
     */
    public Huffman(CharCode[] paths) {
        this.huffmanTree = new LinkedBinaryTree<Character>();
        this.huffmanTree.addRoot(' ');
        for (CharCode path : paths) {
            Character aux = path.getCh();
            char[] code = path.getCode().toCharArray();
            Position<Character> pos = this.huffmanTree.root();
            for (int i = 0; i < code.length - 1; i++) {
                if (code[i] == '0') {
                    if (!this.huffmanTree.hasLeft(pos)) {
                        this.huffmanTree.insertLeft(pos, ' ');
                    }
                    pos = this.huffmanTree.left(pos);
                } else {
                    if (!this.huffmanTree.hasRight(pos)){
                        this.huffmanTree.insertRight(pos, ' ');
                    }
                    pos = this.huffmanTree.right(pos);
                }
            }
            if (code[code.length - 1] == '0') {
                if (!this.huffmanTree.hasLeft(pos)) {
                    this.huffmanTree.insertLeft(pos, aux);
                }
            } else {
                if (!this.huffmanTree.hasRight(pos)) {
                    this.huffmanTree.insertRight(pos, aux);
                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////////


    /**
     * Huffman encodes a text, returning a new text string
     * containing only characters '0' and '1'.
     */
    public String encode(String text) {
        char[] toEncode = text.toCharArray();
        String comp = "";
        for (char aux : toEncode) {
            String code = findCharacterCode(aux, this.huffmanTree.root());
            if (code != null) {
                comp = comp + code;
            }
        }
        return comp;
    }

    // CAMBIA e UTILIZA si quiereis
    private String findCharacterCode(Character ch, Position<Character> pos) {
        char left = '\u0000'; //Null character
        char right = '\u0000';
        if (this.huffmanTree.hasLeft(pos)) {
            left = this.huffmanTree.left(pos).element();
        }
        if (this.huffmanTree.hasRight(pos)) {
            right = this.huffmanTree.right(pos).element();
        }
        if (left == '\u0000' && right == '\u0000') {
            return null;
        }
        if (left == ch) {
            return "0";
        }
        if (right == ch) {
            return "1";
        }
        String res = null;
        if (left == ' ') {
            res = findCharacterCode(ch, this.huffmanTree.left(pos));
            if (res != null) {
                return "0" + res;
            }
        }
        if (right == ' ') {
            res = findCharacterCode(ch, this.huffmanTree.right(pos));
            if (res != null) {
                return "1" + res;
            }
        }
        return null;
    }


    //////////////////////////////////////////////////////////////////////

    /**
     * Given the Huffman encoded text argument (a string of only
     * '0' and '1's), returns the corresponding normal text.
     */
    public String decode(String encodedText) {
        char[] toDecode = encodedText.toCharArray();
        String res = "";
        Position<Character> pos = this.huffmanTree.root();
        for (char a : toDecode) {
            if (a == '0') {
                if (this.huffmanTree.left(pos).element() == ' ') {
                    pos = this.huffmanTree.left(pos);
                } else {
                    res = res + this.huffmanTree.left(pos).element();
                    pos = this.huffmanTree.root();
                }
            } else {
                if (this.huffmanTree.right(pos).element() == ' ') {
                    pos = this.huffmanTree.right(pos);
                } else {
                    res = res + this.huffmanTree.right(pos).element();
                    pos = this.huffmanTree.root();
                }
            }
        }
        return res;
    }
}