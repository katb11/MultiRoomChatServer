import java.util.HashMap;

// Singleton class holding all the rooms on the server
public class RoomList {

    private static RoomList instance = null;
    private static HashMap<String, Room> rooms = new HashMap<>();

    public synchronized static RoomList getRoomList() {
        if (instance == null) {
            instance = new RoomList();
        }
        return instance;
    }

    public synchronized Room getRoom(String roomID) {
        if (instance == null) {
            instance = new RoomList();
        }

        if (rooms.containsKey(roomID)) {
            return rooms.get(roomID);
        } else {
            instance.createRoom(roomID);
            return rooms.get(roomID);
        }
    }

    public synchronized void removeRoom(String roomID) {
        rooms.remove(roomID);
    }

    private synchronized void createRoom(String roomID) {
        rooms.put(roomID, new Room());
    }
}