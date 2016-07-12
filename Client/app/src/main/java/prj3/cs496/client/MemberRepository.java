package prj3.cs496.client;

import com.strongloop.android.loopback.UserRepository;

/**
 * Created by q on 2016-07-12.
 */

public class MemberRepository extends UserRepository<Member> {
    public interface LoginCallback extends UserRepository.LoginCallback<Member> {
    }

    public MemberRepository() {
        super("member", null, Member.class);
    }
}
