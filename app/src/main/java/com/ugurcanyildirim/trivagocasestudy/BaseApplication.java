package com.ugurcanyildirim.trivagocasestudy;

import android.app.Application;

import com.ugurcanyildirim.trivagocasestudy.service.ServiceManager;

/**
 * Created by ugurc on 12.08.2016.
 */
public class BaseApplication extends Application {

    private static ServiceManager serviceManager = null;

    public static ServiceManager getService(){
        if(serviceManager == null){
            serviceManager = new ServiceManager();
        }
        return serviceManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
