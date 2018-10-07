/*
 * File     : MainActivity.java
 * Project  : TemplateActivity
 * Author   : Markus Jaton 2 juillet 2014
 * 			  Fabien Dutoit 28 août 2018
 *            IICT / HEIG-VD
 *
 * mailto:fabien.dutoit@heig-vd.ch
 *
 * This piece of code reads a [email_account / password ] combination.
 * It is used as a template project for the SYM module element given at HEIG-VD
 * Target audience : students IL, TS, IE [generally semester 1, third bachelor year]
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package ch.heigvd.sym.template;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import ch.heigvd.sym.util.AuthManager;

public class MainActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    private AuthManager authManager = new AuthManager();

    // GUI elements
    private EditText email      = null;
    private EditText password   = null;
    private Button   signIn     = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Show the welcome screen / login authentication dialog
        setContentView(R.layout.authent);

        Intent intent   = getIntent();

        if(intent != null) {
            //Get imei we got if user logged out
            String imei = intent.getStringExtra("imei");
            Toast.makeText(MainActivity.this,"Got imei: " + imei, Toast.LENGTH_LONG).show();
        }


        // Link to GUI elements
        this.email      = findViewById(R.id.email);
        this.password   = findViewById(R.id.password);
        this.signIn     = findViewById(R.id.buttOk);

        this.email.setText("guillaume.hochet@heig-vd.ch");
        this.password.setText("yolo");

        // Then program action associated to "Ok" button
        signIn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {

                String mail     = email.getText().toString();
                String passwd   = password.getText().toString();

                if(mail.isEmpty())
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.empty), Toast.LENGTH_LONG).show();

                else if(!mail.contains("@") || !mail.contains(".") || mail.length() < 5)
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.invalid), Toast.LENGTH_LONG).show();

                else if(!authManager.exist(mail))
                    Toast.makeText(MainActivity.this, R.string.unknown, Toast.LENGTH_LONG).show();

                if (authManager.login(mail, passwd)) {

                    Toast.makeText(MainActivity.this, getResources().getString(R.string.good), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, LoggedActivity.class);
                    intent.putExtra("given_email", mail);
                    intent.putExtra("given_image", authManager.getAccount(mail).getImage());
                    startActivity(intent);

                    finish();

                } else {
                    // Wrong combination, display pop-up dialog and stay on login screen
                    showErrorDialog(mail, passwd);
                }
            }

        });
    }

    @Override
    protected void onStart() {

        Log.d("onStartMainActivity", "OnStart hook called, when activity is made interactive to the user");
        super.onStart();
    }

    @Override
    protected void onResume() {

        Log.d("onResumeMainActivity", "OnResume, activity is visible by the user");
        super.onResume();
    }

    @Override
    protected void onPause() {

        Log.d("onPauseMainActivity", "OnPause, activity might still be visible but not in foreground");
        super.onPause();
    }

    @Override
    protected void onStop() {

        Log.d("onStopMainActivity", "OnStop, activity not visible by the user anymore");
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        Log.d("onDestroyMainActivity", "OnDestroy, activity is destroyed and finished");
        super.onDestroy();
    }

    protected void showErrorDialog(String mail, String passwd) {

        AlertDialog.Builder alertbd = new AlertDialog.Builder(this);
        alertbd.setIcon(R.drawable.baseline_error_black_18dp);
        alertbd.setTitle(R.string.wronglogin);
        alertbd.setMessage(R.string.wrong);
        alertbd.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // we do nothing...
                // dialog close automatically
            }
        });
        alertbd.create().show();
    }

}
