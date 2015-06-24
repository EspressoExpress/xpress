package us.ridiculousbakery.espressoexpress.InProgress.Receiving;

/**
 * Created by bkuo on 6/24/15.
 */
public class Message {
    final static public int SENDER=0;
    final static public int RECEIVER=1;

    public Message(String msg, int t){
        this.message=msg;
        this.message_type=t;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String message;
    public int message_type;
}
