public class LinkedList {
    private int count;
    private Node head, tail;

    public LinkedList() {
        head = tail = null;
        count = 0;
    }

    public boolean isEmpty() { return count == 0; }

    public boolean isFull() {
        Node temp = new Node();
        return temp == null;
    }

    public int size() { return count; }

    public boolean insertHead(String data) {
        if (isFull()) return false;

        Node temp = new Node(data, null);

        // if our list does not have a single node
        if (isEmpty()) {
            head = temp;
            tail = temp;
            tail.setNext(head);
        } else {
            // if our list already has a node
            temp.setNext(head);
            head = temp;
            tail.setNext(head);
        }
        count++;
        return true;
    }

    public boolean insertTail(String data) {
        if (isFull()) return false;

        Node temp = new Node(data, null);

        if (isEmpty()) {
            head = temp;
            tail = temp;
            tail.setNext(head);
        } else {
            tail.setNext(temp);
            tail = temp;
            tail.setNext(head);
        }
        count++;
        return true;
    }

    public Node searchNode(Node node) {
        if (isEmpty()) return null;

        Node temp = head;
        boolean found = false;

        while (temp.getNext() != head) {
            if (temp == node) {
                found = true;
                break;
            }
            temp = temp.getNext();
        }

        if (!found) return null;
        return temp;
    }
}
