package com.yoonhs3434.suroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.yoonhs3434.suroom.myLibrary.HttpPost;

import org.json.JSONException;
import org.json.JSONObject;

public class SubjectChoiceActivity extends Activity {

    int num_of_btn;
    CheckBox job_btn, eng_btn, finance_btn, pro_btn, interst_btn, exam_btn, etc_btn;
    CheckBox [] btn;
    Button close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subject_choice);

        num_of_btn = 7;
        btn = new CheckBox[num_of_btn];

        btn[0] = (job_btn = (CheckBox) findViewById(R.id.jobButton));
        btn[1] = (eng_btn = (CheckBox) findViewById(R.id.englishButton));
        btn[2] = (finance_btn = (CheckBox) findViewById(R.id.financeButton));
        btn[3] = (pro_btn = (CheckBox) findViewById(R.id.programmingButton));
        btn[4] = (interst_btn = (CheckBox) findViewById(R.id.interestButton));
        btn[5] = (exam_btn = (CheckBox) findViewById(R.id.examButton));
        btn[6] = (etc_btn = (CheckBox) findViewById(R.id.etcButton));

        close_btn = (Button) findViewById(R.id.closeButton);
    }

    public void closeButtonClicked(View v) throws JSONException {
        JSONObject data_req = new JSONObject();
        data_req.accumulate("id", MySetting.getMyId());
        for(int i=0; i<num_of_btn ; i++){
            data_req.accumulate(String.valueOf(btn[i].getText()), convertBoolInt(btn[i].isChecked()));
        }

        HttpPost send = new HttpPost(MySetting.getMyUrl()+"choice/subject/", data_req);
        Toast.makeText(getApplicationContext(), data_req.toString(), Toast.LENGTH_LONG).show();
        send.start();
        try {
            send.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        return;
    }

    private int convertBoolInt(boolean input){
        if(input){
            return 1;
        }
        else
            return 0;
    }
}
