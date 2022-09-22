package matrix.expression;

public class Expression {
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

}
