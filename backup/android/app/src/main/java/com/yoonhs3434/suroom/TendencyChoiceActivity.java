package com.yoonhs3434.suroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yoonhs3434.suroom.myLibrary.HttpPost;

import org.json.JSONException;
import org.json.JSONObject;

public class TendencyChoiceActivity extends Activity {

    int num_of_group;
    RadioGroup [] radioGroups;
    RadioButton[][] radioButtons;
    Button closeButton;
    TextView[] textViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tendency_choice);

        num_of_group = 6;
        radioGroups = new RadioGroup[num_of_group];
        radioButtons = new RadioButton[num_of_group][3];
        textViews = new TextView[num_of_group];
        buttonInit(radioGroups, radioButtons, textViews);

        closeButton = (Button) findViewById(R.id.closeButton);

    }

    public void closeButtonClicked(View v) throws JSONException {
        int index=0;

        JSONObject data_req = new JSONObject();
        data_req.accumulate("id", MySetting.getMyId());
        for(int i=0; i<num_of_group; i++){
            for(int j=0; j<3; j++){
                if(radioButtons[i][j].isChecked() == true) {
                    index = j;
                }
            }
            data_req.accumulate(String.valueOf(textViews[i].getText()), index);
        }

        HttpPost send = new HttpPost(MySetting.getMyUrl() + "choice/tendency/", data_req);
        send.start();
        try {
            send.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void buttonInit(RadioGroup [] radioGroups, RadioButton [][] radioButtons, TextView[] textViews){
        radioGroups[0] = (RadioGroup) findViewById(R.id.ruleGroup);
        radioGroups[1] = (RadioGroup) findViewById(R.id.learningGroup);
        radioGroups[2] = (RadioGroup) findViewById(R.id.numberPeopleGroup);
        radioGroups[3] = (RadioGroup) findViewById(R.id.friendshipGroup);
        radioGroups[4] = (RadioGroup) findViewById(R.id.environmentGroup);
        radioGroups[5] = (RadioGroup) findViewById(R.id.styleGroup);

        radioButtons[0][0] = (RadioButton) findViewById(R.id.ruleButton1);
        radioButtons[0][1] = (RadioButton) findViewById(R.id.ruleButton2);
        radioButtons[0][2] = (RadioButton) findViewById(R.id.ruleButton3);
        radioButtons[1][0] = (RadioButton) findViewById(R.id.learningButton1);
        radioButtons[1][1] = (RadioButton) findViewById(R.id.learningButton2);
        radioButtons[1][2] = (RadioButton) findViewById(R.id.learningButton3);
        radioButtons[2][0] = (RadioButton) findViewById(R.id.numberPeopleButton1);
        radioButtons[2][1] = (RadioButton) findViewById(R.id.numberPeopleButton2);
        radioButtons[2][2] = (RadioButton) findViewById(R.id.numberPeopleButton3);
        radioButtons[3][0] = (RadioButton) findViewById(R.id.friendshipButton1);
        radioButtons[3][1] = (RadioButton) findViewById(R.id.friendshipButton2);
        radioButtons[3][2] = (RadioButton) findViewById(R.id.friendshipButton3);
        radioButtons[4][0] = (RadioButton) findViewById(R.id.environmentButton1);
        radioButtons[4][1] = (RadioButton) findViewById(R.id.environmentButton2);
        radioButtons[4][2] = (RadioButton) findViewById(R.id.environmentButton3);
        radioButtons[5][0] = (RadioButton) findViewById(R.id.styleButton1);
        radioButtons[5][1] = (RadioButton) findViewById(R.id.styleButton2);
        radioButtons[5][2] = (RadioButton) findViewById(R.id.styleButton3);

        textViews[0] = (TextView) findViewById(R.id.ruleText);
        textViews[1] = (TextView) findViewById(R.id.learningText);
        textViews[2] = (TextView) findViewById(R.id.numberPeopleText);
        textViews[3] = (TextView) findViewById(R.id.friendshipText);
        textViews[4] = (TextView) findViewById(R.id.environmentText);
        textViews[5] = (TextView) findViewById(R.id.styleText);
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
