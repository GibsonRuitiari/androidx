/*
 * Copyright 2020 The Android Open Source Project
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

package android.support.mediacompat.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

/** Test of {@link androidx.media.MediaSessionManager.RemoteUserInfo} methods. */
@SuppressWarnings("deprecation")
@RunWith(AndroidJUnit4.class)
@SmallTest
public class RemoteUserInfoTest {
    @Test
    public void testConstructor() {
        String testPackageName = "com.media.test";
        int testPid = 1000;
        int testUid = 2000;
        androidx.media.MediaSessionManager.RemoteUserInfo remoteUserInfo =
                new androidx.media.MediaSessionManager.RemoteUserInfo(
                        testPackageName, testPid, testUid);
        assertEquals(testPackageName, remoteUserInfo.getPackageName());
        assertEquals(testPid, remoteUserInfo.getPid());
        assertEquals(testUid, remoteUserInfo.getUid());
    }

    @Test
    public void testConstructor_withNullPackageName_throwsNPE() {
        try {
            androidx.media.MediaSessionManager.RemoteUserInfo remoteUserInfo =
                    new androidx.media.MediaSessionManager.RemoteUserInfo(null, 1000, 2000);
            fail("null package name shouldn't be allowed");
        } catch (NullPointerException e) {
            // expected
        } catch (Exception e) {
            fail("unexpected exception " + e);
        }
    }

    @Test
    public void testConstructor_withEmptyPackageName_throwsIAE() {
        try {
            androidx.media.MediaSessionManager.RemoteUserInfo remoteUserInfo =
                    new androidx.media.MediaSessionManager.RemoteUserInfo("", 1000, 2000);
            fail("empty package name shouldn't be allowed");
        } catch (IllegalArgumentException e) {
            // expected
        } catch (Exception e) {
            fail("unexpected exception " + e);
        }
    }
}
