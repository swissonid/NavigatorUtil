
/**
 * Copyright 2015 Patrice Mueller. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.swissonid.navigator;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class Navigator {
    @NonNull
    protected final FragmentManager mFragmentManager;

    private static final int NO_RES_SET = 0;
    private String mRootFragmentTag;

    private int mDefaultInAnim;
    private int mDefaultOutAnim;

    @IdRes
    protected final int mDefaultContainer;

    /**
     * This constructor should be only called once per
     *
     * @param fragmentManager Your FragmentManger
     * @param defaultContainer Your container id where the fragments should be placed
     */
    public Navigator(@NonNull final FragmentManager fragmentManager
                    , @IdRes final int defaultContainer){
        this(fragmentManager, defaultContainer, NO_RES_SET, NO_RES_SET);
    }

    /**
     * This constructor should be only called once per
     *
     * @param fragmentManager Your FragmentManger
     * @param defaultContainer Your container id where the fragments should be placed
     * @param defaultInAnim Your default animation for the "In Animation" it will be added on each
     *                      transaction.
     * @param defaultOutAnim Your default animation for the "Out Animation" it will be added on each
     *                      transaction.
     *
     */
    public Navigator(@NonNull final FragmentManager fragmentManager
                    , @IdRes final int defaultContainer
                    , @AnimRes final int defaultInAnim
                    , @AnimRes final int defaultOutAnim){
        mFragmentManager = fragmentManager;
        mDefaultContainer = defaultContainer;
        mDefaultInAnim = defaultInAnim;
        mDefaultOutAnim = defaultOutAnim;
    }


    /**
     * @return the current active fragment. If no fragment is active it return null.
     */
    public Fragment getActiveFragment() {
        String tag;
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            tag = mRootFragmentTag;
        }else {
            tag = mFragmentManager
                    .getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
        }
        return mFragmentManager.findFragmentByTag(tag);
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) if you have set
     * a default animation it will be added to the transaction.
     *
     * @param fragment The fragment which will be added
     */
    public void goTo(final Fragment fragment) {
        goTo(fragment, mDefaultInAnim, mDefaultOutAnim);
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) with the specific animation.
     * You have to set both animation.
     * @param fragment The fragment which will be added
     * @param inAnim Your custom "In Animation"
     * @param outAnim Your custom "Out Animation"
     */
    public void goTo(final Fragment fragment, @AnimRes int inAnim, @AnimRes int outAnim){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.addToBackStack(getTag(fragment));
        if(inAnim != NO_RES_SET && outAnim != NO_RES_SET){
            transaction.setCustomAnimations(inAnim,outAnim);
        }
        transaction.replace(mDefaultContainer, fragment, getTag(fragment));
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * This is just a helper method which returns the simple name of
     * the fragment.
     * @param fragment That get added to the history (BackStack)
     * @return The simple name of the given fragment
     */
    protected String getTag(final Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    /**
     * Set the new root fragment. If there is any entry in the history (BackStack)
     * it will be deleted.
     *
     * @param rootFragment The new root fragment
     */
    public void setRootFragment(final Fragment rootFragment){
        if(getSize() > 0){
            this.clearHistory();
        }
        mRootFragmentTag = getTag(rootFragment);
        this.replaceFragment(rootFragment);
    }

    /**
     * Replace the current fragment with the given one, without to add it to backstack.
     * So when the users navigates away from the given fragment it will not appaer in
     * the history.
     *
     * @param fragment The new fragment
     */
    private void replaceFragment(final Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(mDefaultContainer, fragment, getTag(fragment))
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Goes one entry back in the history
     */
    public void goOneBack() {
        mFragmentManager.popBackStackImmediate();
    }

    /**
     * @return The current size of the history (BackStack)
     */
    public int getSize() {
        return mFragmentManager.getBackStackEntryCount();
    }

    /**
     * @return True if no Fragment is in the History (BackStack)
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    /**
     * Goes the whole history back until to the first fragment in the history.
     * It would be the same if the user would click so many times the back button until
     * he reach the first fragment of the app.
     */
    public void gotToTheRootFragmentBack() {
        while(getSize() >= 1){
            goOneBack();
        }
    }

    /**
     * Clears the whole history so it will no BackStack entry there any more.
     */
    public void clearHistory() {
        //noinspection StatementWithEmptyBody - it works as designed
        while(mFragmentManager.popBackStackImmediate());
    }
}
