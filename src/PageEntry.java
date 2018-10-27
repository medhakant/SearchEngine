import java.io.*;
import java.util.Scanner;

public class PageEntry {
    public String rawW=""; //the complete string read from the document
    public PageIndex pgIndex = new PageIndex(); //pgIndex contains the page index of all the words in this page
    private String[] words = {}; //cotains each individual strng; made by splitting the rawW file
    private int totalW;  //contains the total no. of words in the document
    public String name; //name of the page; later used for comparing pageEntry
    public AVL_Tree pageWordTree = new AVL_Tree();

    //Constructor method. The argument is the name of the document.
    // Read this le, and create the page index.
    PageEntry(String pageName){
        name = pageName;
        File file = new File("C:\\Users\\Medha Kant\\Desktop\\cs1170350_searchEnginewebpages\\"+pageName);//may need to be modified to point to the webpage address

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replaceAll("[^a-zA-Z0-9+]" ," ").toLowerCase(); //remove the punctuation marks and convert all to lowercase
//                line = line.replaceAll("\\b(a|an|the|they|these|this|for|is|are|was|of|or|and|does|will|whose)\\b\\s?",""); //remove stop words
                line = line.replaceAll("stacks","stack"); //for plural/singular
                line = line.replaceAll("structures","structure");
                line = line.replaceAll("applications","application");
                rawW+=line+" ";
            }
            rawW = rawW.replaceAll("  "," ");
            words = rawW.split(" ");
            totalW =0;
            for(int i=0;i<words.length;i++){
                if(!words[i].matches("\\b(a|an|the|they|these|this|for|is|are|was|of|or|and|does|will|whose)\\b\\s?")){
                    Position p = new Position(this,i);
                    pgIndex.addPositionForWord(words[i],p);
                    Position newWordPos = new Position(this,i);
                    newWordPos.word = words[i];
                    pageWordTree.addNode(newWordPos);
                    totalW++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public float PhraseOccurance(String[] str){
        float phraseFrequency = 0;
        int i = 0;
        MyLinkedList.Node<WordEntry> wordsOfPage = pgIndex.getWordEntries().head;
        while (wordsOfPage!=null && !wordsOfPage.data.word.equals(str[1])){
            wordsOfPage=wordsOfPage.next;
        }
        MyLinkedList.Node<Position> wordPos = wordsOfPage.data.getAllPositionsForThisWord().head;
        while (wordPos!=null){
            MyLinkedList.TreeNode<Position> startIndex = pageWordTree.NodeWithKey(wordPos.data.wi);
            for(i=2;i<str.length;i++){
                if (pageWordTree.InorderSuccessor(startIndex)==null)
                    break;
                if (!pageWordTree.InorderSuccessor(startIndex).data.word.equals(str[i]))
                    break;

                startIndex = pageWordTree.InorderSuccessor(startIndex);
            }
            if(i==str.length)
                phraseFrequency++;

            wordPos = wordPos.next;
        }
        return phraseFrequency;
    }



    //returns the page index
    PageIndex getPageIndex(){
        return pgIndex;
    }

    //return the total no. of words in the webpae
    int totalWords(){
        return totalW;
    }

//    public static void main(String[] args){
//        PageEntry x = new PageEntry("stack_cprogramming");
//        x.pageWordTree.inorder();
//        System.out.println(x.pageWordTree.InorderSuccessor(x.pageWordTree.NodeWithKey(7)).data.word);
//        String test ="the data stack";
//        String[] arr = test.split(" ");
//        System.out.println(x.PhraseOccurance(arr));
//        System.out.println(x.rawW);
//        MyLinkedList.Node<WordEntry> y = x.pgIndex.getWordEntries().head;
//        while (y!=null){
//            MyLinkedList.Node<Position> z= y.data.getAllPositionsForThisWord().head;
//            System.out.print(y.data.word+" ");
//            while (z!=null){
//                System.out.print(z.data.wi+" ");
//                z=z.next;
//            }
//            System.out.println("freq="+y.data.getTermFrequency());
//            System.out.println("");
//            y=y.next;
//        }
//        System.out.println("total words: "+x.totalW);
//    }


}
