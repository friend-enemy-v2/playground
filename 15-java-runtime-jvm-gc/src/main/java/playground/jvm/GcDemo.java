package playground.jvm;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GcDemo {

    public static void main(String[] args) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        List<byte[]> kept = new ArrayList<>();

        byte[] temporary = new byte[5_000_000];
        WeakReference<byte[]> weakReference = new WeakReference<>(temporary);

        printHeap(runtime, "temporary created");

        // 強い参照を外す。
        // この瞬間に削除されるのではなく、GCが回収できる候補になる。
        temporary = null;
        System.out.println("strong reference removed: now GC-eligible");

        // Heapを使い、GCが動く状況を観察しやすくする。
        for (int i = 0; i < 8; i++) {
            kept.add(new byte[1_000_000]);
        }

        // GCを依頼するだけで、即時実行や回収は保証されない。
        System.gc();
        Thread.sleep(200);

        printHeap(runtime, "after allocation + GC request");

        System.out.println(
            "weak reference still sees temporary? " + (weakReference.get() != null)
        );
        System.out.println("reachable kept objects: " + kept.size());
    }

    static void printHeap(Runtime runtime, String label) {
        long used = runtime.totalMemory() - runtime.freeMemory();

        System.out.printf(
            "%s: used heap ~= %.1f MB%n",
            label,
            used / 1024.0 / 1024.0
        );
    }
}
