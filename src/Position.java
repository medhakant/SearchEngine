public class Position {

    public PageEntry pe;
    public int wi;

    //Constructor method.
    Position(PageEntry p, int wordIndex){
        this.pe = p;
        this.wi = wordIndex;
    }

    //Return pi
    PageEntry getPageEntry(){
        return this.pe;
    }

    //Return wordIndex
    int getWordIndex(){
        return this.wi;
    }
}
