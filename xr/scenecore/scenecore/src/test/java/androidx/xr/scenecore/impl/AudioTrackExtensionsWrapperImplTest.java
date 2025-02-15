/*
 * Copyright 2024 The Android Open Source Project
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

package androidx.xr.scenecore.impl;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.media.AudioTrack;

import androidx.xr.extensions.media.PointSourceAttributes;
import androidx.xr.extensions.media.SoundFieldAttributes;
import androidx.xr.extensions.media.SpatializerExtensions;
import androidx.xr.extensions.node.Node;
import androidx.xr.scenecore.JxrPlatformAdapter;
import androidx.xr.scenecore.JxrPlatformAdapter.AudioTrackExtensionsWrapper;
import androidx.xr.scenecore.JxrPlatformAdapter.SpatializerConstants;
import androidx.xr.scenecore.testing.FakeXrExtensions;
import androidx.xr.scenecore.testing.FakeXrExtensions.FakeAudioTrackExtensions;
import androidx.xr.scenecore.testing.FakeXrExtensions.FakeSpatialAudioExtensions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AudioTrackExtensionsWrapperImplTest {

    FakeXrExtensions fakeXrExtensions;
    FakeSpatialAudioExtensions fakeSpatialAudioExtensions;
    FakeAudioTrackExtensions fakeAudioTrackExtensions;

    private EntityManager entityManager;

    @Mock private AudioTrack.Builder builder;

    @Before
    public void setUp() {
        fakeXrExtensions = new FakeXrExtensions();
        fakeSpatialAudioExtensions = fakeXrExtensions.fakeSpatialAudioExtensions;
        fakeAudioTrackExtensions = new FakeAudioTrackExtensions();

        fakeSpatialAudioExtensions.setFakeAudioTrackExtensions(fakeAudioTrackExtensions);

        entityManager = new EntityManager();
    }

    @Test
    public void setPointSourceAttr_callsExtensionsSetPointSourceAttr() {
        Node fakeNode = new FakeXrExtensions().createNode();
        AndroidXrEntity entity = mock(AndroidXrEntity.class);
        when(entity.getNode()).thenReturn(fakeNode);

        JxrPlatformAdapter.PointSourceAttributes expectedRtAttr =
                new JxrPlatformAdapter.PointSourceAttributes(entity);

        AudioTrackExtensionsWrapper wrapper =
                new AudioTrackExtensionsWrapperImpl(fakeAudioTrackExtensions, entityManager);
        AudioTrack.Builder actual = wrapper.setPointSourceAttributes(builder, expectedRtAttr);

        assertThat(actual).isEqualTo(builder);
        assertThat(fakeAudioTrackExtensions.getPointSourceAttributes().getNode())
                .isEqualTo(fakeNode);
    }

    @Test
    public void setSoundFieldAttr_callsExtensionsSetSoundFieldAttr() {
        int expectedAmbisonicOrder = SpatializerExtensions.AMBISONICS_ORDER_THIRD_ORDER;
        JxrPlatformAdapter.SoundFieldAttributes expectedRtAttr =
                new JxrPlatformAdapter.SoundFieldAttributes(
                        JxrPlatformAdapter.SpatializerConstants.AMBISONICS_ORDER_THIRD_ORDER);

        AudioTrackExtensionsWrapper wrapper =
                new AudioTrackExtensionsWrapperImpl(fakeAudioTrackExtensions, entityManager);

        AudioTrack.Builder actual = wrapper.setSoundFieldAttributes(builder, expectedRtAttr);

        assertThat(actual).isEqualTo(builder);
        assertThat(fakeAudioTrackExtensions.getSoundFieldAttributes().getAmbisonicsOrder())
                .isEqualTo(expectedAmbisonicOrder);
    }

    @Test
    public void getPointSourceAttributes_callsExtensionsGetPointSourceAttributes() {
        AudioTrack track = mock(AudioTrack.class);

        Node fakeNode = new FakeXrExtensions().createNode();
        AndroidXrEntity entity = mock(AndroidXrEntity.class);
        when(entity.getNode()).thenReturn(fakeNode);
        entityManager.setEntityForNode(fakeNode, entity);

        fakeAudioTrackExtensions.setPointSourceAttributes(
                new PointSourceAttributes.Builder().setNode(fakeNode).build());

        JxrPlatformAdapter.PointSourceAttributes expectedRtAttr =
                new JxrPlatformAdapter.PointSourceAttributes(entity);
        AudioTrackExtensionsWrapper wrapper =
                new AudioTrackExtensionsWrapperImpl(fakeAudioTrackExtensions, entityManager);

        JxrPlatformAdapter.PointSourceAttributes actual = wrapper.getPointSourceAttributes(track);

        assertThat(actual.getEntity()).isEqualTo(expectedRtAttr.getEntity());
    }

    @Test
    public void getPointSourceAttributes_returnsNullIfNotInExtensions() {
        AudioTrack track = mock(AudioTrack.class);

        AudioTrackExtensionsWrapper wrapper =
                new AudioTrackExtensionsWrapperImpl(fakeAudioTrackExtensions, entityManager);

        JxrPlatformAdapter.PointSourceAttributes actual = wrapper.getPointSourceAttributes(track);

        assertThat(actual).isNull();
    }

    @Test
    public void getSoundFieldAttributes_callsExtensionsGetSoundFieldAttributes() {
        AudioTrack track = mock(AudioTrack.class);

        fakeAudioTrackExtensions.setSoundFieldAttributes(
                new SoundFieldAttributes.Builder()
                        .setAmbisonicsOrder(SpatializerExtensions.AMBISONICS_ORDER_THIRD_ORDER)
                        .build());

        JxrPlatformAdapter.SoundFieldAttributes expectedRtAttr =
                new JxrPlatformAdapter.SoundFieldAttributes(
                        SpatializerConstants.AMBISONICS_ORDER_THIRD_ORDER);
        AudioTrackExtensionsWrapper wrapper =
                new AudioTrackExtensionsWrapperImpl(fakeAudioTrackExtensions, entityManager);

        JxrPlatformAdapter.SoundFieldAttributes actual = wrapper.getSoundFieldAttributes(track);

        assertThat(actual.getAmbisonicsOrder()).isEqualTo(expectedRtAttr.getAmbisonicsOrder());
    }

    @Test
    public void getSoundFieldAttributes_returnsNullIfNotInExtensions() {
        AudioTrack track = mock(AudioTrack.class);

        AudioTrackExtensionsWrapper wrapper =
                new AudioTrackExtensionsWrapperImpl(fakeAudioTrackExtensions, entityManager);

        JxrPlatformAdapter.SoundFieldAttributes actual = wrapper.getSoundFieldAttributes(track);

        assertThat(actual).isNull();
    }

    @Test
    public void getSourceType_returnsFromExtensions() {
        AudioTrack track = mock(AudioTrack.class);

        int expected = SpatializerConstants.SOURCE_TYPE_SOUND_FIELD;

        fakeAudioTrackExtensions.setSourceType(expected);
        AudioTrackExtensionsWrapper wrapper =
                new AudioTrackExtensionsWrapperImpl(fakeAudioTrackExtensions, entityManager);

        int actualSourceType = wrapper.getSpatialSourceType(track);
        assertThat(actualSourceType).isEqualTo(expected);
    }
}
