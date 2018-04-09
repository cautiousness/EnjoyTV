/*
 * Copyright 2016, The Android Open Source Project
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

package com.fuj.enjoytv.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActivityUtils {
    private static List<SoftReference<Activity>> activityList = new LinkedList<>();

    private ActivityUtils() {}

    public static void addActivity(Activity activity) {
        activityList.add(new SoftReference<>(activity));
    }


    public static void delActivity(Activity activity) {
        Iterator iterator = activityList.iterator();
        while (iterator.hasNext()) {
            SoftReference<Activity> softReference = (SoftReference<Activity>) iterator.next();
            if(activity.equals(softReference.get())) {
                iterator.remove();
            }
        }
    }

    public static int size() {
        return activityList.size();
    }

    public static void exit() {
        Iterator iterator = activityList.iterator();
        while (iterator.hasNext()) {
            SoftReference<Activity> softReference = (SoftReference<Activity>) iterator.next();
            if(softReference != null && softReference.get() != null) {
                softReference.get().finish();
            }
            iterator.remove();
        }
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /*public static void goLogin() {
        Iterator iterator = activityList.iterator();
        while (iterator.hasNext()) {
            SoftReference<Activity> softReference = (SoftReference<Activity>) iterator.next();
            if (softReference != null && softReference.get().getClass() != LoginActivity.class) {
                softReference.get().finish();
                iterator.remove();
            }
        }
    }*/

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void replaceFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }
}
