import java.util.*;

public class Node {

    private String name;
    private Node firstChild;
    private Node nextSibling;

    Node (String n, Node d, Node r) {
        this.name = n;
        this.firstChild = d;
        this.nextSibling = r;
    }

    public static Node parsePostfix (String s) {
        //checking whether input string is in format it should be
        if(s.length() == 0) {
            throw new RuntimeException("String is empty!");
        } else if(s.contains(" ") || s.contains(",,") || s.contains("\t") || s.contains("()")) {
            throw new RuntimeException("String: " + s + " input is invalid");
        } else if(s.contains("((") && s.contains("))")) {
            throw new RuntimeException("String " + s + " contains double brackets making input invalid");
        }

        List<String> str = new ArrayList<String>(Arrays.asList(s.split("")));
        Node root = new Node(null, null, null);
        //creating a stack to keep track on nodes so I can deal with them in FILO fashion
        Stack<Node> nodeStack = new Stack();

        //check whether two roots present
        if (str.get(str.size()-2).equals(",")) {
            throw new RuntimeException("String " + s + " has two roots present. Input invalid!");
        }

        for (int i = 0; i < str.size(); i++) {
            if (str.get(i).equals("(")) {
                //start of brackets implies for a new Node / new child
                nodeStack.push(root);
                root.firstChild = new Node(null, null, null);
                root = root.firstChild;
                if (str.get(i+1).trim().equals(",")) {
                    throw new RuntimeException("Missing first child in: " + s + ". Input invalid!");
                }
            } else if (str.get(i).equals(")")) {
                //end of brackets implies for the end of needing to deal with Node and its siblings/ children
                root = nodeStack.pop();
            } else if (str.get(i).equals(",")) {
                // comma implies of a Next Sibling for the current node
                if (nodeStack.isEmpty() || str.get(i+1).equals(")")) {
                    throw new RuntimeException("Empty Node stack or missing sibling when processing: " + s + ". Input invalid!");
                }
                root.nextSibling = new Node(null, null, null);
                root = root.nextSibling;
            } else {
                // giving a name to the root that the pointer is currently pointing at
                if (root.name == null) {
                    root.name = str.get(i);
                } else {
                    root.name += str.get(i);
                }
            }
        }
        return root;
    }

    public String leftParentheticRepresentation() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.name);

        if (this.firstChild != null) {
            //first iteration separately, so there wouldnt be any extra brackets
            sb.append("(").append(this.firstChild.name);
            sb.append(buildByNodes(this.firstChild));
        }
        return sb.toString();
    }

    public String buildByNodes(Node node){
        StringBuffer sb = new StringBuffer();

        // check whether node has a child
        if (node.firstChild != null) {
            sb.append("(").append(node.firstChild.name);
            sb.append(buildByNodes(node.firstChild));
        }

        // check whether node has a sibling
        if (node.nextSibling != null) {
            sb.append(",").append(node.nextSibling.name);
            sb.append(buildByNodes(node.nextSibling));
        } else {
            sb.append(")");
        }
        return sb.toString();
    }

    public static void main (String[] param) {
        String s = "(B1,C)A";
        Node t = Node.parsePostfix (s);
        String v = t.leftParentheticRepresentation();
        String m = "(b,)A";
        Node r = Node.parsePostfix (m);
        String u = r.leftParentheticRepresentation();
        System.out.println (s + " ==> " + v); // (B1,C)A ==> A(B1,C)
    }
}

