import java.lang.Math;
public class AVL_Tree{
    private MySet<Position> wordEntriesSet = new MySet<>();
    public MyLinkedList.TreeNode<Position> root;

    public  Boolean IsEmpty(){
        if (root==null)
            return true;
        else
            return false;
    }

    public MyLinkedList.TreeNode<Position> NodeWithKey(int key) {
        MyLinkedList.TreeNode<Position> n = root;
        return NodeWithKeyInSubtree(n,key);
    }

    public MyLinkedList.TreeNode<Position> NodeWithKeyInSubtree(MyLinkedList.TreeNode<Position> node,int key) {
        if(node.data.getWordIndex()==key)
            return node;
        else if(node.data.getWordIndex() < key){
            if(node.right==null)
                return null;
            else
                return NodeWithKeyInSubtree(node.right,key);
        }
        else{
            if(node.left==null)
                return null;
            else
                return NodeWithKeyInSubtree(node.left,key);
        }
    }

    public  Boolean IsMemberSubTree(MyLinkedList.TreeNode<Position> node, Position e){
        if(node.data.equals(e))
            return true;
        else if(node.data.getWordIndex() < e.getWordIndex()){
            if(node.right==null)
                return false;
            else
                return IsMemberSubTree(node.right,e);
        }
        else{
            if(node.left==null)
                return false;
            else
                return IsMemberSubTree(node.left,e);
        }

    }

    public  Boolean IsMember(Position e){
        MyLinkedList.TreeNode<Position> n = root;
        return IsMemberSubTree(n,e);
    }

    public MyLinkedList.TreeNode<Position> InorderSuccessor(MyLinkedList.TreeNode<Position> findSuccessorFor){
        if(findSuccessorFor.right!=null){
            MyLinkedList.TreeNode<Position> successor = findSuccessorFor.right;
            while (successor.left!=null){
                successor=successor.left;
            }
            return successor;
        }
        else{
            MyLinkedList.TreeNode<Position> successor = findSuccessorFor;
            while (successor!=null){
                if (successor.parent!=null){
                    if(successor.parent.left.equals(successor))
                        break;
                }
                successor=successor.parent;
            }
            if(successor!=null)
                successor=successor.parent;
            return successor;
        }
    }

    public void UpdateHeight(MyLinkedList.TreeNode<Position> NodeWithLatestAdd){
        MyLinkedList.TreeNode<Position> n = NodeWithLatestAdd.parent;
        while (n!=null){
            Height(n);
            n=n.parent;
        }

    }

    public void Height(MyLinkedList.TreeNode<Position> treeN){
        if (treeN.left==null && treeN.right==null)
            treeN.NodeHeight = 0;
        else if(treeN.left==null && treeN.right!=null)
            treeN.NodeHeight=treeN.right.NodeHeight+1;
        else if(treeN.right==null && treeN.left!=null)
            treeN.NodeHeight=treeN.left.NodeHeight+1;
        else
            treeN.NodeHeight = Math.max(treeN.left.NodeHeight,treeN.right.NodeHeight)+1;
    }

    public MyLinkedList.TreeNode<Position> ProbableParentFromSubtree(MyLinkedList.TreeNode<Position> node, Position e){
        if (node.data.getWordIndex() < e.getWordIndex()){
            if(node.right==null)
                return node;
            else{
                return ProbableParentFromSubtree(node.right,e);
            }

        }
        else{
            if(node.left==null)
                return node;
            else{
                return ProbableParentFromSubtree(node.left,e);
            }
        }
    }

    public MyLinkedList.TreeNode<Position> avlPropertyBrokenAt(MyLinkedList.TreeNode<Position> node){
        MyLinkedList.TreeNode<Position> n = node;
        while (n!=null){
            if(n.left==null && n.right.NodeHeight>0)
                break;
            else if(n.right==null && n.left.NodeHeight>0)
                break;
            else if(n.left!=null && n.right!=null){
                int diff = n.left.NodeHeight-n.right.NodeHeight;
                if(Math.abs(diff)>1)
                    break;
            }
            n=n.parent;
        }
        return n;
    }

    void addNode(Position data){
        wordEntriesSet.addElement(data);
        if(IsEmpty()){
            MyLinkedList.TreeNode<Position> node = new MyLinkedList.TreeNode<>(data);
            root=node;
        }
        else{
            MyLinkedList.TreeNode<Position> n = this.root;
            MyLinkedList.TreeNode<Position> node =ProbableParentFromSubtree(n,data);
            MyLinkedList.TreeNode<Position> newChild =new MyLinkedList.TreeNode<>(data);
            if (node.data.getWordIndex() < data.getWordIndex()){
                node.right = newChild;
                newChild.parent = node;
                if(node.left==null)
                    node.NodeHeight++;
                UpdateHeight(node);
            }else {
                node.left = newChild;
                newChild.parent = node;
                if(node.right==null)
                    node.NodeHeight++;
                UpdateHeight(node);
            }
            MyLinkedList.TreeNode<Position> breakNode = avlPropertyBrokenAt(node);
            if(breakNode!=null){
                MyLinkedList.TreeNode<Position> breakNodeParent = avlPropertyBrokenAt(node).parent;
//                System.out.println("AVL property broken at"+breakNode.data+"after inserting "+data);
                int compare=0;
                if(breakNode.right==null)
                    compare=-1;
                else if (breakNode.left==null)
                    compare=1;
                else{
                    if(breakNode.left.NodeHeight>breakNode.right.NodeHeight)
                        compare=-1;
                    else
                        compare=1;
                }
                if(compare==-1){
                    if( data.getWordIndex() < breakNode.left.data.getWordIndex() ){
                        breakNode = rotateWithLeftChild(breakNode);
                    }
                    else{
                        breakNode = doubleWithLeftChild(breakNode);
                    }
                }
                else if(compare==1){
                    if( data.getWordIndex() > breakNode.right.data.getWordIndex())
                        breakNode = rotateWithRightChild(breakNode);
                    else
                        breakNode = doubleWithRightChild(breakNode);
                }
                if(breakNodeParent==null){
                    breakNode.parent=null;
                    this.root=breakNode;
                }
                else if(breakNodeParent.data.getWordIndex()>=breakNode.data.getWordIndex()){
                    breakNodeParent.left = breakNode;
                    breakNode.parent=breakNodeParent;
                }
                else{
                    breakNodeParent.right = breakNode;
                    breakNode.parent=breakNodeParent;
                }
            }
        }
    }

    /* Rotate binary tree node with left child */
    private MyLinkedList.TreeNode<Position> rotateWithLeftChild(MyLinkedList.TreeNode<Position> k2)
    {
//        System.out.println("rotateWithLeftChild");
        MyLinkedList.TreeNode<Position> k1 = k2.left;
        if(k2.parent!=null)
            k1.parent = k2.parent;
        k2.parent =k1;
        if(k1.right!=null){
            k2.left=k1.right;
            k1.right.parent = k2;
        }
        else
            k2.left = null;
        k1.right = k2;
        Height(k2);
        Height(k1);
        return k1;
    }

    /* Rotate binary tree node with right child */
    private MyLinkedList.TreeNode<Position> rotateWithRightChild(MyLinkedList.TreeNode<Position> k1)
    {
//        System.out.println("rotateWithRightChild");
        MyLinkedList.TreeNode<Position> k2 = k1.right;
        if(k1.parent!=null)
            k2.parent = k1.parent;
        k1.parent = k2;
        if(k2.left!=null){
            k1.right = k2.left;
            k2.left.parent = k1;
        }
        else
            k1.right = null;
        k2.left = k1;
        Height(k1);
        Height(k2);
        return k2;
    }
    /**
     * Double rotate binary tree node: first left child
     * getWordIndex()th its right child; then node k3 with new left child */
    private MyLinkedList.TreeNode<Position> doubleWithLeftChild(MyLinkedList.TreeNode<Position> k3)
    {
//        System.out.println("doubleRotateWithLeftChild");
        k3.left = rotateWithRightChild( k3.left );
        k3.left.parent = k3;
        return rotateWithLeftChild( k3 );
    }
    /**
     * Double rotate binary tree node: first right child
     * getWordIndex()th its left child; then node k1 with new right child */
    private MyLinkedList.TreeNode<Position> doubleWithRightChild(MyLinkedList.TreeNode<Position> k1)
    {
//        System.out.println("doubleRotateWithRightChild");
        k1.right = rotateWithLeftChild( k1.right );
        k1.right.parent = k1;
        return rotateWithRightChild( k1 );
    }

    /* Function for inorder traversal */
    public void inorder()
    {
        System.out.print("Inorder: ");
        inorder(root);
    }
    private void inorder(MyLinkedList.TreeNode<Position> r)
    {
        if (r != null)
        {
            inorder(r.left);
            System.out.print(r.data.word+":"+r.data.getWordIndex()+" ,");
            inorder(r.right);
        }
    }

    public MyLinkedList<Position> AllWordPositions(){
        return wordEntriesSet.llist;
    }

    public static void main(String[] args){
        AVL_Tree x = new AVL_Tree();
        PageEntry y = new PageEntry("stack_datastructure_wiki");
        Position a= new Position(y,56);
        Position b= new Position(y,45);
        Position c= new Position(y,36);
        Position d= new Position(y,27);
        Position e= new Position(y,195);
        Position f= new Position(y,212);
        Position g= new Position(y,242);
        Position h= new Position(y,242);
        x.addNode(a);
        x.addNode(b);
        x.addNode(c);
        x.addNode(d);
        x.addNode(e);
        x.addNode(f);
        x.addNode(g);
        x.addNode(h);
        x.inorder();
        System.out.println(x.InorderSuccessor(x.NodeWithKey(242)).data.wi);

    }
}
