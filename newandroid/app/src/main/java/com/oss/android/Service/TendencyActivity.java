package com.oss.android.Service;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.oss.android.Model.Setting;
import com.oss.android.R;
import com.oss.android.Service.Http.HttpPost;

import org.json.JSONException;
import org.json.JSONObject;

public class TendencyActivity extends Activity {


    private static final int NUM_OF_GROUP = 6;
    private RadioGroup [] radioGroups;
    private RadioButton[][] radioButtons;
    private Button closeButton;
    private TextView[] textViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tendency);

        radioGroups = new RadioGroup[NUM_OF_GROUP];
        radioButtons = new RadioButton[NUM_OF_GROUP][3];
        textViews = new TextView[NUM_OF_GROUP];
        buttonInit(radioGroups, radioButtons, textViews);

        closeButton = (Button) findViewById(R.id.tendency_btn_sumit);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int index=0;

                JSONObject data_req = new JSONObject();
                try {
                    data_req.accumulate("id", Setting.getUserId());
                    for(int i=0; i<NUM_OF_GROUP; i++){
                        for(int j=0; j<3; j++){
                            if(radioButtons[i][j].isChecked() == true) {
                                index = j;
                            }
                        }
                        data_req.accumulate(String.valueOf(textViews[i].getText()), index);
                    }

                    HttpPost send = new HttpPost(Setting.getUrl()+ "choice/tendency/", data_req);
                    send.start();
                    send.join();
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void buttonInit(RadioGroup[] radioGroups, RadioButton [][] radioButtons, TextView[] textViews){
        radioGroups[0] = (RadioGroup) findViewById(R.id.tendency_radiogroup_rule);
        radioGroups[1] = (RadioGroup) findViewById(R.id.tendency_radiogroup_learning);
        radioGroups[2] = (RadioGroup) findViewById(R.id.tendency_radiogroup_people);
        radioGroups[3] = (RadioGroup) findViewById(R.id.tendency_radiogroup_friendship);
        radioGroups[4] = (RadioGroup) findViewById(R.id.tendency_radiogroup_env);
        radioGroups[5] = (RadioGroup) findViewById(R.id.tendency_radiogroup_style);

        radioButtons[0][0] = (RadioButton) findViewById(R.id.tendency_radiobtn_rule1);
        radioButtons[0][1] = (RadioButton) findViewById(R.id.tendency_radiobtn_rule2);
        radioButtons[0][2] = (RadioButton) findViewById(R.id.tendency_radiobtn_rule3);
        radioButtons[1][0] = (RadioButton) findViewById(R.id.tendency_radiobtn_learning1);
        radioButtons[1][1] = (RadioButton) findViewById(R.id.tendency_radiobtn_learning2);
        radioButtons[1][2] = (RadioButton) findViewById(R.id.tendency_radiobtn_learning3);
        radioButtons[2][0] = (RadioButton) findViewById(R.id.tendency_radiobtn_people1);
        radioButtons[2][1] = (RadioButton) findViewById(R.id.tendency_radiobtn_people2);
        radioButtons[2][2] = (RadioButton) findViewById(R.id.tendency_radiobtn_people2);
        radioButtons[3][0] = (RadioButton) findViewById(R.id.tendency_radiobtn_friendship1);
        radioButtons[3][1] = (RadioButton) findViewById(R.id.tendency_radiobtn_friendship2);
        radioButtons[3][2] = (RadioButton) findViewById(R.id.tendency_radiobtn_friendship3);
        radioButtons[4][0] = (RadioButton) findViewById(R.id.tendency_radiobtn_env1);
        radioButtons[4][1] = (RadioButton) findViewById(R.id.tendency_radiobtn_env2);
        radioButtons[4][2] = (RadioButton) findViewById(R.id.tendency_radiobtn_env3);
        radioButtons[5][0] = (RadioButton) findViewById(R.id.tendency_radiobtn_style1);
        radioButtons[5][1] = (RadioButton) findViewById(R.id.tendency_radiobtn_style2);
        radioButtons[5][2] = (RadioButton) findViewById(R.id.tendency_radiobtn_style3);

        textViews[0] = (TextView) findViewById(R.id.tendency_textview_rule);
        textViews[1] = (TextView) findViewById(R.id.tendency_textview_learning);
        textViews[2] = (TextView) findViewById(R.id.tendency_textview_people);
        textViews[3] = (TextView) findViewById(R.id.tendency_textview_friendship);
        textViews[4] = (TextView) findViewById(R.id.tendency_textview_env);
        textViews[5] = (TextView) findViewById(R.id.tendency_textview_style);
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
