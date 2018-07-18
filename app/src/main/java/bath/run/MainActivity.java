package bath.run;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import bath.run.database.DatabaseHelper;
import bath.run.database.User;
import bath.run.fragments.CalorieFragment;
import bath.run.fragments.DissonanceFormFragment;
import bath.run.fragments.FormStatePagerAdapter;
import bath.run.fragments.Landing_page.WelcomeGoalFragment;
import bath.run.fragments.Landing_page.WelcomeLandingFragment;
import bath.run.fragments.ProfileFragment;
import bath.run.fragments.SettingsFormFragment;
import bath.run.fragments.StepCountFragment;
import bath.run.fragments.Landing_page.WelcomeDissonanceFragment;
import bath.run.model.DayOfTheWeekModel;
import bath.run.model.DissonanceFormModel;
import bath.run.model.GoalCompletion;
import bath.run.model.JobModel;
import bath.run.model.MotivationalMessages;
import bath.run.model.StepsModel;


//add  View.OnClickListener to implements list if using onClick switch in the future
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
        , DissonanceFormFragment.onFormCompletionListener,
        ProfileFragment.onProfileCompleteListener, WelcomeDissonanceFragment.onFormCompletionListener,
        WelcomeGoalFragment.onStepGoalCompletionListener, SettingsFormFragment.onSettingsCompletion, View.OnClickListener {

    private static final String TAG = "Main";
    public static GoogleApiClient mGoogleApiClient;
    public static Context mContext;
    GoalCompletion goalCompletion = new GoalCompletion();
    DayOfTheWeekModel dotw = new DayOfTheWeekModel();
    DatabaseHelper db = new DatabaseHelper(this);
    User user = User.getInstance();
    DissonanceFormModel dissonanceFormModel = DissonanceFormModel.getInstance();
    StepsModel stepsModel = StepsModel.getInstance();
    private FormStatePagerAdapter mFormStatePagerAdapter;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private ViewPager mViewPagerWelcome;
    private ImageView imgMon;
    private ImageView imgTue;
    private ImageView imgWed;
    private ImageView imgThu;
    private ImageView imgFri;
    private ImageView imgSat;
    private ImageView imgSun;
    private ImageView bg;
    private TextView txtStreak;
    private TextView txtBestStreak;
    private TextView txtViewStreakInfo;
    private Button btnStreakBg;
    private Button btnStreak;
    private Button btnBestStreak;
    private Button btnBestStreakBg;

    public static boolean isJobServiceOn(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        boolean hasBeenScheduled = false;

        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JobModel.JOB_ID) {
                hasBeenScheduled = true;
                break;
            }
        }
        return hasBeenScheduled;
    }

    //TODO add third fragment layout to starter screen where user can customise login screen through selecting the type of scenery they desire. I.e. what bg image to use.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();


        mFormStatePagerAdapter = new FormStatePagerAdapter(getSupportFragmentManager());
        setupViewPager(mViewPager);

        setSupportActionBar(toolbar);
        setNavigationListener(); // listens for navigation bar clicks
        db.init(); //if db does not exist, creates one.
        mContext = this;

        btnBestStreakBg.setOnClickListener(this);
        btnStreakBg.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (MotivationalMessages.getRandomNumberInRange(1,2) == 1) {
            bg.setBackgroundResource(R.drawable.image2);
        } else {
            bg.setBackgroundResource(R.drawable.test);
        }

        System.out.println("called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext = this;
        db.pullFromStepGoalDb();
        db.pullFromDb();
        db.pullFromDissonanceDb();
        db.pullFromProfileDb();
        setDayTextView();
        reloadStreak();
        setStreakIcon(user.getStreak(), btnStreak);
        setStreakIcon(user.getBestStreak(), btnBestStreak);
        //Has user visited before? If yes continue, if no open welcome screen and dissonance form.
        if (!dissonanceFormModel.isAnswered()) {
            setupWelcomePager(mViewPagerWelcome);
        }
    }

    // This is reusable in a sense that it is called to assign the icon to the current streak
    // as well as the users best streak.
    public void setStreakIcon(int streak, Button streakBtn) {

        switch (streak) {
            case 0:
            case 1:
            case 2:
                streakBtn.setBackgroundResource(R.drawable.fire);
                break;
            case 3:
            case 4:
                streakBtn.setBackgroundResource(R.drawable.silvermedal);
                break;
            case 5:
            case 6:
                streakBtn.setBackgroundResource(R.drawable.medal);
                break;
            case 7:
            case 8:
                streakBtn.setBackgroundResource(R.drawable.crown);
                break;
            case 9:
            case 10:
                streakBtn.setBackgroundResource(R.drawable.winner);
                break;
            default:
                streakBtn.setBackgroundResource(R.drawable.run);
        }
    }
    public void reloadStreak () {
        txtStreak.setText(String.valueOf(user.getStreak()));
        txtBestStreak.setText(String.valueOf(user.getBestStreak()));
    }
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("HistoryAPI", "onConnected");

        if (!isJobServiceOn(this)) {
            Log.i(TAG, "onConnected: ok");
            scheduleJob();
        } else {
            Log.i(TAG, "onConnected: Job already scheduled, updating ui");
            setSteps();
        }
    }

    private void initViews() {
        imgMon = (ImageView) findViewById(R.id.imgMon);
        imgTue = (ImageView) findViewById(R.id.imgTue);
        imgWed = (ImageView) findViewById(R.id.imgWed);
        imgThu = (ImageView) findViewById(R.id.imgThu);
        imgFri = (ImageView) findViewById(R.id.imgFri);
        imgSat = (ImageView) findViewById(R.id.imgSat);
        imgSun = (ImageView) findViewById(R.id.imgSun);
        bg = (ImageView) findViewById(R.id.imageViewBg);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPagerWelcome = (ViewPager) findViewById(R.id.containerWelcomePage);
        btnStreak = (Button) findViewById(R.id.btnStreak);
        btnStreakBg = (Button) findViewById(R.id.btnStreakBg);
        btnBestStreak = (Button) findViewById(R.id.btnBestStreak);
        btnBestStreakBg = (Button) findViewById(R.id.btnBestStreakBg);
        txtStreak = (TextView) findViewById(R.id.txtStreak);
        txtBestStreak = (TextView) findViewById(R.id.txtBestStreak);
    }

    /*
    User current step method. Data is read from GoogleFit API and converted to int.
     */
    public void setSteps() {
        if (mGoogleApiClient.isConnected()) {
            Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA)
                    .setResultCallback(new ResultCallback<DailyTotalResult>() {
                        @Override
                        public void onResult(@NonNull DailyTotalResult totalResult) {
                            if (totalResult.getStatus().isSuccess()) {
                                DataSet totalSet = totalResult.getTotal();
                                long total = (totalSet == null) || totalSet.isEmpty()
                                        ? 0
                                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                                stepsModel.setDailysteps(((int) total));
                            } else {
                                // Handle failure
                            }
                        }
                    });
        } else if (!mGoogleApiClient.isConnecting()) {//user is not connected to GoogleFit.
            mGoogleApiClient.connect();
        }
    }

    //Create Job.
    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(JobModel.JOB_ID, componentName)
                .setPersisted(true)
                .setPeriodic(JobModel.TIME_BETWEEN_JOBS)
                .build();

        JobScheduler scheduler = (JobScheduler)
                getSystemService(JOB_SCHEDULER_SERVICE);

        int resultCode = scheduler.schedule(info);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.i(TAG, "scheduleJob: Job Scheduled");
        } else {
            Log.i(TAG, "scheduleJob: Job scheduling failed");
        }
    }

    //Cancel Job.
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler)
                getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JobModel.JOB_ID);
        Log.i(TAG, "cancelJob: Job Cancelled");
    }

    /*
    Sets the bottom navigation bar and onclick interactivity from within this Activity.
     */
    private void setNavigationListener() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_home:
                        setupViewPager(mViewPager);
                        break;
                    case R.id.action_profile:
                        setupProfilePager(mViewPager);
                        break;
                    case R.id.action_custom:
                        setupDissonancePager(mViewPager);
                        //Todo add cases for remaining nav items.
                        break;
                    case R.id.action_settings:
                        setupSettingsPager(mViewPager);
                }
                return true;
            }
        });
    }

    /*
    Method assigns ticks/crosses to all seven daily fields based on weekly step goals.
     This method has to be in this controller as it interacts with the UI thread.
     */
    public void setDayTextView() {
        if (user.isMonday()) {
            imgMon.setImageResource(R.drawable.tickicon);
        } else {
            imgMon.setImageResource(R.drawable.crossicon);
        }
        if (user.isTuesday()) {
            imgTue.setImageResource(R.drawable.tickicon);
        } else {
            imgTue.setImageResource(R.drawable.crossicon);
        }
        if (user.isWednesday()) {
            imgWed.setImageResource(R.drawable.tickicon);
        } else {
            imgWed.setImageResource(R.drawable.crossicon);
        }
        if (user.isThursday()) {
            imgThu.setImageResource(R.drawable.tickicon);
        } else {
            imgThu.setImageResource(R.drawable.crossicon);
        }
        if (user.isFriday()) {
            imgFri.setImageResource(R.drawable.tickicon);
        } else {
            imgFri.setImageResource(R.drawable.crossicon);
        }
        if (user.isSaturday()) {
            imgSat.setImageResource(R.drawable.tickicon);
        } else {
            imgSat.setImageResource(R.drawable.crossicon);
        }
        if (user.isSunday()) {
            imgSun.setImageResource(R.drawable.tickicon);
        } else {
            imgSun.setImageResource(R.drawable.crossicon);
        }
    }

    /*
    Method is linked to dissonanceformfragment as an interface.
     */
    public void onFormCompletion() {
        setViewPagerWelcome(2);
        db.updateDissonance();
    }

    public void onDissonanceFormCompletion() {
        setupViewPager(mViewPager);
        db.updateDissonance();
    }

    public void onSettingsComplete() {
        setupViewPager(mViewPager);
        db.updateStepGoal();
    }

    public void onStepGoalFormCompletion() {
        db.updateStepGoal();
        Toast.makeText(this, "Customising your experience...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
 Method is linked to profileformfragment as an interface.
  */
    public void onProfileUpdated() {
        setupViewPager(mViewPager);
        db.updateProfile(0);
        Toast.makeText(this, "Results successfully stored.", Toast.LENGTH_SHORT).show();
    }

    /*
     *   Automatically starts the first fragment in the list below.
     *   Other fragments are called upon button click as apparent in
     *   each fragment class.
     */
    private void setupViewPager(ViewPager viewPager) {
        FormStatePagerAdapter adapter = new FormStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StepCountFragment(), "Steps");
        adapter.addFragment(new CalorieFragment(), "Calorie");
        viewPager.setAdapter(adapter);
    }

    private void setupProfilePager(ViewPager viewPager) {
        FormStatePagerAdapter adapter = new FormStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "User Profile");
        viewPager.setAdapter(adapter);
    }

    private void setupDissonancePager(ViewPager viewPager) {
        FormStatePagerAdapter adapter = new FormStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DissonanceFormFragment(), "Dissonance Form Fragment");
        viewPager.setAdapter(adapter);
    }

    private void setupSettingsPager(ViewPager viewPager) {
        FormStatePagerAdapter adapter = new FormStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SettingsFormFragment(), "Settings Form Fragment");
        viewPager.setAdapter(adapter);
    }

    private void setupWelcomePager(ViewPager viewPager) {
        FormStatePagerAdapter adapter = new FormStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WelcomeLandingFragment(), "First welcome Fragment");
        adapter.addFragment(new WelcomeDissonanceFragment(), "Welcome Dissonance Form Fragment"); //same form to dissonant pager, but different view used.
        adapter.addFragment(new WelcomeGoalFragment(), "Welcome Goal Fragment");
        viewPager.setAdapter(adapter);
    }

    public void setViewPagerWelcome(int fragmentNumber) {
        mViewPagerWelcome.setCurrentItem(fragmentNumber);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Main Activity", "onPause");
        goalCompletion.goalReached(db); //checks if user has met daily goal.
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("HistoryAPI", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HistoryAPI", "onConnectionFailed");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBestStreakBg:
                System.out.println("Clicked best");
                break;
            case R.id.btnStreakBg:
                System.out.println("Clicked normal");
                break;
        }
    }
}


