package info.a7mady911.myCourses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import com.loopj.android.http.*;

/**
 * User: A7madY911
 * Date: 4/12/13
 */

public class MainUI extends Activity {

    public ConnectivityManager connectivityManager;
    public NetworkInfo activeNetworkInfo;
    private SharedPreferences userInfoPrefs;
    private Button submitButton;
    private EditText idTextField;
    private Intent nextActivityIntent;
    private String student_id, student_email, genCode = null;
    private Boolean user_loggedin, vcode_sent;
    private int codeMin = 100, codeMax = 999;
    private String codeFileName = "vcu";
    private File codeFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);

        //declare shared user info prefs and check for info
        userInfoPrefs = getSharedPreferences("user_info", MODE_PRIVATE);
        student_id = userInfoPrefs.getString("student_id", null);
        user_loggedin = userInfoPrefs.getBoolean("user_loggedin", false);
        vcode_sent = userInfoPrefs.getBoolean("vcode_sent", false);

        //skip if user is logged in
        if(user_loggedin == true){
            //Go to the home screen
            nextActivityIntent = new Intent(MainUI.this, HomeUI.class);
            startActivity(nextActivityIntent);
            finish();
        }

        //skip if user is verification code sent
        if(vcode_sent == true){
/*            //Go to the home screen
            nextActivityIntent = new Intent(MainUI.this, VerifyUI.class);
            startActivity(nextActivityIntent);
            finish();*/
        }

        //Declare UI components
        submitButton = (Button) findViewById(R.id.submit_button);
        idTextField = (EditText) findViewById(R.id.id_editText);

        //submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable component for security
                submitButton.setEnabled(false);

                //validate text input
                if (idTextField.getText().length() != 0){
                    //check for internet connection
                    connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo.isConnected()) {
                        //save preferences
                        student_email = student_id + "@student.polytechnic.bh";
                        SharedPreferences.Editor sharedPresEditor = userInfoPrefs.edit();
                        sharedPresEditor.putString("student_id", idTextField.getText().toString());
                        sharedPresEditor.putString("student_email", student_email);
                        sharedPresEditor.commit();

                        //Generate and send verification code
                        try {
                            final Random rand = new Random();
                            genCode = "A" + rand.nextInt(codeMax) + codeMin;  //generate number between codeMin and codeMax
                            if(genCode != null){
                                //save verification code in file
                                try {
                                    FileOutputStream fos = openFileOutput(codeFileName, Context.MODE_PRIVATE);
                                    fos.write(genCode.getBytes());
                                    fos.close();
                                    codeFile = getBaseContext().getFileStreamPath(codeFileName);
                                    Toast.makeText(MainUI.this, "saved code is : " + genCode, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    //show error message
                                    Toast.makeText(MainUI.this, "Streaming error!", Toast.LENGTH_SHORT).show();
                                }

                                if(codeFile.exists()){
                                    //send verification code
                                    RequestParams params = new RequestParams();
                                    params.put("student_email", student_email);
                                    params.put("verification_code", "Verification code is: " + genCode);
                                    AsyncHttpClient client = new AsyncHttpClient();
                                    client.post("http://a7mady911.info/mycourses/verify.php", params, new AsyncHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(String response) {
                                            //save code sent preferences
                                            SharedPreferences.Editor sharedPresEditor = userInfoPrefs.edit();
                                            sharedPresEditor.putBoolean("vcode_sent", true);
                                            sharedPresEditor.commit();

                                            //start next activity
                                            nextActivityIntent = new Intent(MainUI.this, VerifyUI.class);
                                            startActivity(nextActivityIntent);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Throwable e, String response) {
                                            //show error message
                                            Toast.makeText(MainUI.this, "Error! Cannot connect to the Internet", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    //show error message
                                    Toast.makeText(MainUI.this, "Error! Cannot send the verification code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            //show error message
                            Toast.makeText(MainUI.this, "Cannot send generated code!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //show no connection message
                        Toast.makeText(MainUI.this, "Internet connection is not available!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //show error message
                    Toast.makeText(MainUI.this, "Enter your student ID!", Toast.LENGTH_SHORT).show();
                }

                //enable component for security
                submitButton.setEnabled(true);
            }
        });
    }

}
