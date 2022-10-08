package com.gxa.realm;

import com.gxa.entity.User;
import com.gxa.mapper.UserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("-----------------------认证方法-----------------------------");

        UsernamePasswordToken utoken = (UsernamePasswordToken) token;
        String username = utoken.getUsername();

        User user = this.userMapper.queryByUserName(username);
        ByteSource salt = ByteSource.Util.bytes(user.getSalt());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPwd(),salt,this.getName());
        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("-----------------------授权方法-----------------------------");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

//        Set<String> perms = new HashSet<>();
        User user = (User) principalCollection.getPrimaryPrincipal();
        String username = user.getUserName();
//        if ("admin".equals(username)){
//           perms.add("emp:list");
//             perms.add("emp:add");
//             perms.add("emp:update");
//             perms.add("emp:delete");
//
//        }else if ("zs".equals(username)){
//            perms.add("emp:list");
//            perms.add("emp:add");
//            perms.add("emp:update");
//        }else if ("ls".equals(username)){
//            perms.add("emp:list");
//        }
        Set<String> perms = userMapper.queryPermsByUserName(username);
        authorizationInfo.addStringPermissions(perms);
        return authorizationInfo;
    }

    public static void main(String[] args) {
        String hashAlgorithmName = "MD5";
        Object credentials = "admin";
        Object salt = ByteSource.Util.bytes("123");;
        Object result = new SimpleHash(hashAlgorithmName, credentials, salt, 1024);
        System.out.println(result);
    }
}
