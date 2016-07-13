package prj3.cs496.client;

import com.strongloop.android.loopback.Model;

/**
 * Created by q on 2016-07-13.
 */
public class Content extends Model {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    String type;
    String content;
}
