package com.example.collegeapp.ebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.collegeapp.R;

public class pdfViewerActivity extends AppCompatActivity {
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        url = getIntent().getStringExtra("pdfUrl");

        // Find the WebView in your layout
        WebView webView = findViewById(R.id.webView);

        // Enable JavaScript (optional, but might be necessary for some PDFs)
        webView.getSettings().setJavaScriptEnabled(true);

        // Load the PDF URL into the WebView
        webView.loadUrl(url);

        // Set a WebViewClient to handle page navigation within the WebView
        webView.setWebViewClient(new WebViewClient());

    }
}