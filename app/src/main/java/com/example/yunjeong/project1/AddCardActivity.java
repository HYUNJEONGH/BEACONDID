package com.example.yunjeong.project1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddCardActivity extends AppCompatActivity {
    private Spinner spinner;
    private Button btn_add_card;
    private static String url_add_Card = "http://14.63.196.137/app/add_Card.php";
    private static final String TAG_SUCCESS = "success";

    // UI references.
    private EditText mCardfieldone;
    private EditText mCardfieldotwo;
    private EditText mCardfieldthree;
    private EditText mCardfieldfour;
    private EditText mCardName;
    private EditText mCardExpiaryM;
    private EditText mCardExpiaryY;
    private EditText mCardCVC;
    private EditText mCardPasswd;
    private EditText mCardPasswdcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/koverwatch.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_add_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_card_toolbar);
         /* center */
        toolbar.inflateMenu(R.menu.setting_menu);
        toolbar.setTitle("ADD CARD");

        mCardfieldone = (EditText)findViewById(R.id.num_field_one);
        mCardfieldotwo = (EditText)findViewById(R.id.num_field_two);
        mCardfieldthree = (EditText)findViewById(R.id.num_field_three);
        mCardfieldfour = (EditText)findViewById(R.id.num_field_four);
        mCardName = (EditText)findViewById(R.id.addcard_name);
        mCardExpiaryM = (EditText)findViewById(R.id.card_expiry_month);
        mCardExpiaryY = (EditText)findViewById(R.id.card_expiry_year);
        mCardCVC = (EditText)findViewById(R.id.card_cvc);
        mCardPasswd = (EditText)findViewById(R.id.card_passwd);
        mCardPasswdcheck = (EditText)findViewById(R.id.card_passwd_recheck);

        spinner = (Spinner) findViewById(R.id.card_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.card_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });

        btn_add_card = (Button)findViewById(R.id.btn_add_card);
        btn_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVaild();
                if(checkPasswd()) {
                    //add card
                    new AddCardExecute(setCard()).execute();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private CardData setCard() {

        String numone = mCardfieldone.getText().toString();
        String numtwo = mCardfieldotwo.getText().toString();
        String numthree =  mCardfieldthree.getText().toString();
        String numfour = mCardfieldfour.getText().toString();

        String cardNum = numone + numtwo + numthree + numfour;
        String cardName = mCardName.getText().toString();
        String cardExpiaryM = mCardExpiaryM.getText().toString();
        String cardExpiaryY = mCardExpiaryY.getText().toString();
        String cardEcpiration = cardExpiaryM + "/" + cardExpiaryY;
        String cardCvc = mCardCVC.getText().toString();
        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String userid = preferences.getString(Config.USERID_SHARED_PREF, "no");
        String cardPasswd = mCardPasswd.getText().toString();
        String cardType = spinner.getSelectedItem().toString();

        return new CardData(cardNum, cardName, userid, cardEcpiration, cardPasswd, Integer.parseInt(cardCvc), cardType);
    }

    private void checkVaild() {
        boolean cancel = false;
        View focusView = null;
        String numone = mCardfieldone.getText().toString();
        String numtwo = mCardfieldotwo.getText().toString();
        String numthree =  mCardfieldthree.getText().toString();
        String numfour = mCardfieldfour.getText().toString();
        String cardName = mCardName.getText().toString();
        String cardExpiaryM = mCardExpiaryM.getText().toString();
        String cardExpiaryY = mCardExpiaryY.getText().toString();
        String cardCvc = mCardCVC.getText().toString();

        if(TextUtils.isEmpty(numone) || TextUtils.isEmpty(numtwo) || TextUtils.isEmpty(numthree) || TextUtils.isEmpty(numfour)) {
            Toast.makeText(this, "카드번호를 입력하세요", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(cardName)) {
            mCardName.setError(getString(R.string.error_field_required));
            focusView = mCardName;
            cancel = true;
        } else if(TextUtils.isEmpty(cardExpiaryM) || TextUtils.isEmpty(cardExpiaryY)) {
            mCardExpiaryM.setError(getString(R.string.error_field_required));
            focusView = mCardExpiaryM;
            cancel = true;
        }  else if(TextUtils.isEmpty(cardCvc)) {
            mCardCVC.setError(getString(R.string.error_field_required));
            focusView = mCardCVC;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
    }

    private boolean checkPasswd() {
        String cardPasswd = mCardPasswd.getText().toString();
        String cardPasswdre = mCardPasswdcheck.getText().toString();

        if(!TextUtils.isEmpty(cardPasswd) && !isPasswordValid(cardPasswd)) {
           mCardPasswd.setError(getString(R.string.error_invalid_password));
        } else if(TextUtils.isEmpty(cardPasswd) || TextUtils.isEmpty(cardPasswdre)){
            Toast.makeText(this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return  false;
        } else if(!TextUtils.isEmpty(cardPasswd) && !TextUtils.isEmpty(cardPasswdre)) {
            if(cardPasswd.equals(cardPasswdre)) {
                return true;
            } else {
                mCardPasswdcheck.setError(getString(R.string.error_incorrect_password));
                mCardPasswdcheck.requestFocus();
                return  false;
            }
        }
        return false;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 11;
    }

    class AddCardExecute extends AsyncTask<String, Void, String> {
        CardData cardData;
        int success;
        public AddCardExecute(CardData cardData) {
            this.cardData = cardData;
        }

        @Override
        protected String doInBackground(String... params) {
            PostData postData = new PostData();

            RequestBody formbody = new FormBody.Builder()
                    .add("cardNum", cardData.getCardNum())
                    .add("cardName", cardData.getCardName())
                    .add("userID", cardData.getUserID())
                    .add("cardExpiration", cardData.getCardExpiration())
                    .add("cardPassword", cardData.getCardPassword())
                    .add("cardCVCNum", Integer.toString(cardData.getCardCVCNum()))
                    .add("cardType", cardData.getCardType())
                    .build();

            String response = null;
            JSONObject json = null;
            try {
                response = postData.run(url_add_Card, formbody);
                json = new JSONObject(response);
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
//             successfully created card
            if(success == 1) {
                final Dialog dialog = new Dialog(AddCardActivity.this);
                dialog.setContentView(R.layout.card_success_dialog);
                Button btnOk = (Button) dialog.findViewById(R.id.btnok);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(AddCardActivity.this, BottombarActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        }
    }
}
