//the adress from which the webpages are to be read is specified in the 15:PageEntry, please udate it before testing this class

public class SearchEngine {
    public InvertedPageIndex invertedIndex;

    //This is the constructor method. It should create an empty InvertedPageIndex.
    SearchEngine(){
        this.invertedIndex = new InvertedPageIndex();
    }

    //This the main stub method that you have to implement. It takes an action as
    //a string. The list of actions, and their format will be described later.
    void performAction(String actionMessage){
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
                    MyLinkedList.Node<PageEntry> dummy = invertedIndex.getPagesWhichContainWord(arr[1]).llist.head;
                    if(dummy==null)
                        actionMsg = "No webpage contains word "+arr[1]+", ";
                    while (dummy!=null){
                        actionMsg += dummy.data.name+", ";
                        dummy = dummy.next;
                    }
                }
                System.out.println(actionMsg.substring(0,actionMsg.length()-2));
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
                        MyLinkedList.Node<Position> wordPos = nodeM.data.wordPosition.llist.head;
                        while (wordPos!=null){
                            if(wordPos.data.getPageEntry().name.equals(arr[2])){
                                actionMsg+=wordPos.data.getWordIndex()+", ";
                            }
                            wordPos=wordPos.next;
                        }
                    }
                }
                else
                    System.out.print("Webpage "+arr[2]+" does not contain word "+arr[1]);

                if(actionMsg.length()>2)
                    System.out.println(actionMsg.substring(0,actionMsg.length()-2));
                else
                    System.out.println("");
                return;

            }
            case "queryFindPagesWhichContainAllWords":{

            }
            case "queryFindPagesWhichContainAnyOfTheseWords":{

            }
            case "queryFindPagesWhichContainPhrase":{

            }
            default:
                System.out.println("Enter a valid input");
        }

    }
//
//    public static void main(String[] args){
//        SearchEngine google = new SearchEngine();
//        google.performAction("addPage stack_cprogramming");
//        google.performAction("addPage stack_datastructure_wiki");
//        google.performAction("queryFindPositionsOfWordInAPage stack stack_cprogramming");
//        google.performAction("queryFindPositionsOfWordInAPage stack stack_datastructure_wiki");
////        System.out.println(google.invertedIndex.hash.hashTable[80].data.getTermFrequency());
////        System.out.println(google.invertedIndex.hash.hashTable[80].data.wordPosition.llist.head.next.data.getWordIndex());
////        System.out.println(google.invertedIndex.getPagesWhichContainWord("st").llist.head.data.name);
//        google.performAction("queryFindPagesWhichContainWord delhi");
//        google.performAction("queryFindPagesWhichContainWord stack");
//        google.performAction("queryFindPagesWhichContainWord allain");
////        System.out.println(google.invertedIndex.hash.hashTable[80].data.getTermFrequency());
//        google.performAction("queryFindPagesWhichContainWord allain");
//        google.performAction("queryFindPagesWhichContainWord stack");
//    }
}
