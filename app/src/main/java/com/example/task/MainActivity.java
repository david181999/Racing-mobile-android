package com.example.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


    public class MainActivity extends AppCompatActivity {

        private ExtendedFloatingActionButton main_BTN_left;
        private ExtendedFloatingActionButton main_BTN_right;
        private AppCompatImageView[] cars;

        private AppCompatImageView[] hearts;
        private LinearLayout[] lines_container;
        private int currentLineIndex = 1;
        private AppCompatImageView[][] stones;
        private int lives = 3;

        private Handler handler = new Handler();
        private GameManager gameManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            findViews();
            initViews();
            SignalManager.init(this);
            gameManager = new GameManager(9, 3); // 9 rows and 3 columns
            startFallingStones();
        }

        private void startFallingStones() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameManager.updateMatrix();
                    updateUI();
                    checkCollision();
                    handler.postDelayed(this, 500); // Adjust the delay to control the speed of falling stones
                }
            }, 500);
        }

        private void updateUI() {
            int[][] matrix = gameManager.getMatrix();
            for (int row = 0; row < stones.length; row++) {
                for (int col = 0; col < stones[row].length; col++) {
                    stones[row][col].setVisibility(matrix[row][col] == 1 ? View.VISIBLE : View.INVISIBLE);
                }
            }
        }

        private void checkCollision() {
            int carCol = currentLineIndex;
            if (stones[stones.length - 1][carCol].getVisibility() == View.VISIBLE) {
                handleCollision();
            }
        }

        private void handleCollision() {

            SignalManager.getInstance().toast("Crash");
            SignalManager.getInstance().vibrate(500);

            if (lives > 0) {
                hearts[hearts.length - lives].setVisibility(View.INVISIBLE);
                lives--;
            }
            if (lives == 0) {
                gameOver();
            }
        }

        private void gameOver() {
            handler.removeCallbacksAndMessages(null);
            showGameOverDialog();
        }

        private void showGameOverDialog() {
            // Inflate the game over dialog layout
            View dialogView = getLayoutInflater().inflate(R.layout.game_over, null);

            // Create the AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();

            // Show the dialog
            dialog.show();
        }


        private void initViews() {

            main_BTN_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCarLeft();
                }
            });

            main_BTN_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCarRight();
                }
            });

            cars[0].setVisibility(View.INVISIBLE);
            cars[1].setVisibility(View.VISIBLE);
            cars[2].setVisibility(View.INVISIBLE);
        }

        private void moveCarRight() {
            // check middle car
            if (cars[1].getVisibility() == View.VISIBLE) {
                cars[1].setVisibility(View.INVISIBLE);
                cars[2].setVisibility(View.VISIBLE);
                currentLineIndex = 2;
            }
            // check leftest car
            else if (cars[0].getVisibility() == View.VISIBLE){
                cars[0].setVisibility(View.INVISIBLE);
                cars[1].setVisibility(View.VISIBLE);
                currentLineIndex = 1;
            }
            //pass
            updateUI();
        }

        private void moveCarLeft() {
            // check middle car
            if (cars[1].getVisibility() == View.VISIBLE) {
                cars[1].setVisibility(View.INVISIBLE);
                cars[0].setVisibility(View.VISIBLE);
                currentLineIndex = 0;
            }
            // check rightest car
            else if (cars[2].getVisibility() == View.VISIBLE){
                cars[2].setVisibility(View.INVISIBLE);
                cars[1].setVisibility(View.VISIBLE);
                currentLineIndex = 1;
            }
            // pass
            updateUI();
        }

        private void findViews() {
            main_BTN_left = findViewById(R.id.main_BTN_left);
            main_BTN_right = findViewById(R.id.main_BTN_right);
            cars = new AppCompatImageView[]{
                    findViewById(R.id.main_IMG_car1),
                    findViewById(R.id.main_IMG_car2),
                    findViewById(R.id.main_IMG_car3)
            };

            hearts = new AppCompatImageView[]{
                    findViewById(R.id.heart1),
                    findViewById(R.id.heart2),
                    findViewById(R.id.heart3)
            };

            lines_container = new LinearLayout[]{
                    findViewById(R.id.line1),
                    findViewById(R.id.line2),
                    findViewById(R.id.line3)
            };

            stones = new AppCompatImageView[][]{
                    {findViewById(R.id.stone1_1), findViewById(R.id.stone1_2), findViewById(R.id.stone1_3)},
                    {findViewById(R.id.stone2_1), findViewById(R.id.stone2_2), findViewById(R.id.stone2_3)},
                    {findViewById(R.id.stone3_1), findViewById(R.id.stone3_2), findViewById(R.id.stone3_3)},
                    {findViewById(R.id.stone4_1), findViewById(R.id.stone4_2), findViewById(R.id.stone4_3)},
                    {findViewById(R.id.stone5_1), findViewById(R.id.stone5_2), findViewById(R.id.stone5_3)},
                    {findViewById(R.id.stone6_1), findViewById(R.id.stone6_2), findViewById(R.id.stone6_3)},
                    {findViewById(R.id.stone7_1), findViewById(R.id.stone7_2), findViewById(R.id.stone7_3)},
                    {findViewById(R.id.stone8_1), findViewById(R.id.stone8_2), findViewById(R.id.stone8_3)},
                    {findViewById(R.id.stone9_1), findViewById(R.id.stone9_2), findViewById(R.id.stone9_3)}
            };
    }
}
