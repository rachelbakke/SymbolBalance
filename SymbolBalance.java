import java.io.*;
import java.util.*;

public class SymbolBalance {
	private ArrayList<String> text;
	private String openBrackets = "{<([";
	private String closeBrackets = "}>)]";
	private Stack<Character> myStack = new Stack<Character>();

	public static void main(String[] args) {
		SymbolBalance tester = new SymbolBalance();
		tester.takeInFile(args[0]);
	}

	public SymbolBalance() {
		text = new ArrayList<String>();
	}

	public void takeInFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String content = null;
			while ((content = reader.readLine()) != null) {
				text.add(content);
			}
			reader.close();
			checkSymbols();
		} catch (Exception e) {
			System.out.println("File not found. Please check name and the included .java extension");
		}
	}

	public boolean checkSymbols() {
		boolean insideComment = false;
		boolean insideLiteral = false;
		for (String inLine : text) {
			for (int i = 0; i < inLine.length(); i++) {
				char c = inLine.charAt(i);
				if (!insideComment && c == '"') {
					if (!insideLiteral) {
						myStack.push(c);
						insideLiteral = true;
					} else if (myStack.peek() == '"') {
						insideLiteral = false;
						myStack.pop();
					}
				}
				if (c == '/' && !insideComment && !insideLiteral) {
					if (i + 1 != inLine.length() && inLine.charAt(i + 1) == '*') { // beginning of
						// comment
						myStack.push(c);
					} else if (i != 0 && inLine.charAt(i - 1) == '*') {// end of comment
						myStack.pop();
					} else if (i + 1 != inLine.length() && inLine.charAt(i + 1) == '/')
						i = inLine.length(); // skip to end of whole line bc it is a comment
				} else if (c == '*' && !insideLiteral) {
					if (insideComment && i + 1 != inLine.length() && inLine.charAt(i + 1) == '/'
							&& myStack.peek() == '*') { // end of comment
						myStack.pop();
						myStack.push(c);
						insideComment = false;
					} else if (!insideComment && myStack.peek() == '/') { // beginning of comment
						insideComment = true;
						myStack.pop();
						myStack.push(c);
					} else if (!insideComment && i + 1 != inLine.length() && inLine.charAt(i + 1) == '/') {
						System.out.println("The comment " + c + "/ are unbalanced.");
						return false;
					}
				}

				else if (!insideComment && !insideLiteral) {
					if (openBrackets.indexOf(c) > -1) {// is an opening bracket
						myStack.push(c);
					} else if (closeBrackets.indexOf(c) > -1) { // is a closing bracket
						int index = closeBrackets.indexOf(c);
						if (myStack.isEmpty() || myStack.peek() != openBrackets.charAt(index)) {
							System.out.println("The symbol " + c + " is unbalanced.");
							return false;
						} else
							myStack.pop();
					}
				}
			}
		}
		if (!myStack.isEmpty()) {
			System.out.println("The symbol " + myStack.pop() + " is unbalanced.");
			return false;
		} else {
			System.out.println("Symbol Balance is correct.");
			return true;
		}
	}
}
