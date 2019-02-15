package util;

/**
 * 生成redis的key
 * 因为redis的key不可以 随便取
 * 会产生数据覆盖
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String BIZ_LIKE = "LIKE";
    private static final String BIZ_DISLIKE = "DISLIKE";
    private static final String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    public static final String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT +String.valueOf(entityId);
    }

    public static final String getDislikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT +String.valueOf(entityId);
    }

    public static final String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }
}
