package playground.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolDemo {

    public static void main(String[] args) throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:demo");

        // DB Connectionは同時に2本まで貸し出す。
        config.setMaximumPoolSize(2);

        // Connectionが空かない場合は1秒で待つのを諦める。
        config.setConnectionTimeout(1000);

        try (HikariDataSource dataSource = new HikariDataSource(config)) {
            var workers = Executors.newFixedThreadPool(3);

            // 3人のworkerが、2本しかないConnectionを借りに行く。
            for (int i = 1; i <= 3; i++) {
                int workerId = i;
                workers.submit(() -> borrow(dataSource, workerId));
            }

            workers.shutdown();
            workers.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    static void borrow(HikariDataSource dataSource, int workerId) {
        try {
            System.out.println("worker " + workerId + " waits");

            // try-with-resourcesを抜けるとConnectionがPoolへ返される。
            try (var connection = dataSource.getConnection()) {
                System.out.println("worker " + workerId + " BORROWED " + connection);

                // DB処理に時間がかかっている状態を簡単に再現する。
                Thread.sleep(700);

                System.out.println("worker " + workerId + " RETURNS");
            }
        } catch (Exception e) {
            System.out.println("worker " + workerId + " failed: " + e.getMessage());
        }
    }
}
