package prj3.cs496.client;

/**
 * Created by q on 2016-07-09.
 */
public class UserRepository extends com.strongloop.android.loopback.UserRepository<User> {
    public interface LoginCallback extends com.strongloop.android.loopback.UserRepository.LoginCallback<User> {

    }

    public UserRepository() {
        super("user", null, User.class);
    }
}
