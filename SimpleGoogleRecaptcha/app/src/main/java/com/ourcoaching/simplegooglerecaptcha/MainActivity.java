package com.ourcoaching.simplegooglerecaptcha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private final String url_server_file="https://www.ourcoaching.com/kunal_testCodes/captcha_server_file.php";
    private final String TAG="CAPTCHA ";

    private CheckBox checkBox;
    private TextView textView,textView2;
    // Downloaded from https://github.com/KuKapoor02
    // Visit https://www.ourcoaching.com/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox=(CheckBox)findViewById(R.id.checkBox);
        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CaptchCheckChallenge(b);
            }
        });
    }

    private void CaptchCheckChallenge(boolean b){

        if (b){
            SafetyNet.getClient(this).verifyWithRecaptcha("6Ld4al0UAAAAACHXeuxoMJlv_hT8zoEBqaxnaPN0")
                    .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                        @Override
                        public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                            if (!response.getTokenResult().isEmpty()) {
                                handleSiteVerify(response.getTokenResult());
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof ApiException) {
                                ApiException apiException = (ApiException) e;
                                Log.d(TAG, "Error message: " +
                                        CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                            } else {
                                Log.d(TAG, "Unknown type of error: " + e.getMessage());
                            }
                        }
                    });
        }

    }

    private void handleSiteVerify(String tokenResult) {
        textView.setText(tokenResult);

        CaptchaServerCode captchaServerCode=new CaptchaServerCode();
        captchaServerCode.execute(tokenResult);
    }



    private class CaptchaServerCode extends AsyncTask<String,Void,String>{

        URL url;
        BufferedWriter bufferedWriter;
        BufferedReader bufferedReader;
        OutputStream outputStream;
        InputStream inputStream;
        HttpsURLConnection httpsURLConnection;

        String output="";

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                url=new URL(url_server_file);
                httpsURLConnection=(HttpsURLConnection)url.openConnection();
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setRequestMethod("POST");

                httpsURLConnection.connect();

                String sender_String= URLEncoder.encode("token","UTF-8") + "=" + URLEncoder.encode(strings[0],"UTF-8");

                outputStream=httpsURLConnection.getOutputStream();
                bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                bufferedWriter.write(sender_String);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                if (httpsURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){

                    inputStream=httpsURLConnection.getInputStream();
                    bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                    output="";

                    if (bufferedReader.readLine()!=null){
                        output+=bufferedReader.readLine();
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpsURLConnection.disconnect();

                    return output;
                }else {
                    return "error, connection problem";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.cancel();

            textView2.setText(s);
        }
    }

}
