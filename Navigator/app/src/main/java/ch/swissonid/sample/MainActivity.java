package ch.swissonid.sample;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;

import ch.swissonid.navigator.Navigator;

public class MainActivity extends AppCompatActivity implements SampleFragment.SampleFragmentListener{

    private Navigator mNavigator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigator = new Navigator(getSupportFragmentManager(), R.id.container,
                R.anim.slide_in_left, R.anim.slide_out_right);
        mNavigator.goTo(SampleFragment.newInstance());
    }

    public Navigator getNavigator(){
        return mNavigator;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextClicked() {
        SampleFragment sampleFragment = SampleFragment.newInstance(mNavigator.getSize());
        mNavigator.goTo(sampleFragment);
    }

    @Override
    public void onBackClicked() {
        mNavigator.goOneBack();
    }
}
