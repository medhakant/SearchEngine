public class WordEntry {
    public String word;
//    public MySet<Position> wordPosition;
    public AVL_Tree wordPosition;

    //Constructor method. The argument is the word for which we are creating the word entry.
    WordEntry(String word){
        this.word = word;
        this.wordPosition = new AVL_Tree();
    }

    //Add a position entry for str.
    void addPosition(Position position){
        wordPosition.addNode(position);
    }

    //Add multiple position entries for str.
    void addPositions(MyLinkedList<Position> positions){
        MyLinkedList.Node<Position> node = positions.head;
        while (node!=null){
            wordPosition.addNode(node.data);
            node = node.next;
        }
    }

    //Return a linked list of all position entries for str.
    MyLinkedList<Position> getAllPositionsForThisWord(){
        return this.wordPosition.AllWordPositions();
    }

    //Return the term frequency of the word in a webpage.
    float getTermFrequency(){
        int i=0;
        MyLinkedList.Node<Position> x = wordPosition.AllWordPositions().head;
        while (x!=null){
            i++;
            x=x.next;
        }
        return i;
    }
}
