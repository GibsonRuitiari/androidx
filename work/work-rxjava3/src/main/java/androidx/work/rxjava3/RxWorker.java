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

package androidx.work.rxjava3;

import static androidx.concurrent.futures.CallbackToFutureAdapter.getFuture;

import android.content.Context;

import androidx.annotation.MainThread;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.ListenableWorker;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.SynchronousExecutor;

import com.google.common.util.concurrent.ListenableFuture;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import org.jspecify.annotations.NonNull;

import java.util.concurrent.Executor;

/**
 * RxJava3 interoperability Worker implementation.
 * <p>
 * When invoked by the {@link WorkManager}, it will call @{@link #createWork()} to get a
 * {@code Single<Result>} subscribe to it.
 * <p>
 * By default, RxWorker will subscribe on the thread pool that runs {@link WorkManager}
 * {@link Worker}s. You can change this behavior by overriding {@link #getBackgroundScheduler()}
 * method.
 * <p>
 * An RxWorker is given a maximum of ten minutes to finish its execution and return a
 * {@link androidx.work.ListenableWorker.Result}.  After this time has expired, the worker will be
 * signalled to stop.
 *
 * @see Worker
 */
public abstract class RxWorker extends ListenableWorker {
    @SuppressWarnings("WeakerAccess")
    static final Executor INSTANT_EXECUTOR = new SynchronousExecutor();

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public RxWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @Override
    public final @NonNull ListenableFuture<Result> startWork() {
        return convert(createWork());
    }

    /**
     * Returns the default background scheduler that {@code RxWorker} will use to subscribe.
     * <p>
     * The default implementation returns a Scheduler that uses the {@link Executor} which was
     * provided in {@link WorkManager}'s {@link Configuration} (or the default one it creates).
     * <p>
     * You can override this method to change the Scheduler used by RxWorker to start its
     * subscription. It always observes the result of the {@link Single} in WorkManager's internal
     * thread.
     *
     * @return The default {@link Scheduler}.
     */
    protected @NonNull Scheduler getBackgroundScheduler() {
        return Schedulers.from(getBackgroundExecutor(), true, true);
    }

    /**
     * Override this method to define your actual work and return a {@code Single} of
     * {@link androidx.work.ListenableWorker.Result} which will be subscribed by the
     * {@link WorkManager}.
     * <p>
     * If the returned {@code Single} fails, the worker will be considered as failed.
     * <p>
     * If the {@link RxWorker} is cancelled by the {@link WorkManager} (e.g. due to a constraint
     * change), {@link WorkManager} will dispose the subscription immediately.
     * <p>
     * By default, subscription happens on the shared {@link Worker} pool. You can change it
     * by overriding {@link #getBackgroundScheduler()}.
     * <p>
     * An RxWorker is given a maximum of ten minutes to finish its execution and return a
     * {@link androidx.work.ListenableWorker.Result}.  After this time has expired, the worker will
     * be signalled to stop.
     *
     * @return a {@code Single<Result>} that represents the work.
     */
    @MainThread
    public abstract @NonNull Single<Result> createWork();

    /**
     * Updates the progress for a {@link RxWorker}. This method returns a {@link Completable}
     * similar to the {@link ListenableWorker#setProgressAsync(Data)} API.
     *
     * @param data The progress {@link Data}
     * @return The {@link Completable}
     */
    public final @NonNull Completable setCompletableProgress(@NonNull Data data) {
        return Completable.fromFuture(setProgressAsync(data));
    }

    @Override
    public @NonNull ListenableFuture<ForegroundInfo> getForegroundInfoAsync() {
        return convert(getForegroundInfo());
    }

    /**
     * Return an {@code Single} with an instance of {@link ForegroundInfo} if the
     * {@link WorkRequest} is
     * important to the user.  In this case, WorkManager provides a signal to the OS that the
     * process should be kept alive while this work is executing.
     * <p>
     * Prior to Android S, WorkManager manages and runs a foreground service on your behalf to
     * execute the WorkRequest, showing the notification provided in the {@link ForegroundInfo}.
     * To update this notification subsequently, the application can use
     * {@link android.app.NotificationManager}.
     * <p>
     * Starting in Android S and above, WorkManager manages this WorkRequest using an immediate job.
     *
     * @return A {@link Single} of {@link ForegroundInfo} instance if the WorkRequest
     * is marked immediate. For more information look at
     * {@link WorkRequest.Builder#setExpedited(OutOfQuotaPolicy)}.
     */
    public @NonNull Single<ForegroundInfo> getForegroundInfo() {
        String message =
                "Expedited WorkRequests require a RxWorker to provide an implementation for"
                        + " `getForegroundInfo()`";
        return Single.error(new IllegalStateException(message));
    }

    /**
     * This specifies that the {@link WorkRequest} is long-running or otherwise important.  In
     * this case, WorkManager provides a signal to the OS that the process should be kept alive
     * if possible while this work is executing.
     * <p>
     * Calls to {@code setForegroundAsync} *must* complete before a {@link RxWorker}
     * signals completion by returning a {@link Result}.
     * <p>
     * Under the hood, WorkManager manages and runs a foreground service on your behalf to
     * execute this WorkRequest, showing the notification provided in
     * {@link ForegroundInfo}.
     * <p>
     * Calling {@code setForeground} will fail with an
     * {@link IllegalStateException} when the process is subject to foreground
     * service restrictions. Consider using
     * {@link WorkRequest.Builder#setExpedited(OutOfQuotaPolicy)} and
     * {@link RxWorker#getForegroundInfo()} instead.
     *
     * @param foregroundInfo The {@link ForegroundInfo}
     * @return A {@link Completable} which resolves after the {@link RxWorker}
     * transitions to running in the context of a foreground {@link android.app.Service}.
     */
    public final @NonNull Completable setForeground(@NonNull ForegroundInfo foregroundInfo) {
        return Completable.fromFuture(setForegroundAsync(foregroundInfo));
    }

    private <T> ListenableFuture<T> convert(Single<T> single) {
        return getFuture((completer) -> {
            final Scheduler scheduler = getBackgroundScheduler();
            single.subscribeOn(scheduler)
                    // observe on WM's private thread
                    .observeOn(Schedulers.from(
                            getTaskExecutor().getSerialTaskExecutor(),
                            /* interruptible */true,
                            /* fair */true))
                    .subscribe(new SingleObserver<T>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            completer.addCancellationListener(d::dispose, INSTANT_EXECUTOR);
                        }

                        @Override
                        public void onSuccess(T t) {
                            completer.set(t);
                        }

                        @Override
                        public void onError(Throwable e) {
                            completer.setException(e);
                        }
                    });
            return "converted single to future";
        });
    }
}
