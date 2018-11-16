
package kenken;

import java.util.Comparator;

public class Cell implements Comparable<Cell>{
    public int number;
    public int posX;
    public int posY;
    public boolean caged;
    public int operationId;
    
    public Cell(Cell other){
        this.number = other.number;
        this.posX = other.posX; 
        this.posY = other.posY;
        this.caged = other.caged;
        this.operationId = other.operationId;
    }
    public Cell(int x, int y){
        this.posX = x; 
        this.posY = y;
        this.caged = false;
    }
    @Override
    public int compareTo(Cell another) {
        if(this.posX == another.posX){
            return Integer.compare(this.posY, another.posY);
        }
        else {
            return Integer.compare(this.posX, another.posX);
        }
    }
    
    public static Comparator<Cell> cellComparator = new Comparator<Cell>() {

	    public int compare(Cell cell1, Cell cell2) {
	      return cell1.compareTo(cell2);
	    }

	};
}
