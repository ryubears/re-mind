package com.yoseph.re_mind.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.yoseph.re_mind.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        WebView view = (WebView) findViewById(R.id.webview);
        String myurl = "https://docs.google.com/document/d/e/2PACX-1vSjthBVX41XMKIo1ioIYY7CP70zYjC0Z2Ftki9bjc-OdoeRg2n6K7L84AY3Sfoi9o3ypYc6jaemjP5x/pub";
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(myurl);
//        setContentView(view);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add back arrow to toolbar.
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.menu_privacy_policy));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle arrow click here.
        if (item.getItemId() == android.R.id.home) {
            // Close this activity and return to preview activity.
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
