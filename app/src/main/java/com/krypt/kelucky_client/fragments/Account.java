package com.krypt.kelucky_client.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.krypt.kelucky_client.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Account extends Fragment {
    String data;
    Button deposit,with;
    TextView acc, balance;
    private static final String CONSUMER_KEY = "aUlBQgrFMGLdvF1yhyhLGGU4iJW9NQJKqHLngQVfkrAyqC6S";
    private static final String CONSUMER_SECRET = "ajSEvmK48U0jV0n4UNk6EfmsKrXuNH1ebRqnFdOKivOcW1Dlg3fHIiJh0UrOvbZm";
    private static final String SHORTCODE = "174379";
    private static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    private static final String INITIATOR_NAME = "testapi";
    private static final String SECURITY_CREDENTIAL = "your_security_credential";
    private static final String TRANSACTION_TYPE = "CustomerPayBillOnline";

    private static final String MPESA_URL = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
    private static final String MPESA_PAYMENT_URL = "https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
         data = intent.getStringExtra("key");
        Log.e("testno",data.substring(1));

        acc=view.findViewById(R.id.acc_txt);
        balance=view.findViewById(R.id.acc_view);
        deposit=view.findViewById(R.id.depoBtn);
        with=view.findViewById(R.id.withBtn);
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateMpesaPayment();

            }
        });
        with.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Withdraw still in dev", Toast.LENGTH_SHORT).show();
            }
        });
        balance.setText("Amount : 0");
        acc.setText("acc :"+data);
    }
    private void initiateMpesaPayment() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String accessToken = getAccessToken();
                    return makeMpesaPayment(accessToken);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    // Handle response
                    // You can parse the JSON response here and update UI accordingly
                } else {
                    // Handle error
                }
            }
        }.execute();
    }

    private String getAccessToken() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(MPESA_URL)
                .get()
                .addHeader("Authorization", "Basic " + getCredentials())
                .build();

        Response response = client.newCall(request).execute();
        JSONObject jsonObject = new JSONObject(response.body().string());
        Log.e("response sa",response.body().string());
        return jsonObject.getString("access_token");
    }


    private String makeMpesaPayment(String accessToken) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject requestBody = new JSONObject();
        requestBody.put("BusinessShortCode", SHORTCODE);
        requestBody.put("Password", getPassword());
        requestBody.put("Timestamp", getTimestamp());
        requestBody.put("TransactionType", TRANSACTION_TYPE);
        requestBody.put("Amount", "1");
        requestBody.put("PartyA", "254"+data.substring(1)); // replace with customer phone number
        requestBody.put("PartyB", SHORTCODE);
        requestBody.put("PhoneNumber", "254"+data.substring(1)); // replace with customer phone number
        requestBody.put("CallBackURL", "your_callback_url");
        requestBody.put("AccountReference", "Test");
        requestBody.put("TransactionDesc", "Test payment");

        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url(MPESA_PAYMENT_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private String getCredentials() {
        String credentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;

        // Encode credentials to Base64
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    private String getPassword() {
        String timestamp = getTimestamp();
        String encodedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(SHORTCODE + PASSKEY + timestamp);
        return encodedPassword;
    }

    private String getTimestamp() {
        return new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
    }
}
