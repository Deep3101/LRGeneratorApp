package com.example.lrgeneratorapp;

import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    EditText truck, policyNo, policyDate, policyAmount, consignor, consignee, pkgs, deliveryAddress, weight, desc, conNo, insCo, rate, hamali, invoiceNo, value, gstin, consignorName, consignorAddress, consigneeName, consigneeAddress, fromCity, toCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        truck = findViewById(R.id.et_truck_no);
//        consignor = findViewById(R.id.et_consignor);
        consignorName = findViewById(R.id.et_consignor_name);
        consignorAddress = findViewById(R.id.et_consignor_address);
        consigneeName = findViewById(R.id.et_consignee_name);
        consigneeAddress = findViewById(R.id.et_consignee_address);
        deliveryAddress = findViewById(R.id.et_delivery_address);
        invoiceNo = findViewById(R.id.et_invoice);
//        consignee = findViewById(R.id.et_consignee);
        fromCity = findViewById(R.id.et_from);
        toCity = findViewById(R.id.et_to);
        pkgs = findViewById(R.id.et_pkgs);
        weight = findViewById(R.id.et_weight);
        desc = findViewById(R.id.et_description);
        conNo = findViewById(R.id.et_con_no);
        insCo = findViewById(R.id.et_ins_co);
        rate = findViewById(R.id.et_rate);
        hamali = findViewById(R.id.et_hamali);
        value = findViewById(R.id.et_value);
        gstin = findViewById(R.id.et_gstin);
        policyNo = findViewById(R.id.et_policy_no);
        policyDate = findViewById(R.id.et_policy_date);
        policyAmount = findViewById(R.id.et_policy_amount);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);

        Button btn = findViewById(R.id.btn_generate);

        btn.setOnClickListener(v -> generateLR());
    }

    private void generateLR() {

        String html = loadHTML();

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String sDeliveryAddr = deliveryAddress.getText().toString();
        double rateVal = 0;
        double hamaliVal = 0;

        try {
            rateVal = Double.parseDouble(rate.getText().toString());
        } catch (Exception ignored) {
        }

        try {
            hamaliVal = Double.parseDouble(hamali.getText().toString());
        } catch (Exception ignored) {
        }
        double totalVal = rateVal + hamaliVal + 50.00; // Adding the fixed 50.00 St. Ch. from image

        html = html.replace("{{policy_no}}", policyNo.getText().toString());
        html = html.replace("{{policy_date}}", policyDate.getText().toString());
        html = html.replace("{{policy_amount}}", policyAmount.getText().toString());
        html = html.replace("{{risk}}", "Owner's Risk");
        html = html.replace("{{delivery_address}}", sDeliveryAddr);
        html = html.replace("{{con_no}}", conNo.getText().toString());
        html = html.replace("{{ins_co}}", insCo.getText().toString());
        html = html.replace("{{rate}}", String.format("%.2f", rateVal));
        html = html.replace("{{hamali}}", String.format("%.2f", hamaliVal));
        html = html.replace("{{total}}", String.format("%.2f", totalVal));
        html = html.replace("{{val_rs}}", value.getText().toString());
        html = html.replace("{{gstin}}", gstin.getText().toString());
        html = html.replace("{{consignor_name}}", consignorName.getText().toString());
        html = html.replace("{{consignor_address}}", consignorAddress.getText().toString());
        html = html.replace("{{consignee_name}}", consigneeName.getText().toString());
        html = html.replace("{{consignee_address}}", consigneeAddress.getText().toString());
        html = html.replace("{{invoice_no}}", invoiceNo.getText().toString());
        html = html.replace("{{truck}}", truck.getText().toString());
//        html = html.replace("{{consignor}}", consignor.getText().toString());
//        html = html.replace("{{consignee}}", consignee.getText().toString());
        html = html.replace("{{from_city}}", fromCity.getText().toString());
        html = html.replace("{{to_city}}", toCity.getText().toString());
        html = html.replace("{{pkgs}}", pkgs.getText().toString());
        html = html.replace("{{actual_weight}}", weight.getText().toString());
        html = html.replace("{{charged_weight}}", weight.getText().toString());
        html = html.replace("{{description}}", desc.getText().toString());
        html = html.replace("{{date}}", date);

        String[] copies = {
                "CONSIGNEE COPY",
                "CONSIGNOR COPY",
                "DRIVER COPY",
                "TRANSPORTER COPY",
                "EXTRA COPY"
        };

        String finalHTML = "";

        for (String copy : copies) {

            String page = html.replace("{{copy}}", copy);

            finalHTML += page + "<div style='page-break-after:always'></div>";
        }

        webView.loadDataWithBaseURL(
                "file:///android_asset/",
                finalHTML,
                "text/html",
                "UTF-8",
                null
        );

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                createPDF();
            }
        });
    }

    private String loadHTML() {

        String html = "";

        try {

            InputStream is = getAssets().open("lr_template.html");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            html = new String(buffer, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return html;
    }

    private void createPDF() {

        String fileName = "LR_" + conNo.getText().toString();

        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter(fileName);

        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        printManager.print(
                "LR_Print",
                adapter,
                attributes
        );
    }
}