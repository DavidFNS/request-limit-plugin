package uz.devops.ratelimit.domain;

import io.github.bucket4j.Bucket;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

//@RedisHash(value = "LimitBucket", timeToLive = 60 * 60 * 24 * 365)
@AllArgsConstructor
@NoArgsConstructor
public class LimitBucketRedis {
    private Long id;
    private Bucket bucket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LimitBucketRedis that = (LimitBucketRedis) o;
        return Objects.equals(id, that.id) && Objects.equals(bucket, that.bucket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bucket);
    }
}
