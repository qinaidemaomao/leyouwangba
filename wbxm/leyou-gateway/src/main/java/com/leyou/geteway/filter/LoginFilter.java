package com.leyou.geteway.filter;

import com.leyou.common.utils.JwtUtils;
import com.leyou.geteway.config.Filterproperties;
import com.leyou.geteway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import cpm.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-26 18:28
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, Filterproperties.class})
public class LoginFilter  extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private Filterproperties filterproperties;


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    /**
     * 是否执行Run方法
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //获取白名单
        List<String> allowPaths=this.filterproperties.getAllowPaths();

        //初始化运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //获取请求的路径
        String url = request.getRequestURI();
        //遍历白名单
        for (String allowPath : allowPaths) {
            if (StringUtils.contains(url,allowPath)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //初始化运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();

        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        if (StringUtils.isBlank(token)){
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }
        try {
            JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
