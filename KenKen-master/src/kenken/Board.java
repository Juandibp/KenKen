package kenken;

import com.google.gson.Gson;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import util.ArrayListMatrix;

/**
 *
 * @author Angelo PC
 */
public class Board {

    public Cell[][] cells;
    public int size;
    public ArrayList<Integer> possibleNum;
    public ArrayList<Operation> operations;
    public ArrayList<ArrayList<Integer>> possibleNumRows;
    public ArrayList<ArrayList<Integer>> possibleNumColumns;
    int prob1;
    int prob2;
    int prob4;

    int probSum;
    int probSub;
    int probDiv;
    int probMod;
    int probMult;
    public static final String[] indexcolors = new String[]{
        "#E83000", "#FFAB00", "#E91E63", "#04F757", "#304FFE", "#A3C8C9", "#636375",
        "#C8D0F6", "#FF8A9A", "#A77500", "#3B9700", "#AEEA00", "#CB7E98", "#D0AC94",
        "#FF5722", "#AA00FF", "#6367A9", "#3F51B5", "#FF6F00", "#795548", "#03A9F4",
        "#4A148C", "#BF5650", "#311B92", "#009688", "#7900D7", "#5B4E51", "#607D8B",
        "#452C2C", "#A05837", "#201625", "#33691E", "#320033", "#F57F17", "#8CD0FF",
        "#6200EA", "#772600", "#C895C5", "#BEC459", "#34362D", "#006064", "#FF1A59",
        "#1E6E00", "#001C1E", "#575329", "#99ADC0", "#D157A0", "#806C66", "#B05B6F",
        "#D50000", "#880E4F", "#FFD600", "#A4E804", "#324E72", "#1B5E20", "#404E55",
        "#00BCD4", "#2962FF", "#DA007C", "#3E2723", "#9B9700", "#9C27B0", "#D1F7CE",
        "#C8A1A1", "#FFF69F", "#B71C1C", "#6B002C", "#7A87A1", "#66E1D3", "#64DD17",
        "#788D66", "#549E79", "#5B4534", "#004B28", "#B4A8BD", "#6A3A4C", "#FF9800",
        "#FF913F", "#885578", "#A3A489", "#BC23FF", "#000000", "#0091EA", "#3A2465",
        "#00A6AA", "#DD2C00", "#FAD09F", "#01579B", "#66796D", "#263238", "#00B8D4",
        "#4CAF50", "#1E0200", "#938A81", "#E65100", "#FF6D00", "#922329", "#C51162",
        "#00C853", "#0089A3", "#72418F", "#00FECF", "#BF360C", "#8ADBB4", "#456648",
        "#012C58", "#8BC34A", "#004D40", "#FDE8DC", "#886F4C", "#0086ED", "#0D47A1",
        "#83AB58", "#FF6832", "#7ED379", "#222800", "#FFEB3B", "#00BFA5", "#827717",
        "#2196F3", "#F44336", "#FFC107", "#CFCDAC", "#311B92", "#D790FF", "#673AB7",
        "#212121", "#CDDC39"

    };
    public Board(Board other){
        this.size = other.size;
        this.possibleNum = new ArrayList<>(other.possibleNum);
        this.operations = new ArrayList<>();
        this.possibleNumRows = ArrayListMatrix.copyArrayList(other.possibleNumRows);
        this.possibleNumColumns = ArrayListMatrix.copyArrayList(other.possibleNumColumns);
        this.cells = new Cell[size][size];
        this.prob1 = other.prob1;
        this.prob2 = other.prob2;
        this.prob4 = other.prob4;
        this.probSum = other.probSum;
        this.probSub = other.probSub;
        this.probDiv = other.probDiv;
        this.probMod = other.probMod;
        this.probMult = other.probMult;
        
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cells[y][x] = new Cell(other.cells[y][x]);
            }
        }
        
        for(Operation op : other.operations){
            this.operations.add(new Operation(op, cells));
        }
    
    }
    public Board(int boardSize, int pProb1, int pProb2, int pProb4, int pProbSum, int pProbSub, int pProbDiv, int pProbMult, int pProbMod) {
        int rand;
        possibleNumRows = new ArrayList<>(boardSize);
        possibleNumColumns = new ArrayList<>(boardSize);
        this.prob1 = pProb1;
        this.prob2 = pProb2;
        this.prob4 = pProb4;
        this.probSum = pProbSum;
        this.probSub = pProbSub;
        this.probDiv = pProbDiv;
        this.probMult = pProbMult;
        this.probMod = pProbMod;
        possibleNum = new ArrayList<>(boardSize);
        size = boardSize;
        cells = new Cell[boardSize][boardSize];
        operations = new ArrayList<>();
        rand = 0;
        for (int y = 0; y < boardSize; y++) {
            possibleNum.add(rand);
            for (int x = 0; x < boardSize; x++) {
                cells[y][x] = new Cell(x, y);
                cells[y][x].number = -1;
            }

            rand++;
        }
        for (int y = 0; y < boardSize; y++) {
            possibleNumRows.add(new ArrayList(possibleNum));
            possibleNumColumns.add(new ArrayList(possibleNum));
        }
        LatinBoardGenerator(possibleNum, 0, size);

        OperationGenerator();
    }

    public void sortOperations() {
        operations.sort(Operation.operationComparator);
    }

    public void initPossibleNumbers() {
        int index;
        int boardSize = size;
        ArrayList<Integer> localPossibleNum = new ArrayList<>(size);
        index = 0;
        for (int y = 0; y < boardSize; y++) {
            localPossibleNum.add(index);
            index++;
        }
        for (int y = 0; y < boardSize; y++) {
            possibleNumRows.add(localPossibleNum);
            possibleNumColumns.add(localPossibleNum);
        }
    }

    public void setCellsToZero() {
        operations.forEach(operation -> {
            operation.setCellsToZero();
        });
    }

    public boolean isSafe(Cell cell, int num) {
        int posX = cell.posX;
        int posY = cell.posY;
        for (int k = 0; k < size; k++) {
            if (posX != k) {
                if (cells[posY][k].number == num) {
                    return false;
                }
            }
            if (posY != k) {
                if (cells[k][posX].number == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean LatinBoardGenerator(ArrayList<Integer> possibleNumbers, int cellNumb, int boardSize) {
        Random r = new Random();
        int rand;
        int contador = 0;
        if (cellNumb == boardSize * boardSize) {
            return true; //Se asignaron todos lo vértices exitosamente
        }
        Cell currentCell = cells[cellNumb / boardSize][cellNumb % boardSize];
        while (!possibleNumbers.isEmpty()) {
            rand = r.nextInt(possibleNumbers.size());
            contador++;
            if (contador > boardSize * 2) {
                return false;
            }
            if (isSafe(currentCell, possibleNumbers.get(rand))) { // Determina si a una celda en particular se le puede asignar un numero
                currentCell.number = possibleNumbers.get(rand);
                if (LatinBoardGenerator(possibleNumbers, cellNumb + 1, boardSize)) {
                    return true;
                }
            }
        }
        currentCell.number = size;

        return false;
    }

    private void OperationGenerator() {

        Cell initialCell;
        int operationId = 0;
        int xpos;
        int ypos;
        int cagedAmount = 0;
        int cageSize;

        while (cagedAmount < size * size) {

            cageSize = getCageSize();
            initialCell = getNextUncagedCell();
            xpos = initialCell.posX;
            ypos = initialCell.posY;
            Operation op = new Operation(-1);
            ArrayList<Cell> shape;
            switch (cageSize) {
                case (1):
                    op = new Operation(operationId);
                    op.addCell(initialCell);
                    cagedAmount++;
                    break;
                case (2):
                    op = new Operation(operationId);
                    op.addCell(initialCell);
                    cagedAmount++;
                    initialCell = getRandomAdjacentCell(xpos, ypos);
                    if (initialCell != null) {
                        op.addCell(initialCell);
                        cagedAmount++;

                    }

                    break;

                case (4):
                    op = new Operation(operationId);
                    op.addCell(initialCell);
                    cagedAmount++;
                    shape = getRandomTetrisShape(xpos, ypos);
                    if (shape != null) {
                        cagedAmount += 3;
                        for (Cell part : shape) {
                            op.addCell(part);
                        }

                    } else {
                        initialCell = getRandomAdjacentCell(xpos, ypos);
                        if (initialCell != null) {
                            op.addCell(initialCell);
                            cagedAmount++;
                        }
                    }

                    break;

            }
            op.generateOperation(probSum, probSub, probDiv, probMult, probMod);
            operations.add(op);
            operationId++;

        }
    }

    private Cell getNextUncagedCell() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (!cells[y][x].caged) {
                    return cells[y][x];
                }
            }

        }
        return null;
    }

    private ArrayList<Cell> getRandomTetrisShape(int x, int y) {
        ArrayList<ArrayList<Cell>> candidates = new ArrayList<>();
        ArrayList<Cell> shape;
        if (y < size - 1) {  //Potential Square Shape
            if (y < size - 2) { //Potential L or T shape.
                if (!cells[y + 1][x].caged && !cells[y + 2][x].caged) {
                    if (x < size - 1) {
                        if (!cells[y + 1][x + 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y + 1][x]);
                            shape.add(cells[y + 2][x]);
                            shape.add(cells[y + 1][x + 1]);
                            candidates.add(shape);
                        }
                        if (!cells[y + 2][x + 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y + 1][x]);
                            shape.add(cells[y + 2][x]);
                            shape.add(cells[y + 2][x + 1]);
                            candidates.add(shape);
                        }
                    }
                    if (x > 0) {

                        if (!cells[y + 1][x - 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y + 1][x]);
                            shape.add(cells[y + 2][x]);
                            shape.add(cells[y + 1][x - 1]);
                            candidates.add(shape);
                        }

                        if (!cells[y + 2][x - 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y + 1][x]);
                            shape.add(cells[y + 2][x]);
                            shape.add(cells[y + 2][x - 1]);
                            candidates.add(shape);
                        }
                    }
                }
            }
            if (!cells[y + 1][x].caged) {
                if (x < size - 1) {
                    if (!cells[y + 1][x + 1].caged && !cells[y][x + 1].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y + 1][x]);
                        shape.add(cells[y + 1][x + 1]);
                        shape.add(cells[y][x + 1]);
                        candidates.add(shape);
                    }
                }
                if (x > 0) {
                    if (!cells[y + 1][x - 1].caged && !cells[y][x - 1].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y + 1][x]);
                        shape.add(cells[y + 1][x - 1]);
                        shape.add(cells[y][x - 1]);
                        candidates.add(shape);
                    }
                }
            }
        }

        if (x < size - 1) { //Potential Square Shape
            if (x < size - 2) { //Potential L or T shape.
                if (!cells[y][x + 1].caged && !cells[y][x + 2].caged) {
                    if (y < size - 1) {
                        if (!cells[y + 1][x + 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x + 1]);
                            shape.add(cells[y][x + 2]);
                            shape.add(cells[y + 1][x + 1]);
                            candidates.add(shape);
                        }
                        if (!cells[y + 1][x + 2].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x + 1]);
                            shape.add(cells[y][x + 2]);
                            shape.add(cells[y + 1][x + 2]);
                            candidates.add(shape);
                        }
                    }
                    if (y > 0) {

                        if (!cells[y - 1][x + 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x + 1]);
                            shape.add(cells[y][x + 2]);
                            shape.add(cells[y - 1][x + 1]);
                            candidates.add(shape);
                        }

                        if (!cells[y - 1][x + 2].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x + 1]);
                            shape.add(cells[y][x + 2]);
                            shape.add(cells[y - 1][x + 2]);
                            candidates.add(shape);
                        }
                    }
                }
            }
            if (!cells[y][x + 1].caged) {
                if (y < size - 1) {
                    if (!cells[y + 1][x + 1].caged && !cells[y + 1][x].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y + 1][x]);
                        shape.add(cells[y + 1][x + 1]);
                        shape.add(cells[y][x + 1]);
                        candidates.add(shape);
                    }
                }
                if (y > 0) {
                    if (!cells[y - 1][x + 1].caged && !cells[y - 1][x].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y - 1][x]);
                        shape.add(cells[y - 1][x + 1]);
                        shape.add(cells[y][x + 1]);
                        candidates.add(shape);
                    }
                }
            }
        }
        if (x > 0) {  //Potential Square Shape
            if (x > 1) { //Potential L or T shape.
                if (!cells[y][x - 1].caged && !cells[y][x - 2].caged) {
                    if (y < size - 1) {
                        if (!cells[y + 1][x - 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x - 1]);
                            shape.add(cells[y][x - 2]);
                            shape.add(cells[y + 1][x - 1]);
                            candidates.add(shape);
                        }
                        if (!cells[y + 1][x - 2].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x - 1]);
                            shape.add(cells[y][x - 2]);
                            shape.add(cells[y + 1][x - 2]);
                            candidates.add(shape);
                        }
                    }
                    if (y > 0) {

                        if (!cells[y - 1][x - 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x - 1]);
                            shape.add(cells[y][x - 2]);
                            shape.add(cells[y - 1][x - 1]);
                            candidates.add(shape);
                        }

                        if (!cells[y - 1][x - 2].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y][x - 1]);
                            shape.add(cells[y][x - 2]);
                            shape.add(cells[y - 1][x - 2]);
                            candidates.add(shape);
                        }
                    }
                }
            }
            if (!cells[y][x - 1].caged) {
                if (y < size - 1) {
                    if (!cells[y + 1][x - 1].caged && !cells[y + 1][x].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y + 1][x]);
                        shape.add(cells[y + 1][x - 1]);
                        shape.add(cells[y][x - 1]);
                        candidates.add(shape);
                    }
                }
                if (y > 0) {
                    if (!cells[y - 1][x - 1].caged && !cells[y - 1][x].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y - 1][x]);
                        shape.add(cells[y - 1][x - 1]);
                        shape.add(cells[y][x - 1]);
                        candidates.add(shape);
                    }
                }

            }
        }

        if (y > 0) {  //Potential Square Shape
            if (y > 1) { //Potential L or T shape.
                if (!cells[y - 1][x].caged && !cells[y - 2][x].caged) {
                    if (x < size - 1) {
                        if (!cells[y - 1][x + 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y - 1][x]);
                            shape.add(cells[y - 2][x]);
                            shape.add(cells[y - 1][x + 1]);
                            candidates.add(shape);
                        }
                        if (!cells[y - 2][x + 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y - 1][x]);
                            shape.add(cells[y - 2][x]);
                            shape.add(cells[y - 2][x + 1]);
                            candidates.add(shape);
                        }
                    }
                    if (x > 0) {

                        if (!cells[y - 1][x - 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y - 1][x]);
                            shape.add(cells[y - 2][x]);
                            shape.add(cells[y - 1][x - 1]);
                            candidates.add(shape);
                        }

                        if (!cells[y - 2][x - 1].caged) {
                            shape = new ArrayList<>();
                            shape.add(cells[y - 1][x]);
                            shape.add(cells[y - 2][x]);
                            shape.add(cells[y - 2][x - 1]);
                            candidates.add(shape);
                        }
                    }
                }
            }
            if (!cells[y - 1][x].caged) {
                if (x < size - 1) {
                    if (!cells[y - 1][x + 1].caged && !cells[y][x + 1].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y - 1][x]);
                        shape.add(cells[y - 1][x + 1]);
                        shape.add(cells[y][x + 1]);
                        candidates.add(shape);
                    }
                }
                if (x > 0) {
                    if (!cells[y - 1][x - 1].caged && !cells[y][x - 1].caged) {
                        shape = new ArrayList<>();
                        shape.add(cells[y - 1][x]);
                        shape.add(cells[y - 1][x - 1]);
                        shape.add(cells[y][x - 1]);
                        candidates.add(shape);
                    }
                }
            }
        }

        if (candidates.size() > 0) {
            return candidates.get(new Random().nextInt(candidates.size()));
        }
        return null;
    }

    private Cell getRandomAdjacentCell(int x, int y) {
        Random r = new Random();
        ArrayList<Cell> candidates = new ArrayList<>();
        if (y < size - 1) {
            if (!cells[y + 1][x].caged) {
                candidates.add(cells[y + 1][x]);
            }
        }

        if (x < size - 1) {
            if (!cells[y][x + 1].caged) {
                candidates.add(cells[y][x + 1]);
            }
        }
        if (x > 0) {
            if (!cells[y][x - 1].caged) {
                candidates.add(cells[y][x - 1]);
            }
        }

        if (y > 0) {
            if (!cells[y - 1][x].caged) {
                candidates.add(cells[y - 1][x]);
            }
        }
        if (candidates.size() > 0) {
            return candidates.get(r.nextInt(candidates.size()));
        }
        return null;
    }

    private int getCageSize() {
        Random r = new Random();

        if (r.nextInt(100) < prob4) {
            return 4;
        }
        if (r.nextInt(100) < prob2) {
            return 2;
        } else {
            return 1;
        }
    }

    public void saveBoard(String filename) throws IOException {
        Gson gson = new Gson();
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter(filename));
        writer.write(gson.toJson(this));
        List<String> lines = Arrays.asList(gson.toJson(this));
        Path file = Paths.get(filename);
        Files.write(file, lines, Charset.forName("UTF-8"));

    }
}
