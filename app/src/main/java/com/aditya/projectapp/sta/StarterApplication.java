/**
 * Created by Aditya on 9/20/2020.
 */

package com.aditya.projectapp.sta;

import android.app.Application;
import android.util.Log;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class StarterApplication extends Application {

//  Right now I couldn't handle parse database so it would be best fo r sta if I disable it's installation - 29/9/2020
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);
//
//
//
        /*
         Back4app:
        * applicationId - GYLE5mr04SFHcmRpR1HAu6afiTOce389D5x2SKsT
        * server - https://parseapi.back4app.com/
        * clientKey - eeTX4IQ5cRMpLQznCKn9ZyxTum0ezGag2WNS7UkR
        * */

        /*
         AWS:
        * applicationId - myappID
        * server - http://65.0.31.36/parse/
        * masterkey - l6PM556wAqct
        * */
//
//        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
//                .applicationId("myappID")
//                .clientKey("l6PM556wAqct")
//                .server("http://65.0.31.36/parse/")
//                .build()
//        );
//
//       /*
//       Parse Test - success
//       ParseObject object = new ParseObject("ExampleObject");
//        object.put("myNumber", "123");
//        object.put("myString", "Aditya");
//
//        object.saveInBackground(new SaveCallback () {
//            @Override
//            public void done(ParseException ex) {
//                if (ex == null) {
//                    Log.i("Parse Result", "Successful!");
//                } else {
//                    Log.i("Parse Result", "Failed" + ex.toString());
//                }
//            }
//        });
//        */
//
//        //ParseUser.enableAutomaticUser();
//
//        ParseACL defaultACL = new ParseACL();
//        defaultACL.setPublicReadAccess(true);
//        defaultACL.setPublicWriteAccess(true);
//        ParseACL.setDefaultACL(defaultACL, true);
//
//    }
}
