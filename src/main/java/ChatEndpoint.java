import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint(value = "/room/{roomID}")
public class ChatEndpoint {
    private Logger log = Logger.getLogger(ChatEndpoint.class.getSimpleName());

    private RoomList roomList = RoomList.getRoomList();
    private Room room;
    private volatile String roomID;

    @OnOpen
    public void init(@PathParam("roomID") String roomID, Session session) {
        this.roomID = roomID;
        room = roomList.getRoom(roomID);
    }

    @OnMessage
    public void onMessage(final Session session, final String json) {
        ObjectMapper mapper = new ObjectMapper();
        Message message = null;
        try {
            message = mapper.readValue(json, Message.class);
        } catch (IOException e) {
            String error = "Invalid Message Format";
            System.out.println(error);
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, error));
            } catch (IOException ex) {
                log.severe(ex.getMessage());
            }
        }

        Map<String, Object> properties = session.getUserProperties();
        if (message != null) {
            if (message.getMessageType() == Message.MessageType.LOGIN) {
                String userID = message.getMessage();
                properties.put("userID", userID);
                room.join(session);
                room.sendMessage(userID + " has joined.");
            } else {
                String userID = (String) properties.get("userID");
                room.sendMessage(userID + ": " + message.getMessage());
            }
        } else {
            log.info("Error: No message received.");
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        if (room.leave(session)) {
            roomList.removeRoom(roomID);
            room = null;
        } else {
            room.sendMessage(session.getUserProperties().get("userID") + " has disconnected.");
        }
    }

    @OnError
    public void onError(Session session, Throwable ex) {
        log.severe(ex.getMessage());
    }
}