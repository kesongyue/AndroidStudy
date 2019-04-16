package com.example.ksy.lab_simplewebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button searchBtn;
    EditText urlEditText;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBtn = findViewById(R.id.search_btn);
        urlEditText = findViewById(R.id.edittext_url);
        webView = findViewById(R.id.web_view);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlEditText.getText().toString();
                webView.getSettings().setJavaScriptEnabled(true);
                //webView.setWebViewClient(new WebViewClient());
                webView.loadUrl("https://www.baidu.com/");
            }
        });
    }
}
