package uz.devops.ratelimit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "limit_bucket_cache")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LimitBucketCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "limit_cache_sequence")
    @SequenceGenerator(name = "limit_cache_sequence", sequenceName = "limit_cache_id_seq", allocationSize = 1)
    private Long id;
    private String apiKey;
    private Long availableTokens;

    public LimitBucketCache(String apiKey, Long availableTokens) {
        this.apiKey = apiKey;
        this.availableTokens = availableTokens;
    }
}
