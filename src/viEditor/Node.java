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

public class Node {
    private String data;
    private Node prev, next;

    public Node() { this(null, null, null); }

    public Node(String data) { this(data, null, null); }

    public Node(String data, Node next, Node prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public Node getNext() { return next; }
    public void setNext(Node next) { this.next = next; }

    public Node getPrev() { return prev; }
    public void setPrev(Node prev) { this.prev = prev; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("data: ")
          .append(data)
          .append(", next(id): ")
          .append(next == null? "null" : next.getData())
          .append(", previous(id): ")
          .append(prev == null? "null" : prev.getData());

        return sb.toString();
    }
}

