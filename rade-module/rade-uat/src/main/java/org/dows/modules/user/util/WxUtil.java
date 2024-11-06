//package org.dows.modules.user.util;
//
//import cn.hutool.core.lang.Dict;
//import cn.hutool.core.util.ObjUtil;
//import cn.hutool.core.util.RandomUtil;
//import com.mybatisflex.core.query.QueryWrapper;
//import org.dows.core.enums.UserTypeEnum;
//import org.dows.modules.user.entity.UserInfoEntity;
//import org.dows.modules.user.entity.UserWxEntity;
//
///**
// * @description: </br>
// * @author: lait.zhang@gmail.com
// * @date: 11/6/2024 4:43 PM
// * @history: </br>
// * <author>      <time>      <version>    <desc>
// * 修改人姓名      修改时间        版本号       描述
// */
//public class WxUtil {
//
//
//    public Object wxLoginToken(UserWxEntity userWxEntity) {
//        String unionId = ObjUtil.isNotEmpty(userWxEntity.getUnionid()) ? userWxEntity.getUnionid()
//                : userWxEntity.getOpenid();
//        UserInfoEntity userInfoEntity = userInfoService.getOne(QueryWrapper.create().eq(UserInfoEntity::getUnionid, unionId));
//        if (ObjUtil.isEmpty(userInfoEntity)) {
//            userInfoEntity = new UserInfoEntity();
//            userInfoEntity.setNickName(ObjUtil.isNotEmpty(userWxEntity.getNickName()) ? userWxEntity.getNickName() : generateRandomNickname());
//            userInfoEntity.setGender(userWxEntity.getGender());
//            userInfoEntity.setAvatarUrl(userWxEntity.getAvatarUrl());
//            userInfoEntity.setUnionid(unionId);
//            userInfoEntity.save();
//        }
//        return generateToken(userInfoEntity, null);
//    }
//
//    /**
//     * 前置已校验用户的手机号，
//     * 根据手机号找到用户生成token
//     */
//    public Object generateTokenByPhone(String phone) {
//        UserInfoEntity userInfoEntity = userInfoService.getOne(
//                QueryWrapper.create().eq(UserInfoEntity::getPhone, phone));
//        if (ObjUtil.isEmpty(userInfoEntity)) {
//            userInfoEntity = new UserInfoEntity();
//            userInfoEntity.setPhone(phone);
//            // 生成随机昵称
//            userInfoEntity.setNickName(generateRandomNickname());
//            userInfoEntity.save();
//        }
//        return generateToken(userInfoEntity, null);
//    }
//
//    /**
//     * @return 生成的昵称
//     */
//    public String generateRandomNickname() {
//        // 定义昵称的长度
//        int length = 8;
//        // 生成随机字符串
//        return RandomUtil.randomString(length);
//    }
//
//    /**
//     * 生成token
//     */
//    public Dict generateToken(Long userId, String refreshToken) {
//        UserInfoEntity userInfoEntity = userInfoService.getById(userId);
//        return generateToken(userInfoEntity, refreshToken);
//    }
//
//    public Dict generateToken(UserInfoEntity userInfoEntity, String refreshToken) {
//        Dict tokenInfo = Dict.create()
//                .set("userType", UserTypeEnum.APP.name())
//                .set("userId", userInfoEntity.getId());
//        String token = jwtTokenUtil.generateToken(tokenInfo);
//        if (ObjUtil.isEmpty(refreshToken)) {
//            refreshToken = jwtTokenUtil.generateRefreshToken(tokenInfo);
//        }
//        JwtUser jwtUser = new JwtUser(userInfoEntity.getId(), authority, ObjUtil.equals(userInfoEntity.getStatus(), 1));
//        radeCache.set("app:userDetails:" + jwtUser.getUserId(), jwtUser);
//        return Dict.create()
//                .set("token", token)
//                .set("expire", jwtTokenUtil.getExpire())
//                .set("refreshToken", refreshToken)
//                .set("refreshExpire", jwtTokenUtil.getRefreshExpire());
//    }
//}
//
