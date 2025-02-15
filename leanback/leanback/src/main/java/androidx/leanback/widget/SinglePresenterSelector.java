/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package androidx.leanback.widget;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A {@link PresenterSelector} that always returns the same {@link Presenter}.
 * Useful for rows of items of the same type that are all rendered the same way.
 */
public final class SinglePresenterSelector extends PresenterSelector {

    private final Presenter mPresenter;

    /**
     * @param presenter The Presenter to return for every item.
     */
    public SinglePresenterSelector(@NonNull Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public @Nullable Presenter getPresenter(@Nullable Object item) {
        return mPresenter;
    }

    @Override
    public Presenter @NonNull [] getPresenters() {
        return new Presenter[]{mPresenter};
    }
}
