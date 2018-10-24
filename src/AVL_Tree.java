public class AVL_Tree<X> {
    public MyLinkedList.TreeNode<X> root;
    AVL_Tree(X data){
        this.root  = new MyLinkedList.TreeNode<>(data);
    }

    public  Boolean OnlyRootPresent(){
        if (root.left==null && root.right==null)
            return true;
        else
            return false;
    }

    public  Boolean IsMember(X e){
        MyLinkedList.Node<X> n =llist.head;
        while (n != null)
        {
            if(n.data==e)
                return true;
            n=n.next;
        }
        return false;

    }
}
