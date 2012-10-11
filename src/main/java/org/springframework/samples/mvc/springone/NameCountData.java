package org.springframework.samples.mvc.springone;

public class NameCountData implements Comparable {

	private String name;
	
	private int count;

	
	public NameCountData(String name, int count) {
		super();
		this.name = name;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
    @Override
    public int compareTo(Object obj) {
    	NameCountData s = (NameCountData) obj;
        if (name.equals(s.name)) { 
            return 0;
        } else {
            if (count < s.count) {
                return 1;
            } else if (count > s.count) {
                return -1;
            } else {
                return name.compareTo(s.name);
            }
        }
    }

	@Override
	public String toString() {
		return "NameCountData [name=" + name + ", count=" + count + "]";
	}

	
}
