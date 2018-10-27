import java.lang.Math;
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

    public MyLinkedList.TreeNode<X> NodeWithKey(int key) {
        MyLinkedList.TreeNode<X> n = root;
        return NodeWithKeyInSubtree(n,key);
    }

    public MyLinkedList.TreeNode<X> NodeWithKeyInSubtree(MyLinkedList.TreeNode<X> node,int key) {
        if(((Position)node.data).wi==key)
            return node;
        else if(((Position)node.data).wi < key){
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

    public  Boolean IsMemberSubTree(MyLinkedList.TreeNode<X> node, X e){
        if(node.data.equals(e))
            return true;
        else if(((Position)node.data).wi < (int)e){
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

    public  Boolean IsMember(X e){
        MyLinkedList.TreeNode<X> n = root;
        return IsMemberSubTree(n,e);
    }

    public MyLinkedList.TreeNode<X> InorderSuccessor(MyLinkedList.TreeNode<X> findSuccessorFor){
        if(findSuccessorFor.right!=null){
            MyLinkedList.TreeNode<X> successor = findSuccessorFor.right;
            while (successor.left!=null){
                successor=successor.left;
            }
            return successor;
        }
        else{
            MyLinkedList.TreeNode<X> successor = findSuccessorFor;
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

    public void UpdateHeight(MyLinkedList.TreeNode<X> NodeWithLatestAdd){
        MyLinkedList.TreeNode<X> n = NodeWithLatestAdd.parent;
        while (n!=null){
            Height(n);
            n=n.parent;
        }

    }

    public void Height(MyLinkedList.TreeNode<X> treeN){
        if (treeN.left==null && treeN.right==null)
            treeN.NodeHeight = 0;
        else if(treeN.left==null && treeN.right!=null)
            treeN.NodeHeight=treeN.right.NodeHeight+1;
        else if(treeN.right==null && treeN.left!=null)
            treeN.NodeHeight=treeN.left.NodeHeight+1;
        else
            treeN.NodeHeight = Math.max(treeN.left.NodeHeight,treeN.right.NodeHeight)+1;
    }

    public MyLinkedList.TreeNode<X> ProbableParentFromSubtree(MyLinkedList.TreeNode<X> node, X e){
        if (((Position)node.data).wi < (int)e){
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

    public MyLinkedList.TreeNode<X> avlPropertyBrokenAt(MyLinkedList.TreeNode<X> node){
        MyLinkedList.TreeNode<X> n = node;
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

    void addNode(X data){
        MyLinkedList.TreeNode<X> n = root;
        MyLinkedList.TreeNode<X> node =ProbableParentFromSubtree(n,data);
        MyLinkedList.TreeNode<X> newChild =new MyLinkedList.TreeNode<>(data);
        if ((int)node.data < (int)data){
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
        MyLinkedList.TreeNode<X> breakNode = avlPropertyBrokenAt(node);
        if(breakNode!=null){
            MyLinkedList.TreeNode<X> breakNodeParent = avlPropertyBrokenAt(node).parent;
            System.out.println("AVL property broken at"+breakNode.data+"after inserting "+data);
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
                if( (int)data < (int)breakNode.left.data ){
                    breakNode = rotateWithLeftChild(breakNode);
                }
                else{
                    breakNode = doubleWithLeftChild(breakNode);
                }
            }
            else if(compare==1){
                if( (int)data > (int)breakNode.right.data)
                    breakNode = rotateWithRightChild(breakNode);
                else
                    breakNode = doubleWithRightChild(breakNode);
            }
            if(breakNodeParent==null){
                breakNode.parent=null;
                this.root=breakNode;
            }
            else if((int)breakNodeParent.data>=(int)breakNode.data){
                breakNodeParent.left = breakNode;
                breakNode.parent=breakNodeParent;
            }
            else{
                breakNodeParent.right = breakNode;
                breakNode.parent=breakNodeParent;
            }
        }
    }

    /* Rotate binary tree node with left child */
    private MyLinkedList.TreeNode<X> rotateWithLeftChild(MyLinkedList.TreeNode<X> k2)
    {
        System.out.println("rotateWithLeftChild");
        MyLinkedList.TreeNode<X> k1 = k2.left;
        k2.parent =k1;
        if(k1.right!=null){
            k2.left=k1.right;
            k1.left.parent = k2;
        }
        else
            k2.left = null;
        k1.right = k2;
        Height(k2);
        Height(k1);
        return k1;
    }

    /* Rotate binary tree node with right child */
    private MyLinkedList.TreeNode<X> rotateWithRightChild(MyLinkedList.TreeNode<X> k1)
    {
        System.out.println("rotateWithRightChild");
        MyLinkedList.TreeNode<X> k2 = k1.right;
        k1.parent = k2;
        if(k2.left!=null){
            k1.right = k2.left;
            k1.right.parent = k1;
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
     * with its right child; then node k3 with new left child */
    private MyLinkedList.TreeNode<X> doubleWithLeftChild(MyLinkedList.TreeNode<X> k3)
    {
        System.out.println("doubleRotateWithLeftChild");
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }
    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child */
    private MyLinkedList.TreeNode<X> doubleWithRightChild(MyLinkedList.TreeNode<X> k1)
    {
        System.out.println("doubleRotateWithRightChild");
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }

    /* Function for inorder traversal */
    public void inorder()
    {
        inorder(root);
    }
    private void inorder(MyLinkedList.TreeNode<X> r)
    {
        if (r != null)
        {
            inorder(r.left);
            System.out.print(r.data +" ");
            inorder(r.right);
        }
    }

    public static void main(String[] args){
        AVL_Tree<Integer> binTree = new AVL_Tree<>(32);
        MyLinkedList.TreeNode<Integer> r = binTree.root;
        binTree.addNode(44);
        binTree.addNode(46);
        binTree.addNode(27);
        binTree.addNode(25);
        binTree.addNode(48);
        binTree.addNode(47);
        binTree.addNode(40);
        binTree.addNode(24);
        binTree.addNode(50);
        binTree.addNode(28);
       // binTree.addNode(36);
        System.out.print("\nIn order : ");
        binTree.inorder();
        System.out.println();
        System.out.println("Successor of 32 is: "+binTree.InorderSuccessor(binTree.NodeWithKey(32)).data);
    }
}
