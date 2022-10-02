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

    /**
     * Mengalikan semua variabel dalam list substituent dan memasukkanya ke ekspresi ini
     * Mirip operasi OBE, tapi untuk parametrik dan substitusi balik gauss/gaussjordan
     * @param multiplier
     * @param substituent
     */
    public void addAndSubstitute(double multiplier, ExpressionList substituent) {
        for (Expression e : substituent.variables) {
            Expression multiplied = new Expression(e.getIsNumber(), e.getNumber() * multiplier, e.getVar());
            if (Math.abs(multiplied.getNumber()) > EPSILON_IMPRECISION) {
                this.variables.add(multiplied);
            }
        }
    }

    /**
     * Simplifikasi term yang ada di dalam expr list
     * Misalnyaa a + a = 2a
     */
    public void simplify() {
        for (int i = 0; i < variables.size(); i++) {
            // System.out.println(this.getStringPrint());
            for (int j = i + 1; j < variables.size(); j++) {
                if (variables.get(i) != null && variables.get(j) != null) {
                    // System.out.println("Currently at" );
                    // System.out.println(variables.get(i).getDisplayExpression() + " " + variables.get(j).getDisplayExpression());
                    // System.out.println("");
                    if (variables.get(i).addExpression(variables.get(j))) {
                        variables.remove(j);
                        j -= 1;
                    }
                }
            }
            if (Math.abs(variables.get(i).getNumber()) < EPSILON_IMPRECISION && variables.size() > 1) {
                variables.remove(i);
                i-=1;
            }
        }
    }

    /**
     * Mendapatkan representasi string yang akan di print dari suatu expr list
     * @return
     */
    public String getStringPrint() {
        boolean printedBefore = false;
        boolean isFirst = true;
        String row = "";
        for (Expression e : this.variables) {
            if (!isFirst) {
                if (Math.abs(e.getNumber()) > EPSILON_IMPRECISION) {
                    if (printedBefore) row += " + ";
                } else {
                    continue;
                }
            }
            isFirst = false;
            if (Math.abs(e.getNumber()) < EPSILON_IMPRECISION && this.variables.size() > 1) {
                printedBefore = false;
                continue;
            }
            row += e.getDisplayExpression();
            printedBefore = true;
        }
        return row;
    }

    public double getFirstDouble() {
        return this.variables.get(0).getNumber();
    }
}
