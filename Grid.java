
package SudokuSolver;
import java.util.ArrayList;

/**
 * A class representing a sudoku 9 x 9 grid with 9 rows and 9 columns,
 * possible cell values 1 through 9, and 9 blocks with 9 cells each.
 * A Grid knows how to 'solve itself' and how to check whether it is a valid
 * complete (solved) sudoku grid.
 * @author Tze-Hei "Zee" Yong
 */
public class Grid
{
    private final Cell[][] grid; // 2-dimensional array to store grid of all Cells
    private final ArrayList<Cell> blankCellList; // stores initial list of blank cells
    
    /**
     * Constructor takes a 2-dimensional integer array representing the sudoku
     * grid's initial values. Blank cells should be represented by 0 values.
     * @param newGrid  The 9 x 9 integer array containing the grid's initial values.
     */
    public Grid(int[][] newGrid)
    {
        grid = new Cell[9][9];
        if (!isGridSizeValid(newGrid))
        {
            throw new IllegalArgumentException("Grid with invalid dimensions provided to constructor");
        }
        
        // Initialize new grid of Cells from grid of integers
        for (int i = 0; i < 9; i++)  // iterate through rows
        {
            for (int j = 0; j < 9; j++) // iterate through each column in a row
            {
                boolean hasValue = false;
                int cellValue = newGrid[i][j];
                if (cellValue != 0)
                    hasValue = true;  // indicates non-blank cell in initial grid
                    
                // Determine block for the current cell row and column
                int block = 0;
                if (i <= 2)  // blocks 1, 2, or 3
                {
                    if (j <= 2)
                        block = 1;
                    else if (j >= 3 && j <= 5)
                        block = 2;
                    else // j >= 6
                        block = 3;
                }
                else if (i >= 3 && i <= 5)  // blocks 4, 5, or 6
                {
                    if (j <= 2)
                        block = 4;
                    else if (j >= 3 && j <= 5)
                        block = 5;
                    else // j >= 6
                        block = 6;                
                } 
                else // i >= 6; blocks 7, 8, or 9
                {
                    if (j <= 2)
                        block = 7;
                    else if (j >= 3 && j <= 5)
                        block = 8;
                    else // j >= 6
                        block = 9;                              
                }
                    
                // Construct new Cell object with proper values and place in Cell grid
                Cell newCell = new Cell(cellValue, hasValue, i, j, block);
                grid[i][j] = newCell;
            } // end inner for               
        } // end outer for
   
        // Initialize blankCellList
        blankCellList = new ArrayList<>();
        setBlankCellList();
    } // end constructor
    
    // Private method to check that grid supplied as argument to constructor 
    // has the correct number of rows and columns
    private boolean isGridSizeValid(int[][] newGrid)
    {
        boolean result = true;
        
        if (newGrid.length != 9)  // check number of rows
        {
            result = false;
        }
        else  // assert: newGrid.length == 9  (there are 9 rows)
        {
            // Check length of each row
            for (int i = 0; i < 9; i++)
            {
                if (newGrid[i].length != 9)
                {
                    result = false;
                }  // end if
            } // end for
        } // end else

        return result;
    } // end isGridSizeValid

    
    // Private method to create list of blank cells from initial grid
    private void setBlankCellList()
    {
        // Check each cell in grid in turn and store blank cells (cells with
        // value = 0) in blankCellList
        for (int i = 0; i < 9; i++)  // loop through each row
        {
            for (int j = 0; j < 9; j++)  // loop through each element in a row
            {
                if (grid[i][j].getValue() == 0)
                {
                    blankCellList.add(grid[i][j]);
                }
            } // end inner for
        } // end outer for            
    }  // end setBlankCellList
    
    /**
     * Returns the initial number of blank cells in the grid to be solved.
     * @return  the integer number of initial blank cells
     */
    public int getNumberOfBlankCells()
    {
        return blankCellList.size();
    } // end getNumberOfBlankCells
    
    
    /**
     *  Displays the values of all cells in the grid in a 9 x 9 format.
     */
    public void displayGrid()
    {
        // Display the grid cell values
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                System.out.print(grid[i][j].getValue() + " ");
            } // end inner for
            System.out.println();
        } // end outer for        

        // *** For development only; delete in final version *** //
        // Display the row and column values for every cell
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                System.out.print("(" + grid[i][j].getRow() + ","
                                     + grid[i][j].getColumn() + ") ");
            } // end inner for
            System.out.println();
        } // end outer for           

        // *** For development only; delete in final version *** //
        // Display the 'fixed' state and block value for every cell
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                System.out.print("(" + grid[i][j].getFixed() + ","
                                     + grid[i][j].getBlock() + ") ");
            } // end inner for
            System.out.println();
        } // end outer for              
           
    } // end displayGrid

    
    /**
     * Attempts to solve the grid based on the initial values provided.
     * @return  True if the puzzle is successfully solved; false otherwise
     *          (i.e., if puzzle cannot be solved based on initial values provided)
     */
    public boolean solveGrid()
    {
        boolean solved;
        
        // Declare variables
        Cell currentBlankCell;
        int cellValue;
        int listIndex;
        int testValue;
        
        // Start at first Cell in blankCellList
        listIndex = 0;
       
        // Iterate through all cells in blankCellList and set their values
        // If the initial grid has no blank cells, this while loop is skipped.
        while ( listIndex >= 0  &&  listIndex < blankCellList.size() )
        {           
            // Retrieve information for current blank cell
            currentBlankCell = blankCellList.get(listIndex);
            cellValue = currentBlankCell.getValue();
            
            // If current cell is blank (value = 0), testValue is set = 1;
            // If current cell is not blank, we have returned to it because
            // no valid values were found for a later blank cell.  
            // In this case, testValue is set to the cell's current value + 1.
            testValue = cellValue + 1;            

            // Attempt to find a valid value for the current cell, 
            // starting with testValue.
            boolean valueSet = false;  // flag used in do-while loop
            do
            {
                currentBlankCell.setValue(testValue);
                
                // The isValidCell method checks if currentBlankCell's value 
                // follows the sudoku rules for row, column, and block.
                if (isValidCell(currentBlankCell))
                {
                    valueSet = true;
                }
                else  // value conflicts with another cell
                {
                    testValue++;  // increment to check next possible value
                }

            } while (valueSet == false  &&  testValue <= 9);
        
            // do-while loop is exited when either a valid value is found, or all
            // possible values (1 to 9) are checked without finding a valid value
            if (testValue == 10)  // all values checked without finding a valid value
            {
                // Set current cell value to 0 and go back to previous blank cell
                currentBlankCell.setValue(0);
                listIndex--;
            }
            else  // a valid value was found and set for the current cell
            {
                listIndex++;  // increment to move to next blank cell in list
            }
 
        } // end outer while (exited if listIndex < 0 or listIndex = blankCellList.size())
        
        if (listIndex < 0  || !isValidGrid())
        {
            solved = false;
        }
        else  // valid values were set for all cells in blankCellList
        {     // and the grid as a whole is valid
            solved = true;
        }  
        return solved;
    } // end solveGrid
    
    
    // Private method to check whether a cell follows the row, column,
    // and block rules for its cell value.
    private boolean isValidCell(Cell testCell)
    {
        boolean result = true;
        int cellValue = testCell.getValue();
        int cellRow = testCell.getRow();
        int cellColumn = testCell.getColumn();
        int cellBlock = testCell.getBlock();        
        
        // Check for same value in other cell of the same row
        for (int j = 0; j < 9; j++)
        {
            if ( (j != cellColumn) && (grid[cellRow][j].getValue() == cellValue) )
            {
                result = false;
            } 
        } // end for
        
        // Check for same value in other cell of the same column
        for (int i = 0; i < 9; i++)
        {
            if ( (i != cellRow) && (grid[i][cellColumn].getValue() == cellValue) )
            {
                result = false;
            } 
        } // end for      
        
        // Check for same value in other cell of the same block
        if ( !isValidCellInBlock(cellValue, cellRow, cellColumn, cellBlock) )
        {
            result = false;
        }
        
        return result;
    } // end isValidCell
    
    
    // Private method used by isValidCell to check whether a given cell value
    // is valid within the cell's block. Returns false if another cell in
    // the same block has the same cell value.  
    private boolean isValidCellInBlock(int value, int row, int column, int block)
    {
        boolean result = true;
        
        // Set row and column indexes for upper-left-most cell in the given block
        int topRow;
        int leftColumn;
        switch (block)
        {
            case 1:
                topRow = 0;
                leftColumn = 0;
                break;
            case 2:
                topRow = 0;
                leftColumn = 3;
                break;
            case 3:
                topRow = 0;
                leftColumn = 6;
                break;
            case 4:
                topRow = 3;
                leftColumn = 0;
                break;
            case 5:
                topRow = 3;
                leftColumn = 3;
                break;                
            case 6:
                topRow = 3;
                leftColumn = 6;
                break;                
            case 7:
                topRow = 6;
                leftColumn = 0;
                break;                
            case 8:
                topRow = 6;
                leftColumn = 3;
                break;                
            case 9:
                topRow = 6;
                leftColumn = 6;
                break;                  
            default:
                topRow = -1;      // indicates error
                leftColumn = -1;  // indicates error
        } // end switch
  
        // Starting from upper-left-most cell in block, check all other cells
        // in that block for the given value.     
        for (int i = topRow; i <= topRow + 2; i++)  // iterate through rows
        {
            for (int j = leftColumn; j <= leftColumn + 2; j++) // iterate through columns
            {
                if ( i != row  || j != column) // i.e., not the same cell as the given cell
                {   // check for duplication of the given cell's value
                    if (grid[i][j].getValue() == value)
                    {
                        result = false;
                    }  // end if
                } // end if
            } // end inner for
        } // end outer for
   
        return result;
    } // end isValidCellInBlock       
    
        
    /**
     * Determines whether the values currently set for the grid cells
     * constitute a valid grid according to the rules of sudoku.
     * @return  True if the grid is valid, False otherwise.
     */
    public boolean isValidGrid()
    {
        boolean result = true;  
        
        // Check each cell in grid for a valid value
        for (int row = 0; row < 9; row++)  // iterate through each row
        {
            // Check each cell in a given row
            int column = 0;
            Cell currentCell;
            int cellValue;
            while (result == true  &&  column < 9)
            {
                currentCell = grid[row][column];
                cellValue = currentCell.getValue();

                if (cellValue < 1  ||  cellValue > 9)  // check for valid numeric range
                {
                    result = false;
                }
                else  // check for adherence to row, column, and block rules
                {
                    if (!isValidCell(currentCell))
                    {
                        result = false;
                    }
                }
                
                column++;  // check next cell in row
            } // end while (exits loop if reach end of row, or result == false)
        } // end for
        
        return result;
    } // end isValidGrid
    
   
      
    // Test stub for Grid class
    public static void main(String[] args)
    {
        // Create a 9 x 9 integer grid with some values filled in
        int[][] intGrid = { {2,0,0,0,0,6,7,5,4},
                            {0,0,7,9,0,4,1,8,0},
                            {3,8,4,0,0,7,0,2,0},
                            {5,0,0,0,8,2,0,7,0},
                            {0,3,8,7,0,0,4,0,0},
                            {0,0,9,6,0,0,0,0,5},
                            {0,0,5,3,7,0,0,9,0},
                            {8,7,0,0,6,0,0,0,1},
                            {0,1,3,0,5,8,0,0,0} };
        
        // Create new Grid object, passing the intGrid array to the constructor
        System.out.println("Creating a new grid with some blank values...");
        Grid testGrid = new Grid(intGrid);

        // Check that values were passed successfully
        System.out.println("\nInitial grid: ");
        testGrid.displayGrid();
        
        System.out.println("\nNumber of blank cells: " + testGrid.getNumberOfBlankCells());
 
        // Test solveGrid() method
        System.out.println("Successfully solved? " + testGrid.solveGrid());
        testGrid.displayGrid();

 
        // Directly test public isValidGrid() method with a complete inputted grid //
        // Create a 9 x 9 integer grid with all values filled in
        int[][] intGrid2 = { {2,9,1,8,3,6,7,5,4},
                             {6,5,7,9,2,4,1,8,3},
                             {3,8,4,5,1,7,9,2,6},
                             {5,4,6,1,8,2,3,7,9},
                             {1,3,8,7,9,5,4,6,2},
                             {7,2,9,6,4,3,8,1,5},
                             {4,6,5,3,7,1,2,9,8},
                             {8,7,2,4,6,9,5,3,1},
                             {9,1,3,2,5,8,6,4,7} };
        
        // Create new Grid object, passing the intGrid2 array to the constructor
        System.out.println("Creating a new complete grid with all values filled in...");
        Grid testGrid2 = new Grid(intGrid2);

        // Check that values were passed successfully
        System.out.println("\nInputted grid: ");
        testGrid2.displayGrid();       
        System.out.println("\nNumber of blank cells: " + testGrid2.getNumberOfBlankCells());
        
        // Check validity of grid using isValidGrid()
        System.out.println("\nIs inputted grid valid? " + testGrid2.isValidGrid());
        

        // Test isValidGrid() method with a complete but invalid inputted grid //
        // Create a 9 x 9 integer grid with all values filled in
        int[][] intGrid3 = { {9,9,1,8,3,6,7,5,4},
                             {6,5,7,9,2,4,1,8,3},
                             {3,8,4,5,1,7,9,2,6},
                             {5,4,6,1,8,2,3,7,9},
                             {1,3,8,7,9,5,4,6,2},
                             {7,2,9,6,4,3,8,1,5},
                             {4,6,5,3,7,1,2,9,8},
                             {8,7,2,4,6,9,5,3,1},
                             {9,1,3,2,5,8,6,4,7} };
        
        // Create new Grid object, passing the intGrid3 array to the constructor
        System.out.println("Creating a new complete grid with all values filled in...");
        Grid testGrid3 = new Grid(intGrid3);

        // Check that values were passed successfully
        System.out.println("\nInputted grid: ");
        testGrid3.displayGrid();       
        System.out.println("\nNumber of blank cells: " + testGrid3.getNumberOfBlankCells());
        
        // Check validity of grid using isValidGrid()
        System.out.println("\nIs inputted grid valid? " + testGrid3.isValidGrid());        
  
        
        // Test solveGrid() method with an incomplete grid that has invalid initial values //
        // Create a 9 x 9 integer grid with some values filled in
        int[][] intGrid4 = { {0,0,0,2,2,6,7,5,4},
                             {0,0,7,9,0,4,1,8,0},
                             {3,8,4,0,0,7,0,2,0},
                             {5,0,0,0,8,2,0,7,0},
                             {0,3,8,7,0,0,4,0,0},
                             {0,0,9,6,0,0,0,0,5},
                             {0,0,5,3,7,0,0,9,0},
                             {8,7,0,0,6,0,0,0,1},
                             {0,1,3,0,5,8,0,0,0} };
        
        // Create new Grid object, passing the intGrid4 array to the constructor
        System.out.println("Creating a new grid with some blank values...");
        Grid testGrid4 = new Grid(intGrid4);

        // Check that values were passed successfully
        System.out.println("\nInitial grid: ");
        testGrid4.displayGrid();
        
        System.out.println("\nNumber of blank cells: " + testGrid4.getNumberOfBlankCells());
 
        // Test solveGrid() method
        System.out.println("Successfully solved? " + testGrid4.solveGrid());      
        
    } // end test main    
      
} // end Grid
