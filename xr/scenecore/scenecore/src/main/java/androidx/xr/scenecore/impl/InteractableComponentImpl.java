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

import android.util.Log;

import androidx.xr.scenecore.JxrPlatformAdapter.Entity;
import androidx.xr.scenecore.JxrPlatformAdapter.InputEventListener;
import androidx.xr.scenecore.JxrPlatformAdapter.InteractableComponent;

import java.util.concurrent.Executor;

/** Implementation of [JxrPlatformAdapter.InteractableComponent]. */
class InteractableComponentImpl implements InteractableComponent {
    final InputEventListener consumer;
    final Executor executor;
    Entity entity;

    InteractableComponentImpl(Executor executor, InputEventListener consumer) {
        this.consumer = consumer;
        this.executor = executor;
    }

    @Override
    public boolean onAttach(Entity entity) {
        if (this.entity != null) {
            Log.e("Runtime", "Already attached to entity " + this.entity);
            return false;
        }
        this.entity = entity;
        if (entity instanceof GltfEntityImplSplitEngine) {
            ((GltfEntityImplSplitEngine) entity).setColliderEnabled(true);
        }
        // InputEvent type translation happens here.
        entity.addInputEventListener(executor, consumer);
        return true;
    }

    @Override
    public void onDetach(Entity entity) {
        if (entity instanceof GltfEntityImplSplitEngine) {
            ((GltfEntityImplSplitEngine) entity).setColliderEnabled(false);
        }
        entity.removeInputEventListener(consumer);
        this.entity = null;
    }
}
