package org.pentode.boost;

import android.app.Application;

import com.bugsense.trace.BugSenseHandler;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(
    formKey = "", // This is required for backward compatibility but not used
    formUri = "http://www.bugsense.com/api/acra?api_key=a0191cc8"
)
public class MyAppACRA extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        
        //BugSenseHandler.initAndStartSession(MyAppACRA.this, "a0191cc8");
    }
    
}