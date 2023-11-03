package viEditor;

public class Node {
	private String data;
	private Node previous;
	private Node next;
	
	public Node() { this(null, null, null); }
	
	public Node(String data) { this(data, null, null); }
	
	public Node(String data, Node next, Node previous) {
		this.data = data;
		this.next = next;
		this.previous = previous;
	}
	
	public String getData() { return data; }
	public void setData(String data) { this.data = data; }
	
	public Node getNext() { return next; }
	public void setNext(Node next) { this.next = next; }
	
	public Node getPrevious() { return previous; }
	public void setPrevious(Node previous) { this.previous = previous; }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(data);
		
		return sb.toString();
	}
}

