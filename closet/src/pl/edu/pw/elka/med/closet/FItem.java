package pl.edu.pw.elka.med.closet;

public class FItem {
	private String item;
	private int freq;
	
	public FItem(String item, int freq) {
		this.item = item;
		this.freq = freq;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	@Override
	public String toString() {
		return item + " = " + freq;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.freq == ((FItem) obj).freq && this.item.equals(((FItem) obj).item);
	}
}
