/****************************************************************************************************
 - Authors:
 João Pedro Rodrigues Vieira         32281730
 Sabrina Midori F. T. de Carvalho    42249511
 - Creation Date:
 03.11.2023
 - Project Description:
 A simple text editor similar to Linux VI.
 ****************************************************************************************************/

package viEditor;

public class List {

    private Node head, tail;
    private int count;

    public List() {
        head = tail = null;
        count = 0;
    }

    public Node getHead() { return head; }

    public Node getTail() { return tail; }

    public int getCount() { return count; }

    public boolean isEmpty() { return count == 0; }

    public boolean isFull() {
        Node temp = new Node();
        return temp == null;
    }

    // Insert in the end of the list
    public boolean append(String data) {

        if (isFull()) return false;

        // Create new node to be inserted in the end of the list
        Node node = new Node(data);

        // List is empty
        if (isEmpty()) head = node;
        else {
            // Insert new node in the end of the list
            tail.setNext(node);
            node.setPrev(tail);
        }

        // The new node is the last node of the list
        tail = node;

        // Circular list
        tail.setNext(head);
        head.setPrev(tail);

        ++count;
        return true;
    }

    // Insert anywhere in the list
    public boolean paste(List copy, int line) {

        if (isFull() || line < 1 || line > count) return false;

        // Start at head; previous is tail since it's a circular list
        Node current = head, previous = tail;

        // Start a counter at 1 which will be line n. 1
        int i = 1;

        // Traverse list while counter isn't the given line
        while (i < line) {
            previous = current;
            current = current.getNext();
            ++i;
        }

        // Previous and current are already at the right nodes
        // Get copy list head and tail
        Node copyHead = copy.getHead(), copyTail = copy.getTail();

        // If line is 1 (first line of the list), head list is copy list head
        if (line == 1) head = copyHead;

        // If line is count (last line of the list), tail list is copy list tail
        if (line == count) tail = copyTail;

        // Link previous from list and head from copy
        previous.setNext(copyHead);
        copyHead.setPrev(previous);

        // Link current from list and tail from copy
        copyTail.setNext(current);
        current.setPrev(copyTail);

        count += copy.getCount();
        return true;
    }

    // Add new line to the list after given position
    public boolean addLineAfter(String data, int pos) {

        if (isFull()) return false;

        // If list is empty, insert at list beginning
        if (isEmpty()) pos = 0;

        // Start at head; previous is tail since it's a circular list
        Node current = head, previous = tail;

        // Start a counter at 1 which will be line n. 1
        int i = 1;

        // Traverse list while counter isn't the given position
        while (i <= pos) {
            previous = current;
            current = current.getNext();
            ++i;
        }

        // Previous and current are already at the right nodes
        // New node with given data
        Node node = new Node(data);

        // Link previous and new node
        previous.setNext(node);
        node.setPrev(previous);

        // Link new node and current node
        node.setNext(current);
        current.setPrev(node);

        // If pos is 0 (first line of the list), update head
        if (pos == 0) head = node;

        // If pos is count (last line of the list), update tail
        if (pos == count) tail = node;

        ++count;
        return true;
    }

    // Add new line to the list before given position
    public boolean addLineBefore(String data, int pos) {

        if (isFull()) return false;

        // If list is empty, insert at list beginning
        if (isEmpty()) pos = 1;

        // Start at head; previous is tail since it's a circular list
        Node current = head, previous = tail;

        // Start a counter at 1 which will be line n. 1
        int i = 1;

        // Traverse list while counter isn't the given position
        while (i < pos) {
            previous = current;
            current = current.getNext();
            ++i;
        }

        // Previous and current are already at the right nodes
        // New node with given data
        Node node = new Node(data);

        // Link previous and new node
        previous.setNext(node);
        node.setPrev(previous);

        // Link new node and current node
        node.setNext(current);
        current.setPrev(node);

        // If pos is 1 (first line of the list), update head
        if (pos == 1) head = node;

        // If pos is count + 1 (last line of the list), update tail
        if (pos == count + 1) tail = node;

        ++count;
        return true;
    }

    public void print(int start, int end) {

        // Start at head
        Node current = head;
        int i = 1;

        div();

        // Traverse the list printing its content
        while (i < start) {
            current = current.getNext();
            ++i;
        }
        while (i <= end) {
            System.out.printf("%3d |    %s%n", i, current.getData());

            // Update current node and counter
            current = current.getNext();
            ++i;
        }

        div();
    }

    public void printEvery10Lines(int startLine, int endLine) {

        if (isEmpty()) System.out.println("List is empty.");
        else {
            // Traverse the list and print its content from startLine to line represented by endLine

            // Create a pointer that starts at head
            Node current = searchLine(startLine);

            // While the list is not over
            for (int i = startLine; i < startLine + 10; i++) {
                if (i > endLine || i > count) break;

                System.out.printf("%3d |    %s%n", i, current.getData());
                current = current.getNext();
            }
        }
    }

    // search
    public Node searchLine(int line) {
        if (isEmpty()) return null;

        Node current = head;
        int i = 1;

        while (i != line && i <= count) {
            current = current.getNext();
            ++i;
        }

        if (i > count) return null;
        return current;
    }

    // remove line
    public boolean removeLine(int line) {

        if (isEmpty()) return false;

        // Initializing a current and previous pointers
        Node current = head, previous = null;

        int i = 1;

        // Iterates through the list until the given line is reached
        while (i != line) {
            previous = current;
            current = current.getNext();
            ++i;
        }

        // Storing the node which will be deleted
        Node removedNode = current;

        // If the given line is the first
        if (i == 1) {
            // There will be a new head
            head = head.getNext();

            // New head and the current tail will be pointing to each other
            head.setPrev(tail);
            tail.setNext(head);

        } else if (i == count) {
            // tail is set to a past node and connects again with the head
            tail = previous;

            tail.setNext(head);
            head.setPrev(tail);

        } else {
            // the previous pointer will now be pointing to the next of the current node
            previous.setNext(current.getNext());

            // the next node of the current pointer will be pointing to the previous node
            current.getNext().setPrev(previous);
        }

        // terminating connections of the deleted node
        removedNode.setPrev(null);
        removedNode.setNext(null);

        --count;
        return true;
    }

    // Remove interval node
    public boolean removeLines(int start, int end) {

        if (isEmpty()) return false;

        // Start at head; previous is tail since it's a circular list
        Node current = head, previous = tail;

        // Start a counter at 1 which will be line n. 1
        int i = 1;

        // Traverse list while counter isn't the start line
        while (i < start) {
            previous = current;
            current = current.getNext();
            ++i;
        }

        // Previous is already at the right node

        // If current node (first node to be removed) is the head
        boolean isHeadFirst = current == head;

        // Traverse list while counter isn't the end line
        while (i < end) {
            current = current.getNext();
            ++i;
        }

        // Link previous and next nodes, removing current node
        previous.setNext(current.getNext());
        current.getNext().setPrev(previous);

        // If first node to be removed is the head, update head and link head and tail
        if (isHeadFirst) {
            head = current.getNext();
            head.setPrev(tail);
            tail.setNext(head);
        }

        // If last node to be removed is the tail, update tail and link tail and head
        if (current == tail) {
            tail = previous;
            tail.setNext(head);
            head.setPrev(tail);
        }

        count -= end - start + 1;
        return true;
    }

    public boolean searchElement(String element) {

        if (isEmpty()) return false;

        Node node = head;
        int line = 1;
        boolean found = false;

        while (node.getNext() != head) {
            if (node.getData().contains(element)) {
                System.out.printf("%3d |    %s%n", line, node.getData());
                found = true;
            }
            node = node.getNext();
            ++line;
        }

        return found;
    }

    public boolean replaceElement(String element, String replacement) {

        if (isEmpty()) return false;

        Node node = head;
        int line = 1;
        boolean replaced = false;

        while (node.getNext() != head) {
            if (node.getData().contains(element)) {
                node.setData(node.getData().replace(element, replacement));
                System.out.printf("%3d |    %s%n", line, node.getData());

                replaced = true;
            }
            node = node.getNext();
            ++line;
        }

        return replaced;
    }

    public void div() {
        System.out.println("\n---------------------------------------------------------------------------------\n");
    }

}
