/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfourserver;

/**
 *
 * @author kent
 */
public class BoardSquare {
    int row = 0;
    int column = 0;
    int player = 0;
    Boolean isClicked = false;

    public BoardSquare(int r, int c){
        row = r;
        column = c;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
    
    public void setClicked(int _player){
        isClicked = true;
        player = _player;
    }

    public Boolean isSquareClicked(){
        return isClicked;
    }
    
    public int getPlayer(){
        return player;
    }
}
