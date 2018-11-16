package kenken;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import util.OperationValidator;

public class Operation implements Comparable<Operation> {

    public OperationType operationType;
    public int operationId;
    public int cellAmount;
    public int operationResult;
    public ArrayList<Cell> cells;

    public enum OperationType {
        POWER, DIV, SUB, MULT, SUM, MOD
    }

    public Operation(int id) {
        this.operationId = id;
        this.cellAmount = 0;
        cells = new ArrayList<>();
    }
    
    public Operation(Operation other, Cell[][] cellArray) {
        this.operationId = other.operationId;
        this.operationType = other.operationType;
        this.cellAmount = other.cellAmount;
        this.operationResult = other.operationResult;
        cells = new ArrayList<>();
        for(int i = 0; i< this.cellAmount; i++){
            cells.add(cellArray[other.cells.get(i).posY][other.cells.get(i).posX]);
        }
    }
    public void addCell(Cell cell) {
        cell.caged = true;
        cell.operationId = this.operationId;
        this.cells.add(cell);
        cellAmount++;
    }

    public void generateResult() {
        int auxResult = 0;
        switch (operationType) {
            case POWER:
                auxResult = (int) Math.pow(cells.get(0).number, 3);
                break;
            case SUM:
                for (int i = 0; i < cells.size(); i++) {
                    auxResult = auxResult + cells.get(i).number;
                }
                break;
            case SUB:
                auxResult = cells.get(0).number;
                for (int i = 1; i < cells.size(); i++) {
                    auxResult = auxResult - cells.get(i).number;
                }
                break;
            case MULT:
                auxResult = 1;
                for (int i = 0; i < cells.size(); i++) {
                    auxResult = auxResult * cells.get(i).number;
                }
                break;
            case DIV:
                auxResult = cells.get(0).number;
                for (int i = 1; i < cells.size(); i++) {
                    auxResult = (int) (auxResult /  cells.get(i).number);
                }
                break;
            case MOD:
                auxResult = cells.get(0).number;
                for (int i = 1; i < cells.size(); i++) {
                    auxResult = auxResult % cells.get(i).number;
                }
                break;
        }
        operationResult = auxResult;
    }

    public void setCellsToZero() {
        cells.forEach((cell) -> {
            cell.number = -1;
        });
    }

    public void generateOperation(int probSum, int probSub, int probDiv, int probMult, int probMod) {

        Random r = new Random();
        cells.sort(Cell.cellComparator);
        if (r.nextInt(10) < 5) {
            operationType = OperationType.SUM;
        } else {
            operationType = OperationType.SUB;
        }
        switch (cellAmount) {
            case 1:
                operationType = OperationType.POWER;
                break;
            case 2:
                if (r.nextInt(100) < probDiv) {
                    if (OperationValidator.isDivideable(cells)) {
                        operationType = OperationType.DIV;
                    }
                } else if (r.nextInt(100) < probMod) {
                    if (OperationValidator.notZero(cells)) {
                        operationType = OperationType.MOD;
                    }
                } else if (r.nextInt(100) < probMult) {
                    if (OperationValidator.notZero(cells)) {
                        operationType = OperationType.MULT;
                    }
                } else if (r.nextInt(100) < probSum) {
                    operationType = OperationType.SUM;
                } else if (r.nextInt(100) < probSub) {
                    operationType = OperationType.SUB;
                }

                break;

            case 4:
                if (r.nextInt() < probDiv) {
                    if (OperationValidator.isDivideable(cells)) {
                        operationType = OperationType.DIV;
                    }
                } else if (r.nextInt() < probMult) {
                    if (OperationValidator.notZero(cells)) {
                        if (OperationValidator.notZero(cells)) {
                            operationType = OperationType.MULT;
                        }
                    }
                } else if (r.nextInt() < probSum) {
                    operationType = OperationType.SUM;
                } else if (r.nextInt() < probSub) {
                    operationType = OperationType.SUB;
                }

                break;
        }
        generateResult();

    }

    private String getOperationCharacter() {
        switch (operationType) {
            case POWER:
                return "^";
            case SUM:
                return "+";
            case SUB:
                return "-";
            case MULT:
                return "*";
            case DIV:
                return "/";

            case MOD:
                return "%";
        }
        return "";

    }

    @Override
    public String toString() {
        String result;
        if (operationResult % 1 == 0) {
            int auxNumb = (int) operationResult;
            result = auxNumb + "";
        } else {
            result = String.format("%.2f", operationResult);
        }

        result += getOperationCharacter();
        return result;
    }

    public String toStringRestOfCells(Cell cell){
        String result="";
        int numb = cell.number;
        if (numb > 0) {
            result = result + System.lineSeparator() + "     " + cells.get(0).number;
        } else {
            result = result + System.lineSeparator() + "    " + cells.get(0).number;
        }
        return result;
    }
    public String toStringFirstCell() {
        String result;
        result = operationResult + "";
      
        int numb = cells.get(0).number;

        result += getOperationCharacter();

        if (numb > -1) {
            result = result + System.lineSeparator() + "     " + numb;
        }

        return result;
    }

    @Override
    public int compareTo(Operation another) {
        if(this.cellAmount == another.cellAmount){
            return this.operationType.compareTo(another.operationType);
        }
        return Integer.compare(this.cellAmount, another.cellAmount);

    }

    public static Comparator<Operation> operationComparator = new Comparator<Operation>() {
        @Override
        public int compare(Operation op1, Operation op2) {
            return op1.compareTo(op2);
        }

    };
    
}
