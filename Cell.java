
package SudokuSolver;

/**
 * A class representing one grid cell in a sudoku puzzle.
 * @author Tze-Hei "Zee" Yong
 */
public class Cell 
{
    private int value;              // can be 0 (blank) to 9
    private final boolean fixed;    // whether value is fixed (set initially)
    private final int row;          // specifies array index for row (0 to 8)
    private final int column;       // specifies array index for column (0 to 8)
    private final int block;        // range: 1 to 9
    
    public Cell(int value, boolean fixed, int row, int column, int block)
    {
        this.value = value;
        this.fixed = fixed;
        this.row = row;
        this.column = column;
        this.block = block;
    } // end constructor
    
    public int getValue()
    {
        return value;
    } // end getValue
    
    public boolean getFixed()
    {
        return fixed;
    } // end getFixed
    
    public int getRow()
    {
        return row;
    } // end getRow
    
    public int getColumn()
    {
        return column;
    } // end getColumn
    
    public int getBlock()
    {
        return block;
    } // end getBlock
    
    public void setValue(int newValue)
    {
        if (fixed)
        {
            throw new UnsupportedOperationException("Cannot change fixed cell value");
        }   
        value = newValue;    
    } // end setValue
    
    @Override public String toString()
    {
        String result;
        result = "value:" + getValue() + " fixed:" + getFixed() + " position:(" +
                 getRow() + "," + getColumn() + ") block:" + getBlock();
        return result;
    } // end toString
    
    
    
    // Test stub for Cell class
    public static void main(String[] args)
    {
        // Create a blank grid with all cells set to zero and all blocks set to 1
        System.out.println("Creating a blank grid (with all cells set to zero) and all blocks set to 1 ...");
        Cell[][] grid = new Cell[9][9];  // 2-dimensional array to store Cells
        for (int i = 0; i < 9; i++)      // i = row index
        {
            for (int j = 0; j < 9; j++)  // j = column index
            {
                grid[i][j] = new Cell(0, false, i, j, 1);
            } // end inner for (loops through each element in a row)
        } // end outer for (loops through rows)

        // Display the grid cell values
        System.out.println("\nValues: ");
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                System.out.print(grid[i][j].getValue() + " ");
            } // end inner for
            System.out.println();
        } // end outer for
        
        // Display the row and column index values for every cell
        System.out.println("\nRow and column index values: ");
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                System.out.print("(" + grid[i][j].getRow() + ","
                                     + grid[i][j].getColumn() + ") ");
            } // end inner for
            System.out.println();
        } // end outer for        

        // Test toString() method
        System.out.println("\nInformation for upper-left corner cell: ");
        System.out.println(grid[0][0]);
        
        // Test setValue method
        System.out.println("\nModifying value for upper-left corner cell to 3 ...");
        grid[0][0].setValue(3);
        System.out.println("New information for upper-left corner cell: ");
        System.out.println(grid[0][0]);
        
    } // end test main

} // end Cell


