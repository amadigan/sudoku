package com.andrewmadigan.sudoku.lambda;

import java.util.Base64;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse.APIGatewayV2HTTPResponseBuilder;
import com.andrewmadigan.sudoku.SimpleSolver;
import com.andrewmadigan.sudoku.Solver;
import com.andrewmadigan.sudoku.Sudoku;

public class SudokuLambda implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>, Resource {
  private static final List<Solver> SOLVERS = List.of(new SimpleSolver());

  public SudokuLambda() {
    Core.getGlobalContext().register(this);
  }

  @Override
  public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event,
      com.amazonaws.services.lambda.runtime.Context context) {

    APIGatewayV2HTTPResponseBuilder responseBuilder = APIGatewayV2HTTPResponse.builder();

    String board = StringUtils.substringAfter(event.getRawPath(), "/");

    if (StringUtils.isBlank(board)) {
      if (event.getIsBase64Encoded()) {
        board = new String(Base64.getDecoder().decode(event.getBody()));
      } else {
        board = event.getBody();
      }
    }

    try {
      Sudoku sudoku = new Sudoku(board);

      for (long v = sudoku.getVersion(); !sudoku.isSolved(); v = sudoku.getVersion()) {
        for (Solver solver : SOLVERS) {
          solver.solve(sudoku);
        }

        if (v == sudoku.getVersion()) {
          break;
        }
      }

      responseBuilder.withStatusCode(200).withBody(sudoku.toString());
    } catch (Exception e) {
      responseBuilder.withStatusCode(400).withBody(e.getMessage());
    }

    return responseBuilder.build();
  }

  @Override
  public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
  }

  @Override
  public void afterRestore(Context<? extends Resource> context) throws Exception {
  }
}
