package main.java.com.stockordersexecution.model;

public class StockOrder {

	 private int stockId;
	 private String side;
	 private String company;
	 private int quantity;
	 public StockOrder(int stockId, String side, String company, int quantity) {
	  this.stockId = stockId;
	  this.side = side;
	  this.company = company;
	  this.quantity = quantity;
	 }
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
	 public int getQuantity() {
	  return quantity;
	 }
	 public void setQuantity(int quantity) {
	  this.quantity = quantity;
	 }
}
