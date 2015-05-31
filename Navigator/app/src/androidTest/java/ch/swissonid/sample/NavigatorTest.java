package ch.swissonid.sample;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import ch.swissonid.navigator.Navigator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class NavigatorTest {

    public static final int BIG_NUMBER = 1000;
    public static final int NOT_THAT_BIG_NUMBER = 300;
    private Navigator mNavigator;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp(){
        mNavigator = new Navigator(
                mActivityRule.getActivity().getSupportFragmentManager(),
                R.id.container);
    }

    @Test
    public void setUpTest(){
        assertThat(mNavigator).isNotNull();
        assertThat(mNavigator.getSize()).isEqualTo(0);
    }

    @Test @UiThreadTest
    public void addOneFragment_shouldIncreaseTheSize(){
        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(1);
    }

    @Test @UiThreadTest
    public void addTwoFragment_shouldIncreaseTheSize(){
        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(1);

        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(2);
    }

    @Test @UiThreadTest
    public void goBack_shouldDecreaseTheSize(){
        mNavigator.goTo(SampleFragment.newInstance());
        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(2);

        mNavigator.goOneBack();
        assertThat(mNavigator.getSize()).isEqualTo(1);
    }

    @Test @UiThreadTest
    public void addTowSameFragment_shouldIncreaseTheBackStack(){
        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(1);

        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(2);
    }

    @Test @UiThreadTest
    public void goToStartFragment_shouldClearTheSize(){
        mNavigator.setRootFragment(SampleFragment.newInstance());
        mNavigator.goTo(SampleFragment.newInstance());
        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(2);
        mNavigator.gotToTheRootFragmentBack();
        assertThat(mNavigator.getSize()).isEqualTo(0);
        assertThat(mNavigator.getActiveFragment()).isNotNull();
    }

    @Test @UiThreadTest
    public void addStartFragment_shouldNotIncreaseTheSize(){
        mNavigator.setRootFragment(SampleFragment.newInstance());
        assertThat(mNavigator.getSize()).isEqualTo(0);
    }

    @Test @UiThreadTest
    public void isEmpty_shouldBehaveLikeExpected(){
        assertThat(mNavigator.isEmpty()).isTrue();

        mNavigator.setRootFragment(SampleFragment.newInstance());
        assertThat(mNavigator.isEmpty()).isTrue();

        mNavigator.goTo(SampleFragment.newInstance());
        assertThat(mNavigator.isEmpty()).isFalse();

        mNavigator.goOneBack();
        assertThat(mNavigator.isEmpty()).isTrue();
    }

    @Test @UiThreadTest
    public void goCrazy_shouldStillBehaveCorrecter(){
        mNavigator.setRootFragment(SampleFragment.newInstance());

        for(int i=0;i< BIG_NUMBER;++i){
            mNavigator.goTo(SampleFragment.newInstance());
        }
        assertThat(mNavigator.getSize()).isEqualTo(BIG_NUMBER);

        for(int i=0;i< NOT_THAT_BIG_NUMBER;++i){
            mNavigator.goOneBack();
        }
        assertThat(mNavigator.getSize()).isEqualTo(BIG_NUMBER-NOT_THAT_BIG_NUMBER);

        mNavigator.gotToTheRootFragmentBack();
        assertThat(mNavigator.getSize()).isEqualTo(0);
        assertThat(mNavigator.getActiveFragment()).isNotNull();

    }

}
