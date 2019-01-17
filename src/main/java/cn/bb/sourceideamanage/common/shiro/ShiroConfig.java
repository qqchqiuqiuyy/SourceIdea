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


    /*
    创建ShiroFilterFactoryBean
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

        //设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/LoginC/toErr");

        //设置退出
       /* Map<String, Filter> map = new HashMap<>();
        map.put("logout",systemLogoutFilter);
        shiroFilterFactoryBean.setFilters(map);*/

      //  filterMap.put("/LoginC/*","anon");
   //     filterMap.put("/BackIndexC/*","anon");
        /* filterMap.put("/toAdd","authc");
        filterMap.put("/toUpdate","authc");*/

        //放行 要写在通配前面
       // filterMap.put("/test","anon");

        //授权过滤器 对这个路径Url进行授权拦截
        //拦截后 shiro会自动跳转到未授权页面 需要设置
        //下面两个是对不同Url 所需要的资源访问权限限制
        /*filterMap.put("/toAdd","perms[user:add]");
        filterMap.put("/toUpdate","perms[user:update]");*/

        //用角色 role
       // filterMap.put("/toAdd","roles[boss]");
      //  filterMap.put("/toUpdate","roles[putong]");



        //通配方法 让某个目录所有页面都实现过滤
       // filterMap.put("/*","authc");

        //修改认证失败后跳转页面
        shiroFilterFactoryBean.setLoginUrl("/UserC/toLogin");


        //设置拦截路径 和 过滤器  拦截这些请求URL(controller) 如果没有对应的权限就会定向到login页面
        Map<String,String> filterMap = new LinkedHashMap<>(); //只能用Link
        filterMap.put("/AdminC/*","roles[Administrator]");
        filterMap.put("/LoginC/*","anon");
        filterMap.put("/IndexC/*","anon");
        filterMap.put("/UserC/toLogin","anon");
        filterMap.put("/UserC/toReg","anon");
        filterMap.put("/UserC/toUsers","anon");
        filterMap.put("/TeamC/toTeam","anon");
        filterMap.put("/IdeaC/upIdeaSupports","authc");
        filterMap.put("/IdeaC/toIdea","anon");
        filterMap.put("/ProjectC/toProject","anon");
        filterMap.put("/TeamC/*","authc");
        filterMap.put("/UserC/*","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }



    /*
    创建DefaultWebSecurityManager
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //关联Realm
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    /*
    创建Realm
     */
    @Bean("userRealm")
    public UserRealm getUserRealm(){
        return new UserRealm();
    }

    /*
    配置shiroDialect 用于thymelefa和shiro标签配合使用
     */
    @Bean("shiroDialect")
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }

    /**
     * 设置退出
     * @return
     */

}