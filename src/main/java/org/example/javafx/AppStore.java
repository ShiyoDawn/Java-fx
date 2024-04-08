package org.example.javafx;


import org.example.javafx.controller.DashboardController;
import org.example.javafx.controller.MainFrameController;
import org.example.javafx.pojo.User;
import org.example.javafx.request.JwtResponse;

/**
 * 前端应用全程数据类
 * JwtResponse jwt 客户登录信息
 */
public class AppStore {
    private static JwtResponse jwt;


    private static MainFrameController mainFrameController;

    //未使用SpringSecurity时暂时使用的user
    //使用后将用户数据存入jwt
    private static User user;
    private AppStore(){
    }

    public static JwtResponse getJwt() {
        return jwt;
    }

    public static User getUser() {
        return AppStore.user;
    }

    public static void setUser(User user) {
        AppStore.user = user;
    }

    public static void setJwt(JwtResponse jwt) {
        AppStore.jwt = jwt;
    }

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static void setMainFrameController(MainFrameController mainFrameController) {
        AppStore.mainFrameController = mainFrameController;
    }

    public static String confirmType(User user) {
        Integer type = AppStore.user.getUser_type_id();
        if(type == 1)
            return "管理员";
        else if (type == 2)
            return "教师";
        else return "学生";
    }
}
