package hs_mannheim.bump;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import hs_mannheim.bump.ApplicationActivity;
import hs_mannheim.bump.FirstExperimentActivity;
import hs_mannheim.bump.OptionsActivity;
import hs_mannheim.bump.SecondExperimentActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToFirstExperimentActivity(View view) {
        Intent intent = new Intent(this, FirstExperimentActivity.class);
        startActivity(intent);
    }

    public void goToSecondExperimentActivity(View view) {
        Intent intent = new Intent(this, SecondExperimentActivity.class);
        startActivity(intent);
    }

    public void goToApplicationActivity(View view) {
        Intent intent = new Intent(this, ApplicationActivity.class);
        startActivity(intent);
    }

    public void goToOptions(View view) {
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }
}
