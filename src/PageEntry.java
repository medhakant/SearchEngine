import java.io.*;
import java.util.Scanner;

public class PageEntry {
    public String rawW=""; //the complete string read from the document
    public PageIndex pgIndex = new PageIndex(); //pgIndex contains the page index of all the words in this page
    private String[] words = {}; //cotains each individual strng; made by splitting the rawW file
    private int totalW;  //contains the total no. of words in the document
    public String name; //name of the page; later used for comparing pageEntry

    //Constructor method. The argument is the name of the document.
    // Read this le, and create the page index.
    PageEntry(String pageName){
        name = pageName;
        File file = new File("D:\\Documents\\Java Projects\\Search Engine\\src\\webpages\\"+pageName);//may need to be modified to point to the webpage address

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replaceAll("[^a-zA-Z0-9+]" ," ").toLowerCase(); //remove the punctuation marks and convert all to lowercase
                line = line.replaceAll("\\b(a|an|the|they|these|this|for|is|are|was|of|or|and|does|will|whose)\\b\\s?",""); //remove stop words
                line = line.replaceAll("stacks","stack"); //for plural/singular
                line = line.replaceAll("structures","structure");
                line = line.replaceAll("applications","application");
//                line = line.replaceAll("operations","operation");
                rawW+=line+" ";
            }
            rawW = rawW.replaceAll("  "," ");
            words = rawW.split(" ");
            totalW = words.length;
            for(int i=0;i<words.length;i++){
                Position p = new Position(this,i);
                pgIndex.addPositionForWord(words[i],p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Return the relevance of the webpage
    //for a group of words represented by the array str[ ]. If the flag
    //doTheseWordsRepresentAPhrase is true, it means that the words
    //represent a phrase; otherwise the words are part of a complex query (AND/OR).
    float getRelevanceOfPage(String str[ ], boolean doTheseWordsRepresentAPhrase){
        return 1;
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
//        PageEntry x = new PageEntry("stack_datastructure_wiki");
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
//    }


}
