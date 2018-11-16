/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import kenken.Board;
import kenken.Cell;
import kenken.Operation;
import view.KenKenGrid;


public class GameBoardThread extends Thread {

    KenKenGrid parent;
    Semaphore sem;
    String threadName;
    Board gameBoard;

    private ArrayList<Integer> getAllNumbers(Operation op, int maxRepeated) {
        int y;
        int x;
        Set<Integer> setX;
        Set<Integer> setY;
        ArrayList<Integer> possibleNum = new ArrayList<>();
        for (Cell cell : op.cells) {
            x = cell.posX;
            y = cell.posY;
            setX = new HashSet<>(gameBoard.possibleNumColumns.get(x));
            setY = new HashSet<>(gameBoard.possibleNumRows.get(y));
            setX.retainAll(setY);
            Integer[] partialPossible = setX.toArray(new Integer[setX.size()]);
            for (int i = 0; i < setX.size(); i++) {
                if (Collections.frequency(possibleNum, partialPossible[i]) < maxRepeated) {
                    possibleNum.add(partialPossible[i]);
                }

            }

        }
        return possibleNum;
    }

    private boolean removeInvalidAux(ArrayList<Integer> permutations, ArrayList<Cell> cells, ArrayList<ArrayList<Integer>> possibleRows, ArrayList<ArrayList<Integer>> possibleColumns) {
        int posx;
        int posy;

        for (int x = 0; x < permutations.size(); x++) {
            posx = cells.get(x).posX;
            posy = cells.get(x).posY;

            if (!possibleRows.get(posy).contains(permutations.get(x)) || !possibleColumns.get(posx).contains(permutations.get(x))) {
                return false;
            }
        }
        for (int i = 0; i < permutations.size(); i++) {
            for (int j = 0; j < permutations.size(); j++) {
                if (j != i) {
                    if (cells.get(i).posX == cells.get(j).posX || cells.get(i).posY == cells.get(j).posY) {
                        if (permutations.get(i).equals(permutations.get(j))) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private ArrayList<ArrayList<Integer>> removeInvalidOperations(ArrayList<ArrayList<Integer>> permutations, Operation op, ArrayList<ArrayList<Integer>> possibleRows, ArrayList<ArrayList<Integer>> possibleColumns) {
        ArrayList<ArrayList<Integer>> solution = new ArrayList<>();
        for (ArrayList<Integer> part : permutations) {
            if (removeInvalidAux(part, op.cells, possibleRows, possibleColumns)) {
                solution.add(part);
            }
        }
        return solution;
    }

   

    private ArrayList<ArrayList<Integer>> deleteReps(ArrayList<ArrayList<Integer>> input) {
        ArrayList<ArrayList<Integer>> newSolution = new ArrayList();
        for (ArrayList<Integer> part : input) {
            if (!newSolution.contains(part)) {
                newSolution.add(part);
            }
        }
        return newSolution;
    }

    private ArrayList<ArrayList<Integer>> getAllPermutations(ArrayList<ArrayList<Integer>> solution) {
        ArrayList<ArrayList<Integer>> newSolution = new ArrayList<>(solution);
        for (int i = 0; i < solution.size(); i++) {
            newSolution.addAll(permutation(new ArrayList<>(solution.get(i))));
        }
        newSolution = deleteReps(newSolution);
        return newSolution;
    }
    
     public ArrayList<ArrayList<Integer>> permutation(ArrayList<Integer> nums) {
        ArrayList<ArrayList<Integer>> accum = new ArrayList<>();
        accum = permutationAux(accum, new ArrayList<>(), nums);
        return accum;
    }

    private ArrayList<ArrayList<Integer>> permutationAux(ArrayList<ArrayList<Integer>> accum, ArrayList<Integer> prefix, ArrayList<Integer> nums) {
        int n = nums.size();
        if (n == 0) {
            accum.add(prefix);
        } else {
            for (int i = 0; i < n; ++i) {
                ArrayList<Integer> newPrefix = new ArrayList<>();
                newPrefix.addAll(prefix);
                newPrefix.add(nums.get(i));
                ArrayList<Integer> numsLeft = new ArrayList<>();
                numsLeft.addAll(nums);
                numsLeft.remove(i);
                permutationAux(accum, newPrefix, numsLeft);
            }
        }
        return accum;
    }

    private ArrayList<ArrayList<Integer>> possibleSubtractionSubset(int subsetSize, int result, ArrayList<Integer> inputNumbers) {
        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        Set<Integer> initialNumb = new HashSet<>(inputNumbers);
        ArrayList<Integer> possibilityCopy;
        ArrayList<ArrayList<Integer>> solutionPermutation;
        int resultingSum;
        ArrayList<ArrayList<Integer>> partialSolution;
        for (Integer initial : initialNumb) {

            resultingSum = initial + ((int) -result);
            if (resultingSum >= 0) {

                possibilityCopy = new ArrayList<>(inputNumbers);
                possibilityCopy.remove(initial);
                partialSolution = possibleSumSubset(subsetSize - 1, resultingSum, new ArrayList<>(possibilityCopy), new ArrayList<>(), new ArrayList<>());
                for (ArrayList<Integer> partialArray : partialSolution) {

                    solutionPermutation = permutation(new ArrayList<>(partialArray));
                    for (ArrayList<Integer> permutaton : solutionPermutation) {
                        permutaton.add(0, initial);
                        if (!solutions.contains(permutaton)) {
                            solutions.add(permutaton);
                        }
                    }

                }
            }
        }
        return solutions;
    }

    private ArrayList<ArrayList<Integer>> possibleSumSubset(int subsetSize, int result, ArrayList<Integer> inputNumbers, ArrayList<Integer> partial, ArrayList<ArrayList<Integer>> solutions) {
        int s = 0;
        ArrayList<Integer> remaining;
        ArrayList<Integer> partial_rec;
        for (int x : partial) {
            s += x;
        }
        if (s == result && partial.size() == subsetSize) {
            if (!solutions.contains(partial)) {
                solutions.add(partial);
            }
        }

        if (s > result || partial.size() > subsetSize) {
            return null;
        }
        for (int i = 0; i < inputNumbers.size(); i++) {
            remaining = new ArrayList<>();
            int n = inputNumbers.get(i);
            for (int j = 1 + i; j < inputNumbers.size(); j++) {
                remaining.add(inputNumbers.get(j));
            }
            partial_rec = new ArrayList<>(partial);
            partial_rec.add(n);
            possibleSumSubset(subsetSize, result, remaining, partial_rec, solutions);
        }
        return solutions;

    }

    private ArrayList<ArrayList<Integer>> possibleMultiplicationSubset(int subsetSize, int result, ArrayList<Integer> inputNumbers, ArrayList<Integer> partial, ArrayList<ArrayList<Integer>> solutions) {
        int s = 0;
        ArrayList<Integer> remaining;
        ArrayList<Integer> partial_rec;
        if (s > result || partial.size() > subsetSize) {
            return null;
        }
        if (partial.size() > 0) {
            s = partial.get(0);
        }
        for (int x = 1; x < partial.size(); x++) {
            s = s * partial.get(x);
        }
        if (s == result && partial.size() == subsetSize) {
            if (!solutions.contains(partial)) {
                solutions.add(partial);
            }
        }

        for (int i = 0; i < inputNumbers.size(); i++) {
            remaining = new ArrayList<>();
            int n = inputNumbers.get(i);
            for (int j = 1 + i; j < inputNumbers.size(); j++) {
                remaining.add(inputNumbers.get(j));
            }
            partial_rec = new ArrayList<>(partial);
            partial_rec.add(n);
            possibleMultiplicationSubset(subsetSize, result, remaining, partial_rec, solutions);
        }
        return solutions;
    }

    private static boolean isFloat(int dividend, int divisor) {
        return !(dividend % divisor == 0);
    }

    private ArrayList<ArrayList<Integer>> possibleDivisionSubset(int subsetSize, int result, ArrayList<Integer> inputNumbers, ArrayList<Integer> partial, ArrayList<ArrayList<Integer>> solutions) {
        int s = 0;
        ArrayList<Integer> remaining;
        ArrayList<Integer> partial_rec;
        if (partial.size() > 0 && !partial.contains((Integer) 0)) {
            s = partial.get(0);
            for (int x = 1; x < partial.size(); x++) {
                if (isFloat(s, partial.get(x))) {
                    s = -1;
                    break;
                }
                s = s / partial.get(x);
            }

        }
        if (s == -1 || partial.size() > subsetSize) {
            return null;
        }

        if (s == result && partial.size() == subsetSize) {
            if (!solutions.contains(partial)) {
                solutions.add(partial);
            }
        }
        if (subsetSize == 2) {
            for (int i = inputNumbers.size() - 1; i > -1; i--) {
                remaining = new ArrayList<>();
                int n = inputNumbers.get(i);
                for (int j = i - 1; j > -1; j--) {
                    remaining.add(inputNumbers.get(j));
                }
                partial_rec = new ArrayList<>(partial);
                partial_rec.add(n);
                possibleDivisionSubset(subsetSize, result, remaining, partial_rec, solutions);
            }
        }
        else{
            for (int i = inputNumbers.size() - 1; i > -1; i--) {
                remaining = new ArrayList<>(inputNumbers);
                int n = inputNumbers.get(i);
                remaining.remove((Integer) n);
                partial_rec = new ArrayList<>(partial);
                partial_rec.add(n);
                possibleDivisionSubset(subsetSize, result, remaining, partial_rec, solutions);
            }
        }
        return solutions;
    }

    private ArrayList<ArrayList<Integer>> possibleModulusSubset(int result, ArrayList<Integer> inputNumbers) {
        ArrayList<ArrayList<Integer>> possibleSolution = new ArrayList<>();
        Set<Integer> initialNumb = new HashSet<>(inputNumbers);
        ArrayList<Integer> partialSolution;
        ArrayList<Integer> possibilityCopy;
        while (inputNumbers.contains((Integer) 0)) {
            inputNumbers.remove((Integer) 0);
        }
        for (Integer initial : initialNumb) {

            possibilityCopy = new ArrayList<>(inputNumbers);
            possibilityCopy.remove(initial);

            for (Integer possibility : possibilityCopy) {
                partialSolution = new ArrayList<>();
                if ((initial % possibility) == (result)) {
                    partialSolution.add(initial);
                    partialSolution.add(possibility);
                    if (!possibleSolution.contains(partialSolution)) {
                        possibleSolution.add(partialSolution);
                    }
                }
            }

        }
        return possibleSolution;
    }

    private ArrayList<ArrayList<Integer>> findPower(int result) {
        ArrayList<ArrayList<Integer>> solution = new ArrayList<>();
        ArrayList<Integer> solutionAux = new ArrayList<>();

        solutionAux.add((int) Math.cbrt(result));
        solution.add(solutionAux);
        return solution;
    }

    private boolean backtrackingRandom(int indice, ArrayList<ArrayList<Integer>> NumPossibleRows, ArrayList<ArrayList<Integer>> NumPossibleColumns) {
        if (!Shared.finished) {

            Random r = new Random();
            int x;
            Integer removable;
            
            if (indice >= gameBoard.operations.size()) {
                return true;
            }

            ArrayList<ArrayList<Integer>> possibleSolutions = possibleSolution(gameBoard.operations.get(indice), NumPossibleRows, NumPossibleColumns);
            ArrayList<ArrayList<Integer>> remainingNumPossibleColumns;
            ArrayList<ArrayList<Integer>> remainingNumPossibleRows;
            Operation currentOp = gameBoard.operations.get(indice);
            while (possibleSolutions.size() > 0 && !Shared.finished) {

                x = r.nextInt(possibleSolutions.size());
                remainingNumPossibleColumns = ArrayListMatrix.copyArrayList(NumPossibleColumns);
                remainingNumPossibleRows = ArrayListMatrix.copyArrayList(NumPossibleRows);
                //se asigna una por una cada una de las solucines posibles, quitando del tablero de posibilidades en las filas y columnas respectivas de cada una de las celdas asignadas
                for (int y = 0; y < gameBoard.operations.get(indice).cells.size(); y++) {
                    removable = possibleSolutions.get(x).get(y);
                    currentOp.cells.get(y).number = removable;
                    remainingNumPossibleColumns = removeNumberUsedC(removable, remainingNumPossibleColumns, currentOp.cells.get(y).posX, currentOp.cells.get(y).posY);
                    remainingNumPossibleRows = removeNumberUsedR(removable, remainingNumPossibleRows, currentOp.cells.get(y).posX, currentOp.cells.get(y).posY);

                }
                if (threadName.equals("thread0") && !Shared.finished) {
                    parent.displayOpResult(indice, possibleSolutions.get(x), gameBoard);
                }
                if (backtrackingRandom(indice + 1, remainingNumPossibleRows, remainingNumPossibleColumns)) {
                    return true;
                }
                possibleSolutions.remove(x);
                if (threadName.equals("thread0") && !Shared.finished) {
                    parent.removeOpResult(indice, gameBoard);
                }
            }

        }
        return false;
    }

    private ArrayList<ArrayList<Integer>> removeNumberUsedR(int number, ArrayList<ArrayList<Integer>> NumPossibleRows, int posX, int posY) {
        NumPossibleRows.get(posY).remove((Integer) number);
        return NumPossibleRows;
    }

    private ArrayList<ArrayList<Integer>> removeNumberUsedC(int number, ArrayList<ArrayList<Integer>> NumPossibleColumns, int posX, int posY) {
        NumPossibleColumns.get(posX).remove((Integer) number);
        return NumPossibleColumns;
    }

    private ArrayList<ArrayList<Integer>> possibleSolution(Operation op, ArrayList<ArrayList<Integer>> possibleRows, ArrayList<ArrayList<Integer>> possibleColumns) {

        ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
        ArrayList<Integer> possibleNum = new ArrayList<>();
        switch (op.cellAmount) {
            case (1):
                possibleNum = getAllNumbers(op, 1);
                break;
            case (2):
                possibleNum = getAllNumbers(op, 1);
                break;
            case (4):
                possibleNum = getAllNumbers(op, 2);
                break;
        }

        switch (op.operationType) {
            case POWER:
                solutions = findPower((int) op.operationResult);
                break;

            case SUM:
                solutions = possibleSumSubset(op.cellAmount, op.operationResult, possibleNum, new ArrayList<>(), new ArrayList<>());
                break;
            case SUB:
                solutions = possibleSubtractionSubset(op.cellAmount, op.operationResult, possibleNum);
                break;
            case MULT:
                solutions = possibleMultiplicationSubset(op.cellAmount, op.operationResult, possibleNum, new ArrayList<>(), new ArrayList<>());
                break;
            case DIV:
                solutions = possibleDivisionSubset(op.cellAmount, op.operationResult, possibleNum, new ArrayList<>(), new ArrayList<>());
                break;
            case MOD:
                solutions = possibleModulusSubset(op.operationResult, possibleNum);
                break;
        }
        if (op.operationType == Operation.OperationType.SUM || op.operationType == Operation.OperationType.MULT) {
            solutions = getAllPermutations(new ArrayList<>(solutions)); //hay que encontrar las permutaciones de las posibilidades dadas
        }

        solutions = removeInvalidOperations(new ArrayList<>(solutions), op, possibleRows, possibleColumns); //hay que quitar aquellas permutaciones que no sean validas  
        return solutions;
    }

    public GameBoardThread(Semaphore sem, String threadName, Board gameBoard, KenKenGrid father) {
        super(threadName);
        this.sem = sem;
        this.threadName = threadName;
        this.gameBoard = new Board(gameBoard);
        this.parent = father;

    }

    @Override
    public void run() {
        System.out.println("Se inicio la ejecucion en el hilo " + this.threadName);
        if (backtrackingRandom(0, ArrayListMatrix.copyArrayList(gameBoard.possibleNumRows), ArrayListMatrix.copyArrayList(gameBoard.possibleNumColumns))) {
            try {
                sem.acquire();
                Shared.finished = true;
                System.out.println("Termino primero el hilo " + this.threadName);
                this.parent.desplegarConSolucion(this.gameBoard);
                sem.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(GameBoardThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Se detuvo la ejecucion en el hilo " + this.threadName);
    }
}
