package net.njfc.respawnredux.client.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * <p>A promise callback that can be returned from
 * async operations. A promise can be attached with
 * observer handlers, that will be called as soon as
 * the operation is either successful or has failed.
 *
 * <p>Even though a promise implements the {@link Cloneable}
 * interface, the {@link #clone()} method only makes
 * a copy of the observers, thus allowing an individual
 * result to be assigned per promise copy.
 *
 */
public class Promise<R> implements Cloneable, Comparable<Promise<R>> {

    private Set<Consumer<Throwable>> failure_observers;
    private Set<Consumer<R>> success_observers;
    private boolean completed = false;
    private Throwable failure;
    private R result;

    /**
     * Create a new promise
     */
    public Promise() {
        this.success_observers = new HashSet<>();
        this.failure_observers = new HashSet<>();
    }

    /**
     * Create a new auto-execution promise. The given consumer
     * will be called within the constructor, allowing a chained
     * syntax to be used.
     *
     * @param promise This promise
     */
    public Promise(Consumer<Promise<R>> promise) {
        this.success_observers = new HashSet<>();
        this.failure_observers = new HashSet<>();
        promise.accept(this);
    }

    /**
     * Returns true when this promise has been called and it was either
     * successful or a failure.
     *
     * @return boolean
     */
    public boolean isCalled() {
        return completed;
    }

    /**
     * Returns -1 when the result of this promise was a failure,
     * 0 when this promise is still pending, and 1 when the
     * result of this promise was successful.
     *
     * @return -1 (failure), 0 (pending) or 1 (success)
     */
    public int isSuccess() {
        if(!completed) return 0;

        return failure == null ? 1 : -1;
    }

    /**
     * <p>Assign a success handler to this promise. Each promise supports
     * multiple success handlers that get fired at once.
     *
     * <p>When this promise has already been fulfilled and the result was
     * successful, the handler will be called right away.
     *
     * @param handler Success handler
     * @return self
     */
    public Promise<R> then(Consumer<R> handler) {
        if(completed) {
            if(result != null) handler.accept(result);
        } else {
            this.success_observers.add(handler);
        }

        return this;
    }

    /**
     * <p>Create a success observer that pipes the received
     * result down into the specified promise.
     *
     * @param delegate The promise to send the success to
     * @return self
     */
    public Promise<R> pipeThen(Promise<R> delegate) {
        if(completed) {
            if(result != null) delegate.success(result);
        } else {
            this.success_observers.add(delegate::success);
        }

        return this;
    }

    /**
     * <p>Assign a failure handler to this promise. Each promise supports
     * multiple failure handlers that get fired at once.
     *
     * <p>When this promise has already been fulfilled and the result was
     * a failure, the handler will be called right away.
     *
     * @param handler Failure handler
     * @return self
     */
    public Promise<R> error(Consumer<Throwable> handler) {
        if(completed) {
            if(failure != null) handler.accept(failure);
        } else {
            this.failure_observers.add(handler);
        }

        return this;
    }

    /**
     * <p>Create a failure observer that pipes the received
     * exception down into the specified promise.
     *
     * <p>The promise type does not have to be of the same
     * type as this promise
     *
     * @param delegate The promise to send failure to
     * @return self
     */
    public Promise<R> pipeError(Promise delegate) {
        if(completed) {
            if(failure != null) delegate.failure(failure);
        } else {
            this.failure_observers.add(delegate::failure);
        }

        return this;
    }

    /**
     * <p>Create an observer that sends both the success
     * and the failure down into a delegate promise.
     *
     * @param delegate The promise to send both
     * 				   success and failures to
     * @return self
     */
    public Promise<R> pipe(Promise<R> delegate) {
        pipeThen(delegate);
        pipeError(delegate);

        return this;
    }

    /**
     * Successfully fulfill this promise with a result. All success
     * handlers registered with {@link #then(Consumer)} will be
     * called.
     *
     * @param result Result
     */
    public void success(R result) {
        if(isCalled())
            throw new RuntimeException("Promise already fulfilled");

        this.completed = true;
        this.result = result;

        for(Consumer<R> handler : success_observers)
            handler.accept(result);
    }

    /**
     * Successfully fulfill this promise without a result. All success
     * handlers registered with {@link #then(Consumer)} will be
     * called. This method must be called with caution since it's
     * easy to trigger null pointer exceptions.
     */
    public void success() {
        if(isCalled())
            throw new RuntimeException("Promise already fulfilled");

        this.completed = true;

        try {
            for (Consumer<R> handler : success_observers)
                handler.accept(null);
        } catch(NullPointerException ex) {
            throw new RuntimeException("NPE thrown during handling of non-result promise success. " +
                    "Are you sure the null result isn't causing it?", ex);
        }
    }

    /**
     * Fulfill this promise with a failure. All failure
     * handlers registered with {@link #error(Consumer)} will
     * be called.
     *
     * @param error Throwable
     */
    public void failure(Throwable error) {
        if(isCalled())
            throw new RuntimeException("Promise already fulfilled");

        this.completed = true;
        this.failure = error;

        for(Consumer<Throwable> handler : failure_observers) {
            handler.accept(failure);
        }
    }

    /**
     * Make a new promise containing the same observers as this
     * promise.
     *
     * @return Promise
     */
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Promise<R> clone() {
        Promise<R> promise = new Promise<>();

        // Make new sets containing all registered observers
        promise.success_observers = new HashSet<>(this.success_observers);
        promise.failure_observers = new HashSet<>(this.failure_observers);

        return promise;
    }

    /**
     * Compare this promise to another promise, comparing both
     * promises based on their success state.
     *
     * @param other Other promise
     * @return int
     */
    @Override
    public int compareTo(Promise<R> other) {
        return this.isSuccess() - other.isSuccess();
    }

    /**
     * Create a new combined promise that succeeds when all sub promises are completed.
     *
     * @param promises Sub promises
     * @return Promise
     */
    public static <T> Promise<List<Promise<T>>> combine(List<Promise<T>> promises) {
        return new Promise<>(master -> {
            // Initial check
            for(Promise<T> prom : promises) {
                if(prom.isSuccess() == -1) {
                    master.failure(prom.failure);
                    return;
                }
            }

            for(Promise<T> promise : promises) {
                promise
                        .then(t -> combineCheck(master, promises))
                        .error(e -> combineCheck(master, promises));
            }
        });
    }

    /**
     * Create a new combined promise that succeeds when all sub promises are completed.
     *
     * @param promiseArray Array of promises
     * @return Promise
     */
    @SafeVarargs
    public static <T> Promise<List<Promise<T>>> combine(Promise<T>... promiseArray) {
        List<Promise<T>> promises = Arrays.asList(promiseArray);

        return new Promise<>(master -> {
            // Initial check
            for(Promise<T> prom : promises) {
                if(prom.isSuccess() == -1) {
                    master.failure(prom.failure);
                    return;
                }
            }

            for(Promise<T> promise : promises) {
                promise
                        .then(t -> combineCheck(master, promises))
                        .error(e -> combineCheck(master, promises));
            }
        });
    }

    private static <T> void combineCheck(Promise<List<Promise<T>>> master, List<Promise<T>> combined) {
        int success = 0;

        for(Promise<T> prom : combined) {
            if(prom.isSuccess() == -1) {
                if(!master.isCalled()) {
                    master.failure(prom.failure);
                }

                return;
            } else if(prom.isSuccess() == 1) {
                success++;
            }
        }

        if(success == combined.size()) {
            master.success(combined);
        }
    }
}