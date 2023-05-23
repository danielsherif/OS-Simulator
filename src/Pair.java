
public class Pair {
	
	private String variable;
	private Object value;
	
	public Pair(String name, Object val){
		variable=name;
		value=val;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	

}
