package retrofit.cybersoft.testretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        String url = getIntent().getStringExtra("URL");
        WebView w = (WebView)findViewById(R.id.viewer);
        w.loadUrl(url);
    }
}
