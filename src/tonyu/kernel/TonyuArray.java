package tonyu.kernel;

import java.util.ArrayList;

public class TonyuArray<E> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;
	
	public boolean add(E obj) { return super.add(obj); }
	public void insert(int i, E obj) { super.add(i, obj); }
	
	public E get(int i) { return super.get(i); }
	public E set(int i, E obj) { return super.set(i, obj); }
	
	public int size() { return super.size(); }
	public int indexOf(Object obj) { return super.indexOf(obj); }
	public int lightIndexOf(Object obj) { return super.indexOf(obj); }
	
	public E delete(int i) { return super.remove(i); }
	public boolean remove(Object obj) { return super.remove(obj); }
	
	public void clear() { super.clear(); }
	
	
	// Tonyuにはないメソッド //
	
	//public void _a() {
		
	//}
	
}
