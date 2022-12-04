package yandexTask;

/**
 * Счётчик
 * @author dm
 *
 */
public class Counter {
	private int cnt;

	/**
	 * 
	 * @param cntInit начальное значение счётчика
	 */
	public Counter(int cntInit) {
		super();
		this.cnt = cntInit;
	}
	
	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

}
