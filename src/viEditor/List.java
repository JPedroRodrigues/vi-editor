package viEditor;

public class List {
	private Node head;
	private Node tail;
	private int count;
	
	public List() {
		head = null;
		tail = null;
		count = 0;
	}

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
		if (head == null) { 
			head = node;
		} else {
			// Insert new node in the end of the list
			tail.setNext(node);
			node.setPrevious(tail);
		}

		// The new node is the last of the list
		tail = node;

		// Circular list
		tail.setNext(head);
		head.setPrevious(tail);
		
		count++;
		return true;
	}

	// Insert in the beginning of the list
	public boolean insert(String data) {
		if (isFull()) return false;

		Node node = new Node(data);

		if (head == null) {
			tail = node;
		} else {
			node.setNext(head);
			head.setPrevious(node);
		}

		head = node;
		tail.setNext(head);
		head.setPrevious(tail);

		++count;
		return true;
	}

	// Insert in the middle of the list
	public boolean insertMid(String data, int line) {
		if (isFull()) return false;
		if (line <= 0 || line > count) return false;

		Node node = new Node(data);

		// New node's next points to previous node's next
		node.setNext(node.getPrevious());

		// Previous node's next points to new node
		Node currPointer = head;
		Node prevPointer;

		int index = 1;

		if (line == 1) {
			insert(data);
		} else {
			while (currPointer.getNext() != head) {
				prevPointer = currPointer;
				currPointer = currPointer.getNext();

				if (index == line) {
					// When currPointer reaches the given line
					prevPointer.setNext(node);
					node.setPrevious(prevPointer);

					node.setNext(currPointer);
					currPointer.setPrevious(node);
					break;
				}

				++index;
			}
		}

		++count;
		return true;
	}

	// remove
	public boolean removeLine(int line) {
		if (isEmpty()) return false;

		// Initializing a current and previous pointers
		Node currPointer = head;
		Node prevPointer = null;

		int index = 1;

		// Iterates through the list until the given line is reached
		while (index != line) {
			prevPointer = currPointer;
			currPointer = currPointer.getNext();
			++index;
		}

		// Storing the node which will be deleted
		Node removedNode = currPointer;

		// If the given line is the first
		if (index == 1) {
			// There will be a new head
			head = head.getNext();

			// New head and the current tail will be pointing to each other
			head.setPrevious(tail);
			tail.setNext(head);
		} else if (index == count) {
			// tail is set to a past node and connects again with the head
			tail = prevPointer;

			tail.setNext(head);
			head.setPrevious(tail);
		} else {
			// the previous pointer will now be pointing to the next of the current node
			prevPointer.setNext(currPointer.getNext());

			// the next node of the current pointer will be pointing to the previous node
			currPointer.getNext().setPrevious(prevPointer);
		}

		// terminating connections of the deleted node
		removedNode.setPrevious(null);
		removedNode.setNext(null);

		--count;
		return true;
	}

	// search
	public Node searchLine(int line) {
		if (isEmpty()) return null;

		Node node = head;
		int index = 1;

		while (index != line && index <= count) {
			node = node.getNext();
			++index;
		}

		if (index > count) return null;
		return node;
	}

	public void print(int startLine, int endLine) {
		if (isEmpty()) {
			System.out.println("List is empty!");
		} else {
			// Traverse the list and print its content from startLine to line represented by endLine

			// Create a pointer that starts at head
			Node currentPointer = searchLine(startLine);

			// While the list isn't over
			for (int i = startLine; i < startLine + 10; i++) {
				if (i > endLine || i > len()) break;

				System.out.printf("%3d|\t%s\n", i, currentPointer);
				currentPointer = currentPointer.getNext();
			}
		}
	}

    public boolean searchElement(String element) {
        if (isEmpty()) return false;

        Node node = head;
        int line = 1;
        boolean found = false;

        while (node.getNext() != head) {
            if (node.getData().contains(element)) {
                System.out.printf("%3d|\t%s\n", line, node);
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
				System.out.printf("%3d|\t%s\n", line, node);
				System.out.println("\n");

				replaced = true;
			}
			node = node.getNext();
			++line;
		}

		return replaced;
	}

	public Node getHead() { return head; }

	public Node getTail() { return tail; }

	public int len() { return count; }

	@Override
	public String toString() {
		if (head == null) {
			return null;
		}
		
		Node aux = head;
		
		StringBuilder sb = new StringBuilder();
		sb.append("count: " + count + '\n');

		do {
			sb.append(aux).append(" ->\n");
			aux = aux.getNext();
		} while (aux != head);
		
		return sb.toString();
	}
}
