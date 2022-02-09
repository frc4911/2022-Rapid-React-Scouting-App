package com.example.a2022frcscoutingapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    public static final String FIlE_NAME_KEY = "standard";

    private View lConstraint;

    private EditText eMatchNumber, eScoutID, eTeamNumber;

    private Button bAllianceColor, bMatchPhase, bTarmac,
            bMinusUpperHub, bMinusLowerHub,
            bPlusUpperHub, bPlusLowerHub,
            bLowClimb, bMidClimb,
            bHighClimb, bTraversalClimb,
            bRobotProblem, bFoul, bCard,
            bSave;

    private TextView tNumUpr, tNumLwr;
    
    private static int zMatchNumber, zScoutID, zAllianceColor,
            zTeamNumber,
            zMatchPhase, zTarmac,
            zAutoUpperHub, zAutoLowerHub,
            zUpperHub, zLowerHub,

            zClimb,
            zRobotProblem, zFoul, zCard,
            zSaveCount;

    private static boolean scoringInfoEditable = false;
    private static boolean autoInforEditable = false;
    private static boolean rotationIntoEditable = false;
    private static boolean endgameInfoEditable = false;
    private static boolean saveEnabled = false;
    private static boolean isAuto = false;


    private static final int darkThemeRed = Color.argb(151, 255, 138, 128);
    private static final int darkThemeGreen = Color.argb(151, 204, 255, 144);
    private static final int darkThemeBlue = Color.argb(171, 130, 177, 255);

    private static final int r2gGradient1 = Color.argb(151,255,184,131);
    private static final int r2gGradient2 = Color.argb(151,255,227,135);
    private static final int r2gGradient3 = Color.argb(151,241,255,140);


    private static final int backgroundBlack = Color.argb(200, 0, 0, 0);
    private static final int backgroundBlue = Color.argb(242, 37, 52, 77);

    private static final int secretPurple = Color.argb(254, 179, 136, 255); //?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = sharedPref.getString(FIlE_NAME_KEY, "PLEASE SET FILENAME");
        setTitle(defaultValue);
        setContentView(R.layout.activity_main);

        lConstraint = findViewById(R.id.lConstraint);

        eMatchNumber = findViewById(R.id.eMatchNumber);
        eScoutID = findViewById(R.id.eScoutID);
        eTeamNumber = findViewById(R.id.eTeamNumber);

        bAllianceColor = findViewById(R.id.bAllianceColor);
        bMatchPhase = findViewById(R.id.bMatchPhase);
        bTarmac = findViewById(R.id.bInitiationLine);
        bMinusUpperHub = findViewById(R.id.bMinusUpperHub);
        bMinusLowerHub = findViewById(R.id.bMinusLowerHub);
        bPlusUpperHub = findViewById(R.id.bPlusUpperHub);
        bPlusLowerHub = findViewById(R.id.bPlusLowerHub);
        bLowClimb = findViewById(R.id.bLowCLimb);
        bMidClimb = findViewById(R.id.bMidClimb);
        bHighClimb = findViewById(R.id.bHighClimb);
        bTraversalClimb = findViewById(R.id.bTraversalClimb);
        bRobotProblem = findViewById(R.id.bRobotProblem);
        bFoul = findViewById(R.id.bFoul);
        bCard = findViewById(R.id.bCard);
        bSave = findViewById(R.id.bSave);

        tNumUpr = findViewById(R.id.tNumberUpperHub);
        tNumLwr = findViewById(R.id.tNumberLowerHub);

        zeroAllData();

        setAllElementEditability();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSettings() {
        DialogFragment newFragment = new FileNameDialogFragment();
        newFragment.show(getSupportFragmentManager(), "missiles");
    }

    private static void zeroAllData() {

        zTeamNumber = 0;
        zMatchPhase = 0;
        zTarmac = 0;
        zAutoLowerHub = 0;
        zAutoUpperHub = 0;
        zUpperHub = 0;
        zLowerHub = 0;
        zClimb = 0;
        zRobotProblem = 0;
        zFoul = 0;
        zCard = 0;
        zSaveCount = 0;

    }

    private void setAllElementEditability() {

        setButtonEditable(bTarmac, scoringInfoEditable && autoInforEditable);

        setButtonEditable(bMinusUpperHub, scoringInfoEditable);
        setButtonEditable(bMinusLowerHub, scoringInfoEditable);
        setButtonEditable(bPlusUpperHub, scoringInfoEditable);
        setButtonEditable(bPlusLowerHub, scoringInfoEditable);

        setButtonEditable(bLowClimb, scoringInfoEditable && rotationIntoEditable);
        setButtonEditable(bMidClimb, scoringInfoEditable && rotationIntoEditable);

        setButtonEditable(bHighClimb, scoringInfoEditable && endgameInfoEditable);
        setButtonEditable(bTraversalClimb, scoringInfoEditable && endgameInfoEditable);

        setButtonEditable(bSave, saveEnabled);
    }

    private void setButtonEditable(Button b, Boolean enabled) {

        b.setEnabled(enabled);
        b.setTextColor(enabled ? Color.WHITE : Color.GRAY);
    }

    private void updateScoringPortText() {

        int upperDisplay, lowerDisplay;
        if(isAuto){
            upperDisplay = zAutoUpperHub;
            lowerDisplay = zAutoLowerHub;
        } else {

            upperDisplay = zUpperHub;
            lowerDisplay = zLowerHub;
        }
        tNumUpr.setText("Upper Hub = " + upperDisplay);
        tNumLwr.setText("Lower Hub = " + lowerDisplay);
    }

    private boolean isEmpty(EditText etText) { // a method to check if an edit text is empty

        return etText.getText().toString().trim().length() == 0;

    }


    public void clickAllianceColor(View v){

        zAllianceColor = (zAllianceColor + 1) % 2;
        bAllianceColor.setBackgroundColor( (zAllianceColor == 0) ? darkThemeBlue : darkThemeRed);
        bAllianceColor.setText((zAllianceColor == 0) ? "Blue" : "Red");
    }

    public void clickMatchPhase(View v){

        zMatchPhase++;

        String displayText = "Start Match";
        int displayColor = darkThemeRed;

        if(zMatchPhase > 2) {
            zMatchPhase = 1;
        }

        switch (zMatchPhase){
            case 1:
                displayText = "Autonomous";
                displayColor = darkThemeBlue;
                scoringInfoEditable  = true;
                autoInforEditable    = true;
                rotationIntoEditable = false;
                endgameInfoEditable  = false;
                isAuto = true;
                saveEnabled = false;
                lConstraint.setBackgroundColor(backgroundBlue);
                break;

            case 2:
                displayText = "Tele-Operated";
                displayColor = darkThemeGreen;
                scoringInfoEditable  = true;
                autoInforEditable    = false;
                rotationIntoEditable = true;
                endgameInfoEditable  = true;
                lConstraint.setBackgroundColor(backgroundBlack);
                isAuto = false;
                saveEnabled = true;
                break;

            default:
                scoringInfoEditable  = true;
                autoInforEditable    = true;
                rotationIntoEditable = true;
                endgameInfoEditable  = true;
                saveEnabled = false;

                Toast.makeText(MainActivity.this, "The scouting app crashed. Tell James he screwed up.", Toast.LENGTH_LONG).show();
                break;
        }

        setAllElementEditability();
        updateScoringPortText();
        bMatchPhase.setText(displayText);
        bMatchPhase.setBackgroundColor(displayColor);
    }

    public void clickTarmac(View v){

        zTarmac = (zTarmac + 1) % 2;
        bTarmac.setBackgroundColor( (zTarmac == 0) ? darkThemeRed : darkThemeGreen);
    }

    public void clickPlusUpperHub(View v){

        if(isAuto){
            zAutoUpperHub++;
        } else {
            zUpperHub++;
        }

        updateScoringPortText();
    }
    public void clickPlusLowerHub(View v){

        if(isAuto){
            zAutoLowerHub++;
        } else {
            zLowerHub++;
        }

        updateScoringPortText();
    }

    public void clickMinusUpperHub(View v){

        if(isAuto){
            zAutoUpperHub = Math.max( (zAutoUpperHub - 1), 0);
        } else {
            zUpperHub = Math.max( (zUpperHub - 1), 0);
        }

        updateScoringPortText();
    }

    public void clickMinusLowerHub(View v){

        if(isAuto){
            zAutoLowerHub = Math.max( (zAutoLowerHub - 1), 0);
        } else {
            zLowerHub = Math.max( (zLowerHub - 1), 0);
        }

        updateScoringPortText();
    }

    //level 0 = low and level 3 = traversal
    public void clickClimb(View v) {

        Button[] climbTypes = {bLowClimb, bMidClimb, bHighClimb, bTraversalClimb};
        for (int i = 0; i < climbTypes.length; i++) {

            int buttonName = climbTypes[i].getId();

            if (v.getId() == buttonName) {
                zClimb = i;
                climbTypes[i].setBackgroundColor(darkThemeGreen);
            } else {
                climbTypes[i].setBackgroundColor(darkThemeRed);
            }
        }
    }

    public void clickRobotProblem(View v){

        String[] displayText = {"No Robot Problem", "Broken Parts", "Dead Partially", "Dead All Match"};
        int[] colorGradients = {darkThemeGreen, r2gGradient2, r2gGradient1, darkThemeRed};
        zRobotProblem = (zRobotProblem + 1)%4;
        bRobotProblem.setBackgroundColor(colorGradients[zRobotProblem]);
        bRobotProblem.setText(displayText[zRobotProblem]);
    }

    public void clickFoul(View v){

        String[] displayText = {"No Foul", "1-2 Fouls", "3+ Fouls"};
        int[] colorGradients = {darkThemeGreen, r2gGradient2, darkThemeRed};
        zFoul = (zFoul + 1)%3;
        bFoul.setBackgroundColor(colorGradients[zFoul]);
        bFoul.setText(displayText[zFoul]);
    }

    public void clickCard(View v){

        String[] displayText = {"No Card", "Yellow Card", "Red Card"};
        int[] colorGradients = {darkThemeGreen, r2gGradient2, darkThemeRed};
        zCard = (zCard + 1)%3;
        bCard.setBackgroundColor(colorGradients[zCard]);
        bCard.setText(displayText[zCard]);
    }

    public void clickSave(View v) throws IOException{

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String fileName = sharedPref.getString(FIlE_NAME_KEY, "");

        if (fileName.isEmpty()) {
            Toast.makeText(MainActivity.this, "Error: Please set event and tablet ID.", Toast.LENGTH_LONG).show();

        } else if (isEmpty(eTeamNumber) || isEmpty(eMatchNumber) || isEmpty(eScoutID)){

            Toast.makeText(MainActivity.this, "Error: Match Number, Team Number, and Scout ID cannot be empty.", Toast.LENGTH_LONG).show();
        }  else if(!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(MainActivity.this, "Cannot Write to Internal Storage, Tell Caleb He Screwed Up.", Toast.LENGTH_SHORT).show();
        } else {

            zSaveCount++;
            int[] colorGradients = {darkThemeGreen, r2gGradient3, r2gGradient2, r2gGradient1, darkThemeRed, darkThemeRed};
            int tapsLeft = 5 - zSaveCount;
            bSave.setBackgroundColor(colorGradients[zSaveCount]);
            if (tapsLeft > 1){
                bSave.setText("Press " + tapsLeft + " Times To Save / Reset");
            } else {
                bSave.setText("Press " + tapsLeft + " Time To Save / Reset");
            }

            if (zSaveCount == 5) {

                zMatchNumber = Integer.parseInt(eMatchNumber.getText().toString());
                zScoutID = Integer.parseInt(eScoutID.getText().toString());
                zTeamNumber = Integer.parseInt(eTeamNumber.getText().toString());

                writeFile(fileName);

                zMatchNumber++;
                eMatchNumber.setText(Integer.toString(zMatchNumber));

                scoringInfoEditable = false;
                autoInforEditable = false;
                rotationIntoEditable = false;
                endgameInfoEditable = false;
                saveEnabled = false;
                isAuto = false;

                setAllElementEditability();
                zeroAllData();
                updateScoringPortText();
                eTeamNumber.setText("");
                bMatchPhase.setText("Start Match");
                bTarmac.setBackgroundColor(darkThemeRed);
                bLowClimb.setBackgroundColor(darkThemeRed);
                bMidClimb.setBackgroundColor(darkThemeRed);
                bHighClimb.setBackgroundColor(darkThemeRed);
                bTraversalClimb.setBackgroundColor(darkThemeRed);
                bRobotProblem.setText("No Robot Problem");
                bRobotProblem.setBackgroundColor(darkThemeGreen);
                bFoul.setText("No Foul");
                bFoul.setBackgroundColor(darkThemeGreen);
                bCard.setText("No Card");
                bCard.setBackgroundColor(darkThemeGreen);
                bSave.setText("Press 5 Times to Save / Reset");
                bSave.setBackgroundColor(darkThemeGreen);

                Toast.makeText(MainActivity.this, "Congratulations. File Saved Successfully.", Toast.LENGTH_SHORT).show();

            }

        }

    }

    private void writeFile(String fileName) throws IOException {


        File csvFile = Environment.getExternalStorageDirectory();
        csvFile = new File(csvFile, fileName);

        csvFile.setExecutable(true);
        csvFile.setReadable(true);
        csvFile.setWritable(true);

        BufferedWriter b = new BufferedWriter(new FileWriter(csvFile, true));

        write(b, zMatchNumber);
        write(b, zScoutID);
        write(b, zAllianceColor);
        write(b, zTeamNumber);
        write(b, zTarmac);
        write(b, zAutoUpperHub);
        write(b, zAutoLowerHub);
        write(b, zUpperHub);
        write(b, zLowerHub);
        write(b, zClimb);
        write(b, zRobotProblem);
        write(b, zFoul);
        write(b, zCard);

        b.append("\n");
        b.close();

        MediaScannerConnection.scanFile(this, new String[] {csvFile.toString()}, null, null);
    }

    private boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private void write(BufferedWriter b, int i){
        try {
            b.append(i + ",");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
