package benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;

@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SimpleBenchmark {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        public final long x = System.currentTimeMillis();
    }

    @Benchmark
    @Warmup(iterations = 1, time = 2, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 1, time = 2, timeUnit = TimeUnit.MILLISECONDS)
    public void example(BenchmarkState state, Blackhole blackhole) {
        long result = state.x;
        while (result > 1) {
            result = result / 3;
        }
        blackhole.consume(result);
    }
}