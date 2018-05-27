package dave.countdown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.os.AsyncTask;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int charCount;
    TextView char1;
    TextView char2;
    TextView char3;
    TextView char4;
    TextView char5;
    TextView char6;
    TextView char7;
    TextView char8;
    TextView char9;
    String vowels;
    String consonants;
    Random rand;
    TextView[] textViews;
    boolean timerFlag;
    int statusCode;
    EditText userInput;
    String generatedString;
    String resultString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vowels = "AEIOU";
        consonants = "BCDFGHJKLMNPQRSTVWXZ";

        char1 = findViewById(R.id.char1);
        char2 = findViewById(R.id.char2);
        char3 = findViewById(R.id.char3);
        char4 = findViewById(R.id.char4);
        char5 = findViewById(R.id.char5);
        char6 = findViewById(R.id.char6);
        char7 = findViewById(R.id.char7);
        char8 = findViewById(R.id.char8);
        char9 = findViewById(R.id.char9);

        textViews = new TextView[9];
        textViews[0] = char1;
        textViews[1] = char2;
        textViews[2] = char3;
        textViews[3] = char4;
        textViews[4] = char5;
        textViews[5] = char6;
        textViews[6] = char7;
        textViews[7] = char8;
        textViews[8] = char9;

        rand = new Random();

        charCount = 0;

        generatedString = "";
        timerFlag = false;

        userInput = findViewById(R.id.userInput);
        userInput.setEnabled(false);

        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startAnswerCheck();
                    hideSoftKeyboard();
                    handled = true;
                }
                return handled;
            }
        });
    }


    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void getVowel(View view) {

        // generate pseudo random vowel
        char character = vowels.charAt(rand.nextInt(vowels.length()));

        // add one to the character count
        charCount++;

        addCharacter(character);

    }

    public void getConsonant(View view) {

        // generate pseudo random consonant
        char character = consonants.charAt(rand.nextInt(consonants.length()));

        // add one to the character count
        charCount++;

        addCharacter(character);
    }

    public void addCharacter(char character) {

        String stringChar = String.valueOf(character);
        if (charCount <= 9) {

            // switch.. case statement starts at 1 because the count has 1 added to it on the
            // first click
            switch (charCount) {

                case 1:
                    textViews[0].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;

                case 2:
                    textViews[1].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;

                case 3:
                    textViews[2].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;

                case 4:
                    textViews[3].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 5:
                    textViews[4].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 6:
                    textViews[5].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 7:
                    textViews[6].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 8:
                    textViews[7].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
                case 9:
                    textViews[8].setText(stringChar);
                    generatedString = generatedString + stringChar;
                    break;
            }

            resultString = generatedString;
        } else if (charCount > 9) {
            Toast.makeText(this, "You've picked all your characters!",
                    Toast.LENGTH_SHORT).show();

        }

        if (charCount == 9) {
            userInput.setEnabled(true);
            // start timer only if it has not already started
            if (!timerFlag) {
                timer();
            }

            timerFlag = true;
        }
    }

    public void timer() {

        final TextView timerTextView = findViewById(R.id.timer);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                timerTextView.setText("Seconds remaining: " + millisUntilFinished / 1000);

            }

            public void onFinish() {
                timerTextView.setText("You're out of time!");

                userInput.setEnabled(false);
            }
        }.start();
    }

    public boolean isValid(String word) {

        boolean valid = true;

        word = word.toLowerCase();
        generatedString = generatedString.toLowerCase();

        char[] chars = word.toCharArray();

        Character filler = Character.MIN_VALUE;

        for (char c : chars) {
            int charIndex = generatedString.indexOf(c);

            if (charIndex != -1) {
                char[] generatedChars = generatedString.toCharArray();
                generatedChars[charIndex] = filler;
                generatedString = new String(generatedChars);
            } else {
                valid = false;
            }
        }

        return valid;
    }
    public void startAnswerCheck(View view) {
        String word = userInput.getText().toString();
        if (isValid(word)) {
            new CallbackTask().execute(inflections());
        } else {
            Toast.makeText(this, "Input is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    public void startAnswerCheck() {
        String word = userInput.getText().toString();
        if (isValid(word)) {
            new CallbackTask().execute(inflections());
        } else {
            Toast.makeText(this, "Input is not valid", Toast.LENGTH_SHORT).show();
        }
    }


    private String inflections() {
        final String language = "en";

        EditText userInput = findViewById(R.id.userInput);
        String word = userInput.getText().toString();

        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/inflections/" + language + "/" + word_id;
    }

    public void restart(View view) {

    }


    //in android calling network requests on the main thread forbidden by default
    //create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            final String app_id = "0c92f591";
            final String app_key = "af3bcd697e793cf622c612a0f8cac2f4";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);

                statusCode = urlConnection.getResponseCode();
                Log.d("Status", String.valueOf(statusCode));

                return String.valueOf(statusCode);

            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            useResult(statusCode);
        }


    }

    public void useResult(int status) {
        final TextView resultText = findViewById(R.id.result);
        final String word = userInput.getText().toString();

        switch(status) {

            case 200:

                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                resultText.setText("Congratulations! You found the word '" +
                                        word +
                                        "' from the characters '" +
                                        resultString +
                                        "'. \n\nYou scored " +
                                        String.valueOf(word.length()) +
                                        " points.");
                            }
                        });
                    }
                }.start();

                break;

            case 404:

                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Sorry, that's" +
                                                " not a word.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();

                break;

            default:

                new Thread() {
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Somethings gone " +
                                                "wrong, try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
        }
    }
}

