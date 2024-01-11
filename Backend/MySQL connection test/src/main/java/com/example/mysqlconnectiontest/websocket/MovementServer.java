package com.example.mysqlconnectiontest.websocket;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import com.example.mysqlconnectiontest.Wizard.Wizard;
import com.example.mysqlconnectiontest.Wizard.WizardRepository;

@ServerEndpoint("/player/{username}/{x}/{y}/{id}")
@Component
public class MovementServer {
    // Store all socket session and their corresponding username
    // Two maps for the ease of retrieval by key
    public static ConcurrentHashMap< Session, String > sessionUsernameMap = new ConcurrentHashMap< >();
    public static ConcurrentHashMap < String, Session > usernameSessionMap = new ConcurrentHashMap < > ();
    public static ConcurrentHashMap<String,Player> players = new ConcurrentHashMap< >();

    private static WizardRepository wizardRepository;
    @Autowired
    public void setWizardRepository(WizardRepository repo) {
        MovementServer.wizardRepository = repo;
    }

    // server side logger
    private final Logger logger = LoggerFactory.getLogger(MovementServer.class);

    /**
     * This method is called when a new WebSocket connection is established.
     *
     * @param session represents the WebSocket session for the connected user.
     * @param username username specified in path parameter.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username , @PathParam("x") float x,@PathParam("y") float y,
        @PathParam("id") int id) throws IOException {
        // server side log

        // Handle the case of a duplicate username
        if (usernameSessionMap.containsKey(username)) {
            session.getBasicRemote().sendText("Username already exists");
            session.close();
        }
        else {
            // map current session with username
            sessionUsernameMap.put(session, username);

            // map current username with session
            usernameSessionMap.put(username, session);


            String extension = wizardRepository.findById(id).getExtension();
            Element element = wizardRepository.findById(id).getElement();
            setWizardRepository(wizardRepository);
            players.put(username,new Player(x,y,username,id, extension,element,0, 0));

            // send to the user joining in
            //sendMessageToPArticularUser(username, "Welcome to the chat server, "+username);


            ObjectMapper om = new ObjectMapper();
            String json = "";
            try{
                json = om.writeValueAsString(players.get(username));
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }
            // send to everyone in the chat
            broadcast("Joined"+json);
            logger.info("[onOpen] " + "Joined"+json);


        }
    }
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        if(message.startsWith("~kill")){
            String admin = sessionUsernameMap.get(session);
            String[] split_msg =  message.split(" ");
            String user_name = split_msg[1];
            logger.info(user_name);
            Session sess = usernameSessionMap.get(user_name);
            logger.info(sess.toString());
            sendMessageToPArticularUser(user_name, "!%SHUTDOWN0987654321%!");
            onClose(sess);
            return;

        }
        // get the username by session
        String username = sessionUsernameMap.get(session);

        Player player = players.get(username);

         String[] xy = message.split(" ");
         player.setX(Float.parseFloat(xy[0]));
         player.setY(Float.parseFloat(xy[1]));
         player.setRoomID(Integer.parseInt(xy[2]));

        // server side log

        // // Direct message to backend using the format "~kill <username>"

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {

            // split by space
            String[] split_msg =  message.split("\\s+");

            // Combine the rest of message
            StringBuilder actualMessageBuilder = new StringBuilder();
            for (int i = 1; i < split_msg.length; i++) {
                actualMessageBuilder.append(split_msg[i]).append(" ");
            }
            String destUserName = split_msg[0].substring(1);    //@username and get rid of @
            String actualMessage = actualMessageBuilder.toString();
            sendMessageToPArticularUser(destUserName, "[DM from " + username + "]: " + actualMessage);
            sendMessageToPArticularUser(username, "[DM from " + username + "]: " + actualMessage);
        }
        else { // Message to whole chat
            ObjectMapper om = new ObjectMapper();
            String json = "";
            try{
                json = om.writeValueAsString(players);
            }catch (JsonProcessingException e){
                e.printStackTrace();

            }
            broadcast(json);
            logger.info("[onMessage] " + json);
        }
    }
    /**
     * Handles the closure of a WebSocket connection.
     *
     * @param session The WebSocket session that is being closed.
     */

    @OnClose
    public void onClose(Session session) throws IOException {
        if (session != null && sessionUsernameMap.containsKey(session)) {
            // get the username from session-username mapping
            String username = sessionUsernameMap.get(session);

            // send the message to chat
            broadcast("[onClose] " + username);

            // server side log
            logger.info("[onClose] " + username);

            // remove user from memory mappings
            sessionUsernameMap.remove(session);
            usernameSessionMap.remove(username);
            players.remove(username);
        }
    }



    /**
     * Handles WebSocket errors that occur during the connection.
     *
     * @param session   The WebSocket session where the error occurred.
     * @param throwable The Throwable representing the error condition.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info(throwable.getMessage());
        throwable.printStackTrace();
        // get the username from session-username mapping
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        players.remove(username);
        if (username != null) {
            players.remove(username);
        }

        // do error handling here

        logger.info("[onError]" + username + ": " + throwable.getMessage());
    }

    /**
     * Sends a message to a specific user in the chat (DM).
     *
     * @param username The username of the recipient.
     * @param message  The message to be sent.
     */
    private synchronized void sendMessageToPArticularUser(String username, String message) {
        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.info("[DM Exception] " + e.getMessage());
        }
    }


    /**
     * Broadcasts a message to all users in the chat.
     *
     * @param message The message to be broadcasted to all users.
     */
    private synchronized void broadcast(String message) {
        sessionUsernameMap.entrySet().removeIf(entry -> {
            Session session = entry.getKey();
            synchronized (session) {
                try {
                    if (session.isOpen()) {
                        try {
                            session.getBasicRemote().sendText(message);
                        } catch (IllegalStateException e) {
                            logger.info("[Broadcast Exception] Session was closed while sending message: " + e.getMessage());
                        }
                        return false; // Keep this entry in the map
                    } else {
                        return true; // Remove this entry from the map
                    }
                } catch (IOException e) {
                    logger.info("[Broadcast Exception] " + e.getMessage());
                    return true; // Remove this entry from the map
                }
            }
        });
    }






}
