package cn.bb.sourceideamanage.common.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.websocket.SessionException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    /**
     *  创建ShiroFilterFactoryBean
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager
                                                       ){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加Shiro内置过滤器
        /*
                常用过滤器:
                    anon: 无需认证(登录) 可以访问
                    authc: 必须认证才可以访问
                    user: 如果使用rememberMe的功能可以直接访问
                    perms: 该资源必须得到资源权限才可以访问
                    role: 该资源必须得到角色权限才可以访问
         */
        //修改认证失败后跳转页面
       shiroFilterFactoryBean.setLoginUrl("/userC/toLogin");
        //设置拦截路径 和 过滤器  拦截这些请求URL(controller) 如果没有对应的权限就会定向到login页面
        //只能用Link
        Map<String,String> filterMap = new LinkedHashMap<>();

        //登录注册放行
        filterMap.put("/userC/toLogin","anon");
        filterMap.put("/userC/toReg","anon");
        filterMap.put("/loginC/check","anon");
        filterMap.put("/loginC/register","anon");


        //首页放行
        filterMap.put("/indexC/toIndex","anon");
        //团队放行
        filterMap.put("/teamC/toTeam","anon");
        filterMap.put("/teamC/toTeamMsg/**","anon");
        //想法放行
        filterMap.put("/ideaC/toIdea","anon");
        filterMap.put("/ideaC/toIdeaMsg/**","anon");
        filterMap.put("/ideaC/toBrainStorming/**","anon");
        filterMap.put("/ideaC/toIdeaComments/**","anon");
        //项目放行
        filterMap.put("/projectC/toProject","anon");
        filterMap.put("/projectC/toProjectMsg/**","anon");
        //静态资源放行
        filterMap.put("/pages/**","anon");
        filterMap.put("/bower_components/**","anon");
        filterMap.put("/dist/**","anon");
        filterMap.put("/plugins/**","anon");
        filterMap.put("/res/**","anon");
        //退出
        filterMap.put("/loginC/toLogout","logout");
        //错误
        filterMap.put("/loginC/testErr","anon");
        //除了上面的 其他都需要认证
        filterMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 创建DefaultWebSecurityManager
     * @param userRealm
     * @return
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //关联Realm
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    /**
     * 创建Realm
     * @return
     */
    @Bean("userRealm")
    public UserRealm getUserRealm(){
        return new UserRealm();
    }

    /**
     * 配置shiroDialect 用于thymelefa和shiro标签配合使用
     * @return
     */
    @Bean("shiroDialect")
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }


}