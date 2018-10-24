public class InvertedPageIndex {
    public MyHashTable hash;
    InvertedPageIndex(){
        hash = new MyHashTable();
    }
    //Add a new page entry p to the inverted page index.
    void addPage(PageEntry p){
        MyLinkedList.Node<WordEntry> y = p.pgIndex.getWordEntries().head;
        while (y!=null){
            hash.addPositionsForWord(y.data);
            y=y.next;
        }
    }

    //Return a set of page-entries of webpages which contain the word str.
    MySet<PageEntry> getPagesWhichContainWord(String str){
        MySet<PageEntry> wordOcr = new MySet<>();
        if(hash.hashTable[hash.getHashIndex(str)]==null)
            return null;
        else{
            MyLinkedList.Node<WordEntry> chainL = hash.hashTable[hash.getHashIndex(str)];
            while (chainL!=null&&!chainL.data.word.equals(str)){
                chainL = chainL.next;
            }
            if(chainL!=null && chainL.data.word.equals(str)){
                MyLinkedList.Node<Position> pos = chainL.data.wordPosition.llist.head;
                while (pos!=null){
                    wordOcr.addElement(pos.data.getPageEntry());
                    pos=pos.next;
                }
            }

        }
        return wordOcr;
    }

    //Return a set of page-entries for webpages which contain a sequence
    //of non-connector words (str[0] str[1] ... str[str.len-1]).
    //Assume a webpage which contains the following text: \Data struc-
    //tures is the study of structures for storing data." This webpage
    //contains the phrases: \Data structures", \Data structures study",
    //and \Data structures study structures".
    MySet<PageEntry> getPagesWhichContainPhrase(String str[]){
        MySet<PageEntry> phraseOcr = new MySet<>();
        return phraseOcr;
    }
}
