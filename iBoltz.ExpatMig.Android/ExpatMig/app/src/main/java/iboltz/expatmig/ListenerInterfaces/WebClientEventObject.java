package iboltz.expatmig.ListenerInterfaces;

import java.io.InputStream;
import java.util.EventObject;

/**
 * Created by PonSaravanan on 12/18/2015.
 */
public class WebClientEventObject extends EventObject {
    public String ResponseData;
    public InputStream ResponseStream ;

    public WebClientEventObject(Object source) {
        super(source);
    }
}
