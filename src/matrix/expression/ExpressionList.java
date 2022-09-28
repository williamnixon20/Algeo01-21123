package matrix.expression;

import java.util.ArrayList;

public class ExpressionList {
    private static final double EPSILON_IMPRECISION = 0.00001;
    public ArrayList<Expression> variables = new ArrayList<Expression>();

    public ExpressionList() {

    }

    public Expression getVariable(int index) {
        return variables.get(index);
    }

    public void addExpression(boolean isNumber, double number, String var) {
        Expression variable = new Expression(isNumber, number, var);
        this.variables.add(variable);
    }

    public void addAndSubstitute(double multiplier, ExpressionList substituent) {
        for (Expression e : substituent.variables) {
            Expression multiplied = new Expression(e.getIsNumber(), e.getNumber() * multiplier, e.getVar());
            if (Math.abs(multiplied.getNumber()) > EPSILON_IMPRECISION) {
                this.variables.add(multiplied);
            }
        }
    }

    public void simplify() {
        for (int i = 0; i < variables.size(); i++) {
            // this.print();
            for (int j = i + 1; j < variables.size(); j++) {
                if (variables.get(i) != null && variables.get(j) != null) {
                    // System.out.println("Currently at" );
                    // variables.get(i).displayExpression();
                    // variables.get(j).displayExpression();
                    // System.out.println("");
                    if (variables.get(i).addExpression(variables.get(j))) {
                        variables.remove(j);
                        if (Math.abs(variables.get(i).getNumber()) < EPSILON_IMPRECISION) {
                            variables.remove(i);
                            break;
                        }
                        j -= 1;
                    }
                }
            }
            if (Math.abs(variables.get(i).getNumber()) < EPSILON_IMPRECISION && variables.size() > 1) {
                variables.remove(i);
            }
            // this.print();
            // System.out.println("");
        }
    }

    public void print() {
        boolean isFirst = true;
        for (Expression e : this.variables) {
            if (!isFirst) {
                if (Math.abs(e.getNumber()) > EPSILON_IMPRECISION) {
                    System.out.printf(" + ");
                } else {
                    continue;
                }
            }
            isFirst = false;
            e.displayExpression();
        }
    }

    public double getFirstDouble() {
        return this.variables.get(0).getNumber();
    }
}
