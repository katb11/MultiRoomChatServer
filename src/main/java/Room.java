import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

// Class for holding a single room websocket session
public class Room {

    private List<Session> sessions = new ArrayList<>();

    public Room() { }

    public synchronized void join(Session session) {
        sessions.add(session);
    }

    public synchronized boolean leave(Session session) {
        sessions.remove(session);
        return sessions.isEmpty();
    }

    public synchronized void sendMessage(String message) {
        for (Session session: sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}