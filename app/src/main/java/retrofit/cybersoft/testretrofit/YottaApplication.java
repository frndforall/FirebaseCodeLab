package retrofit.cybersoft.testretrofit;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


public class YottaApplication extends MultiDexApplication {



	@Override
	protected void attachBaseContext(Context base) {
		MultiDex.install(base);
		super.attachBaseContext(base);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
//        Tripod tripod = new Tripod(TRIPOD_KEY);
//        tripod.setup(this,true);
	}
	

}
