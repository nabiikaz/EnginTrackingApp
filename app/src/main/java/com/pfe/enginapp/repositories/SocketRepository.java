package com.pfe.enginapp.repositories;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.pfe.enginapp.models.Agent;

import java.net.URISyntaxException;

public class SocketRepository {

    private static SocketRepository instance;
    private Socket socket ;
    private static String BASE_URL = "http://192.168.1.4:8000";
    private  String authToken;
    private  Agent agent ;

    public static SocketRepository getInstance(String authToken, Agent agent){
        if(instance == null){
            instance = new SocketRepository(authToken,agent);
        }
        return  instance;
    }

    public static SocketRepository getInstance(){
        return  instance;
    }

    private SocketRepository(String authToken, Agent agent){
        //init the authentication key token of the logged in user (Agent)
        this.authToken = authToken;
        this.agent = agent;


        try {
            IO.Options opts = new IO.Options();
            opts.reconnection = true;

            socket = IO.socket(BASE_URL,opts);
            //create connection

            socket.connect();



            socket.emit("join",agent.getAgent_id());


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public Socket getSocket(){
        return  socket;
    }



    private void initListeners(){

        // emit the event join along side with the nickname

        socket.emit("join","Nickname");
    }



}
