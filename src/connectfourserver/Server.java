/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfourserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author kent
 */
public class Server implements Runnable{
    ServerSocket server = null;
    Thread serverThread = null;
    PlayerThread player1 = null;
    PlayerThread player2 = null;
    BoardSquare[][] board;
    
    public Server(){
        try{
            server = new ServerSocket(1138);
            System.out.println("Server Started");
            start();
        } catch (IOException e){
            System.out.println("Server Start up failed");
        }
    }
    
    public void start(){
        if(serverThread == null){
            serverThread = new Thread(this);
            serverThread.start();
        }
        board = new BoardSquare[7][5];
        System.out.println("board created");
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 5; j++){
                board[i][j] = new BoardSquare(j, i);
            }
        }
    }
    
    @Override
    public void run() {
        Socket client = null;
        System.out.println("waiting for clients...");
        while(player1 == null || player2 == null){
            try{
                
                client = server.accept();
                if (player1 == null){
                    player1 = new PlayerThread(this, client,1);
                    player1.start();
                } else if (player2 == null){
                    player2 = new PlayerThread(this, client,2);
                    player2.start();
                }
            } catch (IOException e){
                System.out.println("Client failed");
            }
        }
        System.out.println("All Players Connected");
        System.out.println("Game in progress...");
        player1.send(".s0");
        
    }
    
    public void handlePlayerMove(String _mes, int _player){
        if (_mes.equals(".c0")){
            for (int i = 4; i > -1; i--){
                if(!board[0][i].isSquareClicked()){
                    board[0][i].setClicked(_player);
                    sendMove(_mes, _player, 0, i);
                    break;
                }
            } 
        }else if (_mes.equals(".c1")){
            for (int i = 4; i > -1; i--){
                if(!board[1][i].isSquareClicked()){
                    board[1][i].setClicked(_player);
                    sendMove(_mes, _player, 1, i);
                    break;
                }
            }
        } else if (_mes.equals(".c2")){
            for (int i = 4; i > -1; i--){
                if(!board[2][i].isSquareClicked()){
                    board[2][i].setClicked(_player);
                    sendMove(_mes, _player, 2, i);
                    break;
                }
            }
        } else if (_mes.equals(".c3")){
            for (int i = 4; i > -1; i--){
                if(!board[3][i].isSquareClicked()){
                    board[3][i].setClicked(_player);
                    sendMove(_mes, _player, 3, i);
                    break;
                }
            }
        } else if (_mes.equals(".c4")){
            for (int i = 4; i > -1; i--){
                if(!board[4][i].isSquareClicked()){
                    board[4][i].setClicked(_player);
                    sendMove(_mes, _player, 4, i);
                    break;
                }
            }
        } else if (_mes.equals(".c5")){
            for (int i = 4; i > -1; i--){
                if(!board[5][i].isSquareClicked()){
                    board[5][i].setClicked(_player);
                    sendMove(_mes, _player, 5, i);
                    break;
                }
            }
        } else if (_mes.equals(".c6")){
            for (int i = 4; i > -1; i--){
                if(!board[6][i].isSquareClicked()){
                    board[6][i].setClicked(_player);
                    sendMove(_mes, _player, 6, i);
                    break;
                }
            }
        } else {
            sendMessage(_mes, _player);
        }
    }
    
    public void sendMessage(String _mes, int _player){
        if (_player == 1){
            player2.send(_mes);
        } else if (_player == 2){
            player1.send(_mes);
        }
    }
    
    public void sendMove(String _mes, int _player, int _col, int _row){
        if (isWinner(_player, _col, _row)){
            if(_player == 1){
                player2.send(_mes+"w");
                player1.send(".w0");
            } else if (_player == 2){
                player1.send(_mes+"w");
                player2.send(".w0");
            }
        } else if (isBoardFull()){
            sendAll(".d0");
        } else if (_player == 1){
            player2.send(_mes);
        } else if(_player == 2){
            player1.send(_mes);
        }
    }
    
    public Boolean isBoardFull(){
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 5; j++){
                if(!board[i][j].isSquareClicked()){
                    return false;
                }
            }
        }
        return true;
    }
    
    public Boolean isWinner(int _player, int _col, int _row){
        if(verticalCheck(_player, _col, _row)){
            return true;
        }
        else if(horizontalCheck(_player, _col, _row)){
            return true;
        }
        else if(leftDiagonalCheck(_player, _col, _row)){
            return true;
        }
        else if(rightDiagonalCheck(_player, _col, _row)){
            return true;
        }
        return false;
    }
    
    public Boolean verticalCheck(int _player, int _col, int _row){
        int player = _player;
        int col = _col;
        int row = _row;
        if (row >= 2){
            return false;
        }
        for (int i = row + 1; i <= row + 3; i++){
            if (board[col][i].getPlayer() != player){
                return false;
            }
        }
        return true;
    }
    
    public Boolean horizontalCheck(int _player, int _col, int _row){
        int player = _player;
        int col = _col;
        int row = _row;
        int count = 1;
        
        for (int i = col-1; i>=0; i--){
            if(board[i][row].getPlayer() != player){
                break;
            }
            count++;
        }
        for (int i = col+1; i<7; i++){
            if(board[i][row].getPlayer() != player){
                break;
            }
            count++;
        }
        if (count >= 4){
            return true;
        } else {
            return false;
        }
    }
    
    public Boolean leftDiagonalCheck(int _player, int _col, int _row){
         int player = _player;
        int col = _col-1;
        int row = _row-1;
        int count = 1;
        while(col >= 0 && row >=0){
            if(board[col][row].getPlayer() == player){
                count++;
                col--;
                row--;
            } else {
                break;
            }
        }
        
        col = _col+1;
        row = _row+1;
        while(col < 7 && row < 5){
            if(board[col][row].getPlayer() == player){
                count++;
                col++;
                row++;
            } else {
                break;
            }
        }
        if (count >= 4){
            return true;
        } else {
            return false;
        }
    }
    
    public Boolean rightDiagonalCheck(int _player, int _col, int _row){
         int player = _player;
        int col = _col-1;
        int row = _row+1;
        int count = 1;
        while(col >= 0 && row < 5){
            if(board[col][row].getPlayer() == player){
                count++;
                col--;
                row++;
            } else {
                break;
            }
        }
        
        col = _col+1;
        row = _row-1;
        while(col < 7 && row >= 0){
            if(board[col][row].getPlayer() == player){
                count++;
                col++;
                row--;
            } else {
                break;
            }
        }
        if (count >= 4){
            return true;
        } else {
            return false;
        }
    }
    
    public void sendAll(String mes){
        player1.send(mes);
        player2.send(mes);
    }
    
    public void endgame (){
        sendAll(".k0");
        player1.kill();
        player2.kill();
    }
    
}
