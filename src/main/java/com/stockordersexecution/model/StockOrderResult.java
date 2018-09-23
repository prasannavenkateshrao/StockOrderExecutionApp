package main.java.com.stockordersexecution.model;

public class StockOrderResult {

	 private int stockId;
	 private String side;
	 private String company;
	 private String quantity;
	 private String status;
	 
	public int getStockId() {
	  return stockId;
	 }
	 public void setStockId(int stockId) {
	  this.stockId = stockId;
	 }
	 public String getSide() {
	  return side;
	 }
	 public void setSide(String side) {
	  this.side = side;
	 }
	 public String getCompany() {
	  return company;
	 }
	 public void setCompany(String company) {
	  this.company = company;
	 }
	 public String getQuantity() {
	  return quantity;
	 }
	 public void setQuantity(String quantity) {
	  this.quantity = quantity;
	 }
	 public String getStatus() {
	  return status;
	 }
	 public void setStatus(String status) {
	  this.status = status;
	 }
	 
	 public String toString() { 
		    return this.stockId + "        "+ this.side + "     "+this.company + "      "+this.quantity + "      "+this.status;
		} 
}
