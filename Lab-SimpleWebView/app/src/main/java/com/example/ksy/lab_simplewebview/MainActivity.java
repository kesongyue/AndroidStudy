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
               //Todo: 绑定固定的URL
                webView.loadUrl("https://www.baidu.com/");

//                下面是测试从H5跳转回Android原生的代码
//                if(mui.os.android){
//                    var main = plus.android.runtimeMainActivity();
//                    var Intent = plus.android.importClass("android.content.Intent");
//                    var intent = new Intent(main.getIntent());
//                    intent.setClassName(main, "com.sysu.Smart_health.SimpleWebviewActivity");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    main.startActivity(intent);
//                }else {
//                    mui.alert('目前一键开锁功能仅支持安卓平台。', '提示', null);
//                }
            }
        });
    }
}
