package prj3.cs496.client;

import com.strongloop.android.loopback.ModelRepository;

/**
 * Created by q on 2016-07-13.
 */
public class MessageRepository extends ModelRepository<Message>{
   public MessageRepository() {super("message", Message.class);}

}
