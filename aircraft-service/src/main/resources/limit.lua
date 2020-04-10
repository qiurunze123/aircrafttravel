local key = KEYS[1] --限流KEY（一秒一个）
local limit = tonumber(ARGV[1]) --限流大小
local duration = ARGV[2] --过期时间，秒



local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then --如果超出限流大小
    return 0
else --请求数+1，并设置2秒过期
    redis.call("INCRBY", key,"1")
    redis.call("expire", key,duration)
end
return 1


--local key = KEYS[1] --限流KEY
--local capacity = tonumber(ARGV[1]) --限流大小
--local duration = ARGV[2] --过期时间，秒
--
--local current = tonumber(redis.call('INCR', key))
--if current > capacity then --如果超出限流大小
--    return 0
--else
--    if current == 1
--    then
--        redis.call("EXPIRE", key, duration)
--    end
--    return tonumber(redis.call("GET", key))
--end