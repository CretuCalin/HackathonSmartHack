package com.example.cretucalinn.navigationdrawer;

import android.os.AsyncTask;

import java.io.IOException;

import remote.RemoteProcs;
import rpc.HiRpc;

/**
 * Created by Cretu Calinn on 10/22/2016.
 */

public class NetworkManager {

    public static RemoteProcs remoteProcs;


    public static void connect(final String ip, final String username, final String password) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    remoteProcs = HiRpc.connectSimple(ip, RemoteProcs.PORT, RemoteProcs.class);
                } catch (IOException e) {
                    System.out.println(e);
                }
                return  null;
            }
        }.execute();
    }

    public static RemoteProcs getRemoteProcs() {
        return remoteProcs;
    }
}
