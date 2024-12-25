package com.andrewmadigan.sudoku;

public class SimpleSolver implements Solver {

  @Override
  public void solve(Sudoku board) {
    for (Cell cell : board) {
      if (cell.size() == 1) {
        cell.setValue(cell.getNote());
      }
    }
  }
}
