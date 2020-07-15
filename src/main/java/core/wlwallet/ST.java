package core.wlwallet;

import java.util.HashMap;
import java.util.Stack;

public class ST<T> {
	
	Stack<T> stack;
	HashMap<Integer, T> OP_CODE_FUNCTIONS;
	
	
	public ST(){
		
	}
	
	public T pop(){
		return stack.pop();
	}
	
	public T peek(){
		return stack.peek();
	}
	
	public void push(T item){
		stack.push(item);
	}
	
	public int size(){
		return stack.size();
	}
}
