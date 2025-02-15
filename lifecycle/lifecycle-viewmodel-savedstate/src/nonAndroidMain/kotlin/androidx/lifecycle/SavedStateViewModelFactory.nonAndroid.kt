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

package androidx.lifecycle

import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

/**
 * [androidx.lifecycle.ViewModelProvider.Factory] that can create ViewModels accessing and
 * contributing to a saved state via [SavedStateHandle] received in a constructor. If `defaultArgs`
 * bundle was passed into the constructor, it will provide default values in `SavedStateHandle`.
 */
public actual class SavedStateViewModelFactory public actual constructor() :
    ViewModelProvider.Factory {

    // TODO(b/334076622)
    actual override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T =
        super.create(modelClass, extras)
}
