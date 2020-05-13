package com.leyou.auth.clent;

import com.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lizichen
 * @create 2020-04-26 14:41
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
