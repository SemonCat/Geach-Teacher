package com.semoncat.geach.teacher.bus;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.alljoyn.bus.SignalEmitter;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusSignal;
import org.alljoyn.bus.annotation.BusSignalHandler;

/**
 * Created by SemonCat on 2014/8/11.
 */
public class AlljoynService {
    static {
        System.loadLibrary("alljoyn_java");
    }


    private static final String TAG = AlljoynService.class.getName();
    private static final int RECEIVE_MESSAGE = 0x84331;

    public interface AlljoynEventListener {
        void OnReceive(String message);

        void OnError(Exception e);
    }

    @BusInterface(name = "com.semoncat.geach.teacher.bus.alljoynservice.post")
    interface PostInterface {

        @BusSignal
        public void Post(String name) throws BusException;

    }

    class PostService implements BusObject, PostInterface {

        @BusSignalHandler(iface = "com.semoncat.geach.teacher.bus.alljoynservice.post", signal = "Post")
        public void Receive(String message) {
            Log.d(TAG, "Receive:" + message);
            Message msg = handler.obtainMessage(RECEIVE_MESSAGE);
            msg.obj = message;
            handler.sendMessage(msg);

        }


        public void Post(final String message) throws BusException {
        }
    }

    private BusAttachment bus;

    private Handler handler;

    /* The AllJoyn SignalEmitter used to emit sessionless signals */
    private SignalEmitter emitter;

    private PostService postService;
    private PostInterface postInterface;

    private AlljoynEventListener alljoynEventListener;

    private Context context;

    public AlljoynService(Context context) {
        this.context = context;
        handler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case RECEIVE_MESSAGE:
                        String message = msg.obj.toString();
                        if (alljoynEventListener != null) {
                            alljoynEventListener.OnReceive(message);
                        }
                        break;
                }
            }
        };
    }

    public void connect() {
        org.alljoyn.bus.alljoyn.DaemonInit.PrepareDaemon(context.getApplicationContext());
        bus = new BusAttachment(context.getPackageName(), BusAttachment.RemoteMessage.Receive);
        postService = new PostService();
        bus.registerBusObject(postService, "/PostService");

        bus.connect();

        bus.registerSignalHandlers(postService);
        bus.addMatch("sessionless='t'");

        emitter = new SignalEmitter(postService, 0, SignalEmitter.GlobalBroadcast.Off);
        emitter.setSessionlessFlag(true);
        postInterface = emitter.getInterface(PostInterface.class);


    }

    public void disconnect() {
        bus.unregisterBusObject(postService);
        bus.unregisterSignalHandlers(postService);
        bus.disconnect();
    }

    public void post(String message) {
        try {
            postInterface.Post(message);
        } catch (final BusException e) {
            e.printStackTrace();
            if (alljoynEventListener != null) {
                alljoynEventListener.OnError(e);
            }
        }
    }

    public void setAlljoynEventListener(AlljoynEventListener alljoynEventListener) {
        this.alljoynEventListener = alljoynEventListener;
    }
}
