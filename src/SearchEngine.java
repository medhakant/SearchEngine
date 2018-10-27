//the adress from which the webpages are to be read is specified in the 15:PageEntry, please update it before testing this class
//author Medha Kant
import java.util.ArrayList;

public class SearchEngine {
    public InvertedPageIndex invertedIndex;
    public MySort<SearchResult> sortingEngine = new MySort<>();

    //This is the constructor method. It should create an empty InvertedPageIndex.
    SearchEngine(){
        this.invertedIndex = new InvertedPageIndex();
    }

    //This the main stub method that you have to implement. It takes an action as
    //a string. The list of actions, and their format will be described later.
    void performAction(String actionMessage){
        actionMessage = actionMessage.replaceAll("\\b(stacks)\\b\\s?","stack"); //for plural/singular
        actionMessage = actionMessage.replaceAll("\\b(structures)\\b\\s?","structure");
        actionMessage = actionMessage.replaceAll("\\b(applications)\\b\\s?","application");
        String[] arr = actionMessage.split(" ");
        switch (arr[0]) {
            case "addPage":{
                PageEntry x = new PageEntry(arr[1]);
                invertedIndex.addPage(x);
                return;
            }
            case "queryFindPagesWhichContainWord":{
                System.out.print(actionMessage+": ");
                String actionMsg = new String();
                arr[1] = arr[1].toLowerCase();
                if(invertedIndex.hash.hashTable[invertedIndex.hash.getHashIndex(arr[1])]!=null){
                    ArrayList<SearchResult> relevanceList = sortingEngine.sortThisList(invertedIndex.getRelevanceOfPage(arr,false));
                    for (int i=0;i<relevanceList.size();i++){
                        actionMsg+= relevanceList.get(i).getPageEntry().name+", ";
                    }
                }
                if(actionMsg.length()>2)
                    System.out.println(actionMsg.substring(0,actionMsg.length()-2));
                else
                    System.out.println("No webpage contains the word "+ arr[1]);
                return;
            }
            case "queryFindPositionsOfWordInAPage":{
                System.out.print(actionMessage+": ");
                String actionMsg = new String();
                arr[1] = arr[1].toLowerCase();
                if(invertedIndex.hash.hashTable[invertedIndex.hash.getHashIndex(arr[1])]!=null){
                    MyLinkedList.Node<WordEntry> nodeM = invertedIndex.hash.hashTable[invertedIndex.hash.getHashIndex(arr[1])];
                    while (nodeM!=null && !nodeM.data.word.equals(arr[1])){
                        nodeM=nodeM.next;
                    }
                    if(nodeM==null)
                        System.out.print("Webpage "+arr[2]+" does not contain word "+arr[1]);
                    else{
                        MyLinkedList.Node<Position> wordPos = nodeM.data.wordPosition.AllWordPositions().head;
                        while (wordPos!=null){
                            if(wordPos.data.getPageEntry().name.equals(arr[2])){
                                actionMsg+=wordPos.data.getWordIndex()+", ";
                            }
                            wordPos=wordPos.next;
                        }
                        if(actionMsg.equals("")){
                            actionMsg+="No webpage "+arr[2]+" found  ";
                        }
                    }
                }
                else
                    System.out.print("Webpage "+arr[2]+" does not contain word "+arr[1]);

                if(actionMsg.length()>2)
                    System.out.println(actionMsg.substring(0,actionMsg.length()-2));
                else
                    System.out.println();
                return;

            }
            case "queryFindPagesWhichContainAllWords":{
                System.out.print(actionMessage+": ");
                String actionMsg = new String();
                for(int i=1;i<arr.length;i++){
                    arr[i] = arr[i].toLowerCase();
                }
                if(invertedIndex.hash.hashTable[invertedIndex.hash.getHashIndex(arr[1])]!=null){
                    MyLinkedList<PageEntry> dummy = invertedIndex.getPagesWhichContainAllWords(arr).llist;
                    if(dummy.head==null)
                        actionMsg = "No webpage contains all words  ";
                    else {
                        ArrayList<SearchResult> relevanceList = sortingEngine.sortThisList(invertedIndex.getRelevanceOfPage(arr,false));
                        for (int i=0;i<relevanceList.size();i++){
                            MyLinkedList.Node<PageEntry> tempNode = dummy.head;
                            while (tempNode!=null){
                                if (tempNode.data.name.equals(relevanceList.get(i).getPageEntry().name)){
                                    actionMsg+= relevanceList.get(i).getPageEntry().name+", ";
                                }
                                tempNode=tempNode.next;
                            }
                        }
                    }
                }
                if(actionMsg.length()>2)
                    System.out.println(actionMsg.substring(0,actionMsg.length()-2));
                else
                    System.out.println("No webpage contains all words");
                return;
            }
            case "queryFindPagesWhichContainAnyOfTheseWords":{
                System.out.print(actionMessage+": ");
                String actionMsg = new String();
                for(int i=1;i<arr.length;i++){
                    arr[i] = arr[i].toLowerCase();
                }
                if(invertedIndex.hash.hashTable[invertedIndex.hash.getHashIndex(arr[1])]!=null){
                    MyLinkedList.Node<PageEntry> dummy = invertedIndex.getPagesWhichContainAllWords(arr).llist.head;
                    if(dummy==null)
                        actionMsg = "No webpage contains any of the words  ";
                    else{
                        ArrayList<SearchResult> relevanceList = sortingEngine.sortThisList(invertedIndex.getRelevanceOfPage(arr,false));
                        for (int i=0;i<relevanceList.size();i++){
                            actionMsg+= relevanceList.get(i).getPageEntry().name+", ";
                        }
                    }
                }
                if(actionMsg.length()>2)
                    System.out.println(actionMsg.substring(0,actionMsg.length()-2));
                else
                    System.out.println("No webpage contains any of the words  ");
                return;

            }
            case "queryFindPagesWhichContainPhrase":{
                System.out.print(actionMessage+": ");
                String actionMsg = new String();
                for(int i=1;i<arr.length;i++){
                    arr[i] = arr[i].toLowerCase();
                }
                if(invertedIndex.hash.hashTable[invertedIndex.hash.getHashIndex(arr[1])]!=null){
                    MyLinkedList<PageEntry> dummy = invertedIndex.getPagesWhichContainAllWords(arr).llist;
                    if(dummy.head==null)
                        actionMsg+= "No webpage contains this Phrase  ";
                    else {
                        if (invertedIndex.getRelevanceOfPage(arr,true)!=null){
                            ArrayList<SearchResult> relevanceList = sortingEngine.sortThisList(invertedIndex.getRelevanceOfPage(arr,true));
                            for (int i=0;i<relevanceList.size();i++){
                                actionMsg+= relevanceList.get(i).getPageEntry().name+", ";
                            }
                        }else
                            actionMsg+= "No webpage contains this Phrase  ";
                    }
                }
                if(actionMsg.length()>2)
                    System.out.println(actionMsg.substring(0,actionMsg.length()-2));
                else
                    System.out.println("No webpage contains this Phrase");
                return;
            }
            default:
                System.out.println("Enter a valid input");
        }

    }

//    public static void main(String[] args){
//        SearchEngine google = new SearchEngine();
//        google.performAction("addPage stack_cprogramming");
//        google.performAction("queryFindPositionsOfWordInAPage stack stack_cprogramming");
//        google.performAction("addPage stack_datastructure_wiki");
//        google.performAction("queryFindPositionsOfWordInAPage stack stack_cprogramming");
//        google.performAction("queryFindPositionsOfWordInAPage stack stack_datastructure_wiki");
//        google.performAction("queryFindPagesWhichContainWord delhi");
//        google.performAction("queryFindPagesWhichContainWord stack");
//        google.performAction("queryFindPagesWhichContainWord allain");
//        google.performAction("queryFindPagesWhichContainWord allain");
//        google.performAction("queryFindPagesWhichContainWord stack");
//        google.performAction("addPage references");
//        google.performAction("addPage stack_oracle");
//        google.performAction("addPage stacklighting");
//        google.performAction("addPage stackmagazine");
//        google.performAction("addPage stackoverflow");
//        google.performAction("queryFindPagesWhichContainWord stack");
//        google.performAction("queryFindPagesWhichContainWord data");
//        google.performAction("queryFindPagesWhichContainWord abstract");
//        google.performAction("queryFindPagesWhichContainWord delhi");
//        google.performAction("queryFindPagesWhichContainWord data");
//        google.performAction("queryFindPositionsOfWordInAPage stack stack_oracle");
//        google.performAction("queryFindPagesWhichContainAllWords structure data");
//        google.performAction("queryFindPagesWhichContainAllWords data structure");
//        google.performAction("queryFindPagesWhichContainAnyOfTheseWords delhi");
//        google.performAction("queryFindPagesWhichContainPhrase big bang");
//    }
}
