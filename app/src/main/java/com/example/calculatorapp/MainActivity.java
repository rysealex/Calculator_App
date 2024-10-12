// Alex Ryse
package com.example.calculatorapp;

// imports
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

// MainActivity class
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TextViews for result and solution
    TextView resultTV, solutionTV;
    // MaterialButtons for buttons C, (, and )
    MaterialButton buttonC, buttonBracketOpen, buttonBracketClose;
    // MaterialButtons for 0, 1, 2, 3, 4, 5, 6, 7, 8, and 9
    MaterialButton button0, button1,button2,button3,button4,button5,button6,button7,button8,button9;
    // MaterialButtons for *, +, -, /, and =
    MaterialButton buttonMul, buttonPlus, buttonSub, buttonDivide, buttonEqual;
    // MaterialButtons for AC, and .
    MaterialButton buttonAC, buttonDot;

    /**
     * onCreate method overridden, initialized essential components of application.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // resultTV and solutionTV initialized from TextViews
        resultTV = findViewById(R.id.result_tv);
        solutionTV = findViewById(R.id.solution_tv);

        // assigns ID to all buttons
        // buttons C, (, and )
        assignID(buttonC, R.id.button_c);
        assignID(buttonBracketOpen, R.id.button_open_bracket);
        assignID(buttonBracketClose, R.id.button_close_bracket);

        // buttons 0, 1, 2, 3, 4, 5, 6, 7, 8, and 9
        assignID(button0, R.id.button_0);
        assignID(button1, R.id.button_1);
        assignID(button2, R.id.button_2);
        assignID(button3, R.id.button_3);
        assignID(button4, R.id.button_4);
        assignID(button5, R.id.button_5);
        assignID(button6, R.id.button_6);
        assignID(button7, R.id.button_7);
        assignID(button8, R.id.button_8);
        assignID(button9, R.id.button_9);

        // buttons *, +, -, /, and =
        assignID(buttonMul, R.id.button_mul);
        assignID(buttonPlus, R.id.button_plus);
        assignID(buttonSub, R.id.button_minus);
        assignID(buttonDivide, R.id.button_divide);
        assignID(buttonEqual, R.id.button_equals);

        // buttons AC and .
        assignID(buttonAC, R.id.button_ac);
        assignID(buttonDot, R.id.button_dot);

    }

    /**
     * assignID method sets ID's to each MaterialButton.
     *
     * @param btn The MaterialButton being assigned id.
     * @param id The integer id that is going to be assigned to btn.
     */
    void assignID(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    /**
     * onClick method implements the OnClickListener in View.
     * The functionality when user clicks each button.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        // MaterialButton clicked from user in view
        MaterialButton button = (MaterialButton) view;
        // initialize buttonText, the text on the current button
        String buttonText = button.getText().toString();
        // initialize dataToCalculate, the math problem to calculate
        String dataToCalculate = solutionTV.getText().toString();

        // checks which button was clicked, four checks: "AC", "C", "=", or anything else
        // check for "AC" button
        if (buttonText.equals("AC")) {
            // clear solutionTV and reset resultTV to 0
            solutionTV.setText("");
            resultTV.setText("0");
            return;
        }

        // check for "=" button
        if (buttonText.equals("=")) {
            // copy solutionTV data to resultTV
            solutionTV.setText(resultTV.getText());
            return;
        }

        // check for "C" button
        if (buttonText.equals("C")) {
            // check if solutionTV data is on last value or empty
            if (solutionTV.length() <= 1) {
                // reset dataToCalculate to empty
                dataToCalculate = "";
            }
            else {
                // remove last value in dataToCalculate
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
            // if none of the above, add current button text to dataToCalculate
        } else {
            dataToCalculate = dataToCalculate + buttonText;
        }

        // replace solutionTV data, replace with newly updated dataToCalculate
        solutionTV.setText(dataToCalculate);

        // initialize finalResult using dataToCalculate data in getResults method
        String finalResult = getResults(dataToCalculate);

        // checks if finalResult does not throw an exception
        if (!finalResult.equals("Error")) {
            // update resultTV with the newly calculated finalResult
            resultTV.setText(finalResult);
        }

    }

    /**
     * getResults method calculates the math problem.
     * Uses functionality from online dependency to perform calculations.
     * Implements a try-catch block to check for exceptions.
     *
     * @param data The math problem that needs to be calculated.
     * @return Returns String, the newly calculated answer.
     */
    String getResults(String data) {
        // return 0 if no data to calculate
        if (data.isEmpty()) {
            return "0";
        }
        // try to calculate the math problem
        try {
            // creates new Context object
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            // creates new Scriptable interface
            Scriptable scriptable = context.initStandardObjects();
            // returns newly calculated answer to the math problem
            return context.evaluateString(scriptable, data, "JavaScript", 1, null).toString();

            // catch the exception
        } catch (Exception e) {
            // return message
            return "Error";
        }
    }

}