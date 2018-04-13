package app.jimmy.no;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CircularProgressBar circularProgressBar;
    private Button plus1;
    private LottieAnimationView animationView;
    private TextView progressText,levelChange;
    private int level = 0;
    private float divideBy = 1;
    private float coefficient = 100/divideBy;
    private float total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circularProgressBar = findViewById(R.id.circular_progress);
        progressText = findViewById(R.id.progress_text);
        levelChange = findViewById(R.id.level_change_text);
        plus1 = findViewById(R.id.add);
        animationView = findViewById(R.id.animation_view);
        plus1.setOnClickListener(this);
        circularProgressBar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.circular_progress:{
                circularProgressBar.setProgressWithAnimation(0);
                break;
            }
            case R.id.add:{
                if(total == 0){
                    circularProgressBar.setProgress(0);
                }
                total = total + coefficient;
                if(total >= 100.0f){
                    progressText.setText("100 %");
                    circularProgressBar.setProgressWithAnimation(100);
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                    plus1.setVisibility(View.GONE);
                    levelChange.setVisibility(View.VISIBLE);
                    animationView.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationView.setVisibility(View.GONE);
                            plus1.setVisibility(View.VISIBLE);
                            levelChange.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            animationView.setVisibility(View.GONE);
                            plus1.setVisibility(View.VISIBLE);
                            levelChange.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    total = 0;
                    divideBy = divideBy * 2;
                    coefficient = 100/ divideBy;
                }else{
                    progressText.setText(total+" %");
                    circularProgressBar.setProgressWithAnimation(total);

                }
                break;
            }
        }
    }
}
