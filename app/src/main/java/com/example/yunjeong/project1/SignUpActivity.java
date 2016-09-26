package com.example.yunjeong.project1;

import android.provider.Settings.Secure;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {
    Spinner spinner1;
    Spinner spinner2;
    Button mAccountbtn;
    Snackbar snackbar;
    EditText etFullName;
    EditText etUserId;
    EditText etEmailAddr;
    EditText etUserPass;
    //url
    private static String url_Sign_Up = "http://14.63.196.137/app/SignUp.php";
    private InputMethodManager ipm;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_sign_up);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Sign up");
        /*
        TODO: TITLE 가운데로 옮기기
        Get a support ActionBar corresponding to this toolbar
        */
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        etFullName = (EditText) findViewById(R.id.etFullName);
        etUserId = (EditText) findViewById(R.id.etUserID);
        etEmailAddr = (EditText) findViewById(R.id.etEmailAddr);
        etUserPass = (EditText) findViewById(R.id.etUserPass);

        /*
        * http://stackoverflow.com/questions/9476665/how-to-change-spinner-text-size-and-text-color
        * spinner디자인
        */
        spinner1 = (Spinner) findViewById(R.id.ageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.age_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner2 = (Spinner) findViewById(R.id.inloveSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.inlove_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        //Sing up complete popup
        snackbar = Snackbar.make(findViewById(R.id.mySignupLayout), R.string.signup_complete ,Snackbar.LENGTH_LONG);
        mAccountbtn = (Button) findViewById(R.id.accountbtn);

        mAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = checkValid();
                if(!check) {
                    Toast.makeText(SignUpActivity.this, "정보를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkPassword();
                //keyboard down
               new SignUpExcute(etFullName.getText().toString(),
                        etUserId.getText().toString(),
                        etEmailAddr.getText().toString(),
                        etUserPass.getText().toString(),
                        spinner1.getSelectedItem().toString(),
                        spinner2.getSelectedItem().toString()).execute();
            }
        });
    }

    private boolean checkValid() {
        String name = etFullName.getText().toString();
        String id = etUserId.getText().toString();
        String email = etEmailAddr.getText().toString();
        String pass = etUserPass.getText().toString();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            return false;
        } else {
            return true;
        }
    }

    private void checkPassword() {
        boolean cancel = false;
        //password valid
        etUserPass.setError(null);

        View focusView = null;

        String password = etUserPass.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Log.d("pass","start check");
            etUserPass.setError(getString(R.string.error_invalid_password));
            focusView = etUserPass;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup
            // form field with an error.
            focusView.requestFocus();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Background Async Task to Create new Account
     */
    class SignUpExcute extends AsyncTask<String, String, String> {

        String name, userID, emailAddr, userPass, age_array, inlove_array;
        int success;
        public SignUpExcute(String name, String userID, String emailAddr, String userPass, String age_array, String inlove_array) {
            this.name = name;
            this.userID = userID;
            this.emailAddr = emailAddr;
            this.userPass = userPass;
            this.age_array = age_array;
            this.inlove_array = inlove_array;
        }

        @Override
        protected String doInBackground(String... params) {

            PostData postData = new PostData();
            //GET ANDROID_ID
            final String android_id = Secure.getString(SignUpActivity.this.getContentResolver(), Secure.ANDROID_ID);
            Log.d("android_id:", android_id);

            RequestBody formbody = new FormBody.Builder()
                    .add("name", name)
                    .add("userID", userID)
                    .add("emailAddr", emailAddr)
                    .add("userPass", userPass)
                    .add("age_array", age_array)
                    .add("inlove_array", inlove_array)
                    .add("android_id", android_id)
                    .build();

            String response = null;

            try {
                response = postData.run(url_Sign_Up, formbody);
                JSONObject json = new JSONObject(response);
                Log.d("sign_up_json", json.toString());
                success = json.getInt(TAG_SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
         }

        @Override
        protected void onPostExecute(String s) {
//             successfully created account
            if (success == 1) {
                snackbar.show();
                finish();
            } else {
                //failed
                snackbar = Snackbar.make(findViewById(R.id.mySignupLayout), R.string.signup_idDuplicated ,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

    }
}

