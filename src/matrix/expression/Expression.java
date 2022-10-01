package matrix.expression;

public class Expression {
  private static final double EPSILON_IMPRECISION = 0.0001;
  private boolean isNumber;
  private double number;
  private String var;

  public Expression(boolean isNumber, double number, String var) {
    this.isNumber = isNumber;
    this.number = number;
    this.var = var;
  }

  public void setNumber(double number) {
    this.number = number;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public boolean getIsNumber() {
    return this.isNumber;
  }

  public double getNumber() {
    return this.number;
  }

  public String getVar() {
    return this.var;
  }

  public void multiplyExpression(double constant) {
    this.number *= constant;
  }

  public boolean addExpression(Expression b) {
    if (this.isNumber == true && b.isNumber == true) {
      this.number += b.getNumber();
      return true;
    }
    if (this.isNumber == false && b.isNumber == false) {
      if (b.var.equals(this.var)) {
        this.number += b.getNumber();
        return true;
      }
    }
    return false;
  }

  public String getDisplayExpression() {
    String row = "";
    if (this.number < 0)
      row += "(";
    if (this.isNumber) {
      row += String.format("%.2f", this.number);
    } else {
      if (Math.abs(this.number - 1) < EPSILON_IMPRECISION) {
        row += String.format("%s", this.var);
      } else if (Math.abs((-1) * this.number - 1) < EPSILON_IMPRECISION) {
        row += String.format("-%s", this.var);
      } else {
        row += String.format("%.2f%s", this.number, this.var);
      }
    }
    if (this.number < 0)
      row += ")";
    return row;
  }
}
