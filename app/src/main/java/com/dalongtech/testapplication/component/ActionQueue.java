package com.dalongtech.testapplication.component;

/**
 * Author:xianglei
 * Date: 2019-12-19 17:05
 * Description:动作队列，比如3个弹框依次显示，可以每个弹框加入队列，不同时显示
 */
public class ActionQueue {

    private PendingPostQueue mActions;
    private boolean mExecuting;

    public ActionQueue() {
        mActions = new PendingPostQueue();
    }

    public void enqueue(ActionExe action) {
        synchronized (this) {
            mActions.enqueue(new Action(action));
            if (!mExecuting) {
                mExecuting = true;
                executingNext();
            }
        }
    }

    public void executingNext() {
        Action action = mActions.poll();
        if (action == null) {
            synchronized (this) {
                // Check again, this time in synchronized
                action = mActions.poll();
                if (action == null) {
                    mExecuting = false;
                    return;
                }
            }
        }
        action.action();
    }

    final class PendingPostQueue {
        private Action head;
        private Action tail;

        synchronized void enqueue(Action pendingPost) {

            if (pendingPost == null) {
                throw new NullPointerException("null cannot be enqueued");
            }
            if (tail != null) {
                tail.next = pendingPost;
                tail = pendingPost;
            } else if (head == null) {
                head = tail = pendingPost;
            } else {
                throw new IllegalStateException("Head present, but no tail");
            }
            notifyAll();
        }

        synchronized Action poll() {
            Action pendingPost = head;
            if (head != null) {
                head = head.next;
                if (head == null) {
                    tail = null;
                }
            }
            return pendingPost;
        }

        public synchronized Action poll(int maxMillisToWait) throws InterruptedException {
            if (head == null) {
                wait(maxMillisToWait);
            }
            return poll();
        }
    }

    class Action {

        Action next;
        ActionExe mActionExe;

        Action(ActionExe actionExe) {
            mActionExe = actionExe;
        }
        void action() {
            mActionExe.action();
        }
    }

    public interface ActionExe {
        void action();
    }
}
