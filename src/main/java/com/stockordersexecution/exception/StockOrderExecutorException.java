package main.java.com.stockordersexecution.exception;

public class StockOrderExecutorException extends Exception{

	public StockOrderExecutorException(String errorMessage, Throwable clause) {
		super(errorMessage, clause);
	}
}
