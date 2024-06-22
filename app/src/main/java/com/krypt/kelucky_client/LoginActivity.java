package com.krypt.kelucky_client;

import static com.krypt.kelucky_client.utils.Urls.URL_LOGIN;
import static com.krypt.kelucky_client.utils.Urls.URL_LOGINO;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.kelucky_client.utils.SessionHandler;
import com.krypt.kelucky_client.utils.UserModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private SessionHandler session;
    private UserModel user;
    private Button btn_login;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);


        session = new SessionHandler(LoginActivity.this);
        user = session.getUserDetails();

        EditText usernm=findViewById(R.id.usrnm);
        btn_login=findViewById(R.id.login_btn);
        EditText pass=findViewById(R.id.pass);

        EditText acccode=findViewById(R.id.acccode);
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 11) {
                    // Trim the text to 10 characters
                    pass.setText(s.subSequence(0, 11));
                    // Move the cursor to the end
                    pass.setSelection(11);
                }
            }
        });
        usernm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 10) {
                    // Trim the text to 10 characters
                    usernm.setText(s.subSequence(0, 10));
                    // Move the cursor to the end
                    usernm.setSelection(10);
                }
            }
        });
        acccode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 5) {
                    // Trim the text to 10 characters
                    acccode.setText(s.subSequence(0, 5));
                    // Move the cursor to the end
                    acccode.setSelection(5);
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sunm=usernm.getText().toString();
                String acc=acccode.getText().toString();
                String passi=pass.getText().toString();
                if (sunm.isEmpty() ||acc.isEmpty()||passi.isEmpty()){
                    Toast.makeText(LoginActivity.this,"fill all fields",Toast.LENGTH_LONG).show();
                    return;

                }

                login(sunm,passi,acc);


            }
        });

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void login(String phone, String pass,String acces) {
        if (phone.isEmpty()||pass.isEmpty()||acces.isEmpty()) {
            Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("lengths",phone.length()+" "+acces.length()+" "+pass.length());
        if (phone.length() != 10) {
            // Phone number is exactly 10 digit
            Toast.makeText(this, "Phone number should be 10 digits", Toast.LENGTH_SHORT).show();
        }
        if (acces.length() != 5) {
            Toast.makeText(this, "5 digit access code required", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 11;
        if (pass.length()>i) {
            Toast.makeText(this, "11 digit pass code required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGINO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            //String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (msg.equals("Successful")) {

                                    session.loginUser(phone, "", "", pass);



                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("key", phone);

                                startActivity(intent);
                                    LoginActivity.this.onBackPressed();

                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                                LoginActivity.this.onBackPressed();

                            } else if (msg.equals("Failed to log in.")) {

                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            } else if (msg.equals("Failed to log in.")) {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                btn_login.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("accesscode", acces);
                params.put("password", pass);
                params.put("username",phone );
                params.put("model", Build.MODEL);
                Log.e("params",params.toString());
                          return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);

    }
}