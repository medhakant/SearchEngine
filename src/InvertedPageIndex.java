public class InvertedPageIndex {
    public MyHashTable hash;
    float totalPagesEntry = 1;
    InvertedPageIndex(){
        hash = new MyHashTable();
    }
    //Add a new page entry p to the inverted page index.
    void addPage(PageEntry p){
        totalPagesEntry++;
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
                MyLinkedList.Node<Position> pos = chainL.data.wordPosition.AllWordPositions().head;
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
   // MySet<PageEntry> getPagesWhichContainPhrase(String str[]){}


    MySet<PageEntry> getPagesWhichContainAllWords(String str[]){
        MySet<PageEntry> phraseOcr = new MySet<>();
        int len = str.length;
        MySet<PageEntry> setWithAllWords = new MySet<>();
        if(hash.hashTable[hash.getHashIndex(str[1])]!=null){
            MyLinkedList.Node<PageEntry> dummyOne = getPagesWhichContainWord(str[1]).llist.head;
            while (dummyOne!=null){
                setWithAllWords.addElement(dummyOne.data);
                dummyOne=dummyOne.next;
            }
        }
        for(int i=2;i<len;i++){
            MySet<PageEntry> tempSet = new MySet<>();
            if(hash.hashTable[hash.getHashIndex(str[i])]!=null){
                MyLinkedList.Node<PageEntry> dummyTwo = getPagesWhichContainWord(str[i]).llist.head;
                while (dummyTwo!=null){
                    tempSet.addElement(dummyTwo.data);
                    dummyTwo=dummyTwo.next;
                }
            }
            setWithAllWords=setWithAllWords.intersection(tempSet);
        }
        return setWithAllWords;
    }

    MySet<PageEntry> getPagesWhichContainAnyWords(String str[]){
        MySet<PageEntry> phraseOcr = new MySet<>();
        int len = str.length;
        MySet<PageEntry> setWithAnyWords = new MySet<>();
        if(hash.hashTable[hash.getHashIndex(str[1])]!=null){
            MyLinkedList.Node<PageEntry> dummyOne = getPagesWhichContainWord(str[1]).llist.head;
            while (dummyOne!=null){
                setWithAnyWords.addElement(dummyOne.data);
                dummyOne=dummyOne.next;
            }
        }

        for(int i=2;i<len;i++){
            MySet<PageEntry> tempSet = new MySet<>();
            if(hash.hashTable[hash.getHashIndex(str[i])]!=null){
                MyLinkedList.Node<PageEntry> dummyTwo = getPagesWhichContainWord(str[i]).llist.head;
                while (dummyTwo!=null){
                    tempSet.addElement(dummyTwo.data);
                    dummyTwo=dummyTwo.next;
                }
            }
            setWithAnyWords=setWithAnyWords.union(tempSet);
        }
        return setWithAnyWords;
    }

    //Return the relevance of the webpage
    //for a group of words represented by the array str[ ]. If the flag
    //doTheseWordsRepresentAPhrase is true, it means that the words
    //represent a phrase; otherwise the words are part of a complex query (AND/OR).
    public float getRelevanceOfWordInPage(String word,PageEntry page){
        int count=0;
        MySet<PageEntry> ListOfPagesWhichContainWord = getPagesWhichContainWord(word);
        MyLinkedList.Node<PageEntry> pagesWhichContainWord = ListOfPagesWhichContainWord.llist.head;
        while (pagesWhichContainWord!=null){
            count++;
            pagesWhichContainWord = pagesWhichContainWord.next;
        }
        MyLinkedList.Node<WordEntry> iterator = page.pgIndex.getWordEntries().head;
        while (iterator!=null && !iterator.data.word.equals(word)){
            iterator=iterator.next;
        }
        if (iterator==null)
            return 0;
        else {
            if(Math.log(count/totalPagesEntry)==0)
                return (float) ((iterator.data.getTermFrequency()/page.totalWords()));
            else{
                return (float) (((iterator.data.getTermFrequency()/page.totalWords())*Math.log(count/totalPagesEntry)));
            }
        }
    }
    public MySet<SearchResult> getRelevanceOfPage(String str[ ], boolean doTheseWordsRepresentAPhrase){
        MySet<SearchResult> pageWithRelevance = new MySet<>();
        if(!doTheseWordsRepresentAPhrase){
            int count=0;
            MySet<PageEntry> ListOfPagesWhichContainWord = getPagesWhichContainAnyWords(str);
            MyLinkedList.Node<PageEntry> pagesWhichContainWord = ListOfPagesWhichContainWord.llist.head;
            while (pagesWhichContainWord!=null){
                count++;
                pagesWhichContainWord = pagesWhichContainWord.next;
            }
            float relevance[] = new float[count];
            pagesWhichContainWord = ListOfPagesWhichContainWord.llist.head;
            for(int i=0;i<count;i++){
                relevance[i]=0;
                for(int j=1;j<str.length;j++){
                    relevance[i]+=getRelevanceOfWordInPage(str[j],pagesWhichContainWord.data);
                }
                pagesWhichContainWord=pagesWhichContainWord.next;
            }
            pagesWhichContainWord = ListOfPagesWhichContainWord.llist.head;
            for(int i=0;i<count;i++){
                SearchResult entry= new SearchResult(pagesWhichContainWord.data,relevance[i]);
                pageWithRelevance.addElement(entry);
                pagesWhichContainWord=pagesWhichContainWord.next;
            }

        }else{
            int count=0;
            MySet<PageEntry> ListOfPagesWhichContainWord = getPagesWhichContainAllWords(str);
            MyLinkedList.Node<PageEntry> pagesWhichContainWord = ListOfPagesWhichContainWord.llist.head;
            while (pagesWhichContainWord!=null){
                if(pagesWhichContainWord.data.PhraseOccurance(str)>0)
                    count++;
                pagesWhichContainWord = pagesWhichContainWord.next;
            }
            float relevance[] = new float[count];
            pagesWhichContainWord = ListOfPagesWhichContainWord.llist.head;
            int i=0;
            while (pagesWhichContainWord!=null){
                float m=pagesWhichContainWord.data.PhraseOccurance(str);
                if(m>0){
                    if ((Math.log(count/totalPagesEntry))!=0)
                        relevance[i]= (float)((m/(pagesWhichContainWord.data.totalWords()-((str.length-1)*m)))*(Math.log(count/totalPagesEntry)));
                    else
                        relevance[i]=(m/(pagesWhichContainWord.data.totalWords()-((str.length-1)*m)));
                    SearchResult entry= new SearchResult(pagesWhichContainWord.data,relevance[i]);
                    pageWithRelevance.addElement(entry);
                    i++;
                }
                pagesWhichContainWord=pagesWhichContainWord.next;
            }
        }
        return pageWithRelevance;
    }

//    public static void main(String[] args){
//        PageEntry page1 = new PageEntry("stack_cprogramming");
//        PageEntry page2 = new PageEntry("stack_oracle");
//        PageEntry page3 = new PageEntry("stacklighting");
//        PageEntry page4 = new PageEntry("stackoverflow");
//        PageEntry page5 = new PageEntry("references");
//        InvertedPageIndex invIndex = new InvertedPageIndex();
//        invIndex.addPage(page1);
//        invIndex.addPage(page2);
//        invIndex.addPage(page3);
//        invIndex.addPage(page4);
//        invIndex.addPage(page5);
//        String test ="the stack";
//        String[] arr = test.split(" ");
//        System.out.println(invIndex.getRelevanceOfPage(arr,true).llist.head.data.relevance);
//    }
}
