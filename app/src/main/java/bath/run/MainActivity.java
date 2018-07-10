package bath.run;

import android.app.Activity;
import android.app.Fragment;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import bath.run.database.DatabaseHelper;
import bath.run.database.User;
import bath.run.CalorieFragment;
import bath.run.DistanceFragment;
import bath.run.FormStatePagerAdapter;
import bath.run.HeartRateFragment;
import bath.run.ProfileFragment;
import bath.run.StepCountFragment;


//add  View.OnClickListener to implements list if using onClick switch in the future
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    static final int JOB_ID = 1;
    private static final String TAG = "l";
    public static int dailySteps = 0;
    public static GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;
    GoalCompletion goalCompletion = new GoalCompletion();
    DayOfTheWeek dotw = new DayOfTheWeek();
    DatabaseHelper db = new DatabaseHelper(this);
    private FormStatePagerAdapter mFormStatePagerAdapter;
    private ViewPager mViewPager;
    private ViewPager profileViewPager;
    private ImageView imgMon;
    private ImageView imgTue;
    private ImageView imgWed;
    private ImageView imgThu;
    private ImageView imgFri;
    private ImageView imgSat;
    private ImageView imgSun;

    public static boolean isJobServiceOn(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        boolean hasBeenScheduled = false;

        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JOB_ID) {
                hasBeenScheduled = true;
                break;
            }
        }

        return hasBeenScheduled;
    }

    public static void setSteps() {

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

                                dailySteps = ((int) total);
                                System.out.println("set daily steps " + dailySteps);
                                // Update your UI here
                            } else {
                                // Handle failure
                            }
                        }
                    });
        } else if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: ");

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
        setNavigationListener();
        runDb(); //if db does not exist, creates one.
        //workUserList(); //updates db
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.pullFromDb();
        setDayTextView();
    }

    private void initViews() {
        imgMon = (ImageView) findViewById(R.id.imgMon);
        imgTue = (ImageView) findViewById(R.id.imgTue);
        imgWed = (ImageView) findViewById(R.id.imgWed);
        imgThu = (ImageView) findViewById(R.id.imgThu);
        imgFri = (ImageView) findViewById(R.id.imgFri);
        imgSat = (ImageView) findViewById(R.id.imgSat);
        imgSun = (ImageView) findViewById(R.id.imgSun);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        mViewPager = (ViewPager) findViewById(R.id.container);
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

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
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

    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler)
                getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JOB_ID);
        Log.i(TAG, "cancelJob: Job Cancelled");
    }

    //this creates database if one does not already exist.
    public void runDb() {
        SQLiteDatabase collectionDB = db.getWritableDatabase();
    }

    // public void workUserList() {
    //     db.getUsers();
    //     System.out.println("week = " + User.getWeek());
    //  }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //gets specific item
        int id = item.getItemId();

        switch (id) {
            case R.id.action_newCalorieGoal:
                //do something
                break;

            case R.id.action_newStepGoal:
                goalCompletion.setDailyStepsGoal(goalCompletion.getDailyStepsGoal() * 1.5);
                Toast.makeText(getApplicationContext(), "Your daily goal is being calculated..", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_weeklyStepGoal:
                //do something
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     *   Automatically starts the first fragment in the list below.
     *   Other fragments are called upon button click as apparent in
     *   each fragment class.
     */
    private void setupViewPager(ViewPager viewPager) {
        FormStatePagerAdapter adapter = new FormStatePagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new StepCountFragment(), "Steps");
        adapter.addFragment(new HeartRateFragment(), "Heartrate");
        adapter.addFragment(new DistanceFragment(), "Distance");
        adapter.addFragment(new CalorieFragment(), "Calories");
        viewPager.setAdapter(adapter);
    }

    private void setNavigationListener() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {

                    case R.id.action_home:
                        Log.i(TAG, "onNavigatbonItemSelected: Home");
                        setupViewPager(mViewPager);
                        break;
                    case R.id.action_profile:
                        Log.i(TAG, "onNavigationItemSelected: Profile");
                        setupProfilePager(mViewPager);

                        break;
                    //Todo add cases for remaining nav items.
                }
                return true;
            }
        });
    }

    private void setupProfilePager(ViewPager viewPager) {
        FormStatePagerAdapter adapter = new FormStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "User Profile");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber);
    }

    public void setDayTextView() {
        if (User.isMonday()) {
            imgMon.setImageResource(R.drawable.tickicon);
        } else {
            imgMon.setImageResource(R.drawable.crossicon);
        }
        if (User.isTuesday()) {
            imgTue.setImageResource(R.drawable.tickicon);
        } else {
            imgTue.setImageResource(R.drawable.crossicon);
        }
        if (User.isWednesday()) {
            imgWed.setImageResource(R.drawable.tickicon);
        } else {
            imgWed.setImageResource(R.drawable.crossicon);
        }
        if (User.isThursday()) {
            imgThu.setImageResource(R.drawable.tickicon);
        } else {
            imgThu.setImageResource(R.drawable.crossicon);
        }
        if (User.isFriday()) {
            imgFri.setImageResource(R.drawable.tickicon);
        } else {
            imgFri.setImageResource(R.drawable.crossicon);
        }
        if (User.isSaturday()) {
            imgSat.setImageResource(R.drawable.tickicon);
        } else {
            imgSat.setImageResource(R.drawable.crossicon);
        }
        if (User.isSunday()) {
            imgSun.setImageResource(R.drawable.tickicon);
        } else {
            imgSun.setImageResource(R.drawable.crossicon);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Main Activity", "onPause");
        //when user pauses app, check if daily goal is reached.
        goalCompletion.goalReached(db);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("HistoryAPI", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HistoryAPI", "onConnectionFailed");
    }
}


