import java.util.Scanner;

public class Game {
    enum SquareType {
        EMPTY,
        X,
        O
    }

    static class Pair {
        int first, second;
        
        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    static final int size = 3;
    static boolean quit = false;
    static SquareType turn = SquareType.X;
    static int turnCount = 0;

    static SquareType[][] board;
    static Pair selected;

    static Scanner in;

    static {
        board = new SquareType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = SquareType.EMPTY;
            }
        }

        selected = new Pair(0, 0);
        in = new Scanner(System.in);
    }

    static void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String sqStr = board[i][j] == SquareType.EMPTY ? "_" : board[i][j].toString();
                if (i == selected.first && j == selected.second) {
                    sqStr = String.format("[%s]", sqStr);
                } else {
                    sqStr = String.format(" %s ", sqStr);
                }
                System.out.print(sqStr);
            }
            System.out.print("\n\n");
        }
    }
    
    static int clampi(int i, int min, int max) {
        return Math.min(Math.max(i, min), max);
    }

    static boolean checkWinRow(int row, SquareType player) {
        for (int j = 0; j < size; j++) {
            if (board[row][j] != player) {
                return false;
            }
        }
        return true;
    }

    static boolean checkWinCol(int col, SquareType player) {
        for (int i = 0; i < size; i++) {
            if (board[i][col] != player) {
                return false;
            }
        }
        return true;
    }

    static boolean checkWinDiag1(SquareType player) {
        for (int i = 0; i < size; i++) {
            if (board[i][i] != player) {
                return false;
            }
        }
        return true;
    }

    static boolean checkWinDiag2(SquareType player) {
        for (int i = 0; i < size; i++) {
            if (board[size - 1 - i][i] != player) {
                return false;
            }
        }
        return true;
    }

    static boolean checkWin(SquareType player) {
        for (int i = 0; i < size; i++) {
            if (checkWinRow(i, player) || checkWinCol(i, player)) {
                return true;
            }
        }
        return checkWinDiag1(player) || checkWinDiag2(player);
    }

    static void handleWin() {
        clearScreen();
        printBoard();
        System.out.printf("%s won!\n", turn);
        quit = true;
    }
    
    static void handleTie() {
        clearScreen();
        printBoard();
        System.out.println("Tie!");
        quit = true;
    }

    static void handleMove() {
        if (board[selected.first][selected.second] == SquareType.EMPTY) {
            board[selected.first][selected.second] = turn;
            turnCount++;

            if (checkWin(turn)) {
                handleWin();
                return;
            } else if (turnCount == 9) {
                handleTie();
                return;
            }

            turn = turn == SquareType.X ? SquareType.O : SquareType.X;
        }
    }

    static void processInput(char command) {
        switch (command) {
            case 'w': selected.first = clampi(selected.first - 1, 0, size - 1); break;
            case 'a': selected.second = clampi(selected.second - 1, 0, size - 1); break;
            case 's': selected.first = clampi(selected.first + 1, 0, size - 1); break;
            case 'd': selected.second = clampi(selected.second + 1, 0, size - 1); break;
            case 'q': quit = true; break;
            case 'c': handleMove(); break;
            default: {}
        }
    }

    public static void main(String[] args) {
        while (!quit) {
            clearScreen();
            printBoard();
            System.out.print("> ");

            String command = in.nextLine().strip();
            if (command.length() != 1) {
                continue;
            }
            processInput(command.charAt(0));

        }
    }

    static void clearScreen() {
        try {
            String os = System.getProperty("os.name");

            ProcessBuilder pb;
            if (os.contains("Windows")) {
                pb = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                pb = new ProcessBuilder("clear");
            }

            Process process = pb.inheritIO().start();
            process.waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
