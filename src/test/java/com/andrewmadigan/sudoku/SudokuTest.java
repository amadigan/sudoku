package com.andrewmadigan.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SudokuTest {
  private static byte[][] partialBoard = new byte[][] {
      { 0, 0, 0, 9, 0, 0, 0, 8, 2 },
      { 0, 2, 7, 0, 4, 6, 0, 0, 9 },
      { 5, 1, 0, 8, 0, 3, 0, 0, 0 },
      { 3, 8, 0, 0, 0, 2, 9, 5, 1 },
      { 0, 5, 0, 0, 8, 0, 3, 7, 6 },
      { 1, 0, 0, 5, 3, 0, 0, 2, 0 },
      { 0, 0, 8, 0, 0, 0, 0, 0, 0 },
      { 7, 3, 1, 0, 0, 0, 8, 9, 0 },
      { 2, 0, 5, 0, 9, 0, 0, 1, 7 }
  };

  @Test
  public void testGetRow() {
    Sudoku sudoku = new Sudoku(partialBoard);
    assertCellValues(sudoku.getRow(0), 0, 0, 0, 9, 0, 0, 0, 8, 2);
  }

  @Test
  public void testGetCol() {
    Sudoku sudoku = new Sudoku(partialBoard);
    assertCellValues(sudoku.getCol(0), 0, 0, 5, 3, 0, 1, 0, 7, 2);
  }

  @Test
  public void testGetSquare() {
    Sudoku sudoku = new Sudoku(partialBoard);
    assertCellValues(sudoku.getSquare(0), 0, 0, 0, 0, 2, 7, 5, 1, 0);
  }

  @Test
  public void testPartialBoard() {
    Sudoku sudoku = new Sudoku(partialBoard);

    assertEquals(1, sudoku.getVersion());
    assertFalse(sudoku.isSolved());
    assertTrue(sudoku.validate().isEmpty(), () -> sudoku.validate().toString());
  }

  private static void assertCellValues(Cell[] cells, int... values) {
    assertEquals(cells.length, values.length);
    for (int i = 0; i < cells.length; i++) {
      assertEquals(values[i], cells[i].getValue());
    }
  }

}
