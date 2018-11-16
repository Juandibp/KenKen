package util;

import java.util.ArrayList;
import kenken.Cell;

public class OperationValidator {


    public static boolean isDivideable(ArrayList<Cell> cells) {

        if (!notZero(cells)) {
            return false;
        }

        int result = cells.get(0).number;
        for (int i = 1; i < cells.size(); i++) {
            if (result == 0) {
                return false;
            }
            if  (result % cells.get(i).number != 0) {
                return false;
            }
            result = result / cells.get(i).number;
        }
        return true;
    }

    public static boolean notZero(ArrayList<Cell> cells) {
        return cells.stream().noneMatch((cell) -> (cell.number == 0));
    }
}
