package util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.conference.R;
import com.example.conference.TableActivity;

import org.json.JSONException;
import org.json.JSONObject;

import server.ServerRoot;
import server.SignInServer;

public class SignIn {
    Activity activity;
    private EditText idEdit, pwEdit;
    private Button signInBtn;
    public SignIn(final Activity activity) {
        this.activity = activity;

        idEdit = activity.findViewById(R.id.idEdit);
        pwEdit = activity.findViewById(R.id.pwEdit);
        signInBtn = activity.findViewById(R.id.signInBtn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                signIn();
                Intent intent = new Intent(activity, TableActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    public void signIn(){
        SignInServer signInServer = new SignInServer(activity, "put serverURL here");
        signInServer.setInfo(idEdit.getText().toString().trim(), pwEdit.getText().toString().trim());
        signInServer.setLister(new ServerRoot.OnAfterLister() {
            @Override
            public void after(String result) {
                try {
                    JSONObject o = new JSONObject(result);
                    if((boolean)o.get("signIn")){
                        Intent intent = new Intent(activity, TableActivity.class);
                        activity.startActivity(intent);
                    }else{
                        Toast.makeText(activity, "회원정보가 없거나 틀린 패스워드", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            signInServer.run();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
