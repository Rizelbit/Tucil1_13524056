package tucil1;

public class Board {

    int n;
    char[][] boardMap;
    int[] queenPos;

    public Board(char[][] inputBoard, int size) {
        this.n = size;
        this.boardMap = inputBoard;
        this.queenPos = new int[n];

        for (int i = 0;i < n;i++){
            this.queenPos[i] = -1;
        }
    }

    public boolean isBoardValid() {
        boolean[] usedCol = new boolean[n];
        boolean[] usedColor = new boolean[256];

        for (int row = 0;row < n;row++) {
            int col = queenPos[row];

            if (usedCol[col]) {
                return false;
            }
            usedCol[col] = true;

            char color = boardMap[row][col];
            if (usedColor[color]) {
                return false;
            }
            usedColor[color] = true;

            if (row > 0) {
                int colQueenTop = queenPos[row-1];
                if (Math.abs(col - colQueenTop) <= 1) {
                    return false;
                }
            }
        }

        return true;
    }
}
