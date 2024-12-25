package com.andrewmadigan.sudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class CellTest {

  @Test
  public void testSetValue() {
    AtomicInteger updateFnCalled = new AtomicInteger(0);

    Cell cell = new Cell(0, 0, c -> updateFnCalled.incrementAndGet());

    assertEquals(0, updateFnCalled.get());
    assertEquals(0, cell.getValue());
    assertEquals(9, cell.size());

    cell.setValue((byte) 1);

    assertEquals(1, updateFnCalled.get());
    assertEquals(1, cell.getValue());
    assertEquals(0, cell.size());

    cell.setValue((byte) 1);

    assertEquals(1, updateFnCalled.get());
    assertEquals(1, cell.getValue());
    assertEquals(0, cell.size());

    assertThrows(IllegalStateException.class, () -> cell.setValue((byte) 2));

    assertEquals(1, updateFnCalled.get());
    assertEquals(1, cell.getValue());
    assertEquals(0, cell.size());
  }

  @Test
  public void testGetNote() {
    Cell cell = new Cell(0, 0, c -> {
    });

    assertEquals(9, cell.size());
    assertContains(cell, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 1);
    assertEquals(8, cell.size());
    assertContains(cell, 2, 3, 4, 5, 6, 7, 8, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 2);
    assertEquals(7, cell.size());
    assertContains(cell, 3, 4, 5, 6, 7, 8, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 3);
    assertEquals(6, cell.size());
    assertContains(cell, 4, 5, 6, 7, 8, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 5);
    assertEquals(5, cell.size());
    assertContains(cell, 4, 6, 7, 8, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 6);
    assertEquals(4, cell.size());
    assertContains(cell, 4, 7, 8, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 7);
    assertEquals(3, cell.size());
    assertContains(cell, 4, 8, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 8);
    assertEquals(2, cell.size());
    assertContains(cell, 4, 9);
    assertEquals(0, cell.getNote());

    cell.remove((byte) 9);
    assertEquals(1, cell.size());
    assertContains(cell, 4);
    assertEquals(4, cell.getNote());

    cell.setValue((byte) 4);
    assertEquals(0, cell.size());
    assertEquals(0, cell.getNote());
  }

  @Test
  public void testRowCol() {
    Cell cell = new Cell(4, 3, c -> {
    });

    assertEquals(4, cell.getRow());
    assertEquals(3, cell.getCol());
  }

  private static void assertContains(Cell cell, int... values) {
    for (int value : values) {
      assertEquals(true, cell.contains((byte) value));
    }
  }
}
