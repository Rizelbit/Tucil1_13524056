package tucil1;

import javafx.application.Platform;
import java.util.function.Consumer;

public class Solver {
    long countCase = 0;
    long updateThreshold;
    Consumer<Board> onUpdate;

    public Solver(Consumer<Board> onUpdate, int n) {
        this.onUpdate = onUpdate;

        if (n <= 5) this.updateThreshold = 100;
        else if (n == 6) this.updateThreshold = 5_000;
        else if (n == 7) this.updateThreshold = 100_000;
        else if (n == 8) this.updateThreshold = 1_000_000;
        else this.updateThreshold = 10_000_000;
    }

    public boolean solve(Board board, int currRow) {
        if (currRow == board.n) {
            countCase++;

            if (countCase % updateThreshold == 0) {
                if (onUpdate != null) {
                    onUpdate.accept(board);

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if (board.isBoardValid()) {
                return true;
            }
            return false;
        }

        for (int col = 0;col < board.n;col++) {
            board.queenPos[currRow] = col;
            boolean isSolutionFound = solve(board, currRow+1);

            if (isSolutionFound) {
                return true;
            }
        }
        return false;
    }
}
