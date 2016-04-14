/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectfourserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kent
 */
public class PlayerThread extends Thread{
    
    private Socket player = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private int playerNum = 0;
    private Server main;
    private Boolean running = true;
    
    public PlayerThread(Server _main, Socket _player, int _playerNum){
        player = _player;
        playerNum = _playerNum;
        main = _main;
    }
    
    @Override
    public void run(){
        try{
            out = new DataOutputStream(player.getOutputStream());
            in = new DataInputStream(player.getInputStream());
        } catch (IOException e){
            System.out.println("Player "+playerNum+" streams failed");
        }
        if(playerNum == 1){
            send(".p1");
        } else if(playerNum == 2){
            send(".p2");
        }
        while(running){
            try {
                String n = in.readUTF();
                main.handlePlayerMove(n, playerNum);
            } catch (IOException ex) {
                System.out.println("Player " + playerNum + " has disconnected.");
                main.endgame();
            }
        }
        
    }
    
    public void send(String code){
        try {
            out.writeUTF(code);
            out.flush();
        } catch (IOException e){
            System.out.println(e);
            stop();
        }
    }
    
    public void kill(){
        running = false;
    }
}
