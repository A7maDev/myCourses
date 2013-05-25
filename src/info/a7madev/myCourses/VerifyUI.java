package info.a7madev.myCourses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: A7maDev
 */
public class VerifyUI extends Activity {

    private String codeFileName = "vcu", vCode = null;
    private File codeFile;
    private Button submitButton;
    private EditText codeTextField;
    private Intent nextActivityIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_ui);

        //Declare UI components
        submitButton = (Button) findViewById(R.id.submit_button);
        codeTextField = (EditText) findViewById(R.id.code_editText);

        //show sent info message
        Toast.makeText(this, "Verification code sent to your Polytechnic email", Toast.LENGTH_SHORT).show();

        //check file exists
        codeFile = getBaseContext().getFileStreamPath(codeFileName);
        if (codeFile.exists()) {
            //load verification code from file
            try {
                FileInputStream fis = openFileInput(codeFileName);
                StringBuffer vCodeBuffer = new StringBuffer("");
                byte[] buffer = new byte[1024];
                while (fis.read(buffer) != -1) {
                    vCodeBuffer.append(new String(buffer));
                }
                vCode = vCodeBuffer.toString();

                //enable components
                submitButton.setEnabled(true);
            } catch (IOException e) {
                //show error message
                Toast.makeText(this, "Error! Please contact the developer", Toast.LENGTH_SHORT).show();
            }
        } else {
            //show error message
            Toast.makeText(this, "Error! Please contact the developer", Toast.LENGTH_SHORT).show();

            //disable components
            submitButton.setEnabled(false);
        }

        //On click submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyUI.this, "vCode is: " + vCode, Toast.LENGTH_SHORT).show();
                Toast.makeText(VerifyUI.this, "entered: " + codeTextField.getText().toString(), Toast.LENGTH_SHORT).show();
                if (vCode.equalsIgnoreCase(codeTextField.getText().toString())) {
                    //show success message
                    Toast.makeText(VerifyUI.this, "User verified successfully.", Toast.LENGTH_SHORT).show();

                    //start next activity
                    nextActivityIntent = new Intent(VerifyUI.this, HomeUI.class);
                    startActivity(nextActivityIntent);
                    finish();
                } else {
                    //show error message
                    Toast.makeText(VerifyUI.this, "Error! Incorrect verification code", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}